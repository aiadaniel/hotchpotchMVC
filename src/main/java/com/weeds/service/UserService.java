package com.weeds.service;

import com.platform.utils.Constant;
import com.platform.utils.ErrCodeBase;
import com.platform.utils.MD5Utils;
import com.weeds.dao.IDao;
import com.weeds.dao.UserDao;
import com.weeds.domain.BaseBean;
import com.weeds.domain.PlatformUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

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
//@PropertySource("file:${appProperties}")
public class UserService<T extends BaseBean> extends BaseService<T> {
	
	private static final String CACHE_NAME = "userlist";
	
	private final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Value("${image.file.upload.dir}")//����ʹ�����ַ�ʽ��������ȡ�����ã�ȱ�����Ҫ����һ���ֲ��������Ҳ���Ϊstatic��final
	private String uploadDir;
	
	@Autowired
	UserDao<PlatformUser> udao;
	
	@Autowired
	Environment env;
	
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
	
	@Override
	@CacheEvict(cacheNames=CACHE_NAME,key="'users'",beforeInvocation=true)//��ҵ����濴��ɾ���û���Ӧ�þ���������������������Ч��
	public void delete(T basebean) {
		super.delete(basebean);//TODO:��Ҫ����ɾ�����ӵ���Ϣ���ܳɹ�
	}
	
	@Override
	/* 
	 * NOTE: ����������������ѹ��list�ӿ�ʱ��ͬʱ��swagger ui����ɾ��ĳ�û���ѹ��ʱ���������ӣ�
	 * �������ַ�ʽ����ɾ�Ķ��ʱ��Ӧ��û���𵽻���Ч��������
	 */
	//@Cacheable(cacheNames=CACHE_NAME,key="'users'")
	public java.util.List<T> list(String sql) {
		return super.list(sql);
	};
	
	public PlatformUser getUserByName(String nickname) {
		return udao.find(PlatformUser.class, nickname);
	}
	
	@CacheEvict(cacheNames=CACHE_NAME,key="'lists_PlatformUser'")
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
	
	public Object uploadImg(int uid,MultipartFile file, HttpServletRequest request) {
		PlatformUser user = udao.find(PlatformUser.class, uid);
		HashMap<String, Object> ret = new HashMap<String, Object>();
		if (file != null) {
			if (!file.isEmpty()) {
				try {

					String rootPath = request.getServletContext().getRealPath("/");

					//���ַ�ʽȡ����ֵ����װ��˳���й���
					String relativePath = env.getProperty("image.file.upload.dir");
					logger.info("== env upload dir: {} and uploaddir: {} ",relativePath,uploadDir);
					//TODO:
					relativePath = uploadDir;

					File dir = new File(relativePath);
					if (!dir.exists())
						dir.mkdirs();
					String fileExtension = getFileExtension(file);

					// ����UUID��ʽ���ļ���
					String filename = java.util.UUID.randomUUID().toString() + "." + fileExtension;

					String fullFilename = dir.getAbsolutePath() + File.separator + filename;

					String relativeFile = /*relativePath + File.separator + */filename;
					
					logger.info("== abspath: {}",dir.getAbsolutePath());
					logger.info("== rootpath: {} fullfilename: {} relativeFile: {}",rootPath,fullFilename,relativeFile);
					
//					long starttime = System.currentTimeMillis();
//
//					byte[] bytes = file.getBytes();
//					File serverFile = new File(fullFilename);
//					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
//					stream.write(bytes);
//					stream.close();
					
					long endtime = System.currentTimeMillis();
//					logger.warn("== stream diff {}",endtime-starttime);//6 6 5 8 22 5 
					
					//test transfer to 
					File serverFile2 = new File(fullFilename);
					file.transferTo(serverFile2);//this method finally invoke commons.io.IOUtils.copyLarge
					long endtime2 = System.currentTimeMillis();
					logger.warn("== transfer to diff {}",endtime2-endtime);//1 1 1 1 1 1 ������6�����ݣ�����transferЧ�ʸ�
					
					logger.info("Server File Location = " + serverFile2.getAbsolutePath());

					String serverPath = new URL(request.getScheme(), request.getServerName(), request.getServerPort(),
							request.getContextPath()).toString();
					ret.put("url", serverPath + "/" + relativeFile);

					user.setAvatar(relativeFile);
					udao.save(user);

				} catch (Exception e) {
					logger.error("error: {}", e);
					ret.put("url", "");
				}
				return ret;
			}
		}
		return null;
	}

	public static String getFileExtension(MultipartFile file) {
		if (file == null) {
			return null;
		}

		String name = file.getOriginalFilename();
		int extIndex = name.lastIndexOf(".");

		if (extIndex == -1) {
			return "";
		} else {
			return name.substring(extIndex + 1);
		}
	}

}
