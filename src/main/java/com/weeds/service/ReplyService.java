package com.weeds.service;

import com.weeds.dao.IDao;
import com.weeds.domain.Reply;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReplyService extends BaseService<Reply> {
	
	private final Logger logger = LoggerFactory.getLogger(ReplyService.class);

	@Autowired
	@Qualifier("baseDao")
	public void setDao(IDao<Reply> d) {
		dao = d;
	}
	
	public Reply getLastReply(int postid) {
		Query query = dao.createQuery("from Reply r where r.post.id = :postid order by r.id desc");
		if (query == null) {
			logger.warn("==reply create query null");
			return null;
		}
		Object obj = (Reply) query.setParameter("postid", postid).setMaxResults(1).uniqueResult();
		return (Reply) obj;
	}
}
