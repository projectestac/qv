/*
 * Holes.java
 *
 * Created on 1 de octubre de 2001, 9:58
 */

package edu.xtec.jclic.shapers;

import edu.xtec.util.JDomUtility;

/**
 *
 * @author Francesc Busquets (fbusquets@pie.xtec.es)
 * @version
 */
public class Holes extends Shaper {
    
    public double scaleW;
    public double scaleH;
    protected ShapeData enclosingShapeData;
    // Possible eliminaci� d'aquest membre:
    protected boolean showEnclosure;
    
    public Holes(int nx,int ny) {
        super(1, 1);
        scaleW=1.0;
        scaleH=1.0;
        enclosingShapeData=null;
        showEnclosure=true;
        shapeData=new ShapeData[0];
        nCols=nx;
        nRows=ny;
    }
    
    public void reset(int nCols, int nRows){        
        this.nCols=nCols;
        this.nRows=nRows;
        initiated=false;
    }
                
    public String getEditorPanelClassName(){
        return getClass().getPackage().getName()+".HolesEditorPanel";
    }
    
    public static final String SHAPE="shape";
    public static final String SHOW_ENCLOSURE="showEnclosure";
    protected static final String SCALE_W="scaleX", SCALE_H="scaleY", ENCLOSING="enclosing";
    
    public org.jdom.Element getJDomElement(){
        org.jdom.Element e=super.getJDomElement();
        if(!showEnclosure) e.setAttribute(SHOW_ENCLOSURE, JDomUtility.boolString(showEnclosure));
        if(scaleW!=1.0) e.setAttribute(SCALE_W, Double.toString(scaleW));
        if(scaleH!=1.0) e.setAttribute(SCALE_H, Double.toString(scaleH));
        if(enclosingShapeData!=null)
            e.addContent(new org.jdom.Element(ENCLOSING).addContent(enclosingShapeData.getJDomElement(scaleW, scaleH)));
        if(shapeData!=null)
            for(int i=0; i<nCells; i++)
                e.addContent(shapeData[i].getJDomElement(scaleW, scaleH));
        return e;
    }
    
    public void setProperties(org.jdom.Element e, Object aux) throws Exception{
        scaleW=JDomUtility.getDoubleAttr(e, SCALE_W, scaleW);
        scaleH=JDomUtility.getDoubleAttr(e, SCALE_H, scaleH);
        showEnclosure=JDomUtility.getBoolAttr(e, SHOW_ENCLOSURE, showEnclosure);
        org.jdom.Element child=e.getChild(ENCLOSING);
        if(child!=null){
            child=child.getChild(ShapeData.ELEMENT_NAME);
            enclosingShapeData=ShapeData.getShapeData(child, scaleW, scaleH);
            // ATENCI�:
            showEnclosure=true;
        }
        java.util.List childs=e.getChildren(ShapeData.ELEMENT_NAME);
        if(!childs.isEmpty()){
            nCells=childs.size();
            //nCols=nRows=1;
            shapeData=new ShapeData[nCells];
            for(int i=0; i<nCells; i++)
                shapeData[i]=ShapeData.getShapeData((org.jdom.Element)childs.get(i), scaleW, scaleH);
        }
        else{
            nCells=0;
            nCols=nRows=0;
            shapeData=null;
        }
    }
    
    protected void buildShapes(){
        if(nCells>0)
            initiated=true;
    }
    
    public boolean hasRemainder(){
        return showEnclosure;
    }
    
    protected ShapeData getEnclosingShapeData(){
        if(!showEnclosure) return null;
        return enclosingShapeData!=null ? enclosingShapeData : super.getEnclosingShapeData();
    }
    
    public Object clone() throws CloneNotSupportedException {
        Holes clon=(Holes)super.clone();
        if(enclosingShapeData!=null)
            enclosingShapeData=(ShapeData)enclosingShapeData.clone();
        return clon;
    }    
    
    public void removeShape(int iIndex){
        if (shapeData==null || iIndex<0 || iIndex>=shapeData.length) return;
        ShapeData[] newShapeData=new ShapeData[shapeData.length-1];
        System.arraycopy(shapeData,0,newShapeData,0,iIndex);
        System.arraycopy(shapeData,iIndex+1,newShapeData,iIndex,shapeData.length-iIndex-1);
        nCells=newShapeData.length;
        shapeData=newShapeData;
    }
    
    public void addShape(ShapeData sd){
        ShapeData[] newShapeData=new ShapeData[shapeData.length+1];
        System.arraycopy(shapeData,0,newShapeData,0,shapeData.length);
        newShapeData[shapeData.length]=sd;
        nCells=newShapeData.length;
        shapeData=newShapeData;
    }
    
    public void modifyShape(int iIndex, ShapeData sd){
        if (iIndex>=0 && iIndex<shapeData.length){
            //System.out.println("modifyShape("+iIndex+")");
            sd.comment=(shapeData[iIndex]!=null)?shapeData[iIndex].comment:"";
            shapeData[iIndex]=sd;
        }
    }
    
    public ShapeData getShapeData(int i){
        return (shapeData!=null && shapeData.length>i)?shapeData[i]:null;
    }
}
