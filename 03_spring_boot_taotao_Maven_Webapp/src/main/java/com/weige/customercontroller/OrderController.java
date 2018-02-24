package com.weige.customercontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.weige.mapper.TbUserMapper;
import com.weige.model.TbCart;
import com.weige.model.TbItem;
import com.weige.model.TbOrder;
import com.weige.model.TbOrderItem;
import com.weige.model.TbUser;
import com.weige.pojo.PageResult;
import com.weige.pojo.ResultMsg;
import com.weige.pojo.TaotaoResult;
import com.weige.service.CartService;
import com.weige.service.ItemService;
import com.weige.service.OrderItemService;
import com.weige.service.OrderService;
import com.weige.service.UserService;
import com.weige.threadlocal.UserThreadLocal;


@RequestMapping("/customer/order")
@Controller
public class OrderController {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired 
	private CartService cartService;
	
	
	@RequestMapping(value = "toOrder/{itemId}")
	public String toOrder(Map<String, Object> map,@PathVariable("itemId")Long itemId) {
		TbItem tbItem = itemService.findByItemId(itemId);
		map.put("item", tbItem);
		return "customer/order";
	}
	
	@RequestMapping(value = "myOrders")
	public String myOrders(Map<String, Object> map,@RequestParam(value="page",defaultValue="1")int page) {
		Long userId = UserThreadLocal.get().getId();
		PageResult<TbOrder> pageInfo = orderService.findOrdersById(page,userId);
		map.put("orderList", pageInfo.getData());
		map.put("page", page);
		map.put("totalPage", pageInfo.getTotle()/5+1);
		return "customer/my-orders"; 
	}
	
	@RequestMapping(value = "toOrderCart")
	public String toCartOrder(Map<String, Object> map,HttpServletRequest request) {
		List<TbCart> carts = cartService.queryCartList();
		if(carts.isEmpty()){
			//提示购物车为空
		}
		map.put("carts", carts);
		return "customer/order-cart";
	}
	
	/**
	 * 创建订单
	 * @param json
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/create" , method = RequestMethod.POST)
	public TaotaoResult createOrder(@RequestBody TbOrder order) {
		return orderService.createOrder(order);
	}
	
	
	/**
	 * 根据订单ID查询订单
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/query/{orderId}" ,method = RequestMethod.GET)
	public TbOrder queryOrderById(@PathVariable("orderId") String orderId) {
		return orderService.queryOrderById(orderId);
	}

	/**
	 * 根据用户名分页查询订单
	 * @param buyerNick
	 * @param page
	 * @param count
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query/{buyerNick}/{page}/{count}")
	public PageResult<TbOrder> queryOrderByUserNameAndPage(@PathVariable("buyerNick") String buyerNick,@PathVariable("page") Integer page,@PathVariable("count") Integer count) {
		return orderService.queryOrderByUserNameAndPage(buyerNick, page, count);
	}

	
	/**
	 * 修改订单状态
	 * @param json
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/changeOrderStatus",method = RequestMethod.POST)
	public ResultMsg changeOrderStatus(@RequestBody TbOrder order) {
		return orderService.changeOrderStatus(order);
	}
	
	@ResponseBody
	@RequestMapping(value="/submit",method = RequestMethod.POST)
	public Map<String, Object> submitOrder(TbOrder order,@CookieValue("TAOTAO_TOKEN")String token) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			TbUser tbUser = UserThreadLocal.get();
			order.setUserId(tbUser.getId());
			order.setBuyerNick(tbUser.getUsername());
			TaotaoResult taotaoResult = orderService.createOrder(order);
			
			if(taotaoResult.getStatus()==200){
				//创建成功
				cartService.deleteCart(tbUser.getId(),order);
				result.put("status", "200");
				result.put("data", taotaoResult.getData());
			}else {
				result.put("status", "400");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "/success")
	public String success(Map<String, Object> map,String id) {
		TbOrder order = orderService.queryOrderById(id);
		map.put("order",order);
		return "customer/success";
	}
}
