/*
 * Matjclic.java
 * 
 * Created on 05/abril/2006
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class Matjclic extends MatExtension {

	public Matjclic(){
	}
	
	public Matjclic(String sURI, int iWidth, int iHeight){
		setURI(sURI);
		setWidth(iWidth);
		setHeight(iHeight);
	}
	
	public Matjclic(Element eElement){
		super(eElement);
	}
	
	protected String getMatExtensionTag(){
		return MATJCLIC.getTagName();
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatContentType()
	 */
	public String getMatContentType() {
		return MATJCLIC_CONTENT_TYPE;
	}
	
	protected Element getExtensionXML(Element eExtension) {
		Element eMat = new Element(getMatExtensionTag());
		eMat.addContent(getText());
		eExtension.addContent(eMat);
		if (getAttribute(URI)!=null){
			eMat.setAttribute(URI.getTagName(), getAttributeValue(URI));
		}
		if (getAttribute(WIDTH)!=null){
			eMat.setAttribute(WIDTH.getTagName(), getAttributeValue(WIDTH));
		}
		if (getAttribute(HEIGHT)!=null){
			eMat.setAttribute(HEIGHT.getTagName(), getAttributeValue(HEIGHT));
		}		
		if (getAttribute(LANGUAGE)!=null){
			eMat.setAttribute(LANGUAGE.getTagName(), getAttributeValue(LANGUAGE));
		}		
		return eExtension;
	}
	
	protected Element createExtensionFromXML(Element eElement){
		if (eElement!=null){
			setAttribute(getMatTypeLabel(), eElement.getAttributeValue(getMatTypeTag()));
			setAttribute(URI, eElement.getAttributeValue(URI.getTagName()));
			setAttribute(WIDTH, eElement.getAttributeValue(WIDTH.getTagName()));
			setAttribute(HEIGHT, eElement.getAttributeValue(HEIGHT.getTagName()));
		}		
		return eElement;
	}
	
	public int getWidth(){
		return getAttributeIntValue(WIDTH);
	}
	public void setWidth(int iWidth){
		if (iWidth>0){
			setAttribute(WIDTH, String.valueOf(iWidth));
		}
	}

	public int getHeight(){
		return getAttributeIntValue(HEIGHT);
	}
	public void setHeight(int iHeight){
		if (iHeight>0){
			setAttribute(HEIGHT, String.valueOf(iHeight));
		}
	}


}
