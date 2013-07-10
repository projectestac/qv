/*
 * QVPreguntaDragDropBean.java
 * 
 * Created on 14/maig/2004
 */
package edu.xtec.qv.editor.beans;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.print.attribute.HashAttributeSet;

import edu.xtec.qv.editor.util.QTIUtil;
import edu.xtec.qv.editor.util.QVResponseLabelListControl;
import edu.xtec.qv.qti.And;
import edu.xtec.qv.qti.Conditionvar;
import edu.xtec.qv.qti.Flow;
import edu.xtec.qv.qti.IMSRenderObject;
import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.Material;
import edu.xtec.qv.qti.Matimage;
import edu.xtec.qv.qti.Presentation;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.QTISuperRender;
import edu.xtec.qv.qti.Respcondition;
import edu.xtec.qv.qti.ResponseLabel;
import edu.xtec.qv.qti.Resprocessing;
import edu.xtec.qv.qti.Varsubset;



/**
 * @author sarjona
 */
public class QVPreguntaDragDropBean extends QVPreguntaBean{
	
	public static final String P_SHUFFLE = "p_shuffle";
	public static final String P_INSIDE = "p_inside";
	public static final String P_ALIGN = "p_align";
	public static final String P_SHOW_TARGET = "p_show_target";
	public static final String P_ENABLE_ROTATING = "p_enable_rotating";
	public static final String P_ENABLE_SCALING = "p_enable_scaling";
	public static final String P_NROTATE = "p_nrotate";
	public static final String P_NRATIO = "p_nratio";
	public static final String P_FIT_TARGET = "p_fit_target";
	public static final String P_STYLE_BORDER_COLOR = "p_style_border_color";

	public static final String ALIGN_NONE = "None";
	public static final String ALIGN_AUTO = "Auto";
	
	protected Hashtable hSources = new Hashtable();
	
	
	public QVPreguntaDragDropBean(QVBean mainBean){
		super(mainBean);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.QVBean#start()
	 */
	public boolean start() {
		boolean bOk = true;
		try{
			super.start();
		} catch (Exception e){
			logger.error("EXCEPCIO inicialitzant QVPreguntaDragDropBean --> "+e);
			e.printStackTrace();
			bOk = false;
		}
		return bOk;
	}
	
	protected Item getCurrentItem(){
		if (oItem==null){
			oItem = mainBean.getPregunta();
			if (oItem!=null){
				// Actualitzar pregunta
				String sTitle = getParameter(TITLE_PARAM);
				if (sTitle!=null){
					oItem.setTitle(sTitle);
				}
				String sEnunciat = getParameter(ENUNCIAT_PREGUNTA_PARAM);
				if (sEnunciat!=null){
					oItem.setStatement(sEnunciat);
				}
				String sInteractionswitch = getParameter(P_INTERACTIONSWITCH);
				if (sInteractionswitch!=null && sInteractionswitch.trim().length()>0){
					QTIUtil.setInteractionswitch(oItem, sInteractionswitch);
				}
				
				String sBackgroundImage = getParameter("p_background_image_uri");
				if (sBackgroundImage!=null){
					int iWidth = mainBean.getIntParameter("width", -1);
					int iHeight = mainBean.getIntParameter("height", -1);
					Item oItem = (Item)getBeanObject();
					Presentation oPresentation = oItem.getPresentation();
					if (oPresentation!=null){
						Flow oFlow = oPresentation.getFlow();
						if (oFlow!=null){
							Matimage oMat = null;
							Enumeration enumFlows = oFlow.getFlows().elements();
							while (enumFlows.hasMoreElements() && oMat==null){
								Flow o = (Flow)enumFlows.nextElement();
								oMat = QTIUtil.getMatimage(o);
							}
							if (oMat!=null){
								oMat.setURI(sBackgroundImage);
								oMat.setWidth(iWidth);
								oMat.setHeight(iHeight);
							}else{
								Material oMaterial = Material.createWithMatimage(null, sBackgroundImage, iWidth, iHeight);
								Flow oNew = new Flow();
								oNew.addMaterial(oMaterial);
								oFlow.addFlow(oNew, 1);
							}
						}
					}
					QTISuperRender oRender = QVResponseLabelListControl.getItemRender(oItem);
					if (oRender!=null && oRender instanceof IMSRenderObject){
						String sShuffle = getParameter(P_SHUFFLE)!=null?"Yes":"No";
						((IMSRenderObject)oRender).setShuffle(sShuffle);
						String sInside = getParameter(P_INSIDE)!=null?"Yes":"No";
						((IMSRenderObject)oRender).setInside(sInside);
						String sAlign = getParameter(P_ALIGN)!=null?ALIGN_AUTO:ALIGN_NONE;
						((IMSRenderObject)oRender).setAlign(sAlign);
						String sShowTarget = getParameter(P_SHOW_TARGET)!=null?"true":"false";
						((IMSRenderObject)oRender).setShowTargets(sShowTarget);
						String sEnableRotating = getParameter(P_ENABLE_ROTATING)!=null?"Yes":"No";
						((IMSRenderObject)oRender).setEnableRotating(sEnableRotating);
						String sEnableScaling = getParameter(P_ENABLE_SCALING)!=null?"Yes":"No";
						((IMSRenderObject)oRender).setEnableScaling(sEnableScaling);
						String sNRotate = getParameter(P_NROTATE)!=null?getParameter(P_NROTATE):"1";
						((IMSRenderObject)oRender).setNRotate(sNRotate);
						String sNRatio = getParameter(P_NRATIO)!=null?getParameter(P_NRATIO):"1";
						((IMSRenderObject)oRender).setNRatio(sNRatio);
						String sFitTarget = getParameter(P_FIT_TARGET)!=null?getParameter(P_FIT_TARGET):"1";
						((IMSRenderObject)oRender).setFitTarget(sFitTarget);

						setBorderColorStyle();
					}					
				}
				updateFeedback();
				updatePuntuation();
			}else{
				// Crear nova pregunta
				oItem = QTIUtil.createDragDropItem();
			}
			
			//-->Albert
			String sOrdreItem = getParameter(ITEM_ORDER_PARAM);
			if (sOrdreItem !=null){
				//System.out.println("l'estableixo a sOrdreItem:"+sOrdreItem+" idItem:"+oItem.getIdent());	
				QTIUtil.setItemSelectionOrdering(mainBean.getFull(), oItem, sOrdreItem);
			}
			//ordre_pregunta
			//<--
			
		}
		return oItem;
	}
	
	public String getBackgroundImage(){
		String sURI = null;
		Item oItem = (Item)getBeanObject();
		Presentation oPresentation = oItem.getPresentation();
		if (oPresentation!=null){
			Flow oFlow = oPresentation.getFlow();
			if (oFlow!=null && !oFlow.getFlows().isEmpty()){
				Matimage oMat = null;
				Enumeration enumFlows = oFlow.getFlows().elements();
				while (enumFlows.hasMoreElements() && oMat==null){
					Flow o = (Flow)enumFlows.nextElement();
					oMat = QTIUtil.getMatimage(o);
				}
				if (oMat!=null){
					sURI=oMat.getURI();
				}
			}
		}
		return sURI;
	}
	
	public Matimage getBackgroundMatimage(){
		Matimage oMat = null;
		Item oItem = (Item)getBeanObject();
		Presentation oPresentation = oItem.getPresentation();
		if (oPresentation!=null){
			Flow oFlow = oPresentation.getFlow();
			if (oFlow!=null && !oFlow.getFlows().isEmpty()){
				Enumeration enumFlows = oFlow.getFlows().elements();
				while (enumFlows.hasMoreElements() && oMat==null){
					Flow o = (Flow)enumFlows.nextElement();
					oMat = QTIUtil.getMatimage(o);
				}
			}
		}
		return oMat;
	}
	
	public Vector getSources() {
		Vector vObjects = new Vector();
		try{
			Enumeration enumO = getResponse().getRender().getResponseLabel().elements();
			while (enumO.hasMoreElements()) {
				ResponseLabel o = (ResponseLabel) enumO.nextElement();
				if (o.getRarea() == null || o.getRarea().length() <= 0) {
					vObjects.addElement(o);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return vObjects;
	}
		
	public ResponseLabel getTarget(String sTargetId) {
		ResponseLabel oRespLabel = null;
		try{
			if (sTargetId!=null){
				Enumeration enumTargets = getTargets().elements();
				while (enumTargets.hasMoreElements()){
					ResponseLabel o = (ResponseLabel) enumTargets.nextElement();
					if (sTargetId.equals(o.getIdent())){
						oRespLabel=o;
						break;
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return oRespLabel;
	}
	
	public Vector getTargets() {
		Vector vObjects = new Vector();
		try{
			Enumeration enumO = getResponse().getRender().getResponseLabel().elements();
			while (enumO.hasMoreElements()) {
				ResponseLabel o = (ResponseLabel) enumO.nextElement();
				if (o.getRarea() != null && o.getRarea().length() > 0) {
					vObjects.addElement(o);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return vObjects;
	}
	
	public ResponseLabel getTarget(String sSourceId, String sPosition){
		ResponseLabel oRespLabel = null;
		if (hSources.containsKey(sSourceId)){
			oRespLabel=(ResponseLabel)hSources.get(sSourceId);
		}else{
			Item oItem = mainBean.getPregunta();
			Conditionvar oCondVar = getConditionvar(oItem);
			if (oCondVar!=null){
				try{
					int iPosition = Integer.parseInt(sPosition);
					if (iPosition>0){
						And oAnd = (And)oCondVar.getBooleanOperators().get(iPosition-1);
						Enumeration enumAnd = oAnd.getVarsubset().elements();
						while (enumAnd.hasMoreElements()){
							Varsubset oVar = (Varsubset)enumAnd.nextElement();
							if (oVar.getText().indexOf(sSourceId)==0){
								String sTarget = oVar.getText().substring(sSourceId.length()+1);
								ResponseLabel o = getTarget(sTarget);
								if (o!=null) {
									hSources.put(sSourceId, o);
									oRespLabel=o;
								}
							}
						}
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		
		return oRespLabel;
	}
	
	private Conditionvar getConditionvar(QTIObject oQTI){
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
	
	
		
	public boolean isShuffle(){
		boolean bIs = false;
		Item oItem = (Item)getBeanObject();
		QTISuperRender oRender = QVResponseLabelListControl.getItemRender(oItem);
		if (oRender!=null && oRender instanceof IMSRenderObject){
			String sIs = ((IMSRenderObject)oRender).getShuffle();
			bIs = (sIs!=null && sIs.equalsIgnoreCase("Yes"));
		}
		return bIs;
	}
	
	public boolean isInside(){
		boolean bIs = false;
		Item oItem = (Item)getBeanObject();
		QTISuperRender oRender = QVResponseLabelListControl.getItemRender(oItem);
		if (oRender!=null && oRender instanceof IMSRenderObject){
			String sIs = ((IMSRenderObject)oRender).getInside();
			bIs = (sIs!=null && sIs.equalsIgnoreCase("Yes"));
		}
		return bIs;
	}
	
	public boolean showTarget(){
		boolean bShowTarget = false;
		Item oItem = (Item)getBeanObject();
		QTISuperRender oRender = QVResponseLabelListControl.getItemRender(oItem);
		if (oRender!=null && oRender instanceof IMSRenderObject){
			String sShowTarget = ((IMSRenderObject)oRender).getShowTargets();
			bShowTarget = (sShowTarget==null || !sShowTarget.equalsIgnoreCase("false"));
		}
		return bShowTarget;
	}
	
	public boolean isEnabledRotating(){
		boolean bIs = false;
		Item oItem = (Item)getBeanObject();
		QTISuperRender oRender = QVResponseLabelListControl.getItemRender(oItem);
		if (oRender!=null && oRender instanceof IMSRenderObject){
			String sIs = ((IMSRenderObject)oRender).getEnableRotating();
			bIs = (sIs!=null && sIs.equalsIgnoreCase("Yes"));
		}
		return bIs;
	}
	
	public boolean isEnabledScaling(){
		boolean bIs = false;
		Item oItem = (Item)getBeanObject();
		QTISuperRender oRender = QVResponseLabelListControl.getItemRender(oItem);
		if (oRender!=null && oRender instanceof IMSRenderObject){
			String sIs = ((IMSRenderObject)oRender).getEnableScaling();
			bIs = (sIs!=null && sIs.equalsIgnoreCase("Yes"));
		}
		return bIs;
	}
	
	public String getAlign(){
		String sAlign = ALIGN_NONE;
		Item oItem = (Item)getBeanObject();
		QTISuperRender oRender = QVResponseLabelListControl.getItemRender(oItem);
		if (oRender!=null && oRender instanceof IMSRenderObject){
			sAlign = ((IMSRenderObject)oRender).getAlign();
		}
		return sAlign;
	}
	
	public String getNRotate(){
		String sNRotate = null;
		Item oItem = (Item)getBeanObject();
		QTISuperRender oRender = QVResponseLabelListControl.getItemRender(oItem);
		if (oRender!=null && oRender instanceof IMSRenderObject){
			sNRotate = ((IMSRenderObject)oRender).getNRotate();
		}
		if (sNRotate==null || sNRotate.trim().length()==0) sNRotate="45";
		return sNRotate;
	}
	
	public String getNRatio(){
		String sNRatio = null;
		Item oItem = (Item)getBeanObject();
		QTISuperRender oRender = QVResponseLabelListControl.getItemRender(oItem);
		if (oRender!=null && oRender instanceof IMSRenderObject){
			sNRatio = ((IMSRenderObject)oRender).getNRatio();
		}
		if (sNRatio==null || sNRatio.trim().length()==0) sNRatio="1";
		return sNRatio;
	}
	
	public int getIntFitTarget(){
		int iFit = 0;
		try{
			iFit = Integer.parseInt(getFitTarget());
		}catch (Exception e){}
		return iFit;
	}
	
	public String getFitTarget(){
		String sFit = null;
		Item oItem = (Item)getBeanObject();
		QTISuperRender oRender = QVResponseLabelListControl.getItemRender(oItem);
		if (oRender!=null && oRender instanceof IMSRenderObject){
			sFit = ((IMSRenderObject)oRender).getFitTarget();
		}
		if (sFit==null || sFit.trim().length()==0) sFit="10";
		return sFit;
	}
	
	public String getBorderColorStyle(){
		return QTIUtil.getBorderColorStyle((Item)getBeanObject());
	}
	
	public void setBorderColorStyle(){
		String sBorderColor = getParameter(P_STYLE_BORDER_COLOR);
		if (sBorderColor!=null && !"#RRGGBB".equalsIgnoreCase(sBorderColor) &&
			sBorderColor.startsWith("#") && sBorderColor.trim().length()==7 ){
				QTIUtil.setBorderColorStyle((Item)getBeanObject(), sBorderColor);
		}
	}
	
	protected void updateFeedback(){
		String sFeedbackOK = getParameter(FEEDBACK_OK_PARAM);
		if (sFeedbackOK!=null)	QTIUtil.setFeedback(oItem, oItem.getRespcondition("CORRECT", "True"), sFeedbackOK);
		String sFeedbackKO = getParameter(FEEDBACK_KO_PARAM);
		if (sFeedbackKO!=null) {
			Respcondition orc = oItem.addRespcondition("CORRECT", "False");
			QTIUtil.setFeedback(oItem, orc, sFeedbackKO);		
		}
	}

	
}
