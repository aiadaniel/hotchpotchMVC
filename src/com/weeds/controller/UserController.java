package com.weeds.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.utils.Constant;
import com.weeds.domain.Board;
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
	public ResponseEntity<?> regist(@RequestParam String nickname,@RequestParam String password) {
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
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
	}
	
	@PostMapping(path="/login/{nickname}/{password}"/*,params="myparam=1"*/)
	public String login(@PathVariable String nickname,@PathVariable String password) {//方式1
		
		//System.out.println("== i got login " + nickname + "  " + password);
		logger.debug("== i got login {}  {}", nickname,password);
		return "en " + nickname + " !";
	}
	
	/*
	 * 需要级联删除板块管理员信息
	 */
	@GetMapping("/delete/{uid}")
	@ApiOperation(value="删除用户")
	public ResponseEntity<?> delete(@ApiParam(required=true,name="uid",value="要删除用户的uid") @PathVariable int uid) {
		PlatformUser user = userService.find(PlatformUser.class, uid);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		//for @ManyToMany association we need to delete mapping first from 主控方
		Set<Board> boards = user.getBoardsAdministrated();
		for (Iterator<Board> iterator = boards.iterator(); iterator.hasNext();) {
			Board board = (Board) iterator.next();
			board.removeAdministrator(user);
		}
		
		userService.delete(user);
		logger.debug("== after uid is {}",user.getId());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	//json返回的一种方式，还可以直接gson+response write或者使用@responsebody
//	@PostMapping("/userlist")
	//@JsonView(View.Summary.class) //这种方式需要xml文件配置，使用@ResponseBody更为方便
//	public User getUser() {
//		return new User(new Random().nextLong(),"haha","heihei");
//	}
	
	@GetMapping("/list")
	@ApiOperation(value="列出所有用户")
	public List<PlatformUser> getUsers() {
		//List<PlatformUser> users = new ArrayList<PlatformUser>();
		return userService.list("from PlatformUser");
	}
}
