/*
 * AppletPreviewerStub.java
 *
 * Created on 24 / octubre / 2002, 10:19
 */

package edu.xtec.qv.editor.util;

/**
 *
 * @author  allastar
 */
public class AppletPreviewerStub implements java.applet.AppletStub {
    
    protected java.applet.AppletContext ac;
    protected java.util.HashMap hmParams;
    
    /** Creates a new instance of AppletPreviewerStub */
    public AppletPreviewerStub(java.applet.AppletContext ac, java.util.HashMap hmParams){
        this.ac=ac;
        this.hmParams=hmParams;
    }

    public void appletResize(int width, int height){
        System.out.println("apletResize");
    }

    public java.applet.AppletContext getAppletContext(){
        return ac;
    }
          
    public java.net.URL getCodeBase() {
        //Gets the base URL. 
        System.out.println("getCodeBase");
        return null;
    }
    
    public java.net.URL getDocumentBase(){
          //Returns an absolute URL naming the directory of the document in which the applet is embedded. 
        System.out.println("getDocumentBase");
          return null;
    }
 
    public String getParameter(String name){
        //Returns the value of the named parameter in the HTML tag. 
        System.out.println("getParameter("+name+")");
        Object o=hmParams.get(name);
        if (o!=null) return o.toString();
        else return null;
    }
 
    public boolean isActive(){
        System.out.println("isActive");
        return true;
    }
    
}
