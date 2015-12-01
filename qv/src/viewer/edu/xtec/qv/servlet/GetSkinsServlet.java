/*
 * GetSkinsServlet.java
 * 
 * Created on 04/novembre/2004
 */
package edu.xtec.qv.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.xtec.qv.servlet.util.Skin;
import edu.xtec.qv.util.QuadernConfig;

/**
 * @author sarjona
 */
public class GetSkinsServlet extends QVServlet {

	protected static final String SEPARATOR = ";";
	protected static String sAllSkins;
	protected static Properties properties;
	protected static Hashtable hUserProperties = new Hashtable();
	protected final static String EDITORQV_CONFIG_FILE = "editorQV.properties";
	
	/**
	 * Parametres
	 */
	public static final String P_LANG = "lang";
	public static final String P_ALL_SKINS = "all";
	public static final String P_USERNAME = "username";

	/**
	 * Properties
	 */
	public static final String K_XSL_NAMES= "xsl.names";
	public static final String K_XSL_PUBLIC = "xsl.public";
	
	public static final String EDITORQV_QVSTORE_LOCAL_URL_KEY = "store.localURL";
	public static final String EDITORQV_PUBLIC_SKINS_KEY = "setting.skin.public";


	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		boolean bAll = (request.getParameter(P_ALL_SKINS)!=null);
		Vector vIdSkins = new Vector();
		if (request.getParameter(P_USERNAME)!=null){
			String sUserSkins = getProperty(request.getParameter(P_USERNAME),EDITORQV_PUBLIC_SKINS_KEY);
			if (sUserSkins!=null && sUserSkins.trim().length()>0){
				StringTokenizer st = new StringTokenizer(sUserSkins, ",");
				while (st.hasMoreTokens()){
					vIdSkins.addElement(st.nextToken());
				}
			}
		}
		
		String sLang = request.getParameter(P_LANG);
		if (sLang==null || sLang.trim().length()==0) sLang = "ca";
		Vector vSkins = getQVDataManage().getSkins(vIdSkins, sLang, bAll);
		Enumeration enumSkins = vSkins.elements();
		StringBuffer sb = new StringBuffer();
		while (enumSkins.hasMoreElements()){
			Skin oSkin = (Skin)enumSkins.nextElement();
			sb.append(oSkin.getId());
			sb.append(";");
			sb.append(oSkin.getText(sLang));
			sb.append(";");
		}
		out.println(sb.toString());
		
/*		
		if (request.getParameter(P_ALL_SKINS)!=null){
			sSkins = getAllSkins();			
		}else if (request.getParameter(P_USERNAME)!=null){
			String sUserSkins = getProperty(request.getParameter(P_USERNAME),EDITORQV_PUBLIC_SKINS_KEY);
			StringTokenizer st = new StringTokenizer(sUserSkins, ",");
			while (st.hasMoreTokens()){
				String sId = st.nextToken();
				String sName = (String)QuadernConfig.getXSLNames().get(sId);
				sSkins += sId+";"+sName+";";
			}
		}
		if (sSkins==null || sSkins.trim().length()==0){
			sSkins = getPublicSkins();			
		}
		out.println(sSkins);
*/		
	}

	
	
	protected String getPublicSkins(){
		String sSkins = "";
		String sPublic = QuadernConfig.getProperty(K_XSL_PUBLIC);
		StringTokenizer st = new StringTokenizer(sPublic, SEPARATOR);
		while (st.hasMoreTokens()){
			String sSkin = st.nextToken();
			sSkins+=sSkin+SEPARATOR+getSkinName(sSkin)+SEPARATOR;
		}
		return sSkins;
	}
	
	protected String getSkinName(String sSkin){
		String sName = "";
		StringTokenizer st = new StringTokenizer(getAllSkins(), SEPARATOR);
		while (st.hasMoreTokens()){
			if (st.nextToken().equals(sSkin)){
				sName=st.nextToken();
				break;
			}
		}
		return sName;
	}
	
	protected String getAllSkins(){
		if (sAllSkins==null){
			sAllSkins = QuadernConfig.getProperty(K_XSL_NAMES);
		}
		return sAllSkins;
	}
	

	protected Properties getProperties(){
		if (properties==null){
			properties = new Properties();
			try{
				File f = new File(System.getProperty("user.home"), EDITORQV_CONFIG_FILE);
				if(f.exists()){
					FileInputStream is=new FileInputStream(f);
					properties.load(is);
					is.close();
				}

			} catch (Exception e){
				logger.error("ERROR obtenint properties de l'editor de QV des de getAparences-> "+e);
	        }
		}
		return properties;
	}
	
	protected Properties getProperties(String sUsername){
		Properties userProperties = new Properties();
		try{
			if (sUsername!=null){
				if (hUserProperties.containsKey(sUsername)){
					userProperties = (Properties)hUserProperties.get(sUsername);
				}else{
					String sPath = getProperty(EDITORQV_QVSTORE_LOCAL_URL_KEY)+sUsername;
					if (sPath!=null){
						if (!sPath.endsWith("/") && !sPath.endsWith("\"")){
							sPath += "/";
						}
						File f = new File(sPath+EDITORQV_CONFIG_FILE);
						//userProperties = (Properties)getProperties().clone();
						if(f.exists()){
							FileInputStream is=new FileInputStream(f);
							userProperties.load(is);
							is.close();
						}
						hUserProperties.put(sUsername, userProperties);
					}					
				}
			}
		} catch(Exception e){
			logger.error("EXCEPCIO obtenint les properties de l'usuari '"+sUsername+"' -> e="+e);
		}
		return userProperties;
		
	}
	
	protected String getProperty(String sKey){
		String sValue = "";
		if (sKey!=null){
			sValue = getProperties().getProperty(sKey);
		}
		return sValue;
	}
	
	protected String getProperty(String sUsername, String sKey){
		String sValue = "";
		if (sKey!=null){
			sValue = getProperties(sUsername).getProperty(sKey);
		}
		return sValue;
	}
}
