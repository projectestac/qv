package edu.xtec.qv.editor.bd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.Types;

import javax.sql.DataSource;

import java.util.Properties;


import org.apache.log4j.Logger;

import javax.naming.InitialContext;

public class UserDatabase {

	public final static String DBCONF_PATH = "/edu/xtec/resources/properties/";
	public final static String DBCONF_FILE = "connectionQV.properties";

	private static Connection conn;
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

        Connection c = getConnection();
        try {
            CallableStatement oFunction = c.prepareCall("{ call admxtec.PKG_XTEC.AUTENTICACIO(?,?,?) }");
            logger.info("Connecting to: admxtec.PKG_XTEC.AUTENTICACIO(?,?,?)");
            oFunction.setString(1, sUsername);
            oFunction.setString(2, sPassword);
            oFunction.registerOutParameter(3, Types.INTEGER);
            oFunction.execute();
            String sResult = oFunction.getString(3);
            logger.info("sResult: " + sResult);
            oFunction.close();
            if ("1".equals(sResult)) {
                bValidate = true;
            }
        } catch (SQLException e) {
            logger.error("EXCEPTION validating user '"+sUsername+"' -> "+e);
        } finally {
            freeConnection();
        }
        return bValidate;
	}

//	************************************
//	* Database
//	************************************

	protected Connection getConnection(){
		if (conn == null){
            try {
                Properties prop = getDBProperties();
                String sDriver = prop.getProperty("dbDriver");
                if ("JNDI".equalsIgnoreCase(sDriver)){
                    InitialContext ctx = new InitialContext();
                    String sDbContext = prop.getProperty("dbContext");
                    String sURL = (sDbContext.length() > 0?sDbContext+"/":"")+prop.getProperty("dbServer");
                    DataSource ds = (DataSource)ctx.lookup(sURL);
                    conn = ds.getConnection();
                }else{
                    Class.forName(sDriver);
                    conn = DriverManager.getConnection(prop.getProperty("dbServer"),prop.getProperty("dbLogin"),prop.getProperty("dbPassword"));
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return conn;
	}

	protected void freeConnection() {
		if (conn != null){
            try {
                conn.close();
                conn = null;
            } catch (Exception e) {
                logger.error(e);
            }
        }
	}

//	************************************
//	* Properties
//	************************************

	protected static Properties getDBProperties() throws Exception{
		if (pDB == null) {
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
