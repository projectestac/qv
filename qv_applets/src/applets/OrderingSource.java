import java.awt.Cursor;

/*
 * OrderingSource.java
 *
 * Created on 16 / juny / 2003, 08:56
 */

//package applets;

/**
 *
 * @author  allastar
 */
public class OrderingSource implements java.awt.event.MouseMotionListener, java.awt.event.MouseListener{
    
    String ident;
    java.awt.Image i;
    String text;
    String img_url;
    QVOrdering ord;
    int x,y,width,height;
    int orientation;
    
    boolean isMoving=false;
    
    int position=-1;
    
	/** Creates a new instance of OrderingSource */
	public OrderingSource(QVOrdering ord, String ident, String img_url, String text, int x, int y, int width, int height, int orientation){
		this.img_url=img_url;
		this.ident=ident;
		this.text=text;
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.ord=ord;
		this.orientation=orientation;
        
		//if (img_url!=null){
			//i=ord.getImage(ord.getResourceURL(img_url));
			/////new ImageLoader(i,this).start();
		//}
        
		if (text!=null){
			if (width<1 || height<1){
				java.awt.Font font = ord.getFont();
				java.awt.FontMetrics fm = ord.getFontMetrics(font);
				this.height=(int)fm.getHeight();
				this.width=0;
				for (int i=0;i<text.length();i++) this.width+=fm.charWidth(text.charAt(i));
				this.width+=6; //marge
				//this.height+=6;
			}
		}
		//System.out.println("Source. url="+img_url+" text:"+text+"<--");
	}
	
    /*public void loadImage(){
        java.net.URL url=ord.getImage(ord.getResourceURL(img_url));
        if (img_url!=null)
            return getResourceURL(img_url);
        else
            return null;
    }*/
    
    public String getIdent(){
        return ident;
    }
    
    public void paint(java.awt.Graphics g){
        if (i!=null) {g.drawImage(i,x+1,y+1,width-2,height-2,ord);} //sumo, resto el marge
        if (text!=null){
            g.drawString(text,x+3,(int)(y+height)-3);
        }
        if (isMoving) g.setColor(java.awt.Color.red);
        else g.setColor(java.awt.Color.black);
        g.drawRect(x,y,width,height);
    }
    
    public void updateSize(){
		if (i!=null) width=i.getWidth(ord.c)+2;
		if (i!=null) height=i.getHeight(ord.c)+2;
    }
    
    public void imageLoaded(){
        if (i!=null) width=i.getWidth(ord.c)+2;
        if (i!=null) height=i.getHeight(ord.c)+2;
        ord.updatePositionsXY();
        ord.c.repaint();
    }
    
    public boolean contains(int x, int y){
        boolean b=(x>=this.x && y>=this.y && x<=(this.x+width) && y<=(this.y+height));
        return b;
    }
    
    public void setPosition(int position){
        this.position=position;
    }
    
    public int getPosition(){
        return position;
    }
    
    public boolean setPosition(int x, int y){
        if (orientation==QVOrdering.ROW_ORIENTATION){
            if (x<0 || (x+width)>=ord.getContentWidth()) return false;
            this.x=x;
        }
        else{ //COLUMN
            if (y<0 || (y+height)>=ord.getContentHeight()) return false;
            this.y=y;
        }
        return true;
    }
    
    public int getPositionX(){
        return x;
    }
    
    public int getPositionY(){
        return y;
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
    
	public void setImage(java.awt.Image i){
		this.i = i;
	}
        
    public void mouseDragged(java.awt.event.MouseEvent e){
        if (isMoving){
            int incX=e.getX()-lastX;
            int incY=e.getY()-lastY;
            
            boolean canMove=setPosition(x+incX,y+incY);
            if (canMove){
                lastX=e.getX();
                lastY=e.getY();
                ord.updateOrders();
            }
            ord.c.repaint();
        }
    }
    
    public void mouseMoved(java.awt.event.MouseEvent e){
    }
    
    public void mouseClicked(java.awt.event.MouseEvent e){}
    public void mouseEntered(java.awt.event.MouseEvent e){}
    public void mouseExited(java.awt.event.MouseEvent e){}
    
    public void mousePressed(java.awt.event.MouseEvent e){
        if (!ord.isMoving() && contains(e.getX(),e.getY())){
            ord.setMoving(true);
            ord.setIndexMoving(getPosition());
            isMoving=true;
            lastX=e.getX();
            lastY=e.getY();
            ord.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }
    
    public void mouseReleased(java.awt.event.MouseEvent e){
        if (isMoving){
            isMoving=false;
            ord.setMoving(false);
            ord.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            ord.updatePositionsXY();
            ord.c.repaint();
        }
    }
    
    
    public int lastX, lastY;
}
