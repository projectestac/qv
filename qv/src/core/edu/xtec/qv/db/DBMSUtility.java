package edu.xtec.qv.db;

/*
 * DBMSUtility.java
 *
 * Created on 28 / febrer / 2003, 09:05
 */

/**
 *
 * @author  allastar
 */
public class DBMSUtility {
    
    public static final int MYSQL=0;
    public static final int ORACLE=1;
    public static final int ACCESS=2;

    public static boolean verbose=false;
    
    /** Creates a new instance of DBMSUtility */
    public DBMSUtility() {
    }
    
    public static int getDBMSType(java.sql.Connection connection){
        int iType=ACCESS;
        try{
            String DBMSName=connection.getMetaData().getDatabaseProductName();
            if (DBMSName!=null){
                DBMSName=DBMSName.toLowerCase();
                if (DBMSName.indexOf("oracle")>=0) iType=ORACLE;
                else if (DBMSName.indexOf("mysql")>=0) iType=MYSQL;
                else iType=ACCESS;
            }
        }
   	catch(java.sql.SQLException e){
            System.out.println("Error en consultar nom gestor de BD: "+e.toString());
   	}
        if (verbose){
            switch (iType){
                case ORACLE:
                    System.out.println("Oracle");
                    break;
                case MYSQL:
                    System.out.println("MySQL");
                    break;
                case ACCESS:
                    System.out.println("Access");
                    break;                    
            }
        }
        return iType;
    }

}
