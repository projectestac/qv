/*
 * IMSRenderObject.java
 * 
 * Created on 17/març/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class IMSRenderObject extends RenderExtension {

	public IMSRenderObject(){
	}

	public IMSRenderObject(Element eElement){
		super(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIRenderExtension#getRenderExtensionTag()
	 */
	protected String getRenderExtensionTag(){
		return IMS_RENDER_OBJECT.getTagName();
	}

	protected Element getExtendedXML(Element eElement){
		if (getAttribute(ORDER)!=null){
			eElement.setAttribute(ORDER.getTagName(), getAttributeValue(ORDER));
		}
		if (getAttribute(ORIENTATION)!=null){
			eElement.setAttribute(ORIENTATION.getTagName(), getAttributeValue(ORIENTATION));
		}
		if (getAttribute(SHUFFLE)!=null){
			eElement.setAttribute(SHUFFLE.getTagName(), getAttributeValue(SHUFFLE));
		}
		if (getAttribute(WIDTH)!=null){
			eElement.setAttribute(WIDTH.getTagName(), getAttributeValue(WIDTH));
		}
		if (getAttribute(HEIGHT)!=null){
			eElement.setAttribute(HEIGHT.getTagName(), getAttributeValue(HEIGHT));
		}
		if (getAttribute(INSIDE)!=null){
			eElement.setAttribute(INSIDE.getTagName(), getAttributeValue(INSIDE));
		}
		if (getAttribute(ALIGN)!=null){
			eElement.setAttribute(ALIGN.getTagName(), getAttributeValue(ALIGN));
		}
		if (getAttribute(SHOWTARGETS)!=null){
			eElement.setAttribute(SHOWTARGETS.getTagName(), getAttributeValue(SHOWTARGETS));
		}
		if (getAttribute(STYLE)!=null){
			eElement.setAttribute(STYLE.getTagName(), getAttributeValue(STYLE));
		}
		if (getAttribute(ENABLE_ROTATING)!=null){
			eElement.setAttribute(ENABLE_ROTATING.getTagName(), getAttributeValue(ENABLE_ROTATING));
		}
		if (getAttribute(ENABLE_SCALING)!=null){
			eElement.setAttribute(ENABLE_SCALING.getTagName(), getAttributeValue(ENABLE_SCALING));
		}
		if (getAttribute(NROTATE)!=null){
			eElement.setAttribute(NROTATE.getTagName(), getAttributeValue(NROTATE));
		}
		if (getAttribute(NRATIO)!=null){
			eElement.setAttribute(NRATIO.getTagName(), getAttributeValue(NRATIO));
		}
		if (getAttribute(FITTARGET)!=null){
			eElement.setAttribute(FITTARGET.getTagName(), getAttributeValue(FITTARGET));
		}
		return eElement;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperRender#createSpecificFromXML(org.jdom.Element)
	 */
	protected void createSpecificFromXML(Element eElement){
		setAttribute(ORDER, eElement.getAttributeValue(ORDER.getTagName()));
		setAttribute(ORIENTATION, eElement.getAttributeValue(ORIENTATION.getTagName()));
		setAttribute(SHUFFLE, eElement.getAttributeValue(SHUFFLE.getTagName()));
		setAttribute(WIDTH, eElement.getAttributeValue(WIDTH.getTagName()));
		setAttribute(HEIGHT, eElement.getAttributeValue(HEIGHT.getTagName()));
		setAttribute(INSIDE, eElement.getAttributeValue(INSIDE.getTagName()));
		setAttribute(ALIGN, eElement.getAttributeValue(ALIGN.getTagName()));
		setAttribute(SHOWTARGETS, eElement.getAttributeValue(SHOWTARGETS.getTagName()));
		setAttribute(STYLE, eElement.getAttributeValue(STYLE.getTagName()));
		setAttribute(ENABLE_ROTATING, eElement.getAttributeValue(ENABLE_ROTATING.getTagName()));
		setAttribute(ENABLE_SCALING, eElement.getAttributeValue(ENABLE_SCALING.getTagName()));
		setAttribute(NROTATE, eElement.getAttributeValue(NROTATE.getTagName()));
		setAttribute(NRATIO, eElement.getAttributeValue(NRATIO.getTagName()));
		setAttribute(FITTARGET, eElement.getAttributeValue(FITTARGET.getTagName()));
	}

	public static IMSRenderObject create(String sOrientation, String sShuffle){
		IMSRenderObject oRender = new IMSRenderObject();
		oRender.setOrientation(sOrientation);
		oRender.setShuffle(sShuffle);
		return oRender;
	}
	
	public String getOrder(){
		return getAttributeValue(ORDER);
	}
	
	public String getOrientation(){
		return getAttributeValue(ORIENTATION);
	}
	public void setOrientation(String sOrientation){
		setAttribute(ORIENTATION, sOrientation);
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
	
	public String getShuffle(){
		return getAttributeValue(SHUFFLE);
	}	
	public void setShuffle(String sShuffle){
		setAttribute(SHUFFLE, sShuffle);
	}

	public String getInside(){
		return getAttributeValue(INSIDE);
	}
	public void setInside(String sInside){
		setAttribute(INSIDE, sInside);
	}
	
	public String getAlign(){
		return getAttributeValue(ALIGN);
	}
	public void setAlign(String sAlign){
		setAttribute(ALIGN, sAlign);
	}

	public String getShowTargets(){
		return getAttributeValue(SHOWTARGETS);
	}
	public void setShowTargets(String sShowTargets){
		setAttribute(SHOWTARGETS, sShowTargets);
	}
	
	public String getEnableRotating(){
		return getAttributeValue(ENABLE_ROTATING);
	}	
	public void setEnableRotating(String s){
		setAttribute(ENABLE_ROTATING, s);
	}

	public String getEnableScaling(){
		return getAttributeValue(ENABLE_SCALING);
	}	
	public void setEnableScaling(String s){
		setAttribute(ENABLE_SCALING, s);
	}

	public String getNRotate(){
		return getAttributeValue(NROTATE);
	}	
	public void setNRotate(String s){
		setAttribute(NROTATE, s);
	}

	public String getNRatio(){
		return getAttributeValue(NRATIO);
	}	
	public void setNRatio(String s){
		setAttribute(NRATIO, s);
	}

	public String getFitTarget(){
		return getAttributeValue(FITTARGET);
	}	
	public void setFitTarget(String s){
		setAttribute(FITTARGET, s);
	}

}
