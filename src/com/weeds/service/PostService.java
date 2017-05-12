package com.weeds.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.platform.utils.ErrCodeBase;
import com.weeds.dao.IDao;
import com.weeds.domain.Post;

@Transactional
@Service
public class PostService extends BaseService<Post> {
	
	private static final String POSTS_LIST = "allposts";
	private static final String POSTS_KEY = "allposts_id";
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
	
	/*
	 * TODO: ��Ҫ��sql����ҳ�ɣ�����ÿҳ5000��������᲻���ڴ汬��
	 * 
	 * ���ӻ�����ƣ������б��䶯����ҳ��Ҫһ���ϴε���ʼ��������������Ҫ����պø����ӱ�ɾ���߸��µ����
	 * ����ʹ�ã� 
	 * 
	 * 
	 * 			
	 * 
	 * 
	 * ������΢��˼·��
	 * 
	 * һ��΢��ͨ����������ֶΣ����緢��ʱ�䡢�����û����������ݵȣ�ͨ��ʹ��΢�� id ��Ϊ key �������ֵ����Ϊ hash ������ Redis �С�
	 * 
	 * ��һ���û�����������ҳ��Ϣ��ʱ�������Կ��������й�ע�û����µ���Ϣ��key �ǵ�ǰ�û��� uid, ��Ϣ���������� id / timestamp ����ʽ������ zset �У�
	 * timestamp ���������Ա㷵�ص��б��ǰ���ʱ��˳�����С�΢���� id ����ҵ����һ����ȡ΢���������Ϣ��
	 * 
	 * ���ǿ��԰ѹ�ע����˿��Ҳ���� zset �У�����ʹ�� timestamp ������key �ǵ�ǰ�û� uid��
	 * 
	 * TODO: ���������zset�洢postid(member)-timestamp(score)�����ݣ�ts��Ҫ��ȫ���ظ�����������ر��ǲ���ʱts����һ����
	 */
	//@Cacheable(value=CACHE_LIST,key="#hql")
	public List<Post> list(String hql,int lastid,int pagesize) {
//		ListOperations<String, Posts> listOperations = redisTemplate.opsForList();
//		if (listOperations.size(POSTS_LIST)==0) {
//			List<Posts> res = super.list(hql);
//			listOperations.rightPushAll(POSTS_LIST, res);
//		} else {
//			
//			listOperations.range(POSTS_LIST, lastid, lastid + pagesize);//err: lastid is not index
//		}
		
		ArrayList<Post> result = new ArrayList<Post>();
		ZSetOperations<String, Integer> operations = redisTemplate.opsForZSet();
		HashOperations<String, String, Post> hashOperations = contentTemplate.opsForHash();
		if (operations.size(POSTS_KEY) == 0) {//TODO:we need a time to populate data
			List<Post> res = super.list(hql);
			for (int i = 0; i < res.size(); i++) {
				Post posts = res.get(i);
				operations.add(POSTS_KEY, posts.getId(),posts.getDateCreated().getTime());
				hashOperations.put(POSTS_LIST, posts.getId()+"", posts); 
			}
		} else {
			//set => list
			//Range range = new Range();
			Long lastindex = null;
			if (lastid == 0) {
				lastindex = Long.valueOf(0);
			} else {
				//Posts lastPosts = dao.find(Posts.class, lastid);
				//logger.info("==lastpost {} & index is {}",lastPosts.getId(),operations.reverseRank(POSTS_LIST, lastPosts));
				lastindex = operations.reverseRank(POSTS_KEY, lastid)+1;//TODO: fix lastid not exist issue
			}
			Set<Integer> posts = operations.reverseRange(POSTS_KEY, lastindex, lastindex+pagesize-1);
			if (posts != null) {
				Iterator<Integer> iterator = posts.iterator();
				while (iterator.hasNext()) {
					Integer id = iterator.next();
					result.add(hashOperations.get(POSTS_LIST, id+""));
				}
				return result;
			}
		}
		
		
		return super.list(hql);
	}
	
	/*
	 * we need to update redis cache too
	 */
	@Override
	//@CacheEvict//ÿ�ζ��������list������������Ӱ��ģ���Ҫ��չcacheע�⣬��list�Ĳ���ֻ��ɾ�ģ����� 	MethodInteceptor
	public void saveOrUpdate(Post basebean) {
		super.saveOrUpdate(basebean);
		HashOperations<String, String, Post> hashOperations = contentTemplate.opsForHash();
		hashOperations.put(POSTS_LIST, basebean.getId()+"", basebean);
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
		redisTemplate.opsForZSet().add(POSTS_KEY,basebean.getId(),basebean.getDateCreated().getTime());
		contentTemplate.opsForHash().put(POSTS_LIST, basebean.getId()+"", basebean);
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
