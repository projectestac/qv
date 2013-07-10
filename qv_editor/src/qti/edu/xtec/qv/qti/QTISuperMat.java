/*
 * QTISuperMat.java
 * 
 * Created on 09/març/2004
 */
package edu.xtec.qv.qti;

import org.jdom.CDATA;
import org.jdom.Element;

/**
 * @author sarjona
 */
public abstract class QTISuperMat extends QTISuper {
	
	// material content types
	public static final String MATAPPLET_CONTENT_TYPE = "applet";
	public static final String MATAUDIO_CONTENT_TYPE = "audio";
	public static final String MATBREAK_CONTENT_TYPE = "break";
	public static final String MATHTML_CONTENT_TYPE = "html";
	public static final String MATIMAGE_CONTENT_TYPE = "image";
	public static final String MATEMTEXT_CONTENT_TYPE = "emtext";
	public static final String MATTEXT_CONTENT_TYPE = "text";
	public static final String MATVIDEO_CONTENT_TYPE = "video";
	public static final String MATFLASH_CONTENT_TYPE = "flash";
	public static final String MATJCLIC_CONTENT_TYPE = "jclic";
	public static final String MATAPPLICATION_CONTENT_TYPE = "application";
	public static final String MATLATEX_CONTENT_TYPE = "latex";

	// Text types
	public static final String HTML_TEXTTYPE = "text/html";
	public static final String PLAIN_TEXTTYPE = "text/plain";



	public QTISuperMat(){
	}

	public QTISuperMat(String sText){
		setAttribute(TEXT, sText);
	}

	public QTISuperMat(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eMaterial = new Element(getMatTag());
		if (getAttribute(getMatTypeLabel())!=null){
			eMaterial.setAttribute(getMatTypeTag(), getAttributeValue(getMatTypeLabel()));
		}
		if (!(this instanceof MatExtension)){
			if (getAttribute(TEXT)!=null){
				eMaterial.addContent(new CDATA(getText()));
				//eMaterial.addContent(getAttributeValue(TEXT));
			}
			if (getAttribute(LABEL)!=null){
				eMaterial.setAttribute(LABEL.getTagName(), getAttributeValue(LABEL));
			}
			if (getAttribute(URI)!=null){
				eMaterial.setAttribute(URI.getTagName(), getAttributeValue(URI));
			}
			if (getAttribute(ENTITYREF)!=null){
				eMaterial.setAttribute(ENTITYREF.getTagName(), getAttributeValue(ENTITYREF));
			}
			if (getAttribute(ALIGN)!=null){
				eMaterial.setAttribute(ALIGN.getTagName(), getAttributeValue(ALIGN));
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
		}
		getSpecificXML(eMaterial);
		return eMaterial;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			setAttribute(getMatTypeLabel(), eElement.getAttributeValue(getMatTypeTag()));
			String sText = eElement.getText();
			//if (HTML_TEXTTYPE.equalsIgnoreCase(getMattype())) sText = StringUtil.filterHTML(sText);
			setAttribute(TEXT, sText);
			setAttribute(LABEL, eElement.getAttributeValue(LABEL.getTagName()));
			setAttribute(URI, eElement.getAttributeValue(URI.getTagName()));
			setAttribute(ENTITYREF, eElement.getAttributeValue(ENTITYREF.getTagName()));
			setAttribute(ALIGN, eElement.getAttributeValue(ALIGN.getTagName()));
			setAttribute(WIDTH, eElement.getAttributeValue(WIDTH.getTagName()));
			setAttribute(HEIGHT, eElement.getAttributeValue(HEIGHT.getTagName()));
			setAttribute(X0, eElement.getAttributeValue(X0.getTagName()));
			setAttribute(Y0, eElement.getAttributeValue(Y0.getTagName()));
			createSpecificFromXML(eElement);
		}
	}
	
	public String getMattype(){
		return getAttributeValue(getMatTypeLabel());
	}	
	public void setMattype(String sTexttype){
		setAttribute(getMatTypeLabel(), sTexttype);
	}
	
	public String getText(){
		return getAttributeValue(TEXT);
	}
	public void setText(String sText){
		setAttribute(TEXT, sText);
	}

	public String getAlign(){
		return getAttributeValue(ALIGN);
	}
	public void setAlign(String sAlign){
		setAttribute(ALIGN, sAlign);
	}

	public String getURI(){
		return getAttributeValue(URI);
	}
	public void setURI(String sURI){
		setAttribute(URI, sURI);
	}
	
	public int getX0(){
		return getAttributeIntValue(X0);
	}
	public void setX0(int iX0){
		if (iX0>0){
			setAttribute(X0, String.valueOf(iX0));
		}
	}

	public int getY0(){
		return getAttributeIntValue(Y0);
	}
	public void setY0(int iY0){
		if (iY0>0){
			setAttribute(Y0, String.valueOf(iY0));
		}
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
	

	public String getShowName(){
		String sShowName = null;
		if (getText()!=null && getText().trim().length()>0){
			int iLastIndex = getText().length()<15?getText().length():15;
			sShowName = getText().substring(0,iLastIndex)+"...";
		}
		return sShowName;
	}
	
	public static boolean isMattext(String sMatContentType){
		return MATTEXT_CONTENT_TYPE.equalsIgnoreCase(sMatContentType);
	}
	
	public static boolean isMatHTML(String sMatContentType){
		return MATHTML_CONTENT_TYPE.equalsIgnoreCase(sMatContentType);
	}

	public static boolean isMatimage(String sMatContentType){
		return MATIMAGE_CONTENT_TYPE.equalsIgnoreCase(sMatContentType);
	}

	public static boolean isMatflash(String sMatContentType){
		return isMatflash(sMatContentType, null);
	}
	public static boolean isMatflash(String sMatContentType, String sMattype){
		return MATFLASH_CONTENT_TYPE.equalsIgnoreCase(sMatContentType)  || (MATIMAGE_CONTENT_TYPE.equalsIgnoreCase(sMatContentType) && "application/x-shockwave-flash".equals(sMattype));
	}

	public static boolean isMataudio(String sMatContentType){
		return MATAUDIO_CONTENT_TYPE.equalsIgnoreCase(sMatContentType);
	}

	public static boolean isMatvideo(String sMatContentType){
		return MATVIDEO_CONTENT_TYPE.equalsIgnoreCase(sMatContentType);
	}

	public static boolean isMatbreak(String sMatContentType){
		return MATBREAK_CONTENT_TYPE.equalsIgnoreCase(sMatContentType);
	}

	public static boolean isMatjclic(String sMatContentType){
		return MATJCLIC_CONTENT_TYPE.equalsIgnoreCase(sMatContentType);
	}
	
	public static boolean isMatapplication(String sMatContentType){
		return MATAPPLICATION_CONTENT_TYPE.equalsIgnoreCase(sMatContentType);
	}
	
	public static boolean isMatlatext(String sMatContentType){
		return MATLATEX_CONTENT_TYPE.equalsIgnoreCase(sMatContentType);
	}
	
	public abstract String getMatContentType();
	protected abstract String getMatTag();

	protected abstract String getMatTypeLabel();
	protected abstract String getMatTypeTag();
	
	protected abstract void createSpecificFromXML(Element eElement);
	protected abstract Element getSpecificXML(Element eMaterial);

}
