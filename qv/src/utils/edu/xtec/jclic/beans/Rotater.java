/*
 * Rotater.java
 *
 * Created on 19 / desembre / 2002, 08:55
 */

package edu.xtec.jclic.beans;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author  allastar
 */

public class Rotater extends JPanel {
    
    public static String PROPERTY = "rotate";
    
    public static String[] BT_NAMES={"rotate_left", "rotate_right"};
    public static final java.awt.Dimension BT_DIMENSION=new java.awt.Dimension(16, 16);
    //public static final java.awt.Dimension BT_DIMENSION=new java.awt.Dimension(32, 32);
    
    protected javax.swing.AbstractButton[] buttons=new javax.swing.AbstractButton[2];
    protected int direction;
    protected int sleepTime=50;
    protected boolean bPressed=false;
    
    /** Creates a new instance of Rotater */
    public Rotater() {    
        super();
        direction=-1;        

        MouseListener mlst=new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                setPressed(true);
                requestFocus();
            }
            public void mouseReleased(MouseEvent e){
                setPressed(false);
            }
        };
        addMouseListener(mlst);

        ButtonGroup bg=new ButtonGroup();
        setLayout(new java.awt.GridLayout(1, 2));
        for(int i=0; i<2; i++){
            javax.swing.AbstractButton btn=createButton(i);
            btn.setPreferredSize(BT_DIMENSION);
            //btn.setMinimumSize(BT_DIMENSION);
            //btn.setMaximumSize(BT_DIMENSION);
            bg.add(btn);
            //btn.setActionCommand(BT_NAMES[i]);
            addListeners(btn,i);
            //btn.addChangeListener(lst);
            //btn.addMouseListener(mlst);
            btn.setFocusPainted(false);
            add(btn);
            buttons[i]=btn;
        }
        
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

    
    /*protected void addListeners(javax.swing.AbstractButton btn, int iIndex){
        ChangeListener lst=new ChangeListener(){
            public void stateChanged(ChangeEvent ev){
                
                String c=(ev.getSource() instanceof javax.swing.AbstractButton)?((javax.swing.AbstractButton)ev.getSource()).getActionCommand():"";
                for(int i=0; i<2; i++){
                    if(BT_NAMES[i].equals(c)){
                        setDirection(i);
                        break;
                    }
                }
            }
        };
        btn.addChangeListener(lst);

        MouseListener mlst=new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                setPressed(true);
                requestFocus();
            }
            public void mouseReleased(MouseEvent e){
                setPressed(false);
            }
        };
        btn.addMouseListener(mlst);
        
        btn.setActionCommand(BT_NAMES[iIndex]);
    }*/
    
    protected void setPressed(boolean b){
        bPressed=b;
    }
    
    protected javax.swing.AbstractButton createButton(int i){
        JButton bt=new JButton(new ImageIcon(getClass().getResource("/edu/xtec/resources/icons/"+BT_NAMES[i]+".gif")));
        if (i==0) bt.setToolTipText("Rotar esquerra");
        else bt.setToolTipText("Rotar dreta");
        return bt;
    }
    
    public int getDirection() {
        return direction;
    }
    
    public void setDirection(int value) {
        direction=value;
        firePropertyChange(PROPERTY, direction+1, direction);
    }

        
    /*public void setDirection(int value) {
        final int v=value;
        Runnable r=new Runnable(){
            public void run(){
                direction=v;
                while (bPressed && direction==v){
                    try{
                        firePropertyChange(PROPERTY, direction+1, direction);
                        Thread.currentThread().sleep(sleepTime);
                    }
                    catch(Exception e){
                    }
                }
            }
        };
        Thread t=new Thread(r);
        t.start();
    }*/
    }

