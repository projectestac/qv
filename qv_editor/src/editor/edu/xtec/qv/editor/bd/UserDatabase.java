package edu.xtec.qv.editor.bd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;

public class UserDatabase {

	public final static String DBCONF_PATH = "/edu/xtec/resources/properties/";
	public final static String DBCONF_FILE = "connectionQV.properties";

	private static ConnectionBeanProvider broker;
	private ConnectionBean c;
	private static Properties pDB;
	
	private static final Logger logger = Logger.getRootLogger();
	
	public UserDatabase() {
	}

    /**
     * Check user validation (guest access, not edu365 users)
     * @param sUsername guest username
     * @param sPassword user password
     * @return true if user validation is correct; otherwise false
     */
	public boolean validateUser(String sUsername, String sPassword){
		boolean bValidate = false;
		StringBuffer query = new StringBuffer();
		try{
			query.append("SELECT * ");
			query.append("FROM t_guest_user gu ");
			query.append("WHERE gu.username=? AND gu.password=? ");
			PreparedStatement pstmt=getConnection().getPreparedStatement(query.toString());
			pstmt.setString(1, sUsername);
			pstmt.setString(2, sPassword);
			ResultSet rs=pstmt.executeQuery();
			bValidate = rs.next();
		}
		catch (SQLException e) {
			logger.error("EXCEPTION validating user '"+sUsername+"' -> "+e);
		} finally{
			freeConnection();				
		}
		return bValidate;
	}

//	************************************	
//	* Database
//	************************************
	
	protected ConnectionBeanProvider getConnectionBeanProvider(){
		try{
			if(broker == null) { // Only created by first servlet to call
				broker = ConnectionBeanProvider.getConnectionBeanProvider(true, getDBProperties());				
			}
		}catch (Exception e){
			logger.error("EXCEPTION getting ConnectionBeanProvider-> "+e);
			e.printStackTrace();
		}
		return broker;
	}
	
	protected ConnectionBean getConnection(){
		if (c==null){
			try{
				c = getConnectionBeanProvider().getConnectionBean();
				c.getConnection().setAutoCommit(true);
			}catch (Exception e){
				logger.error("EXCEPTION getting connection -> "+e);				
			}
		}
		return c;
	}

	protected void freeConnection() {
		if (c!=null && broker!=null) {
			broker.freeConnectionBean(c);
			c=null;
		}
	}
	
//	************************************	
//	* Properties
//	************************************
	
	protected static Properties getDBProperties() throws Exception{
		if (pDB==null){
			pDB = new Properties();
			try{
				/*InputStream isl = LOMDatabase.class.getResourceAsStream(DBCONF_PATH+DBCONF_FILE);
				if (isl!=null){
					pDB.load(isl);
				}
				isl.close();*/
				File f = new File(System.getProperty("user.home"), DBCONF_FILE);
				if(f.exists()){
					FileInputStream is=new FileInputStream(f);
					pDB.load(is);
					is.close();
				}
			} catch (FileNotFoundException f) {
				logger.error(f);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return pDB;
	}
	
}
