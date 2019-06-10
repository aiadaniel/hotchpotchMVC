package com.weeds.token;

import com.weeds.model.TokenModel;

/**
 * 
 * token ���󣬺������Խ���redis�ȵ�ʵ��
 */
public interface TokenManager {

	TokenModel createToken(long uid);
	
	boolean checkToken(TokenModel model);
	
	TokenModel getTokenModel(String auth);
	
	boolean deleteToken(long uid);
}
