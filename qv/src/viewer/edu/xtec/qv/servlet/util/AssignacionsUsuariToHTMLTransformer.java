package edu.xtec.qv.servlet.util;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;

import edu.xtec.qv.util.QuadernConfig;

public class AssignacionsUsuariToHTMLTransformer{
    
    private Transformer tr=null;
    
    public void setStyleSheet(Source xsl, String context) throws Exception{

        final String contextf=context;
        TransformerFactory tf=TransformerFactory.newInstance();
        
        tf.setURIResolver(new URIResolver(){
            public Source resolve(String href, String base) throws TransformerException{
                String sUrl=QuadernConfig.transformURLValue(href,contextf);
                //System.out.println("#### sUrl="+sUrl+"<--");
                java.io.InputStream is=null;
                try{
                    is=new java.net.URL(sUrl).openStream();
                }
                catch (Exception e){
                }
                if (is!=null) return new javax.xml.transform.stream.StreamSource(is);
                else return null;
            }
        });
        tr=tf.newTransformer(xsl);
    }
    
    public void transform(Source xmlSource, java.io.Writer out, String userId, String sBackgroundImage, String sReturnPage, String sFinishPage, String sPwdPage, String sColor, String sView/*, String feedbackParameter, String initialSelection*/) throws javax.xml.transform.TransformerException{
        if (tr!=null){
                Result output=new javax.xml.transform.stream.StreamResult(out);
                if (userId!=null) tr.setParameter("userId",userId);
                if (sBackgroundImage!=null) tr.setParameter("bgimage",sBackgroundImage);
                if (sReturnPage!=null) tr.setParameter("returnPage",sReturnPage);
                if (sFinishPage!=null) tr.setParameter("finishPage",sFinishPage);
                if (sPwdPage!=null) tr.setParameter("pwdPage",sPwdPage);
                if (sColor!=null) tr.setParameter("color", sColor);
                if (sView !=null) tr.setParameter("view", sView);
                
                if (output==null) System.out.println("output=null!");
                if (xmlSource==null) System.out.println("xmlSource=null!");
                
                tr.transform(xmlSource,output);
        }
        else{
            System.out.println("Error: S'ha de cridar abans a setStyleSheet(Sorce xsl) abans de transform.");
        }
    }    
}
