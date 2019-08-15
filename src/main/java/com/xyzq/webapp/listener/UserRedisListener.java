package com.xyzq.webapp.listener;

import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import com.xyzq.webapp.dao.system.UserDao;
import com.xyzq.webapp.entity.system.User;

/**
 * Package: com.xyzq.webapp.listener
 * Description： 用户数据缓存在redis
 * Author: linkan
 * Date: Created in 2019/8/1 13:52
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
@WebListener
public class UserRedisListener implements ServletContextListener{
	@Resource
	private RedisTemplate<String, User> redisTemplate;
	@Resource
	private UserDao userDao;

	private static Logger logger = LoggerFactory.getLogger(UserRedisListener.class);
	
	private static final String USER_KEY_PREFIX = "webapp:data:t_user:";
	
	/**
	 * Title: contextInitialized
	 * Description:
	 * @param servletContextEvent servletContextEvent
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
        //先删除用户数据
        Set<String> execute = redisTemplate.keys(USER_KEY_PREFIX+"*");
		redisTemplate.delete(execute);
		
		List<User> userList = userDao.findAll();
		for (User user : userList) {
			String userName = user.getUserName();
			redisTemplate.opsForValue().set(USER_KEY_PREFIX+userName, user);
		}

	}

	/**
	 * Title: contextDestroyed
	 * Description: 上下文销毁
	 * @param servletContextEvent servletContextEvent
	 */
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		logger.info("ServletContext 上下文销毁");
	}
}
