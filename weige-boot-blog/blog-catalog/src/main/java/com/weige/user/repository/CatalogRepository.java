package com.weige.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weige.user.domain.Catalog;
import com.weige.user.domain.User;



/**
 * Catalog 仓库.
 * @author RWF
 *
 */
public interface CatalogRepository extends JpaRepository<Catalog, Long>{
	
	/**
	 * 根据用户查询
	 * @param user
	 * @return
	 */
	List<Catalog> findByUser(User user);
	
	/**
	 * 根据用户查询
	 * @param user
	 * @param name
	 * @return
	 */
	List<Catalog> findByUserAndName(User user,String name);
}
