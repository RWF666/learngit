package com.weige.admincontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ͨ�õ�ҳ����ת�߼�
 * @author RWF
 *
 */
@Controller
@RequestMapping("/page")
public class PageController {
	
	@RequestMapping(value="{pageName}")
	public String toLogin(@PathVariable("pageName")String pageName){
		return pageName;
	}
}
