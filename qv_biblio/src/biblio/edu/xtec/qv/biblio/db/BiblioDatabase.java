package edu.xtec.qv.biblio.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

import edu.xtec.qv.biblio.Activity;
import edu.xtec.qv.biblio.Author;
import edu.xtec.qv.biblio.EducationalInstitution;
import edu.xtec.qv.biblio.Version;
import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;

/**
 * @author sarjona
 *
 */
public class BiblioDatabase {

	public final static String PROPERTIES_PATH = "/edu/xtec/resources/properties/";
	public final static String DBCONF_FILE = "connectionQV.properties";

	private static ConnectionBeanProvider broker;
	private ConnectionBean c;
	private static Properties pDB;
	
	private static final Logger logger = Logger.getRootLogger();
	private static final Hashtable hActivities = new Hashtable();
	
	public BiblioDatabase() {
		getConnection();
		freeConnection();
	}
	


//	************************************	
//	* Database access
//	************************************

	public Vector getActivities(String sLang, String sArea, String sLanguage, String sLevel, String sAuthor, String sTitle, String sDescription, String sRevisionDate, boolean bCache){
		return getActivities(sLang, sArea, sLanguage, sLevel, sAuthor, sTitle, sDescription, sRevisionDate, "public", bCache, false);
	}
	
	public Vector getActivities(String sLang, String sArea, String sLanguage, String sLevel, String sAuthor, String sTitle, String sDescription, String sRevisionDate, boolean bCache, boolean bAllInfo){
		return getActivities(sLang, sArea, sLanguage, sLevel, sAuthor, sTitle, sDescription, sRevisionDate, "public", bCache, bAllInfo);
	}
	public Vector getActivities(String sLang, String sArea, String sLanguage, String sLevel, String sAuthor, String sTitle, String sDescription, String sRevisionDate, String sState, boolean bCache){
		return getActivities(sLang, sArea, sLanguage, sLevel, sAuthor, sTitle, sDescription, sRevisionDate, sState, bCache, false);
	}
    /**
     * @param sLang 
     * Return Vector with all activities
     */
	public Vector getActivities(String sLang, String sArea, String sLanguage, String sLevel, String sAuthor, String sTitle, String sDescription, String sRevisionDate, String sState, boolean bCache, boolean bAllInfo){
		Vector vActivities = new Vector();
		if (sLang!=null){
			boolean bAll = false;
			if (sArea==null && sLevel==null && sLanguage==null && sAuthor==null && sTitle==null && sDescription==null && sRevisionDate==null){
				// Get all activities
				if (hActivities.containsKey(sLang) && bCache){
					vActivities = (Vector)hActivities.get(sLang);
					return vActivities;
				}else{
					bAll = true;
				}
			}
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT ba.id_activitat, ba.data_revisio, bad.titol, bad.descripcio ");
				query.append("FROM biblio_activitat ba, biblio_activitat_desc bad ");
				if (sArea!=null) query.append(", biblio_activitat_area baa ");
				if (sLevel!=null) query.append(", biblio_activitat_nivell ban ");
				if (sLanguage!=null) query.append(", biblio_versio bv, biblio_versio_idioma bvi ");
				if (sAuthor!=null) query.append(", biblio_autor baut, biblio_autor_act bauta ");
				query.append("WHERE ba.id_activitat=bad.id_activitat AND bad.idioma=? ");
				if (sState!=null) query.append(" AND ba.estat=? ");
				if (sArea!=null) query.append(" AND baa.id_activitat=ba.id_activitat AND baa.id_area=? ");
				if (sLevel!=null) query.append(" AND ban.id_activitat=ba.id_activitat AND ban.id_nivell=? ");
				if (sLanguage!=null) query.append(" AND ba.id_activitat=bv.id_activitat AND bv.id_versio=bvi.id_versio AND bvi.idioma=? ");
				if (sAuthor!=null) query.append(" AND ba.id_activitat=bauta.id_activitat AND baut.id_autor=bauta.id_autor AND baut.nom_complet_maj LIKE EN_MAJUSCULES('%"+sAuthor+"%') ");
				if (sTitle!=null || sDescription!=null) query.append(" AND bad.titol_maj LIKE EN_MAJUSCULES('%"+sTitle+"%') ");
				if (sDescription!=null) query.append(" AND bad.descripcio_maj LIKE EN_MAJUSCULES('%"+sDescription+"%') ");
				if (sRevisionDate!=null) {
					try{
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						long lRevisionDate = Long.parseLong(sRevisionDate);
						query.append(" AND TO_CHAR(ba.data_revisio, 'YYYYMMDD')>'"+sdf.format(new Date(lRevisionDate))+"' ");
					}catch (Exception e){
						e.printStackTrace();
					}
				}
				query.append("ORDER BY ba.data_revisio DESC ");
				//logger.debug("query="+query);
				PreparedStatement pstmt = getConnection().getPreparedStatement(query.toString());
				int index = 1;
				pstmt.setString(index++, sLang);
				if (sState!=null) pstmt.setString(index++, sState);
				if (sArea!=null) pstmt.setString(index++, sArea);
				if (sLevel!=null) pstmt.setString(index++, sLevel);
				if (sLanguage!=null) pstmt.setString(index++, sLanguage);
				ResultSet rs=pstmt.executeQuery();
				while (rs.next()){
					int iIdActivitat = rs.getInt("id_activitat");
					Date dRevision = rs.getTimestamp("data_revisio");
					String sTitol = rs.getString("titol");
					Activity oActivity = new Activity(iIdActivitat,dRevision);
					oActivity.addTitle(sLang, sTitol);
					if (bAllInfo){
						String sActDescription = rs.getString("descripcio");
						oActivity.addDescription(sLang, sActDescription);
					}
					vActivities.addElement(oActivity);
				}
				if (bAll) hActivities.put(sLang, vActivities);
				rs.close();
				getConnection().closeStatement(pstmt);				
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting activity versions --> "+e);
			}finally{
				freeConnection();				
			}
		}
		return vActivities;
	}

    /**
     * @param iActivityId activity identifier
     * Return Activity width specified identifier 
     */
	public Activity getActivity(int iActivityId, String sLang){
		Activity oActivity = null;
		if (iActivityId>0 && sLang!=null){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT ba.data_creacio, ba.data_revisio, ba.imatge, ba.llicencia, ba.estat, bad.titol, bad.descripcio ");
				query.append("FROM biblio_activitat ba, biblio_activitat_desc bad ");
				query.append("WHERE bad.id_activitat=ba.id_activitat AND ba.id_activitat=? AND bad.idioma=? ");
				PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
				pstmt.setInt(1, iActivityId);
				pstmt.setString(2, sLang);
				ResultSet rs=pstmt.executeQuery();
				if (rs.next()){
					Date dCreacio = rs.getDate("data_creacio");
					Date dRevisio = rs.getDate("data_revisio");
					String sImatge = rs.getString("imatge");
					String sLlicencia = rs.getString("llicencia");
					String sState = rs.getString("estat");
					String sTitol = rs.getString("titol");
					String sDescripcio = rs.getString("descripcio");
					oActivity = new Activity(iActivityId, dCreacio, dRevisio, sImatge, sLlicencia);
					oActivity.setState(sState);
					oActivity.addTitle(sLang, sTitol);
					oActivity.addDescription(sLang, sDescripcio);
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting activity --> "+e);
			}finally{
				freeConnection();				
			}
		}
		return oActivity;
	}

	/**
     * @param iActivityId
     * Return revised activity date
     */
	public Date getActivityDate(int iActivityId){
		Date dDate = null;
		if (iActivityId>0){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT GREATEST(data_creacio, data_revisio) ");
				query.append("FROM biblio_activitat ");
				query.append("WHERE id_activitat=? ");
				PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
				pstmt.setInt(1, iActivityId);
				ResultSet rs=pstmt.executeQuery();
				if (rs.next()){
					dDate = rs.getDate(1);
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting activity date --> "+e);
			}finally{
				freeConnection();				
			}
			
		}
		return dDate;
	}

    /**
     * @param iActivityId
     * Return activity image
     */
	public String getActivityImage(int iActivityId){
		String sImage = null;
		if (iActivityId>0){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT imatge ");
				query.append("FROM biblio_activitat ");
				query.append("WHERE id_activitat=? ");
				PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
				pstmt.setInt(1, iActivityId);
				ResultSet rs=pstmt.executeQuery();
				if (rs.next()){
					sImage = rs.getString(1);
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting activity image --> "+e);
			}finally{
				freeConnection();				
			}
		}
		return sImage;
	}

    /**
     * @param iActivityId activity identifier
     * Return license type
     */
	public String getActivityLicense(int iActivityId){
		String sLicense = null;
		if (iActivityId>0){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT llicencia ");
				query.append("FROM biblio_activitat ");
				query.append("WHERE id_activitat=? ");
				PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
				pstmt.setInt(1, iActivityId);
				ResultSet rs=pstmt.executeQuery();
				if (rs.next()){
					sLicense = rs.getString(1);
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting activity license --> "+e);
			}finally{
				freeConnection();				
			}
		}
		return sLicense;
	}


	/**
     * @param iActivityId
     * @param sLang
     * Return activity title for activity id and language specified
     */
	public String getActivityTitle(int iActivityId, String sLang){
		String sTitle = "";
		if (iActivityId>0 && sLang!=null){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT titol ");
				query.append("FROM biblio_activitat_desc ");
				query.append("WHERE id_activitat=? and idioma=? ");
				PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
				pstmt.setInt(1, iActivityId);
				pstmt.setString(2, sLang);
				ResultSet rs=pstmt.executeQuery();
				if (rs.next()){
					sTitle = rs.getString("titol");
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting activity title --> "+e);
			}finally{
				freeConnection();				
			}
		}
		return sTitle;
	}

    /**
     * @param iActivityId
     * @param sLang
     * Return activity description for activity id and language specified
     */
	public String getActivityDescription(int iActivityId, String sLang){
		String sDescription = "";
		if (iActivityId>0 && sLang!=null){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT descripcio ");
				query.append("FROM biblio_activitat_desc ");
				query.append("WHERE id_activitat=? and idioma=? ");
				PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
				pstmt.setInt(1, iActivityId);
				pstmt.setString(2, sLang);
				ResultSet rs=pstmt.executeQuery();
				if (rs.next()){
					sDescription = rs.getString("descripcio");
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting activity description --> "+e);
			}finally{
				freeConnection();				
			}
		}
		return sDescription;
	}

    /**
     * @param iActivityId
     * @param sLang
     * Return area for activity id and language specified
     */
	public String getActivityArea(int iActivityId, String sLang){
		String sArea = null;
		if (iActivityId>0 && sLang!=null){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT d.text ");
				query.append("FROM biblio_activitat_area baa, area a, diccionari d ");
				query.append("WHERE baa.ID_AREA=a.ID_AREA and a.CODI_DIC= d.CODI and baa.id_activitat=? and d.IDIOMA=? ");
				PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
				pstmt.setInt(1, iActivityId);
				pstmt.setString(2, sLang);
				ResultSet rs=pstmt.executeQuery();
				if (rs.next()){
					sArea = rs.getString(1);
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting activity area --> "+e);
			}finally{
				freeConnection();				
			}
		}
		return sArea;
	}

    /**
     * @param iActivityId
     * Return area for activity id and language specified
     */
	public Vector getActivityAreaIds(int iActivityId){
		Vector vAreas = new Vector();
		if (iActivityId>0){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT baa.id_area ");
				query.append("FROM biblio_activitat_area baa, area a ");
				query.append("WHERE baa.ID_AREA=a.ID_AREA and baa.id_activitat=? ");
				PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
				pstmt.setInt(1, iActivityId);
				ResultSet rs=pstmt.executeQuery();
				while (rs.next()){
					vAreas.addElement(rs.getString(1));
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting activity areas --> "+e);
			}finally{
				freeConnection();				
			}
		}
		return vAreas;
	}

    /**
     * @param iActivityId
     * @param sLang
     * Return Vector with all educational level for activity id and language specified
     */
	public Vector getActivityLevel(int iActivityId, String sLang){
		Vector vLevel = new Vector();
		if (iActivityId>0 && sLang!=null){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT d.text ");
				query.append("FROM biblio_activitat_nivell ban, nivell n, diccionari d ");
				query.append("WHERE ban.id_nivell=n.id_nivell and n.CODI_DIC= d.CODI and ban.id_activitat=? and d.IDIOMA=? ");
				PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
				pstmt.setInt(1, iActivityId);
				pstmt.setString(2, sLang);
				ResultSet rs=pstmt.executeQuery();
				while (rs.next()){
					String sLevel = rs.getString(1);
					vLevel.addElement(sLevel);
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting activity educational level for activity '"+iActivityId+"' --> "+e);
			}finally{
				freeConnection();				
			}
		}
		return vLevel;
	}

    /**
     * @param iActivityId
     * Return Vector with all educational level for activity id
     */
	public Vector getActivityLevelIds(int iActivityId){
		Vector vLevel = new Vector();
		if (iActivityId>0){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT ban.id_nivell ");
				query.append("FROM biblio_activitat_nivell ban, nivell n ");
				query.append("WHERE ban.id_nivell=n.id_nivell and ban.id_activitat=? ");
				PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
				pstmt.setInt(1, iActivityId);
				ResultSet rs=pstmt.executeQuery();
				while (rs.next()){
					String sLevel = rs.getString(1);
					vLevel.addElement(sLevel);
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting activity educational level for activity '"+iActivityId+"' --> "+e);
			}finally{
				freeConnection();				
			}
		}
		return vLevel;
	}

	
    /**
     * @param iActivityId activity identifier
     * Return Vector with all activity versions
     */
	public Vector getActivityVersions(int iActivityId){
		Vector vVersions = new Vector();
		if (iActivityId>0){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT bvi.id_versio, bv.data_creacio, bv.data_revisio, bv.folder, bv.skin, bv.page, bv.descripcio, bv.sections, bv.items, bv.zipsize, bv.totalscore, bvi.idioma, bvi.path ");
				query.append("FROM biblio_versio bv, biblio_versio_idioma bvi ");
				query.append("WHERE bv.id_activitat=? AND bv.estat='public' AND bv.id_versio=bvi.id_versio ");
				PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
				pstmt.setInt(1, iActivityId);
				ResultSet rs=pstmt.executeQuery();
				while (rs.next()){
					int iIdVersio = rs.getInt("id_versio");
					Date dCreation = rs.getDate("data_creacio");
					Date dRevision = rs.getDate("data_revisio");
					String sDescription = rs.getString("descripcio");
					String sLang = rs.getString("idioma");
					String sFolder = rs.getString("folder");
					String sPath = rs.getString("path");
					String sSkin = rs.getString("skin");
					int iPage = rs.getInt("page");
					int iSections = rs.getInt("sections");
					int iItems = rs.getInt("items");
					int iZipSize = rs.getInt("zipsize");
					long lTotalScore = rs.getLong("totalscore");
					Version oVersion = new Version(iIdVersio, dCreation, dRevision, sDescription, sLang, sFolder, sPath, sSkin, iPage, iSections, iItems, iZipSize, lTotalScore);
					vVersions.addElement(oVersion);
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting activity versions --> "+e);
			}finally{
				freeConnection();				
			}
		}
		return vVersions;
	}

    /**
     * @param iActivityId activity identifier
     * Return Hashtable with all activity authors ('id_rol', Vector Author)
     */
	public Hashtable getActivityAuthors(int iActivityId){
		Hashtable hAuthors = new Hashtable();
		if (iActivityId>0){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT ba.id_autor, ba.nom_complet, ba.mail, ba.url, ba.id_edu365, baa.id_rol ");
				query.append("FROM biblio_autor_act baa, biblio_autor ba ");
				query.append("WHERE baa.id_autor=ba.id_autor AND baa.id_activitat=? ");
				PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
				pstmt.setInt(1, iActivityId);
				ResultSet rs=pstmt.executeQuery();
				while (rs.next()){
					int idAutor = rs.getInt("id_autor");
					String sNomComplet = rs.getString("nom_complet");
					String sRol = rs.getString("id_rol");
					String sMail = rs.getString("mail");
					String sURL = rs.getString("url");
					String sEdu365Id = rs.getString("id_edu365");
					Author oAuthor = new Author(idAutor, sNomComplet, sRol, sMail, sURL, sEdu365Id);
					Vector vAuthors;
					if (hAuthors.containsKey(sRol)){
						vAuthors = (Vector)hAuthors.get(sRol);
					}else{
						vAuthors = new Vector();
						hAuthors.put(sRol, vAuthors);
					}
					vAuthors.addElement(oAuthor);
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting activity authors --> "+e);
			}finally{
				freeConnection();				
			}
		}
		return hAuthors;
	}
	
    /**
     * @param iVersionId version identifier
     * Return Hashtable with all version authors ('id_rol', Vector Author)
     */
	public Hashtable getVersionAuthors(int iVersionId){
		Hashtable hAuthors = new Hashtable();
		if (iVersionId>0){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT ba.id_autor, ba.nom_complet, ba.mail, ba.url, ba.id_edu365, bav.id_rol ");
				query.append("FROM biblio_autor_ver bav, biblio_autor ba ");
				query.append("WHERE bav.id_autor=ba.id_autor AND bav.id_versio=? ");
				PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
				pstmt.setInt(1, iVersionId);
				ResultSet rs=pstmt.executeQuery();
				while (rs.next()){
					int idAutor = rs.getInt("id_autor");
					String sNomComplet = rs.getString("nom_complet");
					String sRol = rs.getString("id_rol");
					String sMail = rs.getString("mail");
					String sURL = rs.getString("url");
					String sEdu365Id = rs.getString("id_edu365");
					Author oAuthor = new Author(idAutor, sNomComplet, sRol, sMail, sURL, sEdu365Id);
					Vector vAuthors;
					if (hAuthors.containsKey(sRol)){
						vAuthors = (Vector)hAuthors.get(sRol);
					}else{
						vAuthors = new Vector();
						hAuthors.put(sRol, vAuthors);
					}
					vAuthors.addElement(oAuthor);
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting version authors --> "+e);
			}finally{
				freeConnection();				
			}
		}
		return hAuthors;
	}


    /**
     * @param iActivityId activity identifier
     * Return Vector with all activity educational institution
     */
	public Vector getActivityEducationalInstitutions(int iActivityId){
		Vector vEduInst = new Vector();
		if (iActivityId>0){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT c.id, c.nom, c.web ");
				query.append("FROM biblio_centre_act bca, clic.centres c ");
				query.append("WHERE c.id=bca.id_centre AND bca.id_activitat=? ");
				PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
				pstmt.setInt(1, iActivityId);
				ResultSet rs=pstmt.executeQuery();
				while (rs.next()){
					String sIdCentre = rs.getString("id");
					String sNom = rs.getString("nom");
					String sWeb = rs.getString("web");
					EducationalInstitution oEduInst = new EducationalInstitution(sIdCentre, sNom, sWeb);
					vEduInst.addElement(oEduInst);
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting activity educational institutions --> "+e);
			}finally{
				freeConnection();				
			}
		}
		return vEduInst;
	}

	/**
     * @param sLang language to get specific language text
     * @param sLangCode language dictionary code
     * Return Language text
     */
	public String getLanguageText(String sLang, String sLangCode){
		String sText = sLang;
		if (sLang!=null && sLangCode!=null){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT d.text  ");
				query.append("FROM idiomes i, diccionari d ");
				query.append("WHERE i.ID=? and d.codi=i.codi_dic and d.idioma=? ");
				PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
				pstmt.setString(1, sLang);
				pstmt.setString(2, sLangCode);
				ResultSet rs=pstmt.executeQuery();
				if (rs.next()){
					sText = rs.getString("text");
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting language text --> "+e);
			}finally{
				freeConnection();				
			}
		}
		return sText;
	}

    /**
     * @param sRoleId role to translate to sLangCode
     * @param sLangCode language dictionary code
     * Return Language text
     */
	public String getRoleText(String sRoleId, String sLangCode){
		String sText = sRoleId;
		if (sRoleId!=null && sLangCode!=null){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT d.text  ");
				query.append("FROM rol r, diccionari d ");
				query.append("WHERE r.id_rol=? and d.codi=r.codi_dic and d.idioma=? ");
				PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
				pstmt.setString(1, sRoleId);
				pstmt.setString(2, sLangCode);
				ResultSet rs=pstmt.executeQuery();
				if (rs.next()){
					sText = rs.getString("text");
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting role text --> "+e);
			}finally{
				freeConnection();
			}
		}
		return sText;
	}

    /**
     * @param sCodiDic  dictionary code to translate to sLang
     * @param sLangCode language code
     * Return Language text
     */
	public String getDictionaryText(String sCodiDic, String sLang){
		String sText = sLang;
		if (sCodiDic!=null && sLang!=null){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT d.text  ");
				query.append("FROM diccionari d ");
				query.append("WHERE d.codi=? and d.idioma=? ");
				PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
				pstmt.setString(1, sCodiDic);
				pstmt.setString(2, sLang);
				ResultSet rs=pstmt.executeQuery();
				if (rs.next()){
					sText = rs.getString("text");
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting language text --> "+e);
			}finally{
				freeConnection();				
			}
		}
		return sText;
	}

	
	
//	************************************	
//	* Database connection
//	************************************
	
	public ConnectionBeanProvider getConnectionBeanProvider(){
		try{
			if(broker == null) { // Only created by first servlet to call
				broker = ConnectionBeanProvider.getConnectionBeanProvider(true, getDBProperties());
			}
		}catch (Exception e){
			logger.error("EXCEPTION getting ConnectionBeanProvider-> "+e);
			e.printStackTrace();
		}
		return broker;
	}
	
	public ConnectionBean getConnection(){
		if (c==null){
			try{
				c = getConnectionBeanProvider().getConnectionBean();
				c.getConnection().setAutoCommit(true);
			}catch (Exception e){
				logger.error("EXCEPTION getting connection -> "+e);				
			}
		}
		return c;
	}

	public void freeConnection() {
		if (c!=null && broker!=null) {
			broker.freeConnectionBean(c);
			c=null;
		}
	}
	
	
//	************************************	
//	* Properties
//	************************************
	
/*	public static Properties getDBProperties() throws Exception{
		if (pDB==null){
			pDB = new Properties();
			try{
				pDB.load(BiblioDatabase.class.getResourceAsStream(PROPERTIES_PATH+DBCONF_FILE));
				File f = new File(System.getProperty("user.home"), DBCONF_FILE);
				//logger.debug("file="+f+" exists?"+f.exists());
				if(f.exists()){
					FileInputStream is=new FileInputStream(f);
					pDB.load(is);
					is.close();
				}
			} catch (FileNotFoundException f) {
				logger.error(f);
			} catch (IOException e) {
				logger.error(e);
			}
			//logger.debug("DBProperties="+pDB);
		}
		return pDB;
	}*/
	
	public static Properties getDBProperties() throws Exception{
		return loadProperties(PROPERTIES_PATH, DBCONF_FILE);
	}
	
	private static Properties loadProperties (String sPath, String sFile) throws Exception{
		Properties p = new Properties();
		try{
			p.load(BiblioDatabase.class.getResourceAsStream(sPath+sFile));
			File f = new File(System.getProperty("user.home"), sFile);
			if(f.exists()){
				FileInputStream is=new FileInputStream(f);
				p.load(is);
				is.close();
			}
		} catch (FileNotFoundException f) {
			logger.error(f);
		} catch (IOException e) {
			logger.error(e);
		}
		return p;    	
	}
	

}
