/*
 * QuestesInterop.java
 * 
 * Created on 03/febrer/2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class QuestestInterop extends QTISuper {
	
	public QuestestInterop(String sIdent){
		setAttribute(IDENT, sIdent);
		Assessment oAssessment = new Assessment(getIdent());
		setAttribute(ASSESSMENT, oAssessment);
	}
	
	public QuestestInterop(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eQuestesInterop = new Element(QUESTESTINTEROP.getTagName());
		if (getAttribute(ASSESSMENT)!=null){
			Assessment oAssessment = (Assessment)getAttribute(ASSESSMENT);
			eQuestesInterop.addContent(oAssessment.getXML()); 
		} 

		Object oSections = getAttribute(SECTION);
		if (oSections!=null){
			Enumeration enumSection = ((Vector)oSections).elements();
			while (enumSection.hasMoreElements()){
				Section oSection = (Section)enumSection.nextElement();
				eQuestesInterop.addContent(oSection.getXML());
			}
		}
		Object oItems = getAttribute(ITEM);
		if (oItems!=null){
			Enumeration enumItems = ((Vector)oItems).elements();
			while (enumItems.hasMoreElements()){
				Item oItem = (Item)enumItems.nextElement();
				eQuestesInterop.addContent(oItem.getXML());
			}
		}
		return eQuestesInterop;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			if (eElement.getChild(ASSESSMENT.getTagName(), eElement.getNamespace())!=null){
				Assessment oAssessment = new Assessment(eElement.getChild(ASSESSMENT.getTagName()));
				setAttribute(ASSESSMENT, oAssessment);
			} else{
				Vector vSections = new Vector();
				Iterator itSections = eElement.getChildren(SECTION.getTagName(), eElement.getNamespace()).iterator();
				while (itSections.hasNext()){
					Element eSection = (Element)itSections.next();
					Section oSection = new Section(eSection);
					vSections.addElement(oSection);
				}
				setAttribute(SECTION, vSections);

				Vector vItems = new Vector();
				Iterator itItems = eElement.getChildren(ITEM.getTagName(), eElement.getNamespace()).iterator();
				while (itItems.hasNext()){
					Element eItem = (Element)itItems.next();
					Item oItem = new Item(eItem);
					vItems.addElement(oItem);
				}
				setAttribute(ITEM, vItems);
			}
		}	
	}
	
	/**
	 * @param sIdAssessment identificador del quadern a retornar
	 * @return el quadern amb l'identificador de quadern indicat; si es null o no existeix l'identificador indicat es retorna el primer quadern
	 */
	public Assessment getAssessment(){
		Assessment oQTI = null;
		Object oObject = getAttribute(QTIObject.ASSESSMENT);
		if (oObject!=null){
			oQTI = (Assessment)oObject;
		}
		return oQTI;
	}
	
}
