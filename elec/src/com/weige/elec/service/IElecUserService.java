package com.weige.elec.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.weige.elec.domain.ElecUser;
import com.weige.elec.domain.ElecUserFile;

public interface IElecUserService {
	public static final String SERVICE_NAME = "com.itheima.elec.service.impl.ElecUserServiceImpl";

	List<ElecUser> findUserListByCondition(ElecUser elecUser);

	String checkUser(String logonName);

	void saveUser(ElecUser elecUser);

	ElecUser findUserByUserID(String userID);

	ElecUserFile findUserFileByID(String fileID);

	void deleteUserByID(ElecUser elecUser);

	ElecUser findUserByLogonName(String name);

	ArrayList<String> findFieldNameWithExcel();

	ArrayList<ArrayList<String>> findFieldDataWithExcel(ElecUser elecUser);

	void saveUserList(List<ElecUser> userList);

	List<Object[]> chartUser(String zName, String eName);
}
