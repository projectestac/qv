package edu.xtec.qv.servlet.util;

import java.util.Vector;

import edu.xtec.qv.util.Utility;


public class FullAssignacioQuadernIndividualInfo extends Object {

    //int iIdAlumne;
    //int iIdAssignacio;
    String sIdAssignacio;
    int iNumFull;
    String sNomFull;
    String sEstatLliurament;
    //String sResposta;
    Vector vRespostes;
    Vector vFeedbacks;
    Vector vInteraccions;
    String sAskingInteraction=null; /* Només es fa servir temporalment per tal d'indicar que l'alumne ha demanat omplir interacció */
    String sTime;
    int iNumLliuraments;
    int iPuntuacio;
    java.util.Date dUltimaEntrega;
    boolean bCorrected;
    //String sScoringRepresentation;
    Vector vPreguntes;
    
    /** Creates new AssignacioQuadernIndividualInfo */
    public FullAssignacioQuadernIndividualInfo(String sIdAssignacio, int iNumFull, String sNomFull,
        String sEstatLliurament, Vector vRespostes, Vector vFeedbacks, Vector vInteraccions,
        int iNumLliuraments, int iPuntuacio, java.util.Date dUltimaEntrega, boolean bCorrected,Vector vPreguntes) {
            this(sIdAssignacio, iNumFull, sNomFull, sEstatLliurament, vRespostes, vFeedbacks, vInteraccions,
                iNumLliuraments, iPuntuacio, dUltimaEntrega, null, bCorrected, vPreguntes);
    }
    
    public FullAssignacioQuadernIndividualInfo(String sIdAssignacio, int iNumFull, String sNomFull,
        String sEstatLliurament, Vector vRespostes, Vector vFeedbacks, Vector vInteraccions,
        int iNumLliuraments, int iPuntuacio, java.util.Date dUltimaEntrega, String sTime, boolean bCorrected, Vector vPreguntes) {
            //this.iIdAlumne=iIdAlumne;
            this.sIdAssignacio=sIdAssignacio;
            this.iNumFull=iNumFull;
            this.sNomFull=sNomFull;
            this.sEstatLliurament=sEstatLliurament;
            this.vRespostes=vRespostes;
            this.vFeedbacks=vFeedbacks;
            this.vInteraccions=vInteraccions;
            this.iNumLliuraments=iNumLliuraments;
            this.iPuntuacio=iPuntuacio;
            this.dUltimaEntrega=dUltimaEntrega;
            this.sTime=sTime;
            this.bCorrected=bCorrected; //
            this.vPreguntes=vPreguntes;
    }
    
    /*public int getIdAlumne(){
        return iIdAlumne;
    }*/
    
/*    public int getIdAssignacio(){
        return iIdAssignacio;
    }*/
    
    public String getIdAssignacio(){
        return sIdAssignacio;
    }    
    
    public int getNumFull(){
        return iNumFull;
    }
    
    public String getNomFull(){
        return sNomFull;
    }

    public String getEstatLliurament(){
        return sEstatLliurament;
    }
    
    public String getResposta(){
        return Resposta.vectorToString(vRespostes);
    }
    
	public Vector getRespostes(){
		return vRespostes;
	}
    
    public Vector getFeedback(){
        return vFeedbacks;
    }
    
    public Vector getInteraccions(){
        return vInteraccions;
    }
    
    public int getNumLliuraments(){
        return iNumLliuraments;
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
    
    public boolean isCorrected(){
        return bCorrected;
    }
    
    public Vector getPreguntes(){
        return vPreguntes;
    }
    
    public void setNumFull(int iNumFull){
        this.iNumFull=iNumFull;
    }

    public void setNomFull(String sNomFull){
        this.sNomFull=sNomFull;
    }
    
    public void setPuntuacio(int iPuntuacio){
    	this.iPuntuacio=iPuntuacio;
    }

    public void setRespostes(Vector vRespostes){
        this.vRespostes=vRespostes;
    }
    
    public void setFeedback(Vector vFeedbacks){
        this.vFeedbacks=vFeedbacks;
    }
    
    public void setEstatLliurament(String sEstatLliurament){
        this.sEstatLliurament=sEstatLliurament;
    }
    
    public void setInteraccions(Vector vInteraccions){
        this.vInteraccions=vInteraccions;
    }
    
    public void addInteraccio(Intervencio oIntervencio){
    	if (oIntervencio != null){
    		vInteraccions.addElement(oIntervencio);
		}
    }
    
    public void setTime(String sTime){
        this.sTime=sTime;
    }
    
    public void setDataDarrerLliurament(java.util.Date d){
        this.dUltimaEntrega=d;
    }
    
    public void incLliuraments(){
        iNumLliuraments++;
    }
    
    public void addTime(String time){
        if (time==null) return;
        if (sTime==null) sTime="00:00:00";
        setTime(Utility.addTime(time,sTime));
        //System.out.println("NOU TIME:"+getTime());
    }
    
    public void setCorrected(boolean b){
        bCorrected=b;
    }
    
    public void setAskingInteraction(String s){
        /* Només es fa servir temporalment per tal d'indicar que l'alumne ha demanat omplir interacció */
        sAskingInteraction=s;
    }
    
    public void setPreguntes(Vector vPreguntes){
        this.vPreguntes=vPreguntes;
    }
    
    public String getAskingInteraction(){
        String temp=sAskingInteraction;
        sAskingInteraction=null;
        return temp;
    }
    
    public Object clone(){
        FullAssignacioQuadernIndividualInfo faq=new FullAssignacioQuadernIndividualInfo(sIdAssignacio,iNumFull,sNomFull,
            sEstatLliurament,vRespostes,vFeedbacks,vInteraccions,iNumLliuraments,iPuntuacio,dUltimaEntrega,sTime,bCorrected,vPreguntes);
        faq.setCorrected(isCorrected());
        return faq;
    }
    
    public String toString(){
        String s=sNomFull;
        return s;
    }
}
