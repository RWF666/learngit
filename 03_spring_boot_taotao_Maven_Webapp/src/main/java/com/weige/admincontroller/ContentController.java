package com.weige.admincontroller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.border.TitledBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weige.model.TbContent;
import com.weige.model.TbContentCategory;
import com.weige.model.TbItem;
import com.weige.model.TbItemCat;
import com.weige.model.TbItemParamItem;
import com.weige.pojo.EasyUIResult;
import com.weige.service.ContentCategoryService;
import com.weige.service.ContentService;
import com.weige.service.ItemService;

@Controller
@RequestMapping("/content")
public class ContentController {
	@Autowired
	private ContentService contentService;
	
	/**
	 * 添加内容
	 * @param tbContent
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public ResponseEntity<Void> addContent(TbContent tbContent){
		try {
			contentService.save(tbContent);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	/**
	 * 查询内容
	 * @param categoryId
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ResponseEntity<List<TbContent>> listContent(Long categoryId,Integer page,Integer rows){
		try {
			PageHelper.startPage(page, rows);
			List<TbContent> contentList = contentService.listContent(categoryId);
			return ResponseEntity.ok(contentList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public ResponseEntity<Void> delete(@RequestParam("ids")List<Long> ids){
		try {
			contentService.deleteContentInIds(ids);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/update")
	public ResponseEntity<Void> updateContent(TbContent content){
		try {
			contentService.update(content);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
