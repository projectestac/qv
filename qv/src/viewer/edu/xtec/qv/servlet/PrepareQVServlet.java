package edu.xtec.qv.servlet;
/*
 * PrepareQVServlet.java
 *
 * Created on 08 / juliol / 2004
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.xtec.qv.servlet.util.Quadern;

/**
 * @author  sarjona
 */
public class PrepareQVServlet extends QVServlet{
    
	public static String P_ASSESSMENT_URL = "quadernUrl";
	public static String P_AUTOADVANCE = "autoAdvance"; //No es fa servir
	public static String P_AUTOCORRECCIO = "correccio";
	public static String P_MAX_LLIURAMENTS = "max";
	public static String P_OBLIGATORI_CONTESTAR = "contestar";
	public static String P_WRITE_INTERVENCIONS = "anotacions";
	public static String P_END_DATE = "endDate";

	/**
	 * Parametres
	 */
	public static final int DEFAULT_MAX_LLIURAMENTS = Quadern.SENSE_LIMIT_ENTREGUES;
	public static final boolean DEFAULT_AUTOCORRECCIO = true;
	public static final boolean DEFAULT_OBLIGATORI_CONTESTAR = false;
	public static final boolean DEFAULT_WRITE_INTERVENCIONS = true;
	public static final boolean DEFAULT_AUTOADVANCE = true;
	public static final String QUADERN_NOT_FOUND="Quadern no trobat";

	public PrepareQVServlet(){
		super();
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		boolean bOk=false;
		PrintWriter out = response.getWriter();
		Exception e2=null;
		try{
			//db = createQVDataManage();
			bOk = createAssignacio(request);
		}
		catch (Exception e){
			bOk=false;
			e2=e;
		}
		/*finally{
			freeConnection();
		}
		*/
        
		if (bOk){
			out.println("ok");
		}
		else out.println("error"+((e2!=null)?(": "+e2.toString()):""));
		out.close();
    }

	protected boolean createAssignacio(HttpServletRequest request) throws Exception{
		boolean bOk=false;
		String sIdAssignacio=request.getParameter(P_ASSIGNMENT_ID);
		String sQuadernURL=request.getParameter(P_ASSESSMENT_URL);
		String sQuadernXSL=request.getParameter(P_ASSESSMENT_XSL);
		String sEndPage=request.getParameter(P_END_PAGE); // pagina que es mostra despres de visualitzat l'ultim full
		String sMaxLliuraments=request.getParameter(P_MAX_LLIURAMENTS); // nombre maxim de lliuraments
		String sAutoCorreccio=request.getParameter(P_AUTOCORRECCIO); // corregir automaticament quan es lliura un quadern
		String sObligatoriContestar=request.getParameter(P_OBLIGATORI_CONTESTAR); // indica si es obligatori contestar totes les preguntes abans de canviar de full
		String sAutoAdvance=request.getParameter(P_AUTOADVANCE); // avançar automaticament quan es lliura un quadern
		String sWriteIntervencions=request.getParameter(P_WRITE_INTERVENCIONS); //mostrar anotacions
		String sEndDate=request.getParameter(P_END_DATE); //data de finalitzacio
        
		int iMaxLliuraments = DEFAULT_MAX_LLIURAMENTS;
		boolean bAutoCorreccio = DEFAULT_AUTOCORRECCIO;
		boolean bObligatoriContestar = DEFAULT_OBLIGATORI_CONTESTAR;
		boolean bAutoAdvance = DEFAULT_AUTOADVANCE;
		boolean bWriteIntervencions = DEFAULT_WRITE_INTERVENCIONS;
        
		if (!existURL(sQuadernURL)) throw new Exception(){
			public String toString(){
				return QUADERN_NOT_FOUND;
			}
		};
		
		Date dEndDate = null;			
		try{
			long lMilis = Long.parseLong(sEndDate);
			dEndDate = new Date(lMilis);			
		}catch (Exception e){
			dEndDate = null;			
		}
		
		try{
			if (sMaxLliuraments!=null) iMaxLliuraments = Integer.parseInt(sMaxLliuraments);
			if (sAutoCorreccio!=null) bAutoCorreccio = sAutoCorreccio.equals("0")?false:true;
			if (sObligatoriContestar!=null) bObligatoriContestar=sObligatoriContestar.equals("0")?false:true;
			if (sAutoAdvance!=null) bAutoAdvance = sAutoAdvance.equals("0")?false:true; 
			if (sWriteIntervencions!=null) bWriteIntervencions = sWriteIntervencions.equals("0")?false:true;
			if (sEndPage==null) sEndPage="";
			if (dEndDate==null) {
				long lTime = (new Long((new Date()).getTime()+Long.parseLong("7776000000"))).longValue();
				dEndDate=new Date();
				dEndDate.setTime(lTime);
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		if (sIdAssignacio!=null) {
			String sIP = ((HttpServletRequest)request).getRemoteAddr();			
			bOk=getQVDataManage().createAssignacio(sIdAssignacio, sQuadernURL, sQuadernXSL, sEndPage, dEndDate, iMaxLliuraments, bAutoCorreccio, bObligatoriContestar, bAutoAdvance, bWriteIntervencions, sIP);
		}
		return bOk;
	}
    
	private boolean existURL(String sUrl){
		boolean bExist=false;
		try{
			java.net.URL u=new java.net.URL(sUrl);
			java.net.URLConnection uc=u.openConnection();
			if (uc instanceof java.net.HttpURLConnection)
				bExist=(((java.net.HttpURLConnection)uc).getResponseCode()==200);
			else return true; // file:// per a les proves locals
		}
		catch (Exception e){
			return false;
		}
		return bExist;
	}
    
    
}
