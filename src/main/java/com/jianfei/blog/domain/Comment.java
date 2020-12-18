package com.jianfei.blog.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;


@Entity
@IdClass(CommentMultiKeys.class)
public class Comment implements Serializable{
	
	@Id
	private long cid;
	@Id
	private int pid;
	@Lob
	private String content;
	private String commenterName;
	private String commenterEmail;
	private String avatar;
	
	private long preCid;
	private String preCommenterName;
	@Lob
	private String preContent;
	
	@ManyToOne
	private Post post;
	
	public long getCid() {
		return cid;
	}
	
	public int getPid() {
		return pid;
	}	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCommenterName() {
		return commenterName;
	}
	public void setCommenterName(String commenterName) {
		this.commenterName = commenterName;
	}
	public String getCommenterEmail() {
		return commenterEmail;
	}
	public void setCommenterEmail(String commenterEmail) {
		this.commenterEmail = commenterEmail;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getPreCommenterName() {
		return preCommenterName;
	}
	public void setPreCommenterName(String preCommenterName) {
		this.preCommenterName = preCommenterName;
	}
	public String getPreContent() {
		return preContent;
	}
	public void setPreContent(String preContent) {
		this.preContent = preContent;
	}

	public long getPreCid() {
		return preCid;
	}

	public void setPreCid(long preCid) {
		this.preCid = preCid;
	}

	public void setCid(long cid) {
		this.cid = cid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}
	
	

}
