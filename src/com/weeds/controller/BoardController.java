package com.weeds.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.Date;
import java.util.List;

import javax.persistence.Tuple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.weeds.domain.Board;
import com.weeds.service.BoardService;


@RestController
@RequestMapping("/forum")
@Api(value="���api")
public class BoardController {
	
	private Logger logger = LoggerFactory.getLogger(BoardController.class);

	//��Ҫ��������ɾ����顢��ȡ����б�
	
	@Autowired
	//@Qualifier("boardService")
	BoardService<Board> boardService;
	
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
	@PostMapping("/board/add/{name}/{des}/{id}")
	@ApiOperation(value="��Ӱ��",httpMethod="POST",notes="�ýӿ�������Ӱ��")
	public ResponseEntity<?> addBoard(@ApiParam(required=true,name="name",value="����Ӱ����") @PathVariable String name,
			@ApiParam(required=false,name="des",value="����Ӱ������") @PathVariable String des,
			@ApiParam(required=true,name="id",value="���id") @PathVariable int id) {
		logger.info("==add board {}",name);
		Board board = new Board();
		board.setId(id);
		board.setName(name);
		board.setDateCreated(new Date());
		board.setDescription(des);
		boardService.create(board);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
