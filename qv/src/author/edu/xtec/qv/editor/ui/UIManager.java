package edu.xtec.qv.editor.ui;

public class UIManager extends javax.swing.UIManager{

	public static java.util.HashMap hmInstalledSkin=new java.util.HashMap();

	public static void initializeLookAndFeels(){
	  try {
	  	/* Utilitzo reflection perquè el jar de look and feel ocupa bastant i potser no hi serà. */
			Class c=ClassLoader.getSystemClassLoader().loadClass("com.incors.plaf.alloy.AlloyLookAndFeel");
			java.lang.reflect.Method m=c.getDeclaredMethod("setProperty",new Class[]{ClassLoader.getSystemClassLoader().loadClass("java.lang.String"),ClassLoader.getSystemClassLoader().loadClass("java.lang.String")});
			m.invoke(c,new Object[]{"alloy.licenseCode","2003/03/25#a_llas2004@yahoo.es#biu25e#195rzn"});	  
	 	  ////com.incors.plaf.alloy.AlloyLookAndFeel.setProperty("alloy.licenseCode", "2002/11/22#a_llas2003@hotmail.com#21vgf3#14dqaq");
      //com.l2fprod.gui.plaf.skin.SkinLookAndFeel.setSkin(com.l2fprod.gui.plaf.skin.SkinLookAndFeel.loadThemePack("themepack.zip"));
      if (!isInstalledLookAndFeel("com.incors.plaf.alloy.AlloyLookAndFeel"))
      	javax.swing.UIManager.installLookAndFeel("Alloy","com.incors.plaf.alloy.AlloyLookAndFeel");
      if (!isInstalledLookAndFeel("net.sourceforge.mlf.metouia.MetouiaLookAndFeel"))
      	javax.swing.UIManager.installLookAndFeel("Metouia","net.sourceforge.mlf.metouia.MetouiaLookAndFeel");
      if (!isInstalledLookAndFeel("com.oyoaha.swing.plaf.oyoaha.OyoahaLookAndFeel"))
      	javax.swing.UIManager.installLookAndFeel("Oyoaha","com.oyoaha.swing.plaf.oyoaha.OyoahaLookAndFeel");
      //if (!isInstalledLookAndFeel("org.compiere.plaf.CompiereLookAndFeel"))
      //	javax.swing.UIManager.installLookAndFeel("Compiere","org.compiere.plaf.CompiereLookAndFeel");
      if (!isInstalledLookAndFeel("nextlf.plaf.NextLookAndFeel"))
      	javax.swing.UIManager.installLookAndFeel("Next","nextlf.plaf.NextLookAndFeel");
      if (!isInstalledLookAndFeel("org.gtk.java.swing.plaf.gtk.GtkLookAndFeel"))
      	javax.swing.UIManager.installLookAndFeel("Gtk","org.gtk.java.swing.plaf.gtk.GtkLookAndFeel");
      //if (!isInstalledLookAndFeel("com.teknolust.plaf.teknolust.TeknolustLookAndFeel"))
      //	javax.swing.UIManager.installLookAndFeel("Teknolust","com.teknolust.plaf.teknolust.TeknolustLookAndFeel");
      if (!isInstalledLookAndFeel("com.incors.plaf.kunststoff.KunststoffLookAndFeel"))
      	javax.swing.UIManager.installLookAndFeel("Kunststoff","com.incors.plaf.kunststoff.KunststoffLookAndFeel");
      if (!isInstalledLookAndFeel("swing.addon.plaf.threeD.ThreeDLookAndFeel"))
      	javax.swing.UIManager.installLookAndFeel("3D","swing.addon.plaf.threeD.ThreeDLookAndFeel");
      if (!isInstalledLookAndFeel("com.memoire.slaf.SlafLookAndFeel")){
        hmInstalledSkin.put("Slaf_Alien","\\.slafrc_alien");
        hmInstalledSkin.put("Slaf_Children","\\.slafrc_children");
        hmInstalledSkin.put("Slaf_Clown","\\.slafrc_clown");
        hmInstalledSkin.put("Slaf_Ecologic","\\.slafrc_ecologic");
        //hmInstalledSkin.put("Slaf_Gnome","\\.slafrc_gnome");
        hmInstalledSkin.put("Slaf_Gtk","\\.slafrc_gtk");
        hmInstalledSkin.put("Slaf_KDE","\\.slafrc_kde");
        hmInstalledSkin.put("Slaf_Mondrian","\\.slafrc_mondrian");
        hmInstalledSkin.put("Slaf_Sand","\\.slafrc_sand");
        hmInstalledSkin.put("Slaf_Shadow","\\.slafrc_shadow");
        hmInstalledSkin.put("Slaf_tx","\\.slafrc_tx");
        hmInstalledSkin.put("Slaf_X11","\\.slafrc_x11");
      }
      if (!isInstalledLookAndFeel("com.l2fprod.gui.plaf.skin.SkinLookAndFeel")){
      	//javax.swing.UIManager.installLookAndFeel("Skin_themepack","com.l2fprod.gui.plaf.skin.SkinLookAndFeel");
      	hmInstalledSkin.put("Skin_default","themepack.zip");
      	hmInstalledSkin.put("Skin_aqua","aquathemepack.zip");
      	hmInstalledSkin.put("Skin_macOs","macosthemepack.zip");
      	hmInstalledSkin.put("Skin_modern","modernthemepack.zip");
      	hmInstalledSkin.put("Skin_beos","beosthemepack.zip");
      	hmInstalledSkin.put("Skin_whistler","whistlerthemepack.zip");
      	hmInstalledSkin.put("Skin_xpluna","xplunathemepack.zip");
      	hmInstalledSkin.put("Skin_bbj","bbjthemepack.zip");
      }
    }
    catch (Exception e){
      System.out.println("Excepció:"+e);
    }
	}
	
	public static javax.swing.UIManager.LookAndFeelInfo[] getInstalledLookAndFeels(){
		javax.swing.UIManager.LookAndFeelInfo[] ini=javax.swing.UIManager.getInstalledLookAndFeels();
		javax.swing.UIManager.LookAndFeelInfo[] end=new javax.swing.UIManager.LookAndFeelInfo[ini.length+hmInstalledSkin.size()];
		System.arraycopy(ini,0,end,0,ini.length);
		java.util.Iterator it=hmInstalledSkin.keySet().iterator();
		for (int i=ini.length;i<end.length && it.hasNext();i++){
			Object oKey=it.next();
			String value=hmInstalledSkin.get(oKey).toString();
			end[i]=new javax.swing.UIManager.LookAndFeelInfo(oKey.toString(),oKey.toString());
		}
		return end;
	}
	
	public static void setLookAndFeel(String className){
		try{
			if (!hmInstalledSkin.containsKey(className)){
				javax.swing.UIManager.setLookAndFeel(className);
			}
			else{
				Object o=hmInstalledSkin.get(className);                                
				if (o!=null){
        	String s=o.toString();
          if (className.toLowerCase().startsWith("skin")){
						java.util.Properties p=System.getProperties();
						p.put("skinlf.themepack",s);
						Class c=ClassLoader.getSystemClassLoader().loadClass("com.l2fprod.gui.plaf.skin.SkinLookAndFeel");
						java.lang.reflect.Method m=c.getDeclaredMethod("loadThemePack",new Class[]{ClassLoader.getSystemClassLoader().loadClass("java.lang.String")});
						m.invoke(c,new Object[]{s});
						////com.l2fprod.gui.plaf.skin.SkinLookAndFeel.loadThemePack(s);
						System.out.println("Prop:"+System.getProperties().get("skinlf.themepack")+"<--");
						javax.swing.UIManager.setLookAndFeel("com.l2fprod.gui.plaf.skin.SkinLookAndFeel");
          }
          else{
						Class c=ClassLoader.getSystemClassLoader().loadClass("com.memoire.slaf.SlafLookAndFeel");
						java.lang.reflect.Method m=c.getDeclaredMethod("setCurrentTheme",new Class[]{ClassLoader.getSystemClassLoader().loadClass("java.lang.String")});
						m.invoke(c,new Object[]{s});
          	////com.memoire.slaf.SlafLookAndFeel.setCurrentTheme(s);
            javax.swing.UIManager.setLookAndFeel("com.memoire.slaf.SlafLookAndFeel");
          }
				}
			}
		}
    catch (javax.swing.UnsupportedLookAndFeelException ex){
    	System.out.println("Excepció amb els look and feel:"+ex);
    }
    catch (Exception e){
      System.out.println("Excepció l&F:"+e);
    }
	}
	
	protected static boolean isInstalledLookAndFeel(String sClassName){
		boolean bFound=false;
		if (sClassName!=null){
			javax.swing.UIManager.LookAndFeelInfo[] aLF=getInstalledLookAndFeels();
   		for (int i=0;i<aLF.length && !bFound;i++){
    		LookAndFeelInfo look=aLF[i];
      	bFound=sClassName.equals(look.getClassName());
    	}
    }
    return bFound;   		
	}
	
}