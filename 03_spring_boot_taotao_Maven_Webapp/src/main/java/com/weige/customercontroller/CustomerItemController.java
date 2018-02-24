package com.weige.customercontroller;



import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weige.model.TbItem;
import com.weige.service.ItemCatService;
import com.weige.service.ItemDescService;
import com.weige.service.ItemParamItemService;
import com.weige.service.ItemService;

@Controller
@RequestMapping("/customer/item")
public class CustomerItemController {
	@Autowired
	private ItemService itemService;
	@Autowired
	private ItemDescService itemDescService;
	@Autowired
	private ItemParamItemService itemParamItemService;
	
	@RequestMapping(value="{itemId}",method = RequestMethod.GET)
	public String showItemDetail(@PathVariable("itemId")Long itemId,Map<String, Object> map){
		//基本数据
		map.put("item", itemService.findByItemId(itemId));
		//描述
		map.put("itemDesc", itemDescService.queryDescById(itemId));
		//规格参数
		map.put("itemParam", itemParamItemService.queryParamById(itemId));
		return "customer/item";
	}
}
