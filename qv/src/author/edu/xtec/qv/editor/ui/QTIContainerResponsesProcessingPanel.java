package edu.xtec.qv.editor.ui;

import edu.xtec.qv.qti.util.CreatorProperties;

public class QTIContainerResponsesProcessingPanel extends javax.swing.JPanel{
    
    public QTIContainerResponsesProcessingPanel(javax.swing.JPanel jpScoring, javax.swing.JPanel jpResp){
        this.jpScoring=jpScoring;
        this.jpResp=jpResp;
        initComponents();
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
    }
    
    protected void initComponents(){
        //setLayout(new java.awt.GridLayout(2,1));
        setLayout(new java.awt.BorderLayout());
        //add(jpScoring);
        //add(jpResp);
        add(jpScoring,java.awt.BorderLayout.NORTH);
        add(jpResp,java.awt.BorderLayout.CENTER);
    }
    
    public void setBackground(java.awt.Color c){
        super.setBackground(c);
        if (jpScoring!=null) jpScoring.setBackground(c);
        if (jpResp!=null) jpResp.setBackground(c);
    }
    
    protected	javax.swing.JPanel jpScoring;
    protected javax.swing.JPanel jpResp;
    
}