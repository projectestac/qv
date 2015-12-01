/*
 * AppletProva.java
 *
 * Created on 23 / octubre / 2002, 13:43
 */

package edu.xtec.qv.editor.ui;

/**
 *
 * @author  allastar
 */
public class AppletProva extends java.applet.Applet {
    
    /** Initialization method that will be called after the applet is loaded
     *  into the browser.
     */
    
    public AppletProva(){
        System.out.println("EXECUTANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
    
    public void init() {
    }
    
    public void paint(java.awt.Graphics g){
        g.setColor(java.awt.Color.blue);
        g.fillOval(10,10,200,200);
    }
    
}
