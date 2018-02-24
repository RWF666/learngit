package com.weige.admincontroller;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weige.model.TbItem;
import com.weige.model.TbItemParamItem;
import com.weige.pojo.EasyUIResult;
import com.weige.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	/**
	 * ������Ʒ
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST,value="/save")
	public ResponseEntity<Void> saveItem(TbItem item,@RequestParam("desc") String desc,String itemParams){
		try {
			item.setId(null);//ǿ��idΪnull��Ϊ��ϵͳ��ȫ
			item.setCreated(new Date());
			item.setUpdated(new Date());
			item.setStatus(1);
			itemService.save(item,desc,itemParams);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	/**
	 * ��ѯ�б�
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	public ResponseEntity<EasyUIResult> queryItemLsit(Integer page,Integer rows){
		try {
			PageHelper.startPage(page, rows);
			List<TbItem> itemLsit = itemService.queryItemLsit();
			PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(itemLsit);
			return ResponseEntity.ok(new EasyUIResult(pageInfo.getTotal(),pageInfo.getList()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * ɾ����Ʒ (�߼�ɾ��)
	 * @param ids
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST,value="/delete")
	public ResponseEntity<Void> delete(@RequestParam("ids")List<Long> ids){
		try {
			itemService.deleteInIds(ids);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	/**
	 * ������Ʒ
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST,value="/update")
	public ResponseEntity<Void> updateItem(TbItem item,@RequestParam("desc") String desc,
			String itemParams,Long itemParamId){
		try {
			item.setUpdated(new Date());
			item.setStatus(1);
			TbItemParamItem tbItemParamItem = new TbItemParamItem();
			tbItemParamItem.setId(itemParamId);
			tbItemParamItem.setParamData(itemParams);
			tbItemParamItem.setUpdated(new Date());
			
			itemService.update(item,desc,tbItemParamItem);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
