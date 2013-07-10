/*
 * QVTargetListControl.java
 * 
 * Created on 22/novembre/2004
 */
package edu.xtec.qv.editor.util;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.QTISuperRender;
import edu.xtec.qv.qti.QTISuperResponse;
import edu.xtec.qv.qti.ResponseLabel;
import edu.xtec.qv.qti.Varsubset;
import edu.xtec.qv.qti.util.StringUtil;

/**
 * @author sarjona
 */
public class QVTargetListControl extends QVRareaListControl{

	public QVTargetListControl(HttpServletRequest request){
		super(request);
	}
	
	protected void addResponseLabel(Item oItem, QTISuperRender oRender, ResponseLabel oNew){
		oRender.addResponseLabel(oNew);
	}
	
	protected void setResponseLabel(Item oItem, QTISuperResponse oResponse, QTISuperRender oRender, String sOldIdent, ResponseLabel oNew){
		setAndUpdateConditions(oItem, oResponse.getIdent(), sOldIdent, oNew.getIdent());
		oRender.setResponseLabel(sOldIdent, oNew);
		setMatchgroup(oItem, getAllTargetsString(oResponse));			
	}

	protected void setMatchgroup(QTIObject o, String sMatchgroup){
		Vector vSources = QVSourceListControl.getSourceResponseLabel(o);
		Enumeration enumSources = vSources.elements();
		while (enumSources.hasMoreElements()){
			ResponseLabel rl = (ResponseLabel)enumSources.nextElement();
			rl.setMatchgroup(sMatchgroup);
		}
	}
	
	protected void setAndUpdateConditions(Item oItem, String sRespident, String sOldSource, String sNewSource){
		boolean bSet = false;
		Vector vSubsets = getAllVarsubsets(oItem);
		Enumeration enumSubsets = vSubsets.elements();
		while (enumSubsets.hasMoreElements()){
			Varsubset oVar = (Varsubset)enumSubsets.nextElement();
			if (oVar.getSubsets().contains(sOldSource)){
				oVar.setText(StringUtil.replace(oVar.getText(), sOldSource, sNewSource));
				bSet = true;
			}
		}
		if (bSet){
			updateConditions(oItem, getDragDropConditions(sRespident, vSubsets));
		}
	}

	protected void delConditions(Item oItem, String sRespident, String sIdent){
		delDragDropConditions(oItem, sRespident, sIdent);
	}

	public static Vector getTargetListObjects(QTIObject o){
		return (new QVTargetListControl(null)).getListObjects(o);
	}

}
