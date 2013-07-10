package edu.xtec.qv.lms.util;

import java.io.StringReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

public class XMLUtility{
	
	public static Document getXMLDocument(String s) throws Exception{
		StringReader reader = new StringReader(s);
		System.out.println("getXMLDocument");
		SAXBuilder sb = createSAXBuilder();
		org.jdom.Document doc=sb.build(reader);
		return doc;        
	}
	
	public static Document getXMLDocument(InputStream is) throws Exception{
		InputStreamReader reader = new InputStreamReader(is);
		System.out.println("getXMLDocument");
		SAXBuilder sb = createSAXBuilder();
		org.jdom.Document doc=sb.build(reader);
		return doc;        
	}
	
	public static SAXBuilder createSAXBuilder() throws Exception{
		SAXBuilder sb=new SAXBuilder(false);
		sb.setValidation(false);
		sb.setExpandEntities(false);
		return sb;
	}
}