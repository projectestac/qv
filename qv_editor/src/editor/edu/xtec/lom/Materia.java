package edu.xtec.lom;

public class Materia {
	
	protected int iId;
	protected String sNom;

	public Materia(int iId, String sNom){
		this.iId = iId;
		this.sNom = sNom;
	}
	
	public int getId(){
		return iId;
	}
	public void setId(int iId){
		this.iId=iId;
	}
	
	public String getNom(){
		return sNom;
	}
	public void setNom(String sNom){
		this.sNom=sNom;
	}
}
