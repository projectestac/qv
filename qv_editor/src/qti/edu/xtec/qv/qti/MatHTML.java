/*
 * MatHTML.java
 * 
 * Created on 16/març/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class MatHTML extends MatExtension {

	public MatHTML(){
	}
	
	public MatHTML(Element eElement){
		super(eElement);
	}
	
	protected String getMatExtensionTag(){
		return MATHTML.getTagName();
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatContentType()
	 */
	public String getMatContentType() {
		return MATHTML_CONTENT_TYPE;
	}
	
	protected Element getExtensionXML(Element eExtension) {
		return eExtension;
	}

	protected Element createExtensionFromXML(Element eExtension) {
		return eExtension;
	}
	

}
