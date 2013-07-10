import java.awt.Graphics;
import java.util.StringTokenizer;

public class qvHotspotLabel{
    
    private String ident;
    private String rarea;
    private boolean rshuffle=false;
    private String rrange;
    private String text;
    private int[] coords;
    private boolean selected=false;
    
    
    public qvHotspotLabel(String ident, String rarea, boolean rshuffle, String rrange, String text){
        this.ident=ident;
        this.rarea=rarea;
        this.rshuffle=rshuffle;
        this.rrange=rrange;
        this.text=text;
        if (text!=null){
            coords=new int[4];
            try{
                StringTokenizer st=new StringTokenizer(text,",");
                for (int i=0;i<4;i++)
                    coords[i]=Integer.parseInt(st.nextToken());
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
    
    public void paint(Graphics g){
        paintFilled(g,false);
    }
    
    public void paintFilled(Graphics g, boolean filled){
        //System.out.println("paint ident="+ident+" selected="+selected);
        if (coords!=null && coords.length==4){
            if (!filled) g.setColor(java.awt.Color.white);
            else g.setColor(java.awt.Color.black);
            if (rarea==null) rarea="Ellipse";
            if (rarea.trim().toLowerCase().equals("ellipse")){
                g.fillOval(coords[0],coords[1],coords[2],coords[3]);
                if (!filled){
                    g.setColor(java.awt.Color.black);
                    g.drawOval(coords[0],coords[1],coords[2],coords[3]);
                    if (isSelected()){
                        g.fillOval(coords[0]+(coords[2]/4),coords[1]+(coords[3]/4),Math.max(2,coords[2]/2),Math.max(2,coords[3]/2));
                    }
                }
            }
            else{
                g.fillRect(coords[0],coords[1],coords[2],coords[3]);
                if (!filled){
                    g.setColor(java.awt.Color.black);
                    g.drawRect(coords[0],coords[1],coords[2],coords[3]);
                    if (isSelected()){
                        g.fillRect(coords[0]+(coords[2]/4),coords[1]+(coords[3]/4),Math.max(2,coords[2]/2),Math.max(2,coords[3]/2));
                    }
                }
            }
        }
    }
    
    public void setSelected(boolean b){
        //System.out.println(ident+" selected:"+b);
        selected=b;
    }
    
    public boolean isSelected(){
        return selected;
    }
    
    public boolean contains(int x, int y){
        if (coords!=null && coords.length==4){
            if (rarea==null) rarea="Ellipse";
            if (rarea.trim().toLowerCase().equals("ellipse")){
                java.awt.geom.Ellipse2D e2d=new java.awt.geom.Ellipse2D.Double(coords[0],coords[1],coords[2],coords[3]);
                return e2d.contains(x,y);
            }
            else{ //Rectangle
                java.awt.Rectangle rect=new java.awt.Rectangle(coords[0],coords[1],coords[2],coords[3]);
                return rect.contains(x,y);
            }
        }
        return false;
    }
    
    public String toString(){
        return("ident:"+ident+" rarea:"+rarea+" rshuffle:"+rshuffle+" rrange:"+rrange+" text:"+text);
    }
}