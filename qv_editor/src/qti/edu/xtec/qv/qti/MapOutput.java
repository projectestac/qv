/*
 * MapOutput.java
 * 
 * Created on 22/març/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class MapOutput extends QTISuper {

	public MapOutput(String sVarname, String sText){
		setVarname(sVarname);
		setText(sText);
	}
	
	public MapOutput(Element eElement){
		createFromXML(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eMapOutput = new Element(MAP_OUTPUT.getTagName());
		if (getAttribute(VARNAME)!=null){
			eMapOutput.setAttribute(VARNAME.getTagName(), getAttributeValue(VARNAME));
		}
		if (getAttribute(TEXT)!=null){
			eMapOutput.setText(getAttributeValue(TEXT));
		}
		return eMapOutput;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		setAttribute(VARNAME, eElement.getAttributeValue(VARNAME.getTagName()));
		setAttribute(TEXT, eElement.getText());
	}
	
	public String getVarname(){
		return getAttributeValue(VARNAME);
	}
	public void setVarname(String sVarname){
		setAttribute(VARNAME, sVarname);
	}
	
	public String getText(){
		return getAttributeValue(TEXT);
	}
	public void setText(String sText){
		setAttribute(TEXT, sText);
	}
	
}
