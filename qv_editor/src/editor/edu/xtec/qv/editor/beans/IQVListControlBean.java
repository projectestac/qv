/*
 * IQVListControlBean.java
 * 
 * Created on 04/juny/2004
 */
package edu.xtec.qv.editor.beans;

import java.util.Vector;

/**
 * @author sarjona
 */
public interface IQVListControlBean {
	
	// List types
	public static final String MATERIAL_TYPE = "material";
	public static final String METADATA_TYPE = "metadata";
	public static final String OBJECTIVE_TYPE = "objective";
	public static final String ORDERED_RESPONSE_TYPE = "ordered_response";
	public static final String CLOZE_RESPONSE_TYPE = "cloze_response";
	public static final String TARGET_TYPE = "target";
	public static final String SOURCE_TYPE = "source";
	public static final String HOTSPOT_TYPE = "hotspot";
	public static final String BOUNDED_TYPE = "bounded";
	public static final String DOT_RESPONSE_TYPE = "dot_response";
	public static final String DRAGDROP_RESPONSE_TYPE = "dragdrop_response";
	public static final String DRAGDROP_POSITION_TYPE = "dragdrop_position";
	
	// Actions
	public static final String A_ADD_LIST_OBJECT = "add_list_object";
	public static final String A_DEL_LIST_OBJECT = "del_list_object";
	public static final String A_SET_LIST_OBJECT = "set_list_object";
	public static final String A_UP_LIST_OBJECT = "up_list_object";
	public static final String A_DOWN_LIST_OBJECT = "down_list_object";
	public static final String A_SAVE_LIST_OBJECT = "save_list_object";
	
	
	public static final String P_LIST_TYPE = "p_list_type";
	public static final String P_LIST_ACTION = "p_list_action";
	
	
	/**
	 * Gets object's data to display in the list  
	 * @param sType list type (metadata, objectives, ...)
	 * @return Vector of <code>ListObject</code>
	 */
	public Vector getListObjects(String sType);
	
	public Object getObject(String sType, String sIdent);
	
	public Object getPreviousObject(String sType, String sIdent);

	public Object getNextObject(String sType, String sIdent);

	public String getCurrentListObject(String sType);

}
