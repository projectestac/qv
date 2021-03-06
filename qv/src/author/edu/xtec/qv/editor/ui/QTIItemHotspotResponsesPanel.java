/*
 * QTIItemHotspotResponsesPanel.java
 *
 * Created on 27 de octubre de 2002, 17:45
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.jclic.shapers.Holes;
import edu.xtec.qv.editor.ManageFrame;
import edu.xtec.qv.editor.shapers.HolesEditorFrame;
import edu.xtec.qv.qti.QTIHotspot;
import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.qv.util.Locator;
import edu.xtec.util.Messages;

/**
 *
 * @author  Albert
 * @version
 */
public class QTIItemHotspotResponsesPanel extends javax.swing.JPanel {
    
    //&protected creator.hotspot.ShapeGenPanel sgp;
    protected MediaImage mi;
    
    protected QTIHotspot qtiHot;
    protected static javax.swing.JFileChooser jfc;
    protected java.io.File directory=null;
    
    protected Holes h;
    protected HolesEditorFrame hef;
    
    
    /** Creates new form QTIItemHotspotResponsesPanel */
    public QTIItemHotspotResponsesPanel(QTIHotspot qtiHot) {
        this.qtiHot=qtiHot;
        mi=new MediaImage(null);
        //&sgp=new creator.hotspot.ShapeGenPanel(this);
        initComponents();
        initComponents2();
        setPreferredSize(getSize());
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
        h=new edu.xtec.jclic.shapers.Holes(1,1);//
        hef=new HolesEditorFrame(qtiHot,h,mi.getImage());//
        initMessages();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jpNorth = new javax.swing.JPanel();
        jpCheck = new javax.swing.JPanel();
        cbDraw = new javax.swing.JCheckBox();
        cbShow = new javax.swing.JCheckBox();
        cbMultiple = new javax.swing.JCheckBox();
        cbTransp = new javax.swing.JCheckBox();
        lbImage = new javax.swing.JLabel();
        tfImage = new javax.swing.JTextField();
        btBrowse = new javax.swing.JButton();
        jpEast = new javax.swing.JPanel();
        lbWidth = new javax.swing.JLabel();
        tfWidth = new javax.swing.JTextField();
        lbHeight = new javax.swing.JLabel();
        tfHeight = new javax.swing.JTextField();
        lbMaxResponses = new javax.swing.JLabel();
        tfMaxResponses = new javax.swing.JTextField();
        btEdit = new javax.swing.JButton();
        spHotspot = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());

        jpNorth.setLayout(new java.awt.GridBagLayout());

        cbDraw.setText("dibuixar linia");
        jpCheck.add(cbDraw);

        cbShow.setText("mostrar opcions");
        jpCheck.add(cbShow);

        cbMultiple.setText("resposta multiple");
        jpCheck.add(cbMultiple);

        cbTransp.setText("transparent");
        jpCheck.add(cbTransp);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jpNorth.add(jpCheck, gridBagConstraints);

        lbImage.setText("Imatge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 2, 2);
        jpNorth.add(lbImage, gridBagConstraints);

        tfImage.setColumns(20);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 2, 2);
        jpNorth.add(tfImage, gridBagConstraints);

        btBrowse.setText("Cercar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 2, 2);
        jpNorth.add(btBrowse, gridBagConstraints);

        add(jpNorth, java.awt.BorderLayout.NORTH);

        jpEast.setLayout(new java.awt.GridBagLayout());

        lbWidth.setText("Amplada");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        jpEast.add(lbWidth, gridBagConstraints);

        tfWidth.setColumns(4);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        jpEast.add(tfWidth, gridBagConstraints);

        lbHeight.setText("Al\u00e7ada");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        jpEast.add(lbHeight, gridBagConstraints);

        tfHeight.setColumns(4);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        jpEast.add(tfHeight, gridBagConstraints);

        lbMaxResponses.setText("M\u00e0x. respostes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        jpEast.add(lbMaxResponses, gridBagConstraints);

        tfMaxResponses.setColumns(2);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        jpEast.add(tfMaxResponses, gridBagConstraints);

        btEdit.setText("Editar");
        btEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(18, 2, 2, 2);
        jpEast.add(btEdit, gridBagConstraints);

        add(jpEast, java.awt.BorderLayout.EAST);

        add(spHotspot, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    private void btEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditActionPerformed
        // Add your handling code here:
        edu.xtec.jclic.shapers.Holes h=new edu.xtec.jclic.shapers.Holes(1,1);
        hef=new HolesEditorFrame(qtiHot,h,mi.getImage());//?�
        hef.hep.setShapes(getShapes());
        hef.setVisible(true);
        
    }//GEN-LAST:event_btEditActionPerformed
    
    private void initComponents2(){
        tfImage.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e){
                String fileName=tfImage.getText();
                mi.setImage(fileName);
                if (mi.getImage()!=null){
                    setImageWidth(mi.getImage().getWidth(QTIItemHotspotResponsesPanel.this));
                    setImageHeight(mi.getImage().getHeight(QTIItemHotspotResponsesPanel.this));
                }
            }
        });
        
        btBrowse.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e){
                String fileName=getFileName();
                if (fileName==null) return;
                tfImage.setText(fileName);
                //&sgp.setBackGroundImage(fileName);
                mi.setImage(fileName);
                if (mi.getImage()!=null){
                    setImageWidth(mi.getImage().getWidth(QTIItemHotspotResponsesPanel.this));
                    setImageHeight(mi.getImage().getHeight(QTIItemHotspotResponsesPanel.this));
                }
            }
        });
        spHotspot.setViewportView(mi);
    }
    
    public void setViewPort(java.awt.Component cView){
        spHotspot.setViewportView(cView);
    }
    
    public void updateImageSize(int iWidth, int iHeight){
        tfWidth.setText(iWidth+"");
        tfHeight.setText(iHeight+"");
    }
    
    public boolean getMultiple(){
        return cbMultiple.isSelected();
    }
    
    public boolean getShowDraw(){
        return cbDraw.isSelected();
    }
    
    public boolean getShowOptions(){
        return cbShow.isSelected();
    }
    
    public boolean getTransp(){
        return cbTransp.isSelected();
    }
    
    public String getURI(){
        return  tfImage.getText();
    }
    
    public String getImageWidth(){
        return tfWidth.getText();
    }
    
    public String getImageHeight(){
        return tfHeight.getText();
    }
    
    public int getMaxResponses(){
        int iMaxResponses=-1;
        try{
            if (tfMaxResponses.getText()!=null && tfMaxResponses.getText().trim().length()>0){
                iMaxResponses=Integer.parseInt(tfMaxResponses.getText());
            }
        }
        catch (Exception e){
        }
        return iMaxResponses;
    }
    
    protected String getFileName(){
        if (jfc==null) jfc=new javax.swing.JFileChooser((directory!=null)?directory:Locator.getLocationDirectory());
        jfc.showOpenDialog(ManageFrame.frame);
        if (jfc.getSelectedFile()==null) return null;
        directory=jfc.getSelectedFile().getParentFile();
        String file=jfc.getSelectedFile().getName();
        
        String sRelativePath=Locator.getRelativePath(jfc.getSelectedFile());
        //System.out.println("Relative:"+sRelativePath);
        return sRelativePath;
    }
    
    
    public void setShapes(java.util.Vector vShapes){
    }
    
    public void notifyShapes(java.util.Vector vShapes){
        qtiHot.notifyShapes(vShapes);
    }
    
    public java.util.Vector getShapes(){
        return qtiHot.getShapes();
    }
    
    public void setImage(java.awt.Image i){
        mi.setImage(i);
        if (mi.getImage()!=null){
            setImageWidth(mi.getImage().getWidth(this));
            setImageHeight(mi.getImage().getHeight(this));
        }
    }
    
    public void setImage(String sURI){
        tfImage.setText(sURI);
    }
    
    public void setImageWidth(int iWidth){
        tfWidth.setText(iWidth+"");
    }
    
    public void setImageHeight(int iHeight){
        tfHeight.setText(iHeight+"");
    }
    
    public void setMultiple(boolean b){
        cbMultiple.setSelected(b);
    }
    
    public void setTransp(boolean b){
        cbTransp.setSelected(b);
    }
    
    public void setShowDraw(boolean b){
        cbDraw.setSelected(b);
    }
    
    public void setShowOptions(boolean b){
        cbShow.setSelected(b);
    }
    
    public void setMaxResponses(String s){
        tfMaxResponses.setText(s);
    }
    
    protected void deselectOther(javax.swing.JToggleButton bt){
    }
    
    protected void initMessages(){
        cbDraw.setText(Messages.getLocalizedString("drawLine"));
        cbShow.setText(Messages.getLocalizedString("showOptions"));
        cbMultiple.setText(Messages.getLocalizedString("multipleResp"));
        cbTransp.setText(Messages.getLocalizedString("transp"));
        lbImage.setText(Messages.getLocalizedString("Image"));
        btBrowse.setText(Messages.getLocalizedString("Search"));
        lbWidth.setText(Messages.getLocalizedString("Width"));
        lbHeight.setText(Messages.getLocalizedString("Height"));
        lbMaxResponses.setText(Messages.getLocalizedString("MaxResp"));
    }
    
    public void setBackground(java.awt.Color c){
        super.setBackground(c);
        if (lbImage!=null) lbImage.setBackground(c);
        if (lbHeight!=null) lbHeight.setBackground(c);
        if (jpCheck!=null) jpCheck.setBackground(c);
        if (tfMaxResponses!=null) tfMaxResponses.setBackground(c);
        if (jpEast!=null) jpEast.setBackground(c);
        if (jpNorth!=null) jpNorth.setBackground(c);
        if (tfWidth!=null) tfWidth.setBackground(c);
        if (cbDraw!=null) cbDraw.setBackground(c);
        if (cbMultiple!=null) cbMultiple.setBackground(c);
        if (cbTransp!=null) cbTransp.setBackground(c);
        if (lbMaxResponses!=null) lbMaxResponses.setBackground(c);
        if (lbWidth!=null) lbMaxResponses.setBackground(c);
        if (btBrowse!=null) btBrowse.setBackground(c);
        if (tfHeight!=null) tfHeight.setBackground(c);
        if (cbShow!=null) cbShow.setBackground(c);
        if (tfImage!=null) tfImage.setBackground(c);
        if (spHotspot!=null) spHotspot.setBackground(c);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbImage;
    private javax.swing.JLabel lbHeight;
    private javax.swing.JPanel jpCheck;
    private javax.swing.JTextField tfMaxResponses;
    private javax.swing.JCheckBox cbTransp;
    private javax.swing.JPanel jpEast;
    private javax.swing.JButton btEdit;
    private javax.swing.JPanel jpNorth;
    private javax.swing.JTextField tfWidth;
    private javax.swing.JCheckBox cbDraw;
    private javax.swing.JCheckBox cbMultiple;
    private javax.swing.JLabel lbMaxResponses;
    private javax.swing.JLabel lbWidth;
    private javax.swing.JButton btBrowse;
    private javax.swing.JTextField tfHeight;
    private javax.swing.JCheckBox cbShow;
    private javax.swing.JTextField tfImage;
    private javax.swing.JScrollPane spHotspot;
    // End of variables declaration//GEN-END:variables
    
}