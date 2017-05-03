package com.platform.utils;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;


public class MyKeyGen implements KeyGenerator {

	@Override
	public Object generate(Object o, Method method, Object... params) {
		StringBuilder sb = new StringBuilder();  
        sb.append(o.getClass().getName());  
        sb.append(method.getName());  
        for (Object obj : params) {  
            sb.append(obj.toString().replace(" ", ""));  
        }  
        return sb.toString();  
	}

}
