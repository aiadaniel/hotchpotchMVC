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
		getHibernateTemplate().save(basebean);//��ͬ��persist�����Է������л�����
	}
	
	@Override
	public void saveOrUpdate(T basebean) {
		getHibernateTemplate().saveOrUpdate(basebean);
	}

	@Override
	//�����ʵ��ɾ�����漯���еĵ������ݣ���ע���޷�������
	//@CacheEvict(cacheNames="anylists",key="'lists_'+T(basebean)")
	public void delete(T basebean) {
		getHibernateTemplate().delete(basebean);
	}

	/*
	 * �ڲ�ʹ��spring cache��redis���棬ÿ��ֱ�Ӳ����ݿ⣬11���û�����10000������ʱԼ15-17s��ʹ��redis+userdao�ķ���1ʱԼ20s��
	 * ʹ��spring cacheע�����Ҫ5-7s�����; ��ʹ��@Cacheableע�ⷢ�ֺ��������ӳ�ʱ
	 * key��keyGeneratorֻ������һ��
	 * #root.target���ӣ�Ccom.weeds.dao.BaseDao@8be6159
	 * #root.targetClass���ӣ� class com.weeds.dao.BaseDao
	 * 
	 * NOTE: ͨ�����ӻ����߷���ʹ��spring cacheע����list�����л�������Ŀ��ʾ������ȫ��һ���Ŷ���������,���л�����ArrayList����
	 * 			��ʹ�÷���1ʱ���з���Ŀ�ģ���list�е�ÿ������ķ�ʽ����PlatformUser
	 */
	@SuppressWarnings("unchecked")
	@Override
	//@Cacheable(cacheNames="anylists"/*,keyGenerator="myKeyGen"*/,key="'lists'+#sql.replace('from ','_').split(' ')[0]")
	public List<T> list(String sql) {
		//ʵ��ʹ����spring cache���ڲ����Բ��ÿ��ǻ����ˣ�ֱ��ҵ���߼�
		//add redis support
/*		List<T> result = redisTemplate.execute(new RedisCallback<List<T>>() {
			@Override
			public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
				List<T> res = new ArrayList<T>();
				//NOTE: ���￴ÿ�������ݶ�Ҫ���з����л�����ô�����������ڣ�����������ʱӦ��Ҳ��������(��ʹ��spring cache�ĺ�ʱ����Լ1��)
				//method 1
				RedisSerializer<String> serializer = getRedisSerializer();//�����String���͵����л�
				byte[] key = serializer.serialize(sql);
				long len = connection.lLen(key);
				if (len == 0) {
					res = (List<T>) getHibernateTemplate().find(sql);//�����ӳټ��ض�����δ���? Ϊ���ٴο�����û���ӳټ��������ˣ�����
					for (int i = 0; i < res.size(); i++) {
						T item = res.get(i);
						long starttime = System.nanoTime();
						//listOperations.rightPush(sql, SerializationUtils.serialize(item));//�����Զ��������Ҫ�Լ�ʵ�����л�
						connection.rPush(key, SerializationUtils.serialize(item));//�����Զ��������Ҫ�Լ�ʵ�����л�
						//logger.error("==redis set key {} value {}", key,item);
						long endtime = System.nanoTime();
						logger.error("== diff {} ms",(endtime-starttime)/1000000);
					}
					return res;
				}
				//res = listOperations.range(sql, 0, -1);//ʹ��jdk�滻������Զ������л����ٶ����������������ӳػ��� 
				List<byte[]> value =  connection.lRange(key, 0, len);
				for (int i = 0; i < value.size(); i++) {
					long starttime = System.nanoTime();
					T item = (T) SerializationUtils.deserialize(value.get(i));
					long endtime = System.nanoTime();
					logger.error("== diff revert {} ms",(endtime-starttime)/1000000);//һ��user���ݾ����ֶ�Ҫƽ��55ms+ ������
					res.add(item);
				}
				
				//method 2:���ĳ�ʹ��jdkԭ�������л�������
				//NOTE: ������GenericJackson2JsonRedisSerializer���л���ÿ��PlatformUser��������л���С��С�˽�10��,��Ϊ@jsonignore����Ч��
				//ʵ�����ַ�����1�����ǳ�������ӳز����õ������Ч�ʸ��ͣ�����jackson2��ѹ�Ⲣ�����Ը��ƣ�cpuռ���н��ͣ����ӳ���Ȼ�ױ���
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
//				res = listOperations.range(sql,0,-1);//���ﷵ�ص��Ѿ������л���
////				for (int i = 0; i < size; i++) {
////					res.add(listOperations.leftPop(sql));//�������ַ������Ƴ��˻���ġ�����
////				}
//				long endtime = System.nanoTime();
//				logger.error("== diff revert {} ms",(endtime-starttime)/1000000);
				
				
				//method 3: ʹ���������ݽṹ�磺hash����ȥ���л���Ԥ�ڸ��졣ʹ��sdr�ṩ��Jackson2HashMapper��ʽ����ȥ�Լ�����map�� 
				//����3Ŀǰ���Ǻ��ʺϣ�����ûŪ�á�����
				
				
				return res;
			}
		});
		return result;*/
		
		//methdo 4: ʹ�ùܵ�pipeline
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
