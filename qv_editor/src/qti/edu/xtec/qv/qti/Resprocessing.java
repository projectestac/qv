/*
 * Resprocessing.java
 * 
 * Created on 10/febrer/2004
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
public class Resprocessing extends QTISuper {

	public Resprocessing(){
		setAttribute(RESPCONDITION, new Vector());
	}
	
	public Resprocessing(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eResprocessing = new Element(RESPROCESSING.getTagName());
		if (getAttribute(OUTCOMES)!=null){
			Outcomes oOutcomes = (Outcomes)getAttribute(OUTCOMES);
			eResprocessing.addContent(oOutcomes.getXML()); 
		}

		if (getAttribute(RESPCONDITION)!=null){
			Vector vRespcondition  = getAttributeVectorValue(RESPCONDITION);
			Enumeration enumRespcondition = vRespcondition.elements();
			while (enumRespcondition.hasMoreElements()){
				QTIObject oRespcondition = (QTIObject)enumRespcondition.nextElement();
				eResprocessing.addContent(oRespcondition.getXML());
			}
		}
		return eResprocessing;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement.getChild(OUTCOMES.getTagName())!=null){
			Outcomes oOutcomes = new Outcomes(eElement.getChild(OUTCOMES.getTagName(), eElement.getNamespace()));
			setAttribute(OUTCOMES, oOutcomes);
		}
		
		Vector vRespcondition = new Vector();
		Iterator itRespcondition = eElement.getChildren(RESPCONDITION.getTagName(), eElement.getNamespace()).iterator();
		while (itRespcondition.hasNext()){
			Element eRespcondition = (Element)itRespcondition.next();
			Respcondition oRespcondition = new Respcondition(eRespcondition);
			vRespcondition.addElement(oRespcondition);
		}
		setAttribute(RESPCONDITION, vRespcondition);
	}
	
	public Outcomes getOutcomes(){
		return (Outcomes)getAttribute(OUTCOMES);
	}
	public void setOutcomes(Outcomes oOutcomes){
		setAttribute(OUTCOMES, oOutcomes);
	}
	
	public Vector getRespcondition(){
		return getAttributeVectorValue(RESPCONDITION);
	}
	public Respcondition getRespcondition(String sVarname, String sValue){
		Respcondition oRespcondition = null;
		Enumeration enumRespconditions = getRespcondition().elements();
		while (enumRespconditions.hasMoreElements()){
			Respcondition tmpRespcondition = (Respcondition)enumRespconditions.nextElement();
			if (tmpRespcondition.containsSetvar(sVarname, sValue)){
				oRespcondition = tmpRespcondition;
				break;
			}
		}
		return oRespcondition;
	}
	public void addRespcondition(Respcondition oRespcondition){
		setAttribute(RESPCONDITION, oRespcondition);
	}

	public final static QTIConstant getQTIConstant(){
		return RESPROCESSING;
	}

		
}
