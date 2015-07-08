package edu.xtec.qv.biblio.beans;

import java.util.Vector;

public class QVActivityListBean extends QVBiblioBean {

	public final static String P_AREA = "p_area";
	public final static String P_LANGUAGE = "p_language";
	public final static String P_LEVEL = "p_level";
	public final static String P_AUTHOR = "p_author";
	public final static String P_TITLE = "p_title";
	public final static String P_DESCRIPTION = "p_description";
	public final static String P_CACHE = "p_cache";

	protected Vector vActivities;
	
	protected boolean start() {
		boolean bOK = true;
		try{
			if (getBiblioDatabase()==null){
				logger.error("ERROR: could not initialitze database");
				bOK = false;
			}
		}catch (Exception e){
			bOK = false;
			logger.error("EXCEPTION initializing QVActivityListBean --> e="+e);
		}
		return bOK;
	}
	
	public Vector getActivities(){
		if (vActivities==null){
			String sArea = getParameter(P_AREA);
			String sLanguage = getParameter(P_LANGUAGE);
			String sLevel = getParameter(P_LEVEL);
			String sAuthor = getParameter(P_AUTHOR);
			String sTitle = getParameter(P_TITLE);
			String sDescription = getParameter(P_DESCRIPTION);
			boolean bCache = !"false".equalsIgnoreCase(getParameter(P_CACHE));
			vActivities = getBiblioDatabase().getActivities(getLanguage(), sArea, sLanguage, sLevel, sAuthor, sTitle, sDescription, null, bCache);
		}
		return vActivities;
	}

	public Vector getNivells() {
		return getLOMDatabase().getNivells();
	}

	public Vector getArees() {
		return getLOMDatabase().getArees();
	}
	public Vector getIdiomes() {
		return getLOMDatabase().getIdiomes(getLanguage());
	}

}
