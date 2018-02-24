package com.weige.customercontroller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Pattern.Flag;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mysql.fabric.xmlrpc.base.Array;
import com.weige.model.TbUser;
import com.weige.pojo.EasyUIResult;
import com.weige.service.BaseService;
import com.weige.service.IndexService;
import com.weige.service.RedisService;
import com.weige.service.UserService;
import com.weige.threadlocal.UserThreadLocal;
import com.weige.utils.CookieUtils;
import com.weige.utils.MyCookieUtiles;

 
@Controller
@RequestMapping("/customer/user")
public class UserController extends BaseService{
	@Autowired
	private UserService userService;
	
	@Autowired
	private RedisService redisService;
	
	@RequestMapping("/register")
	public String index(Map<String, Object> map){
		return "customer/register";
	}
	
	@RequestMapping("/toLogin")
	public String toLogin(Map<String, Object> map,HttpServletRequest request){
		String from = request.getHeader("Referer").toString();
		map.put("from", from);
		return "customer/login";
	}
	
	@RequestMapping(value="/check/{param}/{type}",method=RequestMethod.GET)
	public ResponseEntity<Boolean> check(@PathVariable("param")String param,
			@PathVariable("type")Integer type){
		try {
			Boolean flag = userService.check(param,type);
			if(flag==null){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
			return ResponseEntity.ok(!flag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value="doRegister",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doRegister(@Valid TbUser tbUser,BindingResult bindingResult){
		Map<String, Object> result = new HashMap<String, Object>();
		if(bindingResult.hasErrors()){
			result.put("status", "400");
			List<ObjectError> allErrors = bindingResult.getAllErrors();
			List<String> msg = new ArrayList<String>();
			for(ObjectError error : allErrors){
				msg.add(error.getDefaultMessage());
			}
			result.put("msg",StringUtils.join(msg,"|"));
			return result;
		}
		
		
		try {
			Boolean falg = userService.saveUser(tbUser);
			if(falg){
				result.put("status", "200");
			}else {
				result.put("status", "400");
			}
		} catch (Exception e) {
			result.put("status", "500");
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value="doLogin",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doLogin(TbUser tbUser,
			HttpServletRequest request,HttpServletResponse response,String from){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<Object> list = userService.doLogin(tbUser);
			String token = (String) list.get(0);
			TbUser user = (TbUser) list.get(1);
			if(null==token){
				//µÇÂ¼Ê§°Ü
			}else {
				result.put("status", "200");
				System.out.println(from);
				result.put("from",from);
				userService.updateCart(request,user.getId(),response);
				MyCookieUtiles.addCookie(TAOTAO_TOKEN, token, -1, false, response);
			}
		} catch (JsonProcessingException e) {
			result.put("status", "500");
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value="logout",method=RequestMethod.GET)
	public String logout(HttpServletRequest request,HttpServletResponse response,String from){
		
		String token = CookieUtils.getCookieValue(request, TAOTAO_TOKEN);
		try {
			TbUser tbUser = MAPPER.readValue(redisService.get("TOKEN_"+token), TbUser.class);
			redisService.del("TOKEN_"+token);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MyCookieUtiles.deleteCookie(TAOTAO_TOKEN, "", false, response);
		return "redirect:/customer/index";
	}
	
	@RequestMapping(value="query/{token}",method=RequestMethod.GET)
	public ResponseEntity<TbUser> queryUserByToken(@PathVariable("token")String token){
		try {
			TbUser user = userService.queryUserByToken(token);
			if(user==null){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
