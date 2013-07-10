/*
 * QVSourceListControl.java
 * 
 * Created on 15/novembre/2004
 */
package edu.xtec.qv.editor.util;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.Material;
import edu.xtec.qv.qti.Matimage;
import edu.xtec.qv.qti.Mattext;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.QTISuperMat;
import edu.xtec.qv.qti.QTISuperRender;
import edu.xtec.qv.qti.QTISuperResponse;
import edu.xtec.qv.qti.ResponseLabel;
import edu.xtec.qv.qti.Varsubset;
import edu.xtec.qv.qti.util.StringUtil;

/**
 * @author sarjona
 */
public class QVSourceListControl extends QVResponseLabelListControl {

	// Parameter
	public static final String P_SOURCE_IDENT = "p_source_ident";
	public static final String P_SOURCE_URI = "p_source_uri";
	public static final String P_SOURCE_TEXT = "p_source_text";
	public static final String P_SOURCE_WIDTH = "p_source_width";
	public static final String P_SOURCE_HEIGHT = "p_source_height";
	public static final String P_SOURCE_X0 = "p_source_x0";
	public static final String P_SOURCE_Y0 = "p_source_y0";
	public static final String P_SOURCE_MATCH_MAX = "p_source_match_max";
	public static final String P_SOURCE_TARGETS = "p_source_targets";


	public QVSourceListControl(HttpServletRequest request) {
		super(request);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVResponseLabelListControl#getListObject(edu.xtec.qv.qti.ResponseLabel)
	 */
	protected ListObject getListObject(ResponseLabel o) {
		ListObject lo = null;
		if (o != null && !o.getMaterials().isEmpty()) {
			Material oMaterial = (Material) o.getMaterials().firstElement();
			String sImage="", sText="";
			Enumeration enumMats = oMaterial.getMats().elements();
			while (enumMats.hasMoreElements() || (sImage==null && sText==null) ){
				QTISuperMat oMat = (QTISuperMat)enumMats.nextElement();
				if (oMat instanceof Matimage) {
					sImage=oMat.getURI();
				}else if (oMat instanceof Mattext){
					sText=oMat.getText();
				}
			}
			
			StringBuffer sb = new StringBuffer();
			if (sImage!=null && sImage.trim().length()>0) sb.append(sImage);
			if (sText!=null && sText.trim().length()>0){
				if (sb.length()>0) sb.append("/");
				sb.append(StringUtil.htmlToText(sText));
			}
			String sValue = sb.toString();
			lo = new ListObject(o.getIdent(), sValue);
			
/*			if (!oMaterial.getMats().isEmpty()) {
				QTISuperMat oMat =
					(QTISuperMat) oMaterial.getMats().firstElement();
				String sValue = null;
				if (oMat instanceof Matimage) {
					sValue = "["+ oMat.getMatContentType() + "] " 
							+ oMat.getURI()+ ": "	
							+ ((Matimage) oMat).getX0()	+ ","+ ((Matimage) oMat).getY0() + ","
							+ ((Matimage) oMat).getWidth()+ ","	+ ((Matimage) oMat).getHeight();
				} else {
					sValue = "["+ oMat.getMatContentType()+ "] "
							+ StringUtil.htmlToText(oMat.getText());
				}
				lo = new ListObject(o.getIdent(), sValue);
			}
*/				
		}
		return lo;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#addListObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public void addListObject(QTIObject oQTI, String sIdent) {
		ResponseLabel oNew = (ResponseLabel) getRequestQTIObject(null);
		QTISuperResponse oResponse = getItemResponse((Item) oQTI);
		if (oResponse != null && ((notNullParameter(P_SOURCE_WIDTH) && notNullParameter(P_SOURCE_HEIGHT)) || notNullParameter(P_SOURCE_TEXT)) ){
/*			&& notNullParameter(P_SOURCE_WIDTH)
			&& notNullParameter(P_SOURCE_HEIGHT)
			&& notNullParameter(P_SOURCE_X0)
			&& notNullParameter(P_SOURCE_Y0)) {*/
			QTISuperRender oRender = oResponse.getRender();
			if (oRender != null) {
				oNew.setMatchgroup(getAllTargetsString(oResponse));
				oRender.addResponseLabel(oNew);
				addAndUpdateConditions((Item)oQTI, oResponse.getIdent(), oNew.getIdent());
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#setListObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	protected void setListObject(QTIObject oQTI, String sIdent) {
		ResponseLabel oNew = (ResponseLabel) getRequestQTIObject(sIdent);
		QTISuperResponse oResponse = getItemResponse((Item) oQTI);
		if (oResponse != null && notNullParameter(P_SOURCE_WIDTH) && notNullParameter(P_SOURCE_HEIGHT)){
			QTISuperRender oRender = oResponse.getRender();
			if (oRender != null) {
				oNew.setMatchgroup(getAllTargetsString(oResponse));
				ResponseLabel oOld = oRender.getResponseLabel(sIdent);
				QTISuperMat oOldMat = getFirstMat(oOld);
				QTISuperMat oNewMat = getFirstMat(oNew);
				if (oOldMat!=null && oNewMat!=null){
					// Set x0,y0 new responselabel
					oNewMat.setX0(oOldMat.getX0());
					oNewMat.setY0(oOldMat.getY0());
				}
				oRender.setResponseLabel(sIdent, oNew);
				//setAndUpdateConditions((Item)oQTI, oResponse.getIdent(), sIdent, oNew.getIdent());
			}
		}
	}
	
	protected QTISuperMat getFirstMat(ResponseLabel oRespLabel){
		QTISuperMat oMat = null;
		if (oRespLabel!=null){
			if (!oRespLabel.getMaterials().isEmpty()){
				Material oMaterial = (Material)oRespLabel.getMaterials().firstElement();
				if (!oMaterial.getMats().isEmpty()){
					oMat = (QTISuperMat)oMaterial.getMats().firstElement();
				}
			}
		}
		return oMat;
	}


//	***************************	
//	*  Conditions
//	***************************

	protected void addAndUpdateConditions(Item oItem, String sRespident, String sSource){
		Vector vSubsets = getAllVarsubsets(oItem);
		if (addVarsubsets(vSubsets, sRespident, sSource)){
			updateConditions(oItem, getDragDropConditions(sRespident, vSubsets));
		}
	}
	
	protected boolean addVarsubsets(Vector vSubsets, String sRespident, String sSource){
		boolean bAdd = false;
		String[] sTargets = request.getParameterValues(P_SOURCE_TARGETS);
		if (sTargets!=null){
			for (int i=0;i<sTargets.length;i++){
				Varsubset oVar = new Varsubset(sRespident, null, sSource+","+sTargets[i], null);
				vSubsets.addElement(oVar);						
			}
			bAdd = true;
		}
		return bAdd;
	}

	protected void setAndUpdateConditions(Item oItem, String sRespident, String sOldSource, String sNewSource){
		Vector vSubsets = getAllVarsubsets(oItem);
		boolean bSet = delVarsubsets(vSubsets, sOldSource);
		bSet |= addVarsubsets(vSubsets, sRespident, sNewSource);
		if (bSet){
			updateConditions(oItem, getDragDropConditions(sRespident, vSubsets));
		}
	}
	
	protected void delConditions(Item oItem, String sRespident, String sIdent){
		delDragDropConditions(oItem, sRespident, sIdent);
	}


//	***************************	
//	* ResponseLabelListControl
//	***************************

	/**
	 * @param o 
	 * @return Vector amb els ResponseLabel que són source
	 */
	public static Vector getSourceResponseLabel(QTIObject o){
		return (new QVSourceListControl(null)).getObjects(o);
	}

	public static Vector getSourceListObjects(QTIObject o){
		return (new QVSourceListControl(null)).getListObjects(o);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVResponseLabelListControl#getObjects(edu.xtec.qv.qti.QTIObject)
	 */
	public Vector getObjects(QTIObject oQTI) {
		Vector vObjects = new Vector();
		Enumeration enumO = super.getObjects(oQTI).elements();
		while (enumO.hasMoreElements()) {
			ResponseLabel o = (ResponseLabel) enumO.nextElement();
			if (o.getRarea() == null || o.getRarea().length() <= 0) {
				vObjects.addElement(o);
			}
		}
		return vObjects;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVResponseLabelListControl#getRequestQTIObject(java.lang.String)
	 */
	public QTIObject getRequestQTIObject(String sIdent) {
		ResponseLabel o = null;
		String sURI = getParameter(request, P_SOURCE_URI, null);
		String sText = getParameter(request, P_SOURCE_TEXT, null);
		if (sIdent == null) {
			String sDefaultIdent = QTIUtil.getRandomIdent()+(sURI!=null?"_"+sURI:"");
			sIdent = getParameter(request, P_SOURCE_IDENT, sDefaultIdent);
		}
		if (sURI!=null || sText!=null){
			o = new ResponseLabel(sIdent);
			Material oMaterial = new Material();
			o.addMaterial(oMaterial);
			int iWidth = getIntParameter(request, P_SOURCE_WIDTH, 0);
			int iHeight = getIntParameter(request, P_SOURCE_HEIGHT, 0);
			if (sURI != null) {
				int iX0 = getIntParameter(request, P_SOURCE_X0, 0);
				int iY0 = getIntParameter(request, P_SOURCE_Y0, 0);
				int iMatchMax = getIntParameter(request, P_SOURCE_MATCH_MAX, 1);
				o.setMatchmax(iMatchMax);
				//String[] sTargets = request.getParameterValues(P_SOURCE_TARGETS);
				Matimage oMat = new Matimage(null, sURI, iWidth, iHeight);
				oMaterial.addMaterial(oMat);
				oMat.setX0(iX0);
				oMat.setY0(iY0);
			}
			if (sText!=null){
				Mattext oMat = new Mattext(Mattext.HTML_TEXTTYPE, sText);
				int iTextLength= sText.length()*5;
				if (iWidth==0 || iWidth<iTextLength) iWidth=iTextLength*2;
				if (iHeight==0) iHeight=15;
				oMat.setWidth(iWidth);
				oMat.setHeight(iHeight);
				oMaterial.addMaterial(oMat);				
			}
			//logger.debug("getRequestQTIObject-> text="+sText+"  o="+o);
		}
		return o;
	}

}
