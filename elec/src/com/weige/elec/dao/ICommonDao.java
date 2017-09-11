package com.weige.elec.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.weige.elec.domain.ElecSystemDDL;
import com.weige.elec.domain.ElecUser;
import com.weige.elec.utils.PageInfo;

public interface ICommonDao<T> {
	void save(T entity);
	void update(T entity);
	T findObjectByID(Serializable id);
	void deleteObjectByIds(Serializable... ids);
	void deleteObjectByCollection(List<T> list);
	List<T> findCollectionByConditionNoPage(String condition,Object[] params, Map<String, String> orderby);
	List<T> findCollectionByConditionNoPageWithCache(String condition, Object[] params, Map<String, String> orderby);
	List<T> findCollectionByConditionWithPage(String condition,
			Object[] params, Map<String, String> orderby, PageInfo pageInfo);
	List findCollectionByConditionNoPageWithSelectCodition(String condition,
			Object[] params, Map<String, String> orderby, String selectCondition);
	void saveList(List<T> list);
}
