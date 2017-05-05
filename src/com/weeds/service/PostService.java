package com.weeds.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisZSetCommands.Range;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.platform.utils.ErrCodeBase;
import com.weeds.dao.IDao;
import com.weeds.domain.Posts;

@Transactional
@Service
public class PostService extends BaseService<Posts> {
	
	private static final String POSTS_LIST = "allposts";
	
	private final Logger logger = LoggerFactory.getLogger(PostService.class);

	@Autowired
	@Qualifier("baseDao")
	public void setDao(IDao<Posts> d) {
		dao = d;
	}
	
	@Autowired
	protected RedisTemplate<String, Posts> redisTemplate;
	
	/*
	 * TODO: 需要对sql做分页吧，比如每页5000条，否则会不会内存爆掉
	 * 帖子缓存设计：由于列表会变动，分页需要一个上次的起始索引，索引还需要处理刚好该帖子被删的情况
	 * 数据使用：
	 * 			
	 * 
	 * 
	 * 这里借鉴微博思路：
	 * 
	 * 一条微博通常包括多个字段，比如发表时间、发表用户、正文内容等，通常使用微博 id 作为 key 将多个键值对作为 hash 保存在 Redis 中。
	 * 
	 * 当一个用户访问它的首页信息流时候，他可以看到他所有关注用户最新的信息。key 是当前用户的 uid, 信息流的内容以 id / timestamp 的形式保存在 zset 中，
	 * timestamp 用于排序，以便返回的列表是按照时间顺序排列。微博的 id 用于业务下一步获取微博的相关信息。
	 * 
	 * 我们可以把关注及粉丝库也存在 zset 中，依旧使用 timestamp 来排序。key 是当前用户 uid。
	 */
	public List<Posts> list(String hql,int lastid,int pagesize) {
//		ListOperations<String, Posts> listOperations = redisTemplate.opsForList();
//		if (listOperations.size(POSTS_LIST)==0) {
//			List<Posts> res = super.list(hql);
//			listOperations.rightPushAll(POSTS_LIST, res);
//		} else {
//			
//			listOperations.range(POSTS_LIST, lastid, lastid + pagesize);//err: lastid is not index
//		}
		
//		ZSetOperations<String, Posts> operations = redisTemplate.opsForZSet();
//		if (operations.size(hql) == 0) {
//			List<Posts> res = super.list(hql);
//			for (int i = 0; i < res.size(); i++) {
//				Posts posts = res.get(i);
//				operations.add(hql, posts,posts.getDateCreated().getTime());
//			}
//		} else {
//			//set => list
//			Range range = new Range();
//			range.gt(lastid);
//			range.lte(lastid+pagesize);
//			return new ArrayList<Posts>(redisTemplate.opsForZSet().range(hql, lastid, pagesize));//err: pagesize is not score...
//		}
		
		
		return super.list(hql);
	}
	
	@Override
	public int create(Posts basebean) {
		dao.create(basebean);
		
		//update board 
		int totalPostCnt = dao.getTotalCount("select count(t) from Posts t " + 
							" where t.deleted = false and t.board.id =  " + basebean.getBoard().getId(), new Object[]{});
		
		logger.info("== post create total {} and lastid {} and boardid {}" ,totalPostCnt,basebean.getId(),basebean.getBoard().getId());
		
		dao.createQuery("update Board b "
				+ " set b.lastThread.id = :lastThreadId, b.lastReply.id = null, threadCount = :postCount where b.id = :boardId ")
				.setParameter("lastThreadId", basebean.getId())
				.setParameter("postCount", totalPostCnt)
				.setParameter("boardId", basebean.getBoard().getId())
				.executeUpdate();
		return ErrCodeBase.ERR_SUC;
	}
	
	public Posts deleteAndLast(Posts basebean) {
		dao.delete(basebean);
		//get last post and return
		Posts lastpost = (Posts) dao.createQuery("from Posts p where p.board.id = :boardid order by p.id desc")/*where p.id < :lastid */
				.setParameter("boardid", basebean.getBoard().getId()).setMaxResults(1).uniqueResult();
		return lastpost;
	}
}
