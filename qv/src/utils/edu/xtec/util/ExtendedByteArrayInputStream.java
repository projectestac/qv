/*
 * ExtendedByteArrayInputStream.java
 *
 * Created on 21 de septiembre de 2001, 10:14
 */

package edu.xtec.util;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 *
 * @author  fbusquet
 * @version
 */
public class ExtendedByteArrayInputStream extends ByteArrayInputStream {
    
    protected String m_name;
    
    /** Creates new ExtendedByteArrayInputStream */
    public ExtendedByteArrayInputStream(byte[] buffer, String name){
        super(buffer);
        m_name=name;
    }
    
    public ExtendedByteArrayInputStream(byte[] buffer, int offset, int length, String name){
        super(buffer, offset, length);
        m_name=name;
    }
    
    public ExtendedByteArrayInputStream duplicate(){
        return new ExtendedByteArrayInputStream(buf, m_name);
    }
    
    public String getName(){
        return m_name;
    }
    
    public int getPos(){
        return pos;
    }
    
    public int getMark(){
        return mark;
    }
    
    public int getCount(){
        return count;
    }
    
    public boolean eosReached(){
        return pos>=count;
    }
    
    public long seek(long param) throws IOException{
        pos=0;
        return skip(param);
    }
    
    public byte[] getBuffer(){
        return buf;
    }    
}

