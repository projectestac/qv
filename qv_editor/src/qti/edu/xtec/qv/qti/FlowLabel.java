/*
 * FlowLabel.java
 * 
 * Created on 09-feb-2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class FlowLabel extends QTISuper {

	public FlowLabel(){
		setAttribute(CONTENT, new Vector());
	}
	
	public FlowLabel(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eFlow = new Element(FLOW_LABEL.getTagName());
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
				if (FLOW_LABEL.equals(eContent.getName())){
					oContent = new FlowLabel(eContent);
				}else if (RESPONSE_LABEL.equals(eContent.getName())){
					oContent = new ResponseLabel(eContent);
				} 
				if (oContent!=null){				
					vContents.addElement(oContent);
				}
			}
			setAttribute(CONTENT, vContents);
		}
	}
	
	public Vector getFlowLabel(){
		Vector vFlowLabel = new Vector();
		Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
		while (enumContents.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof FlowLabel){
				vFlowLabel.addElement(oQTI);
			}
		}
		return vFlowLabel;		
	}
	public void addFlowLabel(FlowLabel oFlowLabel){
		setAttribute(CONTENT, oFlowLabel);
	}

	public Vector getResponseLabel(){
		Vector vResponseLabel = new Vector();
		Enumeration enumContents = getContents().elements();
		while (enumContents.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof ResponseLabel){
				vResponseLabel.addElement(oQTI);
			} else if (oQTI instanceof FlowLabel){
				vResponseLabel.addAll(((FlowLabel)oQTI).getResponseLabel());
			}
		}
		return vResponseLabel;
	}
	public void addResponseLabel(ResponseLabel oResponseLabel){
		setAttribute(CONTENT, oResponseLabel);
	}
	public void delResponseLabel(ResponseLabel oResponse){
		delAttributeElement(CONTENT, oResponse);
	}
}
