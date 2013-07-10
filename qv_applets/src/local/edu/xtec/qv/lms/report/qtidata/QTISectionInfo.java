package edu.xtec.qv.lms.report.qtidata;

import java.util.ArrayList;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;

public class QTISectionInfo{

	String id = "";
	String name = "";
	float maxScore = 0;
	String randomizableItems = "";
	int position;
	ArrayList items = new ArrayList();
	
	public QTISectionInfo(){
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

	public void setRandomizableItems(String randomizableItems){
		this.randomizableItems = randomizableItems;
	}
	
	public String getRandomizableItems(){
		return randomizableItems;
	}

	public void setPosition(int position){
		this.position = position;
	}
	
	public int getPosition(){
		return position;
	}
	
	public void addItem(QTIItemInfo item){
		items.add(item);
	}
	
	public void setItems(ArrayList items){
		this.items = items;
	}
	
	public ArrayList getItems(){
		return items;
	}
	
	public String toString(){
		String s = "QTISectionInfo"+"\n";
		s += "	id="+id+"\n";
		s += "	name="+name+"\n";
		s += "	maxScore="+maxScore+"\n";
		s += "	position="+position+"\n";
		s += "	randomizableItems="+randomizableItems+"\n";
		for (int i=0; i<items.size(); i++){
			QTIItemInfo ii = (QTIItemInfo)items.get(i);
			s += ii.toString();
		}
		return s;
	}
	
	public static QTISectionInfo getQTISectionInfo(Element eSection){
		QTISectionInfo si = new QTISectionInfo();
		
		si.setId(eSection.getAttributeValue("ident"));
		si.setName(eSection.getAttributeValue("title"));

		Iterator itResultItems = eSection.getDescendants(new ElementFilter("item"));	
		
		int position = 1;
		while (itResultItems.hasNext()){
			Element eItem = (Element)itResultItems.next();

			QTIItemInfo ii = QTIItemInfo.getQTIItemInfo(eItem);
			ii.setPosition(position);
			si.addItem(ii);
			position++;
		}
		si.setMaxScore(position-1);
		return si;
	}
	
}