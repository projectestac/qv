/*
 * Mataudio.java
 * 
 * Created on 09/març/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class Mataudio extends QTISuperMat {

	public Mataudio(String sURI){
		setURI(sURI);
	}
	
	public Mataudio(Element eElement){
		super(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#createSpecificFromXML(org.jdom.Element)
	 */
	protected void createSpecificFromXML(Element eElement) {
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getSpecificXML(org.jdom.Element)
	 */
	protected Element getSpecificXML(Element eMaterial) {
		return eMaterial;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatContentType()
	 */
	public String getMatContentType() {
		return MATAUDIO_CONTENT_TYPE;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatTag()
	 */
	protected String getMatTag() {
		return MATAUDIO.getTagName();
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatTypeLabel()
	 */
	protected String getMatTypeLabel() {
		return AUDIOTYPE.getAttributeName();
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatTypeTag()
	 */
	protected String getMatTypeTag() {
		return AUDIOTYPE.getTagName();
	}

}
