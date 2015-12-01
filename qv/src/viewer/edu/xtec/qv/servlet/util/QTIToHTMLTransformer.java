package edu.xtec.qv.servlet.util;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;

import edu.xtec.qv.util.QuadernConfig;

public class QTIToHTMLTransformer implements java.io.Serializable{
    
    private Transformer tr=null;
    
    ////public void setStyleSheet(Source xsl, javax.servlet.ServletContext sc) throws Exception{
    public void setStyleSheet(Source xsl, String context) throws Exception{
        //System.out.println("AAA");
        //try{
        ////final javax.servlet.ServletContext scf=sc;
        final String contextf=context;
        //org.apache.xpath.XPath xp;
        //TransformerFactory tf=TransformerFactory.newInstance();
        TransformerFactory tf = new org.apache.xalan.processor.TransformerFactoryImpl();
        
        tf.setURIResolver(new URIResolver(){
            public Source resolve(String href, String base) throws TransformerException{
                ////System.out.println("resolve href:"+href+" base:"+base);
                
                ////System.out.println("url:"+sUrl);
                java.io.InputStream is=null;
                try{
                    if (contextf!=null){
                        String sUrl=QuadernConfig.transformURLValue(href,contextf);
                        is=new java.net.URL(sUrl).openStream();
                    }
                    else{
                        is=getClass().getClassLoader().getResourceAsStream(href);
                    }
                }
                catch (Exception e){
                }
                if (is!=null) return new javax.xml.transform.stream.StreamSource(is);
                else return null;
            }
        });
        tr=tf.newTransformer(xsl);
        /////Templates templates = tf.newTemplates(xsl);//new StreamSource(xslFileName));
        /////tr= templates.newTransformer();
        

    }
    
    public void transform(Source xmlSource, java.io.OutputStream out) throws javax.xml.transform.TransformerException{
        if (tr!=null){
            //try{
            if (xmlSource.getSystemId()==null){
                System.out.println("Atenció!! Hauries d'establir un SystemId al xmlSorce per tal que després es faci la correcció.");
                //QVServlet.err.println("Atenció!! Hauries d'establir un SystemId al xmlSorce per tal que després es faci la correcció.");
                tr.clearParameters();
            }
            else tr.setParameter("QTIUrl",xmlSource.getSystemId());
            //tr.setParameter("displayfeedback","IMS_V01_I_mixd_simr_001cIncorrect#abc");
            Result output=new javax.xml.transform.stream.StreamResult(new java.io.OutputStreamWriter(out));
            
            tr.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
            //System.out.println("Establert ENCODING2");
            
            tr.transform(xmlSource,output);
            System.out.println("Transform fet");
                        /*}
                        catch (Exception e){
                                System.out.println("Excepció:"+e);
                                e.printStackTrace(System.out);
                        }*/
        }
        else{
            System.out.println("Error: S'ha de cridar a setStyleSheet(Sorce xsl) abans de transform.");
            //QVServlet.err.println("Error: S'ha de cridar a setStyleSheet(Sorce xsl) abans de transform.");
        }
    }
    
    /*public void transform(int iUserId, Object sUserId, Source xmlSource, java.io.PrintWriter out, String feedbackParameter, String sInteraction, String initialSelection, String sView, String sServerUrl, String sReturnPage, String sBackGroundImage, String[] clicInfo){
    //public void transform(int iUserId, Object sUserId, Source xmlSource, java.io.OutputStream out, String feedbackParameter, String sInteraction, String initialSelection, String sView, String sServerUrl, String sReturnPage, String sBackGroundImage, String[] clicInfo){
        transform(iUserId,sUserId,1,1,xmlSource,out,feedbackParameter,sInteraction,initialSelection,sView,sServerUrl,sReturnPage, sBackGroundImage, clicInfo,"","true","true");
    }*/
	public void transform(int iUserId, Object sUserId, int iNumFull, int iTotalFulls, int iSectionLimit, int iMaxLimit, Source xmlSource, java.io.PrintWriter out, String feedbackParameter, String sInteraction, String initialSelection, String sView, String sServerUrl, String sReturnPage, String sBackGroundImage, String[] clicInfo, String sQTIUrl, String sQuadernXSL, String sNeedResponse, String notebookTime, String sectionTime, String isCorrection, String canCorrect, String noModify, String sScoringRepresentation, String date, String sWritingEnabled, String sEstat, String sColor){
		transform(iUserId, sUserId, iNumFull, iTotalFulls, iSectionLimit, iMaxLimit, xmlSource, out, feedbackParameter,  sInteraction, initialSelection, sView, sServerUrl, sReturnPage, sBackGroundImage, clicInfo, sQTIUrl, sQuadernXSL, sNeedResponse, notebookTime, sectionTime, isCorrection, canCorrect, noModify, sScoringRepresentation, date, sWritingEnabled, sEstat, sColor, null);
	}
    
    public void transform(int iUserId, Object sUserId, int iNumFull, int iTotalFulls, int iSectionLimit, int iMaxLimit, Source xmlSource, java.io.PrintWriter out, String feedbackParameter, String sInteraction, String initialSelection, String sView, String sServerUrl, String sReturnPage, String sBackGroundImage, String[] clicInfo, String sQTIUrl, String sQuadernXSL, String sNeedResponse, String notebookTime, String sectionTime, String isCorrection, String canCorrect, String noModify, String sScoringRepresentation, String date, String sWritingEnabled, String sEstat, String sColor, String sAssignacioId){
	//public void transform(int iUserId, Object sUserId, int iNumFull, int iTotalFulls, Source xmlSource, java.io.PrintWriter out, String feedbackParameter, String sInteraction, String initialSelection, String sView, String sServerUrl, String sReturnPage, String sBackGroundImage, String[] clicInfo, String sQTIUrl, String sNeedResponse, String notebookTime, String sectionTime, String isCorrection, String canCorrect, String noModify, String sScoringRepresentation, String date, String sWritingEnabled){
    //public void transform(int iUserId, Object sUserId, int iNumFull, int iTotalFulls, Source xmlSource, java.io.OutputStream out, String feedbackParameter, String sInteraction, String initialSelection, String sView, String sServerUrl, String sReturnPage, String sBackGroundImage, String[] clicInfo, String sQTIUrl){
        if (tr!=null){
            try{
                tr.clearParameters();
                if (xmlSource.getSystemId()==null){
                    System.out.println("Atenció!! Hauries d'establir un SystemId al xmlSorce per tal que després es faci la correcció.");
                    //QVServlet.err.println("Atenció!! Hauries d'establir un SystemId al xmlSorce per tal que després es faci la correcció.");
                    tr.clearParameters();
                }
                //else if (xmlSource.getSystemId()!=null) tr.setParameter("QTIUrl",sQTIUrl); //xmlSource.getSystemId());
                if (sQTIUrl!=null) tr.setParameter("QTIUrl",sQTIUrl.replace('\\','/'));
				if (sQuadernXSL!=null) tr.setParameter("quadernXSL", sQuadernXSL);
                tr.setParameter("userId",iUserId+"");
                if (sUserId!=null) tr.setParameter("sUserId",sUserId.toString());
                if (feedbackParameter!=null) tr.setParameter("displayfeedback",feedbackParameter);
                if (sInteraction!=null) tr.setParameter("displayinteraction",sInteraction);
                if (initialSelection!=null) tr.setParameter("initialselection",initialSelection);
                if (sView!=null) tr.setParameter("view",sView);
                if (sServerUrl!=null) tr.setParameter("serverUrl",sServerUrl);
                tr.setParameter("section_number",""+iNumFull);
                tr.setParameter("section_max_number",""+iTotalFulls);
				tr.setParameter("section_limit", ""+iSectionLimit);
				tr.setParameter("section_max_limit", ""+iMaxLimit);
                if (sReturnPage!=null) tr.setParameter("returnPage",sReturnPage);
                if (sBackGroundImage!=null) tr.setParameter("bgimage",sBackGroundImage);
                tr.setParameter("needResponse",sNeedResponse);
                if (isCorrection!=null) tr.setParameter("es_correccio",isCorrection);//
                if (canCorrect!=null) tr.setParameter("canCorrect",canCorrect);//
                if (isCorrection!=null) tr.setParameter("noModify",noModify);//
                if (sectionTime!=null) tr.setParameter("sectionTime",sectionTime);
                if (notebookTime!=null) tr.setParameter("notebookTime",notebookTime);
                if (sScoringRepresentation!=null) tr.setParameter("scoring",sScoringRepresentation);
                if (date!=null) tr.setParameter("date",date);
                if (sWritingEnabled!=null) tr.setParameter("writingEnabled",sWritingEnabled);
                if (sEstat!=null) tr.setParameter("estat_lliurament", sEstat);
				if (sColor!=null) tr.setParameter("color", sColor);
				if (sAssignacioId!=null) tr.setParameter("assignacioId", sAssignacioId);
				
                
                if (clicInfo!=null && clicInfo.length==5){
                    //System.out.println("CLIC INFO!!!!!");
                    if (clicInfo[0]!=null) tr.setParameter("clicReporter",clicInfo[0]);
                    if (clicInfo[1]!=null) tr.setParameter("clicIP",clicInfo[1]);
                    if (clicInfo[2]!=null) tr.setParameter("clicPort",clicInfo[2]);
                    if (clicInfo[3]!=null) tr.setParameter("sessionKey",clicInfo[3]);
                    if (clicInfo[4]!=null) tr.setParameter("sessionContext",clicInfo[4]);
                }
                
                Result output=new javax.xml.transform.stream.StreamResult(out);
                                /*tr.setURIResolver(new URIResolver(){
                                    public Source resolve(String href, String base) throws TransformerException{
                                        return new javax.xml.transform.dom.DOMSource();
                                    }
                                });*/
                //tr.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
                //tr.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
                //System.out.println("Establert ENCODING");
                //java.io.InputStream r =((javax.xml.transform.stream.StreamSource)xmlSource).getInputStream();
                //byte[] b = new byte[r.available()];
				//r.read(b);
                //System.out.println("XML= "+new String(b));

				/*
				String sKey=(sQuadernXSL+"_"+sQTIUrl+iNumFull).replace('/','-').replace(':','-').replace('?','-');
				File f=new File(new File(System.getProperty("java.io.tmpdir")), sKey);
				if (!f.exists()){
					FileOutputStream fos = new FileOutputStream(f);
					Result rf = new javax.xml.transform.stream.StreamResult(fos);
	                tr.transform(xmlSource,rf);
					fos.close();
				}
				FileInputStream fis = new FileInputStream(f);
                byte[] b = new byte[fis.available()];
				fis.read(b);
				out.write(new String(b));
				fis.close();
				*/
				
				tr.transform(xmlSource,output);
            }
            catch (Exception e){
                System.out.println("Excepció:"+e);
                e.printStackTrace();
                //QVServlet.err.println("Excepció:"+e);
                //e.printStackTrace(QVServlet.err);
            }
        }
        else{
            System.out.println("Error: S'ha de cridar abans a setStyleSheet(Sorce xsl) abans de transform.");
            //QVServlet.err.println("Error: S'ha de cridar abans a setStyleSheet(Sorce xsl) abans de transform.");
        }
    }
    
    public static void main(String[] args){
                try{
                        QTIToHTMLTransformer tr=new QTIToHTMLTransformer();
                        Source xsl=new javax.xml.transform.stream.StreamSource(new java.io.FileInputStream("C:\\Albert\\pfc\\bak\\qv_nous_packages\\recursos\\qti_prof.xsl"));
                        //Source xsl=new javax.xml.transform.stream.StreamSource(new java.io.FileInputStream("C:\\Albert\\pfc\\src\\qti.xsl"));
                        tr.setStyleSheet(xsl,"C:\\Albert\\pfc\\bak\\qv_nous_packages");
                        Source xmlSource=new javax.xml.transform.stream.StreamSource(new java.io.FileInputStream("C:\\Albert\\pfc\\quaderns\\medi_030324.xml"));
                        //Source xmlSource=new javax.xml.transform.stream.StreamSource(new java.io.FileInputStream("C:\\Albert\\pfc\\src\\075.xml"));
                        xmlSource.setSystemId("file:///C:\\Albert\\pfc\\quaderns\\medi_030324.xml");
                        //xmlSource.setSystemId("file:///C:\\Albert\\pfc\\src\\075.xml");
                        java.io.OutputStream out=new java.io.FileOutputStream("c:\\out_medi.html");
                        //java.io.OutputStream out=new java.io.FileOutputStream("C:\\Albert\\pfc\\src\\out_java_075.html");
                        //////transform(int iUserId, Object sUserId, int iNumFull, int iTotalFulls, Source xmlSource, java.io.Writer out, String feedbackParameter, String sInteraction, String initialSelection, String sView, String sServerUrl, String sReturnPage, String sBackGroundImage, String[] clicInfo, String sQTIUrl)
                        String[] clicInfo=null;
                        //tr.transform(555,"par?sUserId?", 1, 31, xmlSource, /*new java.io.PrintWriter(*/out/*)*/,"par?feedback?","par?interaction?","par?initial?","par?view?","par?serverUrl?", "par?returnPage?",null,clicInfo,"par?qtiUrl?");
                        tr.transform(555,"par?sUserId?", 1, 31,3,3, xmlSource, new java.io.PrintWriter(out),"par?feedback?","par?interaction?","par?initial?","par?view?","par?serverUrl?", "par?returnPage?",null,clicInfo,"par?qtiUrl?","par?quadernXSL?","true","00:00:00","00:00:00","true","true","true",null,"2003-09-10","true","començat","blau");
                        //tr.transform(xmlSource,out);
                }
                catch (Exception e){
                        System.out.println("Excepció:"+e);
                        e.printStackTrace(System.out);
                        
                        //QVServlet.err.println("Excepcio: "+e);
                        //e.printStackTrace(QVServlet.err);
                }
    }
}
