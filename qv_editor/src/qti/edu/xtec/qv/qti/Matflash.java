/*
 * Matimage.java
 * 
 * Created on 23/febrer/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class Matflash extends Matimage {

	public Matflash(String sImagtype, String sURI){
		super(sImagtype==null?"application/x-shockwave-flash":sImagtype, sURI);
	}
	
	public Matflash(String sImagtype, String sURI, int iWidth, int iHeight){
		super(sImagtype==null?"application/x-shockwave-flash":sImagtype, sURI, iWidth, iHeight);
	}
	
	public Matflash(Element eElement){
		super(eElement);
	}

	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatContentType()
	 */
	public String getMatContentType() {
		return MATFLASH_CONTENT_TYPE;
	}

}
