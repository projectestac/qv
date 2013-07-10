/*
 * MatText.java
 * 
 * Created on 09/febrer/2004
 */
package edu.xtec.qv.qti;

import org.jdom.CDATA;
import org.jdom.Element;

/**
 * @author sarjona
 */
public class Mattext extends QTISuperMat{
	
	public Mattext(String sText){
		super(sText);
	}

	public Mattext(String sTextType, String sText){
		super(sText);
		setMattype(sTextType);
	}

	public Mattext(Element eElement){
		super(eElement);
	}

	protected Element getSpecificXML(Element eMaterial){
		if (isHTML()){
			eMaterial.setText("");
			eMaterial.addContent(new CDATA(getAttributeValue(TEXT)));
			//eMaterial.addContent("<![CDATA["+getAttributeValue(TEXT)+"]]>");
		}
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
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatContentType()
	 */
	public String getMatContentType() {
		String sContentType = MATTEXT_CONTENT_TYPE;
		if (isHTML()){
			sContentType = MATHTML_CONTENT_TYPE;
		}
		return sContentType;
	}

	protected String getMatTag(){
		return MATTEXT.getTagName();
	}

	protected  String getMatTypeLabel(){
		return TEXTTYPE.getAttributeName();
	}
	protected  String getMatTypeTag(){
		return TEXTTYPE.getTagName();
	}
	
	public int getWidth(){
		return getAttributeIntValue(WIDTH);
	}

	public int getHeight(){
		return getAttributeIntValue(HEIGHT);
	}

	public int getX0(){
		return getAttributeIntValue(X0);
	}

	public int getY0(){
		return getAttributeIntValue(Y0);
	}
	
	protected boolean isHTML(){
		return (getMattype()!=null && getMattype().equalsIgnoreCase(HTML_TEXTTYPE));		
	}

}
