package edu.xtec.qv.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.xtec.qv.servlet.util.Assignacio;
import edu.xtec.qv.servlet.util.FullAssignacio;
import edu.xtec.qv.servlet.util.Pregunta;

public class GetPuntuationsServlet extends QVServlet {

	public static final String SEPARATOR = ";";
	public static final String DEFAULT_CSV_SEPARATOR = ",";
	/**
	 * Parametres
	 */
	public static final String P_SEPARATOR = "separator";
	public static final String P_ASSIGNMENT_IDS = "idAssignacions";
	public static final String P_ID_MATERIAL = "idMaterial";
	
	protected String sSeparator = DEFAULT_CSV_SEPARATOR;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		response.setContentType("text/csv");
        response.setHeader("Content-disposition", "filename=puntuacions.csv");
		PrintWriter out = response.getWriter();
		sSeparator = request.getParameter(P_SEPARATOR);
		if (sSeparator==null || sSeparator.trim().length()<=0) sSeparator=DEFAULT_CSV_SEPARATOR;
		String sAssignments = request.getParameter(P_ASSIGNMENT_IDS);
		if (sAssignments!=null && sAssignments.trim().length()>0){
			if (getQVDataManage()!=null){
				String sLastQuadern = null;
				StringTokenizer stAssignments = new StringTokenizer(sAssignments, SEPARATOR);
				// comprovar que totes les assignacions són iguals (tenen assignat el mateix quadern)
				while (stAssignments.hasMoreElements()){
					String sAssignment = stAssignments.nextToken();
					Assignacio oAssignacio = getQVDataManage().getAssignacio(sAssignment);
					appendAssignacio(oAssignacio, out, sLastQuadern);
					if (oAssignacio!=null){
						sLastQuadern = oAssignacio.getQuadern().getURL();
					}
				}
			}else{
				out.println("error: No es pot accedir a la base de dades");
			}
		}else if (request.getParameter(P_ID_MATERIAL)!=null){
			String sLastQuadern = null;
			Enumeration enumAssignacions = getQVDataManage().getAssignacions(request.getParameter(P_ID_MATERIAL)).elements();
			while (enumAssignacions.hasMoreElements()){
				Assignacio oAssignacio = (Assignacio)enumAssignacions.nextElement();
				appendAssignacio(oAssignacio, out, sLastQuadern);
				sLastQuadern = oAssignacio.getQuadern().getURL();
			}			
		}else{
			out.println("error: paràmetre "+P_ASSIGNMENT_IDS+" no especificat");
		}
		freeConnection();
	}
	
	protected void appendHeader(Assignacio oAssignacio, PrintWriter out){
		StringBuffer sbHeader = new StringBuffer();
		appendCVSContent(sbHeader, "");
		int iNumFull = 1;
		Enumeration enumFulls = oAssignacio.getFulls().elements();
		while (enumFulls.hasMoreElements()){
			FullAssignacio oFull = (FullAssignacio)enumFulls.nextElement();
			int iPreguntes = oFull.getPreguntes().size();
			for (int iNumPregunta=1;iNumPregunta<=iPreguntes;iNumPregunta++){
				appendCVSContent(sbHeader, String.valueOf("\"Preg. "+iNumFull+"."+iNumPregunta+"\""));
			}
			appendCVSContent(sbHeader, String.valueOf("\"Full "+iNumFull+"\""));
			appendCVSContent(sbHeader, String.valueOf("\"Lliuraments full "+iNumFull+"\""));
			iNumFull++;
		}
		appendCVSContent(sbHeader, String.valueOf("\"TOTAL Puntuació\""));
		out.println("");
		out.println(sbHeader.toString());		
	}
	
	protected void appendAssignacio(Assignacio oAssignacio, PrintWriter out, String sLastQuadern){
		if (oAssignacio!=null){
			if (sLastQuadern==null || !sLastQuadern.equalsIgnoreCase(oAssignacio.getQuadern().getURL())){
				appendHeader(oAssignacio, out);
			}

			StringBuffer sbPuntuations = new StringBuffer();
			appendCVSContent(sbPuntuations, oAssignacio.getIdAssignacio());
			Enumeration enumFulls = oAssignacio.getFulls().elements();
			while (enumFulls.hasMoreElements()){
				FullAssignacio oFull = (FullAssignacio)enumFulls.nextElement();
				Enumeration enumPreguntes = oFull.getPreguntes().elements();
				while (enumPreguntes.hasMoreElements()){
					Pregunta oPregunta = (Pregunta)enumPreguntes.nextElement();
					appendCVSContent(sbPuntuations, formatDouble(oPregunta.getPuntuacio()));
				}
				appendCVSContent(sbPuntuations, formatDouble(oFull.getDarreraPuntuacio()));
				String sLliuraments = String.valueOf(oFull.getNumCopsLliurat());
				int iMax = oAssignacio.getQuadern().getMaxLliuraments();
				if (iMax>0) {
					sLliuraments +=" / "+iMax;
					appendCVSContent(sbPuntuations, "'"+sLliuraments);
				}else{
					appendCVSContent(sbPuntuations, sLliuraments);					
				}
			}
			appendCVSContent(sbPuntuations, formatDouble(oAssignacio.getDarreraPuntuacio()));
			out.println(sbPuntuations.toString());
		}		
	}
	
	protected void appendCVSContent(StringBuffer sb, Object oContent){
		if (sb!=null){
			sb.append(oContent);
			sb.append(sSeparator);
		}
	}
	
	protected String formatDouble(double d){
		DecimalFormat formatter = new DecimalFormat();
        formatter.setDecimalSeparatorAlwaysShown(true);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		formatter.setDecimalFormatSymbols(dfs);
		return formatter.format(d);
	}

}
