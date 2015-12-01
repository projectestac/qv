package edu.xtec.qv.editor.ui;

import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.util.Messages;

public class QTIGuessingScoringAlgorithmPanel extends javax.swing.JPanel{

	public QTIGuessingScoringAlgorithmPanel(){
		initComponents();
		if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
		initMessages();
	}

	public void setPenalty(String sPenalty){
		tfPenalty.setText(sPenalty);
	}
	
	public String getPenalty(){
		return tfPenalty.getText();
	}

	protected void initComponents(){
		lbPenalty=new javax.swing.JLabel("Penalització");
		tfPenalty=new javax.swing.JTextField();
		add(lbPenalty);
		add(tfPenalty);
	}
	
	protected void initMessages(){
		lbPenalty.setText(Messages.getLocalizedString("Penalty"));
	}

	public void setBackground(java.awt.Color c){
   	super.setBackground(c);
   	if (lbPenalty!=null) lbPenalty.setBackground(c);
   	if (tfPenalty!=null) tfPenalty.setBackground(c);
  }

	protected javax.swing.JLabel lbPenalty;
	protected javax.swing.JTextField tfPenalty;

}