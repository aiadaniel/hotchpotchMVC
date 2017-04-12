package com.weeds.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_platform_users")
public class PlatformUser extends BaseBean {
	
	private String nickname;
	private String avatar;
	private String identity_type;//登录类型，用户名、手机、邮箱、第三方等
	private String identifier;//手机、邮箱、用户名、第三方等的唯一id
	private String credential;//密码or token
	private String randCredential;//密码随机数 salt
	private int login_type;
	
	public int getLogin_type() {
		return login_type;
	}
	public void setLogin_type(int login_type) {
		this.login_type = login_type;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getIdentity_type() {
		return identity_type;
	}
	public void setIdentity_type(String identity_type) {
		this.identity_type = identity_type;
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
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}
