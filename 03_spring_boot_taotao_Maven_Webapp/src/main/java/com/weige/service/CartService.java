package com.weige.service;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weige.mapper.TbCartMapper;
import com.weige.mapper.TbUserMapper;
import com.weige.model.TbCart;
import com.weige.model.TbCartExample;
import com.weige.model.TbCartExample.Criteria;
import com.weige.model.TbItem;
import com.weige.model.TbOrder;
import com.weige.model.TbOrderItem;
import com.weige.model.TbUser;
import com.weige.model.TbUserExample;
import com.weige.threadlocal.UserThreadLocal;

@Service
public class CartService{
	
	@Autowired
	private TbCartMapper tbCartMapper;
	
	@Autowired
	private ItemService itemService;

	public void addItemToCart(Long itemId) {
		//判断该商品是否存在于购物车中
		//如果存在就数量加1
		//如果不存在就新建购物车
		
		TbCartExample example =new TbCartExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(UserThreadLocal.get().getId());
		criteria.andItemIdEqualTo(itemId);
		List<TbCart> cartList = tbCartMapper.selectByExample(example);
		
		if(cartList.size()==0){
			//不存在
			//查询商品
			TbItem item = itemService.findByItemId(itemId);
			
			TbCart cart = new TbCart(new Date(),new Date());
			cart.setId(null);
			cart.setUserId(UserThreadLocal.get().getId());
			cart.setItemId(itemId);
			cart.setItemImage(item.getImage());
			cart.setItemPrice(item.getPrice());
			cart.setItemTitle(item.getTitle());
			cart.setNum(1);
			
			tbCartMapper.insert(cart);
			
		}else {
			//存在
			TbCart oldCart = cartList.get(0);
			oldCart.setNum(oldCart.getNum()+1);
			oldCart.setUpdated(new Date());
			tbCartMapper.updateByPrimaryKeySelective(oldCart);
		}
		
		
	}

	public List<TbCart> queryCartList() {
		TbCartExample example =new TbCartExample();
		example.setOrderByClause("created desc");
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(UserThreadLocal.get().getId());
		List<TbCart> cartList = tbCartMapper.selectByExample(example);
		return cartList;
	}

	public void deleteItem(Long itemId) {
		TbCartExample example =new TbCartExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(UserThreadLocal.get().getId());
		criteria.andItemIdEqualTo(itemId);
		tbCartMapper.deleteByExample(example);
	}

	public void updateItem(Long itemId, Integer num) {
		TbCartExample example =new TbCartExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(UserThreadLocal.get().getId());
		criteria.andItemIdEqualTo(itemId);
		
		TbCart cart = new TbCart();
		cart.setNum(num);
		cart.setUpdated(new Date());
		tbCartMapper.updateByExampleSelective(cart, example);
	}

	public void deleteCart(Long userId, TbOrder order) {
		for(TbOrderItem tbOrderItem : order.getOrderItems()){
			TbCartExample example = new TbCartExample();
			Criteria criteria = example.createCriteria();
			criteria.andUserIdEqualTo(userId);
			criteria.andItemIdEqualTo(tbOrderItem.getItemId());
			tbCartMapper.deleteByExample(example);
		}
		
		
	}

}
