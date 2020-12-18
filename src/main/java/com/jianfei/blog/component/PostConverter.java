package com.jianfei.blog.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.jianfei.blog.domain.Post;
import com.jianfei.blog.service.PostService;

@Component
public class PostConverter implements Converter<Integer, Post>{
	
	@Autowired
	private PostService postService;

	@Override
	public Post convert(Integer pid) {
		return postService.findById(pid);
	}

}
