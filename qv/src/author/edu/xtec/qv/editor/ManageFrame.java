package edu.xtec.qv.editor;

import edu.xtec.qv.editor.ui.MenuManager;
import edu.xtec.qv.editor.ui.UIManager;
import edu.xtec.qv.editor.util.ActionInfo;
import edu.xtec.qv.editor.util.Icons;
import edu.xtec.qv.qti.MetadataProperties;
import edu.xtec.qv.qti.QTIXMLElement;
import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.qv.util.UserConfig;
import edu.xtec.util.Messages;

/*
 * ManageFrame.java
 *
 * Created on 12 de septiembre de 2002, 9:44
 */

/**
 *
 * @author  allastar
 */
public class ManageFrame extends javax.swing.JFrame {
    
    //private LearningDatabaseAdmin db=null; //&
    protected UserConfig uc=null;
    protected MenuManager mm=null;
    protected int iIdUsuari=-1;
    public GrupTreeElement mt=null;
    public static ManageFrame frame=null;
    
    /** Creates new form ManageFrame */
    public ManageFrame() {
        try{
            Messages.init(null,"ca",null,null);
        }
        catch (Exception e){
            System.out.println("EX:"+e);
        }
        frame=this;
        initComponents();
        initComponents2();
        /*try{
            db=new LearningDatabaseAdmin();
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
        }*/ //???????????????????????
        setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());
        //setSize(600,400);
        sPane.setDividerLocation((int)getWidth()/4);
        javax.swing.ImageIcon image=Icons.getIcon("qv_small");
        if (image!=null) setIconImage(image.getImage());
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
        initMessages();
    }
    
    protected void doLogin(){
        iIdUsuari=1;
        initUserConfig(iIdUsuari);
        initMessages();
        mt=new GrupTreeElement(this,iIdUsuari,1);//entitat
        spLeft.setViewportView(mt);
    }
    
    /*protected void doLogin(){
        try{
            iIdUsuari=-1;
            //String sIdUsuari=javax.swing.JOptionPane.showInputDialog(null,Messages.getLocalizedString("IntroUserID"));
            LoginDialog ld=new LoginDialog(this,true);
            ld.show();
            String sIdUsuari=ld.getId();
            if (sIdUsuari!=null){
                iIdUsuari=Integer.parseInt(sIdUsuari);
                String sPwd=ld.getPwd();
                boolean bValidated=db.validateUser(iIdUsuari,sPwd);
                if (bValidated){
                    initUserConfig(iIdUsuari);
                    initMessages();
                    ManageTree mt=new GrupTreeElement(this,iIdUsuari,1);//entitat
                    spLeft.setViewportView(mt);
                }
                else {
                    iIdUsuari=-1;
                    javax.swing.JOptionPane.showMessageDialog(this,"Password erroni!!!","Error",javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
        }
    }*/
    
    protected boolean doLogout(){
        // Retornà un booleà indicant si finalment ha fet el logout.
        int iOpcio=javax.swing.JOptionPane.showConfirmDialog(this,Messages.getLocalizedString("SureExit"),Messages.getLocalizedString("Attention"),javax.swing.JOptionPane.INFORMATION_MESSAGE);
        if (iOpcio==javax.swing.JOptionPane.YES_OPTION){
            //////updateUserConfig(iIdUsuari);
            spLeft.setViewportView(new javax.swing.JPanel());
            spRight.setViewportView(new javax.swing.JPanel());
            iIdUsuari=-1;
            return true;
        }
        else return false;
    }
    
    /*protected boolean doLogout(){
        // Retornà un booleà indicant si finalment ha fet el logout.
        int iOpcio=javax.swing.JOptionPane.showConfirmDialog(this,Messages.getLocalizedString("SureExit"),Messages.getLocalizedString("Attention"),javax.swing.JOptionPane.INFORMATION_MESSAGE);
        if (iOpcio==javax.swing.JOptionPane.YES_OPTION){
            updateUserConfig(iIdUsuari);
            spLeft.setViewportView(new javax.swing.JPanel());
            spRight.setViewportView(new javax.swing.JPanel());
            iIdUsuari=-1;
            return true;
        }
        else return false;
    }*/
    
    protected void initUserConfig(int iUserId){
        //////uc=db.getUserConfig(iUserId);
        if (uc==null) uc=UserConfig.getDefault(iUserId);
        //////System.out.println("Configuració d'usuari:"+uc);
        Messages.setLanguageCode(uc.getLanguage());
        Messages.init(null,uc.getLanguage(),null,null);
        if (uc.getBackGroundColor()!=null) updateBackgroundColor(uc.getBackGroundColor());
        else updateBackgroundColor(new javax.swing.JPanel().getBackground());
        selLookAndFeel(new javax.swing.UIManager.LookAndFeelInfo(uc.getLook(),uc.getLook()));
        CreatorProperties.setIncludeMediaOnView(uc.getIncludeMedia());
    }
    
    protected void updateUserConfig(int iUserId){
        /*String sLang=Messages.getLanguageCode();
        boolean bIncludeMedia=CreatorProperties.getIncludeMediaOnView();
        creator.UserConfig ucNew=new creator.UserConfig(iUserId,sLang,bgColor,sLookName,bIncludeMedia);
        if (uc!=null && !uc.equals(ucNew)) db.setUserConfig(ucNew);*/
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        sPane = new javax.swing.JSplitPane();
        spLeft = new javax.swing.JScrollPane();
        jpRight = new javax.swing.JPanel();
        spRight = new javax.swing.JScrollPane();
        jpActions = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jmUsuari = new javax.swing.JMenu();
        jmiLogin = new javax.swing.JMenuItem();
        jmiLogout = new javax.swing.JMenuItem();

        setTitle("Administraci\u00f3 Quaderns Virtuals");
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        sPane.setLeftComponent(spLeft);

        jpRight.setLayout(new java.awt.BorderLayout());

        jpRight.add(spRight, java.awt.BorderLayout.CENTER);

        jpRight.add(jpActions, java.awt.BorderLayout.NORTH);

        sPane.setRightComponent(jpRight);

        getContentPane().add(sPane, java.awt.BorderLayout.CENTER);

        jmUsuari.setText("Usuari");
        jmiLogin.setText("Login");
        jmiLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiLoginActionPerformed(evt);
            }
        });

        jmUsuari.add(jmiLogin);
        jmiLogout.setText("Logout");
        jmiLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiLogoutActionPerformed(evt);
            }
        });

        jmUsuari.add(jmiLogout);
        jMenuBar1.add(jmUsuari);
        setJMenuBar(jMenuBar1);

        pack();
    }//GEN-END:initComponents
    
    protected void initComponents2(){
        mm=new MenuManager(this);
        /*javax.swing.JMenu jmConfig = new javax.swing.JMenu();
        javax.swing.JMenu jmiLook = new javax.swing.JMenu();
        //javax.swing.JMenu jmiBgColor = new javax.swing.JMenu();
        cm=new ColorMenu(Messages.getLocalizedString("BGColor"));
        javax.swing.JMenu jmLanguage = new javax.swing.JMenu();
         
        jmConfig.setText(Messages.getLocalizedString("Config"));
        jmiLook.setText(Messages.getLocalizedString("Look"));
         
        javax.swing.UIManager.LookAndFeelInfo[] aLF=creator.ui.UIManager.getInstalledLookAndFeels();
        javax.swing.ButtonGroup bgFeel=new javax.swing.ButtonGroup();
        for (int i=0;i<aLF.length;i++){
                final javax.swing.UIManager.LookAndFeelInfo look=aLF[i];
                javax.swing.JCheckBoxMenuItem cbmiLook=new javax.swing.JCheckBoxMenuItem();
                cbmiLook.setText(look.getName());
                //System.out.println("Look:"+look.getName());
                cbmiLook.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        selLookAndFeel(look);
                        }
                });
                jmiLook.add(cbmiLook);
                bgFeel.add(cbmiLook);
                                }
                cm.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        updateBackgroundColor();
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
                        changeLanguage(sLang);
                    }
                });
                jmLanguage.add(jmi);
                bgLanguage.add(jmi);
            }
        }
         
        javax.swing.JMenu jmIncludeMedia = new javax.swing.JMenu();
        jmIncludeMedia.setText(Messages.getLocalizedString("IncludeMedia"));
        javax.swing.ButtonGroup bgInclude=new javax.swing.ButtonGroup();
        javax.swing.JCheckBoxMenuItem cbmiYes=new javax.swing.JCheckBoxMenuItem(Messages.getLocalizedString("Yes"));
        jmIncludeMedia.add(cbmiYes);
        cbmiYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreatorProperties.setIncludeMediaOnView(true);
            }
        });
        bgInclude.add(cbmiYes);
        javax.swing.JCheckBoxMenuItem cbmiNo=new javax.swing.JCheckBoxMenuItem(Messages.getLocalizedString("No"));
        cbmiNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreatorProperties.setIncludeMediaOnView(false);
            }
        });
        jmIncludeMedia.add(cbmiNo);
        bgInclude.add(cbmiNo);
         
         
         
        jmConfig.add(jmiLook);
        //jmConfig.add(jmiBgColor);
        jmConfig.add(cm);
        jmConfig.add(jmIncludeMedia);
        jmConfig.add(jmLanguage);
        jMenuBar1.add(jmConfig);*/
        jMenuBar1.remove(jmUsuari);
    }
    
    public void selLookAndFeel(javax.swing.UIManager.LookAndFeelInfo look){
        try{
            bModifiedUI=true;
            sLookName=look.getClassName();
            //System.out.println("Set look&feel "+look.getName());
            UIManager.setLookAndFeel(look.getClassName());
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
            javax.swing.SwingUtilities.updateComponentTreeUI(jMenuBar1);
            javax.swing.SwingUtilities.updateComponentTreeUI(spLeft);
            invalidate();
            validate();
            //creator.QTIXMLElement.updateLookAndFeel();
        }
        catch (Exception e){
            System.out.println("No es pot establir el Look And Feel "+look.getName());
        }
    }
    
    public void updateBackgroundColor(){
        updateBackgroundColor(mm.getColor());
    }
    
    protected void updateBackgroundColor(java.awt.Color bgColor){
        ManageFrame.bgColor=bgColor;
        CreatorProperties.setComponentColor(mm.getColor());
        CreatorProperties.setBackground(mm.getColor());
        QTIXMLElement.setBackgroundColor(mm.getColor());
        if (spRight.getViewport().getView()!=null) spRight.getViewport().getView().setBackground(bgColor);
        if (spLeft.getViewport().getView()!=null) spLeft.getViewport().getView().setBackground(bgColor);
    }
    
    public static java.awt.Color getBackgroundColor(){
        return bgColor;
    }
    
    public void changeLanguage(String s){
        Messages.setLanguageCode(s);
        MetadataProperties.updateLanguage();
    }
    
    private void jmiLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiLogoutActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_jmiLogoutActionPerformed
    
    public void logoutActionPerformed(){
        doLogout();
    }
    
    public void loginActionPerformed(){
        //////System.out.println("USERID:"+iIdUsuari+"<--");
        boolean bOk=true;
        if (iIdUsuari!=-1){
            if (doLogout()) doLogin();
        }
        else doLogin();
    }
    
    private void jmiLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiLoginActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_jmiLoginActionPerformed
    
/*    public LearningDatabaseAdmin getDb(){
        return db;
    }
*/    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        boolean bLogout=doLogout();
        System.out.println("bLogout?"+bLogout);
        if (bLogout) System.exit(0);
    }//GEN-LAST:event_exitForm
    
    public void setInfoPanel(javax.swing.JPanel jpInfo){
        if (jpInfo==null) return;
        if (bModifiedUI){
            String UIName=jpInfo.getUIClassID();
            if (sLookName!=null && !sLookName.equals(UIName)){
                //System.out.println("Tenia "+UIName+" i li poso "+sLookName);
                try{
                    javax.swing.SwingUtilities.updateComponentTreeUI(jpInfo);
                }
                catch (Exception e){}
            }
        }
        if (bgColor!=null){
            jpInfo.setBackground(bgColor);
        }
        spRight.setViewportView(jpInfo);
        jpActions.removeAll();
    }
    
    public void setActionInfo(java.util.Vector vActionInfo){
        try{
            if (vActionInfo!=null){
                //System.out.println("hi ha "+vActionInfo.size()+" botons.");
                javax.swing.JPanel jpAction=new javax.swing.JPanel();
                //jpAction.setLayout(new java.awt.GridLayout(1,vActionInfo.size()));
                jpAction.setLayout(new java.awt.FlowLayout());
                java.util.Enumeration e=vActionInfo.elements();
                while (e.hasMoreElements()){
                    ActionInfo ai=(ActionInfo)e.nextElement();
                    javax.swing.JButton bt=new javax.swing.JButton(/*ai.getName()*/);
                    bt.setPreferredSize(new java.awt.Dimension(54,30));
                    bt.setToolTipText(ai.getName());
                    if (ai.getIcon()!=null) bt.setIcon(ai.getIcon());
                    else bt.setText(ai.getName());
                    bt.setEnabled(ai.isEnabled());
                    bt.addActionListener(ai.getAction());
                    jpAction.add(bt);
                }
                //jpAction.setPreferredSize(new java.awt.Dimension(400,100));
                jpAction.setVisible(true);
                
                jpActions.invalidate();
                jpActions.removeAll();
                jpActions.add(jpAction,java.awt.BorderLayout.NORTH);
                //jpActions.doLayout();
                //jpActions.setVisible(true);
                jpActions.repaint(0);
                repaint();
                jpActions.validate();
                validate();
            }
            else{
                invalidate();
                jpActions.removeAll();
                validate();
            }
        }
        catch (Exception e){}
    }

/**
 * @param args the command line arguments
 */
public static void main(String args[]) {
    UIManager.initializeLookAndFeels();
    
        /*try {
            UIManager.setLookAndFeel("com.oyoaha.swing.plaf.oyoaha.OyoahaLookAndFeel");
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
        }*/
    ManageFrame mf=new ManageFrame();
    mf.show();
    mf.doLogin();
}

protected void initMessages(){
    setTitle(Messages.getLocalizedString("VirtualNotebookManage"));
        /*jmUsuari.setText(Messages.getLocalizedString("User"));
        jmiLogin.setText(Messages.getLocalizedString("Login"));
        jmiLogout.setText(Messages.getLocalizedString("Logout"));*/
}

public void setBackground(java.awt.Color c){
    super.setBackground(c);
    if (spRight!=null) spRight.setBackground(c);
    if (spLeft!=null) spLeft.setBackground(c);
    if (sPane!=null) sPane.setBackground(c);
    //if (jmiLogout!=null) jmiLogout.setBackground(c);
    //if (jmUsuari!=null) jmUsuari.setBackground(c);
    //if (jmiLogin!=null) jmiLogin.setBackground(c);
    //if (jMenuBar1!=null) jMenuBar1.setBackground(c);
}

public MenuManager getMenuManager(){
    return mm;
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jpRight;
    private javax.swing.JScrollPane spRight;
    private javax.swing.JScrollPane spLeft;
    private javax.swing.JSplitPane sPane;
    private javax.swing.JMenuItem jmiLogout;
    private javax.swing.JMenu jmUsuari;
    private javax.swing.JMenuItem jmiLogin;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jpActions;
    // End of variables declaration//GEN-END:variables
    
    private boolean bModifiedUI=false;
    private String sLookName="";
    //private ColorMenu cm=null;
    private static java.awt.Color bgColor=null;
}