package edu.xtec.qv.lms;

import edu.xtec.qv.lms.data.*;
import edu.xtec.qv.lms.util.thread.*;
import java.util.ArrayList;
import java.util.Properties;
import java.io.*;

public class LocalFileQVLMS extends QVLMSActionsAdapter{ 

	private String resultFilePath = null;
	private String configFilePath = null;
	private QVUserInfo userInfo;
	private static QV qv = null; //Propietats de la realització del quadern
	
	private QVAssignment qvAssignment = null;
	private ArrayList alQVSection = null;
	
	edu.xtec.qv.lms.util.thread.InvokerThread it = null;

	public LocalFileQVLMS(edu.xtec.qv.lms.util.thread.InvokerThread it, String configFilePath, String resultFilePath){
		this.it = it;//A5
		this.configFilePath = configFilePath;
		this.resultFilePath = resultFilePath;
	}
	
	public LocalFileQVLMS(edu.xtec.qv.lms.util.thread.InvokerThread it, QVUserInfo userInfo, String configFilePath, String resultFilePath){
		this.it = it;//A5
		this.userInfo = userInfo;
		this.configFilePath = configFilePath;
		this.resultFilePath = resultFilePath;
		processFile();
	}
	
	private void processFile(){
		System.out.println("processFile resultFilePath="+resultFilePath);
		//Inicialitzem les dades des del fitxer de resultats.
		if (qv==null)
			qv = getQV(-1);
		
		qvAssignment = new QVAssignment();
		
		try{
			boolean existFile = existFile(resultFilePath);
			alQVSection = new ArrayList();
			if (existFile){
				String report = loadFile(resultFilePath);
				edu.xtec.qv.lms.report.ReportParser.parseReport(report, userInfo, qvAssignment, alQVSection);
				System.out.println("EXISTEIX EL FITXER "+resultFilePath+" qv.getOrderSections()="+qv.getOrderSections()+" qvAssignment.getSectionOrder()="+qvAssignment.getSectionOrder());
			} else {
				System.out.println("NO EXISTEIX EL FITXER "+resultFilePath+" qv.getOrderSections()="+qv.getOrderSections());
				//Entra al quadern per primer cop
				if (qv.getOrderSections()==1)
					qvAssignment.setSectionOrder(((int)(Math.random()*80))+1);
				if (qv.getOrderItems()==1)
					qvAssignment.setItemOrder(((int)(Math.random()*80))+1);
				updateQVAssignment(qvAssignment);
				////saveFile(resultFilePath);
			}
		} catch (Exception ex){
			ex.printStackTrace(System.err);
		}
	}
	
	public QV getQV(int assignmentId){

		if (qv == null){
			//System.out.println("-getQV abans loadQVProperties");
			Properties prop = loadQVProperties();
			//System.out.println("-getQV després loadQVProperties");
			if (prop!=null){
				//Inicialitzo les propietats de la realització
				System.out.println("Inicialitzo les propietats de la realització des del fitxer de configuració");
				int id = getIntProperty(prop, "id", -1);
				int course = getIntProperty(prop, "course", 0);
				String name = getStringProperty(prop, "name", "");
				String description = getStringProperty(prop, "description", "");
				String assessmenturl = getStringProperty(prop, "assessmenturl", "");
				String skin = getStringProperty(prop, "skin", "");
				String assessmentlang = getStringProperty(prop, "assessmentlang", "ca");
				int maxdeliver = getIntProperty(prop, "maxdeliver", -1);
				int showcorrection = getIntProperty(prop, "showcorrection", 1);
				int showinteraction = getIntProperty(prop, "showinteraction", 1);
				int ordersections = getIntProperty(prop, "ordersections", 0);
				int orderitems = getIntProperty(prop, "orderitems", 0);
				String target = getStringProperty(prop, "target", "descself");
				float maxgrade = getFloatProperty(prop, "maxgrade", 0);
				String width = getStringProperty(prop, "width", "");
				String height = getStringProperty(prop, "height", "");
				qv = new QV(id, course, name, description, assessmenturl, skin, assessmentlang, maxdeliver, showcorrection, showinteraction, ordersections, orderitems, target, maxgrade, width, height);
				System.out.println("inicialitzades");
				
			} else { //Propietats per defecte
				System.out.println("Inicialitzo les propietats de la realització PER DEFECTE");
				qv = new QV();
				qv.setId(1);
				qv.setMaxDeliver(-1);
				qv.setShowCorrection(0);
			}
		}
		return qv;
	}
	
	public QVAssignment getQVAssignment(int assignmentId){
		return qvAssignment;
	}

	public ArrayList getQVSections(int assignmentId){
		saveFile(resultFilePath);////
		return alQVSection;
	}
	
	public QVSection getQVSection(int assignmentId, String sectionId){
		ArrayList alQVSection = getQVSections(assignmentId);
		QVSection qvSection = null;
		if (alQVSection!=null){
			boolean bFound = false;
			for (int i=0;!bFound && i<alQVSection.size();i++){
				QVSection current = (QVSection)alQVSection.get(i);
				if (sectionId.equalsIgnoreCase(current.getSectionId())){
					qvSection = current;
					bFound = true;
				}
			}
		}
		return qvSection;
	}
	
	public boolean insertQVSection(QVSection qvSection){
		alQVSection = getQVSections(qvSection.getAssignmentId());
		alQVSection.add(qvSection);
		return saveFile(resultFilePath);
	}

	public boolean existsQVAssignment(int assignmentId){
		//Sempre existeix
		return true;
	}
	
	public boolean updateQVAssignment(QVAssignment qvAssignment){
		//actualitzant l'entitat, ja s'actualitza
		return saveFile(resultFilePath);
	}
	
	public boolean updateQVSection(QVSection qvSection){
		//actualitzant l'entitat, ja s'actualitza
		return saveFile(resultFilePath);
	}

	
	//////////////////////////////////////////////////////////////////////////////////////////
	//////// Funcions d'utilitat d'accés als fitxers mitjançant Thread amb permisos //////////
	
	private boolean existFile(String filePath){
		final String file = filePath;
		InvokerAction existFileAction = new InvokerAction(){
			public Object runAction(){
				Boolean exists = new Boolean(false);
				try{
					File f = new File(file);
					exists = new Boolean(f.exists());
					//System.out.println("Existeix el fitxer?"+exists);
				} catch (Exception ex){
					ex.printStackTrace(System.err);
				}
				return exists;
			}
		};
		InvokerThreadWaiter itw = new InvokerThreadWaiter(it, existFileAction);
		itw.start();
		System.out.println("Existeix?"+existFileAction.getResult());
		return ((Boolean)existFileAction.getResult()).booleanValue();
	}
	
	private String loadFile(String filePath){
		final String file = filePath;
		InvokerAction loadAction = new InvokerAction(){
			public Object runAction(){
				//String error = null;
				String s = null;
				try{
					BufferedReader br = new BufferedReader(new java.io.FileReader(file));
					StringBuffer sb = new StringBuffer();
					String line = br.readLine();
					while (line!=null){
						sb.append(line);
						sb.append("\n");
						line = br.readLine();
					}
					s = sb.toString();
					br.close();
					//System.out.println("He acabat de llegir fitxer:"+file);
					//System.out.println("Contingut:"+s);
				} catch (Exception ex){
					ex.printStackTrace(System.err);
					s = "Error: "+ex.toString();
				}
				return s;
			}
		};
		
		InvokerThreadWaiter itw = new InvokerThreadWaiter(it, loadAction);
		itw.start();
		//System.out.println("he llegit:"+loadAction.getResult());
		return (String)loadAction.getResult();
	}
	
	//private String report = null;
	
	private boolean saveFile(String filePath){
		final String file = filePath;
		final String report = edu.xtec.qv.lms.report.ReportGenerator.getQTIResultReport(userInfo, qvAssignment, alQVSection);
		
		InvokerAction saveAction = new InvokerAction(){
			public Object runAction(){
				Boolean bOk = new Boolean(false);
				try{
					File f = new File (file);
					if (f.getParent()!=null && !f.getParentFile().exists())
						f.getParentFile().mkdirs();
					FileWriter fw = new FileWriter(file);
					fw.write(report);
					fw.close();
					bOk = new Boolean(true);
					//System.out.println("He acabat de desar");
				} catch (Exception ex){
					ex.printStackTrace(System.err);
				}
				return bOk;
			}
		};
		
		InvokerThreadWaiter itw = new InvokerThreadWaiter(it, saveAction);
		itw.start();
		System.out.println("report:"+report);
		return ((Boolean)saveAction.getResult()).booleanValue();
	}
	
	private Properties loadQVProperties(){
		InvokerAction loadPropertiesAction = new InvokerAction(){
			public Object runAction(){
				Properties prop = new Properties();
				try{
					//System.out.println("loadQVProperties configFilePath:"+configFilePath);
					FileInputStream fis = new FileInputStream(configFilePath);
					System.out.println("Carregant fitxer de configuració des de: "+configFilePath);
					prop.load(fis);
					fis.close();
				} catch (Exception ex){
					System.out.println("!!!!!!!!!!!!!!!!!!No s'ha trobat el fitxer de configuració: "+configFilePath);
					//ex.printStackTrace(System.err);
				}
				return prop;
			}
		};
		InvokerThreadWaiter itw = new InvokerThreadWaiter(it, loadPropertiesAction);
		itw.start();
		return ((Properties)loadPropertiesAction.getResult());
	}
	
	/////
	
	private static int getIntProperty(Properties prop, String propertyName, int defaultValue){
		int propertyValue = defaultValue;
		String s = prop.getProperty(propertyName);
		if (s!=null){
			try{
				propertyValue = Integer.parseInt(s.trim());
			} catch (Exception ex){
				System.out.println("Error en el format de la propietat "+propertyName+": "+s);
			}
		}
		return propertyValue;
	}

	private static float getFloatProperty(Properties prop, String propertyName, float defaultValue){
		float propertyValue = defaultValue;
		String s = prop.getProperty(propertyName);
		if (s!=null){
			try{
				propertyValue = Float.parseFloat(s.trim());
			} catch (Exception ex){
				System.out.println("Error en el format de la propietat "+propertyName+": "+s);
			}
		}
		return propertyValue;
	}

	private static String getStringProperty(Properties prop, String propertyName, String defaultValue){
		String propertyValue = defaultValue;
		String s = prop.getProperty(propertyName);
		if (s!=null){
			propertyValue = s;
		}
		return propertyValue;
	}

}