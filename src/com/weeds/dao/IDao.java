package com.weeds.dao;

import java.util.List;

import org.hibernate.Query;

public interface IDao<T> {
	
	public T find(Class<T> clazz,int id);
	
	//public T find(Class<T> clazz,String nickname);
	
	public int create(T basebean);
	
	public void save(T basebean);
	
	public void saveOrUpdate(T basebean);
	
	public void delete(T basebean);
	
	public List<T> list(String sql);//or hql and so on
	
	public int getTotalCount(String sql,Object... params);
	
	public List<T> list(String sql,int first,int max,Object... params);
	
	public Query createQuery(String query);
}
