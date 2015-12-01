package edu.xtec.qv.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


/*
 * ImageProxy.java
 *
 * Created on 5 / maig / 2003, 13:59
 */

/**
 *
 * @author  allastar
 */
public class ImageProxyServlet extends HttpServlet{
    
    protected static String[] imageExtensions=new String[]{"gif","jpeg","jpg","png","bmp"};
	protected static Logger logger = Logger.getRootLogger();
    
	public ImageProxyServlet(){
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, java.io.IOException {
		processRequest(request, response);
	}
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, java.io.IOException {
		processRequest(request, response);
	}
    
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, java.io.IOException {
		String sImageUrl=null;
		try{
			//printCookies(request);
			sImageUrl=request.getParameter("url");
			//System.out.println("ImageProxyServlet-> "+sImageUrl);
			if (sImageUrl!=null && sImageUrl.trim().length()>0){
				sImageUrl=sImageUrl.trim();
				int i=sImageUrl.lastIndexOf('.');
				if (sImageUrl.length()>i){
					String ext=sImageUrl.substring(i+1,sImageUrl.length());
					if (isImageExtension(ext)){
						writeImage(sImageUrl,ext,response);//response);
					}
				}
			}
		} catch(Exception ex){
			logger.error("EXCEPTION obtenint imatge '"+sImageUrl+"':"+ex);
			//ex.printStackTrace();
		}
	}
	
    
    protected void writeImage(String sImageUrl, String ext, HttpServletResponse response){
        try{
            java.net.URL u=new java.net.URL(sImageUrl);
            java.net.URLConnection c=u.openConnection();
            String sContentType=c.getContentType();
            java.io.InputStream is=c.getInputStream();
            
            response.setContentType(sContentType);
			java.io.OutputStream os=response.getOutputStream();
            byte[] buffer=new byte[1024];
            int bytesRead;
            boolean bEnd=false;
            while(!bEnd){
                bytesRead=is.read(buffer);
                if(bytesRead<=0) bEnd=true;
                else os.write(buffer, 0, bytesRead);
            }
			os.flush();
			os.close();
        }
        catch (java.io.IOException e){
            logger.error("EXCEPTION accedint a imatge:"+e);
        }
    }
    
    protected boolean isImageExtension(String ext){
        if (ext==null) return false;
        boolean isImageExtension=false;
        //for (int i=0;i<imageExtensions.length && !isImageExtension;i++) isImageExtension=ext.equalsIgnoreCase(imageExtensions[i]);
        return true;
        //return isImageExtension;
    }
    
    protected void printCookies(HttpServletRequest request){
    	Cookie[] cookies = request.getCookies();
		logger.debug("ImageProxyServlet-> COOKIES = "+cookies);
		if(cookies!=null && cookies.length>0){
			for (int i=0;i<cookies.length;i++){
				String key=cookies[i].getName();
				String value=cookies[i].getValue();
				logger.debug("COOKIE-> "+key+"="+value);
			}
		}
    	
    }

}
