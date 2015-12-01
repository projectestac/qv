package edu.xtec.qv.servlet.util;
/*
 * Skin.java
 * Created on 16/01/2006
 * 
 */

import java.util.Hashtable;

import org.apache.log4j.Logger;

/**
 * @author sarjona
 */
public class Skin {

	private static Logger logger = Logger.getRootLogger();
	
	private String sId;
	protected Hashtable hText = new Hashtable();
	
	public Skin(String sId){
		this.sId=sId;
	}

	public String getId() {
		return this.sId;
	}
	
	public void addText(String sLanguage, String sText){
		if (sLanguage!=null && sText!=null){
			this.hText.put(sLanguage, sText);
		}
	}
	public String getText(String sLanguage){
		String sText=null;
		if (this.hText.containsKey(sLanguage)){
			sText = (String)this.hText.get(sLanguage);
		}
		return sText;
	}

	
	
}
