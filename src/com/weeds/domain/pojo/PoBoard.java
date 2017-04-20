package com.weeds.domain.pojo;


public class PoBoard {

	private Integer id;

	private Integer categoryid;

	private String name;

	private String description;

	private int threadCount;

	private int replyCount;

	private Integer lastReplyid;

	private Integer lastThreadid;

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
