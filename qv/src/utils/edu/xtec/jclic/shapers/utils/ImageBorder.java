package edu.xtec.jclic.shapers.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.PixelGrabber;
import java.util.Enumeration;
import java.util.Vector;

import edu.xtec.jclic.shapers.ShapeData;

public class ImageBorder{// extends Frame{
    
    protected Image i;
    int[] pixels;
    int width;
    int height;
    
    int range=40;
    float shapingFactor=3;
    float maxFactor=1.5f;
    
    public ImageBorder(Image i, Component c){
        this.i=i;
        getPixels(c);
    }
    
    public ShapeData getShapeData(int x, int y){
        Vector v=getBorder(x,y);
        if (v!=null) System.out.println("border.size()="+v.size());
        ShapeData sd=getShapes(v);
        return sd;
    }
    
    private void getPixels(Component c){
        int x=0;
        int y=0;
        width=i.getWidth(c);
        height=i.getHeight(c);
        
        pixels=new int[width*height];
        PixelGrabber pg = new PixelGrabber(i, x, y, width, height, pixels, 0, width);
        try {
            pg.grabPixels();
        }
        catch (InterruptedException e) {
            System.err.println("interrupted waiting for pixels!");
            return;
        }
        if ((pg.getStatus() & java.awt.image.ImageObserver.ABORT) != 0) {
            System.err.println("image fetch aborted or errored");
            return;
        }
    }
    
    private ShapeData getShapes(Vector vPoints){
        /* A partir d'una seqüència de punts retorna una agrupació d'aquests en
         forma de figura geomètrica*/
        if (vPoints==null) return null;
        ShapeData sd=new ShapeData();
        int iCurrentPoint=0;
        boolean bFi=false;
        while ((iCurrentPoint+1)<vPoints.size()){
            boolean bPassed=false;
            int i=1;
            for(i=1;(iCurrentPoint+i)<vPoints.size() && !bPassed;i++){
                float d=getPerdua(vPoints,iCurrentPoint, iCurrentPoint+i);
                bPassed=(d>maxFactor);
                //float d=getMaxPerdua(vPoints,iCurrentPoint, iCurrentPoint+i);
                //bPassed=(d>.5);
            }
            Line2D.Float l;
            Point2D p;
            if (iCurrentPoint==0){
                p=(Point2D)vPoints.elementAt(iCurrentPoint);
                sd.moveTo(p.getX(),p.getY());
            }
            if ((iCurrentPoint+i)<vPoints.size())
                p=(Point2D)vPoints.elementAt(iCurrentPoint+i);
            else
                p=(Point2D)vPoints.elementAt(iCurrentPoint+i-1);
            sd.lineTo(p.getX(),p.getY());
            
            iCurrentPoint=iCurrentPoint+i;
        }
        return sd;        
    }
    
    private float getPerdua(Vector vPoints, int startIndex, int endIndex){
        Line2D.Float l=new Line2D.Float((Point2D)vPoints.elementAt(startIndex),(Point2D)vPoints.elementAt(endIndex));
        float d=0;
        for (int i=startIndex+1;i<endIndex;i++) d+=l.ptSegDist((Point2D)vPoints.elementAt(i));
        return d;
    }
    
    private float getMaxPerdua(Vector vPoints, int startIndex, int endIndex){
        Line2D.Float l=new Line2D.Float((Point2D)vPoints.elementAt(startIndex),(Point2D)vPoints.elementAt(endIndex));
        double max=0;
        for (int i=startIndex+1;i<endIndex;i++) max=Math.max(max,l.ptSegDist((Point2D)vPoints.elementAt(i)));
        return (float)max;
    }
    
    private Vector getBorder(int x, int y){
        Vector vUsed=new Vector();
        Vector vBorder=new Vector();
        Point iniPoint=getIniPoint(x,y);
        Color c=getColor(x,y);
        boolean bFi=false;
        Point currentPoint=iniPoint;
        Point lastPoint=null;
        for (int i=0;i<5000 && !bFi;i++){
            Point p=getNextPoint(currentPoint,lastPoint,c,vUsed);
            if (p==null){ //Backtracking
                if (vBorder.size()>2){ // Es pot eliminar algun punt
                    currentPoint=(Point)vBorder.elementAt(vBorder.size()-2);
                    lastPoint=(Point)vBorder.elementAt(vBorder.size()-3);
                    vBorder.removeElementAt(vBorder.size()-1);
                }
                else return null; //No hi ha cap
            }
            else{
                bFi=p.equals(iniPoint);
                if (!bFi){
                    lastPoint=currentPoint;
                    currentPoint=p;
                    vUsed.add(p);
                    vBorder.add(p);
                    //System.out.println(i+": ("+p.getX()+","+p.getY()+")");
                }
            }
        }
        return vBorder;
    }
    
    private Point getIniPoint(int x, int y){
        Color c=getColor(x,y);
        boolean bFound=false;
        while (!bFound && y>=0){
            Color c2=getColor(x,y-1);
            if (!c.equals(c2)) bFound=true;
            else y--;
        }
        return new Point(x,y);
    }
    
    private Point getNextPoint(Point p, Point lastPoint, Color c, Vector vBorder){
        Point nextPoint=null;
        Vector v=getBorderSameColor((int)p.getX(),(int)p.getY(),c,lastPoint,vBorder);
        Enumeration e=v.elements();
        boolean bFound=false;
        while (e.hasMoreElements() && !bFound){
            Point p2=(Point)e.nextElement();
            bFound=hasBorderDistinctColor(p2,c);
            if (bFound) nextPoint=p2;
        }
        return nextPoint;
    }
    
    private boolean hasBorderDistinctColor(Point p, Color c){
        int x=(int)p.getX();
        int y=(int)p.getY();
        if (!c.equals(getColor(x,y-1))) return true;
        else if (!c.equals(getColor(x+1,y))) return true;
        else if (!c.equals(getColor(x,y+1))) return true;
        else if (!c.equals(getColor(x-1,y))) return true;
        else if (!c.equals(getColor(x-1,y-1))) return true;
        else if (!c.equals(getColor(x+1,y-1))) return true;
        else if (!c.equals(getColor(x+1,y+1))) return true;
        else if (!c.equals(getColor(x-1,y+1))) return true;
        else return false;
    }
    
    private Vector getBorderSameColor(int x, int y, Color c, Point lastPoint, Vector vBorder){
        Vector v=new Vector();
        
        Color c2=getColor(x,y-1);
        if (c.equals(c2)) v.add(new Point(x,y-1));
        c2=getColor(x+1,y);
        if (c.equals(c2)) v.add(new Point(x+1,y));
        c2=getColor(x,y+1);
        if (c.equals(c2)) v.add(new Point(x,y+1));
        c2=getColor(x-1,y);
        if (c.equals(c2)) v.add(new Point(x-1,y));
        
        c2=getColor(x-1,y-1);
        if (c.equals(c2)) v.add(new Point(x-1,y-1));
        c2=getColor(x+1,y-1);
        if (c.equals(c2)) v.add(new Point(x+1,y-1));
        c2=getColor(x+1,y+1);
        if (c.equals(c2)) v.add(new Point(x+1,y+1));
        c2=getColor(x-1,y+1);
        if (c.equals(c2)) v.add(new Point(x-1,y+1));
        
        if (lastPoint!=null) v.remove(lastPoint);
        if (vBorder!=null) v.removeAll(vBorder);
        return v;
    }
    
    protected java.awt.Color getColor(int x, int y){
        int i=(y*width)+x;
        if (i>=pixels.length || i<0) return new ExtendedColor(0,0,0,0);
        else{
            int pixel=pixels[i];
            int alpha = (pixel >> 24) & 0xff;
            int red   = (pixel >> 16) & 0xff;
            int green = (pixel >>  8) & 0xff;
            int blue  = (pixel      ) & 0xff;
            //System.out.println("Color ["+red+","+green+","+blue+"]");
            return new ExtendedColor(red,green,blue,range);
        }
    }
    
    public class ExtendedColor extends Color{
        int range;
        
        public ExtendedColor(int r, int g, int b, int range){
            super(r,g,b);
            this.range=range;
        }
        
        protected boolean isSimilar(Color c, int range){
            int red=getRed();
            int green=getGreen();
            int blue=getBlue();
            int red2=c.getRed();
            int green2=c.getGreen();
            int blue2=c.getBlue();
            if (Math.abs(red-red2)<=range && Math.abs(green-green2)<=range && Math.abs(blue-blue2)<=range) return true;
            else return false;
        }
        
        public boolean equals(Object c){
            if (range>0) return isSimilar((Color)c,range);
            else return super.equals(c);
        }
        
    }
}
