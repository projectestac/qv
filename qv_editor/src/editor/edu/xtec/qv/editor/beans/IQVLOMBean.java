package edu.xtec.qv.editor.beans;

import java.util.Vector;

public interface IQVLOMBean {
	
	public final static String SAVE_LOM_PARAM = "save_lom";

	public final static String P_AUTHOR = "p_author";
	public final static String P_TITLE = "p_title";
	public final static String P_DESCRIPTION = "p_description";
	public final static String P_LANGUAGE = "p_language";
	public final static String P_LEVEL = "p_level";
	public final static String P_AREA = "p_area";
	public final static String P_COPYRIGHT = "p_copyright";
	public final static String P_LICENSE = "p_license";

	
	public boolean existsLOM();
	
	public String getLOMAuthor();
	public void addLOMAuthor(String sAuthor);

	public String getLOMTitle();
	public void addLOMTitle(String sTitle);

	public String getLOMDescription();
	public void addLOMDescription(String sDescription);

	public void addLOMLanguage(String sLanguage);
	public String getLOMLanguage();
	
	public Vector getNivells();
	public Vector getLOMEducationalLevels();
	
	public Vector getArees();
	public String getLOMArea();
	
	public Vector getIdiomes();
	//public Vector getNivells();

	public String getLOMCopyright();
	public String getLOMLicense();
	
}
