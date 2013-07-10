/*
 * Respcondition.java
 * 
 * Created on 10/febrer/2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class Respcondition extends QTISuper {

	public Respcondition(){
		setAttribute(SETVAR, new Vector());
		setAttribute(DISPLAYFEEDBACK, new Vector());
	}
	
	public Respcondition(Element eElement){
		createFromXML(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eRespcondition = new Element(RESPCONDITION.getTagName());
		if (getAttribute(CONTINUE)!=null){
			eRespcondition.setAttribute(CONTINUE.getTagName(), getAttributeValue(CONTINUE));
		}
		if (getAttribute(CONDITIONVAR)!=null){
			Conditionvar oConditionvar = (Conditionvar)getAttribute(CONDITIONVAR);
			eRespcondition.addContent(oConditionvar.getXML()); 
		}
		if (getAttribute(SETVAR)!=null){
			Vector vSetvar  = getAttributeVectorValue(SETVAR);
			Enumeration enumSetvar = vSetvar.elements();
			while (enumSetvar.hasMoreElements()){
				QTIObject oSetvar = (QTIObject)enumSetvar.nextElement();
				eRespcondition.addContent(oSetvar.getXML());
			}
		}
		if (getAttribute(DISPLAYFEEDBACK)!=null){
			Vector vDisplayfeedback  = getAttributeVectorValue(DISPLAYFEEDBACK);
			Enumeration enumDecvar = vDisplayfeedback.elements();
			while (enumDecvar.hasMoreElements()){
				QTIObject oDisplayfeedback = (QTIObject)enumDecvar.nextElement();
				eRespcondition.addContent(oDisplayfeedback.getXML());
			}
		}
		return eRespcondition;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement.getChild(CONDITIONVAR.getTagName(), eElement.getNamespace())!=null){
			Conditionvar oConditionvar = new Conditionvar(eElement.getChild(CONDITIONVAR.getTagName()));
			setAttribute(CONDITIONVAR, oConditionvar);
		}
		Vector vSetvar = new Vector();
		Iterator itSetvar = eElement.getChildren(SETVAR.getTagName(), eElement.getNamespace()).iterator();
		while (itSetvar.hasNext()){
			Element eSetvar = (Element)itSetvar.next();
			Setvar oSetvar = new Setvar(eSetvar);
			vSetvar.addElement(oSetvar);
		}
		setAttribute(SETVAR, vSetvar);

		Vector vDisplayfeedback = new Vector();
		Iterator itDisplayfeedback = eElement.getChildren(DISPLAYFEEDBACK.getTagName(), eElement.getNamespace()).iterator();
		while (itDisplayfeedback.hasNext()){
			Element eDisplayfeedback = (Element)itDisplayfeedback.next();
			Displayfeedback oDisplayfeedback = new Displayfeedback(eDisplayfeedback);
			vDisplayfeedback.addElement(oDisplayfeedback);
		}
		setAttribute(DISPLAYFEEDBACK, vDisplayfeedback);
		setAttribute(CONTINUE, eElement.getAttributeValue(CONTINUE.getTagName()));
	}
	
	public Conditionvar getConditionvar(){
		return (Conditionvar)getAttribute(CONDITIONVAR);
	}
	public void setConditionvar (Conditionvar oConditionvar){
		setAttribute(CONDITIONVAR, oConditionvar);
	}
	
	public Vector getSetvar(){
		return getAttributeVectorValue(SETVAR);
	}
	public Setvar getSetvar(String sVarname){
		Setvar oSetvar = null;
		if (sVarname!=null){
			Enumeration enumSetvars = getSetvar().elements();
			while (enumSetvars.hasMoreElements()){
				Setvar tmpSetvar = (Setvar)enumSetvars.nextElement();
				if (sVarname.equalsIgnoreCase(tmpSetvar.getVarname())){
					oSetvar = tmpSetvar;
					break;
				}
			}
		}
		return oSetvar;
	}
	public void setSetvar(String sVarname, String sValue){
		Setvar oSetvar = getSetvar(sVarname);
		if (oSetvar!=null){
			oSetvar.setText(sValue);
		}
	}
	public void addSetvar(Setvar oSetvar){
		setAttribute(SETVAR, oSetvar);
	}
	public boolean containsSetvar(String sVarname, String sValue){
		boolean bContains = false;
		Setvar oSetvar = getSetvar(sVarname);
		if (oSetvar!=null){
			if (sValue==null && (oSetvar.getText()==null || oSetvar.getText().trim().length()==0)){
				bContains = true;
			}else if (sValue!=null){
				bContains = sValue.equalsIgnoreCase(oSetvar.getText());
			}
		}
		return bContains;
	}
	
	public Vector getDisplayfeedback(){
		return getAttributeVectorValue(DISPLAYFEEDBACK);
	}
	public void addDisplayfeedback(Displayfeedback oDisplayfeedback){
		setAttribute(DISPLAYFEEDBACK, oDisplayfeedback);
	}
	
	public static Respcondition create(Vector vConditions, Hashtable hSetvars, String sIdentFeedback){
		Respcondition oRespcondition = new Respcondition();
		Conditionvar oConditionvar = Conditionvar.create(vConditions);
		oRespcondition.setConditionvar(oConditionvar);
		if (hSetvars!=null){
			Enumeration enumActions = hSetvars.keys();
			while (enumActions.hasMoreElements()){
				String sAction = (String)enumActions.nextElement();
				Hashtable hAction = (Hashtable)hSetvars.get(sAction);
				if (hAction!=null){
					Enumeration enumVars = hAction.keys();
					while (enumVars.hasMoreElements()){
						String sVarname = (String)enumVars.nextElement();
						String sText = (String)hAction.get(sVarname);
						Setvar oSetvar = new Setvar(sVarname, sAction, sText);
						oRespcondition.addSetvar(oSetvar);
					}
				}
			}
		}
		if (sIdentFeedback!=null){
			Displayfeedback oDisplayfeedback = new Displayfeedback(null, sIdentFeedback);
			oRespcondition.addDisplayfeedback(oDisplayfeedback);	
		}
		return oRespcondition;
	}

}
