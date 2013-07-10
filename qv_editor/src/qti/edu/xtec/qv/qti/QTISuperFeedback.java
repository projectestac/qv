/*
 * QTISuperFeedback.java
 * 
 * Created on 01/setembre/2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

/**
 * @author sarjona
 */
public abstract class QTISuperFeedback extends QTISuper {

	public QTISuperFeedback(){
		setAttribute(CONTENT, new Vector());
	}
	
	public QTISuperFeedback(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eFeedback = new Element(getFeedbackTag());
		//Attributes
		if (getAttribute(IDENT)!=null){
			eFeedback.setAttribute(IDENT.getTagName(), getIdent());
		}
		if (getAttribute(TITLE)!=null){
			eFeedback.setAttribute(TITLE.getTagName(), getTitle());
		}
		if (getAttribute(VIEW)!=null){
			eFeedback.setAttribute(VIEW.getTagName(), getView());
		}

		// flow_mat and material
		Enumeration enumContents = getContents().elements();
		while (enumContents.hasMoreElements()){
			QTIObject oContent = (QTIObject)enumContents.nextElement();
			eFeedback.addContent(oContent.getXML());
		}
		return getSpecificXML(eFeedback);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			setAttribute(IDENT, eElement.getChildText(IDENT.getTagName()));
			setAttribute(TITLE, eElement.getChildText(TITLE.getTagName()));
			setAttribute(VIEW, eElement.getChildText(VIEW.getTagName()));
			
			// qticomment
			addFromXML(eElement, this, QTIComment.getQTIConstant());
			
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
			createSpecificFromXML(eElement);
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

	protected abstract String getFeedbackTag();
	protected abstract Element getSpecificXML(Element eFeedback);
	protected abstract void createSpecificFromXML(Element eElement);

}
