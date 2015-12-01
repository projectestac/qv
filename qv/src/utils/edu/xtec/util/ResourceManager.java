/*
 * ResourceManager.java
 *
 * Created on 7 de marzo de 2002, 17:11
 */

package edu.xtec.util;

import java.util.HashMap;

import javax.swing.ImageIcon;

/**
 *
 * @author  fbusquet
 * @version 
 */
public abstract class ResourceManager{
    
    public static final String RESOURCE_ROOT="/edu/xtec/resources/";
    public static final String RESOURCE_CLASS_ROOT="edu.xtec.resources.";
    public static final StreamIO.InputStreamProvider STREAM_PROVIDER=new StreamIO.InputStreamProvider(){
        public java.io.InputStream getInputStream(String resourceName) throws Exception{
            return getResourceAsStream(resourceName);
        }
    };
    private static HashMap icons=new HashMap();
    
    public static ImageIcon getImageIcon(String name){
        ImageIcon result=(ImageIcon)icons.get(name);
        if(result==null){
            try{
                result=new ImageIcon(getResource(name));
                String s=name;
                if(s.startsWith("icons/"))
                    s=new StringBuffer("@").append(s.substring(6)).toString();
                result.setDescription(s);
                icons.put(name, result);
            } catch(Exception ex){
                System.err.println("unable to get image "+name);
                System.err.println(ex);
            }
        }
        return result;
    }
    
    public static java.net.URL getResource(String name) throws Exception{
        java.net.URL result=ResourceManager.class.getResource(RESOURCE_ROOT+name);
        if(result==null)
            throw buildException(name);
        return result;
    }
    
    public static java.io.InputStream getResourceAsStream(String name) throws Exception{
        java.io.InputStream result=ResourceManager.class.getResourceAsStream(RESOURCE_ROOT+name);
        if(result==null)
            throw buildException(name);
        return result;
    }
    
    public static byte[] getResourceBytes(String name) throws Exception{
        return StreamIO.readInputStream(getResourceAsStream(name));
    }
    
    public static ExtendedByteArrayInputStream getResourceAsByteArray(String name) throws Exception{
        return new ExtendedByteArrayInputStream(getResourceBytes(name), name);
    }
        
    public static java.util.ResourceBundle getBundle(String name, java.util.Locale locale) throws Exception{
        java.util.ResourceBundle result=java.util.ResourceBundle.getBundle(RESOURCE_CLASS_ROOT+name, locale);
        if(result==null)
            throw buildException(name);
        return result;
    }
    
    private static Exception buildException(String name){
        return new Exception("Unable to load resource: "+name);
    }    
}
