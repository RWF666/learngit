package com.weige.customercontroller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.weige.service.IndexService;


@Controller
@RequestMapping("/customer")
public class IndexController {
	@Autowired
	private IndexService indexService;
	@RequestMapping("/index")
	public String index(Map<String, Object> map){
		String indexAd1 = indexService.getIndexAd1(new Long(21));
		map.put("indexAd1",indexAd1);
		return "customer/index";
	}
}
