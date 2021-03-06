/*
 * QTISelectionResponsePanel.java
 *
 * Created on 6 de octubre de 2002, 17:52
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.qti.QTIMaterial;
import edu.xtec.qv.qti.QTISelectionResponse;
import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.util.Messages;

/**
 *
 * @author  Albert
 * @version
 */
public class QTISelectionResponsePanel extends javax.swing.JPanel implements ExtendedRenderer{
    
    protected QTISelectionResponse qtiItemSelResp;
    
    /** Creates new form QTISelectionResponsePanel */
    public QTISelectionResponsePanel(QTISelectionResponse qtiItemSelResp, QTIMaterial qtiMat) {
        this(qtiItemSelResp, qtiMat.getPanel());
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
    }
    
    public QTISelectionResponsePanel(QTISelectionResponse qtiItemSelResp, javax.swing.JPanel qtiMatPanel) {
        this.qtiItemSelResp=qtiItemSelResp;
        initComponents();
        jScrollPane1.setViewportView(qtiMatPanel);
        if (CreatorProperties.getBackground()!=null) setBackground(CreatorProperties.getBackground());
        initMessages();
    }
    
    public void setIdent(String sIdent){
        tfIdent.setText(sIdent);
    }
    
    public void setMaterial(QTIMaterial qtiMat){
        jScrollPane1.setViewportView(qtiMat.getPanel());
    }
    
    public String getIdent(){
        return tfIdent.getText();
    }
    
    public void requestIdentFieldFocus(){
        tfIdent.requestFocus();
        tfIdent.selectAll();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lbIdent = new javax.swing.JLabel();
        tfIdent = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();

        setLayout(new java.awt.GridBagLayout());

        lbIdent.setText("Identificador");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(lbIdent, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(tfIdent, gridBagConstraints);

        jScrollPane1.setBorder(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.65;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jScrollPane1, gridBagConstraints);

    }//GEN-END:initComponents
        
    protected void initMessages(){
        lbIdent.setText(Messages.getLocalizedString("Identifier"));
        lbIdent.setToolTipText(Messages.getLocalizedString("OptionIdentifierDesc"));
        //btAdd.setText(Messages.getLocalizedString("Add"));
    }
    
    public void setBackground(java.awt.Color c){
        super.setBackground(c);
        if (jScrollPane1!=null) jScrollPane1.setBackground(c);
        java.awt.Color componentColor=CreatorProperties.getComponentColor();
        if (componentColor!=null){
            //if (btAdd!=null) btAdd.setBackground(componentColor);
            if (lbIdent!=null) lbIdent.setBackground(componentColor);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField tfIdent;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbIdent;
    // End of variables declaration//GEN-END:variables
    
}