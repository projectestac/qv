/*
 * QTIObject.java
 * 
 * Created on 03/febrer/2004
 */
package edu.xtec.qv.qti;

import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdom.Element;

import edu.xtec.qv.qti.util.QTIConstant;
	

/**
 * @author sarjona
 */
/**
 * @author sarjona
 */
public abstract class QTISuper implements QTIObject{
	
	protected static Logger logger = Logger.getRootLogger();

	public static final String DEFAULT_VARNAME = "SCORE";
	public static final String DEFAULT_FEEDBACKTYPE = "Response";

	
	protected HashMap hmAttributes = new HashMap();
			
	public HashMap getAttributes(){
		return hmAttributes;
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String sName) {
		return hmAttributes.get(sName);
	}

	public Object getAttribute(QTIConstant oConstant) {
		Object o = null;
		if (oConstant!=null){
			o = getAttribute(oConstant.getAttributeName());
		}
		return o;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getAttribute(java.lang.String, java.lang.String)
	 */
	public Object getAttribute(String sName, String sIdent){
		Object oAttributeIdent = null;
		if (sName!=null && sIdent!=null){
			Object oAttribute = hmAttributes.get(sName);
			if (oAttribute!= null && oAttribute instanceof Vector){
				Enumeration enumAttributes = ((Vector)oAttribute).elements();
				while (enumAttributes.hasMoreElements()){
					QTIObject oQTI = (QTIObject)enumAttributes.nextElement();
					if (sIdent.equalsIgnoreCase(oQTI.getIdent())){
						oAttributeIdent = oQTI;
						break;
					}
				}
			}
		}
		return oAttributeIdent;
	}

	public Object getAttribute(QTIConstant oConstant, String sIdent){
		Object o = null;
		if (oConstant!=null){
			o = getAttribute(oConstant.getAttributeName(), sIdent);
		}
		return o;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getAttributeValue(java.lang.String)
	 */
	public String getAttributeValue(String sName){
		String sValue = new String();
		Object oValue = getAttribute(sName);
		if (oValue!=null){
			sValue = (String)oValue.toString();
			if (sValue.trim().length()<=0){
				sValue = "";
			}
		}
		return sValue;
	}
	
	public String getAttributeValue(QTIConstant oConstant){
		String s = null;
		if (oConstant!=null){
			s = getAttributeValue(oConstant.getAttributeName());
		}
		return s;
	}
	
	public int getAttributeIntValue(String sName){
		int iInt = -1;
		if (getAttribute(sName)!=null){
			String sValue = getAttributeValue(sName);
			if (sValue!=null && !Double.valueOf(sValue).isNaN()){
				iInt = Integer.parseInt(sValue);
			}
		}
		return iInt;		
	}
	protected int getAttributeIntValue(QTIConstant oConstant){
		int i = -1;
		if (oConstant!=null){
			i = getAttributeIntValue(oConstant.getAttributeName());
		}
		return i;
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getAttributeVectorValue(java.lang.String)
	 */
	public Vector getAttributeVectorValue(String sName){
		Vector vValue = new Vector();
		Object oValue = getAttribute(sName);
		if (oValue!=null){
			if (oValue instanceof Vector){
				vValue = (Vector)oValue;
			}else{
				vValue = new Vector();
				vValue.addElement(oValue);
			}
		}
		return vValue;
	}
	public Vector getAttributeVectorValue(QTIConstant oConstant){
		Vector v = null;
		if (oConstant!=null){
			v = getAttributeVectorValue(oConstant.getAttributeName());
		}
		return v;
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String sName, Object sValue) {
		if (sName!=null && sValue!=null){
			Object oOldValue = hmAttributes.get(sName);
			if (oOldValue!=null && oOldValue instanceof Vector && !(sValue instanceof Vector)){
				// Es tracta d'un Vector; l'element s'ha d'afegir al final
				Vector vValue = (Vector)oOldValue;
				vValue.addElement(sValue);
				sValue = vValue;
			}
			hmAttributes.put(sName, sValue);
		} else if (hmAttributes.containsKey(sName)){
			hmAttributes.remove(sName);
		}
	}
	public void setAttribute(QTIConstant oConstant, Object sValue) {
		if (oConstant!=null){
			setAttribute(oConstant.getAttributeName(), sValue);
		}
	}
	
	public void setObjectPosition(Vector vAll, QTIObject oQTI, int iOldPosition, int iNewPosition){
		if (vAll!=null && oQTI!=null){
			Object oQTIObject = null;
			if (vAll.contains(oQTI)){
				oQTIObject = vAll.set(iNewPosition, oQTI);
			}
			vAll.set(iOldPosition, oQTIObject);
		}
	}
	
	public void delAttribute(String sName){
		hmAttributes.remove(sName);
	}

	public void delAttributeElement(String sName, Object oElement){
		String sIdent = null;
		if (oElement!=null){
			if (oElement instanceof String){
				sIdent = (String)oElement;
			} else if (oElement instanceof QTIObject){
				sIdent = ((QTIObject)oElement).getIdent();
			}
			if (sIdent!=null){
				QTIObject oDelQTI = (QTIObject)getAttribute(sName, sIdent);
				Vector vValue = getAttributeVectorValue(sName);
				vValue.removeElement(oDelQTI);
			}
		}
	}

	public void delAttributeElement(QTIConstant oConstant, Object oElement){
		if (oConstant!=null){
			delAttributeElement(oConstant.getAttributeName(), oElement);
		}
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getIdent()
	 */
	public String getIdent(){
		String sIdent = null;
		Object oIdent = getAttribute(IDENT);
		if (oIdent!=null){
			sIdent = (String)oIdent;
		}
		return sIdent;
	}
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#setIdent(java.lang.String)
	 */
	public void setIdent(String sIdent){
		setAttribute(IDENT, sIdent);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getTitle()
	 */
	public String getTitle(){
		return getAttributeValue(TITLE);
	}
	public void setTitle(String sTitle){
		setAttribute(TITLE, sTitle);
	}
	
	
	public String getShowName(){
		String sShowName = getAttributeValue(TITLE);
		if (sShowName==null || sShowName.length()==0){
			sShowName = getIdent();
		}
		return sShowName;
	}
	
	public static String getRandomIdent(){
		long l = System.currentTimeMillis();
		int i=(int)(Math.random()*1000);
		return l+""+i;
	}

	/**
	 * @param oQTI
	 * @return
	 */
	public int getContentPosition(QTIObject oQTI){
		int iPosicio = getContents().indexOf(oQTI);
		return iPosicio;
	}
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getContents()
	 */
	public Vector getContents(){
		return getAttributeVectorValue(CONTENT);
	}	
	
	
//	**********************************	
//	* Static get/set/add/del methods
//	**********************************

	public static void addToXML(Element eParent, QTIObject oParent, QTIConstant oConstant){
		if (eParent!=null && oParent!=null && oConstant!=null){
			if (ZERO_OR_MORE.equals(oConstant.getMultiplicity())){
				Vector v = (Vector)get(oParent, oConstant);
				if (v!=null && !v.isEmpty()){
					Enumeration enum1 = v.elements();
					while (enum1.hasMoreElements()){
						QTIObject oQTI = (QTIObject)enum1.nextElement();
						eParent.addContent(oQTI.getXML());
					}
				}
			}else if (ZERO_OR_ONE.equals(oConstant.getMultiplicity())){
				QTIObject o = getQTIObject(oParent, oConstant);
				if (o!=null){
					eParent.addContent(o.getXML());
				}
			}
		}
	}
	
	public static void addFromXML(Element eParent, QTIObject oParent, QTIConstant oConstant){
		if (eParent!=null && oParent!=null && oConstant!=null){
			if (ZERO_OR_MORE.equals(oConstant.getMultiplicity())){
				if (eParent.getChildren(oConstant.getTagName())!=null){
					Iterator it = eParent.getChildren(oConstant.getTagName(), eParent.getNamespace()).iterator();
					while (it.hasNext()){
						Element e = (Element)it.next();
						add(oParent, (QTIObject)get(e, oConstant), oConstant);
					}
				}else{
					oParent.setAttribute(oConstant, new Vector());
				}
			}else if (ZERO_OR_ONE.equals(oConstant.getMultiplicity())){
				Element e = eParent.getChild(oConstant.getTagName(), eParent.getNamespace());
				if (e!=null){
					add(oParent, (QTIObject)get(e, oConstant), oConstant);
				}
			}
		}
	}

	public static Object get(Element eElement, QTIConstant oConstant){
		Object o = null;
		try{
			Class oClass=Class.forName("edu.xtec.qv.qti."+oConstant.getClassName());
			Constructor oCons = oClass.getConstructor(new Class[]{Element.class});
			o = oCons.newInstance(new Object[]{eElement});
		}catch (Exception e){
			logger.debug("EXCEPTION parsejant d'Element a Object-> "+e);
			e.printStackTrace();
		}
		return o;
	}
	public static Vector getVector(QTIObject oParent, QTIConstant oConstant){
		Vector v = new Vector();
		Object o = get(oParent, oConstant);
		if (o!=null && o instanceof Vector){
			v = (Vector)o;
		}
		return v; 
	}
	public static QTIObject getQTIObject(QTIObject oParent, QTIConstant oConstant){
		QTIObject oQTI = null;
		Object o = get(oParent, oConstant);
		if (o!=null && o instanceof QTIObject){
			oQTI = (QTIObject)o;
		}
		return oQTI; 
	}
	
	
	public static Object get(QTIObject oParent, QTIConstant oConstant){
		Object o = null;
		if (oParent!=null){
			if (ZERO_OR_MORE.equals(oConstant.getMultiplicity())){
				o = oParent.getAttributeVectorValue(oConstant);
			}else if (ZERO_OR_ONE.equals(oConstant.getMultiplicity())){
				o = oParent.getAttribute(oConstant);
			}
		}
		return o;
	}
	
	public static void add(QTIObject oParent, QTIObject oQTI, QTIConstant oConstant){
		if (oParent!=null){
			if (ZERO_OR_MORE.equals(oConstant.getMultiplicity()) && oParent.getAttribute(oConstant)==null){
				oParent.setAttribute(oConstant, new Vector());
			}
			oParent.setAttribute(oConstant, oQTI);
		}
	}
	
	public String toString(){
		return " (" + hmAttributes + ")";
	}

}
