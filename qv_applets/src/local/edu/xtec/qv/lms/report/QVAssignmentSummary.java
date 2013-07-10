package edu.xtec.qv.lms.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import edu.xtec.qv.lms.data.*;
import edu.xtec.qv.lms.QVLMSActionsAdapter;
import edu.xtec.qv.lms.util.TimeUtility;

public class QVAssignmentSummary{
		public int id = 0;
		public int sectionOrder = 0;
		public int itemOrder = 0;
		public float score = 0;
		public float pendingScore = 0;
		public int attempts = 0;
		public HashMap states = new HashMap();
		public String time = "00:00:00";
		
		public QVAssignmentSummary(){
		}
		
		public static QVAssignmentSummary getAssignmentSummary(QVAssignment qvAssignment, ArrayList alQVSection){
			QVAssignmentSummary as = new QVAssignmentSummary();
			HashMap hmStates = new HashMap();
			float score = 0;
			float pendingScore = 0;
			int attempts = 0;
			String time = "00:00:00";
			
			if (alQVSection!=null){
				for (int i=0;i<alQVSection.size();i++){
					QVSection qvSection = (QVSection)alQVSection.get(i);
					QVSectionSummary ss = QVSectionSummary.getSectionSummary(qvSection);
					score += ss.score;
					if (ss.pendingScore>=0)
						pendingScore += ss.pendingScore;
					time = TimeUtility.addTime(time, qvSection.getTime());
					if (ss.attempts > attempts){
							attempts = ss.attempts;
					}
					// States
					String stateId = "state_"+qvSection.getState();
					if (!hmStates.containsKey(stateId)) hmStates.put(stateId, new Integer(0));
					hmStates.put(stateId, new Integer(((Integer)hmStates.get(stateId)).intValue()+1));
				}
				// State
				String state = "-1";
				if (hmStates.containsKey("state_2") && alQVSection.size()==((Integer)hmStates.get("state_2")).intValue()) state = "2";
				else if (hmStates.containsKey("state_1") && alQVSection.size()==((Integer)hmStates.get("state_1")).intValue()) state = "1";
				else if (hmStates.containsKey("state_1") && ((Integer)hmStates.get("state_1")).intValue()>0) state = "1-";
				else if (hmStates.containsKey("state_2") && ((Integer)hmStates.get("state_2")).intValue()>0) state = "2-";
				else if (hmStates.containsKey("state_0") && ((Integer)hmStates.get("state_0")).intValue()>0) state = "0";
				hmStates.put("state", state);
			}
			as.id = qvAssignment.getId();
			as.sectionOrder = qvAssignment.getSectionOrder();
			as.itemOrder = qvAssignment.getItemOrder();
			as.score = score;
			as.pendingScore = pendingScore;
			as.attempts = attempts;
			as.states = hmStates;
			as.time = time;
			return as;
		}
		
		public String getState(){
			if (states!=null && states.containsKey("state"))
				return states.get("state").toString();
			else
				return null;
		}
	}
