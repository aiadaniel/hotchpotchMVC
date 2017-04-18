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

	/* Ҳ�����ִ���why�����ﻹδʹ������
	 * org.springframework.beans.factory.BeanNotOfRequiredTypeException: Bean named 'boardService' is expected to be of type 
	 *  'com.weeds.service.BoardService' but was actually of type 'com.sun.proxy.$Proxy96' ������ΰ�@Transactional�Ƶ�BoardController
	 *  Ҳ��Ч�ˡ�
	 *  
	 *  ���շ����������ļ��������¼��ɣ� ����һ���ӿڶ��ʵ���������Ҫע��
	 *  <aop:aspectj-autoproxy proxy-target-class="true"/>
	 */
	@Autowired
	@Qualifier("baseDao")
	public void setDao(IDao<T> d) {
		dao = d;
	}
}
