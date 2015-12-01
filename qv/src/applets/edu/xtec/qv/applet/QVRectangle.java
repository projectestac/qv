package edu.xtec.qv.applet;

import java.awt.*;
import java.awt.geom.*;

public class QVRectangle extends QVShape{
	
	private Point initPoint = null;
	
	public QVRectangle(){
		this(true);
	}
	
	public QVRectangle(boolean isNew){
		super(new Rectangle2D.Double(-20,-20,0,0), isNew);
		setClosed(true);
	}
	
	public Rectangle2D getOwnShape(){
		return (Rectangle2D)getShape();
	}
	
	public void startShape(Point p) {
		initPoint = p;
		getOwnShape().setRect(p.getX(), p.getY(), 0, 0);
	}
	
	public void setCurrentEndPoint(Point p){
		// Es troben les coordenades del punt mes a l'esquerra
		double iniX = Math.min(p.getX(), initPoint.getX());
		double iniY = Math.min(p.getY(), initPoint.getY());
		double w = Math.abs(p.getX()-initPoint.getX());
		double h = Math.abs(p.getY()-initPoint.getY());
		getOwnShape().setRect(iniX, iniY, w, h);
	}
	
	public void endShape(Point p) {}
	
	public Object clone(){
		return cloneTo(new QVPencil(false));
	}
	
}