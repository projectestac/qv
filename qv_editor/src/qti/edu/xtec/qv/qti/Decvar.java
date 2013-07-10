/*
 * Decvar.java
 * 
 * Created on 10/febrer/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class Decvar extends QTISuper {
	
	public Decvar(String sVarname, String sVartype, String sDefaultval){
		setVarname(sVarname);
		setVartype(sVartype);
		setDefaultval(sDefaultval);
	}
	
	public Decvar(Element eElement){
		createFromXML(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eDecvar = new Element(DECVAR.getTagName());
		Object oVarname = getAttribute(VARNAME);
		if (oVarname==null) {
			oVarname = DEFAULT_VARNAME;
		}
		eDecvar.setAttribute(VARNAME.getTagName(), (String)oVarname);
		
		Object oVartype = getAttribute(VARTYPE);
		if (oVartype!=null) {
			eDecvar.setAttribute(VARTYPE.getTagName(), (String)oVartype);
		}
		Object oDefaultval = getAttribute(DEFAULTVAL);
		if (oDefaultval!=null) {
			eDecvar.setAttribute(DEFAULTVAL.getTagName(), (String)oDefaultval);
		}
		Object oCutvalue = getAttribute(CUTVALUE);
		if (oCutvalue!=null) {
			eDecvar.setAttribute(CUTVALUE.getTagName(), (String)oCutvalue);
		}
		Object oMinvalue = getAttribute(MINVALUE);
		if (oMinvalue!=null) {
			eDecvar.setAttribute(MINVALUE.getTagName(), (String)oMinvalue);
		}
		Object oMaxvalue = getAttribute(MAXVALUE);
		if (oMaxvalue!=null) {
			eDecvar.setAttribute(MAXVALUE.getTagName(), (String)oMaxvalue);
		}
		Object oMembers = getAttribute(MEMBERS);
		if (oMembers!=null) {
			eDecvar.setAttribute(MEMBERS.getTagName(), (String)oMembers);
		}
		return eDecvar;
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
		setAttribute(VARTYPE, eElement.getAttributeValue(VARTYPE.getTagName()));
		setAttribute(DEFAULTVAL, eElement.getAttributeValue(DEFAULTVAL.getTagName()));
		setAttribute(CUTVALUE, eElement.getAttributeValue(CUTVALUE.getTagName()));
		setAttribute(MINVALUE, eElement.getAttributeValue(MINVALUE.getTagName()));
		setAttribute(MAXVALUE, eElement.getAttributeValue(MAXVALUE.getTagName()));
		setAttribute(MEMBERS, eElement.getAttributeValue(MEMBERS.getTagName()));
	}
	
	public String getVarname(){
		return getAttributeValue(VARNAME);
	}
	public void setVarname(String sVarname){
		setAttribute(VARNAME, sVarname);
	}
	
	public String getVartype(){
		return getAttributeValue(VARTYPE);
	}
	public void setVartype(String sVartype){
		setAttribute(VARTYPE, sVartype);
	}
	
	public String getDefaultval(){
		return getAttributeValue(DEFAULTVAL);
	}
	public void setDefaultval(String sDefaultval){
		setAttribute(DEFAULTVAL, sDefaultval);
	}

	public String getCutvalue(){
		return getAttributeValue(CUTVALUE);
	}
	public void setCutvalue(String sCutvalue){
		setAttribute(CUTVALUE, sCutvalue);
	}
	
	public String getMinvalue(){
		return getAttributeValue(MINVALUE);
	}
	public void setMinvalue(String sMinvalue){
		setAttribute(MINVALUE, sMinvalue);
	}

	public String getMaxvalue(){
		return getAttributeValue(MAXVALUE);
	}
	public void setMaxvalue(String sMaxvalue){
		setAttribute(MAXVALUE, sMaxvalue);
	}
	
	public String getMembers(){
		return getAttributeValue(MEMBERS);
	}
	public void setMembers(String sMembers){
		setAttribute(MEMBERS, sMembers);
	}
	
}
