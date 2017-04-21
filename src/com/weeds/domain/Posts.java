package com.weeds.domain;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="tb_thread")
//@org.hibernate.annotations.Entity
//帖子
public class Posts extends BaseBean {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne//(cascade={CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name = "board_id")
	@JsonIgnore
	private Board board;

	private String title;

	@Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition="longtext")
	private String content;

	@ManyToOne
	@JoinColumn(name = "author_id")
	private PlatformUser author;

	@JsonIgnore
	private String ipCreated;

	@JsonIgnore
	private int hit;//点击数
	
	private int prise;//点赞
	private int down;//踩

	public int getPrise() {
		return prise;
	}

	public void setPrise(int prise) {
		this.prise = prise;
	}

	public int getDown() {
		return down;
	}

	public void setDown(int down) {
		this.down = down;
	}

	@ManyToOne
	@JoinColumn(name = "author_last_replied_id")
	private PlatformUser authorLastReplied;//最后回复人

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateLastReplied;//最后回复日期

	private boolean readonly;

	private boolean topped;

	private int replyCount;

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

	public PlatformUser getAuthorLastReplied() {
		return authorLastReplied;
	}

	public void setAuthorLastReplied(PlatformUser authorLastReplied) {
		this.authorLastReplied = authorLastReplied;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDateLastReplied() {
		return dateLastReplied;
	}

	public void setDateLastReplied(Date dateLastReplied) {
		this.dateLastReplied = dateLastReplied;
	}

	public String getIpCreated() {
		return ipCreated;
	}

	public void setIpCreated(String ipCreated) {
		this.ipCreated = ipCreated;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public int getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isTopped() {
		return topped;
	}

	public void setTopped(boolean topped) {
		this.topped = topped;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

}
