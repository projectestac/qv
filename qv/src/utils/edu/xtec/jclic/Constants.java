/*
 * Constants.java
 *
 * Created on 31 de julio de 2002, 9:35
 */

package edu.xtec.jclic;

import java.awt.Color;
import java.awt.RenderingHints;

/**
 *
 * @author Francesc Busquets (fbusquets@pie.xtec.es)
 */
public interface Constants {
    
    // String constants used in applet parameters and common properties
    public static final String
    DEFAULT_BUNDLE="messages.JClicMessages",
    LOGO_ICON="icons/logo.gif";
    
    public static final String
    PROGRAM="JClic",
    VERSION_STR="Ver. 1.0 beta 02 - 01/03/2002",
    COOKIE="cookie",
    EXIT_URL="exitUrl",
    URL_BASE="urlBase",
    INFO_URL_FRAME="infoUrlFrame",
    COMPRESS_IMAGES="compressImages",
    PRE_DRAW_IMAGES="preDrawImages",
    SKIN="skin",
    REPORTER_CLASS="reporter",
    REPORTER_PARAMS="reporterParams",
    SYSTEM_SOUNDS="systemSounds",
    AUDIO_ENABLED="audioEnabled",
    NAV_BUTTONS_ALWAYS="navButtonsAlways",
    TRACE="trace",
    //CURRENT_PROJECT_VERSION="currentProjectVersion",
    TRUE="true",
    FALSE="false",
    DEFAULT="default",
    ENABLED="enabled",
    ID="id",
    QT="QuickTime",
    JMF="Java Media Framework",
    MEDIA_SYSTEM="mediaSystem";
    
    public static final String[] MEDIA_SYSTEMS={DEFAULT, JMF, QT};
    
    public static final RenderingHints DEFAULT_RENDERING_HINTS=new RenderingHints(null);
    
    // Constants for cursors
    public static final int HAND_CURSOR=0, OK_CURSOR=1, REC_CURSOR=2;
    
    public static final Color BG_COLOR=new Color(239, 247, 221);
    
    public static final int ACTION_PREV=0, ACTION_NEXT=1, ACTION_RETURN=2, ACTION_RESET=3, ACTION_INFO=4, ACTION_HELP=5, ACTION_AUDIO=6, ACTION_REPORTS=7, NUM_ACTIONS=8;
    public static final int[] DYNAMIC_ACTIONS={ACTION_PREV, ACTION_NEXT, ACTION_RETURN, ACTION_RESET, ACTION_INFO, ACTION_HELP};
    public static final String[] ACTION_NAME={"prev", "next", "return", "reset", "info", "help", "audio", "about"};
    
    // Constants for counters
    public static final int SCORE_COUNTER=0, ACTIONS_COUNTER=1, TIME_COUNTER=2, NUM_COUNTERS=3;
    public static final String[] counterNames={"score", "actions", "time"};
    
    /** Minimal margin between the activity container internal frame and the activity window bounds.
     */
    public static final int AC_MARGIN=6;
    
    /** Minimal value for both with and height of cells.
     */
    public static final int MIN_CELL_SIZE=10;
    
    public static final String MEDIA_OBJECT="media", SEQUENCE_OBJECT="sequence", ACTIVITY_OBJECT="activity", URL_OBJECT="url", EXTERNAL_OBJECT="external", SKIN_OBJECT="skin";
    
}
