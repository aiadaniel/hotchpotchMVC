package com.weeds.token;

import com.weeds.model.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class BasicTokenManager implements TokenManager {

	private static final int TOKEN_TIMEOUT = 30;
	
	protected RedisTemplate<Long,String> redisTemplate;
	
	@Autowired
    public void setRedis(RedisTemplate redis) {
        this.redisTemplate = redis;
        //�������ó�Long�������Ķ�Ӧ�����л�����
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
		//int res = new Random().nextInt();//���������
		//return res%2==0;
		if (model == null) {
            return false;
        }
        String token = redisTemplate.boundValueOps(model.getUid()).get();
        if (token == null || !token.equals(model.getToken())) {
            return false;
        }
        //�����֤�ɹ���˵�����û�������һ����Ч�������ӳ�token�Ĺ���ʱ��
        redisTemplate.boundValueOps(model.getUid()).expire(TOKEN_TIMEOUT, TimeUnit.SECONDS);
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
	        //ʹ��userId��Դtoken��ƴ�ӳɵ�token���������Ӽ��ܴ�ʩ
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
