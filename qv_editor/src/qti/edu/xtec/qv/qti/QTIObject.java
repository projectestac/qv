/*
 * QTIObject.java
 * 
 * Created on 03/febrer/2004
 */
package edu.xtec.qv.qti;

import java.util.Vector;

import org.jdom.Element;

import edu.xtec.qv.qti.util.QTIConstant;

/**
 * @author sarjona
 */
public interface QTIObject {
	
	// Multiplicity
	public final String ZERO_OR_ONE = "zero_or_one";
	public final String ZERO_OR_MORE = "zero_or_more";

	// Elements i atributs dels objectes QTI
	public static final QTIConstant ACTION = new QTIConstant("action");
	public static final QTIConstant ALIGN = new QTIConstant("align");
	public static final QTIConstant AND = new QTIConstant("and");
	public static final QTIConstant APPLICATIONTYPE = new QTIConstant("applicationtype");
	public static final QTIConstant APPTYPE = new QTIConstant("apptype");
	public static final QTIConstant ASSESSFEEDBACK = new QTIConstant("assessfeedback", "Assessfeedback", ZERO_OR_MORE);
	public static final QTIConstant ASSESSMENT = new QTIConstant("assessment");
	public static final QTIConstant ASSESSMENTCONTROL = new QTIConstant("assessmentcontrol", "AssessmentControl", ZERO_OR_ONE);
	public static final QTIConstant ASSESSPROC_EXTENSION = new QTIConstant("assessproc_extension", "AssessprocExtension", ZERO_OR_ONE);
	public static final QTIConstant AUDIOTYPE = new QTIConstant("audiotype");
	public static final QTIConstant CASE = new QTIConstant("case");
	public static final QTIConstant CHARSET = new QTIConstant("charset");
	public static final QTIConstant COLUMNS = new QTIConstant("columns");
	public static final QTIConstant CONDITIONVAR = new QTIConstant("conditionvar");
	public static final QTIConstant CONTENT = new QTIConstant("content");
	public static final QTIConstant CONTINUE = new QTIConstant("continue");
	public static final QTIConstant CUTVALUE = new QTIConstant("cutvalue");
	public static final QTIConstant DECVAR = new QTIConstant("decvar");
	public static final QTIConstant DEFAULTVAL = new QTIConstant("defaultval");
	public static final QTIConstant DISPLAY = new QTIConstant("display");
	public static final QTIConstant DISPLAYFEEDBACK = new QTIConstant("displayfeedback");
	public static final QTIConstant DURATION = new QTIConstant("duration", "Duration", ZERO_OR_ONE);
	public static final QTIConstant EMBEDDED = new QTIConstant("embedded");
	public static final QTIConstant ENABLE_ROTATING = new QTIConstant("enable_rotating");
	public static final QTIConstant ENABLE_SCALING = new QTIConstant("enable_scaling");
	public static final QTIConstant ENCODING = new QTIConstant("encoding");
	public static final QTIConstant ENTITYREF = new QTIConstant("entityref");
	public static final QTIConstant FEEDBACKSWITCH = new QTIConstant("feedbackswitch");
	public static final QTIConstant FEEDBACKTYPE = new QTIConstant("feedbacktype");
	public static final QTIConstant FIBTYPE = new QTIConstant("fibtype");
	public static final QTIConstant FIELDENTRY = new QTIConstant("fieldentry");
	public static final QTIConstant FIELDLABEL = new QTIConstant("fieldlabel");
	public static final QTIConstant FIRST_MATERIAL = new QTIConstant("first_material");
	public static final QTIConstant FITTARGET = new QTIConstant("fit_target");
	public static final QTIConstant FLOW = new QTIConstant("flow");
	public static final QTIConstant FLOW_LABEL = new QTIConstant("flow_label");
	public static final QTIConstant FLOW_MAT = new QTIConstant("flow_mat");
	public static final QTIConstant HEIGHT = new QTIConstant("height");
	public static final QTIConstant HINTSWITCH = new QTIConstant("hintswitch");
	public static final QTIConstant IDENT = new QTIConstant("ident");
	public static final QTIConstant IMAGTYPE = new QTIConstant("imagtype");
	public static final QTIConstant IMS_RENDER_OBJECT = new QTIConstant("ims_render_object");
	public static final QTIConstant INDEX = new QTIConstant("index");
	public static final QTIConstant INITPARAM = new QTIConstant("initparam");
	public static final QTIConstant INSIDE = new QTIConstant("inside");
	public static final QTIConstant INTERACTIONSWITCH = new QTIConstant("interactionswitch");
	public static final QTIConstant ITEM = new QTIConstant("item", "Item", ZERO_OR_MORE);
	public static final QTIConstant ITEMCONTROL = new QTIConstant("itemcontrol", "ItemControl", ZERO_OR_ONE);
	public static final QTIConstant ITEMFEEDBACK = new QTIConstant("itemfeedback", "Itemfeedback", ZERO_OR_MORE);
	public static final QTIConstant ITEMPROC_EXTENSION = new QTIConstant("itemproc_extension", "ItemprocExtension", ZERO_OR_ONE);
	public static final QTIConstant LABEL = new QTIConstant("label");
	public static final QTIConstant LABELREFID = new QTIConstant("labelrefid"); 
	public static final QTIConstant LANGUAGE = new QTIConstant("language");
	public static final QTIConstant LAST_MATERIAL = new QTIConstant("last_material");
	public static final QTIConstant LINKREFID = new QTIConstant("linkrefid");
	public static final QTIConstant MAP_OUTPUT = new QTIConstant("map_output");
	public static final QTIConstant MAT_EXTENSION = new QTIConstant("mat_extension");
	public static final QTIConstant MATAPPLET = new QTIConstant("matapplet");
	public static final QTIConstant MATAUDIO = new QTIConstant("mataudio");
	public static final QTIConstant MATBREAK = new QTIConstant("matbreak");
	public static final QTIConstant MATCH_GROUP = new QTIConstant("match_group");
	public static final QTIConstant MATCH_MAX = new QTIConstant("match_max");
	public static final QTIConstant MATEMTEXT = new QTIConstant("matemtext");
	public static final QTIConstant MATERIAL = new QTIConstant("material");
	public static final QTIConstant MATHTML = new QTIConstant("mathtml");
	public static final QTIConstant MATIMAGE = new QTIConstant("matimage");
	public static final QTIConstant MATJCLIC = new QTIConstant("matjclic");
	public static final QTIConstant MATAPPLICATION = new QTIConstant("matapplication");
	public static final QTIConstant MATLATEX = new QTIConstant("matlatex");
	public static final QTIConstant MATTEXT = new QTIConstant("mattext");
	public static final QTIConstant MATVIDEO = new QTIConstant("matvideo");
	public static final QTIConstant MAXATTEMPTS	= new QTIConstant("maxattempts");
	public static final QTIConstant MAXCHARS = new QTIConstant("maxchars");
	public static final QTIConstant MAXNUMBER = new QTIConstant("maxnumber");
	public static final QTIConstant MAXVALUE = new QTIConstant("maxvalue");
	public static final QTIConstant MDNAME = new QTIConstant("mdname");//Albert
	public static final QTIConstant MDOPERATOR = new QTIConstant("mdoperator");//Albert
	public static final QTIConstant MEMBERS = new QTIConstant("members");
	public static final QTIConstant MINNUMBER = new QTIConstant("minnumber");
	public static final QTIConstant MINVALUE = new QTIConstant("minvalue");
	public static final QTIConstant NOT = new QTIConstant("not");
	public static final QTIConstant NRATIO = new QTIConstant("nratio");
	public static final QTIConstant NROTATE = new QTIConstant("nrotate");
	public static final QTIConstant NUM_COLORS = new QTIConstant("num_colors");
	public static final QTIConstant NUMTYPE = new QTIConstant("numtype");
	public static final QTIConstant OBJECTIVES = new QTIConstant("objectives");	
	public static final QTIConstant OR = new QTIConstant("or");
	public static final QTIConstant OR_SELECTION = new QTIConstant("or_selection", "OrSelection", ZERO_OR_MORE);//ALbert
	public static final QTIConstant ORDER = new QTIConstant("order");
	public static final QTIConstant ORIENTATION  = new QTIConstant("orientation");
	public static final QTIConstant OUTCOMES = new QTIConstant("outcomes");
	public static final QTIConstant OUTCOMES_PROCESSING = new QTIConstant("outcomes_processing", "OutcomesProcessing", ZERO_OR_MORE);
	public static final QTIConstant PRESENTATION = new QTIConstant("presentation", "Presentation", ZERO_OR_ONE);
	public static final QTIConstant PRESENTATION_MATERIAL = new QTIConstant("presentation_material", "PresentationMaterial", ZERO_OR_ONE);
	public static final QTIConstant PROMPT = new QTIConstant("prompt");
	public static final QTIConstant QTICOMMENT = new QTIConstant("qticomment", "QTIComment", ZERO_OR_ONE);
	public static final QTIConstant QTIMETADATA = new QTIConstant("qtimetadata", "QTIMetadata", ZERO_OR_MORE);
	public static final QTIConstant QTIMETADATAFIELD = new QTIConstant("qtimetadatafield");
	public static final QTIConstant QUESTESTINTEROP = new QTIConstant("questestinterop");
	public static final QTIConstant RAREA = new QTIConstant("rarea");
	public static final QTIConstant RCARDINALITY = new QTIConstant("rcardinality");
	public static final QTIConstant REFERENCE = new QTIConstant("reference", "Reference", ZERO_OR_ONE);
	public static final QTIConstant RENDER = new QTIConstant("render");
	public static final QTIConstant RENDER_EXTENSION = new QTIConstant("render_extension");
	public static final QTIConstant RENDER_CHOICE = new QTIConstant("render_choice");
	public static final QTIConstant RENDER_DRAW = new QTIConstant("render_draw");
	public static final QTIConstant RENDER_FIB = new QTIConstant("render_fib");
	public static final QTIConstant RENDER_HOTSPOT = new QTIConstant("render_hotspot");
	public static final QTIConstant RESPCONDITION = new QTIConstant("respcondition");
	public static final QTIConstant RESPIDENT = new QTIConstant("respident");
	public static final QTIConstant RESPONSE_LABEL = new QTIConstant("response_label");
	public static final QTIConstant RESPONSE_GRP = new QTIConstant("response_grp");
	public static final QTIConstant RESPONSE_LID = new QTIConstant("response_lid");
	public static final QTIConstant RESPONSE_NUM = new QTIConstant("response_num");	
	public static final QTIConstant RESPONSE_STR = new QTIConstant("response_str");
	public static final QTIConstant RESPONSE_XY = new QTIConstant("response_xy");
	public static final QTIConstant RESPROCESSING = new QTIConstant("resprocessing", "Resprocessing", ZERO_OR_MORE);
	public static final QTIConstant ROTATE = new QTIConstant("rotate");
	public static final QTIConstant ROWS = new QTIConstant("rows");
	public static final QTIConstant RRANGE = new QTIConstant("rrange");
	public static final QTIConstant RTIMING = new QTIConstant("rtiming");
	public static final QTIConstant RUBRIC = new QTIConstant("rubric");
	public static final QTIConstant SCOREMODEL = new QTIConstant("scoremodel");
	public static final QTIConstant SECTION = new QTIConstant("section", "Section", ZERO_OR_MORE);
	public static final QTIConstant SECTIONCONTROL = new QTIConstant("sectioncontrol", "SectionControl", ZERO_OR_ONE);
	public static final QTIConstant SECTIONFEEDBACK = new QTIConstant("sectionfeedback", "Sectionfeedback", ZERO_OR_MORE);
	public static final QTIConstant SECTIONPROC_EXTENSION = new QTIConstant("sectionproc_extension", "SectionprocExtension", ZERO_OR_ONE);	
	public static final QTIConstant SELECTION = new QTIConstant("selection", "Selection", ZERO_OR_ONE);//Albert
	public static final QTIConstant SELECTION_METADATA = new QTIConstant("selection_metadata", "SelectionMetadata", ZERO_OR_MORE);//ALbert
	public static final QTIConstant SELECTION_ORDERING  = new QTIConstant("selection_ordering", "SelectionOrdering", ZERO_OR_ONE);
	public static final QTIConstant SETMATCH = new QTIConstant("setmatch");
	public static final QTIConstant SETVAR = new QTIConstant("setvar");
	public static final QTIConstant SHOWDRAW = new QTIConstant("showdraw");
	public static final QTIConstant SHOWOPTIONS = new QTIConstant("showoptions");
	public static final QTIConstant SHOWTARGETS = new QTIConstant("showTargets");
	public static final QTIConstant SHUFFLE = new QTIConstant("shuffle");
	public static final QTIConstant SOLUTIONSWITCH = new QTIConstant("solutionswitch");
	public static final QTIConstant STYLE = new QTIConstant("style");
	public static final QTIConstant URI = new QTIConstant("uri");
	public static final QTIConstant TEXT = new QTIConstant("text");
	public static final QTIConstant TEXTTYPE = new QTIConstant("texttype");
	public static final QTIConstant TITLE = new QTIConstant("title");
	public static final QTIConstant TRANSP = new QTIConstant("transp");
	public static final QTIConstant TYPE = new QTIConstant("type");
	public static final QTIConstant VAREQUAL = new QTIConstant("varequal");
	public static final QTIConstant VARNAME = new QTIConstant("varname");
	public static final QTIConstant VARSUBSET = new QTIConstant("varsubset");
	public static final QTIConstant VARTYPE = new QTIConstant("vartype");
	public static final QTIConstant VIDEOTYPE = new QTIConstant("videotype");
	public static final QTIConstant VIEW = new QTIConstant("view");
	public static final QTIConstant WIDTH = new QTIConstant("width");
	public static final QTIConstant X0 = new QTIConstant("x0");
	public static final QTIConstant XMLLANG = new QTIConstant("xml:lang");
	public static final QTIConstant Y0 = new QTIConstant("y0");
	
	
	// Algoritmes de correccio
	public final String NUMBER_CORRECT = "NumberCorrect";
	public final String NUMBER_CORRECT_ATTEMPTED = "NumberCorrectAttempted";
	public final String SUM_OF_SCORES = "Sumofscores";
	public final String SUM_OF_SCORES_ATTEMPTED  = "SumofscoresAttempted";
	
	// Alineacio
	public final String VERTICAL = "vertical";
	public final String HORIZONTAL = "horizontal";
	
	/**
	 * @return representació XML associada a l'objecte QTI
	 */
	public Element getXML();
	
	/**
	 * Crea l'objecte QTI associat a l'element XML indicat com a parametre
	 * @param eElement element XML del que es vol obtenir la representacio 
	 */
	public void createFromXML(Element eElement);
	
	/**
	 * @param sName nom de l'atribut
	 * @return valor associat a l'atribut indicat
	 */
	public Object getAttribute(String sName);
	
	/**
	 * @param oConstant QTIConstant que conté el nom de l'atribut
	 * @return valor associat a l'atribut indicat
	 */
	public Object getAttribute(QTIConstant oConstant);
	
	/**
	 * @param sName nom de l'atribut
	 * @return String associat a l'atribut indicat
	 */
	public String getAttributeValue(String sName);
	
	/**
	 * @param sName nom de l'atribut
	 * @return Vector associat a l'atribut indicat
	 */
	public Vector getAttributeVectorValue(String sName);
				
	/**
	 * @param oConstant QTIConstant que conté el nom de l'atribut
	 * @return Vector associat a l'atribut indicat
	 */
	public Vector getAttributeVectorValue(QTIConstant oConstant);
				
	/**
	 * @param sName nom de l'atribut
	 * @param sIdent identificador de l'objecte que es vol obtenir
	 * @return objecte que te l'identificador indicat
	 */
	public Object getAttribute(String sName, String sIdent);

	/**
	 * Modifica el valor de l'atribut indicat; si el valor es una Coleccio (com per exemple
	 * un Vector) comprova si l'objecte a afegir ha de substituir aquesta coleccio o be 
	 * s'ha d'afegir dins la colecció.
	 * @param sName nom de l'atribut
	 * @param sValue nou valor de l'atribut
	 */
	public void setAttribute(String sName, Object sValue);
	
	/**
	 * Modifica el valor de l'atribut indicat; si el valor es una Coleccio (com per exemple
	 * un Vector) comprova si l'objecte a afegir ha de substituir aquesta coleccio o be 
	 * s'ha d'afegir dins la colecció.
	 * @param oConstant QTIConstant que conté el nom de l'atribut
	 * @param sValue nou valor de l'atribut
	 */
	public void setAttribute(QTIConstant oConstant, Object sValue);
	
	/**
	 * Elimina el valor de l'atribut indicat
	 * @param sName nom de l'atribut a eliminar
	 */
	public void delAttribute(String sName);
	
	/**
	 * Elimina l'element indicat de l'atribut. Es fa servir quan el valor de l'atribut
	 * es una Collection
	 * @param sName nom de l'atribut
	 * @param oElement element que s'ha d'eliminar de la llista de valors de l'atribut
	 */
	public void delAttributeElement(String sName, Object oElement);
	
	/**
	 * @return l'identificador de l'element QTI 
	 */
	public String getIdent();

	/**
	 * Modifica l'identificador
	 */
	public void setIdent(String sIdent);

	/**
	 * @return l'atribut title de l'element QTI 
	 */
	public String getTitle();

	/**
	 * @return el nom que es mostrarà de l'objecte QTI 
	 */
	public String getShowName();
	
	public Vector getContents();
	
}
