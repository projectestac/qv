/*
 * QVFullBean.java
 * 
 * Created on 05/febrer/2004
 */
package edu.xtec.qv.editor.beans;

import edu.xtec.qv.editor.util.QTIUtil;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.Section;


/**
 * @author sarjona
 */
/**
 * @author sarjona
 */
/**
 * @author sarjona
 */
public class QVFullBean extends QVSpecificBean implements IQVListControlBean, IQVInteractionBean {
	
	public static final String ADD_PREGUNTA_SELECCIO_ACTION_PARAM="add_pregunta_seleccio";
	

	public QVFullBean(QVBean mainBean){
		super(mainBean);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.QVBean#start()
	 */
	public boolean start() {
		boolean bOk = true;
		try{
			if (mainBean.getQuadern()==null){
				mainBean.redirectResponse(mainBean.getTranslatedPath(JSP_QUADERN));
			}
			actualitzarDadesFull();
			String sAccio = getParameter(ACTION_PARAM);
			if (sAccio!=null && sAccio.trim().length()>0){
				if (!sAccio.equalsIgnoreCase(UP_ITEM_ACTION_PARAM) &&
					!sAccio.equalsIgnoreCase(DOWN_ITEM_ACTION_PARAM)){
				}
				if (sAccio.equalsIgnoreCase(ADD_PREGUNTA_SELECCIO_ACTION_PARAM)){
					mainBean.redirectResponse(mainBean.getTranslatedPath(JSP_PREGUNTA_SELECCIO));
				} else if ( sAccio.equalsIgnoreCase(DEL_FULL_ACTION_PARAM) ||
							sAccio.equalsIgnoreCase(DEL_ACTION_PARAM)){
					delFull();
					mainBean.redirectResponse(mainBean.getTranslatedPath(JSP_QUADERN));
				} else if (sAccio.equalsIgnoreCase(UP_ACTION_PARAM)){
					int iOldPosicio = mainBean.getQuadern().getSectionPosition(mainBean.getIdFull());
					int iNewPosicio = iOldPosicio-1;
					if (iNewPosicio>=0 && iNewPosicio<mainBean.getQuadern().getSections().size()){
						mainBean.getQuadern().setFullPosicio(mainBean.getFull(), iOldPosicio, iNewPosicio);
					}
				} else if (sAccio.equalsIgnoreCase(DOWN_ACTION_PARAM)){
					int iOldPosicio = mainBean.getQuadern().getSectionPosition(mainBean.getIdFull());
					int iNewPosicio = iOldPosicio+1;
					if (iNewPosicio>=0 && iNewPosicio<mainBean.getQuadern().getSections().size()){
						mainBean.getQuadern().setFullPosicio(mainBean.getFull(), iOldPosicio, iNewPosicio);
					}
				}
				if (getListControl()!=null){
					getListControl().start(getBeanObject());
				}
			} 
		} catch (Exception e){
			logger.error("EXCEPCIO inicialitzant QVFullBean --> "+e);
			e.printStackTrace();
			bOk = false;
		}
		return bOk;
	}

//	***************************	
//	* Full 
//	***************************
	protected void actualitzarDadesFull(){
		Section oFull = mainBean.getFull();
		// Actualitzar titol
		if (getParameter(TITLE_PARAM)!=null){
			oFull.setTitle(getParameter(TITLE_PARAM));
		}
		// Actualitza algoritme de correccio
		String sScoreModel = getParameter(SCOREMODEL_PARAM);
		if (sScoreModel!=null){
			QTIUtil.setScoreModel(oFull, sScoreModel);
		}
		
		//-->Albert
		String sFullOrder = getParameter(SECTION_ORDER_PARAM);
		if (sFullOrder!=null){
			QTIUtil.setSectionSelectionOrdering(mainBean.getQuadern(), oFull, sFullOrder);
			//////oFull.setFullOrder(sFullOrder);
		}
		//ordre_full
		//<--
		
		// Actualitzar interaction
		String sInteractionswitch = getParameter(P_INTERACTIONSWITCH);
		if (sInteractionswitch!=null && sInteractionswitch.trim().length()>0){
			QTIUtil.setInteractionswitch(oFull, sInteractionswitch);
		}		
		//logger.debug("full->"+edu.xtec.qv.qti.util.XMLUtil.showElement(oFull.getXML()));	
	}
	
	public void delFull(){
		String sIdent = getParameter(IDENT_FULL_PARAM);
		if (sIdent==null || sIdent.trim().length()==0){
			sIdent = mainBean.getIdFull();
		}
		QTIUtil.delFull(mainBean.getQuadern(), sIdent);
		mainBean.setFull(null);
	}
			
	public String getTitle(){
		String sTitle = mainBean.getFull().getTitle();
		if (sTitle == null){
			sTitle = "";
		}
		return sTitle;
	}
	
	public String getScoreModel(){
		String sScoreModel = QTIUtil.getScoreModel(mainBean.getFull());
		if (sScoreModel==null) {
			sScoreModel = QTIObject.SUM_OF_SCORES;
		}
		return sScoreModel;
	}
	
	public String getFullOrder(){//Albert
		String sFullOrder = QTIUtil.getSectionRandomOrder(mainBean.getQuadern(), mainBean.getFull());
		if (sFullOrder == null){
			sFullOrder="no_random";
		}
		return sFullOrder;
	}
	
	
//	************************************	
//	* Implementacio IQVListControlBean
//	************************************

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.QVSpecificBean#getBeanObject()
	 */
	public QTIObject getBeanObject(){
		return mainBean.getFull();
	}

//	************************************	
//	* Implementacio IQVInteractionBean
//	************************************

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.IQVInteractionBean#isInteractionswitch()
	 */
	public boolean isInteractionswitch() {
		return QTIUtil.isInteractionswitch(mainBean.getQuadern(), mainBean.getFull());
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.IQVInteractionBean#getInteractionText()
	 */
	public String getInteractionText() {
		return "section.interactionswitch";
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.IQVInteractionBean#getInteractionTip()
	 */
	public String getInteractionTip() {
		return "section.interactionswitch.tip";
	}

}
