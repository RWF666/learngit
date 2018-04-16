package com.weige.user.service;

import com.weige.user.domain.Authority;

/**
 * Authority 服务接口.
 * @author RWF
 *
 */
public interface AuthorityService {
	 
	
	/**
	 * 根据id获取 Authority
	 * @param Authority
	 * @return
	 */
	Authority getAuthorityById(Long id);
}
