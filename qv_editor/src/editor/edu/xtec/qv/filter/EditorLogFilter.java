/*
 * EditorLogFilter.java
 * 
 * Created on 21/febrer/2005
 */
package edu.xtec.qv.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author sarjona
 */
public class EditorLogFilter extends LogFilter {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		String sRequestURI = ((HttpServletRequest)request).getRequestURI();
		String sIdQuadern = ((HttpServletRequest)request).getParameter("id_quadern");
		if (sIdQuadern==null) sIdQuadern = (String)((HttpServletRequest)request).getSession().getAttribute("id_quadern");
		String sAccio = ((HttpServletRequest)request).getParameter("action");
		if (("set_quadern".equals(sAccio) || "save_quadern".equals(sAccio)) && sIdQuadern!=null){
			StringBuffer sb = new StringBuffer();
			sb.append(" [");
			sb.append(((HttpServletRequest)request).getRemoteAddr());
			sb.append("] ");
			String sUserId = getUserId((HttpServletRequest)request); 
			if (sUserId!=null){
				sb.append(" [");
				sb.append(sUserId);
				sb.append("] ");
			}
			sb.append(": [qv_editor] ");
			sb.append(sAccio);
			sb.append("=");
			sb.append(sIdQuadern);
			System.out.println(sb.toString());
		}else if (sRequestURI.indexOf("index.jsp")>=0){
			StringBuffer sb = new StringBuffer();
			sb.append(" [");
			sb.append(((HttpServletRequest)request).getRemoteAddr());
			sb.append("] ");
			String sUserId = getUserId((HttpServletRequest)request); 
			if (sUserId!=null){
				sb.append(" [");
				sb.append(sUserId);
				sb.append("] ");
			}
			sb.append(": [qv_editor] index");
			System.out.println(sb.toString());
		}
		chain.doFilter(request, response);
	}

}
