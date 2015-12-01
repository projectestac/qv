package edu.xtec.qv.servlet.util;

/*
 * Intervencio.java
 * Created on 16-ene-2004
 *
 */

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;


/**
 * @author sarjona
 */
public class Intervencio {
	
	private String sIdAssignacio;
	private int iIdFull;
	private String sIdPregunta;
	private Date dDataCreacio;
	private String sInterventor;
	private String sIntervencio;
	
	private static Logger logger = Logger.getRootLogger();
	
	public Intervencio(String sIdAssignacio, int iIdFull, String sIdPregunta, Date dDataCreacio, String sInterventor, String sIntervencio){
		this.sIdAssignacio = sIdAssignacio;
		this.iIdFull = iIdFull;
		this.sIdPregunta = sIdPregunta;
		this.dDataCreacio = dDataCreacio;
		this.sInterventor = sInterventor;
		this.sIntervencio = sIntervencio;
	}
	
	/**
	 * Obté un Objecte Intervencio a partir d'un String amb el format idPregunta+"_"+idIntervencio+"_view"+Interventor+"="+txtIntervencio
	 * @param sIdAssignacio identificador de l'assignacio
	 * @param iIdFull identificador del full
	 * @param sIntervencio String amb el format idPregunta+"_"+idIntervencio+"_view"+Interventor+"="+txtIntervencio
	 */
	public Intervencio(String sIdAssignacio, int iIdFull, String sIntervencio){
		this(sIdAssignacio, iIdFull, getIdPregunta(sIntervencio), null, getInterventor(sIntervencio), getTextIntervencio(sIntervencio));
	}
	
	/**
	 * @return
	 */
	public Date getDataCreacio() {
		return dDataCreacio;
	}

	/**
	 * @return
	 */
	public int getIdFull() {
		return iIdFull;
	}

	/**
	 * @return
	 */
	public String getIdAssignacio() {
		return sIdAssignacio;
	}

	/**
	 * @return
	 */
	public String getIdPregunta() {
		return sIdPregunta;
	}

	/**
	 * @return
	 */
	public String getIntervencio() {
		return sIntervencio;
	}

	/**
	 * @return
	 */
	public String getInterventor() {
		return sInterventor;
	}
	
	public static String intervencionsToString(Vector vIntervencions){
		String sIntervencions = new String();
		if (vIntervencions != null){
			Hashtable hPreguntes = getPreguntes(vIntervencions);
			Enumeration tmpEnum = hPreguntes.keys();
			while (tmpEnum.hasMoreElements()){
				String sIdPregunta = (String)tmpEnum.nextElement();
				Vector vIntPregunta = (Vector)hPreguntes.get(sIdPregunta);
				sIntervencions+=intervencionsPreguntaToString(vIntPregunta);
			}
		}
		return sIntervencions;
	}

	public static String intervencionsPreguntaToString(Vector vIntervencions){
		String sIntervencions = new String();
		if (vIntervencions != null){
			int iNumIntervencio = 1;
			Enumeration tmpEnum = vIntervencions.elements();
			while (tmpEnum.hasMoreElements()){
				Intervencio oIntervencio = (Intervencio)tmpEnum.nextElement(); 
				sIntervencions+=oIntervencio.getIdPregunta()+"_";
				sIntervencions+=iNumIntervencio+"_";
				sIntervencions+="view"+oIntervencio.getInterventor()+"=";
				sIntervencions+=oIntervencio.getIntervencio()+"#";
				iNumIntervencio++;
			}
		}
		return sIntervencions;
	}
	
	/**
	 * Obté una hashtable amb els parells (IdPregunta, Vector de <code>Intervencio</code>)
	 * @param vIntervencions Vector amb totes les intervencions d'un full
	 * @return Hashtable amb els parells (IdPregunta, Vector de <code>Intervencio</code>);
	 */
	public static Hashtable getPreguntes(Vector vIntervencions){
		Hashtable hPreguntes = new Hashtable();
		try  {
			if (vIntervencions!=null){
				Enumeration tmpEnum = vIntervencions.elements();
				while (tmpEnum.hasMoreElements()){
					Intervencio oIntervencio = (Intervencio)tmpEnum.nextElement();
					if (!hPreguntes.containsKey(oIntervencio.getIdPregunta())){
						hPreguntes.put(oIntervencio.getIdPregunta(), new Vector());
					}
					Vector vIntPregunta = (Vector)hPreguntes.get(oIntervencio.getIdPregunta());
					vIntPregunta.addElement(oIntervencio);
					hPreguntes.put(oIntervencio.getIdPregunta(), vIntPregunta);
				}
			}
		}catch (Exception e){
			logger.error("ERROR obtenint les interaccions per cada pregunta--> "+e);
		}
		return hPreguntes;
	}

	/**
	 * Obte totes les interaccions de la pregunta indicada
	 * @param sIdPregunta identificador de pregunta
	 * @return Vector de <code>Intevencio</code> amb totes les intervencions de la pregunta especificada
	 */
	public static Vector getInteraccionsPregunta(Vector vInteraccions, String sIdPregunta){
		Vector vIntPregunta = new Vector();
		Hashtable hPreguntes = getPreguntes(vInteraccions);
		if (hPreguntes!=null && hPreguntes.containsKey(sIdPregunta)){
			vIntPregunta = (Vector)hPreguntes.get(sIdPregunta);
		}
		return vIntPregunta;
	}
    	
	/**
	 * 
	 * @param sIntervencio String amb el format idPregunta+"_"+idIntervencio+"_view"+Interventor+"="+txtIntervencio
	 * @return
	 */
	public static String getIdPregunta(String sIntervencio){
		String sIdPregunta = null;
		if (sIntervencio != null){
			int iInterventor = sIntervencio.indexOf("_view");
			int iIdIntervencio = sIntervencio.substring(0,iInterventor).lastIndexOf("_",iInterventor);
			sIdPregunta = sIntervencio.substring(0,iIdIntervencio);
		}
		return sIdPregunta;
	}	
	
	/**
	 * Retorna qui ha realitzat la intervencio
	 * @param sIntervencio String amb el format idPregunta+"_"+idIntervencio+"_view"+Interventor+"="+txtIntervencio
	 * @return el perfil de la persona que ha realitzat la intervencio (candidate o teacher)
	 */
	private static String getInterventor(String sIntervencio){
		String sInterventor = null;
		if (sIntervencio != null){
			sInterventor = sIntervencio.substring(sIntervencio.indexOf("_view")+5, sIntervencio.indexOf("="));
		}
		return sInterventor;
	}	
	
	/**
	 * Retorna el text de la intervencio
	 * @param sIntervencio String amb el format idPregunta+"_"+idIntervencio+"_view"+Interventor+"="+txtIntervencio
	 * @return text de la intervencio
	 */
	private static String getTextIntervencio(String sIntervencio){
		String sTextIntervencio = null;
		if (sIntervencio != null){
			sTextIntervencio = sIntervencio.substring(sIntervencio.indexOf("=")+1);
		}
		return sTextIntervencio;
	}	
	
	public String toString(){
		return "Intervencio("
			+ "idAssignacio="+getIdAssignacio() 
			+ ", idFull="+getIdFull() 
			+ ", idPregunta="+getIdPregunta() 
			+ ", dataCreacio="+getDataCreacio() 
			+ ", interventor="+getInterventor() 
			+ ", intervencio="+getIntervencio() + ")";
	}
}
