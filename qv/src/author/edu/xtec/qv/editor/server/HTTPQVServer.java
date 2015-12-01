/*
 * HttpReportServer.java
 *
 * Created on 13 de febrero de 2003, 14:47
 */

package edu.xtec.qv.editor.server;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

import edu.xtec.qv.servlet.rp.ImageProxy;
import edu.xtec.qv.servlet.rp.compact.GetAssignacionsDocentCompact;
import edu.xtec.qv.servlet.rp.compact.GetAssignacionsUsuariCompact;
import edu.xtec.qv.servlet.rp.compact.GetQuadernAlumneCompact;
import edu.xtec.qv.servlet.rp.compact.GetQuadernDocentCompact;
import edu.xtec.qv.servlet.rp.compact.PrepareQVCompact;
import edu.xtec.servlet.RequestProcessor;
import edu.xtec.servlet.ResourceRP;
import edu.xtec.util.Messages;

/**
 *
 * @author  fbusquet
 */
public class HTTPQVServer extends QVServerEventMaker{
    
    public static final int DEFAULT_PORT = 8081, DEFAULT_TIMEOUT=1200;
    
    /*protected static final String[] URI={"/login", "/main", "/dbAdmin",
    "/userReport", "/actReport", "/img", "/groupReport", "/groupAdmin",
    "/userAdmin", "/resource", "/JClicReportService"};
    
    protected static final Class[] CLS={Login.class, Main.class,
    DbAdmin.class, UserReport.class, ActReport.class, Img.class,
    GroupReport.class, GroupAdmin.class, UserAdmin.class, ResourceRP.class,
    JClicReportService.class};*/
    
    protected static final String[] URI={"/getQuadernAlumne","/getImage","/prepareQV","/getQuadernDocent","/getAssignacio","/getAssignacionsDocent"/*,"/getQualification"*/};
    
    protected static final Class[] CLS={GetQuadernAlumneCompact.class,ImageProxy.class,PrepareQVCompact.class,GetQuadernDocentCompact.class,GetAssignacionsUsuariCompact.class,GetAssignacionsDocentCompact.class/*,GetQualificationCompact.class*/};
    
    HttpThread httpThread;
    
    edu.xtec.util.Messages messages;
    
    /** Creates a new instance of HttpReportServer */
    public HTTPQVServer(Messages messages) {
        super();
        this.messages=messages;
        httpThread=null;
        
        RequestProcessor.setDirectResources(true);
        
    }
    
    public boolean startServer(int port, int timeOut) {
        if(serverRunning()){
            fireReportServerSystemEvent("Server is already started", QVServerEvent.ERROR);
            return false;
        }
        
        try{
            httpThread=new HttpThread(port, timeOut);
            httpThread.start();
        }
        catch(Exception e){
            fireReportServerSystemEvent(e.toString(), QVServerEvent.ERROR);
            return false;
        }
        return true;
    }
    
    public boolean stopServer(){
        if(!serverRunning()){
            fireReportServerSystemEvent("Server is not started!", QVServerEvent.ERROR);
            return false;
        }
        httpThread.stopServer();
        while(serverRunning()){
            Thread.currentThread().yield();
        }
        return true;
    }
    
    protected void finalize() throws Throwable{
        if(serverRunning()) stopServer();
        super.finalize();
    }
    
    public boolean serverRunning(){
        return httpThread!=null;
    }
    
    protected class HttpThread extends Thread{
        
        boolean running;
        ServerSocket ss;
        int socketTimeOut;
        
        HttpThread(int port, int timeOut) throws Exception{
            ss=new ServerSocket(port);
            fireReportServerSystemEvent(Integer.toString(port), QVServerEvent.START);
            socketTimeOut=timeOut;
            running=false;
        }
        
        public void run(){
            try{
                running=true;
                ss.setSoTimeout(1000);
                while(running){
                    try{
                        Socket socket=ss.accept();
                        ClientConnection cc=new ClientConnection(socket);
                        cc.start();
                    }catch(InterruptedIOException ioex){
                        // Timeout. start again...
                    }catch(Exception ex){
                        fireReportServerSystemEvent(ex.toString(), QVServerEvent.ERROR);
                        running=false;
                    }
                }
                ss.close();
                fireReportServerSystemEvent(null, QVServerEvent.STOP);
            }
            catch (IOException e){
                fireReportServerSystemEvent(e.toString(), QVServerEvent.ERROR);
            }
            running=false;
            httpThread=null;
        }
        
        public void stopServer(){
            running=false;
        }
        
        protected class ClientConnection extends Thread{
            
            Socket socket;
            int id;
            
            public ClientConnection(Socket socket) throws IOException{
                this.socket=socket;
                //fireReportServerSocketEvent(socket, null, QVServerEvent.CONNECT);
                //fireReportServerSocketEvent(socket, null, QVServerEvent.CONNECTION);
            }
            
            public void run(){
                RequestProcessor rp=null;
                HTTPRequest re=null;
                try{
                    re=new HTTPRequest(socket);
                    fireReportServerSocketEvent(socket, re.firstLine, QVServerEvent.CONNECTION);

                    String url=re.urlBase;
                    //System.out.println(url);
                    
                    if(url==null || url.length()==0 || url.equals("/"))
                        url=URI[0];
                    
                    Class cl=null;
                    for(int i=0; i<URI.length; i++){
                        if(URI[i].equals(url)){
                            cl=CLS[i];
                            break;
                        }
                    }
                    
                    if(cl==null && url!=null && url.startsWith("/")){
                        String s=url.substring(1);
                        re.params.put(ResourceRP.ID, new String[]{s});
                        //System.out.println(s);
                        cl=ResourceRP.class;
                    }
                    
                    if(cl==null){
                        re.error(HTTPRequest.NOT_FOUND, null);
                    }
                    else{
                        rp=(RequestProcessor)cl.newInstance();
                        rp.setParams(re.params);
                        if(rp.wantsInputStream())
                            rp.setInputStream(re.inputStream);
                        Iterator it=re.cookies.keySet().iterator();
                        while(it.hasNext()){
                            String key=(String)it.next();
                            rp.setCookie(key, (String)re.cookies.get(key));
                        }
                        
                        rp.init();
                        re.head.cache=!rp.noCache();
                        Vector v=new Vector();
                        rp.header(v);
                        
                        if(!v.isEmpty()){
                            it=v.iterator();
                            while(it.hasNext() && !re.head.commited){
                                String[] h=(String[])it.next();
                                if(h[0].equals(RequestProcessor.ERROR)){
                                    int code=Integer.parseInt(h[1]);
                                    re.error(code, h[2]);
                                }
                                else if(h[0].equals(RequestProcessor.REDIRECT)){
                                    re.redirect(h[1]);
                                    break;
                                }
                                else if(h[0].equals(RequestProcessor.CONTENT_TYPE))
                                    re.head.contentType=h[1];
                                else if(h[0].equals(RequestProcessor.CONTENT_LENGTH))
                                    re.head.contentLength=Integer.parseInt(h[1]);
                                else if(h[0].equals(RequestProcessor.EXTRA)){
                                    StringBuffer sb=new StringBuffer(100);
                                    if(re.head.extra!=null)
                                        sb.append(re.head.extra).append("\n\r");
                                    sb.append(h[1]).append(": ").append(h[2]);
                                    re.head.extra=sb.substring(0);
                                }
                                else if(h[0].equals(RequestProcessor.COOKIE))
                                    re.cookies.put(h[1], h[2]);
                            }
                        }
                        if(!re.head.commited){
                            re.head.write();
                            if(rp.usesWriter()){
                                rp.process(re.pw);
                                re.pw.flush();
                                re.pw.close();
                            } else{
                                rp.process(re.os);
                                re.os.flush();
                                re.os.close();
                            }
                            re.commited=true;
                        }
                        rp.end();
                        rp=null;
                    }
                }
                catch(Exception ex){
                    int errCode = (rp!=null && rp.errCode>=0) ? rp.errCode : HTTPRequest.SERVER_ERROR;
                    String errMsg = (rp!=null && rp.errMsg!=null) ? rp.errMsg : ex.getMessage();
                    if(re!=null && !re.head.commited){
                        try{
                            re.error(errCode, errMsg);
                        } catch(Exception ex2){
                            System.err.println("Unable to report error due to\n"+ex2);
                        }
                    }
                    fireReportServerSocketEvent(socket, ex.toString(), QVServerEvent.ERROR);
                    System.err.println("ERROR "+errCode+": "+errMsg);
                    ex.printStackTrace(System.err);
                }
                finally {
                    if(rp!=null)
                        rp.end();
                    try{
                        socket.close();
                    } catch(Exception ex){
                        System.err.println("Unable to close socket due to:\n"+ex);
                    }
                }
            }
        }
    }
    
    protected static boolean isStarted=false;
    
    public static void checkServer(int port, int timeout){
        /* Si no està engegat el servidor, l'engega*/
        if (!isStarted){
            isStarted=true;
            String MSG_BUNDLE="messages.QVServerMessages";
            HTTPQVServer httpServer=new HTTPQVServer(new edu.xtec.util.Messages(MSG_BUNDLE));
            httpServer.startServer(port,timeout);
        }
    }
    
    public static void main(String[] args){
        String MSG_BUNDLE="messages.QVServerMessages";
        HTTPQVServer httpServer=new HTTPQVServer(new edu.xtec.util.Messages(MSG_BUNDLE));
        httpServer.startServer(9200,1000);
        
    }
}
