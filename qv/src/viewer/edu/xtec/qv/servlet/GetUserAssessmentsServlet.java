/*
 * GetUserAssessmentsServlet.java
 * 
 * Created on 04/novembre/2004
 */
package edu.xtec.qv.servlet;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.xtec.qv.util.QuadernConfig;

/**
 * @author sarjona
 */
public class GetUserAssessmentsServlet extends QVServlet  {

	public static final String SEPARATOR = ";";
	/**
	 * Parametres
	 */
	public static final String P_USERNAME = "username";
	
	/**
	 * Properties
	 */
	public static final String K_LOCAL_URL= "store.localURL";
	public static final String K_REMOTE_URL= "store.remoteURL";

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String sUsernames = request.getParameter(P_USERNAME);
		if (sUsernames!=null){
			if (!sUsernames.endsWith(SEPARATOR)) sUsernames+=SEPARATOR;
			StringTokenizer stUsernames = new StringTokenizer(sUsernames, SEPARATOR);
			while (stUsernames.hasMoreElements()){
				String sUsername = stUsernames.nextToken();
				out.println(sUsername+SEPARATOR+getUsernameURLs(sUsername));
			}
		}else{
			out.println("error: paràmetre "+P_USERNAME+" no especificat");
		}
	}
	
	private String getUsernameURLs(String sUsername){
		String sURLs = "";
		if (sUsername!=null){
			String sPrivateRoot = QuadernConfig.getProperty(K_LOCAL_URL)+sUsername;
			String sPublicRoot = QuadernConfig.getProperty(K_REMOTE_URL)+sUsername+"/";
			//logger.debug("private="+sPrivateRoot);
			File fFile = new File(sPrivateRoot);
			String[] files = fFile.list(new DirectoryFilter());
			if (files!=null){
				for (int i=0;i<files.length;i++){
					sURLs += files[i]+SEPARATOR+sPublicRoot+files[i]+"/"+files[i]+".xml"+SEPARATOR;
				}
			}
		}
		return sURLs;		
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

