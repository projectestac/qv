/*
 * QVOrdering.java
 *
 * Created on 16 / juny / 2003, 08:53
 */

//package applets;

/**
 *
 * @author  allastar
 */

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

public class QVOrdering extends QVAppletJS implements java.awt.image.ImageObserver{
    
    public OrderingCanvas c;
    java.util.Vector vSources;
    String serverUrl;
    
    java.util.Vector vImages=null;
    boolean bContainsImages = false;
    
    public static final int ROW_ORIENTATION=0;
    public static final int COLUMN_ORIENTATION=1;
    
    boolean bMoving=false;
    int indexMoving=-1;
    int orientation=-1;
    
    /** Creates a new instance of QVOrdering */
    public QVOrdering() {
        vSources=new java.util.Vector();
    }
    
	public void destroy(){
		super.destroy();
		if (c!=null) c=null;
		if (vSources!=null)	vSources=null;
		if (vImages!=null) vImages=null;
	}    
    
    public boolean isMoving(){ //Hi ha algun Source que s'està movent
        return bMoving;
    }
    
    public synchronized void setMoving(boolean b){ //Estableix si hi ha algun Source que s'està movent
        bMoving=b;
        if (b==false) indexMoving=-1;
    }
    
    class OrderingCanvas extends javax.swing.JPanel implements ImageObserver{
        
        java.awt.Image i=null;
        java.awt.Image offscreenImg; //Doble buffer
        java.awt.Graphics offscreenG; //Doble buffer
        //ImageLoader imgl=null;
        
        public OrderingCanvas(){
        }
        
        public void setBGImage(String imageBack){
            try{
                i=getImage(getResourceURL(imageBack, serverUrl));
                //imgl=new ImageLoader(i,this);
                //imgl.start();
            }
            catch (Exception e){
                e.printStackTrace(System.out);
            }
        }
        
        public void paint(Graphics gr){
			if (getMediaTracker().checkAll()){
				//System.out.println("width="+QVOrdering.this.getSize().width+" height="+QVOrdering.this.getSize().height);
				offscreenImg = createImage(QVOrdering.this.getSize().width,QVOrdering.this.getSize().height);//width,height); //
				offscreenG = offscreenImg.getGraphics();
				java.util.Enumeration e=vSources.elements();
				while (e.hasMoreElements()){
					OrderingSource os=(OrderingSource)e.nextElement();
					//os.imageLoaded();
					os.updateSize();
				}
				updatePositionsXY();
			}else{
				gr.drawString("Carregant imatges...", 20,20);
			}

            if (offscreenG!=null){
                Graphics g=offscreenG;
                if (i!=null) {g.drawImage(i,0,0,i.getWidth(this),i.getHeight(this),null);} 
                else{
                    g.setColor(java.awt.Color.white);
                    g.fillRect(0,0,QVOrdering.this.getSize().width,QVOrdering.this.getSize().height);
                }
                g.setColor(java.awt.Color.black);
                java.util.Enumeration e=vSources.elements();
                while (e.hasMoreElements()){
                    OrderingSource os=(OrderingSource)e.nextElement();
                    if (os.getPosition()!=indexMoving)
                        os.paint(g);
                }
                OrderingSource os=getSourceAtPosition(indexMoving);
                if (os!=null) os.paint(g); //Es pinta el darrer perquè surti a sobre
                if (offscreenImg!=null){
					gr.drawImage(offscreenImg,0,0,this);
                }
            }
        }
        
        public void update(Graphics g) {
            paint(g);
        }
        
        public void imageLoaded(){
            //Doble buffer
            repaint();
        }
        
    }
    
    public int getContentWidth(){
        return getSize().width;
    }
    
    public int getContentHeight(){
        return getSize().height;
    }
    
    public void updatePositionsXY(){
        int position=1;
        OrderingSource os=getSourceAtPosition(position);
        int x=0; int y=0;
        while (os!=null){
            if (position!=indexMoving){
                os.setPosition(x,y);
            }
            if (orientation==ROW_ORIENTATION)
                x+=os.getWidth();
            else
                y+=os.getHeight();
            //height...
            position++;
            os=getSourceAtPosition(position);
        }
    }
    
    public synchronized void updateOrders(){
        OrderingSource os=getSourceAtPosition(indexMoving);
        if (os!=null){
            
            if (orientation==ROW_ORIENTATION){
                int xIni=os.getPositionX();
                int xFi=xIni+os.getWidth();
                OrderingSource osLeft=getSourceAtPosition(indexMoving-1);
                OrderingSource osRight=getSourceAtPosition(indexMoving+1);
                if (osLeft!=null && (osLeft.getPositionX()+(osLeft.getWidth()/2))>=xIni){ //Despl. a l'esquerra
                    osLeft.setPosition(indexMoving);
                    os.setPosition(indexMoving-1);
                    indexMoving=indexMoving-1;
                    updatePositionsXY();
                }
                else if (osRight!=null && (osRight.getPositionX()+(osRight.getWidth()/2))<=xFi){ // Despl. a la dreta
                    osRight.setPosition(indexMoving);
                    os.setPosition(indexMoving+1);
                    indexMoving=indexMoving+1;
                    updatePositionsXY();
                }
            }
            else{
                int yIni=os.getPositionY();
                int yFi=yIni+os.getHeight();
                OrderingSource osUp=getSourceAtPosition(indexMoving-1);
                OrderingSource osDown=getSourceAtPosition(indexMoving+1);
                if (osUp!=null && (osUp.getPositionY()+(osUp.getHeight()/2))>=yIni){ //Despl. amunt
                    osUp.setPosition(indexMoving);
                    os.setPosition(indexMoving-1);
                    indexMoving=indexMoving-1;
                    updatePositionsXY();
                }
                else if (osDown!=null && (osDown.getPositionY()+(osDown.getHeight()/2))<=yFi){ // Despl. avall
                    osDown.setPosition(indexMoving);
                    os.setPosition(indexMoving+1);
                    indexMoving=indexMoving+1;
                    updatePositionsXY();
                }
            }
        }
    }
    
    public void setIndexMoving(int i){
        indexMoving=i;
    }
    
    public boolean initFromParam(String param){
    	//System.out.println("initFromParam-> "+param);
        if (param!=null){/////
            startParams();
            if (bContainsImages){
				new ImageLoaderThread(tracker, c).start();
            }
        }
        return true;
    }
    
    public String getStringRepresentation(){
        StringBuffer s=new StringBuffer();
        int position=1;
        OrderingSource os=getSourceAtPosition(position);
        while (os!=null){
            if (position>1) s.append(',');
            s.append(os.getIdent());
            position++;
            os=getSourceAtPosition(position);
        }
        return s.toString();
    }
    
    public void startParams(){
        //System.out.println("start");
        c=new OrderingCanvas();
        vSources=new java.util.Vector();
        removeAll();
        setLayout(new BorderLayout());
        add(c,BorderLayout.CENTER);
        String initParam=getParams();
        selectInitialResponses(initParam);
        //c.imageLoaded(); //
        updatePositionsXY();
        repaint();
    }
    
    public void paint(Graphics g){
        super.paint(g);
        if (c!=null){
            //c.repaint();
            c.paint(g);
            
        }
    }
    
    public void selectInitialResponses(String initParam){
        //initParam és de la forma:   id_source1:id_target1,id_source2:id_target2, .. , id_sourcen:id_targetn
        //System.out.println("QVOrdering.selectInitialResponses-> "+initParam);
        int orderPosition=1;
        java.util.StringTokenizer st=new java.util.StringTokenizer(initParam,",");
        while (st.hasMoreTokens()){
            String s=st.nextToken();
            OrderingSource os=getSourceWithId(s);
            //System.out.println("QVOrdering.selectInitialResponses-> id="+s+" pos="+orderPosition+" os="+os);
            os.setPosition(orderPosition);
            orderPosition++;
        }
        
		java.util.Enumeration e=vSources.elements();
        while (e.hasMoreElements()){
            OrderingSource os=(OrderingSource)e.nextElement();
            if (os.getPosition()<0) {
            	os.setPosition(orderPosition);} 
            orderPosition++;
        }
        repaint();
    }
    
    public String getParams(){
        //System.out.println("getParams");
        boolean darrerParam=false;
        int i=1; //numparams        
        vSources.removeAllElements();//
        
        urlQuadernBase=getParameter("url_base");
        serverUrl=getParameter("serverUrl");
        
        String sOrientation=getParameter("orientation");
        orientation=(sOrientation!=null && sOrientation.trim().toLowerCase().equals("row"))?ROW_ORIENTATION:COLUMN_ORIENTATION;
        //System.out.println("Orient="+orientation);
        
		String sSize=getParameter("size");
		int iSize = -1;
		if (sSize!=null && sSize.trim().length()>0){
			iSize = Integer.parseInt(sSize);
		}
		
		String sFont=getParameter("font");
		java.awt.Font font = getFont();
		if (sFont!=null || sFont.trim().length()>0 || iSize>0){
			String sFontName = (sFont!=null || sFont.trim().length()>0)?sFont:"Dialog";
			int iFontSize = (iSize>0)?iSize:12;
			font = new java.awt.Font(sFontName,java.awt.Font.PLAIN,iFontSize);
			setFont(font);
    	}else if (font==null){
			font = new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12);
			setFont(font);
		}
		
        String sDisabled=getParameter("disabled");
        if (sDisabled!=null && sDisabled.trim().toLowerCase().equals("true")) isEditable=false;
        else isEditable=true;
        
        while (!darrerParam){
            String ident=getParameter("S"+i+"_ident");
            if(ident!=null){
                String uri=getParameter("S"+i+"_uri");
                int width=getIntParameter("S"+i+"_width");
                int height=getIntParameter("S"+i+"_height");
                int x=getIntParameter("S"+i+"_x");
                int y=getIntParameter("S"+i+"_y");
                String text=getParameter("S"+i+"_text");
                if (text!=null && text.trim().length()<1) text=null;
                
                OrderingSource s=new OrderingSource(this, ident, uri, text, (x>=0)?x:0, (y>=0)?y:0, width, height, orientation);
				addSource(s);
				if (uri!=null){
					//System.out.println("source_uri="+uri);
					try{
						URL uImage = getResourceURL(uri, serverUrl);
						//System.out.println("source="+uImage);
						Image imgSource = getImage(uImage);
						getMediaTracker().addImage(imgSource, i);
						s.setImage(imgSource);
						bContainsImages = true;
					}catch (Exception e){
						e.printStackTrace();
					}
				}
                i++;
            }
            else darrerParam=true;
        }
        
        String sShuffle = getParameter("shuffle");
        if (sShuffle!=null && sShuffle.equalsIgnoreCase("Yes")){
        	// Desordenar les opcions
			int[] iOrder = randomArray(1, vSources.size());
			String sArray = "[";
			for (int k=0;k<iOrder.length;k++){
				sArray += iOrder[k]+",";
			}
			sArray += "]";
			//System.out.println("size="+vSources.size()+" orderArray="+sArray);
			for (int j=0;j<vSources.size();j++){
				OrderingSource os = (OrderingSource)vSources.elementAt(j);
				//System.out.println("posicio old="+os.getPosition()+"  new="+(iOrder[j]+1));
				os.setPosition(iOrder[j]+1);
			}        	
        }

        
        String img_url=getParameter("image_src");
        ////////if (img_url!=null && !img_url.endsWith("../")) c.setBGImage(img_url); //IMATGE DE FONS
        
        String sName=getParameter("name");
        setName(sName);
        
        String initParam=getParameter("INITPARAM");
        repaint();
        return initParam;
    }
    
    
    public void addSource(OrderingSource s){
        vSources.addElement(s);
        if (!isEditable){
            //setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
            return;
        }
        setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        c.addMouseListener(s);
        c.addMouseMotionListener(s);
    }
    
    protected OrderingSource getSourceWithId(String id){
        OrderingSource s=null;
        java.util.Enumeration e=vSources.elements();
        boolean bFound=false;
        while (e.hasMoreElements() && !bFound){
            OrderingSource s2=(OrderingSource)e.nextElement();
            if (s2.getIdent().equals(id)) return s2;
        }
        return s;
    }
    
    protected OrderingSource getSourceAtPosition(int iPosition){
        OrderingSource s=null;
        java.util.Enumeration e=vSources.elements();
        boolean bFound=false;
        while (e.hasMoreElements() && !bFound){
            OrderingSource s2=(OrderingSource)e.nextElement();
            if (s2.getPosition()==iPosition) return s2;
        }
        return s;
    }
    
    public void setCursor(java.awt.Cursor cursor){
        super.setCursor(cursor);
        if (c!=null) c.setCursor(cursor);
    }
    
    private int getIntParameter(String s){
        String sParam=getParameter(s);
        int i=-1;
        if (sParam!=null && sParam.trim().length()>0){
            try{
                i=Integer.parseInt(sParam.trim());
                if (i<0) i=-1;
            }
            catch (Exception e){
                System.out.println("Excepció en el format de "+s+": "+e);
            }
        }
        return i;
    }
    
    public boolean imageUpdate(java.awt.Image image, int param, int param2, int param3, int param4, int param5) {
        boolean b=super.imageUpdate(image, param, param2, param3, param4, param5);
        c.repaint();
        return b;
    }
    
}
