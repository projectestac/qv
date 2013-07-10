package edu.xtec.lom;

import java.util.Hashtable;

public class Idioma {
	
	protected String sId;
	protected Hashtable hText;

	public Idioma(String sId){
		this.sId = sId;
		this.hText = new Hashtable();
	}
	
	public Idioma(String sId, String sLang, String sText){
		this.sId = sId;
		this.hText = new Hashtable();
		addText(sLang, sText);
	}
	
	public String getId(){
		return sId;
	}
	public void setId(String sId){
		this.sId=sId;
	}
	
	public Hashtable getTexts(){
		return hText;
	}
	public String getText(String sLang){
		Object o = hText.get(sLang); 
		if (o!=null) return (String)o;
		return null;
	}
	public void addText(String sLang, String sText){
		if (sLang!=null && sText!=null){
			hText.put(sLang, sText);
		}
	}
}
