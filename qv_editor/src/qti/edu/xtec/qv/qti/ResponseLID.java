/*
 * ResponseLID.java
 * 
 * Created on 09/febrer/2004
 */
package edu.xtec.qv.qti;

import java.util.Vector;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class ResponseLID extends QTISuperResponse {

	public ResponseLID(String sIdent, String sCardinality, String sTiming){
		super(sIdent, sCardinality, sTiming);
	}

	public ResponseLID(Element eElement){
		super(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperResponse#getResponseTag()
	 */
	public String getResponseTag(){
		return RESPONSE_LID.getTagName();
	}

//	***************************	
//	*  Creacio
//	***************************
	public static ResponseLID createRenderChoiceWithMattext(String sIdent, String sCardinality, Vector vResponses, String sAlignment){
		ResponseLID oResponseLID = new ResponseLID(sIdent, sCardinality, null);
		RenderChoice oRenderChoice = RenderChoice.createWithMattext(vResponses, sAlignment);
		oResponseLID.setRender(oRenderChoice);
		return oResponseLID;
	}

}
