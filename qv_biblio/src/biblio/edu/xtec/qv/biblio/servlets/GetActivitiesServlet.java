package edu.xtec.qv.biblio.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.xtec.qv.biblio.Activity;
import edu.xtec.qv.biblio.Version;

public class GetActivitiesServlet extends BiblioServlet{
	
	public final static String P_AREA = "p_area";
	public final static String P_LANGUAGE = "p_language";
	public final static String P_LEVEL = "p_level";
	public final static String P_AUTHOR = "p_author";
	public final static String P_TITLE = "p_title";
	public final static String P_DESCRIPTION = "p_description";
	public final static String P_REVISION_DATE = "p_revision_date";
	public final static String P_CACHE = "p_cache";
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.println(getHeader());
		String sLang = getLang(request);
		Enumeration enumActivities = getActivities(request).elements();
		while (enumActivities.hasMoreElements()){
			Activity oActivity = (Activity)enumActivities.nextElement();
			StringBuffer sb = new StringBuffer();
			sb.append(S_BEGIN);
			appendValue(sb, oActivity.getId());
			appendValue(sb, oActivity.getTitle(sLang));
			appendValue(sb, oActivity.getDescription(sLang));
			appendValue(sb, oActivity.getLicense());
			//appendValue(sb, getBiblioDatabase().getActivityArea(oActivity.getId(), sLang));
			//appendValue(sb, getBiblioDatabase().getActivityLevel(oActivity.getId(), sLang));
			appendValue(sb, getBiblioDatabase().getActivityAreaIds(oActivity.getId()));
			appendValue(sb, getBiblioDatabase().getActivityLevelIds(oActivity.getId()));
			Hashtable hAuthors = getBiblioDatabase().getActivityAuthors(oActivity.getId());
			if (hAuthors!=null && hAuthors.containsKey("aut")){
				appendValue(sb, hAuthors.get("aut"));
			}
			Vector vVersions = getBiblioDatabase().getActivityVersions(oActivity.getId());
			appendValue(sb, vVersions.size());
			Enumeration enumVersions = vVersions.elements();
			while (enumVersions.hasMoreElements()){
				Version oVersion = (Version)enumVersions.nextElement();
				appendValue(sb, oVersion.getId());
				appendValue(sb, oVersion.getLang());
				appendValue(sb, oVersion.getDescription());
				String sPath = oVersion.getFolder()+"_"+oVersion.getLang()+"/"+oVersion.getFolder()+"_"+oVersion.getLang()+".xml";
				/*String sPath = oVersion.getPath();*/
				if (sPath!=null && !sPath.startsWith("http://")) sPath=getBibliotecaURL()+sPath;
				appendValue(sb, sPath);
				appendValue(sb, oVersion.getSkin());
				appendValue(sb, oVersion.getRevisionDate());
			}
			if (!enumActivities.hasMoreElements()) sb.append(S_END);
			out.println(sb.toString());
		}
	}
	
	protected String getHeader(){
		StringBuffer sb = new StringBuffer();
		sb.append(S_BEGIN);
		appendValue(sb, "activity_id");
		appendValue(sb, "title");
		appendValue(sb, "description");
		appendValue(sb, "license");
		appendValue(sb, "area");
		appendValue(sb, "level");
		appendValue(sb, "author");
		appendValue(sb, "version_total_number");
		appendValue(sb, "version_id_i");
		appendValue(sb, "version_lang_i");
		appendValue(sb, "version_description_i");
		//appendValue(sb, "version_web_i");
		appendValue(sb, "version_xml_i");
		appendValue(sb, "version_skin_i");
		appendValue(sb, "version_revision_date_i");
		return sb.toString();
	}
	
	protected Vector getActivities(HttpServletRequest request){
		Vector vActivities = new Vector();
		String sArea = getParameter(request,P_AREA);
		String sLanguage = getParameter(request,P_LANGUAGE);
		String sLevel = getParameter(request,P_LEVEL);
		String sAuthor = getParameter(request,P_AUTHOR);
		String sTitle = getParameter(request,P_TITLE);
		String sDescription = getParameter(request,P_DESCRIPTION);
		String sRevisionDate = getParameter(request,P_REVISION_DATE);
		boolean bCache = !"false".equalsIgnoreCase(getParameter(request, P_CACHE));
		vActivities = getBiblioDatabase().getActivities(getLang(request), sArea, sLanguage, sLevel, sAuthor, sTitle, sDescription, sRevisionDate, bCache, true);
		return vActivities;
	}
	
	
	protected String getBibliotecaURL(){
		return "http://clic.xtec.cat/quaderns/biblioteca/";
	}
		
}
