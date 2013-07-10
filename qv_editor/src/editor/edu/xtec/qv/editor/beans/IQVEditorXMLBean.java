/*
 * QVEditorXMLBean.java
 * 
 * Created on 14/maig/2004
 */
package edu.xtec.qv.editor.beans;

import java.util.Hashtable;
import java.util.Vector;


/**
 * @author sarjona
 */
public interface IQVEditorXMLBean {

	public int getNumberLines(String sXML);
	
	public Vector getErrorLines();
	
	public boolean isErrorLine(int iLine);

	public Hashtable getErrors();
	
	public String getErrors(int iLine);

	public Vector getError(int iLine);

	public String QTIObjectToXML();
		
}
