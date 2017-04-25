package com.weeds.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.platform.utils.ErrCodeBase;
import com.platform.utils.SerializationUtils;

@Repository
public class BaseDao<T,K,V> extends HibernateDaoSupport implements IDao<T> {
	
	private Logger logger = LoggerFactory.getLogger(BaseDao.class);
	
	@Autowired
	protected RedisTemplate<K, V> redisTemplate;
	
	//@Autowired
	//protected RedisTemplate<K, V> kvTemplate;
	
	protected RedisSerializer<String> getRedisSerializer() {
		return redisTemplate.getStringSerializer();
	}
	
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
		getHibernateTemplate().save(basebean);//不同于persist，可以返回序列化对象
	}
	
	@Override
	public void saveOrUpdate(T basebean) {
		getHibernateTemplate().saveOrUpdate(basebean);
	}

	@Override
	public void delete(T basebean) {
		getHibernateTemplate().delete(basebean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> list(String sql) {
		//add redis support
//		List<T> result = redisTemplate.execute(new RedisCallback<List<T>>() {
//			@Override
//			public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
//				List<T> res = new ArrayList<T>();
//				RedisSerializer<String> serializer = getRedisSerializer();//仅针对String类型的序列化
//				byte[] key = serializer.serialize(sql);
//				long end = connection.lLen(key);
//				if (end == 0) {
//					//first time we need to add to redis
//					res = (List<T>) getHibernateTemplate().find(sql);//对于延迟加载对象如何处理?
//					for (int i = 0; i < res.size(); i++) {
//						T item = res.get(i);
//						connection.rPush(key, SerializationUtils.serialize(item));//对于自定义对象需要自己实现序列化
//						logger.error("==redis set key {} value {}", key,item);
//					}
//					return res;
//				}
//				List<byte[]> value =  connection.lRange(key, 0, end);
//				for (int i = 0; i < value.size(); i++) {
//					T item = (T) SerializationUtils.deserialize(value.get(i));
//					res.add(item);
//					logger.error("==redis get key {} value {}",key,item);
//				}
//				return res;
//			}
//		});
//		return result;
		
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
