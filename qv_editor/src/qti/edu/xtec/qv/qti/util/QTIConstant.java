/*
 * QTIConstant.java
 * 
 * Created on 13/agost/2004
 */
package edu.xtec.qv.qti.util;

/**
 * @author sarjona
 */
public class QTIConstant {
	
	protected String sTagName;
	protected String sAttributeName;
	protected String sMultiplicity;
	protected String sClassName;
	
	public QTIConstant(String sName){
		this(sName, sName, null, null);
	}

	public QTIConstant(String sName, String sClassName, String sMultiplicity){
		this(sName, sName, sClassName, sMultiplicity);
	}

	public QTIConstant(String sTagName, String sAttributeName, String sClassName, String sMultiplicity){
		this.sTagName = sTagName;
		this.sAttributeName = sAttributeName;
		this.sClassName = sClassName;
		this.sMultiplicity = sMultiplicity;
	}
	
	public String getTagName(){
		return sTagName;
	}
	public void setTagName(String sTagName){
		this.sTagName=sTagName;
	}

	public String getAttributeName(){
		return sAttributeName;
	}
	public void setAttributeName(String sAttributeName){
		this.sAttributeName=sAttributeName;
	}
	
	public String getClassName(){
		return sClassName;
	}
	public void setClassName(String sClassName){
		this.sClassName=sClassName;
	}
	
	public String getMultiplicity(){
		return sMultiplicity;
	}
	public void setMultiplicity(String sMultiplicity){
		this.sMultiplicity=sMultiplicity;
	}
	
	public boolean equals(Object o){
		boolean bEquals = false;
		if (o!=null){
			if (getAttributeName()!=null && getTagName()!=null && getAttributeName().equalsIgnoreCase(getTagName())){
				bEquals = getAttributeName().equalsIgnoreCase(o.toString());
			}
		}
		return bEquals;
	}
	
	public String toString(){
		return getAttributeName();
	}

}
