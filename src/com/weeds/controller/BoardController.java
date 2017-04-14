package com.weeds.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

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
	 * 
	 */
	@PostMapping("/forum/board/add/{name}")
	@ApiOperation(value="添加板块",httpMethod="post",response=String.class,notes="该接口用于添加板块")
	public String addBoard(@ApiParam(required=true,name="name",value="欲添加板块名") @PathVariable String name) {
		Board board = new Board();
		board.setName(name);
		boardService.create(board);
		return "board!!!";
	}
}
