package com.weeds.dao;

public interface IDao<T> {
	
	public T find(Class<T> clazz,String nickname);
	
	public int create(T basebean);
}
