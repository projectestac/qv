/*
 * QVLoginBean.java
 *
 * Created on 27/abril/2006
 */
package edu.xtec.qv.admin.beans;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author sarjona
 */
public class QVLoginBean extends QVAdminBean {

	public static final String P_USERNAME = "p_username";
	public static final String P_PASSWORD = "p_password";

	public static final String LOGIN_ACTION_PARAM = "login";

	protected boolean bLoginIncorrect = false;

	/* (non-Javadoc)
	 * @see edu.xtec.qv.admin.QVAdminBean#start()
	 */
	public boolean start() {
		try{
			String sAccio = getParameter(ACTION_PARAM);
			if (sAccio != null && sAccio.trim().length() > 0 && sAccio.equalsIgnoreCase(LOGIN_ACTION_PARAM)){
				// Un usuari intenta identificar-se en el sistema
				String sUsername = getParameter(P_USERNAME);
				String sPassword = getParameter(P_PASSWORD);
                bLoginIncorrect = !super.getAdminDatabase().validateUser(sUsername, sPassword);
				if (!bLoginIncorrect){
					Cookie cUser = new Cookie(USER_KEY, sUsername);
					//cUser.setDomain("edu365.com");
					cUser.setMaxAge(24 * 60 * 60);
					//cUser.setPath("/");
					response.addCookie(cUser);
					logger.debug("Cookie creada per "+sUsername);
					response.sendRedirect("index.jsp");
					return true;
				}
			}
			if (isValidated()) {
				response.sendRedirect("index.jsp");
				return true;
			}
		} catch (Exception e){
			logger.error("EXCEPCIO inicialitzant QVLoginBean --> "+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean isLoginIncorrect(){
		return bLoginIncorrect;
	}

}
