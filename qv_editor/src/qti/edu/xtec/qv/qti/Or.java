/*
 * Or.java
 * 
 * Created on 10/febrer/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class Or extends QTIBooleanOperator {

	public Or(){
	}
	
	public Or(Element eElement){
		super(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIBooleanOperator#getOperatorTag()
	 */
	protected String getOperatorTag(){
		return OR.getTagName();
	}

}
