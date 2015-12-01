/*
 * Locator.java
 *
 * Created on 22 / maig / 2003, 09:51
 */

package edu.xtec.qv.util;

/**
 *
 * @author  allastar
 */
public class Locator {
    
    static java.io.File f;
    java.io.File locationFile;
    //String sPath; //Acabarà en '/'
    
    /** Creates a new instance of Locator */
    public Locator(java.io.File f) {
        Locator.f=f;
        locationFile=f;
        /*if (f==null) System.out.println("constr: f==null!!!");
        else System.out.println("constr: "+f.getPath());*/
    }
    
    public void setToCurrentLocation(){
        f=locationFile;
    }
    
    public static java.io.File getLocation(){
        return f;
    }
    
    public static java.io.File getLocationDirectory(){
        if (f!=null) return f.getParentFile();
        else return null;
    }

    public static String getRelativePath(java.io.File f2){
        return getRelativePath(f.getAbsolutePath(),f2.getAbsolutePath());
    }
    
    private static String getRelativePath(String s1, String s2){
        //System.out.println("s1:"+s1+" s2:"+s2);
        int i1=s1.indexOf('/');
        int i2=s2.indexOf('/');
        int i12=s1.indexOf('\\');
        int i22=s2.indexOf('\\');
        if (i1>=0 && i12>=0) i1=Math.min(i1,i12);
        else if (i1<0 && i12>=0) i1=i12;
        if (i2>=0 && i22>=0) i1=Math.min(i2,i22);
        else if (i2<0 && i22>=0) i2=i22;       
        
        if (i1>=0 && i2>=0){
            String pref1=s1.substring(0,i1);
            String pref2=s2.substring(0,i2);
            if (pref1.equals(pref2)) return getRelativePath(s1.substring(i1+1),s2.substring(i2+1));
            else return "../"+getRelativePath(s1.substring(i1+1),"")+s2;
            //else return initialS2;
        }
        else if (i1>=0){ //s2 és anterior...
            //return initialS2;
            return "../"+getRelativePath(s1.substring(i1+1),s2);
        }
        else if (i2>=0) return s2;
        else if (s1.trim().length()==0) return s2; //Cap dels 2 té '/'
        else return "";
    }
    
    public static String getAbsolutePath(String sRelativePath){
        if (f==null) System.out.println("f==null!!!");
        //else System.out.println(""+f.getPath());
            
        if (sRelativePath!=null && !sRelativePath.startsWith("http://") && sRelativePath.indexOf(":/")<0 && sRelativePath.indexOf(":\\")<0){
            return (f!=null && f.getParentFile()!=null)?(f.getParentFile().getPath()+"/"+sRelativePath):sRelativePath;
        }
        else return sRelativePath;
    }
    
    /*public static void main(String[] args){
        Locator l=new Locator(null);
        String s1="d:/abc/cde/f/";
        String s2="d:/abc/d/a.txt";
        System.out.println("-->"+l.getRelativePath(s1,s2)+"<--");
    }*/
    
}
