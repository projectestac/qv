package edu.xtec.jclic.shapers;

/*
 * PolygonDrawPanel.java
 *
 * Created on 17 / maig / 2002, 10:20
 */

/**
 *
 * @author  allastar
 * @version
 */

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import javax.swing.JComponent;

public class PolygonDrawPanel implements java.awt.event.MouseMotionListener, java.awt.event.MouseListener{
    
    private Vector vShapes; //Vector que conté el poligon que s'està modificant en l'actualitat
    private static Vector vCopied;
    //private ImageIcon backGroundImage=null;
    private Vector vRedrawingLines,vRedrawingLinesBeforeModify,vShapeBeforeModify;
    private Vector vPointListeners;
    private double iniX,iniY,finX,finY,lastFinX,lastFinY;
    private Point2D iniPoint=null,lastPoint=null;
    private boolean bSelectedPoint=false;
    private double zoomX=0,zoomY=0,zoomH=-1,zoomW=-1;
    private boolean creatingRect=false,creatingEllipse=false, creatingPolygon=false, bRedrawingLines=false, bSelectingArea=false;
    private boolean bResizing=false;
    private int resizingDirection=NO_RESIZING;
    private boolean bSpecialLine=false;
    private boolean bMoving=false;
    private EditableShape specialLine=null;
    private int drawingMode=SELECTING;
    static double defaultSensibility=1.5;
    private AffineTransform at;
    private Vector vDrawnBorders=null;
    private Shape current=null;
    
    //    private DrawnShapeManage dsm=null;
    
    private JComponent container;
    
    private short INITIAL=0;
    private short END=1;
    
    public static final int SELECTING=1;
    public static final int MOVING=2;
    public static final int NEW_POINT=4;
    public static final int DRAWING_RECT=5;
    public static final int DRAWING_ELLIPSE=6;
    public static final int DRAWING_POLYGON=7;
    public static final int SCANNING_SHAPE=8;
    public static final int ZOOM=12;
    
    public static final int NO_RESIZING=-1;
    public static final int EAST=0;
    public static final int SOUTH=1;
    public static final int SOUTH_EAST=2;
    
    private int backgroundComposite=0;
    
    
    protected  HolesEditorPanel hep;
    protected boolean canResize;
    protected Rectangle lastPreviewArea;
    
    static Cursor[] cursors=null;
    public static final int PEN_CURSOR=0;
    public static final int CIRCLE_CURSOR=1;
    
    protected Shape esborram=null;
    
    /** Creates new PolygonDrawPanel */
    public PolygonDrawPanel(int width, int height, HolesEditorPanel hep, boolean canResize) {
        this.hep=hep;
        this.canResize=canResize;
        vShapes=new Vector();
        if (vCopied==null) vCopied=new Vector();
        
        vRedrawingLines=new Vector();
        vRedrawingLinesBeforeModify=new Vector();
        vPointListeners=new Vector();
        //hep.addMouseMotionListener(this);
        //hep.addMouseListener(this);
        at=new AffineTransform();
        initDrawnBorders();
        
        if (cursors==null) {
            cursors=new Cursor[2];
            Toolkit tk=Toolkit.getDefaultToolkit();
            cursors[PEN_CURSOR]=tk.createCustomCursor(edu.xtec.util.ResourceManager.getImageIcon("cursors/llapis.gif").getImage(), new Point(12,24), "pen");
            cursors[CIRCLE_CURSOR]=tk.createCustomCursor(edu.xtec.util.ResourceManager.getImageIcon("cursors/cercle.gif").getImage(), new Point(16,16), "circle");
        }
        hep.addKeyListener(new PolygonDrawPanel.KeyHandler());
    }
    
    public void setDrawingMode(int drawingMode){
        if (this.drawingMode!=drawingMode){
            this.drawingMode=drawingMode;
            if (creatingPolygon) joinPolygon();
            if (drawingMode!=NEW_POINT && drawingMode!=SELECTING) {
                endPolygon();//&
            }
            hep.repaint(0);
        }
    }
    
    public int getVisibleWidth(){
        return hep.getPreviewPanel().getWidth();
    }
    
    public int getVisibleHeight(){
        return hep.getPreviewPanel().getHeight();
    }
    
    public void initDrawnBorders(){
        if (vDrawnBorders!=null) vDrawnBorders.clear();
        else vDrawnBorders=new Vector();
        for (int i=0;i<hep.getNumShapes();i++){
            if (i!=hep.currentShape){
                Shape s=hep.getHoles().getShape(i,hep.previewArea);
                if (s!=null) vDrawnBorders.addAll(getBorders(s));
            }
        }
        //System.out.println("num. de cantonades inicial:"+vDrawnBorders.size());
    }
    
    private java.util.Vector getBorders(Shape s){
        // Funció d'utilitat que retorna els punts que defineixen els "segments" del polígon 's'.
        //int xIni=(hep!=null && hep.previewArea!=null)?hep.previewArea.x:0;
        //int yIni=(hep!=null && hep.previewArea!=null)?hep.previewArea.y:0;
        int xIni=0; int yIni=0;////
        //System.out.println("xIni="+xIni+" yIni="+yIni+" hep.previewArea.x="+hep.previewArea.x);
        
        if (s==null) return null;
        java.util.Vector vPoints=new java.util.Vector();
        double x,y;
        if (s instanceof GeneralPath){
            GeneralPath gp=(GeneralPath)s;
            PathIterator it=gp.getPathIterator(new AffineTransform());
            double[] coords=new double[6];
            while (!it.isDone()){
                int type=it.currentSegment(coords);
                switch (type){
                    case PathIterator.SEG_MOVETO:
                        x=coords[0]; y=coords[1];
                        vPoints.add(new Rectangle((int)(x+xIni)-(EditableShapeConstants.selectLength/2),(int)(y+yIni)-(EditableShapeConstants.selectLength/2),EditableShapeConstants.selectLength,EditableShapeConstants.selectLength));
                        //vPoints.add(new Point2D.Double(x,y));
                        break;
                    case PathIterator.SEG_LINETO:
                        //System.out.println("line to ("+coords[0]+","+coords[1]+")");
                        x=coords[0]; y=coords[1];
                        vPoints.add(new Rectangle((int)(x+xIni)-(EditableShapeConstants.selectLength/2),(int)(y+yIni)-(EditableShapeConstants.selectLength/2),EditableShapeConstants.selectLength,EditableShapeConstants.selectLength));
                        //vPoints.add(new Point2D.Double(x,y));
                        break;
                    case PathIterator.SEG_CUBICTO:
                        //System.out.println("cubic to ("+coords[4]+","+coords[5]+")");
                        //vShapes.add(new EditableCubicCurve2D(x,y,coords[0],coords[1],coords[2],coords[3],coords[4],coords[5]));
                        x=coords[4]; y=coords[5];
                        vPoints.add(new Rectangle((int)(x+xIni)-(EditableShapeConstants.selectLength/2),(int)(y+yIni)-(EditableShapeConstants.selectLength/2),EditableShapeConstants.selectLength,EditableShapeConstants.selectLength));
                        //vPoints.add(new Point2D.Double(x,y));
                        break;
                    case PathIterator.SEG_QUADTO:
                        //System.out.println("quad to ("+coords[2]+","+coords[3]+")");
                        //vShapes.add(new EditableQuadCurve2D(x,y,coords[0],coords[1],coords[2],coords[3]));
                        x=coords[2]; y=coords[3];
                        vPoints.add(new Rectangle((int)(x+xIni)-(EditableShapeConstants.selectLength/2),(int)(y+yIni)-(EditableShapeConstants.selectLength/2),EditableShapeConstants.selectLength,EditableShapeConstants.selectLength));
                        //vPoints.add(new Point2D.Double(x,y));
                        break;
                    case PathIterator.SEG_CLOSE:
                        //System.out.println("close");
                        break;
                    default:
                        System.out.println("Default");
                }
                it.next();
            }
        }
        return vPoints;
    }
    
    public void paint(java.awt.Graphics2D g){
        Graphics2D g2d=(Graphics2D)g;
        EditableShape current=null;
        
        //if (EditableShapeConstants.gridWidth!=-1) drawGrid(g,EditableShapeConstants.gridWidth);
        /*if (drawingMode==NEW_POINT){
            if (bSpecialLine){
                bSpecialLine=true;
            }
        }*/
        if (EditableShapeConstants.showDrawnPoints) paintDrawnBorders(g);
        
        java.util.Enumeration e=vShapes.elements();
        while (e.hasMoreElements()){
            current=(EditableShape)e.nextElement();
            //System.out.println("ini: ("+current.getInitialPoint().getX()+","+current.getInitialPoint().getY()+") --> ("+current.getEndPoint().getX()+","+current.getEndPoint().getY());
            if (bSpecialLine && current==specialLine)
                current.paintWithColor(g,drawingMode,EditableShapeConstants.CUT_COLOR);
            else
                current.paintWithColor(g,drawingMode,EditableShapeConstants.ACTIVE_COLOR);
        }
        if (bMoving) paintMoved(g);
        if (creatingRect){
            g.setColor(EditableShapeConstants.selectedColor);
            EditableRectangle rect=new EditableRectangle((int)iniX,(int)iniY,(int)(finX-iniX),(int)(finY-iniY));
            rect.paintWithColor(g,drawingMode,EditableShapeConstants.selectedColor);
        }
        if (creatingEllipse){
            g.setColor(EditableShapeConstants.selectedColor);
            EditableEllipse2D ellipse=new EditableEllipse2D((int)iniX,(int)iniY,(int)(finX-iniX),(int)(finY-iniY));
            ellipse.paintWithColor(g,drawingMode,EditableShapeConstants.selectedColor);
        }
        if (creatingPolygon){
            if (lastPoint!=null){
                EditableLine2D el=new EditableLine2D(lastPoint.getX(),lastPoint.getY(),finX,finY);
                el.paintWithColor(g,drawingMode,EditableShapeConstants.selectedColor);
            }
        }
        //if (bSelectingArea) selectArea(g,iniX,iniY,finX,finY);
        //g.setColor(Color.orange);
        //if (selectedPoint!=null) g.drawRect((int)selectedPoint.getX()-(EditableShapeConstants.selectLength/2),(int)selectedPoint.getY()-(EditableShapeConstants.selectLength/2),EditableShapeConstants.selectLength,EditableShapeConstants.selectLength);
    }
    
    public void drawGrid(java.awt.Graphics g, int gridWidth){
        if (gridWidth<=1) return;
        int width=(int)(hep.previewArea.getWidth());
        int height=(int)(hep.previewArea.getHeight());
        g.setColor(EditableShapeConstants.gridColor);
        //System.out.println("gridWidth:"+gridWidth+" width="+width+" height="+height+" prevArea="+hep.previewArea.x+" xFactor:"+hep.xFactor);
        for (double i=hep.previewArea.x;i<=hep.previewArea.x+width;i+=(gridWidth*hep.xFactor)){ //des de 0 pq en fer zoom no canvii la posició relativa de la graella
            //verticals
            g.drawLine((int)i,hep.previewArea.y,(int)i,(int)(hep.previewArea.y+height));
        }
        for (double i=hep.previewArea.y;i<=hep.previewArea.y+height;i+=(gridWidth*hep.yFactor)){
            //horitzontals
            g.drawLine(hep.previewArea.x,(int)i,(int)(hep.previewArea.x+width),(int)i);
        }
    }
    
    protected void paintDrawnBorders(java.awt.Graphics2D g){
        java.util.Enumeration eDr=vDrawnBorders.elements();
        while (eDr.hasMoreElements()){
            Rectangle r=(Rectangle)eDr.nextElement();
            double x=r.getX();
            double y=r.getY();
            double w=r.getWidth();
            double h=r.getHeight();
            x=x+(w/4);
            y=y+(h/4);
            w=w/2;
            h=h/2;
            g.setColor(EditableShapeConstants.DRAWN_BORDER_COLOR);
            g.fillRect((int)x,(int)y,(int)w,(int)h);
            //g.fill(r);
        }
    }
    
    /*public void selectArea(Graphics g,double x1,double y1,double x2,double y2){
        System.out.println("selectArea");
        g.setColor(EditableShapeConstants.selectingAreaColor);
        if (x1>x2){ double x3=x1;x1=x2;x2=x3;}
        if (y1>y2){ double y3=y1;y1=y2;y2=y3;}
        g.drawRect((int)x1,(int)y1,(int)(x2-x1),(int)(y2-y1));
        if (drawingMode==SELECTING){ // si està fent un zoom no seleccionem els elements interiors a l'area
            EditableShape current=null;
            Rectangle2D r=new Rectangle((int)x1,(int)y1,(int)(x2-x1),(int)(y2-y1));
            java.util.Enumeration e=vShapes.elements();
            while (e.hasMoreElements()){
                current=(EditableShape)e.nextElement();
                if (current.isInto(r)) current.setSelected(true);
                else current.setSelected(false);
            }
        }
    }*/
    
    private void paintMoved(java.awt.Graphics2D g){
        EditableShape current;
        java.util.Enumeration e=vCopied.elements();
        while (e.hasMoreElements()){
            current=(EditableShape)e.nextElement();
            EditableShape copied=(EditableShape)current.clone();
            copied.transform(AffineTransform.getTranslateInstance(finX-iniX,finY-iniY));
            //g.setXORMode(Color.white);
            copied.paintWithColor(g,drawingMode,EditableShapeConstants.movingColor);
            //g.setXORMode(Color.black);
            ////copied.drawBorders(g);
        }
    }
    
    public void updateView(){
        Vector v=getGeneralPath();
        if (lastPreviewArea==null) lastPreviewArea=hep.previewArea;
        if (v.size()>0){
            move(hep.previewArea.x-lastPreviewArea.x,hep.previewArea.y-lastPreviewArea.y,false,false);
        }
        try{
            lastPreviewArea=(Rectangle)(hep.previewArea.clone());
        }
        catch(Exception e){System.out.println(""+e);}
    }
    
    public void setShapeData(ShapeData sd, double x, double y, double scaleX, double scaleY){
        //x,y indiquen la posició a la que ens trobem
        clean();
        current=(sd!=null)?sd.getShape(hep.previewArea):null;
        double firstX=-1,firstY=-1;
        if (sd.primitiveType>=0 && sd.primitivePoints!=null && sd.primitivePoints.length>3){
            EditableShape es=null;
            //AffineTransform scale=AffineTransform.getScaleInstance(hep.previewArea.getWidth(),hep.previewArea.getHeight());
            //AffineTransform translation=AffineTransform.getTranslateInstance(hep.previewArea.getX(),hep.previewArea.getY());
            double xTr=(sd.primitivePoints[0]*hep.previewArea.getWidth())+hep.previewArea.getX();
            double yTr=(sd.primitivePoints[1]*hep.previewArea.getHeight())+hep.previewArea.getY();
            double wSc=sd.primitivePoints[2]*hep.previewArea.getWidth();
            double hSc=sd.primitivePoints[3]*hep.previewArea.getHeight();
            switch (sd.primitiveType){
                case ShapeData.RECTANGLE:
                    es=new EditableRectangle((int)xTr,(int)yTr,(int)wSc,(int)hSc);
                    vShapes.add(es);
                    break;
                case ShapeData.ELLIPSE:
                    es=new EditableEllipse2D((int)xTr,(int)yTr,(int)wSc,(int)hSc);
                    vShapes.add(es);
                    break;
            }
        }
        else{
            Shape s=sd.getShape(hep.previewArea);
            if (s instanceof GeneralPath){
                GeneralPath gp=(GeneralPath)s;
                PathIterator it=gp.getPathIterator(new AffineTransform());
                double[] coords=new double[6];
                while (!it.isDone()){
                    int type=it.currentSegment(coords);
                    switch (type){
                        case PathIterator.SEG_MOVETO:
                            x=coords[0]; y=coords[1];
                            if (firstX==-1){ //Per fer el close
                                firstX=x;
                                firstY=y;
                            }
                            break;
                        case PathIterator.SEG_LINETO:
                            //System.out.println("line to ("+coords[0]+","+coords[1]+")");
                            vShapes.add(new EditableLine2D(x,y,coords[0],coords[1]));
                            x=coords[0]; y=coords[1];
                            break;
                        case PathIterator.SEG_CUBICTO:
                            //System.out.println("cubic to ("+coords[4]+","+coords[5]+")");
                            vShapes.add(new EditableCubicCurve2D(x,y,coords[0],coords[1],coords[2],coords[3],coords[4],coords[5]));
                            x=coords[4]; y=coords[5];
                            break;
                        case PathIterator.SEG_QUADTO:
                            //System.out.println("quad to ("+coords[2]+","+coords[3]+")");
                            vShapes.add(new EditableQuadCurve2D(x,y,coords[0],coords[1],coords[2],coords[3]));
                            x=coords[2]; y=coords[3];
                            break;
                        case PathIterator.SEG_CLOSE:
                            //System.out.println("close");
                            if (firstX!=-1 && (x!=firstX || y!=firstY)){
                                //System.out.println("afegeixo linia de ("+x+","+y+") fins a ("+firstX+","+firstY+")");
                                vShapes.add(new EditableLine2D(x,y,firstX,firstY));
                                x=firstX;y=firstY;
                            }
                            break;
                        default:
                            System.out.println("Default");
                    }
                    it.next();
                }
                if (firstX!=-1 && (x!=firstX || y!=firstY)){
                    // Això es perquè no s'admetin figures no tancades (es tanquin)
                    //System.out.println("afegeixo linia de ("+x+","+y+") fins a ("+firstX+","+firstY+")");
                    vShapes.add(new EditableLine2D(x,y,firstX,firstY));
                }
            }
        }
        removeDrawnBorders(sd);
    }
    
    public void clean(){
        vShapes=new Vector();
        vRedrawingLines=new Vector();
        vRedrawingLinesBeforeModify=new Vector();
    }
    
    
    public boolean selectDrawnShape(Point2D p) {
        endPolygon();
        //vDrawnBorders.clear();
        for (int i=0;i<hep.getNumShapes();i++){
            Shape s=hep.getHoles().getShape(i,hep.previewArea);
            if (s.contains(p) && hep.currentShape!=i){
                hep.setCurrentShape(i);
                setShapeData(hep.getHoles().getShapeData(i),0,0,1,1);
                return true;
            }
        }
        hep.setCurrentShape(hep.getHoles().getNumCells()+1);
        clean();
        return false;
    }
    
    public void selectShape(int iIndex){
        if (iIndex<0) return;
        ShapeData sd=hep.getHoles().getShapeData(iIndex);
        if (sd!=null){
            setShapeData(sd,0,0,1,1);
        }
    }
    
    private EditableShape aproximationToLine(double x, double y){
        return aproximationToLine(x,y,null);
    }
    
    private EditableShape aproximationToLine(double x, double y, Vector vRedrawingLines){
        //retorna un EditableShape si a la coordenada (x,y) hi te alguna cantonada que no és d'una
        //linia del Vector vRedrawingLines, en cas contrari retorna null
        EditableShape current=null;
        java.util.Enumeration e=vShapes.elements();
        while (e.hasMoreElements()){
            current=(EditableShape)e.nextElement();
            if (vRedrawingLines==null || !vRedrawingLines.contains(current))
                if (current.hasClickedBorder((int)x,(int)y,false)) return current;
        }
        return null;
    }
    
    private Point2D getTransformedPoint(Point2D p, boolean mustBeOnGrid){
        //mustBeOnGrid serveix per indicar que no faci cas de l'aproximació
        Point2D mousePoint=new Point2D.Double(p.getX(),p.getY());
        if (EditableShapeConstants.gridWidth!=-1 && EditableShapeConstants.pointsOnGrid && mustBeOnGrid) moveToGrid(mousePoint);
        return mousePoint;
    }
    
    private void moveToGrid(Point2D p){
        //Rep un punt i el modifica posant-li les coordenades del punt a la graella més proper.
        int x=(int)p.getX();
        int y=(int)p.getY();
        x-=hep.previewArea.getX();
        y-=hep.previewArea.getY();
        double wd=EditableShapeConstants.gridWidth*hep.xFactor;
        int w=(int)wd;
        if (w==-1) return;
        int xLeft=(int)(((int)(x/wd))*wd);
        if ((x-xLeft)<(w/2)) x=xLeft;
        else x=(int)(((int)((x+w-1)/wd))*wd);
        int yUp=(int)(((int)(y/wd))*wd);
        if ((y-yUp)<(w/2)) y=yUp;
        else y=(int)(((int)((y+w-1)/wd))*wd);
        x+=hep.previewArea.getX();
        y+=hep.previewArea.getY();
        p.setLocation(x,y);
    }
    
    public Point2D aproximationToDrawnBorder(double x, double y){
        // Retorna true si hi ha algun rectangle a vDrawnBorders que contingui el punt (x,y)
        java.util.Enumeration e=vDrawnBorders.elements();
        while (e.hasMoreElements()){
            Rectangle2D r=(Rectangle2D)e.nextElement();
            if (r.contains(x,y)) return new Point2D.Double(r.getX()+(r.getWidth()/2),r.getY()+(r.getHeight()/2));
        }
        return null;
    }
    
    
    protected void redrawingLines(double x, double y){ //mou totes les figures seleccionades que contenen (x,y) en una cantonada
        EditableShape current=null;
        java.util.Enumeration e=vRedrawingLines.elements();
        while (e.hasMoreElements()){
            current=(EditableShape)e.nextElement();
            current.changeBorder(x,y);
        }
    }
    
    private void cleanZoom(){
        zoomX=0;
        zoomY=0;
        zoomW=-1;
        zoomH=-1;
        at=new AffineTransform();
        cancelCurrentOperations();
        bSelectingArea=false;
        hep.repaint(0);
    }
    
    public void cancelCurrentOperations(){
        creatingRect=false;
        creatingEllipse=false;
    }
    
    public void cut(double x, double y){
        copy(false);
        clean();
        bMoving=true;
        iniX=x;
        iniY=y;
        finX=iniX;
        finY=iniY;
    }
    
    public void cut(){
        cut(-1,-1);
    }
    
    public void copy(boolean needSelected){
        vCopied.clear();
        EditableShape current=null;
        java.util.Enumeration e=vShapes.elements();
        while (e.hasMoreElements()){
            //current=(EditableShape)e.nextElement();
            current=(EditableShape)((EditableShape)e.nextElement()).clone();////////
            if (!needSelected || current.isSelected()){
                vCopied.add(current);
            }
        }
    }
    
    public void paste(){
        bMoving=true;
        iniX=-1; iniY=-1; finX=-1; finY=-1;
        paste(5,5);
    }
    
    public void paste(double x, double y){
        Vector newCopied=new Vector();
        deSelectAll();
        EditableShape current=null;
        java.util.Enumeration e=vCopied.elements();
        while (e.hasMoreElements()){
            current=(EditableShape)e.nextElement();
            EditableShape copied=(EditableShape)current.clone();
            copied.transform(AffineTransform.getTranslateInstance(x,y));
            copied.setSelected(true);
            vShapes.add(copied);
            newCopied.add(copied); //perquè volem que si es fan 2 paste's seguits no es posin les copies una a sobre de l'altre
        }
        vCopied=newCopied;
    }
    
    public void deSelectAll(){
        EditableShape current=null;
        java.util.Enumeration e=vShapes.elements();
        while (e.hasMoreElements()){
            current=(EditableShape)e.nextElement();
            current.setSelected(false);
        }
        bSelectedPoint=false;
        hep.repaint(0);
    }
    
    private EditableShape nearestLine(double x, double y){
        EditableShape nearest=null;
        EditableShape current=null;
        double distance=0;
        double currentDistance;
        java.util.Enumeration e=vShapes.elements();
        while (e.hasMoreElements()){
            current=(EditableShape)e.nextElement();
            currentDistance=current.distanceTo(x,y);
            if (nearest==null || (currentDistance<distance)){
                distance=currentDistance;
                nearest=current;
            }
        }
        return nearest;
    }
    
    private void clicatISeleccionada(int x, int y, boolean needSelected){ //Si needSelected=false no cal que estigui seleccionada una figura per arrossegarla
        // deixa a vRedrawingLines les linies seleccionades que a mes tenen una cantonada on s'ha clicat el ratolí
        Point2D redrawingPoint=null;
        EditableShape current=null;
        vRedrawingLines.clear();
        java.util.Enumeration e=vShapes.elements();
        while (e.hasMoreElements()){
            current=(EditableShape)e.nextElement();
            if ((!needSelected || current.isSelected()) && current.hasClickedBorder(x,y,needSelected)){
                Point2D p=current.getNearestBorder(x,y);
                if (redrawingPoint==null || redrawingPoint.equals(p)){
                    redrawingPoint=p;
                    vRedrawingLines.add(current);
                }
            }
        }
        vRedrawingLinesBeforeModify=cloneVector(vRedrawingLines);
    }
    
    private Vector cloneVector(Vector v){
        Vector vClone=new Vector();
        if (v!=null){
            java.util.Enumeration e=v.elements();
            while (e.hasMoreElements()){
                EditableShape es=(EditableShape)e.nextElement();
                vClone.add(es.clone());
            }
        }
        return vClone;
    }
    
    private void divideShape(EditableShape specialLine,double x, double y){
        if (specialLine!=null){
            EditableShape[] shapes=specialLine.divide(x,y);
            if (shapes!=null){
                // -> Les 2 seguents linies són necessaries per assegurar la connexió de la figura resultant. Partir dos cops pel mateix
                // punt pot significar crear una linia independent de la figura
                Vector vCheckPoint=new Vector();
                vCheckPoint.addAll(vShapes);
                vShapes.remove(specialLine);
                //this.current=null; //
                //vRedrawingLines.clear();//
                for (int i=0;i<shapes.length;i++){
                    if (shapes[i]!=null) vShapes.add(shapes[i]);
                }
                boolean bValidate=validateShape();
                if (!bValidate) vShapes=vCheckPoint;
            }
            hep.updateView();//
        }
        hep.repaint(0);
    }
    
    private boolean validateShape(){
        /* Retorna true només si hi ha una única figura dibuixant-se. Si el num. de figures és diferent de 0, la figura no és connexa*/
        return (getGeneralPath().size()==1);
    }
    
    public Vector getGeneralPath(){
        Vector vGpaths=new Vector();
        GeneralPath currentPolygon=new GeneralPath();
        Vector shapes=new Vector();
        shapes.addAll(vShapes);
        if (!(shapes.size()>0)) return vGpaths;
        EditableShape current=(EditableShape)shapes.get(0);
        shapes.remove(current);
        currentPolygon.append(current,true);
        short notUsedPoint=END; //indicarà el costat de l'últim shape que encara no és adjacent a cap altre
        while (shapes.size()>0){
            EditableShape shape=getAdjacent(shapes,current,notUsedPoint);
            if (shape!=null){
                currentPolygon.append(shape,true);
                notUsedPoint=getNotUsed(current,shape); //Ens retornarà quin és el punt de shape que no és adjacent a current
                shapes.remove(shape);
                current=shape;
            }
            else{
                vGpaths.add(currentPolygon);
                currentPolygon=new GeneralPath();
                notUsedPoint=END;
                current=(EditableShape)shapes.get(0);
                shapes.remove(current);
                currentPolygon.append(current,true);
            }
        }
        vGpaths.add(currentPolygon);
        return vGpaths;
    }
    
    private short getNotUsed(EditableShape current, EditableShape shape){ //Ens retornarà quin és el punt de shape que no és adjacent a current
        if (shape.getInitialPoint().equals(current.getInitialPoint())||shape.getInitialPoint().equals(current.getEndPoint()))
            return END;
        else return INITIAL;
    }
    
    private EditableShape getAdjacent(Vector shapes, EditableShape sh, short notUsedPoint){
        java.util.Enumeration e=shapes.elements();
        Point2D p;
        if (notUsedPoint==INITIAL) p=sh.getInitialPoint();
        else p=sh.getEndPoint();
        while (e.hasMoreElements()){
            EditableShape shape=(EditableShape)e.nextElement();
            if (shape.isAdjacentTo(p)) return shape;
        }
        return null;
    }
    
    public boolean hasSelectedDrawnShape(Point2D p) {
        for (int i=0;i<hep.getNumShapes();i++){
            Shape s=hep.getHoles().getShape(i,hep.previewArea);
            if (s.contains(p)) {
                return true;
            }
        }
        return false;
    }
    
    private double distanceToNearest(double x, double y){
        EditableShape nearest=nearestLine(x,y);
        if (nearest!=null) return nearest.distanceTo(x,y);
        else return -1;
    }
    
    public void deleteSelected(boolean isCut){
        if (hasSelectedPoint()){
            joinAdjacentsToSelectedPoint();
            bSelectedPoint=false;
        }
        else{
            EditableShape current=null;
            Vector vShapesCopy=new Vector();
            boolean allSelected=true, noneSelected=true;
            vShapesCopy.addAll(vShapes);
            java.util.Enumeration e=vShapesCopy.elements();
            while (e.hasMoreElements()){
                current=(EditableShape)e.nextElement();
                if (!current.isSelected()) allSelected=false;
                else{
                    noneSelected=false;
                    if (isCut || vShapes.size()>=4){ //Si tenim 3 (o menys...) elements no n'esborrarem cap si no es un cut
                        vShapes.remove(current);
                        if (!isCut) joinAdjacentsTo(current,vShapes);
                    }
                }
            }
            //allSelected indica si estava seleccionat tot l'objecte
            if (allSelected || noneSelected){
                vShapes.clear();
                this.current=null;
                hep.getHoles().removeShape(hep.currentShape);
                hep.setCurrentShape(hep.getHoles().getNumCells());
            }
        }
    }
    
    private void joinAdjacentsTo(EditableShape current, Vector vShapes){
        //Totes les figures de vShapes convergiran en un dels punts de current.
        EditableShape s1=getAdjacent(vShapes,current,INITIAL);
        if (s1!=null){ //Sempre
            s1.hasClickedBorder(current.getInitialPoint().getX(),current.getInitialPoint().getY(),false);
            //hasClickedBorder marca la cantonada de la figura s1 mes propera al punt passat. En fer
            //changeBorder es modificarà aquesta cantonada pel nou punt.
            s1.changeBorder(current.getEndPoint().getX(),current.getEndPoint().getY());
        }
    }
    
    private void joinAdjacentsToSelectedPoint(){
        EditableShape current=null, other=null;
        if (vShapes.size()!=1 && vShapes.size()<=3) return;
        java.util.Enumeration e=vShapes.elements();
        int count=0;
        boolean bFinish=false;
        while (e.hasMoreElements() && !bFinish){
            current=(EditableShape)e.nextElement();
            if (current.hasSelectedBorder()){
                if (current instanceof EditableRectangle){
                    Point2D p=current.getNotSelectedBorder();
                    convertToSimpleShapes();
                    selectBorder(p.getX(),p.getY());
                    joinAdjacentsToSelectedPoint();
                    bFinish=true;
                }
                else{
                    count++;
                    if (count==1) other=current;
                    else{
                        Point2D p1=current.getNotSelectedBorder();
                        Point2D p2=other.getNotSelectedBorder();
                        vShapes.add(new EditableLine2D(p1,p2));
                        vShapes.remove(current);
                        vShapes.remove(other);
                    }
                }
            }
        }
        hep.repaint(0);
    }
    
    private void setEndToVector(double finX, double finY, Vector vRedrawingLines){
        //aproximem totes les linies de vRedrawingLines al punt finX, finY
        EditableShape current=null;
        java.util.Enumeration e=vRedrawingLines.elements();
        while (e.hasMoreElements()){
            current=(EditableShape)e.nextElement();
            current.aproximateNearestBorder(finX,finY);
        }
    }
    
    public boolean hasSelectedPoint(){
        return bSelectedPoint;
    }
    
/*    private void doZoom(double iniX, double iniY, double finX, double finY){
        double panelWidth=hep.getWidth();//
        double panelHeight=hep.getHeight();//
        zoomX=Math.min(iniX,finX);
        zoomY=Math.min(iniY,finY);
        zoomW=Math.abs(finX-iniX);
        zoomH=Math.abs(finY-iniY);
        if (zoomW<4 || zoomH<4){
            zoomH=-1; //No zoom
            return;
        }
        viewIniX=0;////((JScrollPane)container).getViewport().getViewPosition().getX();
        viewIniY=0;////((JScrollPane)container).getViewport().getViewPosition().getY();
        double zoomRatio=(zoomW/zoomH);
        double aspectRatio=panelWidth/panelHeight;
        if (zoomRatio>aspectRatio){
            zoomH=(zoomW*panelHeight)/panelWidth;
        }
        else{
            zoomW=(zoomH*panelWidth)/panelHeight;
        }
    }    */
    
    public Vector getSelectedShapes(){
        Vector v=new Vector();
        java.util.Enumeration e=vShapes.elements();
        EditableShape current;
        while (e.hasMoreElements()){
            current=(EditableShape)e.nextElement();
            if (current.isSelected()) v.add(current);
        }
        return v;
    }
    
    public int getNumShapes(){
        return vShapes.size();
    }
    
    public void deleteCurrent(){
        clean();
        current=null;
        ////dsm.deleteCurrent();
    }
    
    public ShapeData getShapeData(){
        ShapeData sd=null;
        AffineTransform at=AffineTransform.getScaleInstance((1/hep.previewArea.getWidth()),(1/hep.previewArea.getHeight()));
        at.concatenate(AffineTransform.getTranslateInstance(-hep.previewArea.x,-hep.previewArea.y));
        if (getNumShapes()==1){ //És un Rectangle o una el.lipse
            //=EditableShape es=(EditableShape)vShapes.elements().nextElement(); //existeix
            EditableShape es=(EditableShape)((EditableShape)vShapes.elements().nextElement()).clone();
            es.transform(at);
            Shape s;
            if (es instanceof EditableEllipse2D)
                s=((EditableEllipse2D)es).getEllipse();
            else s=es;
            sd=ShapeData.getShapeData(s,false);
        }
        else{
            Vector v=getGeneralPath(); //Només tractarem el primer poligon que hi trobem (hauría de ser únic)
            if (v.size()>0){
                GeneralPath gp=(GeneralPath)(v.elements().nextElement());
                Shape s=gp.createTransformedShape(at);
                sd=ShapeData.getShapeData(s);
            }
        }
        return sd;
    }
    
    public void endPolygon(){
        endPolygon(false,true);
    }
    
    public void endPolygon(boolean changeShape, boolean updateList){
        endPolygon(changeShape,updateList,-1);
    }
    
    public void endPolygon(boolean changeShape, boolean updateList, int iNextShape){
        ShapeData sd=getShapeData();
        addCurrentDrawnBorders(sd);
        endPolygon(sd,changeShape,updateList,iNextShape);
        if (sd!=null) clean();
        bSelectedPoint=false;
    }
    
    private void addCurrentDrawnBorders(ShapeData sd){
        ////System.out.println("sd!=null?"+(sd!=null)+" hep!=null?"+(hep!=null)+" vDrawn!=null?"+(vDrawnBorders!=null));
        if (sd!=null && hep!=null){
            Shape s=sd.getShape(hep.previewArea);
            vDrawnBorders.addAll(getBorders(s));
        }
    }
    
    private void removeDrawnBorders(ShapeData sd){
        Shape s=sd.getShape(hep.previewArea);
        vDrawnBorders.removeAll(getBorders(s)); //Remove all elimina totes les aparicions dels elements que es pasen sobre el Vector (només interesa 1 ocurrencia)
            /*java.util.Vector vBorders=getBorders(s);
            if (vBorders!=null){
                java.util.Enumeration e=vBorders.elements();
                while (e.hasMoreElements()){
                    Rectangle r=(Rectangle)e.nextElement();
                    vDrawnBorders.remove(r);
                }
            }*/
    }
    
    public void endPolygon(ShapeData sd, boolean changeShape, boolean updateList,int iNextShape){
        //Emmagatzemem el poligon que s'acaba de crear/modificar. changeShape indica si "provenim" d'un tabulador.
        if (sd!=null){
            
            addCurrentDrawnBorders(sd);
            
            if (hep.currentShape<hep.getHoles().getNumCells()){ //Ha modificat el hep.currentShape
                hep.getHoles().modifyShape(hep.currentShape,sd);
                hep.updateView();
            }
            else{ //Ha creat un
                if (sd.comment==null || sd.comment.trim().length()==0) sd.comment=(""+hep.currentShape);
                hep.getHoles().addShape(sd);
                hep.updateList();
                hep.updateView();
            }
        }
        int iCurrentShape=hep.currentShape+1;
        if (changeShape){
            if (iNextShape>=0) iCurrentShape=iNextShape;
            else
                iCurrentShape=iCurrentShape%hep.getHoles().getNumCells();
        }
        else iCurrentShape=hep.getHoles().getNumCells(); //El proper serà un de nou
        if (hep.currentShape!=iCurrentShape) hep.setCurrentShape(iCurrentShape);
    }
    
    private void aplicateTransformation(AffineTransform aTransf, boolean needSelected){
        EditableShape current=null;
        java.util.Enumeration e=vShapes.elements();
        while (e.hasMoreElements()){
            current=(EditableShape)e.nextElement();
            if (!needSelected || current.isSelected()){
                current.transform(aTransf);
            }
        }
    }
    
    public void move(int xInc, int yInc, boolean needSelected, boolean moveAll){ //moveAll indica si volem moure també els que no estan actius
        AffineTransform aTransf=AffineTransform.getTranslateInstance(xInc,yInc);
        aplicateTransformation(aTransf, needSelected);
        ////if (moveAll) dsm.aplicateTransformation(aTransf);
        hep.repaint(0);
    }
    
    public void scale(double xInc, double yInc, boolean needSelected, boolean scaleAll){
        Point2D center=getCenter(scaleAll);
        AffineTransform aTransf=AffineTransform.getTranslateInstance(center.getX(),center.getY());
        aTransf.concatenate(AffineTransform.getScaleInstance(xInc,yInc));
        aTransf.concatenate(AffineTransform.getTranslateInstance(-center.getX(),-center.getY()));
        aplicateTransformation(aTransf, needSelected);
        ////if (scaleAll) dsm.aplicateTransformation(aTransf);
        hep.repaint(0);
    }
    
    public void rotate(double theta, boolean needSelected, boolean rotateAll){
        convertToSimpleShapes(); //Si és un rectangle, caldrà convertirlo a línies per rotar-lo.
        Point2D center=getCenter(rotateAll);
        AffineTransform aTransf=AffineTransform.getRotateInstance(theta,center.getX(),center.getY());
        aplicateTransformation(aTransf,needSelected);
        ////if (rotateAll) dsm.aplicateTransformation(aTransf);
        hep.repaint(0);
    }
    
    private Point2D getCenter(boolean cellCenter){
        //Retorna el punt mig de la figura que estem editant si cellCenter==false, sino, de la cel.la
        if (!cellCenter){
            EditableShape current=null;
            GeneralPath gp=new GeneralPath();
            java.util.Enumeration e=vShapes.elements();
            while (e.hasMoreElements()){
                current=(EditableShape)e.nextElement();
                gp.append(current,false);
            }
            Rectangle2D r=gp.getBounds(); //per calcular el punt mig
            return new Point2D.Double(r.getCenterX(),r.getCenterY());
        }
        else return new Point2D.Double(hep.getPreviewPanel().getX(),hep.getPreviewPanel().getY());
    }
    
    private void convertToSimpleShapes(){
        //Si la figura que està editant és un Rectangle/El.lipse el transforma a un conjunt de línies/cúbiques
        EditableShape current=null;
        java.util.Enumeration e=vShapes.elements();
        while (e.hasMoreElements()){
            current=(EditableShape)e.nextElement();
            if (current instanceof EditableRectangle){ //Si rotem una figura rectangular, la convertim abans en figures simples
                vShapes.remove(current);
                EditableShape[] lines=((EditableRectangle)current).divide(-1,-1,false); //Que no afegeixi cap punt
                for (int i=0;i<lines.length;i++) if (lines[i]!=null) vShapes.add(lines[i]);
            }
        }
    }
    
    private EditableShape getSelectedShape(boolean hasToBeALine){ //Retorna la Linia seleccionada si només n'hi ha una
        java.util.Enumeration e=vShapes.elements();
        EditableShape current, selected=null;
        int i=0;
        while (e.hasMoreElements() && i<2){
            current=(EditableShape)e.nextElement();
            if (current.isSelected()){
                if (!hasToBeALine || current instanceof EditableLine2D){
                    selected=current;
                    i++;
                }
                else{
                    i=2; //Perquè no faci res
                }
            }
        }
        if (i==1) return selected;
        else return null;
    }
    
    public void convertToBezier(){
        EditableShape selected=getSelectedShape(false);
        if (selected!=null){
            double x1=selected.getInitialPoint().getX();
            double y1=selected.getInitialPoint().getY();
            double x2=selected.getEndPoint().getX();
            double y2=selected.getEndPoint().getY();
            double ctrl1x=x1+((x2-x1)/3);
            double ctrl2x=x1+(2*((x2-x1)/3));
            double ctrl1y=y1+((y2-y1)/3);
            double ctrl2y=y1+(2*((y2-y1)/3));
            EditableCubicCurve2D bez=new EditableCubicCurve2D(x1,y1,ctrl1x,ctrl1y,ctrl2x,ctrl2y,x2,y2);
            bez.setSelected(true);
            vShapes.remove(selected);
            vShapes.add(bez);
        }
    }
    
    public void convertToQuad(){
        EditableShape selected=getSelectedShape(false);
        if (selected!=null){
            double x1=selected.getInitialPoint().getX();
            double y1=selected.getInitialPoint().getY();
            double x2=selected.getEndPoint().getX();
            double y2=selected.getEndPoint().getY();
            double ctrlx=x1+((x2-x1)/2);
            double ctrly=y1+((y2-y1)/2);
            EditableQuadCurve2D quad=new EditableQuadCurve2D(x1,y1,ctrlx,ctrly,x2,y2);
            quad.setSelected(true);
            vShapes.remove(selected);
            vShapes.add(quad);
        }
    }
    
    public void convertToLine(){
        EditableShape selected=getSelectedShape(false);
        if (selected!=null && !(selected instanceof EditableRectangle)){
            EditableLine2D line=new EditableLine2D(selected.getInitialPoint(),selected.getEndPoint());
            line.setSelected(true);
            vShapes.remove(selected);
            vShapes.add(line);
        }
        else if(selected!=null && selected instanceof EditableRectangle){ //Convertim un rectangle en quatre rectes
            vShapes.remove(selected);
            EditableShape[] lines=((EditableRectangle)selected).divide(-1,-1,false); //Que no afegeixi cap punt
            for (int i=0;i<lines.length;i++) if (lines[i]!=null) vShapes.add(lines[i]);
        }
    }
    
    public void notifyShapeChanged(){
        java.util.Enumeration e=vPointListeners.elements();
        while (e.hasMoreElements()){
            PointListener pl=(PointListener)e.nextElement();
            pl.shapeChanged();
        }
    }
    
    public void addPointListener(PointListener listener){
        vPointListeners.add(listener);
    }
    
    public void undoLastMove(Vector vRedrawingLines, Vector vRedrawingLinesBeforeModify){
        vShapes.removeAll(vRedrawingLines);
        vShapes.addAll(vRedrawingLinesBeforeModify);
        vRedrawingLines.clear();
    }
    
    private boolean isIntoArea(Vector vShapes, boolean move){
        boolean isInto=true;
        Rectangle2D r=new Rectangle2D.Double(hep.previewArea.getX()-1,hep.previewArea.getY()-1,hep.previewArea.getWidth()+2,hep.previewArea.getHeight()+2);
        java.util.Enumeration e=vShapes.elements();
        Point2D[] borders;
        while (e.hasMoreElements() && isInto){
            EditableShape es;
            if (!move) es=(EditableShape)e.nextElement();
            else{
                es=(EditableShape)((EditableShape)e.nextElement()).clone();
                es.transform(AffineTransform.getTranslateInstance(finX-iniX,finY-iniY));
            }
            
            borders=es.getBorders();
            if (borders==null) continue;
            for (int j=0;j<borders.length && isInto ;j++) isInto=r.contains(borders[j]);
        }
        return isInto;
    }
    
    private void joinPolygon(){
        if (vShapes.size()>=2){
            vShapes.add(new EditableLine2D(lastPoint.getX(),lastPoint.getY(),iniPoint.getX(),iniPoint.getY()));
            ////            endPolygon(false,true);
            //hep.shapeChanged();
        }
        else vShapes.clear();
        creatingPolygon=false;
        lastPoint=null;
        iniPoint=null;
        if (bSelectedPoint) deselectBorder();
        bSelectedPoint=false;
        hep.setDrawingMode(SELECTING);
    }
    
    public void mouseDragged(java.awt.event.MouseEvent mouseEvent) {
        if ((mouseEvent.getModifiers()&java.awt.event.MouseEvent.BUTTON1_MASK)==0) return; //No s'ha apretat el Botó 1
        Point2D mousePoint=getTransformedPoint(mouseEvent.getPoint(),true);
        
        if (bMoving) hep.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        //else hep.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        
        if (mousePoint.getX()<hep.previewArea.x || mousePoint.getY()<hep.previewArea.y || mousePoint.getX()>hep.previewArea.x+hep.previewArea.getWidth() || mousePoint.getY()>hep.previewArea.y+hep.previewArea.getHeight()){
            //System.out.println("mousePoint.getX()="+mousePoint.getX()+" hep.previewArea.x="+hep.previewArea.x+" mousePoint.getY()="+mousePoint.getY()+" hep.previewArea.y="+hep.previewArea.y);
            return;
        }
        
        vShapeBeforeModify=(drawingMode==SELECTING && !bMoving)?cloneVector(vRedrawingLines):cloneVector(vCopied);
        EditableShape near=null;
        
        Point2D nearDrawn=aproximationToDrawnBorder(mouseEvent.getX(),mouseEvent.getY());
        if (nearDrawn!=null && EditableShapeConstants.pointsOnGrid){
            finX=nearDrawn.getX();
            finY=nearDrawn.getY();
        }
        else{
            finX=mousePoint.getX();
            finY=mousePoint.getY();
        }
        
        if (creatingRect||creatingEllipse){
            near=aproximationToLine(finX,finY);
            hep.getPreviewPanel().repaint(0);
        }
        else if (bRedrawingLines){
            redrawingLines(finX,finY);
            near=aproximationToLine(finX,finY,vRedrawingLines); //estem sobre una cantonada de la figura no seleccionada near (com a mínim)
            hep.repaint(0);
        }
        else if (bMoving || esInterior(finX,finY)){
            //=bMoving=true;
            near=nearestLine(finX,finY);
            if (near!=null){
                double d=near.distanceTo(finX,finY);
                if (!bMoving && d>(EditableShapeConstants.selectLength/2))
                    cut(finX,finY);
            }
            hep.repaint(0);
        }
        if (creatingRect||creatingEllipse||bRedrawingLines){
            //if (near!=null || aproximationToDrawnBorder(finX-hep.previewArea.x,finY-hep.previewArea.y)!=null) hep.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//////////.HAND_CURSOR));
            if ((near!=null || nearDrawn!=null) && EditableShapeConstants.pointsOnGrid) hep.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            else /*if (!bRedrawingLines) */hep.setCursor(cursors[PEN_CURSOR]);//Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        boolean b=isIntoArea((drawingMode==SELECTING && !bMoving)?vRedrawingLines:vCopied,(bMoving));//
        if (!b) {
            if (drawingMode==SELECTING && !bMoving){
                undoLastMove(vRedrawingLines,vShapeBeforeModify);
                finX=lastFinX;
                finY=lastFinY;
            }
            if (bMoving){
                finX=lastFinX;
                finY=lastFinY;
            }
            ////System.out.println("!b vCopied.size()"+vCopied.size());
        }
        else {
            lastFinX=finX;
            lastFinY=finY;
        }
        
        if (bResizing) setResizingCursor(resizingDirection);
    }
    
    protected boolean esCantonada(double x, double y){
        EditableShape near=aproximationToLine(x,y);
        return (near!=null || aproximationToDrawnBorder(x,y)!=null);
    }
    
    protected boolean esSobreFigura(double x, double y){
        int minimumDistance=Math.max(Math.max(2,EditableShapeConstants.selectLength/2),EditableShapeConstants.gridWidth);
        double dist=distanceToNearest(x,y);
        return (dist>=0 && dist<minimumDistance);
    }
    
    protected boolean esInterior(double x, double y){
        return (current!=null && current.contains(x,y));
    }
    
    public void mouseMoved(java.awt.event.MouseEvent mouseEvent) {
        
        //System.out.println("("+mousePoint.getX()+","+mousePoint.getY()+")");
        double x,y;
        Point2D mousePoint=mouseEvent.getPoint();
        
        boolean esCantonada=esCantonada(mouseEvent.getPoint().getX(),mouseEvent.getPoint().getY());
        if (drawingMode==NEW_POINT || (esCantonada && EditableShapeConstants.pointsOnGrid)){
            x=mousePoint.getX();
            y=mousePoint.getY();
        }
        else{
            ////                mousePoint=getTransformedPoint(mousePoint,EditableShapeConstants.pointsOnGrid); //mustBeOnGrid
            x=mousePoint.getX();
            y=mousePoint.getY();
        }
        
        if (x<hep.previewArea.x || y<hep.previewArea.y || x>hep.previewArea.x+hep.previewArea.getWidth() || y>hep.previewArea.y+hep.previewArea.getHeight()) return;
        if (drawingMode!=NEW_POINT /*&& vShapes.size()!=0*/){
            if (esCantonada && (!creatingPolygon || EditableShapeConstants.pointsOnGrid)) hep.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            else if (creatingPolygon) hep.setCursor(cursors[PEN_CURSOR]);
            else if (!bMoving && esSobreFigura(x,y)) hep.setCursor(cursors[CIRCLE_CURSOR]);
            else if (esInterior(x,y)) hep.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            else hep.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        if (/*vShapes.size()!=0 &&*/ drawingMode==NEW_POINT){
            if (esSobreFigura(x,y)){
                bSpecialLine=true; //perquè de moment la pinti amb un altre color
                specialLine=nearestLine(x,y);
                //hep.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                hep.setCursor(cursors[PEN_CURSOR]);
                hep.repaint(0);
            }
            else{
                boolean willRepaint=bSpecialLine;
                bSpecialLine=false;
                hep.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                if (willRepaint) hep.repaint(0);
            }
        }
        if (bMoving){
            if (iniX==-1){
                deleteSelected(true);
                iniX=x;
                iniY=y;
            }
            finX=x;
            finY=y;
            hep.repaint(0);
        }
        if (creatingPolygon) {
            finX=x;
            finY=y;
            hep.repaint(0);
        }
        ////            if (creatingEllipse || creatingRect || creatingPolygon) hep.setCursor(cursors[PEN_CURSOR]);
        
        if (canResize){
            if (!bResizing){
                int resizing=getResizing(mousePoint);
                if (resizing!=NO_RESIZING) setResizingCursor(resizing);
            }
            else setResizingCursor(resizingDirection);
        }
    }
    
    protected int getResizing(Point2D mousePoint){
        if (!canResize) return NO_RESIZING;
        ShapeData sd=hep.getHoles().getEnclosingShapeData();
        Rectangle r=hep.getPreviewArea();
        double width=r.getWidth();
        double height=r.getHeight();
        //AffineTransform at=AffineTransform.getScaleInstance((1/hep.previewArea.getWidth()),(1/hep.previewArea.getHeight()));
        //at.concatenate(AffineTransform.getTranslateInstance(-hep.previewArea.x,-hep.previewArea.y));
        AffineTransform at=AffineTransform.getTranslateInstance(-hep.previewArea.x,-hep.previewArea.y);
        at.transform(mousePoint,mousePoint);
        if (mousePoint.getX()==(width-1) && mousePoint.getY()==(height-1)) return SOUTH_EAST;
        else if (mousePoint.getX()==(width-1)) return EAST;
        else if (mousePoint.getY()==(height-1)) return SOUTH;
        else return NO_RESIZING;
        //System.out.println("width:"+width+" height:"+height+" x="+mousePoint.getX()+" y="+mousePoint.getY());
    }
    
    protected void setResizingCursor(int resizing){
        if (resizing==EAST) hep.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR),false);
        else if (resizing==SOUTH) hep.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR),false);
        else if (resizing==SOUTH_EAST) hep.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR),false);
    }
    
    protected void selectBorder(double x, double y){
        java.util.Enumeration e=vShapes.elements();
        while (e.hasMoreElements()){
            EditableShape s=(EditableShape)e.nextElement();
            s.selectBorder(x,y);
        }
    }
    
    protected void deselectBorder(){
        java.util.Enumeration e=vShapes.elements();
        while (e.hasMoreElements()){
            EditableShape s=(EditableShape)e.nextElement();
            s.deselectBorder();
        }
    }
    
    protected boolean removeNullLines(java.util.Vector vRedrawingLines){
        //Elimina de vRedrawingLines aquelles línies que tinguin els dos punts a la mateixa coordenada (o aproximada)
        boolean canRemove=false;
        java.util.Enumeration e=vRedrawingLines.elements();
        while (e.hasMoreElements() && !canRemove){
            EditableShape s=(EditableShape)e.nextElement();
            if (s instanceof EditableLine2D){
                Point2D [] p=s.getBorders();
                if (p.length>1){
                    Rectangle r=new Rectangle((int)(p[0].getX())-(EditableShapeConstants.selectLength/2),(int)(p[0].getY())-(EditableShapeConstants.selectLength/2),EditableShapeConstants.selectLength,EditableShapeConstants.selectLength);
                    if (r.contains(p[1].getX(),p[1].getY())){
                        //Aquesta línia és prescindible
                        System.out.println("Aquesta línia és prescindible");
                        if (vShapes.size()>=4){ //Si tenim 3 (o menys...) elements no n'esborrarem cap si no es un cut
                            canRemove=true;
                            vShapes.remove(s);
                            joinAdjacentsTo(s,vShapes);
                        }
                    }
                }
            }
        }
        return canRemove;
    }
    
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        Point2D mousePoint=mouseEvent.getPoint();
        boolean bSobreFigura=esSobreFigura(mousePoint.getX(),mousePoint.getY());
        if (drawingMode!=NEW_POINT && drawingMode!=DRAWING_POLYGON && !bSobreFigura && selectDrawnShape(mousePoint) && !creatingPolygon){
            notifyShapeChanged();
            hep.repaint(0);
        }
        else if (drawingMode!=NEW_POINT && bSobreFigura && !creatingPolygon){
            EditableShape line=nearestLine(mousePoint.getX(),mousePoint.getY());
            if (line!=null){ //Vol seleccionar un fragment del polígon
                if (esCantonada(mousePoint.getX(),mousePoint.getY())) {
                    Point2D p=line.getNearestBorder(mousePoint.getX(),mousePoint.getY());
                    deSelectAll();
                    bSelectedPoint=true;
                    selectBorder(p.getX(),p.getY());
                    hep.repaint(0);
                }
                else{
                    if (bSelectedPoint) deselectBorder();
                    bSelectedPoint=false;
                    if (line.isSelected()) line.setSelected(false);
                    else{
                        if ((mouseEvent.getModifiers()&java.awt.event.MouseEvent.SHIFT_MASK)==0) deSelectAll();
                        line.setSelected(true);
                    }
                    notifyShapeChanged();
                    hep.repaint(0);
                }
            }
        }
        if (creatingPolygon){
            if (mouseEvent.getClickCount()==2) joinPolygon();
            else{
                
                mousePoint=mouseEvent.getPoint();
                
                EditableShape near=aproximationToLine(mousePoint.getX(),mousePoint.getY(),vRedrawingLines);
                Point2D nearDrawn=null;
                Point2D nearDrawnOther=aproximationToDrawnBorder(mousePoint.getX(),mousePoint.getY());
                if (near!=null){ // si hi ha cap que estigui aprop, aproximem el punt final
                    nearDrawn=near.getNearestBorder(mousePoint.getX(),mousePoint.getY());
                }
                
                if (nearDrawnOther!=null && EditableShapeConstants.pointsOnGrid){
                    finX=nearDrawnOther.getX();
                    finY=nearDrawnOther.getY();
                }
                else{
                    mousePoint=getTransformedPoint(mouseEvent.getPoint(),true);
                    finX=mousePoint.getX();
                    finY=mousePoint.getY();
                }
                
                if (lastPoint!=null){ // no és el primer punt
                    if (nearDrawn!=null && iniPoint.getX()==nearDrawn.getX() && iniPoint.getY()==nearDrawn.getY()){//Ha clicat sobre el punt inicial del poligon
                        if (vShapes.size()>=2) joinPolygon();
                    }
                    else{
                        ////Point2D nearDrawn=aproximationToDrawnBorder(finX,finY);
                        if (nearDrawn==null){ //No es pot repetir un punt
                            
                            //if (lastPoint.getX()!=finX || lastPoint.getY()!=finY){ //No s'admet una línia amb les mateixes coordenades com a punt inicial i final
                            vShapes.add(new EditableLine2D(lastPoint.getX(),lastPoint.getY(),finX,finY));
                            lastPoint=new Point2D.Double(finX,finY);
                        }
                    }
                }
                else { //és el primer punt
                    iniPoint=new Point2D.Double(finX,finY);
                    lastPoint=iniPoint;
                }
            }
        }
        
        //System.out.println("clicked");
        //if (creatingPolygon && mouseEvent.getClickCount()==2) joinPolygon();
    }
    
    public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
    }
    
    public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
    }
    
    public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
        Point2D mousePoint=getTransformedPoint(mouseEvent.getPoint(),drawingMode!=SELECTING); //si seleccionem no cal que el punt estigui a la graella
        int x=(int)mousePoint.getX();
        int y=(int)mousePoint.getY();
        
        //hep.addBorderShape(x,y);
        
        if (canResize){
            int resizing=getResizing(mousePoint);
            if (resizing!=NO_RESIZING){
                if (drawingMode!=SELECTING) hep.setDrawingMode(SELECTING);
                bResizing=true;
                resizingDirection=resizing;
            }
        }
        
        if (x<hep.previewArea.x || y<hep.previewArea.y || x>hep.previewArea.x+hep.previewArea.getWidth() || y>hep.previewArea.y+hep.previewArea.getHeight())
            return;
        if (bMoving){ //Està movent una figura i ha fet un CTRL+X
            paste(finX-iniX,finY-iniY);
            bMoving=false;
        }
        iniX=x;
        iniY=y;
        
        //System.out.println("hasSelectedDrawnShape(mousePoint)?"+hasSelectedDrawnShape(mouseEvent.getPoint()));
        
        if (drawingMode==SELECTING && !bMoving){
            clicatISeleccionada(x,y,false); //false: Encara que no estigui seleccionat, podem arrosegar
            if (vRedrawingLines.size()>0) bRedrawingLines=true; //Si hem clicat a una cantonada seleccionada, estem redibuixant linies
        }
        else if((drawingMode==DRAWING_RECT || drawingMode==DRAWING_ELLIPSE || drawingMode==DRAWING_POLYGON)&&!hasSelectedDrawnShape(mouseEvent.getPoint())){
            if (drawingMode==DRAWING_RECT) creatingRect=true;
            else if (drawingMode==DRAWING_ELLIPSE) creatingEllipse=true;
            else creatingPolygon=true;
            EditableShape near=aproximationToLine(x,y);
            Point2D pNear=aproximationToDrawnBorder(x,y);
            if (near!=null){
                pNear=near.getNearestBorder(x,y);
            }
            if (pNear!=null){
                iniX=pNear.getX();
                iniY=pNear.getY();
            }
            else{
                iniX=x;
                iniY=y;
            }
            finX=iniX;
            finY=iniY;
            hep.repaint(0);
        }
        else if(drawingMode==NEW_POINT){
            EditableShape lineToDivide=specialLine;
            EditableShape near=aproximationToLine(x,y,null);
            Point2D nearDrawn=null;
            boolean isSelect=false;
            if (near!=null)
                nearDrawn=near.getNearestBorder(x,y);
            if (drawingMode==NEW_POINT && (lineToDivide!=null && bSpecialLine && (nearDrawn==null || lineToDivide instanceof EditableEllipse2D || lineToDivide instanceof EditableCubicCurve2D || lineToDivide instanceof EditableQuadCurve2D))){ //Només s'afegeix un punt si no estem a sobre d'un altre
                divideShape(lineToDivide,x,y);
            }
            else{
                //isSelect=selectDrawnShape(mousePoint);
                isSelect=selectDrawnShape(mouseEvent.getPoint());
            }
            if (!isSelect){ //Ha agefit un punt o bé ha clicat fora de qualsevol forma
                hep.setDrawingMode(SELECTING);
            }
        }
        /*int resizing=getResizing(mousePoint);
        if (resizing!=NO_RESIZING){
            bResizing=true;
            resizingDirection=resizing;
        }*/
    }
    
    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
        if ((mouseEvent.getModifiers()&java.awt.event.MouseEvent.BUTTON1_MASK)==0){//No s'ha apretat el Botó 1
            return;
        }
        if (/*drawingMode==MOVING && */bMoving){
            paste(finX-iniX,finY-iniY);
            bMoving=false;
            deSelectAll();
        }
        
        Point2D mousePoint=mouseEvent.getPoint();
        
        EditableShape near=aproximationToLine(mousePoint.getX(),mousePoint.getY(),vRedrawingLines);
        Point2D nearDrawnPropi=null;
        Point2D nearDrawn=aproximationToDrawnBorder(mousePoint.getX(),mousePoint.getY()); //Cantonada d'un polígon no actiu
        if (near!=null || nearDrawn!=null) hep.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        else hep.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        if (near!=null){ // si hi ha cap que estigui aprop, aproximem el punt final
            nearDrawnPropi=near.getNearestBorder(mousePoint.getX(),mousePoint.getY()); //Cantonada del polígon actiu
        }
        
        mousePoint=getTransformedPoint(mouseEvent.getPoint(),true);
        if (!(mousePoint.getX()<hep.previewArea.x || mousePoint.getY()<hep.previewArea.y || mousePoint.getX()>hep.previewArea.x+hep.previewArea.getWidth() || mousePoint.getY()>hep.previewArea.y+hep.previewArea.getHeight())){
            if (nearDrawn!=null && EditableShapeConstants.pointsOnGrid){ //Només si es vol aproximar
                finX=nearDrawn.getX();
                finY=nearDrawn.getY();
            }
            else{
                finX=mousePoint.getX(); //Si el punter es troba dintre de l'àrea dibuixable, es manté aquest punt.
                finY=mousePoint.getY();
            }
        }
        
        if ((drawingMode==SELECTING && !bMoving && nearDrawnPropi!=null)|| (!creatingPolygon && vShapes.size()>1 && !validateShape())){
            boolean canRemove=removeNullLines(vRedrawingLines);
            if (!canRemove){
                undoLastMove(vRedrawingLines,vRedrawingLinesBeforeModify);
                finX=iniX;
                finY=iniY;
            }
        }
        else if (bRedrawingLines){
                /*if (nearDrawn!=null && EditableShapeConstants.pointsOnGrid){ //Només si es vol aproximar
                    finX=nearDrawn.getX();
                    finY=nearDrawn.getY();
                }*/
            setEndToVector(finX,finY,vRedrawingLines);
            bRedrawingLines=false;
            vRedrawingLines.clear();
            vRedrawingLinesBeforeModify.clear();
        }
        if (creatingRect){
            creatingRect=false;
            vShapes.add(new EditableRectangle((int)iniX,(int)iniY,(int)(finX-iniX),(int)(finY-iniY)));
            if (hep.currentShape>=hep.getHoles().getNumCells()){ //Reservem l'espai pel rectangle quan es confirmi (per poder posar-li nom)
                //hep.updateList();
                ShapeData sd=new ShapeData();
                if (sd.comment==null || sd.comment.trim().length()==0) sd.comment=(""+hep.currentShape);
                hep.getHoles().addShape(sd);
                hep.updateList();
            }
        }
        if (creatingEllipse){
            creatingEllipse=false;
            vShapes.add(new EditableEllipse2D((int)iniX,(int)iniY,(int)(finX-iniX),(int)(finY-iniY)));
            if (hep.currentShape>=hep.getHoles().getNumCells()){ //Reservem l'espai pel rectangle quan es confirmi (per poder posar-li nom)
                //hep.updateList();
                ShapeData sd=new ShapeData();
                if (sd.comment==null || sd.comment.trim().length()==0) sd.comment=(""+hep.currentShape);
                hep.getHoles().addShape(sd);
                hep.updateList();
            }
        }
        
        if (bResizing){
            if (resizingDirection!=NO_RESIZING){
                double x=mousePoint.getX();
                double y=mousePoint.getY();
                double xInc=x-iniX;
                double yInc=y-iniY;
                if (resizingDirection==EAST) yInc=0;
                else if (resizingDirection==SOUTH) xInc=0;
                hep.incDrawingArea(xInc,yInc);
            }
            bResizing=false;
        }
        
        if (drawingMode==SCANNING_SHAPE){
            //System.out.println("x="+(int)mouseEvent.getPoint().getX()+","+(int)mouseEvent.getPoint().getY());
            hep.addBorderShape((int)mouseEvent.getPoint().getX(),(int)mouseEvent.getPoint().getY());
        }
        else{
            ShapeData sd=getShapeData();
            current=(sd!=null)?sd.getShape(hep.previewArea):null; //Actualitzem les modificacions al shape
            bSelectingArea=false;
            hep.repaint(0);
            if (!creatingPolygon)
                notifyShapeChanged();
        }
    }
    
    public class KeyHandler extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            if (e.getKeyCode()==KeyEvent.VK_DELETE){
                //System.out.println("delete");
                deleteSelected(false);
                hep.shapeChanged();
            }
        }
    }
    
    private double viewIniX=-1;
    private double viewIniY=-1;
    
}
