/*
 * Options.java
 *
 * Created on 2 de julio de 2002, 0:20
 */

package edu.xtec.util;

import java.applet.Applet;
import java.awt.Component;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author  Montse
 * @version 
 */
public class Options extends HashMap {
    
    public static final String TRUE="true", FALSE="false";
    public static final Boolean BTRUE=new Boolean(true), BFALSE=new Boolean(false);    
    public static final String MAC="Mac", WIN="Windows", JAVA14="java14";
    public static final String MAIN_PARENT_COMPONENT="mainParentComponent", APPLET="applet";
    public static final String LANGUAGE_BY_PARAM="languageByParam";
    
    public Options(){
        super();
        init();        
    }
    
    public Options(Map t){
        super(t);
        init();
    }
    
    public Options(Component cmp){
        super();
        init();
        setMainComponent(cmp);
    }
    
    protected void init(){
        String ver=System.getProperty("java.version");
        if(ver!=null && ver.compareTo("1.4.0")>=0)
            put(JAVA14, new Boolean(true));
        
        String s=System.getProperty("os.name").toLowerCase();
        //String s=System.getProperty("java.vendor");
        if(s!=null){
            if(s.indexOf("mac")>=0){
                put(MAC, new Boolean(true));
                put(LookAndFeelUtil.LOOK_AND_FEEL, LookAndFeelUtil.SYSTEM);
            }
            else if(s.toLowerCase().indexOf("win")>=0)
                put(WIN, new Boolean(true));
        }        
    }
    
    public boolean getBoolean(String key){
        return getBoolean(key, false);
    }
    
    public boolean getBoolean(String key, boolean defaultValue){
        boolean result=defaultValue;
        Object r=get(key);
        if(r!=null){
            if(r instanceof Boolean)
                result=((Boolean)r).booleanValue();
            else if(r instanceof String)
                result=((String)r).equalsIgnoreCase(TRUE);
            else if(r instanceof Integer)
                result=((Integer)r).intValue()!=0;
        }
        return result;
    }
    
    public void putBoolean(String key, boolean value){
        put(key, value ? BTRUE : BFALSE);
    }
    
    public String getString(String key){
        return (String)get(key);
    }
    
    public int getInt(String key, int defaultValue){
        int result=defaultValue;
        Object r=get(key);
        if(r!=null){
            if(r instanceof Integer)
                result=((Integer)r).intValue();
            else if(r instanceof String){
                try{
                    result=Integer.parseInt((String)r);
                }catch(NumberFormatException ex){
                    result=defaultValue;
                }
            }
        }
        return result;
    }
    
    public Messages getMessages(){
        return getMessages(null);
    }
    
    public Messages getMessages(String bundle){
        return Messages.getMessages(this, bundle);
    }
    
    public String getMsg(String key){
        return getMessages(null).get(key);
    }
    
    public Component getMainComponent(){
        return (Component)get(MAIN_PARENT_COMPONENT);
    }
    
    public Applet getApplet(){
        return (Applet)get(APPLET);
    }
    
    public void setMainComponent(Component cmp){        
        put(MAIN_PARENT_COMPONENT, cmp);
        if(cmp instanceof Applet)
            put(APPLET, cmp);
    }
    
    public void setLookAndFeel(){
        String s=getString(LookAndFeelUtil.LOOK_AND_FEEL);
        if(s!=null)
            LookAndFeelUtil.setLookAndFeel(s);
    }
    
    public void syncProperties(HashMap src, boolean preserveExistingValues){
        Iterator it=src.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry=(Map.Entry)it.next();
            if(!preserveExistingValues || !containsKey(entry.getKey()))
                put(entry.getKey(), entry.getValue());
        }
    }        
    
    public static Map strToMap(String values, Map map, String delim, char equals, boolean nullsAllowed){
        if(values!=null && values.length()>=0){
            java.util.StringTokenizer st=new java.util.StringTokenizer(values, delim);
            while(st.hasMoreTokens()){
                String e=st.nextToken();
                int i=e.indexOf(equals);
                if(i>0){
                    String key=e.substring(0, i);
                    String value=e.substring(i+1);
                    if(key!=null && key.length()>0 && (nullsAllowed || values.length()>0))
                        map.put(key, value);
                }
            }
        }
        return map;
    }
    
    public static String getString(Map map, String key, String defaultValue){
        String result=(String)map.get(key);
        return result==null ? defaultValue : result;
    }
}
