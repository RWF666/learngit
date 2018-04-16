package com.weige.user.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.weige.user.domain.User;


/**
 * 用户服务接口
 * @author RWF
 *
 */
public interface UserService {
	/**
	 * 新增编辑保存用户
	 * @param user
	 * @return
	 */
	User saveOrUpdateUser(User user);
	
	/**
	 * 注册用户
	 * @param user
	 * @return
	 */
	User registerUser(User user);
	
	/**
	 * 根据id删除用户
	 * @param id
	 */
	void removeUser(Long id);
	
	/**
	 * 根据id查询用户
	 * @param id
	 * @return
	 */
	User getUserById(Long id);
	
	/**
	 * 根据用户名模糊查询所有用户
	 * @param username
	 * @return
	 */
	Page<User> listUsersByNameLike(String username,Pageable pageable);
	
	/**
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	User findByUserName(String username);
	
	/**
	 * 根据名称列表查询
	 * @param usernames
	 * @return
	 */
	List<User> listUsersByUsernames(Collection<String> usernames);
}
