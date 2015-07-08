package edu.xtec.qv.biblio.servlets;

import java.util.Vector;

import edu.xtec.lom.Idioma;


public class GetLanguagesServlet extends GetValuesServlet{
	
	protected Vector getValues(String sLang){
		return getLOMDatabase().getIdiomes(sLang);
	}
	protected void appendObject(StringBuffer sb, Object o, String sLang){
		Idioma oIdioma = (Idioma)o;
		appendValue(sb, oIdioma.getId());
		appendValue(sb, oIdioma.getText(sLang));		
	}
	
}
