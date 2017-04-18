package com.weeds.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tb_reply")
//@org.hibernate.annotations.Entity
//»ØÌû
public class Reply extends BaseBean {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "thread_id")
	private Posts thread;

	private String title;

	@Basic(fetch = FetchType.LAZY)
	private String content;

	@ManyToOne
	@JoinColumn(name = "author_id")
	private PlatformUser author;

	private int floor;

	private String ipCreated;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PlatformUser getAuthor() {
		return author;
	}

	public void setAuthor(PlatformUser author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public String getIpCreated() {
		return ipCreated;
	}

	public void setIpCreated(String ipCreated) {
		this.ipCreated = ipCreated;
	}

	public Posts getThread() {
		return thread;
	}

	public void setThread(Posts thread) {
		this.thread = thread;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
