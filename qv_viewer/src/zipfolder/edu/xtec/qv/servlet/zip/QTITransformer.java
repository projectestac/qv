package edu.xtec.qv.servlet.zip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;



public class QTITransformer {
	
	public final static String PROPERTIES_PATH = "/edu/xtec/resources/properties/";
	public final static String CONF_FILE = "qv_transformer.properties";
	public final static String DEFAULT_ENCODING = "ISO-8859-15";
	
	public static HashMap hDoc = new HashMap();
	protected static Properties prop;
	
	static{
        try{
            prop=new Properties();
            prop.load(QTITransformer.class.getResourceAsStream(PROPERTIES_PATH+CONF_FILE));
        }catch (Exception e){
        	System.err.println("ERROR getting "+CONF_FILE+"!");
        }
	}
	
	protected static void qti2html(Source oXMLSource, Source oXSLSource, String sOut, HashMap oMap){
		if (sOut!=null){
			try{
				File f=new File(sOut);
				if (!f.getParentFile().exists()) f.getParentFile().mkdirs();
				FileOutputStream fos = new FileOutputStream(f);
				qti2html(oXMLSource, oXSLSource, fos, oMap);
				fos.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	protected static Hashtable getXSLImports(){
		Hashtable hXSL = new Hashtable();
		StringTokenizer stXSL = new StringTokenizer(prop.getProperty("transform.xsl.import"), "$$");
		while (stXSL.hasMoreTokens()){
			String sKey = stXSL.nextToken();
			if (stXSL.hasMoreTokens()){
				String sValue = stXSL.nextToken();
				hXSL.put(sKey, sValue);
			}
		}
		return hXSL;
	}
	
	protected static void qti2html(Source oXMLSource, Source oXSLSource, OutputStream os, HashMap oMap){
		try{
			TransformerFactory tf = new org.apache.xalan.processor.TransformerFactoryImpl();
			tf.setURIResolver(new QVURIResolver(getXSLImports()));
			Transformer tr = tf.newTransformer(oXSLSource);
            tr.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
			if (oMap!=null && !oMap.isEmpty()){
				Iterator itKeys = oMap.keySet().iterator();
				while (itKeys.hasNext()){
					String sName = (String)itKeys.next();
					String sValue = (String)oMap.get(sName);
		            tr.setParameter(sName, sValue);					
				}
			}
			
			if (os!=null){
				Result output = new StreamResult(os);
				tr.transform(oXMLSource, output);						
			}
		}catch (Exception e){
			error("EXCEPTION qti2html()-> "+e);
			e.printStackTrace();
		}
	}
	
	
	public static Document getDocument(String sXML){
		Document doc = null;
		//debug("QTITransformer.getDocument-> xml="+sXML);
		try{
			if (hDoc.containsKey(sXML)){
				doc = (Document)hDoc.get(sXML);
			}else{
		        SAXBuilder builder = new SAXBuilder();
		        if (sXML.startsWith("http")){
		        	URL uXML = new URL(sXML);
		        	doc = builder.build(uXML);		        	
		        }else{
		        	File fXML = new File(sXML);
		        	doc = builder.build(fXML);
		        }
				// TODO: To revise cache docs (there is possible to have memory problems in the future???)
				//hDoc.put(sXML, doc);
			}
		}catch (Exception e){
			error("EXCEPTION getDocument()-> "+e);			
		}
		return doc;
	}
	
	public static Vector getSections(Element eRoot){
		Vector vSections = new Vector();
		Iterator it = eRoot.getDescendants(new ElementFilter("section"));
		int i = 1;
		while (it.hasNext()){
			Element eSection = (Element)it.next();
			boolean isRandomizableSection = isRandomizableSection(eRoot, eSection.getAttributeValue("ident"));
			Section oSection = new Section(i++, eSection.getAttributeValue("ident"), eSection.getAttributeValue("title"), isRandomizableSection);
			vSections.addElement(oSection);
		}
		return vSections;
	}
	
	private static boolean isRandomizableSection(Element eRoot, String sectionId){//Albert
		boolean isRandomizableSection = false;
		//Iterator it = eRoot.getDescendants(new ElementFilter("selection_metadata"));
		Iterator it = eRoot.getDescendants(new ElementFilter("assessment"));
		while (it.hasNext() && !isRandomizableSection){
			Element eAssessment = (Element)it.next();
			List l = eAssessment.getChildren("selection_ordering");
			if (l!=null && l.size()>0){ //Només pot ser 1	
				Element eSelectionOrdering = (Element)l.get(0);
				if (eSelectionOrdering!=null){
					Iterator it2 = eSelectionOrdering.getDescendants(new ElementFilter("selection_metadata"));
					while (it2.hasNext() && !isRandomizableSection){					
						Element eSelectionMetadata = (Element)it2.next();
						String mdName = eSelectionMetadata.getAttributeValue("mdname");
						String mdOperator = eSelectionMetadata.getAttributeValue("mdoperator");
						String value = eSelectionMetadata.getText();
						if (mdName!=null && mdOperator!=null && value!=null && 
						  mdName.equalsIgnoreCase("ident") && mdOperator.equalsIgnoreCase("EQ") &&
						  value.equalsIgnoreCase(sectionId)){
							isRandomizableSection = true;	
						}
					}
				}
			}
		}
		return isRandomizableSection;
	}
	
	protected static Element createAssignment(Element root, Vector vSections){
		Element eAssignments = new Element("quadern_assignments");
		Element eAssignment= new Element("assignment");
		eAssignments.addContent(eAssignment);
		List l = root.getChildren("assessment");
		if (!l.isEmpty()){
			Element eAssessment = (Element)l.get(0);			
			eAssignment.setAttribute("name", eAssessment.getAttributeValue("title"));
			eAssignment.setAttribute("ident", eAssessment.getAttributeValue("ident"));
		}
		
		if (vSections!=null){
			Element eSections = new Element("sections");
			Enumeration enumSections = vSections.elements();
			while (enumSections.hasMoreElements()){			
				Section oFull=(Section)enumSections.nextElement();
				eSections.addContent(oFull.toXML());
			}
			eAssignment.addContent(eSections);
		}
		return eAssignments;
	}
	
	public static Source getSource(String sPath){
		Source oSource = null;
		try{
			InputStream is = null;
	        if (sPath.startsWith("http")){
	        	URL uPath = new URL(sPath);
	        	is = uPath.openStream();
	        }else{
	        	is = new FileInputStream(sPath);
	        }
			oSource = getSource(is);
		}catch (Exception e){
			error("EXCEPTION getSource('"+sPath+"')-> "+e);
		}
		return oSource;
	}
	
	protected static Source getSource(InputStream is){
		Source oSource = null;
		try{
			oSource = new javax.xml.transform.stream.StreamSource(is);
		}catch (Exception e){
			error("EXCEPTION getSource()-> "+e);
		}
		return oSource;
	}
	
	protected static void debug(String sMessage){
		System.out.println("DEBUG - "+sMessage);
	}

	protected static void error(String sMessage){
		System.out.println("ERROR - "+sMessage);
	}
	
	protected static String getDefaultSkin(){
		return "default";
	}
	
	public static String getXSL(String sSkin){
		String sXSL = "";
		if (prop!=null && prop.containsKey("skin."+sSkin+".xsl")){
			sXSL = prop.getProperty("skin."+sSkin+".xsl");
		}else{
			sXSL = prop.getProperty("skin.default.xsl"); 
		}
		return sXSL;
	}
	
	public static Hashtable createHTML(String sXML, String sSkin){
		return createHTML(sXML, sSkin, null, new HashMap());
	}
	
	public static Hashtable createHTML(String sXML, String sSkin, HashMap oMap){
		return createHTML(sXML, sSkin, null, oMap);
	}
	
	/**
	 * 
	 * @param sXML
	 * @param sSkin
	 * @param sBase
	 * @return
	 */
	public static Hashtable createHTML(String sXML, String sSkin, String sBase, HashMap oMap){
		return createHTML(sXML, sSkin, sBase, -1, oMap);
	}	

	/**
	 * 
	 * @param sXML
	 * @param sSkin
	 * @param sBase
	 * @param iSection -1: create all; 0: create index; otherwise create iSection section
	 * @return
	 */
	public static Hashtable createHTML(String sXML, String sSkin, String sBase, int iSection, HashMap oMap){
		Hashtable hHTML = new Hashtable();
		try{
			String sXSL = getXSL(sSkin);
			Document doc = getDocument(sXML);
			Element eRoot = doc.getRootElement();
			Vector vSections = getSections(eRoot);
			if (oMap==null) oMap = new HashMap();
			oMap.put("section_max_number", String.valueOf(vSections.size()));
			oMap.put("QTIUrl",".");
			oMap.put("default_skin", getDefaultSkin());
			if (iSection<=0){
				createIndexHTML(sXSL, sBase, oMap, eRoot, vSections, hHTML);
			}
			for (int i=1;i<=vSections.size();i++){
				if (iSection<0 || iSection==i){
					createSectionHTML(sXML, sXSL, sBase, i, oMap, hHTML);
				}
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		return hHTML;
	}	
	

	protected static void createIndexHTML(String sXSL, String sBase, HashMap oMap, Element eRoot, Vector vSections, Hashtable hHTML){
		//System.out.println("createIndexHTML("+sXSL+", "+sBase+", ...)");//ALLR
		InputStream is = null;
		try{
			Element eAssignment = createAssignment(eRoot, vSections);
			XMLOutputter serializer = new XMLOutputter();
			String sAssignment = "<?xml version=\"1.0\" encoding=\""+DEFAULT_ENCODING+"\"?>"+serializer.outputString(eAssignment);
			is = new java.io.ByteArrayInputStream(sAssignment.getBytes(DEFAULT_ENCODING));
			Source oXMLSource = getSource(is);
			Source oXSLSource = getSource(sXSL);
			// Create index
			if (sBase!=null){
				qti2html(oXMLSource, oXSLSource, sBase+"index.htm", oMap);
			}else{
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				qti2html(oXMLSource, oXSLSource, bos, oMap);
				hHTML.put("index.htm", bos);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			try{
				if (is!=null) is.close();
			}catch (Exception e){
				is = null;
			}			
		}
	}		
	
	protected static void createSectionHTML(String sXML, String sXSL, String sBase, int iSection, HashMap oMap, Hashtable hHTML){
		//System.out.println("createSectionHTML("+sXML+", "+sXSL+", "+sBase+", "+iSection+", ...)");//ALLR
		try{
			Source oXMLSource = getSource(sXML);
			Source oXSLSource = getSource(sXSL);
			oMap.put("section_number", String.valueOf(iSection));
			if (sBase!=null){
				qti2html(oXMLSource, oXSLSource, sBase+"section_"+iSection+".htm", oMap);
			}else{
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				qti2html(oXMLSource, oXSLSource, bos, oMap);
				hHTML.put("section_"+iSection+".htm", bos);
			}
			//if (sBase!=null) debug("Created "+"section_"+iSection+".htm");			
		}catch (Exception e){
			e.printStackTrace();
		}
	}		
			
}

class QVURIResolver implements URIResolver{
	protected String sQTIXSL;
	protected String sGenericXSL;
	
	protected Hashtable hXSL;
	
	public QVURIResolver(Hashtable hXSL){
		this.hXSL=hXSL;
	}

	public Source resolve(String href, String base) throws TransformerException {
		if (hXSL!=null){
			if (hXSL.containsKey(href)){
				return QTITransformer.getSource(hXSL.get(href)+href);				
			}
		}
		return null;
	}
	
}	



