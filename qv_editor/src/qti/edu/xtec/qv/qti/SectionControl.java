/*
 * SectionControl.java
 * 
 * Created on 11/agost/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

import edu.xtec.qv.qti.util.QTIConstant;

/**
 * @author sarjona
 */
public class SectionControl extends QTISuperControl {

	public SectionControl(){
		super();
	}

	public SectionControl(Element eElement){
		super(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperControl#getControlTag()
	 */
	protected String getControlTag() {
		return getQTIConstant().getTagName();
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperControl#getSpecificXML(org.jdom.Element)
	 */
	protected Element getSpecificXML(Element eControl) {
		return eControl;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperControl#createSpecificFromXML(org.jdom.Element)
	 */
	protected void createSpecificFromXML(Element eElement) {

	}

	public final static QTIConstant getQTIConstant(){
		return SECTIONCONTROL;
	}


}
