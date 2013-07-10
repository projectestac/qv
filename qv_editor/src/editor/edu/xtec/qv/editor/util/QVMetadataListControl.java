/*
 * QVMetadata.java
 * 
 * Created on 04/juny/2004
 */
package edu.xtec.qv.editor.util;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import edu.xtec.qv.qti.Assessment;
import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.QTIMetadata;
import edu.xtec.qv.qti.QTIMetadatafield;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.Section;

/**
 * @author sarjona
 */
public class QVMetadataListControl extends QVListControl{
	
	public QVMetadataListControl(HttpServletRequest request){
		super(request);
	}

	protected static Logger logger = Logger.getRootLogger();

	public static String P_LABEL = "p_label";
	public static String P_ENTRY = "p_entry";
	
	public static String A_UP = "up";
	public static String A_DOWN = "down";
	
	public Vector getListObjects(QTIObject oQTI){	
		Vector vListObjects = new Vector();
		Vector vMetadata = getQTIMetadata(oQTI);
		Enumeration enumMetadata = vMetadata.elements();
		while (enumMetadata.hasMoreElements()){
			QTIMetadata oMetadata = (QTIMetadata)enumMetadata.nextElement();
			Enumeration enumField = oMetadata.getQTIMetadatafield().elements();
			while (enumField.hasMoreElements()){
				QTIMetadatafield oField = (QTIMetadatafield)enumField.nextElement();
				String sEntry = oField.getFieldEntry()!=null?oField.getFieldEntry():"";
				ListObject o = new ListObject(oField.getFieldLabel(), oField.getFieldLabel()+"="+sEntry);
				vListObjects.addElement(o);
			}
		}
		return vListObjects;
	}
	

	public Object getObject(QTIObject oQTI, String sIdent){
		return getQTIMetadatafield(oQTI, sIdent);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getPreviousObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getPreviousObject(QTIObject oQTI, String sIdent){
		logger.debug("Method getPreviousObject not implemented");
		return null;
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getNextObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getNextObject(QTIObject oQTI, String sIdent){
		logger.debug("Method getPreviousObject not implemented");
		return null;
	}
	
	/**
	 * 
	 * @param oQTI
	 * @param sIdent label of Metadatafield (it identifies MetadataField) 
	 */
	public void addListObject(QTIObject oQTI, String sIdent){
		//logger.debug("addListObject-> "+sIdent);
		QTIMetadatafield oNew = (QTIMetadatafield)getRequestQTIObject();
		if (oQTI!=null && oNew!=null){
			QTIMetadata oMetadata = getQTIMetadata(oQTI, sIdent);
			QTIMetadatafield oMetadatafield = (QTIMetadatafield)oNew;
			if (oMetadata==null){
				Vector vMetadata = getQTIMetadata(oQTI);
				if (vMetadata!=null && !vMetadata.isEmpty()){
					oMetadata=(QTIMetadata)vMetadata.firstElement();
				}else{
					if (oQTI instanceof Assessment){
						oMetadata = new QTIMetadata();
						((Assessment)oQTI).addQTIMetadata(oMetadata);
					}else if (oQTI instanceof Section){
						oMetadata = new QTIMetadata();
						((Section)oQTI).addQTIMetadata(oMetadata);
					}else if (oQTI instanceof Item){
						// TODO: Afegir ItemMetadata a Item
						//ItemMetadata oItemMetadata = new ItemMetadata();
						//oMetadata = new QTIMetadata();
						//oItemMetadata.addQTIMetadata(oMetadata);
						//((Item)oQTI).addItemMetadata(oItemMetadata);
					}
				}
			}
			int iPosition = oMetadata.getQTIMetadatafieldPosition(sIdent);
			if (iPosition>=0) iPosition++;
			oMetadata.addQTIMetadatafield(oMetadatafield, iPosition);
		}
	}
	
	public void delListObject(QTIObject oQTI, String sIdent){
		if (oQTI!=null && sIdent!=null){
			QTIMetadata oMetadata = getQTIMetadata(oQTI, sIdent);
			if (oMetadata!=null){
				oMetadata.delQTIMetadatafield(sIdent);
			}
		}
	}
	
	public void setListObject(QTIObject oQTI, String sIdent){
		QTIMetadatafield oOld = (QTIMetadatafield)getObject(oQTI, sIdent);
		QTIMetadatafield oNew = (QTIMetadatafield)getRequestQTIObject();
		if (oQTI!=null && oOld!=null && oNew!=null){
			QTIMetadata oMetadata = getQTIMetadata(oQTI, sIdent);
			if (oMetadata!=null){
				oMetadata.setQTIMetadatafield(oOld, oNew);
			}
		}
	}
	
	public void upListObject(QTIObject oQTI, String sFieldLabel){
		setMetadataFieldPosition(oQTI, sFieldLabel, A_UP);
	}
	
	public void downListObject(QTIObject oQTI, String sFieldLabel){
		setMetadataFieldPosition(oQTI, sFieldLabel, A_DOWN);
	}

	private static void setMetadataFieldPosition(QTIObject oQTI, String sFieldLabel, String sAction){
		if (oQTI!=null && sFieldLabel!=null && sAction!=null){
			QTIMetadata oMetadata = getQTIMetadata(oQTI, sFieldLabel);
			if (oMetadata!=null){
				int iOldPosition = oMetadata.getQTIMetadatafieldPosition(sFieldLabel);
				int iNewPosition = iOldPosition+1;
				if (sAction.equals(A_UP)){
					iNewPosition = iOldPosition-1;
				}
				if (iNewPosition>=0 && iNewPosition<oMetadata.getQTIMetadatafield().size()){
					oMetadata.setQTIMetadatafieldPosition(iOldPosition, iNewPosition);
				}
			}
		}
	}
	
	private static Vector getQTIMetadata(QTIObject oQTI){
		Vector vMetadata = new Vector();
		if (oQTI instanceof Assessment){
			vMetadata = ((Assessment)oQTI).getQTIMetadata();
		}else if (oQTI instanceof Section){
			vMetadata = ((Section)oQTI).getQTIMetadata();
		}else if (oQTI instanceof Item){
			// TODO: Afegir ItemMetadata a Item
			//Itemmetadata oItemmetadata = ((Item)oQTI).getItemmetadata();
			//vMetadata = oItemmetadata.getQTIMetadata();
		}
		return vMetadata;
	}	

	private static QTIMetadata getQTIMetadata(QTIObject oQTI, String sIdent){
		QTIMetadata oMetadata = null;
		Vector vMetadata = getQTIMetadata(oQTI);
		Enumeration enumMetadata = vMetadata.elements();
		while (enumMetadata.hasMoreElements()){
			QTIMetadata tmpMetadata = (QTIMetadata)enumMetadata.nextElement();
			QTIMetadatafield oField = tmpMetadata.getQTIMetadatafield(sIdent);
			if (oField!=null) {
				oMetadata = tmpMetadata;
				break;
			} 
		}
		return oMetadata;
	}
	
	private static QTIMetadatafield getQTIMetadatafield(QTIObject oQTI, String sIdent){
		QTIMetadatafield oField = null;
		Vector vMetadata = getQTIMetadata(oQTI);
		Enumeration enumMetadata = vMetadata.elements();
		while (enumMetadata.hasMoreElements()){
			QTIMetadata oMetadata = (QTIMetadata)enumMetadata.nextElement();
			oField = oMetadata.getQTIMetadatafield(sIdent);
			if (oField!=null) break;
		}
		return oField;
	}
	
	public QTIObject getRequestQTIObject(){
		QTIMetadatafield oMetadatafield = null;
		String sFieldLabel = request.getParameter(P_LABEL);
		if (sFieldLabel!=null && sFieldLabel.trim().length()>0){
			String sFieldEntry = request.getParameter(P_ENTRY);
			oMetadatafield = new QTIMetadatafield(sFieldLabel, sFieldEntry);
		}
		return oMetadatafield;
	}
}
