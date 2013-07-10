package edu.xtec.qv.lms.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import edu.xtec.qv.lms.data.*;
import edu.xtec.qv.lms.QVLMSActionsAdapter;
import edu.xtec.qv.lms.util.TimeUtility;

public class QVSectionSummary{
	public float score = 0;
	public float pendingScore = 0;
	public int attempts = 0;
	
	public QVSectionSummary(){
	}
	
	public static QVSectionSummary getSectionSummary(QVSection qvSection){
		QVSectionSummary ss = new QVSectionSummary();

		if (qvSection!=null){
			int start = qvSection.getScores().indexOf(qvSection.getSectionId()+"_score=");
			if (start>=0){
				start += (qvSection.getSectionId()+"_score=").length();
				int length = ((qvSection.getScores().substring(start)).indexOf("#"));////
				try{
					ss.score = Float.parseFloat(qvSection.getScores().substring(start, start+length));
				} catch (Exception ex){
					System.out.println("Error en el format de la puntuació: "+qvSection.getScores().substring(start, start+length));
					ex.printStackTrace(System.err);
				}
			}
			// Pending Score
			int start2 = qvSection.getPendingScores().indexOf(qvSection.getSectionId()+"_score=");
			if (start2>=0){
				start2 += (qvSection.getSectionId()+"_score=").length();
				int length2 = ((qvSection.getPendingScores().substring(start2)).indexOf("#"));////
				try{
					ss.pendingScore = Float.parseFloat(qvSection.getPendingScores().substring(start2, start2+length2));
				} catch (Exception ex){
					System.out.println("Error en el format de la puntuació: "+qvSection.getPendingScores().substring(start2, start2+length2));
					ex.printStackTrace(System.err);
				}
			}
			// Attempts
			ss.attempts = qvSection.getAttempts();
		}
		return ss;
	}
}
