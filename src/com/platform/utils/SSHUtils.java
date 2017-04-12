package com.platform.utils;

import java.util.Arrays;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SSHUtils {
	
	private static ThreadLocal<SessionFactory> sessionFactoryLocal;
	private static ThreadLocal<ApplicationContext> applicationContextLocal;
	
	
	//其实使用spring+junit4的测试框架更方便，可以参考spring官网文档
	
	public static SessionFactory getSessionFactory() {
//		if (sessionFactoryLocal ==  null) {
//			Configuration cfg = new Configuration().configure();//针对hibernate.cfg.xml这种方式,如使用spring配置方式要用ioc方式获取
//			Properties properties = cfg.getProperties();
//			ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(properties).build();
//			SessionFactory sf = cfg.buildSessionFactory(registry);
//			sessionFactoryLocal = new ThreadLocal<SessionFactory>();
//			sessionFactoryLocal.set(sf);
//		}
//		return sessionFactoryLocal.get();
		
		//使用spring ioc的方式  XmlBeanDefinitionReader
		//BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("applicationContext.xml"));
		SessionFactory tempSessionFactory = getAppContext().getBean("sessionFactory",SessionFactory.class);
		return tempSessionFactory;
 	}
	
	public static ApplicationContext getAppContext(){
        if(applicationContextLocal == null){
            ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
            applicationContextLocal = new ThreadLocal<ApplicationContext>();
            applicationContextLocal.set(context);
        }
        return applicationContextLocal.get();
    }
	
	
    public static void main(String[] args) {
        //测试appContext
        //ApplicationContext appContext = getAppContext();
        //String[] names = appContext.getBeanDefinitionNames();
        //System.out.println(Arrays.toString(names));
        
        //测试sessionFactory
        SessionFactory sessionFactory = getSessionFactory();
        Session openSession = sessionFactory.openSession();
        System.out.println(openSession);
        openSession.close();


    }

}
