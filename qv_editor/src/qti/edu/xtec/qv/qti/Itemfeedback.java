/*
 * Itemfeedback.java
 * 
 * Created on 10/febrer/2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

import edu.xtec.qv.qti.util.QTIConstant;

/**
 * @author sarjona
 */
public class Itemfeedback extends QTISuper {

	public Itemfeedback(String sIdent){
		if (sIdent==null || sIdent.trim().length()==0){
			sIdent = getRandomIdent();
		}
		setAttribute(IDENT, sIdent);
		setAttribute(CONTENT, new Vector());
	}
	
	public Itemfeedback(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eItemfeedback = new Element(ITEMFEEDBACK.getTagName());
		Object oIdent =  getAttribute(IDENT);
		if (oIdent==null){
			oIdent = getRandomIdent();
		}
		eItemfeedback.setAttribute(IDENT.getTagName(), (String)oIdent);

		Object oTitle =  getAttribute(TITLE);
		if (oTitle!=null){
			eItemfeedback.setAttribute(TITLE.getTagName(), (String)oTitle);
		}

		Object oContents = getAttribute(CONTENT);
		if (oContents!=null){
			Enumeration enumContents = ((Vector)oContents).elements();
			while (enumContents.hasMoreElements()){
				QTIObject oContent = (QTIObject)enumContents.nextElement();
				eItemfeedback.addContent(oContent.getXML());
			}
		}
		return eItemfeedback;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			Vector vContents = new Vector();
			Iterator itContent = eElement.getChildren().iterator();
			while (itContent.hasNext()){
				Element eContent = (Element)itContent.next();
				QTIObject oContent = null;
				if (eContent.getName().equalsIgnoreCase(FLOW_MAT.getTagName())){
					oContent = new FlowMat(eContent);
				} else if (eContent.getName().equalsIgnoreCase(MATERIAL.getTagName())){
					oContent = new Material(eContent);
				}
				if (oContent!=null){
					vContents.addElement(oContent);
				}
			}
			setAttribute(CONTENT, vContents);
			setAttribute(IDENT, eElement.getAttributeValue(IDENT.getAttributeName()));
			setAttribute(TITLE, eElement.getAttributeValue(TITLE.getAttributeName()));
		}
	}
	
	public void setTitle(String sTitle){
		setAttribute(TITLE, sTitle);
	}
	
	public Vector getFlowMat(){
		Vector vFlowMat = new Vector();
		Enumeration enumContents = getContents().elements();
		while (enumContents.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof FlowMat){
				vFlowMat.addElement(oQTI);
			}
		}
		return vFlowMat;
	}
	public void addFlowMat(FlowMat oFlow){
		setAttribute(CONTENT, oFlow);
	}
	
	public Vector getMaterials(){
		Vector vMaterial = new Vector();
		Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
		while (enumContents.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof Material){
				vMaterial.addElement(oQTI);
			} else if (oQTI instanceof FlowMat){
				vMaterial.addAll(((FlowMat)oQTI).getMaterials());
			}
		}
		return vMaterial;
	}
	public void addMaterial(Material oMaterial){
		setAttribute(CONTENT, oMaterial);
	}
	
	public static Itemfeedback createWithMattext(String sIdent, String sFeedback){
		Itemfeedback oItemfeedback = new Itemfeedback(sIdent);
		Material oMaterial = Material.createWithMattext(sFeedback);
		oItemfeedback.addMaterial(oMaterial);
		return oItemfeedback;
	}
	
	public String getText(){
		String sText = new String();
		Enumeration enumMaterials = getMaterials().elements();
		while (enumMaterials.hasMoreElements()){
			Material oMaterial = (Material)enumMaterials.nextElement();
			sText+= oMaterial.getText();
		}
		return sText;
	}

	public void setText(String sText){
		setAttribute(CONTENT, new Vector());
		Material oMaterial = Material.createWithMattext(sText);
		addMaterial(oMaterial);
	}

	public final static QTIConstant getQTIConstant(){
		return ITEMFEEDBACK;
	}


}
