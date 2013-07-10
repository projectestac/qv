/*
 * Presentation.java
 * 
 * Created on 09/febrer/2004
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
public class Presentation extends QTISuper {

	public Presentation(){
		setAttribute(CONTENT, new Vector());
	}
	
	public Presentation(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element ePresentation = new Element(PRESENTATION.getTagName());
		if (getFlow()!=null){
			ePresentation.addContent(getFlow().getXML());
		}else{
			Object oContents = getAttribute(CONTENT);
			if (oContents!=null){
				Enumeration enumContents = ((Vector)oContents).elements();
				while (enumContents.hasMoreElements()){
					QTIObject oContent = (QTIObject)enumContents.nextElement();
					ePresentation.addContent(oContent.getXML());
				}
			}
		}
		return ePresentation;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			if (eElement.getChild(FLOW.getTagName())!=null){
				setAttribute(FLOW, new Flow(eElement.getChild(FLOW.getTagName())));
			}else{
				Vector vContents = new Vector();
				Iterator itContent = eElement.getChildren().iterator();
				while (itContent.hasNext()){
					Element eContent = (Element)itContent.next();
					QTIObject oContent = null;
					if (MATERIAL.equals(eContent.getName())){
						oContent = new Material(eContent);
					} else if (RESPONSE_LID.equals(eContent.getName())){
						oContent = new ResponseLID(eContent);
					} else if (RESPONSE_STR.equals(eContent.getName())){
						oContent = new ResponseSTR(eContent);
					} else if (RESPONSE_NUM.equals(eContent.getName())){
						oContent = new ResponseNUM(eContent);
					} else if (RESPONSE_GRP.equals(eContent.getName())){
						oContent = new ResponseGRP(eContent);
					} else if (RESPONSE_XY.equals(eContent.getName())){
						oContent = new ResponseXY(eContent);
					}
					if (oContent!=null){
						vContents.addElement(oContent);
					}
				}
				setAttribute(CONTENT, vContents);
			}
		}
	}

	public Flow getFlow(){
		Flow oFlow = null;
		Object tmpFlow = getAttribute(FLOW);
		if (tmpFlow!=null){
			oFlow = (Flow)tmpFlow;
		}
		return oFlow;
	}
	public void setFlow(Flow oFlow){
		setAttribute(FLOW, oFlow);
	}

	public Vector getMaterials(){
		Vector vMaterial = new Vector();
		if (getFlow()!=null){
			vMaterial = getFlow().getMaterials();
		}else{
			Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
			while (enumContents.hasMoreElements()){
				QTIObject oQTI = (QTIObject)enumContents.nextElement();
				if (oQTI instanceof Material){
					vMaterial.addElement(oQTI);
				}
			}
		}
		return vMaterial;
	}
	/**
	 * 
	 * @return tots els materials (no només els del primer nivell)
	 */
	public Vector getAllMaterials(){
		Vector vMaterial = new Vector();
		if (getFlow()!=null){
			vMaterial = getFlow().getAllMaterials();
		}else{
			Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
			while (enumContents.hasMoreElements()){
				QTIObject oQTI = (QTIObject)enumContents.nextElement();
				if (oQTI instanceof Material){
					vMaterial.addElement(oQTI);
				}
			}
		}
		return vMaterial;
	}
	public Material getMaterial(int iIndex){
		Material oMaterial = null;
		Vector vMaterials = getMaterials();
		if (vMaterials!=null && iIndex<vMaterials.size()){
			oMaterial = (Material)vMaterials.elementAt(iIndex);
		}
		return oMaterial;
	}
	public void addMaterial(Material oMaterial){
		if (getFlow()!=null){
			getFlow().addMaterial(oMaterial);
		}else{
			setAttribute(CONTENT, oMaterial);
		}
	}

	public QTISuperResponse getResponse(String sIdent){
		QTISuperResponse oResponse = null;
		Enumeration enumResponse = getResponses().elements();
		while (enumResponse.hasMoreElements()){
			QTISuperResponse tmpResponse = (QTISuperResponse)enumResponse.nextElement();
			if (tmpResponse.getIdent().equals(sIdent)){
				oResponse = tmpResponse;
				break;
			}
		}
		return oResponse;
	}
	public QTISuperResponse getResponse(int iPosition){
		QTISuperResponse oResponse = null;
		Object o = getResponses().elementAt(iPosition);
		if (o!=null){
			oResponse =(QTISuperResponse)o;
		}
		return oResponse;
	}
	public void setResponse(QTISuperResponse oOldResponse, QTISuperResponse oNewResponse){
		Vector vResponses = new Vector();
		if (getFlow()!=null){
			vResponses = getFlow().getContents();
		}else{
			vResponses = getContents();
		}
		int iIndex = vResponses.indexOf(oOldResponse);
		if (iIndex>0 && iIndex<vResponses.size()){
			vResponses.set(iIndex, oNewResponse);
		}
	}
	public int getResponsePosition(String sIdent){
		int iPosicio = -1;
		if (sIdent!=null){
			//iPosicio = getContentPosition(getResponseLabel(sId));
			Vector vResponses = new Vector();
			if (getFlow()!=null){
				vResponses = getFlow().getContents();
			}else{
				vResponses = getContents();
			}
			Enumeration enumContents = vResponses.elements();
			while (enumContents.hasMoreElements()){
				QTIObject oQTI = (QTIObject)enumContents.nextElement();
				if (sIdent.equalsIgnoreCase(oQTI.getIdent())){
					iPosicio = vResponses.indexOf(oQTI);
					break;
				}
			}
		}
		return iPosicio;
	}
	
	public void setResponsePosition(QTISuperResponse oResponse, int iOldPosition, int iNewPosition){
		//logger.debug("setResponsePosition-> old="+iOldPosition+" new="+iNewPosition+"  responses="+vResponses);
		if (oResponse!=null){
			int iAbsoluteOldPosition = getResponsePosition(oResponse.getIdent());
			int iAbsoluteNewPosition = iAbsoluteOldPosition+(iNewPosition-iOldPosition);
			//logger.debug("Absolute-> old="+iAbsoluteOldPosition+"  new="+iAbsoluteNewPosition);
			if (getFlow()!=null){
				setObjectPosition(getFlow().getContents(), oResponse, iAbsoluteOldPosition, iAbsoluteNewPosition);
			}else{
				setObjectPosition(getContents(), oResponse, iAbsoluteOldPosition, iAbsoluteNewPosition);
			}
		}
	}

	public void delResponse(String sIdent){
		QTISuperResponse oResponse = getResponse(sIdent);
		if (oResponse!=null){
			Vector vResponses = new Vector();
			if (getFlow()!=null){
				vResponses = getFlow().getContents();
			}else{
				vResponses = getContents();
			}
			if (vResponses!=null){
				vResponses.remove(oResponse);
			}
		}
	}
	
	public Vector getResponses(){
		Vector vResponse = new Vector();
		if (getFlow()!=null){
			vResponse = getFlow().getResponses();
		}else{
			Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
			while (enumContents.hasMoreElements()){
				QTIObject oQTI = (QTIObject)enumContents.nextElement();
				if (oQTI instanceof QTISuperResponse){
					vResponse.addElement(oQTI);
				}
			}
		}
		return vResponse;
	}
	public void addResponse(QTISuperResponse oResponse){
		if (getFlow()!=null){
			getFlow().addResponse(oResponse);
		}else{
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

	public String getText(){
		String sText = new String();
		Enumeration enumMaterials = getMaterials().elements();
		while (enumMaterials.hasMoreElements()){
			Material oMaterial = (Material)enumMaterials.nextElement();
			sText+= oMaterial.getText();
		}
		return sText;
	}

	public final static QTIConstant getQTIConstant(){
		return PRESENTATION;
	}

	
}
