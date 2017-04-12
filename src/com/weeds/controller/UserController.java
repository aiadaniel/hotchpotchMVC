package com.weeds.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.platform.utils.Constant;
import com.platform.utils.MD5Utils;
import com.weeds.domain.PlatformUser;
import com.weeds.service.UserService;

@RestController
public class UserController extends MultiActionController {
	//��Ҫ�Ķ�������������xml�ļ���
	
	@Autowired
	UserService<PlatformUser> userService;
	
	@RequestMapping(path = "/user/regist",method = RequestMethod.POST)
	public String regist() {
		System.out.println("== i got regist!");
		PlatformUser user = new PlatformUser();
		user.setNickname("test" + Math.abs( new Random().nextInt()));
		user.setDateCreated(new Date());
		String salt = MD5Utils.generateRandString(6);
		try {
			user.setCredential(MD5Utils.getEncryptedPwd("123456"+salt));
			user.setRandCredential(salt);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user.setLogin_type(Constant.LOGIN_NAME);
		
		userService.userRegist(user);
		return "en ,yes regist!";
	}
	
	@RequestMapping(path="/user/login/{nickname}/{password}",method=RequestMethod.POST/*,params="myparam=1"*/)
	public String login(@PathVariable String nickname,@PathVariable String password) {//��ʽ1
		
		System.out.println("== i got login " + nickname + "  " + password);
		return "en " + nickname + " !";
	}
	
	//json���ص�һ�ַ�ʽ��������ֱ��gson+response write����ʹ��@responsebody
//	@PostMapping("/userlist")
	//@JsonView(View.Summary.class) //���ַ�ʽ��Ҫxml�ļ����ã�ʹ��@ResponseBody��Ϊ����
//	public User getUser() {
//		return new User(new Random().nextLong(),"haha","heihei");
//	}

}
