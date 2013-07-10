package edu.xtec.qv.lms.data;

public class QVSection{
	
	int id = 0;
	int assignmentid = 0;
	String sectionid = "";
	String responses = "";
	String scores = "";
	String pending_scores = "";
	int attempts = 0;
	int state = 0; // '-1 no_iniciat;0 iniciat; 1 lliurat;2 corregit'
	String time = "00:00:00";

	public QVSection(){
	}
	
	public QVSection(int id, int assignmentid, String sectionid, String responses, String scores, String pending_scores, int attempts, int state, String time){
		this.id = id;
		this.assignmentid = assignmentid;
		this.sectionid = sectionid;
		this.responses = responses;
		this.scores = scores;
		this.pending_scores = pending_scores;
		this.attempts = attempts;
		this.state = state;
		this.time = time;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public void setAssignmentId(int assignmentId){
		this.assignmentid = assignmentId;
	}
	
	public int getAssignmentId(){
		return assignmentid;
	}
	
	public void setSectionId(String sectionId){
		this.sectionid = sectionId;
	}
	
	public String getSectionId(){
		return sectionid;
	}
	
	public void setResponses(String responses){
		this.responses = responses;
	}
	
	public String getResponses(){
		return responses;
	}
	
	public void setScores(String scores){
		this.scores = scores;
	}
	
	public String getScores(){
		return scores;
	}
	
	public void setPendingScores(String pendingScores){
		this.pending_scores = pendingScores;
	}
	
	public String getPendingScores(){
		return pending_scores;
	}
	
	public void setAttempts(int attempts){
		this.attempts = attempts;
	}
	
	public int getAttempts(){
		return attempts;
	}
	
	public void setState(int state){
		this.state = state;
	}
	
	public int getState(){
		return state;
	}
	
	public void setTime(String time){
		this.time = time;
	}
	
	public String getTime(){
		return time;
	}
	
}
