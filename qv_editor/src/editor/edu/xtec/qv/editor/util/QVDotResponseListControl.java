/*
 * QVDotResponseListControl.java
 * 
 * Created on 24/gener/2005
 */
package edu.xtec.qv.editor.util;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import edu.xtec.qv.qti.Conditionvar;
import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.Or;
import edu.xtec.qv.qti.QTIBooleanOperator;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.Respcondition;
import edu.xtec.qv.qti.Varequal;

/**
 * @author sarjona
 */
public class QVDotResponseListControl extends QVListControl {

	public static final String P_DOT_RESPONSE_TEXT = "p_dot_response_text";
	public static final String P_IDENT_RESPONSE = "ident_resposta";

	public QVDotResponseListControl(HttpServletRequest request){
		super(request);
	}


	protected ListObject getListObject(Varequal o, int index){
		ListObject lo = null;
		if (o!=null){
			lo = new ListObject(o.getText(), o.getText());
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
			Varequal o = (Varequal)enumObjects.nextElement();
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
		Varequal o = null;
		try{
			if (sIdent!=null){
				Enumeration enum1 = getObjects(oQTI).elements();
				while (enum1.hasMoreElements()){
					Varequal tmp = (Varequal)enum1.nextElement();
					if (sIdent.equalsIgnoreCase(tmp.getText())){
						o = tmp;
						break;
					}
				}
			}
		}catch (Exception e){
			logger.error("EXCEPTION obtenint respostes correctes -> "+e);
		}
		return o;


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
		Varequal oNew = (Varequal)getRequestQTIObject();
		addVarequal((Item)oQTI, oNew, null);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#delListObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public void delListObject(QTIObject oQTI, String sIdent) {
		Varequal oDel = (Varequal)getRequestQTIObject();
		if (sIdent!=null && oDel!=null){
			Conditionvar oCond = getConditionvar(oQTI);
			if (oCond!=null){
				oCond.delCondition(oDel.getRespident(), sIdent);
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#setListObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	protected void setListObject(QTIObject oQTI, String sIdent) {
		Varequal oNew = (Varequal)getRequestQTIObject();
		addVarequal((Item)oQTI, oNew, sIdent);
	}
	
	protected void addVarequal(Item oItem, Varequal oNew, String sOldText){
		try{
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
					}
					if (oOr==null){
						oOr = new Or();
						oCond.addCondition(oOr);
					}
					oOr.addCondition(oNew);
				}
			}
		}
		} catch(Exception e){
			e.printStackTrace();
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

	private static Vector getObjects(QTIObject oQTI){
		Vector vObjects = new Vector();
		if (oQTI instanceof Item){
			Conditionvar oCondVar = getConditionvar(oQTI);
			if (oCondVar!=null){
				vObjects = oCondVar.getAllVarequals();
			}
		}
		return vObjects;
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

	public QTIObject getRequestQTIObject(){
		return getRequestQTIObject(null);
	}
	
	public QTIObject getRequestQTIObject(String sText){
		Varequal o = null;
		if (sText==null){
			sText = getParameter(request, P_DOT_RESPONSE_TEXT, null);
		}
		String sRespident = getParameter(request, P_IDENT_RESPONSE, null);
		if (sRespident!=null){
			o = new Varequal(sRespident, null, sText, null);
		}
		return o;
	}
	


}
