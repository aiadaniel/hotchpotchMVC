package com.weeds.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.weeds.domain.Board;
import com.weeds.domain.Category;
import com.weeds.domain.PlatformUser;
import com.weeds.service.BoardService;
import com.weeds.service.CategoryService;
import com.weeds.service.UserService;


@RestController
@RequestMapping("/forum")
@Api(value="板块api")
public class BoardController {
	
	private Logger logger = LoggerFactory.getLogger(BoardController.class);

	//主要包含创建删除板块、获取板块列表
	
	@Autowired
	//@Qualifier("boardService")
	BoardService<Board> boardService;
	
	@Autowired
	CategoryService<Category> categoryService;
	
	@Autowired
	UserService<PlatformUser> userService;
	
	/*
	 * 这里遇到延迟加载问题了failed to lazily initialize a collection of role:。。。     
	 * 			 no Session (through reference chain:
	 */
	@PostMapping("/board/list")
	@ResponseBody
	public List<Board> listBoard() {
		String hql = "from Board";
		return boardService.list(hql);
	}
	
	@PostMapping("/board/getAdmins/{boardid}")
	@ApiOperation(value="查询板块管理员")
	public Set<PlatformUser> getAdmins(@ApiParam(required=true,name="boardid",value="板块id") @PathVariable int boardid) {
		Set<PlatformUser> users = new HashSet<PlatformUser>();
		Board board = boardService.find(Board.class, boardid);
		users = board.getAdministrators();
		return users;
	}
	
	/*
	 * When test this api,I got this error:
	 * org.springframework.dao.InvalidDataAccessApiUsageException: Write operations are not allowed in read-only mode (FlushMode.MANUAL): 
	 * Turn your Session into FlushMode.COMMIT/AUTO or remove 'readOnly' marker from transaction definition.
	 * 以上最终使用，tx:advice下的配置解决
	 */
	/*
	 *  另外实测url请求中文乱码
	 *  第一：code
		@RequestMapping(value = "/query/{keyword}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
		public String query(@PathVariable String keyword) {
		
		}
		
		第二：tomcat server.xml 中
		<Connector connectionTimeout="20000" port="8080" protocol="HTTP/1.1" redirectPort="8443" URIEncoding="UTF-8"/>
		
		注意：如果使用eclipse启动tomcat时，需要将workspace下面 的server.xml 中的Connector也添加URIEncoding="UTF-8"
	 */
	/*
	 * 聊天吹水、交友征婚、服饰、美容、瘦身、美食、旅游、家居、汽车、婚嫁、亲子、上班、求职、招聘、房产、商业信息的发布
	 */
	@PostMapping("/board/add/{name}/{des}/{cid}/{uid}")
	@ApiOperation(value="添加板块",httpMethod="POST",notes="该接口用于添加板块")
	public ResponseEntity<?> addBoard(@ApiParam(required=true,name="name",value="欲添加板块名") @PathVariable String name,
			@ApiParam(required=false,name="des",value="欲添加板块描述") @PathVariable String des,
			@ApiParam(required=true,name="cid",value="分类id") @PathVariable int cid,
			@ApiParam(required=true,name="uid",value="用户id") @PathVariable int uid) {
		logger.info("==add board {}",name);
		Map<String, String> res = new HashMap<String, String>();
		Board board = new Board();
		Category category = categoryService.find(Category.class, cid);
		if (category == null) {
			res.put("reason", "not category");
			return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
		}
		PlatformUser user = userService.find(PlatformUser.class, uid);
		if (user == null) {
			res.put("reason", "not user found");
			return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
		}
		board.setCategory(category);;
		board.setName(name);
		board.setDateCreated(new Date());
		board.setDescription(des);
		
		Set<PlatformUser> admins = new HashSet<PlatformUser>();
		admins.add(user);
		board.setAdministrators(admins);
		boardService.saveOrUpdate(board);//not use persist and it work without 
										//(detached entity passed to persist: com.weeds.domain.PlatformUser) any more ,but why ?
		res.put("boardid", board.getId()+"");
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	@GetMapping("/addAdmin/{boardid}/{uid}")
	@ApiOperation(value="添加管理员")
	public ResponseEntity<?> addAdministrator(@ApiParam(required=true,name="boardid",value="板块id") @PathVariable int boardid,
			@ApiParam(required=true,name="uid",value="用户id") @PathVariable int uid) {
		Map<String, String> res = new HashMap<String, String>();
		PlatformUser user = userService.find(PlatformUser.class, uid);
		if ( user == null) {
			res.put("reason", "user not found");
			return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
		}
		Board board = boardService.find(Board.class, boardid);
		if ( board == null) {
			res.put("reason", "board not found");
			return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
		}
		board.addAdministrator(user);//到这行已经抛出 failed to lazily initialize a collection of role: com.weeds.domain.Board.administrators
									//配置成功openSessionInViewFilter后的确不会了
		boardService.save(board);
		res.put("boardid", board.getId()+"");
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
}
