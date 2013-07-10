/*
 * QTIResponseNUM.java
 * 
 * Created on 04/maig/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class ResponseNUM extends QTISuperResponse {

	public ResponseNUM(String sIdent, String sCardinality, String sTiming, String sNumType){
		super(sIdent, sCardinality, sTiming);
		setNumType(sNumType);
	}

	public ResponseNUM(Element eElement){
		super(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperResponse#getResponseTag()
	 */
	public String getResponseTag(){
		return RESPONSE_NUM.getTagName();
	}
	
	public String getNumType(){
		return getAttributeValue(NUMTYPE);
	}
	public void setNumType(String sNumType){
		setAttribute(NUMTYPE, sNumType);
	}

}
