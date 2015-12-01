package edu.xtec.qv.servlet.util;
/*
 * Resposta.java
 * Created on 19-ene-2004
 *
 */

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * @author sarjona
 *
 */
public class Resposta {
	private static Logger logger = Logger.getRootLogger();

	private String sIdPregunta;
	private String sIdResposta;
	private String sResposta;
	
	public Resposta(String sIdPregunta, String sIdResposta, String sResposta){
		this.sIdPregunta=sIdPregunta;
		this.sIdResposta=sIdResposta;
		this.sResposta=sResposta;
	}
	
	/**
	 * 
	 * @param sRepresentation String de la forma id_pregunta-->id_resposta=resposta
	 */
	public Resposta(String sRepresentation){
		this(getIdPregunta(sRepresentation), getIdResposta(sRepresentation), getResposta(sRepresentation));
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
	public String getIdResposta() {
		return sIdResposta;
	}

	/**
	 * @return
	 */
	public String getResposta() {
		return sResposta;
	}
	
	public static String vectorToString(Vector vRespostes){
		String sRespostes = new String();
		if (vRespostes != null){
			Enumeration enumRespostes = vRespostes.elements();
			while (enumRespostes.hasMoreElements()){
				Resposta oResposta = (Resposta)enumRespostes.nextElement();
				sRespostes+=oResposta.toString()+"#";
			}
		}
		return sRespostes;
	}
	
	public static Vector stringToVector(String sRespostes){
		Vector vRespostes = new Vector();
		if (sRespostes != null){
			StringTokenizer stRespostes = new StringTokenizer(sRespostes, "#");
			while (stRespostes.hasMoreElements()){
				String sItem = (String)stRespostes.nextElement();
				Resposta oResposta = new Resposta(sItem);
				vRespostes.addElement(oResposta);
			}
		}
		return vRespostes;
	}
	
	public static String getIdPregunta(String sRepresentation){
		int iIndex = sRepresentation!=null?sRepresentation.indexOf("-->"):-1;
		String sIdPregunta = iIndex>0?sRepresentation.substring(0,iIndex):null;
		return sIdPregunta;
	}
	
	public static String getIdResposta(String sRepresentation){
		int iIndex1 = sRepresentation!=null?sRepresentation.indexOf("-->"):-1;
		int iIndex2 = sRepresentation!=null?sRepresentation.indexOf("="):-1;
		String sIdResposta = iIndex1>0 && iIndex2>0?sRepresentation.substring(iIndex1+3,iIndex2):null;
		return sIdResposta;
	}
	
	public static String getResposta(String sRepresentation){
		int iIndex = sRepresentation!=null?sRepresentation.indexOf("="):-1;
		String sResposta = iIndex>0?sRepresentation.substring(iIndex+1):null;
		return sResposta;		
	}
	
	public static boolean equals(Vector vResposta1, Vector vResposta2){
		boolean bEquals = false;
		if (vResposta1==null && vResposta2==null){
			bEquals = true;
		} else if (vResposta1!=null && vResposta2!=null){
			if (vResposta1.size()==vResposta2.size()){
				bEquals = true;
				Enumeration enumRespostes = vResposta1.elements();
				while (bEquals && enumRespostes.hasMoreElements()){
					Resposta oResposta = (Resposta)enumRespostes.nextElement();
					if (!vResposta2.contains(oResposta)){
						bEquals = false;			
					}
				}
			}
		}
		return bEquals;
	}
	
	public boolean equals(Object o){
		boolean bEquals = false;
		if (o!=null && o instanceof Resposta){
			Resposta oResposta = (Resposta)o;
			bEquals = ( (getIdPregunta()!=null && getIdPregunta().equalsIgnoreCase(oResposta.getIdPregunta())) || (getIdPregunta()==null && oResposta.getIdPregunta()==null))
				&& ((getIdResposta()!=null && getIdResposta().equalsIgnoreCase(oResposta.getIdResposta())) || (getIdResposta()==null && oResposta.getIdResposta()==null))
				&& ((getResposta()!=null && getResposta().equalsIgnoreCase(oResposta.getResposta())) || (getResposta()==null && oResposta.getResposta()==null));
		}
		return bEquals;
	}
	
	/**
	 * Representacio d'una resposta
	 */
	public String toString(){
		return getIdPregunta()+"-->"+getIdResposta()+"="+(getResposta()!=null?getResposta():"");
	}

}
