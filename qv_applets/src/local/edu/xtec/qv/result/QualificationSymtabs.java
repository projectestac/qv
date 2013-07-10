package edu.xtec.qv.result;

/*
 * QualificationSymtabs.java
 *
 * Created on 18 / juny / 2003, 13:36
 */

import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author  allastar
 */
public class QualificationSymtabs {
    
    HashMap hmSectionSymtabs;
    Vector vAssessmentSymtabs;
    
    /** Creates a new instance of QualificationSymtabs */
    public QualificationSymtabs() {
        hmSectionSymtabs=new HashMap();
        vAssessmentSymtabs=new Vector();
    }
    
    public void addSectionSymtab(String sSectionName, Vector vItemSymtabs){
        hmSectionSymtabs.put(sSectionName,vItemSymtabs);
    }
    
    public void addAssessmentSymtab(String sSectionName, QTISymtab symSection){
        Object[] o=new Object[]{sSectionName,symSection};
        vAssessmentSymtabs.add(o);
    }
    
    public Vector getSectionSymtab(String sSectionName){
        Object o=hmSectionSymtabs.get(sSectionName);
        if (o!=null) return (Vector)o;
        else return null;
    }
    
    public void writeAssessmentSymtabs(java.io.PrintWriter pw){
        //pw.println("hm.size:"+hmSectionSymtabs.size()+"-");
        pw.print("<TABLE>");
        java.util.Enumeration e=vAssessmentSymtabs.elements();
        while (e.hasMoreElements()){
            Object[] o=(Object[])e.nextElement();
            Object key=o[0];
            QTISymtab symSection=(QTISymtab)o[1];
            writeSectionSymtab(key.toString(),symSection,pw);
            pw.print("<TR><TD COLSPAN=\"2\">");
            writeItemSymtabs(key.toString(),pw);
            pw.print("</TD></TR>");
        }
        pw.print("</TABLE>");
    }
    
    public void writeSectionSymtab(String sAssessment, QTISymtab symSection, java.io.PrintWriter pw){
        pw.print("<TR>");
        String score=symSection.getValue("SCORE").toString();
        pw.print("<TD>"+sAssessment+"</TD><TD>"+score+"</TD><TD>");
        pw.print("</TR>");
    }
    
    public void writeItemSymtabs(String sSectionName, java.io.PrintWriter pw){
        pw.print("<B>rep:"+getSectionScoringRepresentation(/*sSectionName*/)+"</B>");
        Vector v=(Vector)hmSectionSymtabs.get(sSectionName);
        java.util.Enumeration e=v.elements();
        pw.print("<TABLE>");
        while (e.hasMoreElements()){
            QTISymtab symItem=(QTISymtab)e.nextElement();
            writeItemSymtab(sSectionName,symItem,pw);
        }
        pw.print("</TABLE>");
    }
    
    public void writeItemSymtab(String sSection, QTISymtab symItem, java.io.PrintWriter pw){
        pw.print("<TR>");
        String id=symItem.getValue(".IDENT").toString();
        String correct=symItem.getValue("CORRECT").toString();
        String score=symItem.getValue("SCORE").toString();
        pw.print("<TD>"+id+"</TD><TD>"+correct+"</TD><TD>"+score+"</TD><TD>");
        pw.print("</TR>");
    }
    
    public void writeAssessmentSymtab(String sAssessment, QTISymtab symAssessment, java.io.PrintWriter pw){
        pw.print("<TABLE><TR>");
        String score=symAssessment.getValue("SCORE").toString();
        pw.print("<TD>"+sAssessment+"</TD><TD>"+score+"</TD><TD>");
        pw.print("</TR></TABLE>");
    }
    
    public String getSectionScoringRepresentation(/*String sSectionName*/){
        /* Retorna en forma d'String una representació de les puntuacions i
         correctesa dels items del full del que s'acaba de calcular la
         puntuació. */
        StringBuffer sb=new StringBuffer();

        java.util.Enumeration e=vAssessmentSymtabs.elements();
        if (e.hasMoreElements()){ //La puntuació del full
            Object[] o=(Object[])e.nextElement();
            Object id=o[0];
            QTISymtab symSection=(QTISymtab)o[1];
            String score=symSection.getValue("SCORE").toString();
            sb.append(id).append('_').append("score").append('=').append(score).append('#');
        }
        
        java.util.Iterator it=hmSectionSymtabs.values().iterator();
        if (it.hasNext()){ //La puntuació, correcció dels items
            Vector v=(Vector)it.next();
            if (v!=null){
                e=v.elements();
                while (e.hasMoreElements()){
                    QTISymtab symItem=(QTISymtab)e.nextElement();
                    String id=symItem.getValue(".IDENT").toString();
					String correct="False";
                    if (symItem.getValue("CORRECT")!=null){
						correct=symItem.getValue("CORRECT").toString();
                    }
					String score="0";
                    if (symItem.getValue("SCORE")!=null){
						score=symItem.getValue("SCORE").toString();
                    }
                    sb.append(id).append('_').append("correct").append('=').append(correct).append('#');
                    sb.append(id).append('_').append("score").append('=').append(score).append('#');
                }
            }
        }
        return sb.toString();
    }
    
}
