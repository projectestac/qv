/*
 * AssessprocExtension.java
 * 
 * Created on 01/setembre/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

import edu.xtec.qv.qti.util.QTIConstant;

/**
 * @author sarjona
 */
public class AssessprocExtension extends QTISuperProcessingExtension {

	public AssessprocExtension(){
	}
	
	public AssessprocExtension(Element eElement){
		createFromXML(eElement);
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

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperProcessingExtension#getControlTag()
	 */
	protected String getControlTag() {
		return getQTIConstant().getTagName();
	}

	public static QTIConstant getQTIConstant(){
		return ASSESSPROC_EXTENSION;
	}

}
