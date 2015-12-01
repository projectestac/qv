package edu.xtec.qv.editor.ui;

import edu.xtec.qv.qti.util.CreatorProperties;

public class QTIParameterWeightedScoringAlgorithmPanel extends javax.swing.JPanel{

	public QTIParameterWeightedScoringAlgorithmPanel(){
		initComponents();
		if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
		initMessages();
	}
	
	public void setWeight(String sWeight){
		tfWeight.setText(sWeight);
	}
	
	public String getWeight(){
		return tfWeight.getText();
	}

	protected void initComponents(){
		lbWeight=new javax.swing.JLabel("Pes");
		tfWeight=new javax.swing.JTextField();
                tfWeight.setColumns(3);
		add(lbWeight);
		add(tfWeight);
	}
	
	protected void initMessages(){
		lbWeight.setText("Weight");
	}

	public void setBackground(java.awt.Color c){
   	super.setBackground(c);
   	if (lbWeight!=null) lbWeight.setBackground(c);
   	if (tfWeight!=null) tfWeight.setBackground(c);
  }

	protected javax.swing.JLabel lbWeight;
	protected javax.swing.JTextField tfWeight;

}