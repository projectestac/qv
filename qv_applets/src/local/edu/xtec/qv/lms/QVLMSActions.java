package edu.xtec.qv.lms;

public interface QVLMSActions {

	public static final String ERROR_BEAN_NOT_DEFINED = "error_bean_not_defined";
	
	
	public String getSections(int assignmentId);
	public String getSection(int assignmentId, String sectionId);
	public String correctSection(int assignmentId, String sectionId, String responses, String scores);
	public String deliverSection(int assignmentId, String sectionId, int sectionOrder, int itemOrder, String time, String responses, String scores);
	public String saveSection(int assignmentId, String sectionId, int sectionOrder, int itemOrder, String time, String responses);
	public String saveSectionTeacher(int assignmentId, String sectionId, String responses, String scores);
	public String saveTime(int assignmentId, String sectionId, String time);
	
	public String addMessage(int assignmentId, String sectionId, String itemId, String userId, String message);
	public String getMessages(int assignmentId, String sectionId);
}