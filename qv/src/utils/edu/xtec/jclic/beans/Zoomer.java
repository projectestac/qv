/*
 * Zoomer.java
 *
 * Created on 19 / desembre / 2002, 12:20
 */

package edu.xtec.jclic.beans;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author  allastar
 */
public class Zoomer extends javax.swing.JPanel{
    
    public static String PROPERTY = "zoom";
    public static String[] BT_NAMES={"zoomIn", "zoomOut"};
    public static final java.awt.Dimension BT_DIMENSION=new java.awt.Dimension(16, 16);
    public static final java.awt.Dimension LB_DIMENSION=new java.awt.Dimension(24, 16);
    
    protected javax.swing.AbstractButton[] buttons=new javax.swing.AbstractButton[2];
    protected JLabel jlZoomFactor;
    protected int direction;
    
    protected double xFactor;
    protected double yFactor;
    
    protected double maxFactor;
    protected double minFactor;
    
    /** Creates a new instance of Zoomer */
    public Zoomer() {
        this(10,-2); // -2= 1/4
    }
    
    public Zoomer(double maxFactor, double minFactor) {
        xFactor=1;
        yFactor=1;
        this.maxFactor=maxFactor;
        this.minFactor=minFactor;
        
        direction=-1;
        
        ButtonGroup bg=new ButtonGroup();
        setLayout(new java.awt.GridLayout(1, 3));
        addButton(0,bg);
        jlZoomFactor=new JLabel("",javax.swing.SwingConstants.CENTER);
        jlZoomFactor.setMinimumSize(LB_DIMENSION);
        jlZoomFactor.setPreferredSize(LB_DIMENSION);
        updateZoomFactorLabelText();
        add(jlZoomFactor);
        addButton(1,bg);
        updateButtonsState();
        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.black));
    }
    
    protected void addButton(int i, ButtonGroup bg){
        javax.swing.AbstractButton btn=createButton(i);
        btn.setPreferredSize(BT_DIMENSION);
        bg.add(btn);
        addListeners(btn,i);
        
        btn.setFocusPainted(false);
        add(btn);
        buttons[i]=btn;
    }
    
    protected void updateZoomFactorLabelText(){
        int iFactor=(int)xFactor;
        String sFactor=(iFactor>=1)?(iFactor+""):("1/"+Math.abs(iFactor-2));
        jlZoomFactor.setText(sFactor+"x");
    }
    
    protected javax.swing.AbstractButton createButton(int i){
        JButton bt=new JButton(new ImageIcon(getClass().getResource("/edu/xtec/resources/icons/"+BT_NAMES[i]+".gif")));
        if (i==0) bt.setToolTipText("Apropar");//Zoom in");
        else bt.setToolTipText("Allunyar");//Zoom out");
        return bt;
    }
    
    protected void updateButtonsState(){
        // Habilita/in els butons en funció si són aplicables o no.
        if (xFactor>=maxFactor) buttons[0].setEnabled(false);
        else buttons[0].setEnabled(true);
        if (xFactor<=minFactor) buttons[1].setEnabled(false);
        else buttons[1].setEnabled(true);        
    }
    
    public void setDirection(int value) {
        direction=value;
        double xExtraFactor=doZoom(value);
        firePropertyChange(PROPERTY, direction+1, xExtraFactor);
    }
    
    protected void addListeners(javax.swing.AbstractButton btn, int iIndex){
        java.awt.event.ActionListener lst=new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent ev){
                String c=(ev.getSource() instanceof javax.swing.AbstractButton)?((javax.swing.AbstractButton)ev.getSource()).getActionCommand():"";
                for(int i=0; i<2; i++){
                    if(BT_NAMES[i].equals(c)){
                        setDirection(i);
                        break;
                    }
                }
            }
        };
        btn.addActionListener(lst);
        
        btn.setActionCommand(BT_NAMES[iIndex]);
    }
    
    private synchronized double doZoom(int value){
        double xExtraFactor=1,yExtraFactor=1;
        switch(value){
            case 0:
                if (xFactor<maxFactor){
                    if (xFactor>=1){
                        xExtraFactor=(xFactor+1)/xFactor; yExtraFactor=(yFactor+1)/yFactor;
                    }
                    else{
                        double den=Math.abs(xFactor-2);
                        xExtraFactor=(1/(den-1))/(1/den); yExtraFactor=(1/(den-1))/(1/den); //(den/(den-1))
                    }
                    xFactor++;yFactor++;
                }
                break;
            case 1:
                if (xFactor>minFactor){
                    if (xFactor>1){
                        xExtraFactor=(xFactor-1)/xFactor; yExtraFactor=(yFactor-1)/yFactor;
                    }
                    else {
                        double den=Math.abs(xFactor-2);
                        xExtraFactor=(1/(den+1))/(1/den); yExtraFactor=(1/(den+1))/(1/den);// den/(den+1);
                    }
                    xFactor--;yFactor--;
                }
                break;
        }
        updateButtonsState();
        updateZoomFactorLabelText();
        //System.out.println("xFactor:"+xFactor+" xExtraFactor:"+xExtraFactor);
        return xExtraFactor;
    }
    
}
