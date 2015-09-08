/*
 * QVHTMLBean.java
 * 
 * Created on 10/juny/2005
 */
package edu.xtec.qv.admin.beans;

import java.util.Vector;

public class QVPreviewBean extends QVAdminBean {

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
	public final String P_QUADERN = "p_quadern";
	public final String P_XML = "p_xml";
	
	protected String sTipus;
	protected String sTarget;
	protected String sQuadern;
	protected String sXML;

	public boolean start(){
		return true;
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
	
	/**
	 * Get usernames associated to current user
	 * @return Vector of String with usernames associated to current user
	 */
	public Vector getUsernames(){
		return getAdminDatabase().getUserAssociation(getUserId());
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
			vQuaderns = executeServlet(getSetting("qv.server")+"getQuaderns?username="+sUsername, true);
		}
		return vQuaderns;
	}
	
	public Vector getAparences(){
		String sUsername = null;
		if (getUser()!=null) sUsername=getUser().getUsername();
		return getAparences(sUsername);
	}
	
	public Vector getAparences(String sUsername){
		String sServlet = getSetting("qv.server")+"getAparences";
		if (sUsername!=null && sUsername.trim().length()>0){
			sServlet += "?username="+sUsername;
		}
		return executeServlet(sServlet);
	}
	
	public Vector getAllAparences(){
		String sServlet = getSetting("qv.server")+"getAparences?all";
		return executeServlet(sServlet);		
	}
    
}
