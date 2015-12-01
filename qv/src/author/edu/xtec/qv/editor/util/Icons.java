/*
 * Icons.java
 *
 * Created on 10 / desembre / 2002, 13:03
 */

package edu.xtec.qv.editor.util;

/**
 *
 * @author  allastar
 */
import java.util.*;

public class Icons {

    private static ResourceBundle icons;
    public static final String DEFAULT_BUNDLE="edu/xtec/resources/messages/IconsBundle";
    public static java.util.HashMap hmIcons=null;

    /** Creates a new instance of Icons */
    public Icons() {
        init(null);
    }

    public static void init(String bundle) {
        if(bundle==null) bundle=DEFAULT_BUNDLE;
        icons=ResourceBundle.getBundle(bundle);
    }

    public static javax.swing.ImageIcon getIcon(String sIconKey){
        if (icons==null) init(null);
        if (hmIcons==null) hmIcons=new java.util.HashMap();
        javax.swing.ImageIcon icon=null;
        try{
            if (hmIcons.containsKey(sIconKey)) icon=(javax.swing.ImageIcon)hmIcons.get(sIconKey);
            else{
                String sPath=icons.getString(sIconKey);
                //System.out.println(sIconKey+":"+sPath);
                //icon=new javax.swing.ImageIcon(ClassLoader.getSystemResource(sPath));
                icon=new javax.swing.ImageIcon(Icons.class.getResource("/"+sPath));
                if (icon!=null) hmIcons.put(sIconKey,icon);
            }
        }
        catch(Exception e){
            System.out.println("ERROR - Unable to get icon: "+sIconKey);
            e.printStackTrace(System.out);
        }
        return icon;
    }
}