/*
 * TriangularJigSaw.java
 *
 * Created on 14 de mayo de 2001, 13:51
 */

package edu.xtec.jclic.shapers;

/**
 *
 * @author Francesc Busquets (fbusquets@pie.xtec.es)
 * @version 1.0
 */
public class TriangularJigSaw extends JigSaw {

    /** Creates new TriangularJigSaw */
    public TriangularJigSaw(int nx,int ny) {
        super(nx, ny);
    }

    protected void hLine(ShapeData sd, int type, double x, double y, double w, double h, boolean inv){
        int kx=inv ? -1 : 1;
        int ky=(type==1 ? 1 : -1);
        
        if(type==0){
            sd.lineTo(x+w*kx, y);
        }
        else{
            double x0=x+((w-w*baseWidthFactor)/2)*kx;
            double wb=w*baseWidthFactor*kx;
            sd.lineTo(x0, y);
            double hb=(h*toothHeightFactor)*ky;
            sd.lineTo(x0+wb/2, y+hb);
            sd.lineTo(x0+wb, y);
            sd.lineTo(x+w*kx, y);
        }
    }
    
    protected void vLine(ShapeData sd, int type, double x, double y, double w, double h, boolean inv){
        int ky=inv ? -1 : 1;
        int kx=(type==1 ? 1 : -1);
        
        if(type==0){
            sd.lineTo(x, y+h*ky);
        }
        else{
            double y0=y+((h-h*baseWidthFactor)/2)*ky;
            double hb=h*baseWidthFactor*ky;
            sd.lineTo(x, y0);
            double wb=w*toothHeightFactor*kx;
            sd.lineTo(x+wb, y0+hb/2);
            sd.lineTo(x, y0+hb);
            sd.lineTo(x, y+h*ky);
        }
    }    
}
