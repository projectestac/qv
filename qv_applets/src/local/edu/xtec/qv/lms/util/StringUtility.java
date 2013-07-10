package edu.xtec.qv.lms.util;

public class StringUtility{

	public static String filter(String input){
		//Copiat de LocalQVApplet
		String result=(input==null ? "" : input);
		if(input!=null && input.length()>0){
			StringBuffer filtered = new StringBuffer(input.length());
			char c;
			for(int i=0; i<input.length(); i++) {
				c = input.charAt(i);
				String s=null;
				switch(c){
					case ' ': s="_";break;
					case '+': s="_";break;
					case '<': s="";	break;
					case '>': s="";	break;
					case '"': s="";	break;
					case '\'': s="";break;
					case '´': s=""; break;
					case '`': s=""; break;
					case '¨': s=""; break;
					case '&': s="";	break;
					case '\\': s="";break;
					case '/': s="";	break;
					case ':': s="";	break;
					case '*': s="";	break;
					case '¿': s="";	break;
					case '?': s="";	break;
					case '!': s="";	break;
					case '$': s=""; break;
					case 'ç': s="c"; break;
					case 'Ç': s="C"; break;
					case 'ñ': s="n"; break;
					case 'Ñ': s="n"; break;
					case 'á': s="a";break;
					case 'à': s="a";break;
					case 'ä': s="a";break;
					case 'é': s="e";break;
					case 'è': s="e";break;
					case 'ë': s="e";break;
					case 'í': s="i";break;
					case 'ì': s="i";break;
					case 'ï': s="i";break;
					case 'ó': s="o";break;
					case 'ò': s="o";break;
					case 'ö': s="o";break;
					case 'ú': s="u";break;
					case 'ù': s="u";break;
					case 'ü': s="u";break;
					case 'Á': s="A";break;
					case 'À': s="A";break;
					case 'Ä': s="A";break;
					case 'É': s="E";break;
					case 'È': s="E";break;
					case 'Ë': s="E";break;
					case 'Í': s="I";break;
					case 'Ì': s="I";break;
					case 'Ï': s="I";break;
					case 'Ó': s="O";break;
					case 'Ò': s="O";break;
					case 'Ö': s="O";break;
					case 'Ú': s="U";break;
					case 'Ù': s="U";break;
					case 'Ü': s="U";break;
				}
				if(s!=null)
					filtered.append(s);
				else
					filtered.append(c);
			}
			result=filtered.substring(0);
		}
		return result.toLowerCase();
	}
	
}