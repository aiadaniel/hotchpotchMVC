package com.weeds.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.platform.utils.ErrCodeBase;
import com.weeds.dao.IDao;
import com.weeds.domain.Posts;

@Transactional
@Service
public class PostService extends BaseService<Posts> {
	
	private final Logger logger = LoggerFactory.getLogger(PostService.class);

	@Autowired
	@Qualifier("baseDao")
	public void setDao(IDao<Posts> d) {
		dao = d;
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
}
