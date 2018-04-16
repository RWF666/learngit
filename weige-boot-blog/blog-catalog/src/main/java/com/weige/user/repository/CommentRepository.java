package com.weige.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weige.user.domain.Comment;



/**
 * comment仓库
 * @author RWF
 *
 */
public interface CommentRepository extends JpaRepository<Comment, Long>{
 
}
