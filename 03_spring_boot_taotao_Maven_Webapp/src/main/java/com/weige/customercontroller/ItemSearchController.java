package com.weige.customercontroller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.weige.model.TbItem;
import com.weige.pojo.SolrResult;
import com.weige.service.SolrjService;

/**
 * 商品搜索
 * @author RWF
 *
 */
@Controller
@RequestMapping("/customer/search")
public class ItemSearchController {
	
	@Autowired
	private SolrjService solrjService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String search(@RequestParam("keyword") String keyword,Map<String, Object> map,
			@RequestParam(value="page",defaultValue="1") Integer page){
		try {
			SolrResult<TbItem> pageInfo = solrjService.search(keyword, page);
			map.put("query", keyword);
			map.put("total", pageInfo.getTotal());
			map.put("itemList", pageInfo.getRows());
			map.put("page", page);//当前页
			map.put("pages", (pageInfo.getTotal()+SolrjService.ROWS-1)/SolrjService.ROWS);//总页数
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "customer/search";
	}
}
