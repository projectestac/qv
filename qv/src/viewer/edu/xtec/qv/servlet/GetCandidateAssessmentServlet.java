/*
 * GetCandidateAssessmentServlet.java
 * 
 * Created on 07/juliol/2004
 */
package edu.xtec.qv.servlet;

import java.io.PrintWriter;
import java.net.URL;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import edu.xtec.qv.servlet.util.Assignacio;
import edu.xtec.qv.servlet.util.FullAssignacio;
import edu.xtec.qv.servlet.util.Quadern;
import edu.xtec.qv.servlet.util.Resposta;

/**
 * @author sarjona
 */
public class GetCandidateAssessmentServlet extends GetAssessment {

	/* (non-Javadoc)
	 * @see edu.xtec.qv.servlet.GetAssessment#getView()
	 */
	protected String getView() {
		return CANDIDATE_VIEW;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.servlet.GetAssessment#updateAndCorrect(javax.servlet.http.HttpServletRequest, java.io.PrintWriter, edu.xtec.qv.servlet.util.Assignacio, edu.xtec.qv.servlet.util.FullAssignacio)
	 */
	protected boolean updateAndCorrect(HttpServletRequest request, PrintWriter out, Assignacio oAssignacio, FullAssignacio oFullActual){
		boolean needsBeSaved = false;
		try{
			Quadern oQuadern = oAssignacio.getQuadern();
			URL urlQTI=new URL(oQuadern.getURL());
			boolean bCorregit = oFullActual.isCorregit();
			if (!bCorregit){
				if ( (oFullActual.getEstatLliurament()== null) || (oFullActual.getEstatLliurament().equals(INTERVENCIO_DOCENT)) ){
					if (oFullActual.getDataDarrerLliurament()!=null){
						oFullActual.setEstatLliurament(LLIURAT);
					} else{
						oFullActual.setEstatLliurament(INICIAT);
					}
					needsBeSaved = true;
				}
				String sUserResponses=getUserResponses(request);
				Vector vUserResponses = Resposta.stringToVector(sUserResponses);
				boolean bModification=(vUserResponses!=null && !vUserResponses.containsAll(oFullActual.getRespostes())) || (oFullActual.getRespostes()!=null && !oFullActual.getRespostes().containsAll(vUserResponses));
				int iEntreguesResten=oQuadern.isSenseLimitEntregues()?1000:oQuadern.getMaxLliuraments()-oFullActual.getNumCopsLliurat();
				// TODO: Revisar limit temps
				//boolean isInTime=(new java.util.GregorianCalendar().getTime().getTime())<=(Utility.convertErroneousDate(oFullActual.getDataDarrerLliurament()).getTime());
				boolean isInTime = true;
				if (bModification && iEntreguesResten>0 && isInTime){
					oFullActual.setRespostes(vUserResponses);
					oFullActual.addTime(request.getParameter("time")); // TODO: Revisar si es necessari actualitzar temps si només es guarda
					needsBeSaved = true;
				}
				if (wantsCorrection(request)){
					if (iEntreguesResten>0 && isInTime){
						oFullActual.incrementarLliuraments();
						incLliuraments(request);
						oFullActual.setEstatLliurament(LLIURAT);
						Date dDataActual = new java.util.Date();					
						oFullActual.setDataDarrerLliurament(dDataActual);
						oAssignacio.setDataDarreraEntrega(dDataActual);
						if (oQuadern.isAutoCorreccio()){
							correctSection(oFullActual, urlQTI); // Actualitza la puntuació i el feedback
						}
						needsBeSaved = true;
					}
					else if (!isInTime)
						writeLimitData(out);
					else
						writeLimitLliuraments(out);
				}
			}			
		}catch (Exception e){
			logger.debug("EXCEPTION updating and correcting assessment -> "+e);
		}
		return needsBeSaved;		
	}
	
	protected boolean isLliurable(Assignacio oAssignacio){
		boolean bLliurable = false;
		if (oAssignacio!=null){
			Quadern oQuadern = oAssignacio.getQuadern();
			int iLliuramentsRestants = oQuadern.getMaxLliuraments() - oAssignacio.getNumCopsLliurat();
			bLliurable = oQuadern==null || oQuadern.isSenseLimitEntregues() || iLliuramentsRestants>0;
		}
		return bLliurable;
	}
	
	protected boolean isCorregible(Assignacio oAssignacio){
		boolean bCorregible = false;
		if (oAssignacio!=null){
			Quadern oQuadern = oAssignacio.getQuadern();
			bCorregible = isLliurable(oAssignacio) && oQuadern.isAutoCorreccio();;
		}
		return bCorregible;
	}

	protected String getEstatIntervencio(){
		return INTERVENCIO_ALUMNE;
	}

}
