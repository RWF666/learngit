package com.weige.user.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weige.user.domain.Comment;
import com.weige.user.repository.CommentRepository;



/**
 * Comment服务
 * @author RWF
 *
 */
@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;
	
	@Override
	@Transactional
	public void removeComment(Long id) {
		commentRepository.delete(id);
	}
	@Override
	public Comment getCommentById(Long id) {
		return commentRepository.findOne(id);
	}

}
