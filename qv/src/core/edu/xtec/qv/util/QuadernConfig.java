package edu.xtec.qv.util;

import java.io.File;
import java.util.HashMap;

import org.apache.log4j.Logger;


public class QuadernConfig{
    
	protected static Logger logger = Logger.getRootLogger();
	protected final static String PROPERTIES_PATH = "/edu/xtec/resources/properties/";
	protected final static String CONFIG_FILE = "qv.properties";

    public static java.util.Properties prop=null;
    protected static java.util.HashMap hmQuadernXSL=null;
    protected static java.util.HashMap hmAssigXSL=null;
	protected static java.util.HashMap hmXSL=null;
	protected static HashMap hmXSLNames = null;
    protected static String urlBase=null;

	
	public static final String K_XSL_NAMES= "xsl.names";
	
    public static void iniConfiguration(){
        try{
            if (prop==null){
                prop=Utility.loadProperties(PROPERTIES_PATH, CONFIG_FILE);
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            e.printStackTrace(System.out);
            prop=null;
        }
    }
    
    public static String getPropertyNoURL(String sPropertyName){
        if (prop==null) iniConfiguration();
        if (prop!=null){
            String sValue=prop.getProperty(sPropertyName);
            return sValue;
        }
        else return null;
    }
    
    public static boolean getBooleanProperty(String sPropertyName){
        String sProp=getPropertyNoURL(sPropertyName);
        if (sProp!=null && sProp.trim().toLowerCase().equals("yes")) return true;
        else return false;
    }

    public static String getProperty(String sPropertyName){
        if (prop==null) iniConfiguration();
        if (prop!=null){
            String sValue=prop.getProperty(sPropertyName);
            sValue=transformURLValue(sValue);
            return sValue;
        }
        else return null;
    }
    
    protected static String transformURLValue(String sValue){
        if (sValue!=null && sValue.trim().length()>0 && sValue.indexOf("//")<0){
            if (urlBase==null) urlBase=prop.getProperty("urlBase");
            if (urlBase!=null) sValue=urlBase+sValue;
            //System.out.println("s2:"+sValue+"<--");
        }
        return sValue;
    }
    
    public static String transformURLValue(String href, String context/*javax.servlet.ServletContext sc*/){
        String sUrl;
        if (href!=null && href.trim().length()>0 && href.indexOf("//")<0){
            String sUrlBase=getUrlBase();
            if (sUrlBase==null || sUrlBase.trim().length()==0) {
                String s="";
                //if (sc!=null){
                if (context!=null){
                    s=context;//sc.getRealPath("/");
                    if (!s.endsWith("/") && !s.endsWith("\\")) s=s+"/";
                }
                //sUrl="file:///"+s+href;
                sUrl=(s.indexOf(":/")>0)?(s+href):("file:///"+s+href);
                
                //sUrl=new java.io.File(sc.getRealPath(href)).toURL().toString();
            }
            else sUrl=sUrlBase+href;
        }
        else sUrl=href;
        //System.out.println("href:"+href+"\nURL:"+sUrl+"\ncontext="+context+"<--");
        return sUrl;
    }
    
    public static String getUrlBase(){
        if (prop==null) iniConfiguration();
        if (urlBase==null) urlBase=prop.getProperty("urlBase");
        return urlBase;
    }
    
    public static java.util.Properties getClicProperties(){
        java.util.Properties clicProperties=null;
        if (prop==null) iniConfiguration();
        if (prop!=null){
            try{
                String sClicPropertiesURL=prop.getProperty("clicPropertiesURL");
                //System.out.println("clicPropertiesURL="+sClicPropertiesURL+"<--");
                clicProperties=new java.util.Properties();
                clicProperties.load(new java.net.URL(sClicPropertiesURL).openStream());
            }
            catch (Exception e){
                System.out.println("Excepció:"+e);
            }
        }
        return clicProperties;
    }
    
    public static String getDefaultXSLName(){
        String sXSLPaths=getProperty("xslQTIPaths");
        String sQuadernXSLName=null;
        try{
            java.util.StringTokenizer st=new java.util.StringTokenizer(sXSLPaths,";");
            sQuadernXSLName=st.nextToken();
        }
        catch(Exception e){
            System.out.println("Error en els paràmetres de configuració de l'xsl");
        }
        return sQuadernXSLName;
    }
    
    public static String getDefaultXSLPath(){
        String sXSLPaths=getProperty("xslQTIPaths");
        String sQuadernXSLPath=null;
        try{
            java.util.StringTokenizer st=new java.util.StringTokenizer(sXSLPaths,";");
            sQuadernXSLPath=st.nextToken(); //El nom, però potser s'han equivocat i posen només el path
            sQuadernXSLPath=transformURLValue(st.nextToken());
        }
        catch(Exception e){
            System.out.println("Error en els paràmetres de configuració de l'xsl: xslQTIPaths");
        }
        return sQuadernXSLPath;
    }
    
    public static String getDefaultAssigXSLName(){
        String sAssigXSLPaths=QuadernConfig.getProperty("xslAssigPaths");
        String sAssigXSLName=null;
        try{
            java.util.StringTokenizer st=new java.util.StringTokenizer(sAssigXSLPaths,";");
            sAssigXSLName=st.nextToken();
        }
        catch(Exception e){
            System.out.println("Error en els paràmetres de configuració de l'xsl: xslAssigPaths");
        }
        return sAssigXSLName;
    }
    
    public static String getDefaultAssigXSLPath(){
        String sAssigXSLPaths=QuadernConfig.getProperty("xslAssigPaths");
        String sAssigXSLPath=null;
        try{
            java.util.StringTokenizer st=new java.util.StringTokenizer(sAssigXSLPaths,";");
            sAssigXSLPath=st.nextToken();
            sAssigXSLPath=transformURLValue(st.nextToken());
        }
        catch(Exception e){
            System.out.println("Error en els paràmetres de configuració de l'xsl");
        }
        return sAssigXSLPath;
    }
    
    public static java.util.HashMap getPossibleXSLQuadern(){
        if (hmQuadernXSL==null) initPossibleXSLQuadern();
        return hmQuadernXSL;
    }
    
    protected static void initPossibleXSLQuadern(){
        if (hmQuadernXSL==null){
            hmQuadernXSL=new java.util.HashMap();
            String sXSLPaths=getProperty("xslQTIPaths");
            try{
                java.util.StringTokenizer st=new java.util.StringTokenizer(sXSLPaths,";");
                while (st.hasMoreTokens()){
                    String sQuadernXSLName=st.nextToken();
                    String sQuadernXSLPath=transformURLValue(st.nextToken());
                    hmQuadernXSL.put(sQuadernXSLName,sQuadernXSLPath);
                }
            }
            catch(Exception e){
                System.out.println("Error en els paràmetres de configuració de l'xsl de quaderns.");
            }
        }
    }
    
    public static java.util.HashMap getPossibleXSLAssig(){
        if (hmAssigXSL==null) initPossibleXSLAssig();
        return hmAssigXSL;
    }
    
    protected static void initPossibleXSLAssig(){
        if (hmAssigXSL==null){
            hmAssigXSL=new java.util.HashMap();
            String sAssigPaths=getProperty("xslAssigPaths");
            try{
                java.util.StringTokenizer st=new java.util.StringTokenizer(sAssigPaths,";");
                while (st.hasMoreTokens()){
                    String sAssigXSLName=st.nextToken();
                    String sAssigXSLPath=transformURLValue(st.nextToken());
                    hmAssigXSL.put(sAssigXSLName,sAssigXSLPath);
                }
            }
            catch(Exception e){
                System.out.println("Error en els paràmetres de configuració de l'xsl d'assignacions.");
            }
        }
    }
    
/*	public static String getXSLForQuadernName(String sQuadernName){
		if (hmQuadernXSL==null) initPossibleXSLQuadern();
		String sXSL=(hmQuadernXSL.containsKey(sQuadernName))?(hmQuadernXSL.get(sQuadernName).toString()):getDefaultXSLPath();
		return sXSL;
	}  
*/

	public static HashMap getXSL(){
		if (hmXSL==null){
			hmXSL=new HashMap();
			String sXSLPaths=getProperty("xsl.paths");
			try{
				java.util.StringTokenizer st=new java.util.StringTokenizer(sXSLPaths,";");
				while (st.hasMoreTokens()){
					String sXSLId=st.nextToken();
					String sXSLPath=transformURLValue(st.nextToken());
					hmXSL.put(sXSLId,sXSLPath);
				}
			}
			catch(Exception e){
				System.out.println("Error en els paràmetres de configuració de l'xsl");
			}
		}
		return hmXSL;
	}

	public static String getDefaultXSL(){
		return getXSLForQuadernName("default");
	}

	public static HashMap getXSLNames(){
		if (hmXSLNames==null){
			hmXSLNames=new HashMap();
			String sNames = QuadernConfig.getProperty(K_XSL_NAMES);
			try{
				java.util.StringTokenizer st=new java.util.StringTokenizer(sNames,";");
				while (st.hasMoreTokens()){
					String sId=st.nextToken();
					if (st.hasMoreTokens()){
						String sName=st.nextToken();
						hmXSLNames.put(sId,sName);
					}
				}
			}
			catch(Exception e){
				logger.error("ERROR obtenint el nom de les aparences-> "+e);
			}
		}
		return hmXSLNames;
	}
	
    public static String getXSLForQuadernName(String sQuadernName){
    	String sXSL = null;
    	if (getXSL().containsKey(sQuadernName)){
    		sXSL = getXSL().get(sQuadernName).toString();
    	}else{
    		String sSkinPath = "skins/"+sQuadernName+"/"+sQuadernName+".xsl";
    		try{
				java.io.InputStream is=QuadernConfig.class.getClassLoader().getResourceAsStream(sSkinPath);
				javax.xml.transform.Source xsl=null;
				if (is==null) xsl=new javax.xml.transform.stream.StreamSource(new java.net.URL(QuadernConfig.transformURLValue(sSkinPath, QuadernConfig.getProperty("context.qv"))).openStream());
				else xsl=new javax.xml.transform.stream.StreamSource(is);
				if (xsl!=null) sXSL=sSkinPath;
    		}catch (Exception e){
    	    	sXSL = null;
    		}
    	}
    	
        //String sXSL=(getXSL().containsKey(sQuadernName))?(getXSL().get(sQuadernName).toString()):null;
        return sXSL;
    }
    
    public static String getXSLForAssignName(String sAssigName){
        if (hmAssigXSL==null) initPossibleXSLAssig();
        String sXSL=(hmAssigXSL.containsKey(sAssigName))?(hmAssigXSL.get(sAssigName).toString()):getDefaultAssigXSLPath();
        return sXSL;
    }
    
}
