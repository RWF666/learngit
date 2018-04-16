package com.weige.user.service;

import com.weige.user.domain.Comment;


/**
 * comment服务接口
 * @author RWF
 *
 */
public interface CommentService {
	/**
	 * 根据id获取 Comment
	 * @param id
	 * @return
	 */
	Comment getCommentById(Long id);
	/**
	 * 删除评论
	 * @param id
	 * @return
	 */
	void removeComment(Long id);
}
