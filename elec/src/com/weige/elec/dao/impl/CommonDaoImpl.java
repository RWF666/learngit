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
	/**保存*/
	public void save(T entity) {
		this.getHibernateTemplate().save(entity);
	}
	/**更新*/
	@Override
	public void update(T entity) {
		this.getHibernateTemplate().update(entity);
	}
	/**使用主键id查询对象*/
	@Override
	public T findObjectByID(Serializable id) {
		return (T) this.getHibernateTemplate().get(classt, id);
	}
	/**使用主键删除对象*/
	@Override
	public void deleteObjectByIds(Serializable... ids) {
		if(ids!=null && ids.length>=0){
			for (Serializable id:ids) {
				this.getHibernateTemplate().delete(this.getHibernateTemplate().get(classt,id));
			}
		}
	}
	/**使用集合删除*/
	@Override
	public void deleteObjectByCollection(List<T> list) {
		this.getHibernateTemplate().deleteAll(list);
	}
	/**指定查询条件，查询列表*/
	/**
	 * SELECT * FROM elec_text o WHERE 1=1     #Dao层
		AND o.textName LIKE '%张%'   #Service层
		AND o.textRemark LIKE '%张%'   #Service层
		ORDER BY o.textDate ASC,o.textName DESC  #Service层
	 */
	@Override
	public List<T> findCollectionByConditionNoPage(String condition,
			Object[] params, Map<String, String> orderby) {
		//hql语句
		String hql = "from "+classt.getSimpleName()+" o where 1=1 ";
		//将Map集合中存放的字段排序，组织成ORDER BY o.textDate ASC,o.textName DESC
		String orderbyCondition = this.orderbyHql(orderby);
		//添加条件
		String finalHql = hql + condition + orderbyCondition;
		//查询，执行hql语句
		/**方案一*/
		List<T> list = this.getHibernateTemplate().find(finalHql, params);
		/**方案二*/
		/*SessionFactory sf = this.getHibernateTemplate().getSessionFactory();
		Session s = sf.getCurrentSession();//Session与线程绑定
		Query query = s.createQuery(finalHql);
		if(params!=null && params.length>0){
			for(int i=0;i<params.length;i++){
				query.setParameter(i, params[i]);
			}
		}
		List<T> list = query.list();*/
		/**方案三*/
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
	
	/**将Map集合中存放的字段排序，组织成
	 * ORDER BY o.textDate ASC,o.textName DESC*/
	private String orderbyHql(Map<String, String> orderby) {
		StringBuffer buffer = new StringBuffer();
		if(orderby!=null && orderby.size()>0){
			buffer.append(" ORDER BY ");
			for(Map.Entry<String, String> map:orderby.entrySet()){
				buffer.append(map.getKey()+" "+map.getValue()+",");
			}
			//在循环的最后，删除最后一个逗号
			buffer.deleteCharAt(buffer.length()-1);
		}
		return buffer.toString();
	}
	
	/**适应查询缓存指定查询条件，查询列表*/
	/**
	 * SELECT * FROM elec_text o WHERE 1=1     #Dao层
		AND o.textName LIKE '%张%'   #Service层
		AND o.textRemark LIKE '%张%'   #Service层
		ORDER BY o.textDate ASC,o.textName DESC  #Service层
	 */
	@Override
	public List<T> findCollectionByConditionNoPageWithCache(String condition,
			final Object[] params, Map<String, String> orderby) {
		// hql语句
		String hql = "from " + classt.getSimpleName() + " o where 1=1 ";
		// 将Map集合中存放的字段排序，组织成ORDER BY o.textDate ASC,o.textName DESC
		String orderbyCondition = this.orderbyHql(orderby);
		// 添加条件
		final String finalHql = hql + condition + orderbyCondition;
		// 查询，执行hql语句
		/** 方案三 */
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
		//hql语句
				String hql = "from "+classt.getSimpleName()+" o where 1=1 ";
				//将Map集合中存放的字段排序，组织成ORDER BY o.textDate ASC,o.textName DESC
				String orderbyCondition = this.orderbyHql(orderby);
				//添加条件
				final String finalHql = hql + condition + orderbyCondition;
				//查询，执行hql语句
				List<T> list = this.getHibernateTemplate().execute(new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(finalHql);
						if(params!=null && params.length>0){
							for(int i=0;i<params.length;i++){
								query.setParameter(i, params[i]);
							}
						}
						/**2017-08-21 添加分类  begin*/
						pageInfo.setTotalResult(query.list().size());
						query.setFirstResult(pageInfo.getBeginResult());//当前页从第几页开始检索，默认是从0开始
						query.setMaxResults(pageInfo.getPageSize());//当前页最多显示多少条数据
						/**2017-08-21 添加分类  end*/
						return query.list();
					}
					
				});
				return list;
	}
	
	/**指定产寻条件查询导出excel表的数据，不分页使用投影*/
	public List findCollectionByConditionNoPageWithSelectCodition(
			String condition, Object[] params, Map<String, String> orderby,
			String selectCondition) {
		// hql语句
		String hql = "select "+selectCondition+" from " + classt.getSimpleName() + " o where 1=1 ";
		// 将Map集合中存放的字段排序，组织成ORDER BY o.textDate ASC,o.textName DESC
		String orderbyCondition = this.orderbyHql(orderby);
		// 添加条件
		String finalHql = hql + condition + orderbyCondition;
		// 查询，执行hql语句
		/** 方案一 */
		List list = this.getHibernateTemplate().find(finalHql, params);
		
		return list;
	}
	/**批量保存*/
	public void saveList(List<T> list) {
		for(int i = 0;i<list.size();i++){
			this.getHibernateTemplate().saveOrUpdate(list.get(i));
		}
		
	}

}
