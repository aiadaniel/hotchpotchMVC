package com.weeds.model;

public class TokenModel {
	//token���Ծ�������Ϣ�����ٺ������������ݿ⽻���ļ���
	//token ������ʹ�� JWT (java web token)��׼��ʵ�֣���Ҫע������ֹ���
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
