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
	private Integer id;//����uid��ʼֵ�� ����vip��10000000��ʼ������

	@Column(length = 45,nullable=false,unique=true)
	private String nickname;
	
	private String avatar;//default varchar(255)
	
//	@Column(columnDefinition = "tinyint unsigned",nullable=false)//columnDefinitionֻ�ڽ���ʱ����
//	private String identity_type;// ��¼���ͣ��û������ֻ������䡢��������
	
	@Column(length = 128)
	@JsonIgnore
	private String identifier;// �ֻ������䡢�û������������ȵ�Ψһid
	
	@JsonIgnore
	private String credential;// ����or token
	
	@Column(length=12)
	@JsonIgnore
	private String randCredential;// ��������� salt������ݶ�12
	
	@Column(columnDefinition = "tinyint")
	@JsonIgnore
	private byte login_type;
	
	@Column(columnDefinition = "tinyint(2)")
	private byte sex;
	
	@JsonIgnore
	private Date birthday = new Date();//�൱������default
	
	@JsonIgnore
	private String email;// ��identifier�Ƿ��ظ��ˣ�

	@Temporal(value = TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date dateLastActived = new Date();

	@JsonIgnore
	private String ipLastActived;

	/*
	 * ALLʱ����ɾ���û���������İ��Ҳ��ȫ��ɾ��
	 * �������ֶ�ɾ���� 
	 * ���ط�����Ӧ�ÿ�����
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
