package com.weeds.service;

import com.platform.utils.ErrCodeBase;
import com.weeds.dao.IDao;
import com.weeds.domain.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Transactional
@Service
public class PostService extends BaseService<Post> {
	
	private static final String POSTS_LIST = "allposts_";
	private static final String POSTS_KEY = "allposts_id_";
	@SuppressWarnings("unused")
	private static final String CACHE_LIST = "postlist";
	
	private final Logger logger = LoggerFactory.getLogger(PostService.class);

	@Autowired
	@Qualifier("baseDao")
	public void setDao(IDao<Post> d) {
		dao = d;
	}
	
	@Autowired
	protected RedisTemplate<String, Integer> redisTemplate;
	
	@Autowired
	protected RedisTemplate<String, Post> contentTemplate;
	
	private String getZKey(int boardid) {
		return POSTS_KEY+boardid;
	}
	
	private String getHKey(int boardid) {
		return POSTS_LIST+boardid;
	}
	
	/*
	 * TODO: 需要对sql做分页吧，比如每页5000条，否则会不会内存爆掉
	 * 
	 * 帖子缓存设计：由于列表会变动，分页需要一个上次的起始索引，索引还需要处理刚好该帖子被删或者更新的情况
	 * 数据使用： 
	 * 
	 * 
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
	 * 
	 * TODO: 另外如果以zset存储postid(member)-timestamp(score)的数据，ts需要完全不重复，如何做，特别是并发时ts可能一样的
	 */
	//@Cacheable(value=CACHE_LIST,key="#hql")
	public List<Post> list(String hql,int boardid,int page,String lastid,int pagesize) {
		//sorted set: 	key value score => key id ts
		//hash: 		key field value => key "id" post
		ArrayList<Post> result = new ArrayList<Post>();
		ZSetOperations<String, Integer> zoperations = redisTemplate.opsForZSet();
		String zkey = getZKey(boardid);
		HashOperations<String, String, Post> hashOperations = contentTemplate.opsForHash();
		String hkey = getHKey(boardid);
		if (zoperations.size(zkey) <= pagesize) {//TODO:we need a time to populate data
			List<Post> res = super.list(hql);//currently will get all data from db
			for (int i = 0; i < res.size(); i++) {
				Post posts = res.get(i);
				zoperations.add(zkey, posts.getId(),posts.getDateCreated().getTime());//TODO:如果score一样会覆盖旧数据。。。
				hashOperations.put(hkey, posts.getId()+"", posts); 
			}
		}
		
		{
			//Range range = new Range();
			Long lastindex = Long.valueOf(page*pagesize);

			//add find lastid to deal with post crud issue
			if (lastid != null && !"{lastid}".equals(lastid)) {
				Long invereindex = zoperations.reverseRank(zkey, lastid);
				if (invereindex != null) {
					lastindex = invereindex+1;
				} else {
					logger.info("==lastid {} has been removed from this cache",lastid);
					
				}
			}
			
			Set<Integer> posts = zoperations.reverseRange(zkey, lastindex, lastindex+pagesize-1);
			if (posts != null) {
				Iterator<Integer> iterator = posts.iterator();
				while (iterator.hasNext()) {
					Integer id = iterator.next();
					result.add(hashOperations.get(hkey, id+""));
				}
				return result;
			}
		}
		
		return result;
	}
	
	/*
	 * we need to update redis cache too
	 */
	@Override
	//@CacheEvict//每次都清除整个list，对性能是有影响的，需要扩展cache注解，对list的操作只增删改！！！ 	MethodInteceptor
	public void saveOrUpdate(Post basebean) {
		super.saveOrUpdate(basebean);
		HashOperations<String, String, Post> hashOperations = contentTemplate.opsForHash();
		hashOperations.put(getHKey(basebean.getBoard().getId()), basebean.getId()+"", basebean);
	};
	
	@Override
	public int create(Post basebean) {
		dao.create(basebean);
		
		//update board 
		int totalPostCnt = dao.getTotalCount("select count(t) from Post t " + 
							" where t.deleted = false and t.board.id =  " + basebean.getBoard().getId(), new Object[]{});
		
		logger.info("== post create total {} and lastid {} and boardid {}" ,totalPostCnt,basebean.getId(),basebean.getBoard().getId());
		
		dao.createQuery("update Board b "
				+ " set b.lastPost.id = :lastPostId, b.lastReply.id = null, postCount = :postCount where b.id = :boardId ")
				.setParameter("lastPostId", basebean.getId())
				.setParameter("postCount", totalPostCnt)
				.setParameter("boardId", basebean.getBoard().getId())
				.executeUpdate();
		redisTemplate.opsForZSet().add(getZKey(basebean.getBoard().getId()),basebean.getId(),basebean.getDateCreated().getTime());
		contentTemplate.opsForHash().put(getHKey(basebean.getBoard().getId()), basebean.getId()+"", basebean);
		return ErrCodeBase.ERR_SUC;
	}
	
	public Post deleteAndLast(Post basebean) {
		dao.delete(basebean);
		//get last post and return
		Post lastpost = (Post) dao.createQuery("from Post p where p.board.id = :boardid order by p.id desc")/*where p.id < :lastid */
				.setParameter("boardid", basebean.getBoard().getId()).setMaxResults(1).uniqueResult();
		return lastpost;
	}
}
