/*
 * QTISuperResponsesProcessingPanel.java
 *
 * Created on 2 de noviembre de 2002, 18:53
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.editor.ManageFrame;
import edu.xtec.qv.qti.QTISuperResponseProcessing;
import edu.xtec.qv.qti.QTISuperResponsesProcessing;

/**
 *
 * @author  Albert
 * @version 
 */
public class QTISuperResponsesProcessingPanel extends QTIItemResponsesProcessingPanel{

    protected QTISuperResponsesProcessing qtiSuperRespProc;
    protected java.util.Vector vSuperResponsesProcessing;
    
    /** Creates new form QTIItemResponsesProcessingPanel */
    public QTISuperResponsesProcessingPanel(QTISuperResponsesProcessing qtiSuperRespProc, java.util.Vector vSuperResponsesProcessing){
    	super(null,null);
      this.qtiSuperRespProc=qtiSuperRespProc;
      this.vSuperResponsesProcessing=vSuperResponsesProcessing;
      if (vSuperResponsesProcessing==null) this.vSuperResponsesProcessing=new java.util.Vector();
      //initComponents ();
      //initComponents2();
      //if (creator.CreatorProperties.getBackground()!=null || creator.CreatorProperties.getComponentColor()!=null) setBackground(creator.CreatorProperties.getBackground());
    }
    
    public void setResponsesProcessing(java.util.Vector vQTISuperResponseProcessing){
    	//System.out.println("Poso "+vQTISuperResponseProcessing.size()+" opcions a la llista");
        vSuperResponsesProcessing=vQTISuperResponseProcessing;
        jlConditions.setListData(vSuperResponsesProcessing);
    }
    
    public void deleteSelectedResponseProcessing(){
    	QTISuperResponseProcessing selQtiRespProc=(QTISuperResponseProcessing)jlConditions.getSelectedValue();
    	vSuperResponsesProcessing.remove(selQtiRespProc);
    	jlConditions.setListData(vSuperResponsesProcessing);
    }
    
    protected void btModifyActionPerformed(java.awt.event.ActionEvent evt){
        if (jlConditions.getSelectedValue()!=null){            
            QTISuperResponseProcessing qtiSupRespsProc=(QTISuperResponseProcessing)jlConditions.getSelectedValue();
            qtiSuperRespProc.setModifyingResponseProc(qtiSupRespsProc);
            QTISuperResponsesProcessingDialog qtiRespProcDialog=new QTISuperResponsesProcessingDialog(ManageFrame.frame,true,qtiSupRespsProc);
        }
    }

    protected void btAddActionPerformed(java.awt.event.ActionEvent evt) {
        qtiSuperRespProc.setModifyingResponseProc(null);
        QTISuperResponseProcessing qtiSupRespsProc=new QTISuperResponseProcessing(qtiSuperRespProc);
        QTISuperResponsesProcessingDialog qtiRespProcDialog=new QTISuperResponsesProcessingDialog(ManageFrame.frame,true,qtiSupRespsProc);
    }
}