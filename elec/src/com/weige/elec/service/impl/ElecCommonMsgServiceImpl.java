package com.weige.elec.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.taglibs.standard.lang.jstl.ELEvaluator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.weige.elec.dao.IElecCommonMsgContentDao;
import com.weige.elec.dao.IElecCommonMsgDao;
import com.weige.elec.dao.IElecTextDao;
import com.weige.elec.domain.ElecCommonMsg;
import com.weige.elec.domain.ElecCommonMsgContent;
import com.weige.elec.domain.ElecText;
import com.weige.elec.service.IElecCommonMsgService;
import com.weige.elec.service.IElecTextService;
import com.weige.elec.utils.StringUtil;

@Service(IElecCommonMsgService.SERVICE_NAME)
@Transactional(readOnly=true)
public class ElecCommonMsgServiceImpl implements IElecCommonMsgService{
	/**运行监控表Dao*/
	@Resource(name=IElecCommonMsgDao.SERVICE_NAME)
	IElecCommonMsgDao elecCommonMsgDao;
	
	
	/**运行监控数据表Dao*/
	@Resource(name=IElecCommonMsgContentDao.SERVICE_NAME)
	IElecCommonMsgContentDao elecCommonMsgContentDao;
	
	
	
//	/**  
//	* @Name: findCommonMsg
//	* @Description: 查询运行监控表的数据
//	* @Author: 饶伟峰（作者）
//	* @Version: V1.00 （版本号）
//	* @Create Date: 2017-7-31（创建日期）
//	* @Parameters: 无
//	* @Return: ElecCommonMsg：返回po对象
//	*/
//	@Override
//	public ElecCommonMsg findCommonMsg() {
//		List<ElecCommonMsg> list = elecCommonMsgDao.findCollectionByConditionNoPage("", null, null);
//		ElecCommonMsg elecCommonMsg = null;
//		if(list!=null && list.size()>0){
//			elecCommonMsg = list.get(0);
//		}
//		return elecCommonMsg;
//	}
//	/**  
//	* @Name: saveCommonMsg
//	* @Description: 保存运行监控表的数据
//	* @Author: 饶伟峰（作者）
//	* @Version: V1.00 （版本号）
//	* @Create Date: 2017-7-31（创建日期）
//	* @Parameters: elecCommonMsg vo对象
//	* @Return: wu
//	*/
//	@Transactional(readOnly=false)
//	public void saveCommonMsg(ElecCommonMsg elecCommonMsg) {
//		List<ElecCommonMsg> list = elecCommonMsgDao.findCollectionByConditionNoPage("", null, null);
//		/**如果数据存在则更新*/
//		if(list!=null && list.size()>0){
//			/**使用快照更新*/
//			ElecCommonMsg commonMag = list.get(0);
//			commonMag.setStationRun(elecCommonMsg.getStationRun());
//			commonMag.setDevRun(elecCommonMsg.getDevRun());
//			commonMag.setCreateDate(new Date());
//		}
//		/**如果数据不存在则保存*/
//		else{
//			elecCommonMsg.setCreateDate(new Date());
//			elecCommonMsgDao.save(elecCommonMsg);
//		}
//	}
	/**  
	* @Name: findElecCommonMsg
	* @Description: 获取运行监控中的数据
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 
	* @Parameters: 无
	* @Return: ElecCommonMsg：封装数据对象
	*/
	public ElecCommonMsg findCommonMsg() {
		List<ElecCommonMsg> list = elecCommonMsgDao.findCollectionByConditionNoPage("", null, null);
		ElecCommonMsg commonMsg = null;
		if(list!=null && list.size()>0){
			commonMsg = list.get(0);
			/**********************************************begin**********************************************************/
			//获取数据内容
			//以类型作为条件，按照显示顺序升序排列，查询站点运行情况的数据
			String stationCondition = " and o.type=?";
			Object [] stationParams = {"1"};
			Map<String, String> stationOrderby = new LinkedHashMap<String, String>();
			stationOrderby.put("o.orderby", "asc");
			List<ElecCommonMsgContent> stationList = elecCommonMsgContentDao.findCollectionByConditionNoPage(stationCondition, stationParams, stationOrderby);
			//获取返回的数据（拼装之后）
			String stationContent = "";
			if(stationList!=null && stationList.size()>0){
				for(ElecCommonMsgContent elecCommonMsgContent:stationList){
					String content = elecCommonMsgContent.getContent();
					stationContent += content;
				}
			}
			//将数据赋值给页面的属性（站点运行情况）
			commonMsg.setStationRun(stationContent);
			/**********************************************************************************/
			//以类型作为条件，按照显示顺序升序排列，查询站点运行情况的数据
			String devCondition = " and o.type=?";
			Object [] devParams = {"2"};
			Map<String, String> devOrderby = new LinkedHashMap<String, String>();
			devOrderby.put("o.orderby", "asc");
			List<ElecCommonMsgContent> devList = elecCommonMsgContentDao.findCollectionByConditionNoPage(devCondition, devParams, devOrderby);
			//获取返回的数据（拼装之后）
			String devContent = "";
			if(devList!=null && devList.size()>0){
				for(ElecCommonMsgContent elecCommonMsgContent:devList){
					String content = elecCommonMsgContent.getContent();
					devContent += content;
				}
			}
			//将数据赋值给页面的属性（设备运行情况）
			commonMsg.setDevRun(devContent);
			/**********************************************end**********************************************************/
		}
		return commonMsg;
	}
	
	/**  
	* @Name: saveElecCommonMsg
	* @Description: 保存运行监控
	* @Author: 刘洋（作者）
	* @Version: V1.00 （版本号）
	* @Create Date: 
	* @Parameters: ElecCommonMsg：封装保存的参数
	* @Return: 无
	*/
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveCommonMsg(ElecCommonMsg elecCommonMsg) {
		/**********************************************begin**********************************************************/
		//保存到数据表中
		//删除之前的数据
		List<ElecCommonMsgContent> contentList = elecCommonMsgContentDao.findCollectionByConditionNoPage("", null, null);
		elecCommonMsgContentDao.deleteObjectByCollection(contentList);
		//从页面获取站点运行情况和设备运行情况，根据站点运行情况，和设备运行情况保存数据
		String stationRun = elecCommonMsg.getStationRun();
		String devRun = elecCommonMsg.getDevRun();
		//调用StirngUtil的方法，分割字符串
		List<String> stationList = StringUtil.getContentByList(stationRun, 50);
		if(stationList!=null && stationList.size()>0){
			for(int i=0;i<stationList.size();i++){
				ElecCommonMsgContent elecCommonMsgContent = new ElecCommonMsgContent();
				elecCommonMsgContent.setType("1");//1表示站点运行情况
				elecCommonMsgContent.setContent(stationList.get(i));
				elecCommonMsgContent.setOrderby(i+1);
				elecCommonMsgContentDao.save(elecCommonMsgContent);
			}
		}
		List<String> devList = StringUtil.getContentByList(devRun, 50);
		if(devList!=null && devList.size()>0){
			for(int i=0;i<devList.size();i++){
				ElecCommonMsgContent elecCommonMsgContent = new ElecCommonMsgContent();
				elecCommonMsgContent.setType("2");//2表示设备运行情况
				elecCommonMsgContent.setContent(devList.get(i));
				elecCommonMsgContent.setOrderby(i+1);
				elecCommonMsgContentDao.save(elecCommonMsgContent);
			}
		}
		/**********************************************end**********************************************************/
		
		
		//查询运行监控表，获取运行监控表的数据，返回List<ElecCommonMsg> list，使用list作为判断数据库中是否存在数据
		List<ElecCommonMsg> list = elecCommonMsgDao.findCollectionByConditionNoPage("", null, null);
		//如果list!=null:数据表表中存在数据，获取页面传递的2个参数，组织PO对象，执行更新（update）
		if(list!=null && list.size()>0){
			//方案一：先删除再创建
			//方案二：组织PO对象，执行update
			ElecCommonMsg commonMsg = list.get(0);
			commonMsg.setStationRun("1");//1表示站点运行情况
			commonMsg.setDevRun("2");//2表示设备运行情况
			commonMsg.setCreateDate(new Date());
		}
		//如果list==null:数据表表中不存在数据，获取页面传递的2个参数，组织PO对象，执行新增（save）
		else{
			ElecCommonMsg commonMsg = new ElecCommonMsg();
			commonMsg.setCreateDate(new Date());
			commonMsg.setStationRun("1");//1表示站点运行情况
			commonMsg.setDevRun("2");//2表示设备运行情况
			elecCommonMsgDao.save(commonMsg);
		}
		
		
	}
}
