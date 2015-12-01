package edu.xtec.qv.editor.ui;

import edu.xtec.qv.qti.BagMaterial;
import edu.xtec.qv.qti.util.CreatorProperties;

public class QTISectionPresentationPanel extends javax.swing.JPanel{

    //creator.QTIMaterial qtiMat;
    BagMaterial qtiMat;
    //creator.QTIMultimediaMaterial qtiMultMat;
    
    public QTISectionPresentationPanel(BagMaterial qtiMat){////,creator.QTIMultimediaMaterial qtiMultMat){
    	this.qtiMat=qtiMat;
    	//this.qtiMultMat=qtiMultMat;
      initComponents ();
      if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
        //jSplitPane=new javax.swing.JSplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT);
        //jSplitPane.setTopComponent(qtiMat.getPanel());
        //jSplitPane.setBottomComponent(qtiMultMat.getPanel());
        //add(jSplitPane,java.awt.BorderLayout.CENTER);
        add(qtiMat.getPanel(),java.awt.BorderLayout.CENTER);//
    }

    public void setBackground(java.awt.Color c){
    	super.setBackground(c);
    	//if (qtiMat!=null && qtiMat.getPanel()!=null) qtiMat.getPanel().setBackground(c);
    }
    
    //private javax.swing.JSplitPane jSplitPane;

}