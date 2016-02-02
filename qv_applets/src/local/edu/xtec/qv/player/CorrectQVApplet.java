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
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
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

	public void init() {
		
	}

	public void start() {
		//if (bDebug && bTrace) System.out.println("start...... ");
		
		open(getXMLFilename());
		getInputStream(getXMLFilename());
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
				if (sFile.startsWith("http:") || sFile.startsWith("https:")){
					if (sFile.indexOf("?")<0) sFile+="?";
					else sFile+="&";
					sFile+="random="+(new Date().getTime())+Math.round(Math.random()*100);
					is = new URL(sFile).openStream();
				}else{
					is = new FileInputStream(new File(sFile));
				}
                bA=readInputStream(is,1024);
                //hmInputStream.put(sFile, bA);
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
	 * Obt� el document XML associat al reader indicat
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
	

/*************************************************
 * Call service
 **************************************************/
	
	public static String callService(String sService, String sXML){
		return callService(sService, sXML, "UTF-8");
	}
	
	public static String callService(String sService, String sXML, String sEncoding){
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
	
	public static void main(String[] args){
		String sURL = "http://phobos.xtec.cat/dpllphp1/moodle/moodle16/mod/qv/action/beans.php";
		String sXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<bean id=\"get_sections\" >\n <param name=\"assignmentid\" value=\"1\" />\n</bean>";
		String sResult=callService(sURL, sXML);
		System.out.println("result="+sResult);
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