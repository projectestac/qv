package edu.xtec.qv.servlet;

/*
 * GetTeacherAssignmentServlet.java
 *
 * Created on 7 / juliol / 2004
 */


/**
 *
 * @author  sarjona
 */
public class GetTeacherAssignmentServlet extends GetAssignment{
    
	public GetTeacherAssignmentServlet(){
		super();
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.servlet.GetAssessment#getView()
	 */
	protected String getView() {
		return TEACHER_VIEW;
	}

}
