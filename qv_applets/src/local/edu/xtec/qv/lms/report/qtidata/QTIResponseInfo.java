package edu.xtec.qv.lms.report.qtidata;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;

public class QTIResponseInfo{

	String id = "";
	String responseType = "";
	String cardinality = "";
	String renderType = "";
	
	public QTIResponseInfo(){
	}

	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return id;
	}
	
	public void setResponseType(String responseType){
		this.responseType = responseType;
	}
	
	public String getResponseType(){
		return responseType;
	}
	
	public void setCardinality(String cardinality){
		this.cardinality = cardinality;
	}
	
	public String getCardinality(){
		return cardinality;
	}
	
	public void setRenderType(String renderType){
		this.renderType = renderType;
	}
	
	public String getRenderType(){
		return renderType;
	}

	public String toString(){
		String s = "QTIResponseInfo"+"\n";
		s += "	id="+id+"\n";
		s += "	responseType="+responseType+"\n";
		s += "	cardinality="+cardinality+"\n";
		s += "	renderType="+renderType+"\n";
		return s;
	}
	
	public static QTIResponseInfo getQTIResponseInfo(Element eResponse, String responseType){
		QTIResponseInfo ri = new QTIResponseInfo();
		
		ri.setId(eResponse.getAttributeValue("ident"));
		ri.setResponseType(responseType);
		ri.setCardinality(eResponse.getAttributeValue("rcardinality"));

		String renderType = "";
		if (eResponse.getDescendants(new ElementFilter("render_choice")).hasNext())
			renderType = "choice";
		else if (eResponse.getDescendants(new ElementFilter("render_hotspot")).hasNext())
			renderType = "hotspot";
		else if (eResponse.getDescendants(new ElementFilter("render_slider")).hasNext())
			renderType = "slider";
		else if (eResponse.getDescendants(new ElementFilter("render_fib")).hasNext())
			renderType = "fib";
		else { //render_extension
			renderType = "extension";
		}
		ri.setRenderType(renderType);
		
		return ri;
	}
	
}