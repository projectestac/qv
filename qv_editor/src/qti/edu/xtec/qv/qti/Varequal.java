/*
 * Varequal.java
 * 
 * Created on 10/febrer/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class Varequal extends QTIBooleanVar {

	public Varequal(String sRespident, String sIndex, String sText, String sCase){
		super(sRespident, sIndex, sText);
		setAttribute(CASE, sCase);
	}
	
	public Varequal(Element eElement){
		super(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eVarequal = super.getXML();
		if(eVarequal!=null){
			Object oCase =  getAttribute(CASE);
			if (oCase!=null){
				eVarequal.setAttribute(CASE.getTagName(), (String)oCase);
			}
		}
		return eVarequal;
		
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		super.createFromXML(eElement);
		setAttribute(CASE, eElement.getAttributeValue(CASE.getTagName()));
	}

	protected String getVarTag(){
		return VAREQUAL.getTagName();
	}
	
}
