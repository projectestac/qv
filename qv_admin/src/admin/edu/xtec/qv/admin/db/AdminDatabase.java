package edu.xtec.qv.admin.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

import edu.xtec.lom.Area;
import edu.xtec.lom.Nivell;
import edu.xtec.qv.admin.FullActivity;
import edu.xtec.qv.admin.Role;
import edu.xtec.qv.admin.User;
import edu.xtec.qv.biblio.Author;
import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;

import java.sql.CallableStatement;

public class AdminDatabase {

	public final static String DBCONF_PATH = "/edu/xtec/resources/properties/";
	public final static String DBCONF_FILE = "connectionQV.properties";

	private static ConnectionBeanProvider broker;
	private ConnectionBean conn;
	private static Properties pDB;

	private static final Logger logger = Logger.getRootLogger();

	public AdminDatabase() {
	}



//	************************************
//	* Database access
//	************************************

	/**
     * Check user validation (guest access, not edu365 users)
     * @param sUsername guest username
     * @param sPassword user password
     * @return true if user validation is correct; otherwise false
     */
	public boolean validateUser(String sUsername, String sPassword){
		boolean bValidate = false;

        ConnectionBean c = getConnection();
        try {
        	CallableStatement oFunction = c.getConnection().prepareCall("{ call admxtec.PKG_XTEC.AUTENTICACIO(?,?,?) }");
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

    /**
     * @param sUsername username identifier
     * Return User object with user information (roles,...)
     */
	public User getUser(String sUsername){
		User oUser = null;
		if (sUsername!=null){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT au.id_role ");
				query.append("FROM t_admin_user au ");
				query.append("WHERE au.id_user=? ");
				PreparedStatement pstmt = getConnection().getPreparedStatement(query.toString());
				pstmt.setString(1, sUsername);
				ResultSet rs=pstmt.executeQuery();
				oUser = new User(sUsername);
				while (rs.next()){
					int iIdRole = rs.getInt("id_role");
					Role oRole = new Role(iIdRole);
					oUser.addRole(oRole);
				}
				freeConnection();
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting user '"+sUsername+"' --> "+e);
			}
		}
		return oUser;
	}

    /**
     * @param sUsername username identifier
     * Return Vector of String with username of users associated with sUsername specified
     */
	public Vector getUserAssociation(String sUsername){
		Vector vUser = new Vector();
		if (sUsername!=null){
			StringBuffer query = new StringBuffer();
			try{
				query.append("SELECT aua.id_user2 ");
				query.append("FROM t_admin_user_association aua ");
				query.append("WHERE aua.id_user1=? ");
				query.append("ORDER BY aua.id_user2 ");
				PreparedStatement pstmt = getConnection().getPreparedStatement(query.toString());
				pstmt.setString(1, sUsername);
				ResultSet rs=pstmt.executeQuery();
				while (rs.next()){
					String sUser = rs.getString("id_user2");
					vUser.addElement(sUser);
				}
				freeConnection();
			}
			catch (SQLException e) {
				logger.error("EXCEPTION getting users associated to '"+sUsername+"' --> "+e);
			}
		}
		return vUser;
	}

    /**
     * Return Vector object with authors information (id, name...)
     */
	public Vector getAuthors(){
		Vector vAuthors = new Vector();
		StringBuffer query = new StringBuffer();
		try{
			query.append("SELECT ba.id_autor, ba.nom_complet, ba.id_edu365 ");
			query.append("FROM biblio_autor ba ");
			query.append("ORDER BY ba.nom_complet ");
			PreparedStatement pstmt = getConnection().getPreparedStatement(query.toString());
			ResultSet rs=pstmt.executeQuery();
			while (rs.next()){
				int iId = rs.getInt("id_autor");
				String sName = rs.getString("nom_complet");
				String sUsername = rs.getString("id_edu365");
				Author oAuthor = new Author(iId, sName, null, null, null, sUsername);
				vAuthors.addElement(oAuthor);
			}
			freeConnection();
		}
		catch (SQLException e) {
			logger.error("EXCEPTION getting authors --> "+e);
		}
		return vAuthors;
	}

	public boolean setActivity(FullActivity oActivity){
		boolean bOk = false;
		if (oActivity.getId()>0){
			bOk = updateActivity(oActivity);
		}else{
			bOk = addActivity(oActivity);
		}
		return bOk;
	}

	protected boolean addActivity(FullActivity oActivity){
		boolean bOk = false;
		try{
			insertActivity(oActivity);
			if (oActivity.getId()>0){
				insertActivityAreas(oActivity);
				insertActivityLevels(oActivity);
			}
		}
		catch (Exception e) {
			logger.error("EXCEPTION adding activity --> "+e);
		}finally{
			freeConnection();
		}
		return bOk;
	}

	/**
	 * Insert the activity at biblio_activitat table and update the activity id
	 * @param oActivity
	 * @return
	 * @throws SQLException
	 */
	protected boolean insertActivity(FullActivity oActivity) throws Exception{
		boolean bOk = true;
		try{
			StringBuffer sbQuery = new StringBuffer();
			sbQuery.append("INSERT INTO biblio_activitat (data_creacio, data_revisio, imatge, estat) ");
			sbQuery.append("VALUES  (?,?,?,?)");
			PreparedStatement pstmt = getConnection().getPreparedStatement(sbQuery.toString());
			int i = 1;
			pstmt.setDate(i++, new Date(oActivity.getCreationDate().getTime()));
			pstmt.setDate(i++, new Date(oActivity.getCreationDate().getTime()));
			pstmt.setString(i++, oActivity.getImage());
			pstmt.setString(i++, oActivity.getState());
			pstmt.execute();
			if (bOk){
				int iId = Integer.parseInt(getLastId("sq_activitat"));
				logger.debug("nou id="+iId);
				oActivity.setId(iId);
			}
		}
		catch (Exception e) {
			bOk = false;
			logger.error("EXCEPTION inserting activity --> "+e);
			throw e;
		}finally{
			freeConnection();
		}
		return bOk;
	}
	protected boolean insertActivityAreas(FullActivity oActivity){
		boolean bOk = false;
		try{
			Iterator itAreas = oActivity.getAreas().iterator();
			while (itAreas.hasNext()){
				Area oArea = (Area)itAreas.next();
				StringBuffer sbQuery = new StringBuffer();
				sbQuery.append("INSERT INTO biblio_activitat_area (id_activitat, id_area) ");
				sbQuery.append("VALUES  (?,?)");
				PreparedStatement pstmt = getConnection().getPreparedStatement(sbQuery.toString());
				int i = 1;
				pstmt.setInt(i++, oActivity.getId());
				pstmt.setInt(i++, oArea.getId());

				bOk =pstmt.execute();
			}
		}
		catch (SQLException e) {
			logger.error("EXCEPTION adding activity areas --> "+e);
		}finally{
			freeConnection();
		}
		return bOk;
	}
	protected boolean insertActivityLevels(FullActivity oActivity){
		boolean bOk = false;
		try{
			Iterator it = oActivity.getLevels().iterator();
			while (it.hasNext()){
				Nivell oLevel = (Nivell)it.next();
				StringBuffer sbQuery = new StringBuffer();
				sbQuery.append("INSERT INTO biblio_activitat_nivell (id_activitat, id_nivell) ");
				sbQuery.append("VALUES  (?,?)");
				PreparedStatement pstmt = getConnection().getPreparedStatement(sbQuery.toString());
				int i = 1;
				pstmt.setInt(i++, oActivity.getId());
				pstmt.setInt(i++, oLevel.getId());

				bOk =pstmt.execute();
			}
		}
		catch (SQLException e) {
			logger.error("EXCEPTION adding activity levels --> "+e);
		}finally{
			freeConnection();
		}
		return bOk;
	}
	/*protected boolean insertActivityAuthors(FullActivity oActivity){
		boolean bOk = false;
		try{
			Iterator it = oActivity.getAuthors().iterator();
			while (it.hasNext()){
				Author oAuthor = (Author)it.next();
				StringBuffer sbQuery = new StringBuffer();
				sbQuery.append("INSERT INTO biblio_activitat_nivell (id_activitat, id_nivell) ");
				sbQuery.append("VALUES  (?,?)");
				PreparedStatement pstmt = getConnection().getPreparedStatement(sbQuery.toString());
				int i = 1;
				pstmt.setInt(i++, oActivity.getId());
				pstmt.setInt(i++, oAuthor.getId());

				bOk =pstmt.execute();
			}
		}
		catch (SQLException e) {
			logger.error("EXCEPTION adding activity levels --> "+e);
		}finally{
			freeConnection();
		}
		return bOk;
	}	*/


	protected boolean updateActivity(FullActivity oActivity){
		boolean bOk = false;
		try{
			StringBuffer query = new StringBuffer();
			query.append("UPDATE biblio_activitat ");
			query.append("SET  ");
			if (oActivity.getCreationDate()!=null) query.append("data_creacio=?, ");
			if (oActivity.getRevisionDate()!=null) query.append("data_revisio=?, ");
			if (oActivity.getImage()!=null) query.append("imatge=?, ");
			query.append("estat=? ");
			query.append("WHERE id_activitat=? ");
			PreparedStatement pstmt = getConnection().getPreparedStatement(query.toString());
			int i = 1;
			if (oActivity.getCreationDate()!=null) pstmt.setDate(i++, new Date(oActivity.getCreationDate().getTime()));
			if (oActivity.getRevisionDate()!=null) pstmt.setDate(i++, new Date(oActivity.getRevisionDate().getTime()));
			if (oActivity.getImage()!=null) pstmt.setString(i++, oActivity.getImage());
			pstmt.setString(i++, oActivity.getState());
			pstmt.setInt(i++, oActivity.getId());

			bOk =pstmt.execute();
		}
		catch (SQLException e) {
			logger.error("EXCEPTION updating activity --> "+e);
		}finally{
			freeConnection();
		}
		return bOk;
	}

	protected String getLastId(String sSequence){
		String sId = null;
		try{
			StringBuffer sbQuery = new StringBuffer();
			sbQuery.append("SELECT "+sSequence+".CURRVAL as id FROM dual ");
			PreparedStatement pstmt = getConnection().getPreparedStatement(sbQuery.toString());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				sId = rs.getString("id");
			}
		}
		catch (SQLException e) {
			logger.error("EXCEPTION getting last id from sequence '"+sSequence+"' --> "+e);
		}finally{
			freeConnection();
		}
		return sId;
	}


//	************************************
//	* Database connection
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
		if (conn == null){
			try {
				conn = getConnectionBeanProvider().getConnectionBean();
				conn.getConnection().setAutoCommit(true);
			} catch (Exception e) {
				logger.error("EXCEPTION getting connection -> "+e);
			}
		}
		return conn;
	}

	protected void freeConnection() {
		if (conn != null && broker != null) {
			broker.freeConnectionBean(conn);
			conn = null;
		}
	}

//	************************************
//	* Properties
//	************************************

	protected static Properties getDBProperties() throws Exception{
		if (pDB == null) {
			pDB = new Properties();
			try{
				pDB.load(AdminDatabase.class.getResourceAsStream(DBCONF_PATH+DBCONF_FILE));
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
