package edu.xtec.qv.lms.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Iterator;
import edu.xtec.qv.lms.data.*;
import edu.xtec.qv.lms.QVLMSActionsAdapter;
import edu.xtec.qv.lms.util.TimeUtility;

import java.io.StringReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;

import edu.xtec.qv.lms.util.XMLUtility;

public class ReportParser{
	
	public ReportParser(){
	}

	public static void parseReport(InputStream isReport, QVUserInfo qvUserInfo, QVAssignment qvAssignment, ArrayList alQVSection) throws Exception{
		Document docReport = XMLUtility.getXMLDocument(isReport);
		parseReport(docReport, qvUserInfo, qvAssignment, alQVSection);
	}
	
	public static void parseReport(String sReport, QVUserInfo qvUserInfo, QVAssignment qvAssignment, ArrayList alQVSection) throws Exception{
		System.out.println("parseReport("+sReport+")");
		Document docReport = XMLUtility.getXMLDocument(sReport);
		parseReport(docReport, qvUserInfo, qvAssignment, alQVSection);
	}
	
	public static void parseReport(Document docReport, QVUserInfo qvUserInfo, QVAssignment qvAssignment, ArrayList alQVSection) throws Exception{
		Element eQTIResultReport = docReport.getRootElement();
		Element eResult = eQTIResultReport.getChild("result");
		if (eResult!=null){
			Element eExtensionResult = eResult.getChild("extension_result");
			if (eExtensionResult!=null){
				String sectionOrder = eExtensionResult.getChild("section_order").getText();
				String itemOrder = eExtensionResult.getChild("item_order").getText();
				if (sectionOrder!=null && sectionOrder.trim().length()>0){
					try{
						qvAssignment.setSectionOrder(Integer.parseInt(sectionOrder));
					} catch (Exception ex){
						System.out.println("Error en el format de sectionOrder:"+sectionOrder);
					}
				}
				if (itemOrder!=null && itemOrder.trim().length()>0){
					try{
						qvAssignment.setItemOrder(Integer.parseInt(itemOrder));
					} catch (Exception ex){
						System.out.println("Error en el format de itemOrder:"+itemOrder);
					}
				}
			}
			Element eContext = eResult.getChild("context");
			if (eContext!=null){
				Iterator itGenericIdentifier = eContext.getDescendants(new ElementFilter("generic_identifier"));	
				while (itGenericIdentifier.hasNext()){
					Element eGenericIdentifier = (Element)itGenericIdentifier.next();
					String typeLabel = eGenericIdentifier.getChild("type_label").getText();;
					String identifierString = eGenericIdentifier.getChild("identifier_string").getText();;
					if (typeLabel!=null){
						if (typeLabel.equalsIgnoreCase("Student Number")){
							qvUserInfo.setUserId(identifierString);
							System.out.println("Student Number:"+identifierString);
						} else if (typeLabel.equalsIgnoreCase("Student Group")){
							qvUserInfo.setGroupId(identifierString);
							System.out.println("Student Group:"+identifierString);
						} else if (typeLabel.equalsIgnoreCase("Student Group Name")){
							qvUserInfo.setGroupName(identifierString);
							System.out.println("Student Group Name:"+identifierString);
						} else if (typeLabel.equalsIgnoreCase("Student IT")){
							qvUserInfo.setUserIT(identifierString);
							System.out.println("Student IT:"+identifierString);
						} else if (typeLabel.equalsIgnoreCase("Student NE")){
							qvUserInfo.setUserNE(identifierString);
							System.out.println("Student NE:"+identifierString);
						}
					}
				}
			}
		}
		
		Iterator itSection = eQTIResultReport.getDescendants(new ElementFilter("section_result"));
		while (itSection.hasNext()){
			Element eSection = (Element)itSection.next();
			String sectionId = eSection.getAttributeValue("ident_ref");
			//System.out.println("sectionId="+sectionId);
			QVSection qvSection = new QVSection();
			qvSection.setSectionId(sectionId);
			Element eExtensionSectionResult = eSection.getChild("extension_section_result");
			if (eExtensionSectionResult!=null){
				String responses = eExtensionSectionResult.getChild("responses").getText();
				String scores = eExtensionSectionResult.getChild("scores").getText();
				String pendingScores = eExtensionSectionResult.getChild("pending_scores").getText();
				String attempts = eExtensionSectionResult.getChild("attempts").getText();
				String state = eExtensionSectionResult.getChild("state").getText();
				String time = eExtensionSectionResult.getChild("time").getText();
				qvSection.setResponses(responses);
				qvSection.setScores(scores);
				qvSection.setPendingScores(pendingScores);
				qvSection.setTime(time);
				if (attempts!=null && attempts.trim().length()>0){
					try{
						qvSection.setAttempts(Integer.parseInt(attempts));
					} catch (Exception ex){
						System.out.println("Error en el format d'attempts:"+attempts);
					}
				}
				if (state!=null && state.trim().length()>0){
					try{
						qvSection.setState(Integer.parseInt(state));
					} catch (Exception ex){
						System.out.println("Error en el format d'state:"+state);
					}
				}
				/*System.out.println("responses:"+responses);
				System.out.println("scores:"+scores);
				System.out.println("pending_scores:"+pendingScores);
				System.out.println("attempts:"+attempts);
				System.out.println("state:"+state);
				System.out.println("time:"+time);*/
			}
			alQVSection.add(qvSection);
		}
	}
	
}