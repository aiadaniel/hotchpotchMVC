package com.weeds.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.weeds.domain.PlatformUser;

@Repository
public class UserDao<T extends PlatformUser> extends BaseDao<T> {
	
	@Autowired  
	public void setMySessionFactory(SessionFactory sessionFactory){  
	    super.setSessionFactory(sessionFactory);  
	} 


	// note: clazz这种方式更多是面向orm框架的抽象设计，传统jdbc一般不是这样设计
	@SuppressWarnings("unchecked")
	public T find(Class<T> clazz, String nickname) {
		//System.out.println("== i got user dao find");
		// todo: adjust login type
		String sql = "SELECT * FROM tb_platform_user WHERE nickname = '"  + nickname + "'";
		//List<T> users = (List<T>) getHibernateTemplate().find(sql, nickname);//only for hql
		SQLQuery sqlQuery = currentSession().createSQLQuery(sql);
		List<T> users = sqlQuery.list();
		if (users.size() > 0)
			return users.get(0);
		return null;
	}

}
