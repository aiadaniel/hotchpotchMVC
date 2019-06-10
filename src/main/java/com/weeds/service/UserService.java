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
 * NOTE: 这个地方的 @Transactional注解在上个版本还能正常运行，这次修改加入了其他的Service如BoardService等之后，
 * 发现竟然不工作了，现象是UserController下的userService无法正确的注入，提示类型不对  
 * (org.springframework.beans.factory.BeanNotOfRequiredTypeException: 
 * Bean named 'userService' must be of type [***], but was actually of type [com.sun.proxy.$Proxy*])。
 * 把@Transactional移到UserController时就能工作。 
 * 目前初步判断是跟hibernate的事务代理有一些冲突吧，后续深入了解下。
 */
@Transactional
@Service
//@PropertySource("file:${appProperties}")
public class UserService<T extends BaseBean> extends BaseService<T> {
	
	private static final String CACHE_NAME = "userlist";
	
	private final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Value("${image.file.upload.dir}")//测试使用这种方式能正常获取到配置，缺点就是要定义一个局部变量，且不能为static或final
	private String uploadDir;
	
	@Autowired
	UserDao<PlatformUser> udao;
	
	@Autowired
	Environment env;
	
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
	
	@Override
	@CacheEvict(cacheNames=CACHE_NAME,key="'users'",beforeInvocation=true)//从业务层面看，删除用户不应该经常发生，可以做黑名单效果
	public void delete(T basebean) {
		super.delete(basebean);//TODO:需要级联删除帖子等信息才能成功
	}
	
	@Override
	/* 
	 * NOTE: 另外这样操作，在压测list接口时，同时在swagger ui操作删除某用户，压测时间明显增加，
	 * 所以这种方式在增删改多的时候，应该没有起到缓存效果！！！
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

					//这种方式取不到值，跟装配顺序有关吗？
					String relativePath = env.getProperty("image.file.upload.dir");
					logger.info("== env upload dir: {} and uploaddir: {} ",relativePath,uploadDir);
					//TODO:
					relativePath = uploadDir;

					File dir = new File(relativePath);
					if (!dir.exists())
						dir.mkdirs();
					String fileExtension = getFileExtension(file);

					// 生成UUID样式的文件名
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
					logger.warn("== transfer to diff {}",endtime2-endtime);//1 1 1 1 1 1 测试了6组数据，明显transfer效率高
					
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
