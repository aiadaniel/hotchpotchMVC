package com.weeds.service;

import java.util.List;

import com.weeds.dao.IDao;

public class BaseService<T> implements IService<T> {
	
	public IDao<T> dao;

	@Override
	public T find(Class<T> clazz, int id) {
		return dao.find(clazz, id);
	}

	@Override
	public int create(T basebean) {
		return dao.create(basebean);
	}

	@Override
	public void save(T basebean) {
		dao.save(basebean);
	}

	@Override
	public void delete(T basebean) {
		dao.delete(basebean);
	}

	@Override
	public List<T> list(String sql) {
		return dao.list(sql);
	}

	@Override
	public int getTotalCount(String sql, Object... params) {
		return dao.getTotalCount(sql, params);
	}

	@Override
	public List<T> list(String sql, int first, int max, Object... params) {
		return dao.list(sql, first, max, params);
	}

}
