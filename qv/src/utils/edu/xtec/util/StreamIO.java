/*
 * Utils.java
 *
 * Created on 6 / febrer / 2001, 18:30
 */

package edu.xtec.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

/**
 *
 * @author  fbusquet
 * @version
 */
public abstract class StreamIO{
    
    public static boolean cancel=false;
    
    public interface InputStreamListener{
        public void notify(InputStream in, int bytesRead);
    }
    
    public static int DEFAULT_READ_STEP_SIZE=1024;
    
    public static byte[] readInputStream(InputStream is) throws IOException{
        return readInputStream(is, null, DEFAULT_READ_STEP_SIZE);
    }
    
    public static byte[] readInputStream(InputStream is, InputStreamListener lst, int stepSize) throws IOException{
        cancel=false;
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
            if(lst!=null) lst.notify(is, bytesRead);
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
    
    public static byte[] readFile(File file) throws IOException{
        return readFile(file, null, 0);
    }
    
    public static byte[] readFile(File file, InputStreamListener lst, int stepSize) throws IOException{
        cancel=false;        
        long fileLength = file.length();
        if(/*fileLength<1 ||*/ fileLength>Integer.MAX_VALUE)
            throw new IOException();
        int intFileLength=(int)fileLength;
        byte[] result=new byte[intFileLength];
        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
        for(int k=0; k<fileLength && !cancel;){
            int bytesToRead= intFileLength-k;
            if(stepSize>0 && stepSize<bytesToRead) bytesToRead=stepSize;
            int bytesRead=fis.read(result, k, bytesToRead);
            if(bytesRead<0) break;
            if(lst!=null) lst.notify(fis, bytesRead);
            k+=bytesRead;
            Thread.yield();
        }
        fis.close();
        if(cancel)
            throw new InterruptedIOException("Cancelled by user");            
        return result;
    }
    
    public static byte[] getResourceBytes(Object caller, String packageName, String resourceName) throws IOException{
        return readInputStream(caller.getClass().getResourceAsStream(packageName+"/"+resourceName));
    }
    
    public static void writeStreamTo(InputStream is, OutputStream os) throws IOException{
        writeStreamTo(is, os, null, DEFAULT_READ_STEP_SIZE);
    }
    
    public static void writeStreamTo(InputStream is, OutputStream os, InputStreamListener lst, int stepSize) throws IOException{
        cancel=false;        
        BufferedInputStream bufferedStream = null;
        if(is instanceof BufferedInputStream)
            bufferedStream=(BufferedInputStream)is;
        else
            bufferedStream = new BufferedInputStream(is);
        
        int bytesRead = 0;
        byte[] buf = new byte[stepSize];
        while(!cancel){
            bytesRead=bufferedStream.read(buf, 0, stepSize);
            if(lst!=null) lst.notify(is, bytesRead);
            if(bytesRead<=0) break;
            os.write(buf, 0, bytesRead);
            Thread.yield();
        }
        bufferedStream.close();
        os.flush();
        os.close();
        if(cancel)
            throw new InterruptedIOException("Cancelled by user");            
    }
    
    public interface InputStreamProvider{
        public java.io.InputStream getInputStream(String resourceName) throws Exception;
    }    
}
