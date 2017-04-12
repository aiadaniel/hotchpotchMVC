package com.weeds.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.platform.utils.Constant;
import com.platform.utils.ErrCodeBase;
import com.platform.utils.MD5Utils;
import com.weeds.dao.UserDao;
import com.weeds.domain.BaseBean;
import com.weeds.domain.PlatformUser;

@Transactional
@Service
public class UserService<T extends BaseBean> implements IService<T> {
	
	@Autowired
	UserDao<PlatformUser> dao;
	
	//这种方式声明的bean耦合度比较高，是否放到xml去声明好些~
//	@Bean
//	public UserDao<PlatformUser> createDao() {
//		return new UserDao<PlatformUser>();
//	}
	
	public int userRegist(PlatformUser basebean ) {
		if (dao.find(null, basebean.getNickname())!= null ) {
			return ErrCodeBase.ERR_USER_ALREADY;
		}
		//first we need to generate salt for login by nickname
		if (basebean.getLogin_type()==Constant.LOGIN_NAME) {
			String salt = MD5Utils.generateRandString(6);
			String credential = basebean.getCredential() + salt;
			try {
				basebean.setCredential(MD5Utils.getEncryptedPwd(credential));
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			basebean.setRandCredential(salt);
			if (dao.create(basebean) != ErrCodeBase.ERR_SUC) {
				return ErrCodeBase.ERR_FAIL;
			}
		}
		
		return ErrCodeBase.ERR_SUC;
	}
	
	public int login(PlatformUser user,StringBuffer sb) {
		PlatformUser tempUser = (PlatformUser) dao.find(null, user.getNickname());
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
