/*
 * QTISuperControl.java
 * 
 * Created on 11/agost/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public abstract class QTISuperControl extends QTISuper {

	public QTISuperControl(){
	}

	public QTISuperControl(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eControl = new Element(getControlTag());
		if (getAttribute(HINTSWITCH)!=null){
			eControl.setAttribute(HINTSWITCH.getTagName(), getHintswitch());
		}
		if (getAttribute(SOLUTIONSWITCH)!=null){
			eControl.setAttribute(SOLUTIONSWITCH.getTagName(), getSolutionswitch());
		}
		if (getAttribute(FEEDBACKSWITCH)!=null){
			eControl.setAttribute(FEEDBACKSWITCH.getTagName(), getFeedbackswitch());
		}
		if (getAttribute(INTERACTIONSWITCH)!=null){
			eControl.setAttribute(INTERACTIONSWITCH.getTagName(), getInteractionswitch());
		}
		if (getAttribute(VIEW)!=null){
			eControl.setAttribute(VIEW.getTagName(), getView());
		}
		return getSpecificXML(eControl);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			setAttribute(HINTSWITCH, eElement.getAttributeValue(HINTSWITCH.getTagName()));
			setAttribute(SOLUTIONSWITCH, eElement.getAttributeValue(SOLUTIONSWITCH.getTagName()));
			setAttribute(FEEDBACKSWITCH, eElement.getAttributeValue(FEEDBACKSWITCH.getTagName()));
			setAttribute(INTERACTIONSWITCH, eElement.getAttributeValue(INTERACTIONSWITCH.getTagName()));
			setAttribute(VIEW, eElement.getAttributeValue(VIEW.getTagName()));
			createSpecificFromXML(eElement);
		}
	}
	
	public String getHintswitch(){
		return getAttributeValue(HINTSWITCH);
	}
	public void setHintswitch(String sHint){
		setAttribute(HINTSWITCH, sHint);
	}

	public String getSolutionswitch(){
		return getAttributeValue(SOLUTIONSWITCH);
	}
	public void setSolutionswitch(String sSolution){
		setAttribute(SOLUTIONSWITCH, sSolution);
	}

	public String getFeedbackswitch(){
		return getAttributeValue(FEEDBACKSWITCH);
	}
	public void setFeedbackswitch(String sFeedback){
		setAttribute(FEEDBACKSWITCH, sFeedback);
	}

	public String getInteractionswitch(){
		return getAttributeValue(INTERACTIONSWITCH);
	}
	public void setInteractionswitch(String sInteraction){
		setAttribute(INTERACTIONSWITCH, sInteraction);
	}

	public String getView(){
		return getAttributeValue(VIEW);
	}
	public void setView(String sView){
		setAttribute(VIEW, sView);
	}

	protected abstract String getControlTag();
	protected abstract Element getSpecificXML(Element eControl);
	protected abstract void createSpecificFromXML(Element eElement);
	
}
