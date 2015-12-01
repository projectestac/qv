package edu.xtec.qv.servlet.util;

/*
 * BufferedURL.java
 *
 * Created on 3 / març / 2003, 17:31
 */

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.HashMap;

/**
 *
 * @author  allastar
 */
public class BufferedURL {
    
    protected static HashMap hmBytes;
    
    /** Creates a new instance of BufferedURL */
    public BufferedURL() {
        hmBytes=new HashMap();
    }
    
    public static void clearURL(java.net.URL u){
		if (hmBytes!=null && u!=null){
			String sKey = u.toString();
			hmBytes.remove(sKey);
		}
    }
    
    public static java.io.InputStream getInputStreamForURL(java.net.URL u){
        if (hmBytes==null) hmBytes=new HashMap();
        byte[] bA=null;
        String sKey=u.toString();
        if (hmBytes.containsKey(sKey)){
            System.out.println("URL: "+sKey+" obtinguda des de memòria.");
            bA=(byte[])hmBytes.get(sKey);
        }
        else{
            try{
                InputStream is=u.openStream();
                bA=readInputStream(is,1024);
                //hmBytes.put(sKey,bA);
                //System.out.println("URL: "+sKey+" emmagatzemada a memòria.");
            }
            catch (IOException e){
                System.out.println("Excepció en llegir URL:"+sKey+"<--");
            }
        }
        if (bA!=null) return new java.io.ByteArrayInputStream(bA);
        else return null;
    }
    
	public static java.io.InputStream getInputStreamForURLWithoutBuffered(java.net.URL u){
		byte[] bA=null;
		try{
			InputStream is=u.openStream();
			bA=readInputStream(is,1024);
		}
		catch (IOException e){
			System.out.println("Excepció en llegir URL:"+u+"<--");
		}
		if (bA!=null) return new java.io.ByteArrayInputStream(bA);
		else return null;
	}
    
    public static java.io.InputStream getServletInputStreamForURL(javax.servlet.http.HttpServlet servlet, java.net.URL u){
        if (hmBytes==null) hmBytes=new HashMap();
        byte[] bA=null;
        java.io.ByteArrayInputStream bais=null;
        String sKey=u.toString();
        Object o=servlet.getServletContext().getAttribute(sKey); 
        if (o!=null){
            //System.out.println("URL: "+sKey+" obtinguda des context de servlet.");
            bA=(byte[])o;//hmBytes.get(sKey);
            //bais=(java.io.ByteArrayInputStream)o;
            bais=new ByteArrayInputStream(bA);
            ////bais.reset();
        }
        else{
            try{
                InputStream is=u.openStream();
                bA=readInputStream(is,1024);
                servlet.getServletContext().setAttribute(sKey,bA);
                bais=new java.io.ByteArrayInputStream(bA);//
                //servlet.getServletContext().setAttribute(sKey,bais);
                //System.out.println("URL: "+sKey+" emmagatzemada a memòria.");
            }
            catch (IOException e){
                System.out.println("Excepció en llegir URL:"+sKey+"<--");
            }
        }
        //if (bA!=null) return new java.io.ByteArrayInputStream(bA);
        //else return null;
        return bais;
    }
    
    protected static byte[] readInputStream(InputStream is, int stepSize) throws IOException{
        boolean cancel=false;
        BufferedInputStream bufferedStream = null;
        if(is instanceof BufferedInputStream)
            bufferedStream=(BufferedInputStream)is;
        else
            bufferedStream = new BufferedInputStream(is);
        
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        byte[] buffer=new byte[stepSize];
        int bytesRead;
        while(!cancel){
            //bytesRead=is.read(buffer);
            bytesRead=bufferedStream.read(buffer);
            if(bytesRead<=0) break;
            os.write(buffer, 0, bytesRead);
            Thread.yield();
        }
        buffer=os.toByteArray();
        os.close();
        //is.close();
        bufferedStream.close();
        if(cancel)
            throw new InterruptedIOException("Cancelled by user");            
        return buffer;
    }
}
