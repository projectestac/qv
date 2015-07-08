package edu.xtec.qv.biblio.servlets;

import java.util.Vector;

import edu.xtec.lom.Area;

public class GetAreasServlet extends GetValuesServlet{
	
	protected Vector getValues(String sLang){
		return getLOMDatabase().getArees();
	}
	protected void appendObject(StringBuffer sb, Object o, String sLang){
		Area oArea = (Area)o;
		appendValue(sb, oArea.getId());
		appendValue(sb, oArea.getText(sLang));		
	}
	
}
