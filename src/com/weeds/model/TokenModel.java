package com.weeds.model;

public class TokenModel {
	//token可以尽量多信息，减少后续请求与数据库交互的几率
	//token 还可以使用 JWT (java web token)标准来实现，需要注意防各种攻击
	private long uid;
	private String token;
	
	public TokenModel(long uid,String t) {
		this.uid = uid;
		this.token = t;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
