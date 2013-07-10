/*
 * SelectionOrdering.java
 * 
 * Created on 01/setembre/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

import edu.xtec.qv.qti.util.QTIConstant;

/**
 * @author sarjona
 */
public class SelectionOrdering extends QTISuper {

	public SelectionOrdering(){
	}
	
	public SelectionOrdering(Element eElement){
		//logger.debug("PENDENT IMPLEMENTAR SelectionOrdering...");
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eSelectionOrdering = new Element(getQTIConstant().getTagName());
		
		if (getAttribute(SELECTION)!=null){
			eSelectionOrdering.addContent(getSelection().getXML());
		}		
		return eSelectionOrdering;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement.getChild(SELECTION.getTagName())!=null){
			Selection oSelection = new Selection(eElement.getChild(SELECTION.getTagName()));
			//new Outcomes(eElement.getChild(OUTCOMES.getTagName()));
			setAttribute(SELECTION, oSelection);
		}
	}
	
	public void setSelection(Selection oSelection){
		setAttribute(SELECTION, oSelection);
	}
	public Selection getSelection(){
		Selection oSelection = null;
		if (getAttribute(SELECTION)!=null){
			oSelection = (Selection)getAttribute(SELECTION);
		}
		return oSelection;
	}
	
	public static QTIConstant getQTIConstant(){
		return SELECTION_ORDERING;
	}
}
