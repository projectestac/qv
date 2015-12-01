import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class qvRenderHotspotApplet extends qvApplet implements MouseListener{
    
    //private ImagePanel imagePanel;
    public Image i;
    private Vector labels;
    private String img_url;
    private boolean multipleSelection=false;
    private boolean showDraw=false;
    private boolean showOptions=false;
    private qvHotspotLabel[] line;
    private Vector vMarkers;
    private int iMaxResponses=-1;
    
    public static int markerDimX=5;
    public static int markerDimY=5;
    public static Color markerColor=Color.red;
    
    public qvRenderHotspotApplet(){
        System.out.println("constr");
        labels=new Vector();
        vMarkers=new Vector();
        //add(new ImagePanel());
        addMouseListener(this);
        setVisible(true);
    }
    
    public void startParams(){
        System.out.println("start");
        getParams();
    }
    
    public void getParams(){
        System.out.println("getParams");
        boolean ultimParam=false;
        int i=1; //numparams
        while (!ultimParam){
            String ident=getParameter("P"+i+"_ident");
            if(ident!=null){
                String rarea=getParameter("P"+i+"_rarea");
                String sRshuffle=getParameter("P"+i+"_rshuffle");
                if (sRshuffle==null) sRshuffle="Yes"; //Default
                boolean rshuffle;
                if (sRshuffle.toLowerCase().equals("yes")) rshuffle=true;
                else rshuffle=false;
                String rrange=getParameter("P"+i+"_rrange");
                if (rrange==null) rrange="Exact"; //Default
                String text=getParameter("P"+i+"_text");
                qvHotspotLabel hl=new qvHotspotLabel(ident,rarea,rshuffle,rrange,text);
                labels.add(hl);
                //System.out.println("i:"+hl.toString());
                i++;
            }
            else ultimParam=true;
        }
        img_url=getParameter("image_src");
        String rcardinality=getParameter("rcardinality");
        if (rcardinality!=null){
            multipleSelection=rcardinality.trim().equals("Multiple");
        }
        String sShowDraw=getParameter("showDraw");
        if (sShowDraw!=null && sShowDraw.trim().toLowerCase().equals("yes")){
            showDraw=true;
            line=new qvHotspotLabel[i];
        }
        String sShowOptions=getParameter("showOptions");
        if (sShowOptions!=null && sShowOptions.trim().toLowerCase().equals("yes")){
            showOptions=true;
        }
        String sMaxResponses=getParameter("maxNumber");
        if (sMaxResponses!=null && sMaxResponses.trim().length()>0){
            try{
                iMaxResponses=Integer.parseInt(sMaxResponses);
                if (iMaxResponses<0) iMaxResponses=-1;
            }
            catch (Exception e){
                System.out.println("Excepció en el format del nombre màxim de respostes:"+e);
            }
        }
    }
    
    public void paint(Graphics g){
        //super.paintComponent(g);
        try{
            //"d:\\Albert\\Asignaturas\\pfc\\src\\mchotspot1.gif"
            if (i==null && img_url!=null){
                if (img_url!=null && img_url.startsWith("/")) img_url=(getDocumentBase()+img_url);
                i=Toolkit.getDefaultToolkit().getImage(new java.net.URL(img_url));
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
        }
                /*g.setColor(Color.red);
                g.fillOval(50,50,50,50);*/
        if (i!=null){
            g.drawImage(i,0,0,this);
        }
        if (showOptions){
            //System.out.println("Num labels:"+labels.size()+"<--");
            java.util.Enumeration e=labels.elements();
            while (e.hasMoreElements()){
                //System.out.println("dibuixant label");
                qvHotspotLabel hl=(qvHotspotLabel)e.nextElement();
                hl.paint(g);
            }
        }
        else{
            showMarkers(g);
        }
        if (showDraw && line!=null){
            g.setColor(Color.black);
            boolean bFi=false;
            int x_ant=0,y_ant=0;
            if (line.length>0 && line[0]!=null){
                int[] coords=line[0].getCoords();
                x_ant=coords[0]+(coords[2]/2);
                y_ant=coords[1]+(coords[3]/2);
                int stroke=Math.max(1,coords[2]/2);
                line[0].paintFilled(g,true);
                ((Graphics2D)g).setStroke(new BasicStroke(stroke));
                for (int i=1;i<line.length && !bFi;i++){
                    bFi=(line[i]==null);
                    if (!bFi){
                        coords=line[i].getCoords();
                        int x=coords[0]+(coords[2]/2);
                        int y=coords[1]+(coords[3]/2);
                        g.drawLine(x_ant,y_ant,x,y);
                        x_ant=x;
                        y_ant=y;
                        line[i].paintFilled(g,true);
                    }
                }
            }
        }
    }
    
    public String getStringRepresentation(){
                /* Retorna un String que indica els idents de les
                caselles seleccionades separats per comes. */
        String representation="";
        if (!showDraw){
            int numSelected=0;
            java.util.Enumeration e=labels.elements();
            while (e.hasMoreElements()){
                qvHotspotLabel hl=(qvHotspotLabel)e.nextElement();
                if (hl.isSelected()){
                    if (numSelected>0) representation+=",";
                    representation+=hl.getIdent();
                    numSelected++;
                }
            }
            if (!showOptions){
                java.util.Enumeration en=vMarkers.elements();
                while (en.hasMoreElements()){
                    Point p=(Point)en.nextElement();
                    String s="*"+((int)p.getX())+":"+((int)p.getY());
                    //System.out.println("s:"+s+"<--");
                    representation+=s;
                }
            }
        }
        else{
            if (line!=null && line[0]!=null){
                representation=line[0].getIdent();
                boolean bFi=false;
                for (int i=1;i<line.length && !bFi;i++){
                    bFi=(line[i]==null);
                    if (!bFi){
                        representation+=","+line[i].getIdent();
                    }
                }
                
            }
        }
        return representation;
    }
    
    public void selectInitialResponses(String param){
    	initFromParam(param);
    }
    
    public boolean initFromParam(String param){
                /* Selecciona les caselles que tenen com id algun dels
                que apareixen al paràmetre param. */
        System.out.println("initFromParam");
        if (param!=null){
            startParams(); //per inicialitzar els paràmetres
            java.util.StringTokenizer st=new java.util.StringTokenizer(param,",");
            while (st.hasMoreTokens()){
                String id=st.nextToken();
                id=(id.indexOf('*')>0)?id.substring(0,id.indexOf('*')):id;
                if (!showDraw)
                    selectLabelWithId(id);
                else addToLine(getLabelWithId(id));
            }
        }
        if (!showOptions){
            putInitMarkers(param);
        }
        return true;
    }
    
    protected void putInitMarkers(String param){
        boolean bFinish=false;
        java.util.StringTokenizer st=new java.util.StringTokenizer(param,",");
        while (st.hasMoreTokens()){
            String sUserResponses=st.nextToken().trim();
            if (sUserResponses!=null && sUserResponses.indexOf('*')>=0){
                java.util.StringTokenizer st2=new java.util.StringTokenizer(sUserResponses,"*");
                while (st2.hasMoreTokens()){
                    String sCoord=st2.nextToken(); //124:510
                    //int ast=sUserResponses.indexOf('*');
                    int ig=sCoord.indexOf(':');//,ast);
                    if (ig>0){
                        try{
                            int x=Integer.parseInt(sCoord.substring(0,ig));
                            int y=Integer.parseInt(sCoord.substring(ig+1));
                            addMarker(x,y);
                        }
                        catch(Exception e){
                            System.out.println("Excepció:"+e);
                        }
                    }
                }
            }
        }
    }
    
    public void selectLabelWithId(String id){
        //System.out.println("getLabelWithId:"+id+"<--");
        qvHotspotLabel hl=getLabelWithId(id);
        //System.out.println("hl==null?"+(hl==null));
        if (hl!=null){
            //System.out.println("vaig a seleccionar:"+hl);
            hl.setSelected(true);
            repaint(hl);
            repaint(0);////??
        }
    }
    
    public int getNumSelectedLabels(){
        int iNumSelected=0;
        java.util.Enumeration en=labels.elements();
        while (en.hasMoreElements()){
            qvHotspotLabel hl=(qvHotspotLabel)en.nextElement();
            if (hl.isSelected()) iNumSelected++;
        }
        return iNumSelected;
    }
    
    public qvHotspotLabel getLabelWithId(String id){
        java.util.Enumeration en=labels.elements();
        while (en.hasMoreElements()){
            qvHotspotLabel hl=(qvHotspotLabel)en.nextElement();
            if(hl.getIdent().equals(id)) return hl;
        }
        return null;
    }
    
    public void loadImage(String url){
        try{
            //i=new ImageIcon(new java.net.URL(url)).getImage().getScaledInstance(imagePanel.getWidth(),imagePanel.getHeight(),Image.SCALE_DEFAULT);
            //i=new ImageIcon(new java.net.URL(url)).getImage().getScaledInstance(getWidth(),getHeight()-30,Image.SCALE_DEFAULT);
            //			i=Toolkit.getDefaultToolkit().getImage(url);
        }
        catch(Exception e){
            System.out.println("Excepció: "+e);
        }
                /*MediaTracker tracker = new MediaTracker(this);
                i=getToolkit().getImage("c:\\Albert\\pfc\\JAVASCRIPT\\consjava.gif");
                tracker.addImage(i,0);
                try{
                        tracker.waitForID(0);
                }
                catch(InterruptedException e){
                        System.out.println("Image loading interrupted : " + e);
                }
                if (imagePanel==null){
                        imagePanel=new ImagePanel();
                        imagePanel.setSize(getWidth(),getHeight());
                        add(imagePanel);
                        imagePanel.setVisible(true);
                        doLayout();
                }
                imagePanel.repaint(0);
                repaint(0);*/
    }
    
    private void addToLine(qvHotspotLabel hl){
        if (line!=null){
            boolean bFi=false;
            for (int i=0;i<line.length && !bFi && (iMaxResponses<0 || i<iMaxResponses);i++){
                if (line[i]==null){
                    line[i]=hl;
                    if (line.length>(i+1)) line[i+1]=null; //l'últim de la linia
                    bFi=true;
                }
            }
        }
        repaint(0);////?
    }
    
    private boolean lineContains(qvHotspotLabel hl){
        /* Retorna true si la linia marcada conté hl */
        boolean bContains=false;
        if (line!=null){
            boolean bFi=false;
            for (int i=0;i<line.length && !bContains && !bFi;i++){
                bFi=(line[i]==null);
                if (!bFi) bContains=(line[i]==hl);
            }
        }
        return bContains;
    }
    
    private void cleanLine(){
        /* Esborra la linia marcada */
        if (line!=null) line[0]=null;
        repaint(0,0,0,getWidth(),getHeight());
    }
    
    private void removeLastOfLine(){
        /* Esborra l'últim segment de la linia */
        int last=0;
        int i;
        boolean bFi=false;
        for (i=0;i<line.length && !bFi;i++){
            if (line[i]==null){
                bFi=true;
                last=i-1;
            }
        }
        if (last==0 && i==line.length) line[line.length-1]=null;
        else if (last>=0) line[last]=null;
        repaint(0);
    }
    
    private boolean isLastOfLine(qvHotspotLabel hl){
        int last=0;
        int i;
        boolean bFi=false;
        for (i=0;i<line.length && !bFi;i++){
            if (line[i]==null){
                bFi=true;
                last=i-1;
            }
        }
        if (last==0 && i==line.length) return (line[line.length-1]==hl);
        else if (last>=0) return (line[last]==hl);
        else return false;
    }
    
    private boolean isFirstOfLine(qvHotspotLabel hl){
        if (line!=null && line.length>0 && line[0]==hl) return true;
        else return false;
    }
    
    public void mouseClicked(MouseEvent e){
        //Invoked when the mouse has been clicked on a component.
    }
    
    public void mouseEntered(MouseEvent e){
        //Invoked when the mouse enters a component.
    }
    
    public void mouseExited(MouseEvent e){
        //Invoked when the mouse exits a component.
    }
    
    public void mousePressed(MouseEvent e){
        //Invoked when a mouse button has been pressed on a component.
        //System.out.println("x:"+e.getX()+" y:"+e.getY());
        java.util.Enumeration en=labels.elements();
        while (en.hasMoreElements()){
            qvHotspotLabel hl=(qvHotspotLabel)en.nextElement();
            if(hl.contains(e.getX(),e.getY())){
                if (showDraw){
                    if (isLastOfLine(hl))
                        removeLastOfLine();
                    else if (lineContains(hl) && !isFirstOfLine(hl)){
                        cleanLine();
                        repaint(0);////?
                    }
                    else{
                        addToLine(hl);
                    }
                }
                else{ //!showDraw
                    if (hl.isSelected()){
                        hl.setSelected(false);
                    }
                    else{ //!hl.isSelected()
                        if (multipleSelection){
                            int iNumSelected=getNumSelectedLabels();
                            if (iNumSelected<iMaxResponses) hl.setSelected(true);
                            repaint(hl);
                        }
                        else{ //!multipleSelection
                            hl.setSelected(true);
                            repaint(hl);
                            deselectOther(hl);
                        }
                    }
                }
            }
            else{ //!contains. Si és selecció simple i està seleccionada, s'ha de deseleccionar.
                if (!multipleSelection && hl.isSelected()) hl.setSelected(false);
            }
        }
        if (!showOptions){
            addMarker(e.getX(),e.getY());
            //repaint(e.getX()-markerDimX,e.getY()-markerDimY,2*markerDimX,2*markerDimY);
            repaint(0); ////?
        }
        //System.out.println("seleccionades:"+getNumSelectedLabels());
    }
    
    protected boolean hasSelectedMarker(int x, int y, boolean bDelete){
        java.util.Enumeration e=vMarkers.elements();
        boolean bFound=false;
        while (e.hasMoreElements() && !bFound){
            Point p=(Point)e.nextElement();
            int xMark=(int)p.getX();
            int yMark=(int)p.getY();
            if ((x>=(xMark-markerDimX) && x<=(xMark+markerDimX)) &&
            (y>=(yMark-markerDimY) && y<=(yMark+markerDimY))){
                bFound=true;
                if (bDelete){
                    vMarkers.remove(p);
                    repaint(xMark-markerDimX,yMark-markerDimY,2*markerDimX,2*markerDimY);
                }
            }
        }
        return bFound;
    }
    
    protected void addMarker(int x, int y){
        boolean bAdd=true;
        if (!multipleSelection) vMarkers.clear();
        else{
            bAdd=!hasSelectedMarker(x,y,true); //delete
        }
        if (bAdd && (iMaxResponses<0 || vMarkers.size()<iMaxResponses)) vMarkers.add(new Point(x,y));
    }
    
    protected void showMarkers(java.awt.Graphics g){
        java.util.Enumeration e=vMarkers.elements();
        while (e.hasMoreElements()){
            Point p=(Point)e.nextElement();
            if (g instanceof Graphics2D)
                ((Graphics2D)g).setStroke(new BasicStroke(4f));
            g.setColor(markerColor);
            g.drawLine((int)(p.getX()-markerDimX),(int)(p.getY()-markerDimY),(int)(p.getX()+markerDimX),(int)(p.getY()+markerDimY));
            g.drawLine((int)(p.getX()-markerDimX),(int)(p.getY()+markerDimY),(int)(p.getX()+markerDimX),(int)(p.getY()-markerDimY));
        }
    }
    
    public void deselectOther(qvHotspotLabel hl){
        java.util.Enumeration en=labels.elements();
        while (en.hasMoreElements()){
            qvHotspotLabel hl2=(qvHotspotLabel)en.nextElement();
            if(hl2!=hl){
                hl2.setSelected(false);
                repaint(hl2);
            }
        }
    }
    
    public void repaint(qvHotspotLabel hl){
        int[] coords=hl.getCoords();
        if (coords!=null && coords.length==4)
            repaint(0,coords[0],coords[1],coords[2],coords[3]);
    }
    
    public void mouseReleased(MouseEvent e){
    }
    
/*	public class ImagePanel extends JComponent{
 
                public ImagePanel(){
                        addMouseListener(qvRenderHotspotApplet.this);
                        repaint(0);
                }
 
                public void paintComponent(Graphics g){
                        System.out.println("Paint canvas!!!");
                        i=Toolkit.getDefaultToolkit().getImage("d:\\Albert\\Asignaturas\\pfc\\src\\mchotspot1.gif");
                        if (i!=null){
                                g.drawImage(i,0,0,this);
                        }
                        else System.out.println("imatge=null!!!!");
                        g.setColor(Color.red);
                        g.fillOval(50,50,50,50);
                }
        }
 */
}