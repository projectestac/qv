package edu.xtec.lom;

import java.util.Hashtable;

public class Nivell {
	
	protected int iId;
	protected Hashtable hText;
	protected int iEdatMin;
	protected int iEdatMax;
	protected String sContext;

	public Nivell(int iId, int iEdatMin, int iEdatMax, String sContext){
		this.iId = iId;
		this.iEdatMin = iEdatMin;
		this.iEdatMax = iEdatMax;
		this.sContext = sContext;
		this.hText = new Hashtable();
	}
	
	public int getId(){
		return iId;
	}
	public void setId(int iId){
		this.iId=iId;
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

	public int getEdatMin(){
		return iEdatMin;
	}
	public void setEdatMin(int iEdatMin){
		this.iEdatMin=iEdatMin;
	}
	
	public int getEdatMax(){
		return iEdatMax;
	}
	public void setEdatMax(int iEdatMax){
		this.iEdatMax=iEdatMax;
	}

	public String getContext(){
		return sContext;
	}
	public void setContext(String sContext){
		this.sContext=sContext;
	}
	
	public boolean equals(Nivell n){
		return n.getId()==this.getId();
	}

}
