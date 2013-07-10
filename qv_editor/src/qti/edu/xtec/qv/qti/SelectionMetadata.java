/*
 * OutcomesProcessing.java
 * 
 * Created on 14/mig/2008
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
public class SelectionMetadata extends QTISuper {

	public SelectionMetadata(String sMdName, String sMdOperator, String sText){
		setMdName(sMdName);
		setMdOperator(sMdOperator);
		setText(sText);
	}
	
	public SelectionMetadata(Element eElement){
		createFromXML(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eSelectionMetadata = new Element(getQTIConstant().getTagName());
		if (getAttribute(MDNAME)!=null){
			eSelectionMetadata.setAttribute(MDNAME.getTagName(), getAttributeValue(MDNAME));
		}
		if (getAttribute(MDOPERATOR)!=null){
			eSelectionMetadata.setAttribute(MDOPERATOR.getTagName(), getAttributeValue(MDOPERATOR));
		}
		if (getAttribute(TEXT)!=null){
			eSelectionMetadata.addContent(getAttributeValue(TEXT));
		}
		return eSelectionMetadata;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		setAttribute(MDNAME, eElement.getAttributeValue(MDNAME.getTagName()));
		setAttribute(MDOPERATOR, eElement.getAttributeValue(MDOPERATOR.getTagName()));
		setAttribute(TEXT, eElement.getText());
	}
	
	public String getMdName(){
		return getAttributeValue(MDNAME);
	}
	public void setMdName(String sMdName){
		setAttribute(MDNAME, sMdName);
	}

	public String getMdOperator(){
		return getAttributeValue(MDOPERATOR);
	}
	public void setMdOperator(String sMdOperator){
		setAttribute(MDOPERATOR, sMdOperator);
	}

	public String getText(){
		return getAttributeValue(TEXT);
	}
	public void setText(String sText){
		setAttribute(TEXT, sText);
	}

	public static QTIConstant getQTIConstant(){
		return SELECTION_METADATA;
	}

}
