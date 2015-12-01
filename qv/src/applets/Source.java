//package applets;

import java.awt.Cursor;
import java.awt.*;
import java.awt.geom.*;

public class Source implements ImageObserver, java.awt.event.MouseMotionListener, java.awt.event.MouseListener{
	
	String ident;
	java.awt.Image i;
	String text;
	String img_url;
	QVDragDrop dd;
	String sAlign;
	int x,y,width,height;
	int minWidth, minHeight; //Dimensions mínimes del Source
	int maxWidth, maxHeight; //Dimensions màximes del Source
	int nrotate; //Els graus que es pot moure el Source han de ser múltiples de nrotate 
	double nratio; //El factor amb el que es pot escalar el Source ha de ser múltiple de nratio
	
	double angleRad=0;
	Target target; //On es troba el source
	//ImageLoader imgL=null;
	
	boolean finish=false; //Perquè deixi de carregar imatges
	
	boolean isMoving;
	boolean isRotating;
	boolean isScaling;
	
	public Source(QVDragDrop dd, String ident, String img_url, String text, String sAlign, int x, int y, int width, int height){
		this.img_url=img_url;
		this.ident=ident;
		this.text=text;
		this.sAlign=sAlign;
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.dd=dd;
		
		this.minWidth=20;
		this.minHeight=20;
		this.maxWidth=-1;
		this.maxHeight=-1;
		
		this.nrotate=1;
		this.nratio=0;

		lastX=-1;
		lastY=-1;
		iniX=x;
		iniY=y;
		iniAng=0;
		realWidth=width;
		realHeight=height;
		
		isMoving=false;
		isRotating=false;
		isScaling=false;
		
	}
	
	public void setRotate(int deg){
		angleRad = Math.toRadians(deg);
	}
	
	public void setRatio(double ratio){
		// En el cas del text, no es te per què conéixer la mida abans de pintar-lo, es guarda la dada per quan es pinti per primer cop
		this.ratioPendent = ratio;
	}
	
	public void setImage(java.awt.Image i){
		this.i = i;
	}
	
	public String getIdent(){
		return ident;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public int getRotateDegrees(){
		int deg = (int)Math.toDegrees(angleRad);
		return deg;
	}
	
	public double getScale(){
		return ((double)width/(double)realWidth);
	}
	
	public boolean hasTarget(){
		//Retorna si ja està associat;
		return (target!=null);
	}
	
	public Target getTarget(){
		return target;
	}
	
	public void setTarget(Target t){
		target=t;
	}
	
	public void setPosition(int x, int y){
		this.x=x;
		this.y=y;
		
		iniX=this.x;
		iniY=this.y;
		
	}
	
	public void setCenter(int x, int y){
		this.x=x-(width/2);
		this.y=y-(height/2);
		dd.c.repaint(this.x-10,this.y-10,width+20,height+20);
		
		
		iniX=this.x;
		iniY=this.y;
		
		//dd.c.repaint();
	}
	
	/**
	 * @return Returns the maxHeight.
	 */
	public int getMaxHeight() {
		return maxHeight;
	}
	/**
	 * @param maxHeight The maxHeight to set.
	 */
	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}
	/**
	 * @return Returns the maxWidth.
	 */
	public int getMaxWidth() {
		return maxWidth;
	}
	/**
	 * @param maxWidth The maxWidth to set.
	 */
	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}
	/**
	 * @return Returns the minHeight.
	 */
	public int getMinHeight() {
		return minHeight;
	}
	/**
	 * @param minHeight The minHeight to set.
	 */
	public void setMinHeight(int minHeight) {
		this.minHeight = minHeight;
	}
	/**
	 * @return Returns the minWidth.
	 */
	public int getMinWidth() {
		return minWidth;
	}
	/**
	 * @param minWidth The minWidth to set.
	 */
	public void setMinWidth(int minWidth) {
		this.minWidth = minWidth;
	}	
	/**
	 * @return Returns the nratio.
	 */
	public double getNratio() {
		return nratio;
	}
	/**
	 * @param nratio The nratio to set.
	 */
	public void setNratio(double nratio) {
		this.nratio = nratio;
	}
	/**
	 * @return Returns the nrotate.
	 */
	public int getNrotate() {
		return nrotate;
	}
	/**
	 * @param nrotate The nrotate to set.
	 */
	public void setNrotate(int nrotate) {
		this.nrotate = nrotate;
	}

	private static void drawShape(Graphics2D g2d, Shape s, AffineTransform at){
		PathIterator pi = s.getPathIterator(at);
		GeneralPath gp = new GeneralPath();
		gp.append(pi, false);
		g2d.draw(gp);
	}

	
	public boolean hasSelectedCorner(int xs, int ys){
		boolean selected = false;
		java.awt.geom.AffineTransform at = getTransform(x,y,width,height);
		Rectangle[] r = getBorders();
		for (int i=0;!selected && r!=null && i<r.length;i++){
			Rectangle rec = r[i];
			PathIterator pi = rec.getPathIterator(at);
			GeneralPath gp = new GeneralPath();
			gp.append(pi, false);
			selected = gp.contains(xs,ys);
		}
		return selected;
	}

	public boolean hasSelectedBorder(int xs, int ys){
		boolean selected = false;

		//Primer es calcula el punt (xs,ys) en coordenades sense rotar
		AffineTransform at = this.getInverseTransform(x, y, width, height);
		java.awt.geom.Point2D p = new java.awt.Point(xs,ys);
		java.awt.geom.Point2D p2 = new java.awt.Point();
		java.awt.geom.Point2D p3 = at.transform(p,p2);
		int xCoordIni = (int)p3.getX();
		int yCoordIni = (int)p3.getY();
		// (xCoordIni, yCoordIni) és el punt (xs,ys) en coordenades sense rotar

		/*double factorAmpliacio=((double)width)/((double)realWidth);
		if (xs>=(x-(ampleCostat*factorAmpliacio)) && xs<=(x+width+(ampleCostat*factorAmpliacio))){ //Ha seleccionat una linia horitzontal
			if (ys>=(y-(ampleCostat*factorAmpliacio)) && ys<=(y+(ampleCostat*factorAmpliacio))) selected = true;
			else if (ys>=(y+height-(ampleCostat*factorAmpliacio)) && ys<=(y+height+(ampleCostat*factorAmpliacio))) selected = true;
		}
		if (!selected && ys>=(y-(ampleCostat*factorAmpliacio)) && ys<=(y+height+(ampleCostat*factorAmpliacio))){
			if (xs>=(x-(ampleCostat*factorAmpliacio)) && xs<(x+(ampleCostat*factorAmpliacio))) selected = true;
			else if (xs>=(x+width-(ampleCostat*factorAmpliacio)) && xs<=(x+width+(ampleCostat*factorAmpliacio))) selected = true;
		}*/

		xs=xCoordIni;
		ys=yCoordIni;

		if (xs>=(x-ampleCostat) && xs<=(x+width+ampleCostat)){ //Ha seleccionat una linia horitzontal
			if (ys>=(y-ampleCostat) && ys<=(y+ampleCostat)) selected = true;
			else if (ys>=(y+height-ampleCostat) && ys<=(y+height+ampleCostat)) selected = true;
		}
		if (!selected && ys>=(y-ampleCostat) && ys<=(y+height+ampleCostat)){
			if (xs>=(x-ampleCostat) && xs<(x+ampleCostat)) selected = true;
			else if (xs>=(x+width-ampleCostat) && xs<=(x+width+ampleCostat)) selected = true;
		}
		return selected;
	}

	public Rectangle[] getBorders(){
		Rectangle[] r = new Rectangle[4];
		r[0] = new Rectangle(x-ampleCantonada, y-ampleCantonada, (ampleCantonada*2), (ampleCantonada*2));
		r[1] = new Rectangle(x-ampleCantonada, y-ampleCantonada+height, (ampleCantonada*2), (ampleCantonada*2));
		r[2] = new Rectangle(x-ampleCantonada+width, y-ampleCantonada, (ampleCantonada*2), (ampleCantonada*2));
		r[3] = new Rectangle(x-ampleCantonada+width, y-2+height, (ampleCantonada*2), (ampleCantonada*2));
		return r;
	}

	public GeneralPath[] getRotatedBorders(){
		/* Retorna les 4 cantonades actuals (ja rotades i escalades del Source) */
		GeneralPath [] gp = new GeneralPath[4];
		java.awt.geom.AffineTransform at = getTransform(x,y,width,height);
		Rectangle[] r = getBorders();
		for (int i=0;r!=null && i<r.length;i++){
			Rectangle rec = r[i];
			PathIterator pi = rec.getPathIterator(at);
			gp[i] = new GeneralPath();
			gp[i].append(pi, false);
			
		}
		return gp;
	}

	private void drawBorders(Graphics2D g2d, AffineTransform at){
		//if (i!=null){
			Color c = g2d.getColor();
			Color c2 = new Color(200,200,200);
			g2d.setColor(c2);
			Rectangle rec = new Rectangle(x,y,width,height);
			drawShape(g2d, rec, at);
			Rectangle[] r = getBorders();
			for (int i=0;r!=null && i<r.length;i++){
				rec = r[i];
				drawShape(g2d, rec, at);
			}
			g2d.setColor(c);
		//}
	}
	
	private AffineTransform getTransformImage(int x, int y, int width, int height){
		/* Retorna la transformació per posar la imatge (col·locada a 0,0) al seu lloc*/

		double factorAmpliacio=((double)width)/((double)realWidth);
		java.awt.geom.AffineTransform at = new java.awt.geom.AffineTransform();
		at.translate(x+(width/2),y+(height/2));
		at.rotate(angleRad);
		at.translate(-width/2,-height/2);
		at.scale(factorAmpliacio,factorAmpliacio); //Primer s'escala la imatge perquè width i height ja tenen aplicat l'escalat
		return at;
	}
	
	private AffineTransform getTransformText(int x, int y, int width, int height){
		/* Retorna la transformació per posar el text (col·locat a 0,0) al seu lloc*/

		double factorAmpliacio=((double)width)/((double)realWidth);
		java.awt.geom.AffineTransform at = new java.awt.geom.AffineTransform();
		at.translate(x+(width/2),y+(height/2));
		at.rotate(angleRad);
		at.translate(-width/2,height/2);
		at.scale(factorAmpliacio,factorAmpliacio); //Primer s'escala la imatge perquè width i height ja tenen aplicat l'escalat
		return at;
	}

	private AffineTransform getTransform(int x, int y, int width, int height){
		/* Retorna la transformació de qualsevol objecte (en coordenades abans de rotar) per possar-lo al seu lloc */
		java.awt.geom.AffineTransform at = new java.awt.geom.AffineTransform();
		at.translate(x+(width/2),y+(height/2));
		at.rotate(angleRad);
		at.translate(-(x+(width/2)),-(y+(height/2)));
		return at;
	}
	
	private AffineTransform getInverseTransform(int x, int y, int width, int height){
		/* Retorna la transformació que aplicada a un objecte en coordenades ja rotades, dona l'objecte en coordenades inicials */
		java.awt.geom.AffineTransform at = getTransform(x,y,width,height);
		try{
			at=at.createInverse();
		}
		catch (Exception e){
			e.printStackTrace(System.out);
		}
		return at;
	}
	
	public void paint(java.awt.Graphics g){
		if (g instanceof java.awt.Graphics2D){
			java.awt.Graphics2D g2d=(java.awt.Graphics2D)g;
			
			if (i!=null) {
				if (ratioPendent>0){
					// En el cas del text, no es te per què conéixer la mida abans de pintar-lo, es guarda la dada per quan es pinti per primer cop
					width=(int)(width*ratioPendent);
					height=(int)(height*ratioPendent);
					ratioPendent=0;
				}
				java.awt.geom.AffineTransform at = getTransformImage(x,y,width,height);
				g2d.drawImage(i,at,null);

			}
			if (text!=null){
				//System.out.println("width:"+width+" height:"+height);
				//java.awt.geom.Rectangle2D rect=g.getFontMetrics().getStringBounds(text,g);
				if (width<1 || height<1){
					java.awt.FontMetrics fm=g.getFontMetrics();
					height=(int)fm.getHeight();
					width=0;
					for (int i=0;i<text.length();i++) width+=fm.charWidth(text.charAt(i));
					realWidth=width;
					realHeight=height;
				}
				if (ratioPendent>0){
					// En el cas del text, no es te per què conéixer la mida abans de pintar-lo, es guarda la dada per quan es pinti per primer cop
					width=(int)(width*ratioPendent);
					height=(int)(height*ratioPendent);
					ratioPendent=0;
				}
				
				java.awt.geom.AffineTransform at = getTransformText(x,y,width,height);
				g2d.transform(at);
				//g.drawString(text,x,(int)(y+height));
				g.drawString(text,0,0);
				try{
					at=at.createInverse();
				}
				catch (Exception e){
					e.printStackTrace(System.out);
				}
				g2d.transform(at);
			}
			if (showBorders){
				java.awt.geom.AffineTransform at2 = getTransform(x,y,width,height);
				drawBorders(g2d, at2);
			}
			
		}
		
		
	}
	
	public boolean contains(int x, int y){
		//System.out.println("ini x="+x+" y="+y+" iniX="+iniX+" iniY="+iniY+" w="+width+" h="+height);
		//Agafo el punt i li aplico la transformació inversa

		java.awt.geom.AffineTransform at = getInverseTransform(iniX,iniY,width,height);
		
		java.awt.geom.Point2D p = new java.awt.Point(x,y);
		java.awt.geom.Point2D p2 = new java.awt.Point();
		java.awt.geom.Point2D p3 = at.transform(p,p2);
		x = (int)p3.getX();
		y = (int)p3.getY();
		//System.out.println("fi x="+x+" y="+y);
		boolean b=(x>=this.x && y>=this.y && x<=(this.x+width) && y<=(this.y+height));
		//if (b) System.out.println("text:"+text+" contains:"+b+" x="+x+" y="+y+" this.x="+this.x+" this.y="+this.y+" w="+width+" h="+height);

		return b;
	}
	
	private double getRad(int inicialX, int inicialY, int x, int y){
		/* Retorna l'angle en radians que representa el punt (x, y) sobre el centre del rectangle amb cantonada superior esquerra al punt (inicialX,inicialY) */
		int cx = inicialX+(width/2);
		int cy = inicialY+(height/2);
		double a = x-cx;//Math.abs(x-cx);
		double h = cy-y; //Math.abs(y-cy);
		double ang = 0;
		double deg = 0;
		if (a!=0){
			ang = Math.atan(h/a);
			//deg = Math.toDegrees(ang);
		}
		if (a>0){
//			ang = ang; //ang + (2*Math.PI); 
		}
		else if (a<0){
			ang = ang + Math.PI;
		}
		else { //a==0
			ang = (h>=0)?(Math.PI/2):((3*Math.PI)/2);
		}
		if (ang<0)
			ang += (2*Math.PI);
		deg = Math.toDegrees(ang);
		//System.out.println("deg:"+deg+" c=("+cx+","+cy+") p=("+x+","+y+") a="+a+" h="+h+" ang="+ang);
		//System.out.println("deg:"+deg+" rad:"+ang);
		return ang;
	}

	public int getPropWidth(int newHeight){
		double prop=((double)realWidth)/((double)realHeight);
		double newWidth=prop*((double)newHeight);
		return (int)newWidth;
	}
	
	public int getPropHeight(int newWidth){
		double prop=((double)realWidth)/((double)realHeight);
		double newHeight=((double)newWidth)/prop;
		return (int)newHeight;
	}
	
	public void imageLoaded(){
		dd.c.repaint();
	}
	
	public void mouseDragged(java.awt.event.MouseEvent e){
		if (isMoving){
			int incX=e.getX()-lastX;
			int incY=e.getY()-lastY;
			
			setPosition(x+incX,y+incY);
			lastX=e.getX();
			lastY=e.getY();
			
			//dd.c.repaint(x-(incX+2),y-(incY+2),width+4,height+4);
			dd.c.repaint();
		}
		if (isMoving){
			////Target t=dd.getTargetAt(e.getX(),e.getY());
			Target t=dd.getTargetAt(this);
			if (t!=null) dd.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			else dd.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}
		if (isRotating){
			lastX=e.getX();
			lastY=e.getY();
			
			double ang = this.getRad(x, y, lastX, lastY);
			this.angleRad = -ang-iniAng;
			if (this.angleRad>(2*Math.PI))
				this.angleRad-=2*Math.PI;
			if (this.angleRad<0)
				this.angleRad+=2*Math.PI;

			dd.c.repaint();
			dd.setCursor(dd.getRotateCursor()); ////
		}
		if (isScaling){
			//Els càlculs es fan com si el rectangle fos a la posició sense rotar i el punt que es clica també
			AffineTransform at = this.getInverseTransform(x, y, width, height);
			java.awt.geom.Point2D p = new java.awt.Point(e.getX(),e.getY());
			java.awt.geom.Point2D p2 = new java.awt.Point();
			java.awt.geom.Point2D p3 = at.transform(p,p2);
			int xCoordIni = (int)p3.getX();
			int yCoordIni = (int)p3.getY();
			// (xCoordIni, yCoordIni) és el punt actual del ratolí en coordenades sense rotar
			
			int distVertical=Math.min(Math.abs(xCoordIni-x),Math.abs(xCoordIni-(x+width))); //Distància mínima a les linies verticals del rectangle
			int distHoritzontal=Math.min(Math.abs(yCoordIni-y),Math.abs(yCoordIni-(y+height))); //Distància mínima a les linies horitzontals del rectangle

			int newWidth, newHeight;
			if (distVertical<=distHoritzontal){ //Vol modificar l'amplada
				newWidth = Math.max(Math.abs(xCoordIni-(x+width)),Math.abs(xCoordIni-x));
				int difWidth = newWidth-width;
				newWidth = newWidth+(2*(difWidth));
				newHeight = getPropHeight(newWidth);

				if (xCoordIni<=x+ampleCostat) //Està ampliant a l'oest
					dd.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));	
				else //Amplia a l'est
					dd.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));	
			}
			else{ //Vol modificar l'alçada
				newHeight = Math.max(Math.abs(yCoordIni-y),Math.abs(yCoordIni-(y+height)));
				int difHeight = newHeight-height;
				newHeight = newHeight+(2*(difHeight));
				newWidth = getPropWidth(newHeight);

				if (yCoordIni<=y+ampleCostat) //Està ampliant al nord
					dd.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));	
				else //Amplia al sud
					dd.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));	

			}

			Dimension d = getDimensionsSegonsLimits(newWidth, newHeight, false);
			newWidth = (int)d.getWidth();
			newHeight = (int)d.getHeight();
			//if(newWidth>=minWidth && newHeight>=minHeight && (maxWidth<=0 || newWidth<=maxWidth) && (maxHeight<=0 || newHeight<=maxHeight)){
			x = x+(width/2)-(newWidth/2);
			y = y+(height/2)-(newHeight/2);
			width = newWidth;
			height= newHeight;
			dd.c.repaint();
		}
	}
	
	public Dimension getDimensionsSegonsLimits(int newWidth, int newHeight, boolean aplicaNratio){
		// aplicaNratio indica si es vol que la dimensió que es retorna tingui en compte l'nratio.
		// Aquesta comprovació només cal der-la quan l'objecte ja no s'està escalant
		if (aplicaNratio && nratio>0){
			// S'aproxima la nova dimensió a la mida inicial mes el múltiple mes proper de nratio
			double n =(newWidth-realWidth)/(realWidth*nratio);
			long i = Math.round(n);
			double w1 = realWidth + (i*realWidth*nratio);
			double w2 = w1 + (realWidth*nratio);
			double d1 = newWidth - w1;
			double d2 = w2 - newWidth;
			if (d1<d2)
				newWidth = (int)w1;
			else
				newWidth = (int)w2;
			newHeight = getPropHeight(newWidth);
		}
		if (newWidth<minWidth){
			newWidth = minWidth;
			newHeight = getPropHeight(newWidth);
		} else if (maxWidth>0 && newWidth>maxWidth){
			newWidth = maxWidth;
			newHeight = getPropHeight(newWidth);
		}
		if (newHeight<minHeight){
			newHeight = minHeight;
			newWidth = getPropWidth(newHeight);
		} else if(maxHeight>0 && newHeight>maxHeight){
			newHeight = maxHeight;
			newWidth = getPropWidth(newHeight);
		}
		Dimension d = new Dimension(newWidth, newHeight);
		return d;
	}
	
	public void mouseMoved(java.awt.event.MouseEvent e){
		boolean contains=contains(e.getX(), e.getY());
		boolean border=dd.isScalingEnabled() && hasSelectedBorder(e.getX(), e.getY());
		boolean corner=dd.isRotatingEnabled() && hasSelectedCorner(e.getX(), e.getY());

		if (contains || border || corner){
			if (!showBorders){
				showBorders = true;
				dd.c.repaint();
			}
			//if (!modifiedCursor){
				modifiedCursor=true;
				if (corner){
					dd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					java.awt.Cursor c=dd.getRotateCursor();
					dd.setCursor(c);
				}
				else if(border){
					dd.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
				}
				else //Està a l'interior
					dd.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			//}
		}
		else if (showBorders && !isMoving && !isRotating && !isScaling){
			if (modifiedCursor){
				modifiedCursor=false;
				dd.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			showBorders=false;
			dd.c.repaint();
		}
	}
	
	public void mouseClicked(java.awt.event.MouseEvent e){}
	public void mouseEntered(java.awt.event.MouseEvent e){}
	public void mouseExited(java.awt.event.MouseEvent e){}
	
	public void mousePressed(java.awt.event.MouseEvent e){
	   if (!dd.isMoving() && !dd.isRotating() && !dd.isScaling()){
		if (dd.isRotatingEnabled() && hasSelectedCorner(e.getX(),e.getY())){ //Ha clicat sobre cantonada de rotació
			dd.setRotating(true);
			isRotating=true;
				
			//Calculo a on es troba el punt que ha seleccionat en coordenades no rotades 
			AffineTransform at = this.getInverseTransform(x, y, width, height);
			java.awt.geom.Point2D p = new java.awt.Point(e.getX(),e.getY());
			java.awt.geom.Point2D p2 = new java.awt.Point();
			java.awt.geom.Point2D p3 = at.transform(p,p2);
			int xCoordIni = (int)p3.getX();
			int yCoordIni = (int)p3.getY();
				
			//Angle que ha clicat sobre la vista no rotada
			double radClick = this.getRad(iniX, iniY, xCoordIni, yCoordIni);
			iniAng = (radClick>=(Math.PI/2) && radClick<((3*Math.PI)/2))?Math.PI:0;
				
			iniX=x;
			iniY=y;
			lastX=e.getX();
			lastY=e.getY();
				
			if (target!=null){
				target.removeSource(this);
				target=null;
			}
		}
		else if (dd.isScalingEnabled() && hasSelectedBorder(e.getX(),e.getY())){ //ha clicat al contorn, vol escalar
			dd.setScaling(true);
			isScaling=true;
				
			iniX=x;
			iniY=y;
		}
		else if (contains(e.getX(),e.getY())){ //Ha clicat a l'interior
			dd.setMoving(true);
			isMoving=true;
			
			
			//System.out.println("");
			iniX=x;
			iniY=y;
			startX=x;
			startY=y;
			lastX=e.getX();
			lastY=e.getY();
			
			if (target!=null){
				target.removeSource(this);
				target=null;
			}
		}
	   }
	}
	
	public void mouseReleased(java.awt.event.MouseEvent e){
		if (isMoving){
			isMoving=false;
			
			////Target t=dd.getTargetAt(e.getX(),e.getY());
			Target t=dd.getTargetAt(this);
			//System.out.println("last-> x="+lastX+" y="+lastY+"  event="+e.getX()+","+e.getY()+"  target="+t);
			if (t!=null){
				t.addSource(this, sAlign);
				setTarget(t);
			}else if (dd.bInside){ //Ha deixat el Source a fora de cap Target i només es pot moure a Target's
				setPosition(startX, startY);
			} else{
				if (e.getX()>dd.c.getSize().width || e.getY()>=dd.c.getSize().height){
					setPosition(iniX, iniY);
				}
			}
			dd.setMoving(false);
			dd.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			dd.c.repaint();
		}
		if (isRotating){
			isRotating=false;
			
			if (nrotate!=1){
				// S'aproxima l'angle de rotació al múltiple mes proper dels angles que es pot rotar
				double dRotateRad=Math.toRadians(nrotate);
				double n = angleRad/dRotateRad;
				long i = Math.round(n);
				double a1 = i*dRotateRad;
				double a2 = (i+1)*dRotateRad;
				double d1 = angleRad - a1; 
				double d2 = a2 - angleRad;
				if (d1<=d2)
					this.angleRad=a1;
				else
					this.angleRad=a2;
			}
			
			////Target t=dd.getTargetAt(e.getX(),e.getY());
			Target t=dd.getTargetAt(this);
			//System.out.println("last-> x="+lastX+" y="+lastY+"  event="+e.getX()+","+e.getY()+"  target="+t);
			if (t!=null){
				t.addSource(this, sAlign);
				setTarget(t);
			}else if (dd.bInside){
				setPosition(iniX, iniY);
			} else{
				if (e.getX()>dd.c.getSize().width || e.getY()>=dd.c.getSize().height){
					setPosition(iniX, iniY);
				}
			}
			dd.setRotating(false);
			dd.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			dd.c.repaint();
		}
		if (isScaling){
			isScaling=false;
			
			Dimension d = getDimensionsSegonsLimits(width, height, true);
			width = (int)d.getWidth();
			height = (int)d.getHeight();
			
			////Target t=dd.getTargetAt(e.getX(),e.getY());
			Target t=dd.getTargetAt(this);
			//System.out.println("last-> x="+lastX+" y="+lastY+"  event="+e.getX()+","+e.getY()+"  target="+t);
			if (t!=null){
				t.addSource(this, sAlign);
				setTarget(t);
			}
			/*else if (dd.bInside){
				setPosition(iniX, iniY);
			} else{
				if (e.getX()>dd.c.getSize().width || e.getY()>=dd.c.getSize().height){
					setPosition(iniX, iniY);
				}
			}*/
			dd.setScaling(false);
			dd.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			dd.c.repaint();
		}
	}
	
	private int lastX, lastY;
	private int iniX, iniY;
	private int startX, startY; //Coordenades inicials que té el Source quan es comença a moure
	private double iniAng;

	private double ratioPendent=0; // En el cas del text, no es te per què conéixer la mida abans de pintar-lo, es guarda la dada per quan es pinti per primer cop
	private int realWidth, realHeight;
	private boolean showBorders = false;

	private boolean modifiedCursor = false; //Inidica si aquest source ha modificat el cursor perquè s'ha situat al seu interior

	public static int ampleCantonada=3; //Meitat de l'amplada del quadrat que es mostra a les cantonades quan s'està rotant/escalant
	public static int ampleCostat=3; //Distància a la que es pot estar del contorn del rectangle per tal de modificar les seves dimensions
	
	

}