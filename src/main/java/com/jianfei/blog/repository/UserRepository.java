package com.jianfei.blog.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.jianfei.blog.domain.User;

public interface UserRepository extends CrudRepository<User, Integer>{

	List<User> findByUname(String uname);
	List<User> findByUnameAndPasswd(String uname,String password);

}
