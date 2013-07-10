/*
 * Sectionfeedback.java
 * 
 * Created on 01/setembre/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

import edu.xtec.qv.qti.util.QTIConstant;

/**
 * @author sarjona
 */
public class Sectionfeedback extends QTISuperFeedback {

	public Sectionfeedback(){
		super();
	}
	
	public Sectionfeedback(Element eElement){
		super(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperFeedback#getSpecificXML(org.jdom.Element)
	 */
	protected Element getSpecificXML(Element eFeedback) {
		//qticomment
		if (getQTIComment()!=null){
			eFeedback.addContent(getQTIComment().getXML()); 
		}
		return eFeedback;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperFeedback#createSpecificFromXML(org.jdom.Element)
	 */
	protected void createSpecificFromXML(Element eElement) {
		// qticomment
		addFromXML(eElement, this, QTIComment.getQTIConstant());
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperFeedback#getFeedbackTag()
	 */
	protected String getFeedbackTag() {
		return getQTIConstant().getTagName();
	}

	public static QTIConstant getQTIConstant(){
		return SECTIONFEEDBACK;
	}	
	
	public QTIComment getQTIComment(){
		QTIObject o = getQTIObject(this, QTIComment.getQTIConstant());
		return (o!=null?(QTIComment)o:null);
	}
	

}
