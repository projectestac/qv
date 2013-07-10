/*
 * Item.java
 * 
 * Created on 05/febrer/2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.jdom.Element;

import edu.xtec.qv.qti.util.QTIConstant;
import edu.xtec.qv.qti.util.StringUtil;

/**
 * @author sarjona
 */
public class Item extends QTISuper{

	public Item(String sIdent){
		this(sIdent, null);
	}
	
 	public Item(String sIdent, String sTitle){
		setAttribute(IDENT, sIdent);
		setTitle(sTitle);
		setAttribute(RESPROCESSING, new Vector());
		setAttribute(ITEMFEEDBACK, new Vector());
	}
	
	public Item(Element eElement){
		createFromXML(eElement);
	}

	public String getIdent(){//Albert
		String s = super.getIdent();
		if (s==null){
			s = getRandomIdent();
			setIdent(s);
		}
		//System.out.println("item.getIdent():"+s);
		return s;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eItem = new Element(getQTIConstant().getTagName());
		// Attributes			
		String sIdent = getIdent()!=null?getIdent():getRandomIdent();
		eItem.setAttribute(IDENT.getTagName(), sIdent);
		if (getAttribute(TITLE)!=null) {
			eItem.setAttribute(TITLE.getTagName(), getTitle());
		} 
		if (getAttribute(XMLLANG)!=null) {
			eItem.setAttribute(XMLLANG.getTagName(), getLang());
		} 
		if (getAttribute(MAXATTEMPTS)!=null) {
			eItem.setAttribute(LABEL.getTagName(), getMaxattempts());
		} 
		if (getAttribute(LABEL)!=null) {
			eItem.setAttribute(LABEL.getTagName(), getAttributeValue(LABEL));
		} 

		// qticomment		
		addToXML(eItem, this, QTIComment.getQTIConstant());
		// duration			
		addToXML(eItem, this, Duration.getQTIConstant());		
		// objectives			
		addToXML(eItem, this, Objectives.getQTIConstant());		
		// itemcontrol			
		addToXML(eItem, this,ItemControl.getQTIConstant());		
		// rubric
		addToXML(eItem, this, Rubric.getQTIConstant());		
		//presentation
		addToXML(eItem, this, Presentation.getQTIConstant());		
		// resprocessing			
		addToXML(eItem, this, Resprocessing.getQTIConstant());		
		// itemproc_extension			
		addToXML(eItem, this, ItemprocExtension.getQTIConstant());		
		// itemfeedback
		addToXML(eItem, this, Itemfeedback.getQTIConstant());		
		return eItem;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			//Attributes
			setAttribute(IDENT, eElement.getAttributeValue(IDENT.getTagName()));
			setAttribute(TITLE, eElement.getAttributeValue(TITLE.getTagName()));
			setAttribute(XMLLANG, eElement.getAttributeValue(XMLLANG.getTagName()));
			setAttribute(MAXATTEMPTS, eElement.getAttributeValue(MAXATTEMPTS.getTagName()));
			setAttribute(LABEL, eElement.getAttributeValue(LABEL.getTagName()));

			// qticomment
			addFromXML(eElement, this, QTIComment.getQTIConstant());
			// duration
			addFromXML(eElement, this, Duration.getQTIConstant());		
			// objectives
			addFromXML(eElement, this, Objectives.getQTIConstant());		
			// itemcontrol
			addFromXML(eElement, this, ItemControl.getQTIConstant());
			// rubric
			addFromXML(eElement, this, Rubric.getQTIConstant());		
			// presentation
			addFromXML(eElement, this, Presentation.getQTIConstant());		
			// resprocessing
			addFromXML(eElement, this, Resprocessing.getQTIConstant());		
			// itemproc_extension
			addFromXML(eElement, this, ItemprocExtension.getQTIConstant());		
			// itemfeedback
			addFromXML(eElement, this, Itemfeedback.getQTIConstant());		
		}
	}
	
	public String getTitle(){
		return getAttributeValue(TITLE);
	}
	public void setTitle(String sTitle){
		setAttribute(TITLE, sTitle);
	}
	
	public String getLang(){
		return getAttributeValue(XMLLANG);
	}
	public void setLang(String sXMLLang){
		setAttribute(XMLLANG, sXMLLang);
	}
	
	public String getMaxattempts(){
		return getAttributeValue(MAXATTEMPTS);
	}
	public void setMaxattempts(String sMaxattempts){
		setAttribute(MAXATTEMPTS, sMaxattempts);
	}
	
	public QTIComment getQTIComment(){
		QTIComment oComment = null;
		if (getAttribute(QTICOMMENT)!=null){
			oComment = (QTIComment)getAttribute(QTICOMMENT);
		}
		return oComment;
	}
	
	public ItemControl getItemControl(){
		ItemControl oControl = null;
		Object tmpControl = getAttribute(ITEMCONTROL);
		if (tmpControl!=null){
			oControl = (ItemControl)tmpControl;
		}
		return oControl;
	}
	
	public void setItemControl(ItemControl oControl){
		setAttribute(ITEMCONTROL, oControl);
	}
	
	/**
	 * @param sText enunciat
	 */
	public void setStatement(String sText){
		Presentation oPresentation = getPresentation();
		if (oPresentation==null){
			oPresentation = new Presentation();
			setPresentation(oPresentation);
		}
		Flow oFlow = oPresentation.getFlow(); 
		if (oFlow==null){
			oFlow = new Flow();
			oPresentation.setFlow(new Flow());
		}
		
		// Comprobant si ja té enunciat
		String sHTML = StringUtil.textToHTML(sText);
		Material oMaterial = oPresentation.getMaterial(0);
		if (oMaterial!=null){
			Vector vMattext = oMaterial.getMattext();
			Mattext oMattext = null;
			if (vMattext!=null && !vMattext.isEmpty()){
				oMattext = (Mattext)vMattext.firstElement();
				oMattext.setText(sHTML);
			} else{
				oMattext = new Mattext(sHTML);
				oMaterial.addMaterial(oMattext);
			}
		}else{
			oMaterial = Material.createWithMattext(sHTML);
			oPresentation.addMaterial(oMaterial);
		}
	}
	
	public Vector getAdditionalMaterial(){
		Vector vAdditionalMats = new Vector();
		if (getPresentation()!=null){
			Material oMaterial = getPresentation().getMaterial(0);
			if (oMaterial!=null){
				Vector vMats = oMaterial.getMats();
				if (vMats!=null && !vMats.isEmpty()){
					// Eliminar enunciat
					vAdditionalMats = new Vector(vMats.subList(1, vMats.size()));
				}
			}
		}
		return vAdditionalMats;		
	}
	
	
	public Presentation getPresentation() {
		Presentation oPresentation = null;
		if (getAttribute(PRESENTATION)!=null){
			oPresentation = (Presentation)getAttribute(PRESENTATION);
		}
		return oPresentation;
	}
	public void setPresentation(Presentation oPresentation){
		setAttribute(PRESENTATION, oPresentation);
	}
	
	public Vector getResprocessing(){
		return getAttributeVectorValue(RESPROCESSING);
	}
	public void addResprocessing(Resprocessing oResprocessing){
		setAttribute(RESPROCESSING, oResprocessing);
	}
	
	public Respcondition getRespcondition(String sVarname, String sValue){ 
		Respcondition oRespcondition = null;
		Vector vResprocessing = getResprocessing();
		if (vResprocessing!=null && !vResprocessing.isEmpty()){
			Resprocessing oResprocessing = (Resprocessing)vResprocessing.firstElement();
			oRespcondition = oResprocessing.getRespcondition(sVarname, sValue);
		}
		return oRespcondition;
	}
	
	public Respcondition addRespcondition(String sVarname, String sValue){ 
		Respcondition oRespcondition = getRespcondition(sVarname, sValue);
		if (oRespcondition==null){
			Vector vResprocessing = getResprocessing();
			if (vResprocessing!=null && !vResprocessing.isEmpty()){
				Resprocessing oResprocessing = (Resprocessing)vResprocessing.firstElement();
				oRespcondition = new Respcondition();
				Conditionvar oConditionvar = new Conditionvar();
				oConditionvar.addCondition(new And());
				oRespcondition.setConditionvar(oConditionvar);
				oRespcondition.addSetvar(new Setvar(sVarname, "Set", sValue));
				oResprocessing.addRespcondition(oRespcondition);
			}
		}
		return oRespcondition;
	}
	
	public Vector getItemfeedback(){
		return getAttributeVectorValue(ITEMFEEDBACK);
	}
	public Itemfeedback getItemfeeback(String sIdItemfeedback){
		Itemfeedback oItemfeedback = null;
		Enumeration enumItemfeeback = getItemfeedback().elements();
		while (enumItemfeeback.hasMoreElements()){
			Itemfeedback oQTI = (Itemfeedback)enumItemfeeback.nextElement();
			if (oQTI.getIdent().equals(sIdItemfeedback)){
				oItemfeedback = oQTI;
				break;
			}
		}
		return oItemfeedback;
	}
	
	public Itemfeedback getItemfeebackByTitle(String sTitle){
		Itemfeedback oItemfeedback = null;
		if (sTitle!=null){
			Enumeration enumItemfeeback = getItemfeedback().elements();
			while (enumItemfeeback.hasMoreElements()){
				Itemfeedback oQTI = (Itemfeedback)enumItemfeeback.nextElement();
				if (sTitle.equals(oQTI.getTitle())){
					oItemfeedback = oQTI;
					break;
				}
			}
		}
		return oItemfeedback;
	}	
	
	public void addItemfeedback(Itemfeedback oItemfeedback){
		setAttribute(ITEMFEEDBACK, oItemfeedback);
	}
	
	/**
	 * 
	 * @param sIdent identificador del responseLID
	 * @param sCardinality cardinalitat (Single, Multiple)
	 * @param vResponses Vector amb les respostes
	 */
	public void addRenderChoiceWithMattext(String sIdent, String sCardinality, Vector vResponses, String sAlignment){
		Presentation oPresentation = getPresentation();
		if (oPresentation==null){
			oPresentation = new Presentation();
			oPresentation.setFlow(new Flow());
			setPresentation(oPresentation);
		}
		ResponseLID oResponseLID = (ResponseLID)oPresentation.getResponse(sIdent);
		if (oResponseLID==null){
			oResponseLID = ResponseLID.createRenderChoiceWithMattext(sIdent, sCardinality, vResponses, sAlignment);
			oPresentation.addResponse(oResponseLID);
		}else{
			//TODO: modificar les respostes que calgui
		}
	}
	
	public void addResponse(QTISuperResponse oResponse){
		if (oResponse!=null){
			if (oResponse.getIdent()==null){
				oResponse.setAttribute(IDENT, getRandomIdent());
			}
			Presentation oPresentation = getPresentation();
			if (oPresentation==null){
				oPresentation = new Presentation();
				setPresentation(oPresentation);
			}
			if (oPresentation.getFlow()==null){
				oPresentation.setFlow(new Flow());
			}
			if (!setResponse(oResponse)){
				oPresentation.addResponse(oResponse);
			}
		}
	}
	
	/**
	 * Afegeix la resposta indicada. Si ja existia la substitueix per la nova
	 * @param oResponse QTISuperResponse a afegir
	 */
	public boolean setResponse(QTISuperResponse oResponse){
		boolean bSet = false;
		if (getPresentation()!=null){
			if (getPresentation().getFlow()!=null){
				Enumeration enumContents = getPresentation().getFlow().getContents().elements();
				while (enumContents.hasMoreElements()){
					QTIObject oQTI = (QTIObject)enumContents.nextElement();
					if (oQTI instanceof QTISuperResponse){
						QTISuperResponse tmpResponse = (QTISuperResponse)oQTI;
						if (oResponse.getIdent().equalsIgnoreCase(tmpResponse.getIdent())){
							getPresentation().setResponse(tmpResponse, oResponse);
							bSet = true;
						}
					}
				}
			}
		}
		return bSet;
	}
	
	/**
	 * Afegeix la resposta indicada. Si ja existia la substitueix per la nova
	 * @param oResponse QTISuperResponse a afegir
	 */
	public void addResponseOld(QTISuperResponse oResponse){
		if (oResponse!=null){
			if (oResponse.getIdent()==null){
				oResponse.setAttribute(IDENT, getRandomIdent());
			}
			Presentation oPresentation = getPresentation();
			if (oPresentation==null){
				oPresentation = new Presentation();
				oPresentation.setFlow(new Flow());
				setPresentation(oPresentation);
			}
			Enumeration enumResponse = oPresentation.getResponses().elements();
			boolean bUpdated = false;
			while (enumResponse.hasMoreElements()){
				QTISuperResponse tmpResponse = (QTISuperResponse)enumResponse.nextElement();
				if (oResponse.getIdent().equalsIgnoreCase(tmpResponse.getIdent())){
					oPresentation.setResponse(tmpResponse, oResponse);
					bUpdated = true;
					break;
				}
			}
			if (!bUpdated){
				oPresentation.addResponse(oResponse);
			}
		}
	}
	
	/**
	 * 
	 * @param sIdent identificador del responseLID
	 * @param sCardinality cardinalitat (Single, Multiple)
	 * @param sAlignment alineacio de les respostes (en vertical, totes a la mateixa linia)
	 * @deprecated
	 */
	public RenderChoice addRenderChoice(String sIdentLID, String sCardinality, String sAlignment,
	  String sDisplay, String[] vIdResponses, String[] vTextResponses, String[] vImageResponses){
		Presentation oPresentation = getPresentation();
		if (oPresentation==null){
			oPresentation = new Presentation();
			oPresentation.setFlow(new Flow());
			setPresentation(oPresentation);
		}
		ResponseLID oResponseLID = (ResponseLID)oPresentation.getResponse(sIdentLID);
		if (oResponseLID==null){
			oResponseLID = new ResponseLID(sIdentLID, sCardinality, null);
			oPresentation.addResponse(oResponseLID);
		}
		return oResponseLID.addRenderChoice(sAlignment);
	}
	
	public void addResponseCondition(Outcomes oOutcomes, Conditionvar oCondVar){
		if (oCondVar!=null){
			Resprocessing oResprocessing = null;
			Vector vResprocessing = new Vector();
			if (getAttribute(RESPROCESSING)!=null && !getAttributeVectorValue(RESPROCESSING).isEmpty()){
				vResprocessing = getAttributeVectorValue(RESPROCESSING);
				oResprocessing = (Resprocessing)vResprocessing.firstElement();
			}else{
				oResprocessing = new Resprocessing();
				vResprocessing.addElement(oResprocessing);
				oResprocessing.setOutcomes(oOutcomes);
			}
			Respcondition oRespcondition = new Respcondition();
			oRespcondition.addSetvar(new Setvar("SCORE","Add", "1"));
			oRespcondition.addSetvar(new Setvar("CORRECT","Set", "True"));
			oRespcondition.setConditionvar(oCondVar);
			oResprocessing.addRespcondition(oRespcondition);
			setAttribute(RESPROCESSING, vResprocessing);
		}
	}

	/**
	 * 
	 * @param vConditions Vector amb els operadors i condicions
	 * @param hSetvars Hashtable amb els parells (action, hashtable(varname, value))
	 * @param sFeedback text del feedback
	 */
	public void addResponseCondition(Outcomes oOutcomes, Vector vConditions, Hashtable hSetvars, String sFeedback){
		Resprocessing oResprocessing = null;
		Vector vResprocessing = new Vector();
		if (getAttribute(RESPROCESSING)!=null && !getAttributeVectorValue(RESPROCESSING).isEmpty()){
			vResprocessing = getAttributeVectorValue(RESPROCESSING);
			oResprocessing = (Resprocessing)vResprocessing.firstElement();
		}else{
			oResprocessing = new Resprocessing();
			vResprocessing.addElement(oResprocessing);
			oResprocessing.setOutcomes(oOutcomes);
		}
		Hashtable hSets = hSetvars.containsKey("Set")?(Hashtable)hSetvars.get("Set"):new Hashtable();
		Hashtable hAdds = hSetvars.containsKey("Add")?(Hashtable)hSetvars.get("Add"):new Hashtable();
		String sVarname = "CORRECT";
		String sValue = hSets.containsKey(sVarname)?(String)hSets.get(sVarname):"";
		Respcondition tmpRespcondition = oResprocessing.getRespcondition(sVarname, sValue);
		if (tmpRespcondition!=null){
			//Actualitzar respcondition
			Conditionvar oConditionvar = Conditionvar.create(vConditions);
			tmpRespcondition.setConditionvar(oConditionvar);
			String sScore = hAdds.containsKey("SCORE")?(String)hAdds.get("SCORE"):"";
			tmpRespcondition.setSetvar("SCORE", sScore);
			if (tmpRespcondition.getDisplayfeedback()!=null && !tmpRespcondition.getDisplayfeedback().isEmpty()){
				Displayfeedback oDisplayfeedback = (Displayfeedback)tmpRespcondition.getDisplayfeedback().firstElement();
				String sIdentFeedback = oDisplayfeedback.getLinkrefid();
				Itemfeedback oItemfeedback = getItemfeeback(sIdentFeedback);
				if (oItemfeedback!=null){
					//Actualitzar itemfeedback
					oItemfeedback.setText(sFeedback);
				}else{
					//Crear itemfeedback
					oItemfeedback = Itemfeedback.createWithMattext(null, sFeedback);
					addItemfeedback(oItemfeedback);
				}
			}
		}else{
			//Crear itemfeedback
			Itemfeedback oItemfeedback = Itemfeedback.createWithMattext(null, sFeedback);
			addItemfeedback(oItemfeedback);
			String sIdentFeedback = oItemfeedback.getIdent();
			//Crear respcondition
			Respcondition oRespcondition = Respcondition.create(vConditions, hSetvars, sIdentFeedback);
			oResprocessing.addRespcondition(oRespcondition);
		}		
		setAttribute(RESPROCESSING, vResprocessing);
	}
	
	public void addHint(String sHint){
		Itemfeedback oItemfeedback = getItemfeebackByTitle("hint");
		if (oItemfeedback!=null){
			//Actualitzar itemfeedback
			oItemfeedback.setText(sHint);
		}else{
			//Crear itemfeedback
			oItemfeedback = Itemfeedback.createWithMattext(null, sHint);
			oItemfeedback.setTitle("hint");
			addItemfeedback(oItemfeedback);
		}
		
	}

	/**
	 * Crea un item amb identificador, titol i enunciat
	 * @param sIdent identificador de l'item
	 * @param sTitle titol de l'item
	 * @param sText enunciat
	 * @return
	 */
	public static Item createItem(String sIdent, String sTitle, String sText){
		Item oItem = new Item(sIdent, sTitle);
		Presentation oPresentation = new Presentation();
		oPresentation.setFlow(new Flow());
		String sHTML = StringUtil.textToHTML(sText);
		oPresentation.addMaterial(Material.createWithMattext(sHTML));
		oItem.setPresentation(oPresentation);
		return oItem;
	}

	public String getShowName(){
		String sShowName = getTitle();
		if (sShowName==null || sShowName.length()==0){
			if (getPresentation()!=null){
				sShowName = getPresentation().getShowName();
			} 
			if (sShowName==null || sShowName.length()==0){
				sShowName = getIdent();
			}
		}
		return sShowName;
	}
	
	/**
	 * 
	 * @return Vector de String amb les respostes d'aquesta pregunta
	 */
	public Vector getResponses(){
		Vector vResponses = new Vector();
		Enumeration enumResponseLID = getPresentation().getResponses().elements();
		while (enumResponseLID.hasMoreElements()){
			ResponseLID oResponseLID = (ResponseLID)enumResponseLID.nextElement();
			QTIObject oRender = oResponseLID.getRender();
			if (oRender instanceof RenderChoice){
				RenderChoice oRenderChoice = (RenderChoice)oRender;
				Enumeration enumResponses = oRenderChoice.getResponseLabel().elements();
				while (enumResponses.hasMoreElements()){
					ResponseLabel oResponse = (ResponseLabel)enumResponses.nextElement();
					Vector vMaterial = oResponse.getMaterials();
					if (vMaterial!=null && !vMaterial.isEmpty()){
						Material oMaterial = (Material)vMaterial.firstElement();
						vResponses.addElement(oMaterial.getText());
					}
				}
			}
		}
		return vResponses;
	}
	
	public Vector getDisplayFeedbacks(){
		Vector vFeedbacks = new Vector();
		Enumeration enumResprocessing = getResprocessing().elements();
		while (enumResprocessing.hasMoreElements()){
			Resprocessing oResprocessing = (Resprocessing)enumResprocessing.nextElement();
			Enumeration enumRespcondition = oResprocessing.getRespcondition().elements();
			while (enumRespcondition.hasMoreElements()){
				Respcondition oRespcondition = (Respcondition)enumRespcondition.nextElement();
				Enumeration enumDisplayFeedback = oRespcondition.getDisplayfeedback().elements();
				while (enumDisplayFeedback.hasMoreElements()){
					Displayfeedback oDisplayfeedback = (Displayfeedback)enumDisplayFeedback.nextElement();
					vFeedbacks.addElement(oDisplayfeedback);
				}
			}
		}
		return vFeedbacks;
	}
	
	/**
	 * 
	 * @param oItem
	 * @return feedback de la resposta en que la variable indicada té el valor indicat
	 */
	public String getFeedback( String sVarname, String sValue){
		String sFeedback = null;
		try{
			Respcondition oRespcondition = getRespcondition(sVarname,sValue);
			if (oRespcondition!=null){
				Vector vFeedbacks = oRespcondition.getDisplayfeedback();
				if (vFeedbacks!=null && !vFeedbacks.isEmpty()){
					Displayfeedback oDisplayfeedback = (Displayfeedback)vFeedbacks.firstElement();
					Itemfeedback oItemfeedback = getItemfeeback(oDisplayfeedback.getLinkrefid());
					if (oItemfeedback!=null){
						sFeedback = oItemfeedback.getText();
					}
				}
			}
		}catch (Exception e){
			logger.error("EXCEPCIO obtenint feedback -> e="+e);
			e.printStackTrace();
		}
		return sFeedback;
	}
	
	public final static QTIConstant getQTIConstant(){
		return ITEM;
	}

	
	
}
