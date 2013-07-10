package edu.xtec.lom;

import java.util.Hashtable;

public class Area {
	
	protected int iId;
	protected String sKeyword;
	protected Hashtable hText;

	public Area(int iId, String sKeyword){
		this.iId = iId;
		this.sKeyword = sKeyword;
	}
	
	public int getId(){
		return iId;
	}
	public void setId(int iId){
		this.iId=iId;
	}
	
	public String getKeyword(){
		return sKeyword;
	}
	public void setKeyword(String sKeyword){
		this.sKeyword=sKeyword;
	}
	
	public void setText(Hashtable hText){
		if (hText!=null) this.hText=hText;
		else this.hText = new Hashtable();
	}
	public String getText(String sLanguage){
		if (this.hText.containsKey(sLanguage)){
			return (String)hText.get(sLanguage);
		}
		return null;
	}
	public void addText(String sLanguage, String sText){
		if (sLanguage!=null && sText!=null){
			this.hText.put(sLanguage, sText);
		}
	}

	
}
