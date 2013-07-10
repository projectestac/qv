/*
 * OrSelection.java
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
public class OrSelection extends QTISuper {

	public OrSelection(){
		logger.debug("PENDENT IMPLEMENTAR OrSelection...");
	}
	
	public OrSelection(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eOrSelection = new Element(getQTIConstant().getTagName());
		if (getAttribute(SELECTION_METADATA)!=null){
			Enumeration enumSelectionMetadata = getSelectionMetadata().elements();
			while (enumSelectionMetadata.hasMoreElements()){
				SelectionMetadata oSelectionMetadata = (SelectionMetadata)enumSelectionMetadata.nextElement();
				eOrSelection.addContent(oSelectionMetadata.getXML());
			}
		}
		return eOrSelection;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement.getChildren(SELECTION_METADATA.getTagName(), eElement.getNamespace())!=null){
			Vector vSelectionMetadata = new Vector();
			Iterator itSelectionMetadata = eElement.getChildren(SELECTION_METADATA.getTagName(), eElement.getNamespace()).iterator();
			while (itSelectionMetadata.hasNext()){
				Element eSelectionMetadata = (Element)itSelectionMetadata.next();
				SelectionMetadata oSelectionMetadata = new SelectionMetadata(eSelectionMetadata);
				vSelectionMetadata.addElement(oSelectionMetadata);
			}
			setAttribute(SELECTION_METADATA, vSelectionMetadata);
		}
	}
	
	public Vector getSelectionMetadata(){
		Vector vSelectionMetadata = new Vector();
		if (getAttribute(SELECTION_METADATA)!=null){
			vSelectionMetadata = getAttributeVectorValue(SELECTION_METADATA);
		}
		return vSelectionMetadata;
	}
	public void addSelectionMetadata(SelectionMetadata oSelectionMetadata){
		if (getAttribute(SELECTION_METADATA)==null){
			setAttribute(SELECTION_METADATA, new Vector());
		}
		setAttribute(SELECTION_METADATA, oSelectionMetadata);
	}
	
	public static QTIConstant getQTIConstant(){
		return OR_SELECTION;
	}
}
