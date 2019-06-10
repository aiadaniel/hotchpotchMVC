package com.weeds.service;

import com.weeds.dao.IDao;

import java.util.List;

public class BaseService<T>/*V extends BaseBean> extends RedisService<String,V> */implements IService<T> {
	
	public IDao<T> dao;
	
	//写死了key不灵活
	//另外hibernate 有自己的缓存系统
	//redis是否放在dao层?
	//@Autowired
	//protected RedisTemplate<String, V> redisTemplate;

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
	public void saveOrUpdate(T basebean) {
		dao.saveOrUpdate(basebean);
	};

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
