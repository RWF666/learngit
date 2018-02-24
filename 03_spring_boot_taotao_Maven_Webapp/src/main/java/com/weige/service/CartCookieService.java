package com.weige.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.weige.mapper.TbCartMapper;
import com.weige.model.TbCart;
import com.weige.model.TbItem;
import com.weige.threadlocal.UserThreadLocal;
import com.weige.utils.CookieUtils;
import com.weige.utils.MyCookieUtiles;

@Service
public class CartCookieService extends BaseService{
	
	@Autowired
	private ItemService itemService;

	public void addItemToCart(HttpServletRequest request,
			HttpServletResponse response, Long itemId) {
		//判断该商品是否存在于购物车中
		//如果存在就数量加1
		//如果不存在就新建购物车
		try {
			List<TbCart> cartList = queryCartList(request);
			TbCart cart = null;
			for(TbCart c:cartList){
				if(c.getItemId().intValue()==itemId.intValue()){
					//该商品已经存在
					cart = c;
					break;
				}
			}
			
			if(cart==null){
				//不存在
				TbItem item = itemService.findByItemId(itemId);
				
				cart = new TbCart(new Date(),new Date());
				cart.setId(null);
				cart.setItemId(itemId);
				cart.setItemImage(item.getImage());
				cart.setItemPrice(item.getPrice());
				cart.setItemTitle(item.getTitle());
				cart.setNum(1);
				
				cartList.add(cart);
			}else {
				//存在
				cart.setNum(cart.getNum()+1);
			}
			
			MyCookieUtiles.addCookie(COOKIE_CART, MAPPER.writeValueAsString(cartList),
					60*60*24, true, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	public List<TbCart> queryCartList(HttpServletRequest request) {
		String cookieCart = CookieUtils.getCookieValue(request, COOKIE_CART, true);
		if(StringUtils.isEmpty(cookieCart)){
			return new ArrayList<TbCart>();
		}
		try {
			List<TbCart> cartList = MAPPER.readValue(cookieCart, MAPPER.getTypeFactory()
					.constructCollectionLikeType(List.class, TbCart.class));
			return cartList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<TbCart>();
	}

	public void deleteItem(Long itemId, HttpServletRequest request,
			HttpServletResponse response) {
		List<TbCart> cartList = queryCartList(request);
		for(TbCart c:cartList){
			if(c.getItemId().intValue()==itemId.intValue()){
				//该商品已经存在
				cartList.remove(c);
				break;
			}
		}
		try {
			MyCookieUtiles.addCookie(COOKIE_CART, MAPPER.writeValueAsString(cartList),
					60*60*24, true, response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateItem(Long itemId, Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		List<TbCart> cartList = queryCartList(request);
		TbCart cart = null;
		for(TbCart c:cartList){
			if(c.getItemId().intValue()==itemId.intValue()){
				//该商品已经存在
				c.setNum(num);
				break;
			}
		}
		/*cartList.remove(cart);
		cart.setNum(num);
		cartList.add(cart);*/
		
		try {
			MyCookieUtiles.addCookie(COOKIE_CART, MAPPER.writeValueAsString(cartList),
					60*60*24, true, response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
