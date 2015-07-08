package edu.xtec.qv.biblio;


public class EducationalInstitution {
	
	protected String sId;
	protected String sName;
	protected String sURL;

	public EducationalInstitution(String sId, String sName, String sURL){
		this.sId = sId;
		this.sName=sName;
		this.sURL=sURL;
	}
	
	public String getId(){
		return sId;
	}
	public void setId(String sId){
		this.sId=sId;
	}
		
	public String getName(){
		return sName;
	}
	public void setName(String sName){
		this.sName=sName;
	}
		
	public String getURL(){
		if (sURL!=null && sURL.toLowerCase().startsWith("www")){
			sURL = "http://"+sURL;
		}
		return sURL;
	}
	public void setURL(String sURL){
		this.sURL=sURL;
	}
}
