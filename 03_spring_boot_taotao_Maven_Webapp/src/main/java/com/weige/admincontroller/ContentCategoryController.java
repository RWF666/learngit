package com.weige.admincontroller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.weige.model.TbContentCategory;
import com.weige.model.TbItem;
import com.weige.model.TbItemCat;
import com.weige.model.TbItemParamItem;
import com.weige.pojo.EasyUIResult;
import com.weige.service.ContentCategoryService;
import com.weige.service.ItemService;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	/**
	 * 查询分类
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping("/list")
	public ResponseEntity<List<TbContentCategory>> queryContentCategoryList(@RequestParam(value="id",defaultValue="0") Long id){
		try {
			List<TbContentCategory> contentCategoryList = contentCategoryService.queryContentCategoryList(id);
			if(contentCategoryList.size()==0){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			
			return ResponseEntity.ok(contentCategoryList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping("/add")
	public ResponseEntity<TbContentCategory> addContentCateGory(TbContentCategory tbContentCategory){
		try {
			tbContentCategory.setIsParent(false);
			tbContentCategory.setSortOrder(1);
			tbContentCategory.setStatus(1);
			tbContentCategory.setCreated(new Date());
			tbContentCategory.setUpdated(new Date());
			contentCategoryService.addContentCateGory(tbContentCategory);
			System.out.println(tbContentCategory.getId());
			
			TbContentCategory parent = contentCategoryService.findParentByParentId(tbContentCategory.getParentId());
			if(!parent.getIsParent()){
				parent.setIsParent(true);
				contentCategoryService.update(parent);
			}
			return ResponseEntity.ok(tbContentCategory);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value="/rename")
	public ResponseEntity<Void> rename(TbContentCategory tbContentCategory){
		try {
			contentCategoryService.rename(tbContentCategory);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@RequestMapping(value="/delete")
	public ResponseEntity<Void> delete(TbContentCategory tbContentCategory){
		try {
			List<Long> ids = new ArrayList<Long>();
			ids.add(tbContentCategory.getId());
			//查找该节点的子节点
			findAllIds(tbContentCategory.getId(), ids);
			//删除数据
			contentCategoryService.deleteInIds(ids);
			
			//查找当前节点的父节点是否还有子节点，如果没有，将父节点设置为isparent=false
			TbContentCategory parent = contentCategoryService.findParentByParentId(tbContentCategory.getParentId());
			List<TbContentCategory> allSonContentCategories = contentCategoryService.findAllSonByParentId(parent.getId());
			if(allSonContentCategories.size()==0){
				parent.setIsParent(false);
				contentCategoryService.update(parent);
			}
			
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	private void findAllIds(Long parentId,List<Long> ids){
		List<TbContentCategory> tbContentCategories = contentCategoryService.findAllSonByParentId(parentId);
		for(TbContentCategory tbContentCategory:tbContentCategories){
			ids.add(tbContentCategory.getId());
			if(tbContentCategory.getIsParent()){
				findAllIds(tbContentCategory.getId(), ids);
			}
		}
	}
}
