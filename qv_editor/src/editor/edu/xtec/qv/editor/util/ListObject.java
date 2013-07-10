/*
 * ListObject.java
 * 
 * Created on 04/juny/2004
 */
package edu.xtec.qv.editor.util;

/**
 * @author sarjona
 */
public class ListObject {
	
	private String sIdent;
	private String sValue;
	
	public ListObject(String sIdent, String sValue){
		this.sIdent=sIdent;
		this.sValue=sValue;
	}
	
	public String getIdent(){
		return sIdent;
	}
	public void setIdent(String sIdent){
		this.sIdent=sIdent;
	}
	
	public String getValue(){
		return sValue;
	}
	public void setValue(String sValue){
		this.sValue=sValue;
	}
	
	public boolean equals(Object o){
		boolean bEquals = false;
		if (getIdent()!=null && o!=null && o instanceof ListObject){
			bEquals = getIdent().equals(((ListObject)o).getIdent());
		}
		return bEquals;
	}
	
	public String toString(){
		return "ListObject ["+sIdent+"="+sValue+"]";
	}

}
