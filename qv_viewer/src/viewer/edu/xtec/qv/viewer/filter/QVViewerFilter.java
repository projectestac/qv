package edu.xtec.qv.viewer.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * QVViewerFilter
 * 
 * Created on 01/08/2007
 */

/**
 * @author sarjona
 */
public class QVViewerFilter implements Filter {

	protected FilterConfig config;
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {

	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	throws IOException, ServletException {
		String sBase = ((HttpServletRequest)request).getRequestURI();
		if (sBase!=null && sBase.indexOf("/quaderns/")>=0){
			if (sBase.endsWith("/")) sBase=sBase.substring(0, sBase.length()-1);
			//sBase=sBase.substring(sBase.lastIndexOf("/")+1);
			sBase = sBase.substring(sBase.indexOf("/quaderns/")+10);
			int iFirst = sBase.indexOf("/");
			String sUser = sBase.substring(0, iFirst);
			String sAssessment = sBase.substring(iFirst+1);
			String sURL = ((HttpServletRequest)request).getContextPath()+"/open?";
			if ("biblioteca".equalsIgnoreCase(sUser)){
				sURL+="biblio=true&xml="+sAssessment+"/"+sAssessment+".xml&base="+sAssessment;
			}else{
				sURL+="user="+sUser+"&assessment="+sAssessment;				
			}
			String sParams = ((HttpServletRequest)request).getQueryString();
			if (sParams!=null) sURL+="&"+sParams;
			//System.out.println("QVViewerFilter-> base="+((HttpServletRequest)request).getRequestURI()+" url="+sURL);
			((HttpServletResponse)response).sendRedirect(sURL);
		}
	}	

}
