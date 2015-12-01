import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

public class QVDragDrop extends QVAppletJS{
    
    Vector vSources,vTargets;
    boolean bMoving=false;
    boolean bScaling=false;
    boolean bRotating=false;
    public DragDropCanvas c;
    
    String serverUrl;
    
    
    public boolean bInside = true;
    private boolean bEnableRotating=false;
    private boolean bEnableScaling=false;
    
    public String sAlign = "auto";

    private boolean showTargets = true;
    
    public QVDragDrop(){
        vSources=new Vector();
        vTargets=new Vector();
    }
    
	public void destroy(){
		super.destroy();
		if (c!=null) c=null;
		if (vSources!=null)	vSources=null;
		if (vTargets!=null) vTargets=null;
	}    
    
    
    public void addSource(Source s){
        vSources.addElement(s);
        if (!isEditable){
            //setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
            return;
        }
		setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        c.addMouseListener(s);
        c.addMouseMotionListener(s);
    }
    
    public void addTarget(Target t){
        vTargets.addElement(t);
    }
    
    public Target getTargetAt(int x,int y){
        java.util.Enumeration e=vTargets.elements();
        while (e.hasMoreElements()){
            Target t=(Target)e.nextElement();
            if (t.contains(x,y))
                return t;
        }
        return null;
    }
    
    public Target getTargetAt(Source s){
        java.util.Enumeration e=vTargets.elements();
        while (e.hasMoreElements()){
            Target t=(Target)e.nextElement();
            if (t.contains(s))
                return t;
        }
        return null;
    }

    public boolean isMoving(){ //Hi ha algun Source que s'està movent
        return bMoving;
    }
    
    public synchronized void setMoving(boolean b){ //Estableix si hi ha algun Source que s'està movent
        bMoving=b;
/*
	if (b)
		setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
	else
		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
*/		
    }
    
    public boolean isScaling(){ //Hi ha algun Source que s'està escalant
        return bScaling;
    }
    
    public synchronized void setScaling(boolean b){ //Estableix si hi ha algun Source que s'està escalant
        bScaling=b;
    }

    public boolean isRotating(){ //Hi ha algun Source que s'està rotant
        return bRotating;
    }
    
    public synchronized void setRotating(boolean b){ //Estableix si hi ha algun Source que s'està rotant
        bRotating=b;
    }
    
    public boolean isRotatingEnabled(){
    	return bEnableRotating;
    }
    
    public boolean isScalingEnabled(){
    	return bEnableScaling;
    }

    public boolean showTargets(){
	return showTargets;
    }
    
    //class DragDropCanvas extends Canvas implements ImageObserver{
    class DragDropCanvas extends javax.swing.JPanel implements ImageObserver{
        
        java.awt.Image i;
        java.awt.Image offscreenImg; //Doble buffer
        java.awt.Graphics offscreenG; //Doble buffer
        //ImageLoader imgL;
        
        public DragDropCanvas(){
        }
        
		public void setBGImage(java.awt.Image i){
			this.i = i;
		}
        
        public void setBGImage(String imageBack){
            try{
                i=getImage(getResourceURL(imageBack, serverUrl));
                //imgL=new ImageLoader(i,this,imageBack);
                //imgL.start();
            }
            catch (Exception e){
                e.printStackTrace(System.out);
            }
        }
        
        public void paint(Graphics gr){
        	try{
				if (tracker!=null && getMediaTracker().checkAll()){
					offscreenImg = createImage(getSize().width,getSize().height); //getWidth(), getHeight() a partir de v1.2
					offscreenG = offscreenImg.getGraphics();
				}else{
					gr.drawString("Carregant imatges...", 20,20);
					offscreenG = null;
				}

				if (offscreenG!=null){
					Graphics g=offscreenG;
					if (i!=null) {
						g.drawImage(i,0,0,i.getWidth(this),i.getHeight(this),null);
					}
                
					java.util.Enumeration e=vTargets.elements();
					while (e.hasMoreElements()){
						Target t=(Target)e.nextElement();
						t.paint(g);
					}
					for (int i=vSources.size()-1;i>=0;i--){
						Source s=(Source)vSources.elementAt(i);
						s.paint(g);
					}
					gr.drawImage(offscreenImg,0,0,this);
					//System.out.println("DragDrop doble buffer");
				}
        	}catch (Exception e){
        	}
        }
        
        public void update(Graphics g) {
            paint(g);
        }
        
        public void imageLoaded(){
            //Doble buffer
            try{
				offscreenImg = createImage(getSize().width,getSize().height); //getWidth(), getHeight() a partir de v1.2
				offscreenG = offscreenImg.getGraphics();
				repaint();
            }catch(Exception e){
            	System.out.println("EXCEPCIO obtenint imatge carregada -> "+e);
            }
        }
        
    }
    
    public boolean initFromParam(String param){
                /* Selecciona les caselles que tenen com id algun dels
                que apareixen al paràmetre param. */
        //System.out.println("initFromParam");
        if (param!=null){
            startParams();
            new ImageLoaderThread(tracker, c).start();
        }
        return true;
    }
    
    public String getStringRepresentation(){
    	String sSeparation = ":";
        StringBuffer s=new StringBuffer();
        java.util.Enumeration e=vSources.elements();
        int count=0;
        while (e.hasMoreElements()){
            Source src=(Source)e.nextElement();
            if (count>0) s.append(',');
	    //id_source#x,y,rotacio,escala

            s.append(src.getIdent()).append(sSeparation).append(src.getX()).append(sSeparation).append(src.getY()).append(sSeparation).append(src.getRotateDegrees()).append(sSeparation).append(src.getScale());
    	    if (src.hasTarget()){
    			//id_source#x,y,rotacio,escala:target
    			Target t=src.getTarget();
    			s.append(':').append(t.getIdent());
   		    }
            count++;
        }
        //System.out.println("rep:"+s.toString()+"<--");
        return s.toString();
    }
    
    public void startParams(){
        //System.out.println("start");
        vSources=new java.util.Vector();
        vTargets=new java.util.Vector();
        c=new DragDropCanvas();
        removeAll();
        setLayout(new BorderLayout());
        add(c,BorderLayout.CENTER);
        String initParam=getParams();        
        selectInitialResponses(initParam);
    }
    
    public void selectInitialResponses(String initParam){
        //initParam és de la forma:   id_source1:id_target1,id_source2:id_target2, .. , id_sourcen:id_targetn
        //System.out.println("QVDragDrop.selectInitialResponses()-> init="+initParam);
        Hashtable hCountTargets = new Hashtable();
        java.util.StringTokenizer st=new java.util.StringTokenizer(initParam,",");
        while (st.hasMoreTokens()){
            String s=st.nextToken();
            StringTokenizer stSrc=new StringTokenizer(s,":");
            if (stSrc.hasMoreTokens()){
            	String sSource = stSrc.nextToken();
            	String sTarget = null;
                if (stSrc.hasMoreTokens()){
                	Source oSource = getSourceWithId(sSource);
                	if (oSource!=null){
	                	int iX =Integer.parseInt(stSrc.nextToken());
	                	int iY =Integer.parseInt(stSrc.nextToken());
	                	oSource.setPosition(iX, iY);
	                	int iDegree =Integer.parseInt(stSrc.nextToken());
	                	oSource.setRotate(iDegree);
	                	double dRatio =Double.parseDouble(stSrc.nextToken());
	                	oSource.setRatio(dRatio);
	                	if (stSrc.hasMoreTokens()){
	                		sTarget=stSrc.nextToken();
	                	}
	                    //System.out.println("QVDragDrop.selectInitialResponses()-> x="+iX+" y="+iY+" deg="+iDegree+"  ratio="+dRatio+" target="+sTarget);
                	}
                }
                if (sTarget!=null){
                    int iIndex = 1;
                    if (hCountTargets.containsKey(sTarget)){
    									iIndex = ((Integer)hCountTargets.get(sTarget)).intValue()+1;
                    }
    								hCountTargets.put(sTarget, new Integer(iIndex));
                    join(sSource,sTarget,iIndex);                	
                }
            }
        	
/*
            int i=s.indexOf(':');
            //System.out.println("s="+s+" i="+i);
            if (i>0){
                String srcId=s.substring(0,i);
                String tgtId=s.substring(i+1);
                int iIndex = 1;
                if (hCountTargets.containsKey(tgtId)){
					iIndex = ((Integer)hCountTargets.get(tgtId)).intValue()+1;
                }
				hCountTargets.put(tgtId, new Integer(iIndex));
                join(srcId,tgtId,iIndex);
            }
*/            
        }
        repaint();
    }
    
    public void join(String srcId, String tgtId, int iIndex){
        Source s=getSourceWithId(srcId);
        Target t=getTargetWithId(tgtId);
        if (s!=null && t!=null){
            //System.out.println("join("+srcId+","+tgtId+")");
            ////t.addSource(s, iIndex, sAlign);
	    t.addSource(s, -1, "none");
	    //No s'ha d'aliniar perquè les coordenades s'estan llegint dels paràmetres
	    //iIndex=-1 perquè la posició inicial ja ve fixada
            s.setTarget(t);
        }
    }
    
    protected Source getSourceWithId(String id){
        Source s=null;
        java.util.Enumeration e=vSources.elements();
        boolean bFound=false;
        while (e.hasMoreElements() && !bFound){
            Source s2=(Source)e.nextElement();
            if (s2.getIdent().equals(id)){ // Trobat
                if (!s2.hasTarget()) bFound=true; //No tenia target associat
                s=s2; //Si té target associat, busco si hi ha un altre source amb aquest id, si no hi és li associaré a aquest.
            }
        }
        return s;
    }
    
    protected Target getTargetWithId(String id){
        Target t=null;
        java.util.Enumeration e=vTargets.elements();
        boolean bFound=false;
        while (e.hasMoreElements() && !bFound){
            Target t2=(Target)e.nextElement();
            if (t2.getIdent().equals(id)){ // Trobat
                bFound=true;
                t=t2;
            }
        }
        return t;
    }
    
    public String getParams(){
        //System.out.println("getParams");
        boolean darrerParam=false;
        int i=1; //numparams
        
		try{
        urlQuadernBase=getParameter("url_base");
        serverUrl=getParameter("serverUrl");
        
		String img_url=getParameter("image_src");
		//c.setBGImage(img_url);
		java.awt.Image bgImage = getImage(getResourceURL(img_url, serverUrl));
		c.setBGImage(bgImage);
		getMediaTracker().addImage(bgImage, 0);

		String sDisabled=getParameter("disabled");
		if (sDisabled!=null && sDisabled.trim().toLowerCase().equals("true")) isEditable=false;
		else isEditable=true;

		bInside = "Yes".equalsIgnoreCase(getParameter("inside"));
		//System.out.println("QVDragDrop.getParams()-> inside?"+bInside);
		bEnableRotating = "Yes".equalsIgnoreCase(getParameter("enable_rotating"));
		bEnableScaling = "Yes".equalsIgnoreCase(getParameter("enable_scaling"));
		sAlign = getParameter("align");

		String sShowTargets=getParameter("showTargets");
		if (sShowTargets!=null && sShowTargets.trim().toLowerCase().equals("false")) showTargets=false;
		else showTargets=true;

		int iSMaxWidth=getIntParameter("Smax_width"); //Màxima amplada dels Source
		int iSMinWidth=getIntParameter("Smin_width"); //Mínima amplada dels Source
		int iSMaxHeight=getIntParameter("Smax_height"); //Màxima alçada dels Source
		int iSMinHeight=getIntParameter("Smin_height"); //Mínima alçada dels Source
		double iSNratio=getDoubleParameter("Snratio"); //El factor amb el que es poden escalar els Source ha de ser múltiple de nratio
		int iSNrotate=getIntParameter("Snrotate"); //Els graus que es podet moure el Source han de ser múltiples de nrotate 
		
        while (!darrerParam){
            String ident=getParameter("T"+i+"_ident");
            if(ident!=null){ //És target
                String rarea=getParameter("T"+i+"_rarea");
                String text=getParameter("T"+i+"_text");
                int rotate=getIntParameter("T"+i+"_rotate");
                Target t=new Target(this, ident, rarea, false, "", text, getStyles());
		if (rotate>=0)
                    	t.setRotate(rotate);
                addTarget(t);
                i++;
            }
            else{ //Pot ser source
                ident=getParameter("S"+i+"_ident");
                //System.out.println("QVDragDrop.getParams()-> "+ident);
                if(ident!=null){
                    String uri=getParameter("S"+i+"_uri");
                    int width=getIntParameter("S"+i+"_width");
                    int height=getIntParameter("S"+i+"_height");
                    int rotate=getIntParameter("S"+i+"_rotate");
                    double ratio=getDoubleParameter("S"+i+"_ratio");
                    int iSiMaxWidth=getIntParameter("S"+i+"_max_width"); //Màxima amplada dels Source
                    int iSiMinWidth=getIntParameter("S"+i+"_min_width"); //Mínima amplada dels Source
            		int iSiMaxHeight=getIntParameter("S"+i+"_max_height"); //Màxima alçada dels Source
            		int iSiMinHeight=getIntParameter("S"+i+"_min_height"); //Mínima alçada dels Source
            		double iSiNratio=getDoubleParameter("S"+i+"_nratio"); //El factor amb el que es pot escalar el Source ha de ser múltiple de nratio
            		int iSiNrotate=getIntParameter("S"+i+"_nrotate"); //Els graus que es pot moure el Source han de ser múltiples de nrotate 
            		
                    int x=getIntParameter("S"+i+"_x0");
                    if (x<0){
						x=getIntParameter("S"+i+"_x");                    	
                    }
                    int y=getIntParameter("S"+i+"_y0");
					if (y<0){
						y=getIntParameter("S"+i+"_y");
					}
                    String text=getParameter("S"+i+"_text");
                    if (text!=null && text.trim().length()<1) text=null;
                    
                    Source s = new Source(this, ident, uri, text, getAlign(), (x>=0)?x:50, (y>=0)?y:50, width, height);
                    if (rotate>=0)
                    	s.setRotate(rotate);
                    if (ratio>=0)
                    	s.setRatio(ratio);
                    //S'estableixen les dimensions màximes i mínimes globals per als Source
                    if (iSMaxWidth>0)
                    	s.setMaxWidth(iSMaxWidth);
                    if (iSMaxHeight>0)
                    	s.setMaxHeight(iSMaxHeight);
                    if (iSMinWidth>0)
                    	s.setMinWidth(iSMinWidth);
                    if (iSMinHeight>0)
                    	s.setMinHeight(iSMinHeight);
                    
                    //S'estableixen les dimensions màximes i mínimes particulars del Source (tenen prioritat)
                    if (iSiMaxWidth>0)
                    	s.setMaxWidth(iSiMaxWidth);
                    if (iSiMaxHeight>0)
                    	s.setMaxHeight(iSiMaxHeight);
                    if (iSiMinWidth>0)
                    	s.setMinWidth(iSiMinWidth);
                    if (iSiMinHeight>0)
                    	s.setMinHeight(iSiMinHeight);

                    //S'estableix el factor global amb el que es poden escalar i rotar els Source
            		if (iSNratio>0)
                    	s.setNratio(iSNratio);
                    if (iSNrotate>0)
                    	s.setNrotate(iSNrotate);
                        
                    //S'estableix el factor particular d'aquest Source amb el que es poden escalar i rotar els Source (té prioritat)
            		if (iSiNratio>0)
                    	s.setNratio(iSiNratio);
                    if (iSiNrotate>0)
                    	s.setNrotate(iSiNrotate);
                    
                    java.net.URL imgURL = getResourceURL(uri, serverUrl);
                    //System.out.println("QVDragDrop.getParams()-> imgURL="+imgURL);
                    if (imgURL!=null){
                    	java.awt.Image imgSource = getImage(imgURL);
                    	s.setImage(imgSource);
    					getMediaTracker().addImage(imgSource, i);
                    }
                    addSource(s);
                    i++;
                }
                else darrerParam=true;
            }
        }

		String sShuffle = getParameter("shuffle");
		if ("Yes".equals(sShuffle)){
			randomizeSources();
		}

        String sName=getParameter("name");
        setName(sName);        
		}catch(Exception e){
			e.printStackTrace();
		}

        String initParam=getParameter("INITPARAM");
        
        return initParam;
    }
    
    private String getAlign(){
    	if (sAlign==null) sAlign = "auto";
    	return sAlign;
    }
    
    private void randomizeSources(){
		int[] iOrder = randomArray(1, vSources.size());
		for (int i=0;i<iOrder.length;i++){
			Source source1 = (Source)vSources.elementAt(i);
			Source source2 = (Source)vSources.elementAt(iOrder[i]);
			Point point = new Point(source1.x, source1.y);
			source1.setPosition(source2.x, source2.y);
			source2.setPosition(point.x, point.y);
		}        	
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
    
    private double getDoubleParameter(String s){
        String sParam=getParameter(s);
        double d=-1;
        if (sParam!=null && sParam.trim().length()>0){
            try{
                d=Double.parseDouble(sParam.trim());
                if (d<0) d=-1;
            }
            catch (Exception e){
                System.out.println("Excepció en el format de "+s+": "+e);
            }
        }
        return d;
    }
    
    public void setCursor(java.awt.Cursor cursor){
        super.setCursor(cursor);
        if (c!=null) c.setCursor(cursor);
    }

    public java.awt.Cursor getRotateCursor(){
	if (rotateCursor==null){
		java.net.URL u=getClass().getResource("rotateCursor.gif");
		//System.out.println("u:"+(u!=null?u.toString():"null"));
		if (u!=null){
			java.awt.Image cursor = getImage(u);
			rotateCursor=java.awt.Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(14,14) , "Rotació");
		}
	}
	return rotateCursor;

    }
    
    private java.awt.Cursor rotateCursor = null;
}





