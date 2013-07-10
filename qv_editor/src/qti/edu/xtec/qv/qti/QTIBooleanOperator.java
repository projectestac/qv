/*
 * QTIBooleanOperator.java
 * 
 * Created on 10/febrer/2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

/**
 * @author sarjona
 */
public abstract class QTIBooleanOperator extends QTISuper {

	public QTIBooleanOperator(){
		setAttribute(CONTENT, new Vector());
	}
	
	public QTIBooleanOperator(Element eElement){
		createFromXML(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eOperator = new Element(getOperatorTag());
		Enumeration enumContents = ((Vector)getAttribute(CONTENT)).elements();
		while (enumContents.hasMoreElements()){
			QTIObject oContent = (QTIObject)enumContents.nextElement();
			Element eContent = oContent.getXML();
			if (eContent!=null)	eOperator.addContent(eContent);
		}		
		return eOperator;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			Vector vContents = new Vector();
			Iterator itContents = eElement.getChildren().iterator();
			while (itContents.hasNext()){
				Element eContent = (Element)itContents.next();
				QTIObject oContent = null;
				if (NOT.equals(eContent.getName())){
					oContent = new Not(eContent);
				} else if (AND.equals(eContent.getName())){
					oContent = new And(eContent);
				} else if (OR.equals(eContent.getName())){
					oContent = new Or(eContent);
				} else if (VAREQUAL.equals(eContent.getName())){
					oContent = new Varequal(eContent);
				} else if (VARSUBSET.equals(eContent.getName())){
					oContent = new Varsubset(eContent);
				}
				if (oContent!=null){
					vContents.addElement(oContent); 
				}
			}
			setAttribute(CONTENT, vContents);
		}
	}
	
	public Vector getContents(){
		return getAttributeVectorValue(CONTENT);
	}

	public void addCondition(QTIObject oCondition){
		setAttribute(CONTENT, oCondition);
	}
	public void addConditions(Vector vConditions){
		getContents().addAll(vConditions);
	}
	public void delAllConditions(){
		getContents().removeAllElements();
	}

	public void setCondition(QTIObject oOld, QTIObject oNew){
		int iIndex = getContents().indexOf(oOld);
		setCondition(oNew, iIndex);
	}

	public void delCondition(int iPosition){
		getContents().remove(iPosition);
	}


	public void setCondition(QTIObject oQTI, int iPosition){
		if (oQTI!=null && iPosition>=0){
			getContents().setElementAt(oQTI, iPosition);
		}
	}

	public Vector getBooleanOperators(){
		Vector vBoolOp = new Vector();
		Enumeration enumContents = getContents().elements();
		while (enumContents.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof QTIBooleanOperator){
				vBoolOp.addElement(oQTI);
			}
		}		
		return vBoolOp;		
	}

	public Vector getVarequal(){
		Vector vVarequal = new Vector();
		Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
		while (enumContents.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof Varequal){
				vVarequal.addElement(oQTI);
			} else if (oQTI instanceof QTIBooleanOperator){
				vVarequal.addAll(((QTIBooleanOperator)oQTI).getVarequal());
			}
		}		
		return vVarequal;
	}

	public Vector getVarsubset(){
		Vector vVar = new Vector();
		Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
		while (enumContents.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof Varsubset){
				vVar.addElement(oQTI);
			} else if (oQTI instanceof QTIBooleanOperator){
				vVar.addAll(((QTIBooleanOperator)oQTI).getVarsubset());
			}
		}		
		return vVar;
	}


	protected abstract String getOperatorTag();

	public static QTIBooleanOperator createBooleanOperator(String sOperator){
		QTIBooleanOperator oOperator = null;
		if (sOperator!=null){
			if (NOT.equals(sOperator)){
				oOperator = new Not();
			} else if (AND.equals(sOperator)){
				oOperator =new And();
			} else if (OR.equals(sOperator)){
				oOperator =new Or();
			}
		}
		return oOperator;
	}
	
}
