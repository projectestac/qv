/*
 * FontCheck.java
 *
 * Created on 15 de marzo de 2002, 11:33
 */

package edu.xtec.util;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

/**
 *
 * @author Francesc Busquets (fbusquets@pie.xtec.es)
 * @version 1.0
 */

public abstract class FontCheck {
    
    public static final String DEFAULT_FONT_NAME="default";
    public static final Font DEFAULT_FONT=new Font(DEFAULT_FONT_NAME, Font.PLAIN, 17);
    public static final String TMP_FONT_PREFIX="tmp_font_";
    private static final FontRenderContext FRC=new FontRenderContext(null, true, true);
    private static final HashMap systemFonts=new HashMap();
    
    public static boolean checkFont(Font font){
        
        boolean result=false;
        if(font!=null){
            FontRenderContext frc=new FontRenderContext(new AffineTransform(), false, false);
            TextLayout layout=new TextLayout("AB", font, frc);
            result=layout.getBounds().getWidth()>1;
        }
        return result;
        
        //return font==null ? false : font.getStringBounds("A", FRC).getWidth()>0.0;
    }
    
    public static boolean checkFontFamilyName(Object family){
        return family!=null
        && family instanceof String
        && checkFont(new Font((String)family, Font.PLAIN, 17));
    }
    
    public static Font getValidFont(String family, int style, int size){
        Font f=new Font(family, style, size);
        if(!checkFont(f)){
            Font fontBase=(Font)systemFonts.get(family.toLowerCase());
            if(fontBase==null){
                fontBase=DEFAULT_FONT;
            }
            f=fontBase.deriveFont(style, size);
        }
        return f;
    }
    
    public static String getValidFontFamilyName(Object family){
        Font f=(Font)systemFonts.get(family instanceof String ? ((String)family).toLowerCase() : family);
        if(f!=null)
            return f.getFamily();
        return checkFontFamilyName(family) ? (String)family : DEFAULT_FONT_NAME;
    }
    
    public static Font checkSystemFont(String fontName, String fontFileName){
        String fnLower=fontName.toLowerCase();
        Font f=(Font)systemFonts.get(fnLower);
        if(f==null){
            f=new Font(fontName, Font.PLAIN, 17);
            if(!checkFont(f) || !fontName.toLowerCase().equals(f.getFamily().toLowerCase())){
                try{
                    f=buildNewFont(fontFileName, ResourceManager.STREAM_PROVIDER, "fonts/"+fontFileName);
                    if(checkFont(f))
                        systemFonts.put(fnLower, f);
                    else
                        f=DEFAULT_FONT;
                }
                catch(Exception ex){
                    System.err.println("Unable to build font "+fontName+"\n:"+ex);
                }
            }
        }
        return f;
    } 
    
    public static Font buildNewFont(String fileName, StreamIO.InputStreamProvider isp, String resourceName) throws Exception{
        // code adapted from java.awt.Font
        String tmpDir=System.getProperty("java.io.tmpdir");
        if(tmpDir==null)
            throw new Exception("Unable to create fonts: No temp dir!");
        File fontFile=new File(tmpDir+File.separator+TMP_FONT_PREFIX+fileName);
        if(!fontFile.exists())
            StreamIO.writeStreamTo(isp.getInputStream(resourceName), new FileOutputStream(fontFile));
        sun.java2d.SunGraphicsEnvironment env =
        (sun.java2d.SunGraphicsEnvironment)GraphicsEnvironment
        .getLocalGraphicsEnvironment();
        String createName = null;
        //String createName = sun.java2d.SunGraphicsEnvironment.createFont(fontFile);
        if(createName == null)
            throw new Exception("Unable to create font - bad font data");
        return getValidFont(createName, Font.PLAIN, 1);
    }    
}
