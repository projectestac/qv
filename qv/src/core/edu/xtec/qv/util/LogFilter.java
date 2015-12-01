package edu.xtec.qv.util;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/*
 * LogFilter.java
 * 
 * Created on 17/febrer/2005
 */

/**
 * @author sarjona
 */
public abstract class LogFilter implements Filter {

	protected static final String USER_KEY="usuari-edu365";

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
	
	protected String getUserId(HttpServletRequest request){
		String sUserId = null;
		if(request!=null){
			Cookie[] cookies=request.getCookies();
			if(cookies!=null){
				for(int i=0; i<cookies.length; i++){
					Cookie c=cookies[i];
					if(c.getName().equals(USER_KEY) && c.getValue()!=null){
						sUserId=c.getValue().trim();
						break;
					}
				}
			}
		}
		return sUserId;
	}
	

}
