package com.weeds.token;

import com.weeds.model.TokenModel;

/*
 * 
 * token 抽象，后续可以接入redis等的实现
 */
public interface TokenManager {

	public TokenModel createToken(long uid);
	
	public boolean checkToken(TokenModel model);
	
	public TokenModel getTokenModel(String auth);
	
	public boolean deleteToken(long uid);
}
