package com.weeds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
