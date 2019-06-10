package com.platform.utils;

public class ErrCodeBase {
	/*
	 * 约定：操作不成功的错误码均为负数或0
	 * 
	 */
	public static final int ERR_FAIL = 0;
	public static final int ERR_SUC = 1;
	
	public static final int ERR_SQL = -100;
	
	public static final int ERR_USER_ALREADY = -1;
	public static final int ERR_URER_NOEXISTS = -2;
}
