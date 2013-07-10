/*
 * QVRespostaBean.java
 * 
 * Created on 03/març/2004
 */
package edu.xtec.qv.editor.beans;


/**
 * @author sarjona
 */
public interface IQVResponseBean {

	public final String CARDINALITAT_PARAM = "cardinalitat";
	public final String IDENT_RESPOSTES_PARAM = "ident_respostes";
	public final String IDENT_RESPONSE_LID_PARAM = "ident_resp_lid";
	public final String RECURS_RESPOSTES_PARAM = "recurs_respostes";
	public final String MOSTRAR_LLISTA_PARAM = "mostrar_llista";
	public final String NUM_RESPOSTES_PARAM = "num_respostes";
	public final String NUM_RESPOSTES_LINIA_PARAM = "num_respostes_linia";
	public final String RESPOSTES_CORRECTES_PARAM = "respostes_correctes";
	public final String TEXT_RESPOSTES_PARAM = "text_resposta";

	/**
	 * @param sIdentResp identificador del conjunt de respostes (com per exemple del ResponseLID o del ResponseSTR)
	 * @return número de respostes amb identificadors o text que té el conjunt de respostes indicat
	 */
	public int getNumResponseLabels(String sIdentResp);

	/**
	 * @param sIdentResp identificador del conjunt de respostes (com per exemple del ResponseLID o del ResponseSTR)
	 * @param iPosition posicio del ResponseLabel
	 * @return identificador del ResponseLabel que es troba a la posicio indicada
	 */
	public String getIdentResponseLabel(String sIdentResp, int iPosition);
	
	public String getResponseMaterialType(String sIdentResp);
	
	
	/**
	 * @param sIdentResp identificador del conjunt de respostes (com per exemple del ResponseLID o del ResponseSTR)
	 * @param sIdentRespLabel identificador d'una resposta concreta (és a dir, d'un ResponseLabel)
	 * @return Text de la resposta
	 */
	public String getTextResponseLabel(String sIdentResp, String sIdentRespLabel, int iPosition);
		
	/**
	 * @param sIdentResp identificador del conjunt de respostes (com per exemple del ResponseLID o del ResponseSTR)
	 * @param sIdentRespLabel identificador d'una resposta concreta (és a dir, d'un ResponseLabel)
	 * @return URI del primer recurs de la resposta
	 */
	public String getResourceResponseLabel(String sIdentResp, String sIdentRespLabel);
	/**
	 * @param sIdentResp identificador del conjunt de respostes (com per exemple del ResponseLID o del ResponseSTR)
	 * @param sIdentRespLabel identificador d'una resposta concreta (és a dir, d'un ResponseLabel)
	 * @return true si la resposta es correcta; false en cas contrari
	 */
	public boolean isCorrectResponseLabel(String sIdentResp, String sIdentRespLabel);

	/**
	 * @return identificador del response label actual
	 */
	public String getCurrentIdentResponseLabel();
	
}
