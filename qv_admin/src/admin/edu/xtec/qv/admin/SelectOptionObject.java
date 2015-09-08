/*
 * SelectOptionObject.java
 * 
 * Created on 10/juny/2005
 */
package edu.xtec.qv.admin;

public class SelectOptionObject {

	protected String sValue;
	protected String sText;
	
	public SelectOptionObject(String sText, String sValue){
		this.sValue=sValue;
		this.sText=sText;
	}

	public String getText() {
		return sText;
	}
	public void setText(String sText) {
		this.sText = sText;
	}
	
	public String getValue() {
		return sValue;
	}
	public void setValue(String sValue) {
		this.sValue = sValue;
	}
	
}
