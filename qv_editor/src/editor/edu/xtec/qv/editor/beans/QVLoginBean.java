/*
 * QVMainBean.java
 * 
 * Created on 27/abril/2006
 */
package edu.xtec.qv.editor.beans;

import javax.servlet.http.Cookie;


/**
 * @author sarjona
 */
public class QVLoginBean extends QVSpecificBean {
	
	public static final String P_USERNAME = "p_username";
	public static final String P_PASSWORD = "p_password";

	public static final String LOGIN_ACTION_PARAM = "login";
	
	public QVLoginBean(QVBean mainBean){
		super(mainBean);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.QVBean#start()
	 */
	public boolean start() {
		boolean bOk = true;
		try{
			String sAccio = getParameter(ACTION_PARAM);
			if (sAccio!=null && sAccio.trim().length()>0){
				if (sAccio.equalsIgnoreCase(LOGIN_ACTION_PARAM)){
					// Un usuari intenta identificar-se en el sistema
					String sUsername = getParameter(P_USERNAME);
					String sPassword = getParameter(P_PASSWORD);
					if (validateUser(sUsername, sPassword)){
						Cookie cUser = new Cookie(mainBean.USER_KEY, sUsername);
						Cookie cEdu365 = new Cookie(mainBean.EDU365_KEY, mainBean.session.getId()+"P");
						//cUser.setDomain("edu365.com");
						//cEdu365.setDomain("edu365.com");
						cUser.setMaxAge(24 * 60 * 60);
						cEdu365.setMaxAge(24 * 60 * 60);
						//cUser.setPath("/");
						//cEdu365.setPath("/");
						mainBean.response.addCookie(cUser);
						mainBean.response.addCookie(cEdu365);
						logger.debug("cookie creada per "+sUsername);
						mainBean.redirectResponse(JSP_INDEX+".jsp");
					}
				}
			}
		} catch (Exception e){
			logger.error("EXCEPCIO inicialitzant QVLoginBean --> "+e);
			e.printStackTrace();
			bOk = false;
		}
		return bOk;
	}
	
	protected boolean validateUser(String sUsername, String sPassword){
		boolean bValidate = false;
		bValidate = mainBean.getUserDatabase().validateUser(sUsername, sPassword);
		if (!bValidate){
			mainBean.redirectToValidation(sUsername, sPassword);
		}
		return bValidate;
	}
	
}
