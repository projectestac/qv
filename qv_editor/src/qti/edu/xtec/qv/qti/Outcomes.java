/*
 * Outcomes.java
 * 
 * Created on 10/febrer/2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class Outcomes extends QTISuper {

	public Outcomes(){
		setAttribute(DECVAR, new Vector());
	}
	
	public Outcomes(Element eElement){
		createFromXML(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eOutcomes = new Element(OUTCOMES.getTagName());
		if (getAttribute(DECVAR)!=null){
			Vector vDecvar  = getAttributeVectorValue(DECVAR);
			Enumeration enumDecvar = vDecvar.elements();
			while (enumDecvar.hasMoreElements()){
				QTIObject oDecvar = (QTIObject)enumDecvar.nextElement();
				eOutcomes.addContent(oDecvar.getXML());
			}
		}
		return eOutcomes;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		Vector vDecvar = new Vector();
		Iterator itDecvar = eElement.getChildren(DECVAR.getTagName(), eElement.getNamespace()).iterator();
		while (itDecvar.hasNext()){
			Element eDecvar = (Element)itDecvar.next();
			Decvar oDecvar = new Decvar(eDecvar);
			vDecvar.addElement(oDecvar);
		}
		setAttribute(DECVAR, vDecvar);
	}
	
	public Vector getDecvar(){
		return getAttributeVectorValue(DECVAR);
	}
	public void addDecvar(Decvar oDecvar){
		setAttribute(DECVAR, oDecvar);
	}
	
}
