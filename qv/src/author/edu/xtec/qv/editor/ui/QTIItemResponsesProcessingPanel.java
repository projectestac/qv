/*
 * QTIItemResponsesProcessingPanel.java
 *
 * Created on 6 de octubre de 2002, 20:15
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.editor.ManageFrame;
import edu.xtec.qv.qti.QTIItemResponseProcessing;
import edu.xtec.qv.qti.QTIItemResponsesProcessing;
import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.util.Messages;

/**
 *
 * @author  Albert
 * @version
 */
public class QTIItemResponsesProcessingPanel extends javax.swing.JPanel implements ExtendedRenderer{
    
    protected QTIItemResponsesProcessing qtiItemRespProc;
    protected java.util.Vector vResponsesProcessing;
    
    //public QTIItemResponsesProcessingPanel(){}
    
    /** Creates new form QTIItemResponsesProcessingPanel */
    public QTIItemResponsesProcessingPanel(QTIItemResponsesProcessing qtiItemRespProc, java.util.Vector vResponsesProcessing) {
        this.qtiItemRespProc=qtiItemRespProc;
        this.vResponsesProcessing=vResponsesProcessing;
        if (vResponsesProcessing==null) this.vResponsesProcessing=new java.util.Vector();
        initComponents();
        initComponents2();
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
        initMessages();
    }
    
    public void setResponsesProcessing(java.util.Vector vQTIItemResponseProcessing){
        //System.out.println("AAAAAAAAAAAAAAAAAAAAA");
        vResponsesProcessing=vQTIItemResponseProcessing;
        jlConditions.setListData(vResponsesProcessing);
    }
    
    public void deleteSelectedResponseProcessing(){
        QTIItemResponseProcessing selQtiRespProc=(QTIItemResponseProcessing)jlConditions.getSelectedValue();
        vResponsesProcessing.remove(selQtiRespProc);
        jlConditions.setListData(vResponsesProcessing);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jlConditions = new javax.swing.JList();
        btAdd = new javax.swing.JButton();
        btRemove = new javax.swing.JButton();
        btModify = new javax.swing.JButton();
        btMoveUp = new javax.swing.JButton();
        btMoveDown = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 8);
        add(jlConditions, gridBagConstraints);

        btAdd.setText("Afegir");
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 3, 2);
        add(btAdd, gridBagConstraints);

        btRemove.setText("Eliminar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 3, 2);
        add(btRemove, gridBagConstraints);

        btModify.setText("Modificar");
        btModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btModifyActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 3, 2);
        add(btModify, gridBagConstraints);

        btMoveUp.setText("Pujar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 3, 2);
        add(btMoveUp, gridBagConstraints);

        btMoveDown.setText("Baixar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 3, 2);
        add(btMoveDown, gridBagConstraints);

    }//GEN-END:initComponents
    
    protected void initComponents2(){
        btRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSelectedResponseProcessing();
            }
        });
        jlConditions.addListSelectionListener(new javax.swing.event.ListSelectionListener(){
            public void valueChanged(javax.swing.event.ListSelectionEvent e){
                updateButtonsState();
            }
        });
        btMoveUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int sel=jlConditions.getSelectedIndex();
                int size=jlConditions.getModel().getSize();
                Object[] o=new Object[size];
                for (int i=0;i<size;i++){
                    if (i==(sel-1)) o[i]=jlConditions.getModel().getElementAt(sel);
                    else if (i==sel && sel>0) o[i]=jlConditions.getModel().getElementAt(sel-1);
                    else o[i]=jlConditions.getModel().getElementAt(i);
                }
                jlConditions.setListData(o);
                if (sel>0) jlConditions.setSelectedIndex(sel-1);
            }
        });
        btMoveDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int sel=jlConditions.getSelectedIndex();
                int size=jlConditions.getModel().getSize();
                Object[] o=new Object[size];
                for (int i=0;i<size;i++){
                    if (i==sel && (sel<size)) o[i]=jlConditions.getModel().getElementAt(sel+1);
                    else if (i==(sel+1)) o[i]=jlConditions.getModel().getElementAt(sel);
                    else o[i]=jlConditions.getModel().getElementAt(i);
                }
                jlConditions.setListData(o);
                if (sel<size) jlConditions.setSelectedIndex(sel+1);
            }
        });
        updateButtonsState();
    }
    
    protected void updateButtonsState(){
        if (jlConditions.getSelectedIndex()>=0){
            btRemove.setEnabled(true);
            btModify.setEnabled(true);
            if (jlConditions.getSelectedIndex()>0) btMoveUp.setEnabled(true);
            else btMoveUp.setEnabled(false);
            if (jlConditions.getSelectedIndex()<(jlConditions.getModel().getSize()-1)) btMoveDown.setEnabled(true);
            else btMoveDown.setEnabled(false);
        }
        else{
            btRemove.setEnabled(false);
            btModify.setEnabled(false);
            btMoveUp.setEnabled(false);
            btMoveDown.setEnabled(false);
        }
    }
    
    protected void btModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btModifyActionPerformed
        // Add your handling code here:
        if (jlConditions.getSelectedValue()!=null){
            QTIItemResponseProcessing qtiItRespsProc=(QTIItemResponseProcessing)jlConditions.getSelectedValue();
            qtiItemRespProc.setModifyingResponseProc(qtiItRespsProc);//Marco l'antic objecte indicant que s'est� modificant
            //creator.QTIItemResponseProcessing qtiItRespsProc2=(creator.QTIItemResponseProcessing)qtiItRespsProc.clone();
            ////// S'HAURIA DE FER UN CLONE PER SI DESPR�S ES VOL FER CANCEL!!!!
            QTIResponsesProcessingDialog qtiRespProcDialog=new QTIResponsesProcessingDialog(ManageFrame.frame,true,qtiItRespsProc/*2*/);
        }
    }//GEN-LAST:event_btModifyActionPerformed
    
    protected void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        // Add your handling code here:
        //System.out.println("Add");
        //java.util.Vector vIdents=qtiItemRespProc.getIdents(); ///
        qtiItemRespProc.setModifyingResponseProc(null);
        QTIItemResponseProcessing qtiItRespsProc=new QTIItemResponseProcessing(qtiItemRespProc);
        ////vResponsesProcessing.add(qtiItRespsProc);
        //QTIResponsesProcessingDialog qtiRespProcDialog=new QTIResponsesProcessingDialog(null,true,qtiItemRespsProc,new creator.QTICondition(),new creator.QTIFeedback());
        QTIResponsesProcessingDialog qtiRespProcDialog=new QTIResponsesProcessingDialog(ManageFrame.frame,true,qtiItRespsProc);
        //System.out.println("Fi add");
    }//GEN-LAST:event_btAddActionPerformed
    
    protected void initMessages(){
        btAdd.setText(Messages.getLocalizedString("Add"));
        btAdd.setToolTipText(Messages.getLocalizedString("AddEvaluation"));
        btRemove.setText(Messages.getLocalizedString("Remove"));
        btRemove.setToolTipText(Messages.getLocalizedString("RemoveEvaluation"));
        btModify.setText(Messages.getLocalizedString("Modify"));
        btModify.setToolTipText(Messages.getLocalizedString("ModifyEvaluation"));
        btMoveUp.setText(Messages.getLocalizedString("Up"));
        btMoveDown.setText(Messages.getLocalizedString("Down"));
    }
    
    public void setBackground(java.awt.Color c){
        super.setBackground(c);
        java.awt.Color componentColor=CreatorProperties.getComponentColor();
        if (componentColor!=null){
            if (btMoveDown!=null) btMoveDown.setBackground(componentColor);
            if (btMoveUp!=null) btMoveUp.setBackground(componentColor);
            if (btRemove!=null) btRemove.setBackground(componentColor);
            if (btAdd!=null) btAdd.setBackground(componentColor);
            if (btModify!=null) btModify.setBackground(componentColor);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btMoveDown;
    private javax.swing.JButton btMoveUp;
    private javax.swing.JButton btRemove;
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btModify;
    protected javax.swing.JList jlConditions;
    // End of variables declaration//GEN-END:variables
    
}