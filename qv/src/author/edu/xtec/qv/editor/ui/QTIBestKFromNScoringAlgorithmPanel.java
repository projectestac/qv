package edu.xtec.qv.editor.ui;

import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.util.Messages;

public class QTIBestKFromNScoringAlgorithmPanel extends javax.swing.JPanel{

	public QTIBestKFromNScoringAlgorithmPanel(){
		initComponents();
		if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
		initMessages();
	}

	public void setK(String sK){
		tfK.setText(sK);
	}
	
	public String getK(){
		return tfK.getText();
	}

	protected void initComponents(){
            //setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
		lbK=new javax.swing.JLabel("K");
		tfK=new javax.swing.JTextField();
                tfK.setColumns(2);
		add(lbK);
		add(tfK);
	}
	
	protected void initMessages(){
		lbK.setText(Messages.getLocalizedString("K"));
	}

	public void setBackground(java.awt.Color c){
   	super.setBackground(c);
   	if (lbK!=null) lbK.setBackground(c);
   	if (tfK!=null) tfK.setBackground(c);
  }

	protected javax.swing.JLabel lbK;
	protected javax.swing.JTextField tfK;

}