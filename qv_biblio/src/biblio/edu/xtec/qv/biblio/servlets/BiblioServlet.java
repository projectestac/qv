package edu.xtec.qv.biblio.servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import edu.xtec.lom.bd.LOMDatabase;
import edu.xtec.qv.biblio.db.BiblioDatabase;

public abstract class BiblioServlet  extends HttpServlet{

	public static String S_BEGIN = "##";
	public static String S_END = "##";
	public static String S_SEPARATOR = "$$";
	
	public final static String P_LANG = "lang";

	protected static Logger logger = Logger.getRootLogger();
	
	private BiblioDatabase db;
	private LOMDatabase oLOMDB;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		doAction(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		doAction(request, response);		
	}
	
	protected void doAction(HttpServletRequest request, HttpServletResponse response){
		try{
			processRequest(request, response);
		} catch(ServletException se){
			logger.error("EXCEPTION -> "+se);
		} catch(IOException ioe){
			logger.error("EXCEPTION -> "+ioe);
		} finally{
			freeConnection();
		}		
	}
	
	protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException;

	protected void appendValue(StringBuffer sb, int iValue){
		sb.append(iValue);
		sb.append(S_SEPARATOR);		
	}
	protected void appendValue(StringBuffer sb, Object oValue){
		if (oValue!=null){
			if (oValue instanceof Vector){
				boolean bFirst = true;
				Enumeration enumValue = ((Vector)oValue).elements();
				while (enumValue.hasMoreElements()){
					Object tmpValue = enumValue.nextElement();
					if (bFirst){
						bFirst = false;
					}else{
						sb.append(",");						
					}
					sb.append(tmpValue);
				}
			}else if (oValue instanceof Date){
				try{
					long lDate = ((Date)oValue).getTime()/1000;
					sb.append(lDate);
				}catch (Exception e){
					sb.append("");
				}
			}else {
				sb.append(oValue);	
			}
		}
		sb.append(S_SEPARATOR);
	}

	protected String getLang(HttpServletRequest request){
		return getParameter(request, P_LANG, "ca");
	}
	
	protected String getParameter(HttpServletRequest request, String sParam){
		return getParameter(request, sParam, null);
	}
	protected String getParameter(HttpServletRequest request, String sParam, String sDefault){
		String sValue = request.getParameter(sParam);
		if (sValue==null || sValue.trim().length()==0){
			sValue = sDefault;
		}
		return sValue;
	}
		
	
	protected BiblioDatabase getBiblioDatabase(){
		if (this.db==null){
			this.db = new BiblioDatabase();
		}
		return this.db;
	}

	protected LOMDatabase getLOMDatabase(){
		if (oLOMDB==null){
			oLOMDB = new LOMDatabase();
		}
		return oLOMDB;
	}
	
	

	protected void freeConnection(){
		if (this.db!=null){
			this.db.freeConnection();
		}
	}
}
