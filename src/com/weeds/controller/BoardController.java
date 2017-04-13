package com.weeds.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.weeds.domain.Board;
import com.weeds.service.BoardService;

@Transactional
@RestController
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
	public String addBoard(@PathVariable String name) {
		Board board = new Board();
		board.setName(name);
		boardService.create(board);
		return "board!!!";
	}
}
