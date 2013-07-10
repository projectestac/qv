/*
 * QVConstants.java
 * 
 * Created on 11/febrer/2004
 */
package edu.xtec.qv.editor.util;

/**
 * @author sarjona
 */
public interface QVConstants {
	
	public static final String URL_PATH_SEPARATOR = "/";
	public static final String LANG = "lang";
	public final String DEFAULT_START_TOKEN = "'{";
	public final String DEFAULT_END_TOKEN = "'}";


	// Properties
	public static final String SETTINGS_PATH="/edu/xtec/resources/properties/";
	public static final String SETTINGS_FILE_USER="editorQV.properties";
	public static final String SETTINGS_FILE="qv_editor.properties";
	public static final String DEFAULT_BASE_NAME_BUNDLE="edu.xtec.resources.messages.editorQVMessages";
	public static final String CHECK_IDENT_URL_KEY="validation.URL";
	public static final String QVSTORE_LOCAL_URL_KEY="store.localURL";
	public static final String QVSTORE_REMOTE_URL_KEY="store.remoteURL";
	public static final String QVSTORE_REPOSITORY_PATH_KEY = "store.repositoryPath";
	public static final String HOME_QV_URL_KEY="qv.homeURL";
	public static final String ASSIGNACIONS_QV_URL_KEY="qv.assignacionsServlet";
	public static final String QUADERN_QV_URL_KEY="qv.quadernServlet";
	public static final String BASE_NAME_BUNDLE_KEY="lang.baseNameBundle";
	public static final String DEFAULT_QUOTA_KEY="store.defaultQuota";
	public static final String SETTING_LIST_KEY = "setting.list";
	public static final String DEFAULT_LANGUAGE_KEY="setting.language";
	public static final String DEFAULT_SKIN_KEY = "setting.skin.default";
	public static final String PUBLIC_SKINS_KEY = "setting.skin.public";
	public static final String AUTOSAVE_KEY = "setting.autosave";
	public static final String BACKUP_KEY = "setting.backup";
	public static final String EDITXML_KEY = "setting.editXML";
	public static final String HELP_KEY = "setting.help";
	public static final String XML_PARSER_DRIVER = "xml.parserDriver";
	public static final String XML_SCHEMA_QTI_KEY = "xml.schemaQTI";
	public static final String START_TOKEN_KEY = "lang.startToken";
	public static final String END_TOKEN_KEY = "lang.endToken";

	// JSPs
	public static final String BEANS_PACKAGE = "edu.xtec.qv.editor.beans";
	public static final String ERROR_PAGE="error.html";
	public static final String JSP_EDIT="edit";
	public static final String JSP_INDEX="index";
	public static final String JSP_PREFERENCIES="preferencies";
	public static final String JSP_EDITORXML="editorXML";
	public static final String JSP_QUADERN="quadern";
	public static final String JSP_LOGIN="login";
	//public static final String JSP_QUADERN="edit.jsp?type=QVQuadernBean&page=quadern";
	public static final String JSP_FULL="full";
	public static final String JSP_PREGUNTA="pregunta";
	public static final String JSP_PREGUNTA_SELECCIO="pregunta_seleccio";
	public static final String JSP_PREGUNTA_CLOZE="pregunta_cloze";
	public static final String JSP_PREGUNTA_ORDENACIO="pregunta_ordenacio";
	public static final String JSP_PREGUNTA_DRAGDROP="pregunta_dragdrop";
	public static final String JSP_PREGUNTA_HOTSPOT="pregunta_hotspot";
	public static final String JSP_PREGUNTA_DRAW="pregunta_draw";

	// Parametres request
	public static final String PAGE_PARAM = "page";
	public static final String LOAD_PAGE_PARAM = "loadPage";
	public static final String NOU_QUADERN_PARAM="nou_quadern";
	public static final String ID_QUADERN_PARAM="id_quadern";
	public static final String ID_FITXER_PARAM="id_fitxer";
	public static final String NOU_FITXER_PARAM="fitxer";
	public static final String IDENT_FULL_PARAM="ident_full";
	public static final String TITLE_FULL_PARAM="title_full";
	public static final String IDENT_PREGUNTA_PARAM="ident_pregunta";
	public static final String ACTION_PARAM="action";

	// Accio
	public static final String ADD_QUADERN_ACTION_PARAM="add_quadern";
	public static final String SET_QUADERN_ACTION_PARAM="set_quadern";
	public static final String SAVE_QUADERN_ACTION_PARAM="save_quadern";
	public static final String SAVE_XML_ACTION_PARAM="save_xml";
	public static final String DEL_QUADERN_ACTION_PARAM="del_quadern";
	public static final String EXPORT_QUADERN_ACTION_PARAM="export_quadern";
	public static final String ADD_FITXER_ACTION_PARAM="add_fitxer";
	public static final String DEL_FITXER_ACTION_PARAM="del_fitxer";
	public static final String ADD_FULL_ACTION_PARAM="add_full";
	public static final String SET_FULL_ACTION_PARAM="set_full";
	public static final String DEL_FULL_ACTION_PARAM="del_full";
	public static final String DEL_PREGUNTA_ACTION_PARAM = "del_pregunta";
	public static final String UP_ACTION_PARAM = "up";
	public static final String UP_ITEM_ACTION_PARAM = "up_item";
	public static final String DOWN_ACTION_PARAM = "down";
	public static final String DOWN_ITEM_ACTION_PARAM = "down_item";
	public static final String DEL_ACTION_PARAM = "del";
	public static final String PREVIEW_ACTION_PARAM = "preview";
	public static final String LOGOUT_ACTION_PARAM = "logout";
	
	
	public static final String TITLE_PARAM = "title";
	public static final String SCOREMODEL_PARAM = "scoremodel";
	public static final String SECTION_ORDER_PARAM = "ordre_full";//Albert
	public static final String ITEM_ORDER_PARAM = "ordre_pregunta";//Albert
	
	
	// Parametres sessio
	public static final String QTI_OBJECT_SESSION = "qti_object";
	public static final String REDIRECT_PAGE_SESSION = "redirect";

	// Tipus de pregunta
	public static final String SELECTION_TIPUS_PREGUNTA = "selection";
	public static final String CLOZE_TIPUS_PREGUNTA = "cloze";
	public static final String ORDERED_TIPUS_PREGUNTA = "Ordered";
	public static final String DRAGDROP_TIPUS_PREGUNTA = "drag_drop";
	public static final String HOTSPOT_TIPUS_PREGUNTA = "hotspot";
	public static final String DRAW_TIPUS_PREGUNTA = "draw";

	// Tipus de hotspot
	public static final String ZONE_HOTSPOT_TYPE = "hotspot.zone";
	public static final String OPTION_HOTSPOT_TYPE = "hotspot.option";
	public static final String DOT_HOTSPOT_TYPE = "hotspot.dot";
	public static final String FREE_HOTSPOT_TYPE = "hotspot.free";
	
	public static final String NO = "No";
	public static final String YES = "Yes";
		
	// Tipus de recursos
	public final String IMAGE_RESOURCE = "image";
	public final String FLASH_RESOURCE = "flash";
	public final String AUDIO_RESOURCE = "audio";
	public final String VIDEO_RESOURCE = "video";
	public final String JCLIC_RESOURCE = "jclic";
	public final String APPLICATION_RESOURCE = "application";
	public final String REST_RESOURCE = "rest";

	// Mode d'edició
	public final String VISUAL_EDITION_MODE = "visual";
	public final String TEXT_EDITION_MODE = "text";
	
	
}
