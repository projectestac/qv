/*
 * QVHotspotListControl.java
 * 
 * Created on 22/novembre/2004
 */
package edu.xtec.qv.editor.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.QTISuperRender;
import edu.xtec.qv.qti.QTISuperResponse;
import edu.xtec.qv.qti.ResponseLabel;
import edu.xtec.qv.qti.Varsubset;

/**
 * @author sarjona
 */
public class QVHotspotListControl extends QVRareaListControl{

	public static final String P_CORRECT_RESPONSE = "p_rarea_is_correct_response";

	public QVHotspotListControl(HttpServletRequest request){
		super(request);
	}

	protected void addResponseLabel(Item oItem, QTISuperRender oRender, ResponseLabel oNew){
		oRender.addResponseLabel(oNew);
		String sType = QTIUtil.getHotspotType(oItem);
		if (!QVConstants.DOT_HOTSPOT_TYPE.equals(sType)){
			updateConditions(oItem, oNew.getIdent());
		}
	}

	protected void setResponseLabel(Item oItem, QTISuperResponse oResponse, QTISuperRender oRender, String sOldIdent, ResponseLabel oNew){
		String sType = QTIUtil.getHotspotType(oItem);
		if (!QVConstants.DOT_HOTSPOT_TYPE.equals(sType)){
			updateConditions(oItem, oNew.getIdent());
		}else if (oNew!=null && sOldIdent!=null && !sOldIdent.equals(oNew.getIdent())){
			Vector vConditions = new Vector();
			Vector vResp = getVarequalStrings(oItem, oResponse, null);
			Vector vOK = (Vector)vResp.elementAt(0);
			if (!vOK.isEmpty()){
				boolean bUpdate = false;
				for(int i=0;i<vOK.size();i++){
					String sOK = (String)vOK.elementAt(i);
					if (sOK.indexOf(sOldIdent)>=0){
						sOK = sOK.replaceAll(sOldIdent, oNew.getIdent());
						vOK.setElementAt(sOK, i);
						bUpdate=true;
					}					
				}
				if (bUpdate){
					Hashtable hAnd = new Hashtable();
					vConditions.addElement(hAnd);
					Vector vAnd = new Vector();
					hAnd.put(QTIObject.AND.getAttributeName(), vAnd);
					Hashtable hConditions = new Hashtable();
					hConditions.put(oResponse.getIdent(), vOK);
					vAnd.addElement(hConditions);			
					updateConditions(oItem, vConditions);
				}
			}
			
		}
		oRender.setResponseLabel(sOldIdent, oNew);
	}

	protected void delConditions(Item oItem, String sRespident, String sIdent){
	}

	protected void updateConditions(Item oItem, String sIdent){
		String sType = QTIUtil.getHotspotType(oItem);
		QTISuperResponse oResponse = QTIUtil.getResponse(oItem);
		if (oResponse!=null){
			Vector vConditions = new Vector();
			if (QVConstants.ZONE_HOTSPOT_TYPE.equals(sType) || QVConstants.OPTION_HOTSPOT_TYPE.equals(sType)){				
				vConditions = createVarequals(oItem, sIdent);
			} else if (QVConstants.DOT_HOTSPOT_TYPE.equals(sType)){
				Vector vResp = getVarequalStrings(oItem, oResponse, sIdent);
				Vector vOK = (Vector)vResp.elementAt(0);
				
				Hashtable hAnd = new Hashtable();
				vConditions.addElement(hAnd);
				Vector vAnd = new Vector();
				hAnd.put(QTIObject.AND.getAttributeName(), vAnd);
				Hashtable hConditions = new Hashtable();
				hConditions.put(oResponse.getIdent(), vOK);
				vAnd.addElement(hConditions);
			} else if (QVConstants.FREE_HOTSPOT_TYPE.equals(sType)){
				Vector vVarsubsets = createVarsubsets(oItem);
				vConditions = createConditions(oResponse.getIdent(), vVarsubsets);
			}			
			updateConditions(oItem, vConditions);
		}
	}
	
	/**
	 * 
	 * @param oItem
	 * @param sIdent
	 * @return Vector amb les condicions actualitzades de la pregunta actual
	 */
	protected Vector createVarequals(Item oItem, String sIdent){
		Vector v = new Vector();
		QTISuperResponse oResponse = QTIUtil.getResponse(oItem);
		if (oResponse!=null){
			Vector vResp = getVarequalStrings(oItem, oResponse, sIdent);
			Vector vOK = (Vector)vResp.elementAt(0);
			Vector vKO = (Vector)vResp.elementAt(1);

			Hashtable hAnd = new Hashtable();
			v.addElement(hAnd);
			Vector vAnd = new Vector();
			hAnd.put(QTIObject.AND.getAttributeName(), vAnd);
			Hashtable hConditions = new Hashtable();
			hConditions.put(oResponse.getIdent(), vOK);
			vAnd.addElement(hConditions);

			hConditions = new Hashtable();
			hConditions.put(oResponse.getIdent(), vKO);
			Hashtable hNot = new Hashtable();
			hNot.put(QTIObject.NOT.getAttributeName(), hConditions);
			vAnd.addElement(hNot);
		}
		return v;
	}
	
	/**
	 * 
	 * @return Vector de dos elements: el primer conté un Vector amb les respostes correctes i el segon un amb les incorrectes
	 */
	protected Vector getVarequalStrings(Item oItem, QTISuperResponse oResponse, String sIdent){
		Vector v = new Vector();
		String sOK = "";
		String sKO = "";
		boolean bIsCorrect = request.getParameter(P_CORRECT_RESPONSE)!=null;
		Vector vOK = QTIUtil.getOKResponses(oItem, oResponse.getIdent());
		Vector vRareas = getAllRareas(oItem);
		Enumeration enumRareas = vRareas.elements();
		while (enumRareas.hasMoreElements()){
			String sRarea = (String)enumRareas.nextElement();
			if (sRarea.equals(sIdent)){
				if (isCorrect(vOK, sIdent)){
					if (!bIsCorrect){
						if ("".equals(sKO)) sKO+=sRarea;
						else sKO +=","+sRarea;							
					}else{
						if ("".equals(sOK)) sOK+=sRarea;
						else sOK +=","+sRarea;
					}
				}else if (!vRareas.contains(sRarea) && bIsCorrect){
					if ("".equals(sOK)) sOK+=sRarea;
					else sOK +=","+sRarea;
				} else{
					if (bIsCorrect){
						if ("".equals(sOK)) sOK+=sRarea;
						else sOK +=","+sRarea;
					}else{
						if ("".equals(sKO)) sKO+=sRarea;
						else sKO +=","+sRarea;							
					}
				}
			}else{
				if (isCorrect(vOK, sRarea)){
					if ("".equals(sOK)) sOK+=sRarea;
					else sOK +=","+sRarea;
				}else {
					if ("".equals(sKO)) sKO+=sRarea;
					else sKO +=","+sRarea;
				}
			}
		}
		Vector v1 = new Vector();
		Vector v2 = new Vector();
		v.addElement(v1);
		v.addElement(v2);
		v1.addElement(sOK);
		v2.addElement(sKO);
		//logger.debug("ok="+sOK+" ko="+sKO);
		return v;
	}
	
	protected boolean isCorrect(Vector vOK, String sIdent){
		boolean bIsCorrect = false;
		bIsCorrect = vOK.contains(sIdent);
		if (!bIsCorrect && vOK.size()==1){
			StringTokenizer st = new StringTokenizer((String)vOK.firstElement(),",");
			while (st.hasMoreTokens() && !bIsCorrect){
				String s = st.nextToken();
				bIsCorrect = s.equals(sIdent);
			}
		}
		return bIsCorrect;
	}

	protected Vector createVarsubsets(QTIObject o){
		Vector vVarsubset = new Vector();
		QTISuperResponse oResponse = null;
		if (o instanceof Item){
			oResponse = QTIUtil.getResponse((Item)o);
		}else if (o instanceof QTISuperResponse){
			oResponse = (QTISuperResponse)o;
		}
		if (oResponse!=null){
			Vector vRareas = getAllRareas(o);
			Enumeration enumRareas = vRareas.elements();
			while (enumRareas.hasMoreElements()){
				String sRarea = (String)enumRareas.nextElement();
				Varsubset oVar = new Varsubset(oResponse.getIdent(), null, sRarea, Varsubset.PARTIAL);
				vVarsubset.addElement(oVar);
			}
		}
		return vVarsubset;
	}
	
	public static void updateConditions (HttpServletRequest request, Item oItem){
		(new QVHotspotListControl(request)).updateConditions(oItem, "");
	}
	
}
