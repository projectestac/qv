package edu.xtec.qv.player;

import java.applet.Applet;
import java.awt.Event;
import java.awt.Graphics;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;

import edu.xtec.qv.result.QTIResponseParser;
import edu.xtec.qv.result.QTISymtab;
import edu.xtec.qv.result.QualificationSymtabs;

import edu.xtec.qv.lms.data.QVUserInfo;
import edu.xtec.qv.lms.util.thread.*;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CorrectQVApplet extends Applet{

	public static final String ENCODING = "ISO-8859-1"; 
	protected static boolean bDebug = true;
	protected static boolean bTrace = true;
	
	String sXML;
	String sResponses;
	String sScores;
	String sScore;
	String sCorrect;
	int iSection = 0;
	String sSectionId;
	String sAssessment;
	HashMap hmFeedback = new HashMap();
	static HashMap hmDoc = new HashMap();
	static HashMap hmInputStream = new HashMap();
	
	String loginFilePath = "";
	String configFilePath = "";
	String dataFilePath = "";
	String documentBaseDirPath = null;

	public void init() {
		//if (bDebug && bTrace) System.out.println("init......");
      System.setSecurityManager(new SecurityManager(){public void checkConnect(String host, int port){}});		
	}

	static File fProp = null;
	
	static edu.xtec.qv.lms.util.thread.InvokerThread it; //A5
	
	public void start() {
		//if (bDebug && bTrace) System.out.println("start...... ");
		open(getXMLFilename());
		getInputStream(getXMLFilename());
		
		/*try{
			System.out.println("-0- filename:"+getXMLFilename());
			edu.xtec.qv.lms.report.qtidata.QTIAssessmentInfo ai = edu.xtec.qv.lms.report.qtidata.QTIAssessmentInfo.getQTIAssessmentInfo(getInputStream(getXMLFilename()));
			
			String header = edu.xtec.qv.lms.report.ReportGenerator.getCSVResultHeader(ai);
			
			String content = edu.xtec.qv.lms.report.ReportGenerator.getCSVResultReport(ai, QVUserInfo userInfo, QVAssignment qvAssignment, ArrayList alQVSection)
			
			System.out.println("-1-"+ai.toString());
		} catch (Exception ex){
			ex.printStackTrace(System.err);
		}*/
		

		it = new edu.xtec.qv.lms.util.thread.InvokerThread(); //A5
		it.start();//5

		iniLoginFilePath();
		iniConfigFilePath();
		iniDataFilePath();
		File fDocumentBase = new File(getDocumentBase().getFile()).getParentFile();
		String s = fDocumentBase.getAbsolutePath();
		if (s.indexOf("?")>0){
			s = s.substring(0, s.indexOf("?"));
			fDocumentBase = new File(s);			
		}
		documentBaseDirPath = removeSpace(fDocumentBase.getAbsolutePath());
		
		System.out.println("start****");
		FileSaver fs = new FileSaver();
		fs.start();
		
		loadProperties();
	}
	
	public void paint(Graphics g) {
	}
	
	public boolean action(Event evt, Object arg) {
		return true;
	}	
	
	
/*************************************************
 * Getter/Setter
 **************************************************/

	public String getAssessmentName(){
		if (this.sAssessment==null){
			this.sAssessment=this.getParameter("assessment");
			if (sAssessment==null || sAssessment.trim().length()==0) this.sAssessment = getDirBaseFile().getName();
		}
		return this.sAssessment;
	}
	
	public int getSectionNumber(){
		if (this.iSection<=0){
			try{
				this.iSection = Integer.parseInt(this.getParameter("section_number"));
			}catch (Exception e){
				if (bTrace) e.printStackTrace();
			}
		}
		return iSection;
	}
		
	public String getSectionId(){
		if (this.sSectionId==null){
			this.sSectionId = this.getParameter("section");
		}
		return this.sSectionId;
	}
	
	public edu.xtec.qv.lms.util.thread.InvokerThread getInvokerThread(){
		return it;
	}
	
/*************************************************
 * Responses
 **************************************************/
						
	private static InputStream getInputStream (String sFile){
		//if (bTrace && bDebug) System.out.println("getInputStream()-> file="+sFile);
        byte[] bA=null;
        if (hmInputStream.containsKey(sFile)){
            bA=(byte[])hmInputStream.get(sFile);
        } else{
            try{
            	InputStream is = null;
				if (sFile.startsWith("http:")){
					if (sFile.indexOf("?")<0) sFile+="?";
					else sFile+="&";
					sFile+="random="+(new Date().getTime())+Math.round(Math.random()*100);
					is = new URL(sFile).openStream();
				}else{
					is = new FileInputStream(new File(sFile));
				}
                bA=readInputStream(is,1024);
                hmInputStream.put(sFile, bA);//TODO Aquesta línia estava comentada...
            }
            catch (IOException e){
            	e.printStackTrace();
            }
        }
        if (bA!=null) return new ByteArrayInputStream(bA);
        else return null;
		
/*		
		InputStream is = null;
		if (sFile!=null){
			if (hmInputStream.containsKey(sFile)){
				is = (InputStream)hmInputStream.get(sFile);
				if (bTrace && bDebug) System.out.println("getInputStream()-> HASHMAP");
			}else{
				try {
					if (sFile.startsWith("http:")){
						is = new URL(sFile).openStream();
					}else{
						is = new FileInputStream(sFile);
					}
					hmInputStream.put(sFile, is);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (bTrace && bDebug) System.out.println("getInputStream()-> is?"+(is!=null));
		return is;
*/
	}
	
    private static byte[] readInputStream(InputStream is, int stepSize) throws IOException{
        boolean cancel=false;
        BufferedInputStream bufferedStream = null;
        if(is instanceof BufferedInputStream)
            bufferedStream=(BufferedInputStream)is;
        else
            bufferedStream = new BufferedInputStream(is);
        
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        byte[] buffer=new byte[stepSize];
        int bytesRead;
        while(!cancel){
            //bytesRead=is.read(buffer);
            bytesRead=bufferedStream.read(buffer);
            if(bytesRead<=0) break;
            os.write(buffer, 0, bytesRead);
            Thread.yield();
        }
        buffer=os.toByteArray();
        os.close();
        //is.close();
        bufferedStream.close();
        if(cancel)
            throw new InterruptedIOException("Cancelled by user");            
        return buffer;
    }
	
	
	public void correct(String sResponses){
		//if (bDebug && bTrace) System.out.println("correct() -> responses="+sResponses);
		String sXML = getXMLFilename();
		//if (bDebug && bTrace) System.out.println("correct() -> "+sXML);
		try{
			InputStream is = getInputStream(sXML);
			//InputStream isQTIXml = getInputStreamForURL();
			hmFeedback = new HashMap();
			QTIResponseParser qr = new QTIResponseParser(is, hmFeedback);
			QualificationSymtabs qs = new QualificationSymtabs();
			//QTISymtab st = qr.getFullPuntuation(getSectionNumber(), "1132751260185406-->1132751228532541=1132751260186479#1132751548767336-->1132751533715271=1132751548767622", qs);
			QTISymtab st = qr.getFullPuntuation(getSectionNumber(), sResponses, qs);
			is.close();
			sScore= st.getScore().toString();
			if (bDebug && bTrace) System.out.println("score="+sScore);
			sScores = qs.getSectionScoringRepresentation();
			if (bDebug && bTrace) System.out.println("scores="+sScores);
			if (bDebug && bTrace) System.out.println("feedbacks="+hmFeedback);
		}catch (Exception e){
			if (bTrace){
				System.out.println("EXCEPTION getting correct file ("+sXML+") -> "+e);
				if (bDebug) e.printStackTrace();
			}
		}			
		
	}
		
	public String getScores(){
		return this.sScores;
	}

	public String getScore(){
		return this.sScore;
	}

	public String getResponses(){
		if (this.sResponses==null){
			this.sResponses = this.getParameter("responses");
			this.sResponses = decode(this.sResponses);
		}
		return sResponses;
	}
	
	private String getFeedbackId(String sItemId){
		String sFeedbackId = null;
		Iterator itFeed = hmFeedback.keySet().iterator();
		while (itFeed.hasNext()){
			String sFeed = (String)itFeed.next();
			if (sFeed.startsWith(sItemId+"*")){
				sFeedbackId = (String)hmFeedback.get(sFeed);
				break;
			}
		}
		//if (bTrace && bDebug) System.out.println("getFeedbackId()-> item="+sItemId+"  feedbackId="+sFeedbackId);
		return sFeedbackId;
	}
	
	public String getFeedback(String sItemId){
		String sFeedback = "";
		String sFeedbackId = getFeedbackId(sItemId);
		if (sFeedbackId!=null){
			Document doc = open(getXMLFilename());
			if (doc!=null){
				Element eSection = getElementByIdent(doc.getRootElement(), "section", getSectionId());
				if (eSection!=null){
					Element eItem = getElementByIdent(eSection, "item", sItemId);
					if (eItem!=null){
						Element eItemfeedback = getElementByIdent(eItem, "itemfeedback", sFeedbackId);
						if (eItemfeedback!=null){
							try{
								Iterator it = eItemfeedback.getDescendants(new ElementFilter("material"));
								if (it.hasNext()){
									sFeedback = ((Element)it.next()).getChildText("mattext");
								}
							}catch (Exception e){e.printStackTrace();}
						}
					}
					
				}
			}
		}
		return sFeedback;
	}
	
	private Element getElementByIdent(Element e, String sElementName, String sIdent){
		Element elem = null;
		Iterator itSections = e.getDescendants(new ElementFilter(sElementName));
		while (itSections.hasNext()){
			Element eTmp = (Element)itSections.next();
			if (eTmp.getAttributeValue("ident").equals(sIdent)){
				elem = eTmp;
				break;
			}
		}
		return elem;
	}
	
/*************************************************
 * Utils
 **************************************************/
	protected String decode(String s){
		String sRes  = s;
		try{
			sRes = URLDecoder.decode(s, ENCODING);			
		}catch (Exception e){
			if (bTrace){
				System.out.println("EXCEPTION LocalQVApplet.decode-> "+e);
			}
		}
		return sRes;
	}
	
/*************************************************
 * XML (open, save...)
 **************************************************/
		
	/**
	 * @param sPath xml file to open
	 * @return Document object with specified XML file 
	 * @throws Exception
	 */
	protected static Document open(String sPath){
		Document d = null;
		try{
			if (!hmDoc.containsKey(sPath)){
				if (sPath!=null){
					Reader reader = new InputStreamReader(getInputStream(sPath), ENCODING);
					d = getXMLDocument(reader);
					reader.close();
				}
				hmDoc.put(sPath, d);
			} else{
				d = (Document)hmDoc.get(sPath);
			}
		} catch (Exception e){
			if (bTrace){
				System.out.println("EXCEPTION opening XML file '"+sPath+"' --> "+e);
				e.printStackTrace();
			}
		}
		return d;
	}

	/**
	 * Obté el document XML associat al reader indicat
	 * @param is
	 * @return
	 * @throws Exception
	 */
	protected static Document getXMLDocument(Reader reader) throws Exception{
		SAXBuilder sb = createSAXBuilder();
		org.jdom.Document doc=sb.build(reader);
		return doc;        
	}
	
	/**
	 * Obté el document XML associat a l'String indicat
	 * @param is
	 * @return
	 * @throws Exception
	 */
	protected static Document getXMLDocument(String sXML) throws Exception{
		SAXBuilder sb = createSAXBuilder();
		StringReader sr = new StringReader(sXML);
		org.jdom.Document doc=sb.build(sr);
		return doc;        
	}
	
	/**
	 * Crea un SAXBuilder
	 * @return
	 * @throws Exception
	 */
	protected static SAXBuilder createSAXBuilder() throws Exception{
		SAXBuilder sb=new SAXBuilder(false);
		sb.setValidation(false);
		sb.setExpandEntities(false);
		return sb;
	}
	
	public String getXMLFilename(){
		if (sXML==null){
			sXML = this.getParameter("xml");
			if (sXML==null || sXML.trim().length()==0){
				sXML = getDirBase()+getFileSeparator()+".."+getFileSeparator()+getAssessmentName()+".xml";
			}
			
			File fFile = new File(sXML);
			if (!fFile.exists()){
				File fDir = new File(getDirBase()+getFileSeparator()+".."+getFileSeparator());
				if (fDir.exists()){
					/** TODO: Cerca fitxer **/
					String[] sList = fDir.list(new XMLFilter());
					if (sList!=null){
						for (int i=0;i<sList.length;i++){
							if (!"imsmanifest.xml".equalsIgnoreCase(sList[i])){
								sXML = getDirBase()+getFileSeparator()+".."+getFileSeparator()+sList[i];
								break;
							}
						}
					}
				}
			}
		}
		//if (bTrace && bDebug) System.out.println("getXMLFilename-> xml="+sXML+" base="+getDirBase()+"  assessment="+getAssessmentName());
		return sXML;
		//return new File(getDirBase()).getParent()+getFileSeparator()+getAssessmentName()+".xml";
		
		//return getDirBase()+getFileSeparator()+"qv"+getFileSeparator()+getAssessmentName()+".xml";
	}

	protected String getDirBase(){
		String sDir = null;

		if (getDocumentBase().getProtocol().startsWith("http")){
			String sURL = getDocumentBase().toString();
			if (sURL.indexOf("?")>0){
				sURL=sURL.substring(0, sURL.indexOf("?"));
			}
			sDir = sURL.substring(0, sURL.lastIndexOf("/"));
		}else{
			String sURL=getDocumentBase().getFile().toString();
			if (sURL.indexOf("?")>0){
				sURL=sURL.substring(0, sURL.indexOf("?"));
			}
			sDir = removeSpace(new File(sURL).getParentFile().toString());
		}
		
/*		if (getDocumentBase().getProtocol().startsWith("http")){
			String sURL = getDocumentBase().toString();
			if (sURL.indexOf("?")>0){
				sURL=sURL.substring(0, sURL.indexOf("?"));
			}
			sDir = sURL.substring(0, sURL.lastIndexOf("/"));
		}else{
			sDir = removeSpace(new File(getDocumentBase().getFile()).getParentFile().toString());
		}*/
		
		
		//if (bTrace && bDebug) System.out.println("getDirBase()-> "+sDir);
		return sDir;
	}
	
	protected String removeSpace(String sFile){
		if (sFile.indexOf("%20")>=0) {
			sFile = sFile.replaceAll("%20"," ");
		}	
		return sFile;
	}		
	
	protected String getFileSeparator(){
		if (getDocumentBase().getProtocol().startsWith("http")){
			return "/";
		}else{
			return File.separator;
		}
	}

	protected File getDirBaseFile(){
		//File fDir = new File(getDocumentBase().getFile()).getParentFile();
		File fDir = new File(getDirBase()).getParentFile();
		return fDir;
	}		

/////////////////////////////// Config paths ///////////////////////////////////////////////////////////////////////////////	
	
	private void iniLoginFilePath(){
		loginFilePath = iniFilePath("loginFileRelativePath", "login", "login.properties", true);
	}

	private void iniConfigFilePath(){
		configFilePath = iniFilePath("configFileRelativePath", "config", "qv_config.properties", true);
	}
	
	private void iniDataFilePath(){
		dataFilePath = iniFilePath("dataFileRelativePath", "data", null, false);
	}
	
	public String getLoginFilePath(){
		return loginFilePath;
	}
	
	public String getConfigFilePath(){
		return configFilePath;
	}
	
	public String getDataFilePath(){
		return dataFilePath;
	}
	
	public String getDocumentBaseDirPath(){
		return documentBaseDirPath;
	}
	
	public void setLoginFilePath(String loginFilePath){
		loginFilePath = loginFilePath.replaceAll("/","\\").replaceAll("$","\\");
		System.out.println("setLoginFilePath("+loginFilePath+")");
		this.loginFilePath = documentBaseDirPath+File.separator+loginFilePath;
	}

	public void setConfigFilePath(String configFilePath){
		configFilePath = configFilePath.replaceAll("/","\\").replaceAll("$","\\");
		System.out.println("setConfigFilePath("+configFilePath+")");
		this.configFilePath = documentBaseDirPath+File.separator+configFilePath;
	}

	public void setDataRelativeFilePath(String dataFilePath){
		try{
			dataFilePath = dataFilePath.replace('/','\\').replace('$','\\');
			System.out.println("setDataRelativeFilePath("+dataFilePath+")");
			this.dataFilePath = documentBaseDirPath+File.separator+dataFilePath;
		} catch (Exception ex){
			ex.printStackTrace(System.err);
		}
	}

	
/*************************************************
 * Login
 **************************************************/
	public static final String LOGIN_NUM_USERS_PROPERTY_NAME="numUsers";
	public static final String LOGIN_PREFIX_USERS_PROPERTY_NAME="prefixUsers";
	public static final String LOGIN_PASSWORD_PROPERTY_NAME="password";
	public static final String LOGIN_ADMIN_PASSWORD_PROPERTY_NAME="passwordAdmin";
	public static final String LOGIN_USERID_PROPERTY_NAME="userId";
	public static final String LOGIN_USERNAME_PROPERTY_NAME="userName";
	public static final String LOGIN_USERIT_PROPERTY_NAME="userIT";
	public static final String LOGIN_USERNE_PROPERTY_NAME="userNE";
	
	public static final String LOGIN_NUM_GROUPS_PROPERTY_NAME="numGroups";
	public static final String LOGIN_GROUPNAME_PROPERTY_NAME="groupName";
	
	
	public java.util.Properties prop = null;
	
	private String getLoginProperty(String property){
		String sProp = null;
		if (prop==null)
			loadProperties();
		if (prop!=null)
			sProp = prop.getProperty(property);
		return sProp;
	}
	
	private void setLoginProperty(String propertyName, String value){
		//System.out.println("*****setLoginProperty("+propertyName+","+value+")");
		if (prop==null)
			loadProperties();
		if (prop!=null){
			if (value!=null)
				prop.setProperty(propertyName, value);
			else
				prop.remove(propertyName);
			//A3 setDoSave(true);
		}
		//setDoSave(true);	
	}
	
	public void saveLoginProperties(){//A3
		if (prop==null)
			loadProperties();
		if (prop!=null){
			setDoSave(true);
		}
	}
	
	private void deleteLoginProperties(String groupId, int userNum){
		prop.remove(LOGIN_USERID_PROPERTY_NAME+"_"+groupId+"_"+userNum);
		prop.remove(LOGIN_USERNAME_PROPERTY_NAME+"_"+groupId+"_"+userNum);
		prop.remove(LOGIN_USERIT_PROPERTY_NAME+"_"+groupId+"_"+userNum);
		prop.remove(LOGIN_USERNE_PROPERTY_NAME+"_"+groupId+"_"+userNum);
	}

	public boolean canLoadLoginProperties(){
		return prop!=null;
	}
	
	private void loadProperties(){
		if (prop==null){
			InvokerAction loadPropertiesAction = new InvokerAction(){
				public Object runAction(){
					java.util.Properties prop = new java.util.Properties();
					try{
						String filePath = getLoginFilePath();
						FileInputStream fis = new FileInputStream(filePath);
						System.out.println("Carregant fitxer de login des de: "+filePath);
						prop.load(fis);
						fis.close();
					} catch (Exception ex){
						ex.printStackTrace(System.out);
						prop = null;
					}
					return prop;
				}
			};
			InvokerThreadWaiter itw = new InvokerThreadWaiter(it, loadPropertiesAction);
			itw.start();
			prop = (java.util.Properties)loadPropertiesAction.getResult();
		}
	}
	
	private void saveProperties(){
		try{
			String filePath = getLoginFilePath();
			//System.out.println("-------filePath:"+filePath);
			File fFile = new File(filePath);
			if (!fFile.exists()){
				System.out.println("El fitxer "+filePath+" no existeix");
				fFile.createNewFile();
			}java.io.FileOutputStream fos = new java.io.FileOutputStream(fFile);
			//System.out.println("Desant fitxer de login a: "+filePath+" "+prop.size()+" propietats.");
			prop.store(fos, "QV Login properties file");
			fos.flush();
			fos.close();
		} catch (Exception ex){
			ex.printStackTrace(System.out);
			prop = null;
		}
	}
	
	private String iniFilePath(String parameterName, String dirName, String fileName, boolean bMustExistFile){
		System.out.println("iniFilePath("+dirName+")");
		String filePath = null;
		String fileRelativePath = getParameter(parameterName);
		//System.out.println("fileRelativePath:"+fileRelativePath);
		if (fileRelativePath!=null && fileRelativePath.length()>0){
			System.out.println("parameter");
			File fDir = new File(getDocumentBase().getFile()).getParentFile();
			String s = fDir.getAbsolutePath();
			if (s.indexOf("?")>0){
				s = s.substring(0, s.indexOf("?"));
				fDir = new File(s);			
			}
			filePath = removeSpace(fDir.getAbsolutePath()+File.separator+fileRelativePath);
		} else {
			File dir = getDir(dirName, fileName, bMustExistFile);
			if (bMustExistFile)
				filePath = removeSpace(dir.getAbsolutePath()+File.separator+fileName);
			else
				filePath = removeSpace(dir.getAbsolutePath());
		}
		System.out.println("iniFilePath("+dirName+")="+filePath);
		return filePath;
	}
	
	private File getDir(String dirName, String fileName, boolean bMustExistFile){
		File fDir = new File(getDocumentBase().getFile()).getParentFile();
		fDir = new File(removeSpace(fDir.getAbsolutePath()));
		String s = fDir.getAbsolutePath();
		if (s.indexOf("?")>0){
			s = s.substring(0, s.indexOf("?"));
			fDir = new File(s);			
		}
		boolean bFound = false;
		int prof = 10; //Màxima profunditat a relativa que explorarem per trobar el fitxer
		while (!bFound && fDir!=null && fDir.exists() && prof>0){
			File fDataDir = null;
			if (bMustExistFile) 
				fDataDir = new File(fDir+File.separator+dirName+File.separator+fileName);
			else
				fDataDir = new File(fDir+File.separator+dirName);
			if (fDataDir.exists()){
				bFound = true;
				fDir = new File(fDir+File.separator+dirName);
				System.out.println(dirName+" dir trobat:"+fDataDir.getAbsolutePath());
			} else {
				File fFiles = new File(fDir+File.separator+"files"+File.separator+dirName);
				if (fFiles.exists()){
					bFound = true;
					fDir = fFiles;
					System.out.println(dirName+" dir trobat:"+fFiles.getAbsolutePath());
				} else {
					fDir = fDir.getParentFile();
				}
			}
			prof --;
		}
		if (!bFound){
			fDir = new File(new File(getDocumentBase().getFile()).getParentFile()+File.separator+dirName);
		}
		System.out.println("getDir("+dirName+")="+fDir.getAbsolutePath()+" dirName:"+dirName+" bMustExistFile:"+bMustExistFile+" s="+s);
		return fDir;
	}
	
	public String getDataGroups(){
		String dataGroups = edu.xtec.qv.lms.report.QVResultsGenerator.getDataGroups(this);
		/*int numGroups = getNumGroups();
		for (int i=1; i<=numGroups; i++){
			loginGroups = loginGroups + getGroupName(i) + ";";
		}*/
		return dataGroups;
	}
	
	/*private void iniLoginFilePath(){
		String filePath = null;
		String loginFileRelativePath = getParameter("loginFileRelativePath");
		System.out.println("loginFileRelativePath:"+loginFileRelativePath);
		if (loginFileRelativePath!=null && loginFileRelativePath.length()>0){
			File fDir = new File(getDocumentBase().getFile()).getParentFile();
			filePath = removeSpace(fDir.getAbsolutePath()+File.separator+loginFileRelativePath);
		} else {
			File dataDir = getLoginDir();
			filePath = removeSpace(dataDir.getAbsolutePath()+File.separator+"login.properties");
		}
		System.out.println("filePath:"+filePath);
		loginFilePath = filePath;
	}
	
	private void iniConfigFilePath(){
		String filePath = null;
		String configFileRelativePath = getParameter("configFileRelativePath");
		System.out.println("configFileRelativePath:"+configFileRelativePath);
		if (configFileRelativePath!=null && configFileRelativePath.length()>0){
			File fDir = new File(getDocumentBase().getFile()).getParentFile();
			filePath = removeSpace(fDir.getAbsolutePath()+File.separator+configFileRelativePath);
		} else {
			File dataDir = getConfigDir();
			filePath = removeSpace(dataDir.getAbsolutePath()+File.separator+"qv_config.properties");
		}
		System.out.println("filePath:"+filePath);
		configFilePath = filePath;
	}
	
	private File getLoginDir(){
		File fDir = new File(getDocumentBase().getFile()).getParentFile();
		File fDataDir = new File(fDir+File.separator+"login");
		return fDataDir;
	}

	private File getConfigDir(){
		File fDir = new File(getDocumentBase().getFile()).getParentFile();
		boolean bFound = false;
		int prof = 10; //Màxima profunditat a relativa que explorarem per trobar el fitxer
		while (!bFound && fDir!=null && fDir.exists() && prof>0){
			File fDataDir = new File(fDir+File.separator+"config"+File.separator+"qv_config.properties");
			if (fDataDir.exists()){
				bFound = true;
				fDir = new File(fDir+File.separator+"config");
				System.out.println("Config dir trobat!!!:"+fDataDir.getAbsolutePath());
			} else {
				fDir = fDir.getParentFile();
			}
			prof --;
		}
		if (!bFound){
			fDir = new File(new File(getDocumentBase().getFile()).getParentFile()+File.separator+"config");
		}
		System.out.println("fDir="+fDir.getAbsolutePath());
		return fDir;
	}*/

	public int getNumUsers(String groupId){
		int iNumUsers = 0;
		String sNumUsers = getLoginProperty(LOGIN_NUM_USERS_PROPERTY_NAME+"_"+groupId);
		try{
			if (sNumUsers!=null){
				iNumUsers = Integer.parseInt(sNumUsers);
			}
		}catch (Exception ex){
			System.out.println("Format incorrecte de número d'usuaris: "+sNumUsers+" (groupId="+groupId+")");
			ex.printStackTrace(System.err);
		}
		return iNumUsers;
	}
	
	public void setNumUsers(String groupId, int iNumUsers){
		setLoginProperty(LOGIN_NUM_USERS_PROPERTY_NAME+"_"+groupId, iNumUsers+"");
	}
	
	public String getPrefixUsers(String groupId){
		return getLoginProperty(LOGIN_PREFIX_USERS_PROPERTY_NAME+"_"+groupId);
	}
	
	public void setPrefixUsers(String groupId, String sPrefixUsers){
		setLoginProperty(LOGIN_PREFIX_USERS_PROPERTY_NAME+"_"+groupId, sPrefixUsers);
	}

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
        	int halfbyte = (data[i] >>> 4) & 0x0F;
        	int two_halfs = 0;
        	do {
	            if ((0 <= halfbyte) && (halfbyte <= 9))
	                buf.append((char) ('0' + halfbyte));
	            else
	            	buf.append((char) ('a' + (halfbyte - 10)));
	            halfbyte = data[i] & 0x0F;
        	} while(two_halfs++ < 1);
        }
        return buf.toString();
    }
 
    public static String SHA1(String text) {
	byte[] sha1hash = new byte[40];
	try{
		MessageDigest md;
		md = MessageDigest.getInstance("SHA-1");
		
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		sha1hash = md.digest();
	} catch (Exception ex){
		ex.printStackTrace(System.err);
	}
	return convertToHex(sha1hash);
    }
	
	public String getPassword(String groupId){
		return getLoginProperty(LOGIN_PASSWORD_PROPERTY_NAME+"_"+groupId);
	}
	
	public void setPassword(String groupId, String sPassword){
		//String passwordEncriptat = SHA1(sPassword);
		//setLoginProperty(LOGIN_PASSWORD_PROPERTY_NAME+"_"+groupId, passwordEncriptat);
		setLoginProperty(LOGIN_PASSWORD_PROPERTY_NAME+"_"+groupId, sPassword);
	}
	
	public void setPasswordNoCrypt(String groupId, String sPassword){
		setLoginProperty(LOGIN_PASSWORD_PROPERTY_NAME+"_"+groupId, sPassword);
	}
	
	public String getAdminPassword(){
		return getLoginProperty(LOGIN_ADMIN_PASSWORD_PROPERTY_NAME);
	}
	
	public void setAdminPassword(String sPassword){
		String passwordEncriptat = SHA1(sPassword);
		setLoginProperty(LOGIN_ADMIN_PASSWORD_PROPERTY_NAME, passwordEncriptat);
		//setLoginProperty(LOGIN_ADMIN_PASSWORD_PROPERTY_NAME, sPassword);
	}
	
	public String getUserId(String groupId, int userNum){
		return getLoginProperty(LOGIN_USERID_PROPERTY_NAME+"_"+groupId+"_"+userNum);
	}
	
	public void setUserId(String groupId, int userNum, String userId){
		boolean repeated = false;
		for (int i=1; !repeated && i<userNum; i++){
			if (userId.equalsIgnoreCase(getUserId(groupId, i)))
				repeated = true;
		}
		if (!repeated){
			setLoginProperty(LOGIN_USERID_PROPERTY_NAME+"_"+groupId+"_"+userNum, userId);
			if (getNumUsers(groupId)<userNum)
				setNumUsers(groupId, userNum);
		} else
			setUserId(groupId, userNum, userId+"(2)");
	}
	
	public String getUserName(String groupId, int userNum){
		return getLoginProperty(LOGIN_USERNAME_PROPERTY_NAME+"_"+groupId+"_"+userNum);
	}
	
	public String getUserName(String groupId, String userId){
		int num = getUserNum(groupId, userId);
		return getUserName(groupId, num);
	}
	
	public void setUserName(String groupId, int userNum, String userName){
		setLoginProperty(LOGIN_USERNAME_PROPERTY_NAME+"_"+groupId+"_"+userNum, userName);
		if (getNumUsers(groupId)<userNum)
			setNumUsers(groupId, userNum);
	}

	public String getUserIT(String groupId, int userNum){
		return getLoginProperty(LOGIN_USERIT_PROPERTY_NAME+"_"+groupId+"_"+userNum);
	}
	
	public void setUserIT(String groupId, int userNum, String userIT){
		if (userIT!=null && (userIT.equalsIgnoreCase("true") || userIT.equalsIgnoreCase("si") || userIT.equalsIgnoreCase("yes")))
			userIT = "YES";
		else
			userIT = "NO";
		setLoginProperty(LOGIN_USERIT_PROPERTY_NAME+"_"+groupId+"_"+userNum, userIT);
		if (getNumUsers(groupId)<userNum)
			setNumUsers(groupId, userNum);
	}

	public String getUserNE(String groupId, int userNum){
		return getLoginProperty(LOGIN_USERNE_PROPERTY_NAME+"_"+groupId+"_"+userNum);
	}
	
	public void setUserNE(String groupId, int userNum, String userNE){
		if (userNE!=null && (userNE.equalsIgnoreCase("true") || userNE.equalsIgnoreCase("si") || userNE.equalsIgnoreCase("yes")))
			userNE = "YES";
		else
			userNE = "NO";
		setLoginProperty(LOGIN_USERNE_PROPERTY_NAME+"_"+groupId+"_"+userNum, userNE);
		if (getNumUsers(groupId)<userNum)
			setNumUsers(groupId, userNum);
	}
	
	public int getNumGroups(){
		int iNumGroups = 0;
		String sNumGroups = getLoginProperty(LOGIN_NUM_GROUPS_PROPERTY_NAME);
		try{
			iNumGroups = Integer.parseInt(sNumGroups);
		}catch (Exception ex){
			ex.printStackTrace(System.err);
		}
		return iNumGroups;
	}
	
	public void setNumGroups(int iNumGroups){
		setLoginProperty(LOGIN_NUM_GROUPS_PROPERTY_NAME, iNumGroups+"");
	}
	
	public String getGroupName(int groupNum){
		return getLoginProperty(LOGIN_GROUPNAME_PROPERTY_NAME+groupNum);
	}
	
	public void setGroupName(int groupNum, String groupName){
		setLoginProperty(LOGIN_GROUPNAME_PROPERTY_NAME+groupNum, groupName);
		if (getNumGroups()<groupNum)
			setNumGroups(groupNum);
	}
	
	public String getLoginGroups(){
		String loginGroups = "";
		int numGroups = getNumGroups();
		for (int i=1; i<=numGroups; i++){
			loginGroups = loginGroups + getGroupName(i) + ";";
		}
		return loginGroups;
	}
	
	private void addUser(String groupId, int userNum, String userId, String userName, boolean userIT, boolean userNE){
		setUserId(groupId, userNum, userId);
		setUserName(groupId, userNum, userName);
		setUserIT(groupId, userNum, userIT?"YES":"NO");
		setUserNE(groupId, userNum, userNE?"YES":"NO");
	}
	
	public void deleteUser(String groupId, int userNum){
		int numUsers = getNumUsers(groupId);
		for (int i=userNum;i<numUsers;i++){
			setUserId(groupId, i, getUserId(groupId, i+1));
			setUserName(groupId, i, getUserName(groupId, i+1));
			setUserIT(groupId, i, getUserIT(groupId, i+1));
			setUserNE(groupId, i, getUserNE(groupId, i+1));
			userNum++;
		}
		deleteLoginProperties(groupId, userNum);
		setNumUsers(groupId, numUsers-1);
	}
	
	public void deleteGroup(int groupNum){//AA2
		try{
		int numGroups = getNumGroups();
		for (int i=groupNum; i<numGroups; i++){
			int numUsers = getNumUsers(i+"");
			for (int j=1;j<=numUsers;j++){
				deleteLoginProperties(i+"", j);
			}
			
			int numUsersNext = getNumUsers((i+1)+"");
			for (int j=1;j<=numUsersNext;j++){
				setUserId(i+"", j, getUserId((i+1)+"", j));
				setUserName(i+"", j, getUserName((i+1)+"", j));
				setUserIT(i+"", j, getUserIT((i+1)+"", j));
				setUserNE(i+"", j, getUserNE((i+1)+"", j));
			}
			setGroupName(i, getGroupName(i+1));
			setNumUsers(i+"", getNumUsers((i+1)+""));
			setPrefixUsers(i+"", getPrefixUsers((i+1)+""));
			setPasswordNoCrypt(i+"", getPassword((i+1)+""));
		}
		if (numGroups>0){
			int numUsers = getNumUsers(numGroups+"");
			for (int j=1;j<=numUsers;j++){
				deleteLoginProperties(numGroups+"", j);
			}
			prop.remove(LOGIN_GROUPNAME_PROPERTY_NAME+numGroups);
			prop.remove(LOGIN_NUM_USERS_PROPERTY_NAME+"_"+numGroups);
			prop.remove(LOGIN_PREFIX_USERS_PROPERTY_NAME+"_"+numGroups);
			prop.remove(LOGIN_PASSWORD_PROPERTY_NAME+"_"+numGroups);
			//System.out.println("prop.remove("+LOGIN_GROUPNAME_PROPERTY_NAME+numGroups+")");
		}
		setNumGroups(numGroups-1);
		} catch(Exception ex){
			ex.printStackTrace(System.err);
		}
	}
	
	public void restartParams(String groupId, int numUsers, String prefixUsers){
		int numUsersIni = getNumUsers(groupId);
		for (int i=numUsersIni;i>0;i--)
			deleteUser(groupId, i);
		setNumUsers(groupId, numUsers);
		setPrefixUsers(groupId, prefixUsers);
		
		for (int i=1;i<=numUsers;i++){
			String userId = prefixUsers;
			if (i<10)
				userId+="00";
			else if (i<100)
				userId+="0";
			userId+=i;
			addUser(groupId, i, userId, "", false, false);
		}
	}
	
	private int getUserNum(String groupId, String userId){
		int userNum = -1;
		int numUsers = getNumUsers(groupId);
		for (int i=1; i<=numUsers && userNum<0; i++){
			String currentUserId = getUserId(groupId, i);
			if (userId.equalsIgnoreCase(currentUserId))
				userNum = i;
		}
		return userNum;
	}
	
	public boolean existUserId(String groupId, String userId){
		int userNum = getUserNum(groupId, userId);
		if (userNum>=0)
			return true;
		else
			return false;
	}
	
	public int getTotalUsers(){
		int totalUsers = 0;
		int numGroups = getNumGroups();
		for (int i=1;i<=numGroups;i++){
			totalUsers += getNumUsers(i+"");
		}
		return totalUsers;
	}
	
	public boolean doLogin(String groupId, String userId, String password){
		loogedByTeacher = false;
		if (groupId.equals("") && userId.equalsIgnoreCase("admin")){
			if (password!=null){
				System.out.println("password:"+password);
				System.out.println("SHA1(password):"+SHA1(password));
				System.out.println("getAdminPassword():"+getAdminPassword());
				
			}
		
		
			//if (password!=null && SHA1(password).equals(getAdminPassword()))
			if (password!=null && password.equals(getAdminPassword()))
				return true;
			else
				return false;
		} else {
			if (existUserId(groupId, userId) && password!=null && password.equalsIgnoreCase(getPassword(groupId))){
				loggedUserId = userId;
				loggedUserGroupId = groupId;
				
				
				loggedUserGroupName = "";//A7
				try{
					loggedUserGroupName = getGroupName(Integer.parseInt(loggedUserGroupId));
				} catch (Exception ex){
					loggedUserGroupName = "group"+loggedUserGroupId;
				}
				
				
				edu.xtec.qv.lms.QVLMSActions lmsActions = getQVLMSActions();
				if (lmsActions instanceof edu.xtec.qv.lms.LocalFileQVLMS){
					edu.xtec.qv.lms.LocalFileQVLMS lf = (edu.xtec.qv.lms.LocalFileQVLMS)lmsActions;
					edu.xtec.qv.lms.data.QVAssignment qvAssignment = lf.getQVAssignment(-1);
					sectionOrder = qvAssignment.getSectionOrder()+"";
					itemOrder = qvAssignment.getItemOrder()+"";
					System.out.println("sectionOrder:"+sectionOrder+" itemOrder:"+itemOrder);
				}
				return true;
			}
			else
				return false;
		}
	}
	
	public boolean doLoginByTeacher(String groupName, String userId, String password){
		//if (password.equalsIgnoreCase(getAdminPassword()))
		loggedUserId = userId;
		loggedUserGroupId = groupName;//...
		loggedUserGroupName = groupName;
		loogedByTeacher = true;

		edu.xtec.qv.lms.QVLMSActions lmsActions = getQVLMSActions();
		if (lmsActions instanceof edu.xtec.qv.lms.LocalFileQVLMS){
			edu.xtec.qv.lms.LocalFileQVLMS lf = (edu.xtec.qv.lms.LocalFileQVLMS)lmsActions;
			edu.xtec.qv.lms.data.QVAssignment qvAssignment = lf.getQVAssignment(-1);
			sectionOrder = qvAssignment.getSectionOrder()+"";
			itemOrder = qvAssignment.getItemOrder()+"";
			System.out.println("sectionOrder:"+sectionOrder+" itemOrder:"+itemOrder);
		}
		return true;
	}
	
	public String getAssessmentNames(){
		System.out.println("getAssessmentNames");
		String assessmentNames = "";
		
		
		InvokerAction listFilesAction = new InvokerAction(){
			public Object runAction(){
				String assessmentNames = "";
				File f = new File(getDocumentBase().getFile()).getParentFile().getParentFile();
				f = new File(removeSpace(f.getAbsolutePath()));
				f = new File(f, "proves");
				System.out.println("f:"+f.getAbsolutePath());
				File[] files = f.listFiles();
				for (int i=0; files!=null && i<files.length; i++){
					File file = files[i];
					System.out.println("file "+file.getName());
					if (file.isDirectory()){
						assessmentNames = assessmentNames + file.getName() + ";";
					}
				}
				System.out.println("assessmentNames:"+assessmentNames);
				return assessmentNames;
			}
		};
		InvokerThreadWaiter itw = new InvokerThreadWaiter(it, listFilesAction);
		itw.start();
		return (String)listFilesAction.getResult();
	}
	
	public String getSectionOrder(){
		return sectionOrder;
	}
	
	public String getItemOrder(){
		return itemOrder;
	}
	
	boolean loogedByTeacher = false;//A7
	String loggedUserId = null; //A6
	
	public String getLoggedUserId(){
		return loggedUserId;
	}
	
	String loggedUserGroupId = null; //A6
	String loggedUserGroupName = null; //A7

	public String getLoggedUserGroupName(){
		return loggedUserGroupName;
	}
	
	public String getLoggedUserGroupId(){
		return loggedUserGroupId;
	}
	
	String sectionOrder = "0";
	String itemOrder = "0";
	
/*************************************************
 * Call service
 **************************************************/
	
	public /*static*/ String callService(String sService, String sXML){
		return callService(sService, sXML, "UTF-8");
	}
	
	private /*static*/ edu.xtec.qv.lms.QVLMSActions getQVLMSActions(){
		if(getLoggedUserId()!=null){
			String userId="userId";
			String groupName = getLoggedUserGroupName();
			String resultFilePath = getDataFilePath()+File.separator+
				edu.xtec.qv.lms.util.StringUtility.filter(groupName)+File.separator+
				"qti_result_report_"+edu.xtec.qv.lms.util.StringUtility.filter(loggedUserId)+".xml";
			
			QVUserInfo qvUserInfo;
			if (!loogedByTeacher){
				int userNum = getUserNum(loggedUserGroupId, loggedUserId);
				qvUserInfo = new QVUserInfo(loggedUserId, loggedUserGroupId, groupName, getUserIT(loggedUserGroupId, userNum), getUserNE(loggedUserGroupId, userNum));
			} else 
				qvUserInfo = new QVUserInfo(loggedUserId, groupName, groupName, "", "");
			return new edu.xtec.qv.lms.LocalFileQVLMS(it, qvUserInfo, getConfigFilePath(), resultFilePath);//A5
		} else
			return new edu.xtec.qv.lms.LocalFileQVLMS(it, getConfigFilePath(), getDataFilePath());
	}
	
	/*private edu.xtec.qv.lms.QVLMSActions getTeacherQVLMSActions(String userId, String groupName){
		String resultFilePath = getDataFilePath()+File.separator+
			edu.xtec.qv.lms.util.StringUtility.filter(groupName)+File.separator+
			"qti_result_report_"+edu.xtec.qv.lms.util.StringUtility.filter(userId)+".xml";
		QVUserInfo qvUserInfo = new QVUserInfo(userId, loggedUserGroupId, groupName, getUserIT(loggedUserGroupId, userNum), getUserNE(loggedUserGroupId, userNum));
		return new edu.xtec.qv.lms.LocalFileQVLMS(it, qvUserInfo, getConfigFilePath(), resultFilePath);//A5
	}*/
	
	public /*static*/ String callLocalService(String sService, String sXML, String sEncoding){
		//System.out.println("callLocalService("+sService+", "+sXML+", "+sEncoding+")");
		String result = "";
		edu.xtec.qv.lms.QVLMSActions qvLMS = getQVLMSActions();
		
		try {
			Document doc = getXMLDocument(sXML);
			if (doc!=null){
				Element eBean = doc.getRootElement();
				if (eBean!=null){
					//Element eItem = getElementByIdent(eSection, "item", sItemId);
					String actionName = eBean.getAttributeValue("id");
					if (actionName.equalsIgnoreCase("get_sections")){
						int assignmentId = getIntParamValue(eBean, "assignmentid", -1);
						result = qvLMS.getSections(assignmentId);
					} else if (actionName.equalsIgnoreCase("get_section")){
						int assignmentId = getIntParamValue(eBean, "assignmentid", -1);
						String sectionId = getParamValue(eBean, "sectionid");
						result = qvLMS.getSection(assignmentId, sectionId);
					} else if (actionName.equalsIgnoreCase("correct_section")){
						int assignmentId = getIntParamValue(eBean, "assignmentid", -1);
						String sectionId = getParamValue(eBean, "sectionid");
						String responses = eBean.getChildText("responses");
						String scores = eBean.getChildText("scores");
						result = qvLMS.correctSection(assignmentId, sectionId, responses, scores);
					} else if (actionName.equalsIgnoreCase("deliver_section")){
						int assignmentId = getIntParamValue(eBean, "assignmentid", -1);
						String sectionId = getParamValue(eBean, "sectionid");
						int sectionOrder = getIntParamValue(eBean, "sectionorder", -1);
						int itemOrder = getIntParamValue(eBean, "itemorder", -1);
						String time = getParamValue(eBean, "time");
						String responses = eBean.getChildText("responses");
						String scores = eBean.getChildText("scores");
						result = qvLMS.deliverSection(assignmentId, sectionId, sectionOrder, itemOrder, time, responses, scores);
					} else if (actionName.equalsIgnoreCase("save_section")){
						int assignmentId = getIntParamValue(eBean, "assignmentid", -1);
						String sectionId = getParamValue(eBean, "sectionid");
						int sectionOrder = getIntParamValue(eBean, "sectionorder", -1);
						int itemOrder = getIntParamValue(eBean, "itemorder", -1);
						String time = getParamValue(eBean, "time");
						String responses = eBean.getChildText("responses");
						result = qvLMS.saveSection(assignmentId, sectionId, sectionOrder, itemOrder, time, responses);
					} else if (actionName.equalsIgnoreCase("save_section_teacher")){
						int assignmentId = getIntParamValue(eBean, "assignmentid", -1);
						String sectionId = getParamValue(eBean, "sectionid");
						String responses = eBean.getChildText("responses");
						String scores = eBean.getChildText("scores");
						result = qvLMS.saveSectionTeacher(assignmentId, sectionId, responses, scores);
					} else if (actionName.equalsIgnoreCase("save_time")){
						int assignmentId = getIntParamValue(eBean, "assignmentid", -1);
						String sectionId = getParamValue(eBean, "sectionid");
						String time = getParamValue(eBean, "time");
						result = qvLMS.saveTime(assignmentId, sectionId, time);
					} else if (actionName.equalsIgnoreCase("add_message")){
						int assignmentId = getIntParamValue(eBean, "assignmentid", -1);
						String sectionId = getParamValue(eBean, "sectionid");
						String itemId = getParamValue(eBean, "itemid");
						String userId = getParamValue(eBean, "userid");
						String message = eBean.getChildText("message");
						result = qvLMS.addMessage(assignmentId, sectionId, itemId, userId, message);
					} else if (actionName.equalsIgnoreCase("get_messages")){
						int assignmentId = getIntParamValue(eBean, "assignmentid", -1);
						String sectionId = getParamValue(eBean, "sectionid");
						result = qvLMS.getMessages(assignmentId, sectionId);
					} else {
						result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
						result+= "<bean id=\""+actionName+"\">";
						result+= " <param name=\"error\" value=\""+edu.xtec.qv.lms.QVLMSActions.ERROR_BEAN_NOT_DEFINED+"\"/>";
						result+= "</bean>";
					}
				}
			}
		} catch (Exception ex){
			ex.printStackTrace(System.err);
		}
		return result;
	}
	
	private static String getParamValue(Element eBean, String paramName){
		String paramValue = null;
		//Iterator it = eBean.getDescendants("param");
		Iterator it = eBean.getChildren("param").iterator();
		boolean bFound = false;
		while (!bFound && it.hasNext()){
			Element e = (Element)it.next();
			String name = e.getAttributeValue("name");
			if (name.equalsIgnoreCase(paramName)){
				bFound = true;
				paramValue = e.getAttributeValue("value");
			}
		}		
		return paramValue;
	}
	
	private static int getIntParamValue(Element eBean, String paramName, int defaultValue){
		int paramValue = defaultValue;
		String value = getParamValue(eBean, paramName);
		if (value!=null){
			try{
				paramValue = Integer.parseInt(value);
			} catch (Exception ex){
				System.out.println("No s'indica el paràmetre "+paramName);
				//ex.printStackTrace(System.out);
			}
		}
		return paramValue;
	}
	
	public static String callRemoteService(String sService, String sXML, String sEncoding){
		String sResult = "";
		try{
			// Connect to the service
			URL uService = new URL(sService);
			HttpURLConnection uConnection = (HttpURLConnection)uService.openConnection();
			uConnection.setRequestProperty("Content-Length", Integer.toString(sXML.length()) );
			uConnection.setRequestProperty("Content-Type","text/xml;charset:"+sEncoding+";");
			uConnection.setDoInput(true);
			uConnection.setDoOutput(true);
			uConnection.connect();
	
			// Create a writer to the url
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(uConnection.getOutputStream(), sEncoding));
	
			// Send the content to the server
			writer.println(sXML);
			writer.flush();
	
			// Get a reader from the url
			BufferedReader reader = new BufferedReader(new InputStreamReader(uConnection.getInputStream(), sEncoding));
			String line = reader.readLine();
			while( line != null ) {
				sResult+=line;
				line = reader.readLine();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		
		
//		// Connect to the servlet
//		URL uServlet = new URL(sService);
//		URLConnection ucServlet = uServlet.openConnection();
//
//		// Inform the connection that we will send output and accept input
//		ucServlet.setDoInput(true);
//		ucServlet.setDoOutput(true);
//
//		// Don't use a cached version of URL connection.
//		ucServlet.setUseCaches (false);
//		ucServlet.setDefaultUseCaches (false);
//
//		// Specify the content type that we will send binary data
//		ucServlet.setRequestProperty("Content-Type", "text/xml");
//		// Get input and output streams on servlet
//		ucServlet.
//
//		// send your data to the servlet
		return sResult;		
	}
	
	public /*static*/ String callService(String sService, String sXML, String sEncoding){
		if (sService.startsWith("http"))
			return callRemoteService(sService, sXML, sEncoding);
		else
			return callLocalService(sService, sXML, sEncoding);
	}
	
	public static void main(String[] args){
		String sURL = "http://phobos.xtec.cat/dpllphp1/moodle/moodle16/mod/qv/action/beans.php";
		String sXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<bean id=\"get_sections\" >\n <param name=\"assignmentid\" value=\"1\" />\n</bean>";
		CorrectQVApplet c = new CorrectQVApplet(); 
		String sResult=c.callService(sURL, sXML);
		System.out.println("result="+sResult);
	}
	
	//public String getResultsSummary(int groupId){
	public String getResultsSummary(String groupName){
		StringBuffer sbResult = new StringBuffer();
		try{
			//A Internet Explorer 6 no es reben bé els arrays, es retorna la matriu de la forma m[0][0]:m[0][1]:...m[0][n]|m[1][0]:m[1][1]..m[1][n]|....|m[t][0]..m[t][n]
			edu.xtec.qv.lms.LocalFileQVLMS lf = (edu.xtec.qv.lms.LocalFileQVLMS)getQVLMSActions();
			edu.xtec.qv.lms.data.QV qv = lf.getQV(-1);
			//Object[][] resultsSummary = edu.xtec.qv.lms.report.QVResultsGenerator.getResultsSummary(this, qv, groupId);
			Object[][] resultsSummary = edu.xtec.qv.lms.report.QVResultsGenerator.getResultsSummary(this, qv, groupName);
			if (resultsSummary!=null){
				for (int i=0;i<resultsSummary.length;i++){
					if (i>0)
						sbResult.append('!');
					Object[] studentSummary = resultsSummary[i];
					System.out.println("--------------------------");
					if (studentSummary!=null){
						for (int j=0; j<studentSummary.length - 3; j++){
							System.out.println(studentSummary[j]);
							if (j>0)
								sbResult.append(';');
							sbResult.append((String)studentSummary[j]);
						}
					} else {
						System.out.println("!!!!!!!!!!!!!!	studentSummary=null!!!! i="+i);
					}
				}
			} else {
				System.out.println("No hi ha estudiants");
			}
		} catch (Exception ex){
			ex.printStackTrace(System.err);
		}
		//return resultsSummary;
		return sbResult.toString();
	}
	
	//public String downloadCSVResultReport(int groupId){
	public String downloadCSVResultReport(String groupName){
		String result = "";
		try{
			edu.xtec.qv.lms.report.qtidata.QTIAssessmentInfo ai = edu.xtec.qv.lms.report.qtidata.QTIAssessmentInfo.getQTIAssessmentInfo(getInputStream(getXMLFilename()));
			edu.xtec.qv.lms.LocalFileQVLMS lf = (edu.xtec.qv.lms.LocalFileQVLMS)getQVLMSActions();
			edu.xtec.qv.lms.data.QV qv = lf.getQV(-1);
			//result = edu.xtec.qv.lms.report.ReportDownloader.downloadCSVResultReport(this, ai, qv, groupId);
			result = edu.xtec.qv.lms.report.ReportDownloader.downloadCSVResultReport(this, ai, qv, groupName);
			System.out.println("result:"+result);
		} catch (Exception ex){
			ex.printStackTrace(System.err);
		}
		return result;
	}
	
	public String downloadAll(){
		String result = "";
		try{
			edu.xtec.qv.installer.QVExporter.exportAll(this);
		} catch (Exception ex){
			ex.printStackTrace(System.err);
		}
		return result;			
	}
	
	///// Carregar i desar fitxers mitjançant Thread amb privilegis.
	//TODO: Aquest codi s'ha de migrar a l'InvokerThreadWaiter.
				
	public boolean doSave = false;

	public void setDoSave(boolean b){
		doSave = b;
	}
	
	public boolean getDoSave(){
		return doSave;
	}

	public class FileSaver extends Thread{
		boolean end = false;

		FileSaver(){
			if (prop==null)
				loadProperties();
		}

		public void run(){
			while(!end){
				try{
					if (prop==null)
						loadProperties();
					if (getDoSave()){
						if (prop==null)
							loadProperties();
						System.out.println("FileSaver: vaig a desar");
						setDoSave(false);
						saveProperties();
						System.out.println("FileSaver: fet");
					}
					sleep(1000);
				} catch (Exception ex){
					ex.printStackTrace(System.err);
				}
			}
		}

	}
}

/**
 * @author sarjona
 */
class XMLFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File fDir, String sName) {
		boolean bAccept = false;
		if (fDir!=null){
			File fFile = new File(fDir, sName);
			bAccept = fFile!=null && fFile.exists() && fFile.getName().endsWith(".xml");
		}
		return bAccept;
	}

}