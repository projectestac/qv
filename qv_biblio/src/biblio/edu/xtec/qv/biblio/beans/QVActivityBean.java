package edu.xtec.qv.biblio.beans;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import edu.xtec.qv.biblio.Activity;

public class QVActivityBean extends QVBiblioBean {

	public static String P_ACTIVITY_ID = "activity_id";

	protected int iIdActivitat = -1;
	protected Activity oActivity;
	
	
	protected boolean start() {
		boolean bOK = true;
		try{
			if (getActivityId()<0){
				logger.error("ERROR: 'activity_id' parameter is missing");
				bOK = false;
			} else{
				if (getBiblioDatabase()==null){
					logger.error("ERROR: could not initialize database");
					bOK = false;
				}
			}
		}catch (Exception e){
			bOK = false;
			logger.error("EXCEPTION initializing QVActivityBean --> e="+e);
		}
		return bOK;
	}
	
	public int getActivityId(){
		if (iIdActivitat<0){
			iIdActivitat = getIntParameter(P_ACTIVITY_ID);
		}
		return iIdActivitat;
	}
	
	public Activity getActivity(){
		if (oActivity==null){
			oActivity = getBiblioDatabase().getActivity(getActivityId(), getLanguage());
		}
		return oActivity;
	}
	
	public String getActivityDate(){
		Date dDate = getBiblioDatabase().getActivityDate(getActivityId());
		return formatDate(dDate);
	}

	public String getActivityImage(){
		return getBiblioDatabase().getActivityImage(getActivityId());
	}

	public String getActivityLicense(){
		return getBiblioDatabase().getActivityLicense(getActivityId());
	}
	public boolean isCCLicense(){
		return "by-nc-sa".equals(getActivityLicense());
	}

	public String getActivityTitle(){
		return getBiblioDatabase().getActivityTitle(getActivityId(), getLanguage());
	}

	public String getActivityDescription(){
		return getBiblioDatabase().getActivityDescription(getActivityId(), getLanguage());
	}

	public String getActivityArea(){
		return getBiblioDatabase().getActivityArea(getActivityId(), getLanguage());
	}
	
	public Vector getActivityLevel(){
		return getBiblioDatabase().getActivityLevel(getActivityId(), getLanguage());
	}

	public Vector getActivityVersions(){
		return getBiblioDatabase().getActivityVersions(getActivityId());
	}
	
	public Hashtable getActivityAuthors(){
		return getBiblioDatabase().getActivityAuthors(getActivityId());
	}
	
	public Vector getActivityAuthors(String sRol){
		return getAuthorsByRole(getActivityAuthors(), sRol);
	}
	
	public Hashtable getVersionAuthors(int iVersionId){
		return getBiblioDatabase().getVersionAuthors(iVersionId);
	}
	
	protected Vector getAuthorsByRole(Hashtable hAuthors, String sRol){
		Vector vAuthors = new Vector();
		if (hAuthors!=null && sRol!=null && hAuthors.containsKey(sRol)){
			vAuthors = (Vector)hAuthors.get(sRol);
		}
		return vAuthors;
	}
	
	public Vector getActivityEducationalInstitutions(){
		return getBiblioDatabase().getActivityEducationalInstitutions(getActivityId());
	}
	
	public String getLanguageText(String sLang){
		return getBiblioDatabase().getLanguageText(sLang, getLanguage());
	}

	public String getRoleText(String sIdRol){
		return getBiblioDatabase().getRoleText(sIdRol, getLanguage());
	}

	
}
