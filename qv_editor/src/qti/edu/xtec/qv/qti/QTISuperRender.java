/*
 * QTISuperRender.java
 * 
 * Created on 02/març/2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

/**
 * @author sarjona
 */
public abstract class QTISuperRender extends QTISuper {

	public QTISuperRender(){
		setAttribute(CONTENT, new Vector());
	}

	public QTISuperRender(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eRender = new Element(getRenderTag());
		Enumeration enumContents = ((Vector)getAttribute(CONTENT)).elements();
		while (enumContents.hasMoreElements()){
			QTIObject oContent = (QTIObject)enumContents.nextElement();
			eRender.addContent(oContent.getXML());
		}
		if (getAttribute(DISPLAY)!=null){
			eRender.setAttribute(DISPLAY.getTagName(), getDisplay());
		}
		if (getAttribute(STYLE)!=null){
			eRender.setAttribute(STYLE.getTagName(), getStyle());
		}
		if (getAttribute(MAXNUMBER)!=null){
			eRender.setAttribute(MAXNUMBER.getTagName(), getAttributeValue(MAXNUMBER));
		}
		if (getAttribute(MINNUMBER)!=null){
			eRender.setAttribute(MINNUMBER.getTagName(), getAttributeValue(MINNUMBER));
		}
		return getSpecificXML(eRender);
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
				if (MATERIAL.equals(eContent.getName())){
					oContent = new Material(eContent);
				} else if (RESPONSE_LABEL.equals(eContent.getName())){
					oContent = new ResponseLabel(eContent);
				} else if (FLOW_LABEL.equals(eContent.getName())){
					oContent = new FlowLabel(eContent);
				}
				if (oContent!=null){
					vContents.addElement(oContent);
				}
			}
			setAttribute(CONTENT, vContents);

			setAttribute(DISPLAY, eElement.getAttributeValue(DISPLAY.getTagName()));
			setAttribute(MAXNUMBER, eElement.getAttributeValue(MAXNUMBER.getTagName()));
			setAttribute(MINNUMBER, eElement.getAttributeValue(MINNUMBER.getTagName()));
			setAttribute(STYLE, eElement.getAttributeValue(STYLE.getTagName()));
			createSpecificFromXML(eElement);
		}
	}
	
	public Vector getMaterial(){
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
	public Material getMaterial(int iIndex){
		Material oMaterial = null;
		Vector vMaterials = getMaterial();
		if (vMaterials!=null && !vMaterials.isEmpty() && iIndex>=0 && iIndex<vMaterials.size()){
			oMaterial = (Material)vMaterials.elementAt(iIndex);
		}
		return oMaterial; 
	}
	public void addMaterial(Material oMaterial){
		setAttribute(CONTENT, oMaterial);
	}
	public void setMaterialPosition(int iOldPosition, int iNewPosition){
		setMaterialPosition(getMaterial(iOldPosition), iOldPosition, iNewPosition);
	}
	public void setMaterialPosition(Material oMaterial, int iOldPosition, int iNewPosition){
		int iAbsoluteOldPosition = getContentPosition(oMaterial);
		int iAbsoluteNewPosition = iAbsoluteOldPosition+(iNewPosition-iOldPosition);
		setObjectPosition(getContents(), oMaterial, iAbsoluteOldPosition, iAbsoluteNewPosition);
	}
	public void delMaterials(){
		Enumeration enumContents = getContents().elements();
		while (enumContents.hasMoreElements()){
			Object o = enumContents.nextElement();
			if (o instanceof Material){
				delAttributeElement(CONTENT, o);
			}
		}
	}
	public Material delMaterial(int iIndex){
		Material oMaterial = getMaterial(iIndex);
		if (oMaterial!=null){
			getContents().remove(oMaterial);
		}
		return oMaterial; 
	}

	public ResponseLabel getResponseLabel(String sIdent){
		ResponseLabel oResponseLabel = null;
		if (sIdent!=null) {
			Enumeration enumResponseLabel = getResponseLabel().elements();
			while (enumResponseLabel.hasMoreElements()){
				ResponseLabel tmpResponseLabel = (ResponseLabel)enumResponseLabel.nextElement();
				if (sIdent.equalsIgnoreCase(tmpResponseLabel.getIdent())){
					oResponseLabel = tmpResponseLabel;
					break;
				}
			}
		}
		return oResponseLabel;
	}
	public ResponseLabel getResponseLabel(int iIndex){
		ResponseLabel oResponseLabel = null;
		Vector vResponseLabel = getResponseLabel();
		if (vResponseLabel!=null && !vResponseLabel.isEmpty() && iIndex<vResponseLabel.size()){
			oResponseLabel = (ResponseLabel)vResponseLabel.elementAt(iIndex);
		}
		return oResponseLabel; 
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
	public Vector getResponseLabelOld(){
		Vector vResponseLabel = new Vector();
		if (!getFlowLabel().isEmpty()){
			vResponseLabel = ((FlowLabel)getFlowLabel().firstElement()).getResponseLabel();
		} else{
			Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
			while (enumContents.hasMoreElements()){
				QTIObject oQTI = (QTIObject)enumContents.nextElement();
				if (oQTI instanceof ResponseLabel){
					vResponseLabel.addElement(oQTI);
				}
			}
		}
		return vResponseLabel;
	}
	/**
	 * @param sId identificador de l'objecte del que es dessitja coneixer la posicio
	 * @return la posicio dintre del Vector de l'objecte especificat
	 */
	public int getResponseLabelPosition(String sId){
		int iPosicio = -1;
		if (sId!=null){
			//iPosicio = getContentPosition(getResponseLabel(sId));
			Enumeration enumContents = getResponseLabel().elements();
			while (enumContents.hasMoreElements()){
				QTIObject oQTI = (QTIObject)enumContents.nextElement();
				if (sId.equalsIgnoreCase(oQTI.getIdent())){
					iPosicio = getResponseLabel().indexOf(oQTI);
					break;
				}
			}
			//logger.debug("pos="+iPosicio+"  id="+sId+"  respLab="+getResponseLabel(sId));
		}
		return iPosicio;
	}
	public void setResponseLabelPosition(int iOldPosition, int iNewPosition){
		setResponseLabelPosition(getResponseLabel(iOldPosition), iOldPosition, iNewPosition);
	}
	public void setResponseLabelPosition(ResponseLabel oResponseLabel, int iOldPosition, int iNewPosition){
		int iAbsoluteOldPosition = getResponseLabelPosition(oResponseLabel.getIdent());
		int iAbsoluteNewPosition = iAbsoluteOldPosition+(iNewPosition-iOldPosition);
		//setObjectPosition(getResponseLabel(), oResponseLabel, iAbsoluteOldPosition, iAbsoluteNewPosition);
		if (getFlowLabel().isEmpty()){
			setObjectPosition(getContents(), oResponseLabel, iAbsoluteOldPosition, iAbsoluteNewPosition);
		}else{
			FlowLabel oFlowLabel = (FlowLabel)getFlowLabel().firstElement();
			setObjectPosition(oFlowLabel.getContents(), oResponseLabel, iAbsoluteOldPosition, iAbsoluteNewPosition);
		}
	}
	
	
	public void addResponseLabel(ResponseLabel oResponse){
		if (!setResponseLabel(oResponse)){
			if (oResponse!=null){
				setAttribute(CONTENT, oResponse);
			}
		}
	}
	public void addResponseLabel(ResponseLabel oResponse, int iPosition){
		if (oResponse!=null){
			getResponseLabel().insertElementAt(oResponse, iPosition);
			if (getFlowLabel().isEmpty()){
				getContents().insertElementAt(oResponse, iPosition);
			}else{
				FlowLabel oFlowLabel = (FlowLabel)getFlowLabel().firstElement();
				oFlowLabel.getContents().insertElementAt(oResponse, iPosition);
			}
			//getContents().insertElementAt(oResponse, iPosition);
		}
	}
	public void addResponseLabel(String sIdentResp){
		ResponseLabel oResponseLabel = getResponseLabel(sIdentResp); 
		if (oResponseLabel==null){
			oResponseLabel = new ResponseLabel(sIdentResp);
			addResponseLabel(oResponseLabel);
		}
	}
	public ResponseLabel addResponseLabelWithMattext(String sIdentResp, String sText, String sAlignment){
		ResponseLabel oResponseLabel = getResponseLabel(sIdentResp); 
		if (oResponseLabel!=null){
			oResponseLabel.addMattext(sText, sAlignment);
		} else{
			oResponseLabel = ResponseLabel.createWithMattext(sIdentResp, sText, sAlignment);
			if (getFlowLabel()!=null && !getFlowLabel().isEmpty()){
				((FlowLabel)getFlowLabel().firstElement()).addResponseLabel(oResponseLabel);
			}else{
				addResponseLabel(oResponseLabel);			
			}
		}
		return oResponseLabel;
	}
	public ResponseLabel addResponseLabelWithMatimage(String sIdentResp, String sImagtype, String sURI, String sAlignment){
		return addResponseLabelWithMatimage(sIdentResp, sImagtype, sURI, sAlignment, -1, -1);
	}
	
	public ResponseLabel addResponseLabelWithMatimage(String sIdentResp, String sImagtype, String sURI, String sAlignment, int iWidth, int iHeight){
		ResponseLabel oResponseLabel = getResponseLabel(sIdentResp); 
		if (oResponseLabel!=null){
			oResponseLabel.addMatimage(sImagtype, sURI, sAlignment, iWidth, iHeight);
		} else{
			oResponseLabel = ResponseLabel.createWithMatimage(sIdentResp, sImagtype, sURI, sAlignment, iWidth, iHeight);
			if (getFlowLabel()!=null && !getFlowLabel().isEmpty()){
				((FlowLabel)getFlowLabel().firstElement()).addResponseLabel(oResponseLabel);
			}else{
				addResponseLabel(oResponseLabel);			
			}
		}
		return oResponseLabel;
	}
	
	public ResponseLabel addResponseLabelWithMataudio(String sIdentResp, String sURI, String sAlignment){
		ResponseLabel oResponseLabel = getResponseLabel(sIdentResp); 
		if (oResponseLabel!=null){
			oResponseLabel.addMataudio(sURI, sAlignment);
		} else{
			oResponseLabel = ResponseLabel.createWithMataudio(sIdentResp, sURI, sAlignment);
			if (getFlowLabel()!=null && !getFlowLabel().isEmpty()){
				((FlowLabel)getFlowLabel().firstElement()).addResponseLabel(oResponseLabel);
			}else{
				addResponseLabel(oResponseLabel);			
			}
		}
		return oResponseLabel;
	}
	
	
	public boolean setResponseLabel(ResponseLabel oNew){
		return (oNew!=null && setResponseLabel(oNew.getIdent(), oNew));
	}
	public boolean setResponseLabel(ResponseLabel oOld, ResponseLabel oNew){
		return (oOld!=null && setResponseLabel(oOld.getIdent(), oNew));
	}
	public boolean setResponseLabel(String sIdent, ResponseLabel oNew){
		return setResponseLabel(getContents(), sIdent, oNew);
	}
	public boolean setResponseLabel(Vector vContents, String sIdent, ResponseLabel oNew){
		boolean bSet = false;
		int iIndex = 0;
		Enumeration enumContents = vContents.elements();
		while (enumContents.hasMoreElements() && !bSet){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof ResponseLabel){
				if (oQTI.getIdent().equals(sIdent)){
					vContents.set(iIndex, oNew);
					bSet = true;
				}
			} else if (oQTI instanceof FlowLabel){
				bSet = setResponseLabel(((FlowLabel)oQTI).getContents(), sIdent, oNew);
			}
			iIndex++;
		}
		return bSet;
	}
	
	public void delResponseLabel(ResponseLabel oResponse){
		if (!getFlowLabel().isEmpty()){
			((FlowLabel)getFlowLabel().firstElement()).delResponseLabel(oResponse);
		} else{		
			delAttributeElement(CONTENT, oResponse);
		}
	}
	public void delResponseLabel(String sIdentResp){
		delResponseLabel(getResponseLabel(sIdentResp));
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
	public void addFlowLabel(FlowLabel oFlow){
		setAttribute(CONTENT, oFlow);
	}

	public String getDisplay(){
		return getAttributeValue(DISPLAY);
	}
	public void setDisplay(String sMode){
		setAttribute(DISPLAY, sMode);
	}
	
	public String getStyle(){
		return getAttributeValue(STYLE);
	}
	public void setStyle(String sStyle){
		setAttribute(STYLE, sStyle);
	}
	
	public int getMinNumber(){
		return getAttributeIntValue(MINNUMBER);
	}
	public void setMinNumber(int i){
		setAttribute(MINNUMBER, String.valueOf(i));
	}
	
	public int getMaxNumber(){
		return getAttributeIntValue(MAXNUMBER);
	}
	public void setMaxNumber(int i){
		setAttribute(MAXNUMBER, String.valueOf(i));
	}
	protected abstract String getRenderTag();
	protected abstract Element getSpecificXML(Element eRender);
	protected abstract void createSpecificFromXML(Element eElement);
	
}
