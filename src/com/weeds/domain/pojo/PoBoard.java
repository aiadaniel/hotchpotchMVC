package com.weeds.domain.pojo;

import java.util.Set;


public class PoBoard {

	private Integer id;

	private Integer categoryid;

	private String name;

	private String description;

	private int threadCount;

	private int replyCount;

	private Integer lastReplyid;

	private Integer lastThreadid;
	
	PoPosts poPosts;
	
	Set<PoUser> admins;

	public PoPosts getPoPosts() {
		return poPosts;
	}

	public void setPoPosts(PoPosts poPosts) {
		this.poPosts = poPosts;
	}

	public Set<PoUser> getAdmins() {
		return admins;
	}

	public void setAdmins(Set<PoUser> admins) {
		this.admins = admins;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(Integer categoryid) {
		this.categoryid = categoryid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public int getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}

	public Integer getLastReplyid() {
		return lastReplyid;
	}

	public void setLastReplyid(Integer lastReplyid) {
		this.lastReplyid = lastReplyid;
	}

	public Integer getLastThreadid() {
		return lastThreadid;
	}

	public void setLastThreadid(Integer lastThreadid) {
		this.lastThreadid = lastThreadid;
	}

	//private Set<PlatformUser> administrators = new HashSet<PlatformUser>();
}
