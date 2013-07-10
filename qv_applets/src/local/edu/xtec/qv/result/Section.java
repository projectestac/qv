package edu.xtec.qv.result;

import org.jdom.Element;

public class Section {
	
	protected int iOrder;
	protected String sId;
	protected String sTitle;

	public Section(int iOrder, String sId, String sTitle){
		this.iOrder=iOrder;
		this.sId=sId;
		this.sTitle=sTitle;
	}
	
	public int getOrder(){
		return this.iOrder;
	}
	public String getId(){
		return this.sId;
	}
	public String getTitle(){
		return this.sTitle;
	}
	
	public Element toXML(){
		Element eSection = new Element("section");
		if (getId()!=null) eSection.setAttribute("id", getId());
		eSection.setAttribute("num", String.valueOf(getOrder()));
		eSection.setAttribute("name", getTitle());
		return eSection;
	}
	
}
