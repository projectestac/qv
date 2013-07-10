/*
 * RenderFIB.java
 * 
 * Created on 02/març/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class RenderFIB extends QTISuperRender {

	public RenderFIB(Element eElement){
		super(eElement);
	}

	public RenderFIB(){
		super();
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperRender#getRenderTag()
	 */
	protected String getRenderTag() {
		return RENDER_FIB.getTagName();
	}		

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperRender#getSpecificXML(org.jdom.Element)
	 */
	protected Element getSpecificXML(Element eRender){
		if (getAttribute(ENCODING)!=null){
			eRender.setAttribute(ENCODING.getTagName(), getAttributeValue(ENCODING));
		}
		if (getAttribute(FIBTYPE)!=null){
			eRender.setAttribute(FIBTYPE.getTagName(), getAttributeValue(FIBTYPE));
		}
		if (getAttribute(ROWS)!=null){
			eRender.setAttribute(ROWS.getTagName(), getAttributeValue(ROWS));
		}
		if (getAttribute(MAXCHARS)!=null){
			eRender.setAttribute(MAXCHARS.getTagName(), getAttributeValue(MAXCHARS));
		}
		if (getAttribute(PROMPT)!=null){
			eRender.setAttribute(PROMPT.getTagName(), getAttributeValue(PROMPT));
		}
		if (getAttribute(COLUMNS)!=null){
			eRender.setAttribute(COLUMNS.getTagName(), getAttributeValue(COLUMNS));
		}
		if (getAttribute(CHARSET)!=null){
			eRender.setAttribute(CHARSET.getTagName(), getAttributeValue(CHARSET));
		}
		return eRender;
	}
	
	public String getRows(){
		return getAttributeValue(ROWS);
	}
	
	public void setRows(String sRows){
		setAttribute(ROWS, sRows);
	}
	
	public String getColumns(){
		return getAttributeValue(COLUMNS);
	}
	
	public void setColumns(String sColumns){
		setAttribute(COLUMNS, sColumns);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperRender#createSpecificFromXML(org.jdom.Element)
	 */
	protected void createSpecificFromXML(Element eElement){
		if (eElement!=null){
			setAttribute(ENCODING, eElement.getAttributeValue(ENCODING.getTagName()));
			setAttribute(FIBTYPE, eElement.getAttributeValue(FIBTYPE.getTagName()));
			setAttribute(ROWS, eElement.getAttributeValue(ROWS.getTagName()));
			setAttribute(MAXCHARS, eElement.getAttributeValue(MAXCHARS.getTagName()));
			setAttribute(PROMPT, eElement.getAttributeValue(PROMPT.getTagName()));
			setAttribute(COLUMNS, eElement.getAttributeValue(COLUMNS.getTagName()));
			setAttribute(CHARSET, eElement.getAttributeValue(CHARSET.getTagName()));
		}
	}
}
