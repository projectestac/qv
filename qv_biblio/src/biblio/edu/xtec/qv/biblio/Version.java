package edu.xtec.qv.biblio;

import java.util.Date;

public class Version {
	
	protected int iId;
	protected Date dCreation;
	protected Date dRevision;
	protected String sDescription;
	protected String sLang;
	protected String sFolder;
	protected String sPath;
	protected String sSkin;
	protected int iPage;
	protected int iSections;
	protected int iItems;
	protected int iZipSize;
	protected long lTotalScore;
	

	public Version(int iId, Date dCreation, Date dRevision, String sDescription, String sLang, String sFolder, String sPath, String sSkin, int iPage, int iSections, int iItems, int iZipSize, long lTotalScore){
		this.iId = iId;
		this.dCreation=dCreation;
		this.dRevision=dRevision;
		this.sDescription=sDescription;
		this.sLang=sLang;
		this.sFolder=sFolder;
		this.sPath=sPath;
		this.sSkin=sSkin;
		this.iPage=iPage;
		this.iSections=iSections;
		this.iItems=iItems;
		this.iZipSize=iZipSize;
		this.lTotalScore=lTotalScore;
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
	
	public String getDescription(){
		return sDescription;
	}
	public void setDescription(String sDescription){
		this.sDescription=sDescription;
	}
	
	public String getLang(){
		return sLang;
	}
	public void setLang(String sLang){
		this.sLang=sLang;
	}
	
	public String getFolder(){
		return sFolder;
	}
	public void setFolder(String sFolder){
		this.sFolder=sFolder;
	}

	public String getPath(){
		return sPath;
	}
	public void setPath(String sPath){
		this.sPath=sPath;
	}

	public String getSkin(){
		return sSkin;
	}
	public void setSkin(String sSkin){
		this.sSkin=sSkin;
	}

	public int getPage(){
		return this.iPage;
	}
	public void setPage(int iPage){
		this.iPage=iPage;
	}
	
	public int getSections(){
		return this.iSections;
	}
	public void setSections(int iSections){
		this.iSections=iSections;
	}
	
	public int getItems(){
		return this.iItems;
	}
	public void setItems(int iItems){
		this.iItems=iItems;
	}
	
	public int getZipSize(){
		return this.iZipSize;
	}
	public void setZipSize(int iZipSize){
		this.iZipSize=iZipSize;
	}
	
	public long getTotalScore(){
		return this.lTotalScore;
	}
	public void setTotalScore(long lTotalScore){
		this.lTotalScore=lTotalScore;
	}
	
}
