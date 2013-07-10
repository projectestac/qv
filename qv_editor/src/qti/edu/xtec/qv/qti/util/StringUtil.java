/*
 * StringUtil.java
 * 
 * Created on 04/may/2004
 */
package edu.xtec.qv.qti.util;

import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * @author sarjona
 */
public class StringUtil {
	
	/*public static String replace(String sSource, String sOldString, String sNewString){
		String sTarget = "";
		StringTokenizer st = new StringTokenizer(sSource, sOldString);
		while (st.hasMoreTokens()){
			String sToken = st.nextToken();
			sTarget += sToken+sNewString;
		}
		return sTarget;
	}*/
	
	public static String trim(String sSource){
		String sTarget = replace(sSource, "  "," ");		
		return sTarget;
	}
	
	/**
	 * Ordena l'array indicat en ordre ascendent
	 * @param sArray Array d'Strings a ordenar
	 * @return 
	 */
	public static String[] sort(String[] sArray){
		String[] sSort = null;
		if (sArray!=null){
			sSort = new String[sArray.length];
			System.arraycopy(sArray, 0, sSort, 0, sArray.length);
			Arrays.sort(sSort);
		}
		return sSort;
	}
	
	/**
	 * Compta el nombre de cops que apareix sChar a sSource
	 * @param sSource
	 * @param sChar
	 * @return
	 */
	public static int count(String sSource, String sChar){
		int iCount = 0;
		if (sSource!=null && sChar!=null){
			StringTokenizer st = new StringTokenizer(sSource, sChar);
			iCount = st.countTokens();
		}
		return iCount;
	}
	
	public static String quote(String txt){
		StringBuffer sb=new StringBuffer("");
		sb.append(replace(replace(txt, "'", "&#39;"),"","&apos;"));
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

	public static String replaceTagsFromHTMLCode(String sHTML){
		String sText = null;
		if (sHTML!=null){
			sText = replace(sHTML, "&lt;", "<");
			sText = replace(sText, "&gt;", ">");
			sText = replace(sText, "&amp;&nbsp;", "&nbsp;");
		}
		return sText;
	}
	
	public static String replaceTagsToHTMLCode(String sHTML){
		String sText = null;
		if (sHTML!=null){
			sText = replace(sHTML, "<", "&lt;");
			sText = replace(sText, ">", "&gt;");
			sText = replace(sText, "&nbsp;", "&amp;nbsp;");
		}
		return sText;
	}
	
	public static String filterHTML(String input){
		String result=(input==null ? "" : input);
		if(input!=null && input.length()>0){
			StringBuffer filtered = new StringBuffer(input.length());
			char c;
			for(int i=0; i<input.length(); i++) {
				c = input.charAt(i);
				String s=null;
				switch(c){
					case '<':
						s="&lt;";
						break;
					case '>':
						s="&gt;";
						break;
					case ' ':
						try{
							if (input.charAt(i+1)==' ') s="&nbsp;";
						}catch (Exception e){
						}
						//s="&nbsp;";
						break;
/*					case '"':
						s="&quot;";
						break;
					case '&':
						s="&amp;";
						break;*/
				}
				if(s!=null)
					filtered.append(s);
				else
					filtered.append(c);
			}
			result=filtered.substring(0);
			//System.out.println("filterHTML-> input="+input+"  result="+result);
		}
		return result;
	}
    
	public static String filterFileName(String input){
		String result=(input==null ? "" : input);
		if(input!=null && input.length()>0){
			result=replace(result, "�", "a");
			result=replace(result, "�", "a");
			result=replace(result, "�", "i");
			result=replace(result, "�", "i");
			result=replace(result, "?", "");
		}
		return result;
	}

	public static String filter(String input){
		return filter(input, true);
	}
	
	public static String filter(String input, boolean bSpecial){
		String result=(input==null ? "" : input);
		if(input!=null && input.length()>0){
			StringBuffer filtered = new StringBuffer(input.length());
			char c;
			for(int i=0; i<input.length(); i++) {
				c = input.charAt(i);
				String s=null;
				switch(c){
					case ' ': s="_";break;
					case '[': s="";	break;
					case ']': s="";	break;
					case ')': s="";	break;
					case '(': s="";	break;
					case '-': s="";	break;
					case '<': s="";	break;
					case '>': s="";	break;
					case '"': s="";	break;
					case '\'': s="";break;
					case '�': s=""; break;
					case '`': s=""; break;
					case '�': s=""; break;
					case '&': s="";	break;
					case '\\': s="";break;
					case '/': s="";	break;
					case ':': s="";	break;
					case '*': s="";	break;
					case '�': s="";	break;
					case '?': s="";	break;
					case '!': s="";	break;
					case '$': s=""; break;
					case '�': s="c"; break;
					case '�': s="n";break;
					case '�': s="C"; break;
					case '�': s="N";break;
					case '�': s="a";break;
					case '�': s="a";break;
					case '�': s="a";break;
					case '�': s="e";break;
					case '�': s="e";break;
					case '�': s="e";break;
					case '�': s="i";break;
					case '�': s="i";break;
					case '�': s="i";break;
					case '�': s="o";break;
					case '�': s="o";break;
					case '�': s="o";break;
					case '�': s="u";break;
					case '�': s="u";break;
					case '�': s="u";break;
					case '�': s="A";break;
					case '�': s="A";break;
					case '�': s="A";break;
					case '�': s="E";break;
					case '�': s="E";break;
					case '�': s="E";break;
					case '�': s="I";break;
					case '�': s="I";break;
					case '�': s="I";break;
					case '�': s="O";break;
					case '�': s="O";break;
					case '�': s="O";break;
					case '�': s="U";break;
					case '�': s="U";break;
					case '�': s="U";break;
				}
				if (bSpecial && s==null && Character.getNumericValue(c)<0) s="";
				
				if(s!=null)
					filtered.append(s);
				else
					filtered.append(c);
			}
			result=filtered.substring(0);
		}
		return result;
	}
	    
	public static String textToHTML(String sText){
		String sHTML = "";
		if (sText!=null){
			StringTokenizer stLines = new StringTokenizer(sText,"\r\n");
			while (stLines.hasMoreElements()){
				String sLine = stLines.nextToken();
				sHTML += sLine+"<BR/>";
			}
		}
		return sHTML;
	}

	public static String htmlToText(String sHTML){
		String sText = "";
		if (sHTML!=null){
			int i = 0;
			while (i<sHTML.length()){
				char c = sHTML.charAt(i);
				switch (c){
					case '<':
						i++;
						int iEndTag = sHTML.indexOf(">", i);
						String sTagName = sHTML.substring(i, iEndTag);
						if (!sTagName.startsWith("/")){
							
						}
						i = iEndTag+1;
						break;
					//case '\'' | '\"':
					//	sText += "\\"+c;
					//	i++;	
					//	break;
					default:
						sText += c;
						i++;
						break;
				}
			}
		}
		return sText;
	}
	
	public static boolean isHTML(String sText){
		boolean bHTML = false;
		if (sText!=null){
			bHTML = sText.indexOf("<br")>=0 || sText.indexOf("<p")>=0 || sText.indexOf("</")>=0;
		}
		return bHTML;
	}
	
	public static boolean isNumber(String sText){
		boolean b = false;
		try{
			Double.parseDouble(sText);
			b = true;
		}catch (Exception e){
		}
		return b;
	}
	
}
