package com.weeds.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import com.platform.utils.ErrCodeBase;


public class BaseDao<T> extends HibernateDaoSupport implements IDao<T> {
	
	@Autowired  
	public void setMySessionFactory(SessionFactory sessionFactory){  
	    super.setSessionFactory(sessionFactory);  
	} 

	@Override
	public T find(Class<T> clazz, int id) {
		return getHibernateTemplate().get(clazz, id);
	}

//	@Override
//	public T find(Class<T> clazz, String nickname) {
//		getHibernateTemplate().
//		return null;
//	}

	@Override
	public int create(T basebean) {
		getHibernateTemplate().persist(basebean);
		return ErrCodeBase.ERR_SUC;
	}

	@Override
	public void save(T basebean) {
	}

	@Override
	public void delete(T basebean) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> list(String sql) {
		return (List<T>) getHibernateTemplate().find(sql);
	}

	@Override
	public int getTotalCount(String sql, Object... params) {
		return 0;
	}

	@Override
	public List<T> list(String sql, int first, int max, Object... params) {
		return null;
	}

	@Override
	public Query createQuery(String query) {
		return null;
	}

}
