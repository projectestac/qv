/*
 * MultiBundle.java
 *
 * Created on 28 de enero de 2002, 11:42
 */

package edu.xtec.util;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author  fbusquet
 * @version
 */
public class MultiBundle {
    
    java.util.ArrayList bundles;
    
    public MultiBundle(ResourceBundle mainBundle, String resource, Locale l){
        bundles=new java.util.ArrayList(1);
        bundles.add(new ResourceBundleEx(mainBundle, resource, l));        
    }
    
    public void addBundle(ResourceBundle bundle, String resource, Locale l){
        for(int i=0; i<bundles.size(); i++)
            if(((ResourceBundleEx)bundles.get(i)).resource.equals(resource))
                return;
        bundles.add(new ResourceBundleEx(bundle, resource, l));
    }
    
    public void setLocale(Locale l){
        for(int i=0; i<bundles.size(); i++)
            ((ResourceBundleEx)bundles.get(i)).setLocale(l);
    }
    
    public String getString(String key){
        String result=key;
        for(int i=0; i<bundles.size(); i++){
            try{
                result=((ResourceBundleEx)bundles.get(i)).bundle.getString(key);
                return result;
            } catch(Exception ex){}
        }
        
        System.err.println("Unable to find resource message: ["+result+"]");
        return result;
    }
    
    class ResourceBundleEx{
        ResourceBundle bundle;
        String resource;
        Locale locale;
        
        ResourceBundleEx(ResourceBundle bundle, String resource, Locale locale){
            this.resource=resource;
            this.locale=locale;
            this.bundle=bundle;
        }
        
        void setLocale(Locale l){
            if(!locale.equals(l)){
                try{
                    bundle=ResourceManager.getBundle(resource, l);
                    locale=l;
                } catch(Exception ex){
                    System.err.println("unable to build messagesBundle: "+resource);
                    System.err.println(ex);
                }
            }
        }
    }
}
