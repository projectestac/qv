/*
 * ConfigQuadernTest.java
 *
 * Created on 15 / maig / 2003, 16:15
 */

package edu.xtec.qv.editor.util;

/**
 *
 * @author  allastar
 */
public class ConfigQuadernTest{
  
    public static java.util.Properties prop=null;
    public static java.net.URL urlProperties;

    /** Creates a new instance of ConfigQuadernTest */
    public ConfigQuadernTest() {
    }
  
    public static void iniConfiguration(){
        try{
            if (prop==null){
                urlProperties=new ConfigQuadernTest().getClass().getResource("configQuadernTest.properties");
                prop=new java.util.Properties();
	  	prop.load(urlProperties.openStream());
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            e.printStackTrace(System.out);
            prop=null;
        }
    }
    
    public static void setProperty(String sName, String sValue){
        if (prop==null) iniConfiguration();
        if (prop!=null){
            String sBefore=prop.getProperty(sName);
            if (sBefore==null || !sBefore.equals(sValue)){ //L'ha modificat
                prop.setProperty(sName,sValue);
                try{
                    java.io.OutputStream os=null;
                    java.net.URL u=urlProperties;
                    //System.out.println("u.getFile()="+((u!=null)?u.getFile():"null")+"<--");
                    if (u!=null) os=new java.io.FileOutputStream(u.getFile());
                    if (os!=null) prop.store(os,"Exporter properties");
                    else System.out.println("Error en actualitzar propietats");
                }
                catch (java.io.IOException e){
                    System.out.println("Excepció en desar fitxer de propietats per exportació:"+e);
                }
            }
        }
    }
    
    public static String getProperty(String sPropertyName){
        if (prop==null) iniConfiguration();
	if (prop!=null) return prop.getProperty(sPropertyName);
	else return null;
    }
    
    public static boolean getBooleanProperty(String s){
        boolean b=false;
        try{
            String s2=getProperty(s);
            if (s2!=null && s2.length()>0) b=s2.trim().toLowerCase().equals("true");
            //System.out.println("s="+s+" s2="+s2+" b="+b);
        }
        catch(Exception e){
        }
        return b;
    }
    
    public static int getIntProperty(String s){
        int i=-1;
        try{
            String s2=getProperty(s);
            if (s2!=null && s2.length()>0) i=Integer.parseInt(s2);
        }
        catch(Exception e){
        }
        return i;
    }

    public static boolean isAutoCorrect(){
        return getBooleanProperty("autoCorrect");
    }

    public static boolean isAutoAdvance(){
        return getBooleanProperty("autoAdvance");
    }

    public static boolean isMandatoryResponse(){
        return getBooleanProperty("mandatoryResponse");
    }
    
    public static boolean isWritingEnabled(){
        return getBooleanProperty("anotacions");
    }
    
    public static int getMaxResponses(){
        return getIntProperty("max");
    }
    
    public static String getQuadernXSL(){
        return getProperty("quadernXSL");
    }
    
    public static boolean getShowAllways(){
        return getBooleanProperty("showAllways");
    }
    
    public static void setAutoCorrect(boolean b){
        setProperty("autoCorrect",""+b);
    }

    public static void setAutoAdvance(boolean b){
        setProperty("autoAdvance",""+b);
    }
    
    public static void setMandatoryResponse(boolean b){
        setProperty("mandatoryResponse",""+b);
    }
    
    public static void setWritingEnabled(boolean b){
        setProperty("anotacions",""+b);
    }
    
    public static void setMaxResponses(int i){
        setProperty("max",""+i);
    }
    
    public static void setQuadernXSL(String s){
        setProperty("quadernXSL",s);
    }
    
    public static void setShowAllways(boolean b){
        setProperty("showAllways",""+b);
    }
        
}
