/*
 * Setvar.java
 * 
 * Created on 10-feb-2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class Setvar extends QTISuper {

	public Setvar(String sVarname, String sAction, String sText){
		setVarname(sVarname);
		setAction(sAction);
		setText(sText);
	}
	
	public Setvar(Element eElement){
		createFromXML(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eSetvar = new Element(SETVAR.getTagName());
		Object oVarname = getAttribute(VARNAME);
		if (oVarname==null) {
			oVarname = DEFAULT_VARNAME;
		}
		eSetvar.setAttribute(VARNAME.getTagName(), (String)oVarname);

		Object oAction = getAttribute(ACTION);
		if (oAction!=null) {
			eSetvar.setAttribute(ACTION.getTagName(), (String)oAction);
		}

		if (getAttribute(TEXT)!=null){
			eSetvar.addContent(getAttributeValue(TEXT));
		}
		return eSetvar;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		String sVarname = eElement.getAttributeValue(VARNAME.getTagName());
		if (sVarname==null){
			sVarname = DEFAULT_VARNAME;
		}
		setAttribute(VARNAME, sVarname);
		setAttribute(ACTION, eElement.getAttributeValue(ACTION.getTagName()));
		setAttribute(TEXT, eElement.getText());
	}
	
	public String getVarname(){
		return getAttributeValue(VARNAME);
	}
	public void setVarname(String sVarname){
		setAttribute(VARNAME, sVarname);
	}
	
	public String getAction(){
		return getAttributeValue(ACTION);
	}
	public void setAction(String sAction){
		setAttribute(ACTION, sAction);
	}
	
	public String getText(){
		return getAttributeValue(TEXT);
	}
	public void setText(String sText){
		setAttribute(TEXT, sText);
	}
}
