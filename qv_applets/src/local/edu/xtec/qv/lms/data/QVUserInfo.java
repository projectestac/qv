package edu.xtec.qv.lms.data;

public class QVUserInfo{
	
	private String userId = null;
	private String groupId = null;
	private String groupName = null;
	private String userIT = null;
	private String userNE = null;
	
	public QVUserInfo(){
	}
	
	public QVUserInfo(String userId, String groupId, String groupName, String userIT, String userNE){
		this.userId = userId;
		this.groupId = groupId;
		this.groupName = groupName;
		this.userIT = userIT;
		this.userNE = userNE;
	}
	
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	public String getUserId(){
		return userId;
	}

	public void setGroupId(String groupId){
		this.groupId = groupId;
	}
	
	public String getGroupId(){
		return groupId;
	}

	public void setGroupName(String groupName){
		this.groupName = groupName;
	}
	
	public String getGroupName(){
		return groupName;
	}

	public void setUserIT(String userIT){
		this.userIT = userIT;
	}
	
	public String getUserIT(){
		return userIT;
	}

	public void setUserNE(String userNE){
		this.userNE = userNE;
	}
	
	public String getUserNE(){
		return userNE;
	}

	public String toString(){
		String s = "userId="+userId+" groupId="+groupId+" groupName="+groupName+" userIT="+userIT+" userNE="+userNE;
		return s;
	}
}