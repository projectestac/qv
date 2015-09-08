/*
 * Role.java
 * 
 * Created on 26/october/2005
 */
package edu.xtec.qv.admin;


public class Role {
	
	public static final int ADMIN = 1;
	public static final int TEACHER = 2;
	public static final int VALIDATOR = 3;

	public static final Role ADMIN_ROLE = new Role(ADMIN);
	public static final Role TEACHER_ROLE = new Role(TEACHER);
	public static final Role VALIDATOR_ROLE = new Role(VALIDATOR);

	protected int id;
	
	public Role(int id){
		this.id=id;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public boolean equals(Role o){
		if (o==null) return false;
		return o.getId()==getId();
	}
	
	public String toString(){
		return this.getClass()+": id="+getId();
	}
	
}
