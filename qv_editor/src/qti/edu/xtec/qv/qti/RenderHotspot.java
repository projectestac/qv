/*
 * RenderHotspot.java
 * 
 * Created on 02/març/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class RenderHotspot extends QTISuperRender {

	public RenderHotspot(Element eElement){
		super(eElement);
	}

	public RenderHotspot(){
		super();
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperRender#getRenderTag()
	 */
	protected String getRenderTag() {
		return RENDER_HOTSPOT.getTagName();
	}		

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperRender#getSpecificXML(org.jdom.Element)
	 */
	protected Element getSpecificXML(Element eRender){
		if (getAttribute(SHOWDRAW)!=null){
			eRender.setAttribute(SHOWDRAW.getTagName(), getAttributeValue(SHOWDRAW));
		}
		if (getAttribute(SHOWOPTIONS)!=null){
			eRender.setAttribute(SHOWOPTIONS.getTagName(), getAttributeValue(SHOWOPTIONS));
		}
		if (getAttribute(TRANSP)!=null){
			eRender.setAttribute(TRANSP.getTagName(), getAttributeValue(TRANSP));
		}
		if (getAttribute(INITPARAM)!=null){
			eRender.setAttribute(INITPARAM.getTagName(), getAttributeValue(INITPARAM));
		}
		return eRender;
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperRender#createSpecificFromXML(org.jdom.Element)
	 */
	protected void createSpecificFromXML(Element eElement){
		setAttribute(SHOWDRAW, eElement.getAttributeValue(SHOWDRAW.getTagName()));
		setAttribute(SHOWOPTIONS, eElement.getAttributeValue(SHOWOPTIONS.getTagName()));
		setAttribute(TRANSP, eElement.getAttributeValue(TRANSP.getTagName()));
		setAttribute(INITPARAM, eElement.getAttributeValue(INITPARAM.getTagName()));
	}
	
	public String getShowDraw(){
		return getAttributeValue(SHOWDRAW);
	}
	public void setShowDraw(String s){
		setAttribute(SHOWDRAW, s);
	}

	public String getShowOptions(){
		return getAttributeValue(SHOWOPTIONS);
	}
	public void setShowOptions(String s){
		setAttribute(SHOWOPTIONS, s);
	}

	public String getTransp(){
		return getAttributeValue(TRANSP);
	}
	public void setTransp(String s){
		setAttribute(TRANSP, s);
	}
}
