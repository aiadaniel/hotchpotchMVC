package com.weeds.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.platform.utils.Constant;
import com.weeds.domain.PlatformUser;
import com.weeds.model.TokenModel;
import com.weeds.service.UserService;
import com.weeds.token.TokenManager;

//@Transactional
@RestController
@RequestMapping("/user")
@Api(value="用户api")
public class UserController {
	
	final Logger logger = LoggerFactory.getLogger(UserController.class);
	//主要的东西还是在配置xml文件里
	
	@Autowired
	UserService<PlatformUser> userService;
	
	@Autowired
	TokenManager basicTokenMgr;
	
	/*
	 * TODO: use https to regist and login; 
	 * 			and return a token to client for next login
	 */
	@PostMapping(path = "/regist")
	@ApiOperation(value="用户注册",httpMethod="POST",response=String.class,notes="该接口用于用户注册")
	public ResponseEntity regist(@RequestParam String nickname,@RequestParam String password) {
		//System.out.println("== i got regist!");
		logger.info("== i got regist!");
		PlatformUser user = new PlatformUser();
		//user.setNickname("test" + Math.abs( new Random().nextInt()));
		user.setNickname(nickname);
		user.setDateCreated(new Date());
		user.setCredential(password);
		user.setLogin_type(Constant.LOGIN_NAME);
		//user.setIdentity_type(Constant.IDENTITY_NAME);
		
		long res = userService.userRegist(user);
		if (res > 0) {
			TokenModel model = basicTokenMgr.createToken(res);
			return new ResponseEntity<>(model, HttpStatus.OK);
		}
		return null;
		
	}
	
	@PostMapping(path="/login/{nickname}/{password}"/*,params="myparam=1"*/)
	public String login(@PathVariable String nickname,@PathVariable String password) {//方式1
		
		//System.out.println("== i got login " + nickname + "  " + password);
		logger.debug("== i got login {}  {}", nickname,password);
		return "en " + nickname + " !";
	}
	
	//json返回的一种方式，还可以直接gson+response write或者使用@responsebody
//	@PostMapping("/userlist")
	//@JsonView(View.Summary.class) //这种方式需要xml文件配置，使用@ResponseBody更为方便
//	public User getUser() {
//		return new User(new Random().nextLong(),"haha","heihei");
//	}
	
	@PostMapping("/list")
	@ResponseBody
	public Map<String,Object> getUsers() {
		List<PlatformUser> users = new ArrayList<PlatformUser>();
		PlatformUser user1 = new PlatformUser();
		user1.setId(new Random().nextInt());
		user1.setNickname("Queen");
		users.add(user1);
		PlatformUser user2 = new PlatformUser();
		user2.setId(new Random().nextInt());
		user2.setNickname("Jake");
		users.add(user1);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("users", users);
		return maps;
	}
}
