package com.weige.elec.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.weige.elec.dao.IElecUserDao;
import com.weige.elec.domain.ElecUser;
import com.weige.elec.utils.PageInfo;



@Repository(IElecUserDao.SERVICE_NAME)
public class ElecUserDaoImpl  extends CommonDaoImpl<ElecUser> implements IElecUserDao {

	
	/**ָ����ѯ��������ѯ�б�*/
	/**
	 * SELECT * FROM elec_text o WHERE 1=1     #Dao��
		AND o.textName LIKE '%��%'   #Service��
		AND o.textRemark LIKE '%��%'   #Service��
		ORDER BY o.textDate ASC,o.textName DESC  #Service��
	 */
	@Override
	public List<ElecUser> findCollectionByConditionNoPageWithSql(String condition,
			final Object[] params, Map<String, String> orderby) {
		//hql���
		String sql = "SELECT o.userID,o.logonName,o.userName,a.ddlName,o.contactTel,o.onDutyDate,b.ddlName FROM elec_user o "+
				" INNER JOIN elec_systemddl a ON o.sexID = a.ddlCode AND a.keyword='�Ա�'"+
				" INNER JOIN elec_systemddl b ON o.postID = b.ddlCode AND b.keyword='ְλ'"+
				" WHERE 1 = 1";
		//��Map�����д�ŵ��ֶ�������֯��ORDER BY o.textDate ASC,o.textName DESC
		String orderbyCondition = this.orderbyHql(orderby);
		//�������
		final String finalSql = sql + condition + orderbyCondition;
		//��ѯ��ִ��sql���
		
		/**������*/
		List<Object[]> list = this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				//��ͶӰ��ѯ���ֶ��õ�����ͬ���ֶΣ�Ϊ�����֣���Ҫʹ�ñ�����ѯ
				Query query = session.createSQLQuery(finalSql)
								.addScalar("o.userID")
								.addScalar("o.logonName")
								.addScalar("o.userName")
								.addScalar("a.ddlName")
								.addScalar("o.contactTel")
								.addScalar("o.onDutyDate")
								.addScalar("b.ddlName");
				if(params!=null && params.length>0){
					for(int i=0;i<params.length;i++){
						query.setParameter(i, params[i]);
					}
				}
				return query.list();
			}
			
		});
		//��Object����ת����Object����
		List<ElecUser> userList = new ArrayList<ElecUser>();
		if(list!=null && list.size()>0){
			for(Object[] o:list){
				ElecUser elecUser = new ElecUser();
				//SELECT o.userID,o.logonName,o.userName,a.ddlName,o.contactTel,o.onDutyDate,b.ddlName
				elecUser.setUserID(o[0].toString());
				elecUser.setLogonName(o[1].toString());
				elecUser.setUserName(o[2].toString());
				elecUser.setSexID(o[3].toString());
				elecUser.setContactTel(o[4].toString());
				elecUser.setOnDutyDate((Date)o[5]);
				elecUser.setPostID(o[6].toString());
				userList.add(elecUser);
			}
		}
		
		return userList;
	}
	
	/**��Map�����д�ŵ��ֶ�������֯��
	 * ORDER BY o.textDate ASC,o.textName DESC*/
	private String orderbyHql(Map<String, String> orderby) {
		StringBuffer buffer = new StringBuffer();
		if(orderby!=null && orderby.size()>0){
			buffer.append(" ORDER BY ");
			for(Map.Entry<String, String> map:orderby.entrySet()){
				buffer.append(map.getKey()+" "+map.getValue()+",");
			}
			//��ѭ�������ɾ�����һ������
			buffer.deleteCharAt(buffer.length()-1);
		}
		return buffer.toString();
	}

	/**  
	* @Name: chartUser
	* @Description: ͳ���û��������
	* @Author: ��ΰ�壨���ߣ�
	* @Version: V1.00 ���汾�ţ�
	* @Create Date: 2017-8-29���������ڣ�
	* @Parameters: String zName ���ݵ���������
	* 				String eName �ֶ�����
	* @Return: List<Object[]>�� �û����ݼ���
	*/
	public List<Object[]> chartUser(String zName, String eName) {
		final String sql = "SELECT b.keyword,b.ddlName,COUNT(b.ddlCode) FROM elec_user a " +
			 	 " INNER JOIN elec_systemddl b ON a."+eName+" = b.ddlCode AND b.keyword='"+zName+"' " +
			 	 " WHERE a.isDuty='1' " +
			 	 " GROUP BY b.keyword,b.ddlName " +
			 	 " ORDER BY b.ddlCode ASC ";
		List<Object[]> list = this.getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				return query.list();
			}
		});
		return list;
	}

}
