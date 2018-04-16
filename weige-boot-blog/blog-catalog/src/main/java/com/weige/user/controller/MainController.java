package com.weige.user.controller;


import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.weige.user.domain.Authority;
import com.weige.user.domain.User;
import com.weige.user.repository.AuthorityRepository;
import com.weige.user.service.AuthorityService;
import com.weige.user.service.UserService;
import com.weige.user.vo.Response;

/**
 * 主页控制器
 * @author RWF
 *
 */
@Controller
public class MainController {
	private static final Long ROLE_USER_AUTHORITY_ID = 2L;
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthorityService authorityService;

	@GetMapping("/")
	public String root() {
		return "redirect:/index";
	}
	@GetMapping("/index")
	public String index() {
		return "redirect:/blogs";
	}
	@GetMapping("/login")
	public String login() {
		return "/login";
	}
	
	
	@GetMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		model.addAttribute("errorMsg", "用户名或者密码错误");
		return "/login";
	}
	
	@PostMapping("/register")
	public String register(User user) {
		List<Authority> authorities = new ArrayList<>();
		authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID));
		user.setAuthorities(authorities);
		try {
			userService.saveOrUpdateUser(user);
		}  catch (ConstraintViolationException e)  {
			return "register";
		}
		return "redirect:/login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	@RequestMapping("/list")
	public String list() {
		return "users/list";
	}
	
	@GetMapping("/search")
	public String search() {
		return "search";
	}
}
