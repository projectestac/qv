/*
 * QTIConditionPanel.java
 *
 * Created on 6 de octubre de 2002, 20:30
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.editor.ManageFrame;
import edu.xtec.qv.qti.GroupCondition;
import edu.xtec.qv.qti.QTICondition;
import edu.xtec.qv.qti.QTIItemResponseProcessing;
import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.util.Messages;

/**
 *
 * @author  Albert
 * @version
 */
public class QTIItemResponseProcessingPanel extends javax.swing.JPanel implements ExtendedRenderer{
    
    protected QTIItemResponseProcessing qtiRespProc;
    
    /** Creates new form QTIConditionPanel */
    public QTIItemResponseProcessingPanel(QTIItemResponseProcessing qtiRespProc) {
        this.qtiRespProc=qtiRespProc;
        initComponents();
        initComponents2();
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
        initMessages();
    }
    
    public void setGroupsCondition(java.util.Vector vGroupCondition){
        if (jlConditions.getModel() instanceof javax.swing.DefaultListModel){
            javax.swing.DefaultListModel dlm=(javax.swing.DefaultListModel)jlConditions.getModel();
            dlm.removeAllElements();
            java.util.Enumeration e=vGroupCondition.elements();
            while (e.hasMoreElements()) dlm.addElement(e.nextElement());
        }

    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        spConditionPanel = new javax.swing.JScrollPane();
        jlConditions = new javax.swing.JList();
        btAdd = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        btDeleteAll = new javax.swing.JButton();
        btModify = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(spConditionPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(jlConditions, gridBagConstraints);

        btAdd.setText("Afegir");
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 2, 2);
        add(btAdd, gridBagConstraints);

        btDelete.setText("Eliminar");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btDelete, gridBagConstraints);

        btDeleteAll.setText("Eliminar tots");
        btDeleteAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteAllActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btDeleteAll, gridBagConstraints);

        btModify.setText("Modificar");
        btModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btModifyActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btModify, gridBagConstraints);

    }//GEN-END:initComponents
    
    private void btModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btModifyActionPerformed
        // Add your handling code here:

        if (jlConditions.getSelectedValue()!=null){
            GroupCondition gc=(GroupCondition)jlConditions.getSelectedValue();
            
            QTICondition qsc=gc.getCondition();
            javax.swing.JPanel jp=qsc.getPanel();
            QTIConditionDialog qscd=new QTIConditionDialog(null,(QTIConditionPanel)jp,true);
            qscd.show();
            if (qscd.isOk()){
            }
        }

    }//GEN-LAST:event_btModifyActionPerformed
    
    private java.util.Vector getListElementsVector(javax.swing.JList jl){
        java.util.Vector vElems=new java.util.Vector();
        for (int i=0;i<jl.getModel().getSize();i++){
            vElems.add(jl.getModel().getElementAt(i));
        }
        return vElems;
    }
    
    private void initComponents2(){
        //spConditionPanel.setViewportView(qtiRespProc.getCondition().getPanel());
        //btGroup.setEnabled(false);// NO IMPLEMENTAT
        //btDegroup.setEnabled(false);// NO IMPLEMENTAT
        
        
        remove(spConditionPanel);
        
        //btGroup.setEnabled(false);// NO IMPLEMENTAT
        //btDegroup.setEnabled(false);// NO IMPLEMENTAT
        
        jlConditions.setModel(new javax.swing.DefaultListModel());
        jlConditions.addListSelectionListener(new javax.swing.event.ListSelectionListener(){
            public void valueChanged(javax.swing.event.ListSelectionEvent e){
                checkButtons();
            }
        });
        checkButtons();
    }
    
    private void checkButtons(){
        if (jlConditions.getSelectedIndex()>=0){
            btDelete.setEnabled(true);
            btModify.setEnabled(true);
        }
        else{
            btDelete.setEnabled(false);
            btModify.setEnabled(false);
        }
        if (jlConditions.getModel().getSize()>0) btDeleteAll.setEnabled(true);
        else btDeleteAll.setEnabled(false);
    }
            
    private void btDeleteAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteAllActionPerformed
        // Add your handling code here:
        if (jlConditions.getModel() instanceof javax.swing.DefaultListModel){
            javax.swing.DefaultListModel dlm=(javax.swing.DefaultListModel)jlConditions.getModel();
            for (int i=0;dlm.getSize()>0;i++){
                GroupCondition gc=(GroupCondition)dlm.getElementAt(0);
                dlm.removeElement(gc);
                qtiRespProc.setCurrentGroupCondition(gc);
                qtiRespProc.removeCurrentGroupCondition();
            }
        }
        checkButtons();
    }//GEN-LAST:event_btDeleteAllActionPerformed
    
    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        // Add your handling code here:
        if (jlConditions.getSelectedValue()!=null){
            GroupCondition gc=(GroupCondition)jlConditions.getSelectedValue();
            
            if (jlConditions.getModel() instanceof javax.swing.DefaultListModel){
                javax.swing.DefaultListModel dlm=(javax.swing.DefaultListModel)jlConditions.getModel();
                dlm.removeElement(gc);
            }
            
            qtiRespProc.setCurrentGroupCondition(gc);
            qtiRespProc.removeCurrentGroupCondition();
        }
        checkButtons();

    }//GEN-LAST:event_btDeleteActionPerformed
    
    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        // Add your handling code here:
        /*qtiRespProc.addCurrentGroupCondition();
        initComponents2(); //actualitzo el currentGroupCondition*/
        //creator.QTISuperCondition gsc=qtiRespProc.getCurrentGroupSuperCondition();
        QTICondition gsc=qtiRespProc.getCondition();
        //System.out.println("Condicions:"+gsc.gc.getNumGroupConditions());
        gsc.setFirstCondition(gsc.gc.getNumGroupConditions()==0);
        javax.swing.JPanel jp=gsc.getPanel();
        QTIConditionDialog qscd=new QTIConditionDialog(ManageFrame.frame,(QTIConditionPanel)jp,true);
        qscd.show();
        if (qscd.isOk()){
            //qtiRespProc.addCurrentGroupSuperCondition();
            qtiRespProc.addCurrentGroupCondition();
        }
        checkButtons();

    }//GEN-LAST:event_btAddActionPerformed
    
    protected void initMessages(){
        btAdd.setText(Messages.getLocalizedString("Add"));
        btDelete.setText(Messages.getLocalizedString("Remove"));
        btDeleteAll.setText(Messages.getLocalizedString("RemoveAll"));
        //btGroup.setText(Messages.getLocalizedString("Group"));
        //btDegroup.setText(Messages.getLocalizedString("Degroup"));
        btModify.setText(Messages.getLocalizedString("Modify"));
    }
    
    public void setBackground(java.awt.Color c){
        super.setBackground(c);
        if (spConditionPanel!=null) spConditionPanel.setBackground(c);
        java.awt.Color componentColor=CreatorProperties.getComponentColor();
        if (componentColor!=null){
            if (btDeleteAll!=null) btDeleteAll.setBackground(componentColor);
            if (btAdd!=null) btAdd.setBackground(componentColor);
            if (btModify!=null) btModify.setBackground(componentColor);
            //if (btDegroup!=null) btDegroup.setBackground(componentColor);
            //if (btGroup!=null) btGroup.setBackground(componentColor);
            if (btDelete!=null) btDelete.setBackground(componentColor);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btDeleteAll;
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btModify;
    private javax.swing.JScrollPane spConditionPanel;
    private javax.swing.JButton btDelete;
    private javax.swing.JList jlConditions;
    // End of variables declaration//GEN-END:variables
    
}