/*
 * QTIResponseGRP.java
 * 
 * Created on 05/maig/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class ResponseGRP extends QTISuperResponse {

	public ResponseGRP(String sIdent, String sCardinality, String sTiming){
		super(sIdent, sCardinality, sTiming);
	}

	public ResponseGRP(Element eElement){
		super(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperResponse#getResponseTag()
	 */
	public String getResponseTag(){
		return RESPONSE_GRP.getTagName();
	}
	
}
