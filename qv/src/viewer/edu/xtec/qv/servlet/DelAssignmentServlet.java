/*
 * DelAssignmentServlet.java
 * 
 * Created on 19/juliol/2004
 */
package edu.xtec.qv.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sarjona
 */
public class DelAssignmentServlet extends QVServlet {

	public static final String SEPARATOR = ";";
	public static final String P_ASSIGNMENT_IDS = "idAssignacions";

	public DelAssignmentServlet(){
		super();
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.servlet.QVServlet#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try{
			boolean bOk = false;
			String sAssignments = request.getParameter(P_ASSIGNMENT_ID);
			if (sAssignments==null || sAssignments.trim().length()==0){
				sAssignments = request.getParameter(P_ASSIGNMENT_IDS);
			}
			if (sAssignments!=null && sAssignments.trim().length()>0){
				bOk=true;
				StringTokenizer stAssignments = new StringTokenizer(sAssignments, SEPARATOR);
				while (stAssignments.hasMoreElements()){
					String sAssignmentId = stAssignments.nextToken();
					boolean bAssignment = getQVDataManage().deleteAssignacio(sAssignmentId);
					bOk =bOk && bAssignment; 
					out.println(sAssignmentId+";"+bAssignment);
				}
			}
			if (bOk) out.println("ok");
			else out.println("ko");
			
			/*String historic = request.getParameter("historic");
			if (historic!=null && historic.trim().length()>0){ // Es volen esborrar tots els registres anteriors a una data
				java.util.Date dDeleteBefore = getDate(historic);
				if (dDeleteBefore!=null)
					bOk = getQVDataManage().deleteAssignacioBefore(dDeleteBefore);
			}
			else if (sIdAssignacio!=null){ //Només es vol esborrar la informació d'una assignació
				bOk = getQVDataManage().deleteAssignacio(sIdAssignacio);
			}
			if (bOk)
				out.println("ok");
			else
				out.println("error:BD");
		    */            
		}
		catch (Exception e){
			out.println("Excepció:"+e);
			e.printStackTrace(out);
		}
	}

	private Date getDate(String s){
		if (s==null) return null;
		try{
			StringTokenizer st=new StringTokenizer(s,"-/\\");
			int iYear=Integer.parseInt(st.nextToken());
			int iMonth=Integer.parseInt(st.nextToken());
			int iDay=Integer.parseInt(st.nextToken());
			return new GregorianCalendar(iYear,iMonth-1,iDay).getTime();
		}
		catch (Exception e){
			return null;
		}
	}

	protected boolean isAssignacioIdNeeded(){
		return false;
	}


}
