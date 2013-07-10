/*
 * QTISuperResponse.java
 * 
 * Created on 02/març/2004
 */
package edu.xtec.qv.qti;

import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

/**
 * @author sarjona
 */
public abstract class QTISuperResponse extends QTISuper {

	public QTISuperResponse(String sIdent){
		this(sIdent, null, null);
	}

	public QTISuperResponse(String sIdent, String sCardinality, String sTiming){
		if (sIdent==null || sIdent.trim().length()==0){
			sIdent = getRandomIdent();
		}
		setAttribute(IDENT, sIdent);
		setCardinality(sCardinality);
		setTiming(sTiming);
	}

	public QTISuperResponse(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eResponse = new Element(getResponseTag());
		String sIdent = getIdent()!=null?getIdent():getRandomIdent();
		eResponse.setAttribute(IDENT.getTagName(), sIdent);
		
		if (getAttribute(RCARDINALITY)!=null){
			eResponse.setAttribute(RCARDINALITY.getTagName(), getAttributeValue(RCARDINALITY));
		}
		
		if (getAttribute(RTIMING)!=null){
			eResponse.setAttribute(RTIMING.getTagName(), getAttributeValue(RTIMING));
		}
		
		if (getAttribute(FIRST_MATERIAL)!=null){
			QTIObject oMaterial = (QTIObject)getAttribute(FIRST_MATERIAL);
			eResponse.addContent(oMaterial.getXML());
		}
		
		if (getAttribute(RENDER)!=null){
			QTIObject oRender = (QTIObject)getAttribute(RENDER);
			eResponse.addContent(oRender.getXML());
		}

		if (getAttribute(LAST_MATERIAL)!=null){
			QTIObject oMaterial = (QTIObject)getAttribute(LAST_MATERIAL);
			eResponse.addContent(oMaterial.getXML());
		}
		return eResponse;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			int iIndex = 0;
			Iterator itContents = eElement.getChildren().iterator();
			while (itContents.hasNext()){
				Element eContent = (Element)itContents.next();
				if (eElement.getChild(MATERIAL.getTagName())!=null){
					QTIObject oContent = new Material(eElement.getChild(MATERIAL.getTagName()));
					if (iIndex==0){
						setAttribute(FIRST_MATERIAL, oContent);
					} else{
						setAttribute(LAST_MATERIAL, oContent);						
					}
				}else if (eElement.getChild(RENDER_CHOICE.getTagName())!=null){
					QTIObject oContent = new RenderChoice(eElement.getChild(RENDER_CHOICE.getTagName()));
					setAttribute(RENDER, oContent);
				}else if (eElement.getChild(RENDER_FIB.getTagName())!=null){
					QTIObject oContent = new RenderFIB(eElement.getChild(RENDER_FIB.getTagName()));
					setAttribute(RENDER, oContent);
				}else if (eElement.getChild(RENDER_HOTSPOT.getTagName())!=null){
					QTIObject oContent = new RenderHotspot(eElement.getChild(RENDER_HOTSPOT.getTagName()));
					setAttribute(RENDER, oContent);
				}else if (eContent.getName().equalsIgnoreCase(RENDER_EXTENSION.getTagName())){
					if (eContent.getChild(IMS_RENDER_OBJECT.getTagName())!=null){
						QTIObject oContent = new IMSRenderObject(eContent.getChild(IMS_RENDER_OBJECT.getTagName()));
						setAttribute(RENDER, oContent);
					}else if (eContent.getChild(RENDER_DRAW.getTagName())!=null){
						QTIObject oContent = new RenderDraw(eContent.getChild(RENDER_DRAW.getTagName()));
						setAttribute(RENDER, oContent);
					}
				}
				iIndex++;
			}
			setAttribute(IDENT, eElement.getAttributeValue(IDENT.getTagName()));
			setAttribute(RCARDINALITY, eElement.getAttributeValue(RCARDINALITY.getTagName()));
			setAttribute(RTIMING, eElement.getAttributeValue(RTIMING.getTagName()));
		}
	}
	
	public String getCardinality(){
		return getAttributeValue(RCARDINALITY);
	}
	public void setCardinality(String sCardinality){
		setAttribute(RCARDINALITY, sCardinality);
	}

	public void setTiming(String sTiming){
		setAttribute(RTIMING, sTiming);
	}

	public Material getFirstMaterial(){
		Material oMaterial=null;
		if (getAttribute(FIRST_MATERIAL)!=null){
			oMaterial = (Material)getAttribute(FIRST_MATERIAL);
		}
		return oMaterial;
	}
	public void setFirstMaterial(Material oMaterial){
		setAttribute(FIRST_MATERIAL, oMaterial);
	}
	public Material getLastMaterial(){
		Material oMaterial=null;
		if (getAttribute(LAST_MATERIAL)!=null){
			oMaterial = (Material)getAttribute(LAST_MATERIAL);
		}
		return oMaterial;
	}
	public void setLastMaterial(Material oMaterial){
		setAttribute(LAST_MATERIAL, oMaterial);
	}

	public RenderChoice addRenderChoice(String sAlignment){
		RenderChoice oRenderChoice = RenderChoice.create(sAlignment);
		setRender(oRenderChoice);
		return oRenderChoice;
	}
	public RenderChoice addRenderFIB(String sAlignment){
		RenderChoice oRenderChoice = RenderChoice.create(sAlignment);
		setRender(oRenderChoice);
		return oRenderChoice;
	}
	public QTISuperRender getRender(){
		QTISuperRender oRender = null;
		if (getAttribute(RENDER)!=null){
			oRender = (QTISuperRender)getAttribute(RENDER);
		}
		return oRender;
	}
	public void setRender(QTIObject oRender){
		setAttribute(RENDER, oRender);
	}
	
	public static ResponseLID createRenderChoiceWithMattext(String sIdent, String sCardinality, Vector vResponses, String sAlignment){
		ResponseLID oResponseLID = new ResponseLID(sIdent, sCardinality, null);
		RenderChoice oRenderChoice = RenderChoice.createWithMattext(vResponses, sAlignment);
		oResponseLID.setRender(oRenderChoice);
		return oResponseLID;
	}
	
	protected abstract String getResponseTag();
	
}
