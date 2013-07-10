/*
 * Reference.java
 * 
 * Created on 01/setembre/2004
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
public class Reference extends QTISuper {

	public Reference(){
		setAttribute(CONTENT, new Vector());
	}
	
	public Reference(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eReference = new Element(getQTIConstant().getTagName());
		// contents
		Enumeration enumContents = getContents().elements();
		while (enumContents.hasMoreElements()){
			QTIObject oContent = (QTIObject)enumContents.nextElement();
			eReference.addContent(oContent.getXML());
		}
		return eReference;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			// Add qticomment, material, mattext, matemtext, matimage, mataudio, matvideo, matapplet, matapplication, matbreak, mat_extension
			Vector vContents = new Vector();
			Iterator itContents = eElement.getChildren().iterator();
			while (itContents.hasNext()){
				Element eContent = (Element)itContents.next();
				QTIObject oContent = null;
				if (QTICOMMENT.equals(eContent.getName())){
					oContent = new QTIComment(eContent);
				}else if (MATERIAL.equals(eContent.getName())){
					oContent = new Material(eContent);
				}else if (MATTEXT.equals(eContent.getName())){
					oContent = new Mattext(eContent);
				}else if (MATEMTEXT.equals(eContent.getName())){
					oContent = new Matemtext(eContent);
				} else if (MATIMAGE.equals(eContent.getName())){
					oContent = new Matimage(eContent);
				} else if (MATAUDIO.equals(eContent.getName())){
					oContent = new Mataudio(eContent);
				} else if (MATVIDEO.equals(eContent.getName())){
					oContent = new Matvideo(eContent);
				} else if (MATAPPLICATION.equals(eContent.getName())){
					oContent = new Matapplication(eContent);
				} else if (MATBREAK.equals(eContent.getName())){
					oContent = new Matbreak(eContent);
				} else if (MAT_EXTENSION.equals(eContent.getName())){
					logger.debug("todo: implementar mat_extension");
				} 
				if (oContent!=null){				
					vContents.addElement(oContent);
				}
			}
			setAttribute(CONTENT, vContents);
		}
	}
	
	public static QTIConstant getQTIConstant(){
		return REFERENCE;
	}	
	
}
