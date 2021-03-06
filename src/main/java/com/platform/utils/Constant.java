package com.platform.utils;

public class Constant {
	
	public static final String SEPARATOR_COMMA = ",";
	public static final String SEPARATOR_CR = "\r\n";

	public static final byte LOGIN_NAME = 1;
	public static final byte LOGIN_PHONE = 1 << 1;
	public static final byte LOGIN_MAIL = 1 << 2;
	public static final byte LOGIN_SINA = 1 << 3;
	public static final byte LOGIN_WEIXIN = 1 << 4;
	
	public static final int COOKIE_AGE_ONE_HOUR = 60*60;
	public static final int COOKIE_AGE_ONE_DAY = 24*60*60;
	public static final int COOKIE_AGE_ONE_WEEK = 7*24*60*60;
	
	public static final String IDENTITY_NAME = "name";
	public static final String IDENTITY_PHONE = "phone";
	public static final String IDENTITY_MAIN = "mail";
	public static final String IDENTITY_SINA = "sina";
	public static final String IDENTITY_WEIXIN = "weixin";
	
	public static final String TOKEN = "token";
	public static final String CURRENT_UID = "uid";
	
	public static final String SESS_CURRENTUSER = "user";
}
