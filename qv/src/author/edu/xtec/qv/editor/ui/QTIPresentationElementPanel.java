/*
 * QTIPresentationElementPanel.java
 *
 * Created on 16 / octubre / 2002, 17:27
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.qti.QTIPresentationElement;
import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.util.Messages;

/**
 *
 * @author  allastar
 */
public class QTIPresentationElementPanel extends javax.swing.JPanel implements ExtendedRenderer{
    
    protected QTIPresentationElement qtiPresElem;
    
    /** Creates new form QTIPresentationElementPanel */
    public QTIPresentationElementPanel(QTIPresentationElement qtiPresElem) {
        this.qtiPresElem=qtiPresElem;
        initComponents();
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
    }
    
    //public void setItemPresentationPanel(QTIMaterialPanel jpQTIItPresPanel){
    public void setItemPresentationPanel(javax.swing.JPanel jpQTIItPresPanel){
        jTabbedPane1.addTab(Messages.getLocalizedString("Wording"), null, jpQTIItPresPanel, Messages.getLocalizedString("QuestionWording"));
    }

    //public void setGlobalItemResponsesPanel(QTIItemResponsesPanel jpQTIGlobItResp){
    public void setGlobalItemResponsesPanel(javax.swing.JPanel jpQTIGlobItResp){
        jTabbedPane1.addTab(Messages.getLocalizedString("Responses"), null, jpQTIGlobItResp, Messages.getLocalizedString("QuestionResponses"));
    }
    
    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        setLayout(new java.awt.BorderLayout());
        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }
    
    public void setBackground(java.awt.Color bgColor){
    	super.setBackground(bgColor);
    }
    
    private javax.swing.JTabbedPane jTabbedPane1;    
}