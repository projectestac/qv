/*
 * QVAppletJS.java
 *
 * Created on 17 / febrer / 2003, 09:17
 */
/**
 *
 * @author  allastar
 */

import java.awt.MediaTracker;
import java.util.Hashtable;
import java.util.StringTokenizer;


public abstract class QVAppletJS extends java.applet.Applet{
	
	public static String INIT_PARAM="INITPARAM";
	protected String sName=null;
	protected String urlQuadernBase;
	protected MediaTracker tracker;
	protected Hashtable hStyle = null;
	
    public boolean isEditable=true;
	
	
	/** Creates a new instance of QVAppletJS */
	public QVAppletJS(){
	}
	
	public void destroy(){
		tracker = null;
	}

	public void setName(String sName) {
		this.sName=sName;
	}
	
	public abstract String getStringRepresentation();
	
	public abstract boolean initFromParam(String param);
	
	public void showStatus(String s){
		initFromParam(s);
	}
	
	public void init(){
		super.init();
		//System.out.println("init");
		String param=getParameter(INIT_PARAM);
		initFromParam(param);
		//repaint(0);
	}
	
	public void start(){
		//System.out.println("start");
		repaint(0);
	}
	
	public void updateParam(){
		/*try{
		 JSObject js=JSObject.getWindow(this);
		 String[] sParam=new String[2];
		 sParam[0]=sName;
		 sParam[1]=getStringRepresentation();
		 
		 //if (sName!=null) js.eval("javascript:document.forms[0]."+sName+".value="+sParam[0]);
		  //System.out.println("-->"+"set_param_applet("+sName+","+sParam[1]+")<--");
		   if (sName!=null) js.call("set_param_applet",sParam);
		   js=null;
		   }
		   catch (Exception e){
		   System.out.println("Excepció JS:"+e);
		   e.printStackTrace(System.out);
		   }*/
	}
	
	public void setEditable(boolean b){
    	System.out.println("QVOrdering.setEditable-> "+b);
		this.isEditable=b;
		setEnabled(b);
	}
	
	
	public java.net.URL getResourceURL(String sResource){
		return getResourceURL(sResource, urlQuadernBase);
	}
	
	public java.net.URL getResourceURL(String sResource, String sServerURL){
		return getResourceURL(sResource, sServerURL, urlQuadernBase);
	}
	
	public java.net.URL getResourceURL(String sResource, String sServerURL, String urlQuadernBase){
		java.net.URL u=null;
		java.net.URL uBase=getDocumentBase();
		if (sResource!=null){
			try{
				if (sServerURL==null || sServerURL.trim().length()==0){
					String sDocBase = getDocumentBase().toString();
					if (sDocBase.indexOf("?")>0){
						sDocBase=sDocBase.substring(0, sDocBase.indexOf("?"));
					}
					u = new java.net.URL(sDocBase+urlQuadernBase+sResource);
					//System.out.println("u="+u+"  doc="+sDocBase+"  base="+urlQuadernBase+" res="+sResource);
					return u;
				}
				
				
				/*if (sServerURL==null){
					String path=uBase.getFile();
					sServerURL=path; ////
				}*/
				
				String port;
				if (uBase.getPort()<0) port="";
				else port=":"+uBase.getPort();
				
				String sContext = ((sServerURL!=null && sServerURL.trim().length()>0)?(sServerURL.substring(0,sServerURL.lastIndexOf("/"))):"");
				if (sResource.indexOf(":/")>0 || sResource.indexOf(":\\")>0){
					//u=new java.net.URL(uBase.getProtocol()+"://"+uBase.getHost()+port+sContext+"/getImage?url="+sResource);
					u=new java.net.URL(sResource);
				} else if (urlQuadernBase.endsWith("/")){
					//u=new java.net.URL(uBase.getProtocol()+"://"+uBase.getHost()+port+sContext+"/getImage?url="+urlQuadernBase+sResource);
					u=new java.net.URL(urlQuadernBase+sResource);
				} else{
					//u=new java.net.URL(uBase.getProtocol()+"://"+uBase.getHost()+port+sContext+"/getImage?url="+urlQuadernBase+"/"+sResource);
					u=new java.net.URL(urlQuadernBase+"/"+sResource);
				}
				///////	Això no anava u=new java.net.URL(urlQuadernBase+sResource);////
	
				//u=getClass().getResource(sResource);
				//u=new java.net.URL(getDocumentBase().toString()+"/"+sResource);
	
				//System.out.println("resource_url="+u.toString()+"    urlQuadernBase:"+urlQuadernBase+"    sResource="+sResource+"   serverURL="+sServerURL+"   documentBase="+getDocumentBase());
				//System.out.println("file="+uBase.getFile());
			}catch (Exception e){
				e.printStackTrace(System.out);
			}
		}
		return u;
	}
	
	protected MediaTracker getMediaTracker(){
		if (tracker==null){
			tracker = new MediaTracker(this);
		}
		return tracker;
	}
	
	public Hashtable getStyles(){
		if (hStyle==null){
			hStyle = new Hashtable();
			String sStyle = getParameter("style");
			if (sStyle!=null){
				StringTokenizer st=new StringTokenizer(sStyle, ",");
				while (st.hasMoreTokens()){
					String sAtt = st.nextToken();
					String sAttName = sAtt.substring(0, sAtt.indexOf(":"));
					String sAttValue = sAtt.substring(sAtt.indexOf(":")+1).trim();
					hStyle.put(sAttName, sAttValue);
				}
			}
		}
		return hStyle;
	}
	
	
	protected int[] randomArray(int iMin, int iMax){
		int iSize = iMax - iMin + 1;
		int[] randomArray = new int[iSize];
		for (int i=0;i<iSize;i++){
			randomArray[i] = i;
		}
		for (int i=0;i<iSize;i++){
			int iTmp = randomArray[i];
			int iRandom = randomInt(iMin, iMax)-iMin;
			randomArray[i] = randomArray[iRandom];
			randomArray[iRandom] = iTmp;
		}
		return randomArray;
	}
	
	private int randomInt(int min, int max){
		return (int)((max-min+1)*Math.random()+min);		
	}
	
	private int random(int iMin, int iMax){
		long l = (iMax - iMin);
		if (l<0){
			l = (iMin - iMax);
		}
		double dSeed = Math.random();
		int iRandom = (int) (iMin + (l * dSeed + dSeed));
		return iRandom;
	}
	
	
	
	public String toString(){
		return getStringRepresentation();
	}
	
}
