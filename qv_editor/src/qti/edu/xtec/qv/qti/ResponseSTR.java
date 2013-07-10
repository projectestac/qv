/*
 * ResponseSTR.java
 * 
 * Created on 02/març/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class ResponseSTR extends QTISuperResponse {

	public ResponseSTR(String sIdent, String sCardinality, String sTiming){
		super(sIdent, sCardinality, sTiming);
	}

	public ResponseSTR(Element eElement){
		super(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperResponse#getResponseTag()
	 */
	public String getResponseTag(){
		return RESPONSE_STR.getTagName();
	}

}
