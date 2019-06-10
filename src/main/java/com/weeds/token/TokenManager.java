package com.weeds.token;

import com.weeds.model.TokenModel;

/**
 * 
 * token 抽象，后续可以接入redis等的实现
 */
public interface TokenManager {

	TokenModel createToken(long uid);
	
	boolean checkToken(TokenModel model);
	
	TokenModel getTokenModel(String auth);
	
	boolean deleteToken(long uid);
}
