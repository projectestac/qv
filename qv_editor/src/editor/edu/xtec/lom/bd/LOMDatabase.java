package edu.xtec.lom.bd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

import edu.xtec.lom.Area;
import edu.xtec.lom.Idioma;
import edu.xtec.lom.Nivell;
import edu.xtec.qv.util.XMLUtil;
import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;

public class LOMDatabase {

	public final static String PROPERTIES_PATH = "/edu/xtec/resources/properties/";
	public final static String DBCONF_FILE = "connectionQV.properties";

	private static ConnectionBeanProvider broker;
	private ConnectionBean c;
	private static Properties pDB;
	
	private static final Logger logger = Logger.getRootLogger();
	
	private static Vector vArees;
	private static Hashtable hIdiomes = new Hashtable();
	private static Vector vNivells;
	private static Hashtable hMateries = new Hashtable();
	
	private boolean bOffline = false;
	private String sXMLPath;
    
	public LOMDatabase() {
		this(false, null);
	}
	public LOMDatabase(boolean bOffline, String sXMLPath) {
		this.bOffline=bOffline;
		this.sXMLPath = sXMLPath;
	}
	
	/**
	 * @param sPath path del fitxer XML a obrir
	 * @return representacio del document XML que conté el fitxer indicat 
	 * @throws Exception
	 */
	public Document getXMLData(){
		Document d = null;
		try{
			if (sXMLPath!=null){
				String sEncoding = "ISO-8859-15";
				Reader reader = null;
				FileInputStream fis = null;
				if (sXMLPath.startsWith("http")){
					URL oURL = new URL(sXMLPath);
					reader = new BufferedReader(new InputStreamReader(oURL.openStream(), sEncoding));
				}else{
					fis = new FileInputStream(sXMLPath);
					reader = new InputStreamReader(fis, sEncoding);
				}
				d = XMLUtil.getXMLDocument(reader);
				if (fis!=null) fis.close();
				reader.close();
				//d = getXMLDocument(new FileInputStream(sPath));
			}
		} catch (Exception e){
			logger.error("EXCEPCIO obrint fitxer XML '"+sXMLPath+"' --> "+e);
			e.printStackTrace();
		}
		return d;
	}
    
	
	
	protected Vector getLanguagesFromXML(String sLang){
		Vector vLangs = new Vector();
		Document dXML = getXMLData();
		if (dXML!=null){
			Element eLangs = dXML.getRootElement().getChild("languages");
			if (eLangs!=null){
				Iterator itLang = eLangs.getChildren().iterator();
				while (itLang.hasNext()){
					Element eLang = (Element)itLang.next();
					String sId = eLang.getAttributeValue("id");
					Iterator itDic = eLang.getChildren().iterator();
					while (itDic.hasNext()){
						Element eDic = (Element)itDic.next();
						String sLanguage = eDic.getAttributeValue("language");
						String sValue = eDic.getText();
						if (sLang.equalsIgnoreCase(sLanguage)) vLangs.add(new Idioma(sId, sLanguage, sValue));
					}
				}
				
			}
		}
		return vLangs;
	}

	protected Vector getAreasFromXML(){
		Vector vAreas = new Vector();
		Document dXML = getXMLData();
		if (dXML!=null){
			Element eAreas = dXML.getRootElement().getChild("areas");
			if (eAreas!=null){
				Iterator itArea = eAreas.getChildren().iterator();
				while (itArea.hasNext()){
					Element eArea = (Element)itArea.next();
					int iId = Integer.parseInt(eArea.getAttributeValue("id"));
					String sCode = eArea.getAttributeValue("code");

					Area oArea = new Area(iId, sCode);
					Hashtable hDic = new Hashtable();
					Iterator itLangs = eArea.getChildren().iterator();
					while (itLangs.hasNext()){
						Element eLang = (Element)itLangs.next();
						String sLang = eLang.getAttributeValue("language");
						String sValue = eLang.getText();
						//logger.debug("getAreasFromXML-> "+iId+"="+sValue);
						hDic.put(sLang, sValue);
					}
					oArea.setText(hDic);						
					vAreas.addElement(oArea);
				}
			}
		}
		return vAreas;
	}

	protected Vector getLevelsFromXML(){
		Vector vLevels = new Vector();
		Document dXML = getXMLData();
		if (dXML!=null){
			Element eLevels = dXML.getRootElement().getChild("levels");
			if (eLevels!=null){
				Iterator itLevel = eLevels.getChildren().iterator();
				while (itLevel.hasNext()){
					Element eLevel = (Element)itLevel.next();
					int iId = Integer.parseInt(eLevel.getAttributeValue("id"));
					int iMinAge = Integer.parseInt(eLevel.getAttributeValue("minAge"));
					int iMaxAge = Integer.parseInt(eLevel.getAttributeValue("minAge"));
					String sContext = eLevel.getAttributeValue("context");

					Nivell oLevel = new Nivell(iId, iMinAge, iMaxAge, sContext);
					Hashtable hDic = new Hashtable();
					Iterator itLangs = eLevel.getChildren().iterator();
					while (itLangs.hasNext()){
						Element eLang = (Element)itLangs.next();
						String sLang = eLang.getAttributeValue("language");
						String sValue = eLang.getText();
						hDic.put(sLang, sValue);
					}
					oLevel.setText(hDic);
					vLevels.addElement(oLevel);		
				}
			}
		}
		return vLevels;
	}

    /**
     * Obté els idiomes
     * @param sLang llengua en la que es retornaran els idiomes
     * @return Vector d'objectes Idioma
     */
	public Vector getIdiomes(String sLang){
		Vector vIdiomes = new Vector();
		if (sLang!=null){
			if (hIdiomes.containsKey(sLang)){
				vIdiomes = (Vector)hIdiomes.get(sLang);
			}else{
				if (bOffline){
					vIdiomes=getLanguagesFromXML(sLang);
					hIdiomes.put(sLang, vIdiomes);
				}else{
					StringBuffer query = new StringBuffer();
					try{
						query.append("SELECT i.id, d.text ");
						query.append("FROM idiomes i, diccionari d ");
						query.append("WHERE i.codi_dic=d.codi and d.idioma=? ");
						query.append("ORDER BY i.ordre desc, d.text asc");
						PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
						pstmt.setString(1, sLang);
						ResultSet rs=pstmt.executeQuery();
						while (rs.next()){
							String sId = rs.getString("id");
							String sText = rs.getString("text");
							Idioma oIdioma = new Idioma(sId, sLang, sText);
							vIdiomes.addElement(oIdioma);
						}
						hIdiomes.put(sLang, vIdiomes);
					}
					catch (SQLException e) {
						logger.error("EXCEPCIO obtenint les arees-> "+e);
					} finally{
						freeConnection();				
					}
				}
			}
		}
		return vIdiomes;
	}

    /**
     * Obté les àrees de la base de dades del Merlí
     * @return Vector d'objectes Area
     */
	public Vector getArees(){
		if (vArees==null || vArees.isEmpty()){
			vArees = new Vector();
			if (bOffline){
				vArees=getAreasFromXML();
				
/*				Area oArea = new Area(1, "area_lleng");
				Hashtable hDic = new Hashtable();
				hDic.put("ca","Llengües");
				hDic.put("es","Lenguas");
				oArea.setText(hDic);
				vArees.addElement(oArea);
				oArea = new Area(2, "area_mat");
				hDic = new Hashtable();
				hDic.put("ca","Matemàtiques");
				hDic.put("es","Matemáticas");
				oArea.setText(hDic);
				vArees.addElement(oArea);*/
			}else{
				StringBuffer query = new StringBuffer();
				try{
					query.append("SELECT a.*, p.paraula ");
					query.append("FROM area a, cerca_mud.par_clau_cat p ");
					query.append("WHERE a.id_par_clau=p.id_paraula ");
					PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
					ResultSet rs=pstmt.executeQuery();
					while (rs.next()){
						int iId = rs.getInt("id_area");
						String sKeyword = rs.getString("paraula");
						Area oArea = new Area(iId, sKeyword);
						String sCodiDic = rs.getString("codi_dic");
						oArea.setText(getTextDiccionari(sCodiDic, false));
						vArees.addElement(oArea);
					}
				}
				catch (SQLException e) {
					logger.error("EXCEPCIO obtenint les arees-> "+e);
				} finally{
					freeConnection();				
				}
			}
		}
		return vArees;
	}

    /**
     * Obté el nivell educatiu amb l'identificador especificat
     * @param iId identificador del nivell
     * @return Nivell amb l'identificador especificat
     */
	public Nivell getNivell(int iId){
		Nivell oNivell = null;
		Enumeration enumNivells = getNivells().elements();
		while (enumNivells.hasMoreElements()){
			Nivell tmpNivell = (Nivell)enumNivells.nextElement();
			if (tmpNivell.getId()==iId){
				oNivell = tmpNivell;
				break;
			}
		}
		return oNivell;
	}
	
    /**
     * Obté els nivells educatius
     * @return Vector d'objectes Nivell
     */
	public Vector getNivells(){
		if (vNivells==null){
			vNivells = new Vector();
			if (bOffline){
				vNivells=getLevelsFromXML();
				/*int iId = 1;
				int iEdatMin = 0;
				int iEdatMax = 3;
				String sContext = "1";
				Nivell oNivell = new Nivell(iId, iEdatMin, iEdatMax, sContext);
				Hashtable hDic = new Hashtable();
				hDic.put("ca","Educació preescolar");
				hDic.put("es","Educación preescolar");
				oNivell.setText(hDic);
				vNivells.addElement(oNivell);*/		
			}else{
				StringBuffer query = new StringBuffer();
				try{
					query.append("SELECT n.id_nivell, n.codi_dic, n.edat_min, n.edat_max, c.context ");
					query.append("FROM nivell  n, context c ");
					query.append("WHERE c.id_context=n.id_context ");
					PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
					ResultSet rs=pstmt.executeQuery();
					while (rs.next()){
						int iId = rs.getInt("id_nivell");
						int iEdatMin = rs.getInt("edat_min");
						int iEdatMax = rs.getInt("edat_max");
						String sContext = rs.getString("context");
						Nivell oNivell = new Nivell(iId, iEdatMin, iEdatMax, sContext);
						String sCodiDic = rs.getString("codi_dic");
						oNivell.setText(getTextDiccionari(sCodiDic, false));
						vNivells.addElement(oNivell);
					}
				}
				catch (SQLException e) {
					logger.error("EXCEPCIO obtenint els nivells -> "+e);
				} finally{
					freeConnection();				
				}
			}
		}
		return vNivells;
	}

    /**
     * Obté els texts de tots els idiomes pel codi de diccionari indicat
     * @param sCodiDic codi del diccionari
     * @return Hashtable amb els parells (idioma, text)
     */
	public Hashtable getTextDiccionari(String sCodiDic){
		return getTextDiccionari(sCodiDic, true);
	}
	
    /**
     * Obté els texts de tots els idiomes pel codi de diccionari indicat
     * @param sCodiDic codi del diccionari
     * @param bFreeConn true si s'ha d'alliberar la connexió; false en cas contrari
     * @return Hashtable amb els parells (idioma, text)
     */
	public Hashtable getTextDiccionari(String sCodiDic, boolean bFreeConn){
		Hashtable hText = new Hashtable();
		StringBuffer query = new StringBuffer();
		try{
			query.append("SELECT d.idioma, d.text ");
			query.append("FROM diccionari d ");
			query.append("WHERE d.codi=? ");
			PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
			pstmt.setString(1, sCodiDic);
			ResultSet rs=pstmt.executeQuery();
			while (rs.next()){
				String sIdioma = rs.getString("idioma");
				String sText = rs.getString("text");
				hText.put(sIdioma, sText);
			}
		}
		catch (SQLException e) {
			logger.error("EXCEPCIO obtenint els texts del diccionari pel codi '"+sCodiDic+"' -> "+e);
		} finally{
			if (bFreeConn) freeConnection();				
		}
		return hText;
	}

//	************************************	
//	* Database
//	************************************
	
	protected ConnectionBeanProvider getConnectionBeanProvider(){
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

	protected void freeConnection() {
		if (c!=null && broker!=null) {
			broker.freeConnectionBean(c);
			c=null;
		}
	}
	
//	************************************	
//	* Properties
//	************************************
	
	protected static Properties getDBProperties() throws Exception{
		if (pDB==null){
			pDB = new Properties();
			try{
				InputStream isl = LOMDatabase.class.getResourceAsStream(PROPERTIES_PATH+DBCONF_FILE);
				if (isl!=null){
					pDB.load(isl);
				}
				isl.close();
				File f = new File(System.getProperty("user.home"), DBCONF_FILE);
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
		}
		return pDB;
	}
	
}
