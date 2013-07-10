/*
 * QVPreguntaDrawBean.java
 * 
 * Created on 23/febrer/2006
 */
package edu.xtec.qv.editor.beans;

import edu.xtec.qv.editor.util.QTIUtil;
import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.Material;
import edu.xtec.qv.qti.Matimage;
import edu.xtec.qv.qti.RenderDraw;
import edu.xtec.qv.qti.Respcondition;



/**
 * @author sarjona
 */
public class QVPreguntaDrawBean extends QVPreguntaBean{
	
	public QVPreguntaDrawBean(QVBean mainBean){
		super(mainBean);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.QVBean#start()
	 */
	public boolean start() {
		boolean bOk = true;
		try{
			super.start();
		} catch (Exception e){
			logger.error("EXCEPCIO inicialitzant QVPreguntaDrawBean --> "+e);
			e.printStackTrace();
			bOk = false;
		}
		return bOk;
	}
	
	protected Item getCurrentItem(){
		if (oItem==null){
			oItem = mainBean.getPregunta();
			if (oItem!=null){
				// Actualitzar pregunta
				String sTitle = getParameter(TITLE_PARAM);
				if (sTitle!=null){
					oItem.setTitle(sTitle);
				}
				String sEnunciat = getParameter(ENUNCIAT_PREGUNTA_PARAM);
				if (sEnunciat!=null){
					oItem.setStatement(sEnunciat);
				}
				String sInteractionswitch = getParameter(P_INTERACTIONSWITCH);
				if (sInteractionswitch!=null && sInteractionswitch.trim().length()>0){
					QTIUtil.setInteractionswitch(oItem, sInteractionswitch);
				}
				
				String sBackgroundImage = getParameter("p_background_image_uri");
				if (sBackgroundImage!=null){
					int iWidth = mainBean.getIntParameter("width", -1);
					int iHeight = mainBean.getIntParameter("height", -1);
					setBackgroundImage(oItem, sBackgroundImage, iWidth, iHeight);
				}
				updateAttributes();
				updateFeedback();
				updatePuntuation();
			}else{
				// Crear nova pregunta
				oItem = QTIUtil.createDrawItem();
			}

			//-->Albert
			String sOrdreItem = getParameter(ITEM_ORDER_PARAM);
			if (sOrdreItem !=null){
				//System.out.println("l'estableixo a sOrdreItem:"+sOrdreItem+" idItem:"+oItem.getIdent());	
				QTIUtil.setItemSelectionOrdering(mainBean.getFull(), oItem, sOrdreItem);
			}
			//ordre_pregunta
			//<--
			
		}
		return oItem;
	}
	
	protected void setBackgroundImage(Item oItem, String sURI, int iWidth, int iHeight){
		RenderDraw oRender = QTIUtil.getRenderDraw(oItem);
		if (oRender!=null && sURI!=null){
			Material oMaterial = QTIUtil.getFirstMaterial(oRender);
			if (oMaterial!=null){
				Matimage oMat = QTIUtil.getMatimage(oMaterial);
				if (oMat!=null){
					oMat.setURI(sURI);
					oMat.setWidth(iWidth);
					oMat.setHeight(iHeight);
				}
			}else{
				oMaterial = Material.createWithMatimage(null, sURI, iWidth, iHeight);
				oRender.addMaterial(oMaterial);
				oRender.setObjectPosition(oRender.getContents(), oMaterial, oRender.getContentPosition(oMaterial), 0);
			}
		}
	}
	
	public String getBackgroundImage(){
		String sURI = null;
		RenderDraw oRender = QTIUtil.getRenderDraw((Item)getBeanObject());
		if (oRender!=null){
			Material oMaterial = QTIUtil.getFirstMaterial(oRender);
			if (oMaterial!=null){
				Matimage oMat = QTIUtil.getMatimage(oMaterial);
				if (oMat!=null){
					sURI = oMat.getURI();
				}
			}
		}
		return sURI;
	}
	
	public int getColors(){
		int iColors = 4;
		RenderDraw oRender = QTIUtil.getRenderDraw((Item)getBeanObject());
		if (oRender!=null){
			try{
				iColors = Integer.parseInt(oRender.getColors());
			}catch (Exception e){
				iColors=4;
			}
		}
		return iColors;
	}
	
	public int getWidth(){
		int iWidth = 500;
		RenderDraw oRender = QTIUtil.getRenderDraw((Item)getBeanObject());
		if (oRender!=null){
			try{
				iWidth = Integer.parseInt(oRender.getWidth());
			}catch (Exception e){
			}
		}
		return iWidth;
	}
	
	public int getHeight(){
		int iHeight = 400;
		RenderDraw oRender = QTIUtil.getRenderDraw((Item)getBeanObject());
		if (oRender!=null){
			try{
				iHeight = Integer.parseInt(oRender.getHeight());
			}catch (Exception e){
			}
		}
		return iHeight;
	}

	public void updateAttributes(){
		String sColors = mainBean.getParameter("p_colors");
		String sWidth = mainBean.getParameter("p_width");
		String sHeight = mainBean.getParameter("p_height");
		RenderDraw oRender = QTIUtil.getRenderDraw((Item)getBeanObject());
		if (oRender!=null){
			if (sColors!=null){
				oRender.setColors(sColors);
			}
			if (sWidth!=null){
				oRender.setWidth(sWidth);
			}
			if (sHeight!=null){
				oRender.setHeight(sHeight);
			}
		}
	}
		
}
