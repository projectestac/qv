/*
 * QTIItemPanel.java
 *
 * Created on 6 de octubre de 2002, 16:49
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.qti.QTIItem;
import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.qv.qti.util.ObjectListElement;
import edu.xtec.util.Messages;

/**
 *
 * @author  Albert
 * @version
 */
public class QTIItemPanel extends javax.swing.JPanel implements ExtendedRenderer{
    
    private QTIItem qtiItem;
    
    /** Creates new form QTIItemPanel */
    public QTIItemPanel(QTIItem qtiItem, String sIdent, String sTitle) {
        this(qtiItem,sIdent,sTitle,0);
    }
    
    public QTIItemPanel(QTIItem qtiItem, String sIdent, String sTitle, int style) {
        //System.out.println("%%%%%%% Creant QTIItemPanel");
        this.qtiItem=qtiItem;
        initComponents();
        initComponents2();
        tfIdent.setText(sIdent);
        tfTitle.setText(sTitle);
        setStyle(style);
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
        initMessages();
    }
    
    public void setIdent(String sIdent){
        tfIdent.setText(sIdent);
    }
    
    public void setTitle(String sTitle){
        tfTitle.setText(sTitle);
    }
    
    public void setStyle(int i){
        //System.out.println("%%%%%%% panel-->setStyle:"+i);
        cbStyle.setSelectedIndex(i);
    }
    
    public void setPresentationPanel(QTIMaterialPanel jpQTIMatPanel){//
        jTabbedPane1.addTab(Messages.getLocalizedString("Presentation"), null, jpQTIMatPanel, "");
    }
    
    public void setResponsesPanel(QTIItemResponsesPanel jpQTIItemResponsesPanel){//
        jTabbedPane1.addTab(Messages.getLocalizedString("Responses"), null, jpQTIItemResponsesPanel, "");
    }
    
    public void setResponsesProcessingPanel(QTIItemResponsesProcessingPanel jpQTIItemResponsesProcessingPanel){
        jTabbedPane1.addTab(Messages.getLocalizedString("Evaluation"), null, jpQTIItemResponsesProcessingPanel, Messages.getLocalizedString("ExerciseEvaluation"));
        //jTabbedPane1.setComponentAt(2,jpQTIItemResponsesProcessingPanel);
        //add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }
    
    public Object[] getItemProperties(){
        return new Object[]{tfIdent.getText(),tfTitle.getText(),new Integer(cbStyle.getSelectedIndex())};
    }
    
    public String getIdent(){
        return tfIdent.getText();
    }
    
    public String getTitle(){
        return tfTitle.getText();
    }
    
    public int getStyle(){
        return cbStyle.getSelectedIndex();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jpProperties = new javax.swing.JPanel();
        lbIdent = new javax.swing.JLabel();
        tfTitle = new javax.swing.JTextField();
        lbTitle = new javax.swing.JLabel();
        tfIdent = new javax.swing.JTextField();
        lbStyle = new javax.swing.JLabel();
        cbStyle = new javax.swing.JComboBox();

        setLayout(new java.awt.BorderLayout());

        jpProperties.setLayout(new java.awt.GridBagLayout());

        lbIdent.setText("Identificador");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jpProperties.add(lbIdent, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jpProperties.add(tfTitle, gridBagConstraints);

        lbTitle.setText("T\u00edtol");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jpProperties.add(lbTitle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jpProperties.add(tfIdent, gridBagConstraints);

        lbStyle.setText("Estil");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jpProperties.add(lbStyle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jpProperties.add(cbStyle, gridBagConstraints);

        jTabbedPane1.addTab("Propietats", null, jpProperties, "");

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    protected void initComponents2(){
        cbStyle.addItem(new ObjectListElement(0," "));
        cbStyle.addItem(new ObjectListElement(1,"Opcions dreta"));
        cbStyle.addItem(new ObjectListElement(2,"Directe"));
        cbStyle.addItem(new ObjectListElement(3,"Continuat"));
        cbStyle.setSelectedIndex(0);
        jTabbedPane1.setTitleAt(0,Messages.getLocalizedString("Properties"));
        jTabbedPane1.setToolTipTextAt(0,Messages.getLocalizedString("ExerciseProperties"));
        
        tfTitle.addFocusListener(new java.awt.event.FocusAdapter(){
            public void focusLost(java.awt.event.FocusEvent e){
                if (tfTitle.getText()!=null)
                    qtiItem.getManager().setNom(tfTitle.getText());
            }
        });
        tfTitle.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e){
                if (tfTitle.getText()!=null)
                    qtiItem.getManager().setNom(tfTitle.getText());
            }
        });
        
    }
    
    protected void initMessages(){
        lbIdent.setText(Messages.getLocalizedString("Id"));
        lbTitle.setText(Messages.getLocalizedString("Title"));
        lbStyle.setText(Messages.getLocalizedString("Style"));
    }
    
    public void setBackground(java.awt.Color c){
        super.setBackground(c);
        java.awt.Color componentColor=CreatorProperties.getComponentColor();
        if (componentColor!=null){
            if (jpProperties!=null) jpProperties.setBackground(c);
            //if (jTabbedPane1!=null) jTabbedPane1.setBackground(c);
            if (lbTitle!=null) lbTitle.setBackground(componentColor);
            if (lbIdent!=null) lbIdent.setBackground(componentColor);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField tfTitle;
    private javax.swing.JComboBox cbStyle;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JPanel jpProperties;
    private javax.swing.JTextField tfIdent;
    private javax.swing.JLabel lbStyle;
    private javax.swing.JLabel lbIdent;
    // End of variables declaration//GEN-END:variables
    
}