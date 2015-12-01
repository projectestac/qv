/*
 * Copier.java
 *
 * Created on 19 / desembre / 2002, 12:58
 */

package edu.xtec.jclic.beans;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Copier extends JPanel{

    public static final String PROP_EDITING = "edit";
    
    public static final String[] BT_NAMES={/*"cut", */"copy", "paste"};
    public static final java.awt.Dimension BT_DIMENSION=new java.awt.Dimension(16, 16);
    
    protected javax.swing.AbstractButton[] buttons=new javax.swing.AbstractButton[2];
    private String sEditFunction;

    /** Creates a new instance of Copier */
    public Copier() {
        super();
        sEditFunction="";
        ActionListener lst=new ActionListener(){
            public void actionPerformed(ActionEvent ev){
                String c=ev.getActionCommand();
                for(int i=0; i<2; i++){
                    if(BT_NAMES[i].equals(c)){
                        setEditFunction(c);
                        break;
                    }
                }
            }
        };
        ButtonGroup bg=new ButtonGroup();
        setLayout(new java.awt.GridLayout(1, 2));
        for(int i=0; i<2; i++){
            javax.swing.AbstractButton btn=createButton(i);
            btn.setPreferredSize(BT_DIMENSION);
            bg.add(btn);
            btn.setActionCommand(BT_NAMES[i]);
            btn.addActionListener(lst);
            btn.setFocusPainted(false);
            add(btn);
            buttons[i]=btn;
        }
        
    }
    
    protected javax.swing.AbstractButton createButton(int i){
        //return new JToggleButton(new ImageIcon(getClass().getResource("/edu/xtec/resources/icons/"+BT_NAMES[i]+".gif")));
        javax.swing.JButton bt=new javax.swing.JButton(new ImageIcon(getClass().getResource("/edu/xtec/resources/icons/"+BT_NAMES[i]+".gif")));
        if (i==0) bt.setToolTipText("Copiar");
        else bt.setToolTipText("Enganxar");
        return bt;
    }
    
    public String getEditFunction() {
        return sEditFunction;
    }
    
    public void setEditFunction(String sEditFunction) {
        this.sEditFunction=sEditFunction;
        firePropertyChange(PROP_EDITING, "", sEditFunction);
    }
    
}
