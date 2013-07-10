/*
 * QVPreguntaClozeBean.java
 * 
 * Created on 25/febrer/2004
 */
package edu.xtec.qv.editor.beans;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import edu.xtec.qv.editor.util.QTIUtil;
import edu.xtec.qv.editor.util.QVClozeResponseListControl;
import edu.xtec.qv.qti.Flow;
import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.Material;
import edu.xtec.qv.qti.Mattext;
import edu.xtec.qv.qti.Presentation;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.QTISuperResponse;
import edu.xtec.qv.qti.RenderFIB;
import edu.xtec.qv.qti.ResponseLID;
import edu.xtec.qv.qti.ResponseSTR;
import edu.xtec.qv.qti.util.StringUtil;


/**
 * @author sarjona
 */
public class QVPreguntaClozeBean extends QVPreguntaBean {
	
	public final String PARAGRAF_PREGUNTA_PARAM="paragraf_pregunta";

	public QVPreguntaClozeBean(QVBean mainBean){
		super(mainBean);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.QVBean#start()
	 */
	public boolean start() {
		boolean bOk = true;
		try{
			bOk = super.start();
		} catch (Exception e){
			logger.error("EXCEPCIO inicialitzant QVPreguntaClozeBean --> "+e);
			//e.printStackTrace();
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
				String sClozeText = getParameter(PARAGRAF_PREGUNTA_PARAM);
				Material oAdditionalMaterial = null;
				Vector vResponses = new Vector();
				Presentation oPresentation = oItem.getPresentation();
				if (sEnunciat!=null && sClozeText!=null){
					if (oPresentation!=null){
						vResponses = oPresentation.getResponses();
						oAdditionalMaterial = oPresentation.getMaterial(0);
					}
					oPresentation = new Presentation();
					Flow oFlow = new Flow();
					oPresentation.setFlow(oFlow);
					oFlow.addMaterial(oAdditionalMaterial);
					oItem.setPresentation(oPresentation);
				}

				String sInteractionswitch = getParameter(P_INTERACTIONSWITCH);
				if (sInteractionswitch!=null && sInteractionswitch.trim().length()>0){
					QTIUtil.setInteractionswitch(oItem, sInteractionswitch);
				}

				if (sEnunciat!=null){
					oItem.setStatement(sEnunciat);
				}
				if (sClozeText!=null){
					if (sClozeText.indexOf("<p>")>=0 || sClozeText.indexOf("<br>")>=0){
						Flow oFlow = createClozeLineFlow(sClozeText, vResponses);
						oPresentation.getFlow().addFlow(oFlow);						
					}else{
						StringTokenizer stLines = new StringTokenizer(sClozeText,"\n");;
						while(stLines.hasMoreTokens()){
							String sLine = stLines.nextToken();
							//Flow oFlow = createClozeLineFlow(StringUtil.filterHTML(sLine), vResponses);
							Flow oFlow = createClozeLineFlow(sLine, vResponses);
							oPresentation.getFlow().addFlow(oFlow);
						}
					}
				}
				QVClozeResponseListControl.updateResponse(mainBean.request, oItem);				

				updateFeedback();
				updatePuntuation();
			}else{
				//logger.debug("add new Item");
				String sResponseType = mainBean.getParameter(QVClozeResponseListControl.P_RESPONSE_OPTION_TYPE);
				if (sResponseType==null){
					Object o = mainBean.session.getAttribute(QVClozeResponseListControl.P_RESPONSE_OPTION_TYPE);
					sResponseType=o!=null?(String)o:QVClozeResponseListControl.CLOSE_OPTION_TYPE;
				}
				String sIdent = mainBean.getParameter(IDENT_PREGUNTA_PARAM, QTIUtil.getRandomIdent());
				oItem = Item.createItem(sIdent, "", "");
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
	
	protected Flow createClozeLineFlow(String sLine, Vector vResponses){
		Flow oFlow = new Flow();
		int iStartIndex = 0;
		int iEndIndex = sLine.indexOf("[");
		while (iEndIndex>=0 && iEndIndex<sLine.length()){
			String sMaterialText = sLine.substring(iStartIndex, iEndIndex);
			if (sMaterialText.trim().length()>0){
				oFlow.addMaterial(Material.createWithMattext(Mattext.HTML_TEXTTYPE, sMaterialText));
			}
			iStartIndex = iEndIndex+1;
			iEndIndex = sLine.indexOf("]", iStartIndex);
			String sResponseLID = sLine.substring(iStartIndex, iEndIndex);
			if (sResponseLID.trim().length()>0){
				sResponseLID = sResponseLID.toUpperCase();
				sResponseLID = StringUtil.replace(sResponseLID,"&nbsp;", "_");
				sResponseLID = StringUtil.filter(sResponseLID);
				String sResponseOptionType = getParameter(QVClozeResponseListControl.P_RESPONSE_OPTION_TYPE);
				if (sResponseOptionType==null){
					Object o = mainBean.session.getAttribute(QVClozeResponseListControl.P_RESPONSE_OPTION_TYPE);
					sResponseOptionType=o!=null?(String)o:QVClozeResponseListControl.CLOSE_OPTION_TYPE;
				}
				boolean bIsFIB = showAsFIB(sResponseLID) || sResponseOptionType.equals(QVClozeResponseListControl.OPEN_OPTION_TYPE);
				QTISuperResponse oResponse = getResponse(vResponses, sResponseLID);
				if (oResponse==null){
					if (bIsFIB){
						oResponse = QTIUtil.createResponseSTR(sResponseLID, "Single");
					}else{
						oResponse = QTIUtil.createResponseLID(sResponseLID, "Single", null, "LIST", null, null, null);
					}
				}
				if (oResponse != null){
					oFlow.addResponse(oResponse);
				}
			}
			iStartIndex = iEndIndex+1;
			iEndIndex = sLine.indexOf("[", iStartIndex);
		}
		if (iEndIndex<0 && iStartIndex<sLine.trim().length()){
			String sMaterialText = sLine.substring(iStartIndex);
			oFlow.addMaterial(Material.createWithMattext(Mattext.HTML_TEXTTYPE, sMaterialText));
		}
		if ("".equals(sLine.trim())){
			oFlow.addMaterial(Material.createWithMattext(Mattext.HTML_TEXTTYPE, " "));
		}
		return oFlow;
	}
	
	protected QTISuperResponse getResponse(Vector vResponses, String sIdent){
		QTISuperResponse oResponse = null;
		if (vResponses!=null && sIdent!=null){
			Enumeration enumResponses = vResponses.elements();
			while (enumResponses.hasMoreElements()){
				QTISuperResponse tmpResponse = (QTISuperResponse)enumResponses.nextElement();
				if (sIdent.equals(tmpResponse.getIdent())){
					oResponse = tmpResponse;
					break;
				}
			}
		}
		return oResponse;
	}
	
		
	protected void addResponse(){
	}
	
	protected void delResponse(){
		String sLIDIdent = mainBean.getParameter("ident_resp_lid");
		String sRespLabelIdent = mainBean.getParameter(IDENT_RESPOSTA_PARAM);
		if (sLIDIdent!=null && sRespLabelIdent!=null){
			Presentation oPresentation = mainBean.getPregunta().getPresentation();
			if (oPresentation!=null){
				QTISuperResponse oResponse = oPresentation.getResponse(sLIDIdent);
				if (oResponse!=null){
					if (oResponse instanceof ResponseLID){
						if (oResponse.getRender()!=null){
							oResponse.getRender().delResponseLabel(sRespLabelIdent);
						}
					}else{
					}
				}
			}
		}
	}


//	***************************	
//	*  Pregunta
//	***************************
	
	/**
	 * @return text del paragraf, amb els identificadors de les respostes entre claus ([])
	 */
	public String getClozeText(){
		String sText = QTIUtil.getClozeText(mainBean.getPregunta());
		if (sText==null){
			sText = "";
		}
		return sText;
	}
	
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
	
	/**
	 * @return
	 */
	public boolean showAsList(String sIdentLID){
		return showAsList(mainBean.getPregunta(), sIdentLID);
	}
	public static boolean showAsList(Item oItem, String sIdentLID){
		QTISuperResponse oResponse = QTIUtil.getResponse(oItem, sIdentLID);
		boolean bLlista = oResponse==null || oResponse instanceof ResponseLID;
		return bLlista;
	}

	/**
	 * 
	 */		
	public boolean showAsFIB(String sIdentLID){
		return showAsFIB(mainBean.getPregunta(), sIdentLID);
	}
	public static boolean showAsFIB(Item oItem, String sIdentLID){
		return !showAsList(oItem, sIdentLID);
	}

	public String getRows(String sResponseIdent){
		String sRows = null;
		QTISuperResponse oResponse = QTIUtil.getResponse(mainBean.getPregunta(), sResponseIdent);
		if (oResponse instanceof ResponseSTR){
			RenderFIB oRender = (RenderFIB)oResponse.getRender();
			if (oRender!=null){
				sRows = oRender.getRows();
			}
		}
		return sRows;
		
	}

	public String getColumns(String sResponseIdent){
		String sColumns = null;
		QTISuperResponse oResponse = QTIUtil.getResponse(mainBean.getPregunta(), sResponseIdent);
		if (oResponse instanceof ResponseSTR){
			RenderFIB oRender = (RenderFIB)oResponse.getRender();
			if (oRender!=null){
				sColumns = oRender.getColumns();
			}
		}
		return sColumns;		
	}
	
}
