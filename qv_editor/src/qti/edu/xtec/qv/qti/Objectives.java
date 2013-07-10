/*
 * Objectives.java
 * 
 * Created on 13/agost/2004
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
public class Objectives extends QTISuper {

	public Objectives(){
		setAttribute(CONTENT, new Vector());
	}
	
	public Objectives(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eObjectives = new Element(getQTIConstant().getTagName());

		if (getAttribute(VIEW)!=null){
			eObjectives.setAttribute(VIEW.getTagName(), getView());
		}

		if (getQTIComment()!=null){
			eObjectives.addContent(getQTIComment().getXML()); 
		}
		
		Enumeration enumContents = getContents().elements();
		while (enumContents.hasMoreElements()){
			QTIObject oContent = (QTIObject)enumContents.nextElement();
			eObjectives.addContent(oContent.getXML());
		}
		return eObjectives;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			setAttribute(VIEW, eElement.getChildText(VIEW.getTagName()));
			
			// Add qticomment
			if (eElement.getChild(QTICOMMENT.getTagName(), eElement.getNamespace())!=null){
				QTIComment oComment = new QTIComment(eElement.getChild(QTICOMMENT.getTagName(), eElement.getNamespace()));
				setAttribute(QTICOMMENT, oComment);
			}
			
			// Add material and flow_mat
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
	
	public QTIComment getQTIComment(){
		QTIComment oComment = null;
		if (getAttribute(QTICOMMENT)!=null){
			oComment = (QTIComment)getAttribute(QTICOMMENT);
		}
		return oComment;
	}
	
	public String getView(){
		return getAttributeValue(VIEW);
	}

	public static QTIConstant getQTIConstant(){
		return OBJECTIVES;
	}

}
