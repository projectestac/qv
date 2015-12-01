package edu.xtec.jclic.shapers;

/*
 * EditableShapeConstants.java
 *
 * Created on 28 / febrer / 2002, 11:33
 */

/**
 *
 * @author  allastar
 * @version 
 */
public class EditableShapeConstants {

    public static int selectLength=8;
    public static double scaleXFactor=1.1;
    public static double scaleYFactor=1.1;
    public static int gridWidth=-1; //-1->Sense graella
    public static java.awt.Color selectedColor=java.awt.Color.blue;
    public static java.awt.Color defaultColor=java.awt.Color.black;    
    public static java.awt.Color gridColor=java.awt.Color.lightGray;
    public static java.awt.Color selectingAreaColor=java.awt.Color.darkGray;
    public static java.awt.Color lightColor=java.awt.Color.lightGray;
    public static java.awt.Color BORDER_COLOR=java.awt.Color.black;
    public static java.awt.Color DRAWN_BORDER_COLOR=java.awt.Color.black;
    public static java.awt.Color CUT_COLOR=java.awt.Color.red;
    public static java.awt.Color ACTIVE_COLOR=new java.awt.Color(128,128,255); //Quan es pot modificar el polígon
    public static java.awt.Color SELECTED_BORDER_COLOR=java.awt.Color.orange; //Quan hi ha un punt seleccionat
    //public static java.awt.Color movingColor=java.awt.Color.lightGray;
    public static java.awt.Color movingColor=java.awt.Color.green;
    public static java.awt.Color inactiveFillColor=new java.awt.Color(240,240,240);
    public static java.awt.Color inactiveBorderColor=new java.awt.Color(100,100,100);
    public static boolean fillDrawn=true;
    public static boolean pointsOnGrid=false; //iman
    public static boolean showDrawnPoints=true;
}
