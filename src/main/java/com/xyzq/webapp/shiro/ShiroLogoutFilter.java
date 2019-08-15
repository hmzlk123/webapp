package com.xyzq.webapp.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import com.xyzq.webapp.redis.RedisManager;

/**
 * Package: com.xyzq.webapp.shiro
 * Description： 自定义登出拦截器
 * Author: linkan
 * Date: Created in 2019/8/1 14:55
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
public class ShiroLogoutFilter extends LogoutFilter {
	
	private RedisManager redisManager;
	
    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    /**
     * Title: preHandle
     * Description: 自定义登出,登出之后,清理当前用户redis部分缓存信息
     * @param request  请求
     * @param response  响应
     * @return boolean
     * @throws Exception 异常
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        //登出操作 清除缓存  subject.logout() 可以自动清理缓存信息, 这些代码是可以省略的  这里只是做个笔记 表示这种方式也可以清除
        Subject subject = getSubject(request,response);
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        CustomRealm customRealm = (CustomRealm) securityManager.getRealms().iterator().next();
        PrincipalCollection principals = subject.getPrincipals();
        String userName = (String)subject.getPrincipal();
        customRealm.clearCache(principals);

        redisManager.del(KickoutSessionControlFilter.DEFAULT_KICKOUT_CACHE_KEY_PREFIX+userName);
        //登出
        subject.logout();

        //获取登出后重定向到的地址
        String redirectUrl = getRedirectUrl(request,response,subject);
        //重定向
        issueRedirect(request,response,redirectUrl);
        return false;
    }

}
