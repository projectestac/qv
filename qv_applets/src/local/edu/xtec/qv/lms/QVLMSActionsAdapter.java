package edu.xtec.qv.lms;

import edu.xtec.qv.lms.data.*;
import edu.xtec.qv.lms.util.TimeUtility;
import java.util.ArrayList;

public abstract class QVLMSActionsAdapter implements QVLMSActions, QVLMSDataAccessFunctions{
	
	public String getSections(int assignmentId){
		String response = "";
		String error = null;
		System.out.println("getSections("+assignmentId+")");

		String time = "00:00:00";
		//String orderAttrib = "";
		QV qv = getQV(assignmentId);
		if (qv!=null){
			ArrayList alQVSections = getQVSections(assignmentId);
			if (alQVSections!=null){
				int maxdeliver = qv.getMaxDeliver();
				int showcorrection = qv.getShowCorrection();
				for (int i=0; i<alQVSections.size(); i++){
					QVSection qvSection = (QVSection)alQVSections.get(i);
		  			time = TimeUtility.addTime(time, qvSection.getTime());
		  			response += "<section ";
		  			response += " id=\""+qvSection.getSectionId()+"\" ";
		  			response += " showcorrection=\""+showcorrection+"\"";
		  			response += " maxdeliver=\""+maxdeliver+"\"";
		  			response += " attempts=\""+qvSection.getAttempts()+"\"";
		  			response += " scores=\""+qvSection.getScores()+"\"";
		  			response += " pending_scores=\""+qvSection.getPendingScores()+"\"";
		  			response += " state=\""+qvSection.getState()+"\"";
		  			response += " time=\""+qvSection.getTime()+"\"";
		  			response += "/>";
		  		}
		  	}
			/*QVAssignment qvAssignment = getQVAssignment(assignmentId);
			if (qv.getOrderSections()==1 && qvAssignment.getSectionOrder()>0) //A6
				orderAttrib = " section_order=\""+qvAssignment.getSectionOrder()+"\""; //A6*/
		}
		String responseHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

		//responseHeader += "<bean id=\"get_sections\" assignmentid=\""+assignmentId+"\" time=\""+time+"\" "+((error!=null)?"error=\""+error+"\"":"")+orderAttrib+" >";
		responseHeader += "<bean id=\"get_sections\" assignmentid=\""+assignmentId+"\" time=\""+time+"\" "+((error!=null)?"error=\""+error+"\"":"")+" >";
		response = responseHeader + response;
		response += "</bean>";
		
		return response;
	}
	
	public String getSection(int assignmentId, String sectionId){
		String response = "";
		String error = null;
		System.out.println("getSection("+assignmentId+", "+sectionId+")");

		String responses = "";
		String scores = "";
		String pending_scores = "";
		int attempts = 0;
		int state = -1; // '-1 no_iniciat;0 iniciat; 1 lliurat;2 corregit'
		String time = "00:00:00";
		int maxdeliver = -1;
		int showcorrection = 1;

		QV qv = null;
		if (!existsQVAssignment(assignmentId)){
			error=ERROR_ASSIGNMENTID_DOES_NOT_EXIST;
		}else{
			QVSection qvSection = getQVSection(assignmentId, sectionId);
			if (qvSection!=null){
				responses = qvSection.getResponses();
			  	scores = qvSection.getScores();
			  	pending_scores = qvSection.getPendingScores();
			  	attempts = qvSection.getAttempts();
			  	state = qvSection.getState();
			  	time = qvSection.getTime();
			} else {
				responses = "";
				scores = "";
				pending_scores = "";
				attempts = 0;
				state = -1;
				time = "00:00:00";
			}
			
			qv = getQV(assignmentId);
			if (qv!=null){
			    maxdeliver = qv.getMaxDeliver();
			    showcorrection = qv.getShowCorrection();
			}  
		}
		//QVAssignment qvAssignment = getQVAssignment(assignmentId);
		response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		response += "<bean id=\"get_section\" assignmentid=\""+assignmentId+"\">";
		response += " <section ";
		response += "  id=\""+sectionId+"\" ";
		if (error!=null) response += " error=\""+error+"\" ";
		response += "  showcorrection=\""+showcorrection+"\"";
		response += "  maxdeliver=\""+maxdeliver+"\"";
		response += "  attempts=\""+attempts+"\"";
		response += "  state=\""+state+"\"";
		response += "  time=\""+time+"\"";
		/*if (qv!=null && qv.getOrderSections()==1 && qvAssignment.getSectionOrder()>0) //A6
			response += " section_order=\""+qvAssignment.getSectionOrder()+"\""; //A6
		if (qv!=null && qv.getOrderItems()==1 && qvAssignment.getItemOrder()>0) //A6
			response += " item_order=\""+qvAssignment.getItemOrder()+"\""; //A6*/
		response += " >";
		response += "  <responses><![CDATA["+responses+"]]></responses>";
		response += "  <scores><![CDATA["+scores+"]]></scores>";
		response += "  <pending_scores><![CDATA["+pending_scores+"]]></pending_scores>";
		response += " </section>";	  
		response += "</bean>";
		
		return response;		
	}
	
	public String correctSection(int assignmentId, String sectionId, String responses, String scores){
		String response = "";
		String error = null;
		QVSection qvSection = null;
		System.out.println("correctSection("+assignmentId+", "+sectionId+", "+responses+", "+scores+")");
		
		if (!existsQVAssignment(assignmentId)){
			error=ERROR_ASSIGNMENTID_DOES_NOT_EXIST;
		} else {
			qvSection = getQVSection(assignmentId, sectionId);
			if (qvSection==null){
				// Insert section
				qvSection = new QVSection();
			  	qvSection.setAssignmentId(assignmentId);
			  	qvSection.setSectionId(sectionId);
			  	if (responses.trim().length()<1){
			  		qvSection.setResponses("");
			  	} else {
			  		qvSection.setResponses(responses);
				}
				if (scores.trim().length()<1){
				  	qvSection.setScores("");
					qvSection.setPendingScores("");
			  	} else {
					qvSection.setScores(scores);
					qvSection.setPendingScores(scores);
				}
				qvSection.setAttempts(0);
			  	qvSection.setState(2);		
			  	if (!insertQVSection(qvSection)){
			  		error=ERROR_DB_UPDATE;
			  	}
			} else {
				//Update section
			  	qvSection.setState(2);
				if (responses!=null && responses.trim().length()>0){ 
					qvSection.setResponses(responses);
				}
				if (scores!=null && scores.trim().length()>0){
					qvSection.setScores(scores);
					qvSection.setPendingScores(scores);
				}
			  	if (!updateQVSection(qvSection)){
			  		error=ERROR_DB_UPDATE;
			  	}
			}
		}
			  	
		response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		response += "<bean id=\"correct_section\" assignmentid=\""+assignmentId+"\" >";
		response += " <section id=\""+sectionId+"\" ";
		if (error!=null) response += " error=\""+error+"\" ";
		response += " state=\""+qvSection.getState()+"\" />";
		response += "</bean>";
		
		return response;
	}
	
	public String deliverSection(int assignmentId, String sectionId, int sectionOrder, int itemOrder, String time, String responses, String scores){
		String response = "";
		String error = null;
		System.out.println("deliverSection("+assignmentId+", "+sectionId+", "+sectionOrder+", "+itemOrder+", "+time+", "+responses+", "+scores+")");

		QVSection qvSection = null;
		QVAssignment qvAssignment = getQVAssignment(assignmentId);
		if (qvAssignment == null){
			error = ERROR_ASSIGNMENTID_DOES_NOT_EXIST;
		} else {
			boolean modifiedAssign = false;
		    if (sectionOrder>=0){ //Establishing sectionOrder
		    	if(sectionOrder!=0 && qvAssignment.getSectionOrder()==0){ //it wasn't established before
		    		qvAssignment.setSectionOrder(sectionOrder);
		    		modifiedAssign = true;
		    	}
		    }	
		    if (itemOrder>=0){ //Establishing itemOrder
		    	if(itemOrder!=0 && qvAssignment.getItemOrder()==0){ //it wasn't established before
		    		qvAssignment.setItemOrder(itemOrder);
		    		modifiedAssign = true;
		    	}
		    }	
		    if (modifiedAssign){ //Only update if necessary
		    	if (!updateQVAssignment(qvAssignment)){
		    		error=ERROR_DB_INSERT;
		    	}
		    }
		 
		    qvSection = getQVSection(assignmentId, sectionId);
		    if (qvSection==null){
		    	// Insert section
		    	qvSection = new QVSection();
		  		qvSection.setAssignmentId(assignmentId);
		  		qvSection.setSectionId(sectionId);
		  		qvSection.setResponses(responses);
		  		qvSection.setScores(scores);
				qvSection.setPendingScores(scores);
		  		qvSection.setAttempts(1);
		  		qvSection.setState(1);	
		  		qvSection.setTime(time);
				if (!insertQVSection(qvSection)){
					error=ERROR_DB_INSERT;
				}
		  	} else{
		  		//Check max deliver < current attempts
		  		if(checkMaxDeliverNotExceeded(qvSection)){
		  			//Update section
		  			qvSection.setResponses(responses);
		  			qvSection.setScores(scores);
					qvSection.setPendingScores(scores);
		  			qvSection.setAttempts(qvSection.getAttempts()+1);
		  			qvSection.setState(1);
					qvSection.setTime(TimeUtility.addTime(qvSection.getTime(), time));
		  			if (!updateQVSection(qvSection)){
		  				error=ERROR_DB_UPDATE;
		  			}
		     	} else {
					error=ERROR_MAXDELIVER_EXCEEDED;        		
		     	}
		  	}
		}
		
		QV qv = getQV(assignmentId);
		/*if (qv!=null) {
			maxdeliver = qv.getMaxDeliver();
			showcorrection = qv.getShowCorrection();
		} */
		
		response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		response += "<bean id=\"deliver_section\" assignmentid=\""+assignmentId+"\" >";
		response += " <section id=\""+sectionId+"\" ";
		if (error!=null) response += " error=\""+error+"\" ";
		response += " attempts=\""+qvSection.getAttempts()+"\" ";
		response += " maxdeliver=\""+qv.getMaxDeliver()+"\" ";
		response += " showcorrection=\""+qv.getShowCorrection()+"\" ";
		response += " time=\""+qvSection.getTime()+"\" ";
		response += " state=\""+qvSection.getState()+"\" />";
		response += "</bean>";
		
		return response;
	}
	
	public String saveSection(int assignmentId, String sectionId, int sectionOrder, int itemOrder, String time, String responses){
		String response = "";
		String error = null;
		QVSection qvSection = null;
		System.out.println("saveSection("+assignmentId+", "+sectionId+", "+sectionOrder+", "+itemOrder+", "+time+", "+responses+")");

		QVAssignment qvAssignment = getQVAssignment(assignmentId);
		if (qvAssignment == null){
			error = ERROR_ASSIGNMENTID_DOES_NOT_EXIST;
		} else {
			boolean modifiedAssign = false;
		    if (sectionOrder>=0){ //Establishing sectionOrder
		    	if(sectionOrder!=0 && qvAssignment.getSectionOrder()==0){ //it wasn't established before
		    		qvAssignment.setSectionOrder(sectionOrder);
		    		modifiedAssign = true;
		    	}
		    }	
		    if (itemOrder>=0){ //Establishing itemOrder
		    	if(itemOrder!=0 && qvAssignment.getItemOrder()==0){ //it wasn't established before
		    		qvAssignment.setItemOrder(itemOrder);
		    		modifiedAssign = true;
		    	}
		    }	
		    if (modifiedAssign){ //Only update if necessary
		    	if (!updateQVAssignment(qvAssignment)){
		    		error=ERROR_DB_INSERT;
		    	}
		    }
		 
		    qvSection = getQVSection(assignmentId, sectionId);
		    if (qvSection==null){
		  		// Insert section
		    	qvSection = new QVSection();
		  		qvSection.setAssignmentId(assignmentId);
		  		qvSection.setSectionId(sectionId);
		  		qvSection.setResponses(responses);
		  		qvSection.setState(0);
				qvSection.setTime(time); 
		  		if (!insertQVSection(qvSection)){
		  			error=ERROR_DB_INSERT;
		  		}
		  	} else{
		  		if(checkMaxDeliverNotExceeded(qvSection)){  	
		  			//Update section
		    		qvSection.setResponses(responses);
		    		qvSection.setState(0);
		    		qvSection.setPendingScores(qvSection.getScores());
		    		qvSection.setTime(TimeUtility.addTime(qvSection.getTime(), time)); 

		    		if (!updateQVSection(qvSection)){
		    			error=ERROR_DB_UPDATE;
		    		}
		     	} else {
						error=ERROR_MAXDELIVER_EXCEEDED;        		
		     	}
		  	}  
		}
		  	
		response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		response += "<bean id=\"save_section\" assignmentid=\""+assignmentId+"\" >";
		response += " <section id=\""+sectionId+"\" ";
		if (error!=null) response += " error=\""+error+"\" ";
		response += " state=\""+((qvSection!=null)?qvSection.getState()+"":"")+"\" />";	
		response += "</bean>";
		
		return response;
	}
	
	public String saveSectionTeacher(int assignmentId, String sectionId, String responses, String scores){
		String response = "";
		String error = null;
		QVSection qvSection = null;
		System.out.println("saveSectionTeacher("+assignmentId+", "+sectionId+", "+responses+", "+scores+")");

		QVAssignment qvAssignment = getQVAssignment(assignmentId);
		if (qvAssignment == null){
			error=ERROR_ASSIGNMENTID_DOES_NOT_EXIST;
		} else {
			qvSection = getQVSection(assignmentId, sectionId);
			if (qvSection == null){
				System.out.println("No existia el full");
		  		// Insert section
				qvSection = new QVSection();
		  		qvSection.setAssignmentId(assignmentId);
		  		qvSection.setSectionId(sectionId);
		  		qvSection.setResponses(responses);
		  		qvSection.setPendingScores(scores);
				System.out.println("Poso els pending scores:"+scores);
		  		qvSection.setTime("00:00:00"); 
		  		if (!insertQVSection(qvSection)){
		  			error=ERROR_DB_INSERT;
				} 
				else
					System.out.println("Creat el full");
					
		  	} else{
				System.out.println("El full ja existia");
		  		//Update section
		  		qvSection.setResponses(responses);
				qvSection.setPendingScores(scores);
				System.out.println("Poso els pending scores:"+scores);
				if (!updateQVSection(qvSection)){
					error=ERROR_DB_UPDATE;
				}
		  		else
					System.out.println("Modificat el full");
			}  
		}
		  	
		response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		response += "<bean id=\"save_section_teacher\" assignmentid=\""+assignmentId+"\" >";
		response += " <section id=\""+sectionId+"\" ";
		if (error!=null) response += " error=\""+error+"\" ";
		response += " state=\""+((qvSection!=null)?qvSection.getState()+"":"")+"\" />";	
		response += "</bean>";
		
		return response;
	}

	public String saveTime(int assignmentId, String sectionId, String time){
		String response = "";
		String error = null;
		QVSection qvSection = null;
		System.out.println("saveTime("+assignmentId+", "+sectionId+", "+time+")");

		if (!existsQVAssignment(assignmentId)){
			error=ERROR_ASSIGNMENTID_DOES_NOT_EXIST;
		} else {
			qvSection = getQVSection(assignmentId, sectionId);
		    if (qvSection == null){
		  		// Insert section
		    	qvSection = new QVSection();
		    	qvSection.setAssignmentId(assignmentId);
		    	qvSection.setSectionId(sectionId);
		    	qvSection.setResponses("");
		    	qvSection.setScores("");
		    	qvSection.setAttempts(0);
		    	qvSection.setState(0);
		    	qvSection.setTime(time);		
		    	if (!insertQVSection(qvSection)){
  		  			error=ERROR_DB_UPDATE;
		    	}
		    } else {
		    	//Update section
		    	qvSection.setTime(TimeUtility.addTime(qvSection.getTime(), time));
		    	if (!updateQVSection(qvSection)){
		    		error=ERROR_DB_UPDATE;
		    	}
		    }
		}

		// Response		
		response ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		response += "<bean id=\"save_time\" assignmentid=\""+assignmentId+"\" >";
		response += " <section id=\""+sectionId+"\" ";
		if (error!=null) response += " error=\""+error+"\" ";
		response += " time=\""+((qvSection!=null)?qvSection.getTime():"")+"\" />";
		response += "</bean>";
		
		return response;
	}
	

	public String addMessage(int assignmentId, String sectionId, String itemId, String userId, String message){
		//TODO Sense implementar perquè queda fóra de l'àmbit d'aquest projecte
		String response = "";
		System.out.println("addMessage("+assignmentId+", "+sectionId+", "+itemId+", "+userId+", "+message+")");

		return response;
	}
	
	public String getMessages(int assignmentId, String sectionId){
		//TODO Sense implementar perquè queda fóra de l'àmbit d'aquest projecte
		System.out.println("getMessages("+assignmentId+", "+sectionId+")");

		String response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		response += "<bean id=\"get_messages\" assignmentid=\""+assignmentId+"\" sectionid=\""+sectionId+"\" userid=\"1\">";
	 	response += "</bean>";
		return response;
	}
	
	protected boolean checkMaxDeliverNotExceeded(QVSection qvSection){
		boolean not_exceeded = true;
		//Check max deliver < current attempts
		QV qv = getQV(qvSection.getAssignmentId());
		if (qv!=null){
			not_exceeded=(qv.getMaxDeliver()<0 || qvSection.getAttempts() < qv.getMaxDeliver());
		}
		return not_exceeded;
	}


}