/*
 * QVBean.java
 *
 * Created on 29/gener/2004
 */
package edu.xtec.qv.editor.beans;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.log4j.Logger;

import edu.xtec.lom.bd.LOMDatabase;
import edu.xtec.qv.cp.QVCPDocument;
import edu.xtec.qv.editor.bd.UserDatabase;
import edu.xtec.qv.editor.util.FileUtil;
import edu.xtec.qv.editor.util.QTIUtil;
import edu.xtec.qv.editor.util.QVConstants;
import edu.xtec.qv.qti.Assessment;
import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.Section;
import edu.xtec.qv.qti.util.StringUtil;

/**
 * @author sarjona
 */
public class QVBean implements QVConstants{

	protected static Logger logger = Logger.getRootLogger();
	public static Properties settings;
	private static Hashtable hUserSettings = new Hashtable();
	private static long dateIncrement = 40000000;
	private static Date dUserSettingsLastDate = new Date(new Date().getTime()+dateIncrement);
	private Properties pUserSettings;
	private static Vector vUserSettings = new Vector();

	// Missatges
	//public static final String BUNDLE="edu.xtec.resources.messages.reportMessages";
	protected static HashMap localeObjects=new HashMap();
	protected ResourceBundle bundle;
	protected DateFormat shortDateFormat;
	protected String sStartToken;
	protected String sEndToken;

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	private String sUserId;
	private String serverBaseUrl;
	protected boolean bInitiated = false;
	protected boolean bHeaderLoaded = false;
	protected String sRedirectPage;
	protected Assessment oQuadern;

	protected QVSpecificBean specificBean;
	protected String sPage = null;
	protected Hashtable hImages = new Hashtable();

	protected LOMDatabase oLOMDB;
	protected UserDatabase oUserDB;
	protected QVCPDocument oCPDoc;

// ***************************
// * Constants
// ***************************
	public static final String USER_KEY="usuari-edu365";
	public static final String EDU365_KEY="edu365";
	public static final int CACHED_PORT=7777;
	public static final String DEFAULT_QUOTA="5000000";

	public static final String NULL="null";
	public static final String SI="si";


//	***************************
//	* Inicialitzacio
//	***************************
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
				logger.debug("userId="+getUserId()+"  teacher?"+isTeacher()+"  validated?"+isValidated()+" logout?"+isLogout()+" needValidation?"+needValidation());
				if (needValidation()){
					if (!isTeacher() || !isValidated() || isLogout()){
						if (isLogout()){
							logout();
						}else if (getUserId()!=null){
							logger.info("L'usuari '"+getUserId()+"' no s'ha validat correctament.");
							redirectToValidation();
						}
						return false;
					}
				}
				if (getUserId()!=null){
					logger.debug("Initializing user settings... -> "+getUserId());
					initUserSettings();
				}
				logger.debug("Initializing language...");
				initLanguage();
				bOk = initSpecificBean();
				if (bOk){
					logger.debug("Initializing specific bean... -> "+getSpecificBean());
					bOk = getSpecificBean().init();
				}
			} catch (Exception e){
				logger.fatal("EXCEPCIO inicialitzant bean --> "+e);
				e.printStackTrace();
				bOk = false;
			}
		}
		//logger.debug("quadern="+getIdQuadern()+"   request="+request.getParameter(ID_QUADERN_PARAM)+" session="+session.getAttribute(ID_QUADERN_PARAM)+"  specificBean="+getSpecificBean()+" ok?"+bOk);
		return bOk;
	}

	public String getRedirectPage(){
		return sRedirectPage;
	}

	public void loadHeader(){
		bHeaderLoaded = true;
	}

	public boolean isHeaderLoaded(){
		return bHeaderLoaded;
	}


//	***************************
//	* Specific Bean
//	***************************
	protected boolean initSpecificBean(){
		boolean bOk = false;
		String sPage = getIncludePage();
		String sBeanClassName=getBeanClassName(sPage);
		if(sBeanClassName!=null){
			try{
				Class specificClass=Class.forName(BEANS_PACKAGE+"."+sBeanClassName);
				Constructor cons=specificClass.getConstructor(new Class[]{QVBean.class});
				specificBean=(QVSpecificBean)cons.newInstance(new Object[]{this});
				bOk = true;
			}catch (Exception e){
				logger.error("EXCEPCIO inicialitzant el bean especific --> "+e);
			}
		}
		return bOk;
	}

	protected String getBeanClassName(String sPage){
		String sBeanClassName = "QVSpecificBean";
		if (sPage!=null){
			if (sPage.equals(JSP_INDEX)){
				sBeanClassName = "QVMainBean";
			}else if (sPage.equals(JSP_PREFERENCIES)){
				sBeanClassName = "QVPreferenciesBean";
			}else if (sPage.equals(JSP_EDITORXML)){
				sBeanClassName = "QVPreguntaBean";
			}else if (sPage.equals(JSP_QUADERN)){
				sBeanClassName = "QVQuadernBean";
			}else if (sPage.equals(JSP_FULL)){
				sBeanClassName = "QVFullBean";
			}else if (sPage.equals(JSP_PREGUNTA)){
				sBeanClassName = "QVPreguntaBean";
			}else if (sPage.equals(JSP_PREGUNTA_SELECCIO)){
				sBeanClassName = "QVPreguntaSeleccioBean";
			}else if (sPage.equals(JSP_PREGUNTA_CLOZE)){
				sBeanClassName = "QVPreguntaClozeBean";
			}else if (sPage.equals(JSP_PREGUNTA_ORDENACIO)){
				sBeanClassName = "QVPreguntaOrdenacioBean";
			}else if (sPage.equals(JSP_PREGUNTA_HOTSPOT)){
				sBeanClassName = "QVPreguntaHotspotBean";
			}else if (sPage.equals(JSP_PREGUNTA_DRAGDROP)){
				sBeanClassName = "QVPreguntaDragDropBean";
			}else if (sPage.equals(JSP_PREGUNTA_DRAW)){
				sBeanClassName = "QVPreguntaDrawBean";
			}else if (sPage.equals(JSP_LOGIN)){
				sBeanClassName = "QVLoginBean";
			}
		}
		return sBeanClassName;
	}

	protected String getJSPPage(){
		String sURL = request.getRequestURI();
		if (sURL!=null){
			int iStart = sURL.lastIndexOf("/");
			if (iStart<0){
				iStart = 0;
			}else{
				iStart++;
			}
			sURL = sURL.substring(iStart, sURL.indexOf("."));
		}
		return sURL;
	}

	public String getIncludePage(){
		String sURL = getJSPPage();
		if (sURL!=null){
			if (!sURL.equalsIgnoreCase(JSP_EDIT)){
				sPage = sURL;
			}
		}
		if (sPage==null){
			sPage=getParameter(PAGE_PARAM);
			if(sPage==null || sPage.equals(NULL) || sPage.length()==0){
				if (isMultipartContent()){
					sPage = JSP_QUADERN;
				}else if (getIdPregunta()!=null){
					sPage = JSP_PREGUNTA;
				}else if (getIdFull()!=null){
					sPage = JSP_FULL;
				}else if (getIdQuadern()!=null){
					sPage = JSP_QUADERN;
				}else{
					sPage=JSP_INDEX;
				}
			}
		}
		return sPage;
	}

	public QVSpecificBean getSpecificBean(){
		return specificBean;
	}

//	***************************
//	* Properties
//	***************************
	static{
		settings=new Properties();
		try{
			settings.load(QVBean.class.getResourceAsStream(SETTINGS_PATH+SETTINGS_FILE));
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

	public static Properties getSettings(){
		return settings;
	}

	protected Properties getUserSettings(){
		return getUserSettings(getUserId());
	}

	protected Properties getUserSettings(String sUser){
		if (sUser==null) pUserSettings = new Properties();
		else{
			if (pUserSettings==null || pUserSettings.isEmpty()){
				pUserSettings = new Properties();
				try{
					if (sUser!=null){
						if (new Date().after(dUserSettingsLastDate)){
							hUserSettings.clear();
							dUserSettingsLastDate = new Date(new Date().getTime()+dateIncrement);
						}
						if (hUserSettings.containsKey(sUser)){
							pUserSettings = (Properties)hUserSettings.get(sUser);
						}else{
							if (vUserSettings!=null){
								Enumeration enumUserSettings = vUserSettings.elements();
								while (enumUserSettings.hasMoreElements()){
									String sKey = (String)enumUserSettings.nextElement();
									String sValue = settings.getProperty(sKey);
									if (sValue!=null){
										pUserSettings.setProperty(sKey, sValue);
									}
								}
							}

							String sPath = settings.getProperty(QVSTORE_LOCAL_URL_KEY)+sUser;
							if (sPath!=null){
								if (!sPath.endsWith("/") && !sPath.endsWith("\"")){
									sPath += "/";
								}
								File f = new File(sPath+SETTINGS_FILE_USER);
								if(f.exists()){
									FileInputStream is=new FileInputStream(f);
									pUserSettings.load(is);
									is.close();
									hUserSettings.put(sUser, pUserSettings);
								}
								f = new File(sPath+SETTINGS_FILE);
								if(f.exists()){
									FileInputStream is=new FileInputStream(f);
									pUserSettings.load(is);
									is.close();
									hUserSettings.put(sUser, pUserSettings);
								}
							}
						}
					}
				} catch(Exception e){
					logger.error("EXCEPCIO inicialitzant les opcions especifiques de l'usuari '"+getUserId()+"' -> e="+e);
				}
			}
		}
		return pUserSettings;
	}

    protected void initUserSettings(){
		try{
/*			String sPath = getUserLocalURL();
			if (sPath!=null){
				if (!sPath.endsWith("/") && !sPath.endsWith("\"")){
					sPath += "/";
				}
				File f = new File(sPath+SETTINGS_FILE);
				if(f.exists()){
					FileInputStream is=new FileInputStream(f);
					usersettings.load(is);
					is.close();
				}
			}
*/
			String sUserSettings = settings.getProperty(SETTING_LIST_KEY);
			if (sUserSettings!=null){
				StringTokenizer st = new StringTokenizer(sUserSettings, ",");
				while (st.hasMoreTokens()){
					String sSetting = st.nextToken();
					if (!sSetting.equalsIgnoreCase(SETTING_LIST_KEY)){
						vUserSettings.addElement(sSetting);
					}
				}
			}else{
				logger.info("Inicialitzades opcions de configuracio per defecte (language, autosave, backup i editXML). Per configurar les opcions cal definir '"+SETTING_LIST_KEY+"' al fitxer de properties");
				vUserSettings.addElement(DEFAULT_LANGUAGE_KEY);
				vUserSettings.addElement(AUTOSAVE_KEY);
				vUserSettings.addElement(BACKUP_KEY);
				vUserSettings.addElement(EDITXML_KEY);
			}
		} catch(Exception e){
			logger.error("EXCEPCIO inicialitzant les opcions especifiques de l'usuari '"+getUserId()+"' -> e="+e);
		}
    }

	public String getSetting(String sKey){
		return getSetting(sKey, false);
	}
	public String getSetting(String sKey, boolean bOnlySystem){
		String sProperty = null;
		try{
			if (!bOnlySystem && getUserSettings()!=null && getUserSettings().containsKey(sKey)){
				sProperty = getUserSettings().getProperty(sKey);
			}else{
				sProperty = settings.getProperty(sKey);
			}
		} catch (Exception e){
			logger.error("EXCEPCIO: No s'ha pogut obtenir la propietat '"+sKey+"' --> "+e);
		}
		return sProperty;
	}

	public boolean getBooleanSetting(String sKey, boolean bDefaultValue){
		return getBooleanSetting(sKey, bDefaultValue, false);
	}
	public boolean getBooleanSetting(String sKey, boolean bDefaultValue, boolean bOnlySystem){
		boolean bProperty = bDefaultValue;
		try{
			String sProperty = getSetting(sKey,bOnlySystem);
			if (sProperty==null) bProperty=bDefaultValue;
			else bProperty = sProperty.equalsIgnoreCase("true");
		} catch (Exception e){
			logger.error("EXCEPCIO: No s'ha pogut obtenir la propietat '"+sKey+"' --> "+e);
		}
		return bProperty;
	}

	/**
	 * Si no troba la propietat, retorna el valor per defecte indicat
	 * @param sKey
	 * @param sDefaultValue
	 * @return
	 */
	public String getSetting(String sKey, String sDefaultValue){
		String sProperty = sDefaultValue;
		try{
			if (getUserSettings()!=null && getUserSettings().containsKey(sKey)){
				sProperty = getUserSettings().getProperty(sKey, sDefaultValue);
			}else{
				sProperty = settings.getProperty(sKey, sDefaultValue);
			}
		} catch (Exception e){
			logger.error("EXCEPCIO: No s'ha pogut obtenir la propietat '"+sKey+"' --> "+e);
		}
		return sProperty;
	}

	public void setSetting(String sKey, String sValue){
		try{
			if (getUserSettings()!=null && getUserSettings().containsKey(sKey)){
				getUserSettings().setProperty(sKey, sValue);
			}else{
				settings.setProperty(sKey, sValue);
			}
		} catch (Exception e){
			logger.error("EXCEPCIO modificant la propietat '"+sKey+"' amb el valor '"+sValue+"' --> "+e);
		}
	}

	public void saveSettings(){
		try{
			String sPath = getUserLocalURL();
			if (sPath!=null){
				if (!sPath.endsWith("/") && !sPath.endsWith("\"")){
					sPath += "/";
				}
				FileOutputStream fOutput = new FileOutputStream(sPath+SETTINGS_FILE);
				Properties userSettings = getUserSettings();
				userSettings.store(fOutput,"");
				if (fOutput!=null){
					fOutput.flush();
					fOutput.close();
				}else{
					logger.info("No s'han carregat be les properties de l'usuari '"+getUserId()+"'");
				}
			}
		} catch (Exception e){
			logger.error("EXCEPCIO guardant les properties de l'usuari '"+getUserId()+"' --> "+e);
			e.printStackTrace();
		}
	}

/*
	protected Properties getUserSettings(){
		Properties userSettings = new Properties();
		if (vUserSettings!=null){
			Enumeration enumUserSettings = vUserSettings.elements();
			while (enumUserSettings.hasMoreElements()){
				String sKey = (String)enumUserSettings.nextElement();
				String sValue = getSetting(sKey);
				if (sValue!=null){
					userSettings.setProperty(sKey, sValue);
				}
			}
		}
		return userSettings;
	}
*/

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
//	* Validacio usuari
//	***************************
	public boolean isLogout(){
		String sAction = getParameter(ACTION_PARAM);
		if (sAction!=null){
			if (sAction.equalsIgnoreCase(LOGOUT_ACTION_PARAM)){
				return true;
			}
		}
		return false;
	}

	public void logout(){
		if(request!=null){
			String sType = getSetting("validation.type", true);
			if ("sso".equalsIgnoreCase(sType)){
				try{
					logger.debug("logout SSO");
					response.setHeader("Osso-Return-Url", getSetting("sso.return_url"));
					response.sendError(470, "Oracle SSO");
				}catch (Exception e){
					e.printStackTrace();
				}
			}else{
				Cookie c = new Cookie(USER_KEY, "");
				c.setMaxAge(0);
				response.addCookie(c);
			}
			sUserId = null;
		}
	}
	
	protected String string2MD5(String sPassword) {
		String sMD5 = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(sPassword.getBytes());
	        byte byteData[] = md.digest();
	 
	        //convert the byte to hex format method 1
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        sMD5 = sb.toString();
		} catch (Exception e) {
			logger.error("EXCEPTION string2MD5", e);
			e.printStackTrace();
		}
		return sMD5;
	}

	public String getUserId() {
		//logger.debug("getUserId --> currentUser="+sUserId);
		if (getParameter(request, "userid") != null && !getParameter(request, "userid").equals(sUserId)){
			logger.debug("getUserId: Request userid --> newUser="+getParameter(request, "userid"));
			String sUsername = getParameter(request, "userid");
			String sUserpwd = getParameter(request, "userpwd");
			if (sUsername!=null && sUserpwd!=null){
				String sUsers = getSetting("validation.users", true);
				if (sUsers!=null){
					StringTokenizer stUsers = new StringTokenizer(sUsers, "$$");
					while (stUsers.hasMoreTokens()){
						String sName = stUsers.nextToken();
						if (stUsers.hasMoreTokens()) {
							String sPassword = stUsers.nextToken(); 
							logger.debug("getUserId --> Check if specified user is in validation.users param: "+sName);
							if (sUsername.equals(sName)){
								if (string2MD5(sUserpwd).equalsIgnoreCase(sPassword)) {
									logger.debug("getUserId --> Change current userId: "+sName);
									sUserId=sName;
									session.setAttribute("userid", sUserId);
								}
								break;
							}
						}
					}
				}
			}								
		}
		
		if(!isLogout() && sUserId==null && request!=null){
			// non sso users (specified in validation.users config parameter)
			String sUser = getAuthenticatedUser();
			logger.debug("getUserId: Non sso user--> authenticated="+sUser);
			if (sUser!=null && getSetting("validation.users.admin", true).indexOf("$$"+sUser+"$$")>=0){
				Object o=session.getAttribute("userid");
				if(o!=null) {
					sUserId=o.toString();
				}
			}
			
			if (sUserId==null){
				sUserId = sUser;
			}
		}
		return sUserId;		
		
		
/*		if(isLogout() || sUserId != null || request == null) {
			return sUserId;
		}

		// non sso users (specified in validation.users config parameter)
		String sUser = getAuthenticatedUser();
		if (sUser == null) {
			sUserId = null;
			return null;
		}


		String sAdmins = getSetting("validation.users.admin", true);
		if (sAdmins == null || sAdmins.length() <= 0 || sAdmins.indexOf("$$"+sUser+"$$") < 0) {
			// sUsers not found in sAdmins
			sUserId = sUser;
			return sUserId;
		}


		Object o = session.getAttribute("userid");
		if (o != null) {
			// Get from Session
			sUserId = o.toString();
			return sUserId;
		}

		String sUsername = getParameter(request, "userid");
		String sUserpwd = getParameter(request, "userpwd");
		logger.debug("getUserId --> username="+sUsername);
		if (sUsername != null && sUserpwd != null) {
			String sUsers = getSetting("validation.users", true);
			if (sUsers != null){
				StringTokenizer stUsers = new StringTokenizer(sUsers, "$$");
				while (stUsers.hasMoreTokens()){
					String sName = stUsers.nextToken();
					if (stUsers.hasMoreTokens()) {
						String sPassword = stUsers.nextToken();
						if (sUsername.equals(sName)){
							logger.debug("getUserId --> Change username to "+sName+" if password matches");
							if (sUserpwd.equalsIgnoreCase(sPassword)) {
								sUserId = sName;
								session.setAttribute("userid", sUserId);
							}
							break;
						}
					}
				}
			}
		}

		sUserId = sUser;
		return sUserId;
*/		
	}

	protected String getAuthenticatedUser() {
		String sType = getSetting("validation.type", true);
		if ("sso".equalsIgnoreCase(sType)){
			// SSO LOGIN
			if (request.getRemoteUser() != null) {
				return request.getRemoteUser().toLowerCase();
			}
		} else {
			// COOKIES LOGIN
			Cookie[] cookies=request.getCookies();
			if (cookies!=null) {
				for (int i = 0; i < cookies.length; i++) {
					Cookie c = cookies[i];
					if (c.getName().equals(USER_KEY) && c.getValue()!=null) {
						return c.getValue().trim();
					}
				}
			}
		}
		return null;
	}

	public boolean isTeacher(){
		return true;

//		boolean bIsTeacher = false;
//		if(request!=null){
//			Cookie[] cookies=request.getCookies();
//			if(cookies!=null){
//				for(int i=0; i<cookies.length; i++){
//					Cookie c=cookies[i];
//					if(c.getName().equals(EDU365_KEY) && c.getValue()!=null){
//						bIsTeacher = c.getValue().endsWith("P");
//						break;
//					}
//				}
//			}
//		}
//		return bIsTeacher;
	}

	public boolean isValidated(){
		return (getUserId()!=null && getUserId().length()>0 && !getUserId().equalsIgnoreCase(NULL));
	}

	public boolean needValidation(){
		boolean bValidation = getBooleanSetting("validation.needed", true, true);
		if (!bValidation) sUserId=getSetting("validation.type.none.defaultUser", true);
		String sPath = request.getServletPath();
		return bValidation && (sPath==null || sPath.indexOf("login.jsp")<0);
	}

	public void redirectToValidation(){
		redirectToValidation(getTranslatedPath(JSP_INDEX, JSP_INDEX));
	}

	public void redirectToValidation(String returnPage){
		redirectResponse(getValidateURL(returnPage));
	}

	public void redirectToValidation(String sUsername, String sPassword){
		redirectResponse(getValidateURL(getTranslatedPath(JSP_INDEX, JSP_INDEX), sUsername, sPassword));
	}

	public void redirectResponse(String sRedirectPage){
		redirectResponse(sRedirectPage, false);
	}

	public void redirectResponse(String sRedirectPage, boolean bAddQueryString){
		try{
			if (sRedirectPage!=null && sRedirectPage.startsWith("null")) sRedirectPage=sRedirectPage.substring(4);
			if (sRedirectPage!=null && !sRedirectPage.equals("null")){
				if (bAddQueryString){
					String sQueryString = getQueryString();
					if (sQueryString!=null && sRedirectPage.indexOf("?")<0){
						sQueryString = "?"+sQueryString.substring(1, sQueryString.length());
					}
					sRedirectPage = sRedirectPage+sQueryString;
				}
			}else{
				sRedirectPage = "";
			}
			response.sendRedirect(sRedirectPage);
		}
		catch (Exception e){
			logger.error("Error redirigint la resposta a "+sRedirectPage+" --> "+e);
		}
	}

	private String getQueryString(){
		String sQueryString = "";
		Enumeration enumParameters= request.getParameterNames();
		while (enumParameters.hasMoreElements()){
			String sParamName = (String)enumParameters.nextElement();
			String sParamValue = getParameter(sParamName);
			sQueryString += "&"+sParamName+"="+sParamValue;
		}
		return sQueryString;
	}

	private String getValidateURL(String redirectPage){
		return getValidateURL(redirectPage, "", "");
	}

	private String getValidateURL(String redirectPage, String sUsername, String sPassword){
		String s = getServerBaseUrl() + "/login.jsp";
		s += "?p_username="+sUsername+"&p_password="+sPassword+"&p_url="+getServerBaseUrl()+"/"+redirectPage;
		return s;
	}

//	***************************
//	* Parametres actuals
//	***************************
	public String getIdQuadern(){
		return getSessionParameter(ID_QUADERN_PARAM);
	}

	public String getIdFull(){
		String sIdFull = getSessionParameter(IDENT_FULL_PARAM);
		//Object oIdFull = session.getAttribute(IDENT_FULL_PARAM);
		//if (oIdFull!=null && !oIdFull.equals(NULL) && ((String)oIdFull).trim().length()>0 ){
		//	sIdFull = (String)oIdFull;
		//}
		return sIdFull;
	}

	/**
	 * Actualitza el full actual
	 * @param sIdFull identificador del nou full actual
	 * @return true si es modifica correctament el full actual; false en cas que
	 * no es pugui canviar
	 */
	public boolean setFull(String sIdFull){
		boolean bOk = true;
		if (sIdFull!=null && !sIdFull.equals(NULL) && sIdFull.trim().length()>0){
			session.setAttribute(IDENT_FULL_PARAM, sIdFull);
		}else{
			session.removeAttribute(IDENT_FULL_PARAM);
		}
		setPregunta(null);
		return bOk;
	}

	public String getIdPregunta(){
		String sIdPregunta = getSessionParameter(IDENT_PREGUNTA_PARAM);
		//Object oIdPregunta = session.getAttribute(IDENT_PREGUNTA_PARAM);
		//if (oIdPregunta!=null && !oIdPregunta.equals(NULL) && ((String)oIdPregunta).trim().length()>0 ){
		//	sIdPregunta = (String)oIdPregunta;
		//}
		return sIdPregunta;
	}

	/**
	 * Actualitza la pregunta actual. Si es passa un id null, s'esborra la pregunta
	 * @param sIdFull identificador de la nova pregunta actual
	 * @return true si es modifica correctament la pregunta actual; false en cas que
	 * no es pugui canviar
	 */
	public boolean setPregunta(String sIdPregunta){
		boolean bOk = true;
		if (sIdPregunta!=null && !sIdPregunta.equals(NULL) && sIdPregunta.trim().length()>0){
			session.setAttribute(IDENT_PREGUNTA_PARAM, sIdPregunta);
			String sIdFull = getSessionParameter(IDENT_FULL_PARAM);
			if (getFull()!=null && !getFull().containsItem(sIdPregunta)){
				Section oFull = QTIUtil.getFullPregunta(getQuadern(), sIdPregunta);
				if (oFull!=null){
					session.setAttribute(IDENT_FULL_PARAM, oFull.getIdent());
					oFull = null;
				}
			}
		}else{
			session.removeAttribute(IDENT_PREGUNTA_PARAM);
		}
		return bOk;
	}

	public double getEspaiQuadern(String sNomQuadern){
		return FileUtil.getDiskSpace(getQuadernLocalDirectory(sNomQuadern));
	}

	/**
	 * Retorna l'espai total ocupat per l'usuari actual
	 * @return
	 */
	public long getEspaiOcupat(){
		return FileUtil.getDiskSpace(getUserLocalURL());
	}

	public int getPercentatgeOcupat(){
		return (int)((double)getEspaiOcupat()/(double)getMaxEspai()*100);
	}

	public int getPercentatgeOcupatQuadern(){
		return getPercentatgeOcupatQuadern(getIdQuadern());
	}

	protected int getPercentatgeOcupatQuadern(String sNomQuadern){
		int iPercentatge = (int)((double)getEspaiQuadern(sNomQuadern)/(double)getMaxEspai()*100);
		if (iPercentatge==0){
			iPercentatge = 1;
		}
		return iPercentatge;
	}

	/**
	 *
	 * @return espai disponible que encara pot ocupar l'usuari actual
	 */
	public long getEspaiLliure(){
		long lEspai = getMaxEspai()-getEspaiOcupat();
		if (lEspai<0){
			lEspai = 0;
		}
		return lEspai;
	}

	/**
	 *
	 * @return quota maxima que l'usuari pot ocupar
	 */
	public long getMaxEspai(){
		long lQuota = 0;
		try{
			String sQuota = getSetting(DEFAULT_QUOTA_KEY, DEFAULT_QUOTA);
			lQuota = Long.parseLong(sQuota);
		}catch (Exception e){
		}
		return lQuota;
	}

	/**
	 * @return Nom del driver del parser XML
	 */
	public String getParserDriver(){
		return getSetting(XML_PARSER_DRIVER);
	}

	/**
	 * @return URL de l'schema amb que validar el quadern
	 */
	public String getSchemaQTI(){
		return getSetting(XML_SCHEMA_QTI_KEY);
	}

	public boolean isEditableXML(){
		return (getSetting(EDITXML_KEY)!=null) && (getSetting(EDITXML_KEY).equalsIgnoreCase("true"));
	}

	public String getDefaultSkin(){
		return getSetting(DEFAULT_SKIN_KEY);
	}

	public Vector getSkins(){
		Vector vSkins = new Vector();
		String sSkins = getSetting(PUBLIC_SKINS_KEY);
		if (sSkins!=null){
			StringTokenizer st = new StringTokenizer(sSkins, ",");
			while (st.hasMoreTokens()){
				vSkins.addElement(st.nextToken());
			}
		}
		return vSkins;
	}

	public String getEditionMode(){
		String sEditionMode = getSessionParameter("edition_mode");
		if (sEditionMode==null || sEditionMode.trim().length()<=0){
			sEditionMode = VISUAL_EDITION_MODE;
		}
		return sEditionMode;
	}

	public boolean showEditXML(){
//		String sAccio = getSessionParameter(ACTION_PARAM);
//		return (sAccio!=null && sAccio.equalsIgnoreCase("editXML")) ||
//			(getEditionMode().equalsIgnoreCase(TEXT_EDITION_MODE));
		return (getEditionMode().equalsIgnoreCase(TEXT_EDITION_MODE));
	}

	public boolean showHelp(){
		return (getSetting(HELP_KEY)!=null) && (getSetting(HELP_KEY).equalsIgnoreCase("true"));
	}


//	***************************
//	* Parameter
//	***************************
	/**
	 * @param sParam
	 * @return retorna el parametre del request amb nom='sParam'; si no existeix retorna null
	 */
	public String getParameter(String sParam){
		return getParameter(request, sParam);
	}
	/**
	 * @param sParam
	 * @return retorna el parametre del request amb nom='sParam'; si no existeix retorna null
	 */
	public static String getParameter(HttpServletRequest request, String sParam){
		String sValue = null;
		try{
			sValue = request.getParameter(sParam);
		}catch (Exception e){
			logger.debug("EXCEPCIO obtenint el parametre '"+sParam+"' -->"+e);
		}
		return sValue;
	}

	/**
	 * Retorna el parametre; si no el troba retorna el valor per defecte
	 * @param sParam
	 * @param sDefault
	 * @return
	 */
	public String getParameter(String sParam, String sDefault){
		String sResult=sDefault;
		try{
			sResult=getParameter(sParam);
			if (sResult==null || sResult.trim().length()<=0 || sResult.equalsIgnoreCase("null")){
				sResult=sDefault;
			}
		}
		catch (Exception e){
			logger.debug("EXCEPCIO obtenint el parametre '"+sParam+"' -->"+e);
		}
		return sResult;
	}

	/**
	 * Si al request existeix un parametre amb nom='sParam' l'afegeix a la sessio i el
	 * retorna; Si no existeix, el busca a la sessio
	 * @param sParam
	 */
	public String getSessionParameter(String sParam){
		String result=request.getParameter(sParam);
		if (result==null || result.trim().equalsIgnoreCase(NULL) || result.trim().length()==0){
			Object o=session.getAttribute(sParam);
			if(o!=null)
				result=o.toString();
		}
		else
			session.setAttribute(sParam, result);
		return result;
	}

	/**
	 * Retorna un parametre enter. Veure 'getParameter'.
	 * @param sParam
	 * @param iDefault
	 * @return
	 */
	public int getIntParameter(String sParam, int iDefault){
		return getIntParameter(request, sParam, iDefault);
	}
	/**
	 * Retorna un parametre enter. Veure 'getParameter'.
	 * @param request
	 * @param sParam
	 * @param iDefault
	 * @return
	 */
	public static int getIntParameter(HttpServletRequest request, String sParam, int iDefault){
		int result=iDefault;
		try{
			String s=getParameter(request, sParam);
			if (s!=null && s.trim().length()>0)
				result=Integer.parseInt(s);
		}
		catch (Exception e){
			logger.debug("EXCEPCIO obtenint el parametre '"+sParam+"' -->"+e);
		}
		return result;
	}

	/**
	 * Retorna un parametre decimal. Veure 'getParameter'.
	 * @param sParam
	 * @param iDefault
	 * @return
	 */
	public double getDoubleParameter(String sParam, double dDefault){
		double result=dDefault;
		try{
			String s=getParameter(sParam);
			if (s!=null)
				result=Double.parseDouble(s);
		}
		catch (Exception e){
			logger.debug("EXCEPCIO obtenint el parametre '"+sParam+"' -->"+e);
		}
		return result;
	}

	/**
	 * Retorna un parametre boolea. Veure 'getParameter'
	 * @param sParam
	 * @param bDefault
	 * @return
	 */
	public boolean getBooleanParameter(String sParam, boolean bDefault){
		boolean result=bDefault;
		try{
			String s=getParameter(sParam);
			if (s!=null)
				result=s.trim().equalsIgnoreCase(SI) || s.trim().equalsIgnoreCase(Boolean.TRUE.toString());
		}
		catch (Exception e){
			logger.debug("EXCEPCIO obtenint el parametre '"+sParam+"' -->"+e);
		}
		return result;
	}

	/**
	 * @param sParam
	 * @return retorna els valors del parametre del request amb nom='sParam'; si no existeix retorna null
	 */
	public String[] getParameterValues(String sParam){
		String[] sValues = null;
		try{
			sValues = request.getParameterValues(sParam);
		}catch (Exception e){
			logger.debug("EXCEPCIO obtenint el parametre '"+sParam+"' -->"+e);
		}
		return sValues;
	}

//	***************************
//	* Language
//	***************************
	protected void initLanguage() throws Exception{
		String lang = getLanguage();
		Object[] obj=(Object[])localeObjects.get(lang);
		if(obj!=null){
			bundle=(ResourceBundle)obj[0];
			shortDateFormat=(DateFormat)obj[1];
		}
		else{
			Locale locale=new Locale(lang, lang.toUpperCase());
			Locale.setDefault(locale);
			String sBaseNameBundle = getSetting(BASE_NAME_BUNDLE_KEY, DEFAULT_BASE_NAME_BUNDLE);
			bundle=ResourceBundle.getBundle(sBaseNameBundle, locale);
			if(bundle==null){
				logger.fatal("INTERNAL ERROR: No s'ha pogut inicialitzar l'idioma");
				throw new Exception("Internal error");
			}
			shortDateFormat=DateFormat.getDateInstance(DateFormat.SHORT, locale);
			obj=new Object[]{bundle, shortDateFormat};
			localeObjects.put(lang, obj);
		}

	}

	public String getStartToken(){
		if (sStartToken==null){
			sStartToken = getSetting(START_TOKEN_KEY, DEFAULT_START_TOKEN);
		}
		return sStartToken;
	}

	public String getEndToken(){
		if (sEndToken==null){
			sEndToken = getSetting(END_TOKEN_KEY, DEFAULT_END_TOKEN);
		}
		return sEndToken;
	}

	public String getLanguage(){
		String lang = getSessionParameter(LANG);
		if(lang==null || lang.length()!=2){
			lang=getSetting(DEFAULT_LANGUAGE_KEY, Locale.getDefault().getLanguage());
		}else{
			lang=lang.toLowerCase();
		}
		return lang;
	}

	public String getMsg(String sKey){
		String sMessage = "";
		try{
			//sMessage = filter(bundle.getString(sKey));
			sMessage = bundle.getString(sKey);
		} catch (Exception e){
			logger.error("EXCEPCIO: No s'ha trobat el missatge per l'entrada '"+sKey+"' --> "+e);
			sMessage = sKey;
		}
		return sMessage;
	}

	public String getMsg(String sKey, Vector vValues){
		String sMessage = "";
		try{
			sMessage = bundle.getString(sKey);
			if (vValues!=null){
				for (int i=0;i<vValues.size();i++){
					sMessage = StringUtil.replace(sMessage, getStartToken()+i+getEndToken(), (String)vValues.elementAt(i));
				}
			}
		} catch (Exception e){
			logger.error("EXCEPCIO: No s'ha trobat el missatge per l'entrada '"+sKey+"' --> "+e);
			sMessage = sKey;
		}
		return sMessage;
	}

	public static String filter(String input){
		String result=(input==null ? "" : input);
		if(input!=null && input.length()>0){
			StringBuffer filtered = new StringBuffer(input.length());
			char c;
			for(int i=0; i<input.length(); i++) {
				c = input.charAt(i);
				String s=null;
				switch(c){
					case '<':
						s="&lt;";
						break;
					case '>':
						s="&gt;";
						break;
					case '"':
						s="&quot;";
						break;
					case '&':
						s="&amp;";
						break;
				}
				if(s!=null)
					filtered.append(s);
				else
					filtered.append(c);
			}
			result=filtered.substring(0);
		}
		return result;
	}

//	***************************
//	* QTI
//	***************************
	public Assessment getQuadern(){
		if (oQuadern==null){
			oQuadern = QTIUtil.getQuadern(getQTIObject());
		}
		return oQuadern;
	}

	public Vector getFullsQuadern(){
		return QTIUtil.getFullsQuadern(getQuadern());
	}

	public Section getFull(){
		Section oFull = null;
		if (getIdFull()!=null){
			oFull = QTIUtil.getFull(getQTIObject(), getIdFull());
		}
		return oFull;
	}

	public Item getPregunta(){
		Item oPregunta = null;
		if (getFull()!=null && getIdPregunta()!=null){
			oPregunta = getFull().getItem(getIdPregunta());
		}
		return oPregunta;
	}

	/**
	 * @return arrel del document QTI (questesinterop)
	 */
	public QTIObject getQTIObject(){
		QTIObject oQTI = null;
		Object o = session.getAttribute(QTI_OBJECT_SESSION);
		if (o!=null){
			oQTI = (QTIObject)o;
		} else{
			oQTI = QTIUtil.createQTIObject(getQuadernLocalFile());
			session.setAttribute(QTI_OBJECT_SESSION, oQTI);
		}
		//logger.debug("getQTIObject()-> "+oQTI);
		return oQTI;
	}

	public void saveQuadern(){
		if (session.getAttribute(ID_QUADERN_PARAM)!=null) {
			if (QTIUtil.saveQuadern(getQuadernLocalFile(), getQTIObject())){
				//logger.debug("S'ha guardat correctament el quadern '"+getIdQuadern()+"' de l'usuari '"+getUserId()+"'");
			}
		}
	}

	public String getTitle(QTIObject oQTI){
		String sTitle = null;
		if (oQTI!=null){
			sTitle = oQTI.getTitle();
		}
		return sTitle;
	}

	public String getShowName(QTIObject oQTI){
		return getShowName(oQTI, -1);
	}
	public String getShowName(QTIObject oQTI, int iMaxLongitud){
		String sName = null;
		if (oQTI!=null){
			sName = oQTI.getShowName();
			if (sName==null || (!(oQTI instanceof Assessment) && sName.equals(oQTI.getIdent()))){
				sName = oQTI.getTitle();
				if (sName!=null && sName.trim().length()==0){
					sName=null;
				}
			}
		}
		if (sName!=null && iMaxLongitud>0 && sName.length()>iMaxLongitud){
			sName = sName.substring(0,iMaxLongitud)+"...";
		}
		return sName;
	}

//	********************************************
//	* Recursos: imatges, audio, video, ...
//	********************************************

	/**
	 * Obte el nom dels recursos del tipus especificat (imatge, audio, ...) que conte el quadern actual
	 */
	public String[] getAssessmentResources(String sType){
		return getAssessmentResources(getIdQuadern(), sType);
	}
	/**
	 * Obte el nom dels recursos del tipus especificat (imatge, audio, ...) que conte el quadern indicat
	 */
	protected String[] getAssessmentResources(String sAssessmentName, String sType){
		String[] files = null;
		if (sType!=null){
			if (sType.equalsIgnoreCase(IMAGE_RESOURCE)){
				files = getAssessmentImages(sAssessmentName);
			}else if (sType.equalsIgnoreCase(FLASH_RESOURCE)){
				files = getAssessmentFlash(sAssessmentName);
			}else if (sType.equalsIgnoreCase(AUDIO_RESOURCE)){
				files = getAssessmentAudios(sAssessmentName);
			}else if (sType.equalsIgnoreCase(VIDEO_RESOURCE)){
				files = getAssessmentVideos(sAssessmentName);
			}else if (sType.equalsIgnoreCase(JCLIC_RESOURCE)){
				files = getAssessmentJClic(sAssessmentName);
			}else if (sType.equalsIgnoreCase(APPLICATION_RESOURCE)){
				files = getAssessmentApplications(sAssessmentName);
			}else{
				files = getAssessmentRest(sAssessmentName);
			}
		}
		return files;
	}

	/**
	 * Obte el nom de totes les imatges que conte el quadern indicat
	 */
	protected String[] getAssessmentImages(String sAssessmentName){
		String[] files = FileUtil.getImageFiles(getQuadernLocalDirectory(sAssessmentName));
		return files;
	}

	/**
	 * Obte el nom de totes les pel.licules flash que conte el quadern indicat
	 */
	protected String[] getAssessmentFlash(String sAssessmentName){
		String[] files = FileUtil.getFlashFiles(getQuadernLocalDirectory(sAssessmentName));
		return files;
	}

	/**
	 * Obte el nom de tots els fitxers de so que conte el quadern indicat
	 */
	protected String[] getAssessmentAudios(String sAssessmentName){
		String[] files = FileUtil.getAudioFiles(getQuadernLocalDirectory(sAssessmentName));
		return files;
	}

	/**
	 * Obte el nom de tots els fitxers de video que conte el quadern indicat
	 */
	protected String[] getAssessmentVideos(String sAssessmentName){
		String[] files = FileUtil.getVideoFiles(getQuadernLocalDirectory(sAssessmentName));
		return files;
	}

	/**
	 * Obte el nom de tots els fitxers de video que conte el quadern indicat
	 */
	protected String[] getAssessmentJClic(String sAssessmentName){
		String[] files = FileUtil.getJClicFiles(getQuadernLocalDirectory(sAssessmentName));
		return files;
	}

	/**
	 * Obt� el nom de tots els fitxers d'aplicacions que cont� el quadern indicat
	 */
	protected String[] getAssessmentApplications(String sAssessmentName){
		String[] files = FileUtil.getApplicationFiles(getQuadernLocalDirectory(sAssessmentName));
		return files;
	}

	/**
	 * Obte el nom de la resta de fitxers
	 */
	protected String[] getAssessmentRest(String sAssessmentName){
		String[] files = FileUtil.getOtherFiles(getQuadernLocalDirectory(sAssessmentName));
		return files;
	}

	/**
	 * Obte el nom de tots els recursos (imatges, so, ...) que conte el quadern actual
	 */
	public String[] getRecursosQuadern(){
		return getRecursosQuadern(getIdQuadern());
	}

	/**
	 * Obte el nom de tots els recursos (imatges, so, ...) que conte el quadern indicat
	 */
	protected String[] getRecursosQuadern(String sNomQuadern){
		String[] files = FileUtil.getMultimediaFiles(getQuadernLocalDirectory(sNomQuadern));
		return files;
	}


//	***************************
//	* Utils
//	***************************
	public String getServerBaseUrl(){
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
				if(port>0 && port!=CACHED_PORT)
					result.append(":").append(port);
				result.append(url.getFile());
				serverBaseUrl=result.toString();
			}
			catch(Exception e){
				logger.debug("Error corregint el URL "+serverBaseUrl+"--> "+e);
			}
		}
		return serverBaseUrl;
	}

	public String getRepositoryPath(){
		return getSetting(QVSTORE_REPOSITORY_PATH_KEY);
	}

	public String getUserLocalURL(){
		return getSetting(QVSTORE_LOCAL_URL_KEY)+getUserId();
	}

	/**
	 * Obte el directori local on es troba el quadern actual
	 * @return
	 */
	protected String getQuadernLocalDirectory(){
		String sDirectori = null;
		if (getIdQuadern()!=null){
			sDirectori = getQuadernLocalDirectory(getIdQuadern());
		} else{
			logger.error("No hi ha cap quadern seleccionat");
		}
		return sDirectori;
	}

	/**
	 * Obte el directori local on es troba el quadern indicat
	 * @param sNomQuadern
	 * @return el directori local on es troba el quadern; null si el nom del quadern no es correcte
	 */
	public String getQuadernLocalDirectory(String sNomQuadern){
		String sDirectori = null;
		if (sNomQuadern!=null && sNomQuadern.trim().length()>0){
			sDirectori = getUserLocalURL()+File.separator+sNomQuadern;
		}
		return sDirectori;
	}

	/**
	 * Obte el path del fitxer XML que conte el quadern actual
	 * @param sNomQuadern
	 * @return
	 */
	protected String getQuadernLocalFile(){
		return getQuadernLocalFile(getIdQuadern());
	}

	/**
	 * Obte el path del fitxer XML que conte el quadern indicat
	 * @param sNomQuadern
	 * @return
	 */
	protected String getQuadernLocalFile(String sNomQuadern){
		String sFile = null;
		if (sNomQuadern!=null && !sNomQuadern.equalsIgnoreCase("")){
			sFile = getQuadernLocalDirectory(sNomQuadern)+File.separator+sNomQuadern+".xml";
		}
		return sFile;
	}

	protected String getUserRemoteURL(){
		return getSetting(QVSTORE_REMOTE_URL_KEY)+getUserId();
	}

	/**
	 * Obte la URL del fitxer XML que conte el quadern actual
	 * @return
	 */
	public String getQuadernRemoteURL(){
		return getQuadernRemoteURL(getIdQuadern());
	}

	/**
	 * Obte la URL del fitxer XML que conte el quadern indicat
	 * @param sNomQuadern nom del quadern
	 * @return
	 */
	protected String getQuadernRemoteURL(String sNomQuadern){
		return getUserRemoteURL()+URL_PATH_SEPARATOR+sNomQuadern;
	}

	public String getQuadernResourcesURL(){
		return getQuadernResourcesURL(getIdQuadern());
	}

	public String getQuadernResourcesURL(String sQuadern){
		return getSetting("store.resources.remoteURL", true)+getUserId()+URL_PATH_SEPARATOR+sQuadern;
	}

	/**
	 * Obte la URL del fitxer XML que conte el quadern actual
	 * @return
	 */
	public String getQuadernRemoteFile(){
		return getQuadernRemoteFile(getIdQuadern());
	}

	/**
	 * Obte la URL del fitxer XML que conte el quadern indicat
	 * @param sNomQuadern nom del quadern
	 * @return
	 */
	protected String getQuadernRemoteFile(String sNomQuadern){
		return getUserRemoteURL()+URL_PATH_SEPARATOR+sNomQuadern+URL_PATH_SEPARATOR+sNomQuadern+".xml";
	}

	public String getQuadernPublicURL(){
		return getQuadernPublicURL(getIdQuadern());
	}

	/**
	 * Obte la URL que permet obrir el Quadern indicat
	 * @param sName nom del quadern
	 * @return
	 */
	protected String getQuadernPublicURL(String sName){
		return getUserRemoteURL()+URL_PATH_SEPARATOR+sName+URL_PATH_SEPARATOR;
	}

	public String getPreviewURL(){
		return getPreviewURL(getIdQuadern(), getIdFull(), getIdPregunta());
	}

	public String getPreviewURL(String sIdQuadern){
		return getPreviewURL(sIdQuadern, null, null);
	}

	protected String getPreviewURL(String sIdQuadern, String sIdFull, String sIdPregunta){
		String sPreviewURL = null;
		//String sBase = getSetting(HOME_QV_URL_KEY)+getSetting(QUADERN_QV_URL_KEY);
		String sBase = getSetting(QVSTORE_REMOTE_URL_KEY, true);
		sBase+=getUserId()+"/"+sIdQuadern;
		if (sBase.indexOf("?")<0){
			sBase+="?";
		}
		sBase+="&skin="+getDefaultSkin()+"&nocache=true";
		//sBase+="&user="+getUserId()+"&assessment="+sIdQuadern+"&skin="+getDefaultSkin()+"&nocache=true";
		if (sIdPregunta!=null && sIdPregunta.trim().length()>0){
			//sPreviewURL = getSetting(HOME_QV_URL_KEY)+getSetting(QUADERN_QV_URL_KEY)+getQuadernRemoteFile(sIdQuadern)+"&quadernXSL="+getDefaultSkin()+"&full="+(getQuadern().getSectionPosition(sIdFull)+1)+"&nocache=true"+"#item_"+sIdPregunta;
			sPreviewURL = sBase+"&section="+(getQuadern().getSectionPosition(sIdFull)+1)+"#item_"+sIdPregunta;
		} else if (sIdFull!=null && sIdFull.trim().length()>0){
			//sPreviewURL = getSetting(HOME_QV_URL_KEY)+getSetting(QUADERN_QV_URL_KEY)+getQuadernRemoteFile(sIdQuadern)+"&quadernXSL="+getDefaultSkin()+"&full="+(getQuadern().getSectionPosition(sIdFull)+1)+"&nocache=true";
			sPreviewURL = sBase+"&section="+(getQuadern().getSectionPosition(sIdFull)+1);
		}else{
			//sPreviewURL = getSetting(HOME_QV_URL_KEY)+getSetting(ASSIGNACIONS_QV_URL_KEY)+getQuadernRemoteFile(sIdQuadern)+"&quadernXSL="+getDefaultSkin()+"&nocache=true";
			sPreviewURL = sBase+"&section=0";
		}
		return sPreviewURL;
	}

	public boolean isMultipartContent(){
		return DiskFileUpload.isMultipartContent(request);
	}

	public String getTranslatedPath(String sPage){
		return getTranslatedPath(JSP_EDIT, sPage);
	}

	public String getTranslatedPath(String sMainPage, String sPage){
		String sPath = sMainPage+".jsp?"+PAGE_PARAM+"="+sPage;
		return sPath;
	}

	public int getImageWidth(String sImageURL){
		int iWidth = -1;
		try{
			iWidth = (int)getImageSize(sImageURL).getWidth();
		}catch (Exception e){
			logger.debug("EXCEPTION obtenint l'amplada de la imatge '"+sImageURL+"' --> e="+e);
		}
		return iWidth;
	}

	public int getImageHeight(String sImageURL){
		int iHeight = -1;
		try{
			iHeight = (int)getImageSize(sImageURL).getHeight();
		}catch (Exception e){
			logger.debug("EXCEPTION obtenint l'al�ada de la imatge '"+sImageURL+"' --> e="+e);
		}
		return iHeight;
	}

	public Dimension getImageSize(String sImageURL){
		Dimension dSize = new Dimension(-1, -1);
		try{
			ImageIcon image = getImage(sImageURL);
			if (image!=null){
				int iHeight = image.getIconHeight();
				int iWidth = image.getIconWidth();
				dSize = new Dimension(iWidth, iHeight);
			}
		}catch (Exception e){
			logger.debug("EXCEPTION obtenint les dimensions de la imatge '"+sImageURL+"' --> e="+e);
		}
		return dSize;
	}

	public Dimension getProportionalImageSize(int iWidth, int iHeight, int iMaxWidth, int iMaxHeight){
		int iProportionalWidth = -1;
		int iProportionalHeight = -1;
		if (iWidth>0 && iHeight>0){
			if (iWidth>iHeight && iWidth>iMaxWidth){
				double iProportion = (double)(iWidth/iMaxWidth);
				iProportionalWidth=iMaxWidth;
				iProportionalHeight=(int)(iHeight/iProportion);
			}else if (iHeight>iWidth && iHeight>iMaxHeight){
				double iProportion = (double)(iHeight/iMaxHeight);
				iProportionalHeight=iMaxHeight;
				iProportionalWidth=(int)(iWidth/iProportion);
			}
		}
		//logger.debug("real ["+iWidth+","+iHeight+"]  max["+iMaxWidth+","+iMaxHeight+"]  prop["+iProportionalWidth+","+iProportionalHeight+"]");
		return new Dimension(iProportionalWidth, iProportionalHeight);
	}

	protected ImageIcon getImage(String sImageURL){
		ImageIcon iImage = null;
		try{
			if (!hImages.containsKey(sImageURL)){
				java.net.URL url = new java.net.URL(sImageURL);
				if (url.openStream()!=null){
					iImage = new ImageIcon(url);
					hImages.put(sImageURL, iImage);
				}
			}else{
				iImage = (ImageIcon)hImages.get(sImageURL);
			}
		}catch (Exception e){
			logger.error("EXCEPCIO obtenint la imatge '"+sImageURL+"' --> e="+e);
		}
		return iImage;
	}

	protected String textToHTML(String sText){
		String sHTML = "";
		if (sText!=null){
			Enumeration enumLines = QTIUtil.getLines(sText);
			while (enumLines.hasMoreElements()){
				String sLine = (String)enumLines.nextElement();
				sHTML += sLine+"\n<BR/>";
			}
		}
		return sHTML;
	}

	/**
	 * @param sHTML
	 * @return
	 */
	protected String HTMLToText(String sHTML){
		String sText = "";
		if (sHTML!=null){
			int iBegin = 0;
			int iEnd = sHTML.indexOf("<BR/>");
			int i=0;
			while (iEnd>0 && iBegin<sHTML.length() && iEnd<=(sHTML.length()-5)){
				String sLine = sHTML.substring(iBegin, iEnd);
				sText += sLine;
				if (!sLine.endsWith("\n")){
					sText += "\n";
				}
				iBegin = iEnd+5;
				iEnd = sHTML.indexOf("<BR/>", iBegin);
				i++;
			}
			if (iBegin<sHTML.length() && iEnd<(sHTML.length()-5)){
				sText+=sHTML.substring(iBegin, sHTML.length());
			}
		}
		return sText;
	}


	public double toMB(double d){
		return round((double)d/(double)1000000, 2);
	}
	/**
	 *
	 * @param d double
	 * @param i scale (number of decimals to show)
	 * @return
	 */
	public double round(double d, int i){
		d = d + Math.pow(5*10,(-i - 1));
		d = d * Math.pow(10,i);
		d = (int)d;
		d = d / Math.pow(10,i);
		return d;
	}

//	***************************
//	* Database
//	***************************
	public LOMDatabase getLOMDatabase(){
		if (oLOMDB==null){
			boolean bOffline = getBooleanSetting("common.offline", false);
			String sXMLPath = getSetting("store.metadata.data");
			oLOMDB = new LOMDatabase(bOffline, sXMLPath);
		}
		return oLOMDB;
	}

	public UserDatabase getUserDatabase(){
		if (oUserDB==null){
			oUserDB = new UserDatabase();
		}
		return oUserDB;
	}

	public QVCPDocument getQVCPDocument(){
		return getQVCPDocument(getIdQuadern());
	}

	public QVCPDocument getQVCPDocument(String sQV){
		if (oCPDoc==null){
			oCPDoc = new QVCPDocument(getUserId(), sQV, getQuadernLocalDirectory(sQV), getQuadernRemoteURL(sQV));
		}
		return oCPDoc;

	}

	public void saveQVCPDocument(){
		saveQVCPDocument(getQVCPDocument());
	}

	public void saveQVCPDocument(QVCPDocument oCP){
		if (oCP!=null){
			try{
				oCP.prepare4XMLOutput();
				//XMLOutputter serializer = new XMLOutputter();
				//serializer.output(oCP.getJDOMDocument(), System.out);
				oCP.store2XMLFile();
			}catch (Exception poe){
				logger.error("EXCEPTION desant el document CP del quadern '"+getIdQuadern()+"' de l'usuari '"+getUserId()+"'");
				logger.error(poe.getClass().getName()+"-> "+poe.getMessage());
				poe.printStackTrace();
			}
		}
	}

}
