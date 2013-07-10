package edu.xtec.qv.lms.report.qtidata;

import java.util.ArrayList;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;

public class QTIItemInfo{

	String id = "";
	String name = "";
	float maxScore = 1;
	int position;
	ArrayList responses = new ArrayList();
	
	public QTIItemInfo(){
	}

	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setMaxScore(float maxScore){
		this.maxScore = maxScore;
	}
	
	public float getMaxScore(){
		return maxScore;
	}

	public void setPosition(int position){
		this.position = position;
	}
	
	public int getPosition(){
		return position;
	}

	public void addResponse(QTIResponseInfo response){
		responses.add(response);
	}
	
	public void setResponses(ArrayList responses){
		this.responses = responses;
	}
	
	public ArrayList getResponses(){
		return responses;
	}
	
	public String toString(){
		String s = "QTIItemInfo"+"\n";
		s += "	id="+id+"\n";
		s += "	name="+name+"\n";
		s += "	maxScore="+maxScore+"\n";
		for (int i=0; i<responses.size(); i++){
			QTIResponseInfo ri = (QTIResponseInfo)responses.get(i);
			s += ri.toString();
		}
		return s;
	}
	
	public static QTIItemInfo getQTIItemInfo(Element eItem){
		QTIItemInfo ii = new QTIItemInfo();
		
		ii.setId(eItem.getAttributeValue("ident"));
		ii.setName(eItem.getAttributeValue("title"));
		ii.setMaxScore(1);

		String responseType = "";
		if (eItem.getDescendants(new ElementFilter("response_lid")).hasNext())
			responseType = "lid";
		else if (eItem.getDescendants(new ElementFilter("response_xy")).hasNext())
			responseType = "xy";
		else if (eItem.getDescendants(new ElementFilter("response_str")).hasNext())
			responseType = "str";
		else if (eItem.getDescendants(new ElementFilter("response_num")).hasNext())
			responseType = "num";
		else if (eItem.getDescendants(new ElementFilter("response_grp")).hasNext())
			responseType = "grp";
		else { //response_extension
			responseType = "extension";
		}

		Iterator itResultResponse = eItem.getDescendants(new ElementFilter("response_"+responseType));	
		while (itResultResponse.hasNext()){
			Element eResponse = (Element)itResultResponse.next();
			ii.addResponse(QTIResponseInfo.getQTIResponseInfo(eResponse, responseType));
		}
		return ii;
	}
}