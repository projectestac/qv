/*
 * Conditionvar.java
 * 
 * Created on 10/febrer/2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

import edu.xtec.qv.qti.util.QTIConstant;

/**
 * @author sarjona
 */
public class Conditionvar extends QTISuper {

	public Conditionvar(){
		setAttribute(CONTENT, new Vector());
	}
	
	public Conditionvar(Element eElement){
		createFromXML(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eConditionvar = new Element(CONDITIONVAR.getTagName());
		if (getAttribute(CONTENT)!=null){
			Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
			while (enumContents.hasMoreElements()){
				QTIObject oContent = (QTIObject)enumContents.nextElement();
				Element eContent = oContent.getXML();
				if (eContent!=null) eConditionvar.addContent(eContent);
			}		
		}
		return eConditionvar;
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
				if (eContent.getName().equalsIgnoreCase(NOT.getTagName())){
					oContent = new Not(eContent);
				} else if (eContent.getName().equalsIgnoreCase(AND.getTagName())){
					oContent = new And(eContent);
				} else if (eContent.getName().equalsIgnoreCase(OR.getTagName())){
					oContent = new Or(eContent);
				} else if (eContent.getName().equalsIgnoreCase(VAREQUAL.getTagName())){
					oContent = new Varequal(eContent);
				} else if (eContent.getName().equalsIgnoreCase(VARSUBSET.getTagName())){
					oContent = new Varsubset(eContent);
				}
				if (oContent!=null){
					vContents.addElement(oContent); 
				}
			}
			setAttribute(CONTENT, vContents);
		}
	}
	
	public void addCondition(QTIObject oCondition){
		setAttribute(CONTENT, oCondition);
	}
	
	/**
	 * 
	 * @param vOperators
	 * @param hConditions Hashtable de la forma (identificador de resposta, resposta)
	 * @return
	 */
	public static Conditionvar create(Vector vConditions){
		Conditionvar oConditionvar = new Conditionvar();
		addBooleanOperator(oConditionvar, vConditions);
		return oConditionvar;
	}
	
	protected static void addBooleanOperator(QTIObject oParent, Vector vConditions){
		if (vConditions != null){
			Enumeration enumConditions = vConditions.elements();
			while (enumConditions.hasMoreElements()){
				Hashtable hCondition = (Hashtable)enumConditions.nextElement();
				if (hCondition.keySet().size()==1 && (hCondition.containsKey(NOT.getAttributeName()) ||
					hCondition.containsKey(AND.getAttributeName()) || hCondition.containsKey(OR.getAttributeName()))){
					Object op = hCondition.keys().nextElement();
					String sOperator = null;
					if (op instanceof QTIConstant){
						sOperator = ((QTIConstant)op).getAttributeName();
					}else{
						sOperator = (String)op;
					}
					QTIBooleanOperator oOperator = QTIBooleanOperator.createBooleanOperator(sOperator);
					Object oChild = hCondition.get(sOperator);
					if (oChild instanceof Vector){
						addCondition(oParent, oOperator);
						addBooleanOperator(oOperator, (Vector)oChild);
					} else if (oChild instanceof Hashtable){
						Hashtable hBooleanVar = (Hashtable)oChild;
						if (!hBooleanVar.isEmpty()){
							addCondition(oParent, oOperator);
						}
						addBooleanVar(oOperator, hBooleanVar);
					}
				}else{
					if (oParent instanceof QTIBooleanOperator){
						addBooleanVar((QTIBooleanOperator)oParent, hCondition);
					}
				}
			}
		}
	}
	
	protected static void addBooleanVar(QTIBooleanOperator oOperator, Hashtable hBooleanVar){
		Enumeration enumBooleanVar = hBooleanVar.keys();
		while (enumBooleanVar.hasMoreElements()){
			String sIdentResp = (String)enumBooleanVar.nextElement();
			Object oResponse = hBooleanVar.get(sIdentResp);
			Vector vResponses = new Vector();
			if (oResponse instanceof Vector){ 
				vResponses = (Vector)oResponse;
			}else if (oResponse instanceof String[]){
				vResponses = new Vector();
				String[] sResponses = (String[])oResponse;
				for (int i=0;i<sResponses.length;i++){
					vResponses.addElement(sResponses[i]);
				}
			}
			Enumeration enumResponses = vResponses.elements();
			while (enumResponses.hasMoreElements()){
				Object o = enumResponses.nextElement();
				if (o instanceof String){
					String sResponse = (String)o;
					if (sResponse.trim().length()>0){
						Varequal oVarequal = new Varequal(sIdentResp, null, sResponse, null);
						oOperator.addCondition(oVarequal);
					}
				}else if (o instanceof Varsubset){
					oOperator.addCondition((Varsubset)o);
				}
			}
		}
	}
	
	protected static void addCondition(QTIObject oParent, QTIBooleanOperator oOperator){
		if (oParent != null){
			if (oParent instanceof QTIBooleanOperator){
				((QTIBooleanOperator)oParent).addCondition(oOperator);
			} else if (oParent instanceof Conditionvar){
				((Conditionvar)oParent).addCondition(oOperator);
			}
		}
	}
	
	public Vector getAllVarequals(){
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
	
	public void setCondition(QTIObject oOld, QTIObject oNew){
		int iIndex = getContents().indexOf(oOld);
		setCondition(oNew, iIndex);
	}

	public void setCondition(QTIObject oQTI, int iPosition){
		if (oQTI!=null && iPosition>=0){
			getContents().setElementAt(oQTI, iPosition);
		}
	}

	public void delCondition(String sRespident, String sText){
		delCondition(getContents(), sRespident, sText);
	}

	public void delCondition(int iPosition){
		getContents().remove(iPosition);
	}

	public boolean delCondition(Vector vContents, String sRespident, String sText){
		boolean bDeleted = false;
		Enumeration enumContents = vContents.elements();
		while (enumContents.hasMoreElements() && !bDeleted){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof Varequal){
				Varequal tmp = (Varequal)oQTI;
				if (sRespident.equals(tmp.getRespident()) && sText.equals(tmp.getText())){
					vContents.remove(oQTI);
					bDeleted = true;
					break;
				}
			}else if (oQTI instanceof QTIBooleanOperator){
				QTIBooleanOperator tmp = (QTIBooleanOperator)oQTI;
				bDeleted = delCondition(tmp.getContents(), sRespident, sText);
			}
		}		
		return bDeleted;
	}
	

	public Varequal getVarequal(String sRespident, String sText){
		Varequal oVar = null;
		if (sRespident!=null && sText!=null){
			Enumeration enum1 = getAllVarequals().elements();
			while (enum1.hasMoreElements()){
				Varequal tmp = (Varequal)enum1.nextElement();
				if (sRespident.equals(tmp.getRespident()) && sText.equals(tmp.getText())){
					oVar=tmp;
					break;
				}
			}
		}
		return oVar;
	}

	public Vector getVarequals(){
		Vector vVarequal = new Vector();
		Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
		while (enumContents.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof Varequal){
				vVarequal.addElement(oQTI);
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

	public Vector getAllVarsubset(){
		Vector v = new Vector();
		Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
		while (enumContents.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof Varsubset){
				v.addElement(oQTI);
			} else if (oQTI instanceof QTIBooleanOperator){
				v.addAll(((QTIBooleanOperator)oQTI).getVarsubset());
			}
		}
		return v;
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

	public Vector getContents(){
		return getAttributeVectorValue(CONTENT);
	}	

}
