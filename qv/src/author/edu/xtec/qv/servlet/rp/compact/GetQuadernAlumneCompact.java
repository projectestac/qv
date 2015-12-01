package edu.xtec.qv.servlet.rp.compact;
/*
 * GetQuadernAlumneCompact.java
 *
 * Created on 5 / maig / 2003, 09:21
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.xtec.qv.db.QVDataManage;
import edu.xtec.qv.qti.util.DataAdmin;
import edu.xtec.qv.qti.util.QVSession;
import edu.xtec.qv.servlet.rp.GetQuadernAlumne;
import edu.xtec.qv.servlet.util.BufferedURL;

/**
 *
 * @author  allastar
 */
public class GetQuadernAlumneCompact extends GetQuadernAlumne{
    
    static QVSession session;
    static java.util.HashMap context;
    static DataAdmin db;
    
    /** Creates a new instance of GetQuadernAlumneCompact */
    public GetQuadernAlumneCompact() {
    }

    public boolean init() throws Exception  {
        if (session==null){
            session=QVSession.getDefaultInstance();
            session.setAttribute("validated","true");
        }
        if (context==null) context=new java.util.HashMap();
        return true;
    }
    
    public QVDataManage createQVDataManage(){
        if (db==null) db=DataAdmin.getDefaultInstance();
        return db;
    }
    
    public HttpSession getSession(HttpServletRequest request){
        return session;
    }
    
    public String getParameter(String name){
        //System.out.println("getParam("+name+")="+getParam(name,0));
        return getParam(name,0);
    }
    
    public String getRequestURI(){
        return "";
    }
    
    public String getServletName(){
        return "GetQuadernAlumneServlet";
    }
    
    public javax.servlet.ServletContext getServletContext(){
        return null;
    }
    
    public void setServletAttribute(String name, Object value){
        context.put(name,value);
    }
    
    public Object getServletAttribute(String name){
        return context.get(name);
    }
    
    public java.io.InputStream getInputStreamForURL(java.net.URL urlQTI){
        return BufferedURL.getInputStreamForURL(urlQTI);
    }
    
    public String getContext(){
        java.net.URL u=getClass().getResource(".");
        String s=null;
        if (u!=null) s="file://"+u.getPath()+"../../../../../../../";
        //System.out.println("context="+s+"<--");
        return s;
    }
}
