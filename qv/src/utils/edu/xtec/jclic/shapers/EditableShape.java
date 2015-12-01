/*
 * EditableShape.java
 *
 * Created on 25 / febrer / 2002, 08:55
 */

package edu.xtec.jclic.shapers;

/**
 *
 * @author  allastar
 * @version
 */

public interface EditableShape extends java.awt.Shape{
    
    public void setSelected(boolean b); //selecciona/deselecciona la figura
    public boolean isSelected(); //retorna si la figura està sel.lecionada
    public void paint(java.awt.Graphics g, int drawingMode); //dibuixa la figura amb el color per defecte. Si drawingMode==ShapeGenPanel.NEW_POINT pinta les cantonades de color vermell.
    public void paintWithColor(java.awt.Graphics g, int drawingMode, java.awt.Color c); //dibuixa la figura amb el color indicat. Si drawingMode==ShapeGenPanel.NEW_POINT pinta les cantonades de color vermell.
    public void paintSelection(java.awt.Graphics g); //dibuixa les cantonades en color negre i la resta amb el color de selecció (EditableShapeConstants.selectedColor).
    public void drawBorders(java.awt.Graphics g); //Dibuixa els costats arrosegables de la figura
    public boolean hasClickedBorder(double x, double y, boolean needSelected); //retorna true si a la posició (x,y) hi ha un costat i si needSelected==true cal a mes que estigui seleccionat
    public void selectBorder(double x, double y);
    public java.awt.geom.Point2D getNearestBorder(double x, double y); //retorna les coordenades (el punt) del la cantonada mes propera a (x,y)
    public void aproximateNearestBorder(double x, double y); //estableix a (x,y) les coordenades de la cantonada mes propera a (x,y).
    public double distanceTo(double x,double y); //retorna la distància a (x,y) de la cantonada mes propera a (x,y).
    public boolean isInto(java.awt.geom.Rectangle2D r); //retorna true si el rectangle r intersecta la figura.
    public void changeBorder(double x, double y); //estableix a (x,y) les coordenades de la cantonada previament marcada. (És convenient que hasClickedBorder marqui aquesta cantonada)
    public void deselectBorder();
    public java.awt.geom.Point2D getNotSelectedBorder();
    public boolean hasSelectedBorder();
    public void transform(java.awt.geom.AffineTransform aTransf); //aplica una transformació afí a la figura (rotacions, traslacions, canvis d'escala, girs, ...).
    public EditableShape[] divide(double x, double y); //es vol dividir la figura sent (x,y) el punt "significant" de la divisió. Retorna un array amb les noves figures.
    public boolean isAdjacentTo(java.awt.geom.Point2D p); //En el cas que sigui adjacent al punt final, intercanvia els punts inicial i final
    public java.awt.geom.Point2D getInitialPoint(); //retorna un punt de la figura que es considera com a punt inicial. S'utilitza per establir el camí per dibuixar la figura.
    public java.awt.geom.Point2D getEndPoint(); //retorna un punt de la figura que es considera com a punt final. S'utilitza per establir el camí per dibuixar la figura.
    public java.awt.geom.Point2D[] getBorders();
    public Object clone(); //retorna una còpia de la figura
    public String getArea();
}
