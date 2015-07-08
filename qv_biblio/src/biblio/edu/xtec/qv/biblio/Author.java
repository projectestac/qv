package edu.xtec.qv.biblio;


public class Author {
	
	protected int iId;
	protected String sFullName;
	protected String sRole;
	protected String sMail;
	protected String sEdu365Id;
	protected String sURL;

	/**
	 * 
	 * @param iId Author identifier
	 */
	public Author(int iId){
		this.iId = iId;
	}
	/**
	 * 
	 * @param iId Author identifier
	 * @param sFullName Full name (name + surname)
	 * @param sRole role
	 * @param sMail email address
	 * @param sURL Author's personal page 
	 * @param sEdu365Id XTEC identifier
	 */
	public Author(int iId, String sFullName, String sRole, String sMail, String sURL, String sEdu365Id){
		this.iId = iId;
		this.sFullName=sFullName;
		this.sRole=sRole;
		this.sMail=sMail;
		this.sURL=sURL;
		this.sEdu365Id=sEdu365Id;
	}
	public int getId(){
		return iId;
	}
	public void setId(int iId){
		this.iId=iId;
	}
		
	public String getFullName(){
		return sFullName;
	}
	public void setFullName(String sFullName){
		this.sFullName=sFullName;
	}
	
	public String getRole(){
		return sRole;
	}
	public void setRole(String sRole){
		this.sRole=sRole;
	}

	public String getMail(){
		return sMail;
	}
	public void setMail(String sMail){
		this.sMail=sMail;
	}
	
	public String getURL(){
		return sURL;
	}
	public void setURL(String sURL){
		this.sURL=sURL;
	}
	
	public String getEdu365Id(){
		return sEdu365Id;
	}
	public void setEd365Id(String sEdu365Id){
		this.sEdu365Id=sEdu365Id;
	}
	
	public String toString(){
		return getFullName();
	}
	
	public boolean equals(Object o){
		boolean bEquals = false;
		if (o instanceof Author){
			Author oAuthor = (Author)o;
			bEquals = oAuthor.getId()==getId();
		}
		return bEquals;
	}
}
