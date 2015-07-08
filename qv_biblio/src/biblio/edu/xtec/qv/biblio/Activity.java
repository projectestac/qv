package edu.xtec.qv.biblio;

import java.util.Date;
import java.util.Hashtable;

public class Activity {
	
	protected int iId;
	protected Date dCreation;
	protected Date dRevision;
	protected String sImage;
	protected String sLicense;
	protected Hashtable hTitle = new Hashtable();
	protected Hashtable hDescription = new Hashtable();
	protected String sState;
	

	public Activity(int iId, Date dRevision){
		this.iId = iId;
		this.dRevision=dRevision;
	}
	
	public Activity(int iId, Date dCreation, Date dRevision, String sImage, String sLicense){
		this.iId = iId;
		this.dCreation=dCreation;
		this.dRevision=dRevision;
		this.sImage=sImage;
		this.sLicense=sLicense;
	}
	
	public int getId(){
		return iId;
	}
	public void setId(int iId){
		this.iId=iId;
	}
	
	public Date getCreationDate(){
		return dCreation;
	}
	public void setCreationDate(Date dCreation){
		this.dCreation=dCreation;
	}
	
	public Date getRevisionDate(){
		return dRevision;
	}
	public void setRevisionDate(Date dRevision){
		this.dRevision=dRevision;
	}
	
	public String getImage(){
		return sImage;
	}
	public void setImage(String sImage){
		this.sImage=sImage;
	}
	
	public String getLicense(){
		return sLicense;
	}
	public void setLicense(String sLicense){
		this.sLicense=sLicense;
	}

	public String getState(){
		return sState;
	}
	public void setState(String sState){
		this.sState=sState;
	}
	
	public void addTitle(String sLanguage, String sTitle){
		if (sLanguage!=null && sTitle!=null){
			this.hTitle.put(sLanguage, sTitle);
		}
	}
	public String getTitle(String sLanguage){
		String sTitle=null;
		if (this.hTitle.containsKey(sLanguage)){
			sTitle = (String)this.hTitle.get(sLanguage);
		}
		return sTitle;
	}

	public void addDescription(String sLanguage, String sDescription){
		if (sLanguage!=null && sDescription!=null){
			this.hDescription.put(sLanguage, sDescription);
		}
	}
	public String getDescription(String sLanguage){
		String sDescription=null;
		if (this.hTitle.containsKey(sLanguage)){
			sDescription = (String)this.hDescription.get(sLanguage);
		}
		return sDescription;
	}
	
}
