/*
 * ClicPanel.java
 *
 * Created on 25 / octubre / 2002, 10:35
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.qti.ExtensionClicMaterial;
import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.qv.util.QuadernConfig;
import edu.xtec.qv.util.Utility;
import edu.xtec.util.Messages;
/**
 *
 * @author  allastar
 */
public class ClicPanel extends javax.swing.JPanel {
    
    public static int defaultWidth=400;
    public static int defaultHeight=250;
    
    protected ExtensionClicMaterial clicMat;
    
    /** Creates new form ClicPanel */
    public ClicPanel(ExtensionClicMaterial clicMat) {
        this.clicMat=clicMat;
        initComponents();
        initComponents2();
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
        initMessages();
    }
    
    public String getProject(){
        return tfProject.getText();
    }
    
    public String getLanguage(){
        return (cbLanguage!=null && cbLanguage.getSelectedItem()!=null)?cbLanguage.getSelectedItem().toString():"";
    }

    public String getSkin(){
        return (cbSkin!=null && cbSkin.getSelectedItem()!=null)?cbSkin.getSelectedItem().toString():"";
    }
    
    public boolean getSounds(){
        return cbSounds.isSelected();
    }
    
    public boolean getCompressImages(){
        return cbCompressImages.isSelected();
    }
    
    public int getClicWidth(){
        int width=0;
        try{
  	    width=Integer.parseInt(tfWidth.getText());
        }
        catch (Exception ex){
            width=defaultWidth;
            tfWidth.setText(width+"");
        }
        return width;
    }

    public int getClicHeight(){
        int height=0;
        try{
  	    height=Integer.parseInt(tfHeight.getText());
        }
        catch (Exception ex){
            height=defaultHeight;
            tfHeight.setText(height+"");
        }
        return height;
    }
    /*
    public boolean isReporterUsing(){
        if (cbReporter.getSelectedItem()!=null && cbReporter.getSelectedItem().toString().trim().length()>0 &&
            tfIP.getText().trim().length()>0 && tfPort.getText().trim().length()>0) return true;
        else return false;
    }
     
    public String getReporterType(){
        return (cbReporter.getSelectedItem()!=null)?cbReporter.getSelectedItem().toString().trim():"";
    }
    
    public String getIP(){
        return tfIP.getText();
    }
    
    public String getPort(){
        return tfPort.getText();
    }
    */
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lbProject = new javax.swing.JLabel();
        tfProject = new javax.swing.JTextField();
        lbLanguage = new javax.swing.JLabel();
        cbLanguage = new javax.swing.JComboBox();
        lbSkin = new javax.swing.JLabel();
        cbSkin = new javax.swing.JComboBox();
        cbSounds = new javax.swing.JCheckBox();
        cbCompressImages = new javax.swing.JCheckBox();
        lbWidth = new javax.swing.JLabel();
        tfWidth = new javax.swing.JTextField();
        lbHeight = new javax.swing.JLabel();
        tfHeight = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        lbProject.setText("Projecte");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 6);
        add(lbProject, gridBagConstraints);

        tfProject.setColumns(30);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 6);
        add(tfProject, gridBagConstraints);

        lbLanguage.setText("Idioma");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 6);
        add(lbLanguage, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 6);
        add(cbLanguage, gridBagConstraints);

        lbSkin.setText("Skin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 6);
        add(lbSkin, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 6);
        add(cbSkin, gridBagConstraints);

        cbSounds.setText("sons de sistema");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 6);
        add(cbSounds, gridBagConstraints);

        cbCompressImages.setText("comprimir imatges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 6);
        add(cbCompressImages, gridBagConstraints);

        lbWidth.setText("Amplada");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 6);
        add(lbWidth, gridBagConstraints);

        tfWidth.setColumns(4);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 6);
        add(tfWidth, gridBagConstraints);

        lbHeight.setText("Al\u00e7ada");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 6);
        add(lbHeight, gridBagConstraints);

        tfHeight.setColumns(4);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 6);
        add(tfHeight, gridBagConstraints);

    }//GEN-END:initComponents
    
    protected void initComponents2(){
        cbSkin.setEditable(true);
        //cbReporter.setEditable(true);
        cbLanguage.setEditable(true);
        java.util.Properties clicProperties=QuadernConfig.getClicProperties();
        if (clicProperties!=null){
            String sSkins=clicProperties.getProperty("skins","");
            String sLanguages=clicProperties.getProperty("languages","");
            java.util.StringTokenizer st=new java.util.StringTokenizer(sSkins,";");
            while (st.hasMoreTokens()){
                String sSkin=st.nextToken();
                if (sSkin.length()>0) cbSkin.addItem(sSkin);
            }
            /*
             String sReporters=clicProperties.getProperty("reporter","");
            st=new java.util.StringTokenizer(sReporters,";");
            while (st.hasMoreTokens()){
                String sReporter=st.nextToken();
                if (sReporter.length()>0) cbReporter.addItem(sReporter);
            }*/
            st=new java.util.StringTokenizer(sLanguages,";");
            while (st.hasMoreTokens()){
                String sLanguage=st.nextToken();
                if (sLanguage.length()>0) cbLanguage.addItem(sLanguage);
            }
        }
    }
    
    public void initComponents3(String sProject, String sLanguage, String sSkin, boolean bSounds,
        boolean bCompressImages, String sWidth, String sHeight/*, String[] reporterInfo*/){
            
        tfProject.setText(sProject);
        if (sLanguage!=null && sLanguage.trim().length()>0) Utility.setOption(cbLanguage,sLanguage.trim());
        if (sSkin!=null && sSkin.trim().length()>0) Utility.setOption(cbSkin,sSkin.trim());
        cbSounds.setSelected(bSounds);
        cbCompressImages.setSelected(bCompressImages);
        tfWidth.setText(sWidth);
        tfHeight.setText(sHeight);
        /*if (reporterInfo!=null && reporterInfo.length==3){
            String sReporter=reporterInfo[0];
            if (sReporter!=null && sReporter.trim().length()>0) util.Utility.setOption(cbReporter,sReporter.trim());
            tfIP.setText(reporterInfo[1]);
            tfPort.setText(reporterInfo[2]);
        }*/
    }
    
    protected void initMessages(){
	    lbProject.setText(Messages.getLocalizedString("Project"));
			lbLanguage.setText(Messages.getLocalizedString("Language"));
			lbSkin.setText(Messages.getLocalizedString("Skin"));
			cbSounds.setText(Messages.getLocalizedString("SystemSounds"));
			cbCompressImages.setText(Messages.getLocalizedString("CompImages"));
			lbWidth.setText(Messages.getLocalizedString("Width"));
			lbHeight.setText(Messages.getLocalizedString("Height"));
			//lbReporter.setText(Messages.getLocalizedString("Reporter"));
			//lbIP.setText(Messages.getLocalizedString("IP"));
			//lbPort.setText(Messages.getLocalizedString("Port"));
    }
    
    public void setBackground(java.awt.Color c){
			super.setBackground(c);
	    //if (lbIP!=null) lbIP.setBackground(c);
  	  if (lbHeight!=null) lbHeight.setBackground(c);
    	if (cbCompressImages!=null) cbCompressImages.setBackground(c);
			if (lbProject!=null) lbProject.setBackground(c);
  	  if (cbSounds!=null) cbSounds.setBackground(c);
    	if (tfWidth!=null) tfWidth.setBackground(c);
    	//if (tfIP!=null) tfIP.setBackground(c);
    	//if (lbPort!=null) lbPort.setBackground(c);
    	if (lbWidth!=null) lbWidth.setBackground(c);
    	if (tfHeight!=null) tfHeight.setBackground(c);
    	if (tfProject!=null) tfProject.setBackground(c);
    	//if (jpReporter!=null) jpReporter.setBackground(c);
    	if (lbSkin!=null) lbSkin.setBackground(c);
    	//if (tfPort!=null) tfPort.setBackground(c);
    	//if (lbReporter!=null) lbReporter.setBackground(c);
    	//if (cbReporter!=null) cbReporter.setBackground(c);
    	if (cbSkin!=null) cbSkin.setBackground(c);
    	if (lbLanguage!=null) lbLanguage.setBackground(c);
    	if (cbLanguage!=null) cbLanguage.setBackground(c);
		}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbHeight;
    private javax.swing.JCheckBox cbCompressImages;
    private javax.swing.JLabel lbProject;
    private javax.swing.JCheckBox cbSounds;
    private javax.swing.JTextField tfWidth;
    private javax.swing.JLabel lbWidth;
    private javax.swing.JTextField tfHeight;
    private javax.swing.JTextField tfProject;
    private javax.swing.JLabel lbSkin;
    private javax.swing.JComboBox cbSkin;
    private javax.swing.JLabel lbLanguage;
    private javax.swing.JComboBox cbLanguage;
    // End of variables declaration//GEN-END:variables
    
}