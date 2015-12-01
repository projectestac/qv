package edu.xtec.qv.servlet.util;

/*
 * QTITransformed.java
 *
 * Created on 6 / maig / 2003, 13:25
 */

/**
 *
 * @author  allastar
 */

import javax.xml.transform.Source;

public class QTITransformed implements java.io.Serializable{
    
    String sData;
    java.util.Vector vPositions;
    int iNumSection;
    
    protected static final String sStartParam="#par#?";
    protected static final String sEndParam="?";
    
    /** Creates a new instance of QTITransformed */
    public QTITransformed(int iNumSection, String sData) {
        this.iNumSection=iNumSection;
        //this.data=data;
        this.sData=sData;
        //hmPositions=new java.util.HashMap();
        vPositions=new java.util.Vector();
        parseData();
    }
    
    private void parseData(){
        String s=sData;
        boolean bEnd=false;
        int currentIndex=0;
        while (!bEnd){
            int i=s.indexOf(sStartParam,currentIndex);
            if (i>=0){
                currentIndex=s.indexOf(sEndParam,(i+sStartParam.length()));
                String sParamName=s.substring(i+sStartParam.length(),currentIndex);
                //System.out.println("paramName:"+sParamName+"<--");
                Object[] elem=new Object[2];
                elem[0]=new Integer(i);
                elem[1]=sParamName;
                vPositions.add(elem);
            }
            else bEnd=true;
        }
    }
    
    public static QTITransformed createSection(int iNumSection, int iTotalSections, int iSectionLimit, int iMaxLimit, Source xmlSource, Source xsl, String sContext){
        QTITransformed transformed=null;
        try{
            QTIToHTMLTransformer tr=new QTIToHTMLTransformer();
            tr.setStyleSheet(xsl,sContext);
            
            java.io.ByteArrayOutputStream baos=new java.io.ByteArrayOutputStream();
            tr.transform(555,sStartParam+"sUserId"+sEndParam, iNumSection, iTotalSections, iSectionLimit, iMaxLimit, xmlSource, new java.io.PrintWriter(baos),sStartParam+"feedback"+sEndParam,sStartParam+"interaction"+sEndParam,sStartParam+"initial"+sEndParam,sStartParam+"view"+sEndParam,sStartParam+"serverUrl"+sEndParam, sStartParam+"returnPage"+sEndParam,null,null,sStartParam+"qtiUrl"+sEndParam,sStartParam+"quadernXSL"+sEndParam,sStartParam+"needResponse"+sEndParam,sStartParam+"notebookTime"+sEndParam,sStartParam+"sectionTime"+sEndParam,sStartParam+"es_correccio"+sEndParam,sStartParam+"canCorrect"+sEndParam,sStartParam+"noModify"+sEndParam,sStartParam+"scoring"+sEndParam,sStartParam+"date"+sEndParam,sStartParam+"writingEnabled"+sEndParam,sStartParam+"estat_lliurament"+sEndParam,sStartParam+"color"+sEndParam);
            transformed=new QTITransformed(iNumSection,new String(baos.toByteArray())); //baos.toString()?
            
        }
        catch(Exception e){
            e.printStackTrace(System.out);
        }
        return transformed;
    }
    
    public void transform(java.io.PrintWriter out, int userId, String sUserId, String sFeedback, String sInteraction, String sInitial, String sView, String sServerUrl, String sReturnPage,String qtiUrl, String sQuadernXSL, boolean bNeedResponse, boolean isCorrection, boolean canCorrect, boolean bNoModify, String sScoringRepresentation, String date, boolean bWritingEnabled, String sEstat, String sColor, int iSectionLimit){
        java.util.HashMap hmParams=new java.util.HashMap();
        hmParams.put("sUserId",sUserId);
        hmParams.put("feedback",sFeedback);
        hmParams.put("interaction",sInteraction);
        hmParams.put("initial",sInitial);
        hmParams.put("view",sView);
        hmParams.put("serverUrl",sServerUrl);
        hmParams.put("returnPage",sReturnPage);
        hmParams.put("qtiUrl",qtiUrl);
		hmParams.put("quadernXSL",sQuadernXSL);
        hmParams.put("needResponse",""+bNeedResponse);
        hmParams.put("es_correccio",""+isCorrection);
        hmParams.put("canCorrect",""+canCorrect);
        hmParams.put("noModify",""+bNoModify);
        hmParams.put("scoring",sScoringRepresentation);
        hmParams.put("date",date);
        hmParams.put("writingEnabled",""+bWritingEnabled);
		hmParams.put("estat_lliurament",sEstat);
		hmParams.put("color",sColor);
		hmParams.put("section_limit",""+iSectionLimit);
        //System.out.println("NEED_RESPONSE?"+bNeedResponse);
        doTransform(out,hmParams);
    }
    
    private void doTransform(java.io.PrintWriter out, java.util.HashMap hmParams){
        int currentPos=0;
        java.util.Enumeration e=vPositions.elements();
        try{
            while (e.hasMoreElements()){
                Object[] elem=(Object[])e.nextElement();
                int pos=((Integer)elem[0]).intValue();
                String param=elem[1].toString();
                out.write(sData,currentPos,pos-currentPos);
                if (hmParams.containsKey(param)){
                    Object o=hmParams.get(param);
                    out.write(o!=null?o.toString():"");
                }
                currentPos=pos+param.length()+sStartParam.length()+sEndParam.length();
            }
            if (currentPos<sData.length()){
                out.write(sData,currentPos,sData.length()-currentPos);
            }
        }
        catch (Exception ex){
            ex.printStackTrace(System.out);
            
            //QVServlet.err.println("Excepcio: "+ex);
            //ex.printStackTrace(QVServlet.err);             
        }
    }
    
    /*public static void main(String[] args){
        try{
            String sContext="C:\\Albert\\pfc\\bak\\qv_nous_packages";
            Source xmlSource=new javax.xml.transform.stream.StreamSource(new java.io.FileInputStream("C:\\Albert\\pfc\\quaderns\\medi_030324.xml"));
            xmlSource.setSystemId("file:///C:\\Albert\\pfc\\quaderns\\medi_030324.xml");
            Source xsl=new javax.xml.transform.stream.StreamSource(new java.io.FileInputStream("C:\\Albert\\pfc\\bak\\qv_nous_packages\\recursos\\qti_prof.xsl"));
            QTITransformed qtiTr=QTITransformed.createSection(1,31, xmlSource, xsl, sContext);
            
            java.io.OutputStream out=new java.io.FileOutputStream("c:\\out_medi3.html");
            java.io.PrintWriter pw=new java.io.PrintWriter(out);
            qtiTr.transform(pw, 1, "--usuari--", "--feedback--", "--interaccio--", "--inicial--", "--view--", "--ServerUrl--", "--ReturnPage--","--qtiUrl--");
            pw.flush();
            pw.close();
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            e.printStackTrace(System.out);
        }
    }*/
    
}
