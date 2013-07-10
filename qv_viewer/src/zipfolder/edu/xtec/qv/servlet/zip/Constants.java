/*
 * Params.java
 *
 * Created on 9 / desembre / 2003, 11:48
 */

package edu.xtec.qv.servlet.zip;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 *
 * @author  fbusquet
 */
public final class Constants {
    
    public static final String PROPERTIES_FILE="edu/xtec/resources/properties/zipFolder.properties";
    
    public static boolean INCLUDE_HIDDEN_FILES;
    public static long ALERT_SIZE;
    public static long MAX_SIZE;
    public static int MAX_FILES;    
    public static boolean AVOID_BIG_FILES;    
    public static long VERY_MAX_SIZE;
    public static int VERY_MAX_FILES;    
	public static boolean SHOW_LOG;    
	public static String REPOSITORY_PATH;    
	public static String CSS_JS_PATH;    
	public static String JS_FILE;    
    
    static{
        try{
            Properties prop=new Properties();
            prop.load(Constants.class.getResourceAsStream("/"+PROPERTIES_FILE));
            File propFile=new File(System.getProperty("user.home"), PROPERTIES_FILE);
            if(propFile.exists() && propFile.isFile())
                prop.load(new FileInputStream(propFile));
            INCLUDE_HIDDEN_FILES="true".equals(prop.getProperty("INCLUDE_HIDDEN_FILES", "false"));
            ALERT_SIZE=Long.parseLong(prop.getProperty("ALERT_SIZE", "1048576"));
            MAX_SIZE=Long.parseLong(prop.getProperty("MAX_SIZE", "3000000"));
            MAX_FILES=Integer.parseInt(prop.getProperty("MAX_FILES", "100"));
            AVOID_BIG_FILES="true".equals(prop.getProperty("AVOID_BIG_FILES", "false"));
            VERY_MAX_SIZE=Long.parseLong(prop.getProperty("VERY_MAX_SIZE", "100000000"));
            VERY_MAX_FILES=Integer.parseInt(prop.getProperty("VERY_MAX_FILES", "1000"));            
			SHOW_LOG="true".equals(prop.getProperty("SHOW_LOG", "false"));            
			REPOSITORY_PATH=prop.getProperty("REPOSITORY_PATH", "");
			CSS_JS_PATH=prop.getProperty("CSS_JS_PATH", "");
			JS_FILE=prop.getProperty("JS_FILE", "");
        } catch(java.io.IOException ex){
            System.err.println("Error d'inicialitzacio!");
        }
    }
    
}
