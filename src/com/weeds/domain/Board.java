package com.weeds.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="tb_board")
//板块
public class Board extends BaseBean {
	
	/*例子，现在已经改了
	CREATE TABLE `tb_board` (
		  `id` int(11) NOT NULL AUTO_INCREMENT,
		  `dateCreated` datetime DEFAULT NULL,
		  `deleted` bit(1) NOT NULL,
		  `version` int(11) DEFAULT NULL,
		  `description` varchar(255) DEFAULT NULL,
		  `name` varchar(255) DEFAULT NULL,
		  `replyCount` int(11) NOT NULL,
		  `threadCount` int(11) NOT NULL,
		  `category_id` int(11) DEFAULT NULL,
		  `last_reply_id` int(11) DEFAULT NULL,
		  `last_thread_id` int(11) DEFAULT NULL,
		  PRIMARY KEY (`id`),
		  KEY `FK_2wuuikmopts17tgauc0w6satx` (`category_id`),//这里实际上创建了索引。key分pri、foreign、一般键。
		  KEY `FK_qh7gy6mu5bl84js2u4ya417tn` (`last_reply_id`),
		  KEY `FK_l4u3scdou4moicpxoc2gsusev` (`last_thread_id`),
		  CONSTRAINT `FK_2wuuikmopts17tgauc0w6satx` FOREIGN KEY (`category_id`) REFERENCES `tb_category` (`id`),
		  CONSTRAINT `FK_l4u3scdou4moicpxoc2gsusev` FOREIGN KEY (`last_thread_id`) REFERENCES `tb_thread` (`id`),
		  CONSTRAINT `FK_qh7gy6mu5bl84js2u4ya417tn` FOREIGN KEY (`last_reply_id`) REFERENCES `tb_reply` (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
	*/
	
	/*
	+----------------+--------------+------+-----+---------+----------------+
	| Field          | Type         | Null | Key | Default | Extra          |
	+----------------+--------------+------+-----+---------+----------------+
	| id             | int(11)      | NO   | PRI | NULL    | auto_increment |
	| dateCreated    | datetime     | YES  |     | NULL    |                |
	| deleted        | bit(1)       | NO   |     | NULL    |                |
	| version        | int(11)      | YES  |     | NULL    |                |
	| description    | varchar(255) | YES  |     | NULL    |                |
	| name           | varchar(255) | YES  |     | NULL    |                |
	| replyCount     | int(11)      | NO   |     | NULL    |                |
	| threadCount    | int(11)      | NO   |     | NULL    |                |
	| category_id    | int(11)      | YES  | MUL | NULL    |                |
	| last_reply_id  | int(11)      | YES  | MUL | NULL    |                |
	| last_thread_id | int(11)      | YES  | MUL | NULL    |                |
	+----------------+--------------+------+-----+---------+----------------+
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	private String name;

	private String description;

	private int threadCount;

	private int replyCount;

	
	@ManyToOne
	@JoinColumn(name = "last_reply_id")
	private Reply lastReply;

	@ManyToOne
	@JoinColumn(name = "last_thread_id")
	private Posts lastThread;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "board_administrator", 
				joinColumns = { @JoinColumn(name = "board_id") }, 
				inverseJoinColumns = { @JoinColumn(name = "platformuser_id") })
	@JsonIgnore
	private Set<PlatformUser> administrators = new HashSet<PlatformUser>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	public int getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public Reply getLastReply() {
		return lastReply;
	}

	public void setLastReply(Reply lastReply) {
		this.lastReply = lastReply;
	}

	public Posts getLastThread() {
		return lastThread;
	}

	public void setLastThread(Posts lastThread) {
		this.lastThread = lastThread;
	}

	public Set<PlatformUser> getAdministrators() {
		return administrators;
	}

	public void setAdministrators(Set<PlatformUser> administrators) {
		this.administrators = administrators;
	}
	
	public void addAdministrator(PlatformUser user) {
		this.administrators.add(user);
	}
	
	public void removeAdministrator(PlatformUser user) {
		this.administrators.remove(user);
	}
}
