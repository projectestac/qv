/**
 *  QVGenerateHTMLServlet.java
 *
 * Created on 24 / 05 / 2007
 */

package edu.xtec.qv.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

import edu.xtec.qv.servlet.zip.Constants;
import edu.xtec.qv.servlet.zip.QTITransformer;

/**
 * @author  sarjona
 * @version
 */
public class QVGenerateHTMLServlet extends HttpServlet {
    
	protected static Logger logger = Logger.getRootLogger();
	private static Properties pSettings;
	
	public final static String PROPERTIES_PATH = "/edu/xtec/resources/properties/";
	public final static String DBCONF_FILE = "qv_viewer.properties";
	public static final String QVSTORE_LOCAL_URL_KEY = "store.localURL";
	public static final String QVSTORE_REMOTE_URL_KEY="store.remoteURL";
	public static final String QVSTORE_REPOSITORY_PATH_KEY = "store.repositoryPath";
	
	public static String P_XML = "xml";
	public static String P_BASE = "base";
	public static String P_BIBLIO = "biblio";
	public static String P_LANG = "lang";
	public static String P_SKIN = "skin";
	
	public static String P_USER = "user";
	public static String P_ASSESSMENT = "assessment";
	public static String P_SECTION = "section";
	
	/** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }	

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {	
		try{
			String sUser=null;
			String sAssessment = null;
			String sURL = null;
			HashMap oMap = new HashMap();
			
			String sXML = getParameter(request, P_XML);
			String sBase = getParameter(request, P_BASE);
			String sLang = getParameter(request, P_LANG);
			String sSkin = getParameter(request, P_SKIN);
			int iSection = getIntParameter(request, P_SECTION, -1);
			
			if (sXML==null || sBase==null){
				if (sXML!=null){
					sBase = getLocalBase(sXML);
				}else{						
					sUser = getParameter(request, P_USER);
					sAssessment = getParameter(request, P_ASSESSMENT);
					sBase = getLocalBase(sUser, sAssessment);
					sXML = sBase+sAssessment+".xml";
					oMap.put("teacher", sUser);
					oMap.put("assessment", sAssessment);
				}
				sURL = getRemoteBase(sUser, sAssessment)+"html/"+getSectionNameFile(iSection);
				sBase+="html/";
				writeHTML(sXML, sBase, sSkin, iSection, oMap);
			}else{
				String sLocalXML = null;
				if (getParameter(request, P_BIBLIO)!=null){
					sLocalXML = getProperty("qv.biblio.localURL")+sXML;
					sXML = getProperty("qv.biblio.remoteURL")+sXML;
					sBase = getProperty("qv.biblio.localURL")+sBase;
				} else { //ALLR
					sLocalXML = sXML;
				}
				sURL=sXML.substring(0,sXML.lastIndexOf("/"))+"/html/"+getSectionNameFile(iSection);
				sBase+="/html/";				
				sAssessment = parseXMLFileName(sXML);
				oMap.put("assessment", sAssessment);
				writeAllHTML(sLocalXML, sXML, sBase, sSkin, oMap);				
			}
			
			String sParams = request.getQueryString();
			createResponse(response, sURL, sSkin, sLang, iSection, sParams);
		}catch (Exception e){
			e.printStackTrace(response.getWriter());
			response.sendError(400, e.toString());
		}
	}
	
	protected String parseXMLFileName(String sXML){
		String sName = null;
		if (sXML!=null){
			int iStart = sXML.lastIndexOf("/")+1;
			int iEnd = sXML.lastIndexOf(".xml");
			if (iEnd>0){ 
				sName = sXML.substring(iStart, iEnd);
			}
		}
		return sName;
	}
	
	protected void createResponse(HttpServletResponse response, String sURL, String sSkin, String sLang, int iSection, String sParams) throws Exception{
		
	}
	
	protected void writeAllHTML(String sLocalXML, String sXML, String sBase, String sSkin, HashMap oMap) throws Exception{
		//System.out.println("writeAllHTML("+sLocalXML+", "+sXML+", "+sBase+", "+sSkin+", ...)");//ALLR
		Document doc = QTITransformer.getDocument(sXML);
		Element eRoot = doc.getRootElement();
		Vector vSections = QTITransformer.getSections(eRoot);
		File fHTML = getHTMLFile(sBase, 0);
		File fXML = getXMLFile(sLocalXML);
		if (isModified(fHTML, fXML)){
			// It's necessary to create all files
			for (int i=0;i<=vSections.size();i++){
				writeHTML(sXML, sBase, sSkin, i, oMap);			
			}
		}
	}
	
	protected File getHTMLFile(String sBase, int iSection) throws QVServletException {
		File fHTML = null;
		if (iSection>=0){
			fHTML = new File(sBase+getSectionNameFile(iSection));
		}
		return fHTML;
	}
	
	protected File getXMLFile(String sXML) throws QVServletException {
		File fXML = null;
		if (exists(sXML)){
			fXML = new File(sXML);
		}else{
			throw new QVServletException("EXCEPTION XML file doesn' exist: "+sXML);
		}
		return fXML;
	}
	
	
	protected boolean isModified(File fHTML, File fXML){
		return !(fHTML!=null && fHTML.exists() && fXML!=null && fXML.exists() && fXML.lastModified()<fHTML.lastModified());
		
	}

	protected void writeHTML(String sXML, String sBase, String sSkin, int iSection, HashMap oMap) throws Exception{
		//System.out.println("writeHTML("+sXML+", "+sBase+", "+sSkin+", "+iSection+",...)");//ALLR
		if (sXML!=null && sBase!=null){
			sSkin=(sSkin!=null?sSkin:"default");
			
			File fHTML = getHTMLFile(sBase,iSection);
			File fXML = getXMLFile(sXML);
			if (isModified(fHTML, fXML)){
				// Only create HTML page if XML has been modified after HTML or if HTML doesnt' exists
				Hashtable hHTML = QTITransformer.createHTML(sXML, sSkin, sBase, iSection, oMap);
				
	    		// Add CSS, js...
		        File oFileOut = new File(sBase+Constants.JS_FILE);
		        if (!oFileOut.exists()){
			        File oFileIn = new File(Constants.CSS_JS_PATH+Constants.JS_FILE);
		        	copyFile(oFileIn, oFileOut);
		        }
		        /*File fIndexOut = new File(sBase+File.separator+".."+File.separator+"index.htm");
		        if (!fIndexOut.exists()){
			        File fIndexIn = new File(Constants.CSS_JS_PATH+File.separator+".."+File.separator+"index.htm");
		        	copyFile(fIndexIn, fIndexOut);
		        }*/
			}

		}else{
			throw new QVServletException("Missing mandatory parameter");			
		}
    }
	
	protected static String getLocalBase(String sUser, String sAssessment){
		String sBase = getProperty(QVSTORE_LOCAL_URL_KEY)+sUser+"/"+sAssessment+"/";
		return sBase;
	}
	protected static String getLocalBase(String sXML) throws Exception{
		String sBase = getTemporalyBase();
		if (sXML!=null){
			if (isHTTP(sXML)){
				URL uXML = new URL(sXML);
				sBase+=uXML.getHost()+"/";
			}
			sBase+=(new Date()).getTime()+File.separator;
			//sBase+=File.separator+sXML+File.separator;
		}		
		return sBase;
	}
	
	protected static String getRemoteBase(String sUser, String sAssessment){
		String sBase = getProperty(QVSTORE_REMOTE_URL_KEY)+sUser+"/"+sAssessment+"/";
		return sBase;
	}

	protected static String getTemporalyBase(){
		return getProperty(QVSTORE_REPOSITORY_PATH_KEY)+File.separator;
	}

	protected String getSectionNameFile(int iSection){
		String sName=null;
		if (iSection>0){
			sName = "section_"+iSection+".htm";
		}else{
			sName = "index.htm";
		}
		return sName;		
	}
	
	protected static boolean isHTTP(String sURL){
		return (sURL!=null && sURL.startsWith("http"));
	}
	
	protected static boolean exists(String sURL){
		try {
			if (isHTTP(sURL)){
				HttpURLConnection.setFollowRedirects(false);
				HttpURLConnection con = (HttpURLConnection) new URL(sURL).openConnection();
				con.setRequestMethod("HEAD");
				return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
			}else{
				File fXML = new File(sURL);
				return fXML.exists();
			}
		} catch (Exception e) {
			//System.out.println("File: "+sURL); //ALLR
			e.printStackTrace();
			return false;
		}
	}	
	
	public static String getParameter(HttpServletRequest request, String sParam){
		String sValue = null;
		try{
			sValue = request.getParameter(sParam);
			if (sValue!=null && sValue.trim().length()==0) sValue=null;
		}catch (Exception e){
			logger.debug("EXCEPTION getting request parameter '"+sParam+"' -->"+e);
		}
		return sValue;
	}
	
	protected static String getProperty(String sName){
		return getSettings().getProperty(sName);
	}
	
	protected static void copyFile(File in, File out) throws Exception {
		try{
			if (in.exists()){
			    FileInputStream fis  = new FileInputStream(in);
			    if (!out.exists()) {
			    	out.getParentFile().mkdirs();
			    	out.createNewFile();
			    }
			    FileOutputStream fos = new FileOutputStream(out);
			    byte[] buf = new byte[1024];
			    int i = 0;
			    while((i=fis.read(buf))!=-1) {
			      fos.write(buf, 0, i);
			    }
			    fis.close();
			    fos.close();
			}else{
				logger.info("copyFile: in file ["+in+"] doesn't exist or out file ["+out+"] already exists");
			}
		}catch (Exception e){
			logger.error("EXCEPTION -> "+e);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Retorna un parametre enter. Veure 'getParameter'.
	 * @param request
	 * @param sParam
	 * @param iDefault
	 * @return
	 */
	public static int getIntParameter(HttpServletRequest request, String sParam, int iDefault){
		int result=iDefault;
		String s = null;
		try{
			s=getParameter(request, sParam);
			if (s!=null && s.trim().length()>0)
				result=Integer.parseInt(s);
		}
		catch (Exception e){
			logger.debug("EXCEPCIO obtenint el parametre '"+sParam+"' (value="+s+" -->"+e);
		}
		return result;
	}
	
//	***************************	
//	* Properties
//	***************************	
	public static String getSetting(String sKey){
		return getSettings().getProperty(sKey);
	}

	public static Properties getSettings(){
		if (pSettings==null){
			pSettings = new Properties();
			try{
				pSettings.load(QVGenerateHTMLServlet.class.getResourceAsStream(PROPERTIES_PATH+DBCONF_FILE));
				File f = new File(System.getProperty("user.home"), DBCONF_FILE);
				if(f.exists()){
					FileInputStream is=new FileInputStream(f);
					pSettings.load(is);
					is.close();
				}
			} catch (FileNotFoundException f) {
				logger.error(f);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return pSettings;
	}	
	
	
	
}


/**
 * @author sarjona
 */
class QVServletException extends Exception {
	
	public QVServletException(String sMsg){
		super(sMsg);
	}

}
	