package com.weeds.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.platform.utils.ErrCodeBase;
import com.weeds.domain.BaseBean;

@Repository
public class UserDao<T extends BaseBean> extends HibernateDaoSupport implements IDao<T> {
	
	@Autowired  
	public void setMySessionFactory(SessionFactory sessionFactory){  
	    super.setSessionFactory(sessionFactory);  
	} 


	// note: clazz这种方式更多是面向orm框架的抽象设计，传统jdbc一般不是这样设计
	@SuppressWarnings("unchecked")
	@Override
	public T find(Class<T> clazz, String nickname) {
		System.out.println("== i got user dao find");
		// todo: adjust login type
		String sql = "SELECT * FROM tb_platform_users WHERE nickname = '"  + nickname + "'";
		//List<T> users = (List<T>) getHibernateTemplate().find(sql, nickname);//only for hql
		SQLQuery sqlQuery = currentSession().createSQLQuery(sql);
		List<T> users = sqlQuery.list();
		if (users.size() > 0)
			return users.get(0);
		return null;
	}

	// 思考：TODO: 同时要插入两个表，如何处理事务才可靠？
	@Override
	public int create(T basebean) {
		System.out.println("== i got user dao create");
		// insert into
//		String sql = "INSERT INTO tb_platform_users (nickname) VALUES (?)";
		getHibernateTemplate().persist(basebean);
		return ErrCodeBase.ERR_SUC;
	}

}
