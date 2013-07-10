/*
 * FileUtil.java
 * 
 * Created on 02/febrer/2004
 */
package edu.xtec.qv.editor.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.log4j.Logger;

import edu.xtec.qv.editor.beans.QVBean;
import edu.xtec.qv.qti.util.StringUtil;

/**
 * @author sarjona
 */
public class FileUtil {

	protected static Logger logger = Logger.getRootLogger();
	protected static final int BUFFER = 2048;

	private static Properties settings;

	static{
		settings=new Properties();
		try{
			settings.load(QVBean.class.getResourceAsStream(QVConstants.SETTINGS_PATH+QVConstants.SETTINGS_FILE));
			File f = new File(System.getProperty("user.home"), QVConstants.SETTINGS_FILE);
			if(f.exists()){
				FileInputStream is=new FileInputStream(f);
				settings.load(is);
				is.close();
			}
		} catch(Exception e){
			logger.fatal("ERROR FATAL!: No s'ha pogut llegir el fitxer "+QVConstants.SETTINGS_PATH+QVConstants.SETTINGS_FILE);
			e.printStackTrace(System.err);
		}
	}
    

	public static long getDiskSpace(String sFile){
		long lEspai = 0;
		if (sFile!=null && sFile.trim().length()>0){
			lEspai = getDiskSpace(new File(sFile));
		}
		return lEspai;
	}
	
	/**
	 * Obte l'espai que ocupa el fitxer o directori especificat
	 * @param fFile
	 * @return espai (en bytes) del fitxer o directori indicat.
	 * Si es tracta d'un directori, l'espai es la suma de tot el seu contingut
	 */
	public static long getDiskSpace(File fFile){
		long lQuote = 0;
		if (fFile!=null){
			if (fFile.isDirectory()){
				File[] files = fFile.listFiles();
				if (files!=null){
					for(int i=0;i<files.length;i++){
						lQuote+=getDiskSpace(files[i]);
					}
				}
			}
			lQuote += fFile.length();
		}
		return lQuote;
	}
	
	public static String[] getDirectories(String sRoot){
		String[] files = null;
		try{
			File fFile = new File(sRoot);
			files = fFile.list(new DirectoryFilter());
			Arrays.sort(files);
		} catch (Exception e){
			logger.error("EXCEPCIO obtenint els directoris de '"+sRoot+"' --> "+e);
		}
		return files;
		
	}
	
	public static String[] getFiles(String sRoot){
		String[] files = null;
		try{
			File fFile = new File(sRoot);
			files = fFile.list(new NonDirectoryFilter());
			Arrays.sort(files);
		} catch (Exception e){
			logger.error("EXCEPCIO obtenint els fitxers de '"+sRoot+"' --> "+e);
		}
		return files;
		
	}
	
	public static String[] getNotNullDirectories(String sRoot){
		String[] files = null;
		try{
			File fFile = new File(sRoot);
			files = fFile.list(new DirectoryNotNullFilter());
			Arrays.sort(files);
		} catch (Exception e){
			logger.error("EXCEPCIO obtenint els directoris de '"+sRoot+"' --> "+e);
		}
		return files;
		
	}
	
	public static boolean createDirectory(String sFile){
		boolean bOk = false;
		if (sFile!=null && sFile.trim().length()>0){
			bOk = createDirectory(new File(sFile));
		}
		return bOk;
	}
	
	/**
	 * 
	 * @param fFile
	 * @return true si es crea correctament el directori; false en cas contrari 
	 */
	public static boolean createDirectory(File fFile){
		boolean bOk = true;
		try{
			if (fFile!=null){
				bOk = fFile.mkdirs();
			}	
		} catch (Exception e){
			logger.error("EXCEPCIO creant el directori '"+fFile+"'");
			bOk = false;
		}
		return bOk;
	}
	
	/**
	 * 
	 * @param fFile
	 * @return true si s'esborra correctament el directori (i el seu contingut); false en cas contrari
	 */
	public static boolean delDirectory(File fFile){
		boolean bOk = false;
		try{
			if (fFile!=null){
				// Eliminar el contingut del directori
				File[] files = fFile.listFiles();
				if (files!=null){
					for (int i=0;i<files.length;i++){
						File tmpFile = files[i];
						if (tmpFile.isDirectory()){
							delDirectory(tmpFile);
						} else{
							tmpFile.delete();
						}
					}
				}
				// Eliminar el directori
				bOk = fFile.delete();
			}	
		} catch (Exception e){
			logger.error("EXCEPCIO eliminant el directori '"+fFile+"'");
		}
		return bOk;
		
	}

	public static String[] getImageFiles(String sRoot){
		String[] files = null;
		try{
			File fFile = new File(sRoot);
			files = fFile.list(new ImageFilter());
			Arrays.sort(files);
		} catch (Exception e){
			logger.error("EXCEPCIO obtenint les imatges de '"+sRoot+"' --> "+e);
		}
		return files;			
	}
    
	public static String[] getFlashFiles(String sRoot){
		String[] files = null;
		try{
			File fFile = new File(sRoot);
			files = fFile.list(new FlashFilter());
			Arrays.sort(files);
		} catch (Exception e){
			logger.error("EXCEPCIO obtenint les pel.licules flash de '"+sRoot+"' --> "+e);
		}
		return files;			
	}
    
	public static String[] getAudioFiles(String sRoot){
		String[] files = null;
		try{
			File fFile = new File(sRoot);
			files = fFile.list(new AudioFilter());
			Arrays.sort(files);
		} catch (Exception e){
			logger.error("EXCEPCIO obtenint els fitxers d'audio de '"+sRoot+"' --> "+e);
		}
		return files;			
	}
    
	public static String[] getVideoFiles(String sRoot){
		String[] files = null;
		try{
			File fFile = new File(sRoot);
			files = fFile.list(new VideoFilter());
			Arrays.sort(files);
		} catch (Exception e){
			logger.error("EXCEPCIO obtenint els fitxers de video de '"+sRoot+"' --> "+e);
		}
		return files;			
	}
    
	public static String[] getApplicationFiles(String sRoot){
		String[] files = null;
		try{
			File fFile = new File(sRoot);
			files = fFile.list(new ApplicationFilter());
			Arrays.sort(files);
		} catch (Exception e){
			logger.error("EXCEPCIO obtenint els fitxers d'aplicacions de '"+sRoot+"' --> "+e);
		}
		return files;			
	}
	
	public static String[] getJClicFiles(String sRoot){
		String[] files = null;
		try{
			File fFile = new File(sRoot);
			files = fFile.list(new JClicFilter());
			Arrays.sort(files);
		} catch (Exception e){
			logger.error("EXCEPCIO obtenint els fitxers de jclic de '"+sRoot+"' --> "+e);
		}
		return files;			
	}
    
	public static String[] getOtherFiles(String sRoot){
		String[] files = null;
		try{
			File fFile = new File(sRoot);
			files = fFile.list(new OtherFilter());
			Arrays.sort(files);
		} catch (Exception e){
			logger.error("EXCEPCIO obtenint els fitxers que no són multimedia ni d'aplicacions de '"+sRoot+"' --> "+e);
		}
		return files;			
	}
    
	public static String[] getMultimediaFiles(String sRoot){
		String[] files = null;
		try{
			String[] images = getImageFiles(sRoot);
			String[] sounds = getAudioFiles(sRoot);
			int iLength = (images!=null?images.length:0)+(sounds!=null?sounds.length:0);
			int iIndex = 0;
			files = new String[iLength];
			if (images!=null){
				System.arraycopy(images, 0, files, iIndex, images.length);
				iIndex = images.length;
			}
			if (sounds!=null){
				System.arraycopy(sounds, 0, files, iIndex, sounds.length);
				iIndex += sounds.length;
			}
			Arrays.sort(files);
		} catch (Exception e){
			logger.error("EXCEPCIO obtenint els fitxers multimedia de '"+sRoot+"' --> "+e);
		}
		return files;			
	}
    
	public static boolean createFile(String sFile){
		boolean bOk = false;
		if (sFile!=null && sFile.trim().length()>0){
			bOk = createFile(new File(sFile));
		}
		return bOk;
	}
	
	/**
	 * Crea el fitxer especificat
	 * @param fFile
	 * @return true si el fitxer es crea correctament; false en cas contrari
	 */
	public static boolean createFile(File fFile){
		boolean bOk = false;
		try{
			if (fFile!=null){
				bOk = fFile.createNewFile();
			}	
		} catch (Exception e){
			logger.error("EXCEPCIO creant el fitxer '"+fFile+"'");
			bOk = false;
		}
		return bOk;
	}
	
	public static boolean delFile(String sFile){
		boolean bOk=false;
		if (sFile!=null && sFile.trim().length()>0){
			bOk = delFile(new File(sFile));
		}
		return bOk;
	}
	
	public static boolean delFile(String sPath, String sFile){
		boolean bOk=false;
		if (sPath!=null && sPath.trim().length()>0 && sFile!=null && sFile.trim().length()>0){
			bOk = delFile(new File(sPath, sFile));
		}
		return bOk;
	}
	
	/**
	 * 
	 * @param fFile
	 * @return true si s'esborra correctament el fitxer; false en cas contrari
	 */
	public static boolean delFile(File fFile){
		boolean bOk = false;
		try{
			if (fFile!=null){
				bOk = fFile.delete();
			}	
		} catch (Exception e){
			logger.error("EXCEPCIO eliminant el fitxer '"+fFile+"'");
		}
		return bOk;
	}
    
	/**
	 * Puja un fitxer al directori sDirectoriRemot, sempre i quan aquest no superi lMaxSize
	 * @param request peticio on buscar el fitxer a pujar al directori remot
	 * @param sDirectoriRemot directori remot on es guardarà el fitxer indicat
	 * @param lMaxSize tamany maxim que pot ocupar el fitxer a pujar
	 * @return true si es fa l'upload correctament; false en cas contrari
	 */
	public static String writeUpload(HttpServletRequest request, String sDirectoriRemot, String sRepositoryPath, long lMaxSize){
		String sErrorUpload = null;
		try{
			if (sDirectoriRemot!=null){
				DiskFileUpload uBase = new DiskFileUpload();
				uBase.setSizeMax(lMaxSize);
				//uBase.setSizeThreshold(10000);
				if (sRepositoryPath==null){
					logger.error("Falta definir 'store.repositoryPath' al fitxer de properties");
				}
				uBase.setRepositoryPath(sRepositoryPath);
				if (DiskFileUpload.isMultipartContent(request)){
					Iterator itItems = uBase.parseRequest(request).iterator();
					while (itItems.hasNext()){
						FileItem tmpFileItem = (FileItem)itItems.next();
						if (!tmpFileItem.isFormField()){
							//String sNomFitxer = new File(tmpFileItem.getName()).getName();
							if (tmpFileItem.getSize()>0){
								String sNomFitxer = getFileName(tmpFileItem.getName());
								File fFile = new File(sDirectoriRemot+File.separator+sNomFitxer);
								if (!fFile.exists()){
									if (isImage(tmpFileItem.getName())){
										BufferedImage bimg = null;
										try {
											bimg = ImageIO.read(tmpFileItem.getInputStream());
											int maxwidth = Integer.parseInt(settings.getProperty("img.maxwidth"));
											if (bimg.getWidth()>maxwidth){
												// La imatge és massa gran, l'hem de redimensionar
												int newwidth = maxwidth;
												int newheight = maxwidth*bimg.getHeight()/bimg.getWidth();
												String extension = tmpFileItem.getName().substring(tmpFileItem.getName().lastIndexOf('.')+1, tmpFileItem.getName().length());
											    Image img = bimg.getScaledInstance(newwidth, newheight, Image.SCALE_DEFAULT);
											    BufferedImage newimg = new BufferedImage(newwidth, newheight, BufferedImage.TYPE_INT_RGB);
											    Graphics2D g2D = newimg.createGraphics();
										        g2D.drawImage(img, null, null);
											    ImageIO.write(newimg, extension, fFile);
												logger.info("UPLOAD i RESIZE de la imatge '"+fFile+"' realitzat amb exit");												
											}
											else {
												// No hem de modificar la imatge
												tmpFileItem.write(fFile);
												logger.info("UPLOAD de la imatge '"+fFile+"' realitzat amb exit");												
											}
										} catch (IOException e) {
											logger.error("EXCEPCIO redimensionant la imatge --> "+e);
											sErrorUpload = e.getMessage();
										}
									}
									else {
										tmpFileItem.write(fFile);
										logger.info("UPLOAD del fitxer '"+fFile+"' realitzat amb exit");	
									}
								}else{
									sErrorUpload = "msg.upload.file_already_exists";
								}
							}else{
								sErrorUpload = "msg.upload.file_not_exists";							
							}
						}
					}
				} else{
					logger.error("Per fer l'upload el request ha de ser multipart");
					sErrorUpload = "Per fer l'upload el request ha de ser multipart";
				}
			} else {
				logger.error("Per fer l'upload el directori remot no pot ser null");
				sErrorUpload = "Per fer l'upload el directori remot no pot ser null";
			}
		}catch(SizeLimitExceededException e){
			logger.info("Tamany excedit: No s'ha pogut fer l'upload al directori '"+sDirectoriRemot+"' perque supera l'espai que queda lliure ("+lMaxSize+")");
			sErrorUpload = "msg.upload.size_limit_exceeded";
		}catch (Exception e){
			logger.error("EXCEPCIO fent l'upload --> "+e);
			sErrorUpload = e.getMessage();
		}
		return sErrorUpload;
	}
	
	public static boolean write(File f, FileItem fi){
		boolean bOk = true;
		try{
			BufferedOutputStream dest = null;
			byte data[] = new byte[BUFFER];
			int count;
			InputStream is = fi.getInputStream();
			FileOutputStream fos = new FileOutputStream(f);
			dest = new BufferedOutputStream(fos, BUFFER);
			while ((count = is.read(data, 0, BUFFER)) != -1) {
				dest.write(data, 0, count);
			}
			dest.flush();
			dest.close();
		}catch (Exception e){
			e.printStackTrace();
			bOk = false;
		}
		return bOk;
	}
	
	public static String getFileName(String sPath){
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
			logger.debug("getFileName-> filename="+sFilename);
			if (sFilename!=null){
				sFilename = StringUtil.filter(sFilename, false);
				logger.debug("getFileName-> filter="+sFilename);
				sFilename = StringUtil.replace(sFilename, "-","_");
				logger.debug("getFileName-> guio="+sFilename);
				sFilename = StringUtil.replace(sFilename, "?", "");
				logger.debug("getFileName-> interrogant="+sFilename);
				//sFilename = StringUtil.filterFileName(sFilename);
				//logger.debug("filter="+sFilename);
				//sFilename = sFilename.replace('-','_');
			}
		}
		if (sFilename!=null) sFilename=sFilename.toLowerCase();
		logger.debug("getFileName-> lowercase="+sFilename);
		//logger.debug("getFileName()-> "+sFilename);
		return sFilename;
	}
	
	public static boolean isImage(String sFile){
		return isFileExtension(sFile, "file.image");
	}
	
	public static boolean isFlash(String sFile){
		return isFileExtension(sFile, "file.flash");
	}
	
	protected static boolean isFileExtension(String sFile, String sProperty){
		boolean bIsFile=false;
		if (sFile!=null){
			String tmpFile = sFile.toLowerCase();
			StringTokenizer stFileExt = new StringTokenizer(settings.getProperty(sProperty), ",");
			while (stFileExt.hasMoreTokens() && !bIsFile){
				String sFileExtension = stFileExt.nextToken();
				bIsFile = tmpFile.endsWith("."+sFileExtension);
			}
		}
		return bIsFile;
	}
	public static String getImageType(String sFile){
		String sType = "image/jpeg";
		if (sFile!=null){
			String tmpFile = sFile.toLowerCase();
			if (tmpFile.endsWith(".gif")){
				sType = "image/gif";
			}else if (tmpFile.endsWith(".png")){
				sType = "image/png";
			}else if (tmpFile.endsWith(".tiff")){
				sType = "image/tiff";
			}else if (tmpFile.endsWith(".swf")){
				sType = "application/x-shockwave-flash";
			}
		}
		return sType;  
	}
	
	public static String getApplicationType(String sFile){
		String sType = "";
		if (sFile!=null){
			String tmpFile = sFile.toLowerCase();
			if (tmpFile.endsWith(".odt")){
				sType = "application/vnd.oasis.opendocument.text";
			}else if (tmpFile.endsWith(".doc") || tmpFile.endsWith(".docx")){
				sType = "application/msword";
			}else if (tmpFile.endsWith(".rtf")){
				sType = "application/rtf";
			}else if (tmpFile.endsWith(".odp")){
				sType = "application/vnd.oasis.opendocument.presentation";
			}else if (tmpFile.endsWith(".pps")){
				sType = "application/vnd.ms-powerpoint";
			}else if (tmpFile.endsWith(".ppt")){
				sType = "application/vnd.ms-powerpoint";
			}else if (tmpFile.endsWith(".pptx")){
				sType = "application/vnd.ms-powerpoint";
			}else if (tmpFile.endsWith(".ods")){
				sType = "application/vnd.oasis.opendocument.spreadsheet";
			}else if (tmpFile.endsWith(".xls")){
				sType = "application/vnd.ms-excel";
			}else if (tmpFile.endsWith(".xlsx")){
				sType = "application/vnd.ms-excel";
			}else if (tmpFile.endsWith(".pdf")){
				sType = "application/pdf";
			}
		}
		return sType;  
	}
	
	public static boolean isAudio(String sFile){
		return isFileExtension(sFile, "file.audio");
	}
	
	public static boolean isVideo(String sFile){
		return isFileExtension(sFile, "file.video");
	}
	
	public static boolean isJClic(String sFile){
		return isFileExtension(sFile, "file.jclic");
	}
	
	public static boolean isApplication(String sFile){
		return isFileExtension(sFile, "file.application");
	}
	
	public static boolean isXML(String sFile){
		return sFile!=null && sFile.endsWith(".xml");
	}
	
	
	public static void unzip(String sPath, String sFile){
		try {
			String sZipName=sPath+File.separator+sFile+".zip";
			String sZipDir=sPath+File.separator+sFile;
			logger.debug("zipfile-> "+sZipName);
			createDirectory(sZipDir);
			BufferedOutputStream dest = null;
			BufferedInputStream is = null;
			ZipEntry entry;
			ZipFile zipfile = new ZipFile(sZipName);
			Enumeration e = zipfile.entries();
			while(e.hasMoreElements()) {
				entry = (ZipEntry) e.nextElement();
				logger.info("Extracting: " +entry);
				is = new BufferedInputStream(zipfile.getInputStream(entry));
				int count;
				byte data[] = new byte[BUFFER];
				FileOutputStream fos = new FileOutputStream(sZipDir+File.separator+entry.getName());
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
				is.close();
			}
			logger.info("Unzipped '"+sZipName+"' file to '"+sZipDir+"'");
		} catch(Exception e) {
			logger.error("EXCEPTION unziping file '"+sFile+"' -> "+e);
			//e.printStackTrace();
		}
	}

	
	public static void zip(String sPath, String sFile) {
		try {
			String sZipName=sPath+File.separator+sFile+".zip";
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(sZipName);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
			//out.setMethod(ZipOutputStream.DEFLATED);
			byte data[] = new byte[BUFFER];
			// get a list of files from current directory
			String sSrc = sPath+File.separator+sFile;
			File f = new File(sSrc);
			String files[] = f.list();

			for (int i=0; i<files.length; i++) {
				logger.info("Adding: " +files[i]);
				FileInputStream fi = new FileInputStream(sSrc+File.separator+files[i]);
				origin = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(files[i]);
				out.putNextEntry(entry);
				int count;
				while((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
			}
			logger.info("Created '"+sZipName+"' file");
			out.close();
		} catch(Exception e) {
			logger.error("EXCEPTION zipping file '"+sPath+File.separator+sFile+"' -> "+e);
			//e.printStackTrace();
		}
	}
	
}

/**
 * @author sarjona
 */
class DirectoryFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File fDir, String sName) {
		boolean bAccept = false;
		if (fDir!=null){
			File fFile = new File(fDir, sName);
			bAccept = fFile!=null && fFile.exists() && fFile.isDirectory();
		}
		return bAccept;
	}

}

/**
 * @author sarjona
 */
class DirectoryNotNullFilter extends DirectoryFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File fDir, String sName) {
		boolean bAccept = false;
		if (fDir!=null){
			File fFile = new File(fDir, sName);
			bAccept = super.accept(fDir, sName) && fFile.list()!=null && fFile.list().length>0;
		}
		return bAccept;
	}

}

/**
 * @author sarjona
 */
class NonDirectoryFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File fDir, String sName) {
		boolean bAccept = false;
		if (fDir!=null){
			File fFile = new File(fDir, sName);
			bAccept = fFile!=null && fFile.exists() && !fFile.isDirectory();
		}
		return bAccept;
	}

}

/**
 * @author sarjona
 */
class ImageFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File fDir, String sName) {
		boolean bAccept = false;
		if (fDir!=null){
			bAccept = FileUtil.isImage(sName);
		}
		return bAccept;
	}
}

/**
 * @author sarjona
 */
class FlashFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File fDir, String sName) {
		boolean bAccept = false;
		if (fDir!=null){
			bAccept = FileUtil.isFlash(sName);
		}
		return bAccept;
	}
}

/**
 * @author sarjona
 */
class AudioFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File fDir, String sName) {
		boolean bAccept = false;
		if (fDir!=null){
			bAccept = FileUtil.isAudio(sName);
		}
		return bAccept;
	}
}

/**
 * @author sarjona
 */
class VideoFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File fDir, String sName) {
		boolean bAccept = false;
		if (fDir!=null){
			bAccept = FileUtil.isVideo(sName);
		}
		return bAccept;
	}
}

/**
 * @author sarjona
 */
class JClicFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File fDir, String sName) {
		boolean bAccept = false;
		if (fDir!=null){
			bAccept = FileUtil.isJClic(sName);
		}
		return bAccept;
	}
}

/**
 * @author fbassas
 */
class ApplicationFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File fDir, String sName) {
		boolean bAccept = false;
		if (fDir!=null){
			bAccept = FileUtil.isApplication(sName);
		}
		return bAccept;
	}
}

/**
 * @author sarjona
 */
class OtherFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File fDir, String sName) {
		boolean bAccept = false;
		if (fDir!=null){
			File fFile = new File(fDir, sName);
			bAccept = !FileUtil.isXML(sName) && !FileUtil.isImage(sName) && !FileUtil.isFlash(sName) && !FileUtil.isAudio(sName) && !FileUtil.isVideo(sName) && !FileUtil.isApplication(sName) && !fFile.isDirectory();
		}
		return bAccept;
	}
}