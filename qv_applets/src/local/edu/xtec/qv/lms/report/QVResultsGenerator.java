package edu.xtec.qv.lms.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileFilter;
import edu.xtec.qv.player.CorrectQVApplet;
import edu.xtec.qv.lms.*;
import edu.xtec.qv.lms.data.*;
import edu.xtec.qv.lms.util.thread.*;

public class QVResultsGenerator{

	public QVResultsGenerator(){
	}
	
	//public static Object[][] getResultsSummary(CorrectQVApplet main, QV qv, /*QVLMSDataAccessFunctions dataAccess,*/ int groupId){
	public static Object[][] getResultsSummary(CorrectQVApplet main, QV qv, String groupName){
		final CorrectQVApplet main2 = main;
		//final QVLMSDataAccessFunctions dataAccess2 = dataAccess;
		final QV qv2 = qv;
		final String groupName2 = groupName;
		
		InvokerThread it = main.getInvokerThread();
		InvokerAction getSecureResultsSummaryAction = new InvokerAction(){
			public Object runAction(){
				return getSecureResultsSummary(main2, qv2, groupName2);
			}
		};
		InvokerThreadWaiter itw = new InvokerThreadWaiter(it, getSecureResultsSummaryAction);
		itw.start();
		return ((Object[][])getSecureResultsSummaryAction.getResult());
		
	}
	
	public static String getDataGroups(CorrectQVApplet main){
		InvokerThread it = main.getInvokerThread();
		final File fDataDir = new File(main.getDataFilePath());
		InvokerAction getSecureGroupsNameAction = new InvokerAction(){
			public Object runAction(){
				String groups = "";
				if (fDataDir.exists()){
					ArrayList alFiles = new ArrayList();
					File[] subFolders = fDataDir.listFiles(folderFileFilter);
					for (int j=0; subFolders!=null && j<subFolders.length; j++){
						File f = subFolders[j];
						groups = groups+f.getName()+";";
					}
				}
				return groups;
			}
		};
		InvokerThreadWaiter itw = new InvokerThreadWaiter(it, getSecureGroupsNameAction);
		itw.start();
		/*int numGroups = getNumGroups();
		for (int i=1; i<=numGroups; i++){
			loginGroups = loginGroups + getGroupName(i) + ";";
		}*/
		return ((String)getSecureGroupsNameAction.getResult());
	}

	//private static Object[][] getSecureResultsSummary(CorrectQVApplet main, QV qv, int groupId){
	private static Object[][] getSecureResultsSummary(CorrectQVApplet main, QV qv, String groupName){
		Object[][] resultsSummary = null;
		try{
			
			int assignmentId = 0;
			File fDataDir = new File(main.getDataFilePath());
			//if (groupId>=0){
			if (groupName!=null && !groupName.trim().equals("-1")){
				String folderName = groupName; //edu.xtec.qv.lms.util.StringUtility.filter(main.getGroupName(groupId));
				System.out.println("folderName:"+folderName);
				fDataDir = new File(fDataDir, folderName);
			}
	
			if (fDataDir.exists()){
				System.out.println("Generant resum de la carpeta: "+fDataDir.getAbsolutePath());
				ArrayList alFiles = new ArrayList();
				
				//Obtinc tots els fitxers de reports
				File[] files = fDataDir.listFiles(qtiResultReportFileFilter);
				for (int i=0; files!=null && i<files.length; i++)
					alFiles.add(files[i]);
				
				//Miro també els fitxers de reports de les subcarpetes de primer nivell
				File[] subFolders = fDataDir.listFiles(folderFileFilter);
				for (int j=0; subFolders!=null && j<subFolders.length; j++){
					File f = subFolders[j];
					File[] subFiles = f.listFiles(qtiResultReportFileFilter);
					for (int i=0; subFiles!=null && i<subFiles.length; i++)
						alFiles.add(subFiles[i]);
				}
				
				//Recorro els fitxers per generar el resum
				HashMap hmUserSummary = new HashMap();
				for (int i=0; i<alFiles.size();i++){
					File f = (File)alFiles.get(i);
					try{
						System.out.println("Generant resum del fitxer de resultats:"+f.getAbsolutePath());
						String sCurrentGroupName = getGroupName(f);
						FileInputStream fis = new FileInputStream(f);
						Object[] studentSummary = getStudentSummary(main, qv, fis, sCurrentGroupName);
						//alUserId.add(studentSummary[0]);
						
						//TODO Si es posen en un HashMap... cal posar el nom de la carpeta del grup com a part del nom del grup...
						//El HashMap era per saber si ja teniem les dades de l'alumne i generar o no un informe buit, ara ja no cal.
						
						
						System.out.println("---->"+studentSummary[0].toString()+"_"+studentSummary[1].toString());
						hmUserSummary.put(studentSummary[0].toString()+"_"+studentSummary[1].toString(), studentSummary);
						//resultsSummary[i] = studentSummary;
					} catch (Exception ex){
						ex.printStackTrace(System.err);
					}
				}
				
				//Afegeixo els resultats de qui no ha fet la prova
				///////hmUserSummary = addNotStartedResultsSummary(hmUserSummary, main, qv, groupId);
				
				//Ho retorno en format de matriu
				Set s = hmUserSummary.keySet();
				Iterator it = s.iterator();
				resultsSummary = new Object[s.size()][11];
				for (int i=0;i<s.size() && it.hasNext();i++){
					Object o = hmUserSummary.get(it.next());
					resultsSummary[i]=(Object[])o;
				}
				
			} else {
				System.err.println("No existeix la carpeta de resultats: "+fDataDir);
			}
		} catch (Exception ex){
			ex.printStackTrace(System.err);
		}
		return resultsSummary;
	}
	
	private static String getGroupName(File f){
		String groupName = "";
		if (f!=null)
			groupName = f.getParentFile().getName();
		return groupName;
	}
	
	private static HashMap addNotStartedResultsSummary(HashMap hmUserSummary, CorrectQVApplet main, QV qv, int groupId){
		HashMap hm = new HashMap();
		if (groupId>=0){
			addGroup(hm, hmUserSummary, main, qv, groupId);
		} else {
			for (int i=0;i<=main.getNumGroups(); i++)
				addGroup(hm, hmUserSummary, main, qv, i);
		}
		
		//Afegeixo els que ja hi eren i potser no són al fitxer de login...
		Iterator it = hmUserSummary.keySet().iterator();
		while (it.hasNext()){
			String userId = (String)it.next();
			if (!hm.containsKey(userId))
				hm.put(userId, hmUserSummary.get(userId));
		}
		return hm;
	}
	
	private static void addGroup(HashMap hm, HashMap hmUserSummary, CorrectQVApplet main, QV qv, int groupId){
		System.out.println("Afegint els resultats en blanc dels alumnes que no han fet la prova del grup "+groupId);
		int numUsers = main.getNumUsers(groupId+"");
		for (int i=1;i<=numUsers; i++){
			String userId = main.getUserId(groupId+"", i);
			String userName = main.getUserName(groupId+"", i);
			if (hmUserSummary.containsKey(userId+"_"+groupId)){
				hm.put(userId+"_"+groupId, hmUserSummary.get(userId+"_"+groupId));
			} else { //No tenim el fitxer de resultats
				String indexFile = main.getDocumentBase().getProtocol()+":///"+main.getDocumentBaseDirPath()+"/index.htm";
				String teacherLink = getEmptyTeacherLink(indexFile, qv, userId, groupId+"", userName, main.getPassword(groupId+""));
				QVUserInfo qvUserInfo = new QVUserInfo(userId, groupId+"", main.getGroupName(groupId), main.getUserIT(groupId+"", i), main.getUserNE(groupId+"", i));
				System.out.println("Afegeixo qvUserInfo:"+qvUserInfo.toString());
				QVAssignment qvAssignment = new QVAssignment();
				ArrayList alQVSection = new ArrayList();
				Object[] result = new Object[]{userId, groupId+"", userId, qv_print_states(new HashMap()), "0", "0", "00:00:00", teacherLink, qvUserInfo, qvAssignment, alQVSection};
				hm.put(userId+"_"+groupId, result);
			}
		}
	}
	
	private static String getEmptyTeacherLink(String indexFile, QV qv, String userId, String groupId, String userName, String password){
		String teacherLink = 
			indexFile.replace('/','\\')+"?"+
			"server=local"+
			"&assignmentId=0"+
			"&userid="+userId+
			"&fullname="+userName+
			"&showinteraction="+qv.getShowInteraction()+
			"&showcorrection="+qv.getShowCorrection()+
			"&order_sections="+qv.getOrderSections()+
			"&order_items="+qv.getOrderItems()+
			"&userview=teacher"+
			"&p_checkUser=true"+
			"&p_userId="+userId+
			"&p_groupId="+groupId+
			"&p_password="+password;
		return teacherLink;
	}

	public static Object[] getStudentSummary(CorrectQVApplet main, QV qv, InputStream isReport, String groupName) throws Exception{
		QVUserInfo qvUserInfo = new QVUserInfo();
		QVAssignment qvAssignment = new QVAssignment();
		ArrayList alQVSection = new ArrayList();
		ReportParser.parseReport(isReport, qvUserInfo, qvAssignment, alQVSection);
		//System.out.println("Llegit qvUserInfo:"+qvUserInfo.toString());
		
		String userId = qvUserInfo.getUserId();
		String groupId = qvUserInfo.getGroupId();
		String userName = main.getUserName(groupId, userId);
		if (userName==null || userName.trim().length()<1){
			userName = userId;
		}
		QVAssignmentSummary qvAssignmentSummary = QVAssignmentSummary.getAssignmentSummary(qvAssignment, alQVSection);
		String state = qvAssignmentSummary.getState();
		String score = qvAssignmentSummary.pendingScore+"";
		String attempts = qvAssignmentSummary.attempts+""; 
		String time = qvAssignmentSummary.time;

		String indexFile = main.getDocumentBase().getProtocol()+":///"+main.getDocumentBaseDirPath()+"/index.htm";
		String teacherLink = 
			indexFile.replace('/','\\')+"?"+
			"server=local"+
			"&assignmentId=0"+
			"&userid="+userId+
			"&fullname="+userName+
			"&showinteraction="+qv.getShowInteraction()+
			"&showcorrection="+qv.getShowCorrection()+
			"&order_sections="+qv.getOrderSections()+
			"&order_items="+qv.getOrderItems()+
			"&userview=teacher"+
			"&p_checkUser=true"+
			"&p_userId="+userId+
			"&p_groupId="+groupId+
			"&p_groupName="+groupName+///
			"&p_password="+main.getPassword(groupId);
		if (qv.getOrderSections()==1)
			teacherLink += "&section_order="+qvAssignment.getSectionOrder();
		if (qv.getOrderItems()==1)
			teacherLink += "&item_order="+qvAssignment.getItemOrder();

		//return new Object[]{userId, groupId, userName, qv_print_states(qvAssignmentSummary.states), score, attempts, time, teacherLink, qvUserInfo, qvAssignment, alQVSection};
		return new Object[]{userId, groupName, userName, qv_print_states(qvAssignmentSummary.states), score, attempts, time, teacherLink, qvUserInfo, qvAssignment, alQVSection};
	}
	
	private static String qv_print_states(java.util.HashMap hmStates){
		String statesimg="";
		if (hmStates!=null) {
			if (hmStates.containsKey("state_-1") && ((Integer)hmStates.get("state_-1")).intValue()>0 
					&& (!hmStates.containsKey("state_0") || ((Integer)hmStates.get("state_0")).intValue()==0)
					&& (!hmStates.containsKey("state_1") || ((Integer)hmStates.get("state_1")).intValue()==0)
					&& (!hmStates.containsKey("state_2") || ((Integer)hmStates.get("state_2")).intValue()==0)){
				statesimg += qv_print_state_not_started(false);
			} else if (hmStates.containsKey("state_0") && ((Integer)hmStates.get("state_0")).intValue()>0){
				statesimg += qv_print_state_started(false);	
			}
			if (hmStates.containsKey("state_1") && ((Integer)hmStates.get("state_1")).intValue()>0){
				if ((!hmStates.containsKey("state_0") || ((Integer)hmStates.get("state_0")).intValue()==0)
						&& (!hmStates.containsKey("state_-1") || ((Integer)hmStates.get("state_-1")).intValue()==0)
						&& ((String)hmStates.get("state")).equals("1")){
					 statesimg += qv_print_state_delivered(false);
				} else {
					 statesimg += qv_print_state_partially_delivered(false);
				}
			}
			if (hmStates.containsKey("state_2") && ((Integer)hmStates.get("state_2")).intValue()>0){
				if (((String)hmStates.get("state")).equals("2") 
						&& (!hmStates.containsKey("state_0") || ((Integer)hmStates.get("state_0")).intValue()==0)
						&& (!hmStates.containsKey("state_-1") || ((Integer)hmStates.get("state_-1")).intValue()==0)){ 
					 statesimg += qv_print_state_corrected(false);
				} else {
					 statesimg += qv_print_state_partially_corrected(false);
				}
			}
		}
		if (statesimg.trim().length()==0) statesimg = qv_print_state_not_started(false);
		return statesimg;
	}

	private static String qv_print_state_not_started(boolean text){
		//La localització queda fóra de l'abast del projecte GELAT
		String strstatenotstarted  = "No iniciat";//get_string("statenotstarted", "qv");
		String html="<IMG src='../../../files/pix/qv_state_not_started.gif' alt='"+strstatenotstarted+"' title='"+strstatenotstarted+"'/>";
		if (text) html += " "+strstatenotstarted;
		return html;
	}
	private static String qv_print_state_started(boolean text){
		//La localització queda fóra de l'abast del projecte GELAT
		String strstatestarted = "Iniciat"; //get_string("statestarted", "qv");
		String html="<IMG src='../../../files/pix/qv_state_started.gif' alt='"+strstatestarted+"' title='"+strstatestarted+"'/>";
		if (text) html += " "+strstatestarted;
		return html;
	}
	private static String qv_print_state_delivered(boolean text){
		//La localització queda fóra de l'abast del projecte GELAT
		String strstatedelivered = "Lliurat"; //get_string("statedelivered", "qv");
		String html="<IMG src='../../../files/pix/qv_state_delivered.gif' alt='"+strstatedelivered+"' title='"+strstatedelivered+"'/>";
		if (text) html += " "+strstatedelivered;
		return html;
	}
	private static String qv_print_state_partially_delivered(boolean text){
		//La localització queda fóra de l'abast del projecte GELAT
		String strstatepartiallydelivered = "Lliurat parcialment"; //get_string("statepartiallydelivered", "qv");
		String html="<IMG src='../../../files/pix/qv_state_part_delivered.gif' alt='"+strstatepartiallydelivered+"' title='"+strstatepartiallydelivered+"'/>";
		if (text) html += " "+strstatepartiallydelivered;
		return html;
	}
	private static String qv_print_state_corrected(boolean text){
		//La localització queda fóra de l'abast del projecte GELAT
		String strstatecorrected = "Corregit"; //get_string("statecorrected", "qv");
		String html="<IMG src='../../../files/pix/qv_state_corrected.gif' alt='"+strstatecorrected+"' title='"+strstatecorrected+"'/>";
		if (text) html += " "+strstatecorrected;
		return html;
	}
	private static String qv_print_state_partially_corrected(boolean text){
		//La localització queda fóra de l'abast del projecte GELAT
		String strstatepartiallycorrected = "Corregit parcialment"; //get_string("statepartiallycorrected", "qv");
		String html="<IMG src='../../../files/pix/qv_state_part_corrected.gif' alt='"+strstatepartiallycorrected+"' title='"+strstatepartiallycorrected+"'/>";
		if (text) html += " "+strstatepartiallycorrected;
		return html;
	}

		/*http://localhost/moodle/file.php/2/Quaderns/rep/html/index.htm?
			server=http://localhost/moodle/mod/qv/action/beans.php
			&assignmentid=1
			&userid=3
			&fullname=%C3%80ngels%20Llastarri%20Mateu
			&skin=infantil
			&lang=ca
			&showinteraction=1
			&showcorrection=1
			&order_sections=1
			&order_items=1
			&userview=teacher
			&section_order=62
			&item_order=23
			&appl=http://localhost/recursos/appl/
			&css=http://localhost/recursos/css/
			&js=http://localhost/recursos/scripts/
		 */

	static FileFilter qtiResultReportFileFilter = new FileFilter(){
		public boolean accept(File pathname) {
			if (pathname!=null){
				String fileName = pathname.getName();
				if (fileName.startsWith("qti_result_report_") && fileName.endsWith(".xml"))
					return true;
			}
			return false;
		}
	};
	static FileFilter folderFileFilter = new FileFilter(){
		public boolean accept(File pathname) {
			if (pathname!=null){
				if (pathname.isDirectory())
					return true;
			}
			return false;
		}
	};

}