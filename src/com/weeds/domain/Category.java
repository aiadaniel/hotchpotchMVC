package com.weeds.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tb_category")
//���
public class Category extends BaseBean {
	
	/*
	+-------------+--------------+------+-----+---------+----------------+
	| Field       | Type         | Null | Key | Default | Extra          |
	+-------------+--------------+------+-----+---------+----------------+
	| id          | int(11)      | NO   | PRI | NULL    | auto_increment |
	| dateCreated | datetime     | YES  |     | NULL    |                |
	| deleted     | bit(1)       | NO   |     | NULL    |                |
	| version     | int(11)      | YES  |     | NULL    |                |
	| name        | varchar(255) | YES  |     | NULL    |                |
	+-------------+--------------+------+-----+---------+----------------+
	 */
	
	private String name;

	@OneToMany(mappedBy = "category")
	private List<Board> boards = new ArrayList<Board>();

	public List<Board> getBoards() {
		return boards;
	}

	public void setBoards(List<Board> boards) {
		this.boards = boards;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}