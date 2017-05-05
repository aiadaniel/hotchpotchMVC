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
	 * TODO: ��Ҫ��sql����ҳ�ɣ�����ÿҳ5000��������᲻���ڴ汬��
	 * ���ӻ�����ƣ������б��䶯����ҳ��Ҫһ���ϴε���ʼ��������������Ҫ����պø����ӱ�ɾ�����
	 * ����ʹ�ã�
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
