/*
 * QVRareaListControl.java
 * 
 * Created on 15/novembre/2004
 */
package edu.xtec.qv.editor.util;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.QTISuperRender;
import edu.xtec.qv.qti.QTISuperResponse;
import edu.xtec.qv.qti.ResponseLabel;
import edu.xtec.qv.qti.util.StringUtil;

/**
 * @author sarjona
 */
public abstract class QVRareaListControl extends QVResponseLabelListControl{

	// Parameter
	public static final String P_RAREA_TYPE = "p_rarea_type";
	public static final String P_RAREA_IDENT = "p_rarea_ident";
	public static final String P_RAREA_WIDTH = "p_rarea_width";
	public static final String P_RAREA_HEIGHT = "p_rarea_height";
	public static final String P_RAREA_X0 = "p_rarea_x0";
	public static final String P_RAREA_Y0 = "p_rarea_y0";
	public static final String P_RAREA_BOUNDED_LIST = "p_rarea_bounded_list";

	public QVRareaListControl(HttpServletRequest request){
		super(request);
	}

	/* 
	 * Obté els response_labels amb rarea del primer response
	 */
	public Vector getObjects(QTIObject oQTI) {
		Vector vObjects = new Vector();
		Enumeration enumO = super.getObjects(oQTI).elements();
		while (enumO.hasMoreElements()){
			ResponseLabel o = (ResponseLabel)enumO.nextElement();
			if (o.getRarea()!=null && o.getRarea().length()>0){
				vObjects.addElement(o);
			}
		}
		return vObjects;
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVResponseLabelListControl#getListObject(edu.xtec.qv.qti.ResponseLabel)
	 */
	protected ListObject getListObject(ResponseLabel o){
		ListObject lo = null;
		if (o!=null){
			String sLabel = "";
			if (o.getIdent()!=null && (!StringUtil.isNumber(o.getIdent()) || o.getIdent().length()<8)){
				sLabel = o.getIdent()+": "; 
			}
			lo = new ListObject(o.getIdent(), "["+o.getRarea()+"] "+sLabel+o.getText());
		}
		return lo;		
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#addListObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public void addListObject(QTIObject oQTI, String sIdent) {
		ResponseLabel oNew = (ResponseLabel)getRequestQTIObject(null);
		QTISuperResponse oResponse = getItemResponse((Item)oQTI);
		//logger.debug("addListObject-> new="+oNew+"  response="+oResponse);
		if (oResponse!=null && (notNullParameter(P_RAREA_BOUNDED_LIST) || 
		  (notNullParameter(P_RAREA_WIDTH) && notNullParameter(P_RAREA_HEIGHT) && 
		  notNullParameter(P_RAREA_X0)&& notNullParameter(P_RAREA_Y0)))){
			QTISuperRender oRender = oResponse.getRender();
			if (oRender!=null){
				addResponseLabel((Item)oQTI, oRender, oNew);
			}
		}
	}
	
	protected abstract void addResponseLabel(Item oItem, QTISuperRender oRender, ResponseLabel oNew);
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#setListObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	protected void setListObject(QTIObject oQTI, String sIdent) {
		ResponseLabel oNew = (ResponseLabel)getRequestQTIObject(null);
		QTISuperResponse oResponse = getItemResponse((Item)oQTI);
		if (oResponse!=null && (notNullParameter(P_RAREA_BOUNDED_LIST) || 
		  (notNullParameter(P_RAREA_WIDTH) && notNullParameter(P_RAREA_HEIGHT) && 
		  notNullParameter(P_RAREA_X0)&& notNullParameter(P_RAREA_Y0)))){
			QTISuperRender oRender = oResponse.getRender();
			if (oRender!=null){
				setResponseLabel((Item)oQTI, oResponse, oRender, sIdent, oNew);
			}
		}
	}

	protected abstract void setResponseLabel(Item oItem, QTISuperResponse oResponse, QTISuperRender oRender, String sOldIdent, ResponseLabel oNew);
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVResponseLabelListControl#getRequestQTIObject(java.lang.String)
	 */
	public QTIObject getRequestQTIObject(String sIdent){
		ResponseLabel o = null;
		String sRareaType = request.getParameter(P_RAREA_TYPE);
		if (sIdent==null){
			sIdent = getParameter(request, P_RAREA_IDENT, QTIUtil.getRandomIdent());
		}
		String sText = "";
		if (ResponseLabel.RAREA_BOUNDED.equals(sRareaType)){
			sText = getParameter(request, P_RAREA_BOUNDED_LIST, "");
		}else{
			String sWidth = getParameter(request, P_RAREA_WIDTH, "0");
			String sHeight = getParameter(request, P_RAREA_HEIGHT, "0");
			String sX0 = getParameter(request, P_RAREA_X0, "0");
			String sY0 = getParameter(request, P_RAREA_Y0, "0");
			sText= sX0+","+sY0+","+sWidth+","+sHeight;
		}
		o = new ResponseLabel(sIdent);
		o.setText(sText);
		o.setRarea(sRareaType);
		return o;
	}

	/**
	 * @param oItem
	 * @return Vector amb els identificadors de tots els rareas de la pregunta indicada 
	 */
	public Vector getAllRareas(QTIObject o) {
		Vector vRareas = new Vector();
		Enumeration enumLO = getListObjects(o).elements();
		while (enumLO.hasMoreElements()) {
			ListObject lo = (ListObject) enumLO.nextElement();
			vRareas.addElement(lo.getIdent());
		}
		return vRareas;
	}

	
}
