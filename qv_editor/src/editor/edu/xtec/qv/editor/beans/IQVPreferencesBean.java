/*
 * QVPreferencesBean.java
 * 
 * Created on 03/juny/2004
 */
package edu.xtec.qv.editor.beans;

import java.util.Vector;

/**
 * @author sarjona
 */
public interface IQVPreferencesBean {
	
	public static final String A_SAVE_SETTINGS = "save_settings";

	public static final String P_LOOK_AND_FEEL = "lookAndFeel";
	public static final String P_AUTOSAVE = "autosave";
	public static final String P_BACKUP = "backup";
	public static final String P_EDITXML = "editXML";
	public static final String P_HELP = "help";
	
	public void updateSettings();

	public void saveSettings();
	
	public String getLanguage();

	public String getDefaultSkin();

	public Vector getSkins();

	public boolean isAutosave();

	public int getNumBackups();
	
	public boolean isEditableXML();

	public boolean showHelp();
	
}
