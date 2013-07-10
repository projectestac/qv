/*
 * QVPreguntaOrdenacioBean.java
 * 
 * Created on 17/març/2004
 */
package edu.xtec.qv.editor.beans;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import edu.xtec.qv.editor.util.QTIUtil;
import edu.xtec.qv.editor.util.QVOrderedResponseListControl;
import edu.xtec.qv.qti.Conditionvar;
import edu.xtec.qv.qti.IMSRenderObject;
import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.Presentation;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.QTISuperMat;
import edu.xtec.qv.qti.QTISuperRender;
import edu.xtec.qv.qti.QTISuperResponse;
import edu.xtec.qv.qti.Respcondition;
import edu.xtec.qv.qti.ResponseLID;
import edu.xtec.qv.qti.Varequal;

/**
 * @author sarjona
 */
public class QVPreguntaOrdenacioBean extends QVPreguntaBean {

	String sOK;
	
	
	public QVPreguntaOrdenacioBean(QVBean mainBean){
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
			logger.error("EXCEPCIO inicialitzant QVPreguntaOrdenacioBean --> "+e);
			e.printStackTrace();
			bOk = false;
		}
		return bOk;
	}

//	***************************	
//	*  Actualitzacio/accio
//	***************************
	protected Item getCurrentItem(){
		if (oItem==null){
			oItem = mainBean.getPregunta();
			if (oItem!=null){
				//logger.debug("update Item");
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
	
				QTISuperResponse oResponse = getRequestResponse();
				if (oResponse!=null){
					oItem.addResponse(oResponse);
					updateConditions(oResponse.getIdent());
				}
				updateFeedback();
				updatePuntuation();
			}else{
				//logger.debug("add new Item");
				String sIdent = mainBean.getParameter(IDENT_PREGUNTA_PARAM, QTIUtil.getRandomIdent());
				int iNumRespostes = mainBean.getIntParameter("num_respostes",1);
				String sOrientation = mainBean.getParameter("orientation", QVOrderedResponseListControl.ROW_ORIENTATION);
				if (sOrientation.equalsIgnoreCase("vertical")){
					sOrientation = QVOrderedResponseListControl.COLUMN_ORIENTATION;
				}
				String sMaterialType = mainBean.getParameter("response_material_type_"+sIdent, mainBean.getParameter("response_material_type", QTISuperMat.MATTEXT_CONTENT_TYPE));
				String[] sResponseLabelIdent = new String[iNumRespostes];
				String[] sResponseLabelText = null;
				String[] sResponseLabelImage = null;
				if (QTISuperMat.isMatimage(sMaterialType) || QTISuperMat.isMataudio(sMaterialType)){
					sResponseLabelImage = new String[iNumRespostes];
				}else{
					sResponseLabelText = new String[iNumRespostes];
				}
				for (int i=0;i<iNumRespostes;i++){
					sResponseLabelIdent[i] = QTIUtil.getRandomIdent();
					if (sResponseLabelText!=null) sResponseLabelText[i]="";
					if (sResponseLabelImage!=null) sResponseLabelImage[i]="";
				}
				oItem = Item.createItem(sIdent, "", "");
				String sLIDIdent = mainBean.getParameter(IDENT_RESPONSE_LID_PARAM, QTIUtil.getRandomIdent());
				ResponseLID oResponse = QTIUtil.createResponseLID(sLIDIdent, ORDERED_TIPUS_PREGUNTA, sOrientation, true, sResponseLabelIdent, sResponseLabelText, sResponseLabelImage);
				oItem.addResponse(oResponse);
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
		//logger.debug("pregunta-> "+edu.xtec.qv.qti.util.XMLUtil.showElement(oItem.getXML()));
		return oItem;
	}
	
	
	public QTISuperResponse getRequestResponse(){
		QTISuperResponse oResponse = null;
		String sLIDIdent = getParameter(QVOrderedResponseListControl.P_RESPONSE_LID_IDENT);
		if (sLIDIdent!=null){
			String[] sResponseLabelIdent = getParameterValues(QVOrderedResponseListControl.P_RESPONSE_LABEL_IDENT+"_"+sLIDIdent);
			String[] sResponseLabelText = getParameterValues(QVOrderedResponseListControl.P_RESPONSE_LABEL_TEXT+"_"+sLIDIdent);
			String[] sResponseLabelImage = getParameterValues(QVOrderedResponseListControl.P_RESPONSE_LABEL_IMAGE+"_"+sLIDIdent);
			//String[] sResponseLabelOrder = request.getParameterValues(P_RESPONSE_LABEL_ORDER+"_"+sLIDIdent);
			String sOrientation = getParameter(QVOrderedResponseListControl.P_ORIENTATION+"_"+sLIDIdent);
			if (sOrientation==null){
				sOrientation = QVOrderedResponseListControl.ROW_ORIENTATION;
			}
			boolean bShuffle = getParameter(QVOrderedResponseListControl.P_SHUFFLE)!=null;
			oResponse = QTIUtil.createResponseLID(sLIDIdent, ORDERED_TIPUS_PREGUNTA, sOrientation, bShuffle, sResponseLabelIdent, sResponseLabelText, sResponseLabelImage);
			if (oResponse!=null){
				IMSRenderObject oRender = (IMSRenderObject)oResponse.getRender();
				int iWidth = mainBean.getIntParameter(QVOrderedResponseListControl.P_WIDTH+"_"+sLIDIdent, -1); 
				int iHeight = mainBean.getIntParameter(QVOrderedResponseListControl.P_HEIGHT+"_"+sLIDIdent, -1);
				if (iWidth>0) oRender.setWidth(String.valueOf(iWidth));
				if (iHeight>0) oRender.setHeight(String.valueOf(iHeight));
			}
		}
		return oResponse;
	}
	
	protected void updateConditions(String sLIDIdent){
		if (sLIDIdent!=null){
			// Actualitzar les condicions
			String[] sResponseLabelIdent = getParameterValues(QVOrderedResponseListControl.P_RESPONSE_LABEL_IDENT+"_"+sLIDIdent);
			String[] sResponseLabelOrder = getParameterValues(QVOrderedResponseListControl.P_RESPONSE_LABEL_ORDER+"_"+sLIDIdent);
			String sFeedbackOK = getParameter(FEEDBACK_OK_PARAM);
			String sPuntuationOK = getParameter(PUNTUATION_OK_PARAM);
			Vector vConditions = getAllConditions(oItem, sLIDIdent, sResponseLabelIdent, sResponseLabelOrder);
			// Afegir condicions de la resta de responseLID
			QTIUtil.addCorrectResponseCondition(oItem, vConditions, sFeedbackOK, sPuntuationOK);
			
			String sFeedbackKO = getParameter(FEEDBACK_KO_PARAM);
			String sPuntuationKO = getParameter(PUNTUATION_KO_PARAM);
			QTIUtil.addIncorrectResponseCondition(oItem, vConditions, sFeedbackKO, sPuntuationKO);
		}
	}
	
	protected Vector getAllConditions(Item oItem, String sLIDIdent, String[] sResponseLabelIdent, String[] sResponseLabelOrder){
		Vector vConditions = QTIUtil.getOrderedConditions(oItem, sLIDIdent, sResponseLabelIdent, sResponseLabelOrder);		
		Vector vAnd = new Vector();
		Enumeration enumConditions = vConditions.elements();
		while (enumConditions.hasMoreElements()){
			Hashtable hTmp = (Hashtable)enumConditions.nextElement();
			if (hTmp.containsKey("and")){
				vAnd = (Vector)hTmp.get("and");
				break;
			}
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
	
	protected void addResponse(){
	}
	
	protected void delResponse(){
		String sLIDIdent = mainBean.getParameter("ident_resp_lid");
		String sRespLabelIdent = mainBean.getParameter(IDENT_RESPOSTA_PARAM);
		//logger.debug("del response-> lid="+sLIDIdent+"  respLabel="+sRespLabelIdent);
		if (sLIDIdent!=null && sRespLabelIdent!=null){
			Presentation oPresentation = mainBean.getPregunta().getPresentation();
			if (oPresentation!=null){
				QTISuperResponse oResponse = oPresentation.getResponse(sLIDIdent);
				if (oResponse!=null){
					if (oResponse.getRender()!=null){
						oResponse.getRender().delResponseLabel(sRespLabelIdent);
					}
				}
			}
		}
	}
	
	
//	***************************	
//	*  Pregunta
//	***************************
	/**
	 * @return Vector amb els identificadors de tots els Response del paragraf
	 */
	public Vector getResponses(){
		Vector vIdRespostes = new Vector();
		Vector vRespostes = mainBean.getPregunta().getPresentation().getResponses();
		if (vRespostes!=null){
			Enumeration enumRespostes = vRespostes.elements();
			while (enumRespostes.hasMoreElements()){
				QTIObject oQTI = (QTIObject)enumRespostes.nextElement();
				if (!vIdRespostes.contains(oQTI.getIdent())){
					vIdRespostes.addElement(oQTI.getIdent());
				}
			}
		}
		return vIdRespostes;
	}

//	********************************	
//	* Ordered response 
//	********************************
		
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.QVRespostaOrdenacioBean#getOrderResponseLabel(java.lang.String, java.lang.String)
	 */
	public int getOrderResponseLabel(String sIdentResp, String sIdentRespLabel){
		int iOrder = 0;
		boolean bFound = false;
		if (getOKResponses(sIdentResp)!=null && getOKResponses(sIdentResp).trim().length()>0 && !getOKResponses(sIdentResp).equals("null")){
			StringTokenizer stOK = new StringTokenizer(getOKResponses(sIdentResp), ",");
			while (stOK.hasMoreTokens()){
				String sTmpIdentRespLabel=stOK.nextToken();
				if (sTmpIdentRespLabel.trim().equalsIgnoreCase(sIdentRespLabel)){
					bFound = true;
					break;
				}else{
					iOrder++;
				}
			}
		}
		if (!bFound && (getOKResponses(sIdentResp)==null || getOKResponses(sIdentResp).trim().length()<=0 || getOKResponses(sIdentResp).equals("null"))){
			iOrder = -1;
		}
		return iOrder;
	}
	
	protected String getOKResponses(String sIdentResp){
		if (sOK==null){
			sOK = QTIUtil.getOKResponses(mainBean.getPregunta(), sIdentResp).toString();
			sOK = sOK.substring(1, sOK.length()-1);
		}
		return sOK;
	}

	public boolean isShuffle(String sIdentResp){
		boolean bShuffle = false;
		try{
			QTISuperResponse oResponse = QTIUtil.getResponse(mainBean.getPregunta(), sIdentResp);
			if (oResponse!=null){
				IMSRenderObject oRender = (IMSRenderObject)oResponse.getRender();
				if (oRender!=null){
					String sShuffle = oRender.getShuffle();
					bShuffle = (sShuffle!=null && sShuffle.equalsIgnoreCase("Yes"));
				}
			}
		}catch (Exception e){
			logger.error("EXCEPCIO obtenint l'orientació de la resposta "+sIdentResp+" --> e="+e);
			e.printStackTrace();			
		}
		return bShuffle;
	}

	public String getWidth(String sIdentResp){
		String sWidth = null;
		QTISuperRender oRender = getRender(sIdentResp);
		if (oRender!=null){
			sWidth = ((IMSRenderObject)oRender).getWidth();
		}		
		return sWidth;
	}
	
	public String getHeight(String sIdentResp){
		String sHeight = null;
		QTISuperRender oRender = getRender(sIdentResp);
		if (oRender!=null){
			sHeight = ((IMSRenderObject)oRender).getHeight();
		}		
		return sHeight;
	}
		
}
