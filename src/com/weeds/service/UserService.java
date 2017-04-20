package com.weeds.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.platform.utils.Constant;
import com.platform.utils.ErrCodeBase;
import com.platform.utils.MD5Utils;
import com.weeds.dao.IDao;
import com.weeds.dao.UserDao;
import com.weeds.domain.BaseBean;
import com.weeds.domain.PlatformUser;

/*
 * NOTE: ����ط��� @Transactionalע�����ϸ��汾�����������У�����޸ļ�����������Service��BoardService��֮��
 * ���־�Ȼ�������ˣ�������UserController�µ�userService�޷���ȷ��ע�룬��ʾ���Ͳ���  
 * (org.springframework.beans.factory.BeanNotOfRequiredTypeException: 
 * Bean named 'userService' must be of type [***], but was actually of type [com.sun.proxy.$Proxy*])��
 * ��@Transactional�Ƶ�UserControllerʱ���ܹ����� 
 * Ŀǰ�����ж��Ǹ�hibernate�����������һЩ��ͻ�ɣ����������˽��¡�
 */
@Transactional
@Service
public class UserService<T extends BaseBean> extends BaseService<T> {
	
	@Autowired
	UserDao<PlatformUser> udao;
	
	//���ַ�ʽ������bean��϶ȱȽϸߣ��Ƿ�ŵ�xmlȥ������Щ~
//	@Bean
//	public UserDao<PlatformUser> createDao() {
//		return new UserDao<PlatformUser>();
//	}
	
	@Autowired
	//@Qualifier("baseDao")
	public void setDao(IDao<T> d) {
		dao = d;
	}
	
	public long userRegist(PlatformUser basebean ) {
		if (udao.find(null, basebean.getNickname())!= null ) {
			return ErrCodeBase.ERR_USER_ALREADY;
		}
		//first we need to generate salt for login by nickname
		if (basebean.getLogin_type()==Constant.LOGIN_NAME) {
			String salt = MD5Utils.generateRandString(6);
			String credential = basebean.getCredential() + salt;
			try {
				basebean.setCredential(MD5Utils.getEncryptedPwd(credential));
				basebean.setRandCredential(salt);
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (udao.create(basebean) != ErrCodeBase.ERR_SUC) {
				return ErrCodeBase.ERR_FAIL;
			}
		}
		//when we success to persist obj,the primary key id will be set in obj
		return basebean.getId();
	}
	
	public int login(PlatformUser user,StringBuffer sb) {
		PlatformUser tempUser = (PlatformUser) udao.find(null, user.getNickname());
		if (tempUser == null) {
			return ErrCodeBase.ERR_URER_NOEXISTS;
		}
		//validate password
		try {
			String cre = MD5Utils.getEncryptedPwd(user.getCredential()+tempUser.getRandCredential());
			if (user.getLogin_type()==Constant.LOGIN_NAME && 
					tempUser.getCredential().equals(cre) ) {
				sb.append(cre);
				return ErrCodeBase.ERR_SUC;
			}
			//other login type
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ErrCodeBase.ERR_FAIL;
	}

}
