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
public class ResponseXY extends QTISuperResponse {

	public ResponseXY(String sIdent, String sCardinality, String sTiming){
		super(sIdent, sCardinality, sTiming);
	}

	public ResponseXY(Element eElement){
		super(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperResponse#getResponseTag()
	 */
	public String getResponseTag(){
		return RESPONSE_XY.getTagName();
	}
	
}
