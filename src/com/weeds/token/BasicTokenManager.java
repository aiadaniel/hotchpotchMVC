package com.weeds.token;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;

import com.weeds.model.TokenModel;

@Component
public class BasicTokenManager implements TokenManager {

	private static final int TOKEN_TIMEOUT = 10;
	
	protected RedisTemplate<Long,String> redisTemplate;
	
	@Autowired
    public void setRedis(RedisTemplate redis) {
        this.redisTemplate = redis;
        //泛型设置成Long后必须更改对应的序列化方案
        redis.setKeySerializer(new JdkSerializationRedisSerializer());
    }
	
	@Override
	public TokenModel createToken(long uid) {
		String token = UUID.randomUUID().toString().replace("-", "");
		TokenModel model = new TokenModel(uid, token);
		redisTemplate.boundValueOps(uid).set(token, TOKEN_TIMEOUT, TimeUnit.SECONDS);
		return model;
	}

	@Override
	public boolean checkToken(TokenModel model) {
		//int res = new Random().nextInt();//先随机试试
		//return res%2==0;
		if (model == null) {
            return false;
        }
        String token = redisTemplate.boundValueOps(model.getUid()).get();
        if (token == null || !token.equals(model.getToken())) {
            return false;
        }
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        redisTemplate.boundValueOps(model.getUid()).expire(TOKEN_TIMEOUT, TimeUnit.HOURS);
        return true;
	}

	@Override
	public TokenModel getTokenModel(String auth) {
		 if (auth == null || auth.length() == 0) {
	            return null;
	        }
	        String[] param = auth.split("_");
	        if (param.length != 2) {
	            return null;
	        }
	        //使用userId和源token简单拼接成的token，可以增加加密措施
	        long userId = Long.parseLong(param[0]);
	        String token = param[1];
	        return new TokenModel(userId, token);
	}

	@Override
	public boolean deleteToken(long uid) {
		redisTemplate.delete(uid);
		return true;
	}

}
