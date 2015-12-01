package edu.xtec.qv.db;

/*
 * LearningDatabaseAdmin.java
 *
 * Created on 29 de agosto de 2002, 17:50
 */


/**
 *
 * @author  Albert
 * @version
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import edu.xtec.qv.servlet.QVServlet;
import edu.xtec.qv.servlet.util.Assignacio;
import edu.xtec.qv.servlet.util.Feedback;
import edu.xtec.qv.servlet.util.FullAssignacio;
import edu.xtec.qv.servlet.util.Intervencio;
import edu.xtec.qv.servlet.util.Pregunta;
import edu.xtec.qv.servlet.util.Quadern;
import edu.xtec.qv.servlet.util.Resposta;
import edu.xtec.qv.servlet.util.Skin;
import edu.xtec.qv.util.Utility;
import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;
//import org.jdom.*;

public class LearningDatabaseAdmin implements QVDataManage{
    
	public final static String PROPERTIES_PATH = "/edu/xtec/resources/properties/";
	public final static String DBCONF = "connectionQV.properties";

	private static ConnectionBeanProvider broker;
	private ConnectionBean c;

	//protected ConnectionBean connect=null;
	public static boolean verbose=true;
	private static final Logger logger = Logger.getRootLogger();
    
	int DBMSType=-1;
    
	/** Creates new LearningDatabaseAdmin */
	public LearningDatabaseAdmin() {
	}
	
	/** Creates new LearningDatabaseAdmin */
	public LearningDatabaseAdmin(ConnectionBean c) {
		this.c=c;
	}
	
    
//	*******************	
//	* QUADERN
//	*******************
	/**
	 * Afegeix el quadern indicat a la base de dades
	 * @param oQuadern
	 * @return true si el quadern s'ha pogut inserir correctament; false en cas contrari
	 */
	public boolean addQuadern(Quadern oQuadern){
		boolean bOk=false;
		String query = null;
		try{
			query= "INSERT INTO quadern ";
			query+="(quadernURL, quadernXSL, endpage, data_creacio, max_lliuraments, autoCorreccio, obligatori_contestar, autoAdvance, write_intervencions) ";
			query+="VALUES (?,?,?,?,?,?,?,?,?)";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,oQuadern.getURL());
			pstmt.setString(2,oQuadern.getXSL());
			pstmt.setString(3,oQuadern.getEndPage());
			pstmt.setDate(4, (oQuadern.getDataCreacio()!=null?new java.sql.Date(oQuadern.getDataCreacio().getTime()):null));
			pstmt.setInt(5,oQuadern.getMaxLliuraments());
			pstmt.setBoolean(6,oQuadern.isAutoCorreccio());
			pstmt.setBoolean(7,oQuadern.isObligatoriContestar());
			pstmt.setBoolean(8,oQuadern.isAutoAdvance());
			pstmt.setBoolean(9,oQuadern.isWriteIntervencions());
			pstmt.executeUpdate();
			getConnection().closeStatement(pstmt);
			logger.debug("S'ha creat correctament el quadern '"+oQuadern+"'");			
			bOk = true;
		}
		catch (SQLException e) {
			printSQLError(e, query,"ERROR creant el quadern '"+oQuadern+"'");
		}
		return bOk;
	}
    
	/**
	 * Comproba si existeix el quadern indicat a la base de dades
	 * @param oQuadern
	 * @return true si el quadern existeix; false en cas contrari
	 */
	public boolean existQuadern(Quadern oQuadern){
		boolean bExist=false;
		String query = null;
		try{
			if (oQuadern!=null){
				PreparedStatement pstmt = null;
				if (oQuadern.getIdQuadern()<0){
					query= "SELECT COUNT(*) ";
					query+="FROM quadern ";
					query+="WHERE quadernURL=? AND quadernXSL=? AND endpage=? ";
					query+="AND max_lliuraments=? AND autocorreccio=? AND obligatori_contestar=? ";
					query+="AND autoadvance=? AND write_intervencions=?";
					pstmt=getConnection().getPreparedStatement(query);
					pstmt.setString(1,oQuadern.getURL());
					pstmt.setString(2,oQuadern.getXSL());
					pstmt.setString(3,oQuadern.getEndPage());
					pstmt.setInt(4,oQuadern.getMaxLliuraments());
					pstmt.setBoolean(5,oQuadern.isAutoCorreccio());
					pstmt.setBoolean(6,oQuadern.isObligatoriContestar());
					pstmt.setBoolean(7,oQuadern.isAutoAdvance());
					pstmt.setBoolean(8,oQuadern.isWriteIntervencions());
				} else {
					query= "SELECT COUNT(*) ";
					query+="FROM quadern ";
					query+="WHERE id_quadern=? ";
					pstmt=getConnection().getPreparedStatement(query);
					pstmt.setInt(1,oQuadern.getIdQuadern());
				}
				ResultSet rs=pstmt.executeQuery();
				if (rs.next()){
					int i=rs.getInt(1);
					bExist=(i>0);
				}
				rs.close();
				getConnection().closeStatement(pstmt);
			}
		}
		catch (SQLException e) {
			printSQLError(e, query,"ERROR comprovant si existeix el quadern '"+oQuadern+"'");
		}
		return bExist;
	}
    
    /**
     * Obté l'identificador del quadern indicat; si el quadern no existeix retorna -1
     * @param oQuadern
     * @return
     */
	public int getIdQuadern(Quadern oQuadern){
		int iIdQuadern = -1;
		String query = null;
		try{
			query= "SELECT id_quadern ";
			query+="FROM quadern ";
			query+="WHERE quadernURL=? AND quadernXSL=? AND endpage";
			if (oQuadern.getEndPage()!=null && oQuadern.getEndPage().length()>0){
				query+="=? ";
			}else{
				query+=" is null ";
			}
			query+="AND max_lliuraments=? AND autocorreccio=? AND obligatori_contestar=? ";
			query+="AND autoadvance=? AND write_intervencions=?";
			logger.debug("query="+query);
			int iParameterIndex = 1;
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(iParameterIndex++,oQuadern.getURL());
			pstmt.setString(iParameterIndex++,oQuadern.getXSL());
			if (oQuadern.getEndPage()!=null && oQuadern.getEndPage().length()>0){
				pstmt.setString(iParameterIndex++,oQuadern.getEndPage());
			}
			pstmt.setInt(iParameterIndex++,oQuadern.getMaxLliuraments());
			pstmt.setBoolean(iParameterIndex++,oQuadern.isAutoCorreccio());
			pstmt.setBoolean(iParameterIndex++,oQuadern.isObligatoriContestar());
			pstmt.setBoolean(iParameterIndex++,oQuadern.isAutoAdvance());
			pstmt.setBoolean(iParameterIndex++,oQuadern.isWriteIntervencions());
			ResultSet rs=pstmt.executeQuery();
			if (rs.next()){
				iIdQuadern = rs.getInt("id_quadern");
			}
			rs.close();
			getConnection().closeStatement(pstmt);
		}
		catch (SQLException e) {
			printSQLError(e, query,"ERROR obtenint l'identificador del quadern '"+oQuadern+"'");
		}
		return iIdQuadern;
	}
	
	/**
	 * Obté el quadern. Omple l'identificador i la data de creacio de la base de dades si
	 * el quadern ja existeix. En cas contrari deixa l'identificador a -1 i la posa a la 
	 * data de creació l'actual
	 * @param sQuadernURL
	 * @param sQuadernXSL
	 * @param sEndPage
	 * @param iMaxLliuraments
	 * @param bAutoCorreccio
	 * @param bObligatoriContestar
	 * @param bAutoAdvance
	 * @param bWriteIntervencions
	 * @return <code>Quadern</code>
	 */
	public Quadern getQuadern(String sQuadernURL, String sQuadernXSL, String sEndPage, int iMaxLliuraments, boolean bAutoCorreccio, boolean bObligatoriContestar, boolean bAutoAdvance, boolean bWriteIntervencions){
		Quadern oQuadern = null;
		String query = null;
		try{
			query= "SELECT id_quadern, data_creacio ";
			query+="FROM quadern ";
			query+="WHERE quadernURL=? AND quadernXSL";
			if (sQuadernXSL!=null && sQuadernXSL.length()>0) query+="=? ";
			else query+=" is null ";
			/*query+="AND endpage";
			if (sEndPage!=null && sEndPage.length()>0){
				query+="=? ";
			}else{
				query+=" is null ";
			}*/
			query+="AND max_lliuraments=? AND autocorreccio=? AND obligatori_contestar=? ";
			query+="AND autoadvance=? AND write_intervencions=? ";
			int iParameterIndex = 1;
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(iParameterIndex++,sQuadernURL);
			if (sQuadernXSL!=null && sQuadernXSL.length()>0) pstmt.setString(iParameterIndex++, sQuadernXSL);
			/*if (sEndPage!=null && sEndPage.length()>0){
				pstmt.setString(iParameterIndex++,sEndPage);
			}*/
			pstmt.setInt(iParameterIndex++,iMaxLliuraments);
			pstmt.setBoolean(iParameterIndex++,bAutoCorreccio);
			pstmt.setBoolean(iParameterIndex++,bObligatoriContestar);
			pstmt.setBoolean(iParameterIndex++,bAutoAdvance);
			pstmt.setBoolean(iParameterIndex++,bWriteIntervencions);
			ResultSet rs=pstmt.executeQuery();
			if (rs.next()){
				int iIdQuadern = rs.getInt("id_quadern");
				Date dDataCreacio = rs.getDate("data_creacio");
				oQuadern = new Quadern(iIdQuadern, sQuadernURL, sQuadernXSL, sEndPage, dDataCreacio, iMaxLliuraments, bAutoCorreccio, bObligatoriContestar, bAutoAdvance, bWriteIntervencions);
			} else{
				oQuadern = new Quadern(-1, sQuadernURL, sQuadernXSL, sEndPage, new Date(), iMaxLliuraments, bAutoCorreccio, bObligatoriContestar, bAutoAdvance, bWriteIntervencions);
			}
			rs.close();
			getConnection().closeStatement(pstmt);
		}
		catch (SQLException e) {
			printSQLError(e, query,"ERROR obtenint el quadern amb URL '"+sQuadernURL+"'");
		}
		return oQuadern;			
	}
    
	/**
	 * Obté el quadern. Omple l'identificador i la data de creacio de la base de dades si
	 * el quadern ja existeix. En cas contrari deixa l'identificador a -1 i la posa a la 
	 * data de creació l'actual
	 * @param iIdQuadern
	 * @return <code>Quadern</code>
	 */
	public Quadern getQuadern(int iIdQuadern){
		Quadern oQuadern = null;
		String query = null;
		try{
			query= "SELECT * ";
			query+="FROM quadern ";
			query+="WHERE id_quadern=? ";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setInt(1,iIdQuadern);
			ResultSet rs=pstmt.executeQuery();
			if (rs.next()){
				String sQuadernURL = rs.getString("quadernURL");
				String sQuadernXSL = rs.getString("quadernXSL");
				String sEndPage = rs.getString("endpage");
				Date dDataCreacio = rs.getDate("data_creacio");
				int iMaxLliuraments = rs.getInt("max_lliuraments");
				boolean bAutoCorreccio = rs.getBoolean("autocorreccio");
				boolean bObligatoriContestar = rs.getBoolean("obligatori_contestar");
				boolean bAutoAdvance = rs.getBoolean("autoadvance");
				boolean bWriteIntervencions = rs.getBoolean("write_intervencions");
				oQuadern = new Quadern(iIdQuadern, sQuadernURL, sQuadernXSL, sEndPage, dDataCreacio, iMaxLliuraments, bAutoCorreccio, bObligatoriContestar, bAutoAdvance, bWriteIntervencions);
			}
			rs.close();
			getConnection().closeStatement(pstmt);			
		}
		catch (SQLException e) {
			printSQLError(e, query,"ERROR obtenint el quadern '"+iIdQuadern+"'");
		}
		return oQuadern;			
	}
	
//	*******************	
//	* APARENCES
//	*******************
	    
    /**
     * @param vIdSkins Vector with skins identifiers
     * @param sLang Skin text language
     * @param bAll Returns all skins if true; otherwise only returns public skins
     * Return Vector with Skin objects
     */
	public Vector getSkins(Vector vIdSkins, String sLang, boolean bAll){
		Vector vSkins = new Vector();
		if (sLang==null) sLang = "ca";
		StringBuffer query = new StringBuffer();
		try{
			query.append("SELECT s.id_skin, d.text ");
			query.append("FROM qv_skin s, diccionari d ");
			query.append("WHERE s.codi_dic=d.codi AND d.idioma=? ");
			if (vIdSkins!=null && vIdSkins.size()>0){
				Enumeration enumId = vIdSkins.elements();
				query.append(" AND ( ");
				while (enumId.hasMoreElements()){
					String sId = (String)enumId.nextElement();
					query.append(" s.id_skin='");					
					query.append(sId);					
					query.append("' ");
					if (enumId.hasMoreElements()) query.append(" OR ");					
				}				
				query.append(" ) ");					
			}else{
				if (!bAll){
					query.append(" AND s.is_public>0 ");				
				}				
			}
			//logger.debug("query="+query);
			PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
			pstmt.setString(1, sLang);
			ResultSet rs=pstmt.executeQuery();
			while (rs.next()){
				String sId = rs.getString("id_skin");
				String sText = rs.getString("text");
				Skin oSkin = new Skin(sId);
				oSkin.addText(sLang, sText);
				vSkins.addElement(oSkin);
			}
			rs.close();
			getConnection().closeStatement(pstmt);
		}
		catch (SQLException e) {
			logger.error("EXCEPTION getting skins --> "+e);
		}
		return vSkins;
	}
    

//	*******************	
//	* ASSIGNACIO
//	*******************
	
	/**
	 * Crea una assignacio (i el quadern si es necessari) amb les dades indicades. 
	 * @param sIdAssignacio
	 * @param sQuadernURL
	 * @param sQuadernXSL
	 * @param sEndPage
	 * @param iMaxLliuraments
	 * @param bAutoCorreccio
	 * @param bObligatoriContestar
	 * @param bAutoAdvance
	 * @param bWriteIntervencions
	 * @return true si l'assignacio s'ha pogut crear correctament; false en cas contrari
	 */
	public boolean createAssignacio(String sIdAssignacio, String sQuadernURL, String sQuadernXSL, String sEndPage, Date dEndDate, int iMaxLliuraments, boolean bAutoCorreccio, boolean bObligatoriContestar, boolean bAutoAdvance, boolean bWriteIntervencions, String sIP){
/*		if (sQuadernXSL==null || sQuadernXSL.equalsIgnoreCase("qti_infantil")){
			//TODO: eliminar aquesta linia (l'he posat de prova mentre no afegeixen el nou look&feel a educampus)
			sQuadernXSL = "interneti";
		}*/
		Quadern oQuadern = getQuadern(sQuadernURL, sQuadernXSL, sEndPage, iMaxLliuraments, bAutoCorreccio, bObligatoriContestar, bAutoAdvance, bWriteIntervencions);
		logger.debug("createAssignacio="+oQuadern);
		Assignacio oAssignacio = new Assignacio(sIdAssignacio, oQuadern, new Date(), dEndDate, sIP);
		return addAssignacio(oAssignacio);
	}
    
    /**
     * Inicialitza l'assignacio indicada (creant els seus fulls)
     * @param sIdAssignacio
     * @param hFulls
     * @return
     */
	public boolean initAssignacio(String sIdAssignacio, Vector vFulls){
		boolean bOk=true;
		try{
			Enumeration enumFulls = vFulls.elements();
			while (bOk && enumFulls.hasMoreElements()){
				FullAssignacio oFull = (FullAssignacio)enumFulls.nextElement();
				bOk = addFullAssignacio(sIdAssignacio, oFull);
			}
		}
		catch (Exception e){
			bOk=false;
			logger.error("ERROR inicialitzant els valors de l'assignacio '"+sIdAssignacio+"'--> "+e);
		}
		if (bOk){
			logger.debug("Inicialitzats correctament els fulls de l'assignacio '"+sIdAssignacio+"'");			
		} else {
			logger.error("No s'ha inicialitzat correctament l'assignacio '"+sIdAssignacio+"'");
		}
		return bOk;
	}
    
    
	/**
	 * Afegeix l'assignacio indicada a la base de dades. Primer comprova si ja existeix un quadern
	 * amb les dades especificades i si no existeix el crea. Despres afegeix l'assignacio
	 * a la base de dades.
	 * @param oAssignacio
	 * @param sIP IP des de la que es crea l'assignacio
	 * @return true si l'assignacio s'ha pogut crear correctament; false en cas contrari
	 */
	public boolean addAssignacio(Assignacio oAssignacio){
		boolean bOk = false;
		String query = null;
		try{
			logger.debug("addAssignacio="+oAssignacio);
			if (oAssignacio.getQuadern().getIdQuadern()<0){
				// El quadern no existeix encara a la base de dades. S'ha de crear
				bOk = addQuadern(oAssignacio.getQuadern());
				int iIdQuadern = getIdQuadern(oAssignacio.getQuadern());
				if (!bOk || iIdQuadern<0){
					return false;
				}
				oAssignacio.getQuadern().setIdQuadern(iIdQuadern);
			}

			Assignacio oOldAssignacio = getAssignacio(oAssignacio.getIdAssignacio());
			java.sql.Date dSQLEndDate = null;
			try{
				if (oAssignacio.getDataFinalitzacio()!=null)
					dSQLEndDate = new java.sql.Date(oAssignacio.getDataFinalitzacio().getTime());
			}catch(Exception e){
				dSQLEndDate = null;
			}
			if (oOldAssignacio==null){				
				query="INSERT INTO assignacio ";
				query+="(id_assignacio, id_quadern, data_creacio, ip";
				if (dSQLEndDate!=null){
					query+=", data_fi";
				}
				query+=") ";
				query+="VALUES (?,?,?,?";
				if (dSQLEndDate!=null){
					query+=",?";
				}
				query+=") ";
				
				PreparedStatement pstmt=getConnection().getPreparedStatement(query);
				pstmt.setString(1,oAssignacio.getIdAssignacio());
				pstmt.setInt(2,oAssignacio.getQuadern().getIdQuadern());
				pstmt.setDate(3, new java.sql.Date(oAssignacio.getDataCreacio().getTime())); //Data actual
				pstmt.setString(4, oAssignacio.getIP()!=null?oAssignacio.getIP():"");
				if (dSQLEndDate!=null){
					pstmt.setDate(5, dSQLEndDate); //Data actual
				}
				pstmt.executeUpdate();
				getConnection().closeStatement(pstmt);				
				logger.debug("S'ha creat correctament l'assignacio '"+oAssignacio+"'");			
			}else{
				Quadern oInitialQuadern = oOldAssignacio.getQuadern();
				Quadern oNewQuadern = oAssignacio.getQuadern();
				if (oInitialQuadern.getIdQuadern()!=oNewQuadern.getIdQuadern() || dSQLEndDate!=null){
					if (oInitialQuadern.getURL().equalsIgnoreCase(oNewQuadern.getURL())){
						// modificar els paràmetres de configuració de l'assignacio
						query="UPDATE assignacio ";
						query+="SET id_quadern=?, ip=? ";
						if (dSQLEndDate!=null){
							query+=", data_fi=?";
						}
						query+="WHERE id_assignacio=?";
						PreparedStatement pstmt=getConnection().getPreparedStatement(query);
						pstmt.setInt(1,oNewQuadern.getIdQuadern());
						pstmt.setString(2,oAssignacio.getIP());
						int i=3;
						if (dSQLEndDate!=null){
							pstmt.setDate(i,dSQLEndDate);
							i++;
						}
						pstmt.setString(i,oAssignacio.getIdAssignacio());
						pstmt.executeUpdate();
						getConnection().closeStatement(pstmt);						
						//logger.debug("S'han modificat correctament els paràmetres de configuració de l'assignacio '"+oAssignacio+"'");						
					}else{
						// TODO: Decidir què fer. Hi ha dues opcions: 
						// 			* esborrar les dades associades a l'assignació (fulls, respostes, ...) i actualitzar l'assignació
						// 			* ignorar la petició (no modificar res)
						logger.debug("L'assignacio '"+oAssignacio.getIdAssignacio()+"' ja existeix, i per tant no s'ha tornat a afegit.");
					}
				}
				//logger.debug("L'assignacio '"+oAssignacio.getIdAssignacio()+"' ja existeix, i per tant no s'ha tornat a afegit.");
			}
		}
		catch (SQLException e) {
			printSQLError(e, query,"ERROR creant l'assignacio '"+oAssignacio+"'");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Actualitza el contingut de la pagina inicial de l'assignacio especificada
	 * @param oAssignacio
	 * @return
	 */
	public boolean updateAssignacio(Assignacio oAssignacio){
		boolean bOk = true;
		try{
			FullAssignacio oFull = oAssignacio.getFullInicial();
			if (oFull!=null){
				bOk = updateFullAssignacio(oAssignacio.getIdAssignacio(), oFull);
			}
		}catch (Exception e){
			bOk = false;
			logger.error("EXCEPCIO actualitzant les dades de l'assignacio '"+oAssignacio+"'");
		}
		return bOk;
	}
       
	/*        
		public boolean updateQuadernResponse(String sIdAssignacio,String sUserResponses, int iPuntuacio, String sEstatEntrega, String sFeedback, String sInteraction, boolean incLliuraments, String sTime){
			// feedback i intervencions poden ser nulls;
			boolean bOk=false;
			String query=null;
			try{
				query="UPDATE assignacio ";
				query+="SET estat_lliurament=?, ";
				if (incLliuraments) query+="num_cops_lliurat=num_cops_lliurat+1, ";
				if (sInteraction!=null) query+="intervencions=?, ";
				query+="darrera_puntuacio=?, ";
				query+="data_darrer_lliurament=? ";
				query+="WHERE id_assignacio=?";
				PreparedStatement pstmt=connect.getPreparedStatement(query);
				int iNumArgs=1;
				pstmt.setString(iNumArgs,sEstatEntrega); iNumArgs++;
				if (sInteraction!=null){
					pstmt.setString(iNumArgs,sInteraction);
					iNumArgs++;
				}
				pstmt.setInt(iNumArgs,iPuntuacio);
				pstmt.setTimestamp(iNumArgs+1,new Timestamp(new java.util.GregorianCalendar().getTime().getTime()));
				pstmt.setString(iNumArgs+2,sIdAssignacio);
				pstmt.executeUpdate();
				bOk=true;
			}
			catch (SQLException e){
				printSQLError(e, query, "ERROR actualitzant l'assignacio '"+sIdAssignacio+"'");
			}
			catch (Exception e){
				logger.error("ERROR actualitzant l'assignacio '"+sIdAssignacio+"' -->"+e);
				if (verbose) e.printStackTrace(System.out);
			}
			return bOk;
		}
	*/    
	
	public boolean deleteAssignacio(String sIdAssignacio) {
		boolean bOk=true;
		String query=null;
		try{
			//query="DELETE FROM assignacio ";
			//query+="WHERE id_assignacio=?";

			query="UPDATE assignacio ";
			query+="SET estat='delete' WHERE id_assignacio=?";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			pstmt.executeUpdate();
			getConnection().closeStatement(pstmt);
			logger.debug("S'ha esborrat correctament l'assignacio '"+sIdAssignacio+"'");			
			
/*			
			// Potser s'hauria de fer com a transacció.
			if (deleteIntervencionsAssignacio(sIdAssignacio)){
				if (deleteFeedbacksAssignacio(sIdAssignacio)){
					if (deleteRespostesAssignacio(sIdAssignacio)){
						if (deletePreguntesAssignacio(sIdAssignacio)){
							if (deleteFullsAssignacio(sIdAssignacio)){            
								query="DELETE FROM assignacio ";
								query+="WHERE id_assignacio=?";
								PreparedStatement pstmt=getConnection().getPreparedStatement(query);
								pstmt.setString(1,sIdAssignacio);
								pstmt.executeUpdate();
								getConnection().closeStatement(pstmt);
								logger.debug("S'ha esborrat correctament l'assignacio '"+sIdAssignacio+"'");								
							}
						}
					}
				}
			}
*/			
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR esborrant l'assignacio '"+sIdAssignacio+"'");
			bOk=false;
		}
		return bOk;
	}
    
	public boolean deleteAssignacioBefore(java.util.Date dDeleteBefore) {
		boolean bOk=true;
		String query=null;
		try{
			//Potser s'hauria de fer com a transacció.
			query="SELECT id_assignacio FROM assignacio ";
			query+="WHERE data_creacio<? AND data_creacio IS NOT NULL";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setDate(1,new java.sql.Date(dDeleteBefore.getTime()));
			ResultSet rs = pstmt.executeQuery();
            
			while (rs.next() && bOk){
				String sAssignacioId=rs.getString("id_assignacio");
				bOk=deleteAssignacio(sAssignacioId);
			}
			rs.close();
			getConnection().closeStatement(pstmt);			
			logger.debug("S'han esborrat correctament les assignacions amb data anterior a '"+dDeleteBefore+"'");
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR esborrant les assignacions amb data anterior a'"+dDeleteBefore+"'");
			bOk=false;
		}
		return bOk;
	}    
	
	/**
	 * Obte l'assignacio indicada
	 * @param sIdAssignacio
	 */
	public Assignacio getAssignacio(String sIdAssignacio) {
		Assignacio oAssignacio=null;
		String query=null;
		try{
			query="SELECT * FROM assignacio WHERE id_assignacio=?";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			ResultSet rs=pstmt.executeQuery();
			if (rs.next()){
				Date dDataCreacio=rs.getDate("data_creacio");
				Date dDataFi=rs.getDate("data_fi");
				int iIdQuadern = rs.getInt("id_quadern");
				String sIP = rs.getString("ip");
				Quadern oQuadern = getQuadern(iIdQuadern);
				oAssignacio=new Assignacio(sIdAssignacio, oQuadern, dDataCreacio, dDataFi, sIP, getFullsAssignacio(sIdAssignacio));
			}
			rs.close();
			getConnection().closeStatement(pstmt);
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR obtenint l'assignacio '"+sIdAssignacio+"'");
		}
		return oAssignacio;
	}
	
	/**
	 * Obté les assignacions que tenen com a identificador sIdMaterial_%
	 * @param sIdMaterial identificador del material
	 * @return Vector amb els identificadors de les assignacions
	 */
	public Vector getAssignacionsId(String sIdMaterial){
		return getAssignacions(sIdMaterial, true);
	}
	/**
	 * Obté les assignacions que tenen com a identificador sIdMaterial_%
	 * @param sIdMaterial identificador del material
	 * @return Vector d'Assignacio
	 */
	public Vector getAssignacions(String sIdMaterial){
		return getAssignacions(sIdMaterial, false);
	}
    
	protected Vector getAssignacions(String sIdMaterial, boolean bIds){
		Vector vAssignacions = new Vector();
		String query=null;
		try{
			query="SELECT * FROM assignacio WHERE id_assignacio like ?";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdMaterial+"_%");
			ResultSet rs=pstmt.executeQuery();
			while (rs.next()){
				String sId = rs.getString("id_assignacio");
				if (bIds){
					vAssignacions.addElement(sId);
				}else{
					Date dDataCreacio=rs.getDate("data_creacio");
					Date dDataFi=rs.getDate("data_fi");
					int iIdQuadern = rs.getInt("id_quadern");
					String sIP = rs.getString("ip");
					Quadern oQuadern = getQuadern(iIdQuadern);
					Assignacio oAssignacio=new Assignacio(sId, oQuadern, dDataCreacio, dDataFi, sIP, getFullsAssignacio(sId));
					vAssignacions.addElement(oAssignacio);
				}
			}
			rs.close();
			getConnection().closeStatement(pstmt);
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR obtenint les assignacions pel material '"+sIdMaterial+"'");
		}
		return vAssignacions;
	}
    
	/**
	 * Obte la puntuacio total de l'assignacio indicada
	 * @param sIdAssignacio
	 */
	public double getAssignmentPuntuation(String sAssignmentId) {
		double dPuntuation = 0;
		String query=null;
		try{
			query="SELECT darrera_puntuacio FROM full_assignacio WHERE id_assignacio=? and id_full=0";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sAssignmentId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				dPuntuation = rs.getDouble("darrera_puntuacio");
			}
			rs.close();
			getConnection().closeStatement(pstmt);
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR obtenint la puntuació de l'assignacio '"+sAssignmentId+"'");
		}
		return dPuntuation;
	}
    
	/**
	 * Obte el nombre de lliuraments màxim que es poden fer a l'assignacio indicada
	 * @param sIdAssignacio
	 */
	public int getMaxLliuraments(String sAssignmentId) {
		int iMaxLliuraments = 0;
		String query=null;
		try{
			query="SELECT max_lliuraments FROM quadern q, assignacio a WHERE a.id_quadern=q.id_quadern and a.id_assignacio=?";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sAssignmentId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				iMaxLliuraments = rs.getInt(1);
			}
			rs.close();
			getConnection().closeStatement(pstmt);
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR obtenint el nombre màxim de lliuraments per l'assignació '"+sAssignmentId+"'");
		}
		return iMaxLliuraments;
	}
    
	/**
	 * Obte el nombre de lliuraments total que s'han fet a l'assignacio indicada
	 * @param sIdAssignacio
	 */
	public int getTotalLliuramentsFets(String sAssignmentId) {
		int iTotalLliuraments = 0;
		String query=null;
		try{
			query="SELECT MAX(num_cops_lliurat) FROM full_assignacio WHERE id_assignacio=?";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sAssignmentId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				iTotalLliuraments = rs.getInt(1);
			}
			rs.close();
			getConnection().closeStatement(pstmt);
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR obtenint el nombre total de lliuraments fets per l'assignacio '"+sAssignmentId+"'");
		}
		return iTotalLliuraments;
	}
//	*******************	
//	* FULL QUADERN
//	*******************
	
	public boolean addFullAssignacio(String sIdAssignacio, FullAssignacio oFull){
		boolean bOk=false;
		String query = null;
		try{
			query="INSERT INTO full_assignacio ";
			query+="(id_assignacio, id_full, nom_full, estat_lliurament, ";
			query+="num_cops_lliurat, data_darrer_lliurament) ";
			query+="VALUES (?,?,?,?,?,?)";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			pstmt.setInt(2,oFull.getIdFull());
			pstmt.setString(3,oFull.getNomFull());
			pstmt.setString(4,oFull.getEstatLliurament());
			pstmt.setInt(5,oFull.getNumCopsLliurat());
			pstmt.setDate(6,(oFull.getDataDarrerLliurament()!=null?new java.sql.Date(oFull.getDataDarrerLliurament().getTime()):null));
			pstmt.executeUpdate();
			getConnection().closeStatement(pstmt);			
			bOk= true;
			if (oFull.getFeedbacks()!=null && oFull.getFeedbacks().size()>0){
				bOk = updateFeedbacks(sIdAssignacio, oFull.getIdFull(), oFull.getFeedbacks());
			}
			if (bOk && oFull.getPreguntes()!=null && oFull.getPreguntes().size()>0){
				bOk = addPreguntes(sIdAssignacio, oFull.getIdFull(), oFull.getPreguntes());
			}
			if (bOk && oFull.getRespostes()!=null && oFull.getRespostes().size()>0){
				bOk = addRespostes(sIdAssignacio, oFull.getIdFull(), oFull.getRespostes());
			}
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR afegint el full '"+oFull+"' a l'assignacio '"+sIdAssignacio+"'");
		}
		catch (Exception e){
			logger.error("ERROR afegint el full '"+oFull+"' a l'assignacio '"+sIdAssignacio+"' -->"+e);
			if (verbose) e.printStackTrace(System.out);
		}
		return bOk;
	}
    
	public boolean updateFullAssignacio(String sIdAssignacio, FullAssignacio oFull){
		boolean bOk = false;
		String query = null;
		try{
			// Actualitzar el full
			query="UPDATE full_assignacio ";
			query+="SET nom_full=?, estat_lliurament=?, num_cops_lliurat=?, ";
			query+="darrera_puntuacio=?, data_darrer_lliurament=? ";
			query+="WHERE id_assignacio=? AND id_full=?";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,oFull.getNomFull());
			pstmt.setString(2,oFull.getEstatLliurament());
			pstmt.setInt(3,oFull.getNumCopsLliurat());
			pstmt.setDouble(4,oFull.getDarreraPuntuacio());
			pstmt.setDate(5,(oFull.getDataDarrerLliurament()!=null)?new java.sql.Date(oFull.getDataDarrerLliurament().getTime()):null);
			pstmt.setString(6,sIdAssignacio);
			pstmt.setInt(7,oFull.getIdFull());
			bOk = pstmt.executeUpdate()>0;
			getConnection().closeStatement(pstmt);
			// Actualitzar els feedbacks, preguntes i respostes
			if (oFull.getFeedbacks()!=null && oFull.getFeedbacks().size()>0){
				bOk = updateFeedbacks(sIdAssignacio, oFull.getIdFull(), oFull.getFeedbacks());
			}
			if (bOk && oFull.getPreguntes()!=null && oFull.getPreguntes().size()>0){
				bOk = updatePreguntes(sIdAssignacio, oFull.getIdFull(), oFull.getPreguntes());
			}
			if (bOk && oFull.getRespostes()!=null && oFull.getRespostes().size()>0){
				bOk = updateRespostes(sIdAssignacio, oFull.getIdFull(), oFull.getRespostes());
			}
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR actualitzant el full '"+oFull+"' de l'assignacio '"+sIdAssignacio+"'");
		}
		catch (Exception e){
			logger.error("ERROR actualitzant el full '"+oFull+"' de l'assignacio '"+sIdAssignacio+"' -->"+e);
			if (verbose) e.printStackTrace(System.out);
		}
		return bOk;
	}
    
	public boolean deleteFullsAssignacio(String sIdAssignacio) {
		boolean bOk=true;
		String query=null;
		try{
			query="DELETE FROM full_assignacio ";
			query+="WHERE id_assignacio=?";
			PreparedStatement pstmt = getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			pstmt.executeUpdate();
			getConnection().closeStatement(pstmt);
			logger.debug("S'han esborrat correctament els fulls de l'assignacio '"+sIdAssignacio+"'");
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR esborrant els fulls de l'assignacio '"+sIdAssignacio+"'");
			bOk=false;
		}
		return bOk;
	}
    
	/**
	 * Obté tots els fulls d'una assignacio
	 * @param sIdAssignacio
	 * @return Vector de FullAssignacio
	 */
	public Vector getFullsAssignacio(String sIdAssignacio){
		Vector vFulls = new Vector();
		String query = null;
		try{
			query= "SELECT * ";
			query+="FROM full_assignacio ";
			query+="WHERE id_assignacio=? ";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			ResultSet rs=pstmt.executeQuery();
			while (rs.next()){
				int iIdFull = rs.getInt("id_full");
				String sNomFull = rs.getString("nom_full");
				String sEstatLliurament = rs.getString("estat_lliurament");
				int iNumCopsLliurat = rs.getInt("num_cops_lliurat");
				double dDarreraPuntuacio = rs.getDouble("darrera_puntuacio");
				Date dDataDarrerLliurament = rs.getDate("data_darrer_lliurament");
				FullAssignacio oFull = new FullAssignacio(iIdFull, sNomFull, sEstatLliurament, iNumCopsLliurat, dDataDarrerLliurament, dDarreraPuntuacio, getPreguntesFull(sIdAssignacio, iIdFull), getRespostesFull(sIdAssignacio, iIdFull), getFeedbacksFull(sIdAssignacio, iIdFull), getIntervencionsFull(sIdAssignacio, iIdFull));
				vFulls.addElement(oFull);
			}
			rs.close();
			getConnection().closeStatement(pstmt);
		}
		catch (SQLException e) {
			printSQLError(e, query,"ERROR obtenint els fulls de l'assignacio '"+sIdAssignacio+"'");
		}
		return vFulls;			
	}
    
	/**
	 * Obté el full de l'assignacio especificat
	 * @param sIdAssignacio
	 * @param iIdFull
	 * @return <code>FullAssignacio</code>
	 */
	public FullAssignacio getFullAssignacio(String sIdAssignacio, int iIdFull){
		FullAssignacio oFull = null;
		String query = null;
		try{
			query= "SELECT * ";
			query+="FROM full_assignacio ";
			query+="WHERE id_assignacio=? AND id_full=? ";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			pstmt.setInt(2, iIdFull);
			ResultSet rs=pstmt.executeQuery();
			if (rs.next()){
				String sNomFull = rs.getString("nom_full");
				String sEstatLliurament = rs.getString("estat_lliurament");
				int iNumCopsLliurat = rs.getInt("num_cops_lliurat");
				double dDarreraPuntuacio = rs.getDouble("darrera_puntuacio");
				Date dDataDarrerLliurament = rs.getDate("data_darrer_lliurament");
				oFull = new FullAssignacio(iIdFull, sNomFull, sEstatLliurament, iNumCopsLliurat, dDataDarrerLliurament, dDarreraPuntuacio, getPreguntesFull(sIdAssignacio, iIdFull), getRespostesFull(sIdAssignacio, iIdFull), getFeedbacksFull(sIdAssignacio, iIdFull), getIntervencionsFull(sIdAssignacio, iIdFull));
			}
			rs.close();
			getConnection().closeStatement(pstmt);
		}
		catch (SQLException e) {
			printSQLError(e, query,"ERROR obtenint els fulls de l'assignacio '"+sIdAssignacio+"'");
		}
		return oFull;			
	}
	    
//	*******************	
//	* INTERVENCIO
//	*******************
   
	public boolean addIntervencio(Intervencio oIntervencio){
		boolean bOk=false;
		String query = null;
		try{
			query="INSERT INTO intervencio(";
			query+="id_assignacio,id_full,";
			query+="id_pregunta, interventor, txt_intervencio)";
			query+="VALUES (?,?,?,?,?)";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,oIntervencio.getIdAssignacio());
			pstmt.setInt(2,oIntervencio.getIdFull());
			pstmt.setString(3,oIntervencio.getIdPregunta());
			pstmt.setString(4,oIntervencio.getInterventor());
			pstmt.setString(5,oIntervencio.getIntervencio());
			pstmt.executeUpdate();
			getConnection().closeStatement(pstmt);
			bOk=true;
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR afegint la intervencio '"+oIntervencio.getIntervencio()+"' a la pregunta '"+oIntervencio.getIdPregunta()+"' del full '"+String.valueOf(oIntervencio.getIdFull())+"' de l'assignacio '"+oIntervencio.getIdAssignacio()+"'");
		}
		catch (Exception e){
			logger.error("ERROR afegint la intervencio '"+oIntervencio.getIntervencio()+"' a la pregunta '"+oIntervencio.getIdPregunta()+"' del full '"+String.valueOf(oIntervencio.getIdFull())+"' de l'assignacio '"+oIntervencio.getIdAssignacio()+"' -->"+e);
			if (verbose) e.printStackTrace(System.out);
		}
		return bOk;
	}

	public boolean deleteIntervencionsAssignacio(String sIdAssignacio) {
		boolean bOk=true;
		String query=null;
		try{
			query="DELETE FROM intervencio ";
			query+="WHERE id_assignacio=?";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			pstmt.executeUpdate();
			getConnection().closeStatement(pstmt);
			logger.debug("S'han esborrat correctament les intervencions de l'assignacio '"+sIdAssignacio+"'");
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR esborrant les intervencions de l'assignacio '"+sIdAssignacio+"'");
			bOk=false;
		}
		return bOk;
	}
    
	/**
	 * Obté totes les intervencions del full indicat
	 * @param sIdAssignacio
	 * @param iNumFull
	 * @return
	 */
	public Vector getIntervencionsFull(String sIdAssignacio,int iNumFull){
		Vector vIntervencions = new Vector();
		String query = null;
		try{
			query="SELECT * FROM intervencio ";
			query+="WHERE id_assignacio=? AND id_full=? ";
			query+="ORDER BY data_creacio";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			pstmt.setInt(2,iNumFull);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				String sIdPregunta = rs.getString("id_pregunta");
				java.util.Date dDataCreacio = rs.getDate("data_creacio");
				String sInterventor = rs.getString("interventor");
				String sIntervencio = rs.getString("txt_intervencio");
				Intervencio oIntervencio = new Intervencio(sIdAssignacio, iNumFull, sIdPregunta, dDataCreacio, sInterventor, sIntervencio);
				vIntervencions.addElement(oIntervencio);
			}
			rs.close();
			getConnection().closeStatement(pstmt);
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR obtenint les intervencions del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"'");
		}
		catch (Exception e){
			logger.error("ERROR obtenint les intervencions del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"' -->"+e);
			if (verbose) e.printStackTrace(System.out);
		}		
		return vIntervencions;
	}

//	*******************	
//	* FEEDBACK
//	*******************
   
	public boolean addFeedback(String sIdAssignacio, int iNumFull, Feedback oFeedback){
		boolean bOk=false;
		String query = null;
		try{
			query="INSERT INTO feedback_pregunta(";
			query+="id_assignacio,id_full,";
			query+="id_pregunta, feedback)";
			query+="VALUES (?,?,?,?)";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			pstmt.setInt(2,iNumFull);
			pstmt.setString(3,oFeedback.getIdPregunta());
			pstmt.setString(4,oFeedback.getFeedback());
			pstmt.executeUpdate();
			getConnection().closeStatement(pstmt);
			bOk=true;
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR afegint el feedback '"+oFeedback.getFeedback()+"' a la pregunta '"+oFeedback.getIdPregunta()+"' del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"'");
		}
		catch (Exception e){
			logger.error("ERROR afegint el feedback '"+oFeedback.getFeedback()+"' a la pregunta '"+oFeedback.getIdPregunta()+"' del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"' -->"+e);
			if (verbose) e.printStackTrace(System.out);
		}
		return bOk;
	}
	
	public boolean deleteFeedbacksAssignacio(String sIdAssignacio){
		boolean bOk=false;
		String query = null;
		try{
			query="DELETE FROM feedback_pregunta ";
			query+="WHERE id_assignacio=? ";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			pstmt.executeUpdate();
			getConnection().closeStatement(pstmt);
			bOk=true;
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR esborrant feedbacks de l'assignacio '"+sIdAssignacio+"'");
		}
		catch (Exception e){
			logger.error("ERROR esborrant feedbacks de l'assignacio '"+sIdAssignacio+"' -->"+e);
			if (verbose) e.printStackTrace(System.out);
		}
		return bOk;
	}
	
	public boolean deleteFeedbacksFull(String sIdAssignacio, int iNumFull){
		boolean bOk=false;
		String query = null;
		try{
			query="DELETE FROM feedback_pregunta ";
			query+="WHERE id_assignacio=? AND id_full=? ";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			pstmt.setInt(2,iNumFull);
			pstmt.executeUpdate();
			getConnection().closeStatement(pstmt);
			bOk=true;
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR esborrant feedbacks del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"'");
		}
		catch (Exception e){
			logger.error("ERROR esborrant feedbacks del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"' -->"+e);
			if (verbose) e.printStackTrace(System.out);
		}
		return bOk;
	}
	
	public boolean existFeedback(String sIdAssignacio, int iNumFull, Feedback oFeedback){
		boolean bExist=false;
		String query = null;
		try{
			query="SELECT COUNT(*) FROM feedback_pregunta ";
			query+="WHERE id_assignacio=? AND id_full=? ";
			query+=" AND id_pregunta=? AND feedback=?";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			pstmt.setInt(2, iNumFull);
			pstmt.setString(3, oFeedback.getIdPregunta());
			pstmt.setString(4, oFeedback.getFeedback());
			ResultSet rs=pstmt.executeQuery();
			if (rs.next()){
				int i=rs.getInt(1);
				bExist=(i>0);
			}
			rs.close();
			getConnection().closeStatement(pstmt);
		}
		catch (SQLException e) {
			printSQLError(e, query,"ERROR comprovant si existeix el feedback '"+oFeedback!=null?oFeedback.getFeedback():""+"' de la pregunta '"+oFeedback!=null?oFeedback.getIdPregunta():""+"' del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"'");
			return false;
		}
		catch (Exception e){
			logger.error("ERROR comprovant si existeix el feedback '"+oFeedback!=null?oFeedback.getFeedback():""+"' de la pregunta '"+oFeedback!=null?oFeedback.getIdPregunta():""+"' del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"' -->"+e);
			if (verbose) e.printStackTrace(System.out);
		}
		return bExist;		
	}
	
	public boolean updateFeedbacks(String sIdAssignacio, int iNumFull, Vector vFeedbacks){
		boolean bOk=false;
		String query = null;
		try{
			deleteFeedbacksFull(sIdAssignacio, iNumFull);
			Enumeration enumFeedbacks = vFeedbacks.elements();
			while (enumFeedbacks.hasMoreElements()){
				Feedback oFeedback = (Feedback)enumFeedbacks.nextElement();
				addFeedback(sIdAssignacio, iNumFull, oFeedback);
			}
			bOk=true;
		}
		catch (Exception e){
			logger.error("ERROR actualitzant els feedbacks del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"' -->"+e);
			if (verbose) e.printStackTrace(System.out);
		}
		return bOk;
	}

	/**
	 * Obté tots els feedbacks del full indicat
	 * @param sIdAssignacio
	 * @param iNumFull
	 * @param sIdPregunta
	 * @return HashMap amb els parells (IdPregunta, Feedback)
	 */
	public HashMap getHashMapFeedbacksFull(String sIdAssignacio,int iNumFull){
		HashMap  hmFeedbacks = new HashMap();
		String query = null;
		try{
			query="SELECT * FROM feedback_pregunta ";
			query+="WHERE id_assignacio=? AND id_full=? ";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			pstmt.setInt(2,iNumFull);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				String sIdPregunta = rs.getString("id_pregunta");
				String sFeedback = rs.getString("feedback");
				hmFeedbacks.put(sIdPregunta, sFeedback);
			}
			rs.close();
			getConnection().closeStatement(pstmt);
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR obtenint els feedbacks del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"'");
		}
		catch (Exception e){
			logger.error("ERROR obtenint els feedbacks del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"' -->"+e);
			if (verbose) e.printStackTrace(System.out);
		}		
		return hmFeedbacks;
	}
	
	/**
	 * Obté tots els feedbacks del full indicat
	 * @param sIdAssignacio
	 * @param iNumFull
	 * @return Vector de <code>Feedback</code>
	 */
	public Vector getFeedbacksFull(String sIdAssignacio,int iNumFull){
		Vector  vFeedbacks = new Vector();
		String query = null;
		try{
			query="SELECT * FROM feedback_pregunta ";
			query+="WHERE id_assignacio=? AND id_full=? ";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			pstmt.setInt(2,iNumFull);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				String sIdPregunta = rs.getString("id_pregunta");
				String sFeedback = rs.getString("feedback");
				Feedback oFeedback = new Feedback(sIdPregunta, sFeedback);
				vFeedbacks.addElement(oFeedback);
			}
			rs.close();
			getConnection().closeStatement(pstmt);
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR obtenint els feedbacks del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"'");
		}
		catch (Exception e){
			logger.error("ERROR obtenint els feedbacks del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"' -->"+e);
			if (verbose) e.printStackTrace(System.out);
		}		
		return vFeedbacks;
	}
	
//	*******************	
//	* PREGUNTA
//	*******************
   
	public boolean addPreguntes(String sIdAssignacio, int iNumFull, Vector vPreguntes){
		boolean bOk = true;
		if (vPreguntes!=null){
			Enumeration enumPreguntes = vPreguntes.elements();
			while (enumPreguntes.hasMoreElements() && bOk){
				Pregunta oPregunta = (Pregunta)enumPreguntes.nextElement();
				bOk = addPregunta(sIdAssignacio, iNumFull, oPregunta);
			}
		}
		return bOk;
	}
	
	public boolean addPregunta(String sIdAssignacio, int iNumFull, Pregunta oPregunta){
		boolean bOk=false;
		String query = null;
		try{
			query="INSERT INTO pregunta(";
			query+="id_assignacio,id_full,";
			query+="id_pregunta, puntuacio, correcte)";
			query+="VALUES (?,?,?,?,?)";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			pstmt.setInt(2,iNumFull);
			pstmt.setString(3,oPregunta.getIdPregunta());
			pstmt.setDouble(4,oPregunta.getPuntuacio());
			pstmt.setBoolean(5,oPregunta.isCorrecte());
			pstmt.executeUpdate();
			getConnection().closeStatement(pstmt);
			bOk=true;
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR afegint la pregunta '"+oPregunta.getIdPregunta()+"' del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"'");
		}
		catch (Exception e){
			logger.error("ERROR afegint la pregunta '"+oPregunta.getIdPregunta()+"' del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"' -->"+e);
			if (verbose) e.printStackTrace(System.out);
		}
		return bOk;
	}
	
	public boolean updatePreguntes(String sIdAssignacio, int iNumFull, Vector vPreguntes){
		boolean bOk = true;
		Enumeration enumPreguntes = vPreguntes.elements();
		while (enumPreguntes.hasMoreElements() && bOk){
			Pregunta oPregunta = (Pregunta)enumPreguntes.nextElement();
			if (existPregunta(sIdAssignacio, iNumFull, oPregunta)){
				bOk = updatePregunta(sIdAssignacio, iNumFull, oPregunta);
			} else{
				bOk = addPregunta(sIdAssignacio, iNumFull, oPregunta);
			}
		}
		return bOk;
	}
	
	public boolean updatePregunta(String sIdAssignacio, int iNumFull, Pregunta oPregunta){
		boolean bOk=false;
		String query = null;
		try{
			query="UPDATE pregunta ";
			query+="SET puntuacio=?, correcte=? ";
			query+="WHERE id_assignacio=? AND id_full=? AND id_pregunta=?";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setDouble(1,oPregunta.getPuntuacio());
			pstmt.setBoolean(2,oPregunta.isCorrecte());
			pstmt.setString(3,sIdAssignacio);
			pstmt.setInt(4,iNumFull);
			pstmt.setString(5,oPregunta.getIdPregunta());
			pstmt.executeUpdate();
			getConnection().closeStatement(pstmt);
			bOk=true;
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR actualitzant puntuacio a la pregunta '"+oPregunta.getIdPregunta()+"' del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"'");
		}
		catch (Exception e){
			logger.error("ERROR actualitzant puntuacio a la pregunta '"+oPregunta.getIdPregunta()+"' del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"' -->"+e);
			if (verbose) e.printStackTrace(System.out);
		}
		return bOk;
	}
	
	public boolean deletePreguntesAssignacio(String sIdAssignacio){
		boolean bOk=false;
		String query = null;
		try{
			query="DELETE FROM pregunta ";
			query+="WHERE id_assignacio=? ";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			pstmt.executeUpdate();
			getConnection().closeStatement(pstmt);
			bOk=true;
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR esborrant les preguntes de l'assignacio '"+sIdAssignacio+"'");
		}
		catch (Exception e){
			logger.error("ERROR esborrant les preguntes de l'assignacio '"+sIdAssignacio+"' -->"+e);
			if (verbose) e.printStackTrace(System.out);
		}
		return bOk;
	}
	
	/**
	 * Obté totes les preguntes del full indicat
	 * @param sIdAssignacio
	 * @param iNumFull
	 * @return Vector de <code>Pregunta</code>
	 */
	public Vector getPreguntesFull(String sIdAssignacio,int iNumFull){
		Vector  vPreguntes = new Vector();
		String query = null;
		try{
			query="SELECT * FROM pregunta ";
			query+="WHERE id_assignacio=? AND id_full=? ";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			pstmt.setInt(2,iNumFull);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				String sIdPregunta = rs.getString("id_pregunta");
				double dPuntuacio = rs.getDouble("puntuacio");
				boolean bCorrecte = rs.getBoolean("correcte");
				Pregunta oPregunta = new Pregunta(sIdPregunta, dPuntuacio, bCorrecte);
				vPreguntes.addElement(oPregunta);
			}
			rs.close();
			getConnection().closeStatement(pstmt);
		}
		catch (SQLException e){
			printSQLError(e, query, "ERROR obtenint les preguntes del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"'");
		}
		catch (Exception e){
			logger.error("ERROR obtenint les preguntes del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"' -->"+e);
			if (verbose) e.printStackTrace(System.out);
		}		
		return vPreguntes;
	}
	
	public boolean existPregunta(String sIdAssignacio, int iNumFull, Pregunta oPregunta){
		boolean bExist=false;
		String query = null;
		try{
			query="SELECT COUNT(*) FROM pregunta ";
			query+="WHERE id_assignacio=? AND id_full=? ";
			query+=" AND id_pregunta=?";
			PreparedStatement pstmt=getConnection().getPreparedStatement(query);
			pstmt.setString(1,sIdAssignacio);
			pstmt.setInt(2, iNumFull);
			pstmt.setString(3, oPregunta.getIdPregunta());
			ResultSet rs=pstmt.executeQuery();
			if (rs.next()){
				int i=rs.getInt(1);
				bExist=(i>0);
			}
			rs.close();
			getConnection().closeStatement(pstmt);
		}
		catch (SQLException e) {
			printSQLError(e, query,"ERROR comprovant si existeix la pregunta '"+oPregunta.getIdPregunta()+"' del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"'");
			return false;
		}
		catch (Exception e){
			logger.error("ERROR comprovant si existeix la pregunta '"+oPregunta.getIdPregunta()+"' del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"' -->"+e);
			if (verbose) e.printStackTrace(System.out);
		}
		return bExist;		
	}

// *******************	
// * RESPOSTA
// *******************
   
   public boolean addRespostes(String sIdAssignacio, int iNumFull, Vector vRespostes){
	   boolean bOk = true;
	   if (vRespostes!=null){
		   Enumeration enumRespostes = vRespostes.elements();
		   while (enumRespostes.hasMoreElements() && bOk){
			   Resposta oResposta = (Resposta)enumRespostes.nextElement();
			   bOk = addResposta(sIdAssignacio, iNumFull, oResposta);
		   }
	   }
	   return bOk;
   }
	
   public boolean addResposta(String sIdAssignacio, int iNumFull, Resposta oResposta){
	   boolean bOk=false;
	   String query = null;
	   try{
		   query="INSERT INTO resposta(";
		   query+="id_assignacio,id_full,";
		   query+="id_pregunta, id_resposta, resposta)";
		   query+="VALUES (?,?,?,?,?)";
		   PreparedStatement pstmt=getConnection().getPreparedStatement(query);
		   pstmt.setString(1,sIdAssignacio);
		   pstmt.setInt(2,iNumFull);
		   pstmt.setString(3,oResposta.getIdPregunta());
		   pstmt.setString(4,oResposta.getIdResposta());
		   pstmt.setString(5,oResposta.getResposta());
		   pstmt.executeUpdate();
		   getConnection().closeStatement(pstmt);
		   bOk=true;
	   }
	   catch (SQLException e){
		   printSQLError(e, query, "ERROR afegint la resposta '"+oResposta.getIdResposta()+"' a la pregunta '"+oResposta.getIdPregunta()+"' del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"'");
	   }
	   catch (Exception e){
		   logger.error("ERROR afegint la resposta '"+oResposta.getIdResposta()+"' a la pregunta '"+oResposta.getIdPregunta()+"' del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"' -->"+e);
		   if (verbose) e.printStackTrace(System.out);
	   }
	   return bOk;
   }
	
   public boolean updateRespostes(String sIdAssignacio, int iNumFull, Vector vRespostes){
	   boolean bOk = true;
	   if (vRespostes!=null && vRespostes.size()>0){
		   bOk = deleteRespostesFull(sIdAssignacio, iNumFull);
		   if (bOk){
			   bOk = addRespostes(sIdAssignacio, iNumFull, vRespostes);
		   }
	   }
	   return bOk;
   }
	
   public boolean updateResposta(String sIdAssignacio, int iNumFull, Resposta oResposta){
	   boolean bOk=false;
	   String query = null;
	   try{
		   query="UPDATE resposta ";
		   query+="SET resposta=? ";
		   query+="WHERE id_assignacio=? AND id_full=? AND id_pregunta=? AND id_resposta=? ";
		   PreparedStatement pstmt=getConnection().getPreparedStatement(query);
		   pstmt.setString(1,oResposta.getResposta());
		   pstmt.setString(2,sIdAssignacio);
		   pstmt.setInt(3,iNumFull);
		   pstmt.setString(4,oResposta.getIdPregunta());
		   pstmt.setString(5,oResposta.getIdResposta());
		   pstmt.executeUpdate();
		   getConnection().closeStatement(pstmt);
		   bOk=true;
	   }
	   catch (SQLException e){
		   printSQLError(e, query, "ERROR actualitzant la resposta '"+oResposta.getIdResposta()+"' de la pregunta '"+oResposta.getIdPregunta()+"' del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"'");
	   }
	   catch (Exception e){
		   logger.error("ERROR actualitzant la resposta '"+oResposta.getIdResposta()+"' de la pregunta '"+oResposta.getIdPregunta()+"' del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"' -->"+e);
		   if (verbose) e.printStackTrace(System.out);
	   }
	   return bOk;
   }
	
   public boolean deleteRespostesAssignacio(String sIdAssignacio){
	   boolean bOk=false;
	   String query = null;
	   try{
		   query="DELETE FROM resposta ";
		   query+="WHERE id_assignacio=? ";
		   PreparedStatement pstmt=getConnection().getPreparedStatement(query);
		   pstmt.setString(1,sIdAssignacio);
		   pstmt.executeUpdate();
		   getConnection().closeStatement(pstmt);
		   bOk=true;
	   }
	   catch (SQLException e){
		   printSQLError(e, query, "ERROR esborrant respostes de l'assignacio '"+sIdAssignacio+"'");
	   }
	   catch (Exception e){
		   logger.error("ERROR esborrant respostes de l'assignacio '"+sIdAssignacio+"' -->"+e);
		   if (verbose) e.printStackTrace(System.out);
	   }
	   return bOk;
   }
	
   public boolean deleteRespostesFull(String sIdAssignacio, int iNumFull){
	   boolean bOk=false;
	   String query = null;
	   try{
		   query="DELETE FROM resposta ";
		   query+="WHERE id_assignacio=? AND id_full=? ";
		   PreparedStatement pstmt=getConnection().getPreparedStatement(query);
		   pstmt.setString(1,sIdAssignacio);
		   pstmt.setInt(2,iNumFull);
		   pstmt.executeUpdate();
		   getConnection().closeStatement(pstmt);
		   bOk=true;
	   }
	   catch (SQLException e){
		   printSQLError(e, query, "ERROR esborrant respostes del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"'");
	   }
	   catch (Exception e){
		   logger.error("ERROR esborrant respostes del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"' -->"+e);
		   if (verbose) e.printStackTrace(System.out);
	   }
	   return bOk;
   }
	
   /**
	* Obté totes les respostes del full indicat
	* @param sIdAssignacio
	* @param iNumFull
	* @return Vector de <code>Resposta</code>
	*/
   public Vector getRespostesFull(String sIdAssignacio,int iNumFull){
	   Vector  vRespostes = new Vector();
	   String query = null;
	   try{
		   query="SELECT * FROM resposta ";
		   query+="WHERE id_assignacio=? AND id_full=? ";
		   PreparedStatement pstmt=getConnection().getPreparedStatement(query);
		   pstmt.setString(1,sIdAssignacio);
		   pstmt.setInt(2,iNumFull);
		   ResultSet rs = pstmt.executeQuery();
		   while (rs.next()){
			   String sIdPregunta = rs.getString("id_pregunta");
			   String sIdResposta = rs.getString("id_resposta");
			   String sResposta = rs.getString("resposta");
			   Resposta oResposta = new Resposta(sIdPregunta, sIdResposta, sResposta);
			   vRespostes.addElement(oResposta);
		   }
		   rs.close();
		   getConnection().closeStatement(pstmt);
	   }
	   catch (SQLException e){
		   printSQLError(e, query, "ERROR obtenint les respostes del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"'");
	   }
	   catch (Exception e){
		   logger.error("ERROR obtenint les respostes del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"' -->"+e);
		   if (verbose) e.printStackTrace(System.out);
	   }		
	   return vRespostes;
   }
	
   public boolean existResposta(String sIdAssignacio, int iNumFull, Resposta oResposta){
	   boolean bExist=false;
	   String query = null;
	   try{
		   query="SELECT COUNT(*) FROM resposta ";
		   query+="WHERE id_assignacio=? AND id_full=? ";
		   query+=" AND id_pregunta=? AND id_resposta=? ";
		   PreparedStatement pstmt=getConnection().getPreparedStatement(query);
		   pstmt.setString(1,sIdAssignacio);
		   pstmt.setInt(2, iNumFull);
		   pstmt.setString(3, oResposta.getIdPregunta());
		   pstmt.setString(4, oResposta.getIdResposta());
		   ResultSet rs=pstmt.executeQuery();
		   if (rs.next()){
			   int i=rs.getInt(1);
			   bExist=(i>0);
		   }
		   rs.close();
		   getConnection().closeStatement(pstmt);
	   }
	   catch (SQLException e) {
		   printSQLError(e, query,"ERROR comprovant si existeix la resposta '"+oResposta.getIdResposta()+"' de la pregunta '"+oResposta.getIdPregunta()+"' del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"'");
		   return false;
	   }
	   catch (Exception e){
		   logger.error("ERROR comprovant si existeix la resposta '"+oResposta.getIdResposta()+"' de la pregunta '"+oResposta.getIdPregunta()+"' del full '"+iNumFull+"' de l'assignacio '"+sIdAssignacio+"' -->"+e);
		   if (verbose) e.printStackTrace(System.out);
	   }
	   return bExist;		
   }

// *******************	
// * ESTATS
// *******************
   
	 /**
	  * Comproba si el quadern especificat està a l'estat indicat
	  * @param sIdAssignacio quadern a comprobar si està en un estat determinat
	  * @param sEstat estat de lliurament del quadern que es vol comprobar
	  * @return true si el quadern es troba en l'estat indicat; false en cas contrari
	  */
	 public boolean isEstatQuadern(String sIdAssignacio, String sEstat){
		 boolean bIsEstat = false; 
		 Hashtable hEstats = getEstatsQuadern(sIdAssignacio);
		 if (hEstats != null && sEstat!= null && hEstats.get(sEstat)!=null){
			 Object oEstat = hEstats.get(sEstat);
			 bIsEstat = hEstats.get(sEstat)!=null?((Boolean)hEstats.get(sEstat)).booleanValue():false;
		 }
		 return bIsEstat;    	
	 }
    
	 /**
	  * Obte una hash amb les parelles (ESTAT, boolea). Cada parella indica si el quadern 
	  * es troba en l'estat indicat.
	  * @param sIdAssignacio identificador del quadern
	  * @return hashtable amb les parelles (ESTAT, boolea), que indiquen si el quadern es troba o no en cada estat
	  */
	 public Hashtable getEstatsQuadern(String sIdAssignacio){
		 Hashtable hEstats = new Hashtable();
		 Enumeration tmpEnum = getFullsAssignacio(sIdAssignacio).elements();
		 if (sIdAssignacio!=null && tmpEnum.hasMoreElements()){
			boolean bIsComencat = false;
			boolean bIsPendentRevisio = false;
			boolean bIsPendentModificacio = false;
			boolean bIsLliurat = false;
			boolean bIsTotsLliurats = true;
			boolean bIsCorregit = false;
			boolean bIsTotsCorregits = true;
			while (tmpEnum.hasMoreElements()){
				 FullAssignacio oFull=(FullAssignacio)tmpEnum.nextElement();
				 if (!oFull.isInicial()){
					 if ( oFull.getEstatLliurament() == null || oFull.getEstatLliurament().equals(QVServlet.NO_INICIAT) ){
						 bIsTotsLliurats = false;
						 bIsTotsCorregits = false;
					 } else if (oFull.getEstatLliurament().equals(QVServlet.INICIAT)){
						 bIsComencat = true;
					 }else if (oFull.getEstatLliurament().equals(QVServlet.INTERVENCIO_ALUMNE) || oFull.getEstatLliurament().equals("pendent_revisio")){
						 bIsPendentRevisio = true;
						 bIsComencat = true;
					 } else if (oFull.getEstatLliurament().equals(QVServlet.INTERVENCIO_DOCENT) || oFull.getEstatLliurament().equals("pendent_modificacio")){
						 bIsPendentModificacio = true;
						 bIsComencat = true;
					 }
					 if (oFull.isInState(QVServlet.LLIURAT)){
						 bIsLliurat = true;
						 bIsComencat = true;
					 }
					 if (bIsTotsLliurats && !oFull.isInState(QVServlet.LLIURAT)){
					   bIsTotsLliurats = false;
					 }
					 if (oFull.isInState(QVServlet.CORREGIT)){
						 bIsComencat = true;
						 bIsLliurat = true;  
						 bIsCorregit = true;
					 }
					 if (bIsTotsCorregits && !oFull.isInState(QVServlet.CORREGIT)){
					 	bIsTotsCorregits = false;
					 }
				 }
			 }
			
			 hEstats.put(QVServlet.NO_INICIAT, new Boolean(!bIsComencat)); 					// Tots els fulls estan o sense estat o no començats
			 hEstats.put(QVServlet.INICIAT, new Boolean(bIsComencat));						// No hi ha cap full  sense començar
			 hEstats.put(QVServlet.INTERVENCIO_ALUMNE, new Boolean(bIsPendentRevisio));		// Existeix algun quadern amb una intervencio feta per l'alumne
			 hEstats.put(QVServlet.INTERVENCIO_DOCENT, new Boolean(bIsPendentModificacio)); 	// Existeix algun quadern amb una intervencio del professor
			 hEstats.put(QVServlet.PARCIALMENT_LLIURAT, new Boolean(bIsLliurat || bIsTotsCorregits));	// Existeix algun full lliurat
			 hEstats.put(QVServlet.LLIURAT, new Boolean(bIsTotsLliurats));					// Tots els fulls del quadern estan lliurats
			 hEstats.put(QVServlet.PARCIALMENT_CORREGIT, new Boolean(bIsCorregit));			// Existeix algun full corregit
			 hEstats.put(QVServlet.CORREGIT, new Boolean(bIsTotsCorregits));					// Tots els fulls estan corregits			
		 } else {
			 hEstats.put(QVServlet.NO_INICIAT, Boolean.TRUE); 	
			 hEstats.put(QVServlet.INICIAT, Boolean.FALSE);
			 hEstats.put(QVServlet.INTERVENCIO_ALUMNE, Boolean.FALSE);
			 hEstats.put(QVServlet.INTERVENCIO_DOCENT, Boolean.FALSE);
			 hEstats.put(QVServlet.PARCIALMENT_LLIURAT, Boolean.FALSE);
			 hEstats.put(QVServlet.LLIURAT, Boolean.FALSE);
			 hEstats.put(QVServlet.PARCIALMENT_CORREGIT, Boolean.FALSE);
			 hEstats.put(QVServlet.CORREGIT, Boolean.FALSE);			
		 } 
		 return hEstats;
	 }
    
	 /**
	  * Obté l'estat del quadern indicat
	  * @param sIdAssignacio quadern del que es dessitja obtenir l'estat
	  * @return estat de llirament del quadern. Si es produeix algun error es retornarà null
	  */
	 public String getEstatQuadern(String sIdAssignacio){
		 String sEstat = null;
		 Hashtable hEstats = getEstatsQuadern(sIdAssignacio);
		 if (hEstats.get(QVServlet.NO_INICIAT)!=null && ((Boolean)hEstats.get(QVServlet.NO_INICIAT)).booleanValue()){
			 sEstat = QVServlet.NO_INICIAT;
		 } else if (hEstats.get(QVServlet.CORREGIT)!=null && ((Boolean)hEstats.get(QVServlet.CORREGIT)).booleanValue()){
			 sEstat = QVServlet.CORREGIT;
		 } else if (hEstats.get(QVServlet.INTERVENCIO_ALUMNE)!=null && ((Boolean)hEstats.get(QVServlet.INTERVENCIO_ALUMNE)).booleanValue()){
			 sEstat = QVServlet.INTERVENCIO_ALUMNE;
		 } else if (hEstats.get(QVServlet.INTERVENCIO_DOCENT)!=null && ((Boolean)hEstats.get(QVServlet.INTERVENCIO_DOCENT)).booleanValue()){
			 sEstat = QVServlet.INTERVENCIO_DOCENT;
		 } else if (hEstats.get(QVServlet.LLIURAT)!=null && ((Boolean)hEstats.get(QVServlet.LLIURAT)).booleanValue()){
			 sEstat = QVServlet.LLIURAT;
		 } else if (hEstats.get(QVServlet.PARCIALMENT_LLIURAT)!=null && ((Boolean)hEstats.get(QVServlet.PARCIALMENT_LLIURAT)).booleanValue()){
			 sEstat = QVServlet.PARCIALMENT_LLIURAT;
		 } else if (hEstats.get(QVServlet.PARCIALMENT_CORREGIT)!=null && ((Boolean)hEstats.get(QVServlet.PARCIALMENT_CORREGIT)).booleanValue()){
			 sEstat = QVServlet.PARCIALMENT_CORREGIT;
		 } else {
			 sEstat = QVServlet.INICIAT;
		 }
		 return sEstat;
	 }
    
//		************************************	
//		* Database connection
//		************************************
		
		protected ConnectionBeanProvider getConnectionBeanProvider(){
			try{
				if(broker == null) { // Only created by first servlet to call
					broker = ConnectionBeanProvider.getConnectionBeanProvider(true, Utility.loadProperties(PROPERTIES_PATH, DBCONF));				
				}
			}catch (Exception e){
				logger.error("EXCEPTION getting ConnectionBeanProvider-> "+e);
				e.printStackTrace();
			}
			return broker;
		}
		
		protected ConnectionBean getConnection(){
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
		
    
//	*******************	
//	* UTILS
//	*******************
   
	public void finalize(){
		this.c=null;
	}
        
	private void printSQLError(SQLException e, String sQuery, String sMessage){
		logger.error(sMessage);
		if (sQuery!=null){
			logger.error("Query:        "+sQuery);
		}
		if (e!=null){
			logger.error("SQLState:     "+e.getSQLState());
			logger.error("VendorError:  "+e.getErrorCode());
			logger.error("SQLException: "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public boolean checkConnection(){
		boolean bOK = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
		   StringBuffer sbQuery= new StringBuffer("SELECT sysdate FROM dual ");
		   pstmt = getConnection().getPreparedStatement(sbQuery.toString());
		   rs = pstmt.executeQuery();
		   if (rs.next()){
			   bOK = true;
		   }
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			try{
				if (rs!=null) rs.close();
				getConnection().closeStatement(pstmt);
				getConnection().closeConnection();
			}catch (Exception e){}
		}
		return bOK;
	}
}