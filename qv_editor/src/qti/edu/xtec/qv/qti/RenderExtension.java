/*
 * RenderExtension.java
 * 
 * Created on 17/març/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public abstract class RenderExtension extends QTISuperRender {

	public RenderExtension(){
	}

	public RenderExtension(Element eElement){
		super(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperRender#getRenderTag()
	 */
	protected String getRenderTag() {
		return getRenderExtensionTag();
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperRender#getSpecificXML(org.jdom.Element)
	 */
	protected Element getSpecificXML(Element eRender){
		Element eExtension = new Element(RENDER_EXTENSION.getTagName());
		eExtension.addContent(eRender);
		getExtendedXML(eRender);
		return eExtension;
	}
		
	protected abstract String getRenderExtensionTag();
	protected abstract Element getExtendedXML(Element eElement);
	

}
