/*
 * QTIConditionResultPanel.java
 *
 * Created on 11 / octubre / 2002, 09:03
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.util.Messages;

/**
 *
 * @author  allastar
 */
public class QTIConditionResultPanel extends javax.swing.JPanel implements ExtendedRenderer{
    
    protected javax.swing.JPanel jpQTIFeedbackPanel;
    
    /** Creates new form QTIConditionResultPanel */
    public QTIConditionResultPanel(javax.swing.JPanel jpQTIFeedbackPanel) {
        this.jpQTIFeedbackPanel=jpQTIFeedbackPanel;
        initComponents();
        initComponents2();
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
        initMessages();
    }
    
    public void setPuntuation(int iPuntuation){
        tfPuntuation.setText(iPuntuation+"");
    }
    
    public void setCorrect(boolean b){
        cbCorrect.setSelected(b);
    }
    
    public int getPuntuation(){
        int iPuntuation=0;
        if (tfPuntuation.getText()!=null && tfPuntuation.getText().length()>0){
            try{
                iPuntuation=Integer.parseInt(tfPuntuation.getText());
            }
            catch(Exception e){}
        }
        return iPuntuation;
    }
    
    
    public boolean getCorrect(){
        return cbCorrect.isSelected();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jpPuntuation = new javax.swing.JPanel();
        lbPuntuation = new javax.swing.JLabel();
        tfPuntuation = new javax.swing.JTextField();
        cbCorrect = new javax.swing.JCheckBox();
        lbWeight = new javax.swing.JLabel();
        cbWeight = new javax.swing.JComboBox();
        spFeedback = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());

        jpPuntuation.setLayout(new java.awt.GridBagLayout());

        lbPuntuation.setText("Puntuaci\u00f3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        jpPuntuation.add(lbPuntuation, gridBagConstraints);

        tfPuntuation.setColumns(2);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 10);
        jpPuntuation.add(tfPuntuation, gridBagConstraints);

        cbCorrect.setText("Correcte");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        jpPuntuation.add(cbCorrect, gridBagConstraints);

        lbWeight.setText("Pes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 4);
        jpPuntuation.add(lbWeight, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        jpPuntuation.add(cbWeight, gridBagConstraints);

        add(jpPuntuation, java.awt.BorderLayout.NORTH);

        add(spFeedback, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    private void initComponents2(){
        spFeedback.setViewportView(jpQTIFeedbackPanel);
        spFeedback.setBorder(null);//
    }
    
    protected void initMessages(){
        lbPuntuation.setText(Messages.getLocalizedString("Puntuation"));
        lbPuntuation.setToolTipText(Messages.getLocalizedString("PuntuationDesc"));
        tfPuntuation.setToolTipText(Messages.getLocalizedString("PuntuationDesc"));
        cbCorrect.setText(Messages.getLocalizedString("Correct"));
        cbCorrect.setToolTipText(Messages.getLocalizedString("CorrectDesc"));
        lbWeight.setText(Messages.getLocalizedString("Weight"));
    }
    
    public void setBackground(java.awt.Color c){
        super.setBackground(c);
        if (jpPuntuation!=null) jpPuntuation.setBackground(c);
        if (spFeedback!=null) spFeedback.setBackground(c);
        java.awt.Color componentColor=CreatorProperties.getComponentColor();
        if (componentColor!=null){
            if (lbPuntuation!=null) lbPuntuation.setBackground(componentColor);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cbCorrect;
    private javax.swing.JPanel jpPuntuation;
    private javax.swing.JTextField tfPuntuation;
    private javax.swing.JComboBox cbWeight;
    private javax.swing.JLabel lbPuntuation;
    private javax.swing.JLabel lbWeight;
    private javax.swing.JScrollPane spFeedback;
    // End of variables declaration//GEN-END:variables
    
}