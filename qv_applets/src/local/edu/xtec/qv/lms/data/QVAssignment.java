package edu.xtec.qv.lms.data;

public class QVAssignment{
	
	int id = 0;
	int qvid = 0;
	int userid = 0;
	int sectionorder = 0;
	int itemorder = 0;
	String idnumber = "";
	
	public QVAssignment(){
	}

	public QVAssignment(int id, int qvid, int userid, int sectionorder, int itemorder, String idnumber){
		this.id = id;
		this.qvid = qvid;
		this.userid = userid;
		this.sectionorder = sectionorder;
		this.itemorder = itemorder;
		this.idnumber = idnumber;
	}

	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public void setQVId(int qVId){
		this.qvid = qVId;
	}
	
	public int getQVId(){
		return qvid;
	}
	
	public void setUserId(int userId){
		this.userid = userId;
	}
	
	public int getUserId(){
		return userid;
	}
	
	public void setSectionOrder(int sectionOrder){
		this.sectionorder = sectionOrder;
	}
	
	public int getSectionOrder(){
		return sectionorder;
	}
	
	public void setItemOrder(int itemOrder){
		this.itemorder = itemOrder;
	}
	
	public int getItemOrder(){
		return itemorder;
	}
	
	public void setIdNumber(String idNumber){
		this.idnumber = idNumber;
	}
	
	public String getIdNumber(){
		return idnumber;
	}
	
}
