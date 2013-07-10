/*
 * Matemtext.java
 * 
 * Created on 14/maig/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class Matemtext extends QTISuperMat{

	public Matemtext(String sText){
		super(sText);
	}

	public Matemtext(Element eElement){
		super(eElement);
	}

	protected Element getSpecificXML(Element eMaterial){
		return eMaterial;
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#createSpecificFromXML(org.jdom.Element)
	 */
	protected void createSpecificFromXML(Element eElement){
	}
	
	protected String getMatTag(){
		return MATEMTEXT.getTagName();
	}

	protected  String getMatTypeLabel(){
		return TEXTTYPE.getAttributeName();
	}
	protected  String getMatTypeTag(){
		return TEXTTYPE.getTagName();
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatContentType()
	 */
	public String getMatContentType() {
		return MATEMTEXT_CONTENT_TYPE;
	}

}
