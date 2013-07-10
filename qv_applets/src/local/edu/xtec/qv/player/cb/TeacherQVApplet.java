package edu.xtec.qv.player.cb;

import java.awt.Event;
import java.awt.Graphics;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format.TextMode;

public class TeacherQVApplet extends LocalQVApplet {
	
	protected String[] sGroupsList;
	protected Hashtable hStudents = new Hashtable();
	protected Hashtable hReports = new Hashtable();
	
	
	public void init() {
		for (int i=0;i<getGroupsList().length;i++){
			String sGroup = getGroupsList()[i];
			hStudents.put(sGroup, getStudentsList(sGroup));
		}
		if (getDocumentBase().toString().indexOf("csv.html")>=0){
			getCSVData();
		}
	}

	public void start() {
	}
	
	public void paint(Graphics g) {
		try{
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean action(Event evt, Object arg) {
		return true;
	}
	

/*************************************************
 * Getter/setter
 **************************************************/
		
	public String getUser(){
		return "";
	}

	public String getSurname(){
		return "";	
	}

	public String getGroup(){
		if (isNull(this.sGroup)){
			this.sGroup = this.getParameter("group");
		}
		return this.sGroup;	
	}

	public String getGroups(){
		return arrayToString(getGroupsList());
	}
	
	protected String[] getGroupsList(){
		if (sGroupsList==null){
			if (getDataDir()!=null){
				sGroupsList = getDataDir().list(new DirectoryFilter());
			}
		}
		return sGroupsList;
	}
	
	public String[] getStudentsList(){
		return getStudentsList(getGroup());
	}
	
	public String[] getStudentsList(String sGroup){
		String[] sStudents = null;
		if (getDataDir()!=null){
			if (!isNull(sGroup)){
				File fDir = new File(getDataDir()+File.separator+filter(sGroup));
				sStudents = fDir.list();				
			}
		}
		return sStudents;
	}
	
	public String getStudents(String sGroup){
		String sStudents = null;
		if (hStudents!=null && hStudents.containsKey(sGroup)){
			String[] sStudentsList = (String[])hStudents.get(sGroup);
			sStudents = arrayToString(sStudentsList);
		}
		return sStudents;
	}
	
	
	protected void getCSVData(){
		String[] sGroups = getGroupsList();
		for(int i=0;i<sGroups.length;i++){
			String sData = getReportData(sGroups[i]);
			hReports.put(sGroups[i], sData);
		}
	}
	
	public String getReportData(String sGroup){
		String sReport = "";
		if (hReports.containsKey(sGroup)){
			sReport=(String)hReports.get(sGroup);
			//if (bDebug && bTrace) System.out.println("getReportData-> HASH grup="+sGroup+" report="+sReport);			
		}else{		
			//if (bDebug && bTrace) System.out.println("getReportData-> grup="+sGroup);
			StringBuffer sbCSV = new StringBuffer();
			String[] sStudents = getStudentsList(sGroup);
			if (sStudents!=null){
				sbCSV.append("<H3>Grup: <span class='grup'>");sbCSV.append(sGroup);sbCSV.append("</span></H3>");
				sbCSV.append("<TABLE>");
				printHeader(sbCSV);
				for(int i=0;i<sStudents.length;i++){
					String sStudent = sStudents[i];
					sbCSV.append("<TR>");
					append(sbCSV,getStudentName(sStudent),null, null, true);
					this.doc = getDocument(sGroup, sStudent);
					this.sAssessment="cb";
					//QV Mates (secundaria) 2005/2007
//					this.sSection = "1138800597093664";
//					append(sbCSV,getScore("1138871863701172"));
//					append(sbCSV,getScore("113887213562490"));
//					this.sSection = "1138783141589150";
//					append(sbCSV,getScore("1138793689258839"));
//					append(sbCSV,getScore("1138786635918122"));
//					//append(sbCSV, getScore("1138797078307979"));
//					append(sbCSV,getScore("1138797951731933"));
//					//append(sbCSV,getScore("1138798704885305"));
//					this.sSection = "1138783103612944";
//					append(sbCSV,getScore("1139217144326879"));
//					append(sbCSV,getScore("1139217167389106"));
//					append(sbCSV,getScore("1139217580087831"));
//					append(sbCSV,getScore("1139217767832564"));
//					append(sbCSV,getScore("113921863674393"));					
					
					// QV Mates 16 anys (secundaria) 2005/2007
					this.sSection = "1138800597093664";
					append(sbCSV,getScore("1138871863701172"));
					append(sbCSV,getScore("113887213562490"));
					this.sSection = "1138783141589150";
					append(sbCSV,getScore("1138793689258839"));
					append(sbCSV,getScore("1138786635918122"));
					append(sbCSV,getScore("1138797078307979"));
					append(sbCSV,getScore("1138797951731933"));
					append(sbCSV,getScore("1138798704885305"));
					this.sSection = "1138783103612944";
					append(sbCSV,getScore("1139217144326879"));
					append(sbCSV,getScore("1139217167389106"));
					append(sbCSV,getScore("1139217580087831"));
					append(sbCSV,getScore("1139217767832564"));
					append(sbCSV,getScore("113921863674393"));
					
					/*/ QV Llengua i demo (primaria 2006/2008)
					this.sSection = "116508105207551";
					append(sbCSV,getScore("1165081078871809"));
					append(sbCSV,getScore("1165770204678442"));*/
					
					sbCSV.append("</TR>");
				}
				sbCSV.append("</TABLE>");
			}
			//System.out.println("getCSVData()-> group="+sGroup+"  data="+sbCSV.toString());
			sReport=sbCSV.toString();
		}
		return sReport;
	}
	private void printHeader(StringBuffer sb){
		sb.append("<TR>");
		append(sb, "", null, null, true);
		// QV Mates (secundaria) 2005/2007
//		append(sb, "Activitat 16", null, "2", true);
//		append(sb, "Activitat 17", null, "3", true);
//		append(sb, "Activitat 18", null, "5", true);
//		sb.append("</TR>");
//		sb.append("<TR>");
//		append(sb, "Alumnes", null, null, true);
//		append(sb, "16.1", null, null, true);
//		append(sb, "16.2", null, null, true);
//		append(sb, "17.1", null, null, true);
//		append(sb, "17.2", null, null, true);
//		append(sb, "17.3", null, null, true);
//		append(sb, "18.1", null, null, true);
//		append(sb, "18.2", null, null, true);
//		append(sb, "18.3", null, null, true);
//		append(sb, "18.4", null, null, true);
//		append(sb, "18.5", null, null, true);
		
		// QV Mates 16 anys (secundaria) 2005/2007
		append(sb, "Activitat 16", null, "2", true);
		append(sb, "Activitat 17", null, "5", true);
		append(sb, "Activitat 18", null, "5", true);
		sb.append("</TR>");
		sb.append("<TR>");
		append(sb, "Alumnes", null, null, true);
		append(sb, "16.1", null, null, true);
		append(sb, "16.2", null, null, true);
		append(sb, "17.1", null, null, true);
		append(sb, "17.2", null, null, true);
		append(sb, "17.3", null, null, true);
		append(sb, "17.4", null, null, true);
		append(sb, "17.5", null, null, true);
		append(sb, "18.1", null, null, true);
		append(sb, "18.2", null, null, true);
		append(sb, "18.3", null, null, true);
		append(sb, "18.4", null, null, true);
		append(sb, "18.5", null, null, true);
		
	/*/ QV Llengua i demo (primaria 2006/2008)
		append(sb, "Prova TIC", null, "2", true);
		sb.append("</TR>");
		sb.append("<TR>");
		append(sb, "Alumnes", null, null, true);
		append(sb, "1.1", null, null, true);
		append(sb, "1.2", null, null, true);*/
		
		sb.append("</TR>");
	}
	private void append(StringBuffer sb, String s){
		append(sb, s, "40px");
	}
	private void append(StringBuffer sb, String s, String sWidth){
		append(sb, s, sWidth, null);
	}
	private void append(StringBuffer sb, String s, String sWidth, String sColspan){
		append(sb, s, sWidth, sColspan, false);
	}
	private void append(StringBuffer sb, String s, String sWidth, String sColspan, boolean bIsHeader){
		if (s==null) s="0.0";
		if (bIsHeader) sb.append("<TH ");
		else sb.append("<TD ");
		if (sWidth!=null)sb.append("width='"+sWidth+"' "); 
		if (sColspan!=null)sb.append("colspan='"+sColspan+"' align='center' "); 
		sb.append(">");
		sb.append(s);
		//sb.append(";");
		if (bIsHeader) sb.append("</TH>");
		else sb.append("</TD>");
	}
	
	private String getStudentName(String sFilename){
		String s = sFilename.substring(0, sFilename.length()-8);
		return s.replaceAll("_", " ").trim();
	}
	
	protected String arrayToString(String[] sArray){
		String sRes = "";
		if (sArray!=null){
			for(int i=0;i<sArray.length;i++){
				sRes+=sArray[i]+";";
			}
		}
		return sRes;
	}
	
	public Document getDocument(){
		return this.doc;
	}
	
	public Document getDocument(String sGroup, String sStudentFile){
		Document dXML = null;
		String sPath = getDataDir().toString();
		if (sGroup!=null) sPath+=File.separator+sGroup;
		sPath+=File.separator+sStudentFile;
		File f = new File(sPath);
		if (f.exists()){
			dXML = open(sPath);		
		}
/*		if (bDebug && bTrace){ 
			System.out.println("path="+sPath+" exists?"+f.exists()+" doc="+dXML);		
			Format oFormat = Format.getPrettyFormat();
	        oFormat.setEncoding("ISO-8859-1");
			oFormat.setTextMode(TextMode.NORMALIZE);
			XMLOutputter serializer = new XMLOutputter(oFormat);
			try {
				serializer.output(dXML, System.out);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("###################");
		}*/
		return dXML;
	}	
			
}
