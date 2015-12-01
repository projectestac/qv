package edu.xtec.qv.servlet.util;

import edu.xtec.qv.util.Utility;


/*
 * AssignacioQuadernIndividualInfo.java
 *
 * Created on 11 de septiembre de 2002, 12:03
 */


/**
 *
 * @author  Albert
 * @version 
 */
public class AssignacioQuadernIndividualInfo extends Object {

    int iIdAlumne;
    //int iIdAssignacio;
    String sIdAssignacio;
    String sNomAlumne;
    String sGrup;
    String sEstatResposta;
    String sResposta;
    String sFeedback;
    String sInteraccio;
    int iNumEntregues;
    int iPuntuacio;
    java.util.Date dUltimaEntrega;
    String sTime;
    
    /** Creates new AssignacioQuadernIndividualInfo */
    public AssignacioQuadernIndividualInfo(int iIdAlumne, String sIdAssignacio, String sNomAlumne, String sGrup,
        String sEstatResposta, String sResposta, String sFeedback, String sInteraccio, int iNumEntregues, int iPuntuacio, java.util.Date dUltimaEntrega) {
            
            this(iIdAlumne, sIdAssignacio, sNomAlumne, sGrup, sEstatResposta, sResposta, sFeedback,
                sInteraccio, iNumEntregues, iPuntuacio, dUltimaEntrega,null);
    }

	public AssignacioQuadernIndividualInfo(String sIdAssignacio, String sNomAlumne, String sGrup,
		String sEstatResposta, String sResposta, String sFeedback, String sInteraccio, int iNumEntregues, int iPuntuacio, java.util.Date dUltimaEntrega) {
			this(-10/*idAlumne*/,sIdAssignacio,sNomAlumne,sGrup,sEstatResposta,sResposta,sFeedback,sInteraccio,iNumEntregues,iPuntuacio,dUltimaEntrega);
			this.sIdAssignacio=sIdAssignacio;
	}
    
    public AssignacioQuadernIndividualInfo(int iIdAlumne, String sIdAssignacio, String sNomAlumne, String sGrup,
        String sEstatResposta, String sResposta, String sFeedback, String sInteraccio, int iNumEntregues, int iPuntuacio, java.util.Date dUltimaEntrega, String sTime) {
            this.iIdAlumne=iIdAlumne;
            this.sIdAssignacio=sIdAssignacio;
            this.sNomAlumne=sNomAlumne;
            this.sGrup=sGrup;
            this.sEstatResposta=sEstatResposta;
            this.sResposta=sResposta;
            this.sFeedback=sFeedback;
            this.sInteraccio=sInteraccio;
            this.iNumEntregues=iNumEntregues;
            this.iPuntuacio=iPuntuacio;
            this.dUltimaEntrega=dUltimaEntrega;
            this.sTime=sTime;
    }
    
    public int getIdAlumne(){
        return iIdAlumne;
    }
    
    /*public int getIdAssignacio(){
        return iIdAssignacio;
    }*/
    
    public String getIdAssignacio(){
        return sIdAssignacio;
    }
    
    public String getNomAlumne(){
        return sNomAlumne;
    }
    
    public String getGrup(){
        return sGrup;
    }
    
    public String getEstatResposta(){
        return sEstatResposta;
    }
    
    public String getResposta(){
        return sResposta;
    }
    
    public String getFeedback(){
        return sFeedback;
    }
    
    public String getInteraccio(){
        return sInteraccio;
    }
    
    public int getNumEntregues(){
        return iNumEntregues;
    }
    
    public int getPuntuacio(){
        return iPuntuacio;
    }
    
    public java.util.Date getUltimaEntrega(){
        return dUltimaEntrega;
    }
    
    public String getTime(){
        return (sTime!=null)?sTime:"00:00:00";
    }
    public void setGrup(String sNomGrup){
        sGrup=sNomGrup;
    }
    
    public void setResposta(String sResposta){
        this.sResposta=sResposta;
    }
    
    public void setFeedback(String sFeedback){
        this.sFeedback=sFeedback;
    }
    
    public void setEstatEntrega(String sEstatEntrega){
        this.sEstatResposta=sEstatEntrega;
    }
    
    public void setInteraccio(String sInteraccio){
        this.sInteraccio=sInteraccio;
    }
    
    public void setPuntuacio(int iPuntuacio){
    	this.iPuntuacio=iPuntuacio;
    }
    
    public void setTime(String sTime){
        this.sTime=sTime;
    }
    
    public void addTime(String sTime){
        setTime(Utility.addTime(getTime(),sTime));
    }
    
    public void incLliuraments(){
    	iNumEntregues++;
    }
    
    public Object clone(){
        return new AssignacioQuadernIndividualInfo(iIdAlumne,sIdAssignacio,sNomAlumne,sGrup,
            sEstatResposta,sResposta,sFeedback,sInteraccio,iNumEntregues,iPuntuacio,dUltimaEntrega,sTime);
    }
    
    public String toString(){
        String s=sNomAlumne; // Important no canviar-ho perquè s'utilitza per mostrar-ho a la taula com a nom de l'alumne.
        //+" "+sGrup+" "+sEstatResposta+" "+sResposta+" "+iNumEntregues+" "+iPuntuacio;
        return s;
    }
}
