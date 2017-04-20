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
@Api(value="���api")
public class BoardController {
	
	private Logger logger = LoggerFactory.getLogger(BoardController.class);

	//��Ҫ��������ɾ����顢��ȡ����б�
	
	@Autowired
	//@Qualifier("boardService")
	BoardService<Board> boardService;
	
	@Autowired
	CategoryService<Category> categoryService;
	
	@Autowired
	UserService<PlatformUser> userService;
	
	/*
	 * ���������ӳټ���������failed to lazily initialize a collection of role:������     
	 * 			 no Session (through reference chain:
	 */
	@PostMapping("/board/list")
	@ResponseBody
	public List<Board> listBoard() {
		String hql = "from Board";
		return boardService.list(hql);
	}
	
	@PostMapping("/board/getAdmins/{boardid}")
	@ApiOperation(value="��ѯ������Ա")
	public Set<PlatformUser> getAdmins(@ApiParam(required=true,name="boardid",value="���id") @PathVariable int boardid) {
		Set<PlatformUser> users = new HashSet<PlatformUser>();
		Board board = boardService.find(Board.class, boardid);
		users = board.getAdministrators();
		return users;
	}
	
	/*
	 * When test this api,I got this error:
	 * org.springframework.dao.InvalidDataAccessApiUsageException: Write operations are not allowed in read-only mode (FlushMode.MANUAL): 
	 * Turn your Session into FlushMode.COMMIT/AUTO or remove 'readOnly' marker from transaction definition.
	 * ��������ʹ�ã�tx:advice�µ����ý��
	 */
	/*
	 *  ����ʵ��url������������
	 *  ��һ��code
		@RequestMapping(value = "/query/{keyword}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
		public String query(@PathVariable String keyword) {
		
		}
		
		�ڶ���tomcat server.xml ��
		<Connector connectionTimeout="20000" port="8080" protocol="HTTP/1.1" redirectPort="8443" URIEncoding="UTF-8"/>
		
		ע�⣺���ʹ��eclipse����tomcatʱ����Ҫ��workspace���� ��server.xml �е�ConnectorҲ���URIEncoding="UTF-8"
	 */
	/*
	 * ���촵ˮ���������顢���Ρ����ݡ�������ʳ�����Ρ��Ҿӡ���������ޡ����ӡ��ϰࡢ��ְ����Ƹ����������ҵ��Ϣ�ķ���
	 */
	@PostMapping("/board/add/{name}/{des}/{cid}/{uid}")
	@ApiOperation(value="��Ӱ��",httpMethod="POST",notes="�ýӿ�������Ӱ��")
	public ResponseEntity<?> addBoard(@ApiParam(required=true,name="name",value="����Ӱ����") @PathVariable String name,
			@ApiParam(required=false,name="des",value="����Ӱ������") @PathVariable String des,
			@ApiParam(required=true,name="cid",value="����id") @PathVariable int cid,
			@ApiParam(required=true,name="uid",value="�û�id") @PathVariable int uid) {
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
	@ApiOperation(value="��ӹ���Ա")
	public ResponseEntity<?> addAdministrator(@ApiParam(required=true,name="boardid",value="���id") @PathVariable int boardid,
			@ApiParam(required=true,name="uid",value="�û�id") @PathVariable int uid) {
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
		board.addAdministrator(user);//�������Ѿ��׳� failed to lazily initialize a collection of role: com.weeds.domain.Board.administrators
									//���óɹ�openSessionInViewFilter���ȷ������
		boardService.save(board);
		res.put("boardid", board.getId()+"");
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
}
