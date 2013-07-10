/*
 * Assessment.java
 * 
 * Created on 03/febrer/2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Vector;

import org.jdom.Element;

import edu.xtec.qv.qti.util.QTIConstant;

/**
 * @author sarjona
 */
public class Assessment extends QTISuper {
	
	public Assessment(String sIdent){
		setAttribute(IDENT, sIdent);
		setAttribute(SECTION, new Vector());
		setAttribute(QTIMETADATA, new Vector());
		setAttribute(OBJECTIVES, new Vector());
	}
	
	public Assessment(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eAssessment = new Element(getQTIConstant().getTagName());
		// Attributes			
		String sIdent = getIdent()!=null?getIdent():getRandomIdent();
		eAssessment.setAttribute(IDENT.getTagName(), sIdent);
		Object oTitle = getAttribute(TITLE);
		if (oTitle!=null) {
			eAssessment.setAttribute(TITLE.getTagName(), (String)oTitle);
		} 
		Object oLang = getAttribute(XMLLANG);
		if (oLang!=null) {
			eAssessment.setAttribute(XMLLANG.getTagName(), (String)oLang);
		} 
		// qticomment		
		addToXML(eAssessment, this, QTIComment.getQTIConstant());
		// duration			
		addToXML(eAssessment, this, Duration.getQTIConstant());		
		// qtimetadata		
		addToXML(eAssessment, this, QTIMetadata.getQTIConstant());	
		// objectives			
		addToXML(eAssessment, this, Objectives.getQTIConstant());		
		// assessmentcontrol			
		addToXML(eAssessment, this, AssessmentControl.getQTIConstant());		
		// rubric
		addToXML(eAssessment, this, Rubric.getQTIConstant());		
		//presentation_material
		addToXML(eAssessment, this, PresentationMaterial.getQTIConstant());		
		// outcomes_processing			
		addToXML(eAssessment, this, OutcomesProcessing.getQTIConstant());		
		// assessproc_extension			
		addToXML(eAssessment, this, AssessprocExtension.getQTIConstant());		
		// assessfeedback
		addToXML(eAssessment, this, AssessprocExtension.getQTIConstant());		
		// selection_ordering
		addToXML(eAssessment, this, SelectionOrdering.getQTIConstant());		
		// reference
		addToXML(eAssessment, this, Reference.getQTIConstant());		
		// section			
		addToXML(eAssessment, this, Section.getQTIConstant());
		
		addOrderingToXML(eAssessment);//Albert
		
		//System.gc();
		return eAssessment;
	}
	
	private void addOrderingToXML(Element eAssessment){
		Vector v = getSections();
		Enumeration enum1 = v.elements();
		while (enum1.hasMoreElements()){
			Section s = (Section)enum1.nextElement();
		}
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
			// assessmentcontrol
			addFromXML(eElement, this, AssessmentControl.getQTIConstant());
			// rubric
			addFromXML(eElement, this, Rubric.getQTIConstant());		
			// presentation_material
			addFromXML(eElement, this, PresentationMaterial.getQTIConstant());		
			// outcomes_processing
			addFromXML(eElement, this, OutcomesProcessing.getQTIConstant());		
			// assessproc_extension
			addFromXML(eElement, this, AssessprocExtension.getQTIConstant());		
			// assessfeedback
			addFromXML(eElement, this, Assessfeedback.getQTIConstant());		
			// selection_ordering
			addFromXML(eElement, this, SelectionOrdering.getQTIConstant());		
			// reference
			addFromXML(eElement, this, Reference.getQTIConstant());		
			// section
			addFromXML(eElement, this, Section.getQTIConstant());
		}
	}
	
	public String getLang(){
		return getAttributeValue(XMLLANG);
	}
	
	public QTIComment getQTIComment(){
		QTIObject o = getQTIObject(this, QTIComment.getQTIConstant());
		return (o!=null?(QTIComment)o:null);
	}
	
	public Duration getDuration(){
		QTIObject o = getQTIObject(this, Duration.getQTIConstant());
		return (o!=null?(Duration)o:null);
	}
	
	public Vector getQTIMetadata(){
		return getVector(this, QTIMetadata.getQTIConstant());
	}	
	public void addQTIMetadata(QTIMetadata oMetadata){
		add(this, oMetadata, QTIMetadata.getQTIConstant());
	}
	
	public Vector getObjectives(){
		return getVector(this, Objectives.getQTIConstant());
	}
	public void addObjectives(Objectives oObjectives){
		add(this, oObjectives, Objectives.getQTIConstant());
	}
		
	public AssessmentControl getAssessmentControl(){
		QTIObject o = getQTIObject(this, AssessmentControl.getQTIConstant());
		return (o!=null?(AssessmentControl)o:null);
	}
	
	public void setAssessmentControl(AssessmentControl oControl){
		add(this, oControl, AssessmentControl.getQTIConstant());
	}
	
	public Vector getRubric(){
		return getVector(this, Rubric.getQTIConstant());
	}
	public void addRubric(Rubric oRubric){
		add(this, oRubric, Rubric.getQTIConstant());
	}
	
	public PresentationMaterial getPresentationMaterial(){
		QTIObject o = getQTIObject(this, PresentationMaterial.getQTIConstant());
		return (o!=null?(PresentationMaterial)o:null);
	}
	
	public void setPresentationMaterial(PresentationMaterial oPresentationMaterial){
		add(this, oPresentationMaterial, PresentationMaterial.getQTIConstant());
	}
	
	public Vector getOutcomesProcessing(){
		return getVector(this, OutcomesProcessing.getQTIConstant());
	}
	public void setOutcomesProcessing(OutcomesProcessing oOutcomesProcessing){
		setAttribute(OUTCOMES_PROCESSING, new Vector());
		setAttribute(OUTCOMES_PROCESSING, oOutcomesProcessing);
	}
	public void addOutcomesProcessing(OutcomesProcessing oOutcomesProcessing){
		add(this, oOutcomesProcessing, OutcomesProcessing.getQTIConstant());
	}
		
	public AssessprocExtension getAssessprocExtension(){
		QTIObject o = getQTIObject(this, AssessprocExtension.getQTIConstant());
		return (o!=null?(AssessprocExtension)o:null);
	}
	public void setAssessprocExtension(AssessprocExtension oAssessprocExtension){
		add(this, oAssessprocExtension, AssessprocExtension.getQTIConstant());
	}

	public Vector getAssessfeedback(){
		return getVector(this, Assessfeedback.getQTIConstant());
	}
	public void addAssessfeedback(Assessfeedback oAssessfeedback){
		add(this, oAssessfeedback, Assessfeedback.getQTIConstant());
	}
	
	public Vector getSections(){
		return getAttributeVectorValue(SECTION);
	}
	
	public SelectionOrdering getSelectionOrdering(){
		QTIObject o = getQTIObject(this, SelectionOrdering.getQTIConstant());
		return (o!=null?(SelectionOrdering)o:null);
	}
	public void setSelectionOrdering(SelectionOrdering oSelectionOrdering){
		add(this, oSelectionOrdering, SelectionOrdering.getQTIConstant());
	}

	public Reference getReference(){
		QTIObject o = getQTIObject(this, Reference.getQTIConstant());
		return (o!=null?(Reference)o:null);
	}
	public void setReference(Reference oReference){
		add(this, oReference, Reference.getQTIConstant());
	}

	/**
	 * @param sIdSection identificador del full a retornar
	 * @return el full amb l'identificador indicat; si es null o no existeix l'identificador indicat es retorna el primer full
	 */
	public Section getSection(String sIdSection){
		Section oQTI = null;
		oQTI = (Section)getAttribute(SECTION, sIdSection);
		if (sIdSection==null || oQTI==null){
			Vector vSections = getAttributeVectorValue(SECTION);
			if (vSections!=null && !vSections.isEmpty()){
				oQTI = (Section)vSections.firstElement();
			}				
		}
		return oQTI;
	}
	
	/**
	 * @param iPosition posicio del full a retornar
	 * @return el full que es troba a la posicio indicada; null en cas que no hi hagi cap a la posicio indicada
	 */
	public Section getSection(int iPosition){
		Section oQTI = null;
		if (getSections()!=null && iPosition>=0 && iPosition<getSections().size()){
				oQTI = (Section)getSections().elementAt(iPosition);
		}
		return oQTI;
	}

	public void setSection(Section oSection){
		if (oSection!=null){
			if (containsSection(oSection.getIdent())){
				getSections().set(getSectionPosition(oSection.getIdent()),oSection);
			} else{
				addSection(oSection);
			}
		}
	}

	public void addSection(Section oSection){
		if (getAttribute(SECTION)==null){
			setAttribute(SECTION, new Vector());
		}
		setAttribute(SECTION, oSection);
	}
	public void delSection(Section oSection){
		delAttributeElement(SECTION, oSection);
	}
	
	/**
	 * Comproba si aquest quadern conté el full indicat
	 * @param sIdent identificador del full
	 * @return
	 */
	public boolean containsSection(String sIdent){
		boolean bContains = false;
		Enumeration enumSections = getSections().elements();
		while (enumSections.hasMoreElements()){
			Section oSection = (Section)enumSections.nextElement();
			if (oSection.getIdent().equals(sIdent)){
				bContains = true;
				break;
			}
		}
		return bContains;
	}

	/**
	 * @param sIdFull identificador del full
	 * @return la posicio dintre del quadern del full especificat
	 */
	public int getSectionPosition(String sIdFull){
		int iPosicio = 0;
		if (sIdFull!=null){
			iPosicio = getSections().indexOf(getSection(sIdFull));
		}
		return iPosicio;
	}
	public void setFullPosicio(Section oQTISection, int iOldPosition, int iNewPosition){
		Object oQTIObject = getSections().set(iNewPosition, oQTISection);
		getSections().set(iOldPosition, oQTIObject);
	}
	
	public final static QTIConstant getQTIConstant(){
		return ASSESSMENT;
	}
		
}
