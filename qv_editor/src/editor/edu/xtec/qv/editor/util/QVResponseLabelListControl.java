/*
 * QVResponseLabelListControl.java
 * 
 * Created on 15/novembre/2004
 */
package edu.xtec.qv.editor.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.QTISuperRender;
import edu.xtec.qv.qti.QTISuperResponse;
import edu.xtec.qv.qti.Respcondition;
import edu.xtec.qv.qti.ResponseLabel;
import edu.xtec.qv.qti.Varsubset;

/**
 * @author sarjona
 */
public abstract class QVResponseLabelListControl extends QVListControl{

	// Parameter
	public static final String P_FEEDBACK_OK = "feedback_ok";
	public static final String P_FEEDBACK_KO = "feedback_ko";
	public static final String P_PUNTUATION_OK = "puntuation_ok";
	public static final String P_PUNTUATION_KO = "puntuation_ko";


	public QVResponseLabelListControl(HttpServletRequest request){
		super(request);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getListObjects(edu.xtec.qv.qti.QTIObject)
	 */
	public Vector getListObjects(QTIObject oQTI) {
		Vector vListObjects = new Vector();
		Vector vObjects = getObjects(oQTI);
		Enumeration enumObjects = vObjects.elements();
		while (enumObjects.hasMoreElements()){
			ResponseLabel o = (ResponseLabel)enumObjects.nextElement();
			ListObject lo = getListObject(o);
			vListObjects.addElement(lo);
		}
		return vListObjects;
	}

	/**
	 * Obté el format de cada ListObject (els parametres que s'han de mostrar
	 * depenen del tipus de response_label)
	 * @param o
	 * @return
	 */
	protected abstract ListObject getListObject(ResponseLabel o);

	/* 
	 * Obté tots els response_labels del primer response
	 */
	public Vector getObjects(QTIObject oQTI) {
		Vector vObjects = new Vector();
		if (oQTI instanceof QTISuperResponse){
			QTISuperRender oRender= ((QTISuperResponse)oQTI).getRender();
			if (oRender!=null){
				vObjects = oRender.getResponseLabel();
			}
		}else if (oQTI instanceof Item){
			vObjects = getObjects(getItemResponse((Item)oQTI));
		}
		return vObjects;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getObject(QTIObject oQTI, String sIdent) {
		QTIObject o = null;
		if (sIdent!=null){
			Enumeration enumResponses = getObjects(oQTI).elements();
			while (enumResponses.hasMoreElements()){
				QTIObject tmp = (QTIObject)enumResponses.nextElement();
				if (sIdent.equalsIgnoreCase(tmp.getIdent())){
					o = tmp;
					break;
				}
			}
		}
		return o;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getPreviousObject(QTIObject oQTI, String sIdent) {
		QTIObject o = null;
		if (sIdent!=null){
			QTIObject tmpPrev = null;
			Enumeration enumResponses = getObjects(oQTI).elements();
			while (enumResponses.hasMoreElements()){
				QTIObject tmp = (QTIObject)enumResponses.nextElement();
				if (sIdent.equalsIgnoreCase(tmp.getIdent())){
					o = tmpPrev;
					break;
				}else{
					tmpPrev = tmp;
				}
			}
		}
		return o;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getNextObject(QTIObject oQTI, String sIdent) {
		QTIObject o = null;
		if (sIdent!=null){
			Enumeration enumResponses = getObjects(oQTI).elements();
			while (enumResponses.hasMoreElements()){
				QTIObject tmp = (QTIObject)enumResponses.nextElement();
				if (sIdent.equalsIgnoreCase(tmp.getIdent())){
					if (enumResponses.hasMoreElements()){
						o = (QTIObject)enumResponses.nextElement();
					}
					break;
				}
			}
		}
		return o;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#delListObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public void delListObject(QTIObject oQTI, String sIdent) {
		QTISuperResponse oResponse = getItemResponse((Item)oQTI);
		if (oResponse!=null && sIdent!=null){
			QTISuperRender oRender = oResponse.getRender();
			if (oRender!=null){
				delConditions((Item)oQTI, oResponse.getIdent(), sIdent);
				oRender.delResponseLabel(sIdent);
			}
		}
	}


	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#upListObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public void upListObject(QTIObject oQTI, String sIdent) {
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#downListObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public void downListObject(QTIObject oQTI, String sIdent) {
	}
	
	protected abstract void delConditions(Item oItem, String sRespident, String sIdent);


//	***************************	
//	* Conditions
//	***************************	
	protected Vector createConditions(String sIdent, Vector vConditions){
		Vector v = new Vector();
		Hashtable hAnd = new Hashtable();
		v.addElement(hAnd);
		Vector vAnd = new Vector();
		hAnd.put(QTIObject.AND.getAttributeName(), vAnd);
		Hashtable hConditions = new Hashtable();
		hConditions.put(sIdent, vConditions);
		vAnd.addElement(hConditions);
		return v;
	}


	protected Vector getDragDropConditions(String sIdent, Vector vSubsets){
		return createConditions(sIdent, vSubsets);
	}

	protected Vector getAllVarsubsets(Item oItem){
		Vector vVarsubset = new Vector();
		QTISuperResponse oResponse = getItemResponse(oItem);
		if (oResponse!=null){
			Hashtable hTargets = getTargets(oItem);
			Enumeration enumTargets = hTargets.keys();
			while (enumTargets.hasMoreElements()){
				String sTarget = (String)enumTargets.nextElement();
				Vector vSources = (Vector)hTargets.get(sTarget);
				Enumeration enumSources = vSources.elements();
				while (enumSources.hasMoreElements()){
					String sSource = (String)enumSources.nextElement();
					Varsubset oVar = new Varsubset(oResponse.getIdent(), null, sSource+","+sTarget, null);
					vVarsubset.addElement(oVar);
				}
			}
		}
		return vVarsubset;
	}

	protected void delDragDropConditions(Item oItem, String sRespident, String sIdent){
		Vector vSubsets = getAllVarsubsets(oItem);
		if (delVarsubsets(vSubsets, sIdent)){
			updateConditions(oItem, getDragDropConditions(sRespident, vSubsets));
		}		
	}

	protected boolean delVarsubsets(Vector vSubsets, String sIdent){
		boolean bDel = false;
		Enumeration enumSubsets = vSubsets.elements();
		while (enumSubsets.hasMoreElements()){
			Varsubset oVar = (Varsubset)enumSubsets.nextElement();
			if (oVar.getSubsets().contains(sIdent)){
				vSubsets.remove(oVar);
				bDel = true;
			}
		}
		return bDel;
	}

//	***************************	
//	* Targets
//	***************************	

	/**
	 * 
	 * @param oItem
	 * @param sIdent identificador del ResponseLabel
	 * @return
	 */
	public static Vector getTargets(Item oItem, String sIdent) {
		Vector vTargets = new Vector();
		if (oItem != null) {
			Respcondition oRespcondition = oItem.getRespcondition("CORRECT", "True");
			if (oRespcondition != null) {
				Vector vVar = oRespcondition.getConditionvar().getVarsubset();
				Enumeration enumVar = vVar.elements();
				while (enumVar.hasMoreElements()) {
					Varsubset oVar = (Varsubset) enumVar.nextElement();
					Vector vSubsets = oVar.getSubsets();
					if (vSubsets.contains(sIdent)) {
						Enumeration enumSubsets = vSubsets.elements();
						while (enumSubsets.hasMoreElements()) {
							String s = (String) enumSubsets.nextElement();
							if (!vTargets.contains(s) && !s.equals(sIdent)) {
								vTargets.addElement(s);
							}
						}
					}
				}
			}
		}
		return vTargets;
	}

	public static Hashtable getTargets(Item oItem) {
		Hashtable hTargets = new Hashtable();
		Enumeration enumTargets = getAllTargets(oItem).elements();
		while (enumTargets.hasMoreElements()) {
			String sTarget = (String) enumTargets.nextElement();
			hTargets.put(sTarget, getTargets(oItem, sTarget));
		}
		return hTargets;
	}

	/**
	 * @param oItem
	 * @return Vector amb els identificadors de tots els targets de la pregunta indicada 
	 */
	public static Vector getAllTargets(QTIObject o) {
		Vector vTargets = new Vector();
		Enumeration enumLO = QVTargetListControl.getTargetListObjects(o).elements();
		while (enumLO.hasMoreElements()) {
			ListObject lo = (ListObject) enumLO.nextElement();
			vTargets.addElement(lo.getIdent());
		}
		return vTargets;
	}

	/**
	 * @param oItem
	 * @return String amb els identificadors de tots els targets de la pregunta indicada separats per comes (',')
	 */
	public static String getAllTargetsString(QTIObject o) {
		String sTargets = "";
		Enumeration enumLO = QVTargetListControl.getTargetListObjects(o).elements();
		while (enumLO.hasMoreElements()) {
			ListObject lo = (ListObject) enumLO.nextElement();
			if (sTargets.length() > 0) {
				sTargets += "," + lo.getIdent();
			} else {
				sTargets += lo.getIdent();
			}
		}
		return sTargets;
	}

	public static QTISuperRender getItemRender(Item oItem){
		QTISuperRender oRender = null;
		QTISuperResponse oResponse = getItemResponse(oItem);
		if (oResponse!=null){
			oRender = oResponse.getRender();
		}
		return oRender;		
	}
	
	public QTIObject getRequestQTIObject(){
		return getRequestQTIObject(null);
	}
	
	/**
	 * 
	 * @param sIdent
	 * @return
	 */
	public abstract QTIObject getRequestQTIObject(String sIdent);

	protected boolean notNullParameter(String sParamName){
		return request.getParameter(sParamName)!=null && request.getParameter(sParamName).length()>0;
	}

	protected void updateConditions(Item oItem, Vector vConditions){
		String sFeedbackOK = request.getParameter(P_FEEDBACK_OK);
		String sPuntuationOK = request.getParameter(P_PUNTUATION_OK);
		QTIUtil.addCorrectResponseCondition(oItem, vConditions, sFeedbackOK, sPuntuationOK);
		
		String sFeedbackKO = request.getParameter(P_FEEDBACK_KO);
		String sPuntuationKO = request.getParameter(P_PUNTUATION_KO);
		QTIUtil.addIncorrectResponseCondition(oItem, vConditions, sFeedbackKO, sPuntuationKO);
	}
	
	
}
