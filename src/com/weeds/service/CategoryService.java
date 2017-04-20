package com.weeds.service;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weeds.dao.BaseDao;
import com.weeds.dao.IDao;
import com.weeds.domain.Category;

@Transactional
@Service
public class CategoryService<T extends Category> extends BaseService<T> {

	@Autowired
	@Qualifier("baseDao")
	public void setDao(IDao<T> d) {
		dao = d;
	}
	
	@SuppressWarnings("unchecked")
	public Category find(String cname) {
		String hql = "from Category c where c.name=:cname";
		BaseDao<T> bdao = (BaseDao<T>) dao;
		Query query = bdao.createQuery(hql).setParameter("cname", cname);
		List<Category> lists = query.list();
		if (lists.size() > 0) {
			return lists.get(0);
		}
		return null;
	}
}
