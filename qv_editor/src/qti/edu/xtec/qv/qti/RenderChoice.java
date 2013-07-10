/*
 * RenderChoice.java
 * 
 * Created on 09/febrer/2004
 */
package edu.xtec.qv.qti;

import java.util.Enumeration;
import java.util.Vector;

import org.jdom.Element;

/**
 * @author sarjona
 */
public class RenderChoice extends QTISuperRender {

	public RenderChoice(){
		super();
	}

	public RenderChoice(Element eElement){
		super(eElement);
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperRender#getRenderTag()
	 */
	protected String getRenderTag() {
		return RENDER_CHOICE.getTagName();
	}

	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperRender#getSpecificXML(org.jdom.Element)
	 */
	protected Element getSpecificXML(Element eRender){
		return eRender;
	}
	
	/* (non-Javadoc)
	 * @see edu.xtec.qv.qti.QTISuperRender#createSpecificFromXML(org.jdom.Element)
	 */
	protected void createSpecificFromXML(Element eElement){
	}
	
	
//	***************************	
//	*  Creacio
//	***************************
	public static RenderChoice createWithMattext(Vector vResponses, String sAlignment){
		RenderChoice oRenderChoice = new RenderChoice();
		FlowLabel oFlowLabel = null;
		if (sAlignment!=null && sAlignment.equalsIgnoreCase(HORIZONTAL)){
			oFlowLabel = new FlowLabel();
		}
		Enumeration enumResponses = vResponses.elements();
		while (enumResponses.hasMoreElements()){
			String sResponse = (String)enumResponses.nextElement();
			ResponseLabel oResponseLabel = ResponseLabel.createWithMattext("", sResponse, sAlignment);
			if (oFlowLabel!=null){
				oFlowLabel.addResponseLabel(oResponseLabel); 
			} else{
				oRenderChoice.addResponseLabel(oResponseLabel); 				
			}
		}
		if (oFlowLabel!=null){
			oRenderChoice.addFlowLabel(oFlowLabel);
		}
		return oRenderChoice;
	}
	
	public static RenderChoice create(String sAlignment){
		RenderChoice oRenderChoice = new RenderChoice();
		FlowLabel oFlowLabel = null;
		if (sAlignment!=null && sAlignment.equalsIgnoreCase(HORIZONTAL)){
			oFlowLabel = new FlowLabel();
			oRenderChoice.addFlowLabel(oFlowLabel);
		}
		return oRenderChoice;
	}
	
	
}
