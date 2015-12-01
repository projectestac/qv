/*
 * FileHistory.java
 *
 * Created on 26 / juny / 2003, 12:45
 */

package edu.xtec.qv.editor.util;

/**
 *
 * @author  allastar
 */

import java.io.*;

public class FileHistory {
    
    public static java.util.Properties prop=null;
    public static final int maxEntries=10;
    private static File f=null;
    
    /** Creates a new instance of FileHistory */
    public FileHistory() {
    }
    
    public static void addRecentFile(String sName, String sPath){
        //System.out.println("addRecentFile("+sName+","+sPath+")");
        int iPosition=getFilePosition(sName);
        if (iPosition>=0){
            setProperty("file"+iPosition+"_name",null);
        }
        moveDown();
        setProperty("file0_name",sName);
        setProperty("file0_path",sPath);

        try{ //Emmagatzemo els canvis
            prop.store(new FileOutputStream(f),"");
        }
        catch (Exception e){
            e.printStackTrace(System.out);
        }
    }
    
    public static String[] getRecentFileNames(){
        String[] entries=new String[maxEntries];
        for (int i=0;i<maxEntries;i++) entries[i]=getFileName(i);
        return entries;
    }
    
    public static String getFilePath(String sName){
        int i=getFilePosition(sName);
        if (i>=0) return getFilePath(i);
        else return null;
    }
    
    private static void iniConfiguration(){
        try{
            if (prop==null){
                prop=new java.util.Properties();
                try{
                    String sUserHome=System.getProperty("user.home");
                    //File dir=new File(sUserHome);
                    f=new File(sUserHome+System.getProperty("file.separator")+"qv_history.properties");
                    if (!f.exists()) f.createNewFile();
                    FileInputStream fis=new FileInputStream(f);
                    prop.load(fis);
                }
                catch (Exception e){
                }
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            e.printStackTrace(System.out);
            prop=null;
        }
    }
    
    private static String getProperty(String sPropertyName){
        if (prop==null) iniConfiguration();
        if (prop!=null && prop.containsKey(sPropertyName)){
            String sValue=prop.getProperty(sPropertyName);
            return sValue;
        }
        else return null;
    }
    
    private static void setProperty(String sPropertyName, String sValue){
        if (prop==null) iniConfiguration();
        if (prop!=null){
            //System.out.println("propName="+sPropertyName+" value:"+sValue+"<--");
            if (sValue==null) prop.remove(sPropertyName);
            else prop.setProperty(sPropertyName,sValue);
        }
    }
    
    private static String getFileName(int iPosition){
        return getProperty("file"+iPosition+"_name");
    }
    
    public static String getFilePath(int iPosition){
        return getProperty("file"+iPosition+"_path");
    }
    
    private static int getFilePosition(String sName){
        boolean bFound=false;
        int i=0;
        while (i<maxEntries && !bFound){
           String s=getFileName(i);
           bFound=(s!=null && s.equals(sName));
           if (!bFound) i++;
        }
        return bFound?i:-1;
    }
    
    private static void moveDown(){
        String sNameNext=getFileName(0);
        String sPathNext=getFilePath(0);
        if (sNameNext!=null) moveDown(1,sNameNext,sPathNext);
    }
    
    private static void moveDown(int iPosition, String sName, String sPath){
        if (iPosition<maxEntries){
            String sNameNext=getFileName(iPosition);
            String sPathNext=getFilePath(iPosition);
            setProperty("file"+iPosition+"_name",sName);
            setProperty("file"+iPosition+"_path",sPath);
            //System.out.println("nameNext:"+sNameNext+" poso a la posició "+iPosition+" el nom "+sName);
            if (sNameNext!=null) moveDown(iPosition+1,sNameNext,sPathNext);
        }
    }
    
}
