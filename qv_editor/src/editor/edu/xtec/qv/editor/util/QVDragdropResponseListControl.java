/*
 * QVDragdropResponseListControl.java
 * 
 * Created on 07/febrer/2005
 */
package edu.xtec.qv.editor.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import edu.xtec.qv.qti.And;
import edu.xtec.qv.qti.Conditionvar;
import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.Or;
import edu.xtec.qv.qti.QTIBooleanOperator;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.Respcondition;
import edu.xtec.qv.qti.Varequal;
import edu.xtec.qv.qti.Varsubset;

/**
 * @author sarjona
 */
public class QVDragdropResponseListControl extends QVListControl {

	public static final String P_IDENT_RESPONSE = "ident_resposta";
	public static final String P_IDENT_TARGETS = "p_ident_targets";

	public QVDragdropResponseListControl(HttpServletRequest request){
		super(request);
	}


	protected ListObject getListObject(And o, int index){
		ListObject lo = null;
		if (o!=null){
			StringBuffer sb = new StringBuffer();
			Enumeration enumVarsubsets = o.getVarsubset().elements();
			while (enumVarsubsets.hasMoreElements()){
				Varsubset tmp = (Varsubset)enumVarsubsets.nextElement();
				if (!sb.toString().equals("")){
					sb.append(";");
				}
				sb.append("["+tmp.getText()+"]");
			}
			lo = new ListObject(String.valueOf(index), sb.toString());
		}
		return lo;		
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getListObjects(edu.xtec.qv.qti.QTIObject)
	 */
	public Vector getListObjects(QTIObject oQTI) {
		Vector vListObjects = new Vector();
		Vector vObjects = getObjects(oQTI);
		int index = 0;
		Enumeration enumObjects = vObjects.elements();
		while (enumObjects.hasMoreElements()){
			And o = (And)enumObjects.nextElement();
			ListObject lo = getListObject(o, index);
			vListObjects.addElement(lo);
			index++;
		}
		return vListObjects;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getObject(QTIObject oQTI, String sIdent) {
		And o = null;
		try{
			if (sIdent!=null){
				Vector vObjects = getObjects(oQTI);
				int iIndex = Integer.parseInt(sIdent);
				if (iIndex>=0 && iIndex < vObjects.size()){
					o = (And)vObjects.elementAt(iIndex);
				}
			}
		}catch (Exception e){
			logger.error("EXCEPTION obtenint respostes dragdrop -> "+e);
		}
		return o;


	}

	private static Vector getObjects(QTIObject oQTI){
		Vector vObjects = new Vector();
		if (oQTI instanceof Item){
			Conditionvar oCondVar = getConditionvar(oQTI);
			if (oCondVar!=null){
				if (!oCondVar.getBooleanOperators().isEmpty()){
					QTIBooleanOperator oBool = (QTIBooleanOperator)oCondVar.getBooleanOperators().firstElement();
					vObjects = getObjects(oBool);
				}else{
					And oAnd = new And();
					oAnd.addConditions(oCondVar.getVarsubset());
					vObjects.addElement(oAnd);
				}
			}
		}else if (oQTI instanceof And){
			vObjects.add((QTIBooleanOperator)oQTI);
		}else if (oQTI instanceof Or){
			Enumeration enumBools = ((QTIBooleanOperator)oQTI).getBooleanOperators().elements();
			while (enumBools.hasMoreElements()){
				QTIBooleanOperator o = (QTIBooleanOperator)enumBools.nextElement();
				vObjects.addAll(getObjects(o));	
			}
		}
		return vObjects;
	}
	

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getPreviousObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getPreviousObject(QTIObject oQTI, String sIdent) {
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getNextObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getNextObject(QTIObject oQTI, String sIdent) {
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#addListObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public void addListObject(QTIObject oQTI, String sIdent) {
		And oNew = (And)getRequestQTIObject();
		if (oQTI!=null && oNew!=null){
			try{
				Conditionvar oCondVar = getConditionvar(oQTI);
				if (oCondVar==null){
					oCondVar = new Conditionvar();
					((Item)oQTI).addResponseCondition(QTIUtil.getDefaultOutcomes(), oCondVar);
				}
				if (!oCondVar.getBooleanOperators().isEmpty()){
					QTIBooleanOperator oBool = (QTIBooleanOperator)oCondVar.getBooleanOperators().firstElement();
					if (oBool instanceof And){
						Or oOr = new Or();
						oOr.addCondition(oBool);
						oOr.addCondition(oNew);
						oCondVar.setCondition(oBool, oOr);
					}else if (oBool instanceof Or){
						oBool.addCondition(oNew);
					}						
				}else{
					oCondVar.addCondition(oNew);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#delListObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public void delListObject(QTIObject oQTI, String sIdent) {
		if (oQTI!=null && sIdent!=null){
			Conditionvar oCondVar = getConditionvar(oQTI);
			if (oCondVar!=null){
				int iIndex = -1;
				try{
					iIndex = Integer.parseInt(sIdent);
				}catch (Exception e){
				}
				if (!oCondVar.getBooleanOperators().isEmpty()){
					QTIBooleanOperator oBool = (QTIBooleanOperator)oCondVar.getBooleanOperators().firstElement();
					if (oBool instanceof And){
						oCondVar.delCondition(iIndex);
					}else if (oBool instanceof Or){
						oBool.delCondition(iIndex);
					}						
				}else{
					oCondVar.delCondition(iIndex);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#setListObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	protected void setListObject(QTIObject oQTI, String sIdent) {
		And oNew = (And)getRequestQTIObject();
		if (oQTI!=null && oNew!=null && sIdent!=null){
			try{
				Conditionvar oCondVar = getConditionvar(oQTI);
				int iIndex = Integer.parseInt(sIdent);
				if (oCondVar!=null){
					if (!oCondVar.getBooleanOperators().isEmpty()){
						QTIBooleanOperator oBool = (QTIBooleanOperator)oCondVar.getBooleanOperators().firstElement();
						if (oBool instanceof And){
							oCondVar.setCondition(oBool, oNew);
						}else if (oBool instanceof Or){
							QTIObject oOld = (QTIObject)oBool.getContents().elementAt(iIndex);
							oBool.setCondition(oOld, oNew);
						}						
					}else{
						oCondVar.setCondition(oNew, iIndex);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	protected void addVarequal(Item oItem, Varequal oNew, String sOldText){
		if (oNew!=null && oNew.getText()!=null && oNew.getText().trim().length()>0){
			Conditionvar oCond = getConditionvar(oItem);
			if (oCond!=null){
				if (sOldText!=null){
					Varequal oVar = oCond.getVarequal(oNew.getRespident(), sOldText);
					if (oVar!=null){
						oVar.setText(oNew.getText());
					}
				}else{
					QTIBooleanOperator oOr = null;
					Vector vBooleanOp = oCond.getBooleanOperators();
					if (vBooleanOp!=null && !vBooleanOp.isEmpty()){
						Enumeration enumOp = vBooleanOp.elements();
						while (enumOp.hasMoreElements()){
							QTIBooleanOperator tmp = (QTIBooleanOperator)enumOp.nextElement();
							if (tmp instanceof Or){
								oOr = tmp;
							}
						}
					}else{
						oOr = new Or();
						oCond.addCondition(oOr);
					}
					oOr.addCondition(oNew);
				}
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

	private static Conditionvar getConditionvar(QTIObject oQTI){
		Conditionvar oCondVar = null;
		if (oQTI instanceof Item){
			Respcondition oRespCond = ((Item)oQTI).getRespcondition("CORRECT","True");
			if (oRespCond!=null){
				oCondVar = oRespCond.getConditionvar();
			}
		}
		return oCondVar;
	}
	
	public static Hashtable getSources(And oAnd, Vector vTargets) {
		Hashtable h = new Hashtable();
		if (vTargets!=null){
			Enumeration enumTargets = vTargets.elements();
			while (enumTargets.hasMoreElements()){
				ListObject oTarget = (ListObject)enumTargets.nextElement();
				Vector vSources = getSources(oAnd, oTarget.getIdent());
				StringBuffer sb = new StringBuffer();
				for (int i=0;i<vSources.size();i++){
					if (sb.toString().equals("")) sb.append(vSources.elementAt(i));
					else sb.append(","+vSources.elementAt(i));
				}
				h.put(oTarget.getIdent(), sb.toString());
			}
		}
		return h;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Vector getSources(And oAnd, String sTarget) {
		Vector v = new Vector();
		if (oAnd != null) {			
			Enumeration enumVar = oAnd.getVarsubset().elements();
			while (enumVar.hasMoreElements()) {
				Varsubset oVar = (Varsubset) enumVar.nextElement();
				Vector vSubsets = oVar.getSubsets();
				if (vSubsets.contains(sTarget)) {
					Enumeration enumSubsets = vSubsets.elements();
					while (enumSubsets.hasMoreElements()) {
						String s = (String) enumSubsets.nextElement();
						if (!v.contains(s) && !s.equals(sTarget)) {
							v.addElement(s);
						}
					}
				}
			}
		}
		return v;
	}

	

	public QTIObject getRequestQTIObject(){
		return getRequestQTIObject(null);
	}
	
	public QTIObject getRequestQTIObject(String sText){
		And o = null;
		String sRespident = getParameter(request, P_IDENT_RESPONSE, null);
		if (sRespident!=null){
			String[] vTargets = request.getParameterValues(P_IDENT_TARGETS);
			if (vTargets!=null){
				o = new And();
				for (int i=0;i<vTargets.length;i++){
					String sTarget = vTargets[i];
					String sSources = getParameter(request, "p_sources_"+sTarget, "");
					//logger.debug("target="+sTarget+"  sources="+sSources);
					StringTokenizer st = new StringTokenizer(sSources, ",");
					while(st.hasMoreTokens()){
						String sSource = st.nextToken();
						Varsubset vs = new Varsubset(sRespident, null, sSource+","+sTarget, Varsubset.EXACT);
						o.addCondition(vs);
					}
				}
			}
		}
		return o;
	}
	


}
