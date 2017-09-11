package com.weige.elec.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.management.relation.Role;
import javax.persistence.Id;
import javax.persistence.OrderBy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weige.elec.dao.IElecPopedomDao;
import com.weige.elec.dao.IElecRoleDao;
import com.weige.elec.dao.IElecRolePopedomDao;
import com.weige.elec.dao.IElecUserDao;
import com.weige.elec.dao.impl.ElecUserDaoImpl;
import com.weige.elec.domain.ElecPopedom;
import com.weige.elec.domain.ElecRole;
import com.weige.elec.domain.ElecRolePopedom;
import com.weige.elec.domain.ElecUser;
import com.weige.elec.service.IElecRoleService;

@Service(IElecRoleService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecRoleServiceImpl implements IElecRoleService {

	/** �û���Dao */
	@Resource(name = IElecUserDao.SERVICE_NAME)
	IElecUserDao elecUserDao;

	/** ��ɫ��Dao */
	@Resource(name = IElecRoleDao.SERVICE_NAME)
	IElecRoleDao elecRoleDao;

	/** Ȩ�ޱ�Dao */
	@Resource(name = IElecPopedomDao.SERVICE_NAME)
	IElecPopedomDao elecPopedomDao;

	/** ��ɫȨ�ޱ�Dao */
	@Resource(name = IElecRolePopedomDao.SERVICE_NAME)
	IElecRolePopedomDao elecRolePopedomDao;

	/**
	 * @Name: findAllRoleList
	 * @Description: ��ѯ���ݿ��е����н�ɫ
	 * @Author: ��ΰ�壨���ߣ�
	 * @Version: V1.00 ���汾�ţ�
	 * @Create Date: 2017-8-05���������ڣ�
	 * @Parameters: ��
	 * @Return: List<ElecRole> ��ɫ����
	 */
	public List<ElecRole> findAllRoleList() {
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.roleID", "asc");
		return elecRoleDao.findCollectionByConditionNoPage("", null, orderby);
	}

	/**
	 * @Name: findAllPopedomList
	 * @Description: ��ѯ���ݿ��е�����Ȩ�ޣ���ס���а����ӵļ��ϣ�
	 * @Author: ��ΰ�壨���ߣ�
	 * @Version: V1.00 ���汾�ţ�
	 * @Create Date: 2017-8-05���������ڣ�
	 * @Parameters: ��
	 * @Return: List<ElecPopedom> Ȩ�޼���
	 */
	public List<ElecPopedom> findAllPopedomList() {
		// ��ѯȨ�޵ļ��ϣ�������е�tr��Ҳ����pid=0�ļ���
		String condition = " and o.pid=?";
		Object[] params = { "0" };
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.mid", "asc");
		List<ElecPopedom> list = elecPopedomDao
				.findCollectionByConditionNoPage(condition, params, orderby);

		if (list != null && list.size() > 0) {
			for (ElecPopedom elecPopedom : list) {
				// ��ȡȨ��id�����ֵʱ��һ��pid�������ֵ
				String mid = elecPopedom.getMid();
				String condition1 = " and o.pid=?";
				Object[] params1 = { mid };
				Map<String, String> orderby1 = new LinkedHashMap<String, String>();
				orderby.put("o.mid", "asc");
				List<ElecPopedom> list1 = elecPopedomDao
						.findCollectionByConditionNoPage(condition1, params1,
								orderby1);
				// �������ӵļ��ϣ���ӵ����ļ�������
				elecPopedom.setList(list1);
			}
		}

		return list;
	}

	/**
	 * @Name: findAllPopedomListByRoleID
	 * @Description: ʹ�õ�ǰ��ɫID��ѯϵͳ������Ȩ�ޣ�����ʾ
	 * @Author: ��ΰ�壨���ߣ�
	 * @Version: V1.00 ���汾�ţ�
	 * @Create Date: 2017-8-05���������ڣ�
	 * @Parameters: ��
	 * @Return: List<ElecPopedom> Ȩ�޼���
	 */
	public List<ElecPopedom> findAllPopedomListByRoleID(String roleID) {
		// 1����ѯϵͳ�����е�Ȩ�ޣ���Ȩ�����ڱ���tr����Ȩ�޶�Ӧ���Ӽ��ϣ���������td
		List<ElecPopedom> list = findAllPopedomList();
		// 2��ʹ�ý�ɫID����֯��ѯ��������ѯ��ɫȨ�޹���������List<ElecRolePopedom>
		String condition = " and o.roleID=?";
		Object[] params = { roleID };
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.mid", "asc");
		List<ElecRolePopedom> popedomList = elecRolePopedomDao
				.findCollectionByConditionNoPage(condition, params, orderby);
		/**
		 * 3��ƥ�䣬��ElecPopedom����������flag����ֵ�Ĳ��� ���ƥ��ɹ�������1 ���ƥ�䲻�ɹ�������2
		 * ʹ�ð����ļ�����ƥ�䣨List���ϡ�String��contains()��
		 */
		// ����һ��Ȩ�޵��ַ���
		StringBuffer popedomBuffer = new StringBuffer("");
		// ����popedomList����ÿ��mid��ֵ��ȡ����֯��һ���ַ���
		if (popedomList != null && popedomList.size() > 0) {
			for (ElecRolePopedom elecRolePopedom : popedomList) {
				// ��ȡmid��ֵ
				String mid = elecRolePopedom.getMid();
				// ��֯��һ��Ȩ�޵��ַ�������ʽaa@ab@ac@ad)
				popedomBuffer.append(mid).append("@");
			}
			// ȥ�����һ��@
			popedomBuffer.deleteCharAt(popedomBuffer.length() - 1);
		}
		// ���Ȩ�ޣ���Ȩ�޾��ǵ�ǰ��ɫ���е�Ȩ�ޣ���ʽaa@ab@ac@ad)
		String popedom = popedomBuffer.toString();
		// ����һ�����������ƥ�䣬����
		this.findPopedomResult(popedom, list);
		return list;
	}

	// ����һ�����������ƥ�䣬����
	private void findPopedomResult(String popedom, List<ElecPopedom> list) {
		// �������е�Ȩ�ޣ�Ȩ����ֻ����tr��List
		if (list != null && list.size() > 0) {
			for (ElecPopedom elecPopedom : list) {
				// ��ȡÿ��Ȩ�޵�mid
				String mid = elecPopedom.getMid();
				// ƥ��ɹ�
				if (popedom.contains(mid)) {
					elecPopedom.setFlag("1");
				} else {
					elecPopedom.setFlag("2");
				}
				// ��ȡ����Ӧ���ӵļ���
				List<ElecPopedom> childList = elecPopedom.getList();
				if (childList != null && childList.size() > 0) {
					this.findPopedomResult(popedom, childList);
				}
			}
		}
	}

	/**
	 * @Name: findAllUserListByRoleID
	 * @Description: ʹ�õ�ǰ��ɫID��ѯϵͳ�������û�������ʾ
	 * @Author: ��ΰ�壨���ߣ�
	 * @Version: V1.00 ���汾�ţ�
	 * @Create Date: 2017-8-05���������ڣ�
	 * @Parameters: ��
	 * @Return: List<ElecUser> �û�����
	 */
	public List<ElecUser> findAllUserListByRoleID(String roleID) {
		//1����ѯϵͳ�����е��û�List<ElecUser>
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.onDutyDate", "asc");
		List<ElecUser> list = elecUserDao.findCollectionByConditionNoPage("", null, orderby);
		// 2��ʹ�ý�ɫID����ѯ��ɫ���󣬷���ElecRole����ͨ��Set���ϻ�ȡ���û����еĽ�ɫ
		ElecRole elecRole = elecRoleDao.findObjectByID(roleID);
		Set<ElecUser> elecUsers = elecRole.getElecUsers();
		//����elecUsers����֯List<String>�����ID
		List<String> idList = new ArrayList<String>();
		if(elecUsers!=null && elecUsers.size()>0){
			for(ElecUser elecUser:elecUsers){
				//�û�ID
				String userID = elecUser.getUserID();
				idList.add(userID);
			}
		}
		/** 3��ƥ�䣬��ElecUser����������flag����ֵ�Ĳ���
		    * ���ƥ��ɹ�������1
		    * ���ƥ�䲻�ɹ�������2
		    */
		//�������е��û�����
		if(list!=null && list.size()>0){
			for(ElecUser elecUser:list){
				//��ȡ�û�ID
				String userID = elecUser.getUserID();
				//ƥ��ɹ�
				if(idList.contains(userID)){
					elecUser.setFlag("1");
				}
				else{
					elecUser.setFlag("2");
				}
			}
		}
		return list;
	}

	/**
	 * @Name: saveRole
	 * @Description: �����ɫȨ�޹����������û���ɫ������
	 * @Author: ��ΰ�壨���ߣ�
	 * @Version: V1.00 ���汾�ţ�
	 * @Create Date: 2017-8-05���������ڣ�
	 * @Parameters: ElecPopedom vo����
	 * @Return: ��
	 */
	@Transactional(readOnly=false)
	public void saveRole(ElecPopedom elecPopedom) {
		//��ȡ��ɫID
		String roleID = elecPopedom.getRoleID();
		//��ȡȨ�޵���������ʽpid_mid��
		String[] selectopers = elecPopedom.getSelectoper();
		String[] selectUsers = elecPopedom.getSelectuser();
		/**������ɫȨ�޹�����*/
		this.saveRolePopedom(roleID,selectopers);
		/**�����û���ɫ������*/
		this.saveUserRole(roleID,selectUsers);
	}
	
	/**�����û���ɫ������*/
	private void saveUserRole(String roleID, String[] selectUsers) {
		ElecRole elecRole = elecRoleDao.findObjectByID(roleID);
		Set<ElecUser> set = new HashSet<ElecUser>();
		if(selectUsers!=null && selectUsers.length>0){
			for(String userId:selectUsers){
				ElecUser elecUser = elecUserDao.findObjectByID(userId);
				set.add(elecUser);
			}
		}
		elecRole.setElecUsers(set);
	}
	
	/**������ɫȨ�޹�����*/
	private void saveRolePopedom(String roleID, String[] selectopers) {
		//1��ʹ�ý�ɫID����֯��ѯ��������ѯ��ɫȨ�޹���������List<ElecRolePopedom>
		String condition = " and o.roleID=?";
		String[] params = {roleID};
		List<ElecRolePopedom> list = elecRolePopedomDao.findCollectionByConditionNoPage(condition, params, null);
		//2��ɾ��֮ǰ������List
		elecRolePopedomDao.deleteObjectByCollection(list);
		//3������Ȩ������ID�����飬��֯PO����ִ�б���
		if(selectopers!=null && selectopers.length>0){
			for(String ids:selectopers){
				String[] arrays = ids.split("_");
				ElecRolePopedom elecRolePopedom = new ElecRolePopedom();
				elecRolePopedom.setRoleID(roleID);
				elecRolePopedom.setMid(arrays[0]);
				elecRolePopedom.setPid(arrays[1]);
				elecRolePopedomDao.save(elecRolePopedom);
			}
		}
	}

	/**  
	* @Name: findPopedomByRoleIDs
	* @Description: ʹ�ý�ɫID��Hashtable�ļ��ϣ���ȡ��ɫ��Ӧ��Ȩ�޲���
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-08-06���������ڣ�
	* @Parameters: Hashtable����ɫID�ļ���
	* @Return: String����ʾȨ�޵��ַ�����
	* ��ŵ�Ȩ�޵�mid���ַ����ĸ�ʽ��aa@ab@ac@ad@ae��
	* 
	* ʹ��hql������sql��䣺
	* SELECT DISTINCT o.mid FROM elec_role_popedom o WHERE 1=1 AND o.roleID IN ('1','2');
	*/
	public String findPopedomByRoleIDs(Hashtable<String, String> ht) {
		//��֯��ѯ����
		StringBuffer buffercondition = new StringBuffer("");
		//����Hashtable
		if(ht!=null && ht.size()>0){
			for(Iterator<Entry<String, String>> ite = ht.entrySet().iterator();ite.hasNext();){
				Entry<String, String> entry = ite.next();
				buffercondition.append("'").append(entry.getKey()).append("'").append(",");
			}
			//ɾ�����һ������
			buffercondition.deleteCharAt(buffercondition.length()-1);
		}
		//��ѯ����
		String condition = buffercondition.toString();
		//��֯��ѯ
		List<Object> list = elecRolePopedomDao.findPopedomByRoleIDs(condition);
		//��֯Ȩ�޷�װ���ַ��������ַ����ĸ�ʽ��aa@ab@ac@ad@ae��
		StringBuffer buffer = new StringBuffer("");
		if(list!=null && list.size()>0){
			for(Object o:list){
				buffer.append(o.toString()).append("@");
			}
			//ɾ�����һ��@
			buffer.deleteCharAt(buffer.length()-1);
		}
		return buffer.toString();
	}

	/**  
	* @Name: findPopedomListByString
	* @Description: ʹ��Ȩ�޵��ַ�����ѯ��ǰ�û���Ӧ��Ȩ��
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-08-07���������ڣ�
	* @Parameters: String����ʾȨ�޵��ַ������ַ����ĸ�ʽ��aa@ab@ac@ad@ae��
	* @Return: List<ElecPopedom> Ȩ�޵ļ���
	* ��ŵ�Ȩ�޵�mid���ַ����ĸ�ʽ��aa@ab@ac@ad@ae��
	* 
	* ʹ��hql������sql��䣺
	* SELECT DISTINCT o.mid FROM elec_role_popedom o WHERE 1=1 AND o.roleID IN ('1','2');
	*/
	public List<ElecPopedom> findPopedomListByString(String popedom) {
		String condition = " and o.isMenu=? and o.mid in('"+popedom.replace("@", "','")+"')";
		Object[] params = {true};
		Map<String, String> orderby = new HashMap<String, String>();
		orderby.put("o.mid", "asc");
		List<ElecPopedom> list = elecPopedomDao.findCollectionByConditionNoPage(condition, params, orderby);
		System.out.println(list.size());
		return list;
	}

	/**  
	* @Name: findRolePopedomByID
	* @Description: ʹ�ý�ɫID��Ȩ�޵�code������Ȩ�޵�code��ѯ��ɫȨ�޹������жϵ�ǰ�����Ƿ���Է���Action�ϵķ���
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-08-08���������ڣ�
	* @Parameters: String roleID����ɫID
	* 			 , String mid��Ȩ��code
	* 			 , String pid������Ȩ�޵�code
	* @Return: boolean
	* 			true�����Է���
	* 			false��û��Ȩ�ޣ���ʱ�ܾ�����
	*/
	public boolean findRolePopedomByID(String roleID, String mid, String pid) {
		//��֯��ѯ����
		String condition = "";
		List<Object> paramsList = new ArrayList<Object>();
		//��ɫID
		if(StringUtils.isNotBlank(roleID)){
			condition += " and o.roleID = ?";
			paramsList.add(roleID);
		}
		//��Ȩ������
		if(StringUtils.isNotBlank(mid)){
			condition += " and o.mid = ?";
			paramsList.add(mid);
		}
		//��Ȩ������
		if(StringUtils.isNotBlank(pid)){
			condition += " and o.pid = ?";
			paramsList.add(pid);
		}
		Object [] params = paramsList.toArray();
		//��ѯ��Ӧ�Ľ�ɫȨ����Ϣ
		List<ElecRolePopedom> list = elecRolePopedomDao.findCollectionByConditionNoPage(condition, params, null);
		boolean flag = false;
		if(list!=null && list.size()>0){
			flag = true;
		}
		return flag;

	}

}
