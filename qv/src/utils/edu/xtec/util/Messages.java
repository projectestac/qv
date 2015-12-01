/*
 * Messages.java
 *
 * Created on 14 de septiembre de 2001, 11:22
 */

package edu.xtec.util;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.text.Collator;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author  fbusquet
 * @version
 */
public class Messages {
    
    public static final String LANGUAGE="language", COUNTRY="country", VARIANT="variant";
    //public static final String BASIC_BUNDLE="messages.BasicMessages";
	public static final String BASIC_BUNDLE="messages.MessagesBundle";
    public static final String MESSAGES="messages";
    public static final String ERROR="ERROR", WARNING="WARNING";
    public static final int MAX_PASSWORD_LENGTH=24;
    private static Locale currentLocale;
    private static MultiBundle messages;
    private static Collator collator;
    private static java.text.NumberFormat numberFormat;
    private static java.text.NumberFormat percentFormat;    
    
    public static final String OPTIONS_DELIMITER=",";
    
    public Messages(String bundle){
        init(bundle, null, null, null);
    }
    
    public Messages(String bundle, String options){
        StringTokenizer st=new StringTokenizer(options, OPTIONS_DELIMITER);
        init(bundle,
        st.hasMoreTokens() ? st.nextToken() : null,
        st.hasMoreTokens() ? st.nextToken() : null,
        st.hasMoreTokens() ? st.nextToken() : null);
    }
    
    public Messages(String bundle, java.util.HashMap options){
        init(bundle,
        (String)options.get(LANGUAGE),
        (String)options.get(COUNTRY),
        (String)options.get(VARIANT));
    }
    
    public Messages(String bundle, String language, String country, String variant) {
        init(bundle, language, country, variant);
    }
    
    public static Messages getMessages(HashMap options, String bundle){
        Messages msg=(Messages)options.get(MESSAGES);
        if(msg==null){
            String language=(String)options.get(LANGUAGE);
            if(language==null){
                JOptionPane pane=new JOptionPane("Please select your language:", JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
                pane.setSelectionValues(DESCRIPTIVE_LANGUAGE_CODES);
                pane.setWantsInput(true);
                pane.setInitialSelectionValue(getDescriptiveLanguageCode(Locale.getDefault().getLanguage()));
                int l=showOptionPane((Component)options.get(Options.MAIN_PARENT_COMPONENT), pane, null, "Language selecion");
                String sel=(String)pane.getInputValue();
                if(l==JOptionPane.OK_OPTION && sel!=null)
                    options.put(LANGUAGE, getLanguageFromDescriptive(sel));
            }
            msg=new Messages(bundle, options);
            options.put(MESSAGES, msg);            
        }
        else if(bundle!=null){
            Messages.setLocale(options);
            Messages.addBundle(bundle);
        }        
        return msg;
    }
    
    public static void init(String bundle, String language, String country, String variant) {
        setLocale(language, country, variant);
        addBundle(bundle);
        addBundle(BASIC_BUNDLE);
    }
    
    public static void setLocale(java.util.HashMap options){
        setLocale(
        (String)options.get(LANGUAGE),
        (String)options.get(COUNTRY),
        (String)options.get(VARIANT));
    }
    
    public static void setLocale(String language, String country, String variant) {
        Locale l=null;
        if(country==null) country="";
        if(language==null || language.length()==0)
            l= (currentLocale==null ? Locale.getDefault() : currentLocale);
        else if(variant==null || variant.length()==0)
            l=new Locale(language, country);
        else
            l=new Locale(language, country, variant);                
        if(!l.equals(currentLocale)){
            currentLocale=l;
            numberFormat=java.text.NumberFormat.getInstance(currentLocale);
            percentFormat=java.text.NumberFormat.getPercentInstance(currentLocale);
            collator=null;
            if(messages!=null)
                messages.setLocale(currentLocale);
        }
    }

	public static String getLanguageCode(){
		return "";
	}  
	  
	public static void setLanguageCode(String sLanguage){
	}    
    
    public static void addBundle(String bundle){
        if(currentLocale!=null && bundle!=null){
            try{
                java.util.ResourceBundle b=ResourceManager.getBundle(bundle, currentLocale);
                if(messages==null)
                    messages=new MultiBundle(b, bundle, currentLocale);
                else
                    messages.addBundle(b, bundle, currentLocale);
            }
            catch(Exception ex){
                System.err.println("unable to build messagesBundle: "+bundle);
                System.err.println(ex);
            }
        }
    }
    
    public String getShortDateStr(Date date){
        DateFormat df=DateFormat.getDateInstance(DateFormat.SHORT, currentLocale);
        return df.format(date);
    }
    
    public String getShortDateTimeStr(Date date){
        DateFormat df=DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, currentLocale);
        return df.format(date);
    }
    
    public String get(String group, String key){
        return get(new StringBuffer(group).append(key).toString());
    }
    
    public static String get(HashMap options, String group, String key){
        return getMessages(options, null).get(group, key);
    }
    
    public String get(String key){
        return messages==null ? key : messages.getString(key);
    }

	public static String getLocalizedString(String text){
	  String result;
	  try{
	  	if (messages == null){
			init(null,"ca",null,null);
	  	}
		  result=messages.getString(text);
	  }
	  catch(Exception e){
		  System.out.println("ERROR - Unable to get translated message: " + text);
		  result="["+text+"]";
	  }
	  return result;
	}
    
    public static String get(HashMap options, String key){
        return getMessages(options, null).get(key);
    }
    
    public String getAllowNull(String key){
        return key==null ? "":get(key);
    }
    
    public String get(String key1, String s, String key2){
        StringBuffer sb=new StringBuffer(getAllowNull(key1));
        sb.append(" ").append(s!=null ? s : "").append(" ").append(getAllowNull(key2));
        return sb.toString();
    }
    
    public Locale getLocale(){
        return currentLocale;
    }
    
    public Collator getCollator(){
        if(collator==null) collator=Collator.getInstance(currentLocale);
        return collator;
    }
    
    public String showInputDlg(Component parent, String msgKey, String shortPromptKey, String initialValue, String titleKey, boolean isPassword){
        String[] msgKeys=null;
        if(msgKey!=null)
            msgKeys=new String[]{msgKey};
        return showInputDlg(parent, msgKeys, shortPromptKey, initialValue, titleKey, isPassword);
    }
    
    public String showInputDlg(Component parent, String[] msgKeys, String shortPromptKey, String initialValue, String titleKey, boolean isPassword){
        String result=null;
        JTextField textField=null;
        if(isPassword){
            textField=new JPasswordField(MAX_PASSWORD_LENGTH);
            if(shortPromptKey==null)
                shortPromptKey="PASSWORD";
        }
        else
            textField=new JTextField(MAX_PASSWORD_LENGTH);
        
        if(initialValue!=null)
            textField.setText(initialValue);
        
        if(showInputDlg(parent, msgKeys, new String[]{shortPromptKey}, new JComponent[]{textField}, titleKey)){
            if(isPassword){
                char[] pwch=((JPasswordField)textField).getPassword();
                if(pwch!=null && pwch.length>0)
                    result=String.copyValueOf(pwch);
            }
            else
                result=textField.getText();
        }        
        return result;
    }
    
    public boolean showQuestionDlg(Component parent, String key, String titleKey){        
        return showQuestionDlg(parent, get(key), titleKey);
    }
    
    public boolean showQuestionDlg(Component parent, Object msg, String titleKey){
        JOptionPane pane=new JOptionPane(msg, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        String title=get(titleKey!=null ? titleKey : "QUESTION");
        return showOptionPane(parent, pane, null, title)==JOptionPane.YES_OPTION;                
    }
    
    public boolean showInputDlg(Component parent, String[] msgKeys, String[] shortPromptKeys, JComponent[] promptObjects, String titleKey){
        Vector v=new Vector();
        
        if(msgKeys!=null){
            for(int i=0; i<msgKeys.length; i++)
                v.add(get(msgKeys[i]));
        }
        
        if(promptObjects!=null){
            if(shortPromptKeys==null){
                for(int i=0; i<promptObjects.length; i++)
                    v.add(promptObjects[i]);            
            }
            else{
                GridBagLayout gridBag=new GridBagLayout();
                GridBagConstraints c=new GridBagConstraints();
                c.fill=GridBagConstraints.BOTH;
                c.insets=new java.awt.Insets(3, 3, 3, 3);
                
                JPanel panel=new JPanel(gridBag);
                for(int i=0; i<promptObjects.length; i++){
                    if(shortPromptKeys.length>i){
                        JLabel lb=new JLabel(get(shortPromptKeys[i]));
                        lb.setLabelFor(promptObjects[i]);
                        lb.setHorizontalAlignment(SwingConstants.LEFT);
                        c.gridwidth=GridBagConstraints.RELATIVE;
                        gridBag.setConstraints(lb, c);
                        panel.add(lb);
                    }
                    c.gridwidth=GridBagConstraints.REMAINDER;
                    gridBag.setConstraints(promptObjects[i], c);
                        panel.add(promptObjects[i]);
                }
                v.add(panel);
            }
        }
        String title= (titleKey!=null ? get(titleKey) : "");
                
        JOptionPane pane=new JOptionPane(v.toArray(), JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        return showOptionPane(parent, pane, null, title)==JOptionPane.OK_OPTION;        
    }
    
    public boolean showInputDlg(Component parent, JComponent mainComponent, String titleKey){
        String title= (titleKey!=null ? get(titleKey) : "");
        JOptionPane pane=new JOptionPane(mainComponent, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        return showOptionPane(parent, pane, null, title)==JOptionPane.OK_OPTION;        
    }
    
    public void showAlert(Component parent, String key){
        showAlert(parent, new String[]{get(key)});
    }
    
    public void showAlert(Component parent, String[]msg){
        System.err.println("Warning:");
        for(int i=0; i<msg.length; i++)
            System.err.println(msg[i]);
        JOptionPane pane=new JOptionPane(msg, JOptionPane.WARNING_MESSAGE);
        showOptionPane(parent, pane, null, get(WARNING));        
    }
    
    public void showErrorWarning(Component parent, String key, Exception ex){
        showErrorWarning(parent, key, (Vector)null, ex);
    }
    
    public void showErrorWarning(Component parent, String key, String value, Exception ex){
        Vector v=new Vector();
        if(value!=null)
            v.add(value);
        showErrorWarning(parent, key, v, ex);
    }
    
    public void showErrorWarning(Component parent, String key, Vector values, Exception ex){
        if(key==null)
            key=ERROR;
        Vector v=new Vector();        
        String mainMsg=get(key);
        System.err.println(mainMsg);
        v.add(get(key));
        if(values!=null){
            Iterator it=values.iterator();
            while(it.hasNext()){
                Object o=it.next();
                if(o!=null){
                    v.add(o);
                    System.err.println(o);
                }
            }
        }
        if(ex!=null){
            String s=ex.getLocalizedMessage();
            if(s!=null)
                v.add(s);
            else
                v.add(ex.toString());
            System.err.println(s);
        }
        
        JOptionPane pane=new JOptionPane(v.toArray(), JOptionPane.ERROR_MESSAGE);
        showOptionPane(parent, pane, null, get(ERROR));        
    }
    
    public boolean confirmReadableFile(Component parent, File f){
        boolean result=true;
        if(!f.canRead()){            
            result=false;
            showAlert(parent, new String[]{
                get("FILE_BEG"),
                quote(f.getAbsolutePath()),
                get(f.exists() ? "FILE_NOT READABLE" : "FILE_NOT_EXIST")
            });
        }
        return result;        
    }    
    
    public boolean confirmOverwriteFile(Component parent, File f){
        boolean result=true;                
        if(f.exists()){
            boolean dir=f.isDirectory();
            Vector v=new Vector();
            v.add(get(dir ? "FILE_DIR_BEG" : "FILE_BEG"));
            v.add(quote(f.getAbsolutePath()));
            boolean readOnly=!f.canWrite();
            if(readOnly)
                v.add(get("FILE_READONLY"));
            else{
                v.add(get("FILE_EXISTS"));
                v.add(get(dir ? "FILE_OVERWRITE_DIR_PROMPT" : "FILE_OVERWRITE_PROMPT"));
            }
            if(readOnly){
                showErrorWarning(parent, ERROR, v, null);
                result=false;                
            }
            else{
                result=showQuestionDlg(parent, v.toArray(), "CONFIRM");
            }            
        }
        return result;
    }
    
    protected static int showOptionPane(Component parentComponent, JOptionPane pane, Object[] buttonOptions, String title){
        JDialog dialog = pane.createDialog(parentComponent, title);
        pane.selectInitialValue();
        dialog.show();
        Object selectedValue = pane.getValue();
        if(selectedValue == null)
            return JOptionPane.CANCEL_OPTION;
        //If there is not an array of option buttons:
        if(buttonOptions == null) {
            if(selectedValue instanceof Integer)
                return ((Integer)selectedValue).intValue();
            return JOptionPane.CANCEL_OPTION;
        }
        //If there is an array of option buttons:
        for(int counter = 0, maxCounter = buttonOptions.length; counter < maxCounter; counter++) {
            if(buttonOptions[counter].equals(selectedValue))
                return counter;
        }
        return JOptionPane.CANCEL_OPTION;        
    }
    
    
    public static String quote(String text){
        return new StringBuffer(" \"").append(text).append("\" ").toString();
    }
        
    public String kValue(long v){
        StringBuffer sb=new StringBuffer();
        sb.append(numberFormat.format(v/1024)).append(" Kb");
        return sb.toString();
    }
    
    public String getNumber(long v){
        return numberFormat.format(v);
    }
    
    public String getNumber(double v){
        return numberFormat.format(v);
    }
    
    public String getPercent(long v){
        return percentFormat.format(((double)v)/100);
    }
    
    public String getPercent(double v){
        return percentFormat.format(v);
    }
    
    public String getHmsTime(long milis){
        long v=milis/1000;
        if(v<1)
            v=1;
        StringBuffer sb=new StringBuffer();
        if(v>=3600){
            sb.append(v/3600).append("h");
        }
        if(v>=60){
            sb.append((v%3600)/60).append("'");
        }
        sb.append(v%60).append("\"");
        return sb.toString();
    }    
    
    public static String getDescriptiveLanguageCode(String languageCode){
        String result=null;
        if(languageCode!=null){
            for(int i=0; i<DESCRIPTIVE_LANGUAGE_CODES.length; i++){
                if(getLanguageFromDescriptive(DESCRIPTIVE_LANGUAGE_CODES[i]).equals(languageCode)){
                    result=DESCRIPTIVE_LANGUAGE_CODES[i];
                    break;
                }
            }
        }
        return result;
    }
    
    public static String getLanguageFromDescriptive(String descriptive){
        String result=null;
        if(descriptive!=null){        
            int p=descriptive.length()-3;
            result=descriptive.substring(p, p+2);
        }
        return result;
    }
    
    public static final String[] DESCRIPTIVE_LANGUAGE_CODES={
        "Afar (aa)",
        "Abkhazian (ab)",
        "Avestan (ae)",
        "Afrikaans (af)",
        "Amharic (am)",
        "Arabic (ar)",
        "Assamese (as)",
        "Aymara (ay)",
        "Azerbaijani (az)",
        "Bashkir (ba)",
        "Belarusian (be)",
        "Bulgarian (bg)",
        "Bihari (bh)",
        "Bislama (bi)",
        "Bengali (bn)",
        "Tibetan (bo)",
        "Breton (br)",
        "Bosnian (bs)",
        "Catal\u00E0 (ca)",
        "Chechen (ce)",
        "Chamorro (ch)",
        "Corsican (co)",
        "Czech (cs)",
        "Church Slavic (cu)",
        "Chuvash (cv)",
        "Welsh (cy)",
        "Danish (da)",
        "German (de)",
        "Dzongkha (dz)",
        "Greek, Modern (el)",
        "English (en)",
        "Esperanto (eo)",
        "Espa\u00F1ol (es)",
        "Estonian (et)",
        "Euskara (eu)",
        "Persian (fa)",
        "Finnish (fi)",
        "Fijian (fj)",
        "Faroese (fo)",
        "French (fr)",
        "Frisian (fy)",
        "Irish (ga)",
        "Gaelic (gd)",
        "Galego (gl)",
        "Guarani (gn)",
        "Gujarati (gu)",
        "Manx (gv)",
        "Hausa (ha)",
        "Hebrew (he)",
        "Hindi (hi)",
        "Hiri Motu (ho)",
        "Croatian (hr)",
        "Hungarian (hu)",
        "Armenian (hy)",
        "Herero (hz)",
        "Interlingua (ia)",
        "Indonesian (id)",
        "Interlingue (ie)",
        "Inupiaq (ik)",
        "Ido (io)",
        "Icelandic (is)",
        "Italian (it)",
        "Inuktitut (iu)",
        "Japanese (ja)",
        "Javanese (jv)",
        "Georgian (ka)",
        "Kikuyu (ki)",
        "Kwanyama (kj)",
        "Kazakh (kk)",
        "Kalaallisut (kl)",
        "Khmer (km)",
        "Kannada (kn)",
        "Korean (ko)",
        "Kashmiri (ks)",
        "Kurdish (ku)",
        "Komi (kv)",
        "Cornish (kw)",
        "Kirghiz (ky)",
        "Latin (la)",
        "Letzeburgesch (lb)",
        "Lingala (ln)",
        "Lao (lo)",
        "Lithuanian (lt)",
        "Latvian (lv)",
        "Malagasy (mg)",
        "Marshallese (mh)",
        "Maori (mi)",
        "Macedonian (mk)",
        "Malayalam (ml)",
        "Mongolian (mn)",
        "Moldavian (mo)",
        "Marathi (mr)",
        "Malay (ms)",
        "Maltese (mt)",
        "Burmese (my)",
        "Nauru (na)",
        "Norwegian Bokm\u00E5l (nb)",
        "North Ndebele (nd)",
        "Nepali (ne)",
        "Ndonga (ng)",
        "Dutch (nl)",
        "Norwegian Nynorsk (nn)",
        "Norwegian (no)",
        "South Ndebele (nr)",
        "Navajo (nv)",
        "Nyanja (ny)",
        "Occitan (oc)",
        "Oromo (om)",
        "Oriya (or)",
        "Ossetic (os)",
        "Panjabi (pa)",
        "Pali (pi)",
        "Polish (pl)",
        "Pushto (ps)",
        "Portuguese (pt)",
        "Quechua (qu)",
        "Raeto-Romance (rm)",
        "Rundi (rn)",
        "Romanian (ro)",
        "Russian (ru)",
        "Kinyarwanda (rw)",
        "Sanskrit (sa)",
        "Sardinian (sc)",
        "Sindhi (sd)",
        "Northern Sami (se)",
        "Sango (sg)",
        "Sinhalese (si)",
        "Slovak (sk)",
        "Slovenian (sl)",
        "Samoan (sm)",
        "Shona (sn)",
        "Somali (so)",
        "Albanian (sq)",
        "Serbian (sr)",
        "Swati (ss)",
        "Sotho (st)",
        "Sundanese (su)",
        "Swedish (sv)",
        "Swahili (sw)",
        "Tamil (ta)",
        "Telugu (te)",
        "Tajik (tg)",
        "Thai (th)",
        "Tigrinya (ti)",
        "Turkmen (tk)",
        "Tagalog (tl)",
        "Tswana (tn)",
        "Tonga (to)",
        "Turkish (tr)",
        "Tsonga (ts)",
        "Tatar (tt)",
        "Twi (tw)",
        "Tahitian (ty)",
        "Uighur (ug)",
        "Ukrainian (uk)",
        "Urdu (ur)",
        "Uzbek (uz)",
        "Vietnamese (vi)",
        "Volap\u00FCk (vo)",
        "Walloon (wa)",
        "Wolof (wo)",
        "Xhosa (xh)",
        "Yiddish (yi)",
        "Yoruba (yo)",
        "Zhuang (za)",
        "Chinese (zh)",
        "Zulu (zu)"
    };    
}
