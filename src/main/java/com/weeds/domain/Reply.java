package com.weeds.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="tb_reply")
//@org.hibernate.annotations.Entity
//»ØÌû
public class Reply extends BaseBean {

	private static final long serialVersionUID = 7701768827905279582L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "post_id")
	@JsonIgnore
	private Post post;

	private String title;

	@Basic(fetch = FetchType.LAZY)
	private String content;

	@ManyToOne
	@JoinColumn(name = "author_id")
	private PlatformUser author;

	private int floor;//Â¥²ã

	@JsonIgnore
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

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
