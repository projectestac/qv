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
					case '�': s="C"; break;
					case '�': s="n"; break;
					case '�': s="n"; break;
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