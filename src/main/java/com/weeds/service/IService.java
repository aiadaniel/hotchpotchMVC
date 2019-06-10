package com.weeds.service;

import java.util.List;

public interface IService<T> {
	
	T find(Class<T> clazz,int id);
	
	int create(T basebean);
	
	void save(T basebean);
	
	void saveOrUpdate(T basebean);
	
	void delete(T basebean);
	
	List<T> list(String sql);//or hql and so on
	
	int getTotalCount(String sql,Object... params);
	
	List<T> list(String sql,int first,int max,Object... params);
}
