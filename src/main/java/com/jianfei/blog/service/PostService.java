package com.jianfei.blog.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.jianfei.blog.domain.Post;
import com.jianfei.blog.repository.PostRepository;

@Service
public class PostService {
	
	@Autowired
	private PostRepository postRepository;
	
	public Iterable<Post> findAll(){
		return postRepository.findAll();
	}
	
	public Post findById(Integer id) {
		if (postRepository.existsById(id)) {
			return postRepository.findById(id).get();
		} else {
			return null;
		}
		
	}
	
	public boolean savePost(Post post) {
		try {
			postRepository.save(post);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean deletePost(int pid) {
		if (postRepository.existsById(pid)) {
			postRepository.deleteById(pid);
			return true;
		} else {
			return false;
		}
	}
	
	
	public boolean deletePosts(Iterable<Post> posts) {
		try {
			postRepository.deleteAll(posts);;
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Iterable<Post> findAllByIds(List<Integer> ids){
		try {
			return postRepository.findAllById(ids);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	
	public Iterable<Post> findAllSorted(Sort sort){
		try {
			
			return postRepository.findAll(sort);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
	}
	
	private static final Sort SORT = Sort.by(Direction.DESC, "date").and(Sort.by(Direction.DESC, "time"));
	
	public Iterable<Post> findAllOrderByDateAndTime(){
//		Order order1 = new Order(Direction.DESC, "date");
//		Order order2 = new Order(Direction.DESC, "time");
//		List<Order> orders = new ArrayList<Order>();
//		orders.add(order1);
//		orders.add(order2);
//		Sort sort=new Sort(orders);
		try {
			return postRepository.findAll(SORT);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
	}
	
	public Page<Post> findAllPaged(Pageable pageable) {
		try {
			return postRepository.findAll(pageable);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Page<Post> findAllByIdsPaged(Collection<Integer> ids,Pageable pageable){
		return postRepository.findByPidIn(ids, pageable);
	}

}
