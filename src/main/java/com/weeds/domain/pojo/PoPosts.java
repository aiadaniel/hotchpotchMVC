package com.weeds.domain.pojo;

public class PoPosts {
	private Integer id;
	private PoBoard poBoard;
	private String title;
	private String content;
	private PoUser author;
	private PoUser lastReplyAuthor;
	private int prise;
	private int down;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public PoBoard getPoBoard() {
		return poBoard;
	}
	public void setPoBoard(PoBoard poBoard) {
		this.poBoard = poBoard;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public PoUser getAuthor() {
		return author;
	}
	public void setAuthor(PoUser author) {
		this.author = author;
	}
	public PoUser getLastReplyAuthor() {
		return lastReplyAuthor;
	}
	public void setLastReplyAuthor(PoUser lastReplyAuthor) {
		this.lastReplyAuthor = lastReplyAuthor;
	}
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
}
