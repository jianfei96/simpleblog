package com.jianfei.blog.repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.jianfei.blog.domain.Post;

public interface PostRepository extends PagingAndSortingRepository<Post, Integer>{
	
	Page<Post> findByPidIn(Collection<Integer> ids,Pageable pageable);
}
