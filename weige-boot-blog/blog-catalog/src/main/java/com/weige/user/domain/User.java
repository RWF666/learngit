package com.weige.user.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

/**
 * 用户实体
 * @author RWF
 *
 */
@Data
@Entity
public class User implements UserDetails,Serializable{
	
	@Id //主键
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id; //id，唯一标识
	
	@NotEmpty(message="姓名不能为空")
	@Size(min=2,max=20)
	@Column(nullable = false,length=20)//映射为字段，值不能为空 
	private String name; //姓名
	
	@NotEmpty(message="邮箱不能为空")
	@Size(max=50)
	@Email(message="邮箱格式不对")
	@Column(nullable = false,length=50,unique=true)
	private String email; //邮箱
	
	@NotEmpty(message="用户账号不能为空")
	@Size(min=2,max=20)
	@Column(nullable = false,length=20,unique=true)
	private String username; //用户账号
	
	@NotEmpty(message="密码不能为空")
	@Size(max=100)
	@Column(length=20)
	private String password; //密码
	
	@Column(length=200)
	private String avatar; //头像
	
	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	private List<Authority> authorities;
	
	protected User() { //protected 防止直接使用
	}
	
	
	public User(Long id, String name,String username, String email) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.username =username;
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//  需将 List<Authority> 转成 List<SimpleGrantedAuthority>，否则前端拿不到角色列表名称
		List<SimpleGrantedAuthority> simpleAuthorities = new ArrayList<>();
		for(GrantedAuthority authority : this.authorities){
			simpleAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
		}
		return simpleAuthorities;
	}
	
	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}
	
	public void setEncodePassword(String password) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodePasswd = encoder.encode(password);
		this.password = encodePasswd;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", username=" + username + ", password="
				+ password + ", avatar=" + avatar + ", authorities=" + authorities.get(0).getAuthority() + "]";
	}
}
