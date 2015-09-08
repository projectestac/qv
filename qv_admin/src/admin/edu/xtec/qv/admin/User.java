/*
 * User.java
 * 
 * Created on 26/october/2005
 */
package edu.xtec.qv.admin;

import java.util.Enumeration;
import java.util.Vector;

public class User {

	protected String username;
	protected Vector roles;
	
	public User(String username){
		this.username=username;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public Vector getRoles() {
		return roles;
	}
	public void setRoles(Vector roles) {
		this.roles = roles;
	}
	public void addRole(Role role){
		 if (roles==null) roles = new Vector();
		 roles.addElement(role);
	}
	public boolean isRole(Role role){
		if (roles==null) return false;
		Enumeration enumRoles = roles.elements();
		while (enumRoles.hasMoreElements()){
			Role tmpRole = (Role)enumRoles.nextElement();
			if (tmpRole.equals(role)) return true;
		}
		return false;
	}
	
}
