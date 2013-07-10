/*
 * QVPreguntaBean.java
 * 
 * Created on 13-feb-2004
 */
package edu.xtec.qv.editor.beans;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import edu.xtec.qv.editor.util.QTIUtil;
import edu.xtec.qv.qti.Assessment;
import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.Material;
import edu.xtec.qv.qti.Matimage;
import edu.xtec.qv.qti.Mattext;
import edu.xtec.qv.qti.Presentation;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.QTISuperMat;
import edu.xtec.qv.qti.QTISuperRender;
import edu.xtec.qv.qti.QTISuperResponse;
import edu.xtec.qv.qti.Respcondition;
import edu.xtec.qv.qti.ResponseLID;
import edu.xtec.qv.qti.ResponseLabel;
import edu.xtec.qv.qti.ResponseNUM;
import edu.xtec.qv.qti.ResponseSTR;
import edu.xtec.qv.qti.Resprocessing;
import edu.xtec.qv.qti.Section;
import edu.xtec.qv.qti.Varequal;
import edu.xtec.qv.qti.util.StringUtil;

/**
 * @author sarjona
 */
public class QVPreguntaBean extends QVSpecificBean implements IQVResponseBean, IQVListControlBean, IQVInteractionBean {
	

	public final String ADD_PREGUNTA_ACTION_PARAM = "add_pregunta";
	public final String SET_PREGUNTA_ACTION_PARAM = "set_pregunta";
	public final String DEL_PREGUNTA_ACTION_PARAM = "del_pregunta";
	public final String SAVE_PREGUNTA_ACTION_PARAM = "save_pregunta";
	public final String ADD_RESPOSTA_ACTION_PARAM = "add_num_respostes";
	public final String DEL_RESPOSTA_ACTION_PARAM = "del_resposta";
	
	public final String TIPUS_PREGUNTA_PARAM = "tipus_pregunta";
	public final String ENUNCIAT_PREGUNTA_PARAM = "enunciat_pregunta";
	public final String FEEDBACK_OK_PARAM = "feedback_ok";
	public final String FEEDBACK_KO_PARAM = "feedback_ko";
	public final String HINT_PARAM = "hint";
	public final String IDENT_RESPOSTA_PARAM = "ident_resposta";
	public final String MATEIXA_LINIA_PARAM = "mateixa_linia";
	public final String ORIENTATION_PARAM = "orientation";
	public final String TITLE_PREGUNTA_PARAM="title_pregunta";
	public final String PUNTUATION_OK_PARAM = "puntuation_ok";
	public final String PUNTUATION_KO_PARAM = "puntuation_ko";
	public final String UP_RESPOSTA_PARAM = "up_resposta";
	public final String DOWN_RESPOSTA_PARAM = "down_resposta";


	protected Hashtable hRespostes = new Hashtable();
	protected Hashtable hTextResp = new Hashtable();
	protected Hashtable hImageResp = new Hashtable();
	protected Hashtable hResourceResp = new Hashtable();
	protected String sTipus = null;	
	protected String sCurrentResponse = null;
	protected Item oItem;
	protected String sItemType;

	public QVPreguntaBean(QVBean mainBean){
		super(mainBean);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.QVSpecificBean#start()
	 */
	public boolean start() {
		boolean bOk = true;
		try{
			if (mainBean.getQuadern()==null){
				mainBean.redirectResponse(mainBean.getTranslatedPath(JSP_QUADERN));
			}
			String sAccio = getSessionParameter(ACTION_PARAM);
			// Actualitzar dades de la pregunta actual
			updateCurrentItem();
			//logger.debug("accio="+sAccio+"  getParameter(sAccio)="+getParameter(sAccio)+"  listControl?"+(getListControl()!=null?getListControl().isListControlAction():false)+"  class="+this.getClass().getName());
			if (getListControl()!=null && getListControl().isListControlAction()){
				getListControl().start(getBeanObject());
			}else if (sAccio!=null && sAccio.trim().length()>0){
				// Actualitzar dades de la pregunta actual
				//updateCurrentItem();
				// Redireccionar pregunta a la JSP que li correspongui
				if ( ((sAccio.equalsIgnoreCase(ADD_PREGUNTA_ACTION_PARAM) && getParameter(TIPUS_PREGUNTA_PARAM)!=null) 
					|| (!sAccio.equalsIgnoreCase(ADD_PREGUNTA_ACTION_PARAM) && getItemType()!=null))
					&& isQVPreguntaBean()){
					redirect();
					return true;
				}

				// Accions ITEM
				if (sAccio.equals(ADD_PREGUNTA_ACTION_PARAM)){
					addItem();
				}else if (sAccio.equalsIgnoreCase(SET_PREGUNTA_ACTION_PARAM)){
					setItem();
				} else if ( sAccio.equalsIgnoreCase(DEL_PREGUNTA_ACTION_PARAM) ||
							sAccio.equalsIgnoreCase(DEL_ACTION_PARAM)){
					if (delItem()){
						mainBean.setPregunta(null);
						mainBean.redirectResponse(mainBean.getTranslatedPath(JSP_FULL));
					}
				} else if (sAccio.equalsIgnoreCase(UP_ACTION_PARAM)){
					setItemPosition(sAccio);
				} else if (sAccio.equalsIgnoreCase(DOWN_ACTION_PARAM)){
					setItemPosition(sAccio);
				// Accions RESPOSTES
				} else if (sAccio.equalsIgnoreCase(ADD_RESPOSTA_ACTION_PARAM)){
					addResponse();
				} else if (sAccio.equalsIgnoreCase(DEL_RESPOSTA_ACTION_PARAM)){
					delResponse();
				}else if (sAccio.equalsIgnoreCase(UP_RESPOSTA_PARAM) ||
						  sAccio.equalsIgnoreCase(UP_ITEM_ACTION_PARAM)){
					setResponsePosition(UP_ITEM_ACTION_PARAM);
				} else if (sAccio.equalsIgnoreCase(DOWN_RESPOSTA_PARAM) ||
						   sAccio.equalsIgnoreCase(DOWN_ITEM_ACTION_PARAM)){
					setResponsePosition(DOWN_ITEM_ACTION_PARAM);			
				}
			} 
		} catch (Exception e){
			logger.error("EXCEPCIO inicialitzant QVPreguntaBean ('"+mainBean.getUserId()+"') --> "+e);
			e.printStackTrace();
			bOk = false;
		}
		return bOk;
	}
	
//	***************************	
//	* Actualitzacio/accio 
//	***************************
	protected void addResponse(){
		String sIdentResp = getParameter(IDENT_RESPONSE_LID_PARAM);
		String sIdentRespLabel = mainBean.getParameter(IDENT_RESPOSTA_PARAM);
		int iPosition = 0;
		if (sIdentResp!=null){
			QTISuperResponse oResponse = QTIUtil.getResponse(mainBean.getPregunta(), sIdentResp);
			if (sIdentResp!=null && sIdentRespLabel!=null && sIdentRespLabel.trim().length()>0){
				iPosition = oResponse.getRender().getResponseLabelPosition(sIdentRespLabel)+1;
			}else{
				iPosition = oResponse.getRender().getResponseLabel().size();
			}
			ResponseLabel oNewResponseLabel = new ResponseLabel(QTIUtil.getRandomIdent());
			oResponse.getRender().addResponseLabel(oNewResponseLabel, iPosition);
			sCurrentResponse=oNewResponseLabel.getIdent();
		}		
	}
	
	protected void delResponse(){
		String sIdentResp = mainBean.getParameter(IDENT_RESPOSTA_PARAM);
		if (sIdentResp!=null){
			QTIUtil.delResponse(mainBean.getPregunta(), sIdentResp);
		}
	}
	
	protected boolean isQVPreguntaBean(){
		return this.getClass().getName().lastIndexOf("QVPreguntaBean")>=0;
	}

	protected Item getCurrentItem(){
		if (!isQVPreguntaBean()){
			logger.error("Method getCurrentItem() must be defined at '"+this.getClass().getName()+"'");
		}
		return null;
	}

	public String getItemOrder(String ident){//Albert
		//System.out.println("Invocació a getItemOrder");
		Item currentItem = getCurrentItem();
		String sItemOrder = QTIUtil.getItemRandomOrder(mainBean.getFull(), ident);
		if (sItemOrder == null){
			sItemOrder ="no_random";
		}
		return sItemOrder;
	}
	
	protected void updateCurrentItem(){
		if (getCurrentItem()!=null && mainBean.getIdPregunta()!=null && mainBean.getIdPregunta().equals(getCurrentItem().getIdent()) && !mainBean.showEditXML() && (getParameter("action")==null || !getParameter("action").equalsIgnoreCase("editXML"))){
			mainBean.getFull().setItem(getCurrentItem());
			mainBean.setPregunta(getCurrentItem().getIdent());					
		}
	}
	
	protected void updateFeedback(){
		Respcondition orcOK = oItem.getRespcondition("CORRECT", "True");
		if (orcOK==null){
			orcOK = oItem.addRespcondition("CORRECT", "True");
		}
		Respcondition orcKO = oItem.getRespcondition("CORRECT", "False");
		if (orcKO==null){
			orcKO = oItem.addRespcondition("CORRECT", "False");
		}
		String sFeedbackOK = getParameter(FEEDBACK_OK_PARAM);
		if (sFeedbackOK!=null)	QTIUtil.setFeedback(oItem, orcOK, sFeedbackOK);
		String sFeedbackKO = getParameter(FEEDBACK_KO_PARAM);
		if (sFeedbackKO!=null) QTIUtil.setFeedback(oItem, orcKO, sFeedbackKO);		
	}

	protected void updatePuntuation(){
		Respcondition orcOK = oItem.getRespcondition("CORRECT", "True");
		Respcondition orcKO = oItem.getRespcondition("CORRECT", "False");

		String sPuntuationOK = getParameter(PUNTUATION_OK_PARAM);
		if (sPuntuationOK!=null) QTIUtil.setPuntuation(oItem, orcOK, sPuntuationOK);
		String sPuntuationKO = getParameter(PUNTUATION_KO_PARAM);
		if (sPuntuationKO!=null) QTIUtil.setPuntuation(oItem, orcKO, sPuntuationKO);
	}

	protected void addItem(){
		// S'ha de crear la pregunta i afegir-la al full actual (o l'ultim si no hi ha cap)
		//if (this.getClass().getName().lastIndexOf("QVPreguntaBean")>=0){
		String sType = getParameter(TIPUS_PREGUNTA_PARAM);
		if (getCurrentItem()==null){
			//logger.debug("addItem-> setPregunta(null)");
			mainBean.setPregunta(null);
			if (mainBean.getIdFull()==null){
				Vector vFulls = QTIUtil.getFullsQuadern(mainBean.getQuadern());
				if (vFulls!=null && !vFulls.isEmpty()){
					Section oFull = (Section)vFulls.lastElement();
					mainBean.setFull(oFull.getIdent());
				}else{
					String sIdentFull = QTIUtil.getRandomIdent();
					Section oNouFull = QTIUtil.createSection(sIdentFull, "", QTIObject.SUM_OF_SCORES);
					QTIUtil.addFullQuadern(mainBean.getQuadern(), oNouFull);
					mainBean.setFull(sIdentFull);					
				}
			}
		}else{ 
			//logger.debug("addItem-> afegint pregunta-> "+getCurrentItem());
			mainBean.getFull().addItem(getCurrentItem());
			mainBean.setPregunta(getCurrentItem().getIdent());
		}
	}
	
	protected boolean setItem(){
		String sIdPregunta = mainBean.getSessionParameter(IDENT_PREGUNTA_PARAM);
		return mainBean.setPregunta(sIdPregunta);		
	}
	
	protected boolean delItem(){
		boolean bOk = true;
		String sIdent = getParameter(IDENT_PREGUNTA_PARAM);
		if (sIdent==null || sIdent.trim().length()==0){
			sIdent = mainBean.getIdPregunta();
		}
		QTIUtil.delPregunta(mainBean.getFull(), sIdent);
		return bOk;
	}
	
	/**
	 * @param sAction up o down
	 */
	protected void setItemPosition(String sAction){
		Assessment oQuadern = mainBean.getQuadern();
		Section oFull = mainBean.getFull();
		Item oPregunta = mainBean.getPregunta();
		int iOldPosition = oFull.getItemPosition(mainBean.getIdPregunta());
		int iNewPosition = iOldPosition+1;
		if (sAction!=null && sAction.equals(UP_ACTION_PARAM)){
			iNewPosition = iOldPosition-1;
		}
		if (iNewPosition>=0 && iNewPosition<mainBean.getFull().getItems().size()){
			// Modifiquem la posicio de la pregunta dins del mateix full
			oFull.setItemPosition(oPregunta, iOldPosition, iNewPosition);
		}else{
			// Canviem la pregunta de full
			int iFullIndex = oQuadern.getSectionPosition(mainBean.getFull().getIdent());
			int iNewFullIndex = iFullIndex+1;
			if (sAction!=null && sAction.equals(UP_ACTION_PARAM)){
				iNewFullIndex = iFullIndex - 1;
				iNewPosition = -1;
			}
			Section oNewFull = oQuadern.getSection(iNewFullIndex);
			if (oNewFull!=null){
				if (sAction!=null && sAction.equals(UP_ACTION_PARAM)){
					iNewPosition = oNewFull.getItems().size();
				}else{
					iNewPosition = 0;
				}
				oFull.delItem(oPregunta);
				oNewFull.addItem(oPregunta, iNewPosition);
				mainBean.setPregunta(oPregunta.getIdent());
			}
		}
	}

	protected void redirect(){
		String sJSP = null;
		if (getItemType().equals(SELECTION_TIPUS_PREGUNTA)){
			sJSP = JSP_PREGUNTA_SELECCIO;
		} else if (getItemType().equals(CLOZE_TIPUS_PREGUNTA)){
			sJSP = JSP_PREGUNTA_CLOZE;
		} else if (getItemType().equals(ORDERED_TIPUS_PREGUNTA)){
			sJSP = JSP_PREGUNTA_ORDENACIO;
		} else if (getItemType().equals(HOTSPOT_TIPUS_PREGUNTA)){
			sJSP = JSP_PREGUNTA_HOTSPOT;
		} else if (getItemType().equals(DRAGDROP_TIPUS_PREGUNTA)){
			sJSP = JSP_PREGUNTA_DRAGDROP;
		} else if (getItemType().equals(DRAW_TIPUS_PREGUNTA)){
			sJSP = JSP_PREGUNTA_DRAW;
		}
		if (sJSP!=null){
			String sURL = mainBean.getTranslatedPath(sJSP);
			String sAction = getSessionParameter(ACTION_PARAM);
			boolean bQueryString = sAction!=null&&sAction.equals(ADD_PREGUNTA_ACTION_PARAM)&&isQVPreguntaBean();
			//logger.debug("redirect to-> "+sURL+" query?"+bQueryString+" itemtype="+getItemType());
			mainBean.redirectResponse(sURL, bQueryString);
		}
	}
	
	public String getItemType(){
		if (sItemType==null){
			sItemType = getParameter(TIPUS_PREGUNTA_PARAM);
			if (sItemType==null && mainBean.getPregunta()!=null){
				sItemType = QTIUtil.getTipusPregunta(mainBean.getPregunta());
			}
		}
		return sItemType;
	}
		
//	***************************	
//	* Consulta 
//	***************************
	public String getIdentPregunta(){
		String sIdPregunta = mainBean.getIdPregunta();
		if (sIdPregunta == null){
			sIdPregunta = "";
		}
		return sIdPregunta;
	}
	
	public String getIdentResposta(){
		String sIdent = null;
		QTISuperResponse oResp = getResponse();
		if (oResp!=null) sIdent = oResp.getIdent();
		return sIdent;
	}
	
	public String getTitle(){
		String sTitol = "";
		if (mainBean.getPregunta()!=null){
			sTitol = mainBean.getPregunta().getTitle();
		}
		return sTitol;
	}
	
	public String getEnunciat(){
		String sEnunciat = "";
		if (mainBean.getPregunta()!=null){
			Presentation oPresentation = mainBean.getPregunta().getPresentation();
			//sEnunciat = mainBean.HTMLToText(QTIUtil.getText(oPresentation));
			if (oPresentation!=null){
				if (!oPresentation.getAllMaterials().isEmpty()){
					Material oMaterial = (Material)oPresentation.getAllMaterials().firstElement();
					if (!oMaterial.getMats().isEmpty()){
						QTISuperMat oMat = (QTISuperMat)oMaterial.getMats().firstElement();
						sEnunciat = mainBean.HTMLToText(QTIUtil.getText(oMat));
					}					
				}
			}
		}
		return sEnunciat;
	}
	
	/**
	 * 
	 */		
	public boolean showAsFIB(String sIdentResp){
		return false;
	}
	
	public String getOKFeedback(){
		String sFeedback = QTIUtil.getOKFeedback(mainBean.getPregunta());
		if (sFeedback==null){
			sFeedback = "";
		}
		return sFeedback;
	}
	
	public String getKOFeedback(){
		String sFeedback = QTIUtil.getKOFeedback(mainBean.getPregunta());
		if (sFeedback==null){
			sFeedback = "";
		}
		return sFeedback;
	}
	
	public String getOKPuntuation(){
		String sPuntuation = QTIUtil.getOKPuntuation(mainBean.getPregunta());
		try{
			if (sPuntuation!=null){
				Double.valueOf(sPuntuation);
			}else{
				sPuntuation = "1";
			}
		}catch (Exception e){
			sPuntuation = "1";
		}
		return sPuntuation;
	}
	
	public String getKOPuntuation(){
		String sPuntuation = QTIUtil.getKOPuntuation(mainBean.getPregunta());
		try{
			if (sPuntuation!=null){
				Double.valueOf(sPuntuation);
			}else{
				sPuntuation = "0";
			}
		}catch (Exception e){
			sPuntuation = "0";
		}
		return sPuntuation;
	}
	
	public String getHint(){
		String sHint = QTIUtil.getHint(mainBean.getPregunta());
		if (sHint==null){
			sHint = "";
		}
		return sHint;
	}
	
	
	
//	********************************	
//	* Implementacio QVRespostaBean 
//	********************************
	
	/**
	 * @param sIdentResp
	 * @return Vector amb els identificadors dels ResponseLabel que formen part de la resposta indicada
	 */
	public Vector getIdentResponseLabels(String sIdentResp){
		if (hRespostes.get(sIdentResp)==null){
			Vector vRespostes = getIdentResponseLabels(mainBean.getPregunta(), sIdentResp);
			hRespostes.put(sIdentResp, vRespostes);
		}
		return (Vector)hRespostes.get(sIdentResp);
	}
	public static Vector getIdentResponseLabels(Item oItem, String sIdentResp){
		Vector vRespostes = new Vector();
		vRespostes = QTIUtil.getResponseLabels(oItem, sIdentResp);
		if (vRespostes==null){
			vRespostes = new Vector();
		}
		return vRespostes;
	}
	
	public int getNumRespostesLinia(){
		int iNumRespostesLinia = mainBean.getIntParameter(NUM_RESPOSTES_LINIA_PARAM, 1);
		return iNumRespostesLinia;
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.QVRespostaBean#getNumResponses(java.lang.String)
	 */
	public int getNumResponseLabels(String sIdentResp){
		int iDefaultNumRespostes = getNumRespostesLinia();
		if (sIdentResp!=null){
			Vector vRespostes = new Vector();
			if (showAsFIB(sIdentResp)){
				String sIdentRespLabel = getIdentResponseLabel(sIdentResp, 0);
				vRespostes = getTextResponseLabels(sIdentResp, sIdentRespLabel);
			}else{
				vRespostes = getIdentResponseLabels(sIdentResp);
			}
			if (vRespostes!=null && !vRespostes.isEmpty()){
				iDefaultNumRespostes = vRespostes.size();
			}
		}
		int iNumRespostes = iDefaultNumRespostes;
		if (getParameter(IDENT_RESPONSE_LID_PARAM)!=null && getParameter(IDENT_RESPONSE_LID_PARAM).equalsIgnoreCase(sIdentResp)){
			iNumRespostes = mainBean.getIntParameter(NUM_RESPOSTES_PARAM, iDefaultNumRespostes);
		}
		if (iNumRespostes<=0 || iDefaultNumRespostes>iNumRespostes){
			iNumRespostes = iDefaultNumRespostes;
		}else if (iNumRespostes%getNumRespostesLinia()!=0){
			iNumRespostes+=getNumRespostesLinia()-(iNumRespostes%getNumRespostesLinia());
		}
		return iNumRespostes;		
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.QVRespostaBean#getIdentResponseLabel(java.lang.String, int)
	 */
	public String getIdentResponseLabel(String sIdentLID, int iPosition){
		return getIdentResponseLabel(mainBean.getPregunta(), sIdentLID, iPosition);
	}
	public static String getIdentResponseLabel(Item oItem, String sIdentLID, int iPosition){
		String sIdentResp = QTIUtil.getResponseIdent(oItem, sIdentLID, iPosition);
		if (sIdentResp==null){
			sIdentResp = QTIUtil.getRandomIdent();
		}
		return sIdentResp;
	}
	
	protected QTISuperResponse getResponse(){
		QTISuperResponse oResponse = QTIUtil.getResponse(mainBean.getPregunta());
		return oResponse;
	}
	
	protected QTISuperRender getRender(String sIdentResp){
		QTISuperRender oRender = null;
		if (sIdentResp!=null){
			QTISuperResponse oResponse = QTIUtil.getResponse(mainBean.getPregunta(), sIdentResp);
			if (oResponse!=null){
				oRender = oResponse.getRender();
			}
		}
		return oRender;
	}
	
	public String getResponseMaterialType(String sIdentResp){
		String sRespMatType = QTISuperMat.MATTEXT_CONTENT_TYPE;
		QTISuperRender oRender = getRender(sIdentResp);
		if (oRender!=null){
			Enumeration enumRender = oRender.getResponseLabel().elements();
			while (enumRender.hasMoreElements()){
				ResponseLabel oRespLabel = (ResponseLabel)enumRender.nextElement();
				Enumeration enumMaterials = oRespLabel.getMaterials().elements();
				while (enumMaterials.hasMoreElements()){
					Material oMaterial = (Material)enumMaterials.nextElement();
					if (!oMaterial.getMattext().isEmpty()){
						break;
					}else if (!oMaterial.getMatimage().isEmpty()){
						sRespMatType = QTISuperMat.MATIMAGE_CONTENT_TYPE;
					}else if (!oMaterial.getMataudio().isEmpty()){
						sRespMatType = QTISuperMat.MATAUDIO_CONTENT_TYPE;
					}
				}
			}
		}
		return sRespMatType;
	}
	
	/**
	 * @param sIdentResp
	 * @param sIdentRespLabel
	 * @return Vector amb tots els texts de la resposta indicada
	 */
	protected Vector getTextResponseLabels(String sIdentResp, String sIdentRespLabel){
		Vector vTextResp = new Vector();
		try{
			Hashtable hTextLID = new Hashtable();
			if (sIdentResp!=null){
				if (hTextResp.get(sIdentResp)!=null){
					hTextLID = (Hashtable)hTextResp.get(sIdentResp);
				}else{
					hTextResp.put(sIdentResp, hTextLID);
				}
				if (hTextLID.get(sIdentRespLabel)==null){
					vTextResp = getTextResponseLabels(mainBean.getPregunta(), sIdentResp, sIdentRespLabel);
					hTextLID.put(sIdentRespLabel, vTextResp);
				} else{
					vTextResp = (Vector)hTextLID.get(sIdentRespLabel);
				}
			}
		} catch (Exception e){
			logger.error("EXCEPCIO obtenint el text dels response labels per '"+sIdentResp+"'  -> e="+e);
		}
		return vTextResp;
	}
	
	public static Vector getTextResponseLabels(Item oItem, String sIdentResp, String sIdentRespLabel){
		Vector vTextResp = new Vector();
		try{
			QTISuperResponse oResponse = QTIUtil.getResponse(oItem, sIdentResp);
			if (oResponse!=null){
				if (oResponse instanceof ResponseLID){
					ResponseLabel oResponseLabel = null;
					QTISuperRender oRender = oResponse.getRender();
					if (oRender !=null){
						oResponseLabel = oRender.getResponseLabel(sIdentRespLabel); 
						if (oResponseLabel!=null){
							Vector vMattext = QTIUtil.getMattext(oResponseLabel.getMaterials());
							if (vMattext!=null && !vMattext.isEmpty()){
								String sText = ((Mattext)vMattext.firstElement()).getText();
								if (sText==null){
									sText="";
								}else{
									sText=sText.trim();
								}
								vTextResp.addElement(sText);
							}
						}
					}
				}else if (oResponse instanceof ResponseSTR || oResponse instanceof ResponseNUM){
					Vector vResprocessing = oItem.getResprocessing();
					if (vResprocessing!=null && !vResprocessing.isEmpty()){
						Resprocessing oResprocessing = (Resprocessing)vResprocessing.firstElement();
						Respcondition oRespcondition = oResprocessing.getRespcondition("CORRECT","True");
						if (oRespcondition!=null){
							Enumeration enumVarequal = oRespcondition.getConditionvar().getAllVarequals().elements();
							//logger.debug("varequal="+oRespcondition.getConditionvar().getVarequal());
							while (enumVarequal.hasMoreElements()){
								Varequal oVarequal = (Varequal)enumVarequal.nextElement();
								if (sIdentResp.equalsIgnoreCase(oVarequal.getRespident())){
									vTextResp.addElement(oVarequal.getText());
								}
							}
						}
					}
				}
			}
		} catch (Exception e){
			logger.error("EXCEPCIO obtenint el text dels response labels per '"+sIdentResp+"'  -> e="+e);
		}
		return vTextResp;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.QVRespostaBean#getTextResponseLabel(java.lang.String, java.lang.String)
	 */
	public String getTextResponseLabel(String sIdentResp, String sIdentRespLabel, int iPosition){
		String sText = "";
		Vector vTextResp = getTextResponseLabels(sIdentResp, sIdentRespLabel);
		if (vTextResp!=null && !vTextResp.isEmpty()){
			if (showAsFIB(sIdentResp)){
				if (iPosition < vTextResp.size()){
					sText = (String)vTextResp.elementAt(iPosition);
				}
			} else{
				sText = (String)vTextResp.firstElement();
			}
		}
		sText = StringUtil.replace(sText, "\"","'");
		return sText;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.QVRespostaBean#getResourceResponseLabel(java.lang.String, java.lang.String)
	 */
	public String getResourceResponseLabel(String sIdentResp, String sIdentRespLabel){
		String sURI = null;
		if (sIdentResp!=null && sIdentRespLabel!=null){
			Vector vResourceResp = getResourceResponses(sIdentResp, sIdentRespLabel);
			if (vResourceResp!=null && !vResourceResp.isEmpty()){
				sURI = ((QTISuperMat)vResourceResp.firstElement()).getURI();			
			}
		}
		return sURI;
	}

	/**
	 * @param sIdentResp
	 * @param sIdentRespLabel
	 * @return
	 */
	public Vector getResourceResponses(String sIdentResp, String sIdentRespLabel){
		Vector vResourceResp = new Vector();
		Hashtable hResourceLID = new Hashtable();
		if (hResourceResp.get(sIdentResp)!=null){
			hResourceLID = (Hashtable)hResourceResp.get(sIdentResp);
		}else{
			hResourceResp.put(sIdentResp, hResourceLID);
		}
		if (hResourceLID.get(sIdentRespLabel)==null){
			ResponseLabel oResponseLabel = QTIUtil.getResponseLabel(mainBean.getPregunta(), sIdentResp, sIdentRespLabel);
			if (oResponseLabel!=null){
				vResourceResp = QTIUtil.getMats(oResponseLabel.getMaterials());
			}
			hResourceLID.put(sIdentRespLabel, vResourceResp);
		} else{
			vResourceResp = (Vector)hResourceLID.get(sIdentRespLabel);
		}
		return vResourceResp;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.QVRespostaBean#getImageResponseLabel(java.lang.String, java.lang.String)
	 */
	public String getImageResponseLabel(String sIdentResp, String sIdentRespLabel){
		String sURI = null;
		Vector vImageResp = getImageResponses(sIdentResp, sIdentRespLabel);
		if (vImageResp!=null && !vImageResp.isEmpty()){
			sURI = ((Matimage)vImageResp.firstElement()).getURI();			
		}
		return sURI;
	}

	/**
	 * @param sIdentResp
	 * @param sIdentRespLabel
	 * @return
	 */
	public Vector getImageResponses(String sIdentResp, String sIdentRespLabel){
		Vector vImageResp = new Vector();
		Hashtable hImageLID = new Hashtable();
		if (hImageResp.get(sIdentResp)!=null){
			hImageLID = (Hashtable)hImageResp.get(sIdentResp);
		}else{
			hImageResp.put(sIdentResp, hImageLID);
		}
		if (hImageLID.get(sIdentRespLabel)==null){
			ResponseLabel oResponseLabel = QTIUtil.getResponseLabel(mainBean.getPregunta(), sIdentResp, sIdentRespLabel);
			if (oResponseLabel!=null){
				vImageResp = QTIUtil.getMatimage(oResponseLabel.getMaterials());
			}
			hImageLID.put(sIdentRespLabel, vImageResp);
		} else{
			vImageResp = (Vector)hImageLID.get(sIdentRespLabel);
		}
		return vImageResp;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.QVRespostaBean#isCorrectResponseLabel(java.lang.String, java.lang.String)
	 */
	public boolean isCorrectResponseLabel(String sIdentResp, String sIdentRespLabel){
		return isCorrectResponseLabel(mainBean.getPregunta(), sIdentResp, sIdentRespLabel);
	}
	public static boolean isCorrectResponseLabel(Item oItem, String sIdentResp, String sIdentRespLabel){
		boolean bIsCorrect = false;
		bIsCorrect = getCorrectResponses(oItem, sIdentResp).contains(sIdentRespLabel); 
		return bIsCorrect;
	}
	
	/**
	 * TODO: revisar-> falta tenir en compte el cas que sigui FIB; en aquest cas es retornara Vector amb les respostes, enlloc de amb els identificadors
	 * @param sIdentResp
	 * @return
	 */
	public Vector getCorrectResponses(String sIdentResp){
		return getCorrectResponses(mainBean.getPregunta(), sIdentResp);
	}
	public static Vector getCorrectResponses(Item oItem, String sIdentResp){
		Vector vResponses = QTIUtil.getOKResponses(oItem, sIdentResp);
		return vResponses;
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.QVRespostaBean#getCurrentIdentResponseLabel()
	 */
	public String getCurrentIdentResponseLabel() {
		if (sCurrentResponse==null){
			sCurrentResponse = mainBean.getParameter(IDENT_RESPOSTA_PARAM, null);
		}
		return sCurrentResponse;
	}

	/**
	 * @param sAction up_item o down_item
	 */
	protected void setResponsePosition(String sAction){
		String sIdentResp = getParameter(IDENT_RESPONSE_LID_PARAM);
		String sIdentRespLabel = mainBean.getParameter(IDENT_RESPOSTA_PARAM);
		if (sIdentResp!=null){
			QTISuperResponse oResponse = QTIUtil.getResponse(mainBean.getPregunta(), sIdentResp);
			if (oResponse!=null && oResponse.getRender()!=null){
				int iOldPosition = oResponse.getRender().getResponseLabelPosition(sIdentRespLabel);
				if (iOldPosition>=0){
					int iNewPosition = iOldPosition+1;
					if (sAction!=null && sAction.equals(UP_ITEM_ACTION_PARAM)){
						iNewPosition = iOldPosition-1;
					}
					if (iNewPosition>=0 && iNewPosition<oResponse.getRender().getResponseLabel().size()){
						oResponse.getRender().setResponseLabelPosition(iOldPosition, iNewPosition);
					}
				}
			}
		}
	}
		
//	************************************	
//	* Implementacio IQVListControlBean
//	************************************
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.QVSpecificBean#getBeanObject()
	 */
	public QTIObject getBeanObject(){
		return mainBean.getPregunta();
	}

	public boolean needXMLValidation(){
		return !isQVPreguntaBean();
	}

//	************************************	
//	* Implementacio IQVInteractionBean
//	************************************

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.IQVInteractionBean#isInteractionswitch()
	 */
	public boolean isInteractionswitch() {
		return QTIUtil.isInteractionswitch(mainBean.getQuadern(), mainBean.getFull(), mainBean.getPregunta());
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.IQVInteractionBean#getInteractionText()
	 */
	public String getInteractionText() {
		return "item.interactionswitch";
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.IQVInteractionBean#getInteractionTip()
	 */
	public String getInteractionTip() {
		return "item.interactionswitch.tip";
	}

}
