package edu.xtec.qv.biblio.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class GetValuesServlet extends BiblioServlet{
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer sb = new StringBuffer();
		sb.append(S_BEGIN);
		sb.append(S_SEPARATOR);

		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		String sLang = getLang(request);
		Vector vValues = getValues(sLang);
		Enumeration enumValues = vValues.elements();
		while (enumValues.hasMoreElements()){
			appendObject(sb, enumValues.nextElement(), sLang);
		}
		sb.append(S_END);
		out.println(sb.toString());
	}
	
	protected abstract Vector getValues(String sLang);
	protected abstract void appendObject(StringBuffer sb, Object o, String sLang);
	
}
