/*
 * ViewerLogFilter.java
 * 
 * Created on 21/febrer/2005
 */
package edu.xtec.qv.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author sarjona
 */
public class ViewerLogFilter extends LogFilter {
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
			String sURI = ((HttpServletRequest)request).getRequestURI();
			String sIP = ((HttpServletRequest)request).getRemoteAddr();
			StringBuffer sb = new StringBuffer();
			sb.append(" [");
			sb.append(sIP);
			sb.append("] ");
			sb.append(": [");
			sb.append(sURI);
			sb.append("]");

			String[] IPs = getValidIPs();
			//System.out.println("disabled="+IsDisabledByIP());
			if ( (sURI.indexOf("prepareQV")>=0 || sURI.indexOf("delAssignacio")>0) && IsDisabledByIP() && !Arrays.asList(IPs).contains(sIP)){
				sb.append(" IP sense permis per accedir a "+sURI+" !!!");
				System.out.println(sb.toString());
				PrintWriter out = response.getWriter();
				out.println(" IP sense permis per crear assignacions de Quaderns Virtuals.");
				out.println(" Per més informació contacti amb qv@xtec.cat");
			}else{
				String sURL = request.getParameter("quadernURL");
				String sAssignacio = request.getParameter("assignacioId");
				if (sURL!=null && sURL.trim().length()>0){
					sb.append(" xml="+request.getParameter("quadernURL"));
				}
				if (sAssignacio!=null && sAssignacio.trim().length()>0){
					sb.append(" assignacio="+request.getParameter("assignacioId"));
				}
				System.out.println(sb.toString());
				chain.doFilter(request, response);				
			}
	}
	
	protected boolean IsDisabledByIP(){
		String sDisabled=QuadernConfig.getPropertyNoURL("disableByIP");
		return "true".equals(sDisabled);
	}
	
	protected String[] getValidIPs(){
		String sIPs=QuadernConfig.getPropertyNoURL("prepareQVValidIPs");
		//System.out.println("ip="+sIPs);
		if (sIPs!=null) return sIPs.split(",");
		return new String[0];
	}

}
