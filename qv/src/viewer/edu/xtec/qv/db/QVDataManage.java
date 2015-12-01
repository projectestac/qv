package edu.xtec.qv.db;

/*
 * QVDataManage.java
 *
 * Created on 2 / maig / 2003, 12:00
 */

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import edu.xtec.qv.servlet.util.Assignacio;
import edu.xtec.qv.servlet.util.FullAssignacio;
import edu.xtec.qv.servlet.util.Intervencio;

/**
 *
 * @author  allastar
 */
public interface QVDataManage {

    //public boolean existAssign(String sAssignacioId);    
    //public boolean createAssign(String sAssignId,String sQuadernUrl, int correccio, int max, int contestar, int autoAdvance, int writing, String sQuadernXSL, String sEndPage);
	public boolean createAssignacio(String sIdAssignacio, String sQuadernURL, String sQuadernXSL, String sEndPage, Date dEndDate, int iMaxLliuraments, boolean bAutoCorreccio, boolean bObligatoriContestar, boolean bAutoAdvance, boolean bWriteIntervencions, String sIP);

	public Assignacio getAssignacio(String sIdAssignacio);
	public Vector getAssignacions(String sIdMaterial);
	public Vector getAssignacionsId(String sIdMaterial);
	
	/**
	 * Inicialitza els fulls de l'assignacio indicada
	 * @param sIdAssignacio identificador de l'assignacio
	 * @param vFulls Noms dels fulls que formen part del quadern
	 * @return true si s'ha inicialitzar correctament; false en cas contrari.
	 */
	public boolean initAssignacio(String sIdAssignacio, Vector vFulls);

	public boolean updateFullAssignacio(String sIdAssignacio, FullAssignacio oFull);
	public boolean updateAssignacio(Assignacio oAssignacio);
	public FullAssignacio getFullAssignacio(String sIdAssignacio, int iIdFull);
	public Vector getFullsAssignacio(String sIdAssignacio);


    public boolean deleteAssignacio(String sAssignacioId);
    public boolean deleteAssignacioBefore(java.util.Date dDeleteBefore);
	public boolean isEstatQuadern(String sIdAssignacio, String sEstat);
	public String getEstatQuadern(String sIdAssignacio);
	public Hashtable getEstatsQuadern(String sIdAssignacio);
	
	public double getAssignmentPuntuation(String sAssignmentId);

	public int getMaxLliuraments(String sAssignmentId);
	public int getTotalLliuramentsFets(String sAssignmentId);

    //public AssignacioQuadernData getAssignacioQuadern(String sAssignacioId);
    //public AssignacioQuadernIndividualInfo getAssignacioQuadernIndividualInfo(String sAssignacioId);
    //public FullAssignacioQuadernIndividualInfo getFullAssignacioQuadernIndividualInfo(String sIdAssignacio,int iNumFull);
    
    /*---FAQ---*/
    //public boolean updateFullAssignacioQuadernIndividualInfo(FullAssignacioQuadernIndividualInfo fai);
    /*Actualitza tot el faq*/
    //public boolean updateEstatLliuramentFull(String sIdAssignacio,int iNumFull,String sEstatLliurament);
    /*Només actualitza l'estat de lliurament del full*/
    
    /*---AQI---*/
    //public boolean updateQuadernResponse(String sIdAssignacio,String sUserResponses, int iPuntuacio, String sEstatEntrega, String sFeedback, String sInteraction, boolean incEntregues, String sTime);
    /*Actualitza tot un aqi (AssignacioQuadernIndividualInfo)*/
    //public boolean updateEstatLliuramentAssignacio(String sIdAssignacio,String sEstatLliurament);
    /*Només actualitza l'estat de lliurament del quadern*/

    //public java.util.Vector getVectorFullsAssignacioQuadern(String sIdAssignacio);
    /*Retorna tots els faq (FullAssignacioQuadernIndividualInfo) d'una assignació*/
    
    
    /**
     * Afegeix una nova intervencio
     * @param oIntervencio intervencio a afegir
     */
	public boolean addIntervencio(Intervencio oIntervencio);
	
	public void freeConnection();
	
	public Vector getSkins(Vector vIdSkins, String sLang, boolean bAll);
	

}
