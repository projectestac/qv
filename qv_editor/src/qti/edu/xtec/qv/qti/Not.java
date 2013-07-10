/*
 * Not.java
 * 
 * Created on 10/febrer/2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Vector;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class Not extends QTIBooleanOperator {

	public Not(){
	}
	
	public Not(Element eElement){
		super(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIBooleanOperator#getOperatorTag()
	 */
	protected String getOperatorTag(){
		return NOT.getTagName();
	}
	
	public Element getXML() {
		Element eOperator = null;
		if (!((Vector)getAttribute(CONTENT)).isEmpty()){
			eOperator = new Element(getOperatorTag());
			Enumeration enumContents = ((Vector)getAttribute(CONTENT)).elements();
			while (enumContents.hasMoreElements()){
				QTIObject oContent = (QTIObject)enumContents.nextElement();
				eOperator.addContent(oContent.getXML());
			}
		}
		return eOperator;
	}
	
	
}
