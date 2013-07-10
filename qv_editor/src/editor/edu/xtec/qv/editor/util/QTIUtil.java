/*
 * QTIUtil.java
 * 
 * Created on 04/febrer/2004
 */
package edu.xtec.qv.editor.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.jdom.Document;

import edu.xtec.qv.qti.And;
import edu.xtec.qv.qti.Assessment;
import edu.xtec.qv.qti.AssessmentControl;
import edu.xtec.qv.qti.Conditionvar;
import edu.xtec.qv.qti.Decvar;
import edu.xtec.qv.qti.Displayfeedback;
import edu.xtec.qv.qti.Flow;
import edu.xtec.qv.qti.FlowMat;
import edu.xtec.qv.qti.IMSRenderObject;
import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.ItemControl;
import edu.xtec.qv.qti.Itemfeedback;
import edu.xtec.qv.qti.MapOutput;
import edu.xtec.qv.qti.Mataudio;
import edu.xtec.qv.qti.Material;
import edu.xtec.qv.qti.Matimage;
import edu.xtec.qv.qti.Mattext;
import edu.xtec.qv.qti.Not;
import edu.xtec.qv.qti.Or;
import edu.xtec.qv.qti.Outcomes;
import edu.xtec.qv.qti.OutcomesProcessing;
import edu.xtec.qv.qti.Presentation;
import edu.xtec.qv.qti.QTIBooleanOperator;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.QTISuperControl;
import edu.xtec.qv.qti.QTISuperMat;
import edu.xtec.qv.qti.QTISuperRender;
import edu.xtec.qv.qti.QTISuperResponse;
import edu.xtec.qv.qti.QuestestInterop;
import edu.xtec.qv.qti.RenderChoice;
import edu.xtec.qv.qti.RenderDraw;
import edu.xtec.qv.qti.RenderFIB;
import edu.xtec.qv.qti.RenderHotspot;
import edu.xtec.qv.qti.Respcondition;
import edu.xtec.qv.qti.ResponseGRP;
import edu.xtec.qv.qti.ResponseLID;
import edu.xtec.qv.qti.ResponseLabel;
import edu.xtec.qv.qti.ResponseNUM;
import edu.xtec.qv.qti.ResponseSTR;
import edu.xtec.qv.qti.ResponseXY;
import edu.xtec.qv.qti.Resprocessing;
import edu.xtec.qv.qti.Section;
import edu.xtec.qv.qti.SectionControl;
import edu.xtec.qv.qti.Setvar;
import edu.xtec.qv.qti.Varequal;
import edu.xtec.qv.qti.util.StringUtil;
import edu.xtec.qv.util.XMLUtil;

import edu.xtec.qv.qti.SelectionOrdering;
import edu.xtec.qv.qti.Selection;
import edu.xtec.qv.qti.OrSelection;
import edu.xtec.qv.qti.SelectionMetadata;

/**
 * @author sarjona
 */
/**
 * @author sarjona
 */
public class QTIUtil {

	private static Logger logger = Logger.getRootLogger();

//	***************************	
//	* Quadern
//	***************************
	/**
	 * @param oQuestesInterop 
	 * @return Assessment
	 */
	public static Assessment getQuadern(QTIObject oQuestesInterop){
		Assessment oQTI = null;
		if (oQuestesInterop!=null && oQuestesInterop instanceof QuestestInterop){
			oQTI = ((QuestestInterop)oQuestesInterop).getAssessment();
		}
		return oQTI;
	}
	
	public static boolean saveQuadern(String sURL, QTIObject oQTI){
		boolean bOk = false;
		if (oQTI!=null){
			bOk = XMLUtil.save(sURL, oQTI.getXML());
		}
		return bOk;
	}

//	***************************	
//	* Full
//	***************************
	/**
	 * @param oQTI QTIQuestesInterop o Assessment del que es vol obtenir els fulls
	 * @return Vector de Section
	 */
	public static Vector getFullsQuadern(QTIObject oQTI){
		Vector vFulls = new Vector();
		if (oQTI!=null){
			QTIObject oAssessment = null;
			if (oQTI instanceof QuestestInterop){
				oAssessment = getQuadern(oQTI);
			}else if (oQTI instanceof Assessment){
				oAssessment = oQTI;
			}
			if (oAssessment!=null){
				vFulls = ((Assessment)oAssessment).getSections();
			}
		}
		return vFulls;
	}
	
	/**
	 * @param oQuestesInterop 
	 * @param sIdQuadern identificador del quadern
	 * @param sIdFull identificador del full
	 * @return Section
	 */
	public static Section getFull(QTIObject oQuestesInterop, String sIdFull){
		Section oQTI = null;
		QTIObject oQuadern = getQuadern(oQuestesInterop);
		if (oQuadern!=null){
			oQTI = ((Assessment)oQuadern).getSection(sIdFull);
		}
		return oQTI;
	}
	
	/**
	 * @param oAssessment quadern
	 * @param sIdPregunta identificador de la pregunta, pertanyent al quadern que es retornarà
	 * @return Section que conté la pregunta indicada
	 */
	public static Section getFullPregunta(Assessment oAssessment, String sIdPregunta){
		Section oQTI = null;
		if (oAssessment !=null && sIdPregunta!=null){
			Enumeration enumItems = oAssessment.getSections().elements();
			while (enumItems.hasMoreElements()){
				Section oSection = (Section)enumItems.nextElement();
				if (oSection.containsItem(sIdPregunta)){
					oQTI = oSection;
					break;
				}
			}
		}
		return oQTI;
	}
	
	/**
	 * Afegeix un nou full al quadern especificat
	 * @param oAssessment quadern
	 * @param oSection nou full
	 */
	public static void addFullQuadern(QTIObject oAssessment, QTIObject oSection){
		if (oAssessment!=null && oSection!=null){
			((Assessment)oAssessment).addSection((Section)oSection);
		}
	}
	
	/**
	 * Elimina el full indicat al quadern
	 * @param oAssessment quadern
	 * @param oSection String amb l'identificador del full a eliminar o Section
	 * amb el full a eliminar
	 */
	public static void delFull(QTIObject oAssessment, Object oSection){
		if (oAssessment!=null && oSection!=null){
			Section s = null;
			if (oSection instanceof Section){
				s = (Section)oSection;
			}else{
				s = ((Assessment)oAssessment).getSection((String)oSection);
			}
			if (s!=null){
				((Assessment)oAssessment).delSection(s);
			}
		}
	}
	
	public static Section createSection(String sIdent, String sTitle, String sScoreModel){
		Section oSection = new Section(sIdent, sTitle);
		oSection.addOutcomesProcessing(createOutcomesProcessing(sScoreModel));
		return oSection;
	}
	
	public static OutcomesProcessing createOutcomesProcessing(String sScoreModel){
		OutcomesProcessing oOutcomesProcessing = null;
		if (sScoreModel!=null){
			oOutcomesProcessing = new OutcomesProcessing(sScoreModel);
			Outcomes oOutcomes = new Outcomes();
			oOutcomes.addDecvar(new Decvar("SCORE", "Decimal", "0"));
			oOutcomesProcessing.setOutcomes(oOutcomes);				
			MapOutput oMapOutput = null;
			if (sScoreModel.equalsIgnoreCase(QTIObject.SUM_OF_SCORES)){
				oMapOutput = new MapOutput("SCORE", "SCORE");
			}else if (sScoreModel.equalsIgnoreCase(QTIObject.NUMBER_CORRECT)){
				oMapOutput = new MapOutput("COUNT", "SCORE");
			}
			if (oMapOutput!=null){
				oOutcomesProcessing.addMapOutput(oMapOutput);
			}
		}
		return oOutcomesProcessing;
	}
	
	public static void setScoreModel(QTIObject oQTI, String sScoreModel){
		OutcomesProcessing oOutcomesProcessing = createOutcomesProcessing(sScoreModel);
		if (oQTI instanceof Section){
			Section oSection = (Section)oQTI;
			oSection.setOutcomesProcessing(oOutcomesProcessing);
		} else if (oQTI instanceof Assessment){
			Assessment oAssessment = (Assessment)oQTI;
			oAssessment.setOutcomesProcessing(oOutcomesProcessing);			
		}
	}
	
	public static String getScoreModel(QTIObject oQTI){
		String sScoreModel = null;
		Vector vOutcomesProcessing = null;
		if (oQTI instanceof Section){
			Section oSection = (Section)oQTI;
			vOutcomesProcessing = oSection.getOutcomesProcessing();
			
		} else if (oQTI instanceof Assessment){
			Assessment oAssessment = (Assessment)oQTI;
			vOutcomesProcessing = oAssessment.getOutcomesProcessing();
		}
		if (vOutcomesProcessing!=null && !vOutcomesProcessing.isEmpty()){
			OutcomesProcessing oOutcomesProcessing = (OutcomesProcessing)vOutcomesProcessing.firstElement();
			sScoreModel = oOutcomesProcessing.getScoreModel();
		}
		return sScoreModel;
	}

//-->Albert


	public static void setItemSelectionOrdering(Section oSection, Item oItem, String sOrdreItem){
		String sIdent = oItem.getIdent();
		boolean bWantsRandom = (sOrdreItem!=null && sOrdreItem.trim().equalsIgnoreCase(Section.RANDOM_ORDER));
	
		SelectionOrdering oSelectionOrdering = oSection.getSelectionOrdering();
		if (oSelectionOrdering == null){
			oSelectionOrdering = new SelectionOrdering();
			oSection.setSelectionOrdering(oSelectionOrdering);
		}
		setSelectionOrdering(oSelectionOrdering, sIdent, bWantsRandom); 
		
	}
	public static void setSectionSelectionOrdering(Assessment oAssessment, Section oSection, String sFullOrder){
		String sSectionIdent = oSection.getIdent();
		boolean bWantsRandom = (sFullOrder!=null && sFullOrder.trim().equalsIgnoreCase(Section.RANDOM_ORDER));
	
		SelectionOrdering oSelectionOrdering = oAssessment.getSelectionOrdering();
		if (oSelectionOrdering == null){
			oSelectionOrdering = new SelectionOrdering();
			oAssessment.setSelectionOrdering(oSelectionOrdering);
		}
		setSelectionOrdering(oSelectionOrdering, sSectionIdent  ,bWantsRandom ); 
		
	}

	private static void setSelectionOrdering(SelectionOrdering oSelectionOrdering, String sIdent, boolean bWantsRandom ){
	
		Selection oSelection = oSelectionOrdering.getSelection();
		if (oSelection == null){
			oSelection = new Selection();
			oSelectionOrdering.setSelection(oSelection);
		}
		
		Vector vOrSelection = oSelection.getOrSelection();
		OrSelection oOrSelection = getOrSelection(vOrSelection, sIdent);
		if (oOrSelection!=null){ //Ja estava establert com a aleatori
			if (bWantsRandom){
				//Ja està com volem
			} else { //Hem d'eliminar-lo
				deleteSelectionMetadataFromOrSelection(oOrSelection, sIdent);
			}
		} else { //Abans no era aleatori
			if (bWantsRandom){ // L'hem d'afegir
				createOrSelection(oSelection, sIdent);
			} else { 
				//Ja està com volem
			}
		}
	}

	
	public static String getItemRandomOrder(Section oFull, Item oItem){
		String sIdent = oItem.getIdent();
		SelectionOrdering oSelectionOrdering = oFull.getSelectionOrdering();
		return getRandomOrder(oSelectionOrdering, sIdent);
	}

	public static String getItemRandomOrder(Section oFull, String sItemIdent){
		SelectionOrdering oSelectionOrdering = oFull.getSelectionOrdering();
		return getRandomOrder(oSelectionOrdering, sItemIdent);
	}

	public static String getSectionRandomOrder(Assessment oAssessment, Section oFull){
		String sSectionIdent = oFull.getIdent();
		SelectionOrdering oSelectionOrdering = oAssessment.getSelectionOrdering();
		return getRandomOrder(oSelectionOrdering, sSectionIdent);
	}

	public static String getRandomOrder(SelectionOrdering oSelectionOrdering, String sIdent){
		boolean isRandom = false;
		
		if (oSelectionOrdering != null){
			Selection oSelection = oSelectionOrdering.getSelection();
			if (oSelection != null){
				Vector vOrSelection = oSelection.getOrSelection();
				OrSelection oOrSelection = getOrSelection(vOrSelection, sIdent);
				if (oOrSelection!=null){
					isRandom = true;
				}
			}
		}
		
		if (isRandom){
			return Section.RANDOM_ORDER;
		} else {
			return Section.NO_RANDOM_ORDER;
		}
	}
	
	private static void createOrSelection(Selection oSelection, String ident){
		Vector vOrSelection = oSelection.getOrSelection();
		if (vOrSelection.size()==0){ //Es crea de zero
			OrSelection orSelection = new OrSelection();
			SelectionMetadata oSelectionMetadata = new SelectionMetadata("ident", "EQ", ident.trim());
			orSelection.addSelectionMetadata(oSelectionMetadata);
			oSelection.addOrSelection(orSelection);
		} else{ //S'ha d'afegir
			OrSelection orSelection = (OrSelection)vOrSelection.firstElement();
			SelectionMetadata oSelectionMetadata = new SelectionMetadata("ident", "EQ", ident.trim());
			orSelection.addSelectionMetadata(oSelectionMetadata);
		}
	}

	private static OrSelection getOrSelection(Vector vOrSelection, String ident){
		OrSelection oOrSelectionResult = null;
		Enumeration enumOrSelection = vOrSelection.elements();
		while (oOrSelectionResult == null && enumOrSelection.hasMoreElements()){
			OrSelection oOrSelection = (OrSelection)enumOrSelection.nextElement();
			SelectionMetadata oSelectionMetadata = getSelectionMetadata(oOrSelection, ident);
			if (oSelectionMetadata!=null)
				oOrSelectionResult = oOrSelection;
		}
		return oOrSelectionResult;
	}
	
	private static SelectionMetadata getSelectionMetadata(OrSelection oOrSelection, String ident){
		SelectionMetadata oSelectionMetadataResult = null;
		Vector vSelectionMetadata = oOrSelection.getSelectionMetadata();
		Enumeration enumSelectionMetadata = vSelectionMetadata.elements();
		while (oSelectionMetadataResult == null && enumSelectionMetadata.hasMoreElements()){
			SelectionMetadata oSelectionMetadata = (SelectionMetadata)enumSelectionMetadata.nextElement();
			if (oSelectionMetadata.getText()!=null && oSelectionMetadata.getText().trim().equalsIgnoreCase(ident.trim()))
				oSelectionMetadataResult = oSelectionMetadata;
		}
		return oSelectionMetadataResult;
	}
	
	private static void deleteSelectionMetadataFromOrSelection(OrSelection oOrSelection, String ident){
		SelectionMetadata oSelectionMetadata = getSelectionMetadata(oOrSelection, ident);
		Vector vSelectionMetadata = oOrSelection.getSelectionMetadata();
		vSelectionMetadata.remove(oSelectionMetadata);
	}
//Albert <--
	
	
//	***************************	
//	* Pregunta
//	***************************
	
	public static Item createItem(String sIdentItem, String sTitle, String sText){
		Item oItem = Item.createItem(sIdentItem, sTitle, sText);
		oItem.getPresentation().getFlow().addFlow(new Flow());
		return oItem;
	}

	public static ResponseLID createResponseLID(String sIdentLID, String sCardinality, String sAlignment,
	  String sDisplay, String[] vIdResponses, String[] vTextResponses, String[] vResourcesResponses){
		ResponseLID oResponseLID = new ResponseLID(sIdentLID, sCardinality, null);
		RenderChoice oRenderChoice = RenderChoice.create(sAlignment);
		oRenderChoice.setDisplay(sDisplay);
		if (vIdResponses!=null){
			for(int i=0;i<vIdResponses.length;i++){
				String sIdentResp = vIdResponses[i];
				String sTextResp = null;
				if (vTextResponses!=null) sTextResp = vTextResponses[i];
				//if (sTextResp!=null && sTextResp.trim().length()>0){
				if (sTextResp!=null){
					oRenderChoice.addResponseLabelWithMattext(sIdentResp, sTextResp, sAlignment);
				}
				if (vResourcesResponses!=null && vResourcesResponses[i]!=null && !vResourcesResponses[i].equals("null") && vResourcesResponses[i].trim().length()>0){
					String sURI = vResourcesResponses[i];
					if (FileUtil.isImage(sURI)){
						String sImagtype = FileUtil.getImageType(sURI);
						oRenderChoice.addResponseLabelWithMatimage(sIdentResp, sImagtype, sURI, sAlignment);
					} else if (FileUtil.isAudio(sURI)){
						oRenderChoice.addResponseLabelWithMataudio(sIdentResp, sURI, sAlignment);
					}
				}
			}
		}
		oResponseLID.setRender(oRenderChoice);
		return oResponseLID;		
	}
	
	public static ResponseSTR createResponseSTR(String sIdentSTR, String sCardinality){
		ResponseSTR oResponse = new ResponseSTR(sIdentSTR, sCardinality, null);
		RenderFIB oRender = new RenderFIB();
		oRender.addResponseLabel(getRandomIdent());
		oResponse.setRender(oRender);
		return oResponse;		
	}
	
	/**
	 * Indica el tipus de la pregunta (seleccio, cloze, ...)
	 * TODO: Cal revisar-la cada vegada que s'afegeix un nou tipus
	 */
	public static String getTipusPregunta(Item oItem){
		String sTipus = QVConstants.CLOZE_TIPUS_PREGUNTA;
		try{
			if (oItem!=null && oItem.getPresentation()!=null){
				Vector vResponses = oItem.getPresentation().getResponses();
				if (vResponses!=null && !vResponses.isEmpty()){
					QTISuperResponse oResponse = (QTISuperResponse)vResponses.firstElement();
					if (vResponses.size()==1 && 
						oResponse.getRender() instanceof RenderChoice &&
						oItem.getPresentation().getAllMaterials().size()==1){
							// Seleccio
							sTipus = QVConstants.SELECTION_TIPUS_PREGUNTA;
					}else if (oResponse.getCardinality().equalsIgnoreCase(QVConstants.ORDERED_TIPUS_PREGUNTA) &&
						oResponse instanceof ResponseLID &&
						oResponse.getRender() instanceof IMSRenderObject){
							// Ordenacio
							sTipus = QVConstants.ORDERED_TIPUS_PREGUNTA;
					}else if (oResponse instanceof ResponseGRP &&
						oResponse.getRender() instanceof IMSRenderObject){
							// Drag&Drop
							sTipus = QVConstants.DRAGDROP_TIPUS_PREGUNTA;								
					}else if (oResponse instanceof ResponseLID &&
						oResponse.getRender() instanceof RenderHotspot){
							// Hotspot (seleccio sobre imatges)
							sTipus = QVConstants.HOTSPOT_TIPUS_PREGUNTA;								
					}else if (oResponse instanceof ResponseXY &&
							oResponse.getRender() instanceof RenderDraw){
							// Draw
							sTipus = QVConstants.DRAW_TIPUS_PREGUNTA;								
					}else{
						// Cloze
						sTipus = QVConstants.CLOZE_TIPUS_PREGUNTA;
					}
				}
			}
		}catch (Exception e){
			logger.error("EXCEPCIO obtenint el tipus de la pregunta '"+oItem.getIdent()+"'--> "+e);
		}
		return sTipus;
	}
	
	protected static boolean containsResponseFIB(Vector vResponses){
		boolean bContains = false;
		if (vResponses!=null){
			Enumeration enumResponses = vResponses.elements();
			while (enumResponses.hasMoreElements()){
				Object oQTI = enumResponses.nextElement();
				if (oQTI instanceof ResponseSTR || oQTI instanceof ResponseNUM){
					bContains = true;
					break;
				}
			}
		}
		return bContains;
	}
	
	public static QTISuperRender getItemRender(Item oItem){
		QTISuperRender oRender = null;
		QTISuperResponse oResponse = getResponse(oItem);
		if (oResponse!=null){
			oRender = oResponse.getRender();
		}
		return oRender;		
	}
	
	public static String getBorderColorStyle(QTIObject o){
		String sBorderColor = "";
		String sStyle = getStyle(o);
		if (sStyle!=null){
			StringTokenizer st = new StringTokenizer(sStyle, ",");
			while (st.hasMoreTokens()){
				String s = st.nextToken();
				if (s.trim().startsWith("border-color:")){
					sBorderColor = s.substring(s.indexOf("border-color:")+13);
				}
			}
		}
		return sBorderColor;
	}
	public static void setBorderColorStyle(QTIObject o, String sBorderColor){
		if (sBorderColor!=null && !"#RRGGBB".equalsIgnoreCase(sBorderColor) &&
			sBorderColor.startsWith("#") && sBorderColor.trim().length()==7 ){
			String sStyle = "border-color:"+sBorderColor;
			setStyle(o, sStyle);							
		}

	}
	
	public static String getStyle(QTIObject o){
		String sStyle = "";
		QTISuperRender oRender = null;
		if (o instanceof Item){ 
			oRender = getItemRender((Item)o);
		}else if (o instanceof QTISuperRender){
			oRender = (QTISuperRender)o;
		}
		if (oRender!=null){
			if (oRender instanceof IMSRenderObject){
				sStyle = ((IMSRenderObject)oRender).getStyle();
			}else if (oRender instanceof RenderHotspot){
				sStyle = ((RenderHotspot)oRender).getStyle();
			}
		}
		return sStyle;
	}
	public static void setStyle(QTIObject o, String sStyle){
		QTISuperRender oRender = null;
		if (o instanceof Item){ 
			oRender = getItemRender((Item)o);
		}else if (o instanceof QTISuperRender){
			oRender = (QTISuperRender)o;
		}
		if (oRender!=null){
			if (oRender instanceof IMSRenderObject){
				((IMSRenderObject)oRender).setStyle(sStyle);
			}else if (oRender instanceof RenderHotspot){
				((RenderHotspot)oRender).setStyle(sStyle);
			}
		}
	}

//	***************************	
//	* Pregunta - SELECCIO
//	***************************
	
	public static Item createSelectionItem(String sIdentItem, String sTitle, String sText,
  	  String sIdentLID, String sCardinality, String sAlignment, String sDisplay, 
	  String[] vIdResponses, String[] vTextResponses, String[] vResourcesResponses){
		Item oItem = Item.createItem(sIdentItem, sTitle, sText);
		Presentation oPresentation = oItem.getPresentation();
		if (oPresentation==null){
			oPresentation = new Presentation();
			oPresentation.setFlow(new Flow());
			oItem.setPresentation(oPresentation);
		}
		ResponseLID oResponseLID = createResponseLID(sIdentLID, sCardinality, sAlignment, sDisplay, vIdResponses, vTextResponses, vResourcesResponses);
		oPresentation.addResponse(oResponseLID);
		return oItem;
	}
	
	/**
	 * Elimina el full indicat al quadern
	 * @param oAssessment quadern
	 * @param oSection String amb l'identificador del full que conte la pregunta a eliminar o Section
	 * @param oItem String amb l'identificador de la pregunta a eliminar o Item amb la pregunta a eliminar
	 */
	public static void delPregunta(Section oSection, Object oItem){
		if (oSection!=null && oItem!=null){
			oSection.delAttributeElement(QTIObject.ITEM, oItem);
		}
	}
	
	public static Outcomes getDefaultOutcomes(){
		Outcomes oOutcomes = new Outcomes();
		oOutcomes.addDecvar(new Decvar("SCORE", "Decimal", "0"));
		oOutcomes.addDecvar(new Decvar("CORRECT", "Boolean", "False"));
		return oOutcomes;
	}
	
	
	/**
	 * @param oItem pregunta
	 * @return true si la pregunta s'ha d'alinear en vertical; false en cas contrari
	 */
	public static boolean isVertical(Item oItem){
		boolean bVertical = true;
		if (oItem!=null){
			QTISuperResponse oResponse = getResponse(oItem);
			if (oResponse!=null){
				QTISuperRender oRender = oResponse.getRender();
				if (oRender instanceof RenderChoice){
					Vector vFlowLabel = ((RenderChoice)oRender).getFlowLabel();
					bVertical = vFlowLabel==null || vFlowLabel.isEmpty();
				}
			}
		}
		return bVertical;
	}
	
//	***************************	
//	* Pregunta - CLOZE
//	***************************
	
	public static Item createClozeItem(String sIdentItem, String sTitle, String sText, 
	  String sClozeText, Hashtable hIdResponses, Hashtable hTextResponses, Hashtable hFIBResponses){
		Item oItem = Item.createItem(sIdentItem, sTitle, sText);
		Presentation oPresentation = oItem.getPresentation();
		if (oPresentation==null){
			oPresentation = new Presentation();
			oPresentation.setFlow(new Flow());
			oItem.setPresentation(oPresentation);
		}
		// Afegir els materials i respostes a Presentation a partir del paragraf
		if (sClozeText!=null){
			Enumeration enumLines = getLines(sClozeText);
			while(enumLines.hasMoreElements()){
				String sLine = (String)enumLines.nextElement();
				Flow oFlow = createClozeLineFlow(sLine, hIdResponses, hTextResponses, hFIBResponses);
				oPresentation.getFlow().addFlow(oFlow);
			}
		}
		return oItem;
	}
	
	protected static Flow createClozeLineFlow(String sLine, Hashtable hIdResponses, Hashtable hTextResponses, Hashtable hFIBResponses){
		Flow oFlow = new Flow();
		int iStartIndex = 0;
		int iEndIndex = sLine.indexOf("[");
		while (iEndIndex>=0 && iEndIndex<sLine.length()){
			String sMaterialText = sLine.substring(iStartIndex, iEndIndex);
			if (sMaterialText.trim().length()>0){
				oFlow.addMaterial(Material.createWithMattext(sMaterialText));
			}
			iStartIndex = iEndIndex+1;
			iEndIndex = sLine.indexOf("]", iStartIndex);
			String sResponseLID = sLine.substring(iStartIndex, iEndIndex);
			if (sResponseLID.trim().length()>0){
				String sDisplay = "LIST";
				String[] vIdResponses = (String[])hIdResponses.get(sResponseLID);
				boolean bIsFIB = hFIBResponses.containsKey(sResponseLID) && ((Boolean)hFIBResponses.get(sResponseLID)).booleanValue();
				QTISuperResponse oResponse = null;
				if (bIsFIB){
					oResponse = createResponseSTR(sResponseLID, "Single");
				}else{
					String[] vTextResponses = (String[])hTextResponses.get(sResponseLID);
					String[] vImageResponses = null;
					oResponse = createResponseLID(sResponseLID, "Single", null, sDisplay, vIdResponses, vTextResponses, vImageResponses);
				}
				if (oResponse != null){
					logger.debug("addResponse-> "+oResponse.getIdent());
					oFlow.addResponse(oResponse);
				}
			}
			iStartIndex = iEndIndex+1;
			iEndIndex = sLine.indexOf("[", iStartIndex);
		}
		if (iEndIndex<0 && iStartIndex<sLine.trim().length()){
			String sMaterialText = sLine.substring(iStartIndex);
			oFlow.addMaterial(Material.createWithMattext(sMaterialText));
		}
		return oFlow;
	}
	
	public static String getClozeText(Item oItem){
		String sText = "";
		if (oItem!=null){
			try{
				Flow oPresentationFlow = oItem.getPresentation().getFlow();
				if (oPresentationFlow!=null){
					Vector vFlows = oPresentationFlow.getFlows();
					if (vFlows!=null && !vFlows.isEmpty()){
						Enumeration enumFlows = vFlows.elements();
						while (enumFlows.hasMoreElements()){
							Flow oFlow = (Flow)enumFlows.nextElement();
							Enumeration enumFlowContent = oFlow.getContents().elements();
							while (enumFlowContent.hasMoreElements()){
								QTIObject oQTI = (QTIObject)enumFlowContent.nextElement();
								if (oQTI instanceof Material){
									sText+=((Material)oQTI).getText();
								} else if (oQTI instanceof QTISuperResponse){
									sText+="["+oQTI.getIdent()+"]";
								}
								//if (!sText.endsWith(" ")){
								//	sText += " ";					
								//}
							}
							sText+= "\n";
						}
					}
				}
			}catch(Exception e){
				sText = "";
			}
		}
		return sText;
	}
	
//	***************************	
//	* Pregunta - ORDENACIO
//	***************************
	
	public static Item createOrderedItem(String sIdentItem, String sTitle, String sText,
	  String sIdentLID, String sOrientacio, String[] vIdResponses, String[] vTextResponses){
		Item oItem = Item.createItem(sIdentItem, sTitle, sText);
		Presentation oPresentation = oItem.getPresentation();
		if (oPresentation==null){
			oPresentation = new Presentation();
			oPresentation.setFlow(new Flow());
			oItem.setPresentation(oPresentation);
		}
		String sCardinality = QVConstants.ORDERED_TIPUS_PREGUNTA;
		ResponseLID oResponseLID = createResponseLID(sIdentLID, sCardinality, sOrientacio, vIdResponses, vTextResponses);
		oPresentation.addResponse(oResponseLID);
		return oItem;
	}

	public static Item createOrderedItem(String sIdentItem, String sTitle, String sText,
	  String sIdentLID, String sOrientacio, boolean bShuffle, String[] vIdResponses, String[] vTextResponses, String[] vResourceResponses){
		Item oItem = Item.createItem(sIdentItem, sTitle, sText);
		Presentation oPresentation = oItem.getPresentation();
		if (oPresentation==null){
			oPresentation = new Presentation();
			oPresentation.setFlow(new Flow());
			oItem.setPresentation(oPresentation);
		}
		String sCardinality = QVConstants.ORDERED_TIPUS_PREGUNTA;
		ResponseLID oResponseLID = createResponseLID(sIdentLID, sCardinality, sOrientacio, bShuffle, vIdResponses, vTextResponses, vResourceResponses);
		oPresentation.addResponse(oResponseLID);
		return oItem;
	}

	public static ResponseLID createResponseLID(String sIdentLID, String sCardinality, QTISuperRender oRender){
		ResponseLID oResponseLID = new ResponseLID(sIdentLID, sCardinality, null);
		oResponseLID.setRender(oRender);
		return oResponseLID;		
	 }
	 
	public static ResponseLID createResponseLID(String sIdentLID, String sCardinality, String sOrientacio, String[] vIdResponses, String[] vTextResponses){
		return createResponseLID(sIdentLID, sCardinality, sOrientacio, true, vIdResponses, vTextResponses, null);
	 }
	 
	public static ResponseLID createResponseLID(String sIdentLID, String sCardinality, String sOrientacio, boolean bShuffle, String[] vIdResponses, String[] vTextResponses, String[] vResourceResponses){
		QTISuperRender oRender = createIMSRenderObject(sOrientacio, bShuffle, vIdResponses, vTextResponses, vResourceResponses);
		return createResponseLID(sIdentLID, sCardinality, oRender);
	 }
	
	public static ResponseLID createResponseLID(String sIdentLID, String sCardinality, String sOrientacio, boolean bShuffle, String[] vIdResponses, String[] vTextResponses, String[] vResourceResponses, String sUri) {
		QTISuperRender oRender = createIMSRenderObject(sOrientacio, bShuffle, vIdResponses, vTextResponses, vResourceResponses, sUri);
		return createResponseLID(sIdentLID, sCardinality, oRender);
	}
	
	public static IMSRenderObject createIMSRenderObject(String sAlignment, boolean bShuffle, String[] vIdResponses, String[] vTextResponses, String[] vResourceResponses){
		IMSRenderObject oRender = IMSRenderObject.create(sAlignment, bShuffle?"Yes":"No");
		if (vIdResponses!=null){
			for(int i=0;i<vIdResponses.length;i++){
				String sIdentResp = vIdResponses[i];
				String sTextResp = vTextResponses!=null?vTextResponses[i]:"";
				if (vResourceResponses!=null && vResourceResponses[i]!=null && vResourceResponses[i].trim().length()>0){
					String sImageType = vResourceResponses[i].endsWith(".gif")?"image/gif":"image/jpeg";
					oRender.addResponseLabelWithMatimage(sIdentResp, sImageType, vResourceResponses[i], sAlignment);
				//} else if (sTextResp!=null && sTextResp.trim().length()>0){
				} else{
					oRender.addResponseLabelWithMattext(sIdentResp, sTextResp, sAlignment);
				}
			}
		}
		return oRender;
	}
	
	private static IMSRenderObject createIMSRenderObject(String sAlignment, boolean bShuffle, String[] vIdResponses, String[] vTextResponses, String[] vResourceResponses, String sURI) {
		IMSRenderObject oRender = IMSRenderObject.create(sAlignment, bShuffle?"Yes":"No");
		if (vIdResponses!=null){	
			int min;
			int size[][] = new int[vIdResponses.length][2];
			// Get minimum height of images
			for(int i=0;i<vIdResponses.length;i++){
				try {
					BufferedImage img = ImageIO.read(new File(sURI+"/"	+vResourceResponses[i]));
					size[i][0]= img.getWidth();
					size[i][1]= img.getHeight();
				} catch (IOException e) {
					System.out.println("No s'ha pogut carregar la imatge");
				}
			}
	
			min=size[0][1];
			for(int i=0;i<size.length;i++){
				min = Math.min(min, size[i][1]);
			}
		
			for(int i=0;i<vIdResponses.length;i++){
				String sIdentResp = vIdResponses[i];
				String sTextResp = vTextResponses!=null?vTextResponses[i]:"";
				if (vResourceResponses!=null && vResourceResponses[i]!=null && vResourceResponses[i].trim().length()>0){
					String sImageType = vResourceResponses[i].endsWith(".gif")?"image/gif":"image/jpeg";
					int iWidth = (size[i][0]*min)/size[i][1];
					int iHeight = min;
					oRender.addResponseLabelWithMatimage(sIdentResp, sImageType, vResourceResponses[i], sAlignment, iWidth, iHeight);
				} else{
					oRender.addResponseLabelWithMattext(sIdentResp, sTextResp, sAlignment);
				}
			}
		}
		return oRender;
	}
	
	
//	***************************	
//	* Pregunta - HOTSPOT
//	***************************
	
	public static Item createHotspotItem(){
		String sIdentItem = getRandomIdent();
		Item oItem = Item.createItem(sIdentItem, null, null);
		Presentation oPresentation = oItem.getPresentation();
		if (oPresentation==null){
			oPresentation = new Presentation();
			oPresentation.setFlow(new Flow());
			oItem.setPresentation(oPresentation);
		}
		String sIdentLID = getRandomIdent();
		ResponseLID oResponseLID = createResponseLIDwithRenderHotspot(sIdentLID);
		oPresentation.addResponse(oResponseLID);
		return oItem;
	}

	public static ResponseLID createResponseLIDwithRenderHotspot(String sIdentLID){
		ResponseLID oResponseLID = new ResponseLID(sIdentLID, null, null);
		RenderHotspot oRender = new RenderHotspot();
		oRender.setShowDraw(QVConstants.NO);
		oRender.setShowOptions(QVConstants.YES);
		oRender.setTransp(QVConstants.YES);
		oResponseLID.setRender(oRender);
		return oResponseLID;		
	}

	public static String getHotspotType(QTIObject o){
		String sType = QVConstants.ZONE_HOTSPOT_TYPE;
		RenderHotspot oRender = getRenderHotspot(o);
		if (oRender!=null){
			if (QVConstants.NO.equals(oRender.getShowDraw()) && QVConstants.YES.equals(oRender.getShowOptions()) && QVConstants.YES.equals(oRender.getTransp())){
				sType = QVConstants.ZONE_HOTSPOT_TYPE;
			}else if (QVConstants.NO.equals(oRender.getShowDraw()) && QVConstants.YES.equals(oRender.getShowOptions()) && QVConstants.NO.equals(oRender.getTransp())){
				sType = QVConstants.OPTION_HOTSPOT_TYPE;
			}else if (QVConstants.YES.equals(oRender.getShowDraw()) && QVConstants.YES.equals(oRender.getShowOptions()) && QVConstants.NO.equals(oRender.getTransp())){
				sType = QVConstants.DOT_HOTSPOT_TYPE;
			}else if (QVConstants.NO.equals(oRender.getShowDraw()) && QVConstants.NO.equals(oRender.getShowOptions()) && QVConstants.NO.equals(oRender.getTransp())){
				sType = QVConstants.FREE_HOTSPOT_TYPE;
			}
		}
		return sType;
	}

	public static RenderHotspot getRenderHotspot(QTIObject o){
		RenderHotspot oHotspot = null;
		if (o!=null){
			QTISuperResponse oResponse = null;
			if (o instanceof Item){
				oResponse = QTIUtil.getResponse((Item)o);
			}else if (o instanceof QTISuperResponse){
				oResponse = (QTISuperResponse)o;
			}
			if (oResponse!=null){
				QTISuperRender oRender = oResponse.getRender();
				if (oRender!=null && oRender instanceof RenderHotspot){
					oHotspot = (RenderHotspot)oRender;
				}
			}
		}
		return oHotspot;
	}
	
	
	
//	***************************	
//	* Pregunta - DRAG&DROP
//	***************************
	
	public static Item createDragDropItem(){
		String sIdentItem = getRandomIdent();
		Item oItem = Item.createItem(sIdentItem, null, null);
		Presentation oPresentation = oItem.getPresentation();
		if (oPresentation==null){
			oPresentation = new Presentation();
			oPresentation.setFlow(new Flow());
			oItem.setPresentation(oPresentation);
		}
		String sIdentGRP = getRandomIdent();
		ResponseGRP oResponseGRP = createResponseGRPwithIMSRender(sIdentGRP);
		oPresentation.addResponse(oResponseGRP);
		return oItem;
	}

	public static ResponseGRP createResponseGRPwithIMSRender(String sIdentLID){
		ResponseGRP oResponseGRP = new ResponseGRP(sIdentLID, null, null);
		IMSRenderObject oRender = new IMSRenderObject();
		oRender.setShuffle("Yes");
		oRender.setInside("Yes");
		oRender.setAlign("Auto");
		oResponseGRP.setRender(oRender);
		return oResponseGRP;		
	}

//	***************************	
//	* Pregunta - DRAW
//	***************************
	
	public static Item createDrawItem(){
		String sIdentItem = getRandomIdent();
		Item oItem = Item.createItem(sIdentItem, null, null);
		Presentation oPresentation = oItem.getPresentation();
		if (oPresentation==null){
			oPresentation = new Presentation();
			oPresentation.setFlow(new Flow());
			oItem.setPresentation(oPresentation);
		}
		String sRespIdent = getRandomIdent();
		ResponseXY oResponseXY = createResponseXYPwithRenderDraw(sRespIdent);
		oPresentation.addResponse(oResponseXY);
		
		Resprocessing oResprocessing = new Resprocessing();
		oResprocessing.setOutcomes(QTIUtil.getDefaultOutcomes());
		oItem.addResprocessing(oResprocessing);
		
		return oItem;
	}
	
	public static ResponseXY createResponseXYPwithRenderDraw(String sRespIdent){
		ResponseXY oResponse = new ResponseXY(sRespIdent, null, null);
		RenderDraw oRender = new RenderDraw();
		oResponse.setRender(oRender);
		return oResponse;		
	}

	public static RenderDraw getRenderDraw(QTIObject o){
		RenderDraw oDraw = null;
		if (o!=null){
			QTISuperResponse oResponse = null;
			if (o instanceof Item){
				oResponse = QTIUtil.getResponse((Item)o);
			}else if (o instanceof QTISuperResponse){
				oResponse = (QTISuperResponse)o;
			}
			if (oResponse!=null){
				QTISuperRender oRender = oResponse.getRender();
				if (oRender!=null && oRender instanceof RenderDraw){
					oDraw = (RenderDraw)oRender;
				}
			}
		}
		return oDraw;
	}
	
	
	
//	***************************	
//	* Resposta
//	***************************

	/**
	 * @param oItem
	 * @param sIdentResp identificador del conjunt de respostes
	 * @param iIndex posicio de la resposta de la que es dessitja obtenir l'identificador
	 * @return
	 */
	public static String getResponseIdent(Item oItem, String sIdentResp, int iIndex){
		String sRespIdent = null;
		if (oItem!=null){
			Presentation oPresentation = oItem.getPresentation();
			if (oPresentation!=null){
				QTISuperResponse oResponse = oPresentation.getResponse(sIdentResp);
				if (oResponse!=null){
					QTIObject oRender = oResponse.getRender();
					if (oRender!=null){
						ResponseLabel oResponseLabel = ((QTISuperRender)oRender).getResponseLabel(iIndex);
						if (oResponseLabel!=null){
							sRespIdent = oResponseLabel.getIdent();
						}
					}
				}
			}
		}
		return sRespIdent;
	}
	
	public static void delResponse(QTIObject oQTI, String sIdentResp){
		if (oQTI!=null && sIdentResp!=null){
			QTISuperResponse oResponse = null;
			if (oQTI instanceof Item){
				oResponse = getResponse((Item)oQTI);
			} else if (oQTI instanceof ResponseLID){
				oResponse = (QTISuperResponse)oQTI;
			}
			if (oResponse!=null){
				QTISuperRender oRender = oResponse.getRender();
				if (oRender!=null){
					oRender.delResponseLabel(sIdentResp);
					;
				}
			}
		}
	}
	
	/**
	 * Obte el primer response de l'item especificat
	 * @param oItem
	 * @return
	 */
	public static QTISuperResponse getResponse(Item oItem){
		QTISuperResponse oResponse = null;
		if (oItem!=null){
			Presentation oPresentation = (oItem).getPresentation();
			if (oPresentation!=null){
				if (!oPresentation.getResponses().isEmpty()){
					oResponse = (QTISuperResponse)oPresentation.getResponses().firstElement();
				}
			}
		}
		return oResponse;		
	}
	
	public static QTISuperResponse getResponse(Item oItem, String sIdentResp){
		QTISuperResponse oResponse= null;
		if (sIdentResp==null || sIdentResp.trim().length()<=0){
			oResponse = getResponse(oItem);
		}else if (oItem.getPresentation()!=null){
			oResponse = oItem.getPresentation().getResponse(sIdentResp);
		}
		return oResponse;
	}
	
	public static ResponseLabel getResponseLabel(Item oItem, String sIdentResp, String sIdentRespLabel){
		ResponseLabel oResponseLabel = null;
		QTISuperResponse oResponse= getResponse(oItem, sIdentResp);
		if (oResponse!=null){
			QTISuperRender oRender = oResponse.getRender();
			if (oRender !=null){
				oResponseLabel = oRender.getResponseLabel(sIdentRespLabel); 
			}
		}
		return oResponseLabel;
	}
	
	/**
	 * 
	 * @param oItem
	 * @param sIdentResp
	 * @return Vector de QTIResponseLabel
	 */
	public static Vector getResponseLabels(Item oItem, String sIdentResp){
		Vector vResponseLabel = new Vector();
		if (oItem!=null){
			QTISuperResponse oResponse = getResponse(oItem, sIdentResp);
			if (oResponse!=null){
				QTISuperRender oRender = oResponse.getRender();
				if (oRender !=null){
					vResponseLabel = oRender.getResponseLabel();
				}
			}
		}
		return vResponseLabel;	
	}
	
	/**
	 * @param oItem pregunta
	 * @return Vector amb els identificadors de les respostes que son correctes
	 */
	public static Vector getOKResponses(Item oItem, String sLIDIdent){
		Vector vOKResponses = new Vector();
		if (oItem!=null){
			Respcondition oRespcondition = oItem.getRespcondition("CORRECT","True");
			if (oRespcondition!=null){
				Vector vConditions = oRespcondition.getConditionvar().getContents();
				vOKResponses = getOKResponses(vConditions, sLIDIdent);
			}
		}
		return vOKResponses;
	}
	
	/**
	 * @param oItem
	 * @return feedback de la resposta correcta
	 */
	public static String getOKFeedback(Item oItem){
		String sFeedback = null;
		if (oItem!=null){
			sFeedback = oItem.getFeedback("CORRECT", "True");
		}
		return sFeedback;
	}
	
	/**
	 * @param oItem
	 * @return feedback de la resposta correcta
	 */
	public static String getKOFeedback(Item oItem){
		String sFeedback = null;
		if (oItem!=null){
			sFeedback = oItem.getFeedback("CORRECT", "False");
		}
		return sFeedback;
	}
	
	/**
	 * @param oItem
	 * @return puntuacio de la resposta correcta
	 */
	public static String getOKPuntuation(Item oItem){
		return getPuntuation(oItem, "CORRECT", "True");
	}
	
	/**
	 * @param oItem
	 * @return puntuacio de la resposta correcta
	 */
	public static String getKOPuntuation(Item oItem){
		return getPuntuation(oItem, "CORRECT", "False");
	}
	
	/**
	 * @param oItem
	 * @return hint de la pregunta
	 */
	public static String getHint(Item oItem){
		String sHint = null;
		if (oItem!=null){
			Itemfeedback oItemfeedback = oItem.getItemfeebackByTitle("hint");
			if (oItemfeedback!=null){
				sHint = oItemfeedback.getText();
			}
		}
		return sHint;
	}
	
	
	
	/**
	 * 
	 * @param oItem
	 * @return puntuacio de la resposta en que la variable indicada té el valor indicat
	 */
	protected static String getPuntuation(Item oItem, String sVarname, String sValue){
		String sPuntuation = null;
		if (oItem!=null){
			Respcondition oRespcondition = oItem.getRespcondition(sVarname,sValue);
			if (oRespcondition!=null){
				Setvar oSetvar = oRespcondition.getSetvar("SCORE");
				if (oSetvar!=null){
					sPuntuation = oSetvar.getText();
				}
			}
		}
		return sPuntuation;
	}
	
	protected static Vector getOKResponses(Vector vConditions, String sLIDIdent){
		Vector vOKResponses = new Vector();
		Enumeration enumConditions = vConditions.elements();
		while (enumConditions.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumConditions.nextElement();
			if (oQTI instanceof Varequal){
				Varequal oVarequal = (Varequal)oQTI;
				if (sLIDIdent==null || sLIDIdent.equals(oVarequal.getRespident()))
					vOKResponses.addElement(oVarequal.getText());
			} else if (oQTI instanceof QTIBooleanOperator){
				QTIBooleanOperator oOperator = (QTIBooleanOperator)oQTI;
				if ( !(oOperator instanceof Not)){
					vOKResponses.addAll(getOKResponses(oOperator.getContents(), sLIDIdent));							
				}
			}			
		}
		return vOKResponses;
	}
	
	public static boolean isCorrectResponseLabel(Item oItem, ResponseLabel oResponseLabel){
		boolean bIsCorrect = false;
		QTISuperResponse oResponse = getResponse(oItem);
		if (oResponse!=null){
			bIsCorrect = isCorrectResponseLabel(oItem, oResponse.getIdent(), oResponseLabel.getIdent());
		}
		return bIsCorrect;
	}
	public static boolean isCorrectResponseLabel(Item oItem, String sIdentResp, String sIdentRespLabel){
		boolean bIsCorrect = false;
		Vector vOK = getOKResponses(oItem, sIdentResp);
		bIsCorrect = vOK.contains(sIdentRespLabel);
		if (!bIsCorrect && vOK.size()==1){
			StringTokenizer st = new StringTokenizer((String)vOK.firstElement(),",");
			while (st.hasMoreTokens() && !bIsCorrect){
				String s = st.nextToken();
				bIsCorrect = s.equals(sIdentRespLabel);
			}
		}
		return bIsCorrect;
	}
	
	public static String getDisplayRenderChoice(Item oItem, String sIdentLID){
		String sDisplay = null;
		QTISuperResponse oResponse = oItem.getPresentation().getResponse(sIdentLID);
		if (oResponse!=null){
			QTIObject oRender = oResponse.getRender();
			if (oRender!=null && oRender instanceof RenderChoice){
				sDisplay = ((RenderChoice)oRender).getDisplay();			
			} 			
		}
		return sDisplay;
	}
	
	public static Vector conditionvarToVector(Conditionvar oConditionvar){
		return conditionvarToVector(oConditionvar, null);
	}
	
	/**
	 * 
	 * @param oConditionvar
	 * @param vIdentResp identificadors de les respostes que es retornaran a les condicions (la resta es descartaran)
	 * @return
	 */
	public static Vector conditionvarToVector(Conditionvar oConditionvar, Vector vIdentResp){
		Vector vConditionvar = new Vector();
		if (oConditionvar!=null){
			Enumeration enumContents = oConditionvar.getContents().elements();
			while (enumContents.hasMoreElements()){
				QTIObject oQTI = (QTIObject)enumContents.nextElement();
				vConditionvar.addAll(getConditions(oQTI, vIdentResp));
			}
		}
		return vConditionvar;
	}
	
	protected static Vector getConditions(QTIObject oQTI, Vector vIdentResp){
		Vector vConditions = new Vector();
		if (oQTI!=null){
			if (oQTI instanceof Varequal){
				Varequal oVarequal = (Varequal)oQTI;
				String sRespIdent = oVarequal.getRespident();
				if (vIdentResp==null || vIdentResp.contains(sRespIdent)){
					String sText = oVarequal.getText();
					Vector vRespLabel = new Vector();
					vRespLabel.addElement(sText);
					Hashtable hRespLabel = new Hashtable();
					hRespLabel.put(sRespIdent, vRespLabel);
					vConditions.addElement(hRespLabel);
				}
			}else if (oQTI instanceof QTIBooleanOperator){
				QTIBooleanOperator oOperator = (QTIBooleanOperator)oQTI;
				String sOperatorName = null;
				if (oOperator instanceof And){
					sOperatorName = "and";
				}else if (oOperator instanceof Or){
					sOperatorName = "or";
				}else if (oOperator instanceof Not){
					sOperatorName = "not";
				}
				Vector vOperatorConditions = new Vector();
				Enumeration enumContents = oQTI.getContents().elements();
				while (enumContents.hasMoreElements()){
					QTIObject tmp = (QTIObject)enumContents.nextElement();
					vOperatorConditions.addAll(getConditions(tmp, vIdentResp));					
				}
				if (!vOperatorConditions.isEmpty()){
					Hashtable hBooleanOperator = new Hashtable();
					hBooleanOperator.put(sOperatorName, vOperatorConditions);
					vConditions.addElement(hBooleanOperator);				
				}
			}
		}
		return vConditions;
	}
	
//	***************************	
//	* Puntuation
//	***************************
		
	public static void setPuntuation(Item oItem, Respcondition oRespcondition, String sPuntuation){
		if (oRespcondition!=null){
			Setvar oSetvar = oRespcondition.getSetvar("SCORE");
			if (oSetvar==null){
				oSetvar = new Setvar("SCORE","Add",sPuntuation);
				oRespcondition.addSetvar(oSetvar);
			}
			oSetvar.setText(sPuntuation);
		}		
	}
	
//	***************************	
//	* Feedback
//	***************************
		
	public static void setFeedback(Item oItem, Respcondition oRespcondition, String sFeedback){
		if (oRespcondition!=null){
			Itemfeedback oItemfeedback = null;
			if (oRespcondition.getDisplayfeedback()!=null && !oRespcondition.getDisplayfeedback().isEmpty()){
				Displayfeedback oDisplayfeedback = (Displayfeedback)oRespcondition.getDisplayfeedback().firstElement();
				String sIdentFeedback = oDisplayfeedback.getLinkrefid();
				oItemfeedback = oItem.getItemfeeback(sIdentFeedback);
				if (oItemfeedback==null){
					oItemfeedback = new Itemfeedback(sIdentFeedback);
					oItem.addItemfeedback(oItemfeedback);					
				}
			} else{
				String sIdentFeedback = QTIUtil.getRandomIdent();
				Displayfeedback oDisplayfeedback = new Displayfeedback(sIdentFeedback);
				oRespcondition.addDisplayfeedback(oDisplayfeedback);
				oItemfeedback = new Itemfeedback(sIdentFeedback);
				oItem.addItemfeedback(oItemfeedback);
			}
			if (oItemfeedback !=null){
				oItemfeedback.setText(sFeedback);
			}
		}		
	}
	
	/**
	 * @param oItem
	 * @param vConditions 
	 * @param sFeedback
	 */
	public static void addCorrectResponseCondition(Item oItem, Vector vConditions, String sFeedback, String sPuntuation){
		if (oItem!=null){
			try{
				if (sPuntuation!=null){
					sPuntuation = sPuntuation.replace(',','.');
					Double.valueOf(sPuntuation);
				}else{
					sPuntuation = "1";
				}
			}catch (Exception e){
				sPuntuation = "1";
			}
			if (sFeedback!=null) oItem.addResponseCondition(getDefaultOutcomes(), vConditions, getCorrectSetVars(sPuntuation), sFeedback);
		}
	}
	
	public static Hashtable getCorrectSetVars(){
		return getCorrectSetVars(null);
	}
	
	public static Hashtable getCorrectSetVars(String sPuntuation){
		Hashtable hSetvars = new Hashtable();
		Hashtable hAddSetVars = new Hashtable();
		hAddSetVars.put("CORRECT","True");
		hSetvars.put("Set", hAddSetVars);
		if (sPuntuation!=null){
			hAddSetVars = new Hashtable();
			hAddSetVars.put("SCORE", sPuntuation);
			hSetvars.put("Add", hAddSetVars);
		}
		return hSetvars;
	}
	
	/**
	 * @param oItem
	 * @param hConditions Hashtable amb els parells (idResp, String[]), on l'array conte els valors de les condicions
	 * @param sFeedback
	 */
	public static void addIncorrectResponseCondition(Item oItem, Vector vConditions, String sFeedback, String sPuntuation){
		if (oItem!=null){
			try{
				if (sPuntuation!=null){
					sPuntuation = sPuntuation.replace(',','.');
					Double.valueOf(sPuntuation);
				}else{
					sPuntuation = "0";
				}
			}catch (Exception e){
				sPuntuation = "0";
			}
			Hashtable hAddSetVars = new Hashtable();
			hAddSetVars.put("SCORE", sPuntuation);
			Hashtable hSetvars = new Hashtable();
			hSetvars.put("Add", hAddSetVars);
			hAddSetVars = new Hashtable();
			hAddSetVars.put("CORRECT","False");
			hSetvars.put("Set", hAddSetVars);
			
			Vector vNotConditions = getIncorrectConditions(vConditions);
			if (sFeedback!=null) oItem.addResponseCondition(getDefaultOutcomes(), vNotConditions, hSetvars, sFeedback);
		}
	}
	
	/**
	 * @param oItem
	 * @param hConditions Hashtable amb els parells (idResp, String[]), on l'array conte els valors de les condicions
	 */
	public static Vector getFIBConditions(Item oItem, Hashtable hConditions){
		Vector vConditions = new Vector();
		if (oItem!=null){			
			Hashtable hAnd = new Hashtable();
			Vector vOrs = new Vector();
			Enumeration enumResponses = hConditions.keys();
			while (enumResponses.hasMoreElements()){
				String sIdentResp = (String)enumResponses.nextElement();
				Object vResponses = hConditions.get(sIdentResp);
				Hashtable hOr = new Hashtable();
				Hashtable hOrContent = new Hashtable();
				hOrContent.put(sIdentResp, vResponses);
				hOr.put(QTIObject.OR.getAttributeName(), hOrContent);
				vOrs.addElement(hOr);
			}
			hAnd.put(QTIObject.AND.getAttributeName(), vOrs);
			vConditions.addElement(hAnd);
		}
		return vConditions;
	}
	
	public static Vector getClozeConditions(Item oItem, Hashtable hOKResponses, Hashtable hAllResponses, Hashtable hResponseType){
		Vector vConditions = new Vector();
		Hashtable hAnd = new Hashtable();
		Vector vAnd = new Vector();
		Enumeration enumResponse = hOKResponses.keys();
		while (enumResponse.hasMoreElements()){
			String sIdentResp = (String)enumResponse.nextElement();
			Boolean bIsFIB = (Boolean)hResponseType.get(sIdentResp);
			Hashtable hRespOKResponses = new Hashtable();
			Object vOKResponses = hOKResponses.get(sIdentResp);
			hRespOKResponses.put(sIdentResp, vOKResponses);
			if (bIsFIB!=null && bIsFIB.booleanValue()){
				vAnd.addAll(getFIBConditions(oItem, hRespOKResponses));
			} else{
				Hashtable hRespAllResponses = new Hashtable();
				hRespAllResponses.put(sIdentResp, hAllResponses.get(sIdentResp));
				vAnd.addAll(getSelectionConditions(oItem, hRespAllResponses, hRespOKResponses));
			}
		}
		hAnd.put(QTIObject.AND.getAttributeName(), vAnd);
		vConditions.addElement(hAnd);
		return vConditions;
	}
	
	/**
	 * @param oItem
	 * @param hResponses Hashtable amb els parells (idResp, Vector), on el vector conte totes les respostes
	 * @param hOKResponses Hashtable amb els parells (idResp, Vector), on el vector conte els valors de les respostes correctes
	 */
	public static Vector getSelectionConditions(Item oItem, Hashtable hResponses, Hashtable hOKResponses){
		Vector vConditions = new Vector();
		if (oItem!=null){			
			Hashtable hAnd = new Hashtable();
			Vector vAnds = new Vector();
			Enumeration enumResponses = hResponses.keys();
			while (enumResponses.hasMoreElements()){
				String sIdentResp = (String)enumResponses.nextElement();
				Object oResponses = hResponses.get(sIdentResp);
				Vector vResponses = new Vector();
				if (oResponses instanceof Vector){
					vResponses = (Vector)oResponses;
				} else if (oResponses instanceof String[]){
					String[] sResponses = (String[])oResponses;
					for (int i=0;i<sResponses.length;i++){
						vResponses.addElement(sResponses[i]);
					}
				}
				Vector vOKResponses = (Vector)hOKResponses.get(sIdentResp);
				if (vOKResponses!=null) {
					Hashtable hResponseContent = new Hashtable();
					hResponseContent.put(sIdentResp, vOKResponses);
					vAnds.addElement(hResponseContent);
				}
				Enumeration enumRespLabels = vResponses.elements();
				while (enumRespLabels.hasMoreElements()){
					String sRespLabel = (String)enumRespLabels.nextElement();
					if (vOKResponses==null || !vOKResponses.contains(sRespLabel)){
						Vector vRespLabel = new Vector();
						vRespLabel.addElement(sRespLabel);
						Hashtable hRespLabel = new Hashtable();
						hRespLabel.put(sIdentResp, vRespLabel);
						Hashtable hNot = new Hashtable();
						hNot.put(QTIObject.NOT.getAttributeName(), hRespLabel);
						vAnds.addElement(hNot);
					}
				}
			}
			hAnd.put(QTIObject.AND.getAttributeName(), vAnds);
			vConditions.addElement(hAnd);
		}
		return vConditions;
	}
	
	/**
	 * @param oItem
	 * @param hResponses Hashtable amb els parells (idResp, Vector), on el vector conte totes les respostes
	 * @param hOKResponses Hashtable amb els parells (idResp, Vector), on el vector conte els valors de les respostes correctes
	 */
	public static Vector getOrderedConditions(Item oItem, String sIdentResp, String[] vIdRespostes, String[] vOrdreRespostes){
		Vector vConditions = new Vector();
		if (oItem!=null){
			// Obtenir ordre de les respostes
			String sOrdre = "";
			if (vIdRespostes!=null){
				String[] vIdsOrdenats = new String[vIdRespostes.length];
				for(int i=0;i<vIdRespostes.length;i++){
					String sId = vIdRespostes[i];
					int iOrdre = Integer.parseInt(vOrdreRespostes[i])-1;
					if (iOrdre>=0 && iOrdre<vIdRespostes.length){
						vIdsOrdenats[iOrdre]=sId;
					}
				}
				for(int i=0;i<vIdsOrdenats.length;i++){
					if (i!=0){
						sOrdre += ",";
					}
					sOrdre += vIdsOrdenats[i];
				}
			}
			Hashtable hAnd = new Hashtable();
			Vector vAnds = new Vector();
			Hashtable hRespLabel = new Hashtable();
			Vector vRespLabel = new Vector();
			vRespLabel.addElement(sOrdre);
			hRespLabel.put(sIdentResp, vRespLabel);
			vAnds.addElement(hRespLabel);
			hAnd.put(QTIObject.AND.getAttributeName(), vAnds);
			vConditions.addElement(hAnd);
		}
		return vConditions;
	}
	
	/**
	 * @param vConditions Vector amb les condicions
	 * @return afegeix NOT a les condicions indicades
	 */
	protected static Vector getIncorrectConditions(Vector vConditions){
		Vector vNotConditions = new Vector();
		Hashtable hNot = new Hashtable();
		hNot.put(QTIObject.NOT.getAttributeName(), vConditions);
		vNotConditions.addElement(hNot);
		return vNotConditions;
	}
	
//	***************************	
//	* Materials
//	***************************
	public static Vector getMats(Vector vMaterials){
		Vector vMats = new Vector();
		if (vMaterials!=null){
			Enumeration enumMaterials = vMaterials.elements();
			while (enumMaterials.hasMoreElements()){
				Material oMaterial = (Material)enumMaterials.nextElement();
				vMats.addAll(oMaterial.getMats());
			}
		}
		return vMats;
	}
	
	public static Material getFirstMaterial(QTIObject oQTI){
		Material oMaterial = null;
		if (oQTI!=null){
			Vector vMaterials = null;
			if (oQTI instanceof QTISuperRender){
				vMaterials = ((QTISuperRender)oQTI).getMaterial();
			}
			if (vMaterials!=null && !vMaterials.isEmpty()){
				oMaterial = (Material)vMaterials.firstElement();
			}
		}
		return oMaterial;
	}

	public static QTISuperMat getFirstMat(QTIObject oQTI){
		return getMaterial(oQTI);
	}

	public static QTISuperMat getMaterial(QTIObject oQTI){
		QTISuperMat oMaterial = null;
		if (oQTI!=null){
			Vector vMaterials = null;
			if (oQTI instanceof FlowMat){
				vMaterials = ((FlowMat)oQTI).getAllMaterials();
			} else if (oQTI instanceof Material){
				vMaterials = ((Material)oQTI).getMaterials();
			} else if (oQTI instanceof Flow){
				Vector v = ((Flow)oQTI).getMaterials();
				if (!v.isEmpty()){
					vMaterials = ((Material)v.firstElement()).getMaterials();
				}
			}
			if (vMaterials!=null && !vMaterials.isEmpty()){
				QTIObject oMat = (QTIObject)vMaterials.firstElement();
				if (oMat instanceof QTISuperMat){
					oMaterial = (QTISuperMat)oMat;
				}
			}
		}
		return oMaterial;
	}
	
	
	public static Vector getMattext(Vector vMaterials){
		Vector vMattext = new Vector();
		if (vMaterials!=null){
			Enumeration enumMaterials = vMaterials.elements();
			while (enumMaterials.hasMoreElements()){
				Material oMaterial = (Material)enumMaterials.nextElement();
				vMattext.addAll(oMaterial.getMattext());
			}
		}
		return vMattext;
	}
	
	public static Vector getMatimage(Vector vMaterials){
		Vector vMatimage = new Vector();
		if (vMaterials!=null){
			Enumeration enumMaterials = vMaterials.elements();
			while (enumMaterials.hasMoreElements()){
				Material oMaterial = (Material)enumMaterials.nextElement();
				vMatimage.addAll(oMaterial.getMatimage());
			}
		}
		return vMatimage;
	}
	
	public static Matimage getMatimage(QTIObject oQTI){
		Matimage oMatimage = null;
		if (oQTI!=null){
			Object oMaterial = getMaterial(oQTI);
			if (oMaterial instanceof Matimage){
				oMatimage = (Matimage)oMaterial;
			}
		}
		return oMatimage;
	}
	
	public static Vector getMataudio(Vector vMaterials){
		Vector vMataudio = new Vector();
		if (vMaterials!=null){
			Enumeration enumMaterials = vMaterials.elements();
			while (enumMaterials.hasMoreElements()){
				Material oMaterial = (Material)enumMaterials.nextElement();
				vMataudio.addAll(oMaterial.getMataudio());
			}
		}
		return vMataudio;
	}
	
	public static FlowMat createFlowMatext(String sText){
		FlowMat oFlowMat = new FlowMat();
		if (sText!=null && sText.trim().length()>0){
			Enumeration enumLines = QTIUtil.getLines(sText);
			while (enumLines.hasMoreElements()){
				String sLine = (String)enumLines.nextElement();
				FlowMat oFlowMatLine = new FlowMat();
				oFlowMatLine.addMaterial(Material.createWithMattext(sLine));
				oFlowMat.addFlowMat(oFlowMatLine);
			}
		}else{
			oFlowMat.addMaterial(Material.createWithMattext(""));
		}
		return oFlowMat;
	}
	
	/**
	 * TODO: obtenir imagetype
	 * @param sURI path de la imatge
	 * @return
	 */
	public static FlowMat createFlowMatimage(String sURI, int iWidth, int iHeight){
		FlowMat oFlowMat = null;
		oFlowMat = new FlowMat();
		oFlowMat.addMaterial(Material.createWithMatimage("gif", sURI, iWidth, iHeight));
		return oFlowMat;
	}

	/**
	 * @param sURI path del so
	 * @return
	 */
	public static FlowMat createFlowMataudio(String sURI){
		FlowMat oFlowMat = null;
		oFlowMat = new FlowMat();
		oFlowMat.addMaterial(Material.createWithMataudio(sURI));
		return oFlowMat;
	}
	
	public static String getMaterialType(QTIObject oQTI){
		String sMaterialType = null;
		if (oQTI!=null){
			Vector vAllMaterials = null;
			if (oQTI instanceof FlowMat){
				vAllMaterials = ((FlowMat)oQTI).getAllMaterials();
			}else if (oQTI instanceof Material){
				vAllMaterials = ((Material)oQTI).getMats();
			}
			if (vAllMaterials!=null && !vAllMaterials.isEmpty()){
				if (vAllMaterials.firstElement() instanceof Mattext){
					sMaterialType = QTISuperMat.MATTEXT_CONTENT_TYPE;
				} else if (vAllMaterials.firstElement() instanceof Matimage){
					sMaterialType = QTISuperMat.MATIMAGE_CONTENT_TYPE;
				} else if (vAllMaterials.firstElement() instanceof Mataudio){
					sMaterialType = QTISuperMat.MATAUDIO_CONTENT_TYPE;
				}
			}
		}
		return sMaterialType;		
	}

//	***************************	
//	* Creacio
//	***************************

	public static boolean isInteractionswitch(Assessment oAssessment) {
		return isInteractionswitch(oAssessment, null, null);
	}
	
	public static boolean isInteractionswitch(Assessment oAssessment, Section oSection) {
		return isInteractionswitch(oAssessment, oSection, null);
	}
	
	public static boolean isInteractionswitch(Assessment oAssessment, Section oSection, Item oItem) {
		boolean bIsInteractionswitch = false;
		String sAssessmentInteraction = QTIUtil.getInteractionswitch(oAssessment);
		String sSectionInteraction = QTIUtil.getInteractionswitch(oSection);
		String sItemInteraction = QTIUtil.getInteractionswitch(oItem);
		if (sItemInteraction!=null){
			bIsInteractionswitch = !sItemInteraction.equalsIgnoreCase("No");
		}else{
			if (sSectionInteraction!=null){
				bIsInteractionswitch = !sSectionInteraction.equalsIgnoreCase("No");
			}else{
				bIsInteractionswitch = sAssessmentInteraction== null || !sAssessmentInteraction.equalsIgnoreCase("No");
			}
		}
		return bIsInteractionswitch;
	}

	public static String getInteractionswitch(QTIObject oQTI) {
		String sInteractionswitch = null;
		if (oQTI!=null){
			QTISuperControl oControl = getControl(oQTI);
			if (oControl!=null){
				sInteractionswitch = oControl.getInteractionswitch();
			}
		}
		return sInteractionswitch;
	}
	
	public static QTISuperControl getControl(QTIObject oQTI) {
		QTISuperControl oControl = null;
		if (oQTI!=null){
			if (oQTI instanceof Assessment){
				oControl = ((Assessment)oQTI).getAssessmentControl();
			}else if (oQTI instanceof Section){
				oControl = ((Section)oQTI).getSectionControl();
			}else if (oQTI instanceof Item){
				oControl = ((Item)oQTI).getItemControl();
			}
		}
		return oControl;
	}
	
	public static void setInteractionswitch(QTIObject oQTI, String sInteractionswitch){
		if (oQTI!=null){
			QTISuperControl oControl = null;
			if (oQTI instanceof Assessment){
				oControl = ((Assessment)oQTI).getAssessmentControl();
				if (oControl==null){
					oControl = new AssessmentControl();
					((Assessment)oQTI).setAssessmentControl((AssessmentControl)oControl);
				}
			}else if (oQTI instanceof Section){
				oControl = ((Section)oQTI).getSectionControl();
				if (oControl==null){
					oControl = new SectionControl();
					((Section)oQTI).setSectionControl((SectionControl)oControl);
				}
			}else if (oQTI instanceof Item){
				oControl = ((Item)oQTI).getItemControl();
				if (oControl==null){
					oControl = new ItemControl();
					((Item)oQTI).setItemControl((ItemControl)oControl);
				}
			}
			if (oControl!=null){
				oControl.setInteractionswitch(sInteractionswitch);
			}
		}
	}
	
	
//	***************************	
//	* Creacio
//	***************************
	/**
	 * @return arrel del document QTI (QTIQuestesInterop)
	 */
	public static QTIObject createQTIObject(String sQTIFile){
		QTIObject oQTI = null;
		Document doc = XMLUtil.open(sQTIFile);
		if (doc!=null){
			oQTI = new QuestestInterop(doc.getRootElement());
		}
		return oQTI;
	}
    
	/**
	 * @return arrel del document QTI buit (QTIQuestesInterop)
	 */
	public static QTIObject createEmptyQTIObject(String sIdent){
		QTIObject oQTI = new QuestestInterop(sIdent);
		return oQTI;
	}
    
    public static boolean createEmptyQTIFile(String sURL, String sNomQuadern){
		boolean bOk = true;
		try{
			if (sNomQuadern!=null && sNomQuadern.trim().length()>0){
				QTIObject oQTI = QTIUtil.createEmptyQTIObject(sNomQuadern);
				bOk = saveQuadern(sURL, oQTI);
			}
		}catch (Exception e){
			logger.error("EXCEPCIO creant el fitxer XML del quadern '"+sNomQuadern+"'");
			bOk = false;
		}
		return bOk;
    }

//	***************************	
//	* Utils
//	***************************
	public static String getRandomIdent(){
		long l = System.currentTimeMillis();
		int i=(int)(Math.random()*1000);
		return l+""+i;
	}	
	
	public static Enumeration getLines(String sParagraph){
		StringTokenizer stLines = new StringTokenizer(sParagraph,"\r\n");
		return stLines;
	}
	
	public static String getText(Vector vFlows){
		String sText = "";
		if (vFlows!=null && !vFlows.isEmpty()){
			Enumeration enumFlows = vFlows.elements();
			while (enumFlows.hasMoreElements()){
				QTIObject oFlow = (QTIObject)enumFlows.nextElement();
				if (oFlow instanceof FlowMat){
					sText = getText((FlowMat)oFlow);
				} else if (oFlow instanceof Flow){
					//TODO: implementar
				}
				sText+= "\n";
			}
		}
		return sText;
	}

	public static String getText(QTIObject oQTI){
		String sText = "";
		if (oQTI!=null){
			Vector vContents = new Vector();
			if (oQTI instanceof FlowMat){
				vContents = ((FlowMat)oQTI).getContents();
			}else if (oQTI instanceof Material){
				vContents = new Vector();
				vContents.addElement(oQTI);
			}else if (oQTI instanceof Presentation){
				vContents = ((Presentation)oQTI).getMaterials();
			}else if (oQTI instanceof Flow){
				vContents = ((Flow)oQTI).getMaterials();
			}else{
				vContents.addElement(oQTI);
			}
			Enumeration enumFlowContent = vContents.elements();
			while (enumFlowContent.hasMoreElements()){
				QTIObject oContent = (QTIObject)enumFlowContent.nextElement();
				if (oContent instanceof Material){
					//String sTmp = StringUtil.replace(((Material)oContent).getText(), "\n", "");
					String sTmp = ((Material)oContent).getText();
					sText+=StringUtil.trim(sTmp);
					//sText+=((Material)oContent).getText();
				} else if (oContent instanceof Mattext){
					sText += StringUtil.trim(((Mattext)oContent).getText());
				} else if (oContent instanceof FlowMat){
					sText+=getText((FlowMat)oContent);
					sText+="\n";
				} else if (oContent instanceof Flow){
					sText+=getText((Flow)oContent);
				}
			}
		}
		return sText;
	}
	
	public static String getURI(QTIObject oQTI){
		String sURI = null;
		if (oQTI!=null){
			Vector vMaterials = null;
			if (oQTI instanceof FlowMat){
				vMaterials = ((FlowMat)oQTI).getAllMaterials();
			} else if (oQTI instanceof Material){
				vMaterials = ((Material)oQTI).getMaterials();
			}
			if (vMaterials!=null && !vMaterials.isEmpty()){
				QTIObject oMat = (QTIObject)vMaterials.firstElement();
				if (oMat instanceof QTISuperMat){
					sURI = ((QTISuperMat)oMat).getURI();
				}
			}
		}
		return sURI;
	}
	
	public static Vector arrayToVector(Object[] oArray){
		return arrayToVector(oArray, false);
	}
	
	/**
	 * 
	 * @param oArray
	 * @param bFilter true si també s'han de substituir els caracters especials pel codi html equivalent
	 * @return
	 */
	public static Vector arrayToVector(Object[] oArray, boolean bFilter){
		Vector vVector = new Vector();
		if (oArray !=null){
			for (int i=0;i<oArray.length;i++){
				Object o = oArray[i];
				if (o instanceof String && bFilter){
					o = StringUtil.filterHTML((String)o);
				}
				vVector.addElement(o);
			}	
		}
		return vVector;	
	}
	
}
