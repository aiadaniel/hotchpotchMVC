package com.platform.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cache.interceptor.CacheAspectSupport;

public class MyCacheInterceptor extends CacheAspectSupport implements
		MethodInterceptor {//ʹ�õ���aop alliance�Ľӿڣ�������spring��ܵĻ���cglib�Ľӿ�

	@Override
	public Object invoke(MethodInvocation arg0) {
		return null;
	}

//	@Override
//	public Object intercept(Object arg0, Method arg1, Object[] arg2,
//			MethodProxy arg3) throws Throwable {
//		return null;
//	}

}
