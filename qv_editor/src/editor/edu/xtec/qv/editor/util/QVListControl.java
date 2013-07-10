/*
 * QVListObject.java
 * 
 * Created on 08-jun-2004
 */
package edu.xtec.qv.editor.util;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import edu.xtec.qv.editor.beans.IQVListControlBean;
import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.Presentation;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.QTISuperResponse;

/**
 * @author sarjona
 */
public abstract class QVListControl{

	protected static Logger logger = Logger.getRootLogger();

	Object oObject = null;
	HttpServletRequest request;
	String sAction;
	String sIdent;
	String sCurrent;
	
	public QVListControl(HttpServletRequest request){
		this.request=request;
	}
	
	public abstract Vector getListObjects(QTIObject oQTI);

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.beans.IQVListControlBean#getObject(java.lang.String)
	 */
	public abstract Object getObject(QTIObject oQTI, String sIdent);

	public abstract Object getPreviousObject(QTIObject oQTI, String sIdent);
	
	public abstract Object getNextObject(QTIObject oQTI, String sIdent);
	
	public String getCurrentListObject(){
		if (sCurrent==null){
			sCurrent = getIdent();
		}
		if (sCurrent==null) sCurrent = "";
		return sCurrent;
	}
	
	protected void setCurrentListObject(String sCurrent){
		this.sCurrent=sCurrent;
	}
		
	public abstract void addListObject(QTIObject oQTI, String sIdent);

	public abstract void delListObject(QTIObject oQTI, String sIdent);
	
	protected abstract void setListObject(QTIObject oQTI, String sIdent);
	
	public abstract void upListObject(QTIObject oQTI, String sIdent);

	public abstract void downListObject(QTIObject oQTI, String sIdent);
	
	public boolean start(QTIObject oQTI){
		boolean bOk = true;
		try{
			//logger.debug("QVListControl-> action="+getAction());
			if (getAction()!=null){
				if (getAction().equalsIgnoreCase(IQVListControlBean.A_ADD_LIST_OBJECT)){
					//logger.debug("addListObject-> ident="+getIdent());
					addListObject(oQTI, getIdent());
				}else if (getAction().equalsIgnoreCase(IQVListControlBean.A_DEL_LIST_OBJECT)){
					//logger.debug("delListObject-> ident="+getIdent());
					delListObject(oQTI, getIdent());
				}else if (getAction().equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT)){
					//logger.debug("setListObject-> ident="+getIdent());
					setListObject(oQTI, getIdent());
				}else if (getAction().equalsIgnoreCase(IQVListControlBean.A_UP_LIST_OBJECT)){
					//logger.debug("upListObject-> ident="+getIdent());
					upListObject(oQTI, getIdent());
				}else if (getAction().equalsIgnoreCase(IQVListControlBean.A_DOWN_LIST_OBJECT)){
					//logger.debug("downListObject-> ident="+getIdent());
					downListObject(oQTI, getIdent());
				}
			}
		}catch(Exception e){
			logger.error("EXCEPCIO inicialitzant QVListControl --> "+e);
			e.printStackTrace();
			bOk = false;
		}
		return bOk;
	}
	
	protected String getAction(){
		if (sAction==null){
			sAction = request.getParameter(QVConstants.ACTION_PARAM);
			if (request.getParameter(sAction)!=null){
				sAction = request.getParameter(sAction);
			}
		}
		return sAction;
	}
	
	public boolean isListControlAction(){
		boolean bIsListControl = false;
		String sAction = request.getParameter(QVConstants.ACTION_PARAM);
		if (sAction!=null){
			if (request.getParameter(sAction)!=null){
				sAction = request.getParameter(sAction);
			}
			bIsListControl = sAction.equals(IQVListControlBean.A_ADD_LIST_OBJECT) ||
							 sAction.equals(IQVListControlBean.A_DEL_LIST_OBJECT) ||
				 			 sAction.equals(IQVListControlBean.A_SET_LIST_OBJECT) ||
							 sAction.equals(IQVListControlBean.A_UP_LIST_OBJECT) ||
							 sAction.equals(IQVListControlBean.A_DOWN_LIST_OBJECT);
		}
		return bIsListControl;
	}
	
	protected String getIdent(){
		if (sIdent==null){
			String sIdentName = request.getParameter("p_list_name");
			sIdent = request.getParameter(sIdentName);
			if (sIdent==null || sIdent.trim().length()<=0 || sIdent.equalsIgnoreCase("null")){
				sIdent = request.getParameter("p_default_ident_list");
			}
		}
		//logger.debug(sIdentName+"="+sIdent);
		return sIdent;
	}
	
	/**
	 * Retorna el parametre; si no el troba retorna el valor per defecte
	 * @param sParam
	 * @param sDefault
	 * @return
	 */
	protected static String getParameter(HttpServletRequest request, String sParam, String sDefault){
		String sResult=sDefault;
		try{
			sResult= request.getParameter(sParam);
			if (sResult==null || sResult.trim().length()<=0 || sResult.equalsIgnoreCase("null")){
				sResult=sDefault;
			}
		}
		catch (Exception e){
			logger.debug("EXCEPCIO obtenint el parametre '"+sParam+"' -->"+e);
		}
		return sResult;
	}
	
	/**
	 * Retorna un paràmetre enter
	 * @param request
	 * @param sParam
	 * @param iDefault
	 * @return
	 */
	protected static int getIntParameter(HttpServletRequest request, String sParam, int iDefault){
		int result=iDefault;
		try{
			String s=request.getParameter(sParam);
			if (s!=null && s.trim().length()>0)
				result=Integer.parseInt(s);
		}
		catch (Exception e){
			//logger.debug("EXCEPCIO obtenint el parametre '"+sParam+"' -->"+e);
		}
		return result;
	}

	public static QTISuperResponse getItemResponse(Item oItem){
		QTISuperResponse oResponse = null;
		if (oItem!=null){
			Presentation oPresentation = (oItem).getPresentation();
			if (oPresentation!=null){
				if (!oPresentation.getResponses().isEmpty()){
					oResponse = (QTISuperResponse)oPresentation.getResponses().firstElement();
				}
			}
		}
		return oResponse;		
	}
	
	

}
