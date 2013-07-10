/*
 * Matapplication.java
 * 
 * Created on 19/març/2010
 */
package edu.xtec.qv.qti;

import org.jdom.CDATA;
import org.jdom.Element;

/**
 * @author fbassas
 */
public class Matapplication extends QTISuperMat {

	public Matapplication(String sApptype, String sURI){
		setMattype(sApptype);
		setURI(sURI);
	}
	
	public Matapplication(Element eElement){
		super(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#createSpecificFromXML(org.jdom.Element)
	 */
	protected void createSpecificFromXML(Element eElement) {
		setAttribute(APPTYPE, eElement.getAttributeValue(APPTYPE.getTagName()));
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getSpecificXML()
	 */
	protected Element getSpecificXML(Element eMaterial) {
		return eMaterial;
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatContentType()
	 */
	public String getMatContentType() {
		return MATAPPLICATION_CONTENT_TYPE;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatTag()
	 */
	protected String getMatTag() {
		return MATAPPLICATION.getTagName();
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatTypeLabel()
	 */
	protected String getMatTypeLabel() {
		return APPLICATIONTYPE.getAttributeName();
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatTypeTag()
	 */
	protected String getMatTypeTag() {
		return APPLICATIONTYPE.getTagName();
	}

}
