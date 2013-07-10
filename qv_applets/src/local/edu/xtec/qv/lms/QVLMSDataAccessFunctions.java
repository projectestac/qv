package edu.xtec.qv.lms;

import edu.xtec.qv.lms.data.*;
import java.util.ArrayList;

public interface QVLMSDataAccessFunctions{
	
	public static final String ERROR_ASSIGNMENTID_DOES_NOT_EXIST = "error_assignmentid_does_not_exist";
	public static final String ERROR_MAXDELIVER_EXCEEDED = "error_maxdeliver_exceeded";
	public static final String ERROR_DB_INSERT = "error_db_insert";
	public static final String ERROR_DB_UPDATE = "error_db_update";

	public QV getQV(int assignmentId);
	public boolean existsQVAssignment(int assignmentId);
	public QVAssignment getQVAssignment(int assignmentId);
	public boolean updateQVAssignment(QVAssignment qvAssignment);
	public ArrayList getQVSections(int assignmentId);
	public QVSection getQVSection(int assignmentId, String sectionId);
	public boolean insertQVSection(QVSection qvSection);
	public boolean updateQVSection(QVSection qvSection);
	
}