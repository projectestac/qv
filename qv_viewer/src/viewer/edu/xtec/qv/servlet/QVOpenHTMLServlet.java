/**
 *  QVGenerateHTMLServlet.java
 *
 * Created on 24/05/2007
 */

package edu.xtec.qv.servlet;

import javax.servlet.http.HttpServletResponse;


/**
 * @author  sarjona
 * @version
 */
public class QVOpenHTMLServlet extends QVGenerateHTMLServlet {
    
	protected void createResponse(HttpServletResponse response, String sURL, String sSkin, String sLang, int iSection, String sParams) throws Exception{
		if (exists(sURL)){
			if (sURL.indexOf("?")<0) sURL+="?";
			sURL+=getProperty("qv.dist.params");
			sURL+="&lang="+sLang;
			sURL+="&skin="+sSkin;
			sURL+="&"+sParams;
			response.sendRedirect(sURL);
		}else{
			throw new QVServletException("Can't open url: "+sURL);
		}
	}
	
}
	