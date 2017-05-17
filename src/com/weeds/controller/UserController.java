package com.weeds.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.multipart.MultipartFile;

import com.platform.utils.Constant;
import com.platform.utils.MD5Utils;
import com.platform.utils.ResultStatus;
import com.weeds.apiversion.ApiVersion;
import com.weeds.domain.Board;
import com.weeds.domain.PlatformUser;
import com.weeds.model.ResultModel;
import com.weeds.model.TokenModel;
import com.weeds.service.UserService;
import com.weeds.token.TokenManager;

//@Transactional
@RestController
@RequestMapping("/user")
@Api(value="�û�api")
public class UserController {
	
	final Logger logger = LoggerFactory.getLogger(UserController.class);
	//��Ҫ�Ķ�������������xml�ļ���
	
	@Autowired
	UserService<PlatformUser> userService;
	
	@Autowired
	TokenManager basicTokenMgr;
	
	/*
	 * TODO: use https to regist and login; 
	 * 			and return a token to client for next login
	 */
	@PostMapping(path = "/regist")
	@ApiVersion(3)
	@ApiOperation(value="�û�ע��",httpMethod="POST",response=String.class,notes="�ýӿ������û�ע��")
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
	
	/*
	 * TODO: use token for login
	 * �����¼�ɹ���������session��ϣ�
	 */
	@PostMapping(path="/login/{nickname}/{password}"/*,params="myparam=1"*/)
	@ApiOperation(value="û��tokenʱ�ĵ�¼")
	public ResponseEntity<?> login(@ApiParam(required=true,name="nickname",value="�ǳ�") @PathVariable String nickname,
			@ApiParam(required=true,name="password",value="����") @PathVariable String password,
			HttpSession session) {//��ʽ1
		PlatformUser user = userService.getUserByName(nickname);
		String pass = password + user.getRandCredential();
		try {
			if (user.getCredential().equals(MD5Utils.getEncryptedPwd(pass))) {
				TokenModel model = basicTokenMgr.createToken(user.getId());
				
				//add to session for future use; 
				//also can use @SessionAttribute & @ModelAttribute & ModelMap,in a simple controller
				//ʵ����swagger����ҳ��ֱ��¼��ͬ�˺ţ�session���userֵ�ᱻ���¼�ĸ��ǣ�Ӧ��ʹ��token����������֤�û���Ϣ�����ô�ͳsession
//				session.setAttribute(Constant.SESS_CURRENTUSER, user);
				//session.setMaxInactiveInterval(10);//secs
				
				String auth = model.getUid()+"_"+model.getToken();
				return new ResponseEntity<>(ResultModel.ok(auth),HttpStatus.OK);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//we need to return token
		return new ResponseEntity<>(ResultModel.ok(null),HttpStatus.OK);
	}
	
	@PostMapping("/loginbytoken/{token}")
	@ApiOperation(value="token��¼")
	//@ApiImplicitParam(name="auth",required=true,dataType="string",paramType="header")//��Ҫ������ͷ����֤�ֶ�
	public ResponseEntity<?> loginByToken(@ApiParam(required=true,name="token",value="tokenֵ") @PathVariable String token) {
		TokenModel model = basicTokenMgr.getTokenModel(token);
		if (!basicTokenMgr.checkToken(model)) {
			return new ResponseEntity<>(ResultModel.error(ResultStatus.USER_TOKEN_TIMEOUT),HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/*
	 * ��Ҫ����ɾ��������Ա��Ϣ
	 */
	@GetMapping("/delete/{uid}")
	@ApiOperation(value="ɾ���û�")
	public ResponseEntity<?> delete(@ApiParam(required=true,name="uid",value="Ҫɾ���û���uid") @PathVariable int uid) {
		PlatformUser user = userService.find(PlatformUser.class, uid);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		//for @ManyToMany association we need to delete mapping first from ���ط�
		Set<Board> boards = user.getBoardsAdministrated();
		for (Iterator<Board> iterator = boards.iterator(); iterator.hasNext();) {
			Board board = (Board) iterator.next();
			board.removeAdministrator(user);
		}
		
		userService.delete(user);
		logger.info("== after uid is {}",user.getId());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/list")
	@ApiOperation(value="�г������û�")
	//@ApiImplicitParam(name="auth",/*required=true,*/dataType="string",paramType="header")//��Ҫ������ͷ����֤�ֶ�
	public ResponseEntity<?> getUsers(/*@RequestHeader("auth") String auth,*/HttpSession session) {
//		PlatformUser user = (PlatformUser) session.getAttribute(Constant.SESS_CURRENTUSER);
//		if (user != null) {
//			logger.info(user.toString());
//		} else {
//			logger.error("==can't get user from session");
//		}
		
//		if (!basicTokenMgr.checkToken(basicTokenMgr.getTokenModel(auth))) {
//			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//		}
		return new ResponseEntity<>(userService.list("from PlatformUser"),HttpStatus.OK);
	}
	
	@PostMapping("/upload/avatar/{uid}")
	@ApiOperation(value="�ϴ��û�ͷ��")
	public ResponseEntity<?> uploadUserAvartar(@ApiParam(required=true,name="uid") @PathVariable int uid,
			@RequestParam("file") MultipartFile file,
			HttpServletRequest request) {
		String subfix = ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$";
		if (file != null) {
			Pattern pattern = Pattern.compile(subfix);
			Matcher matcher = pattern.matcher(file.getOriginalFilename());
			if (!matcher.matches()) {
				return new ResponseEntity<>(ResultModel.error(ResultStatus.FILE_NOTSUPPORT),HttpStatus.OK);
			}
			
			logger.info("filename {} originname {} contenttype {}",
					file.getName(),file.getOriginalFilename(),file.getContentType());//contentType: image/png
		}
		userService.uploadImg(uid, file, request);
		return new ResponseEntity<>(ResultModel.ok(),HttpStatus.OK);
	}
}
