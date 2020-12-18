package com.jianfei.blog.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jianfei.blog.domain.User;
import com.jianfei.blog.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public boolean saveUser(User user) {
		try {
			userRepository.save(user);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public User findByUname(String uname) {
		
		java.util.List<User> userList=userRepository.findByUname(uname);
		if (userList.isEmpty()) {
			return null;
		}else{
			return userList.get(0);
		}
	}
	
	public User findByUnameAndPassword(String uname,String password) {
		
		java.util.List<User> userList = userRepository.findByUnameAndPasswd(uname, password);
		if (userList.isEmpty()) {
			return null;
		} else {
			return userList.get(0);
		}
	}

}
