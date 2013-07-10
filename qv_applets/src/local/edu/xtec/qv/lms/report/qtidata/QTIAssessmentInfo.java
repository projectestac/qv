package edu.xtec.qv.lms.report.qtidata;

import java.util.ArrayList;
import java.util.Iterator;

import java.io.InputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;

import edu.xtec.qv.lms.util.XMLUtility;

public class QTIAssessmentInfo{

	String id = "";
	String name = "";
	float maxScore = 0;
	String randomizableSections = "";
	ArrayList sections = new ArrayList();

	public QTIAssessmentInfo(){
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setMaxScore(float maxScore){
		this.maxScore = maxScore;
	}
	
	public float getMaxScore(){
		return maxScore;
	}
	
	public void setRandomizableSections(String randomizableSections){
		this.randomizableSections = randomizableSections;
	}
	
	public String getRandomizableSections(){
		return randomizableSections;
	}
	
	public void addSection(QTISectionInfo section){
		sections.add(section);
	}
	
	public void setSections(ArrayList sections){
		this.sections = sections;
	}
	
	public ArrayList getSections(){
		return sections;
	}
	
	public String toString(){
		String s = "QTIAssessmentInfo\n";
		s += "	id="+id+"\n";
		s += "	name="+name+"\n";
		s += "	maxScore="+maxScore+"\n";
		s += "	randomizableSections="+randomizableSections+"\n";
		for (int i=0; i<sections.size(); i++){
			QTISectionInfo si = (QTISectionInfo)sections.get(i);
			s += si.toString();
		}
		return s;
	}
	
	public static QTIAssessmentInfo getQTIAssessmentInfo(InputStream isXML) throws Exception{
		Document docXML = XMLUtility.getXMLDocument(isXML);
		return getQTIAssessmentInfo(docXML);
	}

	public static QTIAssessmentInfo getQTIAssessmentInfo(String sXML) throws Exception{
		Document docXML = XMLUtility.getXMLDocument(sXML);
		return getQTIAssessmentInfo(docXML);
	}

	public static QTIAssessmentInfo getQTIAssessmentInfo(Document docXML) throws Exception{
		QTIAssessmentInfo ai = new QTIAssessmentInfo();
		
		Element eQuestestinterop = docXML.getRootElement();
		Iterator itResultAssessment = eQuestestinterop.getDescendants(new ElementFilter("assessment"));	
		while (itResultAssessment.hasNext()){
			Element eAssessment = (Element)itResultAssessment.next();
		
			ai.setId(eAssessment.getAttributeValue("ident"));
			ai.setName(eAssessment.getAttributeValue("title"));
			ai.setMaxScore(0);

			Iterator itResultSections = eAssessment.getDescendants(new ElementFilter("section"));	
			int position = 1;
			while (itResultSections.hasNext()){
				Element eSection = (Element)itResultSections.next();

				QTISectionInfo si = QTISectionInfo.getQTISectionInfo(eSection);
				si.setPosition(position);
				String randomizableItems = getRandomizableItems(eQuestestinterop, si);
				si.setRandomizableItems(randomizableItems);
				ai.addSection(si);
				ai.setMaxScore(ai.getMaxScore() + si.getMaxScore());
				position++;
			}
		}
		String randomizable = getRandomizableSections(eQuestestinterop, ai);
		ai.setRandomizableSections(randomizable);
		return ai;
	}

	//////////////////////////////////////////////////// RANDOMIZABLE //////////////////////////////////////////////////// 
	private static String getRandomizableSections(Element eQuestestinterop, QTIAssessmentInfo ai){
		String randomizableSections=",";
		String[] sectionIds = getRandomizableSectionsId(eQuestestinterop);
		int j=1;
		for (int numSection=0; numSection<ai.getSections().size(); numSection++){
			QTISectionInfo si = (QTISectionInfo)(ai.getSections().get(numSection));
			String id = si.getId();
			int i=1;
			boolean found = false;
			while (i<=sectionIds.length && !found){
				String sectId = sectionIds[i-1];
				if (sectId.equals(id)){
					found=true;
					randomizableSections = randomizableSections+j+",";
				}
				i++;
			}
			j++;
		}
		return randomizableSections;
	}

	private static String[] getRandomizableSectionsId(Element eQuestestinterop){
		ArrayList sections = new ArrayList();

		Iterator itResultAssessment = eQuestestinterop.getDescendants(new ElementFilter("assessment"));	
		while (itResultAssessment.hasNext()){
			Element eAssessment = (Element)itResultAssessment.next();
			
			Iterator itSelectionOrdering = eAssessment.getChildren("selection_ordering").iterator();
			while (itSelectionOrdering.hasNext()){
				Element eSelectionOrdering = (Element)itSelectionOrdering.next();
				
				Iterator itSelectionMetadata = eSelectionOrdering.getDescendants(new ElementFilter("selection_metadata"));	
				while (itSelectionMetadata.hasNext()){
					Element eSelectionMetadata = (Element)itSelectionMetadata.next();
					String mdName = eSelectionMetadata.getAttributeValue("mdname");
					String mdOperator = eSelectionMetadata.getAttributeValue("mdoperator");
					String value = eSelectionMetadata.getText();
					
					if (mdName.equalsIgnoreCase("ident") && mdOperator.equalsIgnoreCase("EQ") && value!=null && value.trim().length()>0){
						sections.add(value);
					}
				}
			}
		}
		
		String[] aSections = new String[sections.size()];
		for (int i=0;i<sections.size();i++){
			aSections[i] = sections.get(i).toString();
		}
		return aSections;
	}

	private static String getRandomizableItems(Element eQuestestinterop, QTISectionInfo si){
		String randomizableItems = ",";
		String[] itemIds = getRandomizableItemsId(eQuestestinterop, si.getId());
		int j = 1;
		for (int numItem=0; numItem<si.getItems().size(); numItem++){
			QTIItemInfo ii = (QTIItemInfo)(si.getItems().get(numItem));
			String id = ii.getId();
			int i=1;
			boolean found = false;
			while (i<=itemIds.length && !found){
				String itemId = itemIds[i-1];
				if (itemId.equals(id)){
					found=true;
					randomizableItems = randomizableItems+j+",";
				}
				i++;
			}
			j++;
		}
		return randomizableItems;
	}

	private static String[] getRandomizableItemsId(Element eQuestestinterop, String idSection){
		ArrayList items = new ArrayList();

		Iterator itResultSection = eQuestestinterop.getDescendants(new ElementFilter("section"));	
		while (itResultSection.hasNext()){
			Element eSection = (Element)itResultSection.next();

			String id = eSection.getAttributeValue("ident");
			if (id.equals(idSection)){
				
				Iterator itSelectionMetadata = eSection.getDescendants(new ElementFilter("selection_metadata"));	
				while (itSelectionMetadata.hasNext()){
					Element eSelectionMetadata = (Element)itSelectionMetadata.next();
					String mdName = eSelectionMetadata.getAttributeValue("mdname");
					String mdOperator = eSelectionMetadata.getAttributeValue("mdoperator");
					String value = eSelectionMetadata.getText();
					
					if (mdName.equalsIgnoreCase("ident") && mdOperator.equalsIgnoreCase("EQ") && value!=null && value.trim().length()>0){
						items.add(value);
					}
				}
			}
		}
		
		String[] aItems = new String[items.size()];
		for (int i=0;i<items.size();i++){
			aItems[i] = items.get(i).toString();
		}
		return aItems;
	}
	
	
}