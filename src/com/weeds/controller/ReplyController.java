package com.weeds.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.Date;
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
import com.weeds.domain.PlatformUser;
import com.weeds.domain.Post;
import com.weeds.domain.Reply;
import com.weeds.domain.pojo.PoReply;
import com.weeds.service.IService;
import com.weeds.service.PostService;
import com.weeds.service.ReplyService;

@RestController
@RequestMapping("/forum/reply")
@Api(value="回帖api")
public class ReplyController {
	private Logger logger = org.slf4j.LoggerFactory.getLogger(PostController.class);

	@Autowired
	ReplyService replyService;
	
	@Autowired
	PostService postService;
	
	@Autowired
	IService<Board> boardService;
	
	@Autowired
	IService<PlatformUser> userService;
	
	@GetMapping("/list/{postid}")
	@ApiOperation(value="列出回复",notes="列出帖子下的所有回复")
	public List<Reply> list(@ApiParam(required=true,name="postid",value="帖子id") @PathVariable int postid) {
		logger.info("==list post id {}",postid);
		if (postid > 0) {
			return replyService.list("from Reply r where r.post.id = " + postid);
		}
		return null;
	}
	
	@PostMapping("/create/{postid}/{title}/{content}/{uid}")
	@ApiOperation(value="发回复")
	public ResponseEntity<?> createNewReply(@ApiParam(required=true,name="postid",value="帖子id")@PathVariable int postid,
						@ApiParam(required=true,name="title",value="标题")@PathVariable String title,
						@ApiParam(required=true,name="content",value="内容")@PathVariable String content,
						@ApiParam(required=true,name="uid",value="回帖人") @PathVariable int uid) {
		PoReply poReply= new PoReply();
		//找出板块、user、帖子=> 增加回复 =>更新板块最后回复、帖子最后回复
		PlatformUser user = userService.find(PlatformUser.class, uid);//TODO: may be should get PlatformUser info from session
		if (user == null) {
			return new ResponseEntity<>(poReply,HttpStatus.NOT_FOUND);
		}
		Post posts = postService.find(Post.class, postid);
		if (posts == null) {
			return new ResponseEntity<>(poReply,HttpStatus.NOT_FOUND);
		}
		Board board = posts.getBoard();

		Reply reply = new Reply();
		reply.setAuthor(user);
		reply.setContent(content);
		reply.setTitle(title);
		reply.setPost(posts);
		//need to deal with floor
		reply.setFloor(posts.getReplyCount()+1);
		
		replyService.create(reply);//没用上级联，而是自己写了sql语句更新板块
		
		//update post association
		posts.setAuthorLastReplied(user);
		posts.setDateLastReplied(new Date());
		posts.setReplyCount(posts.getReplyCount()+1);
		postService.saveOrUpdate(posts);
		
		//update board association
		board.setLastReply(reply);
		board.setReplyCount(board.getReplyCount()+1);
		boardService.saveOrUpdate(board);
		
		//now update Board last_thread_id 
		if (reply.getId() > 0) {
			poReply.setId(reply.getId());
			poReply.setPostid(postid);
			return new ResponseEntity<>(poReply,HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/*
	 * 直接删除明显会失败，因为外键关联的原因。 所以建表时能否设置级联方式？
	 * 先更新外键才能删除
	 */
	@PostMapping("/delete/{postid}/{replyid}")
	@ApiOperation(value="删除回复")
	public ResponseEntity<?> deletePost(@ApiParam(required=true,name="postid",value="帖子id") @PathVariable int postid,
			@ApiParam(required=true,name="replyid",value="回复id") @PathVariable int replyid) {
		Reply basebean = replyService.find(Reply.class, replyid);
		if (basebean!=null) {
			Post posts = basebean.getPost();
			posts.setReplyCount(posts.getReplyCount()-1);
			
			Board board = posts.getBoard();
			board.setReplyCount(board.getReplyCount()-1);
			board.setLastReply(null);
			
			replyService.delete(basebean);
			//need to update last reply
			Reply lasReply = replyService.getLastReply(postid);
			if (lasReply != null) {
				logger.debug("==get last reply {}",lasReply.getId());
				board.setLastReply((Reply) lasReply);
			}
			//need to update last author
			
			posts.setAuthorLastReplied(lasReply.getAuthor());
			postService.saveOrUpdate(posts);
			boardService.saveOrUpdate(board);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
