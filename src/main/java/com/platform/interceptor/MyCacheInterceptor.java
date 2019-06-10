package com.platform.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cache.interceptor.CacheAspectSupport;

public class MyCacheInterceptor extends CacheAspectSupport implements
		MethodInterceptor {//使用的是aop alliance的接口，而不是spring框架的基于cglib的接口

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
