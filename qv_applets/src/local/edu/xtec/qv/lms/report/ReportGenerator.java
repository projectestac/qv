package edu.xtec.qv.lms.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import edu.xtec.qv.lms.data.*;
import edu.xtec.qv.lms.QVLMSActionsAdapter;
import edu.xtec.qv.lms.QVLMSDataAccessFunctions;
import edu.xtec.qv.lms.util.TimeUtility;
import edu.xtec.qv.lms.util.RandomUtility;
import edu.xtec.qv.lms.report.qtidata.*;
import edu.xtec.qv.player.CorrectQVApplet;

public class ReportGenerator{

	//public static String getCSVResultReport(CorrectQVApplet main, QTIAssessmentInfo ai, QV qv, int groupId){
	public static String getCSVResultReport(CorrectQVApplet main, QTIAssessmentInfo ai, QV qv, String groupName){	
		StringBuffer sbResult = new StringBuffer();
		try{
			//Object[][] resultsSummary = QVResultsGenerator.getResultsSummary(main, qv, groupId);
			Object[][] resultsSummary = QVResultsGenerator.getResultsSummary(main, qv, groupName);
			if (resultsSummary!=null){
				String header = getCSVResultHeader(ai);
				sbResult.append(header+"\n");
				for (int i=0;i<resultsSummary.length;i++){
					Object[] studentSummary = resultsSummary[i];
					//Object[]{userId, groupId+"", userId, qv_print_states(new HashMap()), "0", "0", "00:00:00", teacherLink, qvUserInfo, qvAssignment, alQVSection}
					if (studentSummary!=null){
						QVUserInfo qvUserInfo = (QVUserInfo)studentSummary[studentSummary.length-3];
						QVAssignment qvAssignment = (QVAssignment)studentSummary[studentSummary.length-2];
						ArrayList alQVSection = (ArrayList)studentSummary[studentSummary.length-1];
						String content = edu.xtec.qv.lms.report.ReportGenerator.getCSVResultReport(ai, qvUserInfo, qvAssignment, alQVSection);
						System.out.println("content:"+content);
						
						sbResult.append(content+"\n");
					} else {
						System.out.println("!!!!!!!!!!!!!!	studentSummary=null!!!! i="+i);
					}
				}
			} else {
				System.out.println("No hi ha estudiants");
			}
		} catch (Exception ex){
			ex.printStackTrace(System.err);
		}
		//return resultsSummary;
		return sbResult.toString();
	}
	
	protected static String getCSVResultReport(QTIAssessmentInfo ai, QVUserInfo userInfo, QVAssignment qvAssignment, ArrayList alQVSection){
		StringBuffer sbXML = new StringBuffer();
		QVAssignmentSummary as = QVAssignmentSummary.getAssignmentSummary(qvAssignment, alQVSection);
		
		sbXML.append(getCSVValue(userInfo.getGroupName()));
		sbXML.append(getCSVValue(userInfo.getUserId()));
		sbXML.append(getBooleanCSVValue(userInfo.getUserIT()));
		sbXML.append(getBooleanCSVValue(userInfo.getUserNE()));
		sbXML.append(getCSVValue(ai.getId()));
		//sbXML.append(getCSVValue(ai.getName())); //Nom del quadern
		//sbXML.append(getCSVValue(as.getState()));
		//sbXML.append(getCSVValue(as.pendingScore+""));
		//sbXML.append(getCSVValue(as.time));
		int[] randomArraySections = RandomUtility.getRandomArrayRandomizable(ai.getSections().size(), qvAssignment.getSectionOrder(), ai.getRandomizableSections());
		
		for (int i=0; i<ai.getSections().size(); i++){
			QTISectionInfo si = (QTISectionInfo)ai.getSections().get(i);
			QVSection s = getQVSection(alQVSection, si.getId());
			if (s==null)
				s = new QVSection();
			QVSectionSummary ss = QVSectionSummary.getSectionSummary(s);;
			sbXML.append(getCSVValue(si.getId()));//
			sbXML.append(getCSVValue(si.getName()));//
			//sbXML.append(getCSVValue(s.getState()+""));
			//sbXML.append(getCSVValue(ss.pendingScore+""));
			//sbXML.append(getCSVValue(s.getTime()));
			//sbXML.append(getCSVValue(ss.attempts+""));
			//if(qvAssignment.getSectionOrder()>0)
			//int sectionOrder = RandomUtility.getReverse(randomArraySections, si.getPosition());
			//sbXML.append(getCSVValue(sectionOrder+""));
			//int[] randomArrayItems = RandomUtility.getRandomArrayRandomizable(si.getItems().size(), qvAssignment.getItemOrder(), si.getRandomizableItems());

			for (int j=0; j<si.getItems().size(); j++){
				QTIItemInfo ii = (QTIItemInfo)si.getItems().get(j);
				//sbXML.append(getCSVValue(s.getState()+""));
				//int itemOrder = RandomUtility.getReverse(randomArrayItems, ii.getPosition());
				//sbXML.append(getCSVValue(itemOrder+""));
				sbXML.append(getCSVValue(ii.getId()));//
				sbXML.append(getCSVValue(ii.getName()));//
				sbXML.append(getBooleanCSVValue(getQTIItemCorrect(s.getPendingScores(), ii.getId())));
				sbXML.append(getCSVValue(getQTIItemScore(s.getPendingScores(), ii.getId())));
				sbXML.append(ii.getMaxScore());//
				
				for (int k=0; k<ii.getResponses().size(); k++){
					QTIResponseInfo ri = (QTIResponseInfo)ii.getResponses().get(k);
					sbXML.append(getCSVValue(/*"R_"+*/getResponseValue(ri.getId(), ii.getId(), s.getResponses())));//La R és perquè l'excel no intenti agafar l'identificador de la resposta com a un enter i el mostri en format exponencial (1,024E12)
				}
			}
		}
		return sbXML.toString();
	}
	
	protected static String getCSVResultHeader(QTIAssessmentInfo ai){
		StringBuffer sbXML = new StringBuffer();
		sbXML.append(getCSVValue("grup"));
		sbXML.append(getCSVValue("alumne/a"));
		sbXML.append(getCSVValue("it"));
		sbXML.append(getCSVValue("nee"));
		sbXML.append(getCSVValue("model"));
		//sbXML.append(getCSVValue("Id quadern"));
		//sbXML.append(getCSVValue("Nom quadern"));
		//sbXML.append(getCSVValue("Estat quadern"));
		//sbXML.append(getCSVValue("Puntuació quadern"));
		//sbXML.append(getCSVValue("Temps quadern"));
		for (int i=0; i<ai.getSections().size(); i++){
			QTISectionInfo si = (QTISectionInfo)ai.getSections().get(i);
			//sbXML.append(getCSVValue("Id full"));
			//sbXML.append(getCSVValue("Nom full"));
			//sbXML.append(getCSVValue("Estat full "+si.getId()));
			//sbXML.append(getCSVValue("Puntuació full "+si.getId()));
			//sbXML.append(getCSVValue("Temps full "+si.getId()));
			//sbXML.append(getCSVValue("Lliuraments full "+si.getId()));
			//sbXML.append(getCSVValue("Ordre full "+si.getId()));
			sbXML.append(getCSVValue("id_bloc"));//
			sbXML.append(getCSVValue("nom_bloc"));//
			for (int j=0; j<si.getItems().size(); j++){
				QTIItemInfo ii = (QTIItemInfo)si.getItems().get(j);
				sbXML.append(getCSVValue("id_item"));//
				sbXML.append(getCSVValue("nom_item"));//
				sbXML.append(getCSVValue("correcte_item"));
				//sbXML.append(getCSVValue("Estat item "+ii.getId()));
				//sbXML.append(getCSVValue("puntuacio_item "+ii.getId()));
				sbXML.append(getCSVValue("puntuacio_item"));//
				sbXML.append(getCSVValue("puntmax_item"));//
				//sbXML.append(getCSVValue("Ordre item "+ii.getId()));
				for (int k=0; k<ii.getResponses().size(); k++){
					QTIResponseInfo ri = (QTIResponseInfo)ii.getResponses().get(k);
					//sbXML.append(getCSVValue("Resposta "+ri.getId()));
					sbXML.append(getCSVValue("resposta"+(k+1)+"_item"));
				}
			}
		}
		return sbXML.toString();
	}
	
	protected static String getCSVValue(String s){
		return s+";";
	}
	
	protected static String getBooleanCSVValue(String s){
		if (s!=null && s.trim().toLowerCase().startsWith("s"))
			return "1;";
		else
			return "0;";
	}
	
	public static String getQTIResultReport(QVUserInfo userInfo, QVAssignment qvAssignment, ArrayList alQVSection){
		StringBuffer sbXML = new StringBuffer();
		sbXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sbXML.append("<qti_result_report>");
		sbXML.append(getQTIResult(userInfo, qvAssignment, alQVSection));
		sbXML.append("</qti_result_report>");
		return sbXML.toString();
	}
	
	protected static String getQTIResult(QVUserInfo userInfo, QVAssignment qvAssignment, ArrayList alQVSection){
		StringBuffer sbXML = new StringBuffer();
		sbXML.append("<result>");
		sbXML.append(getQTIContext(userInfo));
		sbXML.append(getQTIAssessmentResult(qvAssignment, alQVSection));
		sbXML.append("	<extension_result>");
		sbXML.append("		<section_order>"+qvAssignment.getSectionOrder()+"</section_order>");
		sbXML.append("		<item_order>"+qvAssignment.getItemOrder()+"</item_order>");
		sbXML.append("	</extension_result>");
		sbXML.append("</result>");
		return sbXML.toString();
	}
	
	protected static String getQTIContext(QVUserInfo userInfo){
		StringBuffer sbXML = new StringBuffer();
		sbXML.append("<context>");
		//sbXML.append("	<name>"+xmlScape(userInfo.getUserId())+"_name"+"</name>");
		sbXML.append("	<generic_identifier>");
		sbXML.append("		<type_label>"+"Student Number"+"</type_label>");
		sbXML.append("		<identifier_string>"+xmlScape(userInfo.getUserId())+"</identifier_string>");
		sbXML.append("	</generic_identifier>");
		sbXML.append("	<generic_identifier>");
		sbXML.append("		<type_label>"+"Student Group"+"</type_label>");
		sbXML.append("		<identifier_string>"+xmlScape(userInfo.getGroupId())+"</identifier_string>");
		sbXML.append("	</generic_identifier>");
		sbXML.append("	<generic_identifier>");
		sbXML.append("		<type_label>"+"Student Group Name"+"</type_label>");
		sbXML.append("		<identifier_string>"+xmlScape(userInfo.getGroupName())+"</identifier_string>");
		sbXML.append("	</generic_identifier>");
		sbXML.append("	<generic_identifier>");
		sbXML.append("		<type_label>"+"Student IT"+"</type_label>");
		sbXML.append("		<identifier_string>"+xmlScape(userInfo.getUserIT())+"</identifier_string>");
		sbXML.append("	</generic_identifier>");
		sbXML.append("	<generic_identifier>");
		sbXML.append("		<type_label>"+"Student NE"+"</type_label>");
		sbXML.append("		<identifier_string>"+xmlScape(userInfo.getUserNE())+"</identifier_string>");
		sbXML.append("	</generic_identifier>");
		sbXML.append("</context>");
		return sbXML.toString();
	}
	
	protected static String getQTIAssessmentResult(QVAssignment qvAssignment, ArrayList alQVSection){
		StringBuffer sbXML = new StringBuffer();
		QVAssignmentSummary as = QVAssignmentSummary.getAssignmentSummary(qvAssignment, alQVSection);
		sbXML.append("<assessment_result>");
		sbXML.append("	<duration>"+TimeUtility.getDuration(as.time)+"</duration>");
		sbXML.append("	<control feedback_switch=\"No\" hint_switch=\"No\" solution_switch=\"No\"/>"); //No mostrem feedback a nivell d'assessment");
		sbXML.append("	<outcomes>");
		sbXML.append("		<score varname=\"SCORE\" vartype=\"Integer\">");
		sbXML.append("			<score_value>"+as.pendingScore+"</score_value>");
		sbXML.append("			<score_min>0</score_min>");
		sbXML.append("		</score>");
		sbXML.append("	</outcomes>");
		if (alQVSection!=null){
			for (int i=0;i<alQVSection.size();i++){
				QVSection qvSection = (QVSection)alQVSection.get(i);
				QVSectionSummary ss = QVSectionSummary.getSectionSummary(qvSection);
				//$xml.= getSectionResult($qv_assignment_summary, $assessmentInfo, $qv_section, $qv_section_summary, $sectionInfo);
				sbXML.append(getQTISectionResult(qvSection, as, ss));
			}
		}
		sbXML.append("</assessment_result>");
		return sbXML.toString();
	}

	protected static String getQTISectionResult(QVSection qvSection, QVAssignmentSummary as, QVSectionSummary ss){
		StringBuffer sbXML = new StringBuffer();
		sbXML.append("<section_result ident_ref=\""+qvSection.getSectionId()+"\">");
		sbXML.append("	<duration>"+TimeUtility.getDuration(qvSection.getTime())+"</duration>");
		sbXML.append("	<control feedback_switch=\"Yes\" hint_switch=\"No\" solution_switch=\"No\"/>"); //No mostrem feedback a nivell de full");
		sbXML.append("	<outcomes>");
		sbXML.append("		<score varname=\"SCORE\" vartype=\"Integer\">");
		if (as.pendingScore>=0){
			sbXML.append("		<score_value>"+ss.pendingScore+"</score_value>");
		} else {
			sbXML.append("		<score_value>0</score_value>");
		}
		sbXML.append("			<score_min>0</score_min>");
		sbXML.append("		</score>");
		sbXML.append("	</outcomes>");
		if (as.sectionOrder>0){
			//TODO Cal implementar les funcions d'ordenació
			//$randArray = getRandomArrayRandomizable(count($assessmentInfo->sections),$qv_assignment_summary->sectionorder,$assessmentInfo->randomizable_sections);
			//$xml.= '<extension_section_result>';
			//	$xml.= '<order>'.$randArray[$sectionInfo->position].'</order>';
			//$xml.= '</extension_section_result>';
			//sbXML.append("<extension_section_result>");
			//sbXML.append("	<order>"+""+/*.$randArray[$sectionInfo->position].*/+"</order>");
			//sbXML.append("</extension_section_result>");
		}
		sbXML.append(getQTIItemsResult(qvSection, as, ss));
		//foreach($sectionInfo->items as $itemInfo){
		//	$xml.= getItemResult($qv_assignment_summary, $sectionInfo, $qv_section, $itemInfo);
		//}
		sbXML.append("	<extension_section_result>");
		sbXML.append("		<responses><![CDATA["+qvSection.getResponses()+"]]></responses>"); //Aquesta informació ja és present al document però tenint en compte el nombre d'accessos que s'ha de fer al fitxer i que el parsejat seria extensiu, es decideix tenir la informació preparada.
		sbXML.append("		<scores><![CDATA["+qvSection.getScores()+"]]></scores>");
		sbXML.append("		<pending_scores><![CDATA["+qvSection.getPendingScores()+"]]></pending_scores>");
		sbXML.append("		<attempts>"+qvSection.getAttempts()+"</attempts>");
		sbXML.append("		<state>"+qvSection.getState()+"</state>");
		sbXML.append("		<time>"+qvSection.getTime()+"</time>"); 
		sbXML.append("	</extension_section_result>");
		sbXML.append("</section_result>");
		return sbXML.toString();
	}
	
	protected static String getQTIItemsResult(QVSection qvSection, QVAssignmentSummary as, QVSectionSummary ss){
		StringBuffer sbXML = new StringBuffer();
		ArrayList alItemIds = getItemIds(qvSection.getResponses());
		for (int i=0; i<alItemIds.size(); i++){
			String itemId = (String)alItemIds.get(i);
			sbXML.append("<item_result ident_ref=\""+itemId+"\">");
			ArrayList alResponseIds = getResponseIds(qvSection.getResponses(), itemId);
			for (int j=0; j<alResponseIds.size(); j++){
				String responseId = (String)alResponseIds.get(j);
				sbXML.append(getQTIResponse(responseId, itemId, qvSection.getResponses()));
			}
			sbXML.append("	<outcomes>");
			sbXML.append("		<score varname=\"SCORE\" vartype=\"Integer\">");
			sbXML.append("			<score_value>"+getQTIItemScore(qvSection.getPendingScores(), itemId)+"</score_value>");
			sbXML.append("			<score_min>0</score_min>");
			sbXML.append("		</score>");
			sbXML.append("	</outcomes>");
			
			//TODO Cal implementar les funcions d'ordenació
			if (as.itemOrder>0){
				//$randArray = getRandomArrayRandomizable(count($sectionInfo->items),$qv_assignment_summary->itemorder,$sectionInfo->randomizable_items);
				//$xml.= '<extension_item_result>';
				//	$xml.= '<order>'.$randArray[$itemInfo->position].'</order>';
				//$xml.= '</extension_item_result>';
			}
			sbXML.append("</item_result>");
		}
		return sbXML.toString();
	}

	protected static String getQTIResponse(String responseId, String itemId, String sectionResponses){
		StringBuffer sbXML = new StringBuffer();
		sbXML.append("<response ident_ref=\""+responseId+"\">");
		sbXML.append(getQTIResponseValue(responseId, itemId, sectionResponses));
		sbXML.append("</response>");
		return sbXML.toString();
	}
	
	protected static String getQTIResponseValue(String responseId, String itemId, String sectionResponses){
		StringBuffer sbXML = new StringBuffer();
		String response = getResponseValue(responseId, itemId, sectionResponses);
		
		String response_status = "Null"; //No information
		if (response.trim().length()>0)
			response_status = "Valid";
		else
			response_status = "NA";

		sbXML.append("<response_value response_status=\""+response_status+"\">"+xmlScape(response)+"</response_value>");
		return sbXML.toString();
	}
	
	private static String getResponseValue(String responseId, String itemId, String sectionResponses){
		String response = "";
		int start = sectionResponses.indexOf(itemId+"-->"+responseId+"=");
	  	if (start>=0){
	  		start += (itemId+"-->"+responseId+"=").length();
	    	int length = (sectionResponses.substring(start)).indexOf("#");
	    	response = sectionResponses.substring(start, start+length);
	  	}
	  	return response;
	}
	
	private static String getQTIItemScore(String scores, String itemId){
		String score = "0";
	  	int start = scores.indexOf(itemId+"_score=");
	  	if (start>=0){
	  		start += (itemId+"_score=").length();
	    	int length = (scores.substring(start+1)).indexOf("#")+1;
	    	score = scores.substring(start, start+length);
	  	}
		if (score.trim().length()<1)
			score = "0.0";
		return score;
	}
	
	private static String getQTIItemCorrect(String scores, String itemId){
		String correct = "N";
	  	int start = scores.indexOf(itemId+"_correct=");
	  	if (start>=0){
	  		start += (itemId+"_correct=").length();
	    	int length = (scores.substring(start+1)).indexOf("#")+1;
	    	correct = scores.substring(start, start+length);
	  	}
		if (correct.trim().length()>0 && correct.trim().equalsIgnoreCase("true"))
			correct = "S";
		else
			correct = "N";
		//System.out.println("itemId:"+itemId+" correct:"+correct+" scores:"+scores);
		return correct;
	}
	
	private static ArrayList getItemIds(String qvSectionResponses){
		//qvSectionResponses= itemId1-->responseId1=value1#itemId2-->responseId2=value2#...#itemIdn-->responseIdn=valuen
		ArrayList alItemIds = new ArrayList();
		if (qvSectionResponses!=null){ 
			StringTokenizer st = new StringTokenizer(qvSectionResponses, "#");
			while (st.hasMoreTokens()){
				String response = st.nextToken();
				StringTokenizer st2 = new StringTokenizer(response, "=");
				if (st2.hasMoreTokens()){
					String key = st2.nextToken();
					if (st2.hasMoreTokens()){
						String value = st2.nextToken();
					}
					int index = key.indexOf("-->");
					if (index>0){
						String itemId = key.substring(0,index);
						String responseId = key.substring(index+3);
						if (!alItemIds.contains(itemId))
							alItemIds.add(itemId);
					}
				}
			}
		}
		return alItemIds;
	}
	
	private static ArrayList getResponseIds(String qvSectionResponses, String itemId){
		//qvSectionResponses= itemId1-->responseId1=value1#itemId2-->responseId2=value2#...#itemIdn-->responseIdn=valuen
		ArrayList alResponseIds = new ArrayList();
		if (qvSectionResponses!=null){ 
			StringTokenizer st = new StringTokenizer(qvSectionResponses, "#");
			while (st.hasMoreTokens()){
				String response = st.nextToken();
				StringTokenizer st2 = new StringTokenizer(response, "=");
				if (st2.hasMoreTokens()){
					String key = st2.nextToken();
					if (st2.hasMoreTokens()){
						String value = st2.nextToken();
					}
					int index = key.indexOf("-->");
					if (index>0){
						String itemId1 = key.substring(0,index);
						String responseId = key.substring(index+3);
						if (itemId1.equalsIgnoreCase(itemId))
							alResponseIds.add(responseId);
					}
				}
			}
		}
		return alResponseIds;
	}
	
	private static String xmlScape(String s){
		//TODO implementar aquesta funció
		s = "<![CDATA["+s+"]]>";
		return s;
	}

	private static QVSection getQVSection(ArrayList alQVSection, String sectionId){
		QVSection qvSection = null;
		if (alQVSection!=null){
			boolean bFound = false;
			for (int i=0; !bFound && i<alQVSection.size(); i++){
				QVSection s = (QVSection)alQVSection.get(i);
				if (s.getSectionId().equalsIgnoreCase(sectionId)){
					qvSection = s;
					bFound = true;
				}
			}
		}
		return qvSection;
	}

}