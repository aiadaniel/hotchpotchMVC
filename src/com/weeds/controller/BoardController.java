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
@Api(value="���api")
public class BoardController {

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
	
	@PostMapping("/forum/board/add/{name}")
	@ApiOperation(value="��Ӱ��",httpMethod="post",response=String.class,notes="�ýӿ�������Ӱ��")
	public String addBoard(@ApiParam(required=true,name="name",value="����Ӱ����") @PathVariable String name) {
		Board board = new Board();
		board.setName(name);
		boardService.create(board);
		return "board!!!";
	}
}
