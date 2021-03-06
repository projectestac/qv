/*
 * QTIPresentationMaterialPanel.java
 *
 * Created on 14 / octubre / 2002, 08:56
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.qti.QTIInformation;
import edu.xtec.qv.qti.util.CreatorProperties;

/**
 *
 * @author  allastar
 */
public class QTIInformationPanel extends javax.swing.JPanel implements ExtendedRenderer{
    
    protected QTIInformation qtiInfo;
    
    /** Creates new form QTIPresentationMaterialPanel */
    public QTIInformationPanel(QTIInformation qtiInfo) {
        this.qtiInfo=qtiInfo;
        initComponents();
        initComponents2();
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
        jSplitPane1.setDividerLocation(200);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jSplitPane1 = new javax.swing.JSplitPane();

        setLayout(new java.awt.GridBagLayout());

        jSplitPane1.setBorder(new javax.swing.border.TitledBorder(""));
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jSplitPane1, gridBagConstraints);

    }//GEN-END:initComponents
    
    private void initComponents2(){
        if (qtiInfo.getRubric()!=null) jSplitPane1.setTopComponent(qtiInfo.getRubric().getPanel());
        if (qtiInfo.getObjectives()!=null) jSplitPane1.setBottomComponent(qtiInfo.getObjectives().getPanel());
        jSplitPane1.setDividerLocation((int)getHeight()/2);
        //if (qtiInfo.getRubric()!=null) spRubric.setViewportView();
        //if (qtiInfo.getObjectives()!=null) spObjectives.setViewportView();
    }
    
    public void setBackground(java.awt.Color c){
    	super.setBackground(c);
        if (jSplitPane1!=null){
            if (jSplitPane1.getTopComponent()!=null) jSplitPane1.getTopComponent().setBackground(c);
            if (jSplitPane1.getBottomComponent()!=null) jSplitPane1.getBottomComponent().setBackground(c);
        }
    	/*if (spRubric!=null) spRubric.setBackground(c);
    	if (spObjectives!=null) spObjectives.setBackground(c);
    	java.awt.Color componentColor=CreatorProperties.getComponentColor();
    	if (componentColor!=null){
    		if (lbRubric!=null) lbRubric.setBackground(componentColor);
    		if (lbObjectives!=null) lbObjectives.setBackground(componentColor);
    	}*/
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane jSplitPane1;
    // End of variables declaration//GEN-END:variables
    
}