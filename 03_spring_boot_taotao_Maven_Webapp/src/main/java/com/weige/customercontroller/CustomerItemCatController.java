package com.weige.customercontroller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weige.service.ItemCatService;

@Controller
@RequestMapping("/customer/item/cat")
public class CustomerItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@RequestMapping(value="/all",produces=MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> queryItemCatAllToJson(){
		try {
			String json = MAPPER.writeValueAsString(itemCatService.queryAllToTree());
			return ResponseEntity.ok("category.getDataService("+json+")");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
}
