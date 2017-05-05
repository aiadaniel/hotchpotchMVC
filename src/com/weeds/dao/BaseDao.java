package com.weeds.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.ExtendsQueueEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.BeanUtilsHashMapper;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.hash.JacksonHashMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.platform.utils.ErrCodeBase;
import com.platform.utils.SerializationUtils;
import com.weeds.domain.BaseBean;

@Repository
public class BaseDao<T,K,V> extends HibernateDaoSupport implements IDao<T> {
	
	private Logger logger = LoggerFactory.getLogger(BaseDao.class);
	
	@Autowired
	protected RedisTemplate<K, V> redisTemplate;
	
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
	//如果想实现删除缓存集合中的单项数据，用注解无法做到？
	//@CacheEvict(cacheNames="anylists",key="'lists_'+T(basebean)")
	public void delete(T basebean) {
		getHibernateTemplate().delete(basebean);
	}

	/*
	 * 在不使用spring cache及redis缓存，每次直接查数据库，11个用户数据10000并发耗时约15-17s；使用redis+userdao的方法1时约20s，
	 * 使用spring cache注解基本要5-7s才完成; 不使用@Cacheable注解发现很容易链接超时
	 * key和keyGenerator只能设置一个
	 * #root.target例子：Ccom.weeds.dao.BaseDao@8be6159
	 * #root.targetClass例子： class com.weeds.dao.BaseDao
	 * 
	 * NOTE: 通过可视化工具发现使用spring cache注解下list的序列化并非条目显示，而是全部一整团二进制数据,序列化的是ArrayList对象
	 * 			而使用方法1时是有分条目的，按list中的每个对象的方式，如PlatformUser
	 */
	@SuppressWarnings("unchecked")
	@Override
	//@Cacheable(cacheNames="anylists"/*,keyGenerator="myKeyGen"*/,key="'lists'+#sql.replace('from ','_').split(' ')[0]")
	public List<T> list(String sql) {
		//实际使用了spring cache后内部可以不用考虑缓存了，直接业务逻辑
		//add redis support
/*		List<T> result = redisTemplate.execute(new RedisCallback<List<T>>() {
			@Override
			public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
				List<T> res = new ArrayList<T>();
				//NOTE: 这里看每次拿数据都要进行反序列化，那么缓存的意义何在，并发的最大耗时应该也是在这里(比使用spring cache的耗时增加约1倍)
				//method 1
				RedisSerializer<String> serializer = getRedisSerializer();//仅针对String类型的序列化
				byte[] key = serializer.serialize(sql);
				long len = connection.lLen(key);
				if (len == 0) {
					res = (List<T>) getHibernateTemplate().find(sql);//对于延迟加载对象如何处理? 为何再次开启就没有延迟加载问题了？？？
					for (int i = 0; i < res.size(); i++) {
						T item = res.get(i);
						long starttime = System.nanoTime();
						//listOperations.rightPush(sql, SerializationUtils.serialize(item));//对于自定义对象需要自己实现序列化
						connection.rPush(key, SerializationUtils.serialize(item));//对于自定义对象需要自己实现序列化
						//logger.error("==redis set key {} value {}", key,item);
						long endtime = System.nanoTime();
						logger.error("== diff {} ms",(endtime-starttime)/1000000);
					}
					return res;
				}
				//res = listOperations.range(sql, 0, -1);//使用jdk替换下面的自定义序列化，速度有所提升，但连接池会有 
				List<byte[]> value =  connection.lRange(key, 0, len);
				for (int i = 0; i < value.size(); i++) {
					long starttime = System.nanoTime();
					T item = (T) SerializationUtils.deserialize(value.get(i));
					long endtime = System.nanoTime();
					logger.error("== diff revert {} ms",(endtime-starttime)/1000000);//一个user数据就两字段要平均55ms+ ？？？
					res.add(item);
				}
				
				//method 2:更改成使用jdk原生的序列化来测试
				//NOTE: 在引入GenericJackson2JsonRedisSerializer序列化后，每个PlatformUser对象的序列化大小缩小了近10倍,因为@jsonignore是生效的
				//实测这种方法比1产生非常多的连接池不够用的情况，效率更低；引入jackson2后压测并无明显改善，cpu占用有降低，连接池仍然易爆掉
//				ListOperations<String, T> listOperations = (ListOperations<String, T>) redisTemplate.opsForList();
//				Long size = listOperations.size(sql);
//				if (size==0) {
//					res = (List<T>) getHibernateTemplate().find(sql);
//					long starttime = System.nanoTime();
//					listOperations.rightPushAll(sql, res);
//					long endtime = System.nanoTime();
//					logger.error("== diff {} ms",(endtime-starttime)/1000000);
//					return res;
//				}
//				long starttime = System.nanoTime();
//				res = listOperations.range(sql,0,-1);//这里返回的已经反序列化了
////				for (int i = 0; i < size; i++) {
////					res.add(listOperations.leftPop(sql));//但是这种方法是移除了缓存的。。。
////				}
//				long endtime = System.nanoTime();
//				logger.error("== diff revert {} ms",(endtime-starttime)/1000000);
				
				
				//method 3: 使用其他数据结构如：hash，免去序列化，预期更快。使用sdr提供的Jackson2HashMapper方式，免去自己操作map。 
				//方法3目前不是很适合，或者没弄好。。。
				
				
				return res;
			}
		});
		return result;*/
		
		//methdo 4: 使用管道pipeline
//		redisTemplate.executePipelined(new RedisCallback() {
//
//			@Override
//			public List<T> doInRedis(RedisConnection connection)
//					throws DataAccessException {
//				
//				return null;
//			}
//		});
		
		System.out.println("==get from db " + sql);
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
