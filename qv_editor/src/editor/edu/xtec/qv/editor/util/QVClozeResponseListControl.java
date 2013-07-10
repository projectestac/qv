/*
 * QVClozeResponseListControl.java
 * 
 * Created on 27/juliol/2004
 */
package edu.xtec.qv.editor.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import edu.xtec.qv.editor.beans.QVBean;
import edu.xtec.qv.editor.beans.QVPreguntaClozeBean;
import edu.xtec.qv.qti.Conditionvar;
import edu.xtec.qv.qti.Flow;
import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.Material;
import edu.xtec.qv.qti.Matimage;
import edu.xtec.qv.qti.Mattext;
import edu.xtec.qv.qti.Presentation;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.QTISuperMat;
import edu.xtec.qv.qti.QTISuperResponse;
import edu.xtec.qv.qti.RenderFIB;
import edu.xtec.qv.qti.Respcondition;
import edu.xtec.qv.qti.ResponseLID;
import edu.xtec.qv.qti.ResponseLabel;

/**
 * @author sarjona
 */
public class QVClozeResponseListControl extends QVListControl{
	
	public QVClozeResponseListControl(HttpServletRequest request){
		super(request);
	}

	// Parameter
	public static final String P_NUMBER_RESPONSES = "num_respostes";
	public static final String P_RESPONSE_LID_IDENT = "ident_resp_lid";
	public static final String P_RESPONSE_LABEL_IDENT = "p_response_label_ident";
	public static final String P_RESPONSE_LABEL_TEXT ="p_response_label_text";
	public static final String P_RESPONSE_OPTION_TYPE = "response_option_type";
	public static final String P_CORRECT_RESPONSES = "respostes_correctes";
	
	public static final String P_ORIENTATION = "orientation";
	public static final String P_SHUFFLE = "p_shuffle";
	public static final String P_RESPONSE_MATERIAL_TYPE = "response_material_type";
	public static final String P_FEEDBACK_OK = "feedback_ok";
	public static final String P_FEEDBACK_KO = "feedback_ko";
	public static final String P_PUNTUATION_OK = "puntuation_ok";
	public static final String P_PUNTUATION_KO = "puntuation_ko";
	public static final String P_ROWS = "rows"; 
	public static final String P_COLUMNS = "columns"; 
	
	public static final String CLOSE_OPTION_TYPE = "close"; 
	public static final String OPEN_OPTION_TYPE = "open"; 
	
	public static String A_UP = "up";
	public static String A_DOWN = "down";
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getListObjects(edu.xtec.qv.qti.QTIObject)
	 */
	public Vector getListObjects(QTIObject oQTI){
		Vector vListObjects = new Vector();
		Vector vResponses = getClozeResponses(oQTI);
		
		Enumeration enumResponses = vResponses.elements();
		while (enumResponses.hasMoreElements()){
			QTISuperResponse oResponse = (QTISuperResponse)enumResponses.nextElement();			
			ListObject o = new ListObject(oResponse.getIdent(), getClozeResponseText((Item)oQTI, oResponse));
			if (!vListObjects.contains(o)){
				vListObjects.addElement(o);
			}
		}
		return vListObjects;
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getObject(QTIObject oQTI, String sIdent){
		return getClozeResponse(oQTI, sIdent);
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
	}
	
	public void delListObject(QTIObject oQTI, String sIdent){
	}
	
	protected void setListObject(QTIObject oQTI, String sIdent){
		setListObject(request, oQTI, sIdent);
	}
	
	public void upListObject(QTIObject oQTI, String sIdent){
	}
	
	public void downListObject(QTIObject oQTI, String sIdent){
	}

	protected static void setListObject(HttpServletRequest request, QTIObject oQTI, String sIdent){
		QTISuperResponse oNew = (QTISuperResponse)getRequestQTIObject(request, sIdent);
		//logger.debug("setListObject-> "+(oNew!=null?oNew.getClass():null));
		if (oQTI!=null){
			if (oNew!=null){
				String[] sResponseLabelIdent = request.getParameterValues(P_RESPONSE_LABEL_IDENT+"_"+oNew.getIdent());
				if (sResponseLabelIdent!=null){
					Presentation oPresentation = ((Item)oQTI).getPresentation();
					if (oPresentation==null){
						oPresentation = new Presentation();
						((Item)oQTI).setPresentation(oPresentation);
					}
					if (oPresentation.getFlow()==null){
						oPresentation.setFlow(new Flow());
					}
					oPresentation.getFlow().addResponse(oNew);
				}
			}
			updateConditions(request, (Item)oQTI, oNew);
		}
	}
	
	private static void updateConditions(HttpServletRequest request, Item oItem, QTISuperResponse oResponse){
		if (oItem!=null){
			Vector vConditions = getAllConditions(request, oItem, oResponse);

			String sFeedbackOK = request.getParameter(P_FEEDBACK_OK);
			String sPuntuationOK = request.getParameter(P_PUNTUATION_OK);
			QTIUtil.addCorrectResponseCondition(oItem, vConditions, sFeedbackOK, sPuntuationOK);
			
			String sFeedbackKO = request.getParameter(P_FEEDBACK_KO);
			String sPuntuationKO = request.getParameter(P_PUNTUATION_KO);
			QTIUtil.addIncorrectResponseCondition(oItem, vConditions, sFeedbackKO, sPuntuationKO);
		}
	}
	
	private static Vector getAllConditions(HttpServletRequest request, Item oItem, QTISuperResponse oResponse){
		Vector vRespConditions = new Vector();		
		Vector vAnd = new Vector();
		if (oResponse!=null){
			vRespConditions = getRequestResponseConditions(request, oItem, oResponse);
			Enumeration enumConditions = vRespConditions.elements();
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
			vRespConditions.addElement(hAnd);
		}
		Vector vConditions = new Vector();
		Respcondition oRespcondition = oItem.getRespcondition("CORRECT","True");
		if (oRespcondition!=null){
			Conditionvar oConditionvar = oRespcondition.getConditionvar();
			Vector vIdentResp = getIdentResponses(oItem);
			if (oResponse!=null){
				vIdentResp.remove(oResponse.getIdent());
			}
			vConditions = QTIUtil.conditionvarToVector(oConditionvar, vIdentResp);
			//logger.debug("add-> old="+vConditions+"  new="+vRespConditions);
			addResponseCondition(vConditions, vRespConditions);
			//logger.debug("conditions-> "+vConditions);
		}
		return vConditions;
	}
	
	private static void addResponseCondition(Vector vConditions, Vector vNewConditions){
		if (vConditions!=null && vNewConditions!=null){
			if (vConditions.isEmpty()){
				vConditions.addAll(vNewConditions);
			}else{
				try{
					Hashtable hAnd = (Hashtable)vConditions.firstElement();
					Vector vAnd = (Vector)hAnd.get(QTIObject.AND.getAttributeName());
					if (vAnd!=null){
						Hashtable hNewAnd = (Hashtable)vNewConditions.firstElement();
						Vector vNewAnd = (Vector)hNewAnd.get(QTIObject.AND.getAttributeName());
						Enumeration enumNew = vNewAnd.elements();
						while(enumNew.hasMoreElements()){						
							vAnd.addElement(enumNew.nextElement());
						}
					}
				}catch(Exception e){
				}
/*
				Enumeration enumNewConditions = vNewConditions.elements();
				while (enumNewConditions.hasMoreElements()){				
					Hashtable hNew = (Hashtable)enumNewConditions.nextElement();
					Enumeration enumNewHash = hNew.keys();
					while (enumNewHash.hasMoreElements()){
						String sKey = (String)enumNewHash.nextElement();
						Enumeration enumConditions = vConditions.elements();
						while (enumConditions.hasMoreElements()){				
							Hashtable h = (Hashtable)enumConditions.nextElement();
							if (h.containsKey(sKey)){
								if (sKey.equals("and") || sKey.equals("or") || sKey.equals("not")){
									Object oValue = h.get(sKey);
									Object oNewValue = hNew.get(sKey);
									if (oValue instanceof Vector && oNewValue instanceof Vector){
										addResponseCondition((Vector)oValue, (Vector)oNewValue);														
									}else{
										vConditions.addElement(oNewValue);
										break;
									}
								}
							}else{
								vConditions.addElement(hNew);
								break;
							}
						}
					}
				}
*/				
			}			
		}
	}
	
	private static void delResponseCondition(String sIdentResponse, Vector vConditions){
		if (vConditions!=null && sIdentResponse!=null){
			Vector vHashtToDelete = new Vector();
			Enumeration enumConditions = vConditions.elements();
			while (enumConditions.hasMoreElements()){
				Hashtable h = (Hashtable)enumConditions.nextElement();
				Enumeration enumHash = h.keys();
				while (enumHash.hasMoreElements()){
					String sKey = (String)enumHash.nextElement();
					//logger.debug("key="+sKey);
					if (sKey.equals("and") || sKey.equals("or") || sKey.equals("not")){
						Vector vValue = (Vector)h.get(sKey);
						delResponseCondition(sIdentResponse, vValue);
						if (vValue.isEmpty()){
							h.remove(sKey);
						}
					}else{
						if (sKey.equals(sIdentResponse)){
							h.remove(sKey);
						}
					}
					//logger.debug("h="+h+"  conditions="+vConditions);
					if (h.isEmpty()){
						vHashtToDelete.addElement(h);
					}
				}
			}
			if (!vHashtToDelete.isEmpty()){
				Enumeration enum1 = vHashtToDelete.elements();
				while (enum1.hasMoreElements()){
					Hashtable h = (Hashtable)enum1.nextElement();
					vConditions.remove(h);
				}
			}
		}
	}
	
	private static Vector getIdentResponses(QTIObject oQTI){
		Vector vIdentResponses = new Vector();
		Vector vResponses = getClozeResponses(oQTI);
		Enumeration enumResponses = vResponses.elements();
		while (enumResponses.hasMoreElements()){
			QTISuperResponse oResponse = (QTISuperResponse)enumResponses.nextElement();
			String sIdentResp = oResponse.getIdent();
			if (!vIdentResponses.contains(sIdentResp)){
				vIdentResponses.addElement(sIdentResp);
			}
		}
		return vIdentResponses;
	}
	
	private static Vector getClozeResponses(QTIObject oQTI){
		Vector vResponses = new Vector();
		if (oQTI!=null){
			if (oQTI instanceof Item){
				Presentation oPresentation = ((Item)oQTI).getPresentation();
				if (oPresentation!=null){
					vResponses = oPresentation.getResponses();
				}
			}
		}
		return vResponses;
	}

	private QTISuperResponse getClozeResponse(QTIObject oQTI, String sIdent){
		QTISuperResponse oResponse = null;
		try{
			if (sIdent!=null){
				Enumeration enumResponses = getClozeResponses(oQTI).elements();
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
	
	private String getClozeResponseText(Item oItem, QTISuperResponse oResponse){
		String sText = "";
		if (oResponse!=null){
			try{
				sText = "["+oResponse.getIdent()+"] ";
				boolean bIsFIB = QVPreguntaClozeBean.showAsFIB(oItem, oResponse.getIdent());
				sText+=bIsFIB?"":"{ ";
				Vector vRespostes = new Vector();
				if (bIsFIB){
					String sIdentRespLabel = QVPreguntaClozeBean.getIdentResponseLabel(oItem, oResponse.getIdent(), 0);
					vRespostes = QVPreguntaClozeBean.getTextResponseLabels(oItem, oResponse.getIdent(), sIdentRespLabel);
				}else{
					vRespostes = QVPreguntaClozeBean.getIdentResponseLabels(oItem, oResponse.getIdent());
				}
				
				Iterator itRespLabel = vRespostes.iterator();
				while (itRespLabel.hasNext()){
					Object oRespLabel = itRespLabel.next();
					if (oRespLabel instanceof ResponseLabel){
						String sRespLabelId = ((ResponseLabel)oRespLabel).getIdent();
						sText+=getResponseLabelText2Print((ResponseLabel)oRespLabel);
						if (QVPreguntaClozeBean.isCorrectResponseLabel(oItem, oResponse.getIdent(), sRespLabelId)){
							sText+="(*)";
						}
					}else{
						sText+= oRespLabel;
					}
					if (itRespLabel.hasNext()){
						sText += " | ";
					}
				}
				sText+=bIsFIB?"":" } ";

				
/*				Enumeration enumRespLabel = oResponse.getRender().getResponseLabel().elements();
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
								sText += oMat.getURI();
							}
							if (enumRespLabel.hasMoreElements())
								sText += " / ";
						}
					}
				}
*/				
			}catch (Exception e){
				logger.debug("EXCEPCIO obtenint text per les respostes d'omplir espais -> "+e);
			}
		}
		return sText;
	}
	
	protected static String getResponseLabelText2Print(ResponseLabel oRespLabel){
		String sText = "";
		Vector vMaterials = oRespLabel.getMaterials();
		if (vMaterials!=null && !vMaterials.isEmpty()){
			Material oMaterial = (Material)vMaterials.firstElement();
			Vector vMats = oMaterial.getMats();
			if (vMats!=null && !vMats.isEmpty()){
				QTISuperMat oMat = (QTISuperMat)vMats.firstElement();
				if (oMat instanceof Mattext){
					sText += oMat.getText();
				}else if (oMat instanceof Matimage){
					sText += oMat.getURI();
				}
			}
		}
		return sText;
	}
	
	public QTIObject getRequestQTIObject(){
		return getRequestQTIObject(null);
	}
	
	public QTIObject getRequestQTIObject(String sLIDIdent){
		return getRequestQTIObject(request, sLIDIdent);
	}
	
	public static QTIObject getRequestQTIObject(HttpServletRequest request, String sLIDIdent){
		QTISuperResponse oResponse = null;
		int iNumberResponses = getIntParameter(request, P_NUMBER_RESPONSES, -1);
		if (sLIDIdent==null){
			sLIDIdent = request.getParameter(P_RESPONSE_LID_IDENT);
		}
		if (sLIDIdent!=null){
			String sResponseOptionType = request.getParameter(P_RESPONSE_OPTION_TYPE);
			if (sResponseOptionType==null){
				Object o = request.getSession().getAttribute(P_RESPONSE_OPTION_TYPE);
				sResponseOptionType=o!=null?(String)o:CLOSE_OPTION_TYPE;
			}
			String[] sResponseLabelIdent = request.getParameterValues(P_RESPONSE_LABEL_IDENT+"_"+sLIDIdent);
			String[] sResponseLabelText = request.getParameterValues(P_RESPONSE_LABEL_TEXT+"_"+sLIDIdent);

			if (sResponseLabelIdent==null && iNumberResponses>0){
				// S'ha d'iniciatlitzar amb el nombre de respostes indicat
				sResponseLabelIdent = new String[iNumberResponses];
				sResponseLabelText = new String[iNumberResponses];
				for (int i=0;i<iNumberResponses;i++){
					sResponseLabelIdent[i] = QTIUtil.getRandomIdent();
					sResponseLabelText[i]="";
				}
			}
			if (sResponseLabelIdent!=null){
				if (sResponseOptionType.equals(OPEN_OPTION_TYPE)){
					oResponse = QTIUtil.createResponseSTR(sLIDIdent, "Single");
					int iRows = getIntParameter(request, P_ROWS+"_"+sLIDIdent, -1); 
					int iColumns = getIntParameter(request, P_COLUMNS+"_"+sLIDIdent, -1);
					RenderFIB oRender = (RenderFIB)oResponse.getRender();
					if (iRows>0) oRender.setRows(String.valueOf(iRows));
					if (iColumns>0) oRender.setColumns(String.valueOf(iColumns));
				}else{
					oResponse = QTIUtil.createResponseLID(sLIDIdent, "Single", null, "LIST", sResponseLabelIdent, sResponseLabelText, null);
				}
			}
		}
		//logger.debug("getRequestClozeResponse-> "+oResponse);
		return oResponse;
	}

	private static Vector getRequestResponseConditions(HttpServletRequest request, Item oItem, QTISuperResponse oResponse){
		Vector vConditions = new Vector();
		String[] sResponseLabelIdent = request.getParameterValues(P_RESPONSE_LABEL_IDENT+"_"+oResponse.getIdent());
		if (oResponse instanceof ResponseLID){
			String[] sCorrectResponses = request.getParameterValues(P_CORRECT_RESPONSES+"_"+oResponse.getIdent());
			Hashtable hCorrectResponses = new Hashtable();
			Hashtable hAllResponses = new Hashtable();
			hCorrectResponses.put(oResponse.getIdent(), QTIUtil.arrayToVector(sCorrectResponses));
			hAllResponses.put(oResponse.getIdent(), QTIUtil.arrayToVector(sResponseLabelIdent));
			vConditions = QTIUtil.getSelectionConditions(oItem, hAllResponses, hCorrectResponses);
		} else{
			String[] sResponseLabelText = request.getParameterValues(P_RESPONSE_LABEL_TEXT+"_"+oResponse.getIdent());
			if (sResponseLabelIdent!=null && sResponseLabelText!=null){
				String sAction = request.getParameter("action");
				if (sAction!=null && sAction.equalsIgnoreCase("del_resposta")){
					String sIdentRespLabel = request.getParameter("ident_resposta");
					if (sIdentRespLabel!=null){
						int iIndex = -1;
						for (int i=0;i<sResponseLabelIdent.length;i++){
							if (sIdentRespLabel.equals(sResponseLabelIdent[i])){
								iIndex = i;
								break;
							}
						}
						if (iIndex>=0 && iIndex<sResponseLabelText.length){
							sResponseLabelText[iIndex]="";
						}
					}
				}
			}
			Hashtable hCorrectResponses = new Hashtable();
			hCorrectResponses.put(oResponse.getIdent(), QTIUtil.arrayToVector(sResponseLabelText));
			vConditions = QTIUtil.getFIBConditions(oItem, hCorrectResponses);
		}
		return vConditions;
	}

	public static QTISuperResponse getRequestResponse(HttpServletRequest request){
		QTISuperResponse oResponse = null;
		QTIObject oQTI = getRequestQTIObject(request, null);
		if (oQTI!=null){
			oResponse = (QTISuperResponse)oQTI;
		}
		return oResponse;
	}

	public static void updateResponse(HttpServletRequest request, Item oItem){
		setListObject(request, oItem, null);
	}
	
	
}
