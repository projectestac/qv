/*
 * ZipFolder.java
 *
 * Created on 26 / maig / 2004
 */

package edu.xtec.qv.servlet.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 *
 * @author  fbusquet
 * @author  sarjona
 * @version
 */
public class ZipFolder extends HttpServlet {
    
	protected static Logger logger = Logger.getRootLogger();

    public static final String P_PATH="path";
    public static final String P_FOLDER="folder";
    public static final String P_URL="url";
    public static final String P_OK="ok";
    public static final String P_FNAME="fname";    
    public static final String P_INCLUDEALL="includeall";    
    public static final String SERVLET_NAME="zip";
    
    public static final String P_ERROR = "error";
	public static final String P_FILECOUNT = "filecount";    
	public static final String P_TOTALSIZE = "totalsize";    
    public static final String M_NO_FILES = "zip.error.noFiles";
	public static final String M_TOO_MUCH_FILES = "zip.error.tooMuchFiles";
	public static final String M_SERVER_ERROR = "zip.error.internalServerError";
	public static final String M_ALERT_SIZE = "zip.error.alertSize";
	
	protected String folder;
	protected File fXML;
    
    private int fileCount;
    private long totalSize;
    private boolean exitLoop;
    private String path = "";
    private String zipFolder;

    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    	path = request.getParameter(P_PATH);
    	folder = request.getParameter(P_FOLDER);
    	String sURL = request.getParameter(P_URL);
        File file= getFile(path, folder);
        //logger.debug("file="+file);
        if(file==null || !file.exists()){
            response.sendError(400, "Parametre incorrecte: file="+file);
            return;
        }
		zipFolder = file.getAbsolutePath();
        try{
			if(!"true".equals(request.getParameter(P_OK))){
				resetCounters();
				countFiles(file);
				if(fileCount==0){
					//No files
					if (sURL!=null && sURL.length()>0){
						StringBuffer sb=new StringBuffer(sURL);
						sb.append("?"+P_ERROR+"=").append(M_NO_FILES);
						sb.append("&"+P_PATH+"=").append(path);
						sb.append("&"+P_FOLDER+"=").append(folder);
						response.sendRedirect(sb.substring(0));						
					}else{
						response.sendError(400, "ZIP file must have at least one entry");
					}
					return;
				}
				else if(fileCount>=Constants.MAX_FILES || totalSize>=Constants.MAX_SIZE){
					//Too much files
					if (sURL!=null && sURL.length()>0){
						StringBuffer sb=new StringBuffer(sURL);
						sb.append("?"+P_ERROR+"=").append(M_TOO_MUCH_FILES);
						sb.append("&"+P_PATH+"=").append(path);
						sb.append("&"+P_FOLDER+"=").append(folder);
						response.sendRedirect(sb.substring(0));						
					}else{
						response.sendError(400, "Too much files to ZIP");
					}
					return;
				}
				else if(totalSize>Constants.ALERT_SIZE){
					//Avís tamany
					if (sURL!=null && sURL.length()>0){
						StringBuffer sb=new StringBuffer(sURL);
						sb.append("?"+P_ERROR+"=").append(M_ALERT_SIZE);
						sb.append("&"+P_PATH+"=").append(path);
						sb.append("&"+P_FOLDER+"=").append(folder);
						sb.append("&"+P_FILECOUNT+"=").append(java.text.NumberFormat.getInstance().format(fileCount));
						sb.append("&"+P_TOTALSIZE+"=").append(java.text.NumberFormat.getInstance().format(totalSize/1024));
						response.sendRedirect(sb.substring(0));						
					}else{
						response.sendError(400, "Alert size");
					}
					return;
				}
			}
                        
            response.setContentType("application/x-zip-compressed");
            String fname=request.getParameter(P_FNAME);
            if(fname==null || fname.length()<=0){
            	fname = folder;
            }
            response.setHeader("Content-disposition", "filename="+fname+".qv.zip");
            ZipOutputStream out=new ZipOutputStream(response.getOutputStream());
            //out.setComment("ZIP generat automaticament");
			try{
				writeFolder(out, file);
				boolean bIncludeAll = !"false".equalsIgnoreCase(request.getParameter(P_INCLUDEALL)); 
				writeHTML(out, bIncludeAll);
	            out.close();				
	            logger.info("S'ha generat correctament el ZIP de '"+file+"'");
			}catch (Exception e){
				throw e;
			}finally{
			}
        } catch(Exception ex){
        	if (ex.getClass().toString().indexOf("ClientAbortException")<0){
            	logger.error("EXCEPCIO comprimint ZIP -> "+ex);
        		ex.printStackTrace();
                System.err.println(ex.getMessage());
        	}
            if(!response.isCommitted())
				//Internal server error
				if (sURL!=null && sURL.length()>0){
					StringBuffer sb=new StringBuffer(sURL);
					sb.append("?"+P_ERROR+"=").append(M_SERVER_ERROR);
					sb.append("&"+P_PATH+"=").append(path);
					sb.append("&"+P_FOLDER+"=").append(folder);
					response.sendRedirect(sb.substring(0));
				}
        }
    }
    
    protected void writeHTML(ZipOutputStream out, boolean bIncludeAll){
    	try{
    		// Create HTML files
	        Hashtable hHTML = QTITransformer.createHTML(getXMLFile().getPath(), "default");
	        Enumeration enumHTML = hHTML.keys();
	        while(enumHTML.hasMoreElements()){
	        	String sName = (String)enumHTML.nextElement();
				File oFile = new File("html", sName);
	        	ByteArrayOutputStream bos = (ByteArrayOutputStream)hHTML.get(sName);
	        	ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
	            writeZipEntry(out, bis, oFile.toString());
	        }
    		// Add CSS, js...
	        if (bIncludeAll){
	        	File oFile = new File(Constants.CSS_JS_PATH);
	        	writeFolder(out, oFile, "html", true);
	        }else{
	        	File oFile = new File(Constants.CSS_JS_PATH+Constants.JS_FILE);
	        	writeZipEntry(out, oFile, "html"+Constants.JS_FILE);
	        }
    	}catch (Exception e){
    		logger.error("EXCEPTION-> "+e.toString());
    		//e.printStackTrace();
    	}
    }
    
    private File getFile(String sBasePath, String sFolder) throws IOException{
    	File fFile = null;
    	if (sBasePath!=null && sFolder!=null){
			if(sBasePath.endsWith(File.separator))
				sBasePath=sBasePath.substring(0, sBasePath.length()-File.separator.length());
			if(sFolder.startsWith(File.separator))
				sFolder=sFolder.substring(File.separator.length());
			if(sFolder.endsWith(File.separator))
				sFolder=sFolder.substring(0, sFolder.length()-File.separator.length()-1);
			//fFile = new File(sBasePath, sFolder).getCanonicalFile();
			fFile = new File(sBasePath, sFolder);
		}
    	return fFile;
    }
    
    private void resetCounters(){
        fileCount=0;
        totalSize=0;
        exitLoop=false;
    }
    
    private void countFiles(File file) throws Exception{
        if(!exitLoop){
            File[] files=file.listFiles();
            for(int i=0; i<files.length && !exitLoop; i++){
                if(Constants.INCLUDE_HIDDEN_FILES || !files[i].isHidden()){
                    if(files[i].isDirectory()){
                        countFiles(files[i]);
                    }
                    else{
                        fileCount++;
                        totalSize+=files[i].length();
                        if(fileCount>=Constants.MAX_FILES || totalSize>=Constants.MAX_SIZE)
                            exitLoop=true;
                    }
                }
            }
        }
    }
    
    private void writeFolder(ZipOutputStream out, File file) throws Exception{
    	writeFolder(out, file, null, false);
    }    
    
    private void writeFolder(ZipOutputStream out, File file, String sBase, boolean bRecursive) throws Exception{
    	File fTmpXML = null;
        if(!exitLoop){
            File[] files=file.listFiles();
            for(int i=0; i<files.length && !exitLoop; i++){
                if((Constants.INCLUDE_HIDDEN_FILES || !files[i].isHidden()) && !"CVS".equalsIgnoreCase(files[i].getName())){
                    if(files[i].isDirectory()){
                    	if (bRecursive) writeFolder(out, files[i], (sBase!=null)?sBase+File.separator+files[i].getName():files[i].getName(), bRecursive);
                    }
                    else{
                        fileCount++;
                        totalSize+=files[i].length();
                        if(Constants.AVOID_BIG_FILES && (fileCount>=Constants.VERY_MAX_FILES || totalSize>=Constants.VERY_MAX_SIZE))
                            exitLoop=true;
                        else{
                        	//logger.debug("last="+files[i].getCanonicalPath().lastIndexOf(File.separator)+"  file="+files[i].getCanonicalPath().substring(files[i].getCanonicalPath().lastIndexOf(File.separator)));
                        	String name = null;
                        	int iLast = files[i].getCanonicalPath().lastIndexOf(File.separator);
                        	if (iLast>=0){
                        		name=files[i].getCanonicalPath().substring(iLast);
                        	}else{
                                name=files[i].getCanonicalPath().substring(zipFolder.length());                        		
                        	}
                            if (name.startsWith(File.separator))
                            	name = name.substring(File.separator.length());
							if (Constants.SHOW_LOG)
								logger.info("Adding: " +name);
							
							if (files[i].getName().endsWith(".xml") && !files[i].getName().startsWith("imsmanifest")){
								fTmpXML	= files[i];
								if (files[i].getName().startsWith(getFolder())){
									fXML = files[i];									
								}
							}
							if (sBase!=null && sBase.trim().length()>0){
								File oFile = new File(sBase, name);
								name = oFile.getPath();
							}
                        	writeZipEntry(out, files[i], name);
                        }
                    }
                }
            }
        }
        if (fXML==null) fXML = fTmpXML;
    }
    
    protected void writeZipEntry(ZipOutputStream out, File fFile, String sName) throws Exception{
        FileInputStream fis=new FileInputStream(fFile);
        writeZipEntry(out, fis, sName);
    }
    
    
    protected void writeZipEntry(ZipOutputStream out, InputStream is, String sName) throws Exception{
        ZipEntry entry=new ZipEntry(sName);
        out.putNextEntry(entry);
		try{
            byte[] buffer=new byte[1024];
            int read=is.read(buffer);
            while(read>=0){
                if(read>0)
                    out.write(buffer, 0, read);
                Thread.yield();
                read=is.read(buffer);
            }
		}catch (Exception e){
			throw e;
		}finally{
			is.close();
		}
        out.closeEntry();
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Generador dinamic de fitxers zip que contenen una carpeta sencera";
    }
    
    protected String getFolder(){
    	return folder;
    }
    
    protected File getXMLFile(){
    	return fXML;
    }
    
}
