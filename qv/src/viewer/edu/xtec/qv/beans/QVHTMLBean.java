/*
 * QVHTMLBean.java
 * 
 * Created on 10/juny/2005
 */
package edu.xtec.qv.beans;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import edu.xtec.qv.beans.util.SelectOptionObject;

public class QVHTMLBean {

	protected static Logger logger = Logger.getRootLogger();

	public static final String USER_KEY="usuari-edu365";

	// TIPUS
	public static final String TIPUS_USUARI = "usuari";
	public static final String TIPUS_BIBLIOTECA = "biblioteca";
	public static final String TIPUS_XML = "xml";

	// TARGET
	public static final String TARGET_SELF = "_self";
	public static final String TARGET_BLANK = "_blank";

	// Parametres
	public final String P_TIPUS_QV = "tipus_qv";
	public final String P_TARGET = "p_target";
	public final String P_USERNAME = "p_username";
	public final String P_QUADERN = "p_quadern";
	public final String P_XML = "p_xml";
	
	
	protected String sUserId;
	protected String sUsername;
	protected String sTipus;
	protected String sTarget;
	protected String sQuadern;
	protected String sXML;

	protected boolean bInitiated = false;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;

	public boolean init(HttpServletRequest request, HttpSession session, HttpServletResponse response){
		boolean bOk = bInitiated;
		this.request=request;
		if (!request.isRequestedSessionIdValid()){
			this.session = request.getSession(true);
		}else{
			this.session=session;
		}
		this.response=response;
		if (!bInitiated){
			bInitiated = true;
			try{
				writeCacheInfo();
				if (needValidation()){
					if (!isValidated()){
						redirectoToValidation();
					}else{
						
					}
				}
				bOk=true;
			} catch (Exception e){
				logger.fatal("EXCEPCIO inicialitzant bean --> "+e);
				bOk = false;
			}
		}
		return bOk;
	}
	
	public String getTipus(){
		if (sTipus==null){
			sTipus = request.getParameter(P_TIPUS_QV);
		}
		return sTipus;
	}
	
	public String getTarget(){
		if (sTarget==null){
			sTarget = request.getParameter(P_TARGET);
		}
		return sTarget;
	}
	
	public String getQuadern(){
		if (sQuadern==null){
			sQuadern = request.getParameter(P_QUADERN);
		}
		return sQuadern;
	}
	
	public String getUsername(){
		if (sUsername==null){
			sUsername = request.getParameter(P_USERNAME);
			if (sUsername==null) {
				sUsername = (String)session.getAttribute(P_USERNAME);
				if (sUsername==null) sUsername="";
			}
			else session.setAttribute(P_USERNAME, sUsername);
		}
		return sUsername;
	}
	
	public String getXML(){
		if (sXML==null){
			sXML = request.getParameter(P_XML);
		}
		return sXML;
	}
	
	public Vector getQuaderns(){
		return getQuaderns(getUsername());
	}
	
	public Vector getQuaderns(String sUsername){
		Vector vQuaderns = new Vector();
		if (sUsername!=null && sUsername.trim().length()>0){
			vQuaderns = executeServlet("http://clic.xtec.net/qv/getQuaderns?username="+sUsername, true);
		}
		return vQuaderns;
	}
	
	public Vector getAparences(){
		return getAparences(getUsername());
	}
	
	public Vector getAparences(String sUsername){
		String sServlet = "http://clic.xtec.net/qv/getAparences";
		if (sUsername!=null && sUsername.trim().length()>0){
			sServlet += "?username="+sUsername;
		}
		return executeServlet(sServlet);
	}
	
//	***************************	
//	* Util
//	***************************
	protected Vector executeServlet(String sURL){
		return executeServlet(sURL, false);
	}

	/*
	 * @param sURL 
	 * @param bFirst Indica si s'ha de saltar el primer o no
	 */
	protected Vector executeServlet(String sURL, boolean bFirst){
		Vector vResponse = new Vector();
		try {
			java.net.URL servlet = null;
			int intents = 0;
			while (intents<5 && servlet==null){
				try{
					servlet = new java.net.URL(sURL);
				} catch (Exception e){
					logger.debug("intent "+intents+" -> "+e);
				}
				intents++;
			}

			//java.net.URL servlet = new java.net.URL(sURL);
			java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(servlet.openStream()));
			String sResponse = "";
		    String inputLine;
		    while ((inputLine = in.readLine()) != null){
				sResponse+=inputLine;
			}
			in.close();

			java.util.StringTokenizer st = new java.util.StringTokenizer(sResponse, ";");
			if (st.hasMoreTokens()){
				if (bFirst) st.nextToken();
				while(st.hasMoreTokens()){
					String sText = st.nextToken();
					if (st.hasMoreTokens()){
						String sValue = st.nextToken();
						SelectOptionObject oOption = new SelectOptionObject(sText, sValue);
						vResponse.addElement(oOption);
					}
				}
			}
		}catch (Exception e){
			logger.error("EXCEPCIO executant servlet-> "+e);
			e.printStackTrace();
		}
		return vResponse;
	}


	
//	***************************	
//	* Cache
//	***************************
	public void writeCacheInfo(){
		if(isNoCache() && !response.isCommitted()){
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);        
		}        
	}
    
	protected boolean isNoCache(){
		return true;
	}    	

//	***************************	
//	* Validation
//	***************************
	public boolean needValidation(){
		return true;
	}

	public boolean isValidated(){
		return (getUserId()!=null && getUserId().length()>0 && !getUserId().equalsIgnoreCase("null"));
	}
	
	public String getUserId(){
		if(sUserId==null && request!=null){
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
	
	protected void redirectoToValidation(){
		try {
			response.sendRedirect("http://www.edu365.com/pls/edu365/edu_sec_plsql_2.login?p_username=&p_password=&p_url=http://clic.xtec.net/qv/preview.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    

}
