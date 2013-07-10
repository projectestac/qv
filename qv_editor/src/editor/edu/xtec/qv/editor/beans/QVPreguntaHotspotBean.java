/*
 * QVPreguntaHotspotBean.java
 * 
 * Created on 14/maig/2004
 */
package edu.xtec.qv.editor.beans;

import java.util.StringTokenizer;

import edu.xtec.qv.editor.util.QTIUtil;
import edu.xtec.qv.editor.util.QVHotspotListControl;
import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.Material;
import edu.xtec.qv.qti.Matimage;
import edu.xtec.qv.qti.QTISuperResponse;
import edu.xtec.qv.qti.RenderHotspot;



/**
 * @author sarjona
 */
public class QVPreguntaHotspotBean extends QVPreguntaBean{
	
	public static final String A_SET_HOTSPOT_BACKGROUND_TYPE = "set_hotspot_background_type";
	
	// Parameter
	public static final String P_HOTSPOT_TYPE = "p_hotspot_type";
	public static final String P_STYLE_BORDER_COLOR = "p_style_border_color";
	public static final String P_STYLE_BACKGROUND_TYPE = "p_hotspot_background_type";
	public static final String P_STYLE_BACKGROUND_IMAGE = "p_style_background_image";
	public static final String P_STYLE_BACKGROUND_COLOR = "p_style_background_color";
	public static final String P_BACKGROUND_IMAGE = "p_background_image_uri";
	public static final String P_WIDTH = "width";
	public static final String P_HEIGHT = "height";
	public static final String P_MIN_NUMBER = "p_min_number";
	public static final String P_MAX_NUMBER = "p_max_number";

	public static final String BACKGROUND_COLOR_TYPE = "color";
	public static final String BACKGROUND_IMAGE_TYPE = "image";
	
	public QVPreguntaHotspotBean(QVBean mainBean){
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
			logger.error("EXCEPCIO inicialitzant QVPreguntaHotspotBean --> "+e);
			//e.printStackTrace();
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
				
				String sHotspotType = getParameter(P_HOTSPOT_TYPE);
				if (sHotspotType!=null){
					setHotspotType(oItem, sHotspotType);
				}
				
				if (getParameter(P_MIN_NUMBER)!=null && getParameter(P_MAX_NUMBER)!=null){
					int iMinNumber = mainBean.getIntParameter(P_MIN_NUMBER, 1);
					int iMaxNumber = mainBean.getIntParameter(P_MAX_NUMBER, -1);
					setResponseNumber(oItem, iMinNumber, iMaxNumber);
				}
				
				String sBackgroundImage = getParameter(P_BACKGROUND_IMAGE);
				if (sBackgroundImage!=null && !"".equals(sBackgroundImage)){
					int iWidth = mainBean.getIntParameter(P_WIDTH, -1);
					int iHeight = mainBean.getIntParameter(P_HEIGHT, -1);
					setBackgroundImage(oItem, sBackgroundImage, iWidth, iHeight);
				}
				setStyle();
				updateFeedback();
				updatePuntuation();
			}else{
				// Crear nova pregunta
				oItem = QTIUtil.createHotspotItem();
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
	
	public String getHotspotType(){
		return QTIUtil.getHotspotType((Item)getBeanObject());
	}
	
	protected void setHotspotType(Item oItem, String sType){
		String sCurrentType = getHotspotType();
		if (!sCurrentType.equals(sType)){
			RenderHotspot oRender = QTIUtil.getRenderHotspot(oItem);
			if (oRender!=null){
				if (ZONE_HOTSPOT_TYPE.equals(sType)){
					oRender.setShowDraw("No");
					oRender.setShowOptions("Yes");
					oRender.setTransp("Yes");
				} else if (OPTION_HOTSPOT_TYPE.equals(sType)){
					oRender.setShowDraw("No");
					oRender.setShowOptions("Yes");
					oRender.setTransp("No");
				} else if (DOT_HOTSPOT_TYPE.equals(sType)){
					oRender.setShowDraw("Yes");
					oRender.setShowOptions("Yes");
					oRender.setTransp("No");
				} else if (FREE_HOTSPOT_TYPE.equals(sType)){
					oRender.setShowDraw("No");
					oRender.setShowOptions("No");
					oRender.setTransp("No");
				}
				QVHotspotListControl.updateConditions(mainBean.request,oItem);
			}
		}
	}
	
	public int getMinNumber(){
		int iNumber = 1;
		RenderHotspot oRender = QTIUtil.getRenderHotspot((Item)getBeanObject());
		if (oRender!=null){
			iNumber = oRender.getMinNumber();
		}
		if (iNumber<0) iNumber = 1;
		return iNumber;
	}
	public int getMaxNumber(){
		int iNumber = 1;
		RenderHotspot oRender = QTIUtil.getRenderHotspot((Item)getBeanObject());
		if (oRender!=null){
			iNumber = oRender.getMaxNumber();
		}
		if (iNumber<=0) iNumber = -1;
		return iNumber;
	}
	protected void setResponseNumber(Item oItem, int iMinNumber, int iMaxNumber){
		QTISuperResponse oResponse = QTIUtil.getResponse(oItem);
		RenderHotspot oRender = QTIUtil.getRenderHotspot(oResponse);
		if (oRender!=null){
			if (iMinNumber>iMaxNumber) iMinNumber=iMaxNumber;
			oRender.setMinNumber(iMinNumber);
			oRender.setMaxNumber(iMaxNumber);
			if (iMaxNumber==1){
				oResponse.setCardinality("Single");
			}else{
				oResponse.setCardinality("Multiple");
			}
		}
	}
	
	public String getBackgroundImage(){
		String sURI = null;
		RenderHotspot oRender = QTIUtil.getRenderHotspot((Item)getBeanObject());
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
	
	protected void setBackgroundImage(Item oItem, String sURI, int iWidth, int iHeight){
		RenderHotspot oRender = QTIUtil.getRenderHotspot(oItem);
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
	
	public String getBorderColorStyle(){
		return QTIUtil.getBorderColorStyle((Item)getBeanObject());
	}
	public String getBackgroundImageStyle(){
		String sBgImage = null;
		String sBgStyle = getBackgroundStyle();
		if (sBgStyle!=null){
			try{
				sBgImage = sBgStyle.substring(sBgStyle.indexOf("url(")+4,sBgStyle.indexOf(")"));
			}catch (Exception e){
			}
		}
		return sBgImage;
	}
	public String getBackgroundColorStyle(){
		String sBgColor = null;
		String sBgStyle = getBackgroundStyle();
		if (sBgStyle!=null && isColor(sBgStyle)){
			sBgColor = sBgStyle;
		}
		return sBgColor;
	}

	protected String getBackgroundStyle(){
		String sBgStyle = null;
		String sStyle = QTIUtil.getStyle(getBeanObject());
		if (sStyle!=null){
			StringTokenizer st = new StringTokenizer(sStyle, ",");
			while (st.hasMoreTokens()){
				String s = st.nextToken();
				if (s.trim().startsWith("background:")){
					sBgStyle = s.substring(s.indexOf("background:")+11);
				}
			}
		}
		return sBgStyle;
	}
	
	public void setStyle(){
		String sStyle = "";
		// border-color
		String sBorderColor = getParameter(P_STYLE_BORDER_COLOR);
		if (sBorderColor!=null && isColor(sBorderColor) ){
			sStyle+="border-color:"+sBorderColor+",";
		}
		// background
		String sBgType = getParameter(P_STYLE_BACKGROUND_TYPE);
		if (sBgType!=null){
			String sAction = getParameter(ACTION_PARAM);
			if (BACKGROUND_IMAGE_TYPE.equals(sBgType) && !A_SET_HOTSPOT_BACKGROUND_TYPE.equals(sAction)){
				String sBgImage = getParameter(P_STYLE_BACKGROUND_IMAGE);
				if (sBgImage!=null){
					sStyle+="background:url("+sBgImage+"),";
				}
			}else {
				String sBgColor = getParameter(P_STYLE_BACKGROUND_COLOR);
				if (sBgColor!=null && isColor(sBgColor)){
					sStyle+="background:"+sBgColor+",";
				}
			}
			QTIUtil.setStyle(getBeanObject(), sStyle);
		}
	}
	
	protected boolean isColor(String s){
		return !"#RRGGBB".equalsIgnoreCase(s) && s.trim().startsWith("#") && s.trim().length()==7; 
	}
		
}
