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
		//�������ӳ�䵽����ֱ��ͨ��
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        
        //��header�еõ�token
        String authorization = request.getHeader(Constant.TOKEN);
        //logger.info("==i got token from client {}",authorization);
        //��֤token
        TokenModel model = tokenMgr.getTokenModel(authorization);
        if (tokenMgr.checkToken(model)) {
        	//���token��֤�ɹ�����token��Ӧ���û�id����request�У�����֮��ע��
            request.setAttribute(Constant.CURRENT_UID, model.getUid());
            return true;
        }
        //�����֤tokenʧ�ܣ����ҷ���ע����Authorization������401����
        if (method.getAnnotation(Authorization.class) != null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
	}

}
