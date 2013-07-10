/*
 * Matbreak.java
 * 
 * Created on 14/maig/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class Matbreak extends QTISuperMat{

	public Matbreak(){
	}

	public Matbreak(Element eElement){
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
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatContentType()
	 */
	public String getMatContentType() {
		return MATBREAK_CONTENT_TYPE;
	}

	protected String getMatTag(){
		return MATBREAK.getTagName();
	}

	protected  String getMatTypeLabel(){
		return TEXTTYPE.getAttributeName();
	}
	protected  String getMatTypeTag(){
		return TEXTTYPE.getTagName();
	}

}
