package com.jianfei.blog.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import com.jianfei.blog.domain.Comment;


public interface CommentRepository extends CrudRepository<Comment, Integer>{
	
	List<Comment> findByPid(Integer pid);
	@Transactional
	@Modifying
	void deleteByPid(Integer pid);
	
	@Transactional
	@Modifying
	void deleteByPidAndCid(Integer pid,Long cid);
}
