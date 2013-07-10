/*
 * Flow.java
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
public class Flow extends QTISuper {

	public Flow(){
		setAttribute(CONTENT, new Vector());
	}
	
	public Flow(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eFlow = new Element(FLOW.getTagName());
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
				if (eContent.getName().equalsIgnoreCase(FLOW.getTagName())){
					oContent = new Flow(eContent);
				}else if (eContent.getName().equalsIgnoreCase(MATERIAL.getTagName())){
					oContent = new Material(eContent);
				}else if (eContent.getName().equalsIgnoreCase(RESPONSE_LID.getTagName())){
					oContent = new ResponseLID(eContent);
				}else if (eContent.getName().equalsIgnoreCase(RESPONSE_STR.getTagName())){
					oContent = new ResponseSTR(eContent);
				}else if (eContent.getName().equalsIgnoreCase(RESPONSE_NUM.getTagName())){
					oContent = new ResponseNUM(eContent);
				}else if (eContent.getName().equalsIgnoreCase(RESPONSE_GRP.getTagName())){
					oContent = new ResponseGRP(eContent);
				}else if (eContent.getName().equalsIgnoreCase(RESPONSE_XY.getTagName())){
					oContent = new ResponseXY(eContent);
				} 
				if (oContent!=null){				
					vContents.addElement(oContent);
				}
			}
			setAttribute(CONTENT, vContents);
		}
	}
	
	public Vector getContents(){
		return getAttributeVectorValue(CONTENT);
	}
	
	public Vector getFlows(){
		Vector vFlows = new Vector();
		Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
		while (enumContents.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof Flow){
				vFlows.addElement(oQTI);
			}
		}
		return vFlows;
	}
	public void addFlow(Flow oFlow){
		setAttribute(CONTENT, oFlow);
	}
	/**
	 * 
	 * @param oFlow
	 * @param iPosition posicio que ocupará dins tots els continguts. Comença per 0.
	 */
	public void addFlow(Flow oFlow, int iPosition){
		setAttribute(CONTENT, oFlow);
		setObjectPosition(getContents(), oFlow, getContentPosition(oFlow), iPosition);
	}

	public Vector getAllMaterials(){
		Vector vMaterial = new Vector();
		Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
		while (enumContents.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof Material){
				vMaterial.addElement(oQTI);
			}else if (oQTI instanceof Flow){
				vMaterial.addAll(((Flow)oQTI).getMaterials());
			}
		}
		return vMaterial;
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
	public void addMaterial(Material oMaterial){
		setAttribute(CONTENT, oMaterial);
	}

	public Vector getResponses(){
		Vector vResponses = new Vector();
		Enumeration enumContents = getContents().elements();
		while (enumContents.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof Flow){
				vResponses.addAll(((Flow)oQTI).getResponses());				
			}else if (oQTI instanceof QTISuperResponse){
				vResponses.addElement(oQTI);
			}
		}
		return vResponses;
	}
	public void setResponse(QTISuperResponse oOldResponse, QTISuperResponse oNewResponse){
		Vector vResponses = getContents();
		int iIndex = vResponses.indexOf(oOldResponse);
		if (iIndex>=0 && iIndex<vResponses.size()){
			vResponses.set(iIndex, oNewResponse);
		}
	}

	public boolean setResponse(QTISuperResponse oResponse){
		//TODO: modificar la resposta (sigui del tipus que sigui) -> problema cloze
		boolean bSet = false;
		if (oResponse!=null){
			if (oResponse.getIdent()==null){
				oResponse.setAttribute(IDENT, getRandomIdent());
			}
			boolean bFound = false;
			Enumeration enumContents = getContents().elements();
			while (enumContents.hasMoreElements()){
				QTIObject oQTI = (QTIObject)enumContents.nextElement();
				if (oQTI instanceof QTISuperResponse){
					QTISuperResponse tmpResponse = (QTISuperResponse)oQTI;
					if (oResponse.getIdent().equalsIgnoreCase(tmpResponse.getIdent())){
						setResponse(tmpResponse, oResponse);
						bSet = true;
					}
				}else if (oQTI instanceof Flow){
					bSet = ((Flow)oQTI).setResponse(oResponse);
				}
			}
		}
		return bSet;
	}
	public void addResponse(QTISuperResponse oResponse){
		if (!setResponse(oResponse)){
			setAttribute(CONTENT, oResponse);
		}
	}

	

	public String getShowName(){
		String sShowName = null;
		if (getAttribute(CONTENT)!=null && !getAttributeVectorValue(CONTENT).isEmpty()){
			sShowName = ((QTIObject)getAttributeVectorValue(CONTENT).firstElement()).getShowName();
		} 
		return sShowName;
	}	
	
}
