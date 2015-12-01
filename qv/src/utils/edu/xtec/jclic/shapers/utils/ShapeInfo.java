package edu.xtec.jclic.shapers.utils;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

import edu.xtec.jclic.shapers.ShapeData;

public class ShapeInfo{
    
    public static final int RECTANGLE=0;
    public static final int ELLIPSE=1;
    public static final int BOUNDED=2;
    
    String sIdent;
    //edu.xtec.jclic.shapers.EditableShape es;
    edu.xtec.jclic.shapers.ShapeData sd;
    
    public ShapeInfo(String sIdent, String sAreaType, String sArea){
        this.sIdent=sIdent;
        try{
            java.util.StringTokenizer st=new java.util.StringTokenizer(sArea,",");
            /* Si es produeix alguna excepció serà perquè el format no és correcte.
            No es pot corregir automàticament.*/
            
            Shape sh;
            int iType;
            if (sAreaType!=null && sAreaType.toLowerCase().trim().equals("ellipse")) iType=ELLIPSE;
            else if (sAreaType!=null && sAreaType.toLowerCase().trim().equals("rectangle")) iType=RECTANGLE;
            else iType=BOUNDED;
            
            if (iType==ELLIPSE || iType==RECTANGLE){
                int iIniX=(int)Double.parseDouble(st.nextToken());
                int iIniY=(int)Double.parseDouble(st.nextToken());
                int iWidth=(int)Double.parseDouble(st.nextToken());
                int iHeight=(int)Double.parseDouble(st.nextToken());
                if (iType==ELLIPSE){
                    sh=new Ellipse2D.Double(iIniX,iIniY,iWidth,iHeight);
                    sd=ShapeData.getShapeData(sh);
                }
                else{ //RECTANGLE
                    sh=new Rectangle2D.Double(iIniX,iIniY,iWidth,iHeight);
                    sd=ShapeData.getShapeData(sh);
                }
            }
            else{ //BOUNDED
                sd=new ShapeData();
                int iIniX=(int)Double.parseDouble(st.nextToken());
                int iIniY=(int)Double.parseDouble(st.nextToken());
                sd.moveTo(iIniX,iIniY);
                while (st.hasMoreTokens()){
                    int x=(int)Double.parseDouble(st.nextToken());
                    int y=(int)Double.parseDouble(st.nextToken());
                    sd.lineTo(x,y);
                }
                sd.closePath();
            }
            
        }
        catch (Exception e){
            System.out.println("Número incorrecte:"+sArea+" "+e.toString());
            Shape sh=new Rectangle2D.Double(0,0,0,0);
            sd=ShapeData.getShapeData(sh);
        }
        sd.comment=sIdent;
    }
    
    public ShapeInfo(String sIdent, edu.xtec.jclic.shapers.ShapeData sd){
        this.sIdent=sIdent;
        this.sd=sd;
    }
    
    public void setIdent(String sIdent){
        this.sIdent=sIdent;
    }
    
    public void setShape(edu.xtec.jclic.shapers.ShapeData sd){
        this.sd=sd;
    }
    
    public String getIdent(){
        return sIdent;
    }
    
    public String getType(){
        if (sd.primitiveType==ShapeData.RECTANGLE) return "rectangle";
        else if (sd.primitiveType==ShapeData.ELLIPSE) return "ellipse";
        else return "bounded";
        //return es.toString();
    }
    
    public String getArea(){
        if (sd.primitiveType==ShapeData.RECTANGLE || sd.primitiveType==ShapeData.ELLIPSE){
            double[] primitivePoints=sd.primitivePoints;
            return (int)primitivePoints[0]+","+(int)primitivePoints[1]+","+(int)primitivePoints[2]+","+(int)primitivePoints[3];
        }
        else{
            StringBuffer sb=new StringBuffer();
            int k=0;
            for(int i=0; i<sd.descriptorsIndex; i++){
                if(i>0 && sd.descriptors[i]!=PathIterator.SEG_CLOSE)
                    sb.append(',');
                switch(sd.descriptors[i]){
                    case PathIterator.SEG_MOVETO:
                        sb.append((int)sd.points[k]+","+(int)sd.points[k+1]);
                        k+=2;
                        break;
                    case PathIterator.SEG_LINETO:
                        sb.append((int)sd.points[k]+","+(int)sd.points[k+1]);
                        k+=2;
                        break;
                    case PathIterator.SEG_QUADTO:
                        // IMPLEMENTAR
                        k+=4;
                        break;
                    case PathIterator.SEG_CUBICTO:
                        // IMPLEMENTAR
                        k+=6;
                        break;
                    default:
                        break;
                }
            }
            return sb.toString();
        }
    }
    
    public edu.xtec.jclic.shapers.ShapeData getShape(){
        return sd;
    }
    
}
