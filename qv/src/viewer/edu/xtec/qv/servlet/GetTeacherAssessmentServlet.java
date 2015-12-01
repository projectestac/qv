/*
 * GetTeacherAssessmentServlet.java
 * 
 * Created on 07/juliol/2004
 */
package edu.xtec.qv.servlet;

import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import edu.xtec.qv.servlet.util.Assignacio;
import edu.xtec.qv.servlet.util.FullAssignacio;
import edu.xtec.qv.servlet.util.Quadern;

/**
 * @author sarjona
 */
public class GetTeacherAssessmentServlet extends GetAssessment {

	/* (non-Javadoc)
	 * @see edu.xtec.qv.servlet.GetAssessment#getView()
	 */
	protected String getView() {
		return TEACHER_VIEW;
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
			int iEntreguesResten=oQuadern.isSenseLimitEntregues()?1000:oQuadern.getMaxLliuraments()-oFullActual.getNumCopsLliurat();
			if ( iEntreguesResten<=0 && !bCorregit ){
				oFullActual.setEstatLliurament(LLIURAT);
				needsBeSaved = true;
			}
		        
			boolean wantsCorrection=(getParameterValue("wantCorrect",request)!=null)?getParameterValue("wantCorrect",request).equals("true"):false;
			if (wantsCorrection(request)){ //El docent el corregeix
				//if ((faq.getEstatLliurament()!=null && faq.getEstatLliurament().equals(LLIURAT))){ 
				//TODO: Revisar quan es pot corregit un full: si només quan està lliurat o bé en qualsevol cas
				correctSection(oFullActual, urlQTI); // Actualitza la puntuació i el feedback
				oFullActual.setEstatLliurament(CORREGIT);
				needsBeSaved = true;
				//}
			}
		}catch (Exception e){
			logger.debug("EXCEPTION updating and correcting assessment -> "+e);
		}
		return needsBeSaved;
	}

	protected boolean isLliurable(Assignacio oAssignacio){
		return false;
	}
	
	protected boolean isCorregible(Assignacio oAssignacio){
		return true;
	}
	
	protected String getEstatIntervencio(){
		return INTERVENCIO_DOCENT;
	}
	
}
