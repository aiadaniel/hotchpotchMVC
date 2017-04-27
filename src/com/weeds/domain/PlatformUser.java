package com.weeds.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.platform.utils.Constant;

@Entity
@Table(name = "tb_platform_user")
public class PlatformUser extends BaseBean {
	
	private static final long serialVersionUID = 2120004248455526708L;

	@TableGenerator(name="uid_gen",initialValue=10000000)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE,generator="uid_gen")
	private Integer id;//控制uid起始值。 比如vip从10000000开始。。。

	@Column(length = 45,nullable=false,unique=true)
	private String nickname;
	
	private String avatar;//default varchar(255)
	
//	@Column(columnDefinition = "tinyint unsigned",nullable=false)//columnDefinition只在建表时有用
//	private String identity_type;// 登录类型，用户名、手机、邮箱、第三方等
	
	@Column(length = 128)
	@JsonIgnore
	private String identifier;// 手机、邮箱、用户名、第三方等的唯一id
	
	@JsonIgnore
	private String credential;// 密码or token
	
	@Column(length=12)
	@JsonIgnore
	private String randCredential;// 密码随机数 salt，最大暂定12
	
	@Column(columnDefinition = "tinyint")
	@JsonIgnore
	private byte login_type;
	
	@Column(columnDefinition = "tinyint(2)")
	private byte sex;
	
	@JsonIgnore
	private Date birthday = new Date();//相当于设置default
	
	@JsonIgnore
	private String email;// 跟identifier是否重复了？

	@Temporal(value = TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date dateLastActived = new Date();

	@JsonIgnore
	private String ipLastActived;

	/*
	 * ALL时测试删除用户，其关联的板块也被全部删除
	 * 方法是手动删除？ 
	 * 主控方控制应该可以做
	 */
	@ManyToMany(mappedBy = "administrators"/*,cascade={CascadeType.ALL}*/)
	@JsonIgnore
	private Set<Board> boardsAdministrated = new HashSet<Board>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public String getRandCredential() {
		return randCredential;
	}

	public void setRandCredential(String randCredential) {
		this.randCredential = randCredential;
	}

	public byte getLogin_type() {
		return login_type;
	}

	public void setLogin_type(byte login_type) {
		this.login_type = login_type;
	}

	public byte getSex() {
		return sex;
	}

	public void setSex(byte sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDateLastActived() {
		return dateLastActived;
	}

	public void setDateLastActived(Date dateLastActived) {
		this.dateLastActived = dateLastActived;
	}

	public String getIpLastActived() {
		return ipLastActived;
	}

	public void setIpLastActived(String ipLastActived) {
		this.ipLastActived = ipLastActived;
	}

	public Set<Board> getBoardsAdministrated() {
		return boardsAdministrated;
	}

	public void setBoardsAdministrated(Set<Board> boardsAdministrated) {
		this.boardsAdministrated = boardsAdministrated;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("==nickname: ").append(nickname).append(Constant.SEPARATOR_CR).
			append("==id: ").append(id).append(Constant.SEPARATOR_CR).
			append("==avatar: ").append(avatar).append(Constant.SEPARATOR_CR);
		return sb.toString();
		
	}
}
