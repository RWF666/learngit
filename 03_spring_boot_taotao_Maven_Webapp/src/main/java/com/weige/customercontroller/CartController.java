package com.weige.customercontroller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.weige.model.TbCart;
import com.weige.model.TbItem;
import com.weige.model.TbUser;
import com.weige.pojo.SolrResult;
import com.weige.service.BaseService;
import com.weige.service.CartCookieService;
import com.weige.service.CartService;
import com.weige.service.SolrjService;
import com.weige.service.UserService;
import com.weige.threadlocal.UserThreadLocal;
import com.weige.utils.CookieUtils;

/**
 * 商品搜索
 * @author RWF
 *
 */
@Controller
@RequestMapping("/customer/cart")
public class CartController extends BaseService {
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartCookieService cartCookieService;
	
	/**
	 * 商品加入到购物车
	 * @param map
	 * @param itemId
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/add/{itemId}",method=RequestMethod.GET)
	public String addItemToCart(Map<String, Object> map,@PathVariable("itemId")Long itemId,
			HttpServletRequest request,HttpServletResponse response){
		TbUser tbUser = UserThreadLocal.get();
		if(tbUser==null){
			cartCookieService.addItemToCart(request,response,itemId);
		}else {
			cartService.addItemToCart(itemId);
		}
		return "redirect:/customer/cart/show";
	}
	
	/**
	 * 从购物车中删除
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/delete/{itemId}",method=RequestMethod.GET)
	public String deleteItem(Map<String, Object> map,@PathVariable("itemId")String itemId,
			HttpServletRequest request,HttpServletResponse response){
		TbUser tbUser = UserThreadLocal.get();
		if(tbUser==null){
			cartCookieService.deleteItem(Long.parseLong(itemId),request,response);
		}else {
			cartService.deleteItem(Long.parseLong(itemId));
		}
		return "redirect:/customer/cart/show";
	}
	
	/**
	 * 显示购物车列表
	 * @param map
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value="show",method=RequestMethod.GET)
	public String showCartList(Map<String, Object> map,HttpServletRequest request){
		List<TbCart> cartList = null;
		String token = CookieUtils.getCookieValue(request, TAOTAO_TOKEN);
		if(StringUtils.isEmpty(token)){
			cartList = cartCookieService.queryCartList(request);
		}else {
			cartList = cartService.queryCartList();
		}
		map.put("cartList", cartList);
		return "customer/cart";
	}
	
	@RequestMapping(value="/update/num/{itemId}/{num}",method=RequestMethod.POST)
	public ResponseEntity<Void> updateItem(Map<String, Object> map,@PathVariable("itemId")Long itemId,
			@PathVariable("num")Integer num,HttpServletRequest request,HttpServletResponse response){
		TbUser tbUser = UserThreadLocal.get();
		if(tbUser==null){
			cartCookieService.updateItem(itemId,num,request,response);
		}else {
			cartService.updateItem(itemId,num);
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
