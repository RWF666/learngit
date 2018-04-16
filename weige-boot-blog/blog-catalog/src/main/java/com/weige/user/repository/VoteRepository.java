package com.weige.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weige.user.domain.Vote;



/**
 * vote仓库
 * @author RWF
 *
 */
public interface VoteRepository extends JpaRepository<Vote, Long>{
 
}
