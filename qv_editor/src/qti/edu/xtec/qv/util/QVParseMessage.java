/*
 * QVParseMessage.java
 * 
 * Created on 18/maig/2004
 */
package edu.xtec.qv.util;

import org.apache.log4j.Logger;

/**
 * @author sarjona
 */
public class QVParseMessage {
	
	protected static Logger logger = Logger.getRootLogger();

	protected int iLine;
	protected String sCode;
	protected String sMessage;
	
	public QVParseMessage(int iLine, String sError){
		this.iLine = iLine;
		this.sCode = getCode(sError);
		this.sMessage = getMessage(sError);
	}
	
	public int getLine(){
		return iLine;
	}
	
	public String getCode(){
		return sCode;
	}
	
	public String getMessage(){
		return sMessage;
	}
	
	protected String getCode(String sError){
		String sCode = null;
		if (sError!=null && sError.indexOf(":")>=0){
			sCode = sError.substring(0, sError.indexOf(":"));
		}
		return sCode;
	}

	protected String getMessage(String sError){
		String sMessage = sError;
		if (sError!=null && sError.indexOf(":")>=0){
			sMessage = sError.substring(sError.indexOf(":")+1);
		}
		return sMessage;
	}
	
	protected String replace(String sMessage){
		String sReplaced = sMessage;
		return sReplaced;
	}
		
	public String toString(){
		String sMessage = getMessage();
		return sMessage;
	}
}
