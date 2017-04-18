package com.weeds.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weeds.domain.Board;
import com.weeds.domain.Posts;
import com.weeds.service.IService;

@RestController
@RequestMapping("/forum/post")
@Api(value="帖子api")
public class PostController {
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(PostController.class);

	@Autowired
	IService<Posts> postService;
	
	@Autowired
	IService<Board> boardService;
	
	@GetMapping("/list/{boardid}")
	@ApiOperation(value="列出帖子",notes="列出板块下的所有帖子")
	public List<Posts> list(@ApiParam(required=false,name="boardid",value="板块id，未填时列出全部帖子") @PathVariable int boardid) {
		logger.debug("==list post board id {}",boardid);
		if (boardid > 0) {
			return postService.list("from Posts p where p.board.id = " + boardid);
		}
		return postService.list("from Posts");
	}
	
	@PostMapping("/create/{boardid}/{title}/{content}")
	@ApiOperation(value="发帖",notes="发新帖")
	public ResponseEntity<?> createNewPost(@ApiParam(required=true,name="boardid",value="板块id")@PathVariable int boardid,
						@ApiParam(required=true,name="title",value="标题")@PathVariable String title,
						@ApiParam(required=true,name="content",value="内容")@PathVariable String content) {
		//找出板块、user=>增加帖子=>更新最后时间
		Board board = boardService.find(Board.class, boardid);
		if (board == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Posts posts = new Posts();
		posts.setBoard(board);
		
		//may be should get PlatformUser info from session
		
		posts.setContent(content);
		String ipRemote = null;//how to get ?
		posts.setIpCreated(ipRemote);
		posts.setTitle(title);
		posts.setTopped(false);
		
		postService.create(posts);
		if (posts.getId() > 0) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
