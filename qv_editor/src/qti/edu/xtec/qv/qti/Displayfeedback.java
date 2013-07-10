/*
 * Displayfeedback.java
 * 
 * Created on 10-feb-2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class Displayfeedback extends QTISuper {

	public Displayfeedback(String sLinkrefid){
		setAttribute(LINKREFID, sLinkrefid);
	}
	public Displayfeedback(String sFeedbacktype, String sLinkrefid){
		setAttribute(FEEDBACKTYPE, sFeedbacktype);
		setAttribute(LINKREFID, sLinkrefid);
	}
	
	public Displayfeedback(Element eElement){
		createFromXML(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eDisplayfeedback = new Element(DISPLAYFEEDBACK.getTagName());
		Object oFeedbacktype = getAttribute(FEEDBACKTYPE);
		if (oFeedbacktype==null) {
			oFeedbacktype = DEFAULT_FEEDBACKTYPE;
		}
		eDisplayfeedback.setAttribute(FEEDBACKTYPE.getTagName(), (String)oFeedbacktype);

		Object oLinkrefid = getAttribute(LINKREFID);
		if (oLinkrefid!=null) {
			eDisplayfeedback.setAttribute(LINKREFID.getTagName(), (String)oLinkrefid);
		}
		return eDisplayfeedback;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		String sFeedbacktype = eElement.getAttributeValue(FEEDBACKTYPE.getTagName());
		if (sFeedbacktype==null){
			sFeedbacktype = DEFAULT_FEEDBACKTYPE;
		}
		setAttribute(FEEDBACKTYPE, sFeedbacktype);
		setAttribute(LINKREFID, eElement.getAttributeValue(LINKREFID.getTagName()));
	}
	
	public String getFeedbacktype(){
		return getAttributeValue(FEEDBACKTYPE);
	}
	
	public String getLinkrefid(){
		return getAttributeValue(LINKREFID);
	}

}
