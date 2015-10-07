/*
 * QVHTMLBean.java
 *
 * Created on 10/juny/2005
 */
package edu.xtec.qv.admin.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import edu.xtec.qv.admin.Role;
import edu.xtec.qv.admin.SelectOptionObject;
import edu.xtec.qv.admin.User;
import edu.xtec.qv.admin.db.AdminDatabase;

public abstract class QVAdminBean {

	protected static Logger logger = Logger.getRootLogger();
	private static Properties settings;
	public static final String SETTINGS_PATH="/edu/xtec/resources/properties/";
	public static final String SETTINGS_FILE="qv_admin.properties";
	public static final String FORMAT_DATE = "dd/MM/yyyy";

	public static final String USER_KEY="usuari-edu365";
	protected static final String ACTION_PARAM="action";
	protected static final String LOGOUT_ACTION_PARAM="logout";

	private String serverBaseUrl;

	protected String sUserId;
	protected String sUsername;
	protected User oUser;
	protected AdminDatabase oAdminDB;

	protected boolean bInitiated = false;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;

	// Parametres
	public final String P_USERNAME = "p_username";


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
						redirectToValidation();
					} else if (isLogout()){
						logout();
					}
				}
				if (getAdminDatabase()==null){
					logger.error("ERROR: could not initialitze database");
					return false;
				}
				bOk = start();
			} catch (Exception e){
				logger.fatal("EXCEPCIO inicialitzant bean --> "+e);
				bOk = false;
			}
		}
		return bOk;
	}

	protected abstract boolean start();

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
		boolean bValidation = getBooleanSetting("validation.needed", true);
		if (!bValidation) sUserId=getSetting("validation.type.none.defaultUser");
		String sPath = request.getServletPath();
		return bValidation && (sPath==null || sPath.indexOf("login.jsp")<0);
	}

	public boolean isValidated(){
		return (getUserId()!=null && getUserId().length()>0 && !getUserId().equalsIgnoreCase("null"));
	}

	public String getUserId(){
		if(sUserId==null && request!=null){
			sUserId = getAuthenticatedUser();
		}
		return sUserId;
	}

	protected String getAuthenticatedUser(){
		String sUser = null;
		String sType = getSetting("validation.type");
		if ("sso".equalsIgnoreCase(sType)){
			if (request.getRemoteUser()!=null){
				sUser=request.getRemoteUser().toLowerCase();
			}
		}else {
			Cookie[] cookies=request.getCookies();
			if(cookies!=null){
				for(int i=0; i<cookies.length; i++){
					Cookie c=cookies[i];
					if(c.getName().equals(getSetting("validation.userKey")) && c.getValue()!=null){
						sUser=c.getValue().trim();
						break;
					}
				}
			}
		}
		return sUser;
	}

	protected void redirectToValidation(){
		try {
			response.sendRedirect(getValidateURL("", ""));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getValidateURL(String sUsername, String sPassword){
		String s = getServerBaseUrl() + "/login.jsp";
		s += "?p_username="+sUsername+"&p_password="+sPassword+"&p_url="+getServerBaseUrl();
		return s;
	}

	//	***************************
	//	* Validacio usuari
	//	***************************
	public boolean isLogout() {
		String sAction = getParameter(ACTION_PARAM);
		if (sAction!=null) {
			if (sAction.equalsIgnoreCase(LOGOUT_ACTION_PARAM)) {
				return true;
			}
		}
		return false;
	}

	public void logout() {
		if (request != null) {
			String sType = getSetting("validation.type");
			if ("sso".equalsIgnoreCase(sType)){
				try {
					logger.debug("logout SSO");
					response.setHeader("Osso-Return-Url", getSetting("sso.return_url"));
					response.sendError(470, "Oracle SSO");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Cookie c = new Cookie(USER_KEY, "");
				c.setMaxAge(0);
				response.addCookie(c);
				try{
					response.sendRedirect("index.jsp");
				} catch (Exception e){
					logger.error("EXCEPCIO fent logout --> "+e);
					e.printStackTrace();
				}
			}
			sUserId = null;
		}
	}

	public User getUser(){
		if (oUser==null){
			oUser = getAdminDatabase().getUser(getUserId());
		}
		return oUser;
	}

	public boolean isTeacher(){
		return getUser()!=null && getUser().isRole(Role.TEACHER_ROLE);
	}

	public boolean isAdmin(){
		return getUser()!=null && getUser().isRole(Role.ADMIN_ROLE);
	}

	public boolean isValidator(){
		return getUser()!=null && getUser().isRole(Role.VALIDATOR_ROLE);
	}

//	***************************
//	* Database
//	***************************
	public AdminDatabase getAdminDatabase(){
		if (oAdminDB==null){
			oAdminDB = new AdminDatabase();
		}
		return oAdminDB;
	}

//	***************************
//	* Utils
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

	protected String getServerBaseUrl(){
		if(serverBaseUrl==null){
			// VERSIO AMB SERVLETS ANTICS:
			// serverBaseUrl=getSetting("SERVLET_BASE_URL");

			// VERSIO AMB SERVLETS NOUS:
			String s=request.getRequestURL().toString();
			serverBaseUrl=s.substring(0, s.lastIndexOf('/'));
			try{
				StringBuffer result=new StringBuffer("http://");
				java.net.URL url=new java.net.URL(serverBaseUrl);
				String host=url.getHost();
				if(!host.endsWith(getSetting("validation.host"))){
					int l=host.indexOf('.');
					if(l>0)
						host=host.substring(0, l);
					host=host+"."+getSetting("validation.host");
				}
				result.append(host);
				int port=url.getPort();
				if(port>0 && port!=7777)
					result.append(":").append(port);
				result.append(url.getFile());
				serverBaseUrl=result.toString();
			}
			catch(Exception e){
				logger.debug("Error corregint la URL "+serverBaseUrl+"--> "+e);
			}
		}
		return serverBaseUrl;
	}


	static{
		settings=new Properties();
		try{
			settings.load(QVAdminBean.class.getResourceAsStream(SETTINGS_PATH+SETTINGS_FILE));
			File f = new File(System.getProperty("user.home"), SETTINGS_FILE);
			if(f.exists()){
				FileInputStream is=new FileInputStream(f);
				settings.load(is);
				is.close();
			}
		} catch(Exception e){
			logger.fatal("ERROR FATAL!: No s'ha pogut llegir el fitxer "+SETTINGS_PATH+SETTINGS_FILE);
			e.printStackTrace(System.err);
		}
	}

	public static String getSetting(String sKey){
		String sProperty = null;
		try{
			sProperty = settings.getProperty(sKey);
		} catch (Exception e){
			logger.error("EXCEPCIO: No s'ha pogut obtenir la propietat '"+sKey+"' --> "+e);
		}
		return sProperty;
	}

	protected Properties getSettings(){
		return settings;
	}

	public boolean getBooleanSetting(String sKey, boolean bDefaultValue){
		boolean bProperty = bDefaultValue;
		try{
			String sProperty = getSetting(sKey);
			if (sProperty==null) bProperty=bDefaultValue;
			else bProperty = sProperty.equalsIgnoreCase("true");
		} catch (Exception e){
			logger.error("EXCEPCIO: No s'ha pogut obtenir la propietat '"+sKey+"' --> "+e);
		}
		return bProperty;
	}


//	***************************
//	* Parameter
//	***************************
	/**
	 * @param sParam parameter name
	 * @return parameter with sParam name or null if it doesn't exist
	 */
	public String getParameter(String sParam){
		String sValue = null;
		try{
			sValue = request.getParameter(sParam);
			if (sValue!=null && (sValue.trim().length()<=0 || sValue.equals("null"))){
				sValue=null;
			}
		}catch (Exception e){
			logger.debug("EXCEPTION getting '"+sParam+"' parameter -->"+e);
		}
		return sValue;
	}

	/**
	 * @param sParam parameter name
	 * @param iDefault default value if specified parameter name doesn't exist.
	 * @return parameter value or sDefault if it doesn't exist
	 * @return
	 */
	public String getParameter(String sParam, String sDefault){
		String sResult = sDefault;
		try{
			sResult = getParameter(sParam);
			if (sResult==null || sResult.trim().length()<=0 || sResult.equalsIgnoreCase("null")){
				sResult = sDefault;
			}
		}
		catch (Exception e){
			logger.debug("EXCEPTION getting '"+sParam+"' parameter -->"+e);
		}
		return sResult;
	}

	/**
	 * @param sParam parameter name
	 * @return integer parameter value or -1 if it doesn't exist
	 */
	public int getIntParameter(String sParam){
		return getIntParameter(sParam, -1);
	}
	/**
	 * @param sParam parameter name
	 * @param iDefault default integer if specified parameter name doesn't exist.
	 * @return integer parameter value or iDefault if it doesn't exist
	 */
	public int getIntParameter(String sParam, int iDefault){
		int iResult = iDefault;
		try{
			String s = getParameter(sParam);
			if (s!=null) iResult=Integer.parseInt(s);
		}
		catch (Exception e){
			logger.debug("EXCEPCIO obtenint el parametre '"+sParam+"' -->"+e);
		}
		return iResult;
	}

	public Date getDateParameter(String sParam){
		Date dResult = null;
		try{
			String sDate = getParameter(sParam);
			if (sDate!=null){
				SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
				dResult = sdf.parse(sDate);
			}
		}catch (Exception e){
			logger.debug("EXCEPCIO obtenint el parametre '"+sParam+"' de tipus Date -->"+e);
		}
		return dResult;
	}

	public String getLanguage(){
		return "ca";
	}

//	***************************
//	* Util
//	***************************

	/**
	 * @param d date to format
	 * @return formated date to string
	 */
	public String formatDate(Date dDate){
		String sDate = null;
		if (dDate!=null){
	        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
	        sDate = sdf.format(dDate);
		}
		return sDate;
	}

	public String getUsername(){
		return getUsername(true);
	}
	public String getUsername(boolean bSession){
		if (sUsername==null){
			sUsername = request.getParameter(P_USERNAME);
			if (bSession){
				if (sUsername==null) {
					sUsername = (String)session.getAttribute(P_USERNAME);
					if (sUsername==null) sUsername="";
				} else session.setAttribute(P_USERNAME, sUsername);
			}
		}
		if (sUsername==null) sUsername="";
		return sUsername;
	}


	public static long getDiskSpace(String sFile){
		long lEspai = 0;
		if (sFile!=null && sFile.trim().length()>0){
			lEspai = getDiskSpace(new File(sFile));
		}
		return lEspai;
	}

	/**
	 * Get used space of the file or directory specified
	 * @param fFile
	 * @return space (in bytes) of the file. If it's a directory, it'll return the addition of all its content
	 */
	public static long getDiskSpace(File fFile){
		long lQuote = 0;
		if (fFile!=null){
			if (fFile.isDirectory()){
				File[] files = fFile.listFiles();
				if (files!=null){
					for(int i=0;i<files.length;i++){
						lQuote+=getDiskSpace(files[i]);
					}
				}
			}
			lQuote += fFile.length();
		}
		return lQuote;
	}

	public static int parsetoMB(double d){
		return (new Double(Math.round(d)/1000000)).intValue();
		//return round((double)d/(double)1000000, 2);
	}
	public static long parseFromMB(long d){
		return (new Long(Math.round(d*1000000))).longValue();
		//return round((double)d/(double)1000000, 2);
	}
	/**
	 *
	 * @param d double
	 * @param i scale (number of decimals to show)
	 * @return
	 */
	public static double round(double d, int i){
		d = d + Math.pow(5*10,(-i - 1));
		d = d * Math.pow(10,i);
		d = (int)d;
		d = d / Math.pow(10,i);
		return d;
	}



}
