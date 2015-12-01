/*
 * QVRenderHotspotAppletJS.java
 *
 * Created on 17 / febrer / 2003, 09:36
 */

//package applets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

/**
 *
 * @author  allastar
 */
public class QVRenderHotspotAppletJS extends QVAppletJS implements MouseListener, MouseMotionListener{
    
    public Image i, iResponseBg;
    private Vector labels;
    private String img_url;
    private boolean multipleSelection=false;
    private boolean showDraw=false;
    private boolean showOptions=false;
    private boolean transp=false;
    private QVHotspotLabelJS[] line;
    private Vector vMarkers;
    private int iMaxResponses=-1;
    
    public static int markerDimX=5;
    public static int markerDimY=5;
	public static Color markerColor=Color.red;
    
    java.awt.Image offscreenImg; //Doble buffer
    java.awt.Graphics offscreenG; //Doble buffer

    String serverUrl;
    
    private boolean finish=false;
    
    /** Creates a new instance of QVRenderHotspotAppletJS */
    public QVRenderHotspotAppletJS() {
        //System.out.println("constr");
        labels=new Vector();
        vMarkers=new Vector();
        //add(new ImagePanel());
        addMouseListener(this);
        addMouseMotionListener(this);
        setVisible(true);
    }
    
	public void destroy(){
		super.destroy();
		if (i!=null) i=null;
		if (iResponseBg!=null)	iResponseBg=null;
		if (labels!=null) labels=null;
		if (offscreenImg!=null) offscreenImg=null;
		if (offscreenG!=null) offscreenG=null;
	}    
    
    
    public void startParams(){
        //System.out.println("start");
        labels=new Vector();
        vMarkers=new Vector();
        getParams();
    }
    
//    public void init(){
//        //Doble buffer
//		offscreenImg = createImage(getSize().width,getSize().height); //getWidth(), getHeight() a partir de v1.2
//		offscreenG = offscreenImg.getGraphics();
//		super.init();
//    }
    
    public void start(){
        finish=false;
        super.start();
    }
    
    public void stop(){
        finish=true;
        super.stop();
    }
    
    public void getParams(){
        //System.out.println("getParams");
        labels=new Vector();
        boolean ultimParam=false;
        int i=1; //numparams
		urlQuadernBase=getParameter("url_base");
        serverUrl=getParameter("serverUrl");
		
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
                QVHotspotLabelJS hl=new QVHotspotLabelJS(this, ident,rarea,rshuffle,rrange,text, getStyles());
                labels.addElement(hl);
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
            line=new QVHotspotLabelJS[i];
        }
        String sShowOptions=getParameter("showOptions");
        if (sShowOptions!=null && sShowOptions.trim().toLowerCase().equals("yes")){
            showOptions=true;
        }
        String sTransp=getParameter("transp");
        if (sTransp!=null && sTransp.trim().toLowerCase().equals("yes")){
            transp=true;
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
        String sName=getParameter("name");
        setName(sName);
        
        String sDisabled=getParameter("disabled");
        if (sDisabled!=null && sDisabled.trim().toLowerCase().equals("true")) isEditable=false;
        else isEditable=true;
        new ImageLoaderJS().start();
    }
    

    protected String getResponseBackgroundImage(){
    	String sImageURL = null;
		if (getStyles()!=null && getStyles().containsKey("background")){			
			Object o = hStyle.get("background");
			sImageURL = (String)o;
			try{
				sImageURL = sImageURL.substring(sImageURL.indexOf("url(")+4,sImageURL.indexOf(")"));
			}catch(Exception e){
				sImageURL = null;
			}
		}
		return sImageURL;
    }
    
    public class ImageLoaderJS extends Thread{
        
        public ImageLoaderJS(){
        }
        
        public void run(){
            try{
				//System.out.println("image_url= "+img_url+"   i="+i);
                if (i==null && img_url!=null){
                    //java.net.URL u=getDocumentBase();
                    //img_url.replace('\\','/');
                    //if (img_url!=null && img_url.startsWith("/")) img_url=(u.getProtocol()+"://"+u.getHost()+":"+u.getPort()+img_url);
                    //else if (img_url!=null && (img_url.startsWith("http://") || img_url.startsWith("file:/"))) img_url=(u.getProtocol()+"://"+u.getHost()+":"+u.getPort()+((serverUrl!=null && serverUrl.trim().length()>0)?(serverUrl+"/.."):"")+"/getImage?url="+img_url);
                    
                    //System.out.println("image_url:"+img_url+"<-- serverUrl:"+serverUrl);
                    //i=getImage(new java.net.URL(img_url));
					//System.out.println("resourceURL="+getResourceURL(img_url));
					i=getImage(getResourceURL(img_url,serverUrl));
					if (i!=null){
						try{
							getMediaTracker().addImage(i, 0);
							getMediaTracker().waitForID(0);
							if (getResponseBackgroundImage()!=null){
								iResponseBg=getImage(getResourceURL(getResponseBackgroundImage(),serverUrl));
								getMediaTracker().addImage(iResponseBg, 1);
								getMediaTracker().waitForID(1);
							}
							repaint(0);
						}catch(Exception e){
							System.out.println("QVRenderHotspotApplet.run-> Excepció en carregar imatge:"+e);
						}
					}
                }
            }            
            catch (Exception e){
                
            }
        }
    }
    
    public void paint(Graphics gr){
		offscreenImg = createImage(getSize().width,getSize().height); //getWidth(), getHeight() a partir de v1.2
		offscreenG = offscreenImg.getGraphics();
        Graphics g=offscreenG; //Doble buffer

        //super.paintComponent(g);
        /*try{
            if (i==null && img_url!=null){
                java.net.URL u=getDocumentBase();
                if (img_url!=null && img_url.startsWith("/")) img_url=(u.getProtocol()+"://"+u.getHost()+":"+u.getPort()+img_url);
                System.out.println("image_url:"+img_url+"<--");
                i=getImage(new java.net.URL(img_url));
            }            
        }
        catch (Exception e){
            System.out.println("Excepció en carregar imatge:"+e);
        }*/
                /*g.setColor(Color.red);
                g.fillOval(50,50,50,50);*/
        if (i!=null){
            g.drawImage(i,0,0,this);
        }
        else{
            g.drawString("Carregant imatge...",50,50);
        }
        if (showOptions){
            //System.out.println("Num labels:"+labels.size()+"<--");
            java.util.Enumeration e=labels.elements();
            while (e.hasMoreElements()){
                //System.out.println("dibuixant label");
                QVHotspotLabelJS hl=(QVHotspotLabelJS)e.nextElement();
                hl.paintFilled(g,false,transp);
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
                //// NOMÉS v1.2 O SUPERIORS ((Graphics2D)g).setStroke(new BasicStroke(stroke));
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
        gr.drawImage(offscreenImg,0,0,this);
    }

    public void update(Graphics g) { 
        paint(g);
    }


    public String getStringRepresentation(){
                /* Retorna un String que indica els idents de les
                caselles seleccionades separats per comes. */
        String representation="";
        if (!showDraw){
            int numSelected=0;
            java.util.Enumeration e=labels.elements();
            while (e.hasMoreElements()){
                QVHotspotLabelJS hl=(QVHotspotLabelJS)e.nextElement();
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
                    String s="*"+((int)p.x)+":"+((int)p.y);
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
    	System.out.println("QVRenderHotspotAppletJS.selectInitialResponses-> "+param);
    	//initFromParam(param);
    	
        java.util.StringTokenizer st=new java.util.StringTokenizer(param,",");
        while (st.hasMoreTokens()){
            String id=st.nextToken();
            id=(id.indexOf('*')>0)?id.substring(0,id.indexOf('*')):id;
            if (!showDraw)
                selectLabelWithId(id);
            else addToLine(getLabelWithId(id));
        }
	    if (!showOptions){
	        putInitMarkers(param);
	    }    	
    }
    
    public boolean initFromParam(String param){
                /* Selecciona les caselles que tenen com id algun dels
                que apareixen al paràmetre param. */
        //System.out.println("initFromParam");
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
        ////////updateParam();
        return true;
    }
    
    protected void putInitMarkers(String param){
        boolean bFinish=false;
        if (param==null) return;
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
        QVHotspotLabelJS hl=getLabelWithId(id);
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
            QVHotspotLabelJS hl=(QVHotspotLabelJS)en.nextElement();
            if (hl.isSelected()) iNumSelected++;
        }
        return iNumSelected;
    }
    
    public QVHotspotLabelJS getLabelWithId(String id){
        java.util.Enumeration en=labels.elements();
        while (en.hasMoreElements()){
            QVHotspotLabelJS hl=(QVHotspotLabelJS)en.nextElement();
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
    }
    
    private void addToLine(QVHotspotLabelJS hl){
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
    
    private boolean lineContains(QVHotspotLabelJS hl){
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
        //repaint(0,0,0,getWidth(),getHeight());
        repaint(0);
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
    
    private boolean isLastOfLine(QVHotspotLabelJS hl){
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
    
    private boolean isFirstOfLine(QVHotspotLabelJS hl){
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
        if (!isEditable) return;
        java.util.Enumeration en=labels.elements();
        while (en.hasMoreElements()){
            QVHotspotLabelJS hl=(QVHotspotLabelJS)en.nextElement();
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
                        repaint(0);
                    }
                    else{ //!hl.isSelected()
                        if (multipleSelection){
                            int iNumSelected=getNumSelectedLabels();
                            if (iNumSelected<iMaxResponses) hl.setSelected(true);
                            ////repaint(hl);
                            repaint(0);
                        }
                        else{ //!multipleSelection
                            hl.setSelected(true);
                            repaint(hl);
                            deselectOther(hl);
                            repaint(0);//&&
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
        updateParam();
    }
    
    protected boolean hasSelectedMarker(int x, int y, boolean bDelete){
        java.util.Enumeration e=vMarkers.elements();
        boolean bFound=false;
        while (e.hasMoreElements() && !bFound){
            Point p=(Point)e.nextElement();
            int xMark=(int)p.x;
            int yMark=(int)p.y;
            if ((x>=(xMark-markerDimX) && x<=(xMark+markerDimX)) &&
            (y>=(yMark-markerDimY) && y<=(yMark+markerDimY))){
                bFound=true;
                if (bDelete){
                    vMarkers.removeElement(p);
                    repaint(xMark-markerDimX,yMark-markerDimY,2*markerDimX,2*markerDimY);
                }
            }
        }
        return bFound;
    }
    
    protected void addMarker(int x, int y){
        boolean bAdd=true;
        if (!multipleSelection) vMarkers.removeAllElements();//clear();
        else{
            bAdd=!hasSelectedMarker(x,y,true); //delete
        }
        if (bAdd && (iMaxResponses<0 || vMarkers.size()<iMaxResponses)) vMarkers.addElement(new Point(x,y));
    }
    
    protected void showMarkers(java.awt.Graphics g){
        java.util.Enumeration e=vMarkers.elements();
        while (e.hasMoreElements()){
            Point p=(Point)e.nextElement();
            //if (g instanceof Graphics2D)
            //    ((Graphics2D)g).setStroke(new BasicStroke(4f));
            g.setColor(markerColor);
            g.drawLine((int)(p.x-markerDimX),(int)(p.y-markerDimY),(int)(p.x+markerDimX),(int)(p.y+markerDimY));
            g.drawLine((int)(p.x-markerDimX),(int)(p.y-markerDimY)+1,(int)(p.x+markerDimX)-1,(int)(p.y+markerDimY));//
            g.drawLine((int)(p.x-markerDimX)+1,(int)(p.y-markerDimY),(int)(p.x+markerDimX),(int)(p.y+markerDimY)-1);//
            g.drawLine((int)(p.x-markerDimX),(int)(p.y+markerDimY),(int)(p.x+markerDimX),(int)(p.y-markerDimY));
            g.drawLine((int)(p.x-markerDimX),(int)(p.y+markerDimY)-1,(int)(p.x+markerDimX)-1,(int)(p.y-markerDimY));//
            g.drawLine((int)(p.x-markerDimX)+1,(int)(p.y+markerDimY),(int)(p.x+markerDimX),(int)(p.y-markerDimY)+1);//
        }
    }
    
    public void deselectOther(QVHotspotLabelJS hl){
        java.util.Enumeration en=labels.elements();
        while (en.hasMoreElements()){
            QVHotspotLabelJS hl2=(QVHotspotLabelJS)en.nextElement();
            if(hl2!=hl){
                hl2.setSelected(false);
                repaint(hl2);
            }
        }
    }
    
    public void repaint(QVHotspotLabelJS hl){
        int[] coords=hl.getCoords();
        if (coords!=null && coords.length==4)
            repaint(0,coords[0],coords[1],coords[2],coords[3]);
    }
    
    public void mouseReleased(MouseEvent e){
    }
    
    public void mouseDragged(java.awt.event.MouseEvent mouseEvent) {
    }
    
    public void mouseMoved(java.awt.event.MouseEvent mouseEvent) {
        if (!isEditable){
            //setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
            return;
        }
        if (showOptions){
            int x=mouseEvent.getX();
            int y=mouseEvent.getY();
            boolean contains=false;
            if (labels!=null){
                java.util.Enumeration e=labels.elements();
                while (e.hasMoreElements() && !contains){
                    QVHotspotLabelJS l=(QVHotspotLabelJS)e.nextElement();
                    contains=l.contains(x,y);
                }
            }
            if (contains) setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
            else setCursor(java.awt.Cursor.getDefaultCursor());
        }
    }

    public void showStatus(String s){
        initFromParam(s);
    }

    public String toString(){
        return getStringRepresentation();
    }

}
