/*
 * UserConfig.java
 *
 * Created on 12 / novembre / 2002, 13:23
 */

package edu.xtec.qv.util;

/**
 *
 * @author  allastar
 */
public class UserConfig {
    
    private int iUserId;
    private String sLanguage;
    private String sBackGround;
    private String sLook;
    private boolean bIncludeMedia;
    private String sAssigBG=null;
    private String sAssigXSL=null;
    private String sQuadernBG=null;
    private String sQuadernXSL=null;
            
    
    /** Creates a new instance of UserConfig */
    public UserConfig(int iUserId, String sLanguage, String sBackGround, String sLook, boolean bIncludeMedia, String sAssigBG, String sAssigXSL, String sQuadernBG, String sQuadernXSL){
        this.iUserId=iUserId;
        this.sLanguage=sLanguage;
        this.sBackGround=sBackGround;
        this.sLook=sLook;
        this.bIncludeMedia=bIncludeMedia;
        this.sAssigBG=sAssigBG;
        this.sAssigXSL=sAssigXSL;
        this.sQuadernBG=sQuadernBG;
        this.sQuadernXSL=sQuadernXSL;
    }
    
    public UserConfig(int iUserId, String sLanguage, java.awt.Color cBackGround, String sLook, boolean bIncludeMedia){
    	this(iUserId,sLanguage,cBackGround!=null?(cBackGround.getRed()+","+cBackGround.getGreen()+","+cBackGround.getBlue()):"",sLook,bIncludeMedia,null,null,null,null);
    }

    public static UserConfig getDefault(int iUserId){
        String sQuadernXSLName=QuadernConfig.getDefaultXSLName();
        String sAssigXSLName=QuadernConfig.getDefaultAssigXSLName();
        String sQuadernBG=QuadernConfig.getProperty("defaultQuadernBG");
        String sAssigBG=QuadernConfig.getProperty("defaultAssigBG");        
    	return new UserConfig(iUserId,"ca","",javax.swing.UIManager.getSystemLookAndFeelClassName(),false,sAssigBG,sAssigXSLName,sQuadernBG,sQuadernXSLName);
    }
    
    public int getUserId(){
        return iUserId;
    }
    
    public String getLanguage(){
        return sLanguage;
    }
    
    public String getBackGround(){
        return sBackGround;
    }
    
    public java.awt.Color getBackGroundColor(){
        java.awt.Color c=null;
        java.util.StringTokenizer st=new java.util.StringTokenizer(sBackGround);
        try{
            int red=-1;
            int green=-1;
            int blue=-1;
            if (st.hasMoreTokens()) red=Integer.parseInt(st.nextToken());
            if (st.hasMoreTokens()) green=Integer.parseInt(st.nextToken());
            if (st.hasMoreTokens()) blue=Integer.parseInt(st.nextToken());
            if (red>=0 && green>=0 && blue>=0) c=new java.awt.Color(red,green,blue); 
        }
        catch(Exception e){
            //System.out.println("Excepció:"+e);
        }
        return c;
    }
    
    public String getLook(){
    	return sLook;
    }
    
    public boolean getIncludeMedia(){
    	return bIncludeMedia;
    }
    
    public String getAssigBG(){
        return sAssigBG;
    }
    
    public String getQuadernBG(){
        return sQuadernBG;
    }
    
    public String getAssigXSL(){
        return sAssigXSL;
    }
    
    public String getQuadernXSL(){
        return sQuadernXSL;
    }
    
    public void setQuadernXSL(String sXSL){
        sQuadernXSL=sXSL;
    }
    
    public boolean equals(UserConfig uc){
    	if (iUserId==uc.iUserId && sLanguage.equals(uc.sLanguage) &&
    		sBackGround.equals(uc.sBackGround) && sLook.equals(uc.sLook) &&
    		bIncludeMedia==uc.bIncludeMedia) return true;
    	else return false;
    }
    
    public String toString(){
    	String s="user:"+iUserId+" lang:"+sLanguage+" back:"+sBackGround+" look:"+sLook+" media?"+bIncludeMedia;
    	return s;
    }
}
