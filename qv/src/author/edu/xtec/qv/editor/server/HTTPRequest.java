/*
 * HTTPRequest.java
 *
 * Created on 13 de febrero de 2003, 15:19
 */

package edu.xtec.qv.editor.server;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.io.InputStreamReader;
import java.io.BufferedOutputStream;
import java.util.StringTokenizer;
import edu.xtec.util.Html;
import java.util.Iterator;
import edu.xtec.servlet.RequestProcessor;
import java.io.OutputStreamWriter;
import java.io.InputStream;

/**
 *
 * @author  fbusquet
 */
public class HTTPRequest {
    
    public Socket socket;
    public BufferedReader in;
    public OutputStream os;
    public PrintWriter pw;
    public HashMap cookies=new HashMap();
    public HashMap params=new HashMap();
    public String method;
    public String urlBase;
    public String protocol;
    public boolean commited;
    public String firstLine;
    public InputStream inputStream;
    
    ResponseHead head=new ResponseHead();
    
    public static final int OK=200, BAD_REQUEST=400, MOVED_PERM=301, 
    FOUND=302, NOT_FOUND=404, SERVER_ERROR=500;
    public static final String MIME_HTML="text/html";


    /** Creates a new instance of HTTPRequest */
    public HTTPRequest(Socket socket) throws Exception {
        
        this.socket=socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        os = new BufferedOutputStream(socket.getOutputStream());
        pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), RequestProcessor.CHARSET));
        int cl=-1;

        try{
            firstLine = in.readLine();
            //System.out.println(firstLine);
            if(firstLine==null){
                while (in.ready())
                    in.readLine();                
                return;
            }
            StringTokenizer st=new StringTokenizer(firstLine, " ");
            method=st.nextToken();
            String s=st.nextToken();
            int i=s.indexOf('?');
            if(i<0)
                urlBase=s;
            else{
                urlBase=s.substring(0, i);
                processParamsLine(s.substring(i+1), params, '&', '=', true);
            }
            protocol=st.nextToken();
            if(protocol==null || !protocol.equals("HTTP/1.0") && !protocol.equals("HTTP/1.1")){
                error(BAD_REQUEST, null);
                throw new Exception("Bad request!");
            }
        }catch(Exception ex){
            error(BAD_REQUEST, null);
            throw ex;
        }
				
        while (true) {
            String line = in.readLine();
            //System.out.println(line);
            if (line==null || line.trim().length()==0)
                break;
            else if(line.toLowerCase().startsWith("cookie:")){
                int k=line.indexOf(' ');
                if(k>0)
                    processParamsLine(line.substring(k+1), cookies, ';', '=', false);
            }
            else if(line.toLowerCase().startsWith("content-length:")){
                cl=Integer.parseInt(line.substring(16));
            }
        }
        if(cl>=0){
            char[] buf=new char[cl];
            int k=in.read(buf);
            String line=String.copyValueOf(buf);            
            inputStream=new java.io.ByteArrayInputStream(line.getBytes());
            //System.out.println(line);
            if(!line.startsWith("<"))
                processParamsLine(line, params, '&', '=', true);
        }
        else{
            while (in.ready()) {
                ////String line = in.readLine();
                String line = readLine(in);
                //System.out.println(line);
                if (line==null || line.length()==0)
                    break;
                processParamsLine(line, params, '&', '=', true);
            }
        }
    }
    
    public String readLine(BufferedReader br){
    	StringBuffer sb=new StringBuffer();
/*    	boolean b=false;
    	char [] cA=new char[1];
    	try{
    		while (!b && br.ready()){
    			System.out.println("-a-");
    			int i=br.read();//cA,0,1);
    			System.out.println("-b-");
    			if (i==1){
	    			char c=(char)i;//cA[0];
  	  			sb.append(c);
    				b=(c=='\n' || i<0);
    				System.out.println("-c="+b);
    			}
    			else b=true;
    		}
    	}
    	catch (java.io.IOException e){
    	}*/
    	return sb.toString();
    }
    
    public static void processParamsLine(String txt, java.util.HashMap map, char sep, char equalSign, boolean arrays){
        if(txt!=null && txt.length()>0 && map!=null){
            StringTokenizer st=new StringTokenizer(txt, String.valueOf(sep));
            while(st.hasMoreTokens()){
                String s=st.nextToken().trim();
                String key=null, value=null;
                int k=s.indexOf(equalSign);
                if(k>0){
                    key=Html.decode(s.substring(0, k).replace('+', ' '));
                    if(k<s.length()-1)
                        value=Html.decode(s.substring(k+1).replace('+', ' '));
                }
                else
                    key=Html.decode(s.replace('+', ' '));
                
                if(arrays){                
                    String[] vArray=(String[])map.get(key);
                    if(vArray==null){
                        vArray=new String[]{value};
                    } else{
                        String[] v2=new String[vArray.length+1];
                        int i=0;
                        for(; i<vArray.length; i++)
                            v2[i]=vArray[i];
                        v2[i]=value;
                        vArray=v2;
                    }
                    map.put(key, vArray);
                } else{
                    map.put(key, value);
                }
            }
        }
    }    
    
    public void error(int code, String msg) throws Exception{
        String err=null;
        switch(code){
            case BAD_REQUEST: err="Bad Request"; break;
            case MOVED_PERM: err="Moved permanently"; break;
            case NOT_FOUND: err="File not found"; break;
            case SERVER_ERROR: err="Server error"; break;
            default: err="Undefined error";
        }
        head.code=code;
        head.title=err;
        head.write();        
        StringBuffer sb=new StringBuffer(100);
        sb.append("<B>").append(err).append("</B>\n<BR>&nbsp;<BR>\n");
        if(msg!=null)
            sb.append(msg);
        minimalPage("ERROR: "+err, sb.substring(0));
    }
    
    public void redirect(String url) throws Exception{
        head.contentType=null;
        head.code=MOVED_PERM;
        head.title="Moved permanently";
        StringBuffer sb=new StringBuffer(100);
        sb.append("http://");
        sb.append(socket.getLocalAddress().getHostAddress());
        sb.append(":").append(socket.getLocalPort()).append("/").append(url);
                
        String fullUrl=sb.substring(0);
        sb.setLength(0);
        sb.append("Location: ").append(fullUrl);
        head.extra=sb.substring(0);
        head.write();
        sb.setLength(0);
        sb.append("redirected to: <A HREF=\"").append(fullUrl).append("\">").append(fullUrl).append("</A>");
        minimalPage("redirect", sb.substring(0));
    }
    
    public class ResponseHead{
        public String contentType;
        public int code, contentLength;
        public String title, extra;
        public boolean cache;
        public boolean commited;
        
        public ResponseHead(){
            contentType=MIME_HTML;
            code=OK;
            cache=true;
            commited=false;
            contentLength=-1;
        }
        
        public void write(){
            
            StringBuffer sb=new StringBuffer(200);
            sb.append("HTTP/1.0 ");
            sb.append(code).append(" ").append(title==null ? "OK" : title);
            pw.println(sb.substring(0));
            
            if(extra!=null)
                pw.println(extra);
            
            if(contentType!=null){
                sb.setLength(0);
                pw.println(sb.append("Content-Type: ").append(contentType).substring(0));
            }
           
            sb.setLength(0);
            pw.println(sb.append("Date: ").append(RequestProcessor.httpDate(new java.util.Date())).substring(0));
            pw.println("Server: QVHttpServer 1.0");
            if (!cache){
                pw.println("Pragma: no-cache");//HTTP/1.0
                pw.println("Cache-Control: no-cache");//HTTP/1.1
                pw.println("Expires: 0");
            }
            
            if(cookies.size()>0){
                sb.setLength(0);
                sb.append("Set-Cookie: ");
                Iterator it=cookies.keySet().iterator();
                boolean first=true;
                while(it.hasNext()){
                    String key=(String)it.next();
                    if(!first)
                        sb.append(";");
                    else
                        first=false;
                    sb.append(key).append("=").append(cookies.get(key));
                }
                pw.println(sb.substring(0));
            }
            
            if(contentLength>0){
                sb.setLength(0);
                pw.println(sb.append("Content-Length: ").append(contentLength).substring(0));
            }
            
            pw.println("");
            pw.flush();
            commited=true;
        }
    }
    
    public void minimalPage(String title, String text) throws Exception{
        StringBuffer sb=new StringBuffer(1024);
        sb.append("<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n");
        sb.append("<HTTP>\n<HEAD>\n");
        if(title!=null)
            sb.append("<TITLE>").append(title).append("</TITLE>\n");
        sb.append("</HEAD>\n<BODY>\n");
        sb.append("<H2>").append(title).append("</H2>\n");
        sb.append(text);
        sb.append("\n</BODY>\n</HTML>");
        pw.println(sb.substring(0));
        os.flush();
        commited=true;
    }

}
