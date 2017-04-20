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
 * NOTE: 这个地方的 @Transactional注解在上个版本还能正常运行，这次修改加入了其他的Service如BoardService等之后，
 * 发现竟然不工作了，现象是UserController下的userService无法正确的注入，提示类型不对  
 * (org.springframework.beans.factory.BeanNotOfRequiredTypeException: 
 * Bean named 'userService' must be of type [***], but was actually of type [com.sun.proxy.$Proxy*])。
 * 把@Transactional移到UserController时就能工作。 
 * 目前初步判断是跟hibernate的事务代理有一些冲突吧，后续深入了解下。
 */
@Transactional
@Service
public class UserService<T extends BaseBean> extends BaseService<T> {
	
	@Autowired
	UserDao<PlatformUser> udao;
	
	//这种方式声明的bean耦合度比较高，是否放到xml去声明好些~
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
