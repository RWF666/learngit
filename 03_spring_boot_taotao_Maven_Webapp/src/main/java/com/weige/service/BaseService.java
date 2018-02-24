package com.weige.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weige.mapper.TbUserMapper;
import com.weige.model.TbUser;
import com.weige.model.TbUserExample;
import com.weige.model.TbUserExample.Criteria;

@Service
public class BaseService{
	protected static final ObjectMapper MAPPER = new ObjectMapper();
	
	protected static final String TAOTAO_MANAGE_ITEM_DETAIL_= "TAOTAO_MANAGE_ITEM_DETAIL_";
	
	protected static final String TAOTAO_TOKEN = "TAOTAO_TOKEN";
	
	protected static final String COOKIE_CART = "TAOTAO_CART";
}
