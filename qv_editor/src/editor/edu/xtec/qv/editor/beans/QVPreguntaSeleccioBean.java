/*
 * QVPreguntaSeleccioBean.java
 * 
 * Created on 09/febrer/2004
 */
package edu.xtec.qv.editor.beans;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import edu.xtec.qv.editor.util.QTIUtil;
import edu.xtec.qv.editor.util.QVMaterialListControl;
import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.Material;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.QTISuperMat;
import edu.xtec.qv.qti.QTISuperRender;
import edu.xtec.qv.qti.QTISuperResponse;



/**
 * @author sarjona
 */
public class QVPreguntaSeleccioBean extends QVPreguntaBean {
	
	
	public QVPreguntaSeleccioBean(QVBean mainBean){
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
			logger.error("EXCEPCIO inicialitzant QVPreguntaSeleccioBean --> "+e);
			e.printStackTrace();
			bOk = false;
		}
		return bOk;
	}

//	***************************	
//	*  Actualitzacio
//	***************************
	public Item getCurrentItem(){
		if (oItem==null){
			String sIdentItem = getParameter(IDENT_PREGUNTA_PARAM);
			if (sIdentItem==null || sIdentItem.trim().length()==0){
				sIdentItem = QTIUtil.getRandomIdent();
			}
			String sTitle = getParameter(TITLE_PARAM);
			//String sEnunciat = mainBean.textToHTML(getParameter(ENUNCIAT_PREGUNTA_PARAM));
			String sEnunciat = getParameter(ENUNCIAT_PREGUNTA_PARAM);
			String sIdentLID = getParameter(IDENT_RESPONSE_LID_PARAM)!=null?getParameter(IDENT_RESPONSE_LID_PARAM):QTIUtil.getRandomIdent();
			String sCardinalitat = getParameter(CARDINALITAT_PARAM);
			String sAlignment = QTIObject.VERTICAL;
			if (getParameter(ORIENTATION_PARAM)!=null){
				sAlignment = getParameter(ORIENTATION_PARAM);
			}
			// Mostrar com una llista?
			String sDisplay = null;
			if (getParameter(MOSTRAR_LLISTA_PARAM+"_"+sIdentLID)!=null && sCardinalitat!=null && !sCardinalitat.equalsIgnoreCase("multiple")){
				sDisplay ="LIST";			
			}

			int iNumberResponses = mainBean.getIntParameter(NUM_RESPOSTES_PARAM, 0);
			String sMaterialType = mainBean.getParameter("response_material_type", QTISuperMat.MATTEXT_CONTENT_TYPE);
			String[] sResponseLabelIdent = getParameterValues(IDENT_RESPOSTES_PARAM+"_"+sIdentLID);
			String[] sResponseLabelText = getParameterValues(TEXT_RESPOSTES_PARAM+"_"+sIdentLID);
			String[] sResponseLabelResource = getParameterValues(RECURS_RESPOSTES_PARAM+"_"+sIdentLID);

			if (sResponseLabelIdent==null && iNumberResponses>0){
				// S'ha d'iniciatlitzar amb el nombre de respostes indicat
				sResponseLabelIdent = new String[iNumberResponses];
				if (QTISuperMat.isMatimage(sMaterialType)|| QTISuperMat.isMataudio(sMaterialType)){
					sResponseLabelResource = new String[iNumberResponses];
				}else{
					sResponseLabelText = new String[iNumberResponses];
				}
				for (int i=0;i<iNumberResponses;i++){
					sResponseLabelIdent[i] = QTIUtil.getRandomIdent();
					if (sResponseLabelText!=null) sResponseLabelText[i]="";
					if (sResponseLabelResource!=null) sResponseLabelResource[i]="";
				}
			}
			oItem = QTIUtil.createSelectionItem(sIdentItem, sTitle, sEnunciat, sIdentLID, sCardinalitat, sAlignment, sDisplay, sResponseLabelIdent, sResponseLabelText, sResponseLabelResource);
			
			String sInteractionswitch = getParameter(P_INTERACTIONSWITCH);
			if (sInteractionswitch!=null && sInteractionswitch.trim().length()>0){
				QTIUtil.setInteractionswitch(oItem, sInteractionswitch);
			}
	
			// Actualitzar les respostes correctes
			String[] sRespostesCorrectes = getParameterValues(RESPOSTES_CORRECTES_PARAM+"_"+sIdentLID);
			Hashtable hOKResponses = new Hashtable();
			hOKResponses.put(sIdentLID, QTIUtil.arrayToVector(sRespostesCorrectes));
			Hashtable hAllResponses = new Hashtable();
			hAllResponses.put(sIdentLID, QTIUtil.arrayToVector(sResponseLabelIdent));
	
			String sFeedbackOK = getParameter(FEEDBACK_OK_PARAM);
			String sPuntuationOK = getParameter(PUNTUATION_OK_PARAM);
			Vector vConditions = QTIUtil.getSelectionConditions(oItem, hAllResponses, hOKResponses);
			QTIUtil.addCorrectResponseCondition(oItem, vConditions, sFeedbackOK, sPuntuationOK);
	
			String sFeedbackKO = getParameter(FEEDBACK_KO_PARAM);
			String sPuntuationKO = getParameter(PUNTUATION_KO_PARAM);
			QTIUtil.addIncorrectResponseCondition(oItem, vConditions, sFeedbackKO, sPuntuationKO);
			
			String sHint = getParameter(HINT_PARAM);
			oItem.addHint(sHint);
			
			// Actualitzar materials de la pregunta
			updateMaterials(oItem, sIdentLID);

			
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
	

	protected QTISuperRender updateMaterials(Item oItem, String sIdentResp){
		QTISuperRender oRender = null;
		Material oMaterial = QVMaterialListControl.getMaterial(oItem);
		if (oMaterial!=null){
			Vector vMats = QVMaterialListControl.getMaterials(mainBean.getPregunta());
			Enumeration enumMats = vMats.elements();
			while (enumMats.hasMoreElements()){
				QTISuperMat oMat = (QTISuperMat)enumMats.nextElement();
				oMaterial.addMaterial(oMat);
			}
		}
		return oRender;
	}	

	
//	***************************	
//	*  Consulta
//	***************************
	public boolean showImages(String sIdentLID){
		return !showAsList(sIdentLID);
	}
	
	public boolean showAsList(String sIdentResp){
		String sDisplay = QTIUtil.getDisplayRenderChoice(mainBean.getPregunta(), sIdentResp);
		boolean bLlista = (sDisplay!=null && sDisplay.equalsIgnoreCase("LIST"));
		return bLlista;
	}

	public boolean isVertical(String sIdentLID){
		return QTIUtil.isVertical(mainBean.getPregunta());
	}
	

	/**
	 * Obté el ResponseLID per defecte de la pregunta actual
	 * @return
	 */
	public String getIdentResponse(){
		String sIdentResponse = "";
		QTISuperResponse oResponse = QTIUtil.getResponse(mainBean.getPregunta());
		if (oResponse!=null){
			sIdentResponse = oResponse.getIdent();
		}
		return sIdentResponse;
	}
	
	public String getCardinalitat(){
		String sCardinalitat = getParameter(CARDINALITAT_PARAM);
		if (sCardinalitat==null){
			QTISuperResponse oResponse = QTIUtil.getResponse(mainBean.getPregunta());
			if (oResponse!=null){
				sCardinalitat = oResponse.getCardinality();
			}
		}
		if (sCardinalitat == null){
			sCardinalitat = "";
		}
		return sCardinalitat;
	}

}
