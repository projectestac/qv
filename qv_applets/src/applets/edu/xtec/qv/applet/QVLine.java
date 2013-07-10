package edu.xtec.qv.applet;

import java.awt.*;
import java.awt.geom.*;

public class QVLine extends QVShape{
	
	public QVLine(){
		this(true);
	}
	
	public QVLine(boolean isNew){
		super(new Line2D.Double(-20,-20,-20,-20), isNew);
	}
	
	public Line2D getOwnShape(){
		return (Line2D)getShape();
	}
	
	public boolean contains(Point p){
		Line2D l = getOwnShape();
		double d = l.ptSegDist(p.getX(), p.getY());
		return (d<=Displayer.IMAN_SIZE);
	}
	
	public void startShape(Point p) {
		getOwnShape().setLine(p.getX(), p.getY(), p.getX(), p.getY());
	}
	
	public void setCurrentEndPoint(Point p){
		getOwnShape().setLine(getOwnShape().getX1(), getOwnShape().getY1(), p.getX(), p.getY());
	}
	
	public void endShape(Point p) {}
	
	public Object clone(){
		return cloneTo(new QVPencil(false));
	}
	
}