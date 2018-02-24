package com.weige.admincontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weige.model.TbItemCat;
import com.weige.model.TbItemDesc;
import com.weige.service.ItemCatService;

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/list")
	@ResponseBody
	public List<TbItemCat> queryItemCatList(@RequestParam(value="id",defaultValue="0") Long id){
		List<TbItemCat> ItemCatList = itemCatService.queryItemCatList(id);
		return ItemCatList;
	}
	
	@RequestMapping(value="{id}",method=RequestMethod.GET)
	public ResponseEntity<TbItemCat> queryItemCatById(@PathVariable("id")Long id){
		try {
			TbItemCat itemCat = itemCatService.queryItemCatById(id);
			if(itemCat==null){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(itemCat);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
