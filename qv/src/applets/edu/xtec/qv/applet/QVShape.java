package edu.xtec.qv.applet;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public abstract class QVShape implements Paintable, MouseListener, MouseMotionListener{
	
	private Shape s = null;
	private QVShapeAspect aspect = null;
	protected boolean closed = false;
	protected Displayer pissarra = null;
	private boolean isNew = false;
	
	private static final BasicStroke defaultStroke=new BasicStroke(1);
	
	public QVShape(Shape s){
		this(s, true);
	}
	
	public QVShape(Shape s, boolean isNew){
		/* isNew ens indica si estem creant el Shape i, per tant, es pot modificar */
		this.s=s;
		this.isNew=isNew;
	}
	
	public abstract void startShape(Point p);
	public abstract void setCurrentEndPoint(Point p);
	public abstract void endShape(Point p);
	
	public void setDisplayer(Displayer pissarra){
		this.pissarra = pissarra;
		if (isNew){
			startShapeListeners();	
		}
	}
	
	public void setShape(Shape s){
		this.s=s;
		checkClosed();
	}
	
	public Shape getShape(){
		return s;
	}
	
	public void setAspect(QVShapeAspect aspect){
		this.aspect=aspect;
	}
	
	protected QVShapeAspect getAspect(){
		return aspect;
	}
	
	public void setClosed(boolean closed){
		this.closed = closed;
	}
	
	public boolean isClosed(){
		return closed;
	}
	
	public void paint(java.awt.Graphics g, boolean includeBackgroundImage){}
	
	public void paint(Color c){
		getAspect().setColor(c);
	}
	
	public void fill(Color c){
		if (isClosed())
			getAspect().setFillColor(c);
	}
	
	protected void checkClosed(){
		/* Aquesta funció s'invoca per recalcular si aquesta figura està tancada.
		 Per defecte no fa res. Només ho han de calcular les figures que poden ser
		 tancades (els rectangles, el·lipses ja estan tancats i una recta sola no
		 pot estar tancada. */
	}
	
	public Point getAdjacentPoint(Point p){
		/* Retorna el vértex d'aquesta figura que és a distancia inferior a Displayer.IMAN_SIZE del punt p 
		 o null si no hi ha cap
		 */
		Point adjacentPoint = null;
		PathIterator pi = getShape().getPathIterator(null);
		float aF[] = new float[6];
		for(boolean bFound = false;!pi.isDone(); pi.next()){
			Point2D.Float current = null;
			int i = pi.currentSegment(aF);
			switch (i){
				case PathIterator.SEG_MOVETO:
				case PathIterator.SEG_LINETO:
					current = new Point2D.Float(aF[0],aF[1]);
				break;
				case PathIterator.SEG_QUADTO:
					current = new Point2D.Float(aF[2],aF[3]);
				break;
				case PathIterator.SEG_CUBICTO:
					current = new Point2D.Float(aF[4],aF[5]);
				break;
			}
			if (current != null){
				bFound = (current.distance(p.getX(), p.getY()) <= Displayer.IMAN_SIZE);
				if (bFound)
					adjacentPoint = new Point((int)current.getX(), (int)current.getY());
			}
		}
		return adjacentPoint;
	}
	
	public boolean canMergeTo(QVShape shape){
		boolean canMerge = false;
		if (shape!=null && !isClosed() && !shape.isClosed()){
			Point[] corners1 = getCorners();
			Point[] corners2 = shape.getCorners();
			if (corners1!=null && corners1.length>1 && corners2!=null && corners2.length>1){
				if (corners1[0].equals(corners2[0]) || corners1[0].equals(corners2[1]) || corners1[1].equals(corners2[0]) || corners1[1].equals(corners2[1]))
					canMerge = true;
			}
		}
		return canMerge;
	}
	
	public Point[] getCorners(){
		/* Aquesta funció retorna els 2 extrems de les figures NO tancades */
		Point[] corners = null;
		if (!isClosed()){
			corners = new Point[2];
			PathIterator pi = getShape().getPathIterator(null);
			double aD[] = new double[6];
			for(;!pi.isDone(); pi.next()){
				Point current = null;
				int i = pi.currentSegment(aD);
				switch (i){
					case PathIterator.SEG_MOVETO:
					case PathIterator.SEG_LINETO:
						current = new Point((int)aD[0],(int)aD[1]);
					break;
					case PathIterator.SEG_QUADTO:
						current = new Point((int)aD[2],(int)aD[3]);
					break;
					case PathIterator.SEG_CUBICTO:
						current = new Point((int)aD[4],(int)aD[5]);
					break;
					case PathIterator.SEG_CLOSE:
						current = corners[0];
					break;
				}
				if (current != null){
					if (corners[0]==null)
						corners[0] = current;
					else
						corners[1] = current;
				}
			}
		}
		return corners;
	}
	
	public boolean contains(Point p){
		return getShape().contains(p);
	}
	
	public boolean borderContains(Point p){
		/* A l'API tenim funcions per a saber si un punt es troba a l'interior d'un Shape
		 però per a saber si es troba al contorn o a quina distància es troba del contorn.
		 Podem recórrer tots els segments que formen la figura i calcular la distància
		 a la que es troba el punt però només funcionaria amb trams rectes i no amb les
		 el·lipses. Per tant, per saber si ens trobem aprop del contorn agafarem els punts
		 p=(p.x,p.y), p1=(p.x,p.y-1), p2=(p.x+1,p.y), p3=(p.x,p.y+1) i p4=(p.x-1,p.y) i si
		 com a mínim un d'ells es troba a l'interior i com a mínim un altre es troba a fora
		 considerarem que som al contorn.  */
		double dist = Displayer.IMAN_SIZE;
		
		boolean bPointInside = false;
		boolean bPointOutside = false;
		Shape s = getShape();
		if (s.contains(p.getX(), p.getY()))
			bPointInside = true;
		else
			bPointOutside = true;
		if (s.contains(p.getX(), p.getY()-dist))
			bPointInside = true;
		else
			bPointOutside = true;
		if (s.contains(p.getX()+dist, p.getY()))
			bPointInside = true;
		else
			bPointOutside = true;
		if (s.contains(p.getX(), p.getY()+dist))
			bPointInside = true;
		else
			bPointOutside = true;
		if (s.contains(p.getX()-dist, p.getY()))
			bPointInside = true;
		else
			bPointOutside = true;
		
		return (bPointInside && bPointOutside);
	}
	
	private void startShapeListeners(){
		if (pissarra!=null){
			pissarra.addMouseListener(this);
			pissarra.addMouseMotionListener(this);
		}
	}
	
	private void endShapeListeners(){
		if (pissarra!=null){
			pissarra.removeMouseListener(this);
			pissarra.removeMouseMotionListener(this);
		}
	}
	
	public void paint (Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		
		Color c = g2d.getColor();
		boolean dibuixat = false;
		if (aspect!=null){
			if (aspect.getStroke()!=null)
				g2d.setStroke(aspect.getStroke());
			if (isClosed() && aspect.getFillColor()!=null){
				g2d.setColor(aspect.getFillColor());
				g2d.fill(getShape());
				dibuixat = true;
			}
			if (aspect.getColor()!=null && aspect.getColor()!=aspect.getFillColor()){
				dibuixat = false;
				g2d.setColor(aspect.getColor());
			}
			else
				g2d.setColor(c);
		}
		if (!dibuixat) // Si el color de la cantonada i el de l'interior és el mateix no el tornem a dibuixar
			g2d.draw(getShape());
		
		/*Color c = g2d.getColor();
		 if (aspect!=null){
		 if (aspect.getStroke()!=null)
		 g2d.setStroke(aspect.getStroke());
		 if (isClosed() && aspect.getFillColor()!=null){
		 g2d.setColor(aspect.getFillColor());
		 g2d.fill(getShape());
		 } else{
		 if (aspect.getColor()!=null)
		 g2d.setColor(aspect.getColor());
		 g2d.draw(getShape());
		 }
		 } else {
		 g2d.draw(getShape());
		 }*/
		g2d.setColor(c);
	}
	
	public void mousePressed(MouseEvent e) {
		Point p = pissarra.getAdjacentShapePoint(e.getPoint());
		// Si hi ha un punt d'una altra figura molt propera a on s'ha clicat, l'aproximem
		startShape(p!=null?p:e.getPoint());
	}
	
	public void mouseDragged(MouseEvent e) {
		Point p = pissarra.getAdjacentShapePoint(e.getPoint());
		
		int marge = 1;
		Rectangle rectangle = getShape().getBounds();
		pissarra.repaint(rectangle.x - marge, rectangle.y - marge, rectangle.width + (marge*2), rectangle.height + (marge*2));		
		
		// Si hi ha un punt d'una altra figura molt propera a on s'ha clicat, l'aproximem
		setCurrentEndPoint(p!=null?p:e.getPoint());
		
		rectangle = getShape().getBounds();
		pissarra.repaint(rectangle.x - marge, rectangle.y - marge, rectangle.width + (marge*2), rectangle.height + (marge*2));		
	}
	
	public void mouseReleased(MouseEvent e) {
		endShape(e.getPoint());
		endShapeListeners();
		pissarra.repaint();
		pissarra.shapeFinished();
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	
	protected abstract Object clone();
	
	protected QVShape cloneTo(QVShape obj){
		obj.setShape(getShape());
		obj.setAspect((QVShapeAspect)(getAspect().clone()));
		obj.setClosed(isClosed());
		obj.setDisplayer(pissarra);
		return obj;
	}
}