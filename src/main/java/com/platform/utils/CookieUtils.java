package com.platform.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtils {

	private static final String PLATFORM_USER_COOKIE = "platform.user.cookie";
	private static final String PLATFORM_USER_SESSION = "platform.user.session";

	public Cookie addCookie(String nickname,String cre) {
		Cookie cookie = new Cookie(PLATFORM_USER_COOKIE, nickname
				+ "," + cre);
		cookie.setMaxAge(10);
		return cookie;
	}

	public String[] getCookie() {
		//TODO: ServletActionContext.getRequest();
		HttpServletRequest request = null;
		// HttpSession session = request.getSession();//we need to set the login
		// user to session
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cc : cookies) {
				if (PLATFORM_USER_COOKIE.equals(cc.getName())) {
					String content = cc.getValue();
					if (content != null && !content.isEmpty()) {
						return content.split(Constant.SEPARATOR_COMMA);
					}
				}
			}
		}

		return null;
	}

	// É¾³ýcookie
	public Cookie delCookie(HttpServletRequest request) {
		//TODO£º ServletActionContext.getRequest();
		request = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (PLATFORM_USER_COOKIE.equals(cookie.getName())) {
					cookie.setValue("");
					cookie.setMaxAge(0);
					return cookie;
				}
			}
		}
		return null;
	}
}
