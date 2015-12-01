/*
 * LookAndFeelUtil.java
 *
 * Created on 24 de octubre de 2001, 21:22
 */

package edu.xtec.util;

/**
 *
 * @author fbusquets
 * @version 1.0
 */
public abstract class LookAndFeelUtil {
    
    /** Key for Options
     */    
    public static final String LOOK_AND_FEEL="lookAndFeel";
    
    /** Default look & feel name
     */    
    public static final String DEFAULT="default";
    /** System look & feel name
     */    
    public static final String SYSTEM="system";
    /** Metal look & feel name
     */    
    public static final String METAL="metal";
    /** Motif look & feel name
     */    
    public static final String MOTIF="motif";
    /** Windows look & feel name
     */    
    public static final String WINDOWS="windows";
    
    public static final String[] VALUES={DEFAULT, SYSTEM, METAL, MOTIF};
    
    /** Sets the app look & feel
     * @param friendlyName Look & feel name. If null, empty or not recognized this function does nohing.
     */    
    public static void setLookAndFeel(String friendlyName){
        if(friendlyName==null)            
            return;        
        try{
            if(friendlyName.equals(DEFAULT))
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
            else if(friendlyName.equals(SYSTEM))
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            else if(friendlyName.equals(METAL))
                setLookAndFeel("javax.swing.plaf", "metal.MetalLookAndFeel");
            else if(friendlyName.equals(MOTIF))
                setLookAndFeel("com.sun.java.swing.plaf", "motif.MotifLookAndFeel");
            else if(friendlyName.equals(WINDOWS))
                setLookAndFeel("com.sun.java.swing.plaf", "windows.WindowsLookAndFeel");
        }
        catch(Exception ex){
            System.err.println("unable to set lookAndFeel to: \""+friendlyName+"\"\n"+ex);
        }
    }
    
    private static void setLookAndFeel(String prefix, String className) throws Exception{
        javax.swing.UIManager.setLookAndFeel(prefix+"."+className);
        
    }    
    
}
