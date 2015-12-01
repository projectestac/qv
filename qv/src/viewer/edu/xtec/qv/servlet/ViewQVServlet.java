package edu.xtec.qv.servlet;

/*
 * ViewQVServlet.java
 *
 * Created on 6 / juliol / 2004
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.xtec.qv.servlet.util.Assignacio;
import edu.xtec.qv.servlet.util.BufferedURL;
import edu.xtec.qv.servlet.util.Feedback;
import edu.xtec.qv.servlet.util.FullAssignacio;
import edu.xtec.qv.servlet.util.Intervencio;
import edu.xtec.qv.servlet.util.Pregunta;
import edu.xtec.qv.servlet.util.QTIResponseParser;
import edu.xtec.qv.servlet.util.QTISymtab;
import edu.xtec.qv.servlet.util.QTIToHTMLTransformer;
import edu.xtec.qv.servlet.util.QTITransformed;
import edu.xtec.qv.servlet.util.Quadern;
import edu.xtec.qv.servlet.util.QualificationSymtabs;
import edu.xtec.qv.util.QuadernConfig;
import edu.xtec.qv.util.UserConfig;


/**
 *
 * @author  sarjona
 */
public abstract class ViewQVServlet extends QVServlet{

	public final static File TMP_DIR = new File(System.getProperty("java.io.tmpdir"));

	/**
	 * Parametres
	 */
	public final static String P_ASSIGNMENT_SESSION = "assig_quadern";
	public final static String P_USER_CONFIG = "userConfig";
    
	protected static boolean reloadXSL=QuadernConfig.getBooleanProperty("reloadXSL");//=no(false);
	protected UserConfig uc;
	protected static long totalTime=0;
	protected static long totalQuerys=0;

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	    
	public ViewQVServlet(){
		super();
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.servlet.QVServlet#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		try{
			this.request=request;
			this.response=response;
			if(!response.isCommitted()){
				PrintWriter out = response.getWriter();
				response.setContentType("text/html");
				long l1 = System.currentTimeMillis();
				String sQuadernUrl = null;
				try{
					//logger.debug(this.getClass().getName()+"   process -> session="+request.getSession().getAttribute(P_ASSIGNMENT_ID)+"  request="+request.getParameter(P_ASSIGNMENT_ID)+"  id="+request.getSession().getId());
					updateSpecialPages(request);            
					//if (isChangingAssignment(request)) deleteUserSessionValues(request);
            
					initUserConfig(-1,request.getSession());
					String sAssignacioId = getParameterValue(P_ASSIGNMENT_ID, request);
					/*if (sAssignacioId!=null && sAssignacioId.trim().length()>0 && !sAssignacioId.equals("null"))
						createQVDataManage();*/
					try{
						Assignacio oAssignment = getAssignment(request);
						//logger.debug("ViewQVServlet.processRequest() -> assignacio="+oAssignment);
						if (oAssignment!=null){
							if (oAssignment.getDataFinalitzacio()!=null && (new Date()).compareTo(oAssignment.getDataFinalitzacio())>0){
								out.println("La data màxima per realitzar aquest quadern era "+oAssignment.getDataFinalitzacio());
							}else{
								Quadern oQuadern = oAssignment.getQuadern();
								if (uc!=null && oQuadern!=null && oQuadern.getXSL()!=null && oQuadern.getXSL().length()>0){
									String sXSLPath = oQuadern.getXSL();
									if (QuadernConfig.getXSLForQuadernName(sXSLPath)!=null){
										sXSLPath = QuadernConfig.getXSLForQuadernName(sXSLPath);
									}
									uc.setQuadernXSL(sXSLPath);
								}
								doAction(request, out, oAssignment);								
							}
						}else{
							out.println("Cal indicar el paràmetre assignacioId o quadernURL");
						}
					} catch(IOException ioe){
						out.println("L'enllaç del quadern no és correcte");
					}
				}
				catch (Exception e){
					printErrorLog(request, e);
					//logger.error("EXCEPTION processing request->"+e);
					//e.printStackTrace(System.out);
				}
				/*finally{
					if (c!=null) broker.freeConnectionBean(c);
				}*/
				long l2=System.currentTimeMillis();
				totalTime+=(l2-l1);
				totalQuerys++;
        
				out.flush();
				out.close();
			}			
		} catch(Exception ex){
			printErrorLog(request, ex);
		}
	}

	/**
	 * Fa apuntar a la sessió les planes de final i de sol.licitud de password
	 * @param request
	 * @param session
	 */
	protected void updateSpecialPages(HttpServletRequest request){
		if (request.getParameter(P_FINISHED_PAGE)!=null){
			String sFinishPage=request.getParameter(P_FINISHED_PAGE);
			if (sFinishPage.trim().length()>0) request.getSession().setAttribute(P_FINISHED_PAGE, sFinishPage);
		}
		if (request.getParameter(P_PWD_PAGE)!=null){
			String sPwdPage=request.getParameter(P_PWD_PAGE);
			if (sPwdPage.trim().length()>0) request.getSession().setAttribute(P_PWD_PAGE, sPwdPage);
		}
		if (request.getParameter(P_END_PAGE)!=null){
			String sEndPage=request.getParameter(P_END_PAGE);
			if (sEndPage.trim().length()>0) request.getSession().setAttribute(P_END_PAGE, sEndPage);
		}
	}

	protected void initUserConfig(int iUserId, HttpSession session){
		Object oUc=session.getAttribute(P_USER_CONFIG);
		if (oUc==null){
			if (uc==null) uc=UserConfig.getDefault(iUserId);
			session.setAttribute(P_USER_CONFIG, uc);
		}
		else uc=(UserConfig)oUc;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.servlet.GetAssessment#getReturnPage()
	 */
	protected String getReturnPage() {
		String sReturnPage = "getAssignacio";
		if (getView()!=null && getView().equalsIgnoreCase(TEACHER_VIEW))
			sReturnPage = "getAssignacionsDocent";
		return sReturnPage;
	}	
		   
	protected abstract String getView();
	protected abstract void doAction(HttpServletRequest request, PrintWriter out, Assignacio oAssignment);	
	protected abstract boolean refresh();

//	************************************	
//	* Assignment
//	************************************	

	protected Assignacio getAssignment(HttpServletRequest request) throws IOException{
		Assignacio oAssignment = null;
		try{
			oAssignment = (Assignacio)request.getSession().getAttribute(P_ASSIGNMENT_SESSION);
			String sAssignmentId = request.getParameter(P_ASSIGNMENT_ID);
			String sAssessmentURL = request.getParameter(P_ASSESSMENT_URL);
			//logger.debug("ViewQVServlet.getAssignment()-> session?"+(oAssignment!=null)+"  id="+sAssignmentId+" url="+sAssessmentURL);
			if (sAssignmentId!=null && !sAssignmentId.equals("null") && sAssignmentId.trim().length()>0){
				if (refresh() || oAssignment==null || !sAssignmentId.equalsIgnoreCase(oAssignment.getIdAssignacio())){
					// Es tracta d'una assignacio diferent de la que ja s'estava
					deleteUserSessionValues(request); 
					oAssignment = getAssignmentFromId(sAssignmentId);
					if (oAssignment!=null){
						request.getSession().setAttribute(P_ASSIGNMENT_SESSION,oAssignment);
					}
				}
			} else if (sAssessmentURL!=null && !sAssessmentURL.equals("null") && sAssessmentURL.trim().length()>0){
				if (oAssignment==null || oAssignment.getIdAssignacio()==null || !oAssignment.getIdAssignacio().equalsIgnoreCase("-1") ||
					sAssessmentURL.equalsIgnoreCase(oAssignment.getQuadern().getURL())){
					// Es tracta d'una assignacio diferent de la que ja havia, pero que no necessita base de dades (mode previsualització)
					deleteUserSessionValues(request);
					String sAssessmentXSL = request.getParameter(P_ASSESSMENT_XSL);
					oAssignment = getAssignmentFromURL(sAssessmentURL, sAssessmentXSL);
					if (oAssignment!=null){
						request.getSession().setAttribute(P_ASSIGNMENT_SESSION,oAssignment);
					}
				}
			}
			if (oAssignment!=null){
				String sName = getAssessmentName(oAssignment.getQuadern().getURL());
				oAssignment.getQuadern().setNom(sName);				
			}
		}
		catch (Exception e){
			if (e instanceof IOException){
				throw (IOException)e;
			}else{
				logger.error("EXCEPTION obtenint l'assignacio --> "+e);
				e.printStackTrace();
				oAssignment=null;
			}
		}
		return oAssignment;
	}

	protected Assignacio getAssignmentFromId(String sAssignmentId) throws IOException{
		Assignacio oAssignment = null;
		if (sAssignmentId!=null){
			oAssignment = getQVDataManage().getAssignacio(sAssignmentId);
			if (oAssignment!=null){
				if (oAssignment.getNumFulls() == 0){
					Vector vNomsFulls = getSectionNames(oAssignment.getQuadern().getURL());
					Vector vFulls = FullAssignacio.getFulls(vNomsFulls);
					if (getQVDataManage().initAssignacio(sAssignmentId, vFulls)){
						oAssignment.setFulls(vFulls);
					}
				}
				Hashtable hMetadata = getAssessmentMetadata(oAssignment.getQuadern().getURL());
				oAssignment.setAssessmentMetadata(hMetadata);
			}
		}
		return oAssignment;
	}
	
	protected Assignacio getAssignmentFromURL(String sURL, String sXSL) throws IOException{
		Assignacio oAssignment = null;
		if (sURL!=null){
			Quadern oQuadern = new Quadern(sURL, sXSL);
			Vector vNomsFulls = getSectionNames(sURL);
			//Vector vSections = getSections(sURL);
			Vector vFulls = FullAssignacio.getFulls(vNomsFulls);
			Hashtable hMetadata = getAssessmentMetadata(sURL);
			oAssignment = new Assignacio(null,oQuadern, new Date(), null, null, vFulls);
			oAssignment.setAssessmentMetadata(hMetadata);
		}
		return oAssignment;
	}
    
    
	/**
	 * Obte els noms dels fulls del quadern a partir de la URL indicada
	 * @param sQuadernURL
	 * @return Vector amb els noms dels fulls del quadern de la URL indicada
	 */
	private String getAssessmentName(String sQuadernURL) throws IOException{
		String sAssessmentName = null;
		if (sQuadernURL!=null){
			QTIResponseParser rp=new QTIResponseParser(new URL(sQuadernURL).openStream(),null);
			if (rp!=null){
				sAssessmentName = rp.getAssessmentName();
			}
		}
		return sAssessmentName;
	}
    
	/**
	 * Obte els noms dels fulls del quadern a partir de la URL indicada
	 * @param sQuadernUrl
	 * @return Vector amb els noms dels fulls del quadern de la URL indicada
	 */
	private java.util.Vector getSectionNames(String sQuadernUrl) throws IOException{
		Vector vSectionNames=null;
		if (sQuadernUrl!=null){
			QTIResponseParser rp = new QTIResponseParser(new java.net.URL(sQuadernUrl).openStream(),null);
			vSectionNames = rp.getSectionNames();
		}
		return vSectionNames;
	}
    
	/**
	 * Obte els fulls del quadern a partir de la URL indicada
	 * @param sQuadernUrl
	 * @return Vector amb els noms dels fulls del quadern de la URL indicada
	 */
	private java.util.Vector getSections(String sQuadernUrl) throws IOException{
		Vector vSections=null;
		if (sQuadernUrl!=null){
			QTIResponseParser rp = new QTIResponseParser(new java.net.URL(sQuadernUrl).openStream(),null);
			vSections = rp.getSections();
		}
		return vSections;
	}
    
	/**
	 * Obte els noms dels fulls del quadern a partir de la URL indicada
	 * @param sQuadernUrl
	 * @return Vector amb els noms dels fulls del quadern de la URL indicada
	 */
	private Hashtable getAssessmentMetadata(String sQuadernUrl) throws IOException{
		Hashtable hMetadata=null;
		if (sQuadernUrl!=null){
			QTIResponseParser rp=new QTIResponseParser(new java.net.URL(sQuadernUrl).openStream(),null);
			hMetadata=rp.getAssessmentMetadata();
		}
		return hMetadata;
	}	

	protected void correctSection(FullAssignacio oFull, URL urlQTI) throws Exception{
		/* Actualitza la puntuació i feedback del Full en funció de les seves respostes */
		InputStream isQTIXml = getInputStreamForURL(urlQTI);
		HashMap hmFeedback = new HashMap();
		QTIResponseParser qr = new QTIResponseParser(isQTIXml,hmFeedback);
		QualificationSymtabs qs = new QualificationSymtabs();
		QTISymtab st = qr.getFullPuntuation(oFull.getIdFull(), oFull.getResposta(), qs);
		//logger.debug("symtab="+st+"  full="+oFull);
		if (st!=null){
			Object oPuntuacio=st.getScore();
			if (oPuntuacio!=null && oPuntuacio instanceof Integer) oFull.setDarreraPuntuacio(((Integer)oPuntuacio).intValue());
			else if (oPuntuacio!=null && oPuntuacio instanceof Float) oFull.setDarreraPuntuacio(((Float)oPuntuacio).floatValue());
			oFull.setPreguntes(Pregunta.stringToVector(qs.getSectionScoringRepresentation()));
		}
		oFull.setFeedbacks(Feedback.hashMapToVector(hmFeedback));
		//faq.setCorrected(true);
		//TODO: Revisar si es necesari l'atribut de corrected
	}	

//	************************************	
//	* Servlet
//	************************************	
	/**
	 * Retorna el valor del parametre amb nom sParameterName. 
	 * Primer busca si està entre els paràmetres que rep el servlet. 
	 * Si hi és el posa com a atribut de la sessió. 
	 * Si no està entre els paràmetres el busca a la sessió.
	 * @param sParameterName
	 * @param request
	 * @param session
	 * @return
	 */
	protected String getParameterValue(String sParameterName, HttpServletRequest request){
		String s=null;
		if (request.getParameter(sParameterName)!=null){
			s=request.getParameter(sParameterName);
			if (request.getSession()!=null) request.getSession().setAttribute(sParameterName,s);
		}
		else if (request.getSession()!=null){
			Object o=request.getSession().getAttribute(sParameterName);
			s=(o!=null)?o.toString():null;
		}
		return s;
	}
    
	/**
	 * Retorna el valor del parametre amb nom sParameterName. 
	 * Primer busca si està entre els paràmetres que rep el servlet. 
	 * Si hi és el posa com a atribut de la sessió. 
	 * Si no està entre els paràmetres el busca a la sessió
	 * @param sParameterName
	 * @param request
	 * @param session
	 * @return
	 */
	protected int getParameterIntValue(String sParameterName, HttpServletRequest request){
		int i=-1;
		if (request.getParameter(sParameterName)!=null){
			String s=request.getParameter(sParameterName);
			try{
				i=Integer.parseInt(s);
				if (request.getSession()!=null) request.getSession().setAttribute(sParameterName,new Integer(i));
			}
			catch(Exception e){
				i=-1;
			}
		}
		else if (request.getSession()!=null){
			Object o=request.getSession().getAttribute(sParameterName);
			if (o!=null) i=((Integer)o).intValue();
		}
		return i;
	}
	
	protected void deleteUserSessionValues(HttpServletRequest request){
		request.getSession().removeAttribute(P_ASSIGNMENT_SESSION);
		request.getSession().removeAttribute("full_assig_indiv");
		request.getSession().removeAttribute("assig_indiv");
		request.getSession().removeAttribute("full");
		request.getSession().removeAttribute(P_ASSESSMENT_URL);
		request.getSession().removeAttribute("lliuraments");
		request.getSession().removeAttribute("validated");
	}

	protected void deleteSessionValues(HttpServletRequest request){
		deleteSessionValues(request, null);
	}
    
	/**
	 * Elimina tots els atributs de sessio, excepte els que s'indiquen a hs
	 * @param hs noms dels atributs que no volem esborrar
	 */
	protected void deleteSessionValues(HttpServletRequest request, HashSet hs){
		// En dos recorreguts perquè es produeix una excepció si s'eliminen els elements que estem recorrent
		java.util.Vector vNames=new java.util.Vector();
		java.util.Enumeration e=request.getSession().getAttributeNames();
		while (e.hasMoreElements()){
			String s=e.nextElement().toString();
			if (hs!=null && !hs.contains(s)) vNames.add(s);
		}
		e=vNames.elements();
		while (e.hasMoreElements()){
			String s=e.nextElement().toString();
			request.getSession().removeAttribute(s);
		}
	}
    
	public java.io.InputStream getInputStreamForURL(URL urlQTI){
		java.io.InputStream is = BufferedURL.getInputStreamForURLWithoutBuffered(urlQTI);
		//TODO: Revisar quan s'ha de posar al buffer el quadern
		//is=BufferedURL.getServletInputStreamForURL(servlet, urlQTI);
		return is;
	}

	public String getContext(){
		return getServletContext().getRealPath("/");
	}
	
	protected void updateAssignacioQuadern(HttpServletRequest request, URL urlQTI, Assignacio oAssignacio, boolean bOnlyState, boolean bGoFast){
		try{
			//String sQuadernFeedback=aqi.getEstatResposta();//"";
			String sEstatLliurament=null;
            
			if (!bGoFast){
				Vector vFulls=oAssignacio.getFulls();
				Enumeration enumFulls = vFulls.elements();
                
				java.util.Vector vUserResponses = new java.util.Vector();
				double dPuntuacioQuadern = 0;
				int iLliuraments = 0;
				while (enumFulls.hasMoreElements()){
					FullAssignacio oFull = (FullAssignacio)enumFulls.nextElement();
					vUserResponses.add(oFull.getResposta());
					dPuntuacioQuadern += oFull.getDarreraPuntuacio(); 
					if (oFull.getNumCopsLliurat()>iLliuraments){
						iLliuraments=oFull.getNumCopsLliurat();
					}
				}
				if (!bOnlyState){ // Volem també actualitzar la puntuació i el feedback.
					oAssignacio.setDarreraPuntuacio(dPuntuacioQuadern);
					oAssignacio.setNumCopsLliurat(iLliuraments);

					/*java.util.HashMap hmQuadernFeedback = new java.util.HashMap();
					java.io.InputStream isQTIXml=urlQTI.openStream();
					QTIResponseParser qr=new QTIResponseParser(isQTIXml,hmQuadernFeedback);
					QTISymtab st=qr.getQuadernPuntuation(vUserResponses);
					if (st!=null){
						Object oPuntuacio=st.getScore();
						double dPuntuacio = 0;
						if (oPuntuacio!=null && oPuntuacio instanceof Integer) dPuntuacio=((Integer)oPuntuacio).intValue();
						else if (oPuntuacio!=null && oPuntuacio instanceof Float) dPuntuacio=(((Float)oPuntuacio).floatValue());
						oAssignacio.setDarreraPuntuacio(dPuntuacio);
					}*/
					//aqi.setFeedback(sQuadernFeedback);
				}
                
				String sNouEstat = getQVDataManage().getEstatQuadern(oAssignacio.getIdAssignacio());
				if (sNouEstat!= null && !sNouEstat.equalsIgnoreCase(oAssignacio.getEstatLliurament())){
					sEstatLliurament = sNouEstat;
					oAssignacio.setEstatLliurament(sEstatLliurament);
				}
			}
			String sTime=request.getParameter("time");
			if (sTime!=null) {
				oAssignacio.addTime(sTime);
			} 
			
			/*Object iNumLliuraments = request.getSession().getAttribute("lliuraments");
			if (iNumLliuraments != null){
				oAssignacio.setNumCopsLliurat(((Integer)iNumLliuraments).intValue());
			}*/
			getQVDataManage().updateAssignacio(oAssignacio);
			//db.updateQuadernResponse(sIdAssignacio,"",iPuntuacioQuadern,sEstatLliurament,sQuadernFeedback,null,true,aqi.getTime());
		}
		catch (Exception e){
			logger.error("EXCEPCIO actualitzant l'assignacio '"+oAssignacio+"'--> "+e);
			e.printStackTrace();
		}
	}
	
	protected void incLliuraments(HttpServletRequest request){
		Object o=request.getSession().getAttribute("lliuraments");
		if (o!=null){
			int i=((Integer)o).intValue();
			request.getSession().setAttribute("lliuraments",new Integer(i+1));
		}
		else request.getSession().setAttribute("lliuraments",new Integer(1));
	}
    
	
    
//	************************************	
//	* HTML
//	************************************

	protected void writeRedirectionPageTo(PrintWriter out, String sUrl){
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>Quaderns Virtuals</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY onLoad=\"parent.location='"+sUrl+"'\">");
		out.println("</BODY>");
		out.println("</HTML>");
	}
    
	/**
	 * Transforma l'xml d'una plana d'un quadern en html amb les respostes,
	 * feedbacks i intervencions.
	 * @param serverUrl
	 * @param oAssignacio
	 * @param oFullActual
	 * @param bModificable
	 * @param bCorregible
	 * @param out
	 */
	public void writeHTMLPage(HttpServletRequest request, Assignacio oAssignacio, FullAssignacio oFullActual, boolean bModificable, boolean bCorregible, PrintWriter out){
		try{
			if (oFullActual == null){
				writeNotebookFinished(request, out,oAssignacio);
			} else{
				String serverURL = request.getRequestURI();
				Quadern oQuadern = oAssignacio.getQuadern();
				String sQuadernURL=oQuadern.getURL();
				String sXSLPath = request.getParameter(P_ASSESSMENT_XSL);
				if (sXSLPath==null || sXSLPath.trim().length()<=0 || sXSLPath.indexOf("http://")<0){
					sXSLPath = oQuadern.getXSL();
					if (sXSLPath==null || sXSLPath.trim().length()<=0 ){
						if (request.getParameterMap().isEmpty()){
							sXSLPath = (String)request.getSession().getAttribute(P_ASSESSMENT_XSL);
							logger.debug("xsl session-> "+sXSLPath);
						}
					}
					sXSLPath = QuadernConfig.getXSLForQuadernName(sXSLPath);
					if (sXSLPath==null || sXSLPath.trim().length()<=0 ){
						sXSLPath = QuadernConfig.getDefaultXSL();
					}
				}
				request.getSession().setAttribute(P_ASSESSMENT_XSL, sXSLPath);
				// TODO: revisar numSections=6000 o getNumFulls???
				//int numSections=60000; //aqd.getNumSections();
				int numSections = oAssignacio.getNumFulls();
				//int iUserId=aqi.getIdAlumne();
				int iIdFull=oFullActual.getIdFull();
				String sUserResponses=oFullActual.getResposta();
				//logger.debug("sUserResponses="+sUserResponses);
				String sFeedback = Feedback.feedbacksToString(oFullActual.getFeedbacks());
				String sInteraction = Intervencio.intervencionsToString(oFullActual.getIntervencions());
				String sectionTime = oFullActual.getTime();
				String date=(oFullActual.getDataDarrerLliurament()!=null)?new java.text.SimpleDateFormat("yyyy-MM-dd").format(oFullActual.getDataDarrerLliurament()):null;
				if (request!=null && request.getParameter("color")!=null){
					request.getSession().setAttribute("color",request.getParameter("color"));
				}
				boolean lastSection = false;
				java.net.URL urlQTI = new java.net.URL(sQuadernURL);
				java.io.InputStream isQTIXml=getInputStreamForURL(urlQTI);
				java.util.HashMap hmFeedback=new java.util.HashMap();
				QTIResponseParser qr=new QTIResponseParser(isQTIXml,hmFeedback);
				//numSections=getNumSections(qr,sQuadernURL);
				String sNovaIntervencio=oFullActual.getNovaIntervencio();
				sInteraction=(sNovaIntervencio!=null)?(sInteraction+sNovaIntervencio):sInteraction;
				
				if (reloadXSL || (sFeedback!=null && sFeedback.length()>0) ||
				(sInteraction!=null && sInteraction.length()>0) || 
				oAssignacio.getIdAssignacio()==null )  { //Hi ha feedback o no es vol cache, cal fer una transformació de l'XML+XSL
					QTIToHTMLTransformer tr=getQTIToHTMLTransformer(request, sXSLPath);
					//if (oAssignacio.getIdAssignacio()==null /*&& request.getParameter("nocache")!=null */){
					if (oAssignacio.getIdAssignacio()==null){
						isQTIXml=BufferedURL.getInputStreamForURLWithoutBuffered(urlQTI);
					}else{
						isQTIXml=BufferedURL.getInputStreamForURL(urlQTI);
					}
                    
					javax.xml.transform.Source xmlSource=new javax.xml.transform.stream.StreamSource(isQTIXml);
					xmlSource.setSystemId("");//030311 util.QuadernConfig.getProperty("dtdsUrl"));
					// TODO: Revisar transform (eliminar parametros innecesarios)
					tr.transform(-1,request.getSession().getAttribute("userId"),oFullActual.getIdFull(),numSections,oFullActual.getNumCopsLliurat(), oQuadern.getMaxLliuraments(), xmlSource,out,oFullActual.isCorregit(oQuadern.isAutoCorreccio())?sFeedback:"",sInteraction,sUserResponses,getView(),serverURL,getReturnPage(),uc.getQuadernBG(),null,sQuadernURL,oQuadern.getXSL(), oQuadern.isObligatoriContestar()+"",oFullActual.getTime(),sectionTime,oFullActual.isCorregit(oQuadern.isAutoCorreccio())?"true":"false",bCorregible?"true":"false",(!bModificable)?"true":"false",oFullActual.isCorregit(oQuadern.isAutoCorreccio())?Pregunta.vectorToString(oFullActual.getPreguntes()):null,date,oQuadern.isWriteIntervencions()?"true":"false", oFullActual.getEstatLliurament(), (String)request.getSession().getAttribute("color"), oAssignacio.getIdAssignacio());
					//tr.transform(-1,session.getAttribute("userId"),oFullActual.getIdFull(),numSections,xmlSource,out,oFullActual.isCorrected()?sFeedback:"",sInteraction,sUserResponses,view,serverUrl,sReturnPage,uc.getQuadernBG(),null,sQuadernURL,oQuadern.isObligatoriContestar()+"",oFullActual.getTime(),sectionTime,oFullActual.isCorrected()?"true":"false",bCorregible?"true":"false",(!bModificable)?"true":"false",oFullActual.isCorrected()?Pregunta.vectorToString(oFullActual.getPreguntes()):null,date,oQuadern.isWriteIntervencions()?"true":"false", oFullActual.getEstatLliurament(), (String)session.getAttribute("color"));
					//tr.transform(iUserId,session.getAttribute("userId"),iNumFull,numSections,xmlSource,out,faq.isCorrected()?sFeedback:"",sInteraction,sUserResponses,view,serverUrl,sReturnPage,uc.getQuadernBG(),null,sQTIUrl,aqd.getContestar()+"",aqi.getTime(),sectionTime,faq.isCorrected()?"true":"false",canCorrect?"true":"false",(!canModify)?"true":"false",faq.isCorrected()?Pregunta.vectorToString(faq.getPreguntes()):null,date,aqd.isWritingEnabled()?"true":"false", faq.getEstatLliurament(), (String)session.getAttribute("color"));
				}
				else{ //No hi ha feedback, ens serveix el QTITransformed.
					QTITransformed tr = getQTITransformed(oFullActual.getIdFull(),numSections,oFullActual.getNumCopsLliurat(), oQuadern.getMaxLliuraments(), urlQTI,sXSLPath);
					tr.transform(out,-1,request.getSession().getAttribute("userId")!=null?request.getSession().getAttribute("userId").toString():"",oFullActual.isCorregit(oQuadern.isAutoCorreccio())?sFeedback:"",sInteraction,sUserResponses,getView(),serverURL,getReturnPage(),sQuadernURL, oQuadern.getXSL(), oQuadern.isObligatoriContestar(),!bModificable,bCorregible,!bModificable,oFullActual.isCorregit(oQuadern.isAutoCorreccio())?Pregunta.vectorToString(oFullActual.getPreguntes()):null,date,oQuadern.isWriteIntervencions(), oFullActual.getEstatLliurament(), (String)request.getSession().getAttribute("color"), oFullActual.getNumCopsLliurat());
					//tr.transform(out,iUserId,session.getAttribute("userId")!=null?session.getAttribute("userId").toString():"",faq.isCorrected()?sFeedback:"",sInteraction,sUserResponses,view,serverUrl,sReturnPage,sQTIUrl,aqd.getContestar(),!canModify,canCorrect,!canModify,faq.isCorrected()?Pregunta.vectorToString(faq.getPreguntes()):null,date,aqd.isWritingEnabled(), faq.getEstatLliurament(), (String)session.getAttribute("color"));
				}
			}
		}
		catch (Exception e){
			logger.error("ERROR obtenint la pagina HTML per l'assignacio '"+oAssignacio+"'-->  "+e);
		}
	}
    	
	protected void writeNotebookFinished(HttpServletRequest request, PrintWriter out,Assignacio oAssignacio){
		Object o=request.getSession().getAttribute("endPage");
		deleteSessionValues(request);
		if (o!=null && o.toString().trim().length()>0){
			// O bé li paso directament la web o bé li paso un html que el redireccioni a la web.
			// Per descarregar al servidor de feina, trio la segona opció tot i que la primera seria millor per l'usuari.
			writeRedirectionPageTo(out,o.toString());
		}
		else if (oAssignacio!=null && oAssignacio.getQuadern()!=null && oAssignacio.getQuadern().getEndPage()!=null && oAssignacio.getQuadern().getEndPage().trim().length()>0){
			writeRedirectionPageTo(out,oAssignacio.getQuadern().getEndPage());
		}
		else if (getReturnPage()!=null && getReturnPage().trim().length()>0){
			writeRedirectionPageTo(out,getReturnPage());
		}
		else {
			out.println("Ja s'ha acabat la realització del quadern.");
		} 
	}
    

//	************************************	
//	* QTI - XSL
//	************************************

	private QTITransformed getQTITransformed(int newNumFull, int numSections, int iSectionLimit, int iMaxLimit, java.net.URL urlQTI, String sXSLPath){
		QTITransformed tr=null;
        
		String sKey=(sXSLPath+"_"+urlQTI.toString()+newNumFull).replace('/','-').replace(':','-').replace('?','-');
		File f=new File(TMP_DIR, "qv_"+sKey+".ser");
		logger.debug("getQTITransformed-> file="+f+"  exists?"+f.exists());
		try{
			if (f.exists()){
				java.io.FileInputStream in = new java.io.FileInputStream(f);
				java.io.ObjectInputStream s = new java.io.ObjectInputStream(in);
				try{
					tr=(QTITransformed)s.readObject();
					s.close();
				}
				catch (Exception e){
					e.printStackTrace();
					f.delete();
					tr=getQTITransformedNotCreated(f,newNumFull, numSections, iSectionLimit, iMaxLimit, urlQTI, sXSLPath);
				}
			}
			else{
				tr=getQTITransformedNotCreated(f,newNumFull, numSections, iSectionLimit, iMaxLimit, urlQTI, sXSLPath);
			}
		}
		catch (Exception e){
			e.printStackTrace(System.out);
            
		}
		return tr;
	}
    
	private QTITransformed getQTITransformedNotCreated(java.io.File f, int newNumFull, int numSections, int iSectionLimit, int iMaxLimit, java.net.URL urlQTI, String sXSLPath){
		QTITransformed tr=null;
        
		try{
			java.io.InputStream isQTIXml=BufferedURL.getInputStreamForURL(urlQTI);
			
			javax.xml.transform.Source xmlSource=new javax.xml.transform.stream.StreamSource(isQTIXml);
			xmlSource.setSystemId("");
            
			//javax.xml.transform.Source xsl=new javax.xml.transform.stream.StreamSource(new java.net.URL(util.QuadernConfig.transformURLValue(sXSLPath, getContext()/*getServletContext()*/)).openStream());
			java.io.InputStream is=getClass().getClassLoader().getResourceAsStream(sXSLPath);
			javax.xml.transform.Source xsl=new javax.xml.transform.stream.StreamSource(is);
            
			tr=QTITransformed.createSection(newNumFull,numSections, iSectionLimit, iMaxLimit, xmlSource,xsl,getServletContext().getServletContextName());
            
			java.io.FileOutputStream out = new java.io.FileOutputStream(f);
			java.io.ObjectOutputStream s = new java.io.ObjectOutputStream(out);
			s.writeObject(tr);
			//f.deleteOnExit();
			s.close();
		}
		catch (Exception e){
			e.printStackTrace(System.out);
		}
		return tr;
	}
    
	private QTIToHTMLTransformer getQTIToHTMLTransformer(HttpServletRequest request, String sXSLPath){
		QTIToHTMLTransformer tr=null;
		try{
			if (getContext()!=null){ //El buscaré al context del servlet
				Object o=getServletContext().getAttribute(sXSLPath);
				if (o!=null && !reloadXSL)
					tr=(QTIToHTMLTransformer)o;
			}
			else{ //No té context de servlet... el buscaré a la sessió
				Object o=request.getSession().getAttribute(sXSLPath);
				if (o!=null && !reloadXSL)
					tr=(QTIToHTMLTransformer)o;
			}
			if (tr==null){ //No estava creat. Cal crear-lo i emmagatzemar-lo on toqui
				tr=new QTIToHTMLTransformer();
				//javax.xml.transform.Source xsl=new javax.xml.transform.stream.StreamSource(new java.net.URL(sXSLPath, getContext()/*getServletContext()*/)).openStream());
				java.io.InputStream is=getClass().getClassLoader().getResourceAsStream(sXSLPath);
				javax.xml.transform.Source xsl=null;
				if (is==null)
					xsl=new javax.xml.transform.stream.StreamSource(new java.net.URL(QuadernConfig.transformURLValue(sXSLPath, getContext())).openStream());
				else
					xsl=new javax.xml.transform.stream.StreamSource(is);
                
				tr.setStyleSheet(xsl,getContext());
				if (getServletContext()!=null) getServletContext().setAttribute(sXSLPath,tr);
				else request.getSession().setAttribute(sXSLPath,tr);
			}
		}
		catch (Exception e){
			logger.error("EXCEPCIO en aconseguir QTIToHTMLTranformer--> "+e);
		}
		return tr;
	}
    	
}
