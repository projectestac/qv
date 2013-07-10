package edu.xtec.qv.player.cb;

import java.applet.Applet;
import java.awt.Event;
import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLDecoder;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;

public abstract class LocalQVApplet extends Applet {
	
	public static final String ENCODING = "ISO-8859-1"; 
	protected String file;
	protected static boolean bDebug = true;
	protected static boolean bTrace = true;
	
	protected String sAssessment;
	protected String sSection;
	protected String sGroup;
	protected Document doc;
	
	public void init() {
	}

	public void start() {
	}
	
	public void paint(Graphics g) {
		try{
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	protected String getGroupName(){
		String sGroup = this.getParameter("group");
		if (bDebug && bTrace) System.out.println("group="+sGroup);
		return sGroup;
	}
	
	public boolean action(Event evt, Object arg) {
		return true;
	}
	
	
/*************************************************
 * XML (open, save...)
 **************************************************/	

	public File getDirBase(){
		File fDir = new File(getDocumentBase().getFile()).getParentFile();
		fDir=removeSpace(fDir);
		return fDir;
	}
	
	protected File removeSpace(File fFile){
		String sFile = removeSpace(fFile.toString());
		fFile = new File(sFile);
		return fFile;
	}
	
	protected String removeSpace(String sFile){
		if (sFile.indexOf("%20")>=0) {
			sFile = sFile.replaceAll("%20"," ");
		}	
		return sFile;
	}	

	public File getDataDir(){
		File fDir = new File(getDocumentBase().getFile()).getParentFile();
		File fDataDir = new File(fDir+File.separator+"data");
		fDataDir=removeSpace(fDataDir);
		return fDataDir;
	}
	
	public String getFile(){
		if (file==null && getUser()!=null){
			File fDir = getDataDir();
			if (getGroup()!=null) fDir = new File(fDir+File.separator+filter(getGroup()));
			// Creates dir if not exists
			if (!fDir.exists()) fDir.mkdirs();
			// Get file
			String sFilename = filter(getUser())+"_"+filter(getSurname())+"_res.xml";
			file = fDir+File.separator+sFilename;
			if (bDebug && bTrace) System.out.println("LocalQVApplet.getFile()-> "+file);
		}
		return removeSpace(file);
	}
	
/*************************************************
 * Util
 **************************************************/
		
	protected boolean isNull(Object oValue){
		boolean bNull = oValue==null;
		if (!bNull){
			if (oValue instanceof String){
				String sValue = (String)oValue;
				bNull = bNull || sValue.trim().length()==0 || sValue.equalsIgnoreCase("null");
			}
		}
		return bNull;
	}

	protected static String filter(String input){
		String result=(input==null ? "" : input);
		if(input!=null && input.length()>0){
			StringBuffer filtered = new StringBuffer(input.length());
			char c;
			for(int i=0; i<input.length(); i++) {
				c = input.charAt(i);
				String s=null;
				switch(c){
					case ' ': s="_";break;
					case '+': s="_";break;
					case '<': s="";	break;
					case '>': s="";	break;
					case '"': s="";	break;
					case '\'': s="";break;
					case '´': s=""; break;
					case '`': s=""; break;
					case '¨': s=""; break;
					case '&': s="";	break;
					case '\\': s="";break;
					case '/': s="";	break;
					case ':': s="";	break;
					case '*': s="";	break;
					case '¿': s="";	break;
					case '?': s="";	break;
					case '!': s="";	break;
					case '$': s=""; break;
					case 'ç': s="c"; break;
					case 'Ç': s="C"; break;
					case 'ñ': s="n"; break;
					case 'Ñ': s="n"; break;
					case 'á': s="a";break;
					case 'à': s="a";break;
					case 'ä': s="a";break;
					case 'é': s="e";break;
					case 'è': s="e";break;
					case 'ë': s="e";break;
					case 'í': s="i";break;
					case 'ì': s="i";break;
					case 'ï': s="i";break;
					case 'ó': s="o";break;
					case 'ò': s="o";break;
					case 'ö': s="o";break;
					case 'ú': s="u";break;
					case 'ù': s="u";break;
					case 'ü': s="u";break;
					case 'Á': s="A";break;
					case 'À': s="A";break;
					case 'Ä': s="A";break;
					case 'É': s="E";break;
					case 'È': s="E";break;
					case 'Ë': s="E";break;
					case 'Í': s="I";break;
					case 'Ì': s="I";break;
					case 'Ï': s="I";break;
					case 'Ó': s="O";break;
					case 'Ò': s="O";break;
					case 'Ö': s="O";break;
					case 'Ú': s="U";break;
					case 'Ù': s="U";break;
					case 'Ü': s="U";break;
				}
				if(s!=null)
					filtered.append(s);
				else
					filtered.append(c);
			}
			result=filtered.substring(0);
		}
		return result.toLowerCase();
	}
	
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
 * Getter/setter
 **************************************************/
	
	public abstract String getUser();

	public abstract String getSurname();
	
	public abstract String getGroup();
	
	
/*************************************************
 * XML Parser
 **************************************************/

	public String getAssessment(){
		if (isNull(this.sAssessment)){
			if (this.getParameter("assessment")!=null) this.sAssessment = this.getParameter("assessment");
		}
		return this.sAssessment;
	}

	protected Element getAssessmentElement(boolean bCreate){
		return getAssessmentElement(getDocument(), bCreate);
	}
	protected Element getAssessmentElement(Document d, boolean bCreate){
		if (d!=null && getAssessment()!=null){
			Iterator itAss = d.getRootElement().getDescendants(new ElementFilter("assessment_result"));
			while (itAss.hasNext()){
				Element eAss = (Element)itAss.next();
				if (getAssessment().equals(eAss.getAttributeValue("ident_ref"))){
					return eAss;
				}
			}
		}
		if (bCreate){
			Element eAss = new Element("assessment_result");
			eAss.setAttribute("ident_ref", getAssessment());
			Element eResult = null;
			if (d==null){
				Element eRoot = new Element("qti_result_report");
				eResult = new Element("result");
				eRoot.addContent(eResult);
				Element eContext = new Element("context");
				eResult.addContent(eContext);
				this.doc= new Document();
				this.doc.addContent(eRoot);				
			}else{
				eResult = d.getRootElement().getChild("result");				
			}
			eResult.addContent(eAss);
			return eAss;
		}
		return null;
	}
		
	protected Element getSectionElement(String sId, boolean bCreate){
		Element eAss = getAssessmentElement(bCreate);
		if (eAss!=null && sId!=null){
			Iterator itSection = eAss.getChildren("section_result").iterator();
			while (itSection.hasNext()){
				Element eSection = (Element)itSection.next();
				if (sId.equals(eSection.getAttributeValue("ident_ref"))){
					return eSection;
				}
			}
		}
		if (bCreate){
			Element eSection = new Element("section_result");
			eSection.setAttribute("ident_ref", getSection());
			eAss.addContent(eSection);
			return eSection;
		}
		return null;
	}
	public String getSection(){
		if (isNull(this.sSection)){
			if (this.getParameter("section")!=null) this.sSection = this.getParameter("section");
		}
		return this.sSection;
	}
	protected Element getSectionElement(boolean bCreate){
		return getSectionElement(getSection(), bCreate);
	}	
	
		
	protected Element getItemElement(String sItemId, boolean bCreate){
		Element eSection = getSectionElement(bCreate);
		if (eSection!=null && sItemId!=null){
			Iterator itItem = eSection.getChildren("item_result").iterator();
			while (itItem.hasNext()){
				Element tmpItem = (Element)itItem.next();
				if (sItemId.equals(tmpItem.getAttributeValue("ident_ref"))){
					return tmpItem;
				}
			}
		}
		if (bCreate){
			Element eItem = new Element("item_result");
			eSection.addContent(eItem);				
			if (sItemId!=null){
				eItem.setAttribute("ident_ref", sItemId);
			}
			return eItem;
		}
		return null;
	}
	
	protected Element getElement(String sId){
		Element e = getSectionElement(sId, false);
		if (e==null){
			e = getItemElement(sId, false);
		}
		return e;
	}
	
	protected Element getOutcomesElement(String sId){
		return getOutcomesElement(sId, false);
	}
	protected Element getOutcomesElement(String sId, boolean bCreate){
		Element eOutcomes = null;
		Element eElem = getElement(sId);
		if (eElem!=null){
			eOutcomes = eElem.getChild("outcomes");
		}
		if (eOutcomes==null && bCreate && eElem!=null){
			eOutcomes = new Element("outcomes");
			eElem.addContent(eOutcomes);
		}
		return eOutcomes;
	}

	protected Element getScoreElement(String sId, String sVarname){
		return getScoreElement(sId, sVarname, false);
	}
	
	protected Element getScoreElement(String sId, String sVarname, boolean bCreate){
		Element eOutcomes = getOutcomesElement(sId, bCreate);
		if (eOutcomes!=null && sVarname!=null){
			Iterator itScore = eOutcomes.getChildren("score").iterator();
			while (itScore.hasNext()){
				Element tmpScore = (Element)itScore.next();
				if (sVarname.equalsIgnoreCase(tmpScore.getAttributeValue("varname"))){
					return tmpScore;
				}
			}
			Element eScore = new Element("score");
			eScore.setAttribute("varname", sVarname);
			eOutcomes.addContent(eScore);
			Element eScoreValue = new Element("score_value");
			eScore.addContent(eScoreValue);
			return eScore;
		}
		return null;
	}

	public String getScore(String sItemId){
		String sScore = null;
		Element eScore = getScoreElement(sItemId, "SCORE");
		if (eScore!=null) {
			sScore = eScore.getChildText("score_value");
		}
		return sScore;
	}

/*************************************************
 * XML (open, save...)
 **************************************************/
		
	public abstract Document getDocument();
	
	/**
	 * @param sPath path del fitxer XML a obrir
	 * @return representacio del document XML que conté el fitxer indicat 
	 * @throws Exception
	 */
	public static Document open(String sPath){
		Document d = null;
		try{
			if (sPath!=null){
				Reader reader = new InputStreamReader(new FileInputStream(sPath), "ISO-8859-1");
				d = getXMLDocument(reader);
				reader.close();
				//d = getXMLDocument(new FileInputStream(sPath));
			}
		} catch (Exception e){
			if (bTrace){
				System.out.println("EXCEPCIO obrint fitxer XML '"+sPath+"' --> "+e);
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
	public static Document getXMLDocument(Reader reader) throws Exception{
		SAXBuilder sb = createSAXBuilder();
		org.jdom.Document doc=sb.build(reader);
		return doc;        
	}
	
	/**
	 * Crea un SAXBuilder
	 * @return
	 * @throws Exception
	 */
	public static SAXBuilder createSAXBuilder() throws Exception{
		SAXBuilder sb=new SAXBuilder(false);
		sb.setValidation(false);
		sb.setExpandEntities(false);
		return sb;
	}
		
	
}


/**
 * @author sarjona
 */
class DirectoryFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File fDir, String sName) {
		boolean bAccept = false;
		if (fDir!=null){
			File fFile = new File(fDir, sName);
			bAccept = fFile!=null && fFile.exists() && fFile.isDirectory();
		}
		return bAccept;
	}

}