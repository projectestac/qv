/*
 * QTIFlow.java
 * 
 * Created on 04/maig/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class QTIMetadatafield extends QTISuper {

	public QTIMetadatafield(String sLabel, String sEntry){
		setFieldLabel(sLabel);
		setFieldEntry(sEntry);
	}
	
	public QTIMetadatafield(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eMetadatafield = new Element(QTIMETADATAFIELD.getTagName());
		Object oFieldlabel = getAttribute(FIELDLABEL);
		if (oFieldlabel!=null) {
			Element eFieldLabel = new Element(FIELDLABEL.getTagName());
			eFieldLabel.setText((String)oFieldlabel);
			eMetadatafield.addContent(eFieldLabel);
		} 
		Object oFieldEntry = getAttribute(FIELDENTRY);
		if (oFieldlabel!=null) {
			Element eFieldEntry = new Element(FIELDENTRY.getTagName());
			eFieldEntry.setText((String)oFieldEntry);
			eMetadatafield.addContent(eFieldEntry);
		} 
		return eMetadatafield;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			setAttribute(FIELDLABEL, eElement.getChildText(FIELDLABEL.getTagName()));
			setAttribute(FIELDENTRY, eElement.getChildText(FIELDENTRY.getTagName()));
		}
	}
	
	public String getFieldLabel(){
		return getAttributeValue(FIELDLABEL);
	}
	public void setFieldLabel(String sFieldLabel){
		setAttribute(FIELDLABEL, sFieldLabel);
	}
	
	public String getFieldEntry(){
		return getAttributeValue(FIELDENTRY);
	}
	public void setFieldEntry(String sFieldEntry){
		setAttribute(FIELDENTRY, sFieldEntry);
	}

}
