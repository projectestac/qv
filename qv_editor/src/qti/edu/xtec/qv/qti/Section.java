/*
 * Section.java
 * 
 * Created on 03/febrer/2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Vector;

import org.jdom.Element;

import edu.xtec.qv.qti.Section;
import edu.xtec.qv.qti.util.QTIConstant;

/**
 * @author sarjona
 */
public class Section extends QTISuper{

	private String sFullOrder;
	
	public Section(String sIdent, String sTitle){
		setAttribute(IDENT, sIdent);
		setAttribute(TITLE, sTitle);
		setAttribute(ITEM, new Vector());
		setAttribute(QTIMETADATA, new Vector());
	}
	
	public Section(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		return getXML(true);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML(boolean bIncludeItems) {
		Element eSection = new Element(getQTIConstant().getTagName());
		// Attributes			
		String sIdent = getIdent()!=null?getIdent():getRandomIdent();
		eSection.setAttribute(IDENT.getTagName(), sIdent);
		if (getAttribute(TITLE)!=null) {
			eSection.setAttribute(TITLE.getTagName(), getTitle());
		} 
		if (getAttribute(XMLLANG)!=null) {
			eSection.setAttribute(XMLLANG.getTagName(), getLang());
		} 

		// qticomment		
		addToXML(eSection, this, QTIComment.getQTIConstant());
		// duration			
		addToXML(eSection, this, Duration.getQTIConstant());		
		// qtimetadata		
		addToXML(eSection, this, QTIMetadata.getQTIConstant());	
		// objectives			
		addToXML(eSection, this, Objectives.getQTIConstant());		
		// sectioncontrol			
		addToXML(eSection, this, SectionControl.getQTIConstant());		
		// rubric
		addToXML(eSection, this, Rubric.getQTIConstant());		
		//presentation_material
		addToXML(eSection, this, PresentationMaterial.getQTIConstant());		
		// outcomes_processing			
		addToXML(eSection, this, OutcomesProcessing.getQTIConstant());		
		// sectionproc_extension			
		addToXML(eSection, this, SectionprocExtension.getQTIConstant());		
		// sectionfeedback
		addToXML(eSection, this, Sectionfeedback.getQTIConstant());		
		// selection_ordering
		addToXML(eSection, this, SelectionOrdering.getQTIConstant());		
		// reference
		addToXML(eSection, this, Reference.getQTIConstant());		
		// item			
		if (bIncludeItems){
			addToXML(eSection, this, Item.getQTIConstant());
		}
		return eSection;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			// Attributes
			setAttribute(IDENT, eElement.getAttributeValue(IDENT.getTagName()));
			setAttribute(TITLE, eElement.getAttributeValue(TITLE.getTagName()));
			setAttribute(XMLLANG, eElement.getAttributeValue(XMLLANG.getTagName()));
			// qticomment
			addFromXML(eElement, this, QTIComment.getQTIConstant());
			// duration
			addFromXML(eElement, this, Duration.getQTIConstant());		
			// qtimetadata	
			addFromXML(eElement, this, QTIMetadata.getQTIConstant());
			// objectives
			addFromXML(eElement, this, Objectives.getQTIConstant());		
			// sectioncontrol
			addFromXML(eElement, this, SectionControl.getQTIConstant());
			// rubric
			addFromXML(eElement, this, Rubric.getQTIConstant());		
			// presentation_material
			addFromXML(eElement, this, PresentationMaterial.getQTIConstant());		
			// outcomes_processing
			addFromXML(eElement, this, OutcomesProcessing.getQTIConstant());		
			// sectionproc_extension
			addFromXML(eElement, this, SectionprocExtension.getQTIConstant());		
			// sectionfeedback
			addFromXML(eElement, this, Sectionfeedback.getQTIConstant());		
			// selection_ordering
			addFromXML(eElement, this, SelectionOrdering.getQTIConstant());		
			// reference
			addFromXML(eElement, this, Reference.getQTIConstant());		
			// item
			addFromXML(eElement, this, Item.getQTIConstant());
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
	
	public QTIComment getQTIComment(){
		QTIComment oComment = null;
		if (getAttribute(QTICOMMENT)!=null){
			oComment = (QTIComment)getAttribute(QTICOMMENT);
		}
		return oComment;
	}
	
	public Vector getQTIMetadata(){
		return getAttributeVectorValue(QTIMETADATA);
	}
	
	public void addQTIMetadata(QTIMetadata oMetadata){
		setAttribute(QTIMETADATA, oMetadata);
	}
	
	public SectionControl getSectionControl(){
		SectionControl oControl = null;
		Object tmpControl = getAttribute(SECTIONCONTROL);
		if (tmpControl!=null){
			oControl = (SectionControl)tmpControl;
		}
		return oControl;
	}
	
	public void setSectionControl(SectionControl oControl){
		setAttribute(SECTIONCONTROL, oControl);
	}
	
	public Vector getOutcomesProcessing(){
		return getAttributeVectorValue(OUTCOMES_PROCESSING);
	}
	public void setOutcomesProcessing(OutcomesProcessing oOutcomesProcessing){
		setAttribute(OUTCOMES_PROCESSING, new Vector());
		setAttribute(OUTCOMES_PROCESSING, oOutcomesProcessing);
	}
	public void addOutcomesProcessing(OutcomesProcessing oOutcomesProcessing){
		if (getAttribute(OUTCOMES_PROCESSING)==null){
			setAttribute(OUTCOMES_PROCESSING, new Vector());
		}
		setAttribute(OUTCOMES_PROCESSING, oOutcomesProcessing);
	}
	
	public PresentationMaterial getPresentationMaterial(){
		PresentationMaterial oPresentationMat = null;
		if (getAttribute(PRESENTATION_MATERIAL)!=null){
			oPresentationMat = (PresentationMaterial)getAttribute(PRESENTATION_MATERIAL);
		}
		return oPresentationMat;
	}
	public void setPresentationMaterial(PresentationMaterial oPresentationMat){
		setAttribute(PRESENTATION_MATERIAL, oPresentationMat);
	}
	
	public Vector getItems(){
		return getAttributeVectorValue(ITEM);
	}
	public void setItems(Vector vItems){
		setAttribute(ITEM, vItems);
	}
	
	/**
	 * @param sIdItem identificador de la pregunta a retornar
	 * @return la pregunta amb l'identificador indicat; si es null o no existeix l'identificador indicat es retorna la primera pregunta
	 */
	public Item getItem(String sIdItem){
		Item oQTI = null;
		oQTI = (Item)getAttribute(ITEM, sIdItem);
		if (sIdItem==null || oQTI==null){
			Vector vItems = getAttributeVectorValue(ITEM);
			if (vItems!=null && !vItems.isEmpty()){
				oQTI = (Item)vItems.firstElement();
			}				
		}
		return oQTI;
	}
	
	/**
	 * @param sIdPregunta identificador de la pregunta
	 * @return la posicio dintre del full de la pregunta especificada
	 */
	public int getItemPosition(String sIdPregunta){
		int iPosicio = 0;
		if (sIdPregunta!=null){
			iPosicio = getItems().indexOf(getItem(sIdPregunta));
		}
		return iPosicio;
	}
	
	public void setItemPosition(Item oQTIItem, int iOldPosition, int iNewPosition){
		setObjectPosition(getItems(), oQTIItem, iOldPosition, iNewPosition);
	}
	
	public void addItem(Item oItem){
		setAttribute(ITEM, oItem);
	}
	public void addItem(Item oItem, int iPosition){
		if (oItem!=null){
			getItems().insertElementAt(oItem, iPosition);
		}
		//setAttribute(ITEM, oItem);
	}
	public void setItem(Item oItem){
		if (oItem!=null){
			if (containsItem(oItem.getIdent())){
				getItems().set(getItemPosition(oItem.getIdent()),oItem);
			} else{
				addItem(oItem);
			}
		}
	}
	public void delItem(Item oItem){
		getItems().removeElement(oItem);
	}
	
	/**
	 * Comproba si aquest full conté la pregunta indicada
	 * @param sIdent identificador de la pregunta
	 * @return
	 */
	public boolean containsItem(String sIdent){
		boolean bContains = false;
		Enumeration enumItems = getItems().elements();
		while (enumItems.hasMoreElements()){
			Item oItem = (Item)enumItems.nextElement();
			if (oItem.getIdent().equals(sIdent)){
				bContains = true;
				break;
			}
		}
		return bContains;
	}

	public SelectionOrdering getSelectionOrdering(){
		QTIObject o = getQTIObject(this, SelectionOrdering.getQTIConstant());
		return (o!=null?(SelectionOrdering)o:null);
	}
	public void setSelectionOrdering(SelectionOrdering oSelectionOrdering){
		add(this, oSelectionOrdering, SelectionOrdering.getQTIConstant());
	}


	public final static QTIConstant getQTIConstant(){
		return SECTION;
	}
	
	/*public void setFullOrder(String sFullOrder){ //Albert
		System.out.println("sFullOrder:"+sFullOrder);
		this.sFullOrder=sFullOrder;
	}*/
	/*public boolean isRandomFullOrder(){
		return sFullOrder!=null && sFullOrder.equalsIgnoreCase(RANDOM_ORDER);
	}*/
	
	/*public String getFullOrder(Assessment oAssessment){
		return edu.xtec.qv.editor.util.QTIUtil.getSectionRandomOrder(oAssessment, this);
		//return sFullOrder;
	}*/
	
	
	public static final String RANDOM_ORDER="random";
	public static final String NO_RANDOM_ORDER="no_random";
}
