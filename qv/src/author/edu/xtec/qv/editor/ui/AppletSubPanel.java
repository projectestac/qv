/*
 * AppletSubPanel.java
 *
 * Created on 24 / octubre / 2002, 09:13
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.editor.util.AppletPreviewerStub;
import edu.xtec.qv.qti.util.CreatorProperties;

/**
 *
 * @author  allastar
 */
public class AppletSubPanel extends javax.swing.JPanel {
    
    protected AppletPanel appletPanel;
    protected String sClassPath;
    protected String sMainClass;
    protected String sParams;
    
    /** Creates new form AppletSubPanel */
    public AppletSubPanel(AppletPanel appletPanel, String sClassPath, String sMainClass, String sParams, int width, int height) {
        this.appletPanel=appletPanel;
        this.sClassPath=sClassPath;
        this.sMainClass=sMainClass;
        this.sParams=sParams;
        setSize(width,height);
        initComponents();
        openApplet(sClassPath,sMainClass,sParams);
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
    }

    protected void openApplet(String sClassPath, String sMainClass, String sParams){
        if (sClassPath==null || sMainClass==null || sMainClass.trim().length()<1) return;
        try{
            java.net.URL[] urls=getURLArrayFrom(sClassPath);
            java.net.URLClassLoader ucl=new java.net.URLClassLoader(urls);
            Class c=ucl.loadClass(sMainClass);
            //System.out.println("-1-");
            if (c!=null){
                Object o=c.newInstance();
                if (o instanceof java.applet.Applet){
                    java.applet.Applet ap=(java.applet.Applet)o;
                    java.util.HashMap hmParams=getHashMapParamsFrom(sParams);
                    hmParams.put("tip","true");
                    hmParams.put("requestFocus","true");
                    AppletPreviewerStub aps=new AppletPreviewerStub(null/*ap.getAppletContext()*/,hmParams);
                    ap.setStub(aps);
                    javax.swing.JFrame jf=new javax.swing.JFrame();
                    jf.getContentPane().add(ap,java.awt.BorderLayout.CENTER);
                    //jf.show();
                    ap.init();
                    ap.start();
                    ap.repaint(0);
                }
                //System.out.println("-2-");
                removeAll();
                if (o instanceof java.awt.Panel){
                    //System.out.println("-3-");
                    java.awt.Panel p=(java.awt.Panel)o;                        
                    add(p,java.awt.BorderLayout.CENTER);
                }
            }
            else System.out.println("No trobo la classe");
        }
        catch (Exception e){
            System.out.println("Excepci�:"+e);
            e.printStackTrace(System.out);
        }
    }
    
    protected java.net.URL[] getURLArrayFrom(String sClassPath){
        java.util.Vector vUrls=new java.util.Vector();
        java.util.StringTokenizer st=new java.util.StringTokenizer(sClassPath,",");
        while (st.hasMoreTokens()){
            String sUrl=st.nextToken();
            java.net.URL url=null;
            try{
                if (sUrl.trim().startsWith("http://")){
                    url=new java.net.URL(sUrl);
                }
                else{
                    url=new java.net.URL("file:/"+sUrl);
                }
                vUrls.add(url);
            }
            catch (Exception e){
                System.out.println("Url inv�lida:"+e);
            }
        }
        java.net.URL[] urls=(java.net.URL[])vUrls.toArray(new java.net.URL[vUrls.size()]);
        return urls;
    }
    
    protected java.util.HashMap getHashMapParamsFrom(String sParams){
        java.util.HashMap hmParams=new java.util.HashMap();
        java.util.StringTokenizer st=new java.util.StringTokenizer(sParams,";");
        while (st.hasMoreTokens()){
            String sParam=st.nextToken();
            java.util.StringTokenizer st2=new java.util.StringTokenizer(sParam,"=");
            if (st2.hasMoreTokens()){
                String sKey=st2.nextToken();
                String sValue=(st2.hasMoreTokens())?st2.nextToken():"";
                hmParams.put(sKey,sValue);
            }
        }
        return hmParams;
    }
            
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        
        setLayout(new java.awt.BorderLayout());
        
    }//GEN-END:initComponents
    
    public void setBackground(java.awt.Color c){
			super.setBackground(c);
			if (appletPanel!=null) appletPanel.setBackground(c);
		}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}