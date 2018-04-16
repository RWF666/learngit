package com.weige.user.controller;


import java.sql.Timestamp;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.weige.user.domain.Blog;
import com.weige.user.domain.Catalog;
import com.weige.user.domain.Comment;
import com.weige.user.domain.EsBlog;
import com.weige.user.domain.User;
import com.weige.user.domain.Vote;
import com.weige.user.service.BlogService;
import com.weige.user.service.CatalogService;
import com.weige.user.service.EsBlogService;
import com.weige.user.service.UserService;
import com.weige.user.utils.ConstraintViolationExceptionHandler;
import com.weige.user.vo.Response;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

/**
 * 用户主页空间控制器.
 * @author RWF
 *
 */
@Controller
@RequestMapping("/u")
@PropertySource(value="classpath:application.yml")
public class UserspaceController {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private CatalogService catalogService;
	
	@Autowired
	private EsBlogService esBlogService;
	
	@Value("${file.server.url}")
	private String fileServerUrl;
	
	/**
	 * 用户主页
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}")
	public String userSpace(@PathVariable("username") String username, Model model) {
		User  user = (User)userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		return "redirect:/u/" + username + "/blogs";
	}
	
	
	/**
	 * 获取个人设置界面
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ModelAndView profile(@PathVariable("username") String username, Model model) {
		User  user = (User)userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		model.addAttribute("fileServerUrl", fileServerUrl);
		return new ModelAndView("/userspace/profile", "userModel", model);
	}
 
	/**
	 * 保存个人设置
	 * @param user
	 * @param result
	 * @param redirect
	 * @return
	 */
	@PostMapping("/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)") 
	public String saveProfile(@PathVariable("username") String username,User user) {
		User originalUser = userService.getUserById(user.getId());
		originalUser.setEmail(user.getEmail());
		originalUser.setName(user.getName());
		
		// 判断密码是否做了变更
		String rawPassword = originalUser.getPassword();
		PasswordEncoder  encoder = new BCryptPasswordEncoder();
		String encodePasswd = encoder.encode(user.getPassword());
		boolean isMatch = encoder.matches(rawPassword, encodePasswd);
		if (!isMatch) {
			originalUser.setEncodePassword(user.getPassword());
		}
		
		userService.saveOrUpdateUser(originalUser);
		return "redirect:/u/" + username + "/profile";
	}
	
	/**
	 * 获取编辑头像的界面
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ModelAndView avatar(@PathVariable("username") String username, Model model) {
		User  user = (User)userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		return new ModelAndView("/userspace/avatar", "userModel", model);
	}
	
	
	/**
	 * 保存头像
	 * @param username
	 * @param model
	 * @return
	 */
	@PostMapping("/{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username, @RequestBody User user) {
		String avatarUrl = user.getAvatar();
		
		User originalUser = userService.getUserById(user.getId());
		originalUser.setAvatar(avatarUrl);
		userService.saveOrUpdateUser(originalUser);
		
		return ResponseEntity.ok().body(new Response(true, "处理成功", avatarUrl));
	}
	
	/**
	 * 获取用户的博客列表
	 * @param username
	 * @param order
	 * @param category
	 * @param keyword
	 * @param async
	 * @param pageIndex
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs")
	public String listBlogsByOrder(@PathVariable("username") String username,
			@RequestParam(value="order",required=false,defaultValue="new") String order,
			@RequestParam(value="category",required=false ) Long category,
			@RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
			@RequestParam(value="async",required=false) boolean async,
			@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
			Model model) {
		User  user = (User)userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		
		/*if (category != null) {
			System.out.print("category:" +category );
			System.out.print("selflink:" + "redirect:/u/"+ username +"/blogs?category="+category);
			return "/userspace/u";
		}*/
		Page<Blog> page = null;
		if (category != null && category > 0) { // 分类查询
			Catalog catalog = catalogService.getCatalogById(category);
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			page = blogService.listBlogsByCatalog(catalog, pageable);
			order = "";
		}else if (order.equals("hot")) { // 最热查询
			Sort sort = new Sort(Direction.DESC,"reading","comments","likes"); 
			Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
			page = blogService.listBlogsByTitleLikeAndSort(user, keyword, pageable);
		}
		if (order.equals("new")) { // 最新查询
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			page = blogService.listBlogsByTitleLike(user, keyword, pageable);
		}
 
		
		List<Blog> list = page.getContent();	// 当前所在页面数据列表
		
		model.addAttribute("order", order);
		model.addAttribute("page", page);
		model.addAttribute("blogList", list);
		return (async==true?"/userspace/u :: #mainContainerRepleace":"/userspace/u");
	}
	
	/**
	 * 获取博客展示界面
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs/{id}")
	public String getBlogById(@PathVariable("username") String username,@PathVariable("id") Long id, Model model) {
		// 每次读取，简单的可以认为阅读量增加1次
		Blog blog = blogService.getBlogById(id);
		blogService.readingIncrease(id);
 		
		boolean isBlogOwner = false;
		String commentOwner = null;
		User principal = null;
		// 判断操作用户是否是博客的所有者
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if (principal !=null && username.equals(principal.getUsername())) {
				isBlogOwner = true;
			} 
		}
		commentOwner = principal.getUsername();
		List<Comment> commentContents = blogService.getBlogById(id).getCommentContents();
		
		// 判断操作用户的点赞情况
		List<Vote> votes = blog.getVotes();
		Vote currentVote = null; // 当前用户的点赞情况
		
		if (principal !=null) {
			for (Vote vote : votes) {
				vote.getUser().getUsername().equals(principal.getUsername());
				currentVote = vote;
				break;
			}
		}
				
		model.addAttribute("commentOwner",commentOwner);
		model.addAttribute("isBlogOwner", isBlogOwner);
		model.addAttribute("blogModel",blogService.getBlogById(id));
		model.addAttribute("commentContents", blogService.getBlogById(id).getCommentContents());
		model.addAttribute("currentVote",currentVote);
		
		return "/userspace/blog";
	}
	
	/**
	 * 删除博客
	 * @param id
	 * @param model
	 * @return
	 */
	@DeleteMapping("/{username}/blogs/{id}")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username,@PathVariable("id") Long id) {
		
		try {
			blogService.removeBlog(id);
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		String redirectUrl = "/u/" + username + "/blogs";
		return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
	}
	
	/**
	 * 获取新增博客的界面
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs/edit")
	public ModelAndView createBlog(@PathVariable("username") String username,Model model) {
		User user = (User)userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.listCatalogs(user);
		model.addAttribute("catalogs", catalogs);
		model.addAttribute("blog", new Blog(null, null, null));
		return new ModelAndView("/userspace/blogedit", "blogModel", model);
	}
	
	/**
	 * 获取编辑博客的界面
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs/edit/{id}")
	public ModelAndView editBlog(@PathVariable("username") String username,@PathVariable("id") Long id, Model model) {
		User user = (User)userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.listCatalogs(user);
		model.addAttribute("catalogs", catalogs);
		model.addAttribute("blog", blogService.getBlogById(id));
		return new ModelAndView("/userspace/blogedit", "blogModel", model);
	}
	
	/**
	 * 更新或者保存博客
	 * @param username
	 * @param blog
	 * @return
	 */
	@PostMapping("/{username}/blogs/edit")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ResponseEntity<Response> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog) {
		User user = (User)userDetailsService.loadUserByUsername(username);
		blog.setUser(user);
		// 对 Catalog 进行空处理
		if (blog.getCatalog().getId() == null) {
			return ResponseEntity.ok().body(new Response(false,"未选择分类"));
		}
		if(blog.getId()!=null) {
			Blog originBlog = blogService.getBlogById(blog.getId());
			originBlog.setContent(blog.getContent());
			originBlog.setTitle(blog.getTitle());
			originBlog.setSummary(blog.getSummary());
			originBlog.setCreateTime(new Timestamp(System.currentTimeMillis()));
			originBlog.setCatalog(blog.getCatalog());
			originBlog.setTags(blog.getTags());
			
			blogService.saveBlog(originBlog);
			
		}
		else {
			try {
				blogService.saveBlog(blog);
			} catch (ConstraintViolationException e)  {
				return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
			} catch (Exception e) {
				return ResponseEntity.ok().body(new Response(false, e.getMessage()));
			}
		}
		
		String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
		return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
	}
}
