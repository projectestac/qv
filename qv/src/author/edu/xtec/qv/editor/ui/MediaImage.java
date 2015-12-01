/*
 * MediaImage.java
 *
 * Created on 23 / octubre / 2002, 09:11
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.util.Utility;

/**
 *
 * @author  allastar
 */
public class MediaImage extends javax.swing.JPanel{
    
    /** Creates a new instance of MediaImage */
    java.awt.Image i;
    
    public MediaImage(java.awt.Image i){
        if (i!=null) setImage(i);
    }
    
    public void setImage(java.awt.Image i){
        if (i!=null){
            this.i=i;
            if (i.getWidth(this)>0) setSize(i.getWidth(this)+20,i.getHeight(this)+60);
            else setSize(400,400);
            repaint();
        }
    }
    
    public void setImage(String fileName){
        setImage(Utility.loadImage(fileName,this));
    }
    
                /*protected void paintComponent(Graphics g){
                        if (i!=null) g.drawImage(i,0,0,MediaPanel.this);
                }*/
    
    public java.awt.Image getImage(){
        return i;
    }
    
    public void paint(java.awt.Graphics g){
        super.paint(g);
        if (i!=null){
            int viewWidth=getWidth();
            int viewHeight=getHeight();
            int imageWidth=i.getWidth(this);
            int imageHeight=i.getHeight(this);
            
            int iniX=(viewWidth>imageWidth)?((viewWidth-imageWidth)/2):0;
            int iniY=(viewHeight>imageHeight)?((viewHeight-imageHeight)/2):0;
            /*if (i!=null) */g.drawImage(i,iniX,iniY,this);
        }
        ////setSize(getSize());
        //repaint(0);
    }
    
                /*public java.awt.Dimension getSize(){
                    if (i!=null) return new Dimension(i.getWidth(this),i.getHeight(this));
                    else return super.getSize();
                }
                 
                public Dimension getPreferredSize() {
                    return getSize();
                }
                public Dimension getMaximumSize() { return getPreferredSize(); }
                public Dimension getMinimumSize() { return getPreferredSize(); }*/
}
