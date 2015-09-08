/*
 * QVHTMLBean.java
 * 
 * Created on 10/juny/2005
 */
package edu.xtec.qv.admin.beans;

import java.util.Vector;


public class QVIndexBean extends QVAdminBean {

	public boolean start(){
		return true;
	}
	
	public Vector getRoles(){
		Vector vRoles = new Vector();
		if (getUser()!=null && getUser().getRoles()!=null) vRoles = getUser().getRoles();
		return vRoles;
	}
		
}
