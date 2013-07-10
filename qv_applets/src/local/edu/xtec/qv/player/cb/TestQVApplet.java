package edu.xtec.qv.player.cb;

import java.applet.Applet;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.net.URL;

public class TestQVApplet extends Applet {
	
	Image img;
	
	public void init() {
		MediaTracker mt = new MediaTracker(this);
		
		URL uBase = null;
		try {
    	   uBase = getDocumentBase();
    	} catch (Exception e) {e.printStackTrace();}

    	img = getImage(uBase, getImageName());
        mt.addImage(img,1);
        
		try {
			mt.waitForAll();
		}catch (InterruptedException  e) {e.printStackTrace();}
	}

	public void start() {
	}
	
	public void paint(Graphics g) {
		try{
			g.drawImage(img,0,0,this);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	protected String getImageName(){
		//String sBase = new File(getDocumentBase().getFile()).getParentFile().getAbsolutePath();
		String sName = this.getParameter("image");
		//System.out.println("image="+sBase+" name="+sName);
		//String sImage = "file://"+sBase+File.separator+sName;
		return sName;
	}
	
	public boolean action(Event evt, Object arg) {
		if (arg.equals("OK")) {
			System.out.println("action ok");
		} else return false;
		return true;
	}	
		
}
