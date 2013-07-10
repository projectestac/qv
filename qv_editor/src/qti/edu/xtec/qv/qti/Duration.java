/*
 * Duration.java
 * 
 * Created on 13/agost/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

import edu.xtec.qv.qti.util.QTIConstant;

/**
 * @author sarjona
 */
public class Duration extends QTISuper {

	public Duration(String sText){
		setText(sText);
	}
	
	public Duration(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eDuration = new Element(getQTIConstant().getTagName());
		eDuration.setText(getText());
		return eDuration;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			setAttribute(TEXT, eElement.getText());
		}
	}
	
	public String getText(){
		return getAttributeValue(TEXT);
	}
	public void setText(String sText){
		setAttribute(TEXT, sText);
	}

	public static QTIConstant getQTIConstant(){
		return DURATION;
	}

}
