/*
 * FlowMat.java
 * 
 * Created on 09/febrer/2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class FlowMat extends QTISuper {

	public FlowMat(){
		setAttribute(CONTENT, new Vector());
	}
	
	public FlowMat(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eFlow = new Element(FLOW_MAT.getTagName());
		Enumeration enumContents = ((Vector)getAttribute(CONTENT)).elements();
		while (enumContents.hasMoreElements()){
			QTIObject oContent = (QTIObject)enumContents.nextElement();
			eFlow.addContent(oContent.getXML());
		}
		return eFlow;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			Vector vContents = new Vector();
			Iterator itContents = eElement.getChildren().iterator();
			while (itContents.hasNext()){
				Element eContent = (Element)itContents.next();
				QTIObject oContent = null;
				if (FLOW_MAT.equals(eContent.getName())){
					oContent = new FlowMat(eContent);
				}else if (MATERIAL.equals(eContent.getName())){
					oContent = new Material(eContent);
				} 
				if (oContent!=null){				
					vContents.addElement(oContent);
				}
			}
			setAttribute(CONTENT, vContents);
		}
	}

	public String getShowName(){
		String sShowName = null;
		if (getAttribute(CONTENT)!=null && !getAttributeVectorValue(CONTENT).isEmpty()){
			sShowName = ((QTIObject)getAttributeVectorValue(CONTENT).firstElement()).getShowName();
		} 
		return sShowName;
	}	
	
	public Vector getMaterials(){
		Vector vMaterial = new Vector();
		Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
		while (enumContents.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof Material){
				vMaterial.addElement(oQTI);
			}
		}
		return vMaterial;
	}
	/**
	 * @return tots els materials (no només els del primer nivell)
	 */
	public Vector getAllMaterials(){
		Vector vMaterials = new Vector();
		Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
		while (enumContents.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof FlowMat){
				FlowMat oFlowMat = (FlowMat)oQTI;
				vMaterials.addAll(oFlowMat.getAllMaterials());
			} else if (oQTI instanceof Material){
				Material oMaterial = (Material)oQTI;
				vMaterials.addAll(oMaterial.getMaterials());
			}
		}
		return vMaterials;
	}
	public void addMaterial(Material oMaterial){
		setAttribute(CONTENT, oMaterial);
	}
	
	public Vector getFlowMats(){
		Vector vFlowMat = new Vector();
		Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
		while (enumContents.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof FlowMat){
				vFlowMat.addElement(oQTI);
			}
		}
		return vFlowMat;
	}
	public void addFlowMat(FlowMat oFlowMat){
		setAttribute(CONTENT, oFlowMat);
	}
	
	public Vector getContents(){
		return getAttributeVectorValue(CONTENT);
	}
	
}
