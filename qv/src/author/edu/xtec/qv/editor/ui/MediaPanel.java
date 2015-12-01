package edu.xtec.qv.editor.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.xtec.qv.editor.ManageFrame;
import edu.xtec.qv.qti.QTIMaterial;
import edu.xtec.qv.qti.QTIMultimediaMaterial;
import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.qv.util.Locator;
import edu.xtec.qv.util.Utility;
import edu.xtec.util.Messages;

public class MediaPanel extends javax.swing.JPanel{
    
        /*public static final int IMAGE=1;
        public static final int AUDIO=2;
        public static final int VIDEO=3;
        public static final int APPLET=4;*/
    static String[] sAudioExtensions=new String[]{"wav","mp3"};
    static String[] sVideoExtensions=new String[]{"mpg","mov","mpeg","avi"};
    static String[] sImageExtensions=new String[]{"jpg","jpeg","gif","bmp","png"};
    static String[] sAppletExtensions=new String[]{"class","jar"};
    static String[] sFlashExtensions=new String[]{"swf"};
    
    protected boolean includeView=false;
    protected boolean loadMediaOnOpen=false;
    protected JPanel jpNorth;
    protected JPanel jpCenter;
    protected JButton btBrowse;
    protected MediaSubPanel msp;
    protected static JFileChooser jfc;
    protected java.awt.Color bgColor=null;
    
    protected QTIMultimediaMaterial qtiMultMat;
    protected DimensionPanel dp;
    
    int screenWidth=400;
    int screenHeight=400;
    JTextField tfText;
    JLabel lbInfo;
    static String directory=".";
    
    public MediaPanel(QTIMultimediaMaterial qtiMultMat){
        this(null,"","",qtiMultMat);
    }
    
    public MediaPanel(String sFileName, String sWidth, String sHeight, QTIMultimediaMaterial qtiMultMat){
        this.qtiMultMat=qtiMultMat;
        initComponents();
        dp.setMediaWidth(sWidth);
        dp.setMediaHeight(sHeight);
        if (sFileName!=null) tfText.setText(sFileName);
        setVisible(true);
        bgColor=CreatorProperties.getBackground();
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
        
        loadMediaOnOpen=CreatorProperties.getLoadMediaOnOpen();
        includeView=CreatorProperties.getIncludeMediaOnView();
        
        if (sFileName!=null && sFileName.trim().length()>0){
            open(sFileName,true);
        }
        initMessages();
    }
    
    public int getMediaWidth(){
        if (dp!=null) return dp.getMediaWidth();
        else return -1;
    }
    
    public void setMediaWidth(String sWidth){
        if (dp!=null) dp.setMediaWidth(sWidth);
    }
    
    public int getMediaHeight(){
        if (dp!=null) return dp.getMediaHeight();
        else return -1;
    }
    
    public void setMediaHeight(String sHeight){
        if (dp!=null) dp.setMediaHeight(sHeight);
    }
    
    protected void initComponents(){
        setLayout(new BorderLayout());
        tfText=new JTextField();
        jpNorth=new JPanel();
        btBrowse=new JButton("Cerca");
        btBrowse.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String fileName=getFileName();
                if (fileName==null) return;
                tfText.setText(fileName);
                open(fileName);
            }
        });
        tfText.setColumns(25);
        lbInfo=new JLabel();
        lbInfo.setPreferredSize(new Dimension(100,20));
        lbInfo.setMinimumSize(new Dimension(100,20));
        tfText.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                open(tfText.getText());
                jpCenter.invalidate();
                jpCenter.validate();
            }
        });
        jpCenter=new JPanel();
        jpCenter.setLayout(new BorderLayout());
        //jpNorth.add(lbInfo);
        jpNorth.add(tfText);
        jpNorth.add(btBrowse);
        
        /*getContentPane().*/add(jpNorth,BorderLayout.NORTH);
        dp=new DimensionPanel();
        if (qtiMultMat.getMaterialType()!=QTIMaterial.AUDIO) jpCenter.add(dp,java.awt.BorderLayout.EAST);
        
        add(jpCenter,BorderLayout.CENTER);
    }
    
    public String getMaterialURI(){
        if (tfText.getText()!=null) return tfText.getText().replace('\\','/');
        return tfText.getText();
    }
    
    protected void open(String fileName){
        open(fileName,false);
    }
    
    protected void open(String fileName, boolean isCreatingPanel){
        int fileType=getFileType(fileName);
        jpCenter.removeAll();
        if (fileType!=QTIMaterial.AUDIO) jpCenter.add(dp,java.awt.BorderLayout.EAST);
        if (includeView){
            msp=new MediaSubPanel(this,fileName);
            jpCenter.add(msp,BorderLayout.CENTER);
            java.awt.Dimension d=msp.getSize();
            if (d!=null){
                if (d.getWidth()>0) dp.setMediaWidth(d.getWidth()+"");
                if (d.getHeight()>0) dp.setMediaHeight(d.getHeight()+"");
            }
            
        }
        else{
            ///DETECCIÓ AUTOMÀTICA DE TIPUS DE MEDIA////qtiMultMat.setMaterialType(fileType);
            if (fileType!=QTIMaterial.AUDIO){ //no view
                JButton btView=new JButton(Messages.getLocalizedString("Preview"));
                if (fileType==QTIMaterial.IMAGE){
                    int size=150;
                    int min=Math.min(getWidth(),getHeight());
                    size=(min>size)?min:size;  //Màxima amplada que hi cap
                    if (loadMediaOnOpen){
                        Image i=loadImage(fileName);
                        int width=i.getWidth(this);
                        int height=i.getHeight(this);
                        int max=Math.max(width,height);
                        if (max>size){
                            double factor=max/size;
                            width=(int)(width/factor);
                            height=(int)(height/factor);
                        }
                        else{
                            if (width<1){
                                width=size;
                                height=size;
                            }
                        }
                        i=i.getScaledInstance(width,height,Image.SCALE_AREA_AVERAGING);
                        if (dp!=null){
                            dp.setMediaWidth(""+width);
                            dp.setMediaHeight(""+height);
                            //System.out.println("-----------------------------width:"+width+" height:"+height);
                        }
                        //else System.out.println("dp=null!! --------------------------width:"+width+" height:"+height); 
                        ImageIcon ii=new ImageIcon();
                        ii.setImage(i);
                        btView=new JButton(ii);
                    }
                    else btView=new JButton(Messages.getLocalizedString("Preview"));
                    btView.setBackground(bgColor);
                    //
                    
                }
                //else if (fileType==creator.QTIMaterial.VIDEO){ //Video
                else if (fileType==QTIMaterial.VIDEO || fileType==QTIMaterial.FLASH){
                    btView=new JButton(Messages.getLocalizedString("Preview"));
                    btView.setBackground(bgColor);
                    if (!isCreatingPanel && loadMediaOnOpen) new MediaFrame(this,fileName);
                }
                    /*else{ //Applet
                        btView=new JButton("Preview");
                        btView.setBackground(bgColor);
                        if (!isCreatingPanel && loadMediaOnOpen) new MediaFrame(this,fileName);
                    }*/
                final String fName=fileName;
                btView.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        if (tfText.getText()!=null && tfText.getText().trim().length()>0){
                            //open(tfText.getText());
                            new MediaFrame(MediaPanel.this,fName);
                        }
                    }
                });
                //jpCenter.removeAll();
                invalidate();
                jpCenter.add(btView);
                validate();
                
            }
            else /*if (fileType==AUDIO)*/{ //No és imatge
                invalidate();
                msp=new MediaSubPanel(this,fileName,false);
                jpCenter.add(msp,BorderLayout.CENTER);
                validate();
                
            }
            //else new MediaFrame(fileName);
        }
    }
    
    public static int getFileType(String fileName){
        int iType=-1;
        boolean bFound=false;
        for (int i=0;i<sImageExtensions.length && !bFound;i++)
            if (fileName.endsWith(sImageExtensions[i]))bFound=true;
        if (bFound) return(QTIMaterial.IMAGE);
        for (int i=0;i<sAudioExtensions.length && !bFound;i++)
            if (fileName.endsWith(sAudioExtensions[i]))bFound=true;
        if (bFound) return(QTIMaterial.AUDIO);
        for (int i=0;i<sVideoExtensions.length && !bFound;i++)
            if (fileName.endsWith(sVideoExtensions[i]))bFound=true;
        if (bFound) return(QTIMaterial.VIDEO);
        for (int i=0;i<sAppletExtensions.length && !bFound;i++)
            if (fileName.endsWith(sAppletExtensions[i]))bFound=true;
        if (bFound) return(QTIMaterial.APPLET);
        for (int i=0;i<sFlashExtensions.length && !bFound;i++)
            if (fileName.endsWith(sFlashExtensions[i]))bFound=true;
        if (bFound) return(QTIMaterial.FLASH);
        else return QTIMaterial.IMAGE;
    }
    
    public void setMaterialType(int iMaterialType){
        qtiMultMat.setMaterialType(iMaterialType);
    }
    
    String getFileName(){
        if (jfc==null) jfc=new JFileChooser(directory);
        //jfc.setFileHidingEnabled(false);
        jfc.showOpenDialog(ManageFrame.frame);
        if (jfc.getSelectedFile()==null) return null;
        java.io.File f=jfc.getSelectedFile();
        directory=f.getParent();
        String file=f.getName();
        //System.out.println("Ha triat:"+f.getPath());
        String sRelativePath=Locator.getRelativePath(f);
        //System.out.println("Relative:"+sRelativePath);
        return sRelativePath;
        ////return jfc.getSelectedFile().getPath();
    }
    
    public Image loadImage(String fileName){
        Image i=Utility.loadImage(fileName,this);
        return i;
        /*System.out.println("loadImage:"+fileName+"<--");
        Image i=null;
        try{
            if (fileName.indexOf("//")>0){
                java.net.URL u=new java.net.URL(fileName);
                i=Toolkit.getDefaultToolkit().getImage(u);
            }
            else i=Toolkit.getDefaultToolkit().getImage(creator.Locator.getAbsolutePath(fileName));//new java.net.URL(img_url));
            
            boolean bFinish=false;
            for (int j=0;j<6 && !bFinish ;j++){
                Thread.currentThread().sleep(500);
                if (i.getWidth(this)>0) bFinish=true;
            }
        }
        catch(Exception e){
        }
        return i;*/
    }
    
    public void setBackground(java.awt.Color c){
        super.setBackground(c);
        if (jpNorth!=null) jpNorth.setBackground(c);
        if (jpCenter!=null) jpCenter.setBackground(c);
        if (msp!=null) msp.setBackground(c);
        java.awt.Color componentColor=CreatorProperties.getComponentColor();
        if (componentColor!=null){
            if (btBrowse!=null) btBrowse.setBackground(componentColor);
        }
    }
    
    
    public class MediaFrame extends JFrame{
        public MediaFrame(MediaPanel mp, String fileName){
            final MediaSubPanel jp=new MediaSubPanel(mp,fileName);
            getContentPane().add(jp,BorderLayout.CENTER);
            setSize(jp.getSize());
            setVisible(true);
            addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e){
                    jp.controllerClosed();
                }
            });
            show();
        }
    }
    
    protected void initMessages(){
        btBrowse.setText(Messages.getLocalizedString("Search"));
    }
    
        /*public static void main(String[] args){
                MediaPanel mp=new MediaPanel();
                mp.setVisible(true);
                System.out.println("Fi");
        }*/
    
}