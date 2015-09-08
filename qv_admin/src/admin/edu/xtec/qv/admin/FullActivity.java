package edu.xtec.qv.admin;

import java.util.Date;
import java.util.Vector;

import edu.xtec.lom.Area;
import edu.xtec.lom.Nivell;
import edu.xtec.qv.biblio.Activity;
import edu.xtec.qv.biblio.Author;

public class FullActivity extends Activity {
	
	protected Vector vAreas = new Vector();
	protected Vector vLevels = new Vector();
	protected Vector vAuthors = new Vector();
	protected Vector vInstitutions = new Vector();
	

	public FullActivity(int iId) {
		super(iId, null);
	}
	public FullActivity(int iId, Date dRevision) {
		super(iId, dRevision);
	}
	
	public Vector getAreas(){
		return vAreas;
	}
	public void addArea(Area oArea){
		vAreas.addElement(oArea);
	}
	
	public Vector getLevels(){
		return vLevels;
	}
	public void addLevel(Nivell oLevel){
		vLevels.addElement(oLevel);
	}
	public Vector getAuthors(){
		return vAuthors;
	}
	public void addAuthor(Author oAuthor){
		vAuthors.addElement(oAuthor);
	}
	
	public Vector getInstitutions(){
		return vInstitutions;
	}
	public void addInstitution(String sId){
		vInstitutions.addElement(sId);
	}
	
	public String toString(){
		return "FullActivity [id="+getId()+", title="+hTitle+", description="+hDescription+", image="+getImage()+", state="+getState()+", #areas="+getAreas().size()+", #levels="+getLevels().size()+", #authors="+getAuthors().size()+", #institutions="+getInstitutions().size()+"]";
	}

}
