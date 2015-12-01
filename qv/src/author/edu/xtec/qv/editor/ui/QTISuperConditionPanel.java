/*
 * QTISuperConditionPanel.java
 *
 * Created on 2 / noviembre / 2002, 20:17
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.qti.QTICondition;
import edu.xtec.qv.qti.QTISuperCondition;
import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.qv.qti.util.ObjectListElement;
import edu.xtec.util.Messages;

/**
 *
 * @author  allastar
 */
public class QTISuperConditionPanel extends javax.swing.JPanel implements ExtendedRenderer{
    
    protected QTISuperCondition qtiCond;
    protected boolean isFirstCondition;
    protected java.util.HashMap hmIdents;
    
    /** Creates new form QTIConditionPanel */
    public QTISuperConditionPanel(QTISuperCondition qtiCond, boolean isFirstCondition) {
        this.qtiCond=qtiCond;
        this.isFirstCondition=isFirstCondition;
        hmIdents=new java.util.HashMap();
        initComponents();
        initComponents2();
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
        initMessages();
    }
    
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
            cbOption.addItem(sOption);
            cbOption.setSelectedItem(sOption);
        }
    }
    
    public void setConnective(int iConnective){
        if (iConnective==QTICondition.OR_CONNECTIVE){
            rbAnd.setSelected(false);
            rbOr.setSelected(true);
        }
        else{
            rbOr.setSelected(false);
            rbAnd.setSelected(true);
        }
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

    public void setNegation(boolean bNegation){
        cbNegation.setSelected(bNegation);
    }
    
    public void setIdents(java.util.HashMap hmIdents){
        this.hmIdents=hmIdents;
        cbOption.removeAllItems();
        java.util.Iterator it=hmIdents.keySet().iterator();
        while (it.hasNext()){
            String sResponseIdent=it.next().toString();
            cbOption.addItem(sResponseIdent);
        }
        //updatePosibleValues();
      /*java.util.Enumeration e=vIdents.elements();
      while(e.hasMoreElements()){
        cbOption.addItem(((creator.QTISelectionResponse)e.nextElement()).getIdent());
      }*/
    }
    
    /*public void setConcretePanel(javax.swing.JPanel jpConditionPanel){
        spConcreteCondition.setViewportView(jpConditionPanel);
    }*/
    
    public void setOperand(String sOperand){
        tfOperand.setText(sOperand);
    }
    
    public String getOperand(){
        return tfOperand.getText().trim();
    }
    
    public void setOperator(int iOperator){
        cbOperation.setSelectedIndex(iOperator);
    }
    
    public int getOperator(){
        int iOperator=-1;
        if (cbOperation.getSelectedItem()!=null) iOperator=((ObjectListElement)cbOperation.getSelectedItem()).getId();
        return iOperator;
    }
    
    public boolean getNegation(){
        return cbNegation.isSelected();
    }
    
    public String getOption(){
        return (cbOption.getSelectedItem()!=null?cbOption.getSelectedItem().toString():"");
    }
    
    public int getConnective(){
        return isFirstCondition?-1:rbAnd.isSelected()?QTICondition.AND_CONNECTIVE:QTICondition.OR_CONNECTIVE;
    }
    
    private void initComponents2(){
        ObjectListElement jl1=new ObjectListElement(QTISuperCondition.EQUAL_CONDITION,"=");
        ObjectListElement jl2=new ObjectListElement(QTISuperCondition.NOT_EQUAL_CONDITION,"!=");
        ObjectListElement jl3=new ObjectListElement(QTISuperCondition.LT,"<");
        ObjectListElement jl4=new ObjectListElement(QTISuperCondition.LTE,"<=");
        ObjectListElement jl5=new ObjectListElement(QTISuperCondition.GT,">");
        ObjectListElement jl6=new ObjectListElement(QTISuperCondition.GTE,">=");
        cbOperation.addItem(jl1);
        cbOperation.addItem(jl2);
        cbOperation.addItem(jl3);
        cbOperation.addItem(jl4);
        cbOperation.addItem(jl5);
        cbOperation.addItem(jl6);
        /*cbOperation.addItemListener(new java.awt.event.ItemListener(){
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                if (cbOperation.getSelectedItem()!=null){
                    int iConditionType=((ObjectListElement)cbOperation.getSelectedItem()).getId();
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
        updatePosibleValues();        */
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
    
    /*private void updatePosibleValues(){
        Object oSelected=cbOption.getSelectedItem();
      //System.out.println("update Posible values. Selected:"+oSelected);
                if (oSelected!=null && hmIdents.containsKey(oSelected) && hmIdents.get(oSelected)!=null){
                        //System.out.println("setPosibleValues. Size:"+((java.util.Vector)hmIdents.get(oSelected)).size());
        qtiCond.setPosibleValues((java.util.Vector)hmIdents.get(oSelected));
      }
    }*/
    
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
        rbOr = new javax.swing.JRadioButton();
        rbAnd = new javax.swing.JRadioButton();
        lbOperand = new javax.swing.JLabel();
        tfOperand = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        jpCondition.setLayout(new java.awt.GridBagLayout());

        cbNegation.setText("negaci\u00f3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 10);
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
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 10);
        jpCondition.add(cbOperation, gridBagConstraints);

        lbConnective.setText("connectiva");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jpCondition.add(lbConnective, gridBagConstraints);

        rbOr.setText("O");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jpCondition.add(rbOr, gridBagConstraints);

        rbAnd.setText("Y");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jpCondition.add(rbAnd, gridBagConstraints);

        lbOperand.setText("Operand");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 2);
        jpCondition.add(lbOperand, gridBagConstraints);

        tfOperand.setColumns(3);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jpCondition.add(tfOperand, gridBagConstraints);

        add(jpCondition, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    protected void initMessages(){
        cbNegation.setText(Messages.getLocalizedString("negation"));
        lbOption.setText(Messages.getLocalizedString("ID"));
        lbConnective.setText(Messages.getLocalizedString("connective"));
        //lbOperation.setText(Messages.getLocalizedString("operation"));
        rbOr.setText(Messages.getLocalizedString("or"));
        rbAnd.setText(Messages.getLocalizedString("and"));
        lbOperand.setText(Messages.getLocalizedString("Operand"));
    }
    
    public void setBackground(java.awt.Color c){
        super.setBackground(c);
        if (jpCondition!=null) jpCondition.setBackground(c);
        //if (jPanel1!=null) jPanel1.setBackground(c);
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
    
    public void addOperandKeyListener(java.awt.event.KeyListener kl){
        tfOperand.addKeyListener(kl);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jpCondition;
    private javax.swing.JComboBox cbOption;
    private javax.swing.JRadioButton rbAnd;
    private javax.swing.JTextField tfOperand;
    private javax.swing.JLabel lbConnective;
    private javax.swing.JCheckBox cbNegation;
    private javax.swing.JLabel lbOption;
    private javax.swing.JRadioButton rbOr;
    private javax.swing.JComboBox cbOperation;
    private javax.swing.JLabel lbOperand;
    // End of variables declaration//GEN-END:variables
    
}