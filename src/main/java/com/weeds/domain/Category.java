package com.weeds.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tb_category")
//Àà±ð
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
	
	private static final long serialVersionUID = 141567183512940177L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String name;

	@OneToMany(mappedBy = "category")
	@JsonIgnore
	private List<Board> boards = new ArrayList<Board>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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
