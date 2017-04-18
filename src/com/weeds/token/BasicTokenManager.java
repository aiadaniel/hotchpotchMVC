package com.weeds.token;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.weeds.model.TokenModel;

@Component
public class BasicTokenManager implements TokenManager {

	
	
	@Override
	public TokenModel createToken(long uid) {
		String token = uid + " " + UUID.randomUUID().toString().replace("-", "");
		return new TokenModel(uid, token);
	}

	@Override
	public boolean checkToken(TokenModel model) {
		return true;
	}

	@Override
	public TokenModel getTokenModel(String auth) {
		return null;
	}

	@Override
	public boolean deleteToken(long uid) {
		return false;
	}

}
