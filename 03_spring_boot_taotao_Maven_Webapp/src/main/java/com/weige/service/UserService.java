package com.weige.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.weige.mapper.TbCartMapper;
import com.weige.mapper.TbUserMapper;
import com.weige.model.TbCart;
import com.weige.model.TbCartExample;
import com.weige.model.TbUser;
import com.weige.model.TbUserExample;
import com.weige.model.TbUserExample.Criteria;
import com.weige.threadlocal.UserThreadLocal;
import com.weige.utils.CookieUtils;
import com.weige.utils.MyCookieUtiles;

@Service
public class UserService extends BaseService{
	@Autowired
	private TbUserMapper tbUserMapper;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private TbCartMapper tbCartMapper;
	
	
	
	private static final Map<Integer, Boolean> TYPE = new HashMap<Integer, Boolean>();
	
	static{
		TYPE.put(1, true);
		TYPE.put(2, true);
		TYPE.put(3, true);
	}

	public Boolean check(String param, Integer type) {
		if(!TYPE.containsKey(type)){
			return null;
		}
		
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		switch (type) {
		case 1:
			criteria.andUsernameEqualTo(param);
			break;
		case 2:
			criteria.andPhoneEqualTo(param);
			break;
		case 3:
			criteria.andEmailEqualTo(param);
			break;
		}
		
		List<TbUser> tbUserList = tbUserMapper.selectByExample(example);
		return tbUserList.size()==0;
	}
	
	@Transactional
	public Boolean saveUser(TbUser tbUser) {
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		
		//加密处理
		tbUser.setPassword(DigestUtils.md5Hex(tbUser.getPassword()));
		return tbUserMapper.insert(tbUser)==1;
	}
	
	@Transactional
	public List<Object> doLogin(TbUser tbUser) throws JsonProcessingException{
		List<Object> list = new ArrayList<Object>();
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(tbUser.getUsername());
		List<TbUser> selectByExample = tbUserMapper.selectByExample(example);
		if(selectByExample.size()>0){
			if(!selectByExample.get(0).getPassword()
					.equals(DigestUtils.md5Hex(tbUser.getPassword()))){
				return null;
			}else {
				String token =DigestUtils.md5Hex(System.currentTimeMillis()
						+selectByExample.get(0).getUsername());
				redisService.set("TOKEN_"+token, MAPPER.writeValueAsString(selectByExample.get(0)), 60*30);
				list.add(token);
				list.add(selectByExample.get(0));
				return list;
			}
		}
		//用户不存在
		return null;
	}

	public TbUser queryUserByToken(String token) throws Exception {
		String jsonData = redisService.get("TOKEN_"+token);
		if(null==jsonData){
			//登录超时
			return null;
		}
		
		TbUser user = MAPPER.readValue(jsonData, TbUser.class);
		
		//重新设置生存时间
		redisService.expire("TOKEN_"+token,60*30);
		return user;
	}

	public void updateCart(HttpServletRequest request,Long userId,HttpServletResponse response) {
		
		String cookieCart = CookieUtils.getCookieValue(request, COOKIE_CART, true);
		List<TbCart> cartList = null;
		try {
			cartList = MAPPER.readValue(cookieCart, MAPPER.getTypeFactory()
					.constructCollectionLikeType(List.class, TbCart.class));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(cartList!=null){
			for(TbCart c: cartList){
				TbCartExample example =new TbCartExample();
				com.weige.model.TbCartExample.Criteria criteria = example.createCriteria();
				criteria.andUserIdEqualTo(userId);
				criteria.andItemIdEqualTo(c.getItemId());
				List<TbCart> list = tbCartMapper.selectByExample(example);
				if(list.size()>0){
					TbCart tbCart = list.get(0);
					tbCart.setNum(tbCart.getNum()+c.getNum());
					tbCartMapper.updateByPrimaryKeySelective(tbCart);
				}else {
					c.setUserId(userId);
					tbCartMapper.insert(c);
				}
				
			}
			MyCookieUtiles.deleteCookie(COOKIE_CART, "", true, response);
		}
		
		
	}
}
