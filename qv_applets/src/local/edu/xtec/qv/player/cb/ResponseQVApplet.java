package edu.xtec.qv.player.cb;

import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Panel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format.TextMode;

import edu.xtec.qv.result.QTIResponseParser;
import edu.xtec.qv.result.QTISymtab;
import edu.xtec.qv.result.QualificationSymtabs;

public class ResponseQVApplet extends LocalQVApplet {

	String sUser;
	String sSurname;
	int iSection = -1;
	String sResponses;
	String sScores;
	String sCorrect;

	public void init() {
		//System.out.println("init......");
		getDocument();
		
		// Add buttons
		Panel p = new Panel();
		p.add(new Button("Desa"));
		p.add(new Button("Lliura"));
		p.setBackground(new Color(255, 255, 255));
		this.setBackground(new Color(255, 255, 255));
		add("North",p);
		
	}

	public void start() {
		if (bDebug && bTrace) System.out.println("\nstart...... user="+getUser());
		if (getUser()!=null){
			//System.out.println("responses="+getResponses());
			if (isTeacher()){
				updateScores();
			}else{
				updateResponses();
				correct();				
			}
			System.out.println("start invoca a save");
			save();
			save3("K:/out555.txt", "aaaaaaa");
		}
	}
	
	public void paint(Graphics g) {
	}
	
	public String getAssessmentName(){
		return getDirBase().getName();
	}
	
	public boolean action(Event evt, Object arg) {
		if (arg.equals("Desa")) {
			if (bDebug && bTrace) System.out.println("action desa-> doc?"+(getDocument()!=null));
			if (getDocument()!=null){
				//getText();
				save(file, getDocument().getRootElement());
			}
/*			try {
				FileOutputStream fos = new FileOutputStream(file);
				Writer pw = new BufferedWriter(new OutputStreamWriter(fos));
				pw.write(getText());
				pw.close();
				fos.close();				
			} catch (IOException ioe) {
				System.out.println("IOException...");
				ioe.printStackTrace();
			}	*/
		} else if (arg.equals("Lliura")){
			File fXML = getXML();
			if (bDebug && bTrace) System.out.println("action lliura-> "+fXML);
			try{
				InputStream is = new FileInputStream(fXML);
				//InputStream isQTIXml = getInputStreamForURL();
				HashMap hmFeedback = new HashMap();
				QTIResponseParser qr = new QTIResponseParser(is, hmFeedback);
				QualificationSymtabs qs = new QualificationSymtabs();
				QTISymtab st = qr.getFullPuntuation(getSectionNumber(), "1132751260185406-->1132751228532541=1132751260186479#1132751548767336-->1132751533715271=1132751548767622", qs);
				is.close();
				if (bDebug && bTrace) System.out.println("score="+st.getScore());
				if (bDebug && bTrace) System.out.println("preguntes="+qs.getSectionScoringRepresentation());
				if (bDebug && bTrace) System.out.println("feedbacks="+hmFeedback);
			}catch (Exception e){
				if (bTrace){
					System.out.println("Fitxer de correcció no trobat");
					e.printStackTrace();
				}
			}			
		} else return false;
		return true;
	}	
	
/*************************************************
 * Getter/Setter
 **************************************************/
	
	public void setUser(String sUser){
		this.sUser=sUser;
	}
	public String getUser(){
		if (isNull(this.sUser)){
			if (this.getParameter("user")!=null) sUser = this.getParameter("user");
			else{
				int i = getDocumentBase().toString().indexOf("p_name=");
				if (i>0){
					this.sUser = getDocumentBase().toString().substring(i+7);
				}
			}
		}
		return this.sUser;
	}
	public String getSurname(){
		if (isNull(this.sSurname)){
			if (this.getParameter("surname")!=null) {
				this.sSurname = this.getParameter("surname");
				if (this.sSurname!=null){
					this.sSurname=decode(this.sSurname);
/*					int i = 100;
					while (this.sSurname.indexOf("%")>=0 && i>0){
						this.sSurname=URLDecoder.decode(this.sSurname);
						i--;
					}*/
				}
			}
			else{
				int i = getDocumentBase().toString().indexOf("p_surname=");
				if (i>0){
					int j = getDocumentBase().toString().substring(i+10).indexOf("&");
					if (j>=0) this.sSurname = getDocumentBase().toString().substring(i+10, i+10+j);
					else this.sSurname = getDocumentBase().toString().substring(i+10);
				}
			}
		}
		return this.sSurname;
	}
	public String getGroup(){
		if (isNull(this.sGroup)){
			if (this.getParameter("group")!=null) this.sGroup = this.getParameter("group");
			else{
				int i = getDocumentBase().toString().indexOf("p_group=");
				if (i>0){
					this.sGroup = getDocumentBase().toString().substring(i+8);
				}
			}
		}
		return this.sGroup;
	}
	
	
	public void setAssessment(String sAssessment){
		this.sAssessment=sAssessment;
	}

	protected Element getSectionElement(){
		return getSectionElement(false);
	}	
	public void setSection(String sSection){
		this.sSection=sSection;
	}
	public void setSectionNumber(int iSection){
		this.iSection=iSection;
	}
	public int getSectionNumber(){
		if (this.iSection<0){
			if (this.getParameter("section_number")!=null) {
				String sSectionNumber = this.getParameter("section_number");
				try{
					this.iSection = Integer.parseInt(sSectionNumber);
				}catch (NumberFormatException nfe){
					this.iSection = -1;
				}
			}
		}
		return this.iSection;
	}
	
	public boolean isTeacher(){
		String sTeacher = this.getParameter("teacher");
		return !isNull(sTeacher) && "true".equalsIgnoreCase(sTeacher);
	}
	
	public File getXML(){
		System.out.println("getXML");
		File fXML = null;
		String sXML = this.getParameter("xml");
		if (sXML==null) sXML = getDirBase()+File.separator+"resource"+File.separator+getAssessmentName()+".xml";
		if (sXML!=null) fXML = new File(sXML);
		return fXML;
	}
	
	
/*************************************************
 * Responses
 **************************************************/
						
/*
	public Document openFile(){
		Document d = null;
		File f = new File(getFile());
		if (f.exists()){
			d = open(getFile());		
		}	
		return d;
	}
	
	public void loadResponses(){
		//System.out.println("loadResponses");
		//System.out.println("file="+getFile());
		File f = new File(getFile());
		if (f.exists()){
			this.doc = open(getFile());		
		}
		//System.out.println("responses="+getResponses());
		updateResponses();		
	}
*/
	public void correct(){
		System.out.println("correct");
		File fXML = getXML();
		if (bDebug && bTrace) System.out.println("ResponseQVApplet.correct() -> "+fXML);
		try{
			InputStream is = new FileInputStream(fXML);
			//InputStream isQTIXml = getInputStreamForURL();
			HashMap hmFeedback = new HashMap();
			QTIResponseParser qr = new QTIResponseParser(is, hmFeedback);
			QualificationSymtabs qs = new QualificationSymtabs();
			//QTISymtab st = qr.getFullPuntuation(getSectionNumber(), "1132751260185406-->1132751228532541=1132751260186479#1132751548767336-->1132751533715271=1132751548767622", qs);
			QTISymtab st = qr.getFullPuntuation(getSectionNumber(), getResponses(), qs);
			is.close();
			if (bDebug && bTrace) System.out.println("score="+st.getScore());
			String sScores = qs.getSectionScoringRepresentation();
			if (bDebug && bTrace) System.out.println("scores="+sScores);
			updateScores(sScores);
			if (bDebug && bTrace) System.out.println("feedbacks="+hmFeedback);
		}catch (Exception e){
			if (bTrace){
				System.out.println("Fitxer de correcció no trobat");
				e.printStackTrace();
			}
		}			
		
	}
	
	public void updateScores(){
		System.out.println("updateScores");
		String sScores = this.getParameter("responses");
		updateScores(sScores);
	}
	public void updateScores(String sScores){
		System.out.println("updateScores("+sScores+")");
		StringTokenizer stScores = new StringTokenizer(sScores, "#");
		while (stScores.hasMoreTokens()){
			String sScore = stScores.nextToken();
			int iIndex = sScore.indexOf("=");
			String sId = sScore.substring(0, iIndex);
			String sValue = sScore.substring(iIndex+1);
			if (sId.endsWith("_score")){
				sId=sId.substring(0, sId.indexOf("_score"));
				if (getElement(sId)==null) getItemElement(sId, true);
				setScore(sId, sValue);
			} else if (sId.endsWith("_correct")){
				sId=sId.substring(0, sId.indexOf("_correct"));
				if (getElement(sId)==null) getItemElement(sId, true);
				setCorrect(sId, sValue);
			}
		}
	}

	
	public String getVarnameScores(String sVarname){
		StringBuffer sbValue = new StringBuffer();
		Element eSection = getSectionElement();
		if (eSection!=null){
			Iterator itItems = eSection.getChildren("item_result").iterator();
			while (itItems.hasNext()){					
				Element eItem = (Element)itItems.next();
				String sItemId = eItem.getAttributeValue("ident_ref");
				Element eScore = getScoreElement(sItemId, sVarname);
				if (eScore!=null){
					sbValue.append(sItemId+"_"+sVarname.toLowerCase()+"="+eScore.getChildText("score_value")+"#");
				}
			}
		}
		return sbValue.toString();
	}

	public String getCorrects(){
		if (isNull(this.sCorrect)){
			this.sCorrect = getVarnameScores("CORRECT");
		}
		return this.sCorrect;
	}

	public String getScores(){
		if (isNull(this.sScores)){
			this.sScores="";
			Element eSection = getSectionElement();
			if (eSection!=null){
				Iterator itItems = eSection.getChildren("item_result").iterator();
				while (itItems.hasNext()){					
					Element eItem = (Element)itItems.next();
					String sItemId = eItem.getAttributeValue("ident_ref");
					Element eScore = getScoreElement(sItemId, "SCORE");
					if (eScore!=null){
						this.sScores+=sItemId+"_score="+eScore.getChildText("score_value")+"#";
					}
				}
			}
		}
		return this.sScores;
	}

	public void setScore(String sId, String sValue){
		//System.out.println("setScore-> "+sId+"="+sValue);
		Element eScore = getScoreElement(sId, "SCORE", true);
		if (eScore!=null){
			Element eScoreValue = eScore.getChild("score_value");
			if (eScoreValue!=null){
				eScoreValue.setText(sValue);
			}
		}
	}
	public void setCorrect(String sId, String sValue){
		//System.out.println("setCorrect-> "+sId+"="+sValue);
		Element eScore = getScoreElement(sId, "CORRECT", true);
		if (eScore!=null){
			Element eScoreValue = eScore.getChild("score_value");
			if (eScoreValue!=null){
				eScoreValue.setText(sValue);
			}
		}
	}

	public String getResponses(){
		System.out.println("getResponses");
		if (this.sResponses==null){
			this.sResponses = this.getParameter("responses");
			if (isNull(this.sResponses)){
				this.sResponses="";
				Element eSection = getSectionElement();
				if (eSection!=null){
					Iterator itItems = eSection.getChildren("item_result").iterator();
					while (itItems.hasNext()){
						Element eItem = (Element)itItems.next();
						Iterator itResponse = eItem.getChildren("response").iterator();
						while (itResponse.hasNext()){
							Element eResponse = (Element)itResponse.next();
							Iterator itValue = eResponse.getChildren("response_value").iterator();
							while (itValue.hasNext()){
								Object oValue = itValue.next();
								String sValue = "";
								try{
									if (oValue instanceof CDATA){
										CDATA cValue = (CDATA)oValue;
										sValue=cValue.getText();
									}else{
										Element eValue = (Element)oValue;
										sValue = eValue.getText();
									}
								}catch(Exception e){
									e.printStackTrace();
								}
								this.sResponses+=eItem.getAttributeValue("ident_ref")+"-->"+eResponse.getAttributeValue("ident_ref")+"="+sValue+"#";
							}
						}
					}
				}
			}else{
				System.out.println("DECODING responses-> "+this.sResponses);
				this.sResponses = decode(this.sResponses);
			}
		}
		if (bTrace && bDebug) System.out.println("getResponses-> "+this.sResponses);
		return sResponses;
	}

	public void updateResponses(){
		StringTokenizer stResponses = new StringTokenizer(getResponses(), "#");
		Hashtable hResp = new Hashtable();
		while (stResponses.hasMoreTokens()){
			String sResp = stResponses.nextToken();
			int iIndex = sResp.indexOf("=");
			String sRespKey = sResp.substring(0, iIndex);
			String sRespId = sResp.substring(iIndex+1);
			if (!hResp.containsKey(sRespKey)) {
				resetResponse(sRespKey);
			}
			setResponse(sRespKey, sRespId);
			hResp.put(sRespKey, sRespId);			
		}
	}
	
	
	public String getResponse(String sKey){
		String sResponse = null;
		//System.out.println("getResponse-> "+sKey);
		Element eResponse = getResponseElement(sKey);
		if (eResponse!=null) sResponse = eResponse.getChildText("response_value");
		return sResponse;
	}
	
	public void setResponse(String sKey, String sValue){
		if (bTrace && bDebug) System.out.println("setResponse-> key="+sKey+" value="+sValue+"   item="+getItemId(sKey)+"  response="+getResponseId(sKey));
		Element eResponse = getResponseElement(sKey, true);
		if (eResponse!=null){
			Element eResp = new Element("response_value");
			eResponse.addContent(eResp);
			String sRespId = getResponseId(sKey);
			if (sRespId!=null && "INFO".equals(sRespId)){
				CDATA cResp = new CDATA("response_value");
				cResp.setText(sValue);
				eResp.addContent(cResp);
			}else{
				eResp.setText(sValue);
			}
		}
	}
	public void resetResponse(String sKey){
		if (bTrace && bDebug) System.out.println("resetResponse-> item="+getItemId(sKey)+"  response="+getResponseId(sKey));
		Element eResponse = getResponseElement(sKey, true);
		if (eResponse!=null){
			eResponse.removeChildren("response_value");
		}	
	}
		
	protected Element getAssessmentElement(){
		return getAssessmentElement(false);
	}
	
	private Element getResponseElement(String sKey){
		return getResponseElement(sKey, false);
	}
	
	private Element getResponseElement(String sKey, boolean bCreate){
		Element eSection = getSectionElement(bCreate);
		Element eItem = null;
		if (eSection!=null && sKey!=null){
			Iterator itItem = eSection.getChildren("item_result").iterator();
			while (itItem.hasNext()){
				Element tmpItem = (Element)itItem.next();
				if (getItemId(sKey).equals(tmpItem.getAttributeValue("ident_ref"))){
					eItem = tmpItem;
					Iterator itResponses = eItem.getChildren("response").iterator();
					while (itResponses.hasNext()){
						Element eResp = (Element)itResponses.next();
						if (getResponseId(sKey).equals((eResp.getAttributeValue("ident_ref")))){
							return eResp;
						}
					}
					break;
				}
			}
		}
		if (bCreate){
			if (eItem==null){
				eItem = new Element("item_result");
				eItem.setAttribute("ident_ref", getItemId(sKey));
				eSection.addContent(eItem);
			}
			Element eResp = new Element("response");
			eResp.setAttribute("ident_ref", getResponseId(sKey));
			eItem.addContent(eResp);
			return eResp;
		}
		return null;
	}
	
	
	private String getItemId(String sKey){
		String sId = "";
		if (sKey!=null){
			StringTokenizer st = new StringTokenizer(sKey, "--&gt;");
			if (st.hasMoreTokens()) sId = st.nextToken();
		}
		return sId;
	}
	
	private String getResponseId(String sKey){
		String sId = "";
		if (sKey!=null){
			int i = sKey.indexOf("--");
			sId = sKey.substring(i+3);
		}
		return sId;
	}
	
/*************************************************
 * XML (open, save...)
 **************************************************/
	
	public Document getDocument(){
		System.out.println("getDocument");
		//System.out.println("ResponseQVApplet.getDocument()");
		if (this.doc==null && getFile()!=null){
			File f = new File(getFile());
			System.out.println("new File");
			if (f.exists()){
				System.out.println("exists");
				this.doc = open(getFile());
				System.out.println("cridat open(getFile())");
			}			
		}
		return this.doc;
	}

    
	public boolean save(){
		System.out.println("save");
		if (this.file!=null && getDocument()!=null){
			return save(this.file, getDocument().getRootElement());			
		}
		return false;
	}
	
	
	/**
	 * Guarda l'element XML al fitxer indicat
	 * @param sURL path del fitxer on es guardarà l'element XML
	 * @param eElement element XML a guardar
	 * @return false si es produeix algun error guardant el fitxer XML; true en cas contrari
	 */
	public static boolean save(String sURL, Element eElement){
		System.out.println("-save");
		sURL="k:/out3.txt";
		boolean bOk = true;
		try{
			if (bDebug && bTrace) System.out.println("save-> "+sURL+"  elem?"+(eElement!=null));
			if (sURL!=null && eElement!=null){
				File fFile = new File(sURL);
				if (!fFile.exists()){
					System.out.println("!exists");
					fFile.createNewFile();
				} 
				System.out.println("--");
				FileOutputStream fos=new FileOutputStream(sURL);
				Format oFormat = Format.getPrettyFormat();
	            oFormat.setEncoding("ISO-8859-1");
				oFormat.setTextMode(TextMode.NORMALIZE);
				XMLOutputter serializer = new XMLOutputter(oFormat);
				serializer.output(eElement, fos);
				fos.getFD().sync();
				fos.flush();
				fos.close();
				System.out.println("close");
			}
		}
		catch (IOException e){
			if (bTrace){
				System.out.println("EXCEPCIO guardant el fitxer '"+sURL+"' --> "+e);
				e.printStackTrace();
			}
			bOk = false;
		}
		return bOk;
	}
    	
	public static boolean save3(String sURL, String text){
		System.out.println("-save3");
		boolean bOk = true;
		try{
			if (bDebug && bTrace) System.out.println("save-> "+sURL);
			if (sURL!=null){
				File fFile = new File(sURL);
				if (!fFile.exists()){
					System.out.println("!exists");
					fFile.createNewFile();
				} 
				System.out.println("--");
				FileOutputStream fos=new FileOutputStream(sURL);
				java.io.PrintWriter pw=new java.io.PrintWriter(fos);
				pw.println(text);
				fos.getFD().sync();
				fos.flush();
				fos.close();
				System.out.println("close");
			}
		}
		catch (Exception e){
			if (bTrace){
				System.out.println("EXCEPCIO guardant el fitxer '"+sURL+"' --> "+e);
				e.printStackTrace();
			}
			bOk = false;
		}
		return bOk;
	}
    	

	
	/*************************************************
	 * Login
	 **************************************************/
		public static final String LOGIN_NUM_USERS_PROPERTY_NAME="numUsers";
		public static final String LOGIN_PREFIX_USERS_PROPERTY_NAME="prefixUsers";
		public static final String LOGIN_USERID_PROPERTY_NAME="userId";
		public static final String LOGIN_USERNAME_PROPERTY_NAME="userName";
		public static final String LOGIN_USERIT_PROPERTY_NAME="userIT";
		public static final String LOGIN_USERNE_PROPERTY_NAME="userNE";
		
		public java.util.Properties prop = null;
		
		private String getLoginProperty(String property){
			String sProp = null;
			if (prop==null)
				loadProperties();
			if (prop!=null)
				sProp = prop.getProperty(property);
			return sProp;
		}
		
		public void setLoginProperty(String propertyName, String value){
			System.out.println("setLoginProperty("+propertyName+","+value+")");
			if (prop==null)
				loadProperties();
			if (prop!=null){
				System.out.println("put");
				prop.put(propertyName, value);
				System.out.println("abans save");
				saveProperties();
				System.out.println("despres save");
			}
				
		}
		
		private void loadProperties(){
			File dataDir = getDataDir();
			prop = new java.util.Properties();
			try{
				String filePath = removeSpace(dataDir.getAbsolutePath()+File.separator+"login.properties");
				FileInputStream fis = new FileInputStream(filePath);
				System.out.println("Carregant fitxer de login des de: "+filePath);
				prop.load(fis);
				fis.close();
			} catch (Exception ex){
				ex.printStackTrace(System.out);
				prop = null;
			}
		}
		
		private void saveProperties(){
			File dataDir = getDataDir();
			prop = new java.util.Properties();
			try{
				String filePath = removeSpace(dataDir.getAbsolutePath()+File.separator+"login2.xml");
				filePath = "K:/out.txt";
				System.out.println("**filePath:"+filePath);
				File fFile = new File(filePath);
				System.out.println("-0-");
				if (!fFile.exists()){
					System.out.println("-1-");
					fFile.createNewFile();
					System.out.println("-2-");
				}
				System.out.println("-3-");
				FileOutputStream fos=new FileOutputStream(filePath);
				System.out.println("-4-");
				fos.getFD().sync();
				System.out.println("-5-");
				fos.flush();
				System.out.println("-6-");
				fos.close();
				System.out.println("-7-");
				
				
				//java.io.FileOutputStream fos = new java.io.FileOutputStream(filePath);
				//System.out.println("Desant fitxer de login a: "+filePath);
				//prop.store(fos, "QV Login properties file");
				//fos.close();
			} catch (Exception ex){
				ex.printStackTrace(System.out);
				prop = null;
			}
		}
		
		public File getDataDir(){
			File fDir = new File(getDocumentBase().getFile()).getParentFile();
			File fDataDir = new File(fDir+File.separator+"data");
			return fDataDir;
		}
		
		public String getNumUsers(){
			return getLoginProperty(LOGIN_NUM_USERS_PROPERTY_NAME);
		}
		
		public String getPrefixUsers(){
			return getLoginProperty(LOGIN_PREFIX_USERS_PROPERTY_NAME);
		}
		
		public String getUserId(int userNum){
			return getLoginProperty(LOGIN_USERID_PROPERTY_NAME+userNum);
		}
		
		public String getUserName(int userNum){
			return getLoginProperty(LOGIN_USERNAME_PROPERTY_NAME+userNum);
		}
		
		public String getUserIT(int userNum){
			return getLoginProperty(LOGIN_USERIT_PROPERTY_NAME+userNum);
		}
		
		public String getUserNE(int userNum){
			return getLoginProperty(LOGIN_USERNE_PROPERTY_NAME+userNum);
		}
		
	
	
}
