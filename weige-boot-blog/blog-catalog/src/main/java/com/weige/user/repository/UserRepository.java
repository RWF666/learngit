package com.weige.user.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.weige.user.domain.User;

import java.lang.String;
import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
	/**
	 * 根据用户姓名分页查询用户列表
	 * @param username
	 * @param pageable
	 * @return
	 */
	Page<User> findByNameLike(String username,Pageable pageable);
	
	
	/**
	 * 根据用户名查询用户 
	 * @param username
	 * @return
	 */
	User findByUsername(String username);
	
	/**
	 * 根据名称列表查询
	 * @param usernames
	 * @return
	 */
	List<User> findByUsernameIn(Collection<String> usernames);
}
