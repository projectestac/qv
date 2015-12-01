/*
 * Utils.java
 *
 * Created on 6 / febrer / 2001, 18:30
 */

package edu.xtec.jclic.misc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.StringTokenizer;

import javax.swing.filechooser.FileFilter;

import edu.xtec.jclic.Constants;
import edu.xtec.util.Messages;
import edu.xtec.util.Options;
import edu.xtec.util.SimpleFileFilter;


/**
 *
 * @author Francesc Busquets (fbusquets@pie.xtec.es)
 * @version
 */
public abstract class Utils implements Constants{
    
    public static final int
    JCLIC_FF=0, JCLIC_ZIP_FF=1, PAC_FF=2, PCC_FF=3,
    ALL_JCLIC_FF=4, ALL_CLIC_FF=5, ALL_JCLIC_CLIC_FF=6,
    INSTALL_FF=7, GIF_FF=8, JPG_FF=9, PNG_FF=10, ALL_IMAGES_FF=11,
    ALL_FF=12,
    ALL_SOUNDS_FF=13, MIDI_FF=14, ALL_VIDEO_FF=15, ALL_ANIM_FF=16,
    SKINS_FF=17, ALL_MEDIA_FF=18, ALL_MULTIMEDIA_FF=19,
    NUM_FILE_FILTERS=20;
    
    private static FileFilter[] fileFilters=new FileFilter[NUM_FILE_FILTERS];
    
    public static final String EXT_JCLIC=".jclic", EXT_JCLIC_ZIP=".jclic.zip",
    EXT_PAC=".pac", EXT_PCC=".pcc", EXT_INSTALL=".jclic.inst";
    public static final String EXT_GIF=".gif", EXT_JPG=".jpg", EXT_PNG=".png", EXT_BMP=".bmp";
    public static final String EXT_WAV=".wav", EXT_AU=".au", EXT_MP3=".mp3", EXT_AIFF=".aiff", EXT_MID=".mid";
    public static final String EXT_AVI=".avi", EXT_MOV=".mov", EXT_MPEG=".mpeg";
    public static final String EXT_SWF=".swf", EXT_XML=".xml", EXT_ALL=".*";
    
    public static final String[] EXT_ALL_JCLIC=new String[]{EXT_JCLIC, EXT_JCLIC_ZIP};
    public static final String[] EXT_ALL_CLIC=new String[]{EXT_PAC, EXT_PCC};
    public static final String[] EXT_ALL_JCLIC_CLIC=new String[]{EXT_JCLIC, EXT_JCLIC_ZIP, EXT_PAC, EXT_PCC};
    public static final String[] EXT_ALL_IMAGES=new String[]{EXT_GIF, EXT_JPG, EXT_PNG, EXT_BMP};
    public static final String[] EXT_ALL_SOUNDS=new String[]{EXT_WAV, EXT_AU, EXT_MP3, EXT_AIFF};
    public static final String[] EXT_ALL_VIDEO=new String[]{EXT_AVI, EXT_MOV, EXT_MPEG, EXT_SWF};
    public static final String[] EXT_ALL_ANIM=new String[]{EXT_SWF};
    public static final String[] EXT_ALL_MEDIA=new String[]{EXT_GIF, EXT_JPG, EXT_PNG,
    EXT_WAV, EXT_AU, EXT_MP3, EXT_AIFF, EXT_MID, EXT_AVI, EXT_MOV, EXT_MPEG, EXT_SWF, EXT_XML};
    public static final String[] EXT_ALL_MULTIMEDIA=new String[]{EXT_WAV, EXT_AU, EXT_MP3, EXT_AIFF, EXT_MID,
    EXT_AVI,EXT_MOV,EXT_MPEG, EXT_SWF};
    
    public static FileFilter getFileFilter(int fileFilterCode, Messages msg){
        if(fileFilterCode<0 || fileFilterCode>=NUM_FILE_FILTERS)
            return null;
        if(fileFilters[fileFilterCode]==null){
            switch(fileFilterCode){
                case JCLIC_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_JCLIC, msg.get("filefilter_jclic"));
                    break;
                case JCLIC_ZIP_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_JCLIC_ZIP, msg.get("filefilter_jclic_zip"));
                    break;
                case PAC_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_PAC, msg.get("filefilter_pac"));
                    break;
                case PCC_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_PCC, msg.get("filefilter_pcc"));
                    break;
                case ALL_JCLIC_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_ALL_JCLIC, msg.get("filefilter_all_jclic"));
                    break;
                case ALL_CLIC_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_ALL_CLIC, msg.get("filefilter_all_clic"));
                    break;
                case ALL_JCLIC_CLIC_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_ALL_JCLIC_CLIC, msg.get("filefilter_all_jclic_clic"));
                    break;
                case INSTALL_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_INSTALL, msg.get("filefilter_install"));
                    break;
                case GIF_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_GIF, msg.get("filefilter_gif"));
                    break;
                case JPG_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_JPG, msg.get("filefilter_jpg"));
                    break;
                case PNG_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_PNG, msg.get("filefilter_png"));
                    break;
                case ALL_IMAGES_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_ALL_IMAGES, msg.get("filefilter_all_images"));
                    break;
                case ALL_SOUNDS_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_ALL_SOUNDS, msg.get("filefilter_all_sounds"));
                    break;
                case MIDI_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_MID, msg.get("filefilter_midi"));
                    break;
                case ALL_VIDEO_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_ALL_VIDEO, msg.get("filefilter_all_video"));
                    break;
                case ALL_ANIM_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_ALL_ANIM, msg.get("filefilter_all_anim"));
                    break;
                case SKINS_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_XML, msg.get("filefilter_skins"));
                    break;
                case ALL_MEDIA_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_ALL_MEDIA, msg.get("filefilter_all_media"));
                    break;
                    
                case ALL_MULTIMEDIA_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_ALL_MULTIMEDIA, msg.get("filefilter_all_media"));
                    break;
                    
                case ALL_FF:
                    fileFilters[fileFilterCode]=new SimpleFileFilter(EXT_ALL, msg.get("filefilter_all"));
                    break;
            }
        }
        return fileFilters[fileFilterCode];
        
    }
    
    public static String[] getFileFilterExtensions(int fileFilterCode){
        String[] result=null;
        if(fileFilterCode>=0 && fileFilterCode<NUM_FILE_FILTERS){
            switch(fileFilterCode){
                case JCLIC_FF:
                    result=new String[]{EXT_JCLIC};
                    break;
                case JCLIC_ZIP_FF:
                    result=new String[]{EXT_JCLIC_ZIP};
                    break;
                case PAC_FF:
                    result=new String[]{EXT_PAC};
                    break;
                case PCC_FF:
                    result=new String[]{EXT_PCC};
                    break;
                case ALL_JCLIC_FF:
                    result=EXT_ALL_JCLIC;
                    break;
                case ALL_CLIC_FF:
                    result=EXT_ALL_CLIC;
                    break;
                case ALL_JCLIC_CLIC_FF:
                    result=EXT_ALL_JCLIC_CLIC;
                    break;
                case INSTALL_FF:
                    result=new String[]{EXT_INSTALL};
                    break;
                case GIF_FF:
                    result=new String[]{EXT_GIF};
                    break;
                case JPG_FF:
                    result=new String[]{EXT_JPG};
                    break;
                case PNG_FF:
                    result=new String[]{EXT_PNG};
                    break;
                case ALL_IMAGES_FF:
                    result=EXT_ALL_IMAGES;
                    break;
                case ALL_SOUNDS_FF:
                    result=EXT_ALL_SOUNDS;
                    break;
                case MIDI_FF:
                    result=new String[]{EXT_MID};
                    break;
                case ALL_VIDEO_FF:
                    result=EXT_ALL_VIDEO;
                    break;
                case ALL_ANIM_FF:
                    result=EXT_ALL_ANIM;
                    break;
                case SKINS_FF:
                    result=new String[]{EXT_XML};
                    break;
                case ALL_MEDIA_FF:
                    result=EXT_ALL_MEDIA;
                    break;
                    
                case ALL_MULTIMEDIA_FF:
                    result=EXT_ALL_MULTIMEDIA;
                    break;
                    
            }
        }
        return result;
        
    }
    
    public static BufferedImage toBufferedImage(Image image, Color bgColor, ImageObserver io) {
        int w=image.getWidth(io);
        int h=image.getHeight(io);
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, bgColor, io);
        g.dispose();
        return bufferedImage;
    }
    
    public static void drawImage(Graphics g, Image img, Rectangle dest, Rectangle source, ImageObserver io){
        if(g.getClip().intersects(dest)){
            g.drawImage(img, dest.x, dest.y, dest.x+dest.width, dest.y+dest.height, source.x, source.y, source.x+source.width, source.y+source.height, io);
        }
    }
    
    public static void tileImage(Graphics g, Image img, Rectangle dest, Rectangle source, ImageObserver io){
        if(g.getClip().intersects(dest)){
            int x, y;
            Area saveClip=new Area(g.getClip());
            Area newClip=new Area(saveClip);
            newClip.intersect(new Area(dest));
            g.setClip(newClip);
            Rectangle floatDest=new Rectangle(dest.x, dest.y, source.width, source.height);
            for(y=0; y<dest.height; y+=source.height){
                for(x=0; x<dest.width; x+=source.width){
                    floatDest.setLocation(dest.x+x, dest.y+y);
                    drawImage(g, img, floatDest, source, io);
                }
            }
            g.setClip(saveClip);
        }
    }
    
    public static void strToIntArray(String str, int [] intArray){
        if(str==null || intArray==null) return;
        int i=0;
        StringTokenizer st= new StringTokenizer(str, ",", false);
        while(st.hasMoreTokens() && i<intArray.length){
            try{
                intArray[i]=Integer.parseInt(st.nextToken().trim());
            } catch (Exception e){
            }
            i++;
        }
    }
    
    public static void strToPoint(String str, Point pt){
        int [] values=new int[2];
        strToIntArray(str, values);
        pt.x=values[0];
        pt.y=values[1];
    }
    
    public static void strToDimension(String str, java.awt.Dimension d){
        int [] values=new int[2];
        strToIntArray(str, values);
        d.width=values[0];
        d.height=values[1];
    }
    
    public static void strToRect(String str, Rectangle r){
        int [] values=new int[4];
        strToIntArray(str, values);
        r.setBounds(values[0], values[1], values[2], values[3]);
    }
    
    public static String[] strToStrArray(String source, String separator){
        if(source==null || separator==null) return null;
        StringTokenizer st=new StringTokenizer(source, separator, true);
        java.util.Vector v=new java.util.Vector();
        while(st.hasMoreTokens()){
            String s=st.nextToken();
            if(separator.equals(s)) v.add(new String());
            else{
                v.add(s);
                if(st.hasMoreTokens()) st.nextToken();
            }
        }
        String[] array=new String[v.size()];
        array=(String[])v.toArray(array);
        return array;
    }
    
    public static String[] strToStrArrayNoNulls(String source, String separator) throws Exception{
        String[] result=strToStrArray(source, separator);
        if(result==null || result.length==0)
            throw new Exception("Invalid parameter: "+source);
        for(int i=0; i<result.length; i++)
            if(result[i]==null || result[i].length()==0)
                throw new Exception("Invalid parameter: "+source);
        return result;
    }
    
    public static byte[] extractByteSeq(byte[] data, int line, byte searchFor, byte changeTo) {
        
        byte[] result=null;
        int l=data.length;
        int k=0;
        int p0=0, p1=0;
        
        for(int i=0; i<l; i++){
            if(data[i]==0x0D){
                if(i<l && data[i+1]==0x0A){
                    p0=p1==0 ? p1 : p1+2;
                    p1=i;
                    if(k==line) break;
                    k++;
                }
            }
        }
        
        if(p1>p0){
            int j=p1-p0;
            result=new byte[j];
            for(int i=0; i<j; i++){
                result[i]=data[p0+i];
                if(result[i]==searchFor) result[i]=changeTo;
            }
        }
        
        return result;
    }
    
    public static int roundTo(double v, int n){
        return ((int)(v/n))*n;
    }
    
    public static int countSpaces(String tx){
        String t=tx.trim();
        int j=0;
        for(int i=0; i<t.length(); i++){
            if(t.charAt(i)==' ') j++;
        }
        return j;
    }
    
    public static boolean compareStringsIgnoreCase(String s1, String s2){
        if(s1==null && s2==null) return true;
        if(s1==null || s2==null) return false;
        return s1.compareToIgnoreCase(s2)==0;
    }
    
    public static boolean compareTrimStringsIgnoreCase(Object s1, Object s2){
        if(s1==null)
            s1="";
        if(s2==null)
            s2="";
        return s1.toString().trim().compareToIgnoreCase(s2.toString().trim())==0;
    }
    
    public static boolean compareObjects(Object o1, Object o2){
        if(o1==null && o2==null) return true;
        if(o1==null || o2==null) return false;
        return o1.equals(o2);
    }
    
    public static String replace(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        if(str==null)
            str="";
        StringBuffer result = new StringBuffer();
        
        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            if(replace!=null)
                result.append(replace);
            s = e+pattern.length();
        }
        result.append(str.substring(s));
        return result.substring(0);
    }
    
    public static boolean compareMultipleOptions(String answer, String check, boolean checkCase){
        if(answer==null || answer.length()==0 || check==null || check.length()==0) return false;
        StringTokenizer st=new StringTokenizer(check, "|");
        while(st.hasMoreTokens()){
            if(checkCase ? st.nextToken().equals(answer)
            : st.nextToken().equalsIgnoreCase(answer)) return true;
        }
        return false;
    }
    
    public static Point mapPointTo(java.awt.Component srcCmp, Point offset, java.awt.Component destCmp){
        Point p=new Point(srcCmp.getLocationOnScreen());
        p.x+=offset.x; p.y+=offset.y;
        Point pd=destCmp.getLocationOnScreen();
        p.x-=pd.x; p.y-=pd.y;
        return p;
    }
    
    public static String secureString(Object data){
        if(data==null) return new String();
        return data.toString();
    }
    
    public static String nullableString(Object o){
        String result=null;
        if(o!=null){
            result=o.toString().trim();
            if(result.length()==0)
                result=null;
        }
        return result;
    }
    
    public static String secureSQLString(String data){
        return replace(secureString(data), "'", "''");
    }
    
    public static void refreshAnimatedImage(Image img){
        if(img!=null && (Toolkit.getDefaultToolkit().checkImage(img, -1, -1, null) & ImageObserver.ALLBITS)!=0){
            img.flush();
        }
    }
    
    public static int getAbsIntValueOf(String s){
        int result=-1;
        if(s!=null && s.length()>0){
            for(int i=0; i<s.length(); i++){
                char ch=s.charAt(i);
                if(ch<'0' || ch>'9')
                    return result;
            }
            try{
                result=Integer.parseInt(s);
            } catch(NumberFormatException ex){
                result=-1;
            }
        }
        return result;
    }
    
    public static void checkRenderingHints(Options options){
        if(DEFAULT_RENDERING_HINTS.isEmpty()){
            if(options==null)
                options=new Options();
            boolean mac=options.getBoolean(Options.MAC);
            boolean j14=options.getBoolean(Options.JAVA14);
            boolean win=options.getBoolean(Options.WIN);
            
            //DEFAULT_RENDERING_HINTS.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
            
            if(win)
                DEFAULT_RENDERING_HINTS.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            
            //DEFAULT_RENDERING_HINTS.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
            
            //DEFAULT_RENDERING_HINTS.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DEFAULT);
            
            if(true)
                DEFAULT_RENDERING_HINTS.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            
            if(win)
                DEFAULT_RENDERING_HINTS.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            
            //DEFAULT_RENDERING_HINTS.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
            
            if(win)
                DEFAULT_RENDERING_HINTS.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            
            
            if(win)
                DEFAULT_RENDERING_HINTS.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
    }
    
}
