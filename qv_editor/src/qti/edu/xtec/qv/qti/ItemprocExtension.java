/*
 * ItemprocExtension.java
 * 
 * Created on 03/setembre/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

import edu.xtec.qv.qti.util.QTIConstant;

/**
 * @author sarjona
 */
public class ItemprocExtension extends QTISuperProcessingExtension {

	public ItemprocExtension(){
	}
	
	public ItemprocExtension(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperProcessingExtension#getControlTag()
	 */
	protected String getControlTag() {
		return getQTIConstant().getTagName();
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperProcessingExtension#getSpecificXML(org.jdom.Element)
	 */
	protected Element getSpecificXML(Element eProcExtension) {
		return eProcExtension;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperProcessingExtension#createSpecificFromXML(org.jdom.Element)
	 */
	protected void createSpecificFromXML(Element eElement) {
	}

	public static QTIConstant getQTIConstant(){
		return ITEMPROC_EXTENSION;
	}

}
