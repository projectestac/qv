/*
 * QVQuadernBean.java
 * 
 * Created on 02/febrer/2004
 */
package edu.xtec.qv.editor.beans;

import java.util.Vector;

import edu.xtec.eun.LangString;
import edu.xtec.eun.celebrate.Classification;
import edu.xtec.eun.celebrate.CopyrightAndOtherRestrictions;
import edu.xtec.eun.celebrate.Keyword;
import edu.xtec.lom.Nivell;
import edu.xtec.qv.cp.QVCPDocument;
import edu.xtec.qv.editor.util.FileUtil;
import edu.xtec.qv.editor.util.QTIUtil;
import edu.xtec.qv.qti.Assessment;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.Section;

/**
 * @author sarjona
 */
public class QVQuadernBean extends QVSpecificBean 
implements IQVListControlBean, IQVInteractionBean, IQVLOMBean {

	String sErrorUpload;
	Object oObject = null;
	
	public QVQuadernBean(QVBean mainBean){
		super(mainBean);
		mainBean.setFull(null);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.QVSpecificBean#start()
	 */
	public boolean start() {
		boolean bOk = true;
		try{
			if (mainBean.getIdQuadern()==null){
				mainBean.sRedirectPage = mainBean.getTranslatedPath(JSP_INDEX, JSP_INDEX);
				return false;
			}
			String sFitxer = getParameter(NOU_FITXER_PARAM);
			updateQuadern();
			if (mainBean.isMultipartContent()){
				// S'ha de fer un upload d'un fixer
				sErrorUpload = FileUtil.writeUpload(mainBean.request, mainBean.getQuadernLocalDirectory(), mainBean.getRepositoryPath(), mainBean.getEspaiLliure());
				updateFiles();
			} else{
				String sAccio = getParameter(ACTION_PARAM);
				if (sAccio!=null && sAccio.trim().length()>0){
					// ACCIONS Quadern
					if (sAccio.equalsIgnoreCase(DEL_FITXER_ACTION_PARAM)){
						// S'ha d'eliminar un fitxer
						FileUtil.delFile(mainBean.getQuadernLocalDirectory(),getParameter(ID_FITXER_PARAM));
						updateFiles();
					}else if (sAccio.equalsIgnoreCase(SET_QUADERN_ACTION_PARAM)){
						mainBean.setFull(null);
						mainBean.setPregunta(null);
					}else if (sAccio.equalsIgnoreCase(ADD_FULL_ACTION_PARAM)){
						addFull();
						mainBean.redirectResponse(mainBean.getTranslatedPath(JSP_FULL));
					}else if (sAccio.equalsIgnoreCase(SET_FULL_ACTION_PARAM)){
						String sIdFull = mainBean.getSessionParameter(IDENT_FULL_PARAM);
						bOk = mainBean.setFull(sIdFull);
						if (bOk){
							mainBean.redirectResponse(mainBean.getTranslatedPath(JSP_FULL));
						}
					}else if (sAccio.equalsIgnoreCase(IQVLOMBean.SAVE_LOM_PARAM)){
						updateLOM();
					}
					if (getListControl()!=null){
						getListControl().start(getBeanObject());
					}
				} 
			}
		} catch (Exception e){
			logger.error("EXCEPCIO inicialitzant QVQuadernBean --> "+e);
			e.printStackTrace();
			bOk = false;
		}
		return bOk;
	}

//	***************************	
//	* Quadern
//	***************************
	
	protected void updateQuadern(){
		Assessment oQuadern = mainBean.getQuadern();
		// Actualitzar titol
		if (getParameter(TITLE_PARAM)!=null){
			oQuadern.setTitle(getParameter(TITLE_PARAM));
		}
		// Actualitza algoritme de correccio
		String sScoreModel = getParameter(SCOREMODEL_PARAM);
		if (sScoreModel!=null){
			QTIUtil.setScoreModel(oQuadern, sScoreModel);
		}
		// Actualitza intervencions (activar o no)
		String sInteractionswitch = getParameter(P_INTERACTIONSWITCH);
		if (sInteractionswitch!=null && sInteractionswitch.trim().length()>0){
			QTIUtil.setInteractionswitch(oQuadern, sInteractionswitch);
		}
	}
	
	public String getTitle(){
		String sTitle = mainBean.getQuadern().getTitle();
		if (sTitle == null){
			sTitle = "";
		}
		return sTitle;
	}
	
	public String getScoreModel(){
		String sScoreModel = QTIUtil.getScoreModel(mainBean.getQuadern());
		if (sScoreModel==null) {
			sScoreModel = QTIObject.SUM_OF_SCORES;
		}
		return sScoreModel;
	}
	
	
//	***************************	
//	* Full
//	***************************
	
	public void addFull(){
		String sIdent = getParameter(IDENT_FULL_PARAM);
		if (sIdent==null || sIdent.trim().length()==0){
			sIdent = QTIUtil.getRandomIdent();
		}
		String sTitle = getParameter(TITLE_PARAM);
		String sScoreModel = getParameter(SCOREMODEL_PARAM);
		if (sScoreModel==null){
			sScoreModel = QTIObject.SUM_OF_SCORES;
		}
		Section oNouFull = QTIUtil.createSection(sIdent, sTitle, sScoreModel);
		QTIUtil.addFullQuadern(mainBean.getQuadern(), oNouFull);
		mainBean.setFull(sIdent);
	}
	
//	***************************	
//	* Fitxers
//	***************************
	/**
	 * Obte el nom de tots els fitxers que conté el quadern actual
	 */
	public String[] getFitxersQuadern(){
		return getFitxersQuadern(mainBean.getIdQuadern());
	}
	
	/**
	 * Obte el nom de tots els fitxers que conté el quadern actual
	 */
	public String[] getFitxersQuadern(String sNomQuadern){
		String[] files = null;
		try{
			files = FileUtil.getFiles(mainBean.getQuadernLocalDirectory(sNomQuadern));
			//File fFile = new File(mainBean.getQuadernLocalDirectory(sNomQuadern));
			//files = fFile.list();
		} catch (Exception e){
			logger.error("EXCEPCIO obtenint els fitxers del quadern '"+mainBean.getIdQuadern()+"' de l'usuari '"+mainBean.getUserId()+"' --> "+e);
		}
		return files;
		
	}
	
	public boolean isErrorUploading(){
		return getErrorUploading()!=null;
	}

	public String getErrorUploading(){
		return sErrorUpload;
	}
//	************************************	
//	* Implementacio IQVListControlBean
//	************************************

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.IQVListControlBean#getListObjects(java.lang.String)
	 */
	public Vector getListObjects(String sType) {
		return getListControl(sType).getListObjects(getBeanObject());
	}


	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.IQVListControlBean#getObject(java.lang.String)
	 */
	public Object getObject(String sType, String sIdent) {
		return getListControl(sType).getObject(getBeanObject(), sIdent);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.IQVListControlBean#getCurrentListObject(java.lang.String)
	 */
	public String getCurrentListObject(String sType){
		String sCurrent = getListControl(sType).getCurrentListObject();
		return sCurrent;
	}
	
	public QTIObject getBeanObject(){
		return mainBean.getQuadern();
	}
	
	public boolean needXMLValidation(){
		return false;
	}	

//	************************************	
//	* Implementacio IQVInteractionBean
//	************************************

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.IQVInteractionBean#isInteractionswitch()
	 */
	public boolean isInteractionswitch() {
		return QTIUtil.isInteractionswitch(mainBean.getQuadern());
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.IQVInteractionBean#getInteractionText()
	 */
	public String getInteractionText() {
		return "assessment.interactionswitch";
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.IQVInteractionBean#getInteractionTip()
	 */
	public String getInteractionTip() {
		return "assessment.interactionswitch.tip";
	}

//	************************************	
//	* Implementacio IQVLOMBean
//	************************************

	protected void updateFiles(){
		QVCPDocument oCP = mainBean.getQVCPDocument();
		oCP.setFiles();
		mainBean.saveQVCPDocument();
	}
	
	protected void updateLOM(){
		addLOMAuthor(mainBean.getParameter(P_AUTHOR));
		addLOMLanguage(mainBean.getParameter(P_LANGUAGE));
		addLOMTitle(mainBean.getParameter(P_TITLE));
		addLOMDescription(mainBean.getParameter(P_DESCRIPTION));
		addLOMLevel(mainBean.getParameterValues(P_LEVEL));
		addLOMArea(mainBean.getParameter(P_AREA));
		addLOMRights(mainBean.getParameter(P_COPYRIGHT), mainBean.getParameter(P_LICENSE));
		
		mainBean.saveQVCPDocument();
	}
	
	public boolean existsLOM(){
		boolean bLOM = false;
		QVCPDocument oCP = mainBean.getQVCPDocument();
		if (oCP!=null) bLOM = oCP.existsLOM();
		return bLOM;
	}
	
	public String getLOMAuthor(){
		String sAuthor = null;
		QVCPDocument oCP = mainBean.getQVCPDocument();
		if (oCP!=null){
			sAuthor = oCP.getLOMAuthor();
		}		
		
		if (sAuthor==null || sAuthor.trim().length()==0){
			sAuthor = mainBean.getUserId();
		}
		return sAuthor;
	}
	
	public void addLOMAuthor(String sAuthor){
		QVCPDocument oCP = mainBean.getQVCPDocument();
		if (oCP!=null && sAuthor!=null){
			oCP.setLOMAuthor(sAuthor);
		}		
	}

	public String getLOMTitle(){
		String sTitle = null;
		QVCPDocument oCP = mainBean.getQVCPDocument();
		if (oCP!=null){
			sTitle = oCP.getLOMTitle(mainBean.getLanguage());
		}		
		
		if (sTitle==null){
			sTitle = getTitle();
		}
		return sTitle;
	}	
	public void addLOMTitle(String sTitle){
		QVCPDocument oCP = mainBean.getQVCPDocument();
		if (oCP!=null && sTitle!=null){
			oCP.addLOMTitle(sTitle, mainBean.getLanguage());
		}		
	}

	public String getLOMDescription(){
		String sDescription = null;
		QVCPDocument oCP = mainBean.getQVCPDocument();
		if (oCP!=null){
			sDescription = oCP.getLOMDescription(mainBean.getLanguage());
		}		
		
		if (sDescription==null){
			sDescription = "";
		}
		return sDescription;
	}	
	public void addLOMDescription(String sDescription){
		QVCPDocument oCP = mainBean.getQVCPDocument();
		if (oCP!=null && sDescription!=null){
			oCP.addLOMDescription(sDescription, mainBean.getLanguage());
		}		
	}
	public String getLOMLanguage() {
		String sLanguage = null;
		QVCPDocument oCP = mainBean.getQVCPDocument();
		if (oCP!=null){
			sLanguage = oCP.getLOMLanguage();
		}		
		return sLanguage;
	}
	
	public void addLOMLanguage(String sLanguage){
		QVCPDocument oCP = mainBean.getQVCPDocument();
		if (oCP!=null && sLanguage!=null){
			oCP.setLOMLanguage(sLanguage);
		}		
	}

	public Vector getLOMEducationalLevels() {
		Vector vLevels = new Vector();
		QVCPDocument oCP = mainBean.getQVCPDocument();
		if (oCP!=null){
			vLevels = oCP.getLOMEducationalLevels();
		}		
		return vLevels;
	}
	public void addLOMLevel(String[] sLevels){
		QVCPDocument oCP = mainBean.getQVCPDocument();
		if (oCP!=null && sLevels!=null){
			Vector vNivell = new Vector();
			int iEdatMin = 999;
			int iEdatMax = 0;
			oCP.getEducational().deleteContextElements();
			Classification oClass = oCP.getEducationalLevelClassification();
			oClass.deleteKeywordElements();
			for (int i=0;i<sLevels.length;i++){
				int iId = Integer.parseInt(sLevels[i]);
				Nivell oNivell = mainBean.getLOMDatabase().getNivell(iId);
				vNivell.add(oNivell);
				oCP.addLOMLearningContext(oNivell.getContext());
				if (oNivell.getEdatMin()<iEdatMin) iEdatMin=oNivell.getEdatMin();
				if (oNivell.getEdatMax()>iEdatMax) iEdatMax=oNivell.getEdatMax();
				Keyword oKeyword = new Keyword();
				oKeyword.addLangStringElement(new LangString(oNivell.getText("ca")));
				oClass.addKeywordElement(oKeyword);
			}
			oCP.getEducational().setTypicalAgeRange(iEdatMin+"-"+iEdatMax);
		}		
	}
	public Vector getNivells() {
		return mainBean.getLOMDatabase().getNivells();
	}

	public Vector getArees() {
		return mainBean.getLOMDatabase().getArees();
	}
	public String getLOMArea(){
		String sArea = "";
		QVCPDocument oCP = mainBean.getQVCPDocument();
		if (oCP!=null){
			sArea = oCP.getLOMArea();
		}
		return sArea;
	}
	public void addLOMArea(String sArea){
		QVCPDocument oCP = mainBean.getQVCPDocument();
		if (oCP!=null && sArea!=null){
			oCP.addLOMArea(sArea);
		}
	}

	public String getLOMCopyright(){
		String sCopyright = "";
		QVCPDocument oCP = mainBean.getQVCPDocument();
		if (oCP!=null){
			sCopyright = oCP.getLOMCopyright();
		}
		return sCopyright;
	}
	public String getLOMLicense(){
		String sLicense = "";
		QVCPDocument oCP = mainBean.getQVCPDocument();
		if (oCP!=null){
			sLicense = oCP.getLOMLicense();
		}
		if (sLicense==null || sLicense.length()==0){
			sLicense = "<rdf:RDF xmlns=\"http://web.resource.org/cc/\"\n" +
		    "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n" +
			"<License rdf:about=\"http://creativecommons.org/licenses/by-nc-sa/2.0/\">\n" +
			   "   <permits rdf:resource=\"http://web.resource.org/cc/Reproduction\"/>\n" +
			   "   <permits rdf:resource=\"http://web.resource.org/cc/Distribution\"/>\n" +
			   "   <requires rdf:resource=\"http://web.resource.org/cc/Notice\" /> \n" +
			   "   <requires rdf:resource=\"http://web.resource.org/cc/Attribution\" /> \n" +
			   "   <prohibits rdf:resource=\"http://web.resource.org/cc/CommercialUse\" /> \n" +
			   "   <permits rdf:resource=\"http://web.resource.org/cc/DerivativeWorks\" /> \n" +
			   "   <requires rdf:resource=\"http://web.resource.org/cc/ShareAlike\" /> \n" +
			"</License> \n" +
			"</rdf:RDF>";			
		}
		return sLicense;
	}
	public void addLOMRights(String sCopyright, String sLicense){
		QVCPDocument oCP = mainBean.getQVCPDocument();
		if (oCP!=null){
			oCP.setLOMCopyright(sCopyright);
			if (CopyrightAndOtherRestrictions.V_YES.equals(sCopyright)) oCP.setLOMLicense(sLicense);
			else oCP.setLOMLicense("");
		}
	}

	public Vector getIdiomes() {
		return mainBean.getLOMDatabase().getIdiomes(mainBean.getLanguage());
	}

}
