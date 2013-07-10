/*
 * Matlatex.java
 * 
 * Created on 26/05/2006
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class Matlatex extends MatExtension {

	public Matlatex(){
	}
	
	public Matlatex(String sEquation, int iWidth, int iHeight){
		setText(sEquation);
		setWidth(iWidth);
		setHeight(iHeight);
	}
	
	public Matlatex(Element eElement){
		super(eElement);
	}
	
	protected String getMatExtensionTag(){
		return MATLATEX.getTagName();
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatContentType()
	 */
	public String getMatContentType() {
		return MATLATEX_CONTENT_TYPE;
	}
	
	protected Element getExtensionXML(Element eExtension) {
		Element eMat = new Element(getMatExtensionTag());
		eMat.addContent(getText());
		eExtension.addContent(eMat);
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
