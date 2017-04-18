package com.weeds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weeds.dao.IDao;
import com.weeds.domain.Board;

@Transactional
@Service
public class BoardService<T extends Board> extends BaseService<T> {

	/* 也是这种错误，why，这里还未使用事务
	 * org.springframework.beans.factory.BeanNotOfRequiredTypeException: Bean named 'boardService' is expected to be of type 
	 *  'com.weeds.service.BoardService' but was actually of type 'com.sun.proxy.$Proxy96' 并且这次把@Transactional移到BoardController
	 *  也无效了。
	 *  
	 *  最终发现在配置文件加入以下即可： 所以一个接口多个实现类的配置要注意
	 *  <aop:aspectj-autoproxy proxy-target-class="true"/>
	 */
	@Autowired
	@Qualifier("baseDao")
	public void setDao(IDao<T> d) {
		dao = d;
	}
}
