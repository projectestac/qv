/*
 * QVBiblioManagementBean.java
 * 
 * Created on 08/01/2007
 */
package edu.xtec.qv.admin.beans;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import edu.xtec.lom.Area;
import edu.xtec.lom.Nivell;
import edu.xtec.lom.bd.LOMDatabase;
import edu.xtec.qv.admin.FullActivity;
import edu.xtec.qv.biblio.Activity;
import edu.xtec.qv.biblio.Author;
import edu.xtec.qv.biblio.EducationalInstitution;
import edu.xtec.qv.biblio.db.BiblioDatabase;


public class QVBiblioManagementBean extends QVAdminBean {

	public final static String P_AREA = "p_area";
	public final static String P_LANGUAGE = "p_language";
	public final static String P_LEVEL = "p_level";
	public final static String P_AUTHOR = "p_author";
	public final static String P_STATE = "p_state";
	public final static String P_DESCRIPTION = "p_description";
	public final static String P_CACHE = "p_cache";
	public final static String P_TITLE = "p_title";
	public final static String P_IMAGE = "p_image";
	public final static String P_CENTRE = "p_centre";
	public final static String P_CREATION_DATE = "p_creation_date";
	public final static String P_REVISION_DATE = "p_revision_date";
	public final static String P_ACTIVITY_ID = "activity_id";
	
	protected LOMDatabase oLOMDB;	
	protected BiblioDatabase oBiblioDB;
	protected Activity oActivity = null;

	public boolean start(){
		boolean bOk = false;
		try{
			bOk = true;
			String sAction = getParameter("p_action");
			//logger.debug("action="+sAction);
			if ("save".equals(sAction)){
				FullActivity oActivity = getRequestActivity();
				getAdminDatabase().setActivity(oActivity);
				session.setAttribute(P_ACTIVITY_ID, String.valueOf(oActivity.getId()));
			}else{
				session.removeAttribute(P_ACTIVITY_ID);				
			}
		}catch (Exception e){
			logger.debug("Exception starting QVBiblioManagementBean-> "+e);
			e.printStackTrace();
		}
		return bOk;
	}
	
	public Activity getActivity(){
		if (oActivity==null){
			int iActivityId = getIntParameter(P_ACTIVITY_ID, -1);
			if (iActivityId<0) {
				try{
					String s = (String)session.getAttribute(P_ACTIVITY_ID);
					iActivityId = Integer.parseInt(s);
				}catch (Exception e){}
			}
			if (iActivityId>0){
				oActivity = getBiblioDatabase().getActivity(iActivityId, getLanguage());
			}
		}
		return oActivity;
	}
	
	public int getActivityId(){
		int iId = -1;
		if (getActivity()!=null){
			iId = getActivity().getId();
		}
		return iId;
	}
	
	public String getActivityTitle(){
		String sTitle = "";
		if (getActivity()!=null){
			sTitle = getActivity().getTitle(getLanguage());
		}
		return sTitle;
	}
	
	public String getActivityImage(){
		String sImage = "";
		if (getActivity()!=null){
			sImage = getActivity().getImage();
		}
		return sImage;
	}
	
	public String getActivityEducationalInstitutions(){
		String sInst = "";
		if (getActivity()!=null){
			Enumeration enumInst = getBiblioDatabase().getActivityEducationalInstitutions(getActivity().getId()).elements();
			while (enumInst.hasMoreElements()){
				if (sInst.length()>0) sInst+=",";
				sInst+=((EducationalInstitution)enumInst.nextElement()).getId();
			}
		}
		return sInst;
	}

	public String getActivityState(){
		String sState = "";
		if (getActivity()!=null){
			sState = getActivity().getState();
		}
		return sState;
	}
	
	public String getActivityCreationDate(){
		String sDate = "";
		if (getActivity()!=null){
			sDate = formatDate(getActivity().getCreationDate());
		}
		return sDate;
	}	
	
	public String getActivityRevisionDate(){
		String sDate = "";
		if (getActivity()!=null){
			sDate = formatDate(getActivity().getRevisionDate());
		}
		return sDate;
	}	
	
	public boolean hasActivityArea(int iArea){
		boolean bHas = false;
		if (getActivity()!=null){
			bHas = getBiblioDatabase().getActivityAreaIds(getActivity().getId()).contains(String.valueOf(iArea));
		}
		return bHas;
	}
	
	public boolean hasActivityLevel(int iLevel){
		boolean bHas = false;
		if (getActivity()!=null){
			bHas = getBiblioDatabase().getActivityLevelIds(getActivity().getId()).contains(String.valueOf(iLevel));
		}
		return bHas;
	}

	public boolean hasActivityAuthor(int iAuthor){
		boolean bHas = false;
		if (getActivity()!=null){
			Vector vAuthors = (Vector)getBiblioDatabase().getActivityAuthors(getActivity().getId()).get("aut");
			bHas = vAuthors.contains(new Author(iAuthor));
		}
		return bHas;
	}
	
	public Vector getArees(){
		return getLOMDatabase().getArees();
	}
	
	public Vector getLevels(){
		return getLOMDatabase().getNivells();
	}
	
	public Vector getLanguages(){
		return getLOMDatabase().getIdiomes(getLanguage());
	}
	
	public Vector getAuthors(){
		return getAdminDatabase().getAuthors();
	}
	
	public Vector getActivities(){
		Vector vActivities = new Vector();
		String sArea = getParameter(P_AREA);
		String sLanguage = getParameter(P_LANGUAGE);
		String sLevel = getParameter(P_LEVEL);
		String sAuthor = getParameter(P_AUTHOR);
		String sDescription = getParameter(P_DESCRIPTION);
		String sState = getParameter(P_STATE);
		boolean bCache = !"false".equalsIgnoreCase(getParameter(P_CACHE));
		vActivities = getBiblioDatabase().getActivities(getLanguage(), sArea, sLanguage, sLevel, sAuthor, null, sDescription, null, sState, bCache);
		return vActivities;
	}
	
	
	public LOMDatabase getLOMDatabase(){
		if (oLOMDB==null){
			oLOMDB = new LOMDatabase();
		}
		return oLOMDB;
	}
	
	public BiblioDatabase getBiblioDatabase(){
		if (oBiblioDB==null){
			oBiblioDB = new BiblioDatabase();
		}
		return oBiblioDB;
	}
	

	public static String[] getImageFiles(){
		String[] files = null;
		try{
			String sRoot = getSetting("biblio.imagePath");
			File fFile = new File(sRoot);
			files = fFile.list(new ImageFilter());
			Arrays.sort(files);
		} catch (Exception e){
			logger.error("EXCEPCIO obtenint les imatges de la biblioteca --> "+e);
		}
		return files;			
	}
	
	public static String getImageTypes(){
		return getSetting("file.image");
	}
	
	public FullActivity getRequestActivity(){
		int iId = getIntParameter(P_ACTIVITY_ID);
		FullActivity oActivity = new FullActivity(iId);
		oActivity.setImage(getParameter(P_IMAGE));
		if (getDateParameter(P_CREATION_DATE)!=null) oActivity.setCreationDate(getDateParameter(P_CREATION_DATE));
		else oActivity.setCreationDate(new Date()); 
		if (getDateParameter(P_REVISION_DATE)!=null) oActivity.setRevisionDate(getDateParameter(P_REVISION_DATE));
		else oActivity.setRevisionDate(new Date()); 
		oActivity.setState(getParameter(P_STATE));
		oActivity.addTitle(getParameter(P_TITLE), getLanguage());
		String[] sAreas = request.getParameterValues(P_AREA);
		if (sAreas!=null){
			for (int i=0;i<sAreas.length;i++){
				try{
					Area oArea = new Area(Integer.parseInt(sAreas[i]),"");
					oActivity.addArea(oArea);				
				}catch (Exception e){}
			}
		}
		
		String[] sLevels = request.getParameterValues(P_LEVEL);
		if (sLevels!=null){
			for (int i=0;i<sLevels.length;i++){
				try{
					Nivell oLevel = new Nivell(Integer.parseInt(sLevels[i]),1,-1,"");
					oActivity.addLevel(oLevel);				
				}catch (Exception e){}
			}
		}
		
		String[] sAuthors = request.getParameterValues(P_AUTHOR);
		if (sAuthors!=null){
			for (int i=0;i<sAuthors.length;i++){
				try{
					Author oAuthor = new Author(Integer.parseInt(sAuthors[i]));
					oActivity.addAuthor(oAuthor);				
				}catch (Exception e){}
			}
		}

		String sCentre = getParameter(P_CENTRE);
		if (sCentre!=null){
			StringTokenizer stCentre = new StringTokenizer(sCentre,",");
			while (stCentre.hasMoreTokens()){
				oActivity.addInstitution(stCentre.nextToken());
			}
		}
		
		return oActivity;
	}
	
	
	
}

/**
 * @author sarjona
 */
class ImageFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File fDir, String sName) {
		boolean bAccept = false;
		if (fDir!=null){
			bAccept = isImage(sName);
		}
		return bAccept;
	}
	
	public static boolean isImage(String sFile){
		boolean bIsFile=false;
		if (sFile!=null){
			String tmpFile = sFile.toLowerCase();
			StringTokenizer stFileExt = new StringTokenizer(QVBiblioManagementBean.getImageTypes(), ",");
			while (stFileExt.hasMoreTokens() && !bIsFile){
				String sFileExtension = stFileExt.nextToken();
				bIsFile = tmpFile.lastIndexOf(sFileExtension)>0;
			}
		}
		return bIsFile;
	}
    
	
}



