/*
 * Matimage.java
 * 
 * Created on 23/febrer/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class Matimage extends QTISuperMat {
	
	public static final String FLASH_MIME_TYPE = "application/x-shockwave-flash";

	public Matimage(String sImagtype, String sURI){
		setMattype(sImagtype);
		setURI(sURI);
	}
	
	public Matimage(String sImagtype, String sURI, int iWidth, int iHeight){
		setMattype(sImagtype);
		setURI(sURI);
		setWidth(iWidth);
		setHeight(iHeight);
	}
	
	public Matimage(Element eElement){
		super(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getSpecificXML()
	 */
	protected Element getSpecificXML(Element eMaterial){
		if (getAttribute(EMBEDDED)!=null){
			eMaterial.setAttribute(EMBEDDED.getTagName(), getAttributeValue(EMBEDDED));
		}
		return eMaterial;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#createSpecificFromXML(org.jdom.Element)
	 */
	protected void createSpecificFromXML(Element eElement){
		setAttribute(EMBEDDED, eElement.getAttributeValue(EMBEDDED.getTagName()));
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatContentType()
	 */
	public String getMatContentType() {
		if (FLASH_MIME_TYPE.equals(getMattype())) return MATFLASH_CONTENT_TYPE;
		return MATIMAGE_CONTENT_TYPE;
	}

	protected String getMatTag(){
		return MATIMAGE.getTagName();
	}

	protected  String getMatTypeTag(){
		return IMAGTYPE.getTagName();
	}

	protected  String getMatTypeLabel(){
		return IMAGTYPE.getAttributeName();
	}
	
}
