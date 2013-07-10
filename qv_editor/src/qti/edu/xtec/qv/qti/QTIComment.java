/*
 * QTIComment.java
 * 
 * Created on 14/maig/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

import edu.xtec.qv.qti.util.QTIConstant;

/**
 * @author sarjona
 */
public class QTIComment extends QTISuper {

	public QTIComment(String sText){
		setText(sText);
	}
	
	public QTIComment(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eComment = new Element(getQTIConstant().getTagName());
		if (getText()!=null){
			eComment.addContent(getText());
		}
		return eComment;
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

	public final static QTIConstant getQTIConstant(){
		return QTICOMMENT;
	}

}
