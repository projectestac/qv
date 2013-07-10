package edu.xtec.qv.lms.data;

public class QV{
	
	int id;
	int course = 0;
	String name = "";
	String description;
	String assessmenturl = "";
	String skin = "";
	String assessmentlang = "ca";
	int maxdeliver = -1;
	int showcorrection = 1;
	int showinteraction = 1;
	int ordersections = 0;
	int orderitems = 0;
	String target = "descself";
	float maxgrade;
	String width;
	String height;

	public QV(){
	}

	public QV(int id, int course, String name, String description, String assessmenturl, String skin, String assessmentlang, int maxdeliver, int showcorrection, int showinteraction, int ordersections, int orderitems, String target, float maxgrade,	String width, String height){
		this.id = id;
		this.course = course;
		this.name = name;
		this.description = description;
		this.assessmenturl = assessmenturl;
		this.skin = skin;
		this.assessmentlang = assessmentlang;
		this.maxdeliver = maxdeliver;
		this.showcorrection = showcorrection;
		this.showinteraction = showinteraction;
		this.ordersections = ordersections;
		this.orderitems = orderitems;
		this.target = target;
		this.maxgrade = maxgrade;
		this.width = width;
		this.height = height;
	}

	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public void setCourse(int course){
		this.course = course;
	}
	
	public int getCourse(){
		return course;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setAssessmentURL(String assessmentURL){
		this.assessmenturl = assessmentURL;
	}
	
	public String getAssessmentURL(){
		return assessmenturl;
	}
	
	public void setSkin(String skin){
		this.skin = skin;
	}
	
	public String getSkin(){
		return skin;
	}
	
	public void setAssessmentLang(String assessmentLang){
		this.assessmentlang = assessmentLang;
	}
	
	public String getAssessmentLang(){
		return assessmentlang;
	}
	
	public void setMaxDeliver(int maxDeliver){
		this.maxdeliver = maxDeliver;
	}
	
	public int getMaxDeliver(){
		return maxdeliver;
	}
	
	public void setShowCorrection(int showCorrection){
		this.showcorrection = showCorrection;
	}
	
	public int getShowCorrection(){
		return showcorrection;
	}
	
	public void setShowInteraction(int showInteraction){
		this.showinteraction = showInteraction;
	}
	
	public int getShowInteraction(){
		return showinteraction;
	}
	
	public void setOrderSections(int orderSections){
		this.ordersections = orderSections;
	}
	
	public int getOrderSections(){
		return ordersections;
	}
	
	public void setOrderItems(int orderItems){
		this.orderitems = orderItems;
	}
	
	public int getOrderItems(){
		return orderitems;
	}
	
	public void setTarget(String target){
		this.target = target;
	}
	
	public String getTarget(){
		return target;
	}
	
	public void setMaxGrade(float maxGrade){
		this.maxgrade = maxGrade;
	}
	
	public float getMaxGrade(){
		return maxgrade;
	}
	
	public void setWidth(String width){
		this.width = width;
	}
	
	public String getWidth(){
		return width;
	}
	
	public void setHeight(String height){
		this.height = height;
	}
	
	public String getHeight(){
		return height;
	}
	
	
}
