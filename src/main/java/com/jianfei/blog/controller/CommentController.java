package com.jianfei.blog.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.jianfei.blog.component.MD5Util;
import com.jianfei.blog.domain.Comment;
import com.jianfei.blog.service.CommentService;

@SessionAttributes(value = {"commenterName","commenterEmail"})

@Controller
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	@RequestMapping(value = "/submitComment")
	public String submit(Comment comment, Model model) {

		try {
			List<Comment> commentList = commentService.findByPid(comment.getPid());
			comment.setCid((new Date()).getTime());
			comment.setAvatar("https://www.gravatar.com/avatar/"
					+ MD5Util.crypt(comment.getCommenterEmail().trim().toLowerCase()));

			for (Comment c : commentList) {
				if (c.getCid() == comment.getPreCid()) {
					comment.setPreCommenterName(c.getCommenterName());
					comment.setPreContent(c.getContent());
				}
			}

			commentService.saveComment(comment);
			model.addAttribute("url", "/post/" + comment.getPid() + ".html#footer");
			model.addAttribute("commenterName", comment.getCommenterName());
			model.addAttribute("commenterEmail", comment.getCommenterEmail());
			return "redirect";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("url", "/post/" + comment.getPid() + ".html");
			model.addAttribute("urlName", "返回文章");
			model.addAttribute("message", "评论失败！");
			return "result";
		}
	}
	
	@RequestMapping(value = "/deleteComment")
	public String deleteComment(int pid,long cid,Model model) {
		try {
			commentService.deleteByPidAndCid(pid, cid);
			model.addAttribute("url", "/post/"+pid+".html");
			return "redirect";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("url", "/post/"+pid+".html");
			return "redirect";
		}
	}

}
