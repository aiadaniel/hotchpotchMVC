package com.weeds.dao;

import org.hibernate.Query;

import java.util.List;

public interface IDao<T> {
	
	T find(Class<T> clazz,int id);
	
	//T find(Class<T> clazz,String nickname);
	
	int create(T basebean);
	
	void save(T basebean);
	
	void saveOrUpdate(T basebean);
	
	void delete(T basebean);
	
	List<T> list(String sql);//or hql and so on
	
	int getTotalCount(String sql,Object... params);
	
	List<T> list(String sql,int first,int max,Object... params);
	
	Query createQuery(String query);
}
