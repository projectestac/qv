package edu.xtec.qv.servlet.util;
/*
 * Assignacio.java
 * Created on 20-ene-2004
 * 
 */

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.xtec.qv.servlet.QVServlet;
import edu.xtec.qv.util.Utility;

/**
 * @author sarjona
 */
public class Assignacio {

	public static final String GET_QUADERN_ALUMNE="getQuadernAlumne";
	public static final String GET_QUADERN_DOCENT="getQuadernDocent";
	
	private static Logger logger = Logger.getRootLogger();
	
	private String sIdAssignacio;
	private Quadern oQuadern;
	private Date dDataCreacio;
	private Date dFinalitzacio;
	private String sIP;
	private Vector vFulls;
	private Hashtable hMetadata;
	
	public Assignacio(String sIdAssignacio, Quadern oQuadern, Date dDataCreacio, Date dFinalitzacio, String sIP, Vector vFulls){
		this.sIdAssignacio=sIdAssignacio;
		this.oQuadern=oQuadern;
		this.dDataCreacio=dDataCreacio;
		this.dFinalitzacio=dFinalitzacio;
		this.sIP=sIP;
		this.vFulls=vFulls;
	}

	public Assignacio(String sIdAssignacio, Quadern oQuadern, Date dDataCreacio, Date dFinalitzacio, String sIP){
		this (sIdAssignacio, oQuadern, dDataCreacio, dFinalitzacio, sIP, null);
	}

	/**
	 * @return
	 */
	public String getIdAssignacio() {
		return sIdAssignacio;
	}

	/**
	 * @return
	 */
	public Quadern getQuadern() {
		return oQuadern;
	}

	/**
	 * @return
	 */
	public Date getDataCreacio() {
		return dDataCreacio;
	}
	
	public Date getDataFinalitzacio() {
		return dFinalitzacio;
	}

	public String getIP() {
		return sIP;
	}
	
	
	public FullAssignacio getFull(int iIdFull){
		FullAssignacio oFull = null;
		if (vFulls != null){
			Enumeration enumFulls = vFulls.elements();
			while (oFull==null && enumFulls.hasMoreElements()){
				FullAssignacio tmpFull = (FullAssignacio)enumFulls.nextElement();
				if (tmpFull.getIdFull()==iIdFull){
					oFull = tmpFull;
				}
			}
		}
		return oFull;
	}

	/**
	 * @return Vector de <code>FullQuadern</code>
	 */
	public Vector getFulls() {
		Vector vFullsSenseInicial = new Vector();
		if (vFulls != null){
			Enumeration enumFulls = vFulls.elements();
			while(enumFulls.hasMoreElements()){
				FullAssignacio oFull = (FullAssignacio)enumFulls.nextElement();
				if (!oFull.isInicial()){
					vFullsSenseInicial.addElement(oFull);
				}
			}
		}
		return vFullsSenseInicial;
	}
	
	public FullAssignacio getFullInicial(){
		return getFull(0);		
	}
	
	public void setFulls(Vector vFulls){
		this.vFulls=vFulls;
	}
	
	public int getNumFulls(){
		return (getFulls().size());
	}
	
	public String getEstatLliurament(){
		String sEstatLliurament = null;
		FullAssignacio oFullQuadern = getFullInicial();
		if (oFullQuadern != null){
			sEstatLliurament = oFullQuadern.getEstatLliurament();
		}
		return sEstatLliurament;
	}
	
	public void setEstatLliurament(String sEstatLliurament){
		FullAssignacio oFullQuadern = getFullInicial();
		if (oFullQuadern != null){
			oFullQuadern.setEstatLliurament(sEstatLliurament);
		}
	}
	
	public Hashtable getEstats(){
		Hashtable hEstats = new Hashtable();
		if (getNumFulls()>1){
		   Enumeration tmpEnum = getFulls().elements();
		   boolean bIsComencat = false;
		   boolean bIsPendentRevisio = false;
		   boolean bIsPendentModificacio = false;
		   boolean bIsLliurat = false;
		   boolean bIsTotsLliurats = true;
		   boolean bIsCorregit = false;
		   boolean bIsTotsCorregits = true;
		   while (tmpEnum.hasMoreElements()){
			FullAssignacio oFull=(FullAssignacio)tmpEnum.nextElement();
			if (!oFull.isInicial()){
				if ( oFull.getEstatLliurament() == null || oFull.getEstatLliurament().equals(QVServlet.NO_INICIAT) ){
					bIsTotsLliurats = false;
					bIsTotsCorregits = false;
				} else if (oFull.getEstatLliurament().equals(QVServlet.INICIAT)){
					bIsComencat = true;
				}else if (oFull.getEstatLliurament().equals(QVServlet.INTERVENCIO_ALUMNE) || oFull.getEstatLliurament().equals("pendent_revisio")){
					bIsPendentRevisio = true;
					bIsComencat = true;
				} else if (oFull.getEstatLliurament().equals(QVServlet.INTERVENCIO_DOCENT) || oFull.getEstatLliurament().equals("pendent_modificacio")){
					bIsPendentModificacio = true;
					bIsComencat = true;
				} else if (oFull.getEstatLliurament().equals(QVServlet.LLIURAT)){
					bIsLliurat = true;
					bIsComencat = true;
				} else if (oFull.getEstatLliurament().equals(QVServlet.CORREGIT)){
					bIsComencat = true;
					bIsLliurat = true;  
					bIsCorregit = true;
				}
				if (bIsTotsLliurats && !oFull.isInState(QVServlet.LLIURAT) && !oFull.isInState(QVServlet.CORREGIT)){
				   bIsTotsLliurats = false;
				}
				if (bIsTotsCorregits && !oFull.isInState(QVServlet.CORREGIT)){
					bIsTotsCorregits = false;
				}
			}
		  }
			
		    hEstats.put(QVServlet.NO_INICIAT, new Boolean(!bIsComencat)); 					// Tots els fulls estan o sense estat o no començats
		    hEstats.put(QVServlet.INICIAT, new Boolean(bIsComencat));						// No hi ha cap full  sense començar
			hEstats.put(QVServlet.INTERVENCIO_ALUMNE, new Boolean(bIsPendentRevisio));		// Existeix algun quadern amb una intervencio feta per l'alumne
			hEstats.put(QVServlet.INTERVENCIO_DOCENT, new Boolean(bIsPendentModificacio)); 	// Existeix algun quadern amb una intervencio del professor
			hEstats.put(QVServlet.PARCIALMENT_LLIURAT, new Boolean(bIsLliurat || bIsTotsCorregits));	// Existeix algun full lliurat
			hEstats.put(QVServlet.LLIURAT, new Boolean(bIsTotsLliurats));					// Tots els fulls del quadern estan lliurats
			hEstats.put(QVServlet.PARCIALMENT_CORREGIT, new Boolean(bIsCorregit));			// Existeix algun full corregit
			hEstats.put(QVServlet.CORREGIT, new Boolean(bIsTotsCorregits));					// Tots els fulls estan corregits
		} else {
			hEstats.put(QVServlet.NO_INICIAT, Boolean.TRUE); 	
			hEstats.put(QVServlet.INICIAT, Boolean.FALSE);
			hEstats.put(QVServlet.INTERVENCIO_ALUMNE, Boolean.FALSE);
			hEstats.put(QVServlet.INTERVENCIO_DOCENT, Boolean.FALSE);
			hEstats.put(QVServlet.PARCIALMENT_LLIURAT, Boolean.FALSE);
			hEstats.put(QVServlet.LLIURAT, Boolean.FALSE);
			hEstats.put(QVServlet.PARCIALMENT_CORREGIT, Boolean.FALSE);
			hEstats.put(QVServlet.CORREGIT, Boolean.FALSE);			
		}
		return hEstats;
	}
	

	/**
	 * Obte una hash amb les parelles (ESTAT, boolea). Cada parella indica si el full 
	 * es troba en l'estat indicat.
	 * @return hashtable amb les parelles (ESTAT, boolea), que indiquen si el full es troba o no en cada estat
	 */
	public static Hashtable getEstats(String sEstat, Date dDataLliurament){
		Hashtable hEstats = new Hashtable();
		boolean bIsComencat = false;
		boolean bIsPendentRevisio = false;
		boolean bIsPendentModificacio = false;
		boolean bIsLliurat = false;
		boolean bIsCorregit = false;
		if (sEstat!=null){
			if (sEstat.equals(QVServlet.INICIAT)){
				bIsComencat = true;
			}else if (sEstat.equals(QVServlet.INTERVENCIO_ALUMNE) || sEstat.equals("pendent_revisio")){
				bIsPendentRevisio = true;
				bIsComencat = true;
			} else if (sEstat.equals(QVServlet.INTERVENCIO_DOCENT) || sEstat.equals("pendent_modificacio")){
				bIsPendentModificacio = true;
				bIsComencat = true;
			} else if (sEstat.equals(QVServlet.CORREGIT)){
				bIsComencat = true; 
				bIsLliurat = true;
				bIsCorregit = true;
			} else if (sEstat.equals(QVServlet.LLIURAT) || dDataLliurament!=null){
				bIsLliurat = true;
				bIsComencat = true;
			}
		}
		
		hEstats.put(QVServlet.NO_INICIAT, new Boolean(!bIsComencat)); 					// Tots els fulls estan o sense estat o no començats
		hEstats.put(QVServlet.INICIAT, new Boolean(bIsComencat));						// No hi ha cap full  sense començar
		hEstats.put(QVServlet.INTERVENCIO_ALUMNE, new Boolean(bIsPendentRevisio));		// Existeix algun quadern amb una intervencio feta per l'alumne
		hEstats.put(QVServlet.INTERVENCIO_DOCENT, new Boolean(bIsPendentModificacio)); 	// Existeix algun quadern amb una intervencio del professor
		hEstats.put(QVServlet.PARCIALMENT_LLIURAT, new Boolean(bIsLliurat || bIsCorregit));	// Existeix algun full lliurat
		hEstats.put(QVServlet.LLIURAT, new Boolean(bIsLliurat));					// Tots els fulls del quadern estan lliurats
		hEstats.put(QVServlet.PARCIALMENT_CORREGIT, new Boolean(bIsCorregit));			// Existeix algun full corregit
		hEstats.put(QVServlet.CORREGIT, new Boolean(bIsCorregit));					// Tots els fulls estan corregits			
		return hEstats;		
	}
		
	public Date getDataDarreraEntrega(){
		Date dDataDarreraEntrega = null;
		FullAssignacio oFullQuadern = getFullInicial();
		if (oFullQuadern != null){
			dDataDarreraEntrega = oFullQuadern.getDataDarrerLliurament();
		}
		return dDataDarreraEntrega;
	}
	
	public void setDataDarreraEntrega(Date dDataDarreraEntrega){
		FullAssignacio oFullQuadern = getFullInicial();
		if (oFullQuadern != null){
			oFullQuadern.setDataDarrerLliurament(dDataDarreraEntrega);
		}
	}
	
	public double getDarreraPuntuacio(){
		double dDarreraPuntuacio = 0;
		FullAssignacio oFullQuadern = getFullInicial();
		if (oFullQuadern != null){
			dDarreraPuntuacio = oFullQuadern.getDarreraPuntuacio();
		}
		return dDarreraPuntuacio;
	}
	
	public void setDarreraPuntuacio(double dDarreraPuntuacio)
	{
		FullAssignacio oFullQuadern = getFullInicial();
		if (oFullQuadern != null){
			oFullQuadern.setDarreraPuntuacio(dDarreraPuntuacio);
		}
	}
	
	public int getNumCopsLliurat(){
		int iNumCopsLliurat = 0;
		FullAssignacio oFullQuadern = getFullInicial();
		if (oFullQuadern != null){
			iNumCopsLliurat = oFullQuadern.getNumCopsLliurat();
		}
		return iNumCopsLliurat;
	}
	
	public void setNumCopsLliurat(int iNumCopsLliurat){
		FullAssignacio oFullQuadern = getFullInicial();
		if (oFullQuadern != null){
			oFullQuadern.setNumCopsLliurat(iNumCopsLliurat);
		}
	}
	
	public void addTime(String sTime){
		FullAssignacio oFullQuadern = getFullInicial();
		if (oFullQuadern != null){
			oFullQuadern.addTime(sTime);
		}
	}
	
	public Hashtable getAssessmentMetadata(){
		return hMetadata;
	}
	public void setAssessmentMetadata(Hashtable hMetadata){
		this.hMetadata=hMetadata;
	}
	public void addAssessmentMetadata(String sLabel, String sEntry){
		if (sLabel!=null && sEntry!=null){
			if (hMetadata==null){
				hMetadata = new Hashtable();
			}
			hMetadata.put(sLabel, sEntry);
		}
	}
	
	public Document toXML(boolean isDocent) throws ParserConfigurationException {
		Document doc = null;
		doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element eRoot = doc.createElement("quadern_assignments");
		doc.appendChild(eRoot);
		Element eStudent = doc.createElement("student");
		eRoot.appendChild(eStudent);
		Element eAssignment=createAssignment(doc,isDocent?GET_QUADERN_DOCENT:GET_QUADERN_ALUMNE);
		eRoot.appendChild(eAssignment);
		return doc;
	}

	protected Element createAssignment(org.w3c.dom.Document doc, String sURL){
		Quadern oQuadern = getQuadern();
		if (oQuadern == null){
			 return null; 
		}
		Element eAssignment=doc.createElement("assignment");
		eAssignment.setAttribute("id", getIdAssignacio());
		eAssignment.setAttribute("name",oQuadern.getNomQuadern());
		//eAssignment.setAttribute("urlQuadern",sURL+"?assignacioId="+getIdAssignacio());
		eAssignment.setAttribute("servlet",sURL);
		if (getQuadern()!=null){
			if(getQuadern().getURL()!=null) eAssignment.setAttribute("quadernURL",getQuadern().getURL());
			if(getQuadern().getXSL()!=null) eAssignment.setAttribute("quadernXSL",getQuadern().getXSL());
		}

		if (getAssessmentMetadata()!=null){
			Element eMetadata=doc.createElement("qtimetadata");
			Enumeration enumMetadata = getAssessmentMetadata().keys();
			while(enumMetadata.hasMoreElements()){
				String sLabel = (String)enumMetadata.nextElement();
				String sEntry = (String)getAssessmentMetadata().get(sLabel);
				Element eMetadataField=doc.createElement("qtimetadatafield");
				Element eFieldLabel = doc.createElement("fieldlabel");
				eFieldLabel.appendChild(doc.createTextNode(sLabel));
				eMetadataField.appendChild(eFieldLabel);
				Element eFieldEntry = doc.createElement("fieldentry");
				eFieldEntry.appendChild(doc.createTextNode(sEntry));
				eMetadataField.appendChild(eFieldEntry);
				eMetadata.appendChild(eMetadataField);
			}
			eAssignment.appendChild(eMetadata);
		}
		// Estats
		Element eStates = doc.createElement("assignment_states");
		Hashtable hEstats = getEstats();
		Enumeration enumEstats = hEstats.keys();
		while (enumEstats.hasMoreElements()){
			String sStateName = (String)enumEstats.nextElement();
			Element eState=doc.createElement(sStateName);			
			eState.appendChild(doc.createTextNode(hEstats.get(sStateName).toString()));
			eStates.appendChild(eState);
		}
		eAssignment.appendChild(eStates);


		if (getEstatLliurament()!=null){
			Element eState=doc.createElement("state");
			eState.appendChild(doc.createTextNode(getEstatLliurament()));
			eAssignment.appendChild(eState);
		}
		if (oQuadern.getDescripcio()!=null){
			Element eDescription=doc.createElement("description");
			eDescription.appendChild(doc.createTextNode(oQuadern.getDescripcio()));
			eAssignment.appendChild(eDescription);
		}
		if (getDataDarreraEntrega()!=null){
			Element ePuntuation=doc.createElement("puntuation");
			ePuntuation.appendChild(doc.createTextNode(""+getDarreraPuntuacio()));
			eAssignment.appendChild(ePuntuation);
		}
		if (getDataCreacio()!=null){
			Element eAssignmentDate=doc.createElement("assignment_date");
			eAssignmentDate.appendChild(doc.createTextNode(Utility.toStringDate(getDataCreacio())));
			eAssignment.appendChild(eAssignmentDate);
		}
		if (getDataDarreraEntrega()!=null){
			Element eFinishDate=doc.createElement("finish_date");
			eFinishDate.appendChild(doc.createTextNode(Utility.toStringDate(getDataDarreraEntrega())));
			eAssignment.appendChild(eFinishDate);
		}
		if (!oQuadern.isSenseLimitEntregues()){
			Element eLimit=doc.createElement("limit");
			eLimit.appendChild(doc.createTextNode(""+(oQuadern.getMaxLliuraments()-getNumCopsLliurat())));
			eAssignment.appendChild(eLimit);
		}
		// Número màxim de lliuraments 
		Element eMaxDelivery = doc.createElement("max_delivery");
		eMaxDelivery.appendChild(doc.createTextNode(String.valueOf(getQuadern().getMaxLliuraments())));
		eAssignment.appendChild(eMaxDelivery); 

		// Número total de lliuraments (màxim entre els lliuraments dels fulls) 
		Element eAssessLimit = doc.createElement("assessment_limit");
		eAssessLimit.appendChild(doc.createTextNode(String.valueOf(getNumCopsLliurat())));
		eAssignment.appendChild(eAssessLimit); 

		Vector vFulls= getFulls();		
		if (vFulls!=null){
			Element eFulls=doc.createElement("sections");
			Enumeration e=vFulls.elements();
			while (e.hasMoreElements()){
				FullAssignacio oFull=(FullAssignacio)e.nextElement();
				org.w3c.dom.Element eFull=createFull(doc,oFull);
				eFulls.appendChild(eFull);
			}
			eAssignment.appendChild(eFulls);
		}
		//logger.debug("<!------ assignacio.xml ");
		//printDOMTree(eAssignment);
		//logger.debug("\nasignacio.xml --------->");
		return eAssignment;
	}
	
	protected Element createFull(Document doc, FullAssignacio oFull){
		org.w3c.dom.Element eFull=doc.createElement("section");
		eFull.setAttribute("num",""+oFull.getIdFull());
		eFull.setAttribute("id",oFull.getSectionId());
		eFull.setAttribute("name",oFull.getNomFull());
		if (/*oFull.isCorrected() &&*/ oFull.getRespostes()!=null && oFull.getRespostes().size()>0){ 
			org.w3c.dom.Element ePuntuation=doc.createElement("section_puntuation");
			ePuntuation.appendChild(doc.createTextNode(""+oFull.getDarreraPuntuacio()));
			eFull.appendChild(ePuntuation);
		}
		if (oFull.getEstatLliurament()!=null){
			org.w3c.dom.Element eState=doc.createElement("section_state");
			eState.appendChild(doc.createTextNode(oFull.getEstatLliurament()));
			eFull.appendChild(eState);
		}
		if (getQuadern()!=null && !getQuadern().isSenseLimitEntregues()){
			org.w3c.dom.Element eLimit=doc.createElement("section_limit");
			eLimit.appendChild(doc.createTextNode(""+(getQuadern().getMaxLliuraments()-oFull.getNumCopsLliurat())));
			eFull.appendChild(eLimit);
		}
		if (oFull!=null){
			// Estats
			Element eStates=doc.createElement("section_states");
			Hashtable hEstats = getEstats(oFull.getEstatLliurament(), oFull.getDataDarrerLliurament());
			Enumeration enumEstats = hEstats.keys();
			while (enumEstats.hasMoreElements()){
				String sStateName = (String)enumEstats.nextElement();
				Element eState=doc.createElement(sStateName);			
				eState.appendChild(doc.createTextNode(hEstats.get(sStateName).toString()));
				eStates.appendChild(eState);
			}
			eFull.appendChild(eStates);
			// Numero de cops lliurat
			Element eDelivery = doc.createElement("section_delivery");
			eDelivery.appendChild(doc.createTextNode(String.valueOf(oFull.getNumCopsLliurat())));
			eFull.appendChild(eDelivery);
		}
		return eFull;
	}
	
	
	public String toString(){
		return "Assignacio ("
			+  "id="+getIdAssignacio()
			+  ", quadern="+getQuadern()
			+  ", dataCreacio="+getDataCreacio()
			+  ", fulls="+getFulls()
			+  ")";
	}
	
	/** Prints the specified node, recursively. */
	public static void printDOMTree(Node node) 
	{
	  int type = node.getNodeType();
	  switch (type)
	  {
		// print the document element
		case Node.DOCUMENT_NODE: 
		  {
			printDOMTree(((Document)node).getDocumentElement());
			break;
		  }

		  // print element with attributes
		case Node.ELEMENT_NODE: 
		  {
			System.out.print("\n<");
			System.out.print(node.getNodeName());
			NamedNodeMap attrs = node.getAttributes();
			for (int i = 0; i < attrs.getLength(); i++)
			{
			  Node attr = attrs.item(i);
			  System.out.print(" " + attr.getNodeName() + 
						"=\"" + attr.getNodeValue() + 
						"\"");
			}
			System.out.print(">");

			NodeList children = node.getChildNodes();
			if (children != null)
			{
			  int len = children.getLength();
			  for (int i = 0; i < len; i++)
				printDOMTree(children.item(i));
			}

			break;
		  }

		  // handle entity reference nodes
		case Node.ENTITY_REFERENCE_NODE: 
		  {
			System.out.print("&");
			System.out.print(node.getNodeName());
			System.out.print(";");
			break;
		  }

		  // print cdata sections
		case Node.CDATA_SECTION_NODE: 
		  {
			System.out.print("<![CDATA[");
			System.out.print(node.getNodeValue());
			System.out.print("]]>");
			break;
		  }

		  // print text
		case Node.TEXT_NODE: 
		  {
			System.out.print(node.getNodeValue());
			break;
		  }

		  // print processing instruction
		case Node.PROCESSING_INSTRUCTION_NODE: 
		  {
			System.out.print("<?");
			System.out.print(node.getNodeName());
			String data = node.getNodeValue();
			{
				System.out.print(" ");
				System.out.print(data);
			}
			System.out.print("?>");
			break;
		  }
	  }

	  if (type == Node.ELEMENT_NODE)
	  {
		System.out.print("</");
		System.out.print(node.getNodeName());
		System.out.print('>');
	  }
	} // printDOMTree(Node, PrintWriter)	

}
