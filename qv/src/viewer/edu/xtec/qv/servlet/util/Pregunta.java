package edu.xtec.qv.servlet.util;

/*
 * Pregunta.java
 * 
 * Created on 16-ene-2004
 *
 */

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;


/**
 * @author sarjona
 */
public class Pregunta {
	
	private static Logger logger = Logger.getRootLogger();

	private String sIdPregunta;
	private double dPuntuacio;
	private boolean bCorrecte;
	
	public Pregunta (String sIdPregunta, double dPuntuacio, boolean bCorrecte){
		this.sIdPregunta=sIdPregunta;
		this.dPuntuacio=dPuntuacio;
		this.bCorrecte=bCorrecte;
	}

	/**
	 * @return
	 */
	public boolean isCorrecte() {
		return bCorrecte;
	}

	/**
	 * @param b
	 */
	public void setCorrecte(boolean bCorrecte) {
		this.bCorrecte = bCorrecte;
	}

	/**
	 * @return
	 */
	public double getPuntuacio() {
		return dPuntuacio;
	}

	/**
	 * @param d
	 */
	public void setPuntuacio(double dPuntuacio) {
		this.dPuntuacio = dPuntuacio;
	}

	
	/**
	 * @return
	 */
	public String getIdPregunta() {
		return sIdPregunta;
	}

	/**
	 * Retorna la representacio de les preguntes 
	 * @param vPreguntes Vector de <code>Pregunta</code>
	 * @return String amb les preguntes 
	 */
	public static String vectorToString(Vector vPreguntes){
		String sPreguntes = new String();
		if (vPreguntes != null){
			Enumeration enumPreguntes = vPreguntes.elements();
			while (enumPreguntes.hasMoreElements()){
				Pregunta oPregunta = (Pregunta)enumPreguntes.nextElement();
				sPreguntes+=oPregunta.toString()+"#";
			}
		}
		return sPreguntes;
	}
	
	public static Vector stringToVector(String sPreguntes){
		Hashtable hPreguntes = new Hashtable();
		StringTokenizer stPreguntes = new StringTokenizer(sPreguntes, "#");
		while (stPreguntes.hasMoreElements()){
			String sItem = (String)stPreguntes.nextElement();
			String sIdPregunta = getIdPregunta(sItem);
			if (isScore(sItem)){
				double dPuntuacio = getScore(sItem);
				if (hPreguntes.containsKey(sIdPregunta)){
					((Pregunta)hPreguntes.get(sIdPregunta)).setPuntuacio(dPuntuacio);
				} else{
					hPreguntes.put(sIdPregunta, new Pregunta(sIdPregunta, dPuntuacio, false));
				}
			} else if (isCorrect(sItem)){
				boolean bCorrecte = getCorrect(sItem);
				if (hPreguntes.containsKey(sIdPregunta)){
					((Pregunta)hPreguntes.get(sIdPregunta)).setCorrecte(bCorrecte);
				} else{
					hPreguntes.put(sIdPregunta, new Pregunta(sIdPregunta, 0, bCorrecte));
				}				
			}
		}
		return new Vector(hPreguntes.values());
	}
	
	private static boolean isScore(String sItem){
		return (sItem!=null && sItem.indexOf("_score=")>0);
	}
	
	private static boolean isCorrect(String sItem){
		return (sItem!=null && sItem.indexOf("_correct=")>0);
	}
		
	public static String getIdPregunta(String sItem){
		int iIndex = sItem!=null?sItem.indexOf("_score=")>0?sItem.indexOf("_score="):sItem.indexOf("_correct="):-1;
		String sIdPregunta = iIndex>0?sItem.substring(0,iIndex):null;
		return sIdPregunta;
	}
	
	public static double getScore(String sItem){
		double dScore = 0;
		if (sItem != null){
			String sScore = sItem.substring(sItem.indexOf("_score=")+7);
			dScore = sScore!=null?Double.parseDouble(sScore):0;
		}
		return dScore;
	}
	
	public static boolean getCorrect(String sItem){
		boolean bCorrect = false;
		if (sItem != null){
			String sCorrect = sItem.substring(sItem.indexOf("_correct=")+9);
			bCorrect = sCorrect!=null?Boolean.valueOf(sCorrect).booleanValue():false;
		}
		return bCorrect;
	}
	
	/**
	 * Representacio d'una pregunta
	 */
	public String toString(){
		return getIdPregunta()+"_correct="+isCorrecte()+"#"
			 + getIdPregunta()+"_score="+getPuntuacio();
	}

}
