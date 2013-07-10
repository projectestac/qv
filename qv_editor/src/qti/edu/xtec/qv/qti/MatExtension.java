/*
 * MatExtension.java
 * 
 * Created on 16/març/2004
 */
package edu.xtec.qv.qti;

import org.jdom.Element;

/**
 * @author sarjona
 */
public abstract class MatExtension extends QTISuperMat {

	public MatExtension(){
	}
	
	public MatExtension(Element eElement){
		super(eElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getSpecificXML(org.jdom.Element)
	 */
	protected Element getSpecificXML(Element eMaterial) {
		getExtensionXML(eMaterial);
		/*if (getAttribute(MAT_EXTENSION)!=null){
			QTISuperMat oExtension = (QTISuperMat)getAttribute(MAT_EXTENSION);
			eExtension.addContent(oExtension.getText());
		}*/
		return eMaterial;
	}

	
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#createSpecificFromXML(org.jdom.Element)
	 */
	protected void createSpecificFromXML(Element eElement) {
		if (eElement.getChild(MATHTML.getTagName(), eElement.getNamespace())!=null){
			MatHTML oExtension = new MatHTML(eElement.getChild(MATHTML.getTagName(), eElement.getNamespace()));
			setAttribute(MAT_EXTENSION, oExtension);
		}else if (eElement.getChild(MATJCLIC.getTagName(), eElement.getNamespace())!=null){
			Matjclic oExtension = new Matjclic(eElement.getChild(MATJCLIC.getTagName(), eElement.getNamespace()));
			setAttribute(MAT_EXTENSION, oExtension);
		}else if (eElement.getChild(MATLATEX.getTagName(), eElement.getNamespace())!=null){
			Matjclic oExtension = new Matjclic(eElement.getChild(MATLATEX.getTagName(), eElement.getNamespace()));
			setAttribute(MAT_EXTENSION, oExtension);
		}
		createExtensionFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatTag()
	 */
	protected String getMatTag() {
		return MAT_EXTENSION.getTagName();
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatTypeLabel()
	 */
	protected String getMatTypeLabel() {
		return TYPE.getAttributeName();
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperMat#getMatTypeTag()
	 */
	protected String getMatTypeTag() {
		return TYPE.getTagName();
	}

	protected String getMatExtensionTag(){
		return MAT_EXTENSION.getTagName();
	}
	
	protected abstract Element getExtensionXML(Element eExtension);
	protected abstract Element createExtensionFromXML(Element eExtension);
	

}
