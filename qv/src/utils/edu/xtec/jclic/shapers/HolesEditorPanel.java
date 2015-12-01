/*
 * JigSawEditor.java
 *
 * Created on 3 de diciembre de 2002, 10:03
 */

package edu.xtec.jclic.shapers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.xtec.jclic.Constants;
import edu.xtec.jclic.boxes.BoxBase;
import edu.xtec.jclic.shapers.utils.ImageBorder;
import edu.xtec.jclic.shapers.utils.ShapeInfo;
import edu.xtec.util.Messages;
import edu.xtec.util.Options;

// import edu.xtec.jclic.edit.EditorPanel;

/**
 *
 * @author  fbusquet
 */
public class HolesEditorPanel extends javax.swing.JPanel implements PointListener{
    
    Options options;
    Holes shaper;
    Image img;
    public Rectangle previewArea;
    //Rectangle previewAreaStart;
    BoxBase previewBb;
    ListModel listModel;
    ListSelectionListener listSelectionListener;
    boolean modified;
    public int currentShape;
    
    //double zoomFactor=2;//1.2;
    
    static final int MARGIN=40;
    
    protected PolygonDrawPanel pdp;
    private boolean shapeDrawn=false;
    public double xFactor=1,yFactor=1;
    double lastWidth=-1, lastHeight=-1;
    
    Dimension previewDim;//
    public ImageBorder ib=null; //NOU

    /** Creates new form JigSawEditor */
    public HolesEditorPanel(Options options, Holes shaper, Dimension previewDim, Image img, BoxBase previewBb) {
        this.options=options;
        this.shaper=shaper;
        this.img=img;
        this.previewDim=previewDim;
        
        //currentShape=-1;
        currentShape=shaper.getNumCells()+1;
        previewArea=new Rectangle(img==null ? previewDim : new Dimension(img.getWidth(this), img.getHeight(this)));

        //previewAreaStart=previewArea;
        this.previewBb=previewBb;
        initMembers();
        initComponents();
        initComponents2();
        customizeComponents();

        pdp=new PolygonDrawPanel(img==null ? (int)(previewDim.getWidth()):img.getWidth(this), img==null ? ((int)previewDim.getHeight()) :img.getHeight(this),this,(img==null));
        pdp.addPointListener((PointListener) this);

        if (previewPanel!=null){
            ((PreviewPanel)previewPanel).vp.addMouseMotionListener(pdp);
            ((PreviewPanel)previewPanel).vp.addMouseListener(pdp);
        }
        
        shapeChanged();
    }
    
    public void setCursor(java.awt.Cursor c){
        if (previewPanel!=null)
            ((PreviewPanel)previewPanel).vp.setCursor(c);
    }
    
    public void setCursor(java.awt.Cursor c, boolean onlyPreviewPanel){
        if (previewPanel!=null)
            ((PreviewPanel)previewPanel).vp.setCursor(c);
        /*if (!onlyPreviewPanel)
            super.setCursor(c);
        else super.setCursor(null);*/
    }
    
    public void setShapes(java.util.Vector vShapeInfo){ //NOU
        java.util.Enumeration e=vShapeInfo.elements();
        while (e.hasMoreElements()){
            ShapeInfo si=(ShapeInfo)e.nextElement();
            java.awt.Rectangle r=getPreviewArea();
            try{
                //System.out.println("creant "+si.getType()+" "+si.getArea());
                ShapeData sd2=si.getShape();
                ShapeData sd=(ShapeData)sd2.clone();
                sd.scaleTo(r.getWidth(),r.getHeight()); // Es redueix al pla [0..1,0..1]
                getHoles().addShape(sd);
            }
            catch (Exception ex){
                ex.printStackTrace(System.out);
            }
        }
        updateList();
        updateView();
        setCurrentShapeNoList(0);
    }

    private void initMembers(){
        listModel=new AbstractListModel(){
            public int getSize(){
                return shaper.shapeData.length;
            }
            public Object getElementAt(int index){
                ShapeData sd=shaper.shapeData[index];
                return (sd.comment==null || sd.comment.length()==0)? Integer.toString(index) : sd.comment;
            }
        };
        
        listSelectionListener=new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent ev){
                if(ev.getValueIsAdjusting())
                    return;
                int v=shapesList.getSelectedIndex();
                if(v!=currentShape && v>=0){
                    if (pdp.getNumShapes()>0) pdp.endPolygon(true,false,v); ////&
                    //if (pdp.getNumShapes()>0) pdp.endPolygon(); ////&
                    if (v>=0 && getHoles().getShapeData(v)!=null) tfName.setText(getHoles().getShapeData(v).comment);
                    else tfName.setText("");
                    ////else if (v>=0) tfName.setText(""+v);
                    setCurrentShapeNoList(v);
                }
                else tfName.setText("");
                //shapeChanged();
            }
        };
    }
    
    public void confirmChanges(){
        pdp.endPolygon();
    }
    
    protected void customizeComponents(){
        customizeIman();
        customizeGrid();
        customizePointsOnGrid();
        btSelect.setSelected(true);
        
        btSelect.setIcon(new ImageIcon(getClass().getResource("/edu/xtec/resources/icons/selectionMode.gif")));
        btSelect.setText("");
        btSelect.setToolTipText("Selecció");
        btDivide.setIcon(new ImageIcon(getClass().getResource("/edu/xtec/resources/icons/plus.gif")));
        btDivide.setText("");
        btDivide.setToolTipText("Afegir punt");
        btDelete.setIcon(new ImageIcon(getClass().getResource("/edu/xtec/resources/icons/missing.gif")));
        btDelete.setText("");
        btDelete.setToolTipText("Eliminar");
        btRect.setIcon(new ImageIcon(getClass().getResource("/edu/xtec/resources/icons/rect.gif")));
        btRect.setText("");
        btRect.setToolTipText("Dibuixar rectangle");
        btEllipse.setIcon(new ImageIcon(getClass().getResource("/edu/xtec/resources/icons/rodona.gif")));
        btEllipse.setText("");
        btEllipse.setToolTipText("Dibuixar el·lipse");
        btScan.setIcon(new ImageIcon(getClass().getResource("/edu/xtec/resources/icons/scanner.gif")));
        btScan.setText("");
        btScan.setToolTipText("Obtenir polígon de la imatge");
        btLine.setIcon(new ImageIcon(getClass().getResource("/edu/xtec/resources/icons/line.gif")));
        btLine.setText("");
        btLine.setToolTipText("Transformar a línia");
        btBezier.setIcon(new ImageIcon(getClass().getResource("/edu/xtec/resources/icons/bezier.gif")));
        btBezier.setText("");
        btBezier.setToolTipText("Transformar a corba de bézier");
        btQuad.setIcon(new ImageIcon(getClass().getResource("/edu/xtec/resources/icons/quad.gif")));
        btQuad.setText("");
        btQuad.setToolTipText("Transformar a quàdrica");
        btPolygon.setIcon(new ImageIcon(getClass().getResource("/edu/xtec/resources/icons/pent.gif")));
        btPolygon.setText("");
        btPolygon.setToolTipText("Dibuixar polígon");
        lbShowGrid.setIcon(new ImageIcon(getClass().getResource("/edu/xtec/resources/icons/grid2.gif")));
        btImant.setIcon(new ImageIcon(getClass().getResource("/edu/xtec/resources/icons/imant2.gif")));
        btImant.setText("");
        btImant.setToolTipText("Aproximar punts");
        
        cbGridSize.setToolTipText("Mida de la graella");
        
        cbIman.setToolTipText("Força de l'iman"); // Els imans tenen camps magnétics i la força és resultant però...
    }
    
    public void updateList(){
        initMembers();
        shapesList.setModel(listModel);
    }
    
    public void setCurrentShape(int v){
        //requestFocus();
        if (shapesList.getSelectedIndex()!=v) shapesList.setSelectedIndex(v);
        btDelete.setEnabled(pdp.getNumShapes()>0);
        ////System.out.println("setCurrentShape btDelete.setEnabled("+(pdp.getNumShapes()>0)+")");
        updateTransformingButtons();
        //System.out.println("current després:"+currentShape);
        currentShape=v;//&
    }
    
    public void setCurrentShapeNoList(int v){
        //System.out.println("setCurrent:"+v);
        currentShape=v;
        pdp.selectShape(v);
        btDelete.setEnabled(pdp.getNumShapes()>0);
        //previewPanel.repaint();
        ((PreviewPanel)previewPanel).updateView();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        bgMode = new javax.swing.ButtonGroup();
        controlPanel = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btSelect = new javax.swing.JToggleButton();
        btDivide = new javax.swing.JToggleButton();
        btDelete = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        btRect = new javax.swing.JToggleButton();
        btEllipse = new javax.swing.JToggleButton();
        btPolygon = new javax.swing.JToggleButton();
        btScan = new javax.swing.JToggleButton();
        btLine = new javax.swing.JButton();
        btBezier = new javax.swing.JButton();
        btQuad = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        rotater = new edu.xtec.jclic.beans.Rotater();
        expander = new edu.xtec.jclic.beans.Expander();
        copier = new edu.xtec.jclic.beans.Copier();
        zoomer = new edu.xtec.jclic.beans.Zoomer();
        btImant = new javax.swing.JToggleButton();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        lbShowGrid = new javax.swing.JLabel();
        cbGridSize = new javax.swing.JComboBox();
        cbIman = new javax.swing.JComboBox();
        cbShowDrawnPoints = new javax.swing.JCheckBox();
        previewPanel = new PreviewPanel();
        jPanel1 = new javax.swing.JPanel();
        listScroll = new javax.swing.JScrollPane();
        shapesList = new javax.swing.JList();
        tfName = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        controlPanel.setLayout(new java.awt.GridBagLayout());

        btSelect.setText("Select");
        bgMode.add(btSelect);
        btSelect.setPreferredSize(new java.awt.Dimension(32, 32));
        btSelect.setMaximumSize(new java.awt.Dimension(32, 32));
        btSelect.setMinimumSize(new java.awt.Dimension(32, 32));
        btSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelectActionPerformed(evt);
            }
        });

        jToolBar1.add(btSelect);

        btDivide.setText("Partir");
        bgMode.add(btDivide);
        btDivide.setPreferredSize(new java.awt.Dimension(32, 32));
        btDivide.setMaximumSize(new java.awt.Dimension(32, 32));
        btDivide.setMinimumSize(new java.awt.Dimension(32, 32));
        btDivide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDivideActionPerformed(evt);
            }
        });

        jToolBar1.add(btDivide);

        btDelete.setText("Eliminar");
        btDelete.setPreferredSize(new java.awt.Dimension(32, 32));
        btDelete.setMaximumSize(new java.awt.Dimension(32, 32));
        btDelete.setMinimumSize(new java.awt.Dimension(32, 32));
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        jToolBar1.add(btDelete);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator2.setPreferredSize(new java.awt.Dimension(10, 0));
        jToolBar1.add(jSeparator2);

        btRect.setText("Rect");
        bgMode.add(btRect);
        btRect.setPreferredSize(new java.awt.Dimension(32, 32));
        btRect.setMaximumSize(new java.awt.Dimension(32, 32));
        btRect.setMinimumSize(new java.awt.Dimension(32, 32));
        btRect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRectActionPerformed(evt);
            }
        });

        jToolBar1.add(btRect);

        btEllipse.setText("El.lipse");
        bgMode.add(btEllipse);
        btEllipse.setPreferredSize(new java.awt.Dimension(32, 32));
        btEllipse.setMaximumSize(new java.awt.Dimension(32, 32));
        btEllipse.setMinimumSize(new java.awt.Dimension(32, 32));
        btEllipse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEllipseActionPerformed(evt);
            }
        });

        jToolBar1.add(btEllipse);

        btPolygon.setText("Poligon");
        bgMode.add(btPolygon);
        btPolygon.setPreferredSize(new java.awt.Dimension(32, 32));
        btPolygon.setMaximumSize(new java.awt.Dimension(32, 32));
        btPolygon.setMinimumSize(new java.awt.Dimension(32, 32));
        btPolygon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPolygonActionPerformed(evt);
            }
        });

        jToolBar1.add(btPolygon);

        btScan.setText("Scan");
        bgMode.add(btScan);
        btScan.setPreferredSize(new java.awt.Dimension(32, 32));
        btScan.setMaximumSize(new java.awt.Dimension(32, 32));
        btScan.setMinimumSize(new java.awt.Dimension(32, 32));
        btScan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btScanActionPerformed(evt);
            }
        });

        jToolBar1.add(btScan);

        btLine.setText("L\u00ednia");
        btLine.setPreferredSize(new java.awt.Dimension(32, 32));
        btLine.setMaximumSize(new java.awt.Dimension(32, 32));
        btLine.setMinimumSize(new java.awt.Dimension(32, 32));
        btLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLineActionPerformed(evt);
            }
        });

        jToolBar1.add(btLine);

        btBezier.setText("Bezier");
        btBezier.setPreferredSize(new java.awt.Dimension(32, 32));
        btBezier.setMaximumSize(new java.awt.Dimension(32, 32));
        btBezier.setMinimumSize(new java.awt.Dimension(32, 32));
        btBezier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBezierActionPerformed(evt);
            }
        });

        jToolBar1.add(btBezier);

        btQuad.setText("Quad.");
        btQuad.setPreferredSize(new java.awt.Dimension(32, 32));
        btQuad.setMaximumSize(new java.awt.Dimension(32, 32));
        btQuad.setMinimumSize(new java.awt.Dimension(32, 32));
        btQuad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btQuadActionPerformed(evt);
            }
        });

        jToolBar1.add(btQuad);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setPreferredSize(new java.awt.Dimension(10, 0));
        jToolBar1.add(jSeparator1);

        rotater.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                rotaterPropertyChange(evt);
            }
        });

        jToolBar1.add(rotater);

        expander.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                expanderPropertyChange(evt);
            }
        });

        jToolBar1.add(expander);

        copier.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                copierPropertyChange(evt);
            }
        });

        jToolBar1.add(copier);

        zoomer.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                zoomerPropertyChange(evt);
            }
        });

        jToolBar1.add(zoomer);

        btImant.setText("jToggleButton1");
        btImant.setPreferredSize(new java.awt.Dimension(32, 32));
        btImant.setMaximumSize(new java.awt.Dimension(32, 32));
        btImant.setMinimumSize(new java.awt.Dimension(32, 32));
        jToolBar1.add(btImant);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator3.setPreferredSize(new java.awt.Dimension(10, 0));
        jToolBar1.add(jSeparator3);

        controlPanel.add(jToolBar1, new java.awt.GridBagConstraints());

        jPanel3.setLayout(new java.awt.GridBagLayout());

        lbShowGrid.setPreferredSize(new java.awt.Dimension(32, 32));
        lbShowGrid.setMinimumSize(new java.awt.Dimension(32, 32));
        lbShowGrid.setMaximumSize(new java.awt.Dimension(32, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel3.add(lbShowGrid, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 7);
        jPanel3.add(cbGridSize, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 7, 3, 3);
        jPanel3.add(cbIman, gridBagConstraints);

        cbShowDrawnPoints.setText("Mostrar punts");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel3.add(cbShowDrawnPoints, gridBagConstraints);

        controlPanel.add(jPanel3, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(controlPanel, gridBagConstraints);

        previewPanel.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        previewPanel.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        previewPanel.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                previewPanelPropertyChange(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(previewPanel, gridBagConstraints);

        jPanel1.setLayout(new java.awt.BorderLayout(10, 8));

        listScroll.setPreferredSize(new java.awt.Dimension(50, 100));
        shapesList.setModel(listModel);
        shapesList.addListSelectionListener(listSelectionListener);
        listScroll.setViewportView(shapesList);

        jPanel1.add(listScroll, java.awt.BorderLayout.CENTER);

        tfName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfNameActionPerformed(evt);
            }
        });

        jPanel1.add(tfName, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        add(jPanel1, gridBagConstraints);

    }//GEN-END:initComponents

    private void btScanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btScanActionPerformed
        // Add your handling code here:
        requestFocus();
        pdp.setDrawingMode(PolygonDrawPanel.SCANNING_SHAPE);
    }//GEN-LAST:event_btScanActionPerformed
    
    protected void initComponents2(){
        jToolBar1.setFloatable(false);
    }
    
    public void addKeyListener(java.awt.event.KeyListener kl){
        super.addKeyListener(kl);
        if (previewPanel!=null) ((PreviewPanel)previewPanel).vp.addKeyListener(kl);
        if (jToolBar1!=null) jToolBar1.addKeyListener(kl);
        if (previewPanel!=null) previewPanel.addKeyListener(kl);
    }
    
    private void tfNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfNameActionPerformed
        // Add your handling code here:
        ShapeData sd=null;
        if (currentShape>=0){
            sd=getHoles().getShapeData(currentShape);
            //Potser caldria comprovar que no estigui repetit el nom
            if (sd!=null && tfName.getText()!=null && tfName.getText().trim().length()>0) sd.comment=tfName.getText();
            updateList();
        }
    }//GEN-LAST:event_tfNameActionPerformed
    
    private void btPolygonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPolygonActionPerformed
        // Add your handling code here:
        requestFocus();
        pdp.setDrawingMode(PolygonDrawPanel.DRAWING_POLYGON);
    }//GEN-LAST:event_btPolygonActionPerformed
    
    private void btMoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMoveActionPerformed
        // Add your handling code here:
        pdp.setDrawingMode(PolygonDrawPanel.MOVING);
    }//GEN-LAST:event_btMoveActionPerformed
    
    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        // Add your handling code here:
        clean();
        shapeChanged();
        tfName.setText("");
        repaint(0);
    }//GEN-LAST:event_btDeleteActionPerformed
    
    private void previewPanelPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_previewPanelPropertyChange
        // Add your handling code here:
        //System.out.println("P");
        repaint(0);
    }//GEN-LAST:event_previewPanelPropertyChange
    
    private void copierPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_copierPropertyChange
        // Add your handling code here:
        if (evt==null || evt.getNewValue()==null) return;
        String sValue=evt.getNewValue().toString();
        if (sValue==null) return;
        try{
            /*if (sValue.equals(edu.xtec.jclic.beans.Copier.BT_NAMES[0])){ //cut
                pdp.cut();
            }*/
            if (sValue.equals(edu.xtec.jclic.beans.Copier.BT_NAMES[0])){ //copy
                pdp.copy(false);
            }
            if (sValue.equals(edu.xtec.jclic.beans.Copier.BT_NAMES[1])){ //paste
                pdp.endPolygon();////////
                pdp.paste();
            }
        }
        catch (Exception e){
        }
        updateView();
    }//GEN-LAST:event_copierPropertyChange
    
    private void zoomerPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_zoomerPropertyChange
        // Add your handling code here:
        if (evt==null || evt.getNewValue()==null) return;
        String sValue=evt.getNewValue().toString();
        try{
            //int value=Integer.parseInt(sValue);
            double xExtraFactor=Double.parseDouble(sValue);
            doZoom(xExtraFactor);
        }
        catch (Exception e){
            pdp.endPolygon();
        }
    }//GEN-LAST:event_zoomerPropertyChange
    
    private synchronized void doZoom(double xExtraFactor){
        if (xExtraFactor!=1 /*|| yExtraFactor!=1*/){
            pdp.endPolygon();
            updatePreviewArea(xExtraFactor,xExtraFactor);//yExtraFactor);
            ////pdp.initDrawnBorders();
        }
    }
    
    private void expanderPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_expanderPropertyChange
        // Add your handling code here:
        if (evt==null || evt.getNewValue()==null) return;
        String sValue=evt.getNewValue().toString();
        try{
            int value=Integer.parseInt(sValue);
            switch(value){
                case 0:
                    pdp.scale(EditableShapeConstants.scaleXFactor,EditableShapeConstants.scaleYFactor,false,false);
                    break;
                case 1:
                    pdp.scale(1/EditableShapeConstants.scaleXFactor,1/EditableShapeConstants.scaleYFactor,false,false);
                    break;
            }
        }
        catch (Exception e){
        }
    }//GEN-LAST:event_expanderPropertyChange
    
    private void rotaterPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_rotaterPropertyChange
        // Add your handling code here:
        if (evt==null || evt.getNewValue()==null) return;
        String sValue=evt.getNewValue().toString();
        try{
            int value=Integer.parseInt(sValue);
            double theta=0;
            switch(value){
                case 0:
                    theta=-(Math.PI/180)*15;
                    break;
                case 1:
                    theta=(Math.PI/180)*15;
                    break;
            }
            if (theta!=0) pdp.rotate(theta,false,false);
        }
        catch (Exception e){
        }
    }//GEN-LAST:event_rotaterPropertyChange
    
    private void btDivideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDivideActionPerformed
        // Add your handling code here:
        deselectAll();
        requestFocus();
        btDivide.setSelected(true);
        pdp.setDrawingMode(PolygonDrawPanel.NEW_POINT);
        pdp.deSelectAll();
        repaint(0);
    }//GEN-LAST:event_btDivideActionPerformed
    
    private void btQuadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btQuadActionPerformed
        // Add your handling code here:
        requestFocus();
        pdp.convertToQuad();
        repaint(0);
    }//GEN-LAST:event_btQuadActionPerformed
    
    private void btBezierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBezierActionPerformed
        // Add your handling code here:
        requestFocus();
        pdp.convertToBezier();
        repaint(0);
    }//GEN-LAST:event_btBezierActionPerformed
    
    private void btLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLineActionPerformed
        // Add your handling code here:
        requestFocus();
        pdp.convertToLine();
        repaint(0);
    }//GEN-LAST:event_btLineActionPerformed
    
    private void btNewPolygonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNewPolygonActionPerformed
        // Add your handling code here:
        //pdp.requestFocus();
        pdp.endPolygon();
        clean();
        repaint(0);
        shapeChanged();
        setDrawingRectangleMode();
    }//GEN-LAST:event_btNewPolygonActionPerformed
    
    private void setDrawingRectangleMode(){
        deselectAll();
        btRect.setSelected(true);
        pdp.setDrawingMode(PolygonDrawPanel.DRAWING_RECT);
        pdp.deSelectAll();
        repaint(0);
    }
    
    private void deselectAll(){
        pdp.cancelCurrentOperations();
        btSelect.setSelected(false);
        btRect.setSelected(false);
        btScan.setSelected(false);
        btEllipse.setSelected(false);
        btPolygon.setSelected(false);
        btBezier.setSelected(false);
        btQuad.setSelected(false);
        btDivide.setSelected(false);
        //        btZoom.setSelected(false);
    }
    
    public void setDrawingMode(int drawingMode){
        pdp.setDrawingMode(drawingMode);
        switch (drawingMode){
            case PolygonDrawPanel.SELECTING:
                btSelect.setSelected(true);
                break;
            case PolygonDrawPanel.NEW_POINT:
                btDivide.setSelected(true);
                break;
        }
        btDelete.setEnabled(pdp.getNumShapes()>0);
    }
    
    public void shapeChanged() {
        if (pdp.getNumShapes()>0){ //Ja no podem crear un rectangle o el.lipse
            btDelete.setEnabled(true);
            //System.out.println("shapeChanged btDelete.setEnabled(true)");
            shapeDrawn=true;
            repaint(0);
        }
        else {
            btDelete.setEnabled(pdp.hasSelectedPoint());
            //System.out.println("shapeChanged btDelete.setEnabled("+pdp.hasSelectedPoint()+")");
            clean();
        }
        if (!btDivide.isSelected()){
            pdp.setDrawingMode(PolygonDrawPanel.SELECTING);
            deselectAll();
            btSelect.setSelected(true);
        }
        else btSelect.setSelected(false);
        
        updateTransformingButtons();
    }
    
    protected void updateTransformingButtons(){
        java.util.Vector v=pdp.getSelectedShapes();
        if (v.size()==1){
            java.util.Enumeration e=v.elements();
            EditableShape shape=(EditableShape)e.nextElement(); //El té segur: size==1
            if (!(shape instanceof EditableRectangle)){
                btBezier.setEnabled(true);
                btQuad.setEnabled(true);
            }
            if (!(shape instanceof EditableEllipse2D)) btLine.setEnabled(true);
        }
        else{
            btBezier.setEnabled(false);
            btQuad.setEnabled(false);
            btLine.setEnabled(false);
            repaint(0);
        }
    }
    
    public PreviewPanel getPreviewPanel(){
        return (PreviewPanel)previewPanel;
    }
    
    private void clean(){
        //pdp.deleteCurrent();
        pdp.deleteSelected(false);
        shapeDrawn=false;
        deselectAll();
        btRect.setEnabled(true);
        btEllipse.setEnabled(true);
        btPolygon.setEnabled(true);
        btRect.setEnabled(true);
        ////btDivide.setEnabled(false);
    }
    
    private void btEllipseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEllipseActionPerformed
        // Add your handling code here:
        requestFocus();
        pdp.setDrawingMode(PolygonDrawPanel.DRAWING_ELLIPSE);
    }//GEN-LAST:event_btEllipseActionPerformed
    
    private void btZoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btZoomActionPerformed
            }//GEN-LAST:event_btZoomActionPerformed
    
    private void btSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSelectActionPerformed
        // Add your handling code here:
        requestFocus();
        pdp.setDrawingMode(PolygonDrawPanel.SELECTING);
    }//GEN-LAST:event_btSelectActionPerformed
    
    private void btRectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRectActionPerformed
        // Add your handling code here:
        requestFocus();
        pdp.setDrawingMode(PolygonDrawPanel.DRAWING_RECT);
    }//GEN-LAST:event_btRectActionPerformed
    
    private void customizeIman(){
        Integer[] aprox={new Integer(0),new Integer(1),new Integer(2),new Integer(4),new Integer(6),new Integer(10),new Integer(15)};
        for (int i=0;i<aprox.length;i++) cbIman.addItem(aprox[i]);
        cbIman.setSelectedItem(new Integer(EditableShapeConstants.selectLength/2));
        javax.swing.Action actionAprox = new javax.swing.AbstractAction("Aproximar"){ //l'iman
            public void actionPerformed(java.awt.event.ActionEvent e){
                //pdp.requestFocus();
                EditableShapeConstants.selectLength=((Integer)cbIman.getSelectedItem()).intValue()*2;
                //System.out.println("SelectLength:"+EditableShapeConstants.selectLength);
                pdp.initDrawnBorders();
                repaint(0);
            }
        };
        cbIman.addActionListener(actionAprox);
        
        cbShowDrawnPoints.setSelected(EditableShapeConstants.showDrawnPoints);
        javax.swing.Action actionShowDrawnPoints = new javax.swing.AbstractAction("Mostrar punts"){ //l'iman
            public void actionPerformed(java.awt.event.ActionEvent e){
                //pdp.requestFocus();
                EditableShapeConstants.showDrawnPoints=cbShowDrawnPoints.isSelected();
                repaint(0);
            }
        };
        cbShowDrawnPoints.addActionListener(actionShowDrawnPoints);

    }
    
    private void customizeGrid(){
        String[] grid={"No","5","10","15","20","30","50"};
        for (int i=0;i<grid.length;i++) cbGridSize.addItem(grid[i]);
        if (EditableShapeConstants.gridWidth==-1) cbGridSize.setSelectedItem("No");
        else cbGridSize.setSelectedItem(""+EditableShapeConstants.gridWidth);
        javax.swing.Action actionGrid = new javax.swing.AbstractAction("Graella"){ //l'iman
            public void actionPerformed(java.awt.event.ActionEvent e){
                //pdp.requestFocus();
                String selected=(String)cbGridSize.getSelectedItem();
                if (selected.equals("No")) EditableShapeConstants.gridWidth=-1;
                else EditableShapeConstants.gridWidth=Integer.parseInt(selected);
                repaint(0);
            }
        };
        cbGridSize.addActionListener(actionGrid);
    }
    
    private void customizePointsOnGrid(){
        btImant.setSelected(EditableShapeConstants.pointsOnGrid);
        //cbAdjust.setSelected(EditableShapeConstants.pointsOnGrid);
        javax.swing.Action actionCheckGrid = new javax.swing.AbstractAction("Aproximar punts"){ //l'iman
            public void actionPerformed(java.awt.event.ActionEvent e){
                EditableShapeConstants.pointsOnGrid=btImant.isSelected();
                //EditableShapeConstants.pointsOnGrid=cbAdjust.isSelected();
                repaint(0);
            }
        };
        //cbAdjust.addActionListener(actionCheckGrid);
        btImant.addActionListener(actionCheckGrid);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btDivide;
    private javax.swing.JToggleButton btScan;
    private javax.swing.JToggleButton btRect;
    private javax.swing.ButtonGroup bgMode;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToggleButton btEllipse;
    private javax.swing.JLabel lbShowGrid;
    private javax.swing.JToggleButton btImant;
    private javax.swing.JToggleButton btPolygon;
    private javax.swing.JCheckBox cbShowDrawnPoints;
    private edu.xtec.jclic.beans.Expander expander;
    private edu.xtec.jclic.beans.Rotater rotater;
    private javax.swing.JComboBox cbGridSize;
    private edu.xtec.jclic.beans.Copier copier;
    private javax.swing.JButton btBezier;
    private javax.swing.JComboBox cbIman;
    private javax.swing.JButton btLine;
    private javax.swing.JScrollPane listScroll;
    private javax.swing.JList shapesList;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JToggleButton btSelect;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton btDelete;
    private javax.swing.JScrollPane previewPanel;
    private javax.swing.JTextField tfName;
    private javax.swing.JButton btQuad;
    private edu.xtec.jclic.beans.Zoomer zoomer;
    // End of variables declaration//GEN-END:variables
    
    public void updateView(){
        ((PreviewPanel)previewPanel).updateView();
    }
    
    public class PreviewPanel extends JScrollPane{
        
        public VP vp;
        
        public PreviewPanel(){
            vp=new VP();
            setViewportView(vp);
            updateView();
        }
        
        public void updateView(){
            vp.updateView();
            //doLayout();////
        }
        
        public void doLayout(){
            super.doLayout();
            //////vp.doLayout();
        }
    }
    
    class VP extends JPanel{
        
        private int xBak=-1, yBak=-1, wBak=-1, hBak=-1;
        
        Vector shapes=new Vector();
        
        public void updateView(){
            setSize(getSize());
            setPreferredSize(getSize());
            shapes.removeAllElements();
            for(int i=0; i<shaper.getNumCells(); i++){
                shapes.add(shaper.getShape(i, previewArea));
            }
            if (pdp!=null) pdp.updateView();
            super.updateUI();
            repaint();
        }
        
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2=(Graphics2D)g;
            RenderingHints rh=g2.getRenderingHints();
            g2.setRenderingHints(Constants.DEFAULT_RENDERING_HINTS);
            
            Color defaultBgColor=g2.getBackground();
            Color defaultColor=g2.getColor();
            
            g2.setColor(previewBb.backColor);
            g2.fill(previewArea);
            g2.setBackground(previewBb.backColor);
            g2.setColor(previewBb.borderColor);
            Stroke defaultStroke=g2.getStroke();
            g2.setStroke(previewBb.getBorder());
            
            if(img!=null){
                g2.drawImage(img, previewArea.x, previewArea.y, previewArea.width, previewArea.height, this);
            }
            
            pdp.drawGrid(g,EditableShapeConstants.gridWidth);
            
            g2.setColor(Color.black);
            for(int i=0; i<shapes.size(); i++){
                if(i!=currentShape)
                    g2.draw((Shape)shapes.get(i));
            }
            
            g2.setColor(Color.red);
            pdp.paint(g2);
            ///%            drawBorder(g2,defaultBgColor);
            g2.setStroke(defaultStroke);
            g2.setColor(defaultColor);
            g2.setBackground(defaultBgColor);
            
            g2.setRenderingHints(rh);
        }
        
        protected void drawBorder(Graphics g, Color c){
            g.setColor(c);
            g.fillRect(0,0,(int)previewArea.getX(),getHeight());
        }
        
        public void doLayout(){
            previewArea.x=(getBounds().width-previewArea.width)/2;
            previewArea.y=(getBounds().height-previewArea.height)/2;
            //System.out.println("ACTUALITZAT PREVIEWAREA x="+previewArea.x+" y="+previewArea.y+" w="+previewArea.width+" h="+previewArea.height);
            if (previewArea.x!=xBak || previewArea.y!=yBak || previewArea.width!=wBak || previewArea.height!=hBak){
                xBak=previewArea.x;
                yBak=previewArea.y;
                wBak=previewArea.width;
                hBak=previewArea.height;
                pdp.initDrawnBorders();
            }
            //previewArea.x=0;
            //previewArea.y=0;
            updateView();
        }
        
        public Dimension getSize(){
            return new Dimension(previewArea.x+(int)previewArea.getBounds().getWidth(),previewArea.y+(int)previewArea.getBounds().getHeight());
        }
    }
    
    public Holes getHoles(){
        return shaper;
    }
    
    public Rectangle getPreviewArea(){
        return previewArea;
    }
    
    public void setPreviewArea(Rectangle r){
        previewArea=r;
    }
    
    public int getNumShapes(){
        return shaper.getNumCells();
    }
    
    public static Shaper getShaper(Shaper initialShaper, Component parent, Options options, Dimension dim, Image img, BoxBase bb){
        Messages msg=options.getMessages();
        if(initialShaper==null || !(initialShaper instanceof Holes))
            return null;
        
        Holes sh=null;
        try{
            sh=(Holes)initialShaper.clone();
        } catch(CloneNotSupportedException ex){
            msg.showErrorWarning(parent, "edit_act_shaper_err", ex);
            return null;
        }
        
        HolesEditorPanel he=new HolesEditorPanel(options, sh, dim, img, bb);
        boolean b=msg.showInputDlg(parent, he, "edit_act_shaper_properties");
        if (b) he.confirmChanges();
        return b ? sh : null;
    }
    
    public void updatePreviewArea(double xFactor, double yFactor){
        if (lastWidth!=-1){
            lastWidth*=xFactor;
            lastHeight*=yFactor;
        }
        else{
            lastWidth=previewArea.getWidth()*xFactor;
            lastHeight=previewArea.getHeight()*yFactor;
        }
        this.xFactor*=xFactor;
        this.yFactor*=yFactor;
        
        //previewArea=new Rectangle(new Point((int)(previewArea.x*xFactor),(int)(previewArea.y*yFactor)),new Dimension((int)(lastWidth), (int)(lastHeight)));
        previewArea=new Rectangle(new Dimension((int)(lastWidth), (int)(lastHeight)));        
        
        //previewArea.x=(int)((((PreviewPanel)previewPanel).vp.getBounds().width-lastWidth)/2);
        //previewArea.y=(int)((((PreviewPanel)previewPanel).vp.getBounds().height-lastHeight)/2);
        
        //previewArea=new Rectangle(new Dimension((int)(previewArea.getWidth()*xFactor), (int)(previewArea.getHeight()*yFactor)));
        updateView();
    }
    
    public void incDrawingArea(double incWidth, double incHeight){
        modifyDrawingArea(previewArea.width+incWidth,previewArea.height+incHeight);
    }
    
    protected void modifyDrawingArea(double newWidth, double newHeight){
        if(newWidth>0 && newHeight>0){
            double xFactor=newWidth/previewArea.width;
            double yFactor=newHeight/previewArea.height;
            for(int i=0; i<shaper.getNumCells(); i++){
                ShapeData sd=shaper.getShapeData(i);
                sd.scaleTo(xFactor, yFactor);
            }
            previewArea.setSize((int)newWidth, (int)newHeight);
            previewDim.setSize(previewArea.getSize());
            
            shaper.scaleW=newWidth;
            shaper.scaleH=newHeight;
            
            updateView();
        }
    }
    
    public void addBorderShape(int x, int y){ //NOU
        //System.out.println("addBorderShape("+x+","+y+")");
        if (previewArea.x>0) x-=previewArea.x;
        if (previewArea.y>0) y-=previewArea.y;
        if (xFactor!=1) x/=xFactor;
        if (yFactor!=1) y/=yFactor;
        //System.out.println("-->addBorderShape("+x+","+y+")");
        if (ib==null) ib=new ImageBorder(img,this);
        ShapeData sd=ib.getShapeData(x,y);
        if (sd==null) System.out.println("sd==null!!");
        if (sd!=null){
            if (xFactor!=1 || yFactor!=1) sd.scaleTo(1.0/xFactor,1.0/yFactor);
            sd.comment=(""+currentShape);
            sd.scaleTo(previewArea.width,previewArea.height);
            pdp.endPolygon();
            getHoles().addShape(sd);
            updateList();
            updateView();//
        }
    }
    
    public void pointMoved(java.awt.geom.Point2D p) {
    }
    
}
