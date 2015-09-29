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

	protected boolean bLoginIncorrect = false;

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
			if (sAccio != null && sAccio.trim().length() > 0 && sAccio.equalsIgnoreCase(LOGIN_ACTION_PARAM)){
				// Un usuari intenta identificar-se en el sistema
				String sUsername = getParameter(P_USERNAME);
				String sPassword = getParameter(P_PASSWORD);
                bLoginIncorrect = !mainBean.getUserDatabase().validateUser(sUsername, sPassword);
				if (!bLoginIncorrect){
					Cookie cUser = new Cookie(mainBean.USER_KEY, sUsername);
					//cUser.setDomain("edu365.com");
					cUser.setMaxAge(24 * 60 * 60);
					//cUser.setPath("/");
					mainBean.response.addCookie(cUser);
					logger.debug("Cookie creada per "+sUsername);
					mainBean.redirectResponse(JSP_INDEX+".jsp");
				}
			}
		} catch (Exception e){
			logger.error("EXCEPCIO inicialitzant QVLoginBean --> "+e);
			e.printStackTrace();
			bOk = false;
		}
		return bOk;
	}

	public boolean isLoginIncorrect(){
		return bLoginIncorrect;
	}

}
