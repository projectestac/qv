/*
 * OutcomesProcessing.java
 * 
 * Created on 22/març/2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

import edu.xtec.qv.qti.util.QTIConstant;

/**
 * @author sarjona
 */
public class OutcomesProcessing extends QTISuper {

	public OutcomesProcessing(String sScoreModel){
		setScoreModel(sScoreModel);
	}
	
	public OutcomesProcessing(Element eElement){
		createFromXML(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eOutcomesProcessing = new Element(getQTIConstant().getTagName());
		if (getAttribute(OUTCOMES)!=null){
			eOutcomesProcessing.addContent(getOutcomes().getXML());
		}		
		if (getAttribute(MAP_OUTPUT)!=null){
			Enumeration enumMapOutput = getMapOutput().elements();
			while (enumMapOutput.hasMoreElements()){
				MapOutput oMapOutput = (MapOutput)enumMapOutput.nextElement();
				eOutcomesProcessing.addContent(oMapOutput.getXML());
			}
		}
		if (getAttribute(SCOREMODEL)!=null){
			eOutcomesProcessing.setAttribute(SCOREMODEL.getTagName(), getAttributeValue(SCOREMODEL));
		}
		return eOutcomesProcessing;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		setAttribute(SCOREMODEL, eElement.getAttributeValue(SCOREMODEL.getTagName()));
			
		if (eElement.getChild(OUTCOMES.getTagName())!=null){
			Outcomes oOutcomes = new Outcomes(eElement.getChild(OUTCOMES.getTagName()));
			setAttribute(OUTCOMES, oOutcomes);
		}

		if (eElement.getChildren(MAP_OUTPUT.getTagName(), eElement.getNamespace())!=null){
			Vector vMapOutput = new Vector();
			Iterator itMapOutput = eElement.getChildren(MAP_OUTPUT.getTagName(), eElement.getNamespace()).iterator();
			while (itMapOutput.hasNext()){
				Element eMapOutput = (Element)itMapOutput.next();
				MapOutput oMapOutput = new MapOutput(eMapOutput);
				vMapOutput.addElement(oMapOutput);
			}
			setAttribute(MAP_OUTPUT, vMapOutput);
		}

	}
	
	public void setOutcomes(Outcomes oOutcomes){
		setAttribute(OUTCOMES, oOutcomes);
	}
	public Outcomes getOutcomes(){
		Outcomes oOutcomes = null;
		if (getAttribute(OUTCOMES)!=null){
			oOutcomes = (Outcomes)getAttribute(OUTCOMES);
		}
		return oOutcomes;
	}
	
	public Vector getMapOutput(){
		Vector vMapOutput = new Vector();
		if (getAttribute(MAP_OUTPUT)!=null){
			vMapOutput = getAttributeVectorValue(MAP_OUTPUT);
		}
		return vMapOutput;
	}
	public void addMapOutput(MapOutput oMapOutput){
		if (getAttribute(MAP_OUTPUT)==null){
			setAttribute(MAP_OUTPUT, new Vector());
		}
		setAttribute(MAP_OUTPUT, oMapOutput);
	}
	
	public String getScoreModel(){
		return getAttributeValue(SCOREMODEL);
	}
	public void setScoreModel(String sScoreModel){
		setAttribute(SCOREMODEL, sScoreModel);
	}

	public static QTIConstant getQTIConstant(){
		return OUTCOMES_PROCESSING;
	}

}
