package com.weige.user.service;

import com.weige.user.domain.Vote;

/**
 * VOTE服务接口
 * @author RWF
 *
 */
public interface VoteService {
	/**
	 * 根据id获取 Vote
	 * @param id
	 * @return
	 */
	Vote getVoteById(Long id);
	/**
	 * 删除Vote
	 * @param id
	 * @return
	 */
	void removeVote(Long id);
}
