package com.platform.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.platform.utils.Constant;
import com.weeds.model.TokenModel;
import com.weeds.token.TokenManager;

public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	TokenManager tokenMgr;
	
	private final Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//logger.info("==preHandle");
		//如果不是映射到方法直接通过
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        
        //从header中得到token
        String authorization = request.getHeader(Constant.TOKEN);
        //logger.info("==i got token from client {}",authorization);
        //验证token
        TokenModel model = tokenMgr.getTokenModel(authorization);
        if (tokenMgr.checkToken(model)) {
        	//如果token验证成功，将token对应的用户id存在request中，便于之后注入
            request.setAttribute(Constant.CURRENT_UID, model.getUid());
            return true;
        }
        //如果验证token失败，并且方法注明了Authorization，返回401错误
        if (method.getAnnotation(Authorization.class) != null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
	}

}
