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
@Api(value="板块api")
public class BoardController {
	
	private Logger logger = LoggerFactory.getLogger(BoardController.class);

	//主要包含创建删除板块、获取板块列表
	
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
	@PostMapping("/forum/board/add/{name}")
	@ApiOperation(value="添加板块",httpMethod="POST",response=String.class,notes="该接口用于添加板块")
	public String addBoard(@ApiParam(required=true,name="name",value="欲添加板块名") @PathVariable String name) {
		logger.info("==add board {}",name);
		Board board = new Board();
		board.setName(name);
		boardService.create(board);
		return "board!!!";
	}
}
