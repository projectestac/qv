/*
 * ShapeData.java
 *
 * Created on 12 de mayo de 2001, 0:13
 */

package edu.xtec.jclic.shapers;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.util.StringTokenizer;

import edu.xtec.util.JDomUtility;

/**
 *
 * @author  Montse
 * @version
 */
public class ShapeData extends Object implements Cloneable{
    
    public static final int CAPACITY_BLOCK=6;
    public double[] points;
    protected int pointsIndex;
    public int[] descriptors;
    public int descriptorsIndex;
    protected int windingRule;
    public int primitiveType;
    public double[] primitivePoints;
    public String comment;
    
    /** Creates new ShapeData */
    public ShapeData() {
        points=new double[2*CAPACITY_BLOCK];
        pointsIndex=0;
        descriptors=new int[CAPACITY_BLOCK];
        descriptorsIndex=0;
        windingRule=PathIterator.WIND_NON_ZERO;
        primitivePoints=null;
        primitiveType=-1;
        comment=null;
    }
    
    public static final String ELEMENT_NAME="shape", COMMENT="comment";
    public static final int RECTANGLE=0, ELLIPSE=1, ROUND_RECT=2, ARC=3, NUM_PRIMITIVES=4;
    public static final String[] PRIMITIVES={"rectangle", "ellipse", "roundRectangle", "pie"};
    public static final String RULE="rule", MOVE_TO="M", LINE_TO="L", QUAD_TO="Q", CUBIC_TO="B", CLOSE="X";
    public static final String DELIMS="|:,";
    public static final char[] DELIM_CHAR=DELIMS.toCharArray();
    
    public org.jdom.Element getJDomElement(double scaleW, double scaleH){
        org.jdom.Element e=new org.jdom.Element(ELEMENT_NAME);
        if(comment!=null && comment.length()>0)
            e.setAttribute(COMMENT, comment);
        StringBuffer sb=new StringBuffer();
        int j=0;
        if(primitiveType>=0 && primitivePoints!=null){
            writeToSb(sb, PRIMITIVES[primitiveType], primitivePoints, j, primitivePoints.length, scaleW, scaleH);
        }
        else{
            if(windingRule!=PathIterator.WIND_NON_ZERO)
                e.setAttribute(RULE, Integer.toString(windingRule));
            for(int i=0; i<descriptorsIndex; i++){
                String type=CLOSE;
                int k=0;
                if(i>0)
                    sb.append(DELIM_CHAR[0]);
                switch(descriptors[i]){
                    case PathIterator.SEG_MOVETO:
                        type=MOVE_TO; k=2;
                        break;
                    case PathIterator.SEG_LINETO:
                        type=LINE_TO; k=2;
                        break;
                    case PathIterator.SEG_QUADTO:
                        type=QUAD_TO; k=4;
                        break;
                    case PathIterator.SEG_CUBICTO:
                        type=CUBIC_TO; k=6;
                        break;
                    default:
                        break;
                }
                writeToSb(sb, type, points, j, k, scaleW, scaleH);
                j+=k;
            }
        }
        e.addContent(sb.substring(0));
        return e;
    }
    
    public static ShapeData getShapeData(org.jdom.Element e, double scaleW, double scaleH) throws Exception{
        
        JDomUtility.checkName(e, ELEMENT_NAME);
        
        ShapeData sd=new ShapeData();
        sd.comment=JDomUtility.getStringAttr(e, COMMENT, sd.comment, false);
        sd.setWindingRule(JDomUtility.getIntAttr(e, RULE, sd.windingRule));
        StringTokenizer st=new StringTokenizer(e.getText(), DELIMS);
        while(st.hasMoreTokens()){
            String s=st.nextToken();
            for(int k=0; k<NUM_PRIMITIVES; k++){
                if(s.equals(PRIMITIVES[k])){
                    double[] data=decodeData(st, k>=ROUND_RECT ? 6 : 4, scaleW, scaleH);
                    Shape sh=null;
                    switch(k){
                        case RECTANGLE:
                            sh=new Rectangle2D.Double(data[0], data[1], data[2], data[3]);
                            break;
                        case ELLIPSE:
                            sh=new Ellipse2D.Double(data[0], data[1], data[2], data[3]);
                            break;
                        case ROUND_RECT:
                            sh=new RoundRectangle2D.Double(data[0], data[1], data[2], data[3], data[4], data[5]);
                            break;
                        case ARC:
                            sh=new Arc2D.Double(data[0], data[1], data[2], data[3], data[4]*scaleW, data[5]*scaleH, Arc2D.PIE);
                            break;
                        default:
                            throw new Exception("unknown primitive shape!");
                    }
                    if(sh!=null)
                        return getShapeData(sh);
                }
            }
            if(s.equals(MOVE_TO)){
                sd.addDescriptor(PathIterator.SEG_MOVETO);
                sd.addData(decodeData(st, 2, scaleW, scaleH));
            }
            else if(s.equals(LINE_TO)){
                sd.addDescriptor(PathIterator.SEG_LINETO);
                sd.addData(decodeData(st, 2, scaleW, scaleH));
            }
            else if(s.equals(QUAD_TO)){
                sd.addDescriptor(PathIterator.SEG_QUADTO);
                sd.addData(decodeData(st, 4, scaleW, scaleH));
            }
            else if(s.equals(CUBIC_TO)){
                sd.addDescriptor(PathIterator.SEG_CUBICTO);
                sd.addData(decodeData(st, 6, scaleW, scaleH));
            }
            else if(s.equals(CLOSE)){
                sd.addDescriptor(PathIterator.SEG_CLOSE);
            }
            else{
                throw new IllegalArgumentException("Unknown ShapeData type: "+s);
            }
        }
        return sd;
    }
    
    private void writeToSb(StringBuffer sb, String type, double[] values, int j, int k, double scaleW, double scaleH){
        sb.append(type);
        if(k>0){
            sb.append(DELIM_CHAR[1]);
            for(int w=0; w<k; w++){
                String s=Double.toString(values[j++]*((j&1)!=0 ? scaleW : scaleH));
                if(s.endsWith(".0")) s=s.substring(0, s.length()-2);
                if(w>0)sb.append(DELIM_CHAR[2]);
                sb.append(s);
            }
        }
    }
    
    private void addDescriptor(int descriptor){
        if(descriptorsIndex+1>=descriptors.length){
            int[] d2=new int[descriptors.length+CAPACITY_BLOCK];
            for(int i=0; i<descriptorsIndex; i++) d2[i]=descriptors[i];
            descriptors=d2;
        }
        descriptors[descriptorsIndex++]=descriptor;
    }
    
    private void addData(double[] data){
        if(data==null) return;
        if(pointsIndex+data.length>=points.length){
            double[] d2=new double[points.length+2*CAPACITY_BLOCK];
            for(int i=0; i<pointsIndex; i++) d2[i]=points[i];
            points=d2;
        }
        for(int i=0; i<data.length; i++)
            points[pointsIndex++]=data[i];
    }
    
    private static double[] decodeData(StringTokenizer st, int count, double scaleW, double scaleH) throws Exception{
        double[] data=new double[count];
        for(int i=0; i<count; i++){
            data[i]=Double.parseDouble(st.nextToken())/((i&1)==0 ? scaleW : scaleH);
        }
        return data;
    }
    
    protected void add(int descriptor, double[] data){
        addDescriptor(descriptor);
        if(data!=null) addData(data);
    }
    
    public void moveTo(double x, double y){
        add(PathIterator.SEG_MOVETO, new double[]{x, y});
    }
    
    public void lineTo(double x, double y){
        add(PathIterator.SEG_LINETO, new double[]{x, y});
    }
    
    public void quadTo(double x0, double y0, double x1, double y1){
        add(PathIterator.SEG_QUADTO, new double[]{x0, y0, x1, y1});
    }
    
    public void cubicTo(double x0, double y0, double x1, double y1, double x2, double y2){
        add(PathIterator.SEG_CUBICTO, new double[]{x0, y0, x1, y1, x2, y2});
    }
    
    public void closePath(){
        add(PathIterator.SEG_CLOSE, null);
    }
    
    public void setWindingRule(int setRule){
        windingRule=setRule;
    }
    
    public void scaleTo(double scaleX, double scaleY){
        if(points!=null)
            for(int i=0; i<points.length; i+=2){
                points[i]/=scaleX;
                points[i+1]/=scaleY;
            }
        
        if(primitivePoints!=null)
            for(int i=0; i<primitivePoints.length; i+=2){
                primitivePoints[i]/=scaleX;
                primitivePoints[i+1]/=scaleY;
            }
    }
    
    public Shape getShape(Rectangle2D rect){
        return getShape(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }
    
    public Shape getShape(double dx, double dy, double scaleX, double scaleY){
        GeneralPath gp=new GeneralPath(windingRule, pointsIndex+1);
        int j=0;
        for(int i=0; i<descriptorsIndex; i++){
            switch(descriptors[i]){
                case PathIterator.SEG_MOVETO: gp.moveTo((float)(dx+scaleX*points[j++]), (float)(dy+scaleY*points[j++])); break;
                case PathIterator.SEG_LINETO: gp.lineTo((float)(dx+scaleX*points[j++]), (float)(dy+scaleY*points[j++])); break;
                case PathIterator.SEG_QUADTO: gp.quadTo((float)(dx+scaleX*points[j++]), (float)(dy+scaleY*points[j++]), (float)(dx+scaleX*points[j++]), (float)(dy+scaleY*points[j++])); break;
                case PathIterator.SEG_CUBICTO: gp.curveTo((float)(dx+scaleX*points[j++]), (float)(dy+scaleY*points[j++]), (float)(dx+scaleX*points[j++]), (float)(dy+scaleY*points[j++]), (float)(dx+scaleX*points[j++]), (float)(dy+scaleY*points[j++])); break;
                default: gp.closePath();
            }
        }
        return gp;
    }
    
    public static ShapeData getShapeData(Shape sh){
        return getShapeData(sh,true);
    }
    
    public static ShapeData getShapeData(Shape sh, boolean closeTo){
        ShapeData sd=new ShapeData();
        if(sh instanceof RectangularShape){
            //System.out.println("Rectangular!!!!");
            RectangularShape rs=(RectangularShape)sh;
            if(sh instanceof Rectangle2D){
                //System.out.println("rectangle x="+rs.getX()+" y="+rs.getY()+" w="+rs.getWidth()+" h="+rs.getHeight());
                sd.primitiveType=RECTANGLE;
                sd.primitivePoints=new double[]{rs.getX(), rs.getY(), rs.getWidth(), rs.getHeight()};
            }
            else if(sh instanceof Ellipse2D){
                //System.out.println("ellipse x="+rs.getX()+" y="+rs.getY()+" w="+rs.getWidth()+" h="+rs.getHeight());
                sd.primitiveType=ELLIPSE;
                sd.primitivePoints=new double[]{rs.getX(), rs.getY(), rs.getWidth(), rs.getHeight()};
            }
            else if(sh instanceof RoundRectangle2D){
                sd.primitiveType=ROUND_RECT;
                RoundRectangle2D rr=(RoundRectangle2D)sh;
                sd.primitivePoints=new double[]{rs.getX(), rs.getY(), rs.getWidth(), rs.getHeight(), rr.getArcWidth(), rr.getArcHeight()};
            }
            else if(sh instanceof Arc2D){
                Arc2D ar=(Arc2D)sh;
                if(ar.getArcType()==Arc2D.PIE){
                    sd.primitiveType=ARC;
                    sd.primitivePoints=new double[]{rs.getX(), rs.getY(), rs.getWidth(), rs.getHeight(), ar.getAngleStart(), ar.getAngleExtent()};
                }
            }
        }
        PathIterator it=sh.getPathIterator(null);
        double[] data=new double[6];
        sd.setWindingRule(it.getWindingRule());
        while(!it.isDone()){
            switch(it.currentSegment(data)){
                case PathIterator.SEG_MOVETO:
                    //System.out.println("MoveTo");
                    sd.moveTo(data[0], data[1]);
                    break;
                case PathIterator.SEG_LINETO:
                    //System.out.println("LineTo");
                    sd.lineTo(data[0], data[1]);
                    break;
                case PathIterator.SEG_QUADTO:
                    sd.quadTo(data[0], data[1], data[2], data[3]);
                    break;
                case PathIterator.SEG_CUBICTO:
                    sd.cubicTo(data[0], data[1], data[2], data[3], data[4], data[5]);
                    break;
                case PathIterator.SEG_CLOSE:
                    if (closeTo) sd.closePath();
                    break;
                default:
                    System.err.println("ShapeData error: Unknown PathIterator!");
            }
            it.next();
        }
        return sd;
    }
    
    public Object clone() throws CloneNotSupportedException{
        ShapeData sd=(ShapeData)super.clone();
        if(points!=null)
            sd.points=(double[])points.clone();
        if(descriptors!=null)
            sd.descriptors=(int[])descriptors.clone();
        if(primitivePoints!=null)
            sd.primitivePoints=(double[])primitivePoints.clone();
        return sd;
    }
    
}
