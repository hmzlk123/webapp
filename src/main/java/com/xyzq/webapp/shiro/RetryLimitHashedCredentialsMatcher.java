package com.xyzq.webapp.shiro;


import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyzq.webapp.contants.ShiroConstant;
import com.xyzq.webapp.entity.system.User;
import com.xyzq.webapp.entity.system.UserLockedRecord;
import com.xyzq.webapp.redis.RedisManager;
import com.xyzq.webapp.service.system.UserService;
import com.xyzq.webapp.utils.DateUtils;

/**
 * Package: com.xyzq.webapp.shiro
 * Description： 登录错误次数限制
 * Author: linkan
 * Date: Created in 2019/8/1 14:55
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
public class RetryLimitHashedCredentialsMatcher extends SimpleCredentialsMatcher {

    private static final Logger logger = LoggerFactory.getLogger(SimpleCredentialsMatcher.class);

    private static final String DEFAULT_RETRYLIMIT_CACHE_KEY_PREFIX = "webapp:shiro:cache:retrylimit:";

    @Autowired
    private UserService userService;
    private RedisManager redisManager;

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    private String getRedisKickoutKey(String username) {
        return DEFAULT_RETRYLIMIT_CACHE_KEY_PREFIX + username;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        //获取用户名
        String userName = (String)token.getPrincipal();
        //获取用户登录次数
        AtomicInteger retryCount = (AtomicInteger)redisManager.get(getRedisKickoutKey(userName));
        if (retryCount == null) {
            //如果用户没有登陆过,登陆次数加1 并放入缓存
            retryCount = new AtomicInteger(0);
        }
        //当用户尝试登陆错误数量达到阙值
        if (retryCount.incrementAndGet() > ShiroConstant.RETRY_LIMIT_COUNT) {
            //如果用户登陆失败次数大于设定次数次 抛出锁定用户异常  并修改数据库字段
            User user = userService.findByName(userName);
            if(user != null) {
            	if (user.getLocked()==0){
            		UserLockedRecord userLockedRecord = new UserLockedRecord();
        			userLockedRecord.setUserId(user.getUserId());
        			
        			Date lockedTime = new Date();
        			Date unlockedTime = DateUtils.addHours(ShiroConstant.RETRY_LIMIT_LOCK_TIMEOUT);
        			
        			userLockedRecord.setLockedTime(lockedTime);
        			userLockedRecord.setUnlockedTime(unlockedTime);
        			userService.recordLocked(userLockedRecord);
        			logger.info("用户名:["+ user.getUserName()+"],密码输入错误达到"+(ShiroConstant.RETRY_LIMIT_COUNT+1)+"次,账户锁定");
        			throw new ExcessiveAttemptsException();
                }else {
                	logger.info("锁定用户:[" + user.getUserName()+"]");
                    //抛出用户锁定异常
                    throw new LockedAccountException();
                }
            }else {
            	logger.info("不存在的用户名:["+userName+"]");
            	throw new UnknownAccountException();
            }
        }
        //判断用户账号和密码是否正确
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            //如果正确,从缓存中将用户登录计数 清除
            redisManager.del(getRedisKickoutKey(userName));
        }else{
            redisManager.set(getRedisKickoutKey(userName), retryCount,ShiroConstant.RETRY_LIMIT_REDIS_TIMEOUT);
        }
        return matches;
    }
}