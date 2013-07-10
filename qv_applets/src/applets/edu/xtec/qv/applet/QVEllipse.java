package edu.xtec.qv.applet;

import java.awt.*;
import java.awt.geom.*;

public class QVEllipse extends QVShape{
	
	private Point initPoint = null;
	
	public QVEllipse(){
		this(true);
	}
	
	public QVEllipse(boolean isNew){
		super(new Ellipse2D.Double(-20,-20,0,0), isNew);
		setClosed(true);
	}
	
	public Ellipse2D getOwnShape(){
		return (Ellipse2D)getShape();
	}
	
	public void startShape(Point p) {
		initPoint = p;
		getOwnShape().setFrame(p.getX(), p.getY(), 0, 0);
	}
	
	public void setCurrentEndPoint(Point p){
		// Es troben les coordenades del punt mes a l'esquerra
		double iniX = Math.min(p.getX(), initPoint.getX());
		double iniY = Math.min(p.getY(), initPoint.getY());
		double w = Math.abs(p.getX()-initPoint.getX());
		double h = Math.abs(p.getY()-initPoint.getY());
		getOwnShape().setFrame(iniX, iniY, w, h);
	}
	
	public void endShape(Point p) {}
	
	public Object clone(){
		return cloneTo(new QVPencil(false));
	}
	
}