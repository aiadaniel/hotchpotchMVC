package com.platform.utils;

public class Constant {
	
	public static final String SEPARATOR_COMMA = ",";
	
	public static final int LOGIN_NAME = 1 << 0;//nickname
	public static final int LOGIN_PHONE = 1 << 1;
	public static final int LOGIN_MAIL = 1 << 2;
	public static final int LOGIN_SINA = 1 << 3;
	public static final int LOGIN_WEIXIN = 1 << 4;
	
	public static final int COOKIE_AGE_ONE_HOUR = 60*60;
	public static final int COOKIE_AGE_ONE_DAY = 24*60*60;
	public static final int COOKIE_AGE_ONE_WEEK = 7*24*60*60;
}
