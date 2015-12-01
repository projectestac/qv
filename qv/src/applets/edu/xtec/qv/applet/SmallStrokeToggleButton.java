package edu.xtec.qv.applet;

import javax.swing.*;
import java.awt.*;

public class SmallStrokeToggleButton extends SmallToggleButton{
	
	protected int stroke;
	
	public SmallStrokeToggleButton(boolean selected, ImageIcon imgUnselected, ImageIcon imgSelected, String tip, int stroke) {
		super(selected, imgUnselected, imgSelected, tip);
		this.stroke = stroke;
	}
	
	public void setStroke(int stroke){
		this.stroke = stroke;
	}
	
	public int getStroke(){
		return stroke;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		super.paintComponent(g);
		g2d.setColor(Color.black);
		int coord = (int)(((float)24/2)-((float)stroke/2));
		g2d.fillRect(coord,coord,stroke,stroke);
	}
	
}
