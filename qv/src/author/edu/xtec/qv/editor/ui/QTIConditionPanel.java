/*
 * QTIConditionPanel.java
 *
 * Created on 9 / octubre / 2002, 10:18
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.qti.QTICondition;
import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.qv.qti.util.ObjectListElement;
import edu.xtec.util.Messages;

/**
 *
 * @author  allastar
 */
public class QTIConditionPanel extends javax.swing.JPanel implements ExtendedRenderer{
    
    protected QTICondition qtiCond;
    protected boolean isFirstCondition;
    protected java.util.HashMap hmIdents;
        
    //public static int lastComparative=0;//
    
    /** Creates new form QTIConditionPanel */
    public QTIConditionPanel(QTICondition qtiCond, boolean isFirstCondition) {
        //System.out.println("Creo QTIConditionPanel");
        this.qtiCond=qtiCond;
        this.isFirstCondition=isFirstCondition;
        hmIdents=new java.util.HashMap();
        initComponents();
        initComponents2();
        //setOperation(lastComparative);
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
        initMessages();
    }
    
    //private String ss="";
    
    public void setOption(String sOption){
        //cbOption.setSelectedItem(sOption); //provar això perquè no se si ha de ser el mateix objecte exactament o ho falseamb equals...
        boolean bFound=false;
        for (int i=0;i<cbOption.getItemCount() && !bFound;i++){
            String sItemName=cbOption.getItemAt(i).toString();
            if (sItemName.equals(sOption)){
                bFound=true;
                cbOption.setSelectedIndex(i);
            }
        }
        if (!bFound){
            //System.out.println("Cal seleccionar "+sOption);
            //ss=sOption;
            cbOption.addItem(sOption);
            cbOption.setSelectedItem(sOption);
        }
    }
    
    public void setOperation(int iOperation){
        try{
            cbOption.setSelectedIndex(iOperation);
        }
        catch (Exception e){}
    }
    
    public void setFirstCondition(boolean b){
        isFirstCondition=b;
        if (isFirstCondition) {
            rbAnd.setEnabled(false);
            rbOr.setEnabled(false);
        }
        else{
            rbAnd.setEnabled(true);
            rbOr.setEnabled(true);
        }
    }
    
    public void setConnective(int iConnective){
        if (iConnective==QTICondition.OR_CONNECTIVE){
            //System.out.println("setConnective:or opt:"+ss);
            rbAnd.setSelected(false);
            rbOr.setSelected(true);
        }
        else{
            //System.out.println("setConnective:and opt:"+ss);
            rbOr.setSelected(false);
            rbAnd.setSelected(true);
        }
    }
    
    public void setNegation(boolean bNegation){
        cbNegation.setSelected(bNegation);
    }
    
    public void setIdents(java.util.HashMap hmIdents){
        this.hmIdents=hmIdents;
////        cbOption.removeAllItems();
        java.util.Iterator it=hmIdents.keySet().iterator();
        while (it.hasNext()){
            String sResponseIdent=it.next().toString();
            ////cbOption.addItem(sResponseIdent);
            boolean bFound=false;
            for (int i=0;i<cbOption.getModel().getSize() && !bFound;i++)
                bFound=cbOption.getModel().getElementAt(i).toString().equals(sResponseIdent);
            if (!bFound) cbOption.addItem(sResponseIdent);
        }
        updatePosibleValues();
      /*java.util.Enumeration e=vIdents.elements();
      while(e.hasMoreElements()){
        cbOption.addItem(((creator.QTISelectionResponse)e.nextElement()).getIdent());
      }*/
    }
    
    public void setConcretePanel(javax.swing.JPanel jpConditionPanel){
        spConcreteCondition.setViewportView(jpConditionPanel);
    }
    
    public boolean getNegation(){
        return cbNegation.isSelected();
    }
    
    public String getOption(){
        //System.out.println("getOption!!!!!!!! creat amb:"+ss+" valor actual:"+(cbOption.getSelectedItem()!=null?cbOption.getSelectedItem().toString():""));
        return (cbOption.getSelectedItem()!=null?cbOption.getSelectedItem().toString():"");
    }
    
    public int getConnective(){
        return isFirstCondition?-1:rbAnd.isSelected()?QTICondition.AND_CONNECTIVE:QTICondition.OR_CONNECTIVE;
    }
    
    private void initComponents2(){
        /*ObjectListElement jl1=new ObjectListElement(creator.QTICondition.EQUAL_CONDITION,"igual");
        ObjectListElement jl2=new ObjectListElement(creator.QTICondition.INSIDE_CONDITION,"dintre");
        ObjectListElement jl3=new ObjectListElement(creator.QTICondition.SUBSET_CONDITION,"conté element");
        cbOperation.addItem(jl1);
        cbOperation.addItem(jl2);
        cbOperation.addItem(jl3);*/
        
        cbOperation.addItem(new ObjectListElement(QTICondition.EQUAL_CONDITION,"igual a"));
        cbOperation.addItem(new ObjectListElement(QTICondition.LT_CONDITION,"menor que"));
        cbOperation.addItem(new ObjectListElement(QTICondition.LTE_CONDITION,"menor o igual que"));
        cbOperation.addItem(new ObjectListElement(QTICondition.GT_CONDITION,"mes gran que"));
        cbOperation.addItem(new ObjectListElement(QTICondition.GTE_CONDITION,"mes gran o igual que"));
        cbOperation.addItem(new ObjectListElement(QTICondition.INSIDE_CONDITION,"dins"));
        cbOperation.addItem(new ObjectListElement(QTICondition.SUBSET_CONDITION,"conté element"));
        cbOperation.addItem(new ObjectListElement(QTICondition.UNANSWERED_CONDITION,"sense respondre"));
        cbOperation.addItem(new ObjectListElement(QTICondition.OTHER_CONDITION,"per defecte"));
        
        cbOperation.addItemListener(new java.awt.event.ItemListener(){
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                if (cbOperation.getSelectedItem()!=null){
                    int iConditionType=((ObjectListElement)cbOperation.getSelectedItem()).getId();
                    //lastComparative=iConditionType;
                    qtiCond.setConditionType(iConditionType,null);
                    updatePosibleValues();
                }
            }
        });
        cbOption.addItemListener(new java.awt.event.ItemListener(){
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                updatePosibleValues();
            }
        });
        updatePosibleValues();
        javax.swing.ButtonGroup bg=new javax.swing.ButtonGroup();
        bg.add(rbAnd);
        bg.add(rbOr);
        if (isFirstCondition) {
            rbAnd.setEnabled(false);
            rbOr.setEnabled(false);
        }
        else{
            rbAnd.setSelected(true);
        }
    }
    
    private void updatePosibleValues(){
        Object oSelected=cbOption.getSelectedItem();
        //System.out.println("update Posible values. Selected:"+oSelected);
        if (oSelected!=null && hmIdents.containsKey(oSelected) && hmIdents.get(oSelected)!=null){
            //System.out.println("setPosibleValues. Size:"+((java.util.Vector)hmIdents.get(oSelected)).size());
            qtiCond.setPosibleValues((java.util.Vector)hmIdents.get(oSelected));
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jpCondition = new javax.swing.JPanel();
        cbNegation = new javax.swing.JCheckBox();
        lbOption = new javax.swing.JLabel();
        cbOption = new javax.swing.JComboBox();
        cbOperation = new javax.swing.JComboBox();
        lbConnective = new javax.swing.JLabel();
        spConcreteCondition = new javax.swing.JScrollPane();
        rbOr = new javax.swing.JRadioButton();
        rbAnd = new javax.swing.JRadioButton();

        setLayout(new java.awt.BorderLayout());

        setPreferredSize(new java.awt.Dimension(400, 140));
        jpCondition.setLayout(new java.awt.GridBagLayout());

        cbNegation.setText("negaci\u00f3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jpCondition.add(cbNegation, gridBagConstraints);

        lbOption.setText("id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jpCondition.add(lbOption, gridBagConstraints);

        cbOption.setToolTipText("identificador");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jpCondition.add(cbOption, gridBagConstraints);

        cbOperation.setToolTipText("operaci\u00f3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weighty = 0.1;
        jpCondition.add(cbOperation, gridBagConstraints);

        lbConnective.setText("connectiva");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jpCondition.add(lbConnective, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jpCondition.add(spConcreteCondition, gridBagConstraints);

        rbOr.setText("O");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jpCondition.add(rbOr, gridBagConstraints);

        rbAnd.setText("I");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jpCondition.add(rbAnd, gridBagConstraints);

        add(jpCondition, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    protected void initMessages(){
        cbNegation.setText(Messages.getLocalizedString("negation"));
        lbOption.setText(Messages.getLocalizedString("ID"));
        lbConnective.setText(Messages.getLocalizedString("connective"));
        ////lbOperation.setText(Messages.getLocalizedString("ID"));
        rbOr.setText(Messages.getLocalizedString("or"));
        rbAnd.setText(Messages.getLocalizedString("and"));
    }
    
    public void setBackground(java.awt.Color c){
        super.setBackground(c);
        if (jpCondition!=null) jpCondition.setBackground(c);
        if (spConcreteCondition!=null) spConcreteCondition.setBackground(c);
        java.awt.Color componentColor=CreatorProperties.getComponentColor();
        if (componentColor!=null){
            if (cbOption!=null) cbOption.setBackground(componentColor);
            if (rbAnd!=null) rbAnd.setBackground(componentColor);
            if (lbConnective!=null) lbConnective.setBackground(componentColor);
            if (cbNegation!=null) cbNegation.setBackground(componentColor);
            if (rbOr!=null) rbOr.setBackground(componentColor);
            if (cbOperation!=null) cbOperation.setBackground(componentColor);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jpCondition;
    private javax.swing.JComboBox cbOption;
    private javax.swing.JRadioButton rbAnd;
    private javax.swing.JLabel lbConnective;
    private javax.swing.JCheckBox cbNegation;
    private javax.swing.JLabel lbOption;
    private javax.swing.JRadioButton rbOr;
    private javax.swing.JComboBox cbOperation;
    private javax.swing.JScrollPane spConcreteCondition;
    // End of variables declaration//GEN-END:variables
    
}