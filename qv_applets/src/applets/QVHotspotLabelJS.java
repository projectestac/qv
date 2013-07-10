/*
 * QVHotspotLabelJS.java
 *
 * Created on 17 / febrer / 2003, 09:39
 */

//package applets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 *
 * @author  allastar
 */
public class QVHotspotLabelJS {
    
    private QVAppletJS parent;
    private String ident;
    private String rarea;
    private boolean rshuffle=false;
    private String rrange;
    private String text;
    protected int[] coords;
    private boolean selected=false;
    protected java.awt.Polygon p=null;
    protected Hashtable hStyle = new Hashtable();
    
    
    protected int degrees = 0;
    
    
    private int type;
    
    //protected static java.awt.Color drawColor=java.awt.Color.red;
    //protected static java.awt.Color borderColor=java.awt.Color.black;
    
    public static final int RECTANGLE=0;
    public static final int ELLIPSE=1;
    public static final int BOUNDED=2;
    
    /** Creates a new instance of QVHotspotLabelJS */
	public QVHotspotLabelJS(String ident, String rarea, boolean rshuffle, String rrange, String text, Hashtable hStyle) {
		this(null, ident, rarea, rshuffle, rrange, text, hStyle);
	}

    public QVHotspotLabelJS(QVAppletJS parent, String ident, String rarea, boolean rshuffle, String rrange, String text, Hashtable hStyle) {
    	this.parent=parent;
        this.ident=ident;
        this.rarea=rarea;
        this.rshuffle=rshuffle;
        this.rrange=rrange;
        this.text=text;
        this.hStyle=hStyle;
        
        if (rarea==null) rarea="Ellipse";
        if (rarea.trim().toLowerCase().equals("ellipse")) type=ELLIPSE;
        else if (rarea.trim().toLowerCase().equals("rectangle")) type=RECTANGLE;
        else type=BOUNDED;
        
        //System.out.println("creo label type="+type+" text==null?"+(text==null));
        
        if (text!=null){
            StringTokenizer st=new StringTokenizer(text,",");
            try{
                if (type==RECTANGLE || type==ELLIPSE){
                    coords=new int[4];
                    for (int i=0;i<4;i++)
                        coords[i]=(int)(Double.valueOf(st.nextToken().trim()).doubleValue());
                    if (st.hasMoreTokens()) degrees=(int)(Double.valueOf(st.nextToken().trim()).doubleValue());
                    //coords[i]=(int)(Double.parseDouble(st.nextToken().trim()));
                }
                else{ //BOUNDED
                    //System.out.println("creo bounded");
                    p=new java.awt.Polygon();
                    while (st.hasMoreTokens()){
                        int x=(int)(Double.valueOf(st.nextToken().trim()).doubleValue());
                        int y=(int)(Double.valueOf(st.nextToken().trim()).doubleValue());
                        p.addPoint(x,y);
                    }
					coords=new int[4];
					coords[0]=p.getBounds().x;
					coords[1]=p.getBounds().y;
					coords[2]=p.getBounds().width;
					coords[3]=p.getBounds().height;
                }
            }
            catch (Exception e){
                System.out.println("Exception:"+e);
            }
        }
    }
    
    public String getIdent(){
        return ident;
    }
    
    public int[] getCoords(){
        return coords;
    }

	public int getType(){
		return type;
	}
    
    public void paint(Graphics g){
        paintFilled(g,false);
    }
    
    public void paintFilled(Graphics g, boolean filled){
        paintFilled(g,filled,false);
    }
    
    public void paintFilled(Graphics g, boolean filled, boolean transp){
        paintFilled(g,filled,transp,isSelected());
    }

	public void paintFilled(Graphics g, boolean filled, boolean transp, boolean sel){
		paintFilled(g, filled, transp, sel, hStyle);
	}
	
    public void paintFilled(Graphics g, boolean filled, boolean transp, boolean sel, Hashtable hStyle){
        if (!filled) g.setColor(java.awt.Color.white);
        else g.setColor(getBackgroundColor(hStyle));
        if (type==ELLIPSE && coords!=null && coords.length==4){
            if (!transp) g.fillOval(coords[0],coords[1],coords[2],coords[3]);
            if (!filled){
                g.setColor(getBorderColor(hStyle));
                if (!transp) g.drawOval(coords[0],coords[1],coords[2],coords[3]);
                if (sel){
                    g.setColor(getBackgroundColor(hStyle));
                    if (!transp)
                        g.fillOval(coords[0]+(coords[2]/4),coords[1]+(coords[3]/4),Math.max(2,coords[2]/2),Math.max(2,coords[3]/2));
                    else{
                        g.setColor(getBorderColor(hStyle));
                        g.drawOval(coords[0],coords[1],coords[2],coords[3]);
                        g.drawOval(coords[0]-1,coords[1]-1,coords[2]+2,coords[3]+2);
                    }
                }
            }
        }
        else if (type==RECTANGLE && coords!=null && coords.length==4){
        	//System.out.println("rectangle-> bgcolor="+getBackgroundColor(hStyle)+"  filled?"+filled+" sel?"+sel+" transp?"+transp);
            if (!transp) g.fillRect(coords[0],coords[1],coords[2],coords[3]);
            if (!filled){
                g.setColor(getBorderColor(hStyle));
                if (!transp) g.drawRect(coords[0],coords[1],coords[2],coords[3]);
                if (sel){
                    g.setColor(getBackgroundColor(hStyle));
                    if (!transp)
                        g.fillRect(coords[0]+(coords[2]/4),coords[1]+(coords[3]/4),Math.max(2,coords[2]/2),Math.max(2,coords[3]/2));
                    else{
						g.setColor(getBorderColor(hStyle));
                        g.drawRect(coords[0],coords[1],coords[2],coords[3]);
                        g.drawRect(coords[0]-1,coords[1]-1,coords[2]+2,coords[3]+2);
                        Color cBg = getBackgroundColor(hStyle, false);
                        if (parent!=null && parent instanceof QVRenderHotspotAppletJS && ((QVRenderHotspotAppletJS)parent).iResponseBg!=null){
                        	Image iRespBg = ((QVRenderHotspotAppletJS)parent).iResponseBg;
							g.drawImage(iRespBg,coords[0],coords[1],coords[2],coords[3],null);
                        }else if (cBg!=null){
							g.setColor(getBackgroundColor(hStyle));
							g.fillRect(coords[0],coords[1],coords[2],coords[3]);
                        }
                    }
                }
            }
        }
        else if (p!=null){ //BOUNDED
            //System.out.println("dibuixo bounded");
            if (!transp) g.fillPolygon(p);
            if (!filled){
                g.setColor(getBorderColor(hStyle));
                if (!transp) g.drawPolygon(p);
                if (sel){
                    g.setColor(getBackgroundColor(hStyle));
                    if (!transp){
                        g.fillPolygon(p);
                        //g.fillOval(coords[0]+(coords[2]/4),coords[1]+(coords[3]/4),Math.max(2,coords[2]/2),Math.max(2,coords[3]/2));
                    }
                    else{
                        g.setColor(getBorderColor(hStyle));
                        g.drawPolygon(p);
                        //g.drawOval(coords[0]-1,coords[1]-1,coords[2]+2,coords[3]+2);
                    }
                }
            }
        }
    }
    
	public void setStyle(Hashtable hStyle){
		this.hStyle = hStyle;
	}
	
	public Color getFontColor(Hashtable hStyle){
		return getColorFromStyle(hStyle, "color", Color.red);
	}
	
	public Color getBorderColor(Hashtable hStyle){
		return getColorFromStyle(hStyle, "border-color", Color.black);
	}
	
	public Color getBackgroundColor(Hashtable hStyle, boolean bDefault){
		Color c = null;
		if (bDefault) c = getBackgroundColor(hStyle);
		else c = getColorFromStyle(hStyle, "background-color", null);
		return c;
	}
	
	public Color getBackgroundColor(Hashtable hStyle){
		Color c = getColorFromStyle(hStyle, "background-color", null);
		if (c==null){
			c = getColorFromStyle(hStyle, "background", Color.red);
		}
		return c;
	}
	
	private Color getColorFromStyle(Hashtable hStyle, String sStyleName){
		return getColorFromStyle(hStyle, sStyleName, null);
	}
	
	private Color getColorFromStyle(Hashtable hStyle, String sStyleName, Color cDefaultColor){
		Color cColor = cDefaultColor;
		if (hStyle!=null && hStyle.containsKey(sStyleName)){			
			Object o = hStyle.get(sStyleName);
			if (o instanceof Color){
				cColor = (Color)o;
			}else if (o instanceof String){
				try{
					cColor = Color.decode((String)o);
				}catch (Exception e){}
			}
		}
		return cColor;		
	}
	
    public void setSelected(boolean b){
        //System.out.println(ident+" selected:"+b);
        selected=b;
    }
    
    public boolean isSelected(){
        return selected;
    }
    
    public boolean contains(int x, int y){
        if (type==ELLIPSE && coords!=null && coords.length==4){
            ////NOMES A PARTIR DE LA 1.2 java.awt.geom.Ellipse2D e2d=new java.awt.geom.Ellipse2D.Double(coords[0],coords[1],coords[2],coords[3]);
            ////NOMES A PARTIR DE LA 1.2 return e2d.contains(x,y);
            java.awt.Rectangle rect=new java.awt.Rectangle(coords[0],coords[1],coords[2],coords[3]);
            return rect.contains(x,y);
        }
        else if (type==RECTANGLE && coords!=null && coords.length==4){ //Rectangle
            java.awt.Rectangle rect=new java.awt.Rectangle(coords[0],coords[1],coords[2],coords[3]);
            return rect.contains(x,y);
        }
        else if (p!=null){ //Bounded
            return p.contains(x,y);
        }
        return false;
    }
    
    public String toString(){
        return("ident:"+ident+" rarea:"+rarea+" rshuffle:"+rshuffle+" rrange:"+rrange+" text:"+text);
    }
}
