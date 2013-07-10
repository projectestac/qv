/*
 * QVDragdropPositionListControl.java
 * 
 * Created on 07/abril/2006
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
import edu.xtec.qv.qti.Material;
import edu.xtec.qv.qti.Matimage;
import edu.xtec.qv.qti.Mattext;
import edu.xtec.qv.qti.Or;
import edu.xtec.qv.qti.QTIBooleanOperator;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.QTISuperMat;
import edu.xtec.qv.qti.QTISuperRender;
import edu.xtec.qv.qti.QTISuperResponse;
import edu.xtec.qv.qti.Respcondition;
import edu.xtec.qv.qti.ResponseGRP;
import edu.xtec.qv.qti.ResponseLabel;
import edu.xtec.qv.qti.Resprocessing;
import edu.xtec.qv.qti.Varequal;
import edu.xtec.qv.qti.Varsubset;
import edu.xtec.qv.qti.util.StringUtil;

/**
 * @author sarjona
 */
public class QVDragdropPositionListControl extends QVListControl {

	public static final String P_IDENT_RESPONSE = "ident_resposta";
	public static final String P_IDENT_TARGETS = "p_ident_targets";
	public static final String P_POSITIONS = "p_positions";

	public QVDragdropPositionListControl(HttpServletRequest request){
		super(request);
	}


	protected ListObject getListObject(QTIObject o, int index){
		ListObject lo = null;
		if (o!=null){
			if (index==0) lo = new ListObject(String.valueOf(index), "initial");
			else lo = new ListObject(String.valueOf(index), "solution");
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
			QTIObject o = (QTIObject)enumObjects.nextElement();
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
		QTIObject o = null;
		if (sIdent!=null){
			try{
				int iIndex=Integer.parseInt(sIdent);
				o = (QTIObject)getObjects(oQTI).elementAt(iIndex);
			}catch (Exception e){e.printStackTrace();}
		}
		return o;
	}

	/* 
	 * Obté tots els response_labels del primer response
	 */
	public Vector getObjects(QTIObject oQTI) {
		Vector vObjects = new Vector();
		vObjects.add(getRender(oQTI));
		vObjects.add(oQTI);
		return vObjects;
	}

	/* 
	 * Obté el Render del primer response
	 */
	public QTISuperRender getRender(QTIObject oQTI) {
		QTISuperRender oRender = null;
		if (oQTI instanceof QTISuperResponse){
			oRender = ((QTISuperResponse)oQTI).getRender();
		}else if (oQTI instanceof Item){
			oRender = getRender(getItemResponse((Item)oQTI));
		}
		return oRender;
	}
	
	/* 
	 * Obté tots els response_labels del primer response
	 */
	public Vector getSourceResponseLabel(QTIObject oQTI) {
		Vector vObjects = new Vector();
		if (oQTI instanceof QTISuperResponse){
			QTISuperRender oRender= ((QTISuperResponse)oQTI).getRender();
			if (oRender!=null){
				Enumeration enumO = oRender.getResponseLabel().elements();
				while (enumO.hasMoreElements()) {
					ResponseLabel o = (ResponseLabel) enumO.nextElement();
					if (o.getRarea() == null || o.getRarea().length() <= 0) {
						vObjects.addElement(o);
					}
				}

			}
		}else if (oQTI instanceof Item){
			vObjects = getSourceResponseLabel(getItemResponse((Item)oQTI));
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
		//logger.debug("addListObject");
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
		//logger.debug("setListObject-> ident="+sIdent);
		QTISuperRender oRender = getRender(oQTI);
		String sPositions = getParameter(request, P_POSITIONS, null);
		//logger.debug("setListObject-> positions="+sPositions+" null?"+(sPositions!=null)+"   index="+getIntParameter(request, "dragdrop_position_list", -1));
		if (sPositions!=null){
			int iPositionId = getIntParameter(request, "dragdrop_position_list", -1);
			And oAnd=null;
			if (iPositionId>0){				
				oAnd = new And();
				Conditionvar oCondVar = getConditionvar(oQTI);
				if (oCondVar!=null){
					if (oCondVar.getBooleanOperators()!=null){
						Enumeration enumBool = oCondVar.getBooleanOperators().elements();
						int i=0;
						while (enumBool.hasMoreElements()){
							QTIBooleanOperator oBool = (QTIBooleanOperator)enumBool.nextElement();
							if (oBool instanceof And){
								i++;
								if (String.valueOf(i).equals(sIdent)){
									oAnd = (And)oBool;
									break;
								}
							}
						}
						// Remove targets
						Enumeration enumTargets = getTargets(oQTI).elements();
						while (enumTargets.hasMoreElements()){
							ResponseLabel oTarget = (ResponseLabel)enumTargets.nextElement();
							Enumeration enumAnd = oAnd.getVarsubset().elements();
							while (enumAnd.hasMoreElements()){
								Varsubset oVar = (Varsubset)enumAnd.nextElement();
								if (oVar.getText().indexOf(oTarget.getIdent())>0){
									getItemResponse((Item)oQTI).getRender().delResponseLabel(oTarget);
								}
							}
						}
						
						// Remove conditions
						oAnd.delAllConditions();
						
					//if (oCondVar.getBooleanOperators()!=null && !oCondVar.getBooleanOperators().isEmpty() && 
					//	oCondVar.getBooleanOperators().firstElement() instanceof And && ((And)oCondVar.getBooleanOperators().firstElement()).getContents().isEmpty()){
					//		oAnd = (And)oCondVar.getBooleanOperators().firstElement();
					}else{
						oCondVar.addCondition(oAnd);
					}
				}else{
					oCondVar = new Conditionvar();
				}
			}
	
			//1144413850546375_figura1.gif:50:200:0:1.0,1144413855406786:317:192:0:1.0,1144413940625998_figura3.gif:50:50:0:1.0,1144413943578634_figura4.gif:50:50:0:1.0
			int iFitTarget = getIntParameter(request, "p_fit_target", 0);
			
			StringTokenizer stSources = new StringTokenizer(sPositions, ",");
			while (stSources.hasMoreTokens()){
				String sSource = stSources.nextToken();
				StringTokenizer stParams = new StringTokenizer(sSource, ":");
				try{
					String sSourceIdent = stParams.nextToken();
					int iX0 = -1;
					try{
						String sX = stParams.nextToken();
						iX0 = Integer.parseInt(sX);
						if (iX0>iFitTarget) iX0-=iFitTarget;
					}catch (Exception e){}
					int iY0 = -1;
					try{
						String sY = stParams.nextToken();
						iY0 = Integer.parseInt(sY);
						if (iY0>iFitTarget) iY0-=iFitTarget;
					}catch (Exception e){}
					String sRotate = stParams.nextToken();
					String sRatio = stParams.nextToken();
					String sTargetIdent = null;
					if (stParams.hasMoreTokens()){
						sTargetIdent = stParams.nextToken();
					}
					ResponseLabel oSourceRespLabel = oRender.getResponseLabel(sSourceIdent);
					QTISuperMat oSourceMat = null;
					if (oSourceRespLabel!=null){
						Material oMaterial = (Material)oSourceRespLabel.getMaterials().firstElement();
						if (oMaterial.getMats()!=null && !oMaterial.getMats().isEmpty()){
							oSourceMat = (QTISuperMat)oMaterial.getMats().firstElement();
						}
/*						if (!oMaterial.getMatimage().isEmpty()){
							oSourceMat = (Matimage)oMaterial.getMatimage().firstElement();
						}*/
					}
					if (iPositionId==0){
						// Set x0,y0 source
						oSourceMat.setX0(iX0);
						oSourceMat.setY0(iY0);
					}else{
						// create target and conditionvar
						ResponseLabel oTargetRespLabel = oRender.getResponseLabel(sTargetIdent);
						if (oTargetRespLabel==null){
							String sText = (oSourceMat!=null?(!"".equals(oSourceMat.getText())?(oSourceMat.getText().length()>6?oSourceMat.getText().substring(0,6):oSourceMat.getText()):oSourceMat.getURI()):"");
							sText = sText.trim().replace(' ', '_');
							sText = StringUtil.filter(sText).toLowerCase();
							sTargetIdent="T_"+QTIUtil.getRandomIdent()+"_"+sText;
							oTargetRespLabel = new ResponseLabel(sTargetIdent);
							oTargetRespLabel.setRarea(ResponseLabel.RAREA_RECTANGLE);
							if (!"0".equals(sRotate)) oTargetRespLabel.setRotate(sRotate);
							oRender.addResponseLabel(oTargetRespLabel);
						}
						int iWidth = oSourceMat!=null?(oSourceMat instanceof Mattext?oSourceMat.getWidth()+20:oSourceMat.getWidth()):-1;
						if (iWidth>=0) iWidth+=iFitTarget*2;
						int iHeight = oSourceMat!=null?(oSourceMat instanceof Mattext?oSourceMat.getHeight()+5:oSourceMat.getHeight()):-1;
						if (iHeight>=0) iHeight+=iFitTarget*2;
						oTargetRespLabel.setText(iX0+","+iY0+","+iWidth+","+iHeight);
						// Create conditionvar
						String sVarText = sSourceIdent+","+sTargetIdent;
						Varsubset oVar = new Varsubset(getItemResponse((Item)oQTI).getIdent(), null, sVarText, Varsubset.EXACT);
						oAnd.addCondition(oVar);
						if (!"0".equals(sRotate)){
							sVarText=sSourceIdent+",rotate="+sRotate;
							oVar = new Varsubset(getItemResponse((Item)oQTI).getIdent(), null, sVarText, Varsubset.EXACT);
							oAnd.addCondition(oVar);
						}
						if (!"1.0".equals(sRatio)){
							sVarText=sSourceIdent+",ratio="+sRatio;
							oVar = new Varsubset(getItemResponse((Item)oQTI).getIdent(), null, sVarText, Varsubset.EXACT);
							oAnd.addCondition(oVar);
						}
					}
				}catch (Exception e){e.printStackTrace();}
			}
		}
		
		
		/*And oNew = (And)getRequestQTIObject();
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
		}*/
	}
	
	/* 
	 * Obté els response_labels amb rarea del primer response
	 */
	protected Vector getTargets(QTIObject oQTI) {
		Vector vObjects = new Vector();
		Vector vResponseLabel = new Vector();
		if (oQTI instanceof QTISuperResponse){
			QTISuperRender oRender= ((QTISuperResponse)oQTI).getRender();
			if (oRender!=null){
				vResponseLabel = oRender.getResponseLabel();
			}
		}else if (oQTI instanceof Item){
			vResponseLabel = getTargets(getItemResponse((Item)oQTI));
		}
		
		Enumeration enumO = vResponseLabel.elements();
		while (enumO.hasMoreElements()){
			ResponseLabel o = (ResponseLabel)enumO.nextElement();
			if (o.getRarea()!=null && o.getRarea().length()>0){
				vObjects.addElement(o);
			}
		}
		return vObjects;
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
			Vector vResprocessing = ((Item)oQTI).getResprocessing();
			Resprocessing oResprocessing = null;
			if (vResprocessing==null || vResprocessing.isEmpty()){
				oResprocessing = new Resprocessing();
				oResprocessing.setOutcomes(QTIUtil.getDefaultOutcomes());
				((Item)oQTI).addResprocessing(oResprocessing);
			}else{
				oResprocessing = (Resprocessing)vResprocessing.firstElement();
			}
			Respcondition oRespCond = oResprocessing.getRespcondition("CORRECT","True");
			if (oRespCond==null){
				oRespCond=((Item)oQTI).addRespcondition("CORRECT","True");
			}
			oCondVar = oRespCond.getConditionvar();
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
		ResponseGRP o = null;
		String sRespident = getParameter(request, P_IDENT_RESPONSE, null);
		if (sRespident!=null){
			o = QTIUtil.createResponseGRPwithIMSRender(sRespident);
			String sPositions = getParameter(request, P_POSITIONS, null);
			//1144413850546375_figura1.gif:50:200:0:1.0,1144413855406786:317:192:0:1.0,1144413940625998_figura3.gif:50:50:0:1.0,1144413943578634_figura4.gif:50:50:0:1.0
			StringTokenizer stSources = new StringTokenizer(sPositions, ",");
			while (stSources.hasMoreTokens()){
				String sSource = stSources.nextToken();
				StringTokenizer stParams = new StringTokenizer(sSource, ":");
				try{
					String sIdent = stParams.nextToken();
					String sWidth = stParams.nextToken();
					String sHeight = stParams.nextToken();
					String sRotate = stParams.nextToken();
					String sRatio = stParams.nextToken();
					if (stParams.hasMoreTokens()){
						// has target
					}
					ResponseLabel oRespLabel = new ResponseLabel(sIdent);
					//Material oMaterial = Material.createWithMatimage("image/jpg", "", sWidth, sHeight);
				}catch (Exception e){}
			}
			
			logger.debug("o="+o);
			
			
			
/*			String[] vTargets = request.getParameterValues(P_IDENT_TARGETS);
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
*/			
		}
		return o;
	}
	


}
