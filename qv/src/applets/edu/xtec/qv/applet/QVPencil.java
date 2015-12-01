package edu.xtec.qv.applet;

import java.awt.*;
import java.awt.geom.*;

public class QVPencil extends QVShape{
	
	public QVPencil(){
		this(true);
	}
	
	public QVPencil(boolean isNew){
		super(new GeneralPath(), isNew);
	}
	
	public GeneralPath getOwnShape(){
		return (GeneralPath)getShape();
	}
	
	public boolean contains(Point p){
		boolean contains = false;
		if (isClosed())
			contains = super.contains(p);
		else{
			//GeneralPath gp = getOwnShape();
			
			PathIterator pi = getShape().getPathIterator(null);
			double aD[] = new double[6];
			double x0 = -1;
			double y0 = -1;
			double x1 = -1;
			double y1 = -1;
			double x2 = -1;
			double y2 = -1;
			for(contains = false;!contains && !pi.isDone(); pi.next()){
				int i = pi.currentSegment(aD);
				switch (i){
					case PathIterator.SEG_MOVETO:
						x0 = aD[0]; y0 = aD[1];
						x2 = aD[0]; y2 = aD[1];
						break;
					case PathIterator.SEG_LINETO:
						x2 = aD[0]; y2 = aD[1];
						break;
					case PathIterator.SEG_QUADTO:
						x2 = aD[2]; y2 = aD[3];
						break;
					case PathIterator.SEG_CUBICTO:
						x2 = aD[4]; y2 = aD[5];
						break;
					case PathIterator.SEG_CLOSE:
						x2 = x0; y2 = y0;
						break;
				}
				if (x1!=-1 || y1!=-1){
					contains = (Line2D.ptSegDist(x1, y1, x2, y2, p.getX(), p.getY())<=Displayer.IMAN_SIZE);
				}
				x1 = x2;
				y1 = y2;
			}
		}
		return contains;
	}
	
	protected void checkClosed(){
		/* Aquesta funció s'invoca per recalcular si aquesta figura està tancada */
		if (!isClosed()){
			Point[] p = getCorners();
			if (p!=null && p.length==2){
				if (p[0].equals(p[1])){
					//System.out.println("Closed!!!!!!!!!!!!!!!!!!!!!!!!");
					setClosed(true);
				}
			}
		}
	}
	
	public void startShape(Point p) {
		getOwnShape().moveTo((float)p.getX(), (float)p.getY());
	}
	
	public void setCurrentEndPoint(Point p) {
		getOwnShape().lineTo((float)p.getX(), (float)p.getY());
	}
	
	public void endShape(Point p) {
		setShape(GeomUtilities.simplifica(getOwnShape()));
		setShape(GeomUtilities.convertToCurve(getOwnShape()));
	}
	
	public Object clone(){
		return cloneTo(new QVPencil(false));
	}
}