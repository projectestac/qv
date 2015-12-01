package edu.xtec.qv.servlet.util;
/*
 * AssignacioQuadernData.java
 *
 * Created on 10 de septiembre de 2002, 9:41
 */

/**
 *
 * @author  allastar
 */
public class AssignacioQuadernData {
    
    public static int SENSE_LIMIT_ENTREGUES=-1;

    protected String sId;
    protected String sNom;
    protected String sQuadern;
    protected String sDescripcio;
    protected java.util.Date dAssignacio;
    protected java.util.Date dEntrega;
    protected java.util.Date dDesaparicioInfo;
    protected boolean bCorreccioAutomatica;
    protected int iMaximEntregues;
    protected boolean bContestar;
    protected boolean bAutoAdvance;
    protected boolean bWriting;
    protected String[] clicInfo;
    protected String sQuadernXSL=null;
    protected String sEndPage=null;

    /** Creates a new instance of AssignacioQuadernData */
    public AssignacioQuadernData(String sId,String sNom,String sQuadern,String sDescripcio,java.util.Date dAssignacio,
        java.util.Date dEntrega,java.util.Date dDesaparicioInfo,boolean bCorreccioAutomatica,int iMaximEntregues, boolean bContestar, boolean bAutoAdvance, boolean bWriting, String[] clicInfo, String sQuadernXSL, String sEndPage){
            this.sId=sId;
            this.sNom=sNom;
            this.sQuadern=sQuadern;
            this.sDescripcio=sDescripcio;
            this.dAssignacio=dAssignacio;
            this.dEntrega=dEntrega;
            this.dDesaparicioInfo=dDesaparicioInfo;
            this.bCorreccioAutomatica=bCorreccioAutomatica;
            this.iMaximEntregues=iMaximEntregues;        
            this.bContestar=bContestar;
            this.bAutoAdvance=bAutoAdvance;
            this.bWriting=bWriting;
            this.clicInfo=clicInfo;
            this.sQuadernXSL=sQuadernXSL;
            this.sEndPage=sEndPage;
    }
    
    public void setData(String sIdAssignacio, String sNom, String sQuadern, String sDescripcio,
        java.util.Date dAssignacio,java.util.Date dEntrega,java.util.Date dDesaparicioInfo,
        boolean bCorreccioAutomatica, int iMaximEntregues, boolean bWriting){
    
        this.sId=sIdAssignacio;
        this.sNom=sNom;
        this.sQuadern=sQuadern;
        this.sDescripcio=sDescripcio;
        this.dAssignacio=dAssignacio;
        this.dEntrega=dEntrega;
        this.dDesaparicioInfo=dDesaparicioInfo;
        this.bCorreccioAutomatica=bCorreccioAutomatica;
        this.iMaximEntregues=iMaximEntregues;
        this.bWriting=bWriting;
    }
    
    public String getId(){
        return sId;
    }
    
    public String getNom(){
        return sNom;
    }

    public String getQuadern(){
        return sQuadern;
    }
    
    public String getDescripcio(){
        return sDescripcio;
    }
    
    public java.util.Date getDataAssignacio(){
        return dAssignacio;
    }
    
    public java.util.Date getDataEntrega(){
        return dEntrega;
    }
    
    public java.util.Date getDataDesaparicio(){
        return dDesaparicioInfo;
    }
    
    public boolean isCorreccioAutomatica(){
        return bCorreccioAutomatica;
    }
    
    public int getMaximEntregues(){
        return iMaximEntregues;
    }
    
    public boolean isSenseLimitEntregues(){
        return iMaximEntregues==SENSE_LIMIT_ENTREGUES;
    }
    
    public boolean getContestar(){
        return bContestar;
    }
    
    public boolean isAutoAdvance(){
        return bAutoAdvance;
    }
    
    public boolean isWritingEnabled(){
        //Retorna si es poden afegir intervencions al quadern
        return bWriting;
    }
    
    public String[] getClicInfo(){
        return clicInfo;
    }
    
    public String getClicSessionKey(){
        return "qv_"+getId();
    }
    
    public String getQuadernXSL(){
        return sQuadernXSL;
    }

    public String getEndPage(){
        return sEndPage;
    }

}
