/*
 * Material.java
 * 
 * Created on 09/febrer/2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.jdom.Element;

import edu.xtec.qv.qti.util.StringUtil;

/**
 * @author sarjona
 */
public class Material extends QTISuper {

	public Material(){
		setAttribute(CONTENT, new Vector());
	}
	
	public Material(QTIObject oMaterial){
		Vector vContent = new Vector();
		if (oMaterial!=null){
			vContent.addElement(oMaterial);
		}
		setAttribute(CONTENT, vContent);
	}

	public Material(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eMaterial = new Element(MATERIAL.getTagName());
		Object oMaterials = getAttribute(CONTENT);
		if (oMaterials!=null){
			Enumeration enumMaterials = ((Vector)oMaterials).elements();
			while (enumMaterials.hasMoreElements()){
				QTIObject oMaterial = (QTIObject)enumMaterials.nextElement();
				eMaterial.addContent(oMaterial.getXML());
			}
		}
		return eMaterial;
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
				// TODO: Afegir la resta de MAT (com per exemple MATAPPLET, ...)
				if (MATTEXT.equals(eContent.getName())){
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
				}else if (MATBREAK.equals(eContent.getName())){
					oContent = new Matbreak(eContent);
				} else if (MAT_EXTENSION.equals(eContent.getName())){
					if (eContent.getChild(MATHTML.getTagName())!=null){
						oContent = new MatHTML(eContent.getChild(MATHTML.getTagName()));
					}else if (eContent.getChild(MATJCLIC.getTagName())!=null){
							oContent = new Matjclic(eContent.getChild(MATJCLIC.getTagName()));
					}else if (eContent.getChild(MATLATEX.getTagName())!=null){
						oContent = new Matlatex(eContent.getChild(MATLATEX.getTagName()));
					}
				}
				if (oContent!=null){
					vContents.addElement(oContent);
				}
			}
			setAttribute(CONTENT, vContents);
		}
	}
	
	public Vector getMaterials(){
		return getAttributeVectorValue(CONTENT);
	}
	public void addMaterial(QTIObject oMaterial){
		setAttribute(CONTENT, oMaterial);
	}
	public void addMaterial(QTIObject oMaterial, int iPosition){
		if (oMaterial!=null){
			if (iPosition>=0 && iPosition<getContents().size()){
				getContents().insertElementAt(oMaterial, iPosition);
			}else{
				addMaterial(oMaterial);
			}
		}
	}
	public void setMaterial(QTIObject oOld, QTIObject oNew){
		if (oNew!=null){
			int iIndex = getContents().indexOf(oOld);
			getContents().setElementAt(oNew, iIndex);
		}
	}
	public void delMaterial(QTIObject oMaterial){
		getContents().removeElement(oMaterial);
	}
	
	public void setMaterialPosition(int iOldPosition, int iNewPosition){
		setMaterialPosition((QTIObject)getContents().elementAt(iOldPosition), iOldPosition, iNewPosition);
	}
	
	public void setMaterialPosition(QTIObject oQTI, int iOldPosition, int iNewPosition){
		setObjectPosition(getContents(), oQTI, iOldPosition, iNewPosition);
	}
	
	public String getShowName(){
		String sShowName = null;
		if (getMaterials()!=null && !getMaterials().isEmpty()){
			sShowName = ((QTIObject)getMaterials().firstElement()).getShowName();
		}
		return sShowName;
	}
	
	public String getText(){
		String sText = "";
		if (getMaterials()!=null && !getMaterials().isEmpty()){
			Enumeration enumMaterials = getMaterials().elements();
			while(enumMaterials.hasMoreElements()){
				QTIObject oQTI = (QTIObject)enumMaterials.nextElement();
				if (oQTI instanceof Mattext){
					sText += ((Mattext)oQTI).getText();
				}else if(oQTI instanceof Matbreak){
					sText += "\n";
				}
			}
		}
		return sText;
	}
	
	public Vector getMattext(){
		Vector vMattext = new Vector();
		if (getMaterials()!=null){
			Enumeration enumMaterials = getMaterials().elements();
			while (enumMaterials.hasMoreElements()){
				QTIObject oQTI = (QTIObject)enumMaterials.nextElement();
				if (oQTI instanceof Mattext){
					vMattext.addElement(oQTI);
				}
			}
		}
		return vMattext;
	}
	public boolean containsMattext(){
		boolean bContains = false;
		if (getMaterials()!=null){
			Enumeration enumMaterials = getMaterials().elements();
			while (enumMaterials.hasMoreElements() && !bContains){
				QTIObject oQTI = (QTIObject)enumMaterials.nextElement();
				if (oQTI instanceof Mattext){
					bContains = true;
				}
			}
		}
		return bContains;
	}
		
	public Vector getMats(){
		Vector vMats = new Vector();
		if (getMaterials()!=null){
			Enumeration enumMaterials = getMaterials().elements();
			while (enumMaterials.hasMoreElements()){
				QTIObject oQTI = (QTIObject)enumMaterials.nextElement();
				//if (oQTI instanceof Matimage ||
				//	oQTI instanceof Mataudio){
				if (oQTI instanceof QTISuperMat){
					vMats.addElement(oQTI);
				}
			}
		}
		return vMats;
	}
	public Vector getMatimage(){
		Vector vMatimage = new Vector();
		if (getMaterials()!=null){
			Enumeration enumMaterials = getMaterials().elements();
			while (enumMaterials.hasMoreElements()){
				QTIObject oQTI = (QTIObject)enumMaterials.nextElement();
				if (oQTI instanceof Matimage){
					vMatimage.addElement(oQTI);
				}
			}
		}
		return vMatimage;
	}
	public boolean containsMatimage(){
		boolean bContains = false;
		if (getMaterials()!=null){
			Enumeration enumMaterials = getMaterials().elements();
			while (enumMaterials.hasMoreElements() && !bContains){
				QTIObject oQTI = (QTIObject)enumMaterials.nextElement();
				if (oQTI instanceof Matimage){
					bContains = true;
				}
			}
		}
		return bContains;
	}
		
	public Vector getMataudio(){
		Vector vMataudio = new Vector();
		if (getMaterials()!=null){
			Enumeration enumMaterials = getMaterials().elements();
			while (enumMaterials.hasMoreElements()){
				QTIObject oQTI = (QTIObject)enumMaterials.nextElement();
				if (oQTI instanceof Mataudio){
					vMataudio.addElement(oQTI);
				}
			}
		}
		return vMataudio;
	}
	public boolean containsMataudio(){
		boolean bContains = false;
		if (getMaterials()!=null){
			Enumeration enumMaterials = getMaterials().elements();
			while (enumMaterials.hasMoreElements() && !bContains){
				QTIObject oQTI = (QTIObject)enumMaterials.nextElement();
				if (oQTI instanceof Mataudio){
					bContains = true;
				}
			}
		}
		return bContains;
	}

//	***************************	
//	*  Creacio
//	***************************
	public static Enumeration getLines(String sParagraph){
		Enumeration stLines = null;
		if (StringUtil.isHTML(sParagraph)){
			//stLines = new StringTokenizer(sParagraph, "12345678901234567890");
			//stLines = new StringTokenizer(sParagraph, "$$$");
			Vector vText = new Vector();
			vText.add(sParagraph);
			stLines = vText.elements();
		}else{
			stLines = new StringTokenizer(sParagraph,"\r\n");
		}
		return stLines;
	}

	public static Material createWithMattext(String sText){
		return createWithMattext(Mattext.HTML_TEXTTYPE, sText);
	}
	public static Material createWithMattext(String sTextType, String sText){
		Material oMaterial = new Material();
		if (sText!=null){
			Enumeration enumLines = getLines(sText);
			if (!enumLines.hasMoreElements()){
				Mattext oMattext = new Mattext(sTextType, "");
				oMaterial.addMaterial(oMattext);
			}else{
				while (enumLines.hasMoreElements()){
					String sLine = (String)enumLines.nextElement();
					Mattext oMattext = new Mattext(sTextType, sLine);
					oMaterial.addMaterial(oMattext);
					if (!StringUtil.isHTML(sText) && enumLines.hasMoreElements()){
						Matbreak oMatbreak = new Matbreak();
						oMaterial.addMaterial(oMatbreak);
					}
				}
			}
		}else{
			Mattext oMattext = new Mattext(sTextType, "");
			oMaterial.addMaterial(oMattext);
		}
		return oMaterial;
	}
	
	public static Material createWithMatimage(String sImagtype, String sURI){
		return createWithMatimage(sImagtype, sURI, -1, -1);
	}

	public static Material createWithMatimage(String sImagtype, String sURI, int iWidth, int iHeight){
		Matimage oMatimage = new Matimage(sImagtype, sURI, iWidth, iHeight);
		Material oMaterial = new Material(oMatimage);
		return oMaterial;
	}

	public static Material createWithMataudio(String sURI){
		Mataudio oMataudio = new Mataudio(sURI);
		Material oMaterial = new Material(oMataudio);
		return oMaterial;
	}
}
