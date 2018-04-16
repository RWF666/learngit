package com.weige.user.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weige.user.domain.Vote;
import com.weige.user.repository.VoteRepository;


/**
 * vote服务
 * @author RWF
 *
 */
@Service
public class VoteServiceImpl implements VoteService {

	@Autowired
	private VoteRepository voteRepository;
	
	@Override
	@Transactional
	public void removeVote(Long id) {
		voteRepository.delete(id);
	}
	@Override
	public Vote getVoteById(Long id) {
		return voteRepository.findOne(id);
	}

}
