package edu.xtec.qv.editor.ui;

import edu.xtec.qv.editor.ManageFrame;
import edu.xtec.qv.editor.ManageToolConfig;
import edu.xtec.qv.editor.util.ColorMenu;
import edu.xtec.qv.editor.util.FileHistory;
import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.util.Messages;

/*
 * MenuManager.java
 *
 * Created on 2 / desembre / 2002, 09:08
 */

/**
 *
 * @author  allastar
 */
public class MenuManager {
    
    protected javax.swing.JMenuBar jmb;
    protected javax.swing.JToolBar jtb;
    protected ManageFrame mf;
    
    /** Creates a new instance of MenuManager */
    public MenuManager(ManageFrame mf) {
        this.mf=mf;
        initComponents2();
        initMessages();
        
    }
    
    public java.awt.Color getColor(){
        return cm.getColor();
    }
    
    public void setSaveEnabled(boolean b){
        //jmiSaveNoteBook.setEnabled(b);
    }
    
    public void setSaveAsEnabled(boolean b){
        //jmiSaveAsNoteBook.setEnabled(b);
    }
    
    protected void initComponents2(){
        jmb=new javax.swing.JMenuBar();
        jtb=new javax.swing.JToolBar();

        jmFile=new javax.swing.JMenu();
        jmFile.addSeparator();
        jmFile.addItemListener(new java.awt.event.ItemListener(){
            public void itemStateChanged(java.awt.event.ItemEvent e){
                jmFile.removeAll();
                jmFile.addSeparator();
                String [] recentFileNames=FileHistory.getRecentFileNames();
                for (int i=0;i<recentFileNames.length && recentFileNames[i]!=null;i++){
                    //System.out.println("name="+recentFileNames[i]);
                    javax.swing.JMenuItem jmi=new javax.swing.JMenuItem(i+". "+recentFileNames[i]);
                    final int pos=i;
                    jmi.addActionListener(new java.awt.event.ActionListener(){
                        public void actionPerformed(java.awt.event.ActionEvent e){
                            String s=FileHistory.getFilePath(pos);
                            //System.out.println("open("+s+")");
                            try{
                                mf.mt.mts.mtq.select(null);
                                mf.mt.mts.mtq.open(new java.io.File(s));
                                ////mf.mt.mtq.select(null);
                                ////mf.mt.mtq.open(new java.io.File(s));
                            }
                            catch (Exception ex){
                                ex.printStackTrace(System.out);
                            }
                        }
                    });
                    jmFile.add(jmi);
                }
            }
        });
        /*jmiNewNoteBook=new javax.swing.JMenuItem();
        jmiOpenNoteBook=new javax.swing.JMenuItem();
        jmiSaveNoteBook=new javax.swing.JMenuItem();
        jmiSaveAsNoteBook=new javax.swing.JMenuItem();
        jmFile.add(jmiNewNoteBook);
        jmFile.add(jmiOpenNoteBook);
        jmFile.add(jmiSaveNoteBook);
        jmFile.add(jmiSaveAsNoteBook);*/
        jmb.add(jmFile);

        jmUsuari = new javax.swing.JMenu();
        //////jmiLogin = new javax.swing.JMenuItem();
        jmiLogout = new javax.swing.JMenuItem();
        
        jmUsuari.setText("Usuari");
        /*jmiLogin.setText("Login");
        jmiLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mf.loginActionPerformed();
            }
        });

        jmUsuari.add(jmiLogin);
        jmiLogout.setText("Logout");*/
        jmiLogout.setText("Exit");
        jmiLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mf.logoutActionPerformed();
            }
        });

        jmUsuari.add(jmiLogout);
        ////jmb.add(jmUsuari);

        
        jmConfig=new javax.swing.JMenu();
        jmiLook=new javax.swing.JMenu();
        //javax.swing.JMenu jmiBgColor = new javax.swing.JMenu();
        cm=new ColorMenu(Messages.getLocalizedString("BGColor"));
        jmLanguage=new javax.swing.JMenu();
        
        jmConfig.setText(Messages.getLocalizedString("Config"));
        jmiLook.setText(Messages.getLocalizedString("Look"));

        javax.swing.UIManager.LookAndFeelInfo[] aLF=UIManager.getInstalledLookAndFeels();
       	javax.swing.ButtonGroup bgFeel=new javax.swing.ButtonGroup();
       	for (int i=0;i<aLF.length;i++){
       		final javax.swing.UIManager.LookAndFeelInfo look=aLF[i];
       		javax.swing.JCheckBoxMenuItem cbmiLook=new javax.swing.JCheckBoxMenuItem();
       		cbmiLook.setText(look.getName());
       		//System.out.println("Look:"+look.getName());
       		cbmiLook.addActionListener(new java.awt.event.ActionListener() {
       			public void actionPerformed(java.awt.event.ActionEvent evt) {
     					mf.selLookAndFeel(look);
         		}
       		});
       		jmiLook.add(cbmiLook);
       		bgFeel.add(cbmiLook);
				}
     		cm.addActionListener(new java.awt.event.ActionListener() {
     			public void actionPerformed(java.awt.event.ActionEvent evt) {
   					mf.updateBackgroundColor();
       		}
     		});

        //jmiBgColor.setText("Color de fons");
        javax.swing.ButtonGroup bgLanguage=new javax.swing.ButtonGroup();
        jmLanguage.setText(Messages.getLocalizedString("Language"));
        String sLanguages=ManageToolConfig.getProperty("languages");
        if (sLanguages!=null){
            java.util.StringTokenizer st=new java.util.StringTokenizer(sLanguages,",");
            while (st.hasMoreTokens()){
                final String sLang=st.nextToken();
                javax.swing.JCheckBoxMenuItem jmi=new javax.swing.JCheckBoxMenuItem(Messages.getLocalizedString(sLang));
                jmi.addActionListener(new java.awt.event.ActionListener(){
                    public void actionPerformed(java.awt.event.ActionEvent e){
                        Messages.init(null,sLang,null,null);
                        mf.changeLanguage(sLang);
                    }
                });
                jmLanguage.add(jmi);
                bgLanguage.add(jmi);
            }
        }
        
        jmIncludeMedia = new javax.swing.JMenu();
        jmIncludeMedia.setText(Messages.getLocalizedString("IncludeMedia"));
        javax.swing.ButtonGroup bgInclude=new javax.swing.ButtonGroup();
        cbmiYes=new javax.swing.JCheckBoxMenuItem(Messages.getLocalizedString("Yes"));
        jmIncludeMedia.add(cbmiYes);
        cbmiYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreatorProperties.setIncludeMediaOnView(true);
            }
       	});
        bgInclude.add(cbmiYes);
        cbmiNo=new javax.swing.JCheckBoxMenuItem(Messages.getLocalizedString("No"));
        cbmiNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreatorProperties.setIncludeMediaOnView(false);
            }
       	});
        jmIncludeMedia.add(cbmiNo);
        bgInclude.add(cbmiNo);

        javax.swing.JMenuItem jmiTestConfig=new javax.swing.JMenuItem();
        jmiTestConfig.setText("Configuració d'assignacions");
        jmiTestConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new ConfigQuadernTestDialog(mf,true).show();
            }
        });

        
        
        jmConfig.add(jmiLook);
        //jmConfig.add(jmiBgColor);
        jmConfig.add(cm);
        jmConfig.add(jmIncludeMedia);
	jmConfig.add(jmLanguage);
        jmConfig.add(jmiTestConfig);
        jmb.add(jmConfig);
            
        mf.setJMenuBar(jmb);
    }
 
    protected void initMessages(){
        jmUsuari.setText(Messages.getLocalizedString("User"));
        //////jmiLogin.setText(Messages.getLocalizedString("Login"));
        jmiLogout.setText(Messages.getLocalizedString("Logout"));
        jmIncludeMedia.setText(Messages.getLocalizedString("IncludeMedia"));
        jmConfig.setText(Messages.getLocalizedString("Config"));
        jmiLook.setText(Messages.getLocalizedString("Look"));
        jmLanguage.setText(Messages.getLocalizedString("Language"));
        cbmiYes.setText(Messages.getLocalizedString("Yes"));
        cbmiNo.setText(Messages.getLocalizedString("No"));
        jmFile.setText(Messages.getLocalizedString("File"));
        /*jmiNewNoteBook.setText(Messages.getLocalizedString("CreateNotebook"));
        jmiOpenNoteBook.setText(Messages.getLocalizedString("OpenNotebook"));
        jmiSaveNoteBook.setText(Messages.getLocalizedString("Save"));
        jmiSaveAsNoteBook.setText(Messages.getLocalizedString("SaveAs"));*/

    }
        
    private javax.swing.JMenu jmUsuari;
    private javax.swing.JMenuItem jmiLogin;
    private javax.swing.JMenuItem jmiLogout;
    private javax.swing.JMenu jmIncludeMedia;
    private javax.swing.JMenu jmConfig;
    private javax.swing.JMenu jmiLook;
    private javax.swing.JMenu jmLanguage;
    private javax.swing.JCheckBoxMenuItem cbmiYes;
    private javax.swing.JCheckBoxMenuItem cbmiNo;
    private javax.swing.JMenu jmFile;
    /*private javax.swing.JMenuItem jmiNewNoteBook;
    private javax.swing.JMenuItem jmiOpenNoteBook;
    private javax.swing.JMenuItem jmiSaveNoteBook;
    private javax.swing.JMenuItem jmiSaveAsNoteBook;*/
    
    
    
    
    private ColorMenu cm=null;
}
