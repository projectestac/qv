/*
 * RenderDraw.java
 * 
 * Created on 23/febrer/2006
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class RenderDraw extends RenderExtension {

	public RenderDraw(){
	}

	public RenderDraw(Element eElement){
		super(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIRenderExtension#getRenderExtensionTag()
	 */
	protected String getRenderExtensionTag(){
		return RENDER_DRAW.getTagName();
	}

	protected Element getExtendedXML(Element eElement){
		if (getAttribute(WIDTH)!=null){
			eElement.setAttribute(WIDTH.getTagName(), getAttributeValue(WIDTH));
		}
		if (getAttribute(HEIGHT)!=null){
			eElement.setAttribute(HEIGHT.getTagName(), getAttributeValue(HEIGHT));
		}
		if (getAttribute(NUM_COLORS)!=null){
			eElement.setAttribute(NUM_COLORS.getTagName(), getAttributeValue(NUM_COLORS));
		}
		return eElement;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperRender#createSpecificFromXML(org.jdom.Element)
	 */
	protected void createSpecificFromXML(Element eElement){
		setAttribute(WIDTH, eElement.getAttributeValue(WIDTH.getTagName()));
		setAttribute(HEIGHT, eElement.getAttributeValue(HEIGHT.getTagName()));
		setAttribute(NUM_COLORS, eElement.getAttributeValue(NUM_COLORS.getTagName()));
	}

	public static RenderDraw create(){
		RenderDraw oRender = new RenderDraw();
		return oRender;
	}
	
	public String getOrder(){
		return getAttributeValue(ORDER);
	}
		
	public String getWidth(){
		return getAttributeValue(WIDTH);
	}
	public void setWidth(String sWidth){
		setAttribute(WIDTH, sWidth);
	}
	
	public String getHeight(){
		return getAttributeValue(HEIGHT);
	}
	public void setHeight(String sHeight){
		setAttribute(HEIGHT, sHeight);
	}
	
	public String getColors(){
		return getAttributeValue(NUM_COLORS);
	}
	public void setColors(String sColors){
		setAttribute(NUM_COLORS, sColors);
	}
	
	
}
