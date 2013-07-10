/*
 * Varsubset.java
 * 
 * Created on 14/maig/2004
 */
package edu.xtec.qv.qti;

import java.util.StringTokenizer;
import java.util.Vector;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class Varsubset extends QTIBooleanVar {
	
	public static final String PARTIAL = "Partial";
	public static final String EXACT = "Exact";

	public Varsubset(String sRespident, String sIndex, String sText, String sSetmatch){
		super(sRespident, sIndex, sText);
		setAttribute(SETMATCH, sSetmatch);
	}
	
	public Varsubset(Element eElement){
		super(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eVarsubset = super.getXML();
		if(eVarsubset!=null){
			if (getAttribute(SETMATCH)!=null){
				eVarsubset.setAttribute(SETMATCH.getTagName(), getAttributeValue(SETMATCH));
			}
		}
		return eVarsubset;
		
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		super.createFromXML(eElement);
		setAttribute(SETMATCH, eElement.getAttributeValue(SETMATCH.getTagName()));
	}

	protected String getVarTag(){
		return VARSUBSET.getTagName();
	}

	public Vector getSubsets(){
		Vector vSubsets = new Vector();
		if (getText()!=null){
			StringTokenizer st = new StringTokenizer(getText(), ",");
			while (st.hasMoreTokens()){
				vSubsets.addElement(st.nextToken().trim());
			}
		}
		return vSubsets;
	}

}
