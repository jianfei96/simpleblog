package com.jianfei.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jianfei.blog.domain.Comment;
import com.jianfei.blog.repository.CommentRepository;

@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepository;
	
	public List<Comment> findByPid(Integer pid){
		return commentRepository.findByPid(pid);
	}
	
	public boolean saveComment(Comment comment) {
		try {
			commentRepository.save(comment);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean deleteByPid(Integer pid) {
		try {
			commentRepository.deleteByPid(pid);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteByPidAndCid(Integer pid,Long cid) {
		try {
			commentRepository.deleteByPidAndCid(pid, cid);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
