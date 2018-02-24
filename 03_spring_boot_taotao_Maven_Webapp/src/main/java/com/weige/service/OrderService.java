package com.weige.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.weige.mapper.TbOrderItemMapper;
import com.weige.mapper.TbOrderMapper;
import com.weige.mapper.TbOrderShippingMapper;
import com.weige.model.TbOrder;
import com.weige.model.TbOrderExample;
import com.weige.model.TbOrderExample.Criteria;
import com.weige.model.TbOrderItem;
import com.weige.model.TbOrderShipping;
import com.weige.pojo.PageResult;
import com.weige.pojo.ResultMsg;
import com.weige.pojo.TaotaoResult;
import com.weige.utils.ValidateUtil;


@Service
public class OrderService extends BaseService{

    @Autowired
    private TbOrderMapper tbOrderMapper;
    
    @Autowired
    private OrderItemService orderItemService;
    
    @Autowired
    private OrderShippingService orderShippingService;
    
    
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
    
    @Transactional
    public TaotaoResult createOrder(TbOrder tbOrder) {
        TbOrder order = null;
        try {
        	order = tbOrder;
            // 校验Order对象
            ValidateUtil.validate(order);
        } catch (Exception e) {
            return TaotaoResult.build(400, "请求参数有误!");
        }

        try {
            // 生成订单ID，规则为：userid+当前时间戳
            String orderId = order.getUserId() + "" + System.currentTimeMillis();
            order.setOrderId(orderId);

            // 设置订单的初始状态为未付款
            order.setStatus(1);

            // 设置订单的创建时间
            order.setCreateTime(new Date());
            order.setUpdateTime(order.getCreateTime());

            // 设置买家评价状态，初始为未评价
            order.setBuyerRate(0);

            // 持久化order对象
            tbOrderMapper.insert(order);
            
            List<TbOrderItem> orderItems = order.getOrderItems();
            for(TbOrderItem tbOrderItem:orderItems){
            	tbOrderItem.setOrderId(order.getOrderId());
            	orderItemService.insert(tbOrderItem);
            }
            
            TbOrderShipping orderShipping = order.getOrderShipping();
            orderShipping.setOrderId(order.getOrderId());
            orderShippingService.insert(orderShipping);
            
            //发送消息到RabbitMQ
//            Map<String, Object> msg = new HashMap<String, Object>(3);
//            msg.put("userId", order.getUserId());
//            msg.put("orderId", order.getOrderId());
//            List<Long> itemIds = new ArrayList<Long>(order.getOrderItems().size());
//            for (OrderItem orderItem : order.getOrderItems()) {
//                itemIds.add(orderItem.getItemId());
//            }
//            msg.put("itemIds", itemIds);
//            this.rabbitTemplate.convertAndSend(objectMapper.writeValueAsString(msg));
            return TaotaoResult.ok(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TaotaoResult.build(400, "保存订单失败!");
    }

    public TbOrder queryOrderById(String orderId) {
    	TbOrderExample example = new TbOrderExample();
    	Criteria criteria = example.createCriteria();
    	criteria.andOrderIdEqualTo(orderId);
    	List<TbOrder> list = tbOrderMapper.selectByExample(example);
    	TbOrder tbOrder = null;
    	if(list.size()>0){
    		tbOrder = list.get(0);
    		List<TbOrderItem> orderItems =  orderItemService.findByOrderId(tbOrder.getOrderId());
    		TbOrderShipping tbOrderShipping =  orderShippingService.findByOrderId(tbOrder.getOrderId());
    		tbOrder.setOrderItems(orderItems);
    		tbOrder.setOrderShipping(tbOrderShipping);
    	}
    	
    	
        return tbOrder;
    }

    public PageResult<TbOrder> queryOrderByUserNameAndPage(String buyerNick, int page, int count) {
    	TbOrderExample example = new TbOrderExample();
    	example.setOrderByClause("create_time asc");
    	Criteria criteria = example.createCriteria();
    	criteria.andBuyerNickEqualTo(buyerNick);
    	PageHelper.startPage(page,count);
    	List<TbOrder> list = tbOrderMapper.selectByExample(example);
    	
    	List<TbOrder> listAll = new ArrayList<TbOrder>();
    	
    	for(TbOrder tbOrder:list){
    		List<TbOrderItem> orderItems =  orderItemService.findByOrderId(tbOrder.getOrderId());
    		TbOrderShipping tbOrderShipping =  orderShippingService.findByOrderId(tbOrder.getOrderId());
    		tbOrder.setOrderItems(orderItems);
    		tbOrder.setOrderShipping(tbOrderShipping);
    		listAll.add(tbOrder);
    	}
    	return new PageResult<TbOrder>(listAll.size(), listAll);
    }

    public ResultMsg changeOrderStatus(TbOrder order) {
    	order.setPaymentTime(new Date());
        TbOrderExample example = new TbOrderExample();
    	Criteria criteria = example.createCriteria();
    	criteria.andOrderIdEqualTo(order.getOrderId());
    	
    	int result = tbOrderMapper.updateByExampleSelective(order, example);
        
    	if(result!=0){ 
    		 return new ResultMsg("200", "成功");
    	}
       
    	return new ResultMsg("200", "失败");
    }

	public PageResult<TbOrder> findOrdersById(int page,Long userId) {
		// TODO Auto-generated method stub
		TbOrderExample example = new TbOrderExample();
    	example.setOrderByClause("create_time asc");
    	Criteria criteria = example.createCriteria();
    	criteria.andUserIdEqualTo(userId);
    	int total = tbOrderMapper.selectByExample(example).size();
    	PageHelper.startPage(page,5);
    	List<TbOrder> list = tbOrderMapper.selectByExample(example);
    	
    	List<TbOrder> listAll = new ArrayList<TbOrder>();
    	
    	for(TbOrder tbOrder:list){
    		List<TbOrderItem> orderItems =  orderItemService.findByOrderId(tbOrder.getOrderId());
    		TbOrderShipping tbOrderShipping =  orderShippingService.findByOrderId(tbOrder.getOrderId());
    		tbOrder.setOrderItems(orderItems);
    		tbOrder.setOrderShipping(tbOrderShipping);
    		listAll.add(tbOrder);
    	}
    	return new PageResult<TbOrder>(total, listAll);
	}

}
