/*
 * Expander.java
 *
 * Created on 19 / desembre / 2002, 09:29
 */

package edu.xtec.jclic.beans;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author  allastar
 */
public class Expander extends Rotater{

    public static String PROPERTY = "expand";    
    public static String[] BT_NAMES={"expand", "contract"};
    
    /** Creates a new instance of Expander */
    public Expander() {
        //sleepTime=200;
    }
    
    protected javax.swing.AbstractButton createButton(int i){
        JButton bt=new JButton(new ImageIcon(getClass().getResource("/edu/xtec/resources/icons/"+BT_NAMES[i]+".gif")));
        if (i==0) bt.setToolTipText("Expandir");
        else bt.setToolTipText("Contraure");
        return bt;
    }    

}
