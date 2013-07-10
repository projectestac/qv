package edu.xtec.qv.installer;

import java.io.*;
import java.util.zip.*;
import java.util.ArrayList;
import java.util.HashMap;
import edu.xtec.qv.player.CorrectQVApplet;
import edu.xtec.qv.lms.util.thread.*;
import javax.swing.*;

public class QVExporter{
	
	public static String exportAll(CorrectQVApplet main){
		final CorrectQVApplet main2 = main;
		InvokerAction exportAction = new InvokerAction(){
			public Object runAction(){
				String result = "";
				try{
					System.out.println("showFileChooser");
					JFileChooser chooser = new JFileChooser();
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					chooser.setDialogTitle("Tria on vols desar les proves i els resultats");
					boolean end = false;
					while (!end){
						int returnVal = chooser.showSaveDialog(main2);
						if(returnVal == JFileChooser.APPROVE_OPTION) {
							java.io.File fSelectedFile = chooser.getSelectedFile();
							boolean canWrite = fSelectedFile.getParentFile().canWrite();
							if (!canWrite){
								JOptionPane.showMessageDialog(main2, "No es t� acc�s per escriure en aquesta carpeta. Tria'n una altre.", "Tria on vols desar les proves i els resultats", JOptionPane.ERROR_MESSAGE); 
							} else {
								if (!fSelectedFile.getName().toLowerCase().endsWith(".zip"))
									fSelectedFile = new File(fSelectedFile.getAbsolutePath()+".zip");
								boolean bContinue = true;
								if (fSelectedFile.exists()){
									int result2 = JOptionPane.showConfirmDialog(null, 
											"El fitxer "+fSelectedFile.getName()+" ja existeix. El voleu sobrescriure?", "Fitxer existent", JOptionPane.YES_NO_OPTION);
									if (result2==JOptionPane.YES_OPTION)
										bContinue = true;
									else
										bContinue = false;
								}
								if (bContinue){
									try{
										File fProvesDir = new File(main2.getDocumentBase().getFile()).getParentFile().getParentFile();
										fProvesDir = new File(removeSpace(fProvesDir.getAbsolutePath()));
										fProvesDir = new File(fProvesDir, "proves");
										
										result = exportToFile(fProvesDir, fSelectedFile);
										if (result.equalsIgnoreCase("OK"))
											JOptionPane.showMessageDialog(main2, "Proves desades al fitxer: "+fSelectedFile.getAbsolutePath(), "Tria on vols desar les proves i els resultats", JOptionPane.INFORMATION_MESSAGE);
										else 
											JOptionPane.showMessageDialog(main2, "Error desant les proves: "+result, "Error desant les proves", JOptionPane.ERROR_MESSAGE);
										end = true;
									} catch (Exception ex){
										ex.printStackTrace(System.err);
										JOptionPane.showMessageDialog(main2, "No s'ha pogut escriure en aquest fitxer:"+fSelectedFile.getAbsolutePath()+". Tria'n una altre.", "Tria on vols desar les proves i els resultats", JOptionPane.ERROR_MESSAGE);
									}
								}
							}
						} else {
							end = true;
							result = "CANCEL";
						}
					}
				} catch (Exception ex){
					ex.printStackTrace(System.err);
					result = ex.toString();
				}
				return result;
			}
		};
		InvokerThreadWaiter itw = new InvokerThreadWaiter(main.getInvokerThread(), exportAction);
		itw.start();
		return (String)exportAction.getResult();
	}
	
	protected static String removeSpace(String sFile){
		if (sFile.indexOf("%20")>=0) {
			sFile = sFile.replaceAll("%20"," ");
		}	
		return sFile;
	}
	
	private static String exportToFile(File fDataDir, File fDest){
		String result = "";
		try{
            FileOutputStream fos = new FileOutputStream(fDest);
            ZipOutputStream zos = new ZipOutputStream(fos);
            
            String filePath = fDataDir.getParentFile().getAbsolutePath()+File.separator;
            result = addFileToZip(fDataDir.getAbsolutePath(), zos, filePath);
            
            zos.flush();
            zos.close();
            fos.close();

			if (result.length()<1)
				result = "OK";	
		} catch (Exception ex){
			ex.printStackTrace(System.err);
			result = ex.toString();
		}
		return result;
	}


    private static String addFileToZip(String filePath, ZipOutputStream zos, String prefixPath) {
    	String result = "";
        try {
        	File f = new File(filePath);
        	if (f.isDirectory()){
        		File[] files = f.listFiles();
        		for (int i=0;files!=null && i<files.length;i++){
        			result = addFileToZip(files[i].getAbsolutePath(), zos, prefixPath);
        		}
        	} else {
        		FileInputStream fis = new FileInputStream(filePath);
	            BufferedInputStream bis = new BufferedInputStream(fis);
	            
	            ZipEntry fileEntry;
	            fileEntry=new ZipEntry(filePath.substring(prefixPath.length(),filePath.length()));
	            zos.putNextEntry(fileEntry);
	            
	            int nBytes = bis.available();
	            byte[] data = new byte[nBytes];
	            bis.read(data, 0, nBytes);
	            zos.write(data, 0, nBytes);
	            
	            bis.close();
	            fis.close();
        	}
        }
        catch(Exception ex) {
        	ex.printStackTrace(System.err);
			result = ex.toString();
		}
		return result;
    }
/*	
	String selectedDir = "";
	String assessmentPath = "";
	String installResult = "";
	String installCurrentMessage = "";
	
	public boolean doShowFileChooser = false;
	public boolean selectFolder = true;
	public boolean doInstall = false;
	public boolean doInstallAssessment = false;
	private String installDir = "";
	private String urlInstalledDir = "";
	
	
	public QVInstaller(){
	}
	
	public void start(){
		System.out.println("start QVInstaller ****");
		ExecutorThread et = new ExecutorThread();
		et.start();
	}
	
	private String showFileChooser(){
		try{
			boolean end = false;
			while (!end){
				System.out.println("showFileChooser");
				JFileChooser chooser = new JFileChooser();
				//chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
				javax.swing.filechooser.FileFilter filter = new javax.swing.filechooser.FileFilter(){
					public boolean accept(File f){
						if (f.isDirectory() || (f!=null && f.getName().endsWith(".zip")))
							return true;
						else
							return false;
					}
					public String getDescription(){
						return "Fitxers zip de la prova";
					}
				};
				
				chooser.setFileFilter(filter);
				chooser.setDialogTitle("Tria el fitxer zip que cont� la prova.");
				int returnVal = chooser.showOpenDialog(this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					java.io.File fSelectedDir = chooser.getSelectedFile();
					selectedDir = fSelectedDir.getAbsolutePath();
					if (selectedDir==null || !selectedDir.endsWith(".zip")){
						JOptionPane.showMessageDialog(this, "Cal seleccionar un fitxer zip.", "Tria el fitxer zip que cont� la prova.", JOptionPane.ERROR_MESSAGE); 
					} else {
						end = true;
					}
				} else
					end = true;
				System.out.println("zip=" +selectedDir);
			}
	    } catch (Exception ex){
	    	ex.printStackTrace(System.err);
	    }
		return selectedDir;
	}
	
	private String showFolderChooser(){
		try{
			boolean end = false;
			while (!end){
				System.out.println("showFolderChooser");
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setDialogTitle("Tria la carpeta d'instal�laci�");
				int returnVal = chooser.showOpenDialog(this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					java.io.File fSelectedDir = chooser.getSelectedFile();
					boolean canWrite = canWriteInFolder(fSelectedDir.getAbsolutePath());//fSelectedDir.canWrite();
					if (!canWrite){
						JOptionPane.showMessageDialog(this, "No es t� acc�s per escriure en aquesta carpeta. Tria'n una altre.", "Tria la carpeta d'instal�laci�", JOptionPane.ERROR_MESSAGE); 
					} else {
						end = true;
						selectedDir = fSelectedDir.getAbsolutePath();
					}
				} else
					end = true;
				System.out.println("dir=" +selectedDir);
			}
	    } catch (Exception ex){
	    	ex.printStackTrace(System.err);
	    }
		return selectedDir;
	}
	
	private String doInstall(String filePath){
		urlInstalledDir = asyncGetUrlInstalledDir();
		installResult = "";
		try{
			if (filePath==null || filePath.trim().length()<1)
				installResult = "Cal establir un directori.";
			else if (!existFolder(filePath))
				installResult = "La carpeta seleccionada no existeix.";
			else if (!isFolder(filePath))
				installResult = "El fitxer seleccionat no �s una carpeta.";
			else if (!canWriteInFolder(filePath))
				installResult = "No es pot escriure a la carpeta seleccionada.";
			else {
				boolean existInstall = existInstallInFolder(filePath);
				if (existInstall){
					int result = JOptionPane.showConfirmDialog(null, 
						"Ja existeix una instal�laci� en aquesta carpeta. Esteu segur que voleu sobrescriure-la?", "Instal�laci� existent", JOptionPane.YES_NO_OPTION);
					if (result==JOptionPane.YES_OPTION)
						existInstall = false;
				}
				if (!existInstall){
					installCurrentMessage = "Instal�lant...";
					installResult = install(filePath);
					if (installResult.trim().length()<1)
						installResult = "ok";
				} else {
					installResult = "Ja existeix una instal�laci� en aquesta carpeta.";
				}
			}
		} catch (Exception ex){
			installResult = "Error instal�lant la prova: "+ex;
			ex.printStackTrace(System.err);
		}
		System.out.println("installResult:"+installResult);
		installCurrentMessage = installResult;
		return installResult;
	}
	
	private String doInstallAssessment(String assessmentPath){
		urlInstalledDir = asyncGetUrlInstalledDir();
		installResult = "";
		//System.out.println("asyncGetUrlInstalledDir="+urlInstalledDir);
		try{
			File fDest = new File(getDocumentBase().getFile()).getParentFile().getParentFile();
			fDest = new File(removeSpace(fDest.getAbsolutePath()));
			fDest = new File(fDest, "proves");
			try{
				if (!fDest.exists())
					fDest.mkdirs();
			} catch (Exception ex){
				ex.printStackTrace(System.err);
			}
			System.out.println("assessmentPath="+assessmentPath+" fDest:"+fDest.getAbsolutePath());
			
			if (assessmentPath==null || assessmentPath.trim().length()<1)
				installResult = "Cal establir la prova a instal�lar.";
			else if (!isZipFile(assessmentPath))
				installResult = "El fitxer seleccionat no �s un zip.";
			else if (!canWriteInFolder(fDest.getAbsolutePath()))
				installResult = "No es pot escriure a la carpeta "+fDest.getAbsolutePath()+".";
			else {
				installCurrentMessage = "Instal�lant prova...";
				installResult = installAssessment(assessmentPath, fDest);
				if (installResult.trim().length()<1)
					installResult = "ok";
			}
		} catch (Exception ex){
			installResult = "Error instal�lant la prova: "+ex;
			ex.printStackTrace(System.err);
		}
		System.out.println("installResult:"+installResult);
		installCurrentMessage = installResult;
		return installResult;
	}

	private String install(String destFolderPath){
		System.out.println("install!!!!!!");
		destFolderPath = removeSpace(destFolderPath);
		File fSource = new File(getDocumentBase().getFile()).getParentFile();
		File fDest = new java.io.File(destFolderPath, fSource.getName());
		String result = copyDir(fSource, fDest);
		if (result.trim().length()<1){
			result = copyFile(new File(fSource, "index.tmp"), new File(fDest.getParentFile(),"index.html"));
		}
		return result;
	}
	
	private String installAssessment(String assessmentPath, File fDest){
		System.out.println("installAssessment assessmentPath="+assessmentPath+" fDest:"+fDest.getAbsolutePath());
		File fAssessment = new File(assessmentPath);
		String result = unzip(fAssessment, fDest);
		if (result.trim().length()<1){
			try{
				String folderName = fAssessment.getName();
				int i = folderName.indexOf(".");
				if (i>0){
					folderName = folderName.substring(0, i);
				}
				result = copyFile(new File(fDest.getParentFile()+File.separator+"files"+File.separator+"start.tmp"), 
						new File(fDest+File.separator+folderName+File.separator+"html"+File.separator+"start.html"), false);
				if (result.trim().length()<1){
					result = copyFile(new File(fDest.getParentFile()+File.separator+"files"+File.separator+"summary.tmp"), 
						new File(fDest+File.separator+folderName+File.separator+"html"+File.separator+"summary.html"), false);
				}
			} catch (Exception ex){ //Si instal�lem proves exportades, ja tenen els fitxers copiats i renombrats
				result = "";
			}
		}
		return result;
	}

    private String unzip(File assessmentFile, File destDir) {
    	System.out.println("unzip");
    	String result = "";
        if (!destDir.exists() || !destDir.isDirectory()){
        	destDir.mkdirs();
        }
        
        boolean existInstall = false; 
        String nomCarpetaPral = assessmentFile.getName();
        if (nomCarpetaPral.indexOf('.')>0)
        	nomCarpetaPral = nomCarpetaPral.substring(0,nomCarpetaPral.indexOf('.'));
        File f = new File(destDir, nomCarpetaPral);
        System.out.println("Carpeta principal de la prova:"+f.getAbsolutePath());
        if (f.exists()){
        	existInstall = true;
        	int resultOp = JOptionPane.showConfirmDialog(null, 
					"Aquesta prova ja est� instal�lada. Voleu sobrescriure-la?", "Instal�laci� existent", JOptionPane.YES_NO_OPTION);
				if (resultOp==JOptionPane.YES_OPTION)
					existInstall = false;
        }
        boolean existResults = false;
        if (!existInstall){
	        //java.util.ArrayList al=new java.util.ArrayList(1);
	        if (assessmentFile!=null && assessmentFile.exists()){
	        	//System.out.println("existeix el fitxer "+assessmentFile);
	            try {
	                FileInputStream fis = new FileInputStream(assessmentFile);
	                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
	                ZipEntry entry = null;
	                final int BUFFER = 2048;
	                java.io.BufferedOutputStream dest = null;
	                while((entry = zis.getNextEntry()) != null) {
	                	String entryName = entry.getName();
	                	boolean isResultFile = false;
	                	String s = entryName.replace('\\','/');
	                	if (s.startsWith("proves/")){
	                		entryName = entryName.substring(7);
	                		s = s.substring(7);
	                	}
	                	if (s.indexOf("/data/")>=0){
	                		isResultFile = true;
	                		existResults = true;
	                	}
	                
	                	System.out.println("isResultFile "+entry.getName()+"? "+isResultFile);
	                	if (!isResultFile){
	                		int count;
		                    byte data[] = new byte[BUFFER];
		                    String fileName = destDir.getAbsolutePath()+java.io.File.separator+entryName;
		                    fileName = removeSpace(fileName);
		                    java.io.File fTemp=new java.io.File(fileName);
		                    createParents(fTemp);
		                    if (entry.isDirectory()){
		                    	installCurrentMessage = "Creant carpeta "+fTemp.getAbsolutePath();
		                    	System.out.println(installCurrentMessage);
		                    	fTemp.mkdir();
		                    } else {
		                    	FileOutputStream fos = new FileOutputStream(fTemp);
		                    	installCurrentMessage = "Copiant fitxer "+fTemp.getAbsolutePath();
		                    	System.out.println(installCurrentMessage);
		                    	dest = new java.io.BufferedOutputStream(fos, BUFFER);
		                    	while ((count = zis.read(data, 0, BUFFER)) != -1) {
		                    		dest.write(data, 0, count);
		                    	}
		                    	dest.flush();
		                    	dest.close();
		                    }
		                    //al.add(fTemp);
	                	}
	                }
	                zis.close();
	                fis.close();
	                
	                
	            }
	            catch(Exception e) {
	            	System.out.println("excepci�");
	                e.printStackTrace();
	            	//al=null;
	                result = "Error instal�lant la prova: "+e.toString();
	            }
	        } else {
	        	result = "Error instal�lant la prova. El fitxer "+assessmentFile+" no existeix.";
	        }
        } else {
        	result = "La prova ja estava instal�lada.";
        }
        if (result.length()<1){
        	unzipResults(assessmentFile, destDir); 
        }
        System.out.println("fi unzip");
        return result;
    }
    
    private String unzipResults(File assessmentFile, File destDir) {
    	System.out.println("unzipResults");
    	String result = "";

    	ArrayList alAssessmentResultsImport = new ArrayList();
    	ArrayList alAssessmentResultsNoImport = new ArrayList();
    	HashMap hmRenameAssessmentGroupResults = new HashMap();
    	
    	//java.util.ArrayList al=new java.util.ArrayList(1);
    	if (assessmentFile!=null && assessmentFile.exists()){
    		//System.out.println("existeix el fitxer "+assessmentFile);
    		try {
    			FileInputStream fis = new FileInputStream(assessmentFile);
    			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
    			ZipEntry entry = null;
    			final int BUFFER = 2048;
    			java.io.BufferedOutputStream dest = null;
    			while((entry = zis.getNextEntry()) != null) {
    				String entryName = entry.getName();
    				boolean isResultFile = false;
    				String s = entryName.replace('\\','/');
    				if (s.startsWith("proves/")){
    					entryName = entryName.substring(7);
    					s = s.substring(7);
    				}
    				String assessmentName = "";
    				String groupName = null;
    				int i = s.indexOf("/data/");
    				if (i>=0){
    					isResultFile = true;
    					assessmentName = s.substring(0,i);
    					if (!alAssessmentResultsImport.contains(assessmentName) &&
    						!alAssessmentResultsNoImport.contains(assessmentName)){
    						int resultOp = JOptionPane.showConfirmDialog(null, 
    								"Voleu importar els resultats de la prova "+assessmentName+"?", "Importaci� de resultats", JOptionPane.YES_NO_OPTION);
    							if (resultOp==JOptionPane.YES_OPTION)
    								alAssessmentResultsImport.add(assessmentName);
    							else
    								alAssessmentResultsNoImport.add(assessmentName);
    					}
    					
    					int j = s.indexOf('/',i+6);
    					if (j>0){
    						groupName = s.substring(i+6, j);
    					}
    				}

    				System.out.println("isResultFile "+entry.getName()+"? "+isResultFile);
    				if (isResultFile && alAssessmentResultsImport.contains(assessmentName)){
    					int count;
    					byte data[] = new byte[BUFFER];
    					String fileName = destDir.getAbsolutePath()+java.io.File.separator+entryName;
    					fileName = removeSpace(fileName);
    					java.io.File fTemp=new java.io.File(fileName);
    					createParents(fTemp);
    					if (entry.isDirectory()){
    						if (groupName!=null && !fTemp.exists()){
    							installCurrentMessage = "Creant carpeta "+fTemp.getAbsolutePath();
    							System.out.println(installCurrentMessage);
    							fTemp.mkdir();
    						} else if (groupName!=null && fTemp.exists()){
    							String newGroupName = groupName;
    							while (fTemp.exists()){
    								String newGroupNameAux = newGroupName+"2";
    								fileName = fileName.replaceAll(newGroupName, newGroupNameAux);
    								newGroupName = newGroupNameAux;
    								fTemp=new java.io.File(fileName);
    							}
    							
    							JOptionPane.showMessageDialog(null, 
    									"Ja hi ha dades importades del grup "+groupName+" de la prova "+assessmentName+". Es reanomena el grup per "+newGroupName, "Ja hi ha dades importades del grup", JOptionPane.INFORMATION_MESSAGE);
    							hmRenameAssessmentGroupResults.put(assessmentName+"_"+groupName, newGroupName);
    							installCurrentMessage = "Creant carpeta renombrada "+fTemp.getAbsolutePath();
    							System.out.println(installCurrentMessage);
    							fTemp.mkdir();
    						}
    					} else {
    						if (groupName!=null && hmRenameAssessmentGroupResults.containsKey(assessmentName+"_"+groupName)){
    							System.out.println("Fitxer "+fileName+" renombrat.");
    							String newGroupName = hmRenameAssessmentGroupResults.get(assessmentName+"_"+groupName).toString();
    							fileName = fileName.replaceAll(groupName, newGroupName);
    							fTemp=new java.io.File(fileName);
    						}
    						FileOutputStream fos = new FileOutputStream(fTemp);
    						installCurrentMessage = "Copiant fitxer "+fTemp.getAbsolutePath();
    						System.out.println(installCurrentMessage);
    						dest = new java.io.BufferedOutputStream(fos, BUFFER);
    						while ((count = zis.read(data, 0, BUFFER)) != -1) {
    							dest.write(data, 0, count);
    						}
    						dest.flush();
    						dest.close();
    					}
    					//al.add(fTemp);
    				}
    			}
    			zis.close();
    			fis.close();
    		}
    		catch(Exception e) {
    			System.out.println("excepci�");
    			e.printStackTrace();
    			//al=null;
    			result = "Error instal�lant la prova: "+e.toString();
    		}
    	} else {
    		result = "Error instal�lant la prova. El fitxer "+assessmentFile+" no existeix.";
    	}

    	System.out.println("fi unzip");
    	return result;
    }
    
    private static void createParents(java.io.File f){
        if (f!=null && f.getParentFile()!=null && !f.getParentFile().exists()){
            createParents(f.getParentFile());
            f.getParentFile().mkdir();
        }
    }
    
	private String copyDir(File srcPath, File dstPath){
		String result = "";
		try{
			if (srcPath.isDirectory()){
				installCurrentMessage = "Copiant directori "+srcPath.getAbsolutePath()+" a "+dstPath.getAbsolutePath();
				System.out.println(installCurrentMessage);
				if (!dstPath.exists()){
					dstPath.mkdir();
				}
				String files[] = srcPath.list();
				//A for(int i = 0; i < files.length; i++){
				for(int i = 0; result.trim().length()<1 && i < files.length; i++){
					//A copyDir(new File(srcPath, files[i]), new File(dstPath, files[i]));
					result = copyDir(new File(srcPath, files[i]), new File(dstPath, files[i]));//A
				}
			}
			else{
				result = copyFile(srcPath, dstPath);
			}
		} catch (Exception ex){
			ex.printStackTrace(System.err);
			result = "Error copiant l'arxiu "+dstPath.getAbsolutePath()+" ("+ex+")";
		}
		return result;
	}
	
	private String copyFile(File srcPath, File dstPath){
		return copyFile(srcPath, dstPath, true);
	}
	
	private String copyFile(File srcPath, File dstPath, boolean infoErrors){
		String result = "";
		try{
				installCurrentMessage = "Copiant fitxer "+srcPath.getAbsolutePath()+" a "+dstPath.getAbsolutePath(); 
				System.out.println(installCurrentMessage);
				InputStream in = new FileInputStream(srcPath);
				OutputStream out = new FileOutputStream(dstPath); 
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
		} catch (Exception ex){
			if (infoErrors){
				ex.printStackTrace(System.err);
				result = "Error copiant l'arxiu "+dstPath.getAbsolutePath()+" ("+ex+")";
			}
		}
		return result;
	}
	
	private boolean existFolder(String folderPath){
		boolean exists = false;
		try{
			java.io.File fSelectedDir = new java.io.File(folderPath);
			exists = fSelectedDir.exists();
		} catch (Exception ex){
			ex.printStackTrace(System.err);
		}
		return exists;
	}
	
	private boolean isZipFile(String filePath){
		boolean isZipFile = false;
		try{
			isZipFile = (filePath!=null && filePath.endsWith(".zip"));
		} catch (Exception ex){
			ex.printStackTrace(System.err);
		}
		return isZipFile;
	}
	
	private boolean isFolder(String folderPath){
		boolean isFolder = false;
		try{
			java.io.File fSelectedDir = new java.io.File(folderPath);
			isFolder = fSelectedDir.isDirectory();
		} catch (Exception ex){
			ex.printStackTrace(System.err);
		}
		return isFolder;
	}
	
	private boolean canWriteInFolder(String folderPath){
		boolean canWrite = false;
		try{
			folderPath = removeSpace(folderPath);
			java.io.File fSelectedDir = new java.io.File(folderPath);
			canWrite = fSelectedDir.canWrite();
		} catch (Exception ex){
			ex.printStackTrace(System.err);
		}
		return canWrite;
	}

	private boolean existInstallInFolder(String folderPath){
		boolean exist = false;
		try{
			folderPath = removeSpace(folderPath);
			java.io.File fSelectedDir = new java.io.File(folderPath);
			if (fSelectedDir.exists()){
				File f2= new File(fSelectedDir, "files");
				File f3= new File(fSelectedDir, "index.html");
				if (f2.exists() || f3.exists()){
					exist = true;
				}
			}
		} catch (Exception ex){
			ex.printStackTrace(System.err);
		}
		return exist;
	}

	public String getSelectedDir(){
		return selectedDir;
	}
	
	public String getUrlInstalledDir(){
		return urlInstalledDir;
	}
	
	public String asyncGetUrlInstalledDir(){
		String path = "";
		try{
			if (selectedDir!=null && selectedDir.length()>1){
				File fSource = new File(getDocumentBase().getFile()).getParentFile();
				//path = selectedDir+File.separator+fSource.getName()+File.separator;
				File f = new File(selectedDir+File.separator+fSource.getName()+File.separator);
				path = f.toURL().toString();
			}
		} catch (Exception ex){
			ex.printStackTrace(System.err);
		}
		return path;
	}
	
	public String getInstallResult(){
		return installResult;
	}
	
	public String getInstallCurrentMessage(){
		return installCurrentMessage;
	}
	
	public void setDoShowFileChooser(boolean b){
		doShowFileChooser = b;
		this.selectFolder = true;
	}
	
	public void setDoShowFileChooser(boolean b, boolean selectFolder){
		doShowFileChooser = b;
		this.selectFolder = selectFolder;
		//System.out.println("setDoShowFileChooser("+b+", "+selectFolder+")");
	}
	
	public boolean getDoShowFileChooser(){
		return doShowFileChooser;
	}

	public void setDoInstall(boolean b, String installDir){
		installResult = "";
		this.installDir = installDir;
		doInstall = b;
	}
	
	public void setDoInstallAssessment(boolean b, String assessmentPath){
		installResult = "";
		this.assessmentPath = assessmentPath;
		doInstallAssessment = b;
	}
	
	public boolean getDoInstall(){
		return doInstall;
	}
	
	public boolean getDoInstallAssessment(){
		return doInstallAssessment;
	}
	
	protected String removeSpace(String sFile){
		if (sFile.indexOf("%20")>=0) {
			sFile = sFile.replaceAll("%20"," ");
		}	
		return sFile;
	}

	public class ExecutorThread extends Thread{
		boolean end = false;

		ExecutorThread(){
		}

		public void run(){
			while(!end){
				try{
					if (getDoShowFileChooser()){
						setDoShowFileChooser(false, selectFolder);
						if (selectFolder)
							showFolderChooser();
						else
							showFileChooser();
						System.out.println("showFileChooser: fet");
					}
					if (getDoInstall()){
						setDoInstall(false, installDir);
						doInstall(installDir);
						System.out.println("doInstall: fet");
					}
					if (getDoInstallAssessment()){
						setDoInstallAssessment(false, assessmentPath);
						doInstallAssessment(assessmentPath);
						System.out.println("doInstallAssessment: fet");
					}
					sleep(1000);
				} catch (Exception ex){
					ex.printStackTrace(System.err);
				}
			}
		}

	}*/
}