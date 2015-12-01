package edu.xtec.qv.servlet;

/*
 * GetCandidateAssignamentServlet.java
 *
 * Created on 6 / juliol / 2004
 */


/**
 *
 * @author  sarjona
 */
public class GetCandidateAssignmentServlet extends GetAssignment{
    
	public GetCandidateAssignmentServlet(){
		super();
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.servlet.GetAssessment#getView()
	 */
	protected String getView() {
		return CANDIDATE_VIEW;
	}

}
