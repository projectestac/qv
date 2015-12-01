/*
 * MetadataPropertiesPanel.java
 *
 * Created on 5 / novembre / 2002, 12:24
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.editor.ManageTreeObject;
import edu.xtec.qv.qti.MetadataProperties;
import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.qv.qti.util.ObjectListElement;
import edu.xtec.qv.util.Utility;
import edu.xtec.util.Messages;

/**
 *
 * @author  allastar
 */
public class MetadataPropertiesPanel extends javax.swing.JPanel {
    
    protected MetadataProperties metaProp;
    private String sMateria=null;
    
    private ManageTreeObject mto=null; //Per poder notificar els canvis de nom.
    
    /** Creates new form MetadataPropertiesPanel */
    public MetadataPropertiesPanel(MetadataProperties metaProp) {
        this.metaProp=metaProp;
        initComponents();
        initComponents2();
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
        initMessages();
    }
    
    public void setManager(ManageTreeObject mto){
        this.mto=mto;
    }
    
    public void setID(String sId){
        tfID.setText(sId);
    }
    
    public void setTitle(String sTitle){
        tfTitle.setText(sTitle);
    }
    
    public void setPossibleMateria(java.util.Vector vMateria){//Àrea
        // Els elements del vector han de ser ObjectListElement
        cbMateria.removeAllItems();
        if (vMateria==null) return;
        java.util.Enumeration e=vMateria.elements();
        while (e.hasMoreElements())
            cbMateria.addItem(e.nextElement());
    }
    
    public void setMateria(Object o){
        //l'objecte pot ser un String, però molt millor que sigui ObjectListElement amb qualsevol id (si ja hi fos es mantindria l'anterior).
        Utility.setOption(cbMateria,o);
    }
    
    public void setPossibleSubMateria(java.util.Vector vMateria){//Tema
        // Els elements del vector han de ser ObjectListElement
        cbSubMateria.removeAllItems();
        if (vMateria==null) return;
        java.util.Enumeration e=vMateria.elements();
        while (e.hasMoreElements())
            cbSubMateria.addItem(e.nextElement());
    }
    
    public void setSubMateria(Object o){
        //l'objecte pot ser un String, però molt millor que sigui ObjectListElement amb qualsevol id (si ja hi fos es mantindria l'anterior).
        Utility.setOption(cbSubMateria,o);
    }
    
    public void setPossibleLanguages(java.util.Vector vLanguage){
        // Els elements del vector han de ser ObjectListElement
        cbLanguage.removeAllItems();
        if (vLanguage==null) return;
        java.util.Enumeration e=vLanguage.elements();
        while (e.hasMoreElements()){
            Object o=e.nextElement();
            cbLanguage.addItem(o);
            if (o!=null && o.toString().trim().toLowerCase().startsWith("ca")) cbLanguage.setSelectedItem(o); //Selecciono inicialment el català
        }
    }
    
    public void setLanguage(Object o){
        //l'objecte pot ser un String, però molt millor que sigui ObjectListElement amb qualsevol id (si ja hi fos es mantindria l'anterior).
        Utility.setOption(cbLanguage,o);
    }
    
    public void setPossibleLevels(java.util.Vector vLevel){ //Nivell
        // Els elements del vector han de ser ObjectListElement
        cbLevel.removeAllItems();
        if (vLevel==null) return;
        java.util.Enumeration e=vLevel.elements();
        while (e.hasMoreElements())
            cbLevel.addItem(e.nextElement());
    }
    
    public void setLevel(Object o){
        //l'objecte pot ser un String, però molt millor que sigui ObjectListElement amb qualsevol id (si ja hi fos es mantindria l'anterior).
        Utility.setOption(cbLevel,o);
    }
    
    /*public void setPossibleSubLevels(java.util.Vector vSubLevel){ //Subnivell
        // Els elements del vector han de ser ObjectListElement
        java.util.Enumeration e=vSubLevel.elements();
        while (e.hasMoreElements())
    }*/
    
    public void setSubLevel(String sSubLevel){
        tfSubLevel.setText(sSubLevel);
    }
    
    public void setPossibleDifficulty(java.util.Vector vDificulty){ //Nivell de dificultat
        // Els elements del vector han de ser ObjectListElement
        cbDificulty.removeAllItems();
        if (vDificulty==null) return;
        java.util.Enumeration e=vDificulty.elements();
        while (e.hasMoreElements())
            cbDificulty.addItem(e.nextElement());
    }
    
    public void setDifficulty(Object o){
        //l'objecte pot ser un String, però molt millor que sigui ObjectListElement amb qualsevol id (si ja hi fos es mantindria l'anterior).
        Utility.setOption(cbDificulty,o);
    }
    
    public void setKeyWords(String sKeyWords){
        tfKeyWords.setText(sKeyWords);
    }
    
    public void setNeedVisual(boolean b){
        cbVisual.setSelected(!b);
    }
    
    public void setNeedAuditive(boolean b){
        cbAuditive.setSelected(!b);
    }
    
    public void setNeedMotrive(boolean b){
        cbMotrive.setSelected(!b);
    }
    
    public String getID(){
        return tfID.getText().trim();
    }
    
    public String getTitle(){
        return tfTitle.getText().trim();
    }
    
    public ObjectListElement getMateria(){
        ObjectListElement ole=null;
        Object o=cbMateria.getSelectedItem();
        if (o!=null){
            if (o instanceof ObjectListElement) return (ObjectListElement)o;
            else ole=new ObjectListElement(-1,o);
        }
        return ole;
    }
    
    public ObjectListElement getSubMateria(){
        ObjectListElement ole=null;
        Object o=cbSubMateria.getSelectedItem();
        if (o!=null){
            if (o instanceof ObjectListElement) return (ObjectListElement)o;
            else ole=new ObjectListElement(-1,o);
        }
        return ole;
    }
    
    public ObjectListElement getLevel(){
        ObjectListElement ole=null;
        Object o=cbLevel.getSelectedItem();
        if (o!=null){
            if (o instanceof ObjectListElement) return (ObjectListElement)o;
            else ole=new ObjectListElement(-1,o);
        }
        return ole;
    }
    
    public String getSubLevel(){
        return tfSubLevel.getText().trim();
    }
    
    public ObjectListElement getLanguage(){
        ObjectListElement ole=null;
        Object o=cbLanguage.getSelectedItem();
        if (o!=null){
            if (o instanceof ObjectListElement) return (ObjectListElement)o;
            else ole=new ObjectListElement(-1,o);
        }
        return ole;
    }
    
    public ObjectListElement getDifficulty(){
        ObjectListElement ole=null;
        Object o=cbDificulty.getSelectedItem();
        if (o!=null){
            if (o instanceof ObjectListElement) return (ObjectListElement)o;
            else ole=new ObjectListElement(-1,o);
        }
        return ole;
    }
    
    public String getKeyWords(){
        return tfKeyWords.getText().trim();
    }
    
    public boolean needVisual(){
        return !cbVisual.isSelected();
    }
    
    public boolean needAuditive(){
        return !cbAuditive.isSelected();
    }
    
    public boolean needMotrive(){
        return !cbMotrive.isSelected();
    }
    
    public void setTime(String sTime){
        //System.out.println("setTime("+sTime+") <---------------------------------------------");
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
    /* <-- Copiat de QTISectionPanel. Es podria fer un Panel de temps del que heretessim però l'editor del Forte no permetria editar-ho. */
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lbID = new javax.swing.JLabel();
        tfID = new javax.swing.JTextField();
        lbTitle = new javax.swing.JLabel();
        tfTitle = new javax.swing.JTextField();
        lbMateria = new javax.swing.JLabel();
        cbMateria = new javax.swing.JComboBox();
        lbSubMateria = new javax.swing.JLabel();
        cbSubMateria = new javax.swing.JComboBox();
        lbLevel = new javax.swing.JLabel();
        cbLevel = new javax.swing.JComboBox();
        lbSubLevel = new javax.swing.JLabel();
        tfSubLevel = new javax.swing.JTextField();
        lbLanguage = new javax.swing.JLabel();
        cbLanguage = new javax.swing.JComboBox();
        lbDificulty = new javax.swing.JLabel();
        cbDificulty = new javax.swing.JComboBox();
        lbKeyWords = new javax.swing.JLabel();
        tfKeyWords = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        cbSeconds = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cbMinutes = new javax.swing.JComboBox();
        cbHours = new javax.swing.JComboBox();
        cbTime = new javax.swing.JCheckBox();
        jpAccessibility = new javax.swing.JPanel();
        lbAccessibility = new javax.swing.JLabel();
        cbVisual = new javax.swing.JCheckBox();
        cbAuditive = new javax.swing.JCheckBox();
        cbMotrive = new javax.swing.JCheckBox();

        setLayout(new java.awt.GridBagLayout());

        lbID.setText("Identificador");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(lbID, gridBagConstraints);

        tfID.setColumns(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(tfID, gridBagConstraints);

        lbTitle.setText("T\u00edtol");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(lbTitle, gridBagConstraints);

        tfTitle.setColumns(20);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(tfTitle, gridBagConstraints);

        lbMateria.setText("\u00c0rea");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(lbMateria, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(cbMateria, gridBagConstraints);

        lbSubMateria.setText("Tema");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(lbSubMateria, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(cbSubMateria, gridBagConstraints);

        lbLevel.setText("Nivell");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(lbLevel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(cbLevel, gridBagConstraints);

        lbSubLevel.setText("Subnivell");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(lbSubLevel, gridBagConstraints);

        tfSubLevel.setColumns(15);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(tfSubLevel, gridBagConstraints);

        lbLanguage.setText("Idioma");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(lbLanguage, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(cbLanguage, gridBagConstraints);

        lbDificulty.setText("Nivell de dificultat");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(lbDificulty, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(cbDificulty, gridBagConstraints);

        lbKeyWords.setText("Paraules clau");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(lbKeyWords, gridBagConstraints);

        tfKeyWords.setColumns(30);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(tfKeyWords, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(cbSeconds, gridBagConstraints);

        jLabel2.setText(":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel1.setText(":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(cbMinutes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(cbHours, gridBagConstraints);

        cbTime.setText("temps");
        cbTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTimeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel1.add(cbTime, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(jPanel1, gridBagConstraints);

        jpAccessibility.setBorder(new javax.swing.border.TitledBorder("Accessibilitat"));
        lbAccessibility.setText("El quadern \u00e9s accessible per persones amb dificultats...");
        jpAccessibility.add(lbAccessibility);

        cbVisual.setText("visuals");
        jpAccessibility.add(cbVisual);

        cbAuditive.setText("auditives");
        jpAccessibility.add(cbAuditive);

        cbMotrive.setText("motrius");
        jpAccessibility.add(cbMotrive);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        add(jpAccessibility, gridBagConstraints);

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
        cbMateria.setEditable(true);
        cbSubMateria.setEditable(true);
        cbLanguage.setEditable(true);
        
        try{
            cbMateria.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent e){
                    if (metaProp==null) return;
                    Object oSelected=cbMateria.getSelectedItem();
                    if (oSelected!=null && oSelected instanceof ObjectListElement){
                        ObjectListElement ole=(ObjectListElement)oSelected;
                        sMateria=(ole!=null && ole.getElement()!=null)?ole.getElement().toString().trim():""; //()()
                        metaProp.setMateria(ole.getId());
                    }
                    else if (oSelected!=null){
                        if (sMateria==null || !oSelected.toString().trim().equals(sMateria)) cbSubMateria.removeAllItems();
                    }
                    else{
                        cbSubMateria.removeAllItems();
                    }
                }
            });
        }
        catch (Exception e){
        }
        
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
                if (mto!=null && tfTitle.getText()!=null)
                    mto.setNom(tfTitle.getText());
            }
        });
        tfTitle.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e){
                if (mto!=null && tfTitle.getText()!=null)
                    mto.setNom(tfTitle.getText());
            }
        });

    }
    
    protected void initMessages(){
        lbID.setText(Messages.getLocalizedString("Id"));
        lbTitle.setText(Messages.getLocalizedString("Title"));
        lbMateria.setText(Messages.getLocalizedString("Materia"));
        lbSubMateria.setText(Messages.getLocalizedString("SubMateria"));
        lbLevel.setText(Messages.getLocalizedString("Level"));
        lbSubLevel.setText(Messages.getLocalizedString("SubLevel"));
        lbLanguage.setText(Messages.getLocalizedString("Language"));
        lbDificulty.setText(Messages.getLocalizedString("Difficulty"));
        lbKeyWords.setText(Messages.getLocalizedString("KeyWords"));
        lbAccessibility.setText(Messages.getLocalizedString("AccessibleFor"));
        cbVisual.setText(Messages.getLocalizedString("Visual"));
        cbAuditive.setText(Messages.getLocalizedString("Auditive"));
        cbMotrive.setText(Messages.getLocalizedString("Motrive"));
    }
    
    public void setBackground(java.awt.Color c){
        super.setBackground(c);
        if (lbSubMateria!=null) lbSubMateria.setBackground(c);
        if (tfTitle!=null) tfTitle.setBackground(c);
        if (lbAccessibility!=null) lbAccessibility.setBackground(c);
        if (tfSubLevel!=null) tfSubLevel.setBackground(c);
        if (lbSubLevel!=null)lbSubLevel.setBackground(c);
        if (lbID!=null) lbID.setBackground(c);
        if (lbMateria!=null) lbMateria.setBackground(c);
        if (tfKeyWords!=null) tfKeyWords.setBackground(c);
        if (lbKeyWords!=null) lbKeyWords.setBackground(c);
        if (lbTitle!=null) lbTitle.setBackground(c);
        if (cbAuditive!=null) cbAuditive.setBackground(c);
        if (cbLevel!=null) cbLevel.setBackground(c);
        if (cbMotrive!=null) cbMotrive.setBackground(c);
        if (lbDificulty!=null) lbDificulty.setBackground(c);
        if (cbSubMateria!=null) cbSubMateria.setBackground(c);
        if (cbMateria!=null) cbMateria.setBackground(c);
        if (cbDificulty!=null) cbDificulty.setBackground(c);
        if (jpAccessibility!=null) jpAccessibility.setBackground(c);
        if (lbLevel!=null) lbLevel.setBackground(c);
        if (tfID!=null) tfID.setBackground(c);
        if (cbVisual!=null) cbVisual.setBackground(c);
        if (lbLanguage!=null) lbLanguage.setBackground(c);
        if (cbLanguage!=null) cbLanguage.setBackground(c);
    }
    
    public void disableAll(){
        tfTitle.setEnabled(false);
        tfSubLevel.setEnabled(false);
        tfKeyWords.setEnabled(false);
        cbAuditive.setEnabled(false);
        cbVisual.setEnabled(false);
        cbMotrive.setEnabled(false);
        cbLevel.setEnabled(false);
        cbSubMateria.setEnabled(false);
        cbMateria.setEnabled(false);
        cbDificulty.setEnabled(false);
        tfID.setEnabled(false);
        cbLanguage.setEnabled(false);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbSubMateria;
    private javax.swing.JTextField tfTitle;
    private javax.swing.JLabel lbAccessibility;
    private javax.swing.JCheckBox cbTime;
    private javax.swing.JTextField tfSubLevel;
    private javax.swing.JLabel lbSubLevel;
    private javax.swing.JLabel lbID;
    private javax.swing.JLabel lbMateria;
    private javax.swing.JComboBox cbSeconds;
    private javax.swing.JTextField tfKeyWords;
    private javax.swing.JLabel lbKeyWords;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JCheckBox cbAuditive;
    private javax.swing.JComboBox cbLevel;
    private javax.swing.JCheckBox cbMotrive;
    private javax.swing.JComboBox cbMinutes;
    private javax.swing.JLabel lbDificulty;
    private javax.swing.JComboBox cbSubMateria;
    private javax.swing.JComboBox cbMateria;
    private javax.swing.JComboBox cbDificulty;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jpAccessibility;
    private javax.swing.JLabel lbLevel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField tfID;
    private javax.swing.JCheckBox cbVisual;
    private javax.swing.JLabel lbLanguage;
    private javax.swing.JComboBox cbLanguage;
    private javax.swing.JComboBox cbHours;
    // End of variables declaration//GEN-END:variables
    
}