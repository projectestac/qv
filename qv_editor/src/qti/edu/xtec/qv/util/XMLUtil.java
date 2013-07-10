/*
 * UtilXML.java
 * 
 * Created on 04/febrer/2004
 */
package edu.xtec.qv.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format.TextMode;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * @author sarjona
 */
public class XMLUtil {

	private static Logger logger = Logger.getRootLogger();
	
	private final static String DEFAULT_PARSER_DRIVER = "org.apache.xerces.parsers.SAXParser";

	/**
	 * @param sPath path del fitxer XML a obrir
	 * @return representacio del document XML que conté el fitxer indicat 
	 * @throws Exception
	 */
	public static Document open(String sPath){
		Document d = null;
		try{
			if (sPath!=null){
				FileInputStream fis = new FileInputStream(sPath);
				Reader reader = new InputStreamReader(fis, "ISO-8859-1");
				d = getXMLDocument(reader);
				fis.close();
				reader.close();
				//d = getXMLDocument(new FileInputStream(sPath));
			}
		} catch (Exception e){
			logger.error("EXCEPCIO obrint fitxer XML '"+sPath+"' --> "+e);
			e.printStackTrace();
		}
		return d;
	}
    
	/**
	 * Obté el document XML associat a l'stream indicat
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static Document getXMLDocument(InputStream is) throws Exception{
		SAXBuilder sb = createSAXBuilder();
		org.jdom.Document doc=sb.build(new InputStreamReader(is));
		//logger.debug("XML-> "+doc!=null?showElement(doc.getRootElement()):"");
		return doc;        
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
       
        
	/**
	 * Obté el document XML equivalent al text indicat
	 * @param sXMLText String que conte codi XML
	 * @return Document
	 * @throws QVParseException
	 */
	public static Document getXMLDocument(String sXMLText) throws QVParseException{
		return getXMLDocument(sXMLText, null, null);
	}

	/**
	 * Obté el document XML equivalent al text indicat
	 * @param sXMLText String que conte codi XML
	 * @param sSchema Schema amb el que es validarà el document XML
	 * @return Document
	 * @throws QVParseException
	 */
	public static Document getXMLDocument(String sXMLText, String sSchema) throws QVParseException{
		return getXMLDocument(sXMLText, null, sSchema);
	}

	/**
	 * Obté el document XML equivalent al text indicat
	 * @param sXMLText String que conte codi XML
	 * @param sDriver Driver del Parser (per defecte es fa servir el Xerces)
	 * @param sSchema Schema amb el que es validarà el document XML
	 * @return Document
	 * @throws QVParseException
	 */
	public static Document getXMLDocument(String sXMLText, String sDriver, String sSchema) throws QVParseException{
		Document doc=null;
		try{
			if (sXMLText!=null){
				if (sDriver==null){
					sDriver = DEFAULT_PARSER_DRIVER;
				}
				boolean bValidation = sSchema!=null;
				SAXBuilder builder = new SAXBuilder(sDriver, bValidation);
				try{
					if (bValidation){
						builder.setFeature("http://xml.org/sax/features/validation", bValidation);		
						builder.setFeature("http://apache.org/xml/features/validation/schema", bValidation);		
						builder.setFeature("http://apache.org/xml/features/continue-after-fatal-error", bValidation);
						//builder.setFeature("http://apache.org/xml/features/validation/dynamic", true);		
						//builder.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
					}
					if (sSchema!=null){
						builder.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",  sSchema);
					}
				}catch(Exception e){
					logger.error("EXCEPCIO inicialitzant propietats del validador XML -> "+e);
				}finally{
					QVErrorHandler qvErrorHandler = new QVErrorHandler();
					builder.setErrorHandler(qvErrorHandler);
					doc = builder.build(new StringReader(sXMLText));
					if (!qvErrorHandler.hErrors.isEmpty()){
						throw new QVParseException(qvErrorHandler.vLines, qvErrorHandler.hErrors);
					}
				}
			}
		}catch (Exception e){
			if (e instanceof QVParseException){
				throw (QVParseException)e;
			}else{
				logger.error("EXCEPCIO ("+e+") obtenint Document XML -> xml="+sXMLText);
			}
		}
		return doc;        
	}
        
	/**
	 * Guarda l'element XML al fitxer indicat
	 * @param sURL path del fitxer on es guardarà l'element XML
	 * @param eElement element XML a guardar
	 * @return false si es produeix algun error guardant el fitxer XML; true en cas contrari
	 */
	public static boolean save(String sURL, Element eElement){
		//logger.debug("saveXML -> url="+sURL+"  xml="+showElement(eElement));
		boolean bOk = true;
		try{
			if (sURL!=null && eElement!=null){
				Format oFormat = Format.getPrettyFormat();
	            oFormat.setEncoding("ISO-8859-1");
				oFormat.setTextMode(TextMode.NORMALIZE);
				oFormat.setOmitDeclaration(false);
				oFormat.setOmitEncoding(false);
				
				XMLOutputter xmlOut = new XMLOutputter();
				xmlOut.setFormat(oFormat);
				
				FileOutputStream fos=new FileOutputStream(sURL);
				Document doc = new Document(eElement);
				xmlOut.output(doc, fos);
				fos.getFD().sync();
				fos.flush();
				fos.close();
						
/*				FileOutputStream fos=new FileOutputStream(sURL);
				Writer pw = new BufferedWriter(new OutputStreamWriter(fos, "ISO-8859-1"));
				//PrintWriter pw=new PrintWriter(fos);
				String sXML=showElement(eElement);
				//pw.print(sXML); 
				pw.write(sXML); 
				pw.flush();
				fos.getFD().sync();
				pw.close();
				fos.flush();
				fos.close();
*/				
			}
		}
		catch (java.io.IOException e){
			logger.error("EXCEPCIO guardant el fitxer '"+sURL+"' --> "+e);
			e.printStackTrace();
			bOk = false;
		}
		return bOk;
	}
    
	/**
	 * Retorna un String representant de forma indentada l'element e
	 * @param e element XML
	 * @return String amb la representacio de l'element 'e' amb capçalera XML
	 */
	public static String showElement(Element e){
		return showElement(e,true);
	}
	
	/**
	 * Retorna un String representant de forma indentada l'element e
	 * @param e element XML
	 * @param bHeader indica si s'ha de posar o no la capçalera
	 * @return String amb la representacio de l'element 'e' 
	 */
	public static String showElement(Element e, boolean bHeader){
		String s=(bHeader)?"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n":"";
		s=s+showElement(e,0);
		return s;
		//return StringUtil.quote(s);
	}
        
	/**
	 * Retorna un String representant de forma indentada l'element e
	 * @param e element XML
	 * @param iIndent indentacio
	 * @return String amb la representacio de l'element 'e' 
	 */
	protected static String showElement(Element e, int iIndent){
		return showElement(e,iIndent,true);
	}
	
	/**
	 * Retorna un String representant de forma indentada l'element e
	 * @param e element XML
	 * @param iIndent indentacio
	 * @param bIndent indica si s'ha d'indentar l'string
	 * @return String amb la representacio de l'element 'e' 
	 */
	protected static String showElement(Element e, int iIndent, boolean bIndent){
		StringBuffer sIndent=new StringBuffer();
		StringBuffer s=new StringBuffer(528);
		try{
			if (e==null){
				logger.error("Element XML a mostrar null"); 
				return "";
				}
			String sBlank=" ";
			for (int i=0;i<iIndent;i++) {
				sIndent.append(sBlank);
			} 
			s.append(sIndent.toString()+"<").append(e.getName());
			java.util.Iterator it=e.getAttributes().iterator();
			while (it.hasNext()){
				Attribute a=(Attribute)it.next();
				s.append(" "+a.getName()+"=\""+a.getValue()+"\"");
			}
			if (e.getChildren().size()>0 || (e.getText()!=null && e.getText().trim().length()>0)){
				s.append(">");
				it=e.getChildren().iterator();
				if (it.hasNext()){
					s.append("\n");
				}
				while (it.hasNext()){
					Element el=(Element)it.next();
					s.append(showElement(el,bIndent?(iIndent+1):iIndent));
				}
				if (e.getText()!=null && e.getText().trim().length()>0){
					//s.append(e.getText().trim());
					//s.append("<![CDATA["+e.getTextNormalize()+"]]>");
					s.append("<![CDATA["+e.getTextNormalize()+"]]>");
					//s.append(e.getTextNormalize());
					s.append("</"+e.getName()+">\n");
				}
				else{
					 s.append(sIndent+"</"+e.getName()+">\n");
				}                
			}else{
				s.append("/>\n");
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		//logger.debug("s="+s.toString());
		return s.toString();
	}
	
	static class QVErrorHandler implements ErrorHandler{
		
		public Vector vLines = new Vector();
		public Hashtable hErrors = new Hashtable();
		
		public QVErrorHandler(){
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
		 */
		public void warning(SAXParseException arg0) throws SAXException {
			addError(arg0);
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
		 */
		public void error(SAXParseException arg0) throws SAXException {
			addError(arg0);
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
		 */
		public void fatalError(SAXParseException arg0) throws SAXException {
			addError(arg0);
		}
		
		protected void addError(SAXParseException arg0){
			Integer iLine = new Integer(arg0.getLineNumber());
			String sMessage = arg0.getLocalizedMessage();
			//logger.debug(iLine+": "+sMessage+"  locale="+java.util.Locale.getDefault());
			if (!vLines.contains(iLine)){
				vLines.addElement(iLine);
			}
			Vector vErrorsLine = null;
			if (hErrors.containsKey(iLine)){
				vErrorsLine = (Vector)hErrors.get(iLine);
			}else{
				vErrorsLine = new Vector();
				hErrors.put(iLine, vErrorsLine);
			}
			QVParseMessage oMessage = new QVParseMessage(iLine.intValue(), sMessage);
			vErrorsLine.addElement(oMessage);
		}
	}

}
