package com.weeds.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.platform.utils.ErrCodeBase;

@Repository
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
		getHibernateTemplate().delete(basebean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> list(String sql) {
		return (List<T>) getHibernateTemplate().find(sql);
	}

	@Override
	public int getTotalCount(String sql, Object... params) {
		Query query = createQuery(sql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i+1, params[i]);
		}
		Object object = createQuery(sql).uniqueResult();
		return ((Long)object).intValue();
	}

	@Override
	public List<T> list(String sql, int first, int max, Object... params) {
		return null;
	}

	@Override
	public Query createQuery(String query) {
		return currentSession().createQuery(query);
	}

}
