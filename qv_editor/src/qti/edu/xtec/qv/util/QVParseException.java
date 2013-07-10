/*
 * QVParseException.java
 * 
 * Created on 18/maig/2004
 */
package edu.xtec.qv.util;

import java.util.Hashtable;
import java.util.Vector;

/**
 * @author sarjona
 */
public class QVParseException extends Exception{
	
	private Vector vLines;
	private Hashtable hErrors;

	public QVParseException(Vector vLines, Hashtable hErrors){
		this.vLines = vLines;
		this.hErrors = hErrors;
	}

	public Vector getErrorLines(){
		return vLines;
	}
	
	public Hashtable getErrors(){
		return hErrors;
	}

	public String toString(){
		return "QVParseException: "+getErrors();
	}
}
