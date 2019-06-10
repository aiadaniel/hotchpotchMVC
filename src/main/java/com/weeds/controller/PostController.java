package com.weeds.controller;

import com.weeds.domain.Board;
import com.weeds.domain.PlatformUser;
import com.weeds.domain.Post;
import com.weeds.domain.pojo.PoBoard;
import com.weeds.service.IService;
import com.weeds.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/forum/post")
@Api(value="帖子api")
public class PostController {
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(PostController.class);

	@Autowired
	PostService postService;
	
	@Autowired
	IService<Board> boardService;
	
	@Autowired
	IService<PlatformUser> userService;
	
	@GetMapping("/list/{boardid}/{page}/{lastid}")
	@ApiOperation(value="列出帖子",notes="列出板块下的所有帖子")
	public List<Post> list(@ApiParam(required=false,name="boardid",value="板块id，未填时列出全部帖子") @PathVariable int boardid,
			@ApiParam(required=true,name="page",value="页码，主要用于分页") @PathVariable int page,
			@ApiParam(required=false,name="lastid",value="上次请求的最后帖子id") @PathVariable String lastid) {
		logger.info("==list post board id {} page {} lastid {}",boardid,page,lastid);
		if (boardid > 0) {
			return postService.list("from Post p where p.board.id = " + boardid + "  order by p.dateCreated asc ",boardid,page,lastid,10);
		}
		return postService.list("from Post");
	}
	
	@PostMapping("/create/{boardid}/{title}/{content}/{uid}")
	@ApiOperation(value="发帖",notes="发新帖")
	public ResponseEntity<?> createNewPost(@ApiParam(required=true,name="boardid",value="板块id")@PathVariable int boardid,
						@ApiParam(required=true,name="title",value="标题")@PathVariable String title,
						@ApiParam(required=true,name="content",value="内容")@PathVariable String content,
						@ApiParam(required=true,name="uid",value="发帖人") @PathVariable int uid) {
		PoBoard poBoard = new PoBoard();
		//找出板块、user=>增加帖子=>更新板块最后发帖时间
		Board board = boardService.find(Board.class, boardid);
		if (board == null) {
			return new ResponseEntity<>(poBoard,HttpStatus.NOT_FOUND);
		}
		PlatformUser user = userService.find(PlatformUser.class, uid);
		if (user == null) {
			return new ResponseEntity<>(poBoard,HttpStatus.NOT_FOUND);
		}
		Post posts = new Post();
		posts.setBoard(board);
		
		//TODO: may be should get PlatformUser info from session
		posts.setAuthor(user);
		
		posts.setContent(content);
		String ipRemote = null;//how to get ?
		posts.setIpCreated(ipRemote);
		posts.setTitle(title);
		posts.setTopped(false);
		posts.setDateCreated(new Date());
		
		postService.create(posts);//没用上级联，而是自己写了sql语句更新板块
		
		//now update Board last_thread_id 
		if (posts.getId() > 0) {
			poBoard.setCategoryid(posts.getBoard().getCategory().getId());
			poBoard.setId(posts.getId());
			poBoard.setName(posts.getTitle());
			return new ResponseEntity<>(poBoard,HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/*
	 * 直接删除明显会失败，因为外键关联的原因。 所以建表时能否设置级联方式？
	 * 先更新外键才能删除
	 */
	@PostMapping("/delete/{boardid}/{postid}")
	@ApiOperation(value="删除帖子")
	public ResponseEntity<?> deletePost(@ApiParam(required=true,name="postid",value="待删id") @PathVariable int postid,
			@ApiParam(required=true,name="boardid",value="板块id") @PathVariable int boardid) {
		Post basebean = postService.find(Post.class, postid);
		if (basebean!=null) {
			Board board = boardService.find(Board.class, boardid);
			board.setLastPost(null);//dis association
			
			Post lastPosts = postService.deleteAndLast(basebean);
			board.setLastPost(lastPosts);
			boardService.saveOrUpdate(board);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
