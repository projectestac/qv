package edu.xtec.qv.applet;

import java.awt.event.*;
import java.util.ArrayList;

public class Displayer extends javax.swing.JPanel implements Paintable, MouseListener, MouseMotionListener{
	
	QVDrawer drawer = null;
	UndoRedoer undoRedoer = null;
	ArrayList alQVShapes = null;
	java.awt.Image bgImage = null;
	QVShape currentShape = null;
	public static final double IMAN_SIZE=5;
	
	public Displayer(QVDrawer drawer){
		this.drawer=drawer;
		alQVShapes = new ArrayList();
		this.undoRedoer=new UndoRedoer(alQVShapes);
		updateUndoRedoButtons();
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void addQVShape(QVShape s){
		s.setDisplayer(this);
		alQVShapes.add(s);
		undoRedoer.setNewAlShapes(alQVShapes);
		updateUndoRedoButtons();
		currentShape = s;
	}
	
	private void removeQVShape(QVShape s){
		alQVShapes.remove(s);
		undoRedoer.setNewAlShapes(alQVShapes);
		updateUndoRedoButtons();
	}
	
	public void removeAllQVShapes(){
		alQVShapes.clear();
		repaint();
	}
	
	public int getNumShapes(){
		if (alQVShapes!=null)
			return alQVShapes.size();
		else
			return 0;
	}
	
	protected void updateUndoRedoButtons(){
		drawer.setCanUndo(undoRedoer.canUndo());
		drawer.setCanRedo(undoRedoer.canRedo());
	}
	
	public void undo(){
		if (undoRedoer.canUndo()){
			alQVShapes = undoRedoer.undo();
			updateUndoRedoButtons();
			repaint();
		}
	}
	
	public void redo(){
		if (undoRedoer.canRedo()){
			alQVShapes = undoRedoer.redo();
			updateUndoRedoButtons();
			repaint();
		}
	}
	
	public void setBackgroundImage(java.awt.Image bgImage){
		this.bgImage=bgImage;
	}
	
	public java.awt.Point getAdjacentShapePoint(java.awt.Point p){
		/* Retorna el vértex de la figura que estigui a distancia inferior a Displayer.IMAN_SIZE del punt p 
		 o null si no hi ha cap
		 */
		java.awt.Point adjacentPoint = null;
		for (int i=0;alQVShapes!=null && adjacentPoint==null && i<alQVShapes.size();i++){
			QVShape current=(QVShape)alQVShapes.get(i);
			adjacentPoint = current.getAdjacentPoint(p);
		}
		return adjacentPoint;
	}
	
	private void paintShapeAt(java.awt.Point p, java.awt.Color c){
		// Cal crear un clon per poder mantenir l'historic intacte
		QVShape s = getShapeAt(p);
		if (s!=null){
			QVShape clone = (QVShape)(s.clone());
			if (s.borderContains(p))
				clone.paint(c);
			else
				clone.fill(c);
			alQVShapes.remove(s);
			alQVShapes.add(clone);
			undoRedoer.setNewAlShapes(alQVShapes);
			updateUndoRedoButtons();
			repaint();
		}
	}
	
	private void deleteShapeAt(java.awt.Point p){
		QVShape s = getShapeAt(p);
		if (s!=null){
			removeQVShape(s);
			repaint();
		}
	}
	
	private QVShape getShapeAt(java.awt.Point p){
		QVShape s = null;
		boolean bFound = false;
		for (int i=0;alQVShapes!=null && !bFound && i<alQVShapes.size();i++){
			QVShape current=(QVShape)alQVShapes.get(i);
			if (current.contains(p) || current.borderContains(p)){
				s = current;
				bFound = true;
			}
		}
		return s;
	}
	
	public void shapeFinished(){
		if (currentShape!=null && !currentShape.isClosed()){ //sempre
			//Comprovem si es pot juntar la figura que s'ha acabat amb una altra
			boolean canMerge = false;
			for (int i=0;alQVShapes!=null && !canMerge && i<alQVShapes.size();i++){
				QVShape s=(QVShape)alQVShapes.get(i);
				if (s!=currentShape){ //No interesa fer-ho amb equals perquè seria la mateixa referencia
					canMerge = currentShape.canMergeTo(s);
					if (canMerge){
						// Es pot juntar
						//System.out.println("merge");
						QVShape newShape = GeomUtilities.mergeShapes(s, currentShape);
						alQVShapes.remove(s);//
						alQVShapes.remove(currentShape);//
						undoRedoer.removeCurrentScene(); // Cal esborrar-lo perquè no ha existit mai per separat
						addQVShape(newShape);
						shapeFinished(); //Per comprovar si el nou Shape es pot juntar amb cap altre
					}
				}
			}
			
		}
		currentShape = null;
	}
	
	public String getStringSVG(){
		String s = SVGUtility.createSVGElement(this);
		return s;
	}
	
	public void addShapesFromSVG(String sSVG){
		ArrayList alQVShapes=SVGUtility.loadShapesFromSVG(sSVG);
		for (int i=0;alQVShapes!=null && i<alQVShapes.size();i++){
			QVShape qvShape=(QVShape)alQVShapes.get(i);
			addQVShape(qvShape);
		}
		currentShape = null;
		undoRedoer.reset(this.alQVShapes);
		updateUndoRedoButtons();
		repaint();
	}
	
	public void paint (java.awt.Graphics g){
		super.paint(g);
		paint(g, true);
	}
	
	public void paint (java.awt.Graphics g, boolean includeBackgroundImage){
		//includeBackgroundImage indica si volem que s'inclogui la imatge de fons.
		//Em generar l'SVG no la voldrem exportar
		java.awt.Graphics2D g2d = (java.awt.Graphics2D)g;
		if (includeBackgroundImage){
			g2d.setColor(java.awt.Color.black);
			g2d.drawRect(0,0,getWidth()-1,getHeight()-1);
			if (bgImage!=null)
				g.drawImage(bgImage,0,0,this);
		}
		for (int i=0;alQVShapes!=null && i<alQVShapes.size();i++){
			QVShape s=(QVShape)alQVShapes.get(i);
			s.paint(g2d);
		}
	}
	
	private void newShape(int tool){
		// Crea una figura nova del tipus que indiqui tool
		QVShape qvShape = null;
		switch (tool){
			case QVDrawer.LINE_TOOL:
				qvShape = new QVLine();
			break;
			case QVDrawer.RECTANGLE_TOOL:
				qvShape = new QVRectangle();
			break;
			case QVDrawer.ELLIPSE_TOOL:
				qvShape = new QVEllipse();
			break;
			case QVDrawer.PENCIL_TOOL:
				qvShape = new QVPencil();
			break;
		}
		if (qvShape!=null){
			qvShape.setAspect((QVShapeAspect)drawer.getCurrentAspect().clone());
			addQVShape(qvShape);
		}
	}
	
	public void mousePressed(MouseEvent e) {
		/*
		 LINE_TOOL
		 RECTANGLE_TOOL
		 ELLIPSE_TOOL
		 PENCIL_TOOL
		 DELETE_TOOL
		 PAINT_TOOL
		 */
		int tool = drawer.getCurrentTool();
		if (tool==QVDrawer.LINE_TOOL || tool==QVDrawer.RECTANGLE_TOOL || tool==QVDrawer.ELLIPSE_TOOL || tool==QVDrawer.PENCIL_TOOL){
			if (currentShape == null)
				newShape(tool);
			if (currentShape != null)
				currentShape.mousePressed(e);
		} else if (tool==QVDrawer.PAINT_TOOL){
			paintShapeAt(e.getPoint(), drawer.getCurrentAspect().getColor());
		} else if (tool==QVDrawer.DELETE_TOOL){
			deleteShapeAt(e.getPoint());
			setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
		}
	}
	
	public void mouseReleased(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	
	public void mouseMoved(MouseEvent e) {
		int tool = drawer.getCurrentTool();
		if (tool == QVDrawer.PAINT_TOOL){
			QVShape s = getShapeAt(e.getPoint());
			if (s!=null){ // && s.isClosed()){
				setCursor(drawer.getPaintCursor());
			} else {
				setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
			}
		} else if (tool == QVDrawer.DELETE_TOOL){
			QVShape s = getShapeAt(e.getPoint());
			if (s!=null){
				setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.CROSSHAIR_CURSOR));
			} else {
				setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
			}
		} else if (tool == QVDrawer.LINE_TOOL || tool == QVDrawer.RECTANGLE_TOOL || tool == QVDrawer.ELLIPSE_TOOL || tool == QVDrawer.PENCIL_TOOL){
			setCursor(drawer.getDrawCursor());
		}else {
			setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
		}
	}
	
	
}

