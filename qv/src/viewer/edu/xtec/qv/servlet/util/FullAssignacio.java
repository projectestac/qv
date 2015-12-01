package edu.xtec.qv.servlet.util;
/*
 * FullQuadern.java
 * Created on 20-ene-2004
 * 
 */

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import edu.xtec.qv.servlet.QVServlet;
import edu.xtec.qv.util.Utility;

/**
 * @author sarjona
 */
public class FullAssignacio {

	private static Logger logger = Logger.getRootLogger();
	
	private int iIdFull;
	private String sSectionId;
	private String sNomFull;
	private String sEstatLliurament;
	private int iNumCopsLliurat;
	private Date dDataDarrerLliurament;
	private double dDarreraPuntuacio;
	private String sTime;
	private Vector vPreguntes;
	private Vector vRespostes;
	private Vector vFeedbacks;
	private Vector vIntervencions;
	private String sNovaIntervencio;
	
	public FullAssignacio(int iIdFull, String sNomFull, String sEstatLliurament, 
			int iNumCopsLliurat, Date dDataDarrerLliurament, double dDarreraPuntuacio, Vector vPreguntes, Vector vRespostes, Vector vFeedbacks, Vector vIntervencions){
		this.iIdFull=iIdFull;
		this.sNomFull=sNomFull;
		this.sEstatLliurament=sEstatLliurament;
		this.iNumCopsLliurat=iNumCopsLliurat;
		this.dDataDarrerLliurament=dDataDarrerLliurament;
		this.dDarreraPuntuacio = dDarreraPuntuacio;
		this.vPreguntes=vPreguntes;
		this.vRespostes=vRespostes;
		this.vFeedbacks=vFeedbacks;
		this.vIntervencions=vIntervencions;
	}
	
	public FullAssignacio (int iIdFull, String sNomFull){
		this(iIdFull, sNomFull, null, 0, null, 0, null, null, null, null);
	}
	
	public FullAssignacio (int iIdFull, String sSectionId, String sNomFull){
		this(iIdFull, sNomFull, null, 0, null, 0, null, null, null, null);
		this.sSectionId=sSectionId;
	}
	
	/**
	 * @return
	 */
	public int getIdFull() {
		return iIdFull;
	}
	public void setIdFull(int iIdFull) {
		this.iIdFull=iIdFull;
	}

	/**
	 * @return
	 */
	public String getNomFull() {
		return sNomFull;
	}

	/**
	 * @return
	 */
	public String getSectionId() {
		if (this.sSectionId==null) this.sSectionId="";
		return this.sSectionId;
	}
	public void setSectionId(String sSectionId) {
		this.sSectionId=sSectionId;
	}
	
	/**
	 * @return
	 */
	public String getEstatLliurament() {
		return sEstatLliurament;
	}
	
	public void setEstatLliurament(String sEstatLliurament){
		this.sEstatLliurament = sEstatLliurament;
	}
	
	/**
	 * Obte una hash amb les parelles (ESTAT, boolea). Cada parella indica si el full 
	 * es troba en l'estat indicat.
	 * @return hashtable amb les parelles (ESTAT, boolea), que indiquen si el full es troba o no en cada estat
	 */
	public Hashtable getEstats(){
		Hashtable hEstats = new Hashtable();
		boolean bIsComencat = false;
		boolean bIsPendentRevisio = false;
		boolean bIsPendentModificacio = false;
		boolean bIsLliurat = false;
		boolean bIsCorregit = false;
		if (getEstatLliurament()!=null){
			if (getEstatLliurament().equals(QVServlet.INICIAT)){
				bIsComencat = true;
			}else if (getEstatLliurament().equals(QVServlet.INTERVENCIO_ALUMNE) || getEstatLliurament().equals("pendent_revisio")){
				bIsPendentRevisio = true;
				bIsComencat = true;
			} else if (getEstatLliurament().equals(QVServlet.INTERVENCIO_DOCENT) || getEstatLliurament().equals("pendent_modificacio")){
				bIsPendentModificacio = true;
				bIsComencat = true;
			} else if (getEstatLliurament().equals(QVServlet.LLIURAT)){
				bIsLliurat = true;
				bIsComencat = true;
			} else if (getEstatLliurament().equals(QVServlet.CORREGIT)){
				bIsComencat = true; 
				bIsCorregit = true;
			}
		}
		if (getDataDarrerLliurament()!=null || getNumCopsLliurat()>0){
			bIsLliurat = true;
			bIsComencat = true;
		}
		
		hEstats.put(QVServlet.NO_INICIAT, new Boolean(!bIsComencat)); 					// Tots els fulls estan o sense estat o no començats
		hEstats.put(QVServlet.INICIAT, new Boolean(bIsComencat));						// No hi ha cap full  sense començar
		hEstats.put(QVServlet.INTERVENCIO_ALUMNE, new Boolean(bIsPendentRevisio));		// Existeix algun quadern amb una intervencio feta per l'alumne
		hEstats.put(QVServlet.INTERVENCIO_DOCENT, new Boolean(bIsPendentModificacio)); 	// Existeix algun quadern amb una intervencio del professor
		hEstats.put(QVServlet.PARCIALMENT_LLIURAT, new Boolean(bIsLliurat || bIsCorregit));	// Existeix algun full lliurat
		hEstats.put(QVServlet.LLIURAT, new Boolean(bIsLliurat));					// Tots els fulls del quadern estan lliurats
		hEstats.put(QVServlet.PARCIALMENT_CORREGIT, new Boolean(bIsCorregit));			// Existeix algun full corregit
		hEstats.put(QVServlet.CORREGIT, new Boolean(bIsCorregit));					// Tots els fulls estan corregits
		return hEstats;		
	}
	
	public boolean isInState(String sState){
		return getEstats().containsKey(sState)?((Boolean)getEstats().get(sState)).booleanValue():false;
	}

	/**
	 * @return
	 */
	public int getNumCopsLliurat() {
		return iNumCopsLliurat;
	}
	
	public void setNumCopsLliurat(int iNumCopsLliurat){
		this.iNumCopsLliurat=iNumCopsLliurat;
	}
	
	public void incrementarLliuraments(){
		iNumCopsLliurat++;
	}
    
	/**
	 * @return
	 */
	public Date getDataDarrerLliurament() {
		return dDataDarrerLliurament;
	}
	
	public void setDataDarrerLliurament(Date dDataDarrerLliurament){
		this.dDataDarrerLliurament = dDataDarrerLliurament;
	}
	
	public double getDarreraPuntuacio(){
		return dDarreraPuntuacio;
	}
	
	public void setDarreraPuntuacio(double dPuntuacio){
		this.dDarreraPuntuacio = dPuntuacio;
	}
	
	public boolean isCorregit(){
		return isCorregit(false);
	}

	public boolean isCorregit(boolean bAutoCorreccio){
		return  (getEstatLliurament()!=null && getEstatLliurament().equals(QVServlet.CORREGIT)) ||
				(bAutoCorreccio && getEstatLliurament()!=null && getDataDarrerLliurament() != null);
	}

	/**
	 * @return
	 */
	public String getTime() {
		if (sTime == null){
			sTime = "00:00:00";
		}
		return sTime;
	}

	/**
	 * @param string
	 */
	public void setTime(String sTime) {
		this.sTime = sTime;
	}
	
	public void addTime(String sTime){
		if (sTime == null) return;
		setTime(Utility.addTime(sTime,getTime()));
	}
    
	/**
	 * @return Vector de <code>Pregunta</code>
	 */
	public Vector getPreguntes() {
		if (vPreguntes==null){
			vPreguntes = new Vector();
		}
		return vPreguntes;
	}
	
	public void setPreguntes(Vector vPreguntes){
		this.vPreguntes = vPreguntes;
	}
	
	/**
	 * @return Vector de <code>Resposta</code>
	 */
	public Vector getRespostes(){
		if (vRespostes == null){
			vRespostes = new Vector();
		}
		return vRespostes;
	}
	
	/**
	 * 
	 * @param vRespostes
	 */
	public void setRespostes(Vector vRespostes){
		this.vRespostes=vRespostes;
	}
    
	/**
	 * Obte un String amb la representacio de totes les respostes del full
	 * @return
	 */
	public String getResposta(){
		return Resposta.vectorToString(getRespostes());
	}
    
    public boolean isCorregible(){
    	// TODO: Crear clase EstatLliurament amb tots els possibles estats (classe enumerada)
    	return getEstatLliurament()==null || !getEstatLliurament().equals(QVServlet.INTERVENCIO_ALUMNE);
    }
    
    public boolean isInicial(){
    	return getIdFull() == 0;
    }
    
	/**
	 * @return
	 */
	public Vector getFeedbacks() {
		if (vFeedbacks==null){
			vFeedbacks = new Vector();
		}
		return vFeedbacks;
	}

	/**
	 * @param vFeedbacks
	 */
	public void setFeedbacks(Vector vFeedbacks) {
		this.vFeedbacks = vFeedbacks;
	}

	/**
	 * @return
	 */
	public Vector getIntervencions() {
		return vIntervencions;
	}

	public void addIntervencio(Intervencio oIntervencio){
		if (oIntervencio != null){
			if (vIntervencions==null){
				vIntervencions = new Vector();
			}
			vIntervencions.addElement(oIntervencio);
		}
	}
	
	public String getNovaIntervencio(){
		return sNovaIntervencio;
	}
    
	public void setNovaIntervencio(String sNovaIntervencio){
		this.sNovaIntervencio=sNovaIntervencio;
	}

	/**
	 * Guarda la nova intervencio (pero no la guarda amb la resta d'intervencions, sino que la deixa pendent)
	 * @param sIntervencio
	 * @return
	 */
	public void addNovaIntervencio(String sIntervencio){
		//logger.debug("addNovaIntervencio-> "+sIntervencio);
		if (getIdFull()>0 && sIntervencio!=null){
			int i=sIntervencio.indexOf("#");
			sIntervencio=(i>0)?sIntervencio.substring(0,i):sIntervencio;
			int iNewIndex=(Intervencio.getInteraccionsPregunta(getIntervencions(), sIntervencio)).size()+1;
			setNovaIntervencio(sIntervencio+"_"+iNewIndex+"_view=|#"); // Indico que ha de preguntar aquesta interacció.
		}
	}
    
	/**
	 * Retorna un Vector de <code>FullQuadern</code>, amb els fulls inicialitzats
	 * amb els valors per defecte
	 * @param vNomsFulls Vector amb els noms dels fulls
	 * @return
	 */
	public static Vector getFulls(Vector vSections){
		Vector vFulls = new Vector();
		// Afegir el full0 que conté les dades del quadern
		vFulls.addElement(new FullAssignacio(0,"Full 0"));
		if (vSections==null || vSections.size()==0){
			// quadern sense section's. Hi haura un unic full
			vFulls.addElement(new FullAssignacio(1, "Full 1"));
		} else{
			// Afegir la resta de fulls del quadern
			Enumeration enumNomsFulls = vSections.elements();
			int iIdQuadern = 1;
			while (enumNomsFulls.hasMoreElements()){
				String sNomFull = (String)enumNomsFulls.nextElement();
				FullAssignacio oFullQuadern = new FullAssignacio(iIdQuadern, sNomFull);
				//FullAssignacio oFullQuadern = (FullAssignacio)enumNomsFulls.nextElement();
				//oFullQuadern.setIdFull(iIdQuadern);
				vFulls.addElement(oFullQuadern);
				iIdQuadern++;
			}
		}
		return vFulls;
	}
	
	public boolean equals(Object o){
		boolean bEquals = false;
		if (o!=null){
			FullAssignacio oFull = (FullAssignacio)o;
			bEquals = oFull.getIdFull() == getIdFull();
		}
		return bEquals;
	}

	public String toString(){
		return "Full ("
			+  "idFull="+getIdFull()
			+  ", nomFull="+getNomFull()
			+  ", estatLliurament="+getEstatLliurament()
			+  ", copsLliurat="+getNumCopsLliurat()
			+  ", dataDarrerLliurament="+getDataDarrerLliurament()
			+  ", preguntes="+getPreguntes()
			+  ", respostes="+getRespostes()
			+  ", feedbacks="+getFeedbacks()
			+  ", intervencions="+getIntervencions()
			+  ")";
	}

}
