/**
 * 
 */
package com.platform.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * ��Controller�ķ�����ʹ�ô�ע�⣬�÷�����ӳ��ʱ�����û��Ƿ��¼��δ��¼����401���� 
 */ 
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorization {

}
