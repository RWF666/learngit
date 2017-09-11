package com.weige.elec.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class ElecRole implements Serializable {
	
	private String roleID;		//Ö÷¼üID
	private String roleName;	//½ÇÉ«Ãû³Æ
	
	private Set<ElecUser> elecUsers = new HashSet<ElecUser>();
		
	public Set<ElecUser> getElecUsers() {
		return elecUsers;
	}
	public void setElecUsers(Set<ElecUser> elecUsers) {
		this.elecUsers = elecUsers;
	}
	public String getRoleID() {
		return roleID;
	}
	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
}
