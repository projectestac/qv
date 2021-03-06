/*
 * Matapplet.java
 * 
 * Created on 14/maig/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class Matapplet extends QTISuperMat{

	public Matapplet(String sURI){
		setURI(sURI);
	}

	public Matapplet(Element eElement){
		super(eElement);
	}

	protected Element getSpecificXML(Element eMaterial){
		if (getAttribute(WIDTH)!=null){
			eMaterial.setAttribute(WIDTH.getTagName(), getAttributeValue(WIDTH));
		}
		if (getAttribute(HEIGHT)!=null){
			eMaterial.setAttribute(HEIGHT.getTagName(), getAttributeValue(HEIGHT));
		}
		if (getAttribute(X0)!=null){
			eMaterial.setAttribute(X0.getTagName(), getAttributeValue(X0));
		}
		if (getAttribute(Y0)!=null){
			eMaterial.setAttribute(Y0.getTagName(), getAttributeValue(Y0));
		}
		if (getAttribute(EMBEDDED)!=null){
			eMaterial.setAttribute(EMBEDDED.getTagName(), getAttributeValue(EMBEDDED));
		}
		return eMaterial;
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#createSpecificFromXML(org.jdom.Element)
	 */
	protected void createSpecificFromXML(Element eElement){
		setAttribute(WIDTH, eElement.getAttributeValue(WIDTH.getTagName()));
		setAttribute(HEIGHT, eElement.getAttributeValue(HEIGHT.getTagName()));
		setAttribute(X0, eElement.getAttributeValue(X0.getTagName()));
		setAttribute(Y0, eElement.getAttributeValue(Y0.getTagName()));
		setAttribute(EMBEDDED, eElement.getAttributeValue(EMBEDDED.getTagName()));
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatContentType()
	 */
	public String getMatContentType() {
		return MATAPPLET_CONTENT_TYPE;
	}

	protected String getMatTag(){
		return MATAPPLET.getTagName();
	}

	protected  String getMatTypeLabel(){
		return APPTYPE.getAttributeName();
	}
	protected  String getMatTypeTag(){
		return APPTYPE.getTagName();
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
