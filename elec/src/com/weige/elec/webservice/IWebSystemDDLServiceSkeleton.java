
/**
 * IWebSystemDDLServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.6  Built on : Jul 30, 2017 (09:08:31 BST)
 */
    package com.weige.elec.webservice;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.weige.elec.dao.IElecSystemDDLDao;
import com.weige.elec.domain.ElecSystemDDL;
    /**
     *  IWebSystemDDLServiceSkeleton java skeleton for the axisService
     */
    public class IWebSystemDDLServiceSkeleton{
        
         
        /**
         * Auto generated method signature
         * 
                                     * @param findSystemByKeyword 
             * @return findSystemByKeywordResponse 
         */
        
                 public com.weige.elec.webservice.FindSystemByKeywordResponse findSystemByKeyword
                  (
                  com.weige.elec.webservice.FindSystemByKeyword findSystemByKeyword
                  )
                 {
                     //实现代码
                     	String keyword = findSystemByKeyword.getArgs0();
                      	ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
                   		IElecSystemDDLDao elecSystemDDLDao = (IElecSystemDDLDao) ac.getBean(IElecSystemDDLDao.SERVICE_NAME);
                   		
                   		//组织查询条件
                   		String condition = "";
                   		List<Object> paramsList = new ArrayList<Object>();
                   		if(StringUtils.isNotBlank(keyword)){
                   			condition += " and o.keyword = ?";
                   			paramsList.add(keyword);
                   		}
                   		Object [] params = paramsList.toArray();
                   		//排序语句
                   		Map<String, String> orderby = new LinkedHashMap<String, String>();
                   		orderby.put("o.ddlCode","asc");//按照数据项的编号升序排列
                   		//数据字典进行查询的时候
                   		List<ElecSystemDDL> list = elecSystemDDLDao.findCollectionByConditionNoPage(condition, params, orderby);
                   		
                   		/**将查询的List<com.itheima.elec.domain.ElecSystemDDL>集合转换成数组com.itheima.elec.domain.xsd.ElecSystemDDL[]*/
                   		com.weige.elec.domain.xsd.ElecSystemDDL[] elecSystemDDLs = new com.weige.elec.domain.xsd.ElecSystemDDL[list.size()];
                   		if(list!=null && list.size()>0){
                   			for(int i=0;i<list.size();i++){
                   				//持久对象
                   				ElecSystemDDL ddl = list.get(i);
                   				//将持久化对象的属性全部拷贝到webservice的封装的对象
                   				com.weige.elec.domain.xsd.ElecSystemDDL elecSystemDDL  = new com.weige.elec.domain.xsd.ElecSystemDDL();
                   				try {
     								BeanUtils.copyProperties(elecSystemDDL, ddl);
     							} catch (Exception e) {
     								e.printStackTrace();
     							} 
                   				//将elecSystemDDL对象放置到数组中
                   				elecSystemDDLs[i] = elecSystemDDL;
                   			}
                   		}
                   		
                   		//封装结果
                   		FindSystemByKeywordResponse response = new FindSystemByKeywordResponse();
                   		response.set_return(elecSystemDDLs);
                   		return response;

                     	 
             }
     
    }
    