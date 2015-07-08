package edu.xtec.qv.biblio.servlets;

import java.util.Vector;

import edu.xtec.lom.Nivell;

public class GetLevelsServlet extends GetValuesServlet{
	
	protected Vector getValues(String sLang){
		return getLOMDatabase().getNivells();
	}
	protected void appendObject(StringBuffer sb, Object o, String sLang){
		Nivell oNivell = (Nivell)o;
		appendValue(sb, oNivell.getId());
		appendValue(sb, oNivell.getText(sLang));		
	}
	
}
