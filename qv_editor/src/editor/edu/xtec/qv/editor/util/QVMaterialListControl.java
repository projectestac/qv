/*
 * QVMaterialListControl.java
 * 
 * Created on 09/juny/2004
 */
package edu.xtec.qv.editor.util;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import edu.xtec.qv.qti.Flow;
import edu.xtec.qv.qti.FlowMat;
import edu.xtec.qv.qti.Item;
import edu.xtec.qv.qti.MatHTML;
import edu.xtec.qv.qti.Mataudio;
import edu.xtec.qv.qti.Matbreak;
import edu.xtec.qv.qti.Matapplication;
import edu.xtec.qv.qti.Material;
import edu.xtec.qv.qti.Matimage;
import edu.xtec.qv.qti.Matjclic;
import edu.xtec.qv.qti.Matlatex;
import edu.xtec.qv.qti.Mattext;
import edu.xtec.qv.qti.Matvideo;
import edu.xtec.qv.qti.Presentation;
import edu.xtec.qv.qti.PresentationMaterial;
import edu.xtec.qv.qti.QTIObject;
import edu.xtec.qv.qti.QTISuperMat;
import edu.xtec.qv.qti.Section;
import edu.xtec.qv.qti.util.StringUtil;

/**
 * @author sarjona
 */
public class QVMaterialListControl extends QVListControl{
	
	public QVMaterialListControl(HttpServletRequest request){
		super(request);
	}

	// Parameter
	public static final String P_CONTENT_TYPE = "p_content_type";
	public static final String P_MATERIAL_TEXT = "p_material_text";
	public static final String P_MATERIAL_HTML = "p_material_html";
	public static final String P_MATERIAL_URI = "p_material_uri";
	public static final String P_MATERIAL_FLASH_URI = "p_material_flash_uri";
	public static final String P_MATERIAL_AUDIO_URI = "p_material_audio_uri";
	public static final String P_MATERIAL_VIDEO_URI = "p_material_video_uri";
	public static final String P_MATERIAL_JCLIC_URI = "p_material_jclic_uri";
	public static final String P_MATERIAL_APPLICATION_URI = "p_material_application_uri";
	public static final String P_MAT_TYPE = "p_mat_type";
	public static final String P_MATERIAL_WIDTH = "p_material_width";
	public static final String P_MATERIAL_HEIGHT = "p_material_height";
	public static final String P_MATERIAL_FLASH_WIDTH = "p_material_flash_width";
	public static final String P_MATERIAL_FLASH_HEIGHT = "p_material_flash_height";
	public static final String P_MATERIAL_JCLIC_WIDTH = "p_material_jclic_width";
	public static final String P_MATERIAL_JCLIC_HEIGHT = "p_material_jclic_height";

	public static final String P_MATERIAL_LATEX_EQUATION = "p_material_latex_equation";
	public static final String P_MATERIAL_LATEX_WIDTH = "p_material_latex_width";
	public static final String P_MATERIAL_LATEX_HEIGHT = "p_material_latex_height";
	
	public static String P_LABEL = "p_label";
	public static String P_ENTRY = "p_entry";
	
	public static String A_UP = "up";
	public static String A_DOWN = "down";
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getListObjects(edu.xtec.qv.qti.QTIObject)
	 */
	public Vector getListObjects(QTIObject oQTI){
		Vector vListObjects = new Vector();
		Vector vMaterials = getMaterials(oQTI);
		Enumeration enumMats = vMaterials.elements();
		int i = 0;
		while (enumMats.hasMoreElements()){
			QTISuperMat oMat = (QTISuperMat)enumMats.nextElement();
			String sValue = "";
			if (oMat instanceof Mataudio){
				sValue = "["+oMat.getMatContentType()+"] "+ oMat.getURI();
			}else if (oMat instanceof MatHTML){
				sValue = "["+oMat.getMatContentType()+"] "+ StringUtil.htmlToText(oMat.getText());
			}else if (oMat instanceof Matimage){
				sValue = "["+oMat.getMatContentType()+"] "+ oMat.getURI();
			}else if (oMat instanceof Mattext){
				String sContentType = oMat.getMatContentType();
				if (sContentType.equals(QTISuperMat.MATHTML_CONTENT_TYPE)){
					sValue = "["+oMat.getMatContentType()+"] "+ StringUtil.htmlToText(oMat.getText());
				}else{
					sValue = "["+oMat.getMatContentType()+"] "+ oMat.getText();
				}
			}else if (oMat instanceof Matvideo){
				sValue = "["+oMat.getMatContentType()+"] "+ oMat.getURI();
			}else if (oMat instanceof Matjclic){
				sValue = "["+oMat.getMatContentType()+"] "+ oMat.getURI();
			}else if (oMat instanceof Matapplication){
				sValue = "["+oMat.getMatContentType()+"] "+ oMat.getURI();
			}else if (oMat instanceof Matlatex){
				sValue = "["+oMat.getMatContentType()+"] "+ oMat.getText();
			}else if (oMat instanceof Matbreak){
				sValue = "["+oMat.getMatContentType()+"] ";
			}
			ListObject o = new ListObject(String.valueOf(i), sValue);
			vListObjects.addElement(o);
			i++;
		}
		return vListObjects;
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getObject(QTIObject oQTI, String sIdent){
		return getMat(oQTI, sIdent);
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getPreviousObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getPreviousObject(QTIObject oQTI, String sIdent){
		logger.debug("Method getPreviousObject not implemented");
		return null;
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.editor.util.QVListControl#getNextObject(edu.xtec.qv.qti.QTIObject, java.lang.String)
	 */
	public Object getNextObject(QTIObject oQTI, String sIdent){
		logger.debug("Method getPreviousObject not implemented");
		return null;
	}
	
	/**
	 * 
	 * @param oQTI
	 * @param sIdent label of Metadatafield (it identifies MetadataField) 
	 */
	public void addListObject(QTIObject oQTI, String sIdent){
		QTISuperMat oNew = (QTISuperMat)getRequestQTIObject();
		if (oQTI!=null && oNew!=null){
			Material oMaterial = getMaterial(oQTI);
			if (oMaterial!=null){
				int iPosition = -1;
				if (sIdent!=null){
					iPosition = Integer.parseInt(sIdent)+1;
				}
				oMaterial.addMaterial(oNew, iPosition);
			}
		}
	}
	
	public void delListObject(QTIObject oQTI, String sIdent){
		if (oQTI!=null && sIdent!=null){
			Material oMaterial = getMaterial(oQTI);
			if (oMaterial!=null){
				QTISuperMat oMat = null;
				if (oQTI instanceof Item){
					oMat = getMat(oQTI, sIdent);
				}else{
					oMat = getMat(oMaterial, sIdent);
				}
				oMaterial.delMaterial(oMat);
			}
		}
	}
	
	public void setListObject(QTIObject oQTI, String sIdent){
		QTISuperMat oOld = (QTISuperMat)getMat(oQTI, sIdent);
		QTISuperMat oNew = (QTISuperMat)getRequestQTIObject();
		if (oQTI!=null && oOld!=null && oNew!=null){
			Material oMaterial = getMaterial(oQTI);
			if (oMaterial!=null){
				oMaterial.setMaterial(oOld, oNew);
			}
		}
	}
	
	public void upListObject(QTIObject oQTI, String sIdent){
		setMaterialPosition(oQTI, sIdent, A_UP);
	}
	
	public void downListObject(QTIObject oQTI, String sIdent){
		setMaterialPosition(oQTI, sIdent, A_DOWN);
	}

	private void setMaterialPosition(QTIObject oQTI, String sIdent, String sAction){
		if (oQTI!=null && sIdent!=null && sAction!=null){
			int iOldPosition = Integer.parseInt(sIdent);
			int iNewPosition = iOldPosition+1;
			if (sAction.equals(A_UP)){
				iNewPosition = iOldPosition-1;
			}
			Material oMaterial = getMaterial(oQTI);
			int iMinSize = 0;
			if (oQTI instanceof Item){
				iMinSize = 1;
				iOldPosition++;
				iNewPosition++;
			}
			if (iNewPosition>=iMinSize && iNewPosition<oMaterial.getMats().size()){
				oMaterial.setMaterialPosition(iOldPosition, iNewPosition);
				if (oQTI instanceof Item){
					iNewPosition--;
				}
				setCurrentListObject(String.valueOf(iNewPosition));
			}
		}
	}
	
	public static Vector getMaterials(QTIObject oQTI){
		Vector vMaterials = new Vector();
		if (oQTI!=null){
			if (oQTI instanceof Section){
				PresentationMaterial oPresentationMat = ((Section)oQTI).getPresentationMaterial();
				if (oPresentationMat!=null){
					Enumeration enumFlowMats = oPresentationMat.getFlowMats().elements();
					while (enumFlowMats.hasMoreElements()){
						FlowMat oFlowMat = (FlowMat)enumFlowMats.nextElement();
						vMaterials.addAll(oFlowMat.getAllMaterials());
					}
				}
			}else if (oQTI instanceof Material){
				vMaterials = ((Material)oQTI).getMats();
			}else if (oQTI instanceof FlowMat){
				vMaterials = ((FlowMat)oQTI).getAllMaterials();
			}else if (oQTI instanceof Item){
				Presentation oPresentation = ((Item)oQTI).getPresentation();
				if (oPresentation!=null){
					Vector vAllMaterials = oPresentation.getAllMaterials();
					if (vAllMaterials!=null && !vAllMaterials.isEmpty()){
						Material oMaterial = (Material)vAllMaterials.firstElement();
						Vector vMats = oMaterial.getMats();
						vMaterials = new Vector(vMats.subList(1, vMats.size()));
					}
				}
			}
		}
		return vMaterials;
	}
	

	private QTISuperMat getMat(QTIObject oQTI, String sIdent){
		QTISuperMat oMat = null;
		try{
			if (sIdent!=null){
				Vector vMaterials = getMaterials(oQTI);
				int iIndex = Integer.parseInt(sIdent);
				if (iIndex>=0 && iIndex < vMaterials.size()){
					oMat = (QTISuperMat)vMaterials.elementAt(iIndex);
				}
			}
		}catch (Exception e){
			logger.error("EXCEPTION obtenint material-> "+e);
		}
		return oMat;
	}
	
	public static Material getMaterial(QTIObject oQTI){
		Material oMaterial = null;
		if (oQTI instanceof Section){
			// Obtenir Presentation (si no existeix crear-lo)
			PresentationMaterial oPresentation = ((Section)oQTI).getPresentationMaterial();
			if (oPresentation==null){
				oPresentation = new PresentationMaterial();
				((Section)oQTI).setPresentationMaterial(oPresentation);
			}
			// Obtenir FlowMat (si no existeix crear-lo)
			FlowMat oFlowMat = null;
			if (oPresentation.getFlowMats().isEmpty()){
				oFlowMat = new FlowMat();
				oPresentation.addFlowMat(oFlowMat);
			}else{
				oFlowMat = (FlowMat)oPresentation.getFlowMats().firstElement();
			}
			// Obtenir Material (si no existeix crear-lo)
			if (oFlowMat.getMaterials().isEmpty()){
				oMaterial = new Material();
				oFlowMat.addMaterial(oMaterial);
			}else{
				oMaterial = (Material)oFlowMat.getMaterials().firstElement();
			}
		}else if (oQTI instanceof Item){
			// Obtenir Presentation (si no existeix crear-lo)
			Presentation oPresentation = ((Item)oQTI).getPresentation();
			if (oPresentation==null){
				oPresentation = new Presentation();
				((Item)oQTI).setPresentation(oPresentation);
			}
			// Obtenir Flow (si no existeix crear-lo)
			Flow oFlow = oPresentation.getFlow();
			if (oFlow==null){
				oFlow = new Flow();
				oPresentation.setFlow(oFlow);
			}
			// Obtenir Material (si no existeix crear-lo)
			if (oFlow.getMaterials().isEmpty()){
				// Afegir enunciat
				oMaterial = Material.createWithMattext("");
				oFlow.addMaterial(oMaterial);
			}else{
				oMaterial = (Material)oFlow.getMaterials().firstElement();
			}
		}
		return oMaterial;
	}
	
	public QTIObject getRequestQTIObject(){
		QTISuperMat oMat = null;
		String sContentType = request.getParameter(P_CONTENT_TYPE);
		//logger.debug("contentType="+sContentType+" isflash?"+QTISuperMat.isMatflash(sContentType));
		if (QTISuperMat.isMattext(sContentType)){
			String sMaterialText = request.getParameter(P_MATERIAL_TEXT);
			if (sMaterialText!=null && sMaterialText.trim().length()>0){
				oMat = new Mattext(sMaterialText);
			}
		}else if (QTISuperMat.isMatHTML(sContentType)){
			String sMaterialText = request.getParameter(P_MATERIAL_HTML);
			if (sMaterialText!=null && sMaterialText.trim().length()>0){
				oMat = new Mattext(Mattext.HTML_TEXTTYPE, sMaterialText);
			}
		} else if (QTISuperMat.isMatimage(sContentType)){
			String sURI = request.getParameter(P_MATERIAL_URI);
			String sMatType = FileUtil.getImageType(sURI);
			int iWidth = getIntParameter(request, P_MATERIAL_WIDTH, -1);
			int iHeight = getIntParameter(request, P_MATERIAL_HEIGHT, -1);
			if (sURI!=null && sURI.trim().length()>0){
				oMat = new Matimage(sMatType, sURI, iWidth, iHeight);
			}
		} else if (QTISuperMat.isMatflash(sContentType)){
			String sURI = request.getParameter(P_MATERIAL_FLASH_URI);
			String sMatType = FileUtil.getImageType(sURI);
			int iWidth = getIntParameter(request, P_MATERIAL_FLASH_WIDTH, 200);
			int iHeight = getIntParameter(request, P_MATERIAL_FLASH_HEIGHT, 200);
			if (sURI!=null && sURI.trim().length()>0){
				oMat = new Matimage(sMatType, sURI, iWidth, iHeight);
			}
		} else if (QTISuperMat.isMataudio(sContentType)){
			String sURI = request.getParameter(P_MATERIAL_AUDIO_URI);
			if (sURI!=null && sURI.trim().length()>0){
				oMat = new Mataudio(sURI);
			}
		} else if (QTISuperMat.isMatvideo(sContentType)){
			String sURI = request.getParameter(P_MATERIAL_VIDEO_URI);
			if (sURI!=null && sURI.trim().length()>0){
				oMat = new Matvideo(sURI);
			}
		} else if (QTISuperMat.isMatbreak(sContentType)){
			oMat = new Matbreak();
		} else if (QTISuperMat.isMatjclic(sContentType)){
			String sURI = request.getParameter(P_MATERIAL_JCLIC_URI);			
			if (sURI!=null && sURI.trim().length()>0){
				int iWidth = getIntParameter(request, P_MATERIAL_JCLIC_WIDTH, -1);
				int iHeight = getIntParameter(request, P_MATERIAL_JCLIC_HEIGHT, -1);
				oMat = new Matjclic(sURI, iWidth, iHeight);
			}
		} else if (QTISuperMat.isMatapplication(sContentType)){
			String sURI = request.getParameter(P_MATERIAL_APPLICATION_URI);
			String sMatType = FileUtil.getApplicationType(sURI);
			if (sURI!=null && sURI.trim().length()>0){
				oMat = new Matapplication(sMatType,sURI);
			}
		} else if (QTISuperMat.isMatlatext(sContentType)){
			String sEquation = request.getParameter(P_MATERIAL_LATEX_EQUATION);			
			if (sEquation!=null && sEquation.trim().length()>0){
				int iWidth = getIntParameter(request, P_MATERIAL_LATEX_WIDTH, -1);
				int iHeight = getIntParameter(request, P_MATERIAL_LATEX_HEIGHT, -1);
				oMat = new Matlatex(sEquation, iWidth, iHeight);
			}
		}
		//logger.debug("getRequestQTIObject()-> "+oMat);
		return oMat; 	
	}
}
