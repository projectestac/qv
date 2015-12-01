/*
 * Gradient.java
 *
 * Created on 6 de marzo de 2002, 15:11
 */

package edu.xtec.jclic.misc;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import edu.xtec.util.Domable;
import edu.xtec.util.JDomUtility;

/**
 *
 * @author Francesc Busquets (fbusquets@pie.xtec.es)
 * @version 1.0
 */
public class Gradient extends Object implements Cloneable, Domable{
    
    protected Color c1, c2;
    protected int angle=0;
    protected GradientPaint gp=null;
    protected Rectangle2D lastRect=null;    
    protected boolean transparency=false;
    protected int cycles=1;

    /** Creates new Gradient */
    public Gradient(Color c1, Color c2, int angle, int cycles) {
        setColors(c1, c2);
        setAngle(angle);
        setCycles(cycles);
    }        
    
    public Gradient(){
        this(Color.black, Color.white, 0, 1);
    }
    
    public Object clone(){
        return new Gradient(c1, c2, angle, cycles);
    }
    
    public boolean hasTransparency(){
        return transparency;
    }
    
    public void setCycles(int cycles){
        this.cycles=Math.max(1, cycles);
        clear();
    }
    
    public int getCycles(){
        return cycles;
    }
    
    public void setAngle(int angle){
        this.angle=angle % 360;
        clear();
    }
    
    public int getAngle(){
        return angle;
    }
    
    public Color[] getColors(){
        return new Color[] {c1, c2};
    }
    
    public void setColors(Color c1, Color c2){
        this.c1=c1;
        this.c2=c2;
        transparency=(c1.getAlpha()!=255 || c2.getAlpha()!=255);
        clear();
    }
    
    public void clear(){
        gp=null;
        lastRect=null;
    }
    
    public static final String ELEMENT_NAME="gradient";
    public static final String C1="source", C2="dest", ANGLE="angle", CYCLES="cycles";
    
    public org.jdom.Element getJDomElement(){
        org.jdom.Element e=new org.jdom.Element(ELEMENT_NAME);
        e.setAttribute(C1, JDomUtility.colorToString(c1));
        e.setAttribute(C2, JDomUtility.colorToString(c2));
        e.setAttribute(ANGLE, Integer.toString(angle));
        if(cycles>1)
            e.setAttribute(CYCLES, Integer.toString(cycles));
        return e;
    }
    
    public static Gradient getGradient(org.jdom.Element e) throws Exception {        
        Gradient g=new Gradient();
        g.setProperties(e, null);
        return g;
    }
    
    public void setProperties(org.jdom.Element e, Object aux) throws Exception {        
        JDomUtility.checkName(e, ELEMENT_NAME);        
        c1=JDomUtility.getColorAttr(e, C1, c1);
        c2=JDomUtility.getColorAttr(e, C2, c2);
        setAngle(JDomUtility.getIntAttr(e, ANGLE, angle)); 
        setCycles(JDomUtility.getIntAttr(e, CYCLES, cycles));
    }
    
    public void paint(Graphics2D g2, Shape shape){
        Paint p=g2.getPaint();
        g2.setPaint(getGradient(shape));
        g2.fill(shape);
        g2.setPaint(p);
    }
    
    public GradientPaint getGradient(Shape shape){        
        Rectangle2D r=shape.getBounds2D();
        if(gp==null || lastRect==null || !lastRect.equals(r)){
            lastRect=r;
            double rw=r.getWidth();
            double rh=r.getHeight();
            double radius=rw/2;
            // angle in radians
            double alpha=(Math.PI*angle)/180.0;
            switch(angle){
                case 0:
                case 180:
                    break;
                    
                case 90:
                case 270:
                    radius=rh/2;
                    break;
                    
                default:
                    // diagonal of r
                    radius=Math.sqrt(rw*rw+rh*rh)/2;
                    // diagonal angle
                    double gamma=toFirstQuadrant(Math.atan2(rh, rw));
                    // diagonal vs. alpha
                    double beta=Math.abs(toFirstQuadrant(alpha)-gamma);
                    // projected radius
                    radius*=Math.cos(beta);
            }
            float cx=(float)(r.getX()+rw/2);
            float cy=(float)(r.getY()+rh/2);
            float px=(float)(radius*Math.cos(alpha));
            float py=(float)(radius*Math.sin(alpha));
            gp=new GradientPaint(cx+px, cy-py, c1, cx+px-2*px/cycles, cy-py+2*py/cycles, c2, cycles>1);
        }
        return gp;
    }
    
    protected double toFirstQuadrant(double a){
        if(a>Math.PI)
            a=2*Math.PI-a;
        if(a>Math.PI / 2)
            a=Math.PI - a;
        return a;
    }
    
    public boolean equals(Object obj){
        boolean result=false;
        if(obj!=null && obj instanceof Gradient){
            Gradient g=(Gradient)obj;
            result=
            c1.equals(g.c1) &&
            c2.equals(g.c2) &&
            cycles==g.cycles &&
            angle==g.angle;
        }
        return result;
    }

}
