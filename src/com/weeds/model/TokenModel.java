package com.weeds.model;

public class TokenModel {
	private long user_id;
	private String token;
	
	public TokenModel(long uid,String t) {
		user_id = uid;
		token = t;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
