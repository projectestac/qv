/*
 * QTISuperProcessingExtension.java
 * 
 * Created on 01/setembre/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public abstract class QTISuperProcessingExtension extends QTISuper {

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eProcExtension = new Element(getControlTag());
		return getSpecificXML(eProcExtension);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			createSpecificFromXML(eElement);
		}
	}

	protected abstract String getControlTag();
	protected abstract Element getSpecificXML(Element eProcExtension);
	protected abstract void createSpecificFromXML(Element eElement);

}
