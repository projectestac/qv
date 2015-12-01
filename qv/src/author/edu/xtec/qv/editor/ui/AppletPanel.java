/*
 * AppletPanel.java
 *
 * Created on 23 / octubre / 2002, 12:41
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.qti.QTIAppletMaterial;
import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.util.Messages;

/**
 *
 * @author  allastar
 */
public class AppletPanel extends javax.swing.JPanel {
    
    public static int defaultWidth=400;
    public static int defaultHeight=250;
    
    protected QTIAppletMaterial qtiApMat;
    protected AppletSubPanel asp;
    protected boolean includeView=false;
    
    /** Creates new form AppletPanel */
    public AppletPanel(QTIAppletMaterial qtiApMat) {
    	this(qtiApMat,"","","","","");
    }


    public AppletPanel(QTIAppletMaterial qtiApMat, String sClassPath, String sClassName, String sParams, String sWidth, String sHeight) {
        this.qtiApMat=qtiApMat;
        initComponents();
        initComponents2();
        tfClassName.setText(sClassName);
        tfClassPath.setText(sClassPath);
        tfParams.setText(sParams);
        tfWidth.setText(sWidth);
        tfHeight.setText(sHeight);
        includeView=CreatorProperties.getIncludeMediaOnView();
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
        initMessages();
        //includeView=true;
    }
    
    public String getAppletName(){
    	return (tfClassName.getText()!=null)?tfClassName.getText().trim():"";
    }
    
    public String getClassPath(){
    	return (tfClassPath.getText()!=null)?tfClassPath.getText().trim():"";
    }

    public String getParams(){
    	return (tfParams.getText()!=null)?tfParams.getText().trim():"";
    }
    
    public int getAppletWidth(){
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
    
    public int getAppletHeight(){
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
    
    protected void open(String sClassPath, String sMainClass, String sParams, int width, int height){
        spApplet.removeAll();
        if (includeView){
            asp=new AppletSubPanel(this,sClassPath,sMainClass,sParams,width,height);
            spApplet.setViewportView(asp);
            //jpCenter.add(msp,BorderLayout.CENTER);
        }
        else{
            new AppletFrame(this,sClassPath,sMainClass,sParams,width,height);
        }
    }
    
    public class AppletFrame extends javax.swing.JFrame{
            public AppletFrame(AppletPanel ap,String sClassPath, String sMainClass, String sParams, int width, int height){
                final AppletSubPanel jp=new AppletSubPanel(ap,sClassPath,sMainClass,sParams,width,height);
                getContentPane().add(jp,java.awt.BorderLayout.CENTER);
                //spApplet.setViewportView(jp);
                setSize(new java.awt.Dimension(width,height));
                setVisible(true);
                addWindowListener(new java.awt.event.WindowAdapter(){
                    public void windowClosing(java.awt.event.WindowEvent e){
                        setVisible(false);
                        dispose();
                    }
                });
                show();
            }
        }
            

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        spApplet = new javax.swing.JScrollPane();
        jpControl = new javax.swing.JPanel();
        lbWidth = new javax.swing.JLabel();
        tfWidth = new javax.swing.JTextField();
        lbHeight = new javax.swing.JLabel();
        tfHeight = new javax.swing.JTextField();
        jpNorth = new javax.swing.JPanel();
        lbClassPath = new javax.swing.JLabel();
        tfClassPath = new javax.swing.JTextField();
        lbClassName = new javax.swing.JLabel();
        tfClassName = new javax.swing.JTextField();
        lbParams = new javax.swing.JLabel();
        tfParams = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        add(spApplet, java.awt.BorderLayout.CENTER);

        jpControl.setLayout(new java.awt.GridBagLayout());

        lbWidth.setText("Amplada");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 8, 6);
        jpControl.add(lbWidth, gridBagConstraints);

        tfWidth.setColumns(4);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 8, 4);
        jpControl.add(tfWidth, gridBagConstraints);

        lbHeight.setText("Al\u00e7ada");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 8, 6);
        jpControl.add(lbHeight, gridBagConstraints);

        tfHeight.setColumns(4);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 8, 4);
        jpControl.add(tfHeight, gridBagConstraints);

        add(jpControl, java.awt.BorderLayout.EAST);

        jpNorth.setLayout(new java.awt.GridBagLayout());

        lbClassPath.setText("Class path");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        jpNorth.add(lbClassPath, gridBagConstraints);

        tfClassPath.setColumns(30);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        jpNorth.add(tfClassPath, gridBagConstraints);

        lbClassName.setText("Nom de la classe");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        jpNorth.add(lbClassName, gridBagConstraints);

        tfClassName.setColumns(15);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        jpNorth.add(tfClassName, gridBagConstraints);

        lbParams.setText("Par\u00e0metres");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        jpNorth.add(lbParams, gridBagConstraints);

        tfParams.setColumns(30);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        jpNorth.add(tfParams, gridBagConstraints);

        add(jpNorth, java.awt.BorderLayout.NORTH);

    }//GEN-END:initComponents
    
    protected void initComponents2(){
        java.awt.event.ActionListener al=new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e){
                if ((tfClassName.getText()!=null && tfClassName.getText().trim().length()>0)&&
                (tfClassPath.getText()!=null && tfClassPath.getText().trim().length()>0)){
                    int width=0;
                    int height=0;
                    try{
                        width=Integer.parseInt(tfWidth.getText());
                    }
                    catch (Exception ex){
                        width=defaultWidth;
                        tfWidth.setText(width+"");
                    }
                    try{
                        height=Integer.parseInt(tfHeight.getText());
                    }
                    catch (Exception ex){
                        height=defaultHeight;
                        tfHeight.setText(height+"");
                    }
                    AppletPanel.this.invalidate();
                    if (spApplet!=null) remove(spApplet);
                    //spApplet.invalidate();
                    open(tfClassPath.getText(),tfClassName.getText(),tfParams.getText(),width,height);
                    spApplet=new javax.swing.JScrollPane(asp);
                    add(spApplet,java.awt.BorderLayout.CENTER);
                    AppletPanel.this.validate();
                    AppletPanel.this.doLayout();////??
                    //spApplet.validate();
                }
            }
        };
        tfClassPath.addActionListener(al);
        tfClassName.addActionListener(al);
        tfParams.addActionListener(al);
        tfWidth.addActionListener(al);
        tfHeight.addActionListener(al);
    }
    
    protected void initMessages(){
	    lbWidth.setText(Messages.getLocalizedString("Width"));
			lbHeight.setText(Messages.getLocalizedString("Height"));
			lbClassPath.setText(Messages.getLocalizedString("CP"));
			lbClassName.setText(Messages.getLocalizedString("ClassName"));
			lbParams.setText(Messages.getLocalizedString("Parameters"));
    }
    
    public void setBackground(java.awt.Color c){
			super.setBackground(c);
			if (tfParams!=null) tfParams.setBackground(c);
			if (lbHeight!=null) lbHeight.setBackground(c);
			if (jpNorth!=null) jpNorth.setBackground(c);
    	if (lbParams!=null) lbParams.setBackground(c);
    	if (tfWidth!=null) tfWidth.setBackground(c);
    	if (lbClassPath!=null) lbClassPath.setBackground(c);
    	if (lbClassName!=null) lbClassName.setBackground(c);
    	if (tfClassPath!=null) tfClassPath.setBackground(c);
    	if (spApplet!=null) spApplet.setBackground(c);
    	if (tfClassName!=null) tfClassName.setBackground(c);
    	if (lbWidth!=null) lbWidth.setBackground(c);
    	if (tfHeight!=null) tfHeight.setBackground(c);
    	if (jpControl!=null) jpControl.setBackground(c);
		}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField tfParams;
    private javax.swing.JLabel lbHeight;
    private javax.swing.JPanel jpNorth;
    private javax.swing.JLabel lbParams;
    private javax.swing.JTextField tfWidth;
    private javax.swing.JLabel lbClassPath;
    private javax.swing.JLabel lbClassName;
    private javax.swing.JTextField tfClassPath;
    private javax.swing.JScrollPane spApplet;
    private javax.swing.JTextField tfClassName;
    private javax.swing.JLabel lbWidth;
    private javax.swing.JTextField tfHeight;
    private javax.swing.JPanel jpControl;
    // End of variables declaration//GEN-END:variables
    
}