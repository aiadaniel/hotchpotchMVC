package com.weeds.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.weeds.domain.Board;
import com.weeds.service.BoardService;


@RestController
@Api(value="���api")
public class BoardController {
	
	private Logger logger = LoggerFactory.getLogger(BoardController.class);

	//��Ҫ��������ɾ����顢��ȡ����б�
	
	@Autowired
	//@Qualifier("boardService")
	BoardService<Board> boardService;
	
	@PostMapping("/forum/board/list")
	@ResponseBody
	public List<Board> listBoard() {
		String hql = "from Board";
		return boardService.list(hql);
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
	@PostMapping("/forum/board/add/{name}")
	@ApiOperation(value="��Ӱ��",httpMethod="POST",response=String.class,notes="�ýӿ�������Ӱ��")
	public String addBoard(@ApiParam(required=true,name="name",value="����Ӱ����") @PathVariable String name) {
		logger.info("==add board {}",name);
		Board board = new Board();
		board.setName(name);
		boardService.create(board);
		return "board!!!";
	}
}
