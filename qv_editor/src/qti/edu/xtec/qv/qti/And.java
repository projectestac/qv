/*
 * And.java
 * 
 * Created on 10/febrer/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class And extends QTIBooleanOperator {

	public And(){
	}
	
	public And(Element eElement){
		super(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIBooleanOperator#getOperatorTag()
	 */
	protected String getOperatorTag(){
		return AND.getTagName();
	}
}
