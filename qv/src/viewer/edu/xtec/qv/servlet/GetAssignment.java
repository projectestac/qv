package edu.xtec.qv.servlet;

/*
 * GetAssignments.java
 *
 * Created on 6 / juliol / 2004
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import edu.xtec.qv.servlet.util.Assignacio;
import edu.xtec.qv.servlet.util.AssignacionsUsuariToHTMLTransformer;
import edu.xtec.qv.servlet.util.BufferedURL;
import edu.xtec.qv.servlet.util.FullAssignacio;
import edu.xtec.qv.servlet.util.Quadern;
import edu.xtec.qv.util.QuadernConfig;


/**
 *
 * @author  sarjona
 */
public abstract class GetAssignment extends ViewQVServlet{
   
	public GetAssignment(){
		super();
	}

	protected void doAction(HttpServletRequest request, PrintWriter out, Assignacio oAssignment){
		try{
			if (oAssignment!=null){
				if (oAssignment.getQuadern()!=null){
					BufferedURL.clearURL(new URL(oAssignment.getQuadern().getURL()));
				}
				boolean wantsCorrection=(request.getParameter("wantCorrect")!=null)?request.getParameter("wantCorrect").equalsIgnoreCase("true"):false;
				if (wantsCorrection && oAssignment.getIdAssignacio()!=null){
					correctNotebookAndSections(oAssignment,!isDocent());
				}
				generateResponse(request, oAssignment, out);
			}
		} catch(Exception ex){
			logger.error("EXCEPTION :"+ex);
			ex.printStackTrace();
		}
	}

	protected boolean refresh(){
		return true;
	}
	
	
	/**
	 * 
	 * @param oAssignacio
	 * @param bIncLliuraments indica si s'ha d'incrementar el numero de lliuraments
	 * @throws Exception
	 */
	protected void correctNotebookAndSections(Assignacio oAssignacio,boolean bIncLliuraments) throws Exception{
		URL urlQTI = new java.net.URL(oAssignacio.getQuadern().getURL());
		Enumeration enumFulls = oAssignacio.getFulls().elements();
		Quadern oQuadern = oAssignacio.getQuadern();
		boolean bInc = false;
		while (enumFulls.hasMoreElements()){ // Corregeixo un a un els fulls del quadern (sempre que calgui)
			FullAssignacio oFull = (FullAssignacio)enumFulls.nextElement();
			// TODO: Comprobar quan es necessari corregir un full (es necessari l'atribut corrected?)
			correctSection(oFull, urlQTI);
			if (isDocent()) {
				if (oFull.isCorregible()){
					oFull.setEstatLliurament(CORREGIT); 
					oAssignacio.setEstatLliurament(CORREGIT);
				}
			} else{
				int iEntreguesResten=oQuadern.isSenseLimitEntregues()?1000:oQuadern.getMaxLliuraments()-oFull.getNumCopsLliurat();
				if (iEntreguesResten>0){
					if (bIncLliuraments){
						oFull.incrementarLliuraments();
						bInc=true;
					}
					oFull.setEstatLliurament(LLIURAT);
					oAssignacio.setEstatLliurament(LLIURAT);
					Date dDataActual = new java.util.Date();					
					oFull.setDataDarrerLliurament(dDataActual);
					oAssignacio.setDataDarreraEntrega(dDataActual);
					if (oQuadern.isAutoCorreccio()){
						correctSection(oFull, urlQTI); // Actualitza la puntuació i el feedback
					}
				}

			}
			getQVDataManage().updateFullAssignacio(oAssignacio.getIdAssignacio(), oFull);
		}
		if (bInc){
			incLliuraments(request);
		}
		updateAssignacioQuadern(request, urlQTI, oAssignacio, false, false); //!onlyState,!goFast			
		//TODO: Revisar updateQuadernData (es necessari??)
		//updateQuadernData(urlQTI, oAssignacio, false, false); //!onlyState,!goFast
	}
    		
	private void generateResponse(HttpServletRequest request, Assignacio oAssignacio, PrintWriter out) throws Exception{
		//String sXSLPath=QuadernConfig.getDefaultAssigXSLPath(); //.getXSLForAssignName(uc.getAssigXSL());
		String sXSLPath = request.getParameter(P_ASSESSMENT_XSL);
		if (oAssignacio!=null){
			Quadern oQuadern = oAssignacio.getQuadern();
			if (oQuadern!=null){
				sXSLPath = oQuadern.getXSL();
			}
		}
		if (sXSLPath==null || sXSLPath.equalsIgnoreCase("null")  || sXSLPath.trim().length()<=0){
			//sXSLPath=QuadernConfig.getDefaultAssigXSLPath();
			sXSLPath=QuadernConfig.getDefaultXSL();
		}
		//logger.debug("xsl="+sXSLPath+" url="+QuadernConfig.getXSLForQuadernName(sXSLPath));
		if (QuadernConfig.getXSLForQuadernName(sXSLPath)!=null){
			sXSLPath = QuadernConfig.getXSLForQuadernName(sXSLPath);
		}
		request.getSession().setAttribute(P_ASSESSMENT_XSL, sXSLPath);
		
		AssignacionsUsuariToHTMLTransformer tr=getAssigTransformer(request, sXSLPath);
        
		//java.io.InputStream is=getClass().getClassLoader().getResourceAsStream(sXSLPath);
		try{
			java.io.InputStream is = null;
			String sXSL = null;
			if (getServletContext()!=null){
				if (sXSLPath!=null && !sXSLPath.startsWith("http") && !sXSLPath.startsWith("file") ){
					sXSL = getServletContext().getRealPath(java.io.File.separator+sXSLPath);
				}else{
					sXSL = sXSLPath;
				}
				if (!sXSL.startsWith("http")){
					sXSL = "file:///"+sXSL;   
				}     	
			} else{
				sXSL = getContext()+sXSLPath;
			}
			URL xslURL = new URL(sXSL);
			try{
				is = xslURL.openStream();
			}catch(FileNotFoundException fe){
				if (getServletContext()!=null) sXSL = "file:///"+getServletContext().getRealPath(java.io.File.separator+QuadernConfig.getDefaultXSL());
				else  sXSL = "file:///"+getContext()+QuadernConfig.getDefaultXSL();
				xslURL = new URL(sXSL);
				is = xslURL.openStream();
			}
			javax.xml.transform.Source xsl=new javax.xml.transform.stream.StreamSource(is);
			tr.setStyleSheet(xsl,getContext());
		}
		catch(Exception e){
			System.out.println("Ex:"+e);
			e.printStackTrace();
		}
        
		//org.w3c.dom.Document dXML=AssignacionsXML.getAssignmentXML(db,sIdAssignacio, isDocent);
		//Assignacio oAssignacio = db.getAssignacio(sIdAssignacio);
		//Assignacio oAssignacio = getAssignacio(session, sIdAssignacio);
		org.w3c.dom.Document dXML=null;
		if (oAssignacio!=null){
			dXML = oAssignacio.toXML(isDocent());
		}
		if (dXML!=null){
			try{
				//logger.debug("<!------ assignacio.xml ");
				//Assignacio.printDOMTree(dXML);
				//logger.debug("\n asignacio.xml --------->");
				if (request!=null && request.getParameter("color") != null) request.getSession().setAttribute("color", request.getParameter("color"));
				javax.xml.transform.Source xmlSource=new javax.xml.transform.dom.DOMSource(dXML);
				xmlSource.setSystemId("");
				tr.transform(xmlSource,out,"","","/qv/"+getServletName(),"","", (String)request.getSession().getAttribute("color"),isDocent()?"teacher":"candidate"); //
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		else
			out.println("Identificador no vàlid.");
	}
    
	protected AssignacionsUsuariToHTMLTransformer getAssigTransformer(HttpServletRequest request, String sXSLPath){
		AssignacionsUsuariToHTMLTransformer tr=null;
		try{
			if (getServletContext()!=null){ //El buscaré al context del servlet
				Object o=getServletContext().getAttribute(sXSLPath);
				if (o!=null && !reloadXSL)
					tr=(AssignacionsUsuariToHTMLTransformer)o;
			}
			else{ //No té context de servlet... el buscaré a la sessió
				Object o=request.getSession().getAttribute(sXSLPath);
				if (o!=null && !reloadXSL)
					tr=(AssignacionsUsuariToHTMLTransformer)o;
			}
			if (tr==null){ //No estava creat. Cal crear-lo i emmagatzemar-lo on toqui
				tr=new AssignacionsUsuariToHTMLTransformer();
				//javax.xml.transform.Source xsl=new javax.xml.transform.stream.StreamSource(new java.net.URL(util.QuadernConfig.transformURLValue(sXSLPath, getContext())).openStream());
				java.io.InputStream is=getClass().getClassLoader().getResourceAsStream(sXSLPath);
				javax.xml.transform.Source xsl=null;
				if (is==null)
					xsl=new javax.xml.transform.stream.StreamSource(new java.net.URL(QuadernConfig.transformURLValue(sXSLPath, getContext())).openStream());
				else 
					xsl=new javax.xml.transform.stream.StreamSource(is);
				try{
					tr.setStyleSheet(xsl,getContext());
				}
				catch(Exception e){
					System.out.println("Ex:"+e);
				}
				if (getServletContext()!=null) getServletContext().setAttribute(sXSLPath,tr);
				else request.getSession().setAttribute(sXSLPath,tr);
			}
		}
		catch (Exception e){
			logger.error("EXCEPCIO en crear URL de xsl de transformacions '"+sXSLPath+"'--> "+e);
		}
		return tr;
	}
	
	public String getContext(){
		return getServletContext().getRealPath("/");
	}
	
	protected boolean isDocent(){
		return getView()!=null && getView().equalsIgnoreCase(TEACHER_VIEW);
	}
		
}
