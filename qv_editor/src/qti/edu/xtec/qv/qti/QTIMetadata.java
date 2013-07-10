/*
 * QTIFlow.java
 * 
 * Created on 04/maig/2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

import edu.xtec.qv.qti.util.QTIConstant;

/**
 * @author sarjona
 */
public class QTIMetadata extends QTISuper {

	public QTIMetadata(){
		setAttribute(CONTENT, new Vector());
	}
	
	public QTIMetadata(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eMetadata = new Element(getQTIConstant().getTagName());
		Enumeration enumContents = getQTIMetadatafield().elements();
		while (enumContents.hasMoreElements()){
			QTIObject oContent = (QTIObject)enumContents.nextElement();
			eMetadata.addContent(oContent.getXML());
		}
		return eMetadata;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			Vector vMetadataField = new Vector();
			Iterator itContents = eElement.getChildren().iterator();
			while (itContents.hasNext()){
				Element eContent = (Element)itContents.next();
				if (QTIMETADATAFIELD.equals(eContent.getName())){
					QTIObject oContent = new QTIMetadatafield(eContent);
					vMetadataField.addElement(oContent);
				} 
			}
			setAttribute(QTIMETADATAFIELD, vMetadataField);
		}
	}
	
	public Vector getQTIMetadatafield(){
		return getAttributeVectorValue(QTIMETADATAFIELD);
	}
	public QTIMetadatafield getQTIMetadatafield(int iPosition){
		QTIMetadatafield oMetadatafield = null;
		Object o = getQTIMetadatafield().elementAt(iPosition);
		if (o!=null){
			oMetadatafield =(QTIMetadatafield)o;
		}
		return oMetadatafield;
	}
	public QTIMetadatafield getQTIMetadatafield(String sFieldLabel){
		QTIMetadatafield oMetadatafield = null;
		if (sFieldLabel!=null){
			Enumeration enumFields = getQTIMetadatafield().elements();
			while (enumFields.hasMoreElements()){
				QTIMetadatafield tmpMetadatafield = (QTIMetadatafield)enumFields.nextElement();
				if (sFieldLabel.equalsIgnoreCase(tmpMetadatafield.getFieldLabel())){
					oMetadatafield = tmpMetadatafield;
					break;
				}
			}
		}
		return oMetadatafield;
	}
	public void setQTIMetadatafield(QTIMetadatafield oOld, QTIMetadatafield oNew){
		if (oNew!=null){
			int iIndex = getQTIMetadatafield().indexOf(oOld);
			getQTIMetadatafield().setElementAt(oNew, iIndex);
		}
	}

	public int getQTIMetadatafieldPosition(String sFieldLabel){
		int iPosition = -1;
		if (sFieldLabel!=null){
			iPosition = getQTIMetadatafield().indexOf(getQTIMetadatafield(sFieldLabel));
		}
		return iPosition;
	}
	public void setQTIMetadatafieldPosition(int iOldPosition, int iNewPosition){
		setObjectPosition(getQTIMetadatafield(), getQTIMetadatafield(iOldPosition), iOldPosition, iNewPosition);
	}
	
	public void setQTIMetadatafieldPosition(QTIMetadatafield oMetadatafield, int iOldPosition, int iNewPosition){
		setObjectPosition(getQTIMetadatafield(), oMetadatafield, iOldPosition, iNewPosition);
	}
	
	public void addQTIMetadatafield(QTIMetadatafield oMetadatafield){
		if (getAttribute(QTIMETADATAFIELD)==null){
			setAttribute(QTIMETADATAFIELD, new Vector());
		}
		setAttribute(QTIMETADATAFIELD, oMetadatafield);		
	}
	
	public void addQTIMetadatafield(QTIMetadatafield oMetadatafield, int iPosition){
		if (oMetadatafield!=null){
			if (iPosition>=0){
				getQTIMetadatafield().insertElementAt(oMetadatafield, iPosition);
			}else{
				addQTIMetadatafield(oMetadatafield);
			}
		}
	}
	
	public void delQTIMetadatafield(String sFieldLabel){
		QTIMetadatafield oMetadatafield = getQTIMetadatafield(sFieldLabel);
		delQTIMetadatafield(oMetadatafield);
	}
	
	public void delQTIMetadatafield(QTIMetadatafield oMetadatafield){
		getQTIMetadatafield().removeElement(oMetadatafield);
	}
	
	public final static QTIConstant getQTIConstant(){
		return QTIMETADATA;
	}	
	
	/* (non-Javadoc)
	 * Redefinit per no guardar metadatas amb qtimetadatafield buits
	 * @see edu.xtec.qv.qti.QTISuper#addToXML(org.jdom.Element, edu.xtec.qv.qti.QTIObject)
	 *
	public static void addToXML(Element eParent, QTIObject oParent){
		if (eParent!=null && oParent!=null){
			Vector v = (Vector)get(oParent);
			if (v!=null && !v.isEmpty()){
				Enumeration enum = v.elements();
				while (enum.hasMoreElements()){
					QTIMetadata oMetadata = (QTIMetadata)enum.nextElement();
					if (oMetadata.getQTIMetadatafield().size()>0)
						eParent.addContent(oMetadata.getXML());
				}
			}
		}
	}*/
	
	
	
}
