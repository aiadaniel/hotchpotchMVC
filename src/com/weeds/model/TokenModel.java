package com.weeds.model;

public class TokenModel {
	private String uid;
	private String token;
	
	public TokenModel(String uid,String t) {
		this.uid = uid;
		this.token = t;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
