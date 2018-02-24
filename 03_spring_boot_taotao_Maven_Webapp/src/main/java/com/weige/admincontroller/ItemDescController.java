package com.weige.admincontroller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.LongAccumulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weige.model.TbItem;
import com.weige.model.TbItemDesc;
import com.weige.pojo.EasyUIResult;
import com.weige.service.ItemDescService;
import com.weige.service.ItemService;

@Controller
@RequestMapping("/item/desc")
public class ItemDescController {
	@Autowired
	private ItemDescService itemDescService;
	
	/**
	 * ≤È—Ø√Ë ˆ
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="{itemId}")
	public ResponseEntity<TbItemDesc> queryDescById(@PathVariable("itemId")Long itemId){
		try {
			TbItemDesc tbItemDesc = itemDescService.queryDescById(itemId);
			if(tbItemDesc==null){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			
			return ResponseEntity.ok(tbItemDesc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
