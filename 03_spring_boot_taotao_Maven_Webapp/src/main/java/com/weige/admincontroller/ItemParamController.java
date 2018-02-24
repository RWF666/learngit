package com.weige.admincontroller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weige.model.TbItem;
import com.weige.model.TbItemParam;
import com.weige.pojo.EasyUIResult;
import com.weige.service.ItemParamService;

@Controller
@RequestMapping("/item/param")
public class ItemParamController {
	@Autowired
	private ItemParamService itemParamService;
	
	@RequestMapping(value="query/{itemCatId}",method=RequestMethod.GET)
	public ResponseEntity<TbItemParam> queryByItemCatId(@PathVariable("itemCatId")Long itemCatId){
		try {
			TbItemParam tbItemParam = itemParamService.queryByItemCatId(itemCatId);
			if(tbItemParam==null){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(tbItemParam);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value="/save/{itemCatId}",method=RequestMethod.POST)
	public ResponseEntity<TbItemParam> saveIteParam(@PathVariable("itemCatId")Long itemCatId,
			@RequestParam("paramData")String paramData){
		
		try {
			TbItemParam tbItemParam = new TbItemParam(new Date(),new Date());
			tbItemParam.setItemCatId(itemCatId);  
			tbItemParam.setParamData(paramData);
			itemParamService.save(tbItemParam);
			return ResponseEntity.status(HttpStatus.CREATED).build();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@RequestMapping(value="/list")
	public ResponseEntity<EasyUIResult> queryItemLsit(Integer page,Integer rows){
		try {
			PageHelper.startPage(page, rows);
			List<TbItemParam> itemLsit = itemParamService.listItemParam();
			PageInfo<TbItemParam> pageInfo = new PageInfo<TbItemParam>(itemLsit);
			return ResponseEntity.ok(new EasyUIResult(pageInfo.getTotal(),pageInfo.getList()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value="/delete")
	public ResponseEntity<Void> queryItemLsit(@RequestParam("ids")List<Long> ids){
		try {
			itemParamService.deleteInIds(ids);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
