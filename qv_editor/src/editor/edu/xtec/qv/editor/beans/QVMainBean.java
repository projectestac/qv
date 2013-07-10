/*
 * QVMainBean.java
 * 
 * Created on 29/gener/2004
 */
package edu.xtec.qv.editor.beans;

import java.io.File;
import java.util.Vector;

import edu.xtec.qv.cp.QVCPDocument;
import edu.xtec.qv.editor.util.FileUtil;
import edu.xtec.qv.editor.util.QTIUtil;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.util.StringUtil;

/**
 * @author sarjona
 */
public class QVMainBean extends QVSpecificBean implements IQVPreferencesBean{
	
	public static final String M_ALREADY_EXISTS = "assessment.error.alreadyExists";
	public static final String M_INCORRECT_NAME = "assessment.error.incorrectName";
	
	private boolean bErrorAddQuadern;
	private boolean bErrorDelQuadern;
	private String sError;

	public QVMainBean(QVBean mainBean){
		super(mainBean);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.QVBean#start()
	 */
	public boolean start() {
		boolean bOk = true;
		try{
			File fFile = new File(mainBean.getUserLocalURL());
			bOk = initUserStore(fFile);
			if (bOk){
				String sAccio = getParameter(ACTION_PARAM);
				if (sAccio!=null && sAccio.trim().length()>0){
					if (sAccio.equalsIgnoreCase(ADD_QUADERN_ACTION_PARAM)){
						// S'ha de crear un quadern
						String sValidName = getValidName(getParameter(NOU_QUADERN_PARAM));
						bErrorAddQuadern = !addQuadern(sValidName);
						setQuadern(sValidName);
						mainBean.redirectResponse(mainBean.getTranslatedPath(JSP_QUADERN));
					}else if (sAccio.equalsIgnoreCase(SET_QUADERN_ACTION_PARAM)){
						String sIdQuadern = getParameter(ID_QUADERN_PARAM);
						bOk = setQuadern(sIdQuadern);
						if (bOk){
							mainBean.redirectResponse(mainBean.getTranslatedPath(JSP_QUADERN));
						}
					}else if (sAccio.equalsIgnoreCase(DEL_QUADERN_ACTION_PARAM)){
						bErrorDelQuadern = !delQuadern(getParameter(ID_QUADERN_PARAM));
						setQuadern(null);
					}else if(sAccio.equalsIgnoreCase(A_SAVE_SETTINGS)){
						updateSettings();
						saveSettings();
					}else if(sAccio.equalsIgnoreCase(EXPORT_QUADERN_ACTION_PARAM)){
						String sQV = getParameter(ID_QUADERN_PARAM);
						QVCPDocument oCP = mainBean.getQVCPDocument(sQV);
						mainBean.saveQVCPDocument(oCP);

						String sURL= mainBean.getSetting("qv.zipServlet")+"path="+mainBean.getUserLocalURL()+"&folder="+getParameter(ID_QUADERN_PARAM)+"&url=web/index.jsp";
						mainBean.redirectResponse(sURL);
					}				
				}
			}
		} catch (Exception e){
			logger.error("EXCEPCIO inicialitzant QVMainBean --> "+e);
			e.printStackTrace();
			bOk = false;
		}
		return bOk;
	}
	
//	***************************	
//	* Espai d'usuari
//	***************************
	private boolean initUserStore(File fFile){
		boolean bOk = true;
		if (!fFile.exists()){
			// L'usuari no ha utilitzat mai el seu espai i per tant s'ha d'inicialitzar
			bOk = FileUtil.createDirectory(fFile);				
			if (bOk){
				logger.info("S'ha inicialitzat correctament l'espai per l'usuari '"+mainBean.getUserId()+"'");
			} else{
				logger.error("No s'ha pogut inicialitzar l'espai per l'usuari '"+mainBean.getUserId()+"'");
			}
		}	
		return bOk;	
	}
	
//	***************************	
//	* Quaderns
//	***************************
	public String[] getQuadernsUser(){
		String[] files = FileUtil.getNotNullDirectories(mainBean.getUserLocalURL());
		return files;
		
	}
	
	/**
	 * Inicialitza el quadern indicat
	 * @param sNomQuadern
	 * @return false si el quadern ja existeix; true si es crea correctament el quadern
	 */
	private boolean addQuadern(String sNomQuadern){
		boolean bOk = false;
		// (isValid(sNomQuadern)){
			File fFile = new File(mainBean.getQuadernLocalDirectory(sNomQuadern));
			if (fFile.exists() && fFile.list()!=null && fFile.list().length>0){
				logger.error("ERROR: el quadern '"+sNomQuadern+"' ja existeix");
				sError = M_ALREADY_EXISTS;
				return false;
			} else{
				// Crear directori pel quadern
				if (!fFile.exists()){
					bOk = FileUtil.createDirectory(fFile);
				}else{
					bOk=true;
				}
				// Crear fitxer xml que contindra el quadern
				if (bOk){
					bOk = QTIUtil.createEmptyQTIFile(mainBean.getQuadernLocalFile(sNomQuadern), sNomQuadern);
				}
			}
			if (bOk){
				logger.info("S'ha creat correctament el quadern '"+sNomQuadern+"' de l'usuari '"+mainBean.getUserId()+"'");
			} else{
				logger.info("No s'ha pogut crear el quadern '"+sNomQuadern+"' a l'usuari '"+mainBean.getUserId()+"'");
			}
		//else{	
		//logger.info("Nom de quadern ('"+sNomQuadern+"') no vàlid");
		//}
		return bOk;
	}
	
	/**
	 * Actualitza el quadern actual
	 * @return true si es modifica correctament el quadern actual; false en cas que 
	 * no es pugui canviar
	 */
	public boolean setQuadern(String sIdQuadern){
		boolean bOk = true;
		// Eliminar parametres que no es fan servir de la sessio
		mainBean.session.removeAttribute(IDENT_FULL_PARAM);
		mainBean.session.removeAttribute(IDENT_PREGUNTA_PARAM);
		mainBean.session.removeAttribute(QTI_OBJECT_SESSION);
		if (sIdQuadern==null){
			mainBean.session.removeAttribute(ID_QUADERN_PARAM);
		}else{
			mainBean.session.setAttribute(ID_QUADERN_PARAM, sIdQuadern);
			//logger.debug("L'usuari '"+mainBean.getUserId()+"' comença l'edició del quadern '"+sIdQuadern+"'");
		}
		if (sIdQuadern!=null && mainBean.getQTIObject()==null){
			logger.error("ERROR actualitzant el quadern actual '"+getParameter(ID_QUADERN_PARAM)+"'");
			bOk = false;
		}
		return bOk;
	}
	
	public boolean isErrorAddQuadern(){
		return bErrorAddQuadern;
	}
	
	public String getError(){
		return sError;
	}
	
	/**
	 * 
	 * @param sNomQuadern
	 * @return false si el quadern a esborrar no existeix; true si s'elimina correctament
	 */
	private boolean delQuadern(String sNomQuadern){
		boolean bOk = false;
		if (sNomQuadern!= null && sNomQuadern.trim().length()>0){
			File fFile = new File(mainBean.getQuadernLocalDirectory(sNomQuadern));
			if (!fFile.exists()){
				logger.error("ERROR: el quadern '"+sNomQuadern+"' no existeix, i per tant no es pot esborrar");
				return false;
			} else{
				// Eliminar directori del quadern i el seu contingut
				bOk = FileUtil.delDirectory(fFile);
			}
			if (bOk){
				logger.info("S'ha esborrat correctament el quadern '"+sNomQuadern+"' de l'usuari '"+mainBean.getUserId()+"'");
			} else{
				logger.error("No s'ha pogut esborrar el quadern '"+sNomQuadern+"' de l'usuari '"+mainBean.getUserId()+"'");
			}
	}
		return bOk;
	}
	
	protected boolean isValid(String sNomQuadern){
		boolean bOk = false;
		if (sNomQuadern!= null){
			// Longitud entre 1 i 8 caracters
			bOk = sNomQuadern.trim().length()>0 && sNomQuadern.trim().length()<=8;
			// No conté espais
			bOk = sNomQuadern.indexOf(" ")<0;
		}
		return bOk;
	}
	
	protected String getValidName(String sNomQuadern){
		String sName = "";
		if (sNomQuadern!= null){
			sName = sNomQuadern.trim();
			sName = sName.replace(' ', '_');
			sName = StringUtil.filter(sName);
			sName = sName.toLowerCase();
		}
		return sName;
	}
	
	public boolean isErrorDelQuadern(){
		return bErrorDelQuadern;
	}
	
	/**
	 * @return true si s'ha produit algun error; false en cas contrari
	 */
	public boolean isError(){
		return isErrorAddQuadern() || isErrorDelQuadern();
	}
	
	
//	***********************************	
//	* Implementacio QVPreferencesBean
//	***********************************
	public void updateSettings(){
		String sLang = getParameter(LANG);
		if (sLang!=null){
			mainBean.setSetting(DEFAULT_LANGUAGE_KEY, sLang);
		}
		String sLookAndFeel = getParameter(P_LOOK_AND_FEEL);
		if (sLookAndFeel!=null){
			mainBean.setSetting(DEFAULT_SKIN_KEY, sLookAndFeel);
		}
		String sAutosave = getParameter(P_AUTOSAVE)!=null?"true":"false";
		if (sAutosave!=null){
			mainBean.setSetting(AUTOSAVE_KEY, sAutosave);
		}
		int iBackup = mainBean.getIntParameter(P_BACKUP, -1);
		if (iBackup>=0){
			mainBean.setSetting(BACKUP_KEY, String.valueOf(iBackup));
		}
		String sEditXML = getParameter(P_EDITXML)!=null?"true":"false";
		if (sEditXML!=null){
			mainBean.setSetting(EDITXML_KEY, sEditXML);
		}
		String sHelp = getParameter(P_HELP)!=null?"true":"false";
		if (sHelp!=null){
			mainBean.setSetting(HELP_KEY, sHelp);
		}
	}
	
	public void saveSettings(){
		mainBean.saveSettings();
	}
	
	public String getLanguage(){
		return mainBean.getLanguage();		
	}
	
	public String getDefaultSkin(){
		return mainBean.getDefaultSkin();		
	}
	
	public Vector getSkins(){
		return mainBean.getSkins();		
	}
	
	public boolean isAutosave(){
		return (mainBean.getSetting(AUTOSAVE_KEY)!=null) && (mainBean.getSetting(AUTOSAVE_KEY).equalsIgnoreCase("true"));
	}

	public int getNumBackups(){
		int iNumBackups = 0;
		if (mainBean.getSetting(BACKUP_KEY)!=null){
			iNumBackups = Integer.parseInt(mainBean.getSetting(BACKUP_KEY));
		}
		return iNumBackups;
	}
	
	public boolean isEditableXML(){
		return mainBean.isEditableXML();
	}

	public boolean showHelp(){
		return mainBean.showHelp();
	}
	
//	********************************************	
//	* Redefinicio del mètode de QVSpecificBean
//	********************************************

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.QVSpecificBean#getBeanObject()
	 */
	public QTIObject getBeanObject(){
		return null;
	}

	public boolean needXMLValidation(){
		return false;
	}
		
}
