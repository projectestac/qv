/*
 * QVOrderedResponseListControl.java
 * 
 * Created on 09/juny/2004
 */
package edu.xtec.qv.editor.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import edu.xtec.qv.editor.beans.QVBean;
import edu.xtec.qv.qti.Conditionvar;
import edu.xtec.qv.qti.IMSRenderObject;
import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.Material;
import edu.xtec.qv.qti.Matimage;
import edu.xtec.qv.qti.Mattext;
import edu.xtec.qv.qti.Presentation;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.QTISuperMat;
import edu.xtec.qv.qti.QTISuperResponse;
import edu.xtec.qv.qti.Respcondition;
import edu.xtec.qv.qti.ResponseLID;
import edu.xtec.qv.qti.ResponseLabel;
import edu.xtec.qv.qti.Varequal;

/**
 * @author sarjona
 */
public class QVOrderedResponseListControl extends QVListControl{
	
	private QVBean oBean = null;
	
	public QVOrderedResponseListControl(HttpServletRequest request){
		super(request);
	}
	
	public QVOrderedResponseListControl(HttpServletRequest request, QVBean oBean){
		super(request);
		this.oBean = oBean;
	}

	// Parameter
	public static final String P_RESPONSE_LID_IDENT = "ident_resp_lid";
	public static final String P_RESPONSE_LABEL_IDENT = "p_response_label_ident";
	public static final String P_RESPONSE_LABEL_TEXT ="p_response_label_text";
	public static final String P_RESPONSE_LABEL_IMAGE = "p_response_label_image";
	public static final String P_RESPONSE_LABEL_ORDER = "p_response_label_order";
	public static final String P_ORIENTATION = "orientation";
	public static final String P_SHUFFLE = "p_shuffle";
	public static final String P_NUMBER_RESPONSES = "num_respostes";
	public static final String P_RESPONSE_MATERIAL_TYPE = "response_material_type";
	public static final String P_FEEDBACK_OK = "feedback_ok";
	public static final String P_FEEDBACK_KO = "feedback_ko";
	public static final String P_PUNTUATION_OK = "puntuation_ok";
	public static final String P_PUNTUATION_KO = "puntuation_ko";
	public static final String P_WIDTH = "width";
	public static final String P_HEIGHT = "height";
	
	public static final String ROW_ORIENTATION = "row"; 
	public static final String COLUMN_ORIENTATION = "column"; 
	
	public static String A_UP = "up";
	public static String A_DOWN = "down";
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getListObjects(edu.xtec.qv.qti.QTIObject)
	 */
	public Vector getListObjects(QTIObject oQTI){
		Vector vListObjects = new Vector();
		Vector vResponses = getOrderedResponses(oQTI);
		Enumeration enumResponses = vResponses.elements();
		while (enumResponses.hasMoreElements()){
			QTISuperResponse oResponse = (QTISuperResponse)enumResponses.nextElement();
			ListObject o = new ListObject(oResponse.getIdent(), getOrderedResponseText(oResponse));
			vListObjects.addElement(o);
		}
		return vListObjects;
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getObject(QTIObject oQTI, String sIdent){
		return getOrderedResponse(oQTI, sIdent);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getPreviousObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getPreviousObject(QTIObject oQTI, String sIdent){
		logger.debug("Method getPreviousObject not implemented");
		return null;
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getNextObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getNextObject(QTIObject oQTI, String sIdent){
		logger.debug("Method getPreviousObject not implemented");
		return null;
	}
	
	/**
	 * 
	 * @param oQTI
	 * @param sIdent ResponseLID ident
	 */
	public void addListObject(QTIObject oQTI, String sIdent){
		QTISuperResponse oNew = (QTISuperResponse)getRequestQTIObject(sIdent);
		addResponse((Item)oQTI, oNew);
	}
	
	public void delListObject(QTIObject oQTI, String sIdent){
		if (oQTI!=null && sIdent!=null){
			if (oQTI instanceof Item){
				Presentation oPresentation = ((Item)oQTI).getPresentation();
				if (oPresentation!=null){
					oPresentation.delResponse(sIdent);
					Vector vConditions = getAllConditions((Item)oQTI, sIdent);
					updateConditions((Item)oQTI, sIdent, vConditions);
				}
			}
		}
	}
	
	public void setListObject(QTIObject oQTI, String sIdent){
		QTISuperResponse oNew = (QTISuperResponse)getRequestQTIObject(sIdent);
		String[] sResponseLabelIdent = request.getParameterValues(QVOrderedResponseListControl.P_RESPONSE_LABEL_IDENT+"_"+sIdent);
		if (sResponseLabelIdent!=null){
			addResponse((Item)oQTI, oNew);
		}
	}
	
	public void upListObject(QTIObject oQTI, String sIdent){
		setOrderedResponsePosition(oQTI, sIdent, A_UP);
	}
	
	public void downListObject(QTIObject oQTI, String sIdent){
		setOrderedResponsePosition(oQTI, sIdent, A_DOWN);
	}

	private void setOrderedResponsePosition(QTIObject oQTI, String sIdent, String sAction){
		if (oQTI!=null && sIdent!=null && sAction!=null){
			Presentation oPresentation = ((Item)oQTI).getPresentation();
			if (oPresentation!=null){
				int iOldPosition = oPresentation.getResponsePosition(sIdent);
				int iNewPosition = iOldPosition+1;
				if (sAction.equals(A_UP)){
					iNewPosition = iOldPosition-1;
				}
				//if (iNewPosition>=0 && iNewPosition<oPresentation.getResponses().size()){
				if (iNewPosition>=0){
					QTISuperResponse oResponse = getOrderedResponse(oQTI, sIdent);
					oPresentation.setResponsePosition(oResponse, iOldPosition, iNewPosition);
				}
			}
		}
	}

	private void addResponse(Item oItem, QTISuperResponse oResponse){
		if (oItem!=null && oResponse!=null){
			oItem.addResponse(oResponse);
			String[] sResponseLabelIdent = request.getParameterValues(QVOrderedResponseListControl.P_RESPONSE_LABEL_IDENT+"_"+oResponse.getIdent());
			String[] sResponseLabelOrder = request.getParameterValues(QVOrderedResponseListControl.P_RESPONSE_LABEL_ORDER+"_"+oResponse.getIdent());
			Vector vConditions = getAllConditions(oItem, oResponse.getIdent(), sResponseLabelIdent, sResponseLabelOrder);
			updateConditions(oItem, oResponse.getIdent(), vConditions);
		}
	}
	
	private void updateConditions(Item oItem, String sLIDIdent, Vector vConditions){
		if (sLIDIdent!=null){
			String sFeedbackOK = request.getParameter(P_FEEDBACK_OK);
			String sPuntuationOK = request.getParameter(P_PUNTUATION_OK);
			QTIUtil.addCorrectResponseCondition(oItem, vConditions, sFeedbackOK, sPuntuationOK);
			
			String sFeedbackKO = request.getParameter(P_FEEDBACK_KO);
			String sPuntuationKO = request.getParameter(P_PUNTUATION_KO);
			QTIUtil.addIncorrectResponseCondition(oItem, vConditions, sFeedbackKO, sPuntuationKO);
		}
	}
	
	private Vector getAllConditions(Item oItem, String sLIDIdent){
		return getAllConditions(oItem, sLIDIdent, null, null);
	}
	private Vector getAllConditions(Item oItem, String sLIDIdent, String[] sResponseLabelIdent, String[] sResponseLabelOrder){
		Vector vConditions = new Vector();		
		Vector vAnd = new Vector();
		if (sResponseLabelIdent!=null){
			vConditions = QTIUtil.getOrderedConditions(oItem, sLIDIdent, sResponseLabelIdent, sResponseLabelOrder);		
			Enumeration enumConditions = vConditions.elements();
			while (enumConditions.hasMoreElements()){
				Hashtable hTmp = (Hashtable)enumConditions.nextElement();
				if (hTmp.containsKey("and")){
					vAnd = (Vector)hTmp.get("and");
					break;
				}
			}
		}else{
			Hashtable hAnd = new Hashtable();
			hAnd.put("and", vAnd);
			vConditions.addElement(hAnd);
		}
		Respcondition oRespcondition = oItem.getRespcondition("CORRECT","True");
		if (oRespcondition!=null){
			Conditionvar oConditionvar = oRespcondition.getConditionvar();
			if (oConditionvar!=null){
				Enumeration enumVarequals = oConditionvar.getAllVarequals().elements();
				while (enumVarequals.hasMoreElements()){
					Varequal oVarequal = (Varequal)enumVarequals.nextElement();
					String sIdent = oVarequal.getRespident();
					if (sIdent!=null && !sIdent.equals(sLIDIdent)){
						String sText = oVarequal.getText();
						Vector vRespLabel = new Vector();
						vRespLabel.addElement(sText);
						Hashtable hRespLabel = new Hashtable();
						hRespLabel.put(sIdent, vRespLabel);
						vAnd.addElement(hRespLabel);
					}
				}
			}
		}
		return vConditions;
	}
	
	private Vector getOrderedResponses(QTIObject oQTI){
		Vector vResponses = new Vector();
		if (oQTI!=null){
			if (oQTI instanceof Item){
				Presentation oPresentation = ((Item)oQTI).getPresentation();
				if (oPresentation!=null){
					Enumeration enumResponses = oPresentation.getResponses().elements();
					while (enumResponses.hasMoreElements()){
						QTISuperResponse oResponse = (QTISuperResponse)enumResponses.nextElement();
						if (oResponse instanceof ResponseLID && oResponse.getCardinality()!=null && oResponse.getCardinality().equals(QVConstants.ORDERED_TIPUS_PREGUNTA)){
							vResponses.addElement(oResponse);
						}
					}
				}
			}
		}
		return vResponses;
	}

	private QTISuperResponse getOrderedResponse(QTIObject oQTI, String sIdent){
		QTISuperResponse oResponse = null;
		try{
			if (sIdent!=null){
				Enumeration enumResponses = getOrderedResponses(oQTI).elements();
				while (enumResponses.hasMoreElements()){
					QTISuperResponse tmpResponse = (QTISuperResponse)enumResponses.nextElement();
					if (sIdent.equalsIgnoreCase(tmpResponse.getIdent())){
						oResponse = tmpResponse;
						break;
					}
				}
			}
		}catch (Exception e){
			logger.error("EXCEPTION obtenint resposta-> "+e);
		}
		return oResponse;
	}
	
	private String getOrderedResponseText(QTISuperResponse oResponse){
		String sText = "";
		if (oResponse!=null){
			try{
				Enumeration enumRespLabel = oResponse.getRender().getResponseLabel().elements();
				while (enumRespLabel.hasMoreElements()){
					ResponseLabel oRespLabel = (ResponseLabel)enumRespLabel.nextElement();
					Vector vMaterials = oRespLabel.getMaterials();
					if (vMaterials!=null && !vMaterials.isEmpty()){
						Material oMaterial = (Material)vMaterials.firstElement();
						Vector vMats = oMaterial.getMats();
						if (vMats!=null && !vMats.isEmpty()){
							QTISuperMat oMat = (QTISuperMat)vMats.firstElement();
							if (oMat instanceof Mattext){
								sText += oMat.getText();
							}else if (oMat instanceof Matimage){
								sText += "["+oMat.getURI()+"]";
							}
							if (enumRespLabel.hasMoreElements())
								sText += " / ";
						}
					}
				}
			}catch (Exception e){
				logger.debug("EXCEPCIO obtenint text per les respostes d'ordenació -> "+e);
			}
		}
		return sText;
	}
	
	public QTIObject getRequestQTIObject(){
		return getRequestQTIObject(null);
	}
	
	public QTIObject getRequestQTIObject(String sLIDIdent){
		int iNumberResponses = getIntParameter(request, P_NUMBER_RESPONSES, 1);
		boolean bShuffle = request.getParameter(QVOrderedResponseListControl.P_SHUFFLE)!=null || sLIDIdent==null;
		if (sLIDIdent==null){
			sLIDIdent = getParameter(request, P_RESPONSE_LID_IDENT, QTIUtil.getRandomIdent());
		}
		String sMaterialType = getParameter(request, P_RESPONSE_MATERIAL_TYPE, getParameter(request, P_RESPONSE_MATERIAL_TYPE+"_"+sLIDIdent, QTISuperMat.MATTEXT_CONTENT_TYPE));
		String sOrientation = getParameter(request, P_ORIENTATION, getParameter(request, P_ORIENTATION+"_"+sLIDIdent, ROW_ORIENTATION));
		if (sOrientation.equalsIgnoreCase("vertical")){
			sOrientation = COLUMN_ORIENTATION;
		}
		String[] sResponseLabelIdent = request.getParameterValues(QVOrderedResponseListControl.P_RESPONSE_LABEL_IDENT+"_"+sLIDIdent);
		String[] sResponseLabelText = request.getParameterValues(QVOrderedResponseListControl.P_RESPONSE_LABEL_TEXT+"_"+sLIDIdent);
		String[] sResponseLabelImage = request.getParameterValues(QVOrderedResponseListControl.P_RESPONSE_LABEL_IMAGE+"_"+sLIDIdent);

		if (sResponseLabelIdent==null){
			// S'ha d'iniciatlitzar amb el nombre de respostes indicat
			sResponseLabelIdent = new String[iNumberResponses];
			if (QTISuperMat.isMatimage(sMaterialType) || QTISuperMat.isMataudio(sMaterialType)){
				sResponseLabelImage = new String[iNumberResponses];
			}else{
				sResponseLabelText = new String[iNumberResponses];
			}
			for (int i=0;i<iNumberResponses;i++){
				sResponseLabelIdent[i] = QTIUtil.getRandomIdent();
				if (sResponseLabelText!=null) sResponseLabelText[i]="";
				if (sResponseLabelImage!=null) sResponseLabelImage[i]="";
			}
		}
		
		ResponseLID oResponse;
		if (sResponseLabelImage != null) {
			String sURI =  oBean.getQuadernLocalDirectory(oBean.getIdQuadern());
			oResponse = QTIUtil.createResponseLID(sLIDIdent, QVConstants.ORDERED_TIPUS_PREGUNTA, sOrientation, bShuffle, sResponseLabelIdent, sResponseLabelText, sResponseLabelImage, sURI);
		}
		else{
			oResponse = QTIUtil.createResponseLID(sLIDIdent, QVConstants.ORDERED_TIPUS_PREGUNTA, sOrientation, bShuffle, sResponseLabelIdent, sResponseLabelText, sResponseLabelImage);
		}
		
		if (oResponse!=null){
			IMSRenderObject oRender = (IMSRenderObject)oResponse.getRender();
			int iWidth = getIntParameter(request, P_WIDTH+"_"+sLIDIdent, -1); 
			int iHeight = getIntParameter(request, P_HEIGHT+"_"+sLIDIdent, -1);
			if (iWidth>0) oRender.setWidth(String.valueOf(iWidth));
			if (iHeight>0) oRender.setHeight(String.valueOf(iHeight));
		}
		return oResponse;
	}
	
}
