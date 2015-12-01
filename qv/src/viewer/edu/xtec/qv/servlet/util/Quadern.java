package edu.xtec.qv.servlet.util;

/*
 * Quadern.java
 * Created on 20-ene-2004
 *
 */

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * @author sarjona
 */
public class Quadern {
	
	public static int SENSE_LIMIT_ENTREGUES=-1;
	private static Logger logger = Logger.getRootLogger();
	
	private int iIdQuadern;
	private String sNomQuadern;
	private String sDescripcio;
	private String sURL;
	private String sXSL;
	private String sEndPage;
	private Date dDataCreacio;
	private int iMaxLliuraments;
	private boolean bAutoCorreccio;
	private boolean bObligatoriContestar;
	private boolean bAutoAdvance;
	private boolean bWriteIntervencions;
	
	public Quadern(String sURL, String sXSL){
		this(-1, sURL, sXSL, null, new Date(), SENSE_LIMIT_ENTREGUES, true, false, false, true);
	}
	
	public Quadern(int iIdQuadern, String sURL, String sXSL, String sEndPage,
			Date dDataCreacio, int iMaxLliuraments, boolean bAutoCorreccio, 
			boolean bObligatoriContestar, boolean bAutoAdvance, boolean bWriteIntervencions){
		this.iIdQuadern=iIdQuadern;
		this.sURL=sURL;
		this.sXSL=sXSL;
		this.sEndPage=sEndPage;
		this.dDataCreacio=dDataCreacio;
		this.iMaxLliuraments=iMaxLliuraments;
		this.bAutoCorreccio=bAutoCorreccio;
		this.bObligatoriContestar=bObligatoriContestar;
		this.bAutoAdvance=bAutoAdvance;
		this.bWriteIntervencions=bWriteIntervencions;
	}
	
	/**
	 * @return
	 */
	public int getIdQuadern() {
		return iIdQuadern;
	}
	
	public void setIdQuadern(int iIdQuadern){
		this.iIdQuadern=iIdQuadern;
	}

	/**
	 * @return
	 */
	public String getNomQuadern() {
		return sNomQuadern;
	}
	
	public void setNom(String sNomQuadern){
		this.sNomQuadern=sNomQuadern;
	}
	
	/**
	 * @return
	 */
	public String getDescripcio(){
		return sDescripcio;
	}

	/**
	 * @return
	 */
	public boolean isAutoAdvance() {
		return bAutoAdvance;
	}

	/**
	 * @return
	 */
	public boolean isAutoCorreccio() {
		return bAutoCorreccio;
	}

	/**
	 * @return
	 */
	public boolean isObligatoriContestar() {
		return bObligatoriContestar;
	}

	/**
	 * @return
	 */
	public boolean isWriteIntervencions() {
		return bWriteIntervencions;
	}

	/**
	 * @return
	 */
	public Date getDataCreacio() {
		return dDataCreacio;
	}

	/**
	 * @return
	 */
	public int getMaxLliuraments() {
		return iMaxLliuraments;
	}

	public boolean isSenseLimitEntregues(){
		return iMaxLliuraments==SENSE_LIMIT_ENTREGUES;
	}
    
	/**
	 * @return
	 */
	public String getEndPage() {
		return sEndPage;
	}

	/**
	 * @return
	 */
	public String getURL() {
		return sURL;
	}

	/**
	 * @return
	 */
	public String getXSL() {
		return sXSL;
	}
	
	public String toString(){
		return "Quadern("
			+  "idQuadern="+getIdQuadern()
			+  ", URL="+getURL()
			+  ", XSL="+getXSL()
			+  ", endPage="+getEndPage()
			+  ", dataCreacio="+getDataCreacio()
			+  ", maxLliuraments="+getMaxLliuraments()
			+  ", autoCorreccio="+isAutoCorreccio()
			+  ", obligatoriContestar="+isObligatoriContestar()
			+  ", autoAdvance="+isAutoAdvance()
			+  ", writeIntervencions="+isWriteIntervencions()
			+  ");";
	}

}
