package com.jianfei.blog.controller;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.jianfei.blog.component.FirstParagraph;
import com.jianfei.blog.component.HTMLtoText;
import com.jianfei.blog.component.MDtoHTML;
import com.jianfei.blog.domain.Comment;
import com.jianfei.blog.domain.Post;
import com.jianfei.blog.domain.Posts;
import com.jianfei.blog.domain.User;
import com.jianfei.blog.service.CommentService;
import com.jianfei.blog.service.PostService;
import com.jianfei.blog.service.StorageService;



@Controller
@SessionAttributes(value = { "activePage", "post","user"})
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private CommentService commentService;

	private final StorageService storageService;

	@Autowired
	public PostController(StorageService storageService) {
		this.storageService = storageService;
	}

	private static final Sort SORT = Sort.by(Direction.DESC, "date").and(Sort.by(Direction.DESC, "time"));
	private static final Integer SIZE = 5;

	@RequestMapping(value = "/postList")
	public String postList(Model model, Integer page,HttpSession session) {
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
		if (page == null) {
			page = 1;
		}
		Pageable pageable = PageRequest.of(page - 1, SIZE, SORT);
		// model.addAttribute("postList", postService.findAllOrderByDateAndTime());
		Page<Post> postList = postService.findAllPaged(pageable);
		if (page > postList.getTotalPages() && page !=1) {
			model.addAttribute("urlName", "Post List");
			model.addAttribute("url", "/postList");
			model.addAttribute("message", "Page number is not exist！");
			return "result";
		}
		Integer length = postList.getTotalPages();
		if (length.equals(0)) {
			length = 1;
		}
		model.addAttribute("activePage", page);
		model.addAttribute("postList", postList);
		model.addAttribute("length", length);
		model.addAttribute("posts", new Posts());
		return "postList";
	}

	@RequestMapping(value = "/deletePost")
	public String deletePost(@ModelAttribute Posts posts, Model model, HttpSession session) {
		List<Post> postListFromPage = posts.getCheakedPosts();
		if (postListFromPage.isEmpty()) {
			model.addAttribute("urlName", "Post List");
			model.addAttribute("url", "/postList");
			model.addAttribute("message", "Please select posts！");
			return "result";
		}

		postListFromPage.forEach(post -> storageService.deleteByName(postService.findById(post.getPid()).getImage()));
		postListFromPage.forEach(post -> commentService.deleteByPid(post.getPid()));
		postService.deletePosts(postListFromPage);
		
		// model.addAttribute("posts",new Posts());
		// model.addAttribute("postList", postService.findAllOrderByDateAndTime());
		Integer activePage = (Integer) session.getAttribute("activePage");
		if (activePage == null) {
			activePage = 1;
		}
//		Pageable pageable = new PageRequest(activePage - 1, SIZE, SORT);
//		Page<Post> postList = postService.findAllPaged(pageable);
//		model.addAttribute("activePage", activePage);
//		model.addAttribute("postList", postList);
//		model.addAttribute("length", postList.getTotalPages());
//		model.addAttribute("posts", new Posts());
//		return "postList";
		if (activePage == 1) {
			model.addAttribute("url", "/postList");
		}else {
			model.addAttribute("url", "/postList?page="+activePage);
		}
		return "redirect";
	}

	@RequestMapping(value = "/addPost")
	public String addPost(Model model,HttpSession session) {
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
		Post post = new Post();
		model.addAttribute("remove", "all");
		model.addAttribute("post", post);
		model.addAttribute("pageTitle", "Add Psot");
		model.addAttribute("url", "/addPost.action");
		model.addAttribute("button", "Add");
		return "savePost";
	}

	@RequestMapping(value = "/addPost.action")
	public String addPostAction(Post post, Model model, @RequestParam("file") MultipartFile file) {
		if (post.getTitle() == "") {
			model.addAttribute("urlName", "Post List");
			model.addAttribute("url", "/postList");
			model.addAttribute("message", "Null title is not allow！");
			return "result";
		}

		long date = new java.util.Date().getTime();

		if (!file.isEmpty()) {
			String filename = StringUtils.cleanPath(file.getOriginalFilename());
			String suffix = filename.substring(filename.lastIndexOf(".") + 1);
			storageService.storeAndSetName(file, String.valueOf(date) + "." + suffix);
			post.setImage(String.valueOf(date) + "." + suffix);
		}

		post.setDate(new Date(date));
		post.setTime(new Time(date));

		boolean result1 = postService.savePost(post);

		if (result1) {
//			Pageable pageable = new PageRequest(0, SIZE, SORT);
//			Page<Post> postList = postService.findAllPaged(pageable);
//			model.addAttribute("activePage", 1);
//			model.addAttribute("postList", postList);
//			model.addAttribute("length", postList.getTotalPages());
//			model.addAttribute("posts", new Posts());
//			return "postList";
			model.addAttribute("url", "/postList");
			return "redirect";
		} else {
			model.addAttribute("urlName", "Post List");
			model.addAttribute("url", "/postList");
			model.addAttribute("message", "Addition Failure!");
			return "result";
		}

	}

	@RequestMapping(value = "/updatePost")
	public String updatePost(@ModelAttribute Posts posts, Model model,HttpSession session) {
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
		List<Post> postList = posts.getCheakedPosts();
		if (postList.size() > 1) {
			model.addAttribute("urlName", "Post List");
			model.addAttribute("url", "/postList");
			model.addAttribute("message", "Your can edit only one post at a time！");
			return "result";
		} else if (postList.isEmpty()) {
			model.addAttribute("urlName", "Post List");
			model.addAttribute("url", "/postList");
			model.addAttribute("message", "Nothing been selected！");
			return "result";
		}
		Post post = postService.findById(postList.get(0).getPid());
		model.addAttribute("post", post);
		model.addAttribute("remove", "none");
		model.addAttribute("pageTitle", "Edit Post");
		model.addAttribute("url", "/updatePost.action");
		model.addAttribute("button", "Edit");
		return "savePost";
	}

	@RequestMapping(value = "/updatePost.action")
	public String updatePostAction(Post post, Model model, HttpSession session,
			@RequestParam("file") MultipartFile file) {

		if (post.getTitle() == "") {
			model.addAttribute("urlName", "Post List");
			model.addAttribute("url", "/postList");
			model.addAttribute("message", "Null title is not allow！");
			return "result";
		}

		if (!file.isEmpty()) {
			long date = new java.util.Date().getTime();
			String filename = StringUtils.cleanPath(file.getOriginalFilename());
			String suffix = filename.substring(filename.lastIndexOf(".") + 1);
			storageService.storeAndSetName(file, String.valueOf(date) + "." + suffix);
			storageService.deleteByName(post.getImage());
			post.setImage(String.valueOf(date) + "." + suffix);
		}
		boolean result = postService.savePost(post);
		if (result) {
			Integer activePage = (Integer) session.getAttribute("activePage");
			if (activePage == null) {
				activePage = 1;
			}
//			Pageable pageable = new PageRequest(activePage - 1, SIZE, SORT);
//			Page<Post> postList = postService.findAllPaged(pageable);
//			model.addAttribute("activePage", activePage);
//			model.addAttribute("postList", postList);
//			model.addAttribute("length", postList.getTotalPages());
//			model.addAttribute("posts", new Posts());
//			return "postList";
			model.addAttribute("url", "/postList?page="+activePage);
			return "redirect";
		} else {
			model.addAttribute("urlName", "Post List");
			model.addAttribute("url", "/postList");
			model.addAttribute("message", "Failure to edit post！");
			return "result";
		}

	}

	@RequestMapping(value = "/")
	public String index(Model model, Integer page,HttpSession session) {

		if (page == null) {
			page = 1;
		}
		Pageable pageable = PageRequest.of(page - 1, 5, SORT);
		// model.addAttribute("postList", postService.findAllOrderByDateAndTime());
		Page<Post> postList = postService.findAllPaged(pageable);
		if (page > postList.getTotalPages() && page !=1) {
			model.addAttribute("urlName", "");
			model.addAttribute("url", "");
			model.addAttribute("message", "Page number is not exist！");
			return "result";
		}

		for (Post post : postList) {
			//String[] contents = post.getContent().split("\\r?\\n");
			String html = MDtoHTML.convert(post.getContent());
			//String content = contents[0];
			String excerpt = HTMLtoText.convert(FirstParagraph.get(html));
//			if (content.length() > 200) {
//				content = content.substring(0, 150);
//			}
			post.setContent(excerpt);
		}
		Integer length = postList.getTotalPages();
		if (length.equals(0)) {
			length = 1;
		}
		model.addAttribute("activePage", page);
		model.addAttribute("postList", postList);
		model.addAttribute("length", length);
		model.addAttribute("posts", new Posts());
		User user = (User)session.getAttribute("user");
		if (user==null) {
			model.addAttribute("loginStat", 0);
		}else if (user.getUname() == null) {
			model.addAttribute("loginStat", 0);
		}else {
			model.addAttribute("loginStat", 1);
		}
		return "index";
	}

	@RequestMapping(value = "/post")
	public String post(Model model, Integer id, HttpSession session) {
		Post post = postService.findById(id);
		//String[] contents;
		String html = "";
		if (post.getContent() != null) {
			html = MDtoHTML.convert(post.getContent());
			
			//contents = post.getContent().split("\\r?\\n");
		}else {
			html = null;
		}
		
		Comment comment = new Comment();
		comment.setPid(id);
		if (session.getAttribute("commenterName") != null && session.getAttribute("commenterEmail") != null) {
			comment.setCommenterName(session.getAttribute("commenterName").toString());
			comment.setCommenterEmail(session.getAttribute("commenterEmail").toString());
		}
		model.addAttribute("post", post);
		model.addAttribute("comment", comment);
		//model.addAttribute("contents", contents);
		model.addAttribute("html", html);
		model.addAttribute("image", "/files/" + post.getImage());
		model.addAttribute("commentList", commentService.findByPid(id));
		
		User user = (User)session.getAttribute("user");
		if (user==null) {
			model.addAttribute("loginStat", 0);
		}else if (user.getUname() == null) {
			model.addAttribute("loginStat", 0);
		}else {
			model.addAttribute("loginStat", 1);
		} 

		return "post";
	}

	@RequestMapping(value = "/search")
	public String search(String keywords, Integer page, Model model) {
		if (page == null) {
			page = 1;
		}
		Pageable pageable = PageRequest.of(page - 1, 5, SORT);
		String[] keywordArray = keywords.split("\\s+");
		// String[] keywordArray = keywords.split(" +");
		Iterable<Post> postIterable = postService.findAll();
		List<Integer> a = new ArrayList<>();
		int i = 0;
		for (String keyword : keywordArray) {
			List<Integer> b = new ArrayList<>();
			for (Post post : postIterable) {
				Integer result = post.getTitle().toUpperCase().indexOf(keyword.toUpperCase(), 0);
				if (result != -1) {
					b.add(post.getPid());
				}
			}
			if (i != 0) {
				List<Integer> c = new ArrayList<>();
				for (Integer i1 : b) {
					for (Integer i2 : a) {
						if (i1.equals(i2)) {

							c.add(i1);
						}
					}
				}
				a = c;
			} else {
				a = b;
				i = i + 1;
			}
		}
		Page<Post> postList = postService.findAllByIdsPaged(a, pageable);

		for (Post post : postList) {
			//String[] contents = post.getContent().split("\\r?\\n");
			String html = MDtoHTML.convert(post.getContent());
			//String content = contents[0];
			String excerpt = HTMLtoText.convert(FirstParagraph.get(html));
//			if (content.length() > 200) {
//				content = content.substring(0, 200) + "......";
//			}
			post.setContent(excerpt);
		}
		Integer length = postList.getTotalPages();
		if (length.equals(0)) {
			length = 1;
		}
		model.addAttribute("keywords", keywords);
		model.addAttribute("activePage", page);
		model.addAttribute("postList", postList);
		model.addAttribute("length", length);
		model.addAttribute("posts", new Posts());
		return "search";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

}
