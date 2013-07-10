import java.util.Hashtable;

//package applets;

public class Target extends QVHotspotLabelJS{

	java.util.Vector vSource;
	QVDragDrop dd;
	int degrees = 0;

	public Target(QVDragDrop dd, String ident, String rarea, boolean rshuffle, String rrange, String text, Hashtable hStyle){
		super(ident, rarea, rshuffle, rrange, text, hStyle);
		vSource=new java.util.Vector();
		this.dd=dd;
	}
	
	public void setRotate(int degrees){
		this.degrees=degrees;
	}

	public int getRotate(){
		return degrees;
	}

	public void addSource(Source s){
		addSource(s, -1);
	}

	public void addSource(Source s, int iIndex){
		addSource(s, iIndex, "auto");
	}
	
	public void addSource(Source s, String sAlign){
		addSource(s, -1, sAlign);
	}
	
	public void addSource(Source s, int iIndex, String sAlign){
		vSource.addElement(s);
		//System.out.println(s.ident+"-> index="+iIndex+" width="+s.width+"  height="+s.height+" ("+coords[0]+","+coords[1]+","+coords[2]+","+coords[3]+")");
		if ("none".equalsIgnoreCase(sAlign)){
			if (iIndex>0){
				s.setCenter(coords[0]+(coords[2]/2),coords[1]+iIndex*s.height);
			}
		}else{
			s.setCenter(coords[0]+(coords[2]/2),coords[1]+(coords[3]/2));
		}
//		}else{
//			s.setCenter(s.iniX, s.iniY);
//			s.setCenter(coords[0]+(coords[2]/2),coords[1]+(coords[3]/2));
	}
	
	public void removeSource(Source s){
		vSource.removeElement(s);
	}
        
        public java.util.Vector getSources(){
            return vSource;
        }
	
	public void paint(java.awt.Graphics g){
		if (dd.showTargets())
			paintFilled(g, false, true, true);
	}


	private java.awt.geom.GeneralPath getGP(){
		java.awt.geom.GeneralPath gp = null;
        	if (getType()==ELLIPSE && coords!=null && coords.length==4){
            		java.awt.Rectangle rect=new java.awt.Rectangle(coords[0],coords[1],coords[2],coords[3]);
			gp = new java.awt.geom.GeneralPath(rect);
        	}
	        else if (getType()==RECTANGLE && coords!=null && coords.length==4){ //Rectangle
			java.awt.Rectangle rect=new java.awt.Rectangle(coords[0],coords[1],coords[2],coords[3]);
			gp = new java.awt.geom.GeneralPath(rect);
	        }
        	else if (p!=null){ //Bounded
			gp = new java.awt.geom.GeneralPath(p);
        	}
		return gp;
	}

	public boolean contains(Source s){
/*
- Fer un GeneralPath amb el Shape del Target
- Rotarlo els graus degrees i ampliar-lo un 20% per assegurar que els punts poden ser a dins
- Obtenir les cantonades rotades del Source
- Retornar true quan les 4 cantonades del source estiguin dins del GeneralPath
*/
		java.awt.geom.GeneralPath gp = getGP();

		//if (degrees!=0){
			java.awt.geom.AffineTransform at = new java.awt.geom.AffineTransform();
			double angleRad = Math.toRadians(degrees);
			java.awt.Rectangle r1=gp.getBounds();
			int cx=(int)(r1.getX()+(r1.getWidth()/2));
			int cy=(int)(r1.getY()+(r1.getHeight()/2));
			at.translate(cx,cy);
			at.rotate(angleRad);
			at.scale(1.25,1.25);
			at.translate(-cx,-cy);
			gp.transform(at);
		//}
		java.awt.geom.GeneralPath [] gpBorders = s.getRotatedBorders();
		boolean contains = true;
		for (int i=0;contains && gpBorders!=null && i<gpBorders.length; i++){
			java.awt.geom.GeneralPath current=gpBorders[i];
			java.awt.Rectangle r=current.getBounds();
			int x=(int)(r.getX()+(r.getWidth()/2));
			int y=(int)(r.getY()+(r.getHeight()/2));
			contains = gp.contains(x,y);
		}

        	return contains;
    	}

    	public void paintFilled(java.awt.Graphics g, boolean filled, boolean transp, boolean sel, Hashtable hStyle){
		java.awt.Graphics2D g2d=(java.awt.Graphics2D)g;
		java.awt.geom.AffineTransform atIni = g2d.getTransform();
		if (degrees!=0){
			java.awt.geom.GeneralPath gp = getGP();
			java.awt.geom.AffineTransform at = new java.awt.geom.AffineTransform();
			double angleRad = Math.toRadians(degrees);
			java.awt.Rectangle r1=gp.getBounds();
			int cx=(int)(r1.getX()+(r1.getWidth()/2));
			int cy=(int)(r1.getY()+(r1.getHeight()/2));
			at.translate(cx,cy);
			at.rotate(angleRad);
			at.translate(-cx,-cy);
			g2d.setTransform(at);
		}
		super.paintFilled(g, filled, transp, sel, hStyle);
		if (degrees!=0)
			g2d.setTransform(atIni);
	}
}