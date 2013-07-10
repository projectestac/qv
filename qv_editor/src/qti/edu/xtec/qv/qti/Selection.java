/*
 * Selection.java
 * 
 * Created on 14/maig/2008
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

import edu.xtec.qv.qti.util.QTIConstant;

/**
 * @author allastarri
 */
public class Selection extends QTISuper {

	public Selection(){
	}
	
	public Selection(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eSelection = new Element(getQTIConstant().getTagName());
		if (getAttribute(OR_SELECTION)!=null){
			Enumeration enumOrSelection = getOrSelection().elements();
			while (enumOrSelection.hasMoreElements()){
				OrSelection oOrSelection = (OrSelection)enumOrSelection.nextElement();
				eSelection.addContent(oOrSelection.getXML());
			}
		}
		return eSelection;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement.getChildren(OR_SELECTION.getTagName(), eElement.getNamespace())!=null){
			Vector vOrSelection = new Vector();
			Iterator itOrSelection = eElement.getChildren(OR_SELECTION.getTagName(), eElement.getNamespace()).iterator();
			while (itOrSelection.hasNext()){
				Element eOrSelection = (Element)itOrSelection.next();
				OrSelection oOrSelection = new OrSelection(eOrSelection);
				vOrSelection.addElement(oOrSelection);
			}
			setAttribute(OR_SELECTION, vOrSelection);
		}
	}
	
	public Vector getOrSelection(){
		Vector vOrSelection = new Vector();
		if (getAttribute(OR_SELECTION)!=null){
			vOrSelection = getAttributeVectorValue(OR_SELECTION);
		}
		return vOrSelection;
	}
	public void addOrSelection(OrSelection oOrSelection){
		if (getAttribute(OR_SELECTION)==null){
			setAttribute(OR_SELECTION, new Vector());
		}
		setAttribute(OR_SELECTION, oOrSelection);
	}
	
	public static QTIConstant getQTIConstant(){
		return SELECTION;
	}
}
