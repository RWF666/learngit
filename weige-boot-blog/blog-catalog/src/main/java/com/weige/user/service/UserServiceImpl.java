package com.weige.user.service;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.weige.user.domain.User;
import com.weige.user.repository.UserRepository;

/**
 * 用户服务接口实现
 * @author RWF
 *
 */
@Service
public class UserServiceImpl implements UserService,UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	@Override
	public User saveOrUpdateUser(User user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}
	
	@Transactional
	@Override
	public User registerUser(User user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}
	@Transactional
	@Override
	public void removeUser(Long id) {
		userRepository.delete(id);
	}
	
	@Override
	public User getUserById(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findOne(id);
	}

	@Override
	public Page<User> listUsersByNameLike(String username, Pageable pageable) {
		// TODO Auto-generated method stub
		username = "%" + username +"%";
		return userRepository.findByNameLike(username, pageable);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username);
	}

	@Override
	public User findByUserName(String username) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}
	

	@Override
	public List<User> listUsersByUsernames(Collection<String> usernames) {
		return userRepository.findByUsernameIn(usernames);
	}

}
