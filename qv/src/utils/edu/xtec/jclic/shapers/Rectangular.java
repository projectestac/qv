/*
 * RectangularShaper.java
 *
 * Created on 10 de mayo de 2001, 16:53
 */

package edu.xtec.jclic.shapers;


/**
 *
 * @author Francesc Busquets (fbusquets@pie.xtec.es)
 * @version 1.0
 */
public class Rectangular extends Shaper {

    
    public Rectangular(int nx,int ny){
        super(nx, ny);
    }
    
    public boolean rectangularShapes(){
        return true;
    }
    
    protected void buildShapes(){
        int r, c;
        double w=WIDTH/nCols;
        double h=HEIGHT/nRows;
        double x, y;
        
        for(r=0; r<nRows; r++){
            for(c=0; c<nCols; c++){
                ShapeData sh=shapeData[r*nCols+c];
                x=c*w; y=r*h;
                sh.moveTo(x, y);
                sh.lineTo(x+w, y);
                sh.lineTo(x+w, y+h);
                sh.lineTo(x, y+h);
                sh.lineTo(x, y);
                sh.closePath();
            }
        }
    }
}
