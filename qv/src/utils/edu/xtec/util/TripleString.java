/*
 * TripleString.java
 *
 * Created on 28 de abril de 2003, 10:14
 */

package edu.xtec.util;

/**
 *
 * @author  fbusquet
 */
public class TripleString extends Object implements Comparable{
    
    public static final int ELEMENTS=3;
    public static final int NAME=0, CLASS=1, DESCRIPTION=2;
    
    String[] str;
    
    /** Creates a new instance of TripleString */
    public TripleString(String name, String className, String description) {
        str=new String[ELEMENTS];
        str[NAME]=name;
        str[CLASS]=className;
        str[DESCRIPTION]=description;
    }
    
    public String getStr(int index){
        return (index>=0 && index<ELEMENTS) ? str[index] : null;
    }
    
    public void setStr(int index, String s){
        if(index>=0 && index<ELEMENTS)
            str[index] =s;
    }
    
    public String toString(){
        return str[NAME];
    }
    
    public String getDescription(){
        return str[DESCRIPTION];
    }
    
    public String getClassName(){
        return str[CLASS];
    }
    
    public boolean equals(Object obj){
        boolean result=false;
        if(obj!=null){
            String s=obj.toString();
            result=obj.equals(str[NAME]);
        }
        return result;
    }
    
    public static java.util.Vector getTripleList(String bundlePath, String descBundlePath, java.util.Locale locale) throws Exception{
        java.util.ResourceBundle bundle=ResourceManager.getBundle(bundlePath, locale);
        java.util.ResourceBundle descBundle=null;
        if(descBundlePath!=null)
            descBundle=ResourceManager.getBundle(descBundlePath, locale);
        java.util.Enumeration keys=bundle.getKeys();
        java.util.Vector v=new java.util.Vector();
        while(keys.hasMoreElements()){
            String key=((String)keys.nextElement()).trim();
            String name=bundle.getString(key).trim();
            String description=null;
            try{
                description=(descBundle==null) ? null : descBundle.getString(key);
            } catch(Exception ex){
            }
            v.addElement(new TripleString(name, key, description));            
        }
        java.util.Collections.sort(v);
        
        return v;
    }
    
    public int compareTo(Object obj) {
        String s1=(obj instanceof TripleString) ? ((TripleString)obj).getClassName() : obj==null ? "" : obj.toString();
        return getClassName().compareTo(s1);
    }
    
}
