package edu.xtec.qv.servlet.rp.compact;

/**
 *
 * @author  allastar
 */
public class GetAssignacionsDocentCompact extends GetAssignacionsUsuariCompact{
    
    String servletName="getAssignacionsDocent";
    
    /** Creates a new instance of GetAssignacionsDocentCompact */
    public GetAssignacionsDocentCompact() {
    	super();
    	setDocent(true);
    	sReturnPage="getAssignacionsDocent";
    }

    public String getServletName(){
        return "GetAssignacionsDocentServlet";
    }
    
}
