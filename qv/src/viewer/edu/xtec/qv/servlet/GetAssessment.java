/*
 * GetAssessment.java
 * 
 * Created on 07/juliol/2004
 */
package edu.xtec.qv.servlet;

import java.io.PrintWriter;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import edu.xtec.qv.servlet.util.Assignacio;
import edu.xtec.qv.servlet.util.FullAssignacio;
import edu.xtec.qv.servlet.util.Intervencio;
import edu.xtec.qv.servlet.util.Quadern;
import edu.xtec.qv.servlet.util.Resposta;

/**
 * @author sarjona
 */
public abstract class GetAssessment extends ViewQVServlet {

	/* (non-Javadoc)
	 * @see edu.xtec.qv.servlet.QVServlet#doAction(javax.servlet.http.HttpServletRequest, java.io.PrintWriter, edu.xtec.qv.servlet.util.Assignacio)
	 */
	protected void doAction(HttpServletRequest request,	PrintWriter out, Assignacio oAssignment) {
		try{
			if (oAssignment!=null){ 
				int iIdFull = getParameterIntValue("full",request);
				if (request.getParameter("param")!=null){ //Està fent un lliurament.
					lliuramentAction(request, out, oAssignment, iIdFull);
				} else{ //Està sol.licitant un quadern
					showNotebookAction(request, out, oAssignment, iIdFull);
				}
			}
		}catch (Exception e){
			logger.error("EXCEPTION obtenint quadern -> "+e);
			e.printStackTrace();
		}
	}
	
	protected boolean refresh(){
		return (request.getParameter("refresh")!=null);
	}	

	protected boolean isAskingForNewInteraction(String sInteraction){
		if (sInteraction!=null && sInteraction.trim().length()>0 && sInteraction.charAt(0)!='#') return true;
		else return false;
	}

	/**
	 * Actualitza el full i la base de dades, afegint-li la nova intervencio
	 * @param oFull
	 * @param sIntervencions
	 * @return true si s'afegeix una nova intervencio correctament (tant a la base de dades com al full); false en cas contrari
	 */
	protected boolean actualitzaIntervencions(String sIdAssignacio, FullAssignacio oFull, String sIntervencions){
		if (sIntervencions!=null && sIntervencions.indexOf("interaccio_")>=0){ 
			//Comprovo que no hagi omplert l'alumne cap camp d'interacció
			int iIni=sIntervencions.indexOf("interaccio_");
			int iEnd=sIntervencions.indexOf("#",iIni);
			if (iEnd>0){
				String sNovaInteraccio=sIntervencions.substring(iIni+11,iEnd);
				String sPregunta = sNovaInteraccio.substring(sNovaInteraccio.indexOf("=")+1);
				if (sNovaInteraccio.trim().length()>0 && sPregunta.trim().length()>0){ //Només afegeixo l'intervenció si realment ha escrit alguna cosa.
					Intervencio oIntervencio = new Intervencio(sIdAssignacio, oFull.getIdFull(), sNovaInteraccio);
					oFull.addIntervencio(oIntervencio);
					boolean bOk = getQVDataManage().addIntervencio(oIntervencio);
					return bOk;
				}
			}
		}
		return false;
	}
    
	protected void writeLimitData(PrintWriter out){
		out.println("No pots lliurar mes cops el quadern.<BR/>Ha passat la data de lliurament.");
	}
    
	protected void writeLimitLliuraments(PrintWriter out){
		out.println("No pots lliurar mes cops el full.<BR/>Has exhaurit els lliuraments.");
	}
        
	/* (non-Javadoc)
	 * @see edu.xtec.qv.servlet.GetAssessment#showNotebookAction(javax.servlet.http.HttpServletRequest, java.io.PrintWriter, edu.xtec.qv.servlet.util.Assignacio, int)
	 */
	protected void showNotebookAction(HttpServletRequest request, PrintWriter out, Assignacio oAssignacio, int iIdFull){
		if (iIdFull<=0) {
			try{
				String sParams = "?";
				if (request.getParameter("assignacioId")!=null){
					sParams+="assignacioId="+request.getParameter("assignacioId");
				}
				if (request.getParameter("quadernURL")!=null){
					if (!sParams.endsWith("?")) sParams+="&";
					sParams+="quadernURL="+request.getParameter("quadernURL");
				}
				if (request.getParameter("quadernXSL")!=null){
					if (!sParams.endsWith("?")) sParams+="&";
					sParams+="quadernXSL="+request.getParameter("quadernXSL");
				}
				response.sendRedirect(getReturnPage()+sParams);
				//writeRedirectionPageTo(out, getReturnPage());
			}catch (Exception e){
				logger.error("ERROR redireccionant a '"+getReturnPage()+"'");
				e.printStackTrace();
			}
		} else{
			Quadern oQuadern = oAssignacio.getQuadern();
			if (oAssignacio==null || oQuadern == null){
				//todo: pendent implementar writefinished a showNotebookAction
				logger.debug("Pendent implementar writeFinished (assignacio o quadern null)");
				//writeFinished(aq, out);
				return;
			}
			FullAssignacio oFullActual = oAssignacio.getFull(iIdFull);
			writeHTMLPage(request, oAssignacio, oFullActual, isLliurable(oAssignacio), isCorregible(oAssignacio), out); 
		}		
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.servlet.GetAssessment#lliuramentAction(javax.servlet.http.HttpServletRequest, java.io.PrintWriter, edu.xtec.qv.servlet.util.Assignacio, int)
	 */
	protected void lliuramentAction(HttpServletRequest request, PrintWriter out, Assignacio oAssignacio, int iIdFull) throws Exception{
		// TODO: Revisar si es necessari inicialitzar bCorregit amb el valor del full actual o del nou
		Quadern oQuadern = oAssignacio.getQuadern();
		int iIdFullActual = getParameterIntValue("currentSect", request);
		int iIdFullSeguent = getParameterIntValue("next",request);
		FullAssignacio oFullActual = oAssignacio.getFull(iIdFullActual);
		URL urlQTI=new URL(oQuadern.getURL());
		if (oAssignacio.getIdAssignacio()!=null){
			boolean needsBeSaved = false;
			boolean bCorregit = oFullActual.isCorregit();
			
			// Actualitzar intervencions
			boolean bIntervencions = actualitzaIntervencions(oAssignacio.getIdAssignacio(), oFullActual,request.getParameter("interaction")); /*Potser ha omplert un camp d'interacció*/
			if (!bCorregit && bIntervencions){
				oFullActual.setEstatLliurament(getEstatIntervencio());
				needsBeSaved = true;
			}
			if (isAskingForNewInteraction(request.getParameter("interaction"))){
				oFullActual.addNovaIntervencio(request.getParameter("interaction"));
			}
			
			// Actualitzar respostes i corregir si fa falta
			needsBeSaved = needsBeSaved || updateAndCorrect(request, out, oAssignacio, oFullActual);
			
			if (needsBeSaved){
				getQVDataManage().updateFullAssignacio(oAssignacio.getIdAssignacio(), oFullActual);
				updateAssignacioQuadern(request, urlQTI, oAssignacio, false, false); //!onlyState,!goFast			
			}			
		}else{
			// Mode previsualització (no es necessari guardar-ho a la base de dades)
			if (wantsCorrection(request)){
				String sUserResponses=getUserResponses(request);
				Vector vUserResponses = Resposta.stringToVector(sUserResponses);
				oFullActual.setRespostes(vUserResponses);
				oFullActual.setEstatLliurament(LLIURAT);
				oFullActual.setDataDarrerLliurament(new java.util.Date());
				correctSection(oFullActual, urlQTI); // Actualitza la puntuació i el feedback
			}			
		}
		showNotebookAction(request, out, oAssignacio, (iIdFullSeguent>=0)?iIdFullSeguent:iIdFullActual);
	}
	
	protected String getUserResponses(HttpServletRequest request){
		String sUserResponses=request.getParameter("param").trim();
		sUserResponses = replaceLineSeparator(sUserResponses);
		sUserResponses = replace(sUserResponses, "\"", "\\\"");
		sUserResponses = replace(sUserResponses, "'", "\\'");
		return sUserResponses;
	}

	public static String replaceLineSeparator(String str) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(str, "\n");
		//logger.debug("tokens="+st.countTokens());
		if (st.countTokens()>0){
			while (st.hasMoreTokens()){
				String sToken = st.nextToken();
				sb.append(sToken.substring(0, sToken.length()-1));
				if (st.countTokens()>0) sb.append("\\n");
			}
			return sb.toString();
		}
		return str;
	}

	public static String replace(String str, String pattern, String replace) {
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();
        
		if (str!=null){
			while ((e = str.indexOf(pattern, s)) >= 0) {
				result.append(str.substring(s, e));
				if(replace!=null)
					result.append(replace);
				s = e+pattern.length();
			}
			result.append(str.substring(s));
		}
		return result.toString();
	}

	protected boolean wantsCorrection(HttpServletRequest request){
		return (getParameterValue("wantCorrect",request)!=null)?getParameterValue("wantCorrect",request).equals("true"):false;
	}
	
	protected abstract boolean isLliurable(Assignacio oAssignacio);
	protected abstract boolean isCorregible(Assignacio oAssignacio);
	protected abstract String getEstatIntervencio();
	/**
	 * Actualitzar respostes i corregir si fa falta
	 * @param request
	 * @param out
	 * @param oAssignacio
	 * @param oFullActual
	 * @return si s'ha modificat l'assignacio retorna true; en cas contrari, false
	 */
	protected abstract boolean updateAndCorrect(HttpServletRequest request, PrintWriter out, Assignacio oAssignacio, FullAssignacio oFullActual);

}
