package com.weeds.token;

import com.weeds.model.TokenModel;

/*
 * 
 * token ���󣬺������Խ���redis�ȵ�ʵ��
 */
public interface TokenManager {

	public TokenModel createToken(long uid);
	
	public boolean checkToken(TokenModel model);
	
	public TokenModel getTokenModel(String auth);
	
	public boolean deleteToken(long uid);
}
