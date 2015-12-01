/*
 * QTIComparativeConditionPanel.java
 *
 * Created on 26 / setembre / 2003, 14:03
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.qti.QTIComparativeCondition;

/**
 *
 * @author  allastar
 */
public class QTIComparativeConditionPanel extends javax.swing.JPanel implements ExtendedRenderer{
    
    /** Creates new form QTIComparativeConditionPanel */
    public QTIComparativeConditionPanel() {
        initComponents();
    }
    
    public void setComparative(int iComparative){
        lbComparative.setText(QTIComparativeCondition.sAComparatives[iComparative]);
        if (iComparative==QTIComparativeCondition.UNANSWERED || 
                iComparative==QTIComparativeCondition.OTHER){
            tfComparativeTo.setText("");
            tfComparativeTo.setEnabled(false);
            tfComparativeTo.setVisible(false);
        }
        else{
            tfComparativeTo.setEnabled(true);
            tfComparativeTo.setVisible(true);
        }
    }
    
    public void setCompareTo(Object oCompareTo){
        tfComparativeTo.setText(oCompareTo!=null?oCompareTo.toString():"");
    }
    
    public String getCompareTo(){
        return tfComparativeTo.getText();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        lbComparative = new javax.swing.JLabel();
        tfComparativeTo = new javax.swing.JTextField();

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 5));

        lbComparative.setText(" ");
        add(lbComparative);

        tfComparativeTo.setColumns(4);
        tfComparativeTo.setText(" ");
        add(tfComparativeTo);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField tfComparativeTo;
    private javax.swing.JLabel lbComparative;
    // End of variables declaration//GEN-END:variables
    
}
