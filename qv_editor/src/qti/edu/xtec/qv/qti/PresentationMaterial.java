/*
 * PresentationMaterial.java
 * 
 * Created on 08/març/2004
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
public class PresentationMaterial extends QTISuper {

	public PresentationMaterial(){
		setAttribute(CONTENT, new Vector());
	}
	
	public PresentationMaterial(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element ePresentation = new Element(getQTIConstant().getTagName());
		Object oContents = getAttribute(CONTENT);
		if (oContents!=null){
			Enumeration enumContents = ((Vector)oContents).elements();
			while (enumContents.hasMoreElements()){
				QTIObject oContent = (QTIObject)enumContents.nextElement();
				ePresentation.addContent(oContent.getXML());
			}
		}
		return ePresentation;
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
				if (FLOW_MAT.equals(eContent.getName())){
					oContent = new FlowMat(eContent);
				}
				if (oContent!=null){
					vContents.addElement(oContent);
				}
			}
			setAttribute(CONTENT, vContents);
		}
	}
		
	public void addFlowMat(FlowMat oFlowMat){
		setAttribute(CONTENT, oFlowMat);
	}
	public void addFlowMat(FlowMat oFlowMat, int iPosition){
		if (iPosition<0 || iPosition>getContents().size()){
			iPosition = getContents().size();
		}
		getContents().insertElementAt(oFlowMat, iPosition);
	}
	public Vector getFlowMats(){
		Vector vFlowMats = new Vector();
		Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
		while (enumContents.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof FlowMat){
				vFlowMats.addElement(oQTI);
			}
		}
		return vFlowMats;
	}
	public FlowMat getFlowMat(int iIndex){
		FlowMat oFlowMat = null;
		Vector vFlowMats = getFlowMats();
		if (vFlowMats!=null && !vFlowMats.isEmpty() && iIndex>=0 && iIndex<vFlowMats.size()){
			oFlowMat = (FlowMat)vFlowMats.elementAt(iIndex);
		}
		return oFlowMat; 
	}
	public FlowMat delFlowMat(int iIndex){
		FlowMat oFlowMat = getFlowMat(iIndex);
		if (oFlowMat!=null){
			getContents().remove(oFlowMat);
		}
		return oFlowMat; 
	}
	public void setFlowMatPosition(int iOldPosition, int iNewPosition){
		setFlowMatPosition(getFlowMat(iOldPosition), iOldPosition, iNewPosition);
	}
	public void setFlowMatPosition(FlowMat oFlowMat, int iOldPosition, int iNewPosition){
		setObjectPosition(getContents(), oFlowMat, iOldPosition, iNewPosition);
	}
	
	public static QTIConstant getQTIConstant(){
		return PRESENTATION_MATERIAL;
	}

	
}
