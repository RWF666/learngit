package com.weige.elec.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.weige.elec.dao.ICommonDao;
import com.weige.elec.domain.ElecText;
import com.weige.elec.utils.PageInfo;
import com.weige.elec.utils.TUtil;

public class CommonDaoImpl<T> extends HibernateDaoSupport implements ICommonDao<T>{
	Class classt = TUtil.getActualType(this.getClass());
	@Resource(name="sessionFactory")
	public void setDi(SessionFactory sessionFactory){
		this.setSessionFactory(sessionFactory);
	}
	/**����*/
	public void save(T entity) {
		this.getHibernateTemplate().save(entity);
	}
	/**����*/
	@Override
	public void update(T entity) {
		this.getHibernateTemplate().update(entity);
	}
	/**ʹ������id��ѯ����*/
	@Override
	public T findObjectByID(Serializable id) {
		return (T) this.getHibernateTemplate().get(classt, id);
	}
	/**ʹ������ɾ������*/
	@Override
	public void deleteObjectByIds(Serializable... ids) {
		if(ids!=null && ids.length>=0){
			for (Serializable id:ids) {
				this.getHibernateTemplate().delete(this.getHibernateTemplate().get(classt,id));
			}
		}
	}
	/**ʹ�ü���ɾ��*/
	@Override
	public void deleteObjectByCollection(List<T> list) {
		this.getHibernateTemplate().deleteAll(list);
	}
	/**ָ����ѯ��������ѯ�б�*/
	/**
	 * SELECT * FROM elec_text o WHERE 1=1     #Dao��
		AND o.textName LIKE '%��%'   #Service��
		AND o.textRemark LIKE '%��%'   #Service��
		ORDER BY o.textDate ASC,o.textName DESC  #Service��
	 */
	@Override
	public List<T> findCollectionByConditionNoPage(String condition,
			Object[] params, Map<String, String> orderby) {
		//hql���
		String hql = "from "+classt.getSimpleName()+" o where 1=1 ";
		//��Map�����д�ŵ��ֶ�������֯��ORDER BY o.textDate ASC,o.textName DESC
		String orderbyCondition = this.orderbyHql(orderby);
		//�������
		String finalHql = hql + condition + orderbyCondition;
		//��ѯ��ִ��hql���
		/**����һ*/
		List<T> list = this.getHibernateTemplate().find(finalHql, params);
		/**������*/
		/*SessionFactory sf = this.getHibernateTemplate().getSessionFactory();
		Session s = sf.getCurrentSession();//Session���̰߳�
		Query query = s.createQuery(finalHql);
		if(params!=null && params.length>0){
			for(int i=0;i<params.length;i++){
				query.setParameter(i, params[i]);
			}
		}
		List<T> list = query.list();*/
		/**������*/
		/*List<T> list = this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(finalHql);
				if(params!=null && params.length>0){
					for(int i=0;i<params.length;i++){
						query.setParameter(i, params[i]);
					}
				}
				return query.list();
			}
			
		});*/
		return list;
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
	
	/**��Ӧ��ѯ����ָ����ѯ��������ѯ�б�*/
	/**
	 * SELECT * FROM elec_text o WHERE 1=1     #Dao��
		AND o.textName LIKE '%��%'   #Service��
		AND o.textRemark LIKE '%��%'   #Service��
		ORDER BY o.textDate ASC,o.textName DESC  #Service��
	 */
	@Override
	public List<T> findCollectionByConditionNoPageWithCache(String condition,
			final Object[] params, Map<String, String> orderby) {
		// hql���
		String hql = "from " + classt.getSimpleName() + " o where 1=1 ";
		// ��Map�����д�ŵ��ֶ�������֯��ORDER BY o.textDate ASC,o.textName DESC
		String orderbyCondition = this.orderbyHql(orderby);
		// �������
		final String finalHql = hql + condition + orderbyCondition;
		// ��ѯ��ִ��hql���
		/** ������ */
		List<T> list = this.getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(finalHql);
						if (params != null && params.length > 0) {
							for (int i = 0; i < params.length; i++) {
								query.setParameter(i, params[i]);
							}
						}
						query.setCacheable(true);
						return query.list();
					}

				});
		return list;
	}
	
	@Override
	public List<T> findCollectionByConditionWithPage(String condition,
			final Object[] params, Map<String, String> orderby, final PageInfo pageInfo) {
		//hql���
				String hql = "from "+classt.getSimpleName()+" o where 1=1 ";
				//��Map�����д�ŵ��ֶ�������֯��ORDER BY o.textDate ASC,o.textName DESC
				String orderbyCondition = this.orderbyHql(orderby);
				//�������
				final String finalHql = hql + condition + orderbyCondition;
				//��ѯ��ִ��hql���
				List<T> list = this.getHibernateTemplate().execute(new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(finalHql);
						if(params!=null && params.length>0){
							for(int i=0;i<params.length;i++){
								query.setParameter(i, params[i]);
							}
						}
						/**2017-08-21 ��ӷ���  begin*/
						pageInfo.setTotalResult(query.list().size());
						query.setFirstResult(pageInfo.getBeginResult());//��ǰҳ�ӵڼ�ҳ��ʼ������Ĭ���Ǵ�0��ʼ
						query.setMaxResults(pageInfo.getPageSize());//��ǰҳ�����ʾ����������
						/**2017-08-21 ��ӷ���  end*/
						return query.list();
					}
					
				});
				return list;
	}
	
	/**ָ����Ѱ������ѯ����excel������ݣ�����ҳʹ��ͶӰ*/
	public List findCollectionByConditionNoPageWithSelectCodition(
			String condition, Object[] params, Map<String, String> orderby,
			String selectCondition) {
		// hql���
		String hql = "select "+selectCondition+" from " + classt.getSimpleName() + " o where 1=1 ";
		// ��Map�����д�ŵ��ֶ�������֯��ORDER BY o.textDate ASC,o.textName DESC
		String orderbyCondition = this.orderbyHql(orderby);
		// �������
		String finalHql = hql + condition + orderbyCondition;
		// ��ѯ��ִ��hql���
		/** ����һ */
		List list = this.getHibernateTemplate().find(finalHql, params);
		
		return list;
	}
	/**��������*/
	public void saveList(List<T> list) {
		for(int i = 0;i<list.size();i++){
			this.getHibernateTemplate().saveOrUpdate(list.get(i));
		}
		
	}

}
