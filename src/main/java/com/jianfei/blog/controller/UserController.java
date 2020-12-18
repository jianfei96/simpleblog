package com.jianfei.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.jianfei.blog.domain.User;
import com.jianfei.blog.service.UserService;

@Controller
@SessionAttributes(value = "user")	
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping(path = "/login")
	public String toLogin(Model model,HttpSession session) {
		User user = (User)session.getAttribute("user");
		if (user==null) {
			model.addAttribute(new User());
			return "login";
		}else if (user.getUname() == null) {
			model.addAttribute(new User());
			return "login";
		} 
		else {
			model.addAttribute("message", "Already Logged！");
			model.addAttribute("url", "/manager");
			model.addAttribute("urlName", "Manager Page");
			return "result";
		}
		
	}
	@RequestMapping(value = "/login.action")
	public String login(String uname,String passwd,Model model) {
    	if(uname==""||passwd=="") {
			model.addAttribute("message", "Null UserName and Passsword is not allow！");
			model.addAttribute("url", "/login");
			model.addAttribute("urlName", "Re-Login");
			return "result";
		}
    	try {
    		User user=userService.findByUname(uname);
    		if (user==null) {
				model.addAttribute("message", "User is not exist！");
				model.addAttribute("url", "/login");
				model.addAttribute("urlName", "Re-Login");
				return "result";
			}
    		else if (user.getPasswd().equals(passwd)) {
    			model.addAttribute("user", user); 
    			model.addAttribute("message", "Login Success！");
    			model.addAttribute("url", "/manager");
    			model.addAttribute("urlName", "Manager Page");
				return "result";
			}else {
				model.addAttribute("message", "Password wrong！");
				model.addAttribute("url", "/login");
				model.addAttribute("urlName", "Re-Login");
				return "result";
			}

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "Controller");
			model.addAttribute("url", "/login");
			model.addAttribute("urlName", "Re-login");
			return "result";
		}
    	
	}
	
	@RequestMapping(value = "/manager")
	public String manager(HttpSession session,Model model) {
		User user = (User)session.getAttribute("user");
		if (user==null) {
			model.addAttribute("message", "Please login first！");
			model.addAttribute("url", "/login");
			model.addAttribute("urlName", "Login");
			return "result";
		}else if (user.getUname() == null) {
			model.addAttribute("message", "Please login first！");
			model.addAttribute("url", "/login");
			model.addAttribute("urlName", "Login");
			return "result";
		} 
		return "manager";
	}
	
	@RequestMapping(value = "/updateUser")
	public String update(Model model,HttpSession session) {
		if (session.getAttribute("user") == null) {
			model.addAttribute("url", "/login");
			model.addAttribute("urlName", "Login");
			model.addAttribute("message", "Session expired, please re-login!");
			return "result";
		}
		return "updateUser";
	}
	
	@RequestMapping(value = "/updateUser.action")
	public String save(User user,Model model,HttpSession session) {
		try {
			userService.saveUser(user);
			return "manager";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("url", "/manager");
			model.addAttribute("urlName", "Manager");
			model.addAttribute("message", "Failure");
			return "result";
		}
	}
	
	@RequestMapping(value = "/logout")
	public String logout(Model model,HttpSession session,SessionStatus sessionStatus) {
		session.removeAttribute("user");
		sessionStatus.setComplete();
		model.addAttribute("url", "/login");
		model.addAttribute("urlName", "Re-login");
		model.addAttribute("message", "Logout Success！");
		return "result";
	}

}
