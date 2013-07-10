/*
 * ResponseLabel.java
 * 
 * Created on 09/febrer/2004
 */
package edu.xtec.qv.qti;

import java.awt.Point;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class ResponseLabel extends QTISuper {
	
	public static final String RAREA_RECTANGLE = "Rectangle";
	public static final String RAREA_ELLIPSE = "Ellipse";
	public static final String RAREA_BOUNDED = "Bounded";

	public ResponseLabel(String sIdent){
		if (sIdent==null || sIdent.trim().length()==0){
			sIdent = getRandomIdent();
		}
		setAttribute(IDENT, sIdent);
		setAttribute(CONTENT, new Vector());
	}

	public ResponseLabel(Element eElement){
		createFromXML(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#getXML()
	 */
	public Element getXML() {
		Element eResponse = new Element(RESPONSE_LABEL.getTagName());

		String sIdent = getIdent()!=null?getIdent():getRandomIdent();
		eResponse.setAttribute(IDENT.getTagName(), sIdent);

		if (getAttribute(RAREA)!=null){
			eResponse.setAttribute(RAREA.getTagName(), getRarea());
		}
		if (getAttribute(RRANGE)!=null){
			eResponse.setAttribute(RRANGE.getTagName(), getAttributeValue(RRANGE));
		}
		if (getAttribute(LABELREFID)!=null){
			eResponse.setAttribute(LABELREFID.getTagName(), getAttributeValue(LABELREFID));
		}
		if (getAttribute(MATCH_GROUP)!=null){
			eResponse.setAttribute(MATCH_GROUP.getTagName(), getAttributeValue(MATCH_GROUP));
		}
		if (getAttribute(MATCH_MAX)!=null){
			eResponse.setAttribute(MATCH_MAX.getTagName(), getAttributeValue(MATCH_MAX));
		}
		if (getAttribute(TEXT)!=null){
			eResponse.addContent(getAttributeValue(TEXT));
		}
		if (getAttribute(ROTATE)!=null){
			eResponse.setAttribute(ROTATE.getTagName(), getRotate());
		}

		Enumeration enumContents = ((Vector)getAttribute(CONTENT)).elements();
		while (enumContents.hasMoreElements()){
			QTIObject oContent = (QTIObject)enumContents.nextElement();
			eResponse.addContent(oContent.getXML());
		}		
		return eResponse;
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTIObject#createFromXML(org.jdom.Element)
	 */
	public void createFromXML(Element eElement) {
		if (eElement!=null){
			Vector vContents = new Vector();
			Iterator itContents = eElement.getChildren().iterator();
			while (itContents.hasNext()){
				Element eContent = (Element)itContents.next();
				QTIObject oContent = null;
				if (eContent.getName().equalsIgnoreCase(MATERIAL.getTagName())){
					oContent = new Material(eContent);
				} else if (eContent.getName().equalsIgnoreCase(FLOW_MAT.getTagName())){
					oContent = new FlowMat(eContent);
				} else if (eContent.getName().equalsIgnoreCase(QTICOMMENT.getTagName())){
					oContent = new QTIComment(eContent);
				}
				if (oContent!=null){
					vContents.addElement(oContent);
				}
			}
			setAttribute(CONTENT, vContents);
			setAttribute(IDENT, eElement.getAttributeValue(IDENT.getTagName()));
			setAttribute(RAREA, eElement.getAttributeValue(RAREA.getTagName()));
			setAttribute(RRANGE, eElement.getAttributeValue(RRANGE.getTagName()));
			setAttribute(LABELREFID, eElement.getAttributeValue(LABELREFID.getTagName()));
			setAttribute(MATCH_GROUP, eElement.getAttributeValue(MATCH_GROUP.getTagName()));
			setAttribute(MATCH_MAX, eElement.getAttributeValue(MATCH_MAX.getTagName()));
			setAttribute(TEXT, eElement.getText());
			setAttribute(ROTATE, eElement.getAttributeValue(ROTATE.getTagName()));
		}
	}

	public Vector getMaterials(){
		Vector vMaterial = new Vector();
		if (getFlowMat()!=null && !getFlowMat().isEmpty()){
			vMaterial = ((FlowMat)getFlowMat().firstElement()).getMaterials();
		}else{
			Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
			while (enumContents.hasMoreElements()){
				QTIObject oQTI = (QTIObject)enumContents.nextElement();
				if (oQTI instanceof Material){
					vMaterial.addElement(oQTI);
				}
			}
		}
		return vMaterial;
	}
	public void addMaterial(Material oMaterial){
		setAttribute(CONTENT, oMaterial);
	}
	protected void addMaterial(Material oMaterial, String sAlignment){
		if (sAlignment!=null && sAlignment.equalsIgnoreCase(HORIZONTAL)){
			addMaterial(oMaterial);
		}else{
			FlowMat oFlowMat = new FlowMat();
			oFlowMat.addMaterial(oMaterial);
			addFlowMat(oFlowMat);
		}
	}
	

	public Vector getFlowMat(){
		Vector vFlowMat = new Vector();
		Enumeration enumContents = getAttributeVectorValue(CONTENT).elements();
		while (enumContents.hasMoreElements()){
			QTIObject oQTI = (QTIObject)enumContents.nextElement();
			if (oQTI instanceof FlowMat){
				vFlowMat.addElement(oQTI);
			}
		}
		return vFlowMat;
	}
	public void addFlowMat(FlowMat oFlow){
		setAttribute(CONTENT, oFlow);
	}
	
	public String getText(){
		return getAttributeValue(TEXT);
	}
	
	public void setText(String sText){
		setAttribute(TEXT, sText);
	}
	
	public String getRarea(){
		return getAttributeValue(RAREA);
	}
	public void setRarea(String sRarea){
		setAttribute(RAREA, sRarea);
	}
	
	public String getRotate(){
		return getAttributeValue(ROTATE);
	}
	public void setRotate(String sRotate){
		setAttribute(ROTATE, sRotate);
	}
	
	public String getX0(){
		String sX0 = "";
		if (getText()!=null && getText().length()>0 && (RAREA_RECTANGLE.equals(getRarea()) || RAREA_ELLIPSE.equals(getRarea()) )){
			int iIndexX = getText().indexOf(",");
			if (iIndexX>0){
				sX0 = getText().substring(0, iIndexX); 
			}
		}else if (getFirstMat()!=null){
			QTISuperMat oMat = getFirstMat();
			if (oMat instanceof Mattext){
				sX0 = String.valueOf(((Mattext)oMat).getX0());
			}else if (oMat instanceof Matimage){
				sX0 = String.valueOf(((Matimage)oMat).getX0());
			}
		}
		return sX0;
	}
	
	public String getY0(){
		String sY0 = null;
		if (getText()!=null && getText().length()>0 && (RAREA_RECTANGLE.equals(getRarea()) || RAREA_ELLIPSE.equals(getRarea()) )){
			int iIndexX = getText().indexOf(",")+1;
			int iIndexY = getText().indexOf(",", iIndexX);
			if (iIndexY>0){
				sY0 = getText().substring(iIndexX, iIndexY);
			} 
		}else if (getFirstMat()!=null){
			QTISuperMat oMat = getFirstMat();
			if (oMat instanceof Mattext){
				sY0 = String.valueOf(((Mattext)oMat).getY0());
			}else if (oMat instanceof Matimage){
				sY0 = String.valueOf(((Matimage)oMat).getY0());
			}
		}
		return sY0;
	}
	
	public String getWidth(){
		String sWidth = null;
		if (getText()!=null && getText().length()>0 && (RAREA_RECTANGLE.equals(getRarea()) || RAREA_ELLIPSE.equals(getRarea()) )){
			int iIndexX = getText().indexOf(",")+1;
			int iIndexY = getText().indexOf(",", iIndexX)+1;
			int iIndexW = getText().indexOf(",", iIndexY);
			if (iIndexW>0){
				sWidth = getText().substring(iIndexY, iIndexW);
			} 
		}else if (getFirstMat()!=null){
			QTISuperMat oMat = getFirstMat();
			if (oMat instanceof Mattext){
				sWidth = String.valueOf(((Mattext)oMat).getWidth());
			}else if (oMat instanceof Matimage){
				sWidth = String.valueOf(((Matimage)oMat).getWidth());
			}
		}
		return sWidth;
	}
	
	public String getHeight(){
		String sHeight = null;
		if (getText()!=null && getText().length()>0 && (RAREA_RECTANGLE.equals(getRarea()) || RAREA_ELLIPSE.equals(getRarea()) )){
			int iIndexX = getText().indexOf(",")+1;
			int iIndexY = getText().indexOf(",", iIndexX)+1;
			int iIndexW = getText().indexOf(",", iIndexY)+1;
			if (iIndexW>0){
				sHeight = getText().substring(iIndexW);
			} 
		}else if (getFirstMat()!=null){
			QTISuperMat oMat = getFirstMat();
			if (oMat instanceof Mattext){
				sHeight = String.valueOf(((Mattext)oMat).getHeight());
			}else if (oMat instanceof Matimage){
				sHeight = String.valueOf(((Matimage)oMat).getHeight());
			}
		}
		return sHeight;
	}
	
	public Vector getPoints(){
		Vector vPoints = new Vector();
		if (getText()!=null && getText().length()>0 && RAREA_BOUNDED.equals(getRarea())){
			StringTokenizer st = new StringTokenizer(getText(), ",");
			while (st.hasMoreTokens()){
				String sx=st.nextToken();
				if (st.hasMoreTokens()){
					String sy=st.nextToken();
					try{
						int x = Integer.parseInt(sx);
						int y = Integer.parseInt(sy);
						vPoints.addElement(new Point(x, y));
					}catch (Exception e){
					}
				}
			}
		}
		return vPoints;
	}
	
	public String getURI(){
		String sURI = null;
		if (!getMaterials().isEmpty()){
			Material oMaterial = (Material)getMaterials().firstElement();
			Enumeration enumMats = oMaterial.getMats().elements();
			while (enumMats.hasMoreElements()){
				QTISuperMat oMat = (QTISuperMat)enumMats.nextElement();
				if (oMat.getURI()!=null && oMat.getURI().trim().length()>0) {
					sURI=oMat.getURI();
					break;
				}
			}
		}
		return sURI;
	}
	
	public String getMatText(){
		String sText = "";
		if (!getMaterials().isEmpty()){
			Material oMaterial = (Material)getMaterials().firstElement();
			Enumeration enumMats = oMaterial.getMats().elements();
			while (enumMats.hasMoreElements()){
				QTISuperMat oMat = (QTISuperMat)enumMats.nextElement();
				if (oMat.getText()!=null && oMat.getText().trim().length()>0) {
					return oMat.getText();
				}
			}
		}
		return sText;
	}
	
	public String getMatchgroup(){
		return getAttributeValue(MATCH_GROUP);
	}
	public void setMatchgroup(String sGroup){
		setAttribute(MATCH_GROUP, sGroup);
	}
	
	public int getMatchmax(){
		return getAttributeIntValue(MATCH_MAX);
	}
	public void setMatchmax(int iMax){
		if (iMax>0){
			setAttribute(MATCH_MAX, String.valueOf(iMax));
		}
	}
	
	protected QTISuperMat getFirstMat(){
		QTISuperMat oMat = null;
		if (!getMaterials().isEmpty()){
			Material oMaterial = (Material)getMaterials().firstElement();
			if (!oMaterial.getMats().isEmpty()){
				oMat = (QTISuperMat)oMaterial.getMats().firstElement();
			}
		}
		return oMat;
	}
		
	
//	***************************	
//	*  Creacio
//	***************************
	public static ResponseLabel createWithMattext(String sIdent, String sText, String sAlignment){
		Material oMaterial = Material.createWithMattext(sText);
		return ResponseLabel.create(sIdent, oMaterial, sAlignment);		
	}
	
	public static ResponseLabel createWithMatimage(String sIdent, String sImagtype, String sURI, String sAlignment){
		Material oMaterial = Material.createWithMatimage(sImagtype, sURI);
		return ResponseLabel.create(sIdent, oMaterial, sAlignment);		
	}
	
	public static ResponseLabel createWithMatimage(String sIdent, String sImagtype, String sURI, String sAlignment, int iWidth, int iHeight){
		Material oMaterial = Material.createWithMatimage(sImagtype, sURI, iWidth, iHeight);
		return ResponseLabel.create(sIdent, oMaterial, sAlignment);		
	}
	
	public static ResponseLabel createWithMataudio(String sIdent, String sURI, String sAlignment){
		Material oMaterial = Material.createWithMataudio(sURI);
		return ResponseLabel.create(sIdent, oMaterial, sAlignment);		
	}
	
	protected static ResponseLabel create(String sIdent, Material oMaterial, String sAlignment){
		ResponseLabel oResponseLabel = new ResponseLabel(sIdent);
		if (sAlignment!=null && sAlignment.equalsIgnoreCase(HORIZONTAL)){
			oResponseLabel.addMaterial(oMaterial);
		}else{
			FlowMat oFlowMat = new FlowMat();
			oFlowMat.addMaterial(oMaterial);
			oResponseLabel.addFlowMat(oFlowMat);
		}
		return oResponseLabel;		
	}
	
	public void addMattext(String sText, String sAlignment){
		Material oMaterial = Material.createWithMattext(sText);
		addMaterial(oMaterial, sAlignment);
	}
	
	public void addMatimage(String sImagtype, String sURI, String sAlignment){
		addMatimage(sImagtype, sURI, sAlignment, -1, -1);
	}
	
	public void addMatimage(String sImagtype, String sURI, String sAlignment, int iWidth, int iHeight){
		Vector vMaterials = getMaterials();
		Matimage oMatimage = new Matimage(sImagtype, sURI, iWidth, iHeight);
		Material oMaterial = null; 
		if (vMaterials!=null & !vMaterials.isEmpty()){
			oMaterial = (Material)vMaterials.firstElement();
			oMaterial.addMaterial(oMatimage);
		}else{
			oMaterial = Material.createWithMatimage(sImagtype, sURI, iWidth, iHeight);
			addMaterial(oMaterial, sAlignment);
		}
	}
	
	public void addMataudio(String sURI, String sAlignment){
		Vector vMaterials = getMaterials();
		Mataudio oMataudio = new Mataudio(sURI);
		Material oMaterial = null; 
		if (vMaterials!=null & !vMaterials.isEmpty()){
			oMaterial = (Material)vMaterials.firstElement();
			oMaterial.addMaterial(oMataudio);
		}else{
			oMaterial = Material.createWithMataudio(sURI);
			addMaterial(oMaterial, sAlignment);
		}
	}
	
}
