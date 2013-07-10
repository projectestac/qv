/*
 * QVBoundedListControl.java
 * 
 * Created on 10-ene-2005
 */
package edu.xtec.qv.editor.util;

import java.awt.Point;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.QTIObject;

/**
 * @author sarjona
 */
public class QVBoundedListControl extends QVListControl{

	public QVBoundedListControl(HttpServletRequest request){
		super(request);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getListObjects(edu.xtec.qv.qti.QTIObject)
	 */
	public Vector getListObjects(QTIObject oQTI) {
		Vector vListObjects = new Vector();
		Vector vObjects = getObjects(oQTI);
		int index = 0;
		Enumeration enumObjects = vObjects.elements();
		while (enumObjects.hasMoreElements()){
			Point o = (Point)enumObjects.nextElement();
			ListObject lo = getListObject(o, index);
			vListObjects.addElement(lo);
			index++;
		}
		return vListObjects;
	}

	protected ListObject getListObject(Point o, int index){
		ListObject lo = null;
		if (o!=null){
			lo = new ListObject("point_"+index, o.x+","+o.y);
		}
		return lo;		
	}


	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getObject(QTIObject oQTI, String sIdent) {
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getPreviousObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getPreviousObject(QTIObject oQTI, String sIdent) {
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getNextObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getNextObject(QTIObject oQTI, String sIdent) {
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#addListObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public void addListObject(QTIObject oQTI, String sIdent) {
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#delListObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public void delListObject(QTIObject oQTI, String sIdent) {
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#setListObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	protected void setListObject(QTIObject oQTI, String sIdent) {
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#upListObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public void upListObject(QTIObject oQTI, String sIdent) {
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#downListObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public void downListObject(QTIObject oQTI, String sIdent) {
	}

	private static Vector getObjects(QTIObject oQTI){
		Vector vObjects = new Vector();
		if (oQTI instanceof Item){
		}
		return vObjects;
	}	


}
