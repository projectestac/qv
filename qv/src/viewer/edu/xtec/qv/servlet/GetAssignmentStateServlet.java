package edu.xtec.qv.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.xtec.qv.servlet.util.Assignacio;
import edu.xtec.qv.servlet.util.FullAssignacio;
import edu.xtec.qv.servlet.util.Pregunta;
/*
 * GetAssignmentStateServlet.java
 *
 * Created on 22 / desembre / 2003
 */

/**
 *
 * @author  sarjona
 */

public class GetAssignmentStateServlet extends QVServlet {

	/**
	 * Parametres
	 */
	public static final String P_ASSIGNMENT_IDS = "idAssignacions";
	public static final String P_ID_MATERIAL = "idMaterial";

	public static final String P_PUNTUACIO = "puntuacio";
	public static final String P_MAX_LLIURAMENTS = "max_lliuraments";
	public static final String P_LLIURAMENTS_FETS = "lliuraments_fets";

	public static final String SEPARATOR = ";";


	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		if (getQVDataManage()!=null){
			String sAssignments = request.getParameter(P_ASSIGNMENT_IDS);
			if (sAssignments!=null && sAssignments.trim().length()>0){
				String sLastQuadern = null;
				StringTokenizer stAssignments = new StringTokenizer(sAssignments, SEPARATOR);
				while (stAssignments.hasMoreElements()){
					String sIdAssignacio = stAssignments.nextToken();
					appendEstatsAssignacio(sIdAssignacio, out);
				}
			}else if (request.getParameter(P_ID_MATERIAL)!=null){
				Enumeration enumAssignacions = getQVDataManage().getAssignacionsId(request.getParameter(P_ID_MATERIAL)).elements();
				while (enumAssignacions.hasMoreElements()){
					String sIdAssignacio = (String)enumAssignacions.nextElement();
					appendEstatsAssignacio(sIdAssignacio, out);
				}			
			}else{
				out.println("error: paràmetre "+P_ASSIGNMENT_IDS+" o "+P_ID_MATERIAL+" no especificat");
			}
		}else{
			out.println("error: No es pot accedir a la base de dades");
		}
		//freeConnection();
	}

	protected void appendEstatsAssignacio(String sIdAssignacio, PrintWriter out){
		if (sIdAssignacio!=null){
			String sEstats = new String();
			Hashtable hEstats = getQVDataManage().getEstatsQuadern(sIdAssignacio);
			sEstats+=NO_INICIAT+"="+(hEstats.get(NO_INICIAT)!=null?hEstats.get(NO_INICIAT):"false")+";";
			sEstats+=INICIAT+"="+(hEstats.get(INICIAT)!=null?hEstats.get(INICIAT):"false")+";";
			sEstats+=INTERVENCIO_ALUMNE+"="+(hEstats.get(INTERVENCIO_ALUMNE)!=null?hEstats.get(INTERVENCIO_ALUMNE):"false")+";";
			sEstats+=INTERVENCIO_DOCENT+"="+(hEstats.get(INTERVENCIO_DOCENT)!=null?hEstats.get(INTERVENCIO_DOCENT):"false")+";";
			sEstats+=PARCIALMENT_LLIURAT+"="+(hEstats.get(PARCIALMENT_LLIURAT)!=null?hEstats.get(PARCIALMENT_LLIURAT):"false")+";";
			sEstats+=LLIURAT+"="+(hEstats.get(LLIURAT)!=null?hEstats.get(LLIURAT):"false")+";";
			sEstats+=PARCIALMENT_CORREGIT+"="+(hEstats.get(PARCIALMENT_CORREGIT)!=null?hEstats.get(PARCIALMENT_CORREGIT):"false")+";";
			sEstats+=CORREGIT+"="+(hEstats.get(CORREGIT)!=null?hEstats.get(CORREGIT):"false")+";";
			// Afegir puntuacio
			sEstats+=P_PUNTUACIO+"="+getQVDataManage().getAssignmentPuntuation(sIdAssignacio)+";";
			// Afegir nombre de lliuraments fets/nombre de lliuraments totals
			sEstats+=P_MAX_LLIURAMENTS+"="+getQVDataManage().getMaxLliuraments(sIdAssignacio)+";";
			sEstats+=P_LLIURAMENTS_FETS+"="+getQVDataManage().getTotalLliuramentsFets(sIdAssignacio);
			out.println(sIdAssignacio+";"+sEstats);
		}
	}
	
}
