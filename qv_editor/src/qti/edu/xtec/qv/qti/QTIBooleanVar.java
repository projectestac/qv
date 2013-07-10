/*
 * QTIBooleanVar.java
 * 
 * Created on 10/febrer/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public abstract class QTIBooleanVar extends QTISuper {

	public QTIBooleanVar(String sRespident, String sIndex, String sText){
		setAttribute(RESPIDENT, sRespident);
		setAttribute(INDEX, sIndex);
		setText(sText);
	}
	
	public QTIBooleanVar(Element eElement){
		createFromXML(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eOperator = new Element(getVarTag());
		eOperator.setAttribute(RESPIDENT.getTagName(), getAttributeValue(RESPIDENT));
		
		Object oIndex =  getAttribute(INDEX);
		if (oIndex!=null){
			eOperator.setAttribute(INDEX.getTagName(), (String)oIndex);
		}
		if (getAttribute(TEXT)!=null){
			eOperator.addContent(getAttributeValue(TEXT));
		}
		return eOperator;
	}


	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			setAttribute(RESPIDENT, eElement.getAttributeValue(RESPIDENT.getTagName()));
			setAttribute(INDEX, eElement.getAttributeValue(INDEX.getTagName()));
			setAttribute(TEXT, eElement.getText());
		}
	}
	
	public String getRespident(){
		return getAttributeValue(RESPIDENT);
	}
	
	public String getIndex(){
		return getAttributeValue(INDEX);
	}

	public String getText(){
		return getAttributeValue(TEXT);
	}
	
	public void setText(String sText){
		setAttribute(TEXT, sText);
	}
	
	protected abstract String getVarTag();
	
}
