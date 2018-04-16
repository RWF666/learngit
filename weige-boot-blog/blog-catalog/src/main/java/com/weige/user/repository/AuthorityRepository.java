package com.weige.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weige.user.domain.Authority;

/**
 * Authority 仓库.
 * @author RWF
 *
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long>{
}
