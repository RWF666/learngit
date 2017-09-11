package com.weige.elec.service;

import java.util.Hashtable;
import java.util.List;

import com.weige.elec.domain.ElecPopedom;
import com.weige.elec.domain.ElecRole;
import com.weige.elec.domain.ElecUser;

public interface IElecRoleService {
	public static final String SERVICE_NAME = "com.weige.elec.service.impl.ElecRoleServiceImpl";

	List<ElecRole> findAllRoleList();

	List<ElecPopedom> findAllPopedomList();

	List<ElecPopedom> findAllPopedomListByRoleID(String roleID);

	List<ElecUser> findAllUserListByRoleID(String roleID);

	void saveRole(ElecPopedom elecPopedom);

	String findPopedomByRoleIDs(Hashtable<String, String> ht);

	List<ElecPopedom> findPopedomListByString(String popedom);

	boolean findRolePopedomByID(String roleID, String mid, String pid);
}
