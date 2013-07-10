/*
 * QVSpecificBean.java
 * 
 * Created on 11-feb-2004
 */
package edu.xtec.qv.editor.beans;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

import edu.xtec.qv.editor.util.QVBoundedListControl;
import edu.xtec.qv.editor.util.QVClozeResponseListControl;
import edu.xtec.qv.editor.util.QVConstants;
import edu.xtec.qv.editor.util.QVDotResponseListControl;
import edu.xtec.qv.editor.util.QVDragdropPositionListControl;
import edu.xtec.qv.editor.util.QVDragdropResponseListControl;
import edu.xtec.qv.editor.util.QVHotspotListControl;
import edu.xtec.qv.editor.util.QVListControl;
import edu.xtec.qv.editor.util.QVMaterialListControl;
import edu.xtec.qv.editor.util.QVMetadataListControl;
import edu.xtec.qv.editor.util.QVOrderedResponseListControl;
import edu.xtec.qv.editor.util.QVSourceListControl;
import edu.xtec.qv.editor.util.QVTargetListControl;
import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.Section;
import edu.xtec.qv.qti.util.StringUtil;
import edu.xtec.qv.util.QVParseException;
import edu.xtec.qv.util.XMLUtil;

/**
 * @author sarjona
 */
public class QVSpecificBean implements QVConstants, IQVEditorXMLBean{

	protected static Logger logger = Logger.getRootLogger();

	protected QVBean mainBean;
	protected Hashtable hListControl = new Hashtable(); 
	protected Vector vErrorLines = new Vector();
	protected Hashtable hErrors = new Hashtable();

	
	public QVSpecificBean(QVBean mainBean){
		this.mainBean=mainBean;
	}
	
	public boolean init(){
		boolean bOk = true;
		bOk = setUp();
		if (bOk){
			bOk = start();
			if (bOk){
				bOk = tearDown();
			}
		}
		return bOk;
	}
	
	public boolean start(){
		if (!isQVSpecificBean()){
			logger.debug("You must redefine start function");
		}
		return true;	
	}

	protected boolean setUp(){	
		return true;	
	}
	
	protected boolean tearDown(){		
		boolean bOk = true;
		if (mainBean.isLogout()){
			mainBean.logout();
		}else{
			if (mainBean.showEditXML() && needXMLValidation()){
				if (!validateXML()){
					return bOk;
				}
			}
			String sAction = getParameter(ACTION_PARAM);
			/*if ((sAction!=null && sAction.equalsIgnoreCase(SAVE_QUADERN_ACTION_PARAM)) || 
					mainBean.getBooleanSetting(AUTOSAVE_KEY, false)){*/
			if ((sAction!=null && sAction.equalsIgnoreCase(SAVE_QUADERN_ACTION_PARAM))){
				mainBean.saveQuadern();
			}
		}
		return bOk;
	}

	public boolean validateXML(){
		boolean bValidated = false;
		String sXML = getParameter("xmlText");
		//logger.debug("validateXML->"+this.getClass().getName());
		if (sXML==null && getBeanObject()!=null){
			sXML = QTIObjectToXML();
		}
		if (sXML!=null){
			try{
				//String sXMLFiltrat = StringUtil.replaceTagsFromHTMLCode(sXML);
				QTIObject oQTI = XMLToQTIObject(sXML);
				if (oQTI!=null){
					bValidated = true;
				}
/*				Document doc = XMLUtil.getXMLDocument(sXML, mainBean.getParserDriver(), mainBean.getSchemaQTI());
				if (doc!=null){
					Element eXML = doc.getRootElement();
					//logger.debug("Document="+XMLUtil.showElement(eXML));
					Item oNewItem = new Item(eXML);
					if (!oNewItem.getIdent().equalsIgnoreCase(mainBean.getIdPregunta())){
						mainBean.getPregunta().setIdent(oNewItem.getIdent());
					}
					mainBean.getFull().setItem(oNewItem);					
					bValidated = true;
				}*/
			}catch(QVParseException e){
				vErrorLines = e.getErrorLines();
				hErrors = e.getErrors();
			}
		}
		return bValidated;
	}
		
	public String getParameter(String sParam){
		return mainBean.getParameter(sParam);
	}

	public String[] getParameterValues(String sParam){
		return mainBean.getParameterValues(sParam);
	}

	public String getSessionParameter(String sParam){
		return mainBean.getSessionParameter(sParam);
	}
	
	public QVListControl getListControl(){
		String sListType = getParameter(IQVListControlBean.P_LIST_TYPE);
		return getListControl(sListType);
	}
	
	public QVListControl getListControl(String sListType){
		QVListControl oListControl = null;
		if (sListType!=null){
			if (hListControl.containsKey(sListType)){
				oListControl = (QVListControl)hListControl.get(sListType);
			}else{
				if (sListType.equalsIgnoreCase(IQVListControlBean.METADATA_TYPE)){
					oListControl = new QVMetadataListControl(mainBean.request);
				}else if (sListType.equalsIgnoreCase(IQVListControlBean.MATERIAL_TYPE)){
					oListControl = new QVMaterialListControl(mainBean.request);
				}else if (sListType.equalsIgnoreCase(IQVListControlBean.ORDERED_RESPONSE_TYPE)){
					oListControl = new QVOrderedResponseListControl(mainBean.request, mainBean);
				}else if (sListType.equalsIgnoreCase(IQVListControlBean.CLOZE_RESPONSE_TYPE)){
					oListControl = new QVClozeResponseListControl(mainBean.request);
				}else if (sListType.equalsIgnoreCase(IQVListControlBean.TARGET_TYPE)){
					oListControl = new QVTargetListControl(mainBean.request);
				}else if (sListType.equalsIgnoreCase(IQVListControlBean.SOURCE_TYPE)){
					oListControl = new QVSourceListControl(mainBean.request);
				}else if (sListType.equalsIgnoreCase(IQVListControlBean.HOTSPOT_TYPE)){
					oListControl = new QVHotspotListControl(mainBean.request);
				}else if (sListType.equalsIgnoreCase(IQVListControlBean.BOUNDED_TYPE)){
					oListControl = new QVBoundedListControl(mainBean.request);
				}else if (sListType.equalsIgnoreCase(IQVListControlBean.DOT_RESPONSE_TYPE)){
					oListControl = new QVDotResponseListControl(mainBean.request);
				}else if (sListType.equalsIgnoreCase(IQVListControlBean.DRAGDROP_RESPONSE_TYPE)){
					oListControl = new QVDragdropResponseListControl(mainBean.request);
				}else if (sListType.equalsIgnoreCase(IQVListControlBean.DRAGDROP_POSITION_TYPE)){
					oListControl = new QVDragdropPositionListControl(mainBean.request);
				}
				if (oListControl!=null){
					hListControl.put(sListType, oListControl);
				}
			}
		}
		return oListControl;
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
	 * @see edu.xtec.qv.editor.beans.IQVListControlBean#getPreviousObject(java.lang.String)
	 */
	public Object getPreviousObject(String sType, String sIdent) {
		return getListControl(sType).getPreviousObject(getBeanObject(), sIdent);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.IQVListControlBean#getNextObject(java.lang.String)
	 */
	public Object getNextObject(String sType, String sIdent) {
		return getListControl(sType).getNextObject(getBeanObject(), sIdent);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.IQVListControlBean#getCurrentListObject(java.lang.String)
	 */
	public String getCurrentListObject(String sType){
		String sCurrent = getListControl(sType).getCurrentListObject();
		return sCurrent;
	}
	
	/**
	 * @return Objecte principal que es tracta en el Bean específic
	 */
	public QTIObject getBeanObject(){
		if (!isQVSpecificBean()){
			logger.debug("You must redefine getBeanObject() function");
		}
		return null;
	}
	
	public boolean needXMLValidation(){
		return true;
	}
	
//	********************************	
//	* Implementacio QVEditorXMLBean
//	********************************
	
	public int getNumberLines(String sXML){
		return StringUtil.count(sXML, "\n");
	}
	
	public Vector getErrorLines(){
		return vErrorLines;
	}
	
	public boolean isErrorLine(int iLine){
		return hErrors!=null && hErrors.containsKey(new Integer(iLine));
	}
	
	public Hashtable getErrors(){
		return hErrors;
	}
	
	public String getErrors(int iLine){
		String sErrors = "";
		try{
			Enumeration enumErrors = getError(iLine).elements();
			while (enumErrors.hasMoreElements()){
				sErrors += enumErrors.nextElement()+"\n\n";
			}
		}catch (Exception e){
		}
		return sErrors.trim();	
	}

	public Vector getError(int iLine){
		Vector vError = new Vector();
		try{
			if (hErrors!=null && hErrors.containsKey(new Integer(iLine))){
				vError = (Vector)hErrors.get(new Integer(iLine));
			}
		}catch (Exception e){
		}
		return vError;	
	}
	
	public String QTIObjectToXML(){
		String sXML = null;
		if (getBeanObject()!=null){
			if (getBeanObject() instanceof Item){
				sXML = XMLUtil.showElement(getBeanObject().getXML(), false);
			}else if (getBeanObject() instanceof Section){
				sXML = XMLUtil.showElement(((Section)getBeanObject()).getXML(false), false);
			}
		}
		return sXML;
	}
	protected QTIObject XMLToQTIObject(String sXML) throws QVParseException{
		QTIObject oQTI = null;
		if (sXML!=null && getBeanObject()!=null){
			Document doc = XMLUtil.getXMLDocument(sXML, mainBean.getParserDriver(), mainBean.getSchemaQTI());
			if (doc!=null){
				Element eXML = doc.getRootElement();
				//logger.debug("Document="+XMLUtil.showElement(eXML));
				if (getBeanObject() instanceof Item){
					oQTI = new Item(eXML);
					// Actualitzar identificador en cas que s'hagi modificat
					if (!oQTI.getIdent().equalsIgnoreCase(getBeanObject().getIdent())){
						getBeanObject().setIdent(oQTI.getIdent());
					}
					// Actualitzar pregunta
					mainBean.getFull().setItem((Item)oQTI);
				}else if (getBeanObject() instanceof Section){
					oQTI = new Section(eXML);
					//Afegir preguntes al full (perque l'edicio XML dels fulls no mostra l'XML de les preguntes)
					((Section)oQTI).setItems(((Section)getBeanObject()).getItems());
					// Actualitzar identificador en cas que s'hagi modificat
					if (!oQTI.getIdent().equalsIgnoreCase(getBeanObject().getIdent())){
						getBeanObject().setIdent(oQTI.getIdent());
						mainBean.setFull(oQTI.getIdent());
					}
					// Actualitzar full
					mainBean.getQuadern().setSection((Section)oQTI);
				}
				//logger.debug("XMLToQTIObject="+oQTI);
			}
		}
		return oQTI;
	}

	protected boolean isQVSpecificBean(){
		return this.getClass().getName().lastIndexOf("QVSpecificBean")>=0;
	}

	
}
