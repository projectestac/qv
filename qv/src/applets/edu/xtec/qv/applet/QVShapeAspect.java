package edu.xtec.qv.applet;

import java.awt.Color;
import java.awt.BasicStroke;

public class QVShapeAspect{
	
	private Color c = null;
	private Color fillColor = null;
	private BasicStroke stroke = null;
	
	public QVShapeAspect(Color c, Color fillColor, BasicStroke stroke){
		this.c=c;
		this.fillColor=fillColor;
		this.stroke=stroke;
	}
	
	public void setColor(Color c){
		this.c=c;
	}
	
	public Color getColor(){
		return c;
	}
	
	public void setFillColor(Color fillColor){
		this.fillColor=fillColor;
	}
	
	public Color getFillColor(){
		return fillColor;
	}
	
	public void setStroke(BasicStroke stroke){
		this.stroke=stroke;
	}
	
	public BasicStroke getStroke(){
		return stroke;
	}
	
	public Object clone(){
		return new QVShapeAspect(getColor(), getFillColor(), getStroke());
	}
}