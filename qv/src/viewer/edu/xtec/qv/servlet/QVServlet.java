package edu.xtec.qv.servlet;

/*
 * QVServlet.java
 *
 * Created on 6 / juliol / 2004
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import edu.xtec.qv.db.LearningDatabaseAdmin;
import edu.xtec.qv.db.QVDataManage;
import edu.xtec.qv.util.Utility;
import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;


/**
 *
 * @author  sarjona
 */
public abstract class QVServlet extends HttpServlet{
    
	protected static Logger logger = Logger.getRootLogger();
	protected static final String USER_KEY="usuari-edu365";

	public final static String PROPERTIES_PATH = "/edu/xtec/resources/properties/";
	public final static String DBCONF = "connectionQV.properties";
	
	/**
	 * Parametres
	 */
	public final static String P_ASSIGNMENT_ID = "assignacioId";
	public static String P_ASSESSMENT_URL = "quadernURL";
	public static String P_ASSESSMENT_XSL = "quadernXSL";
	public final static String P_FINISHED_PAGE = "finishedPage";
	public final static String P_PWD_PAGE = "pwdPage";
	public final static String P_END_PAGE = "endPage";

	/**
	 * Estats d'un quadern
	 */
	public static final String NO_INICIAT = "no_iniciat";
	public static final String INICIAT = "iniciat";
	public static final String INTERVENCIO_ALUMNE= "intervencio_alumne";
	public static final String INTERVENCIO_DOCENT = "intervencio_docent";
	public static final String PARCIALMENT_LLIURAT = "parcialment_lliurat";
	public static final String LLIURAT = "lliurat";
	public static final String PARCIALMENT_CORREGIT = "parcialment_corregit";
	public static final String CORREGIT = "corregit";

	/**
	 * View
	 */
	public final static String CANDIDATE_VIEW = "candidate";
	public final static String TEACHER_VIEW = "teacher";

    
	protected static ConnectionBeanProvider broker;
	private QVDataManage db;
	//protected QVDataManage db;
	private ConnectionBean c;

	    
	public QVServlet(){
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		ServletException sex=null;
		IOException ioex=null;
		try{
			processRequest(request, response);
		} catch(ServletException ex){
			sex=ex;			
		} catch(IOException ex){
			ioex=ex;
		} finally{
			freeConnection();			
		}
		if(sex!=null){
			sex.printStackTrace();
			throw sex;
		}
		if(ioex!=null){
			ioex.printStackTrace();
			throw ioex;
		}		
	}
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		ServletException sex=null;
		IOException ioex=null;
		try{
			processRequest(request, response);
		} catch(ServletException ex){
			sex=ex;			
		} catch(IOException ex){
			ioex=ex;
		} finally{
			freeConnection();			
		}
		if(sex!=null){
			throw sex;
		}
		if(ioex!=null){
			throw ioex;
		}		
	}
    
	protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException;

//	************************************	
//	* Database
//	************************************

/*	
	protected ConnectionBeanProvider getConnectionBeanProvider(){
		try{
			if(broker == null) { // Only created by first servlet to call
				broker = ConnectionBeanProvider.getConnectionBeanProvider(true, Utility.loadProperties(PROPERTIES_PATH, DBCONF));
			}
		}catch (Exception e){
			logger.error("EXCEPTION getting ConnectionBeanProvider-> "+e);
		}
		return broker;
	}
	
	private void initConnection(ConnectionBean c){
		try{
			c.getConnection().setAutoCommit(true);
			//c.getConnection().setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
		}
		catch(Exception e){
			logger.error("EXCEPTION init connection -> "+e);
		}
	}
    
	protected QVDataManage createQVDataManage(){
		c = getConnectionBeanProvider().getConnectionBean();
		initConnection(c);
		db = new LearningDatabaseAdmin(c);
		return db;
	}
*/
	
	protected QVDataManage getQVDataManage(){
		if (db==null){
			//createQVDataManage();
			db = new LearningDatabaseAdmin();
		}
		return db;
	}

	protected void freeConnection() {
		if (getQVDataManage()!=null){
			getQVDataManage().freeConnection();
		}
		if (c!=null && broker!=null) {
			broker.freeConnectionBean(c);
			db = null;
			c=null;
		}
	}
	
	protected static String getUserId(HttpServletRequest request){
		String sUserId = null;
		if(request!=null){
			Cookie[] cookies=request.getCookies();
			if(cookies!=null){
				for(int i=0; i<cookies.length; i++){
					Cookie c=cookies[i];
					if(c.getName().equals(USER_KEY) && c.getValue()!=null){
						sUserId=c.getValue().trim();
						break;
					}
				}
			}
		}
		return sUserId;
	}
	
	protected static void printErrorLog(HttpServletRequest request, Exception e){
		StringBuffer sb = new StringBuffer();
		sb.append(" [");
		sb.append(request.getRemoteAddr());
		sb.append("] ");
		String sUserId = getUserId((HttpServletRequest)request); 
		if (sUserId!=null){
			sb.append(" [");
			sb.append(sUserId);
			sb.append("] ");
		}
		sb.append("EXCEPTION processing request-> ");
		sb.append(e);
		logger.error(sb.toString());
		//logger.error("EXCEPTION processing request:"+ex);
		e.printStackTrace();		
	}
	
}
