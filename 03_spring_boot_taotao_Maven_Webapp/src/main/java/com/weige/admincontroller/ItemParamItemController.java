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
import com.weige.mapper.TbItemParamItemMapper;
import com.weige.model.TbItem;
import com.weige.model.TbItemParam;
import com.weige.model.TbItemParamItem;
import com.weige.pojo.EasyUIResult;
import com.weige.service.ItemParamItemService;
import com.weige.service.ItemParamService;

@Controller
@RequestMapping("/item/param/item")
public class ItemParamItemController {
	@Autowired
	private ItemParamItemService itemParamItemService;
	
	/**
	 * 根据商品id查找ParamItem
	 * @param itemCatId
	 * @return
	 */
	@RequestMapping(value="query/{itemId}",method=RequestMethod.GET)
	public ResponseEntity<TbItemParamItem> queryByItemId(@PathVariable("itemId")Long itemId){
		try {
			TbItemParamItem tbItemParamItem = itemParamItemService.queryByItemId(itemId);
			System.out.println(tbItemParamItem.getParamData());
			if(tbItemParamItem==null){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(tbItemParamItem);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
