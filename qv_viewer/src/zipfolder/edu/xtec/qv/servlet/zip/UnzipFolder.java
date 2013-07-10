/*
 * ZipFolder.java
 *
 * Created on 01 / juny / 2004
 */

package edu.xtec.qv.servlet.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.log4j.Logger;

/**
 *
 * @author  sarjona
 * @version
 */
public class UnzipFolder extends HttpServlet {
    
	protected static Logger logger = Logger.getRootLogger();

	protected static final int BUFFER = 2048;
	public static final String SERVLET_NAME="unzip";
	
    public static final String P_PATH="path";
    public static final String P_URL="url";
    public static final String P_MAX_SIZE="maxsize"; 
	public static final String P_FNAME="fname";    
    
    public static final String P_ERROR = "error";
    public static final String P_ZIP_FILE = "zipFile";
	public static final String M_NO_PATH = "unzip.error.noPath";
	public static final String M_NO_FILE = "unzip.error.noFile";
    public static final String M_NO_FILES = "unzip.error.noFiles";
	public static final String M_NO_ZIP_FILE = "unzip.error.noZipFile";
	public static final String M_MAX_SIZE = "unzip.error.maxSize";
	public static final String M_FILE_EXISTS = "unzip.error.fileExists";
    
    private String zipFile;
	private int fileCount;
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
		String sPath = request.getParameter(P_PATH);
		String sURL = request.getParameter(P_URL);
		String sFileName = request.getParameter(P_FNAME);
		String sMaxSize = request.getParameter(P_MAX_SIZE);
		long lMaxSize = Constants.MAX_SIZE;
		if (sMaxSize!=null){
			lMaxSize = Long.parseLong(sMaxSize);
		}
		//logger.debug("filename="+sFileName+" maxsize="+lMaxSize+" string="+sMaxSize);
		StringBuffer sb=new StringBuffer(sURL);
		try{
			if (sPath!=null && !sPath.equalsIgnoreCase("")){
				DiskFileUpload uBase = new DiskFileUpload();
				uBase.setSizeMax(lMaxSize);
				//uBase.setSizeThreshold(10000);
				if (Constants.REPOSITORY_PATH==null || Constants.REPOSITORY_PATH.trim().length()<=0){
					logger.debug("S'ha de definir REPOSITORY_PATH al fitxer de properties");            
				}
				uBase.setRepositoryPath(Constants.REPOSITORY_PATH);
				if (DiskFileUpload.isMultipartContent(request)){
					FileItem fiBrowse = null;
					Iterator itItems = uBase.parseRequest(request).iterator();
					while (itItems.hasNext()){
						FileItem tmpFileItem = (FileItem)itItems.next();
						//logger.debug(tmpFileItem.getFieldName());
						if (tmpFileItem.isFormField()){
							if (tmpFileItem.getFieldName().equalsIgnoreCase(P_FNAME)){
								sFileName = tmpFileItem.getString();
							}
						}else{
							fiBrowse = tmpFileItem;
						}
					}
					if (fiBrowse!=null){
						String sError = unzip(fiBrowse, sPath, sFileName);
						if (sError!=null && sError.trim().length()>0){
							logger.error("Ja existeix un directori amb aquest nom ('"+zipFile+"')");
							sb.append("?"+P_ERROR+"=").append(sError);							
						}else{
							if (fileCount<=0){
								logger.error("El ZIP ('"+zipFile+"') no conté cap fitxer o no és un fitxer vàlid");
								sb.append("?"+P_ERROR+"=").append(M_NO_FILES);
							}
						}
					}
					logger.info("S'ha descomprimit correctament el ZIP '"+zipFile+"'");
				} else{
					logger.error("No hi ha cap ZIP per descomprimir");
					sb.append("?"+P_ERROR+"=").append(M_NO_FILE);
				}
			}else{
				logger.error("No s'ha especificat el path on es descomprimirà el ZIP");
				sb.append("?"+P_ERROR+"=").append(M_NO_PATH);
			}
		}catch(NoZipFileException nze){
			logger.info("El fitxer ('"+sPath+"') no té una extensió vàlida de fitxer comprimit (.zip)");
			if (zipFile!=null && zipFile.length()>0)
				sb.append("?"+P_ERROR+"=").append(M_NO_ZIP_FILE);
			else
			sb.append("?"+P_ERROR+"=").append(M_NO_FILE);			
		}catch(SizeLimitExceededException e){
			logger.info("Tamany excedit: No s'ha pogut descomprimir el ZIP perque supera l'espai que queda lliure ("+lMaxSize+")");
			sb.append("?"+P_ERROR+"=").append(M_MAX_SIZE);
		}catch(FileUploadException fue){
			logger.error("El ZIP ('"+zipFile+"') no és un fitxer vàlid -> "+fue);
			sb.append("?"+P_ERROR+"=").append(M_NO_FILES);
		}catch(Exception ex){
			logger.error("EXCEPCIO unzipping file --> "+ex);
			ex.printStackTrace();
			System.err.println(ex.getMessage());
			if(!response.isCommitted())
				response.sendError(500, ex.getMessage());
		}finally{
			if (sb.substring(0).indexOf("?")<0){
				sb.append("?"+P_ZIP_FILE+"=").append(zipFile);
			}else{
				sb.append("&"+P_ZIP_FILE+"=").append(zipFile);
			}
			response.sendRedirect(sb.substring(0));
		}
    }
    
    
	public String getFileName(String sPath){
		String sFilename = null;
		if (sPath!=null){
			sFilename = sPath;
			int iIndex = sPath.lastIndexOf("\\"); 
			if (iIndex<0){
				iIndex = sPath.lastIndexOf("/");
			}
			if (iIndex>=0){
				sFilename = sPath.substring(iIndex+1);
			}
		}
		return sFilename;
	}    

	/**
	 * 
	 * @param file
	 * @param sPath
	 * @param sFileName
	 * @return error string code to send or null if no errors
	 * @throws Exception
	 */
	public String unzip(FileItem file, String sPath, String sFileName) throws Exception{
		String sError = null;
		String sOriginalZipFile = null;
		zipFile = getFileName(file.getName());
		if (zipFile==null || !zipFile.endsWith(".zip")){
			throw new NoZipFileException();
		}else{
			sOriginalZipFile = zipFile.substring(0, zipFile.lastIndexOf(".zip"));
			if (sFileName!=null && sFileName.length()>0){
				zipFile = sFileName;
			}else{
				zipFile = sOriginalZipFile;
			}
			// Filter file name
			zipFile = zipFile.trim();
			zipFile = zipFile.replace(' ', '_');
			zipFile = filterFilename(zipFile);
			zipFile = zipFile.toLowerCase();
			
		}
		File dir = new File(sPath+File.separator+zipFile);
		if (!dir.exists() || dir.list()==null || dir.list().length==0) {
			dir.mkdirs();
			ZipInputStream zis = null;
			BufferedOutputStream dest = null;
			try{
				zis = new ZipInputStream(new BufferedInputStream(file.getInputStream()));
				dest = null;
				ZipEntry entry;
				fileCount=0;
				while((entry = zis.getNextEntry()) != null) {
					if (Constants.SHOW_LOG)
						logger.info("Extracting: " +entry);
					fileCount++;
					int count;
					byte data[] = new byte[BUFFER];
					// write the files to the disk
					String sEntryName = entry.getName();
					if (sEntryName.endsWith(".xml") && sEntryName.indexOf("imsmanifest")<0){
						sEntryName = zipFile+".xml";
						//logger.debug("zip="+sEntryName);
					}
					
					if (sEntryName.indexOf("/")<0 && sEntryName.indexOf("\\")<0){
						File fFile = new File(dir+File.separator+sEntryName);
		                File fDirFile = fFile.getParentFile();
		                if(fDirFile!=null && !fDirFile.exists()){
		                	fDirFile.mkdirs();
		                }
		                
						if (entry.isDirectory()){
							fDirFile.mkdirs();
						}else{
							FileOutputStream fos = new FileOutputStream(dir.getCanonicalPath()+File.separator+sEntryName);
							dest = new BufferedOutputStream(fos, BUFFER);
							while ((count = zis.read(data, 0, BUFFER)) != -1) {
								dest.write(data, 0, count);
							}
							dest.flush();
							dest.close();
						}					
					}
				}
			}catch(Exception e){
				throw e;
			}finally{
				if (dest!=null){
					dest.flush();
					dest.close();
				}
				if (zis!=null){
					zis.close();
				}
			}
		}else{
			sError = M_FILE_EXISTS;
		}
		return sError;
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
        return "Descompressor dinamic de fitxers zip";
    }


	protected static String filterFilename(String input){
		String result=(input==null ? "" : input);
		if(input!=null && input.length()>0){
			StringBuffer filtered = new StringBuffer(input.length());
			char c;
			for(int i=0; i<input.length(); i++) {
				c = input.charAt(i);
				String s=null;
				switch(c){
					case '<': s="";	break;
					case '>': s="";	break;
					case '"': s="";	break;
					case '\'': s="";break;
					case '´': s=""; break;
					case '`': s=""; break;
					case '¨': s=""; break;
					case '&': s="";	break;
					case '\\': s="";break;
					case '/': s="";	break;
					case ':': s="";	break;
					case '*': s="";	break;
					case '¿': s="";	break;
					case '?': s="";	break;
					case '!': s="";	break;
					case 'á': s="a";break;
					case 'à': s="a";break;
					case 'ä': s="a";break;
					case 'é': s="e";break;
					case 'è': s="e";break;
					case 'ë': s="e";break;
					case 'í': s="i";break;
					case 'ì': s="i";break;
					case 'ï': s="i";break;
					case 'ó': s="o";break;
					case 'ò': s="o";break;
					case 'ö': s="o";break;
					case 'ú': s="u";break;
					case 'ù': s="u";break;
					case 'ü': s="u";break;
					case 'Á': s="A";break;
					case 'À': s="A";break;
					case 'Ä': s="A";break;
					case 'É': s="E";break;
					case 'È': s="E";break;
					case 'Ë': s="E";break;
					case 'Í': s="I";break;
					case 'Ì': s="I";break;
					case 'Ï': s="I";break;
					case 'Ó': s="O";break;
					case 'Ò': s="O";break;
					case 'Ö': s="O";break;
					case 'Ú': s="U";break;
					case 'Ù': s="U";break;
					case 'Ü': s="U";break;
				}
				if(s!=null)
					filtered.append(s);
				else
					filtered.append(c);
			}
			result=filtered.substring(0);
		}
		return result;
	}
       
}

class NoZipFileException extends Exception{
    	
	public NoZipFileException(){
	}
    	
	public String toString(){
		return "NoZipFileException";
	}

}
    
