package edu.xtec.qv.biblio.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import edu.xtec.lom.bd.LOMDatabase;
import edu.xtec.qv.biblio.db.BiblioDatabase;
import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;

public abstract class QVBiblioBean {

	public final static String PROPERTIES_PATH = "/edu/xtec/resources/properties/";
	public final static String DBCONF_FILE = "qv_biblio.properties";
	public static String P_LANG = "lang";
	protected String sLang;


	protected static Logger logger = Logger.getRootLogger();
	private static Properties pConfig;

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected static HashMap localeObjects=new HashMap();
	protected static ResourceBundle bundle;
	protected DateFormat shortDateFormat;

	protected BiblioDatabase oBiblioDB;
	protected LOMDatabase oLOMDB;
	
	
//	***************************	
//	* Initialization
//	***************************
	public boolean init(HttpServletRequest request, HttpServletResponse response){
		this.request=request;
		this.response=response;
		return start();
	}
	
	protected abstract boolean start();
	

//	***************************	
//	* Parameter
//	***************************
	/**
	 * @param sParam parameter name
	 * @return parameter with sParam name or null if it doesn't exist
	 */
	public String getParameter(String sParam){
		String sValue = null;
		try{
			sValue = request.getParameter(sParam);
			if (sValue!=null && sValue.trim().length()<=0){
				sValue=null;
			}
			
		}catch (Exception e){
			logger.debug("EXCEPTION getting '"+sParam+"' parameter -->"+e);
		}
		return sValue;
	}

	/**
	 * @param sParam parameter name
	 * @param iDefault default value if specified parameter name doesn't exist.
	 * @return parameter value or sDefault if it doesn't exist
	 * @return
	 */
	public String getParameter(String sParam, String sDefault){
		String sResult = sDefault;
		try{
			sResult = getParameter(sParam);
			if (sResult==null || sResult.trim().length()<=0 || sResult.equalsIgnoreCase("null")){
				sResult = sDefault;
			}
		}
		catch (Exception e){
			logger.debug("EXCEPTION getting '"+sParam+"' parameter -->"+e);
		}
		return sResult;
	}
	
	/**
	 * @param sParam parameter name
	 * @return integer parameter value or -1 if it doesn't exist
	 */
	public int getIntParameter(String sParam){
		return getIntParameter(sParam, -1);
	}
	/**
	 * @param sParam parameter name
	 * @param iDefault default integer if specified parameter name doesn't exist.
	 * @return integer parameter value or iDefault if it doesn't exist
	 */
	public int getIntParameter(String sParam, int iDefault){
		int iResult = iDefault;
		try{
			String s = getParameter(sParam);
			if (s!=null && s.trim().length()>0)
				iResult=Integer.parseInt(s);
		}
		catch (Exception e){
			logger.debug("EXCEPCIO obtenint el parametre '"+sParam+"' -->"+e);
		}
		return iResult;
	}

//	***************************	
//	* Database
//	***************************
	protected BiblioDatabase getBiblioDatabase(){
		if (oBiblioDB==null){
			oBiblioDB = new BiblioDatabase();
		}
		return oBiblioDB;
	}
	
	public LOMDatabase getLOMDatabase(){
		if (oLOMDB==null){
			oLOMDB = new LOMDatabase(true, getProperty("store.metadata.data"));
		}
		return oLOMDB;
	}
	
//	***************************	
//	* Messages
//	***************************
	protected ResourceBundle getBundle() throws Exception{
		String lang = getLanguage();
		Object[] obj=(Object[])localeObjects.get(lang);
		if(obj!=null){
			bundle=(ResourceBundle)obj[0];
			shortDateFormat=(DateFormat)obj[1];
		}
		else{
			Locale locale=new Locale(lang, lang.toUpperCase());
			Locale.setDefault(locale);
			String sBaseNameBundle = "edu.xtec.resources.messages.qv_biblio_messages";
			bundle=ResourceBundle.getBundle(sBaseNameBundle, locale);
			if(bundle==null){
				logger.fatal("INTERNAL ERROR: No s'ha pogut inicialitzar l'idioma");
				throw new Exception("Internal error");
			}
			shortDateFormat=DateFormat.getDateInstance(DateFormat.SHORT, locale);
			obj=new Object[]{bundle, shortDateFormat};
			localeObjects.put(lang, obj);
		}
        return bundle;
	}

	public String getMsg(String sKey){
		String sMessage = "";
		try{
			sMessage = getBundle().getString(sKey);
		} catch (Exception e){
			logger.debug("EXCEPCIO: No s'ha trobat el missatge per l'entrada '"+sKey+"' --> "+e);
			sMessage = sKey;
		}
		return sMessage;
	}
	
	public String getMsg(String sKey, Object values){
		String sMessage = "";
		try{
			sMessage = bundle.getString(sKey);
			if (values!=null){
				if (values instanceof Vector){
					Vector vValues = (Vector)values;
					for (int i=0;i<vValues.size();i++){
						sMessage = replace(sMessage, "{"+i+"}", (String)vValues.elementAt(i));
					}
				} else{
					sMessage = replace(sMessage, "{1}", (String)values);					
				}
			}else{
				sMessage=getMsg(sKey);
			}
		} catch (Exception e){
			logger.debug("EXCEPCIO: No s'ha trobat el missatge per l'entrada '"+sKey+"' --> "+e);
			sMessage = sKey;
		}
		return sMessage;
	}

//	***************************	
//	* Properties
//	***************************	
	public static String getProperty(String sKey){
		return getProperties().getProperty(sKey);
	}

	public static Properties getProperties(){
		if (pConfig==null){
			pConfig = new Properties();
			try{
				pConfig.load(BiblioDatabase.class.getResourceAsStream(PROPERTIES_PATH+DBCONF_FILE));
				File f = new File(System.getProperty("user.home"), DBCONF_FILE);
				if(f.exists()){
					FileInputStream is=new FileInputStream(f);
					pConfig.load(is);
					is.close();
				}
			} catch (FileNotFoundException f) {
				logger.error(f);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return pConfig;
	}	
	
	
//	***************************	
//	* Utils
//	***************************
	/**
	 * @param d date to format
	 * @return formated date to string
	 */
	public String formatDate(Date dDate){
		String sDate = null;
		if (dDate!=null){
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        sDate = sdf.format(dDate);
		}
		return sDate;
	}

	public String getLanguage(){
		if (sLang==null){
			sLang = getParameter(P_LANG);
		}
		if (sLang==null){
			sLang = (String)request.getSession(true).getAttribute(P_LANG);
			if (sLang==null) sLang = "ca";
		}else{
			request.getSession(true).setAttribute(P_LANG, sLang);
		}
		return sLang;
	}
	
	public static String replace(String str, String pattern, String replace) {
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();
        
		if (str!=null){
			while ((e = str.indexOf(pattern, s)) >= 0) {
				result.append(str.substring(s, e));
				if(replace!=null)
					result.append(replace);
				s = e+pattern.length();
			}
			result.append(str.substring(s));
		}
		return result.toString();
	}

	
}
