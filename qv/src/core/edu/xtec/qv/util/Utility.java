package edu.xtec.qv.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;


/*
 * Utility.java
 *
 * Created on 9 de septiembre de 2002, 14:04
 */

/**
 *
 * @author  allastar
 */

public class Utility {
    
	protected static Logger logger = Logger.getRootLogger();

    /** Creates a new instance of Utility */
    public static java.util.HashMap getHashMapInFirstNotInSecond(java.util.HashMap hm1, java.util.HashMap hm2){
        /* Retorna un nou HashMap que conté els elements presents a hm1 i que no hi són a hm2. */
        java.util.HashMap hmResult=new java.util.HashMap();
        if (hm2==null) hmResult=hm1;
        else if (hm1!=null){ //hm1!=null && hm2!=null
            java.util.Iterator it=hm1.keySet().iterator();
            while (it.hasNext()){
                Object oKey=it.next();
                if (!hm2.containsKey(oKey)) hmResult.put(oKey,hm1.get(oKey));
            }
        }
        return hmResult;
    }
    
    public static String toStringDate(java.util.Date d){
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d);
        /*java.util.GregorianCalendar gc=new java.util.GregorianCalendar();
        gc.setTime(d);
        String s=gc.get(java.util.Calendar.DAY_OF_MONTH)+"/"+(gc.get(java.util.Calendar.MONTH)+1)+"/"+d.getYear();
        //Hauria de fer gc.get(Calendar.YEAR) però dona l'any 3908... potser perquè prové de sql.Date...
        //+gc.get(java.util.Calendar.YEAR);
        return s;*/
    }
    
    public static java.util.Date convertErroneousDate(java.util.Date d){
        // Aquesta funció utilitza funcions deprecated, però la d que rebem pateix certs problemes...
        //System.out.println("Year:"+d.getYear());
        java.util.GregorianCalendar gc;
        if (d!=null) gc=new java.util.GregorianCalendar(d.getYear(),d.getMonth(),d.getDate()+1);
        else gc=new java.util.GregorianCalendar();
        return gc.getTime();
    }
    
    public static void showVector(java.util.Vector v){
        java.util.Enumeration e=v.elements();
        while (e.hasMoreElements()){
            System.out.println(e.nextElement().toString());
        }
    }
    
    public static void setOption(javax.swing.JComboBox cb, Object option){
        boolean bFound=false;
        if (option==null) return;
        for (int i=0;i<cb.getItemCount() && !bFound;i++){
            String sItemName=cb.getItemAt(i).toString();
            if (sItemName.equals(option.toString())){
                bFound=true;
                cb.setSelectedIndex(i);
            }
        }
        if (!bFound){
            cb.addItem(option);
            cb.setSelectedItem(option);
        }
    }
    
    public static void moveInVector(java.util.Vector v, Object o, int iDir){
        int i=0;
        boolean bFound=false;
        while (i<v.size() && !bFound){
            if (v.elementAt(i).equals(o)) bFound=true;
            else i++;
        }
        if (bFound){
            v.remove(i);
            v.add(i+iDir,o);
        }
    }
    
    public static java.awt.Image loadImage(String fileName, java.awt.Component c){
        java.awt.Image i=null;
        if (fileName!=null && (fileName.startsWith("http") || fileName.startsWith("file"))){
            try{
                i=java.awt.Toolkit.getDefaultToolkit().getImage(new java.net.URL(fileName));
            }
            catch (Exception e){
                System.out.println("Excepció amb la URL:"+fileName+" "+e);
                return null;
            }
        }
        else i=java.awt.Toolkit.getDefaultToolkit().getImage(Locator.getAbsolutePath(fileName));//new java.net.URL(img_url));
        try{
            boolean bFinish=false;
            for (int j=0;j<6 && !bFinish ;j++){
                Thread.currentThread().sleep(500);
                if (i.getWidth(c)>0) bFinish=true;
            }
            c.repaint();//////
        }
        catch(Exception e){
        }
        return i;
    }
    
    public static String quote(String txt){
        StringBuffer sb=new StringBuffer("");
        sb.append(replace(replace(txt, "'", "&#39;"),"’","&apos;"));
        return sb.toString();
    }
    
    public static String replace(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
        
        if (str!=null){
            while ((e = str.indexOf(pattern, s)) >= 0) {
                result.append(str.substring(s, e));
                if(replace!=null)
                    result.append(replace);
                s = e+pattern.length();
            }
            result.append(str.substring(s));
        }
        return result.toString();
    }
    
    public static String addTime(String time1, String time2){
        if (time1==null) return time2;
        if (time2==null) return time1;
        int hours=0,minutes=0,seconds=0;
        try{
            java.util.StringTokenizer st=new java.util.StringTokenizer(time1,":");
            if (st.hasMoreTokens()) hours=Integer.parseInt(st.nextToken());
            if (st.hasMoreTokens()) minutes=Integer.parseInt(st.nextToken());
            if (st.hasMoreTokens()) seconds=Integer.parseInt(st.nextToken());
            st=new java.util.StringTokenizer(time2,":");
            if (st.hasMoreTokens()) hours+=Integer.parseInt(st.nextToken());
            if (st.hasMoreTokens()) minutes+=Integer.parseInt(st.nextToken());
            if (st.hasMoreTokens()) seconds+=Integer.parseInt(st.nextToken());
            if (seconds>59){
                minutes++;
                seconds-=60;
            }
            if (minutes>59){
                hours++;
                minutes-=60;
            }
            java.text.DecimalFormat df=new java.text.DecimalFormat();
            df.setMaximumIntegerDigits(2);
            df.setMinimumIntegerDigits(2);
            df.setMaximumFractionDigits(0);
            return (df.format(hours)+":"+df.format(minutes)+":"+df.format(seconds));
        }
        catch (Exception e){
            e.printStackTrace(System.out);
        }
        return "00:00:00";
    }
    
    public static String getFileURL(String sFile){
        if (sFile!=null && !(sFile.indexOf('/')>=0 || sFile.startsWith("http") || sFile.startsWith("file") || sFile.trim().length()==0)){
            sFile="file:/"+sFile.trim();
        }
        return sFile;
    }

	/**
	 * Primer carrega el fitxers de properties per defecte. Després carrega a sobre el fitxer
	 * de properties de l'usuari (si existeix).
	 * @param aPath path del fitxer de properties
	 * @param aFile nom del fitxer de properties
	 * @return properties del fitxer de properties especificat
	 * @throws Exception
	 */
	public static Properties loadProperties (String aPath, String aFile) throws Exception{
		Properties p = new Properties();
		try{
			p.load(Utility.class.getResourceAsStream(aPath+aFile));
			File f = new File(System.getProperty("user.home"), aFile);
			if(f.exists()){
				FileInputStream is=new FileInputStream(f);
				p.load(is);
				is.close();
			}
		} catch (FileNotFoundException f) {
			logger.error(f);
		} catch (IOException e) {
			logger.error(e);
		}
		return p;    	
	}

	
}
