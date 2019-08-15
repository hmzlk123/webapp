package com.xyzq.webapp.service.system.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.xyzq.webapp.dao.system.UserDao;
import com.xyzq.webapp.entity.system.Role;
import com.xyzq.webapp.entity.system.User;
import com.xyzq.webapp.entity.system.UserLockedRecord;
import com.xyzq.webapp.service.system.UserService;

/**
 * Package: com.xyzq.webapp.service.system.impl
 * Description： 用户服务接口实现类
 * Author: linkan
 * Date: Created in 2019/8/1 14:55
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
@Service
public class UserServiceImpl implements UserService {
	
	private static final String USER_KEY_PREFIX = "webapp:data:t_user:";
	
	@Resource
	private UserDao userDao;
	
	@Resource
	private RedisTemplate<String, User> redisTemplate;
	 

	@Override
	public User findByNameAndPassword(String userName, String password) {
		return userDao.findByNameAndPassword(userName, password);
	}

	@Override
	public User findByName(String userName) {
		//先查询redis缓存数据
		User user = redisTemplate.opsForValue().get(USER_KEY_PREFIX+userName);
		if(user!=null) {
			return user;
		}
		
		//查询数据库的数据
		user = userDao.findByName(userName);
		if(user!=null)
			redisTemplate.opsForValue().set(USER_KEY_PREFIX+userName, user);
		return user;
	}
	
	@Override
	public void recordLocked(UserLockedRecord userLockedRecord) {
		userDao.saveUserLockedRecord(userLockedRecord);
		//更新缓存数据
	}

	@Override
	public List<Role> findRoleByName(String userName) {
		return userDao.findRoleByName(userName);
	}

	@Override
	public List<User> findAll() {
		
		return userDao.findAll();
	}

	@Override
	public void changeUserEnable(User user) {
		userDao.changeUserEnable(user);
	}

}
