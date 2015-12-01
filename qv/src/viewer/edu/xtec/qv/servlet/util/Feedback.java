package edu.xtec.qv.servlet.util;
/*
 * Feedback.java
 * Created on 16-ene-2004
 *
 */

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * @author sarjona
 */
public class Feedback {

	private static Logger logger = Logger.getRootLogger();
	
	private String sIdPregunta;
	private String sFeedback;
	
	public Feedback(String sIdPregunta, String sFeedback){
		this.sIdPregunta=sIdPregunta;
		this.sFeedback=sFeedback;
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
	public String getFeedback() {
		return sFeedback;
	}

	/**
	 * Retorna la representacio del feedbacks 
	 * @param vFeedbacks Vector de <code>Feedback</code>
	 * @return String amb els feedbacks 
	 */
	public static String feedbacksToString(Vector vFeedbacks){
		String sFeedbacks = new String();
		if (vFeedbacks != null){
			Enumeration enumFeedbacks = vFeedbacks.elements();
			while (enumFeedbacks.hasMoreElements()){
				Feedback oFeedback = (Feedback)enumFeedbacks.nextElement();
				sFeedbacks+=oFeedback.toString()+"#";
			}
		}
		return sFeedbacks;
	}
	
	/**
	 * Retorna un Vector de <code>Feedback</code>
	 * @param hmFeedbacks HashMap de la forma (IdPregunta*Feedback, Feedback)
	 * @return Vector de <code>Feedback</code>
	 */
	public static Vector hashMapToVector(HashMap hmFeedbacks){
		Vector vFeedbacks = new Vector();
		Iterator itFeedbacks = hmFeedbacks.keySet().iterator();
		while (itFeedbacks.hasNext()){
			String sIdPregFeed = (String)itFeedbacks.next();
			int iIndex = sIdPregFeed.indexOf("*");
			String sIdPregunta = iIndex>0?sIdPregFeed.substring(0,iIndex):null;
			if (sIdPregunta!=null){
				String sFeedback = (String)hmFeedbacks.get(sIdPregFeed);
				Feedback oFeedback = new Feedback(sIdPregunta, sFeedback);
				vFeedbacks.addElement(oFeedback);
			}
		}
		return vFeedbacks;
	}
	
	public boolean equals(Object o){
		boolean bEquals = false;
		if (o instanceof Feedback){
			Feedback oFeedback = (Feedback)o;
			bEquals = getIdPregunta().equals(oFeedback.getIdPregunta())
					&& getFeedback().equals(oFeedback.getFeedback());
		}
		return bEquals;
	}
	
	/**
	 * La representacio del feedback es IdPregunta+Feedback (sense cap separador).
	 */
	public String toString(){
		 return sIdPregunta+sFeedback;
	}

}
