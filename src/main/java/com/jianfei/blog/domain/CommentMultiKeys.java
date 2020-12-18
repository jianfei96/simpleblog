package com.jianfei.blog.domain;

import java.io.Serializable;



public class CommentMultiKeys implements Serializable {
	private long cid;
	private int pid;
	public CommentMultiKeys() {
		
	}
	public CommentMultiKeys(long cid, int pid) {
		this.cid = cid;
		this.pid = pid;
	}
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (cid ^ (cid >>> 32));
		result = prime * result + pid;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommentMultiKeys other = (CommentMultiKeys) obj;
		if (cid != other.cid)
			return false;
		if (pid != other.pid)
			return false;
		return true;
	}
	
}
