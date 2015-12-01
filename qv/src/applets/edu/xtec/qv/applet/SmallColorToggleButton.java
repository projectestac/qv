package edu.xtec.qv.applet;

import javax.swing.*;
import java.awt.*;

public class SmallColorToggleButton extends SmallToggleButton{
	
	protected Color c = null;
	
	public SmallColorToggleButton(boolean selected, ImageIcon imgUnselected, ImageIcon imgSelected, String tip, Color c) {
		super(selected, imgUnselected, imgSelected, tip);
		this.c = c;
	}
	
	public void setColor(Color c){
		this.c = c;
	}
	
	public Color getColor(){
		return c;
	}
	
	protected void paintComponent(java.awt.Graphics g){
		super.paintComponent(g);
		java.awt.Color current = g.getColor();
		g.setColor(c);
		g.fillRect(6,6,12,12);
		g.setColor(current);
	}
	
}
