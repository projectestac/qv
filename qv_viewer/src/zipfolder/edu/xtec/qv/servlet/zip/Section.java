package edu.xtec.qv.servlet.zip;

import org.jdom.Element;

public class Section {
	
	protected int iOrder;
	protected String sId;
	protected String sTitle;
	protected boolean isRandomizableSection;

	public Section(int iOrder, String sId, String sTitle){
		this(iOrder, sId, sTitle, false);
	}
	
	public Section(int iOrder, String sId, String sTitle, boolean isRandomizableSection){//Albert
		this.iOrder=iOrder;
		this.sId=sId;
		this.sTitle=sTitle;
		this.isRandomizableSection=isRandomizableSection;
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
	
	public String toString(){
		return "id="+getId()+", title="+sTitle+", order="+getOrder();
	}
	
	public Element toXML(){
		Element eSection = new Element("section");
		if (getId()!=null) eSection.setAttribute("id", getId());
		eSection.setAttribute("num", String.valueOf(getOrder()));
		eSection.setAttribute("name", getTitle()!=null?getTitle():"");
		eSection.setAttribute("randomizable", isRandomizableSection?"true":"false"); 
		return eSection;
	}
	
}
