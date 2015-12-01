/*
 * MediaSubPanel.java
 *
 * Created on 23 / octubre / 2002, 09:03
 */

package edu.xtec.qv.editor.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;

import javax.media.Controller;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerErrorEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.RealizeCompleteEvent;

import edu.xtec.qv.qti.QTIMaterial;
import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.qv.util.Locator;
import edu.xtec.qv.util.Utility;

/**
 *
 * @author  allastar
 */

public class MediaSubPanel extends javax.swing.JPanel implements ControllerListener{
    
    /** Creates a new instance of MediaSubPanel */
    
    Player player=null;
    Player newPlayer=null;
    boolean bStart;
    Component visualComponent=null;
    Component controllerComponent=null;
    Component mediaImage=null;
    //JLabel lbInfo;
    MediaPanel mp;
    
    public MediaSubPanel(MediaPanel mp, String fileName){
        this(mp,fileName,true);
        //initComponents2();
    }
    
    public MediaSubPanel(MediaPanel mp, String fileName, boolean bStart){
        this.mp=mp;
        this.bStart=bStart;
        setLayout(new BorderLayout());
        int i=MediaPanel.getFileType(fileName);
        if (mp!=null) mp.setMaterialType(i);
        
        if (fileName!=null && fileName.trim().length()>0) fileName=Locator.getAbsolutePath(fileName);///#
        //System.out.println("fileName:"+fileName);
        
        if (i==QTIMaterial.IMAGE){
            openImage(fileName);
        }
                /*else if (i==creator.QTIMaterial.APPLET){
                    openApplet(fileName);
                }*/
        else{
            openMultimedia(fileName,bStart);
            setSize(400,400);
        }
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
        setVisible(true);
    }
    
    protected void openMultimedia(String fileName, boolean bStart){
        //System.out.println("openMultimedia");
        if (fileName==null) return;
        
        newPlayer=createPlayer(fileName);
        boolean closingPlayer=false;
        if (player!=null){
            closingPlayer=true;
            player.close();
        }
        if (newPlayer==null) return;
        if (mediaImage!=null) remove(mediaImage);
        if (!closingPlayer){
            invalidate();
            player=newPlayer;
            player.addControllerListener(MediaSubPanel.this);
            //lbInfo.setText("Carregant...");
            player.realize();
            validate();//
        }
        //System.out.println("-openMultimedia");
    }
    
    protected void openImage(String fileName){
        if (fileName==null) return;
        newPlayer=null;
        boolean closingPlayer=false;
        if (player!=null){
            closingPlayer=true;
            player.close();
        }
        if (visualComponent!=null) remove(visualComponent);
        if (controllerComponent!=null) remove(controllerComponent);
        if (mediaImage!=null) remove(mediaImage);
        
        //&Image i=mp.loadImage(fileName);
        Image i=Utility.loadImage(fileName,this);
        if (i!=null){
            mp.setMediaWidth(i.getWidth(this)+"");
            mp.setMediaHeight(i.getHeight(this)+"");
        }
        mediaImage=new MediaImage(i);
        setSize(mediaImage.getSize());
        invalidate();
        /*getContentPane().*/add(mediaImage,BorderLayout.CENTER);
        validate();
        setSize(mediaImage.getSize());
    }
    
    Player createPlayer(String fileName){
        Player newPlayer;
        try{
            //System.out.println("-createPlayer-");
            MediaLocator locator;
            if (!fileName.startsWith("http://") && fileName.indexOf(":/")<0 && fileName.indexOf(":\\")<0) locator=new MediaLocator("file:"+fileName);
            else locator=new MediaLocator(fileName);
            if (locator==null) return null;
            newPlayer=Manager.createPlayer(locator);
            //  System.out.println("-CreatePlayer");
        }
        catch(Exception e){
            //lbInfo.setText(e.toString());
            return null;
        }
        return newPlayer;
    }
    
    void realizeComplete(){
        //System.out.println("realizeComplete");
        visualComponent=player.getVisualComponent();
        controllerComponent=player.getControlPanelComponent();
        if (visualComponent!=null)
            /*getContentPane().*/add(visualComponent,BorderLayout.CENTER);
        if (controllerComponent!=null)
            /*getContentPane().*/add(controllerComponent,BorderLayout.SOUTH);
        validate();
        player.prefetch();
        //System.out.println("-realizeComplete");
    }
    
    void prefetchComplete(){
        //System.out.println("prefetchComplete");
        //lbInfo.setText("");
        if (player.getTargetState()!=Controller.Started)
            if (bStart) player.start();
        //System.out.println("-prefetchComplete");
    }
    
    void controllerError(){
        //System.out.println("controllerError");
        player.close();
        if (visualComponent!=null) remove(visualComponent);
        if (controllerComponent!=null) remove(controllerComponent);
        if (mediaImage!=null) remove(mediaImage);
        validate();
        visualComponent=null;
        controllerComponent=null;
        player.removeControllerListener(this);
        player=null;
        //System.out.println("-controllerError");
    }
    
    void controllerClosed(){
        //System.out.println("controllerClosed");
        if (visualComponent!=null) remove(visualComponent);
        if (controllerComponent!=null) remove(controllerComponent);
        if (mediaImage!=null) remove(mediaImage);
        player=null;
        System.gc();
        System.runFinalization();
        if (newPlayer!=null){
            player=newPlayer;
            newPlayer=null;
            player.addControllerListener(this);
            //lbInfo.setText("carregant...");
            player.realize();
        }
        //validate();
        //System.out.println("-controllerClosed");
    }
    
    public synchronized void controllerUpdate(ControllerEvent e){
        //System.out.println("controllerUpdate");
        if (e instanceof RealizeCompleteEvent) realizeComplete();
        else if (e instanceof PrefetchCompleteEvent) prefetchComplete();
        else if (e instanceof ControllerErrorEvent) controllerError();
        else if (e instanceof ControllerClosedEvent) controllerClosed();
        //System.out.println("-controllerUpdate");
    }
    
    public void setBackground(java.awt.Color c){
        super.setBackground(c);
    }
    
    
            /*public java.awt.Dimension getSize(){
                if (controllerComponent!=null && visualComponent!=null){
                    return new Dimension(Math.max(controllerComponent.getWidth(),visualComponent.getWidth())+20,controllerComponent.getHeight()+visualComponent.getHeight()+30);
                }
                else if (mediaImage!=null) return mediaImage.getSize();
                else return super.getSize();
            }
             
            public Dimension getPreferredSize() {
                return getSize();
            }
            public Dimension getMaximumSize() { return getPreferredSize(); }
            public Dimension getMinimumSize() { return getPreferredSize(); }*/
    
}