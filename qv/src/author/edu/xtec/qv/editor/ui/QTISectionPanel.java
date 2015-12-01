/*
 * QTISectionPanel.java
 *
 * Created on 14 / octubre / 2002, 08:52
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.qti.QTISection;
import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.qv.qti.util.ObjectListElement;
import edu.xtec.qv.util.Utility;
import edu.xtec.util.Messages;

/**
 *
 * @author  allastar
 */
public class QTISectionPanel extends javax.swing.JPanel implements ExtendedRenderer{
    
    protected QTISection qtiSection;
    
    /** Creates new form QTISectionPanel */
    public QTISectionPanel(QTISection qtiSection, String sIdent, String sTitle, String sType) {
        this.qtiSection=qtiSection;
        initComponents();
        initComponents2();
        tfIdent.setText(sIdent);
        tfTitle.setText(sTitle);
        if (sType!=null && sType.trim().length()>0) Utility.setOption(cbType,sType);
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
        ////initComponents2();
        initMessages();
    }
    
    /*public void setPresentationPanel(QTIMaterialPanel jpQTIMatPanel){
        jTabbedPane1.addTab("Presentació", null, jpQTIMatPanel, "");
    }*/
    public void setPresentationPanel(QTISectionPresentationPanel jpQTISectPanel){
        jTabbedPane1.addTab(Messages.getLocalizedString("Presentation"), null, jpQTISectPanel, "");
    }
    
    public void setInformationPanel(QTIInformationPanel jpInfoPanel){
        jTabbedPane1.addTab(Messages.getLocalizedString("Information"), null, jpInfoPanel, "");
    }
    
    public void setResponsesPanel(QTIContainerResponsesProcessingPanel jpResp){
        if (jpResp!=null) jTabbedPane1.remove(jpResp);
        this.jpResp=jpResp;
        jTabbedPane1.addTab(Messages.getLocalizedString("Evaluation"), null, jpResp, Messages.getLocalizedString("SectionEvaluation"));
    }
    
    public void setIdent(String sIdent){
        tfIdent.setText(sIdent);
    }
    
    public void setTitle(String sTitle){
        tfTitle.setText(sTitle);
    }
    
    public String getIdent(){
        return tfIdent.getText();
    }
    
    public String getTitle(){
        return tfTitle.getText();
    }
    
    public void setType(String sType){
        if (sType!=null && sType.trim().length()>0) Utility.setOption(cbType,sType);
    }
    
    public String getType(){
        return cbType.getSelectedItem().toString();
    }
    
    public void setTime(String sTime){
        //Admet el temps en el format de l'estàndard ISO8601 (Thhmmss o bé Thh:mm:ss)
        if (sTime==null || sTime.length()<1) return;
        if (sTime.charAt(0)=='t' || sTime.charAt(0)=='T') sTime=sTime.substring(1);
        int i=sTime.indexOf(':');
        if (i>0){ //Està en el format amb :
            java.util.StringTokenizer st=new java.util.StringTokenizer(sTime,":");
            if (st.hasMoreTokens()){
                int hours=getIntFromTime(st.nextToken());
                if (cbHours.getModel().getSize()>hours) cbHours.setSelectedIndex(hours);
            }
            if (st.hasMoreTokens()){
                int mins=getIntFromTime(st.nextToken());
                if (cbMinutes.getModel().getSize()>mins) cbMinutes.setSelectedIndex(mins);
            }
            if (st.hasMoreTokens()){
                int seconds=getIntFromTime(st.nextToken());
                if (cbSeconds.getModel().getSize()>seconds) cbSeconds.setSelectedIndex(seconds);
            }
        }
        else{
            if (sTime.length()>1){
                int hours=getIntFromTime(sTime.substring(0,1));
                if (cbHours.getModel().getSize()>hours) cbHours.setSelectedIndex(hours);
            }
            if (sTime.length()>3){
                int minutes=getIntFromTime(sTime.substring(2,3));
                if (cbMinutes.getModel().getSize()>minutes) cbMinutes.setSelectedIndex(minutes);
            }
            if (sTime.length()>3){
                int seconds=getIntFromTime(sTime.substring(4,5));
                if (cbSeconds.getModel().getSize()>seconds) cbSeconds.setSelectedIndex(seconds);
            }
        }
        cbTime.setSelected(true);
        enableTime(true);
    }
    
    private int getIntFromTime(String s){
        int time=0;
        try{
            time=Integer.parseInt(s.trim());
            if (time>=60) time=0;
        }
        catch (Exception e){
        }
        return time;
    }
    
    public String getTime(){
        //Retorna el temps en el format de l'estàndard ISO8601 (Thhmmss o bé Thh:mm:ss)
        if (cbTime.isSelected()) return "T"+getHours()+":"+getMinutes()+":"+getSeconds();
        else return null;
    }
    
    public String getHours(){
        Object o=cbHours.getSelectedItem();
        if (o!=null) return o.toString();
        else return "00";
    }
    
    public String getMinutes(){
        Object o=cbMinutes.getSelectedItem();
        if (o!=null) return o.toString();
        else return "00";
    }
    
    public String getSeconds(){
        Object o=cbSeconds.getSelectedItem();
        if (o!=null) return o.toString();
        else return "00";
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jpProperties = new javax.swing.JPanel();
        lbIdent = new javax.swing.JLabel();
        tfTitle = new javax.swing.JTextField();
        lbTitle = new javax.swing.JLabel();
        tfIdent = new javax.swing.JTextField();
        lbType = new javax.swing.JLabel();
        cbType = new javax.swing.JComboBox();
        cbTime = new javax.swing.JCheckBox();
        cbHours = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        cbMinutes = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cbSeconds = new javax.swing.JComboBox();

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
        gridBagConstraints.gridwidth = 5;
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
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jpProperties.add(tfIdent, gridBagConstraints);

        lbType.setText("Tipus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jpProperties.add(lbType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        jpProperties.add(cbType, gridBagConstraints);

        cbTime.setText("temps");
        cbTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTimeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        jpProperties.add(cbTime, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jpProperties.add(cbHours, gridBagConstraints);

        jLabel1.setText(":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jpProperties.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jpProperties.add(cbMinutes, gridBagConstraints);

        jLabel2.setText(":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jpProperties.add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jpProperties.add(cbSeconds, gridBagConstraints);

        jTabbedPane1.addTab("Propietats", null, jpProperties, "");

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    private void cbTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTimeActionPerformed
        // Add your handling code here:
        enableTime(cbTime.isSelected());
    }//GEN-LAST:event_cbTimeActionPerformed
    
    private void enableTime(boolean b){
        cbHours.setEnabled(b);
        cbMinutes.setEnabled(b);
        cbSeconds.setEnabled(b);
    }
    
    protected void initComponents2(){
        cbType.addItem(new ObjectListElement(0," "));
        cbType.addItem(new ObjectListElement(1,"send"));
        cbType.addItem(new ObjectListElement(2,"continue"));
        cbType.addItem(new ObjectListElement(3,"start"));
        cbType.addItem(new ObjectListElement(4,"end"));
        cbType.addItem(new ObjectListElement(5,"back"));
        cbType.setSelectedIndex(0);
        java.text.DecimalFormat df=new java.text.DecimalFormat();
        df.setMaximumFractionDigits(0);
        df.setMinimumIntegerDigits(2);
        for (int i=0;i<=20;i++) cbHours.addItem(df.format(i));
        cbHours.setSelectedIndex(0);
        for (int i=0;i<60;i++) cbMinutes.addItem(df.format(i));
        cbMinutes.setSelectedIndex(0);
        for (int i=0;i<60;i++) cbSeconds.addItem(df.format(i));
        cbSeconds.setSelectedIndex(0);
        enableTime(false);
        
        tfTitle.addFocusListener(new java.awt.event.FocusAdapter(){
            public void focusLost(java.awt.event.FocusEvent e){
                if (tfTitle.getText()!=null)
                    qtiSection.getManager().setNom(tfTitle.getText());
            }
        });
        tfTitle.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e){
                if (tfTitle.getText()!=null)
                    qtiSection.getManager().setNom(tfTitle.getText());
            }
        });

    }
    
    protected void initMessages(){
        lbIdent.setText(Messages.getLocalizedString("Identifier"));
        lbTitle.setText(Messages.getLocalizedString("Title"));
        lbType.setText(Messages.getLocalizedString("type"));
    }
    
    public void setBackground(java.awt.Color c){
        super.setBackground(c);
        if (jpProperties!=null) jpProperties.setBackground(c);
        //if (jTabbedPane1!=null) jTabbedPane1.setBackground(c);
        java.awt.Color componentColor=CreatorProperties.getComponentColor();
        if (componentColor!=null){
            if (lbTitle!=null) lbTitle.setBackground(componentColor);
            if (lbIdent!=null) lbIdent.setBackground(componentColor);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField tfTitle;
    private javax.swing.JCheckBox cbTime;
    private javax.swing.JComboBox cbSeconds;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JPanel jpProperties;
    private javax.swing.JComboBox cbMinutes;
    private javax.swing.JLabel lbType;
    private javax.swing.JTextField tfIdent;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JComboBox cbType;
    private javax.swing.JLabel lbIdent;
    private javax.swing.JComboBox cbHours;
    // End of variables declaration//GEN-END:variables
    protected QTIContainerResponsesProcessingPanel jpResp=null;
}