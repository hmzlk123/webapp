package com.xyzq.webapp.configuration;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.xyzq.webapp.contants.ShiroConstant;
import com.xyzq.webapp.listener.ShiroSessionListener;
import com.xyzq.webapp.redis.RedisManager;
import com.xyzq.webapp.shiro.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Package com.xyzq.webapp.configuration
 * @Description shiro配置类
 * @author linkan
 * @date Created in 2019/8/19 23:38
 * @Copyright Copyright (c) 2019
 * @Version 0.0.1
 */
@Configuration
public class ShiroConfiguration {

    /**
     * @Description shiro过滤器
     * @author linkan
     * @date 2019/8/19 23:53
     * @param securityManager 安全管理类
     * @return org.apache.shiro.spring.web.ShiroFilterFactoryBean
     */
	@Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // setLoginUrl 如果不设置值，默认会自动寻找Web工程根目录下的"/login.jsp"页面 或 "/login" 映射
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //未授权页面跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");

        //自定义拦截器
        Map<String, Filter> filtersMap = new LinkedHashMap<>();
        //限制同一帐号同时在线的个数。
        filtersMap.put("kickout", kickoutSessionControlFilter());
        //配置自定义登出 覆盖 logout 之前默认的LogoutFilter
        filtersMap.put("logout", shiroLogoutFilter());
        //配置自定义url权限拦截器
        filtersMap.put("custompermission", shiroPermissionsFilter());
        
        shiroFilterFactoryBean.setFilters(filtersMap);
        // 设置拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/", "anon");
        //开放错误页面跳转
        filterChainDefinitionMap.put("/error*", "anon");
        //开放静态资源访问
        filterChainDefinitionMap.put("/assets/**", "anon");
        //开放druid界面访问
        filterChainDefinitionMap.put("/druid/**", "anon");
        //解决登录成功跳转/favicon.ico的问题
        filterChainDefinitionMap.put("/favicon.ico", "anon");

        //注销
        filterChainDefinitionMap.put("/logout", "logout");


        filterChainDefinitionMap.put("/user/**", "custompermission");
        filterChainDefinitionMap.put("/role/**", "custompermission");
        filterChainDefinitionMap.put("/menu/**", "custompermission");

        //其余接口一律拦截
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
        filterChainDefinitionMap.put("/**", "authc,kickout");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
	}

	/**
	 * @Description 注入 securityManager
	 * @author linkan
	 * @date 2019/8/19 23:53
	 * @return org.apache.shiro.mgt.SecurityManager
	 */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        //自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        //设置realm.
        securityManager.setRealm(customRealm()); 
        return securityManager;
    }

    /**
     * @Description 注入customRealm
     * @author linkan
     * @date 2019/8/19 23:53
     * @return com.xyzq.webapp.shiro.CustomRealm
     */
    @Bean
    public CustomRealm customRealm() {
    	CustomRealm customRealm = new CustomRealm();
        //配置自定义密码比较器
    	customRealm.setCredentialsMatcher(retryLimitHashedCredentialsMatcher());
        return customRealm;
    }

    /**
     * @Description 让某个实例的某个方法的返回值注入为Bean的实例
     * @author linkan
     * @date 2019/8/19 23:54
     * @return org.springframework.beans.factory.config.MethodInvokingFactoryBean
     */
    @Bean
    public MethodInvokingFactoryBean getMethodInvokingFactoryBean(){
        MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(securityManager());
        return factoryBean;
    }    

    /**
     * @Description 清除缓存
     * @author linkan
     * @date 2019/8/19 23:54
     * @return com.xyzq.webapp.shiro.RedisCacheManager
     */
    @Bean
    public RedisCacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //redis中针对不同用户缓存
        redisCacheManager.setPrincipalIdFieldName("userName");
        //用户权限信息缓存时间
        redisCacheManager.setExpire(ShiroConstant.REDIS_CACHE_EXPIRE);
        return redisCacheManager;
    }

    /**
     * @Description 注入redis
     * @author linkan
     * @date 2019/8/19 23:54
     * @return com.xyzq.webapp.redis.RedisManager
     */
    @Bean
    public RedisManager redisManager() {
        return new RedisManager();
    }

    /**
     * @Description 配置session监听
     * @author linkan
     * @date 2019/8/19 23:55
     * @return com.xyzq.webapp.listener.ShiroSessionListener
     */
    @Bean("sessionListener")
    public ShiroSessionListener sessionListener(){
        return new ShiroSessionListener();
    }

    /**
     * @Description 配置保存sessionId的cookie,默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid
     * @author linkan
     * @date 2019/8/19 23:55
     * @return org.apache.shiro.web.servlet.SimpleCookie
     */
    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie(){
        //这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie(ShiroConstant.COOKIE_NAME);
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(ShiroConstant.COOKIE_MAX_AGE);
        return simpleCookie;
    }

    /**
     * @Description 配置会话管理器，设定会话超时及保存
     * @author linkan
     * @date 2019/8/19 23:56
     * @return org.apache.shiro.session.mgt.SessionManager
     */
    @Bean("sessionManager")
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<>();
        //配置监听
        listeners.add(sessionListener());
        sessionManager.setSessionListeners(listeners);
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setCacheManager(cacheManager());

        //全局会话超时时间(单位毫秒),默认30分钟
        sessionManager.setGlobalSessionTimeout(ShiroConstant.GLOBAL_SESSION_TIMEOUT);
        //是否开启删除无效的session对象 默认为true
        sessionManager.setDeleteInvalidSessions(true);
        //是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        //暂时设置为 5秒 用来测试
        sessionManager.setSessionValidationInterval(ShiroConstant.SESSION_VALIDATION_INTERVAL);
        //取消url 后面的 JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;

    }

    /**
     * @Description 注入sessionDao
     * @author linkan
     * @date 2019/8/19 23:56
     * @return org.apache.shiro.session.mgt.eis.SessionDAO
     */
    @Bean
    public SessionDAO sessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        //session在redis中的保存时间,最好大于session会话超时时间
        redisSessionDAO.setExpire(ShiroConstant.REDIS_SESSION_EXPIRE);
        return redisSessionDAO;
    }

    /**
     * @Description 在线人数并发控制
     * @author linkan
     * @date 2019/8/19 23:56
     * @return com.xyzq.webapp.shiro.KickoutSessionControlFilter
     */
    @Bean
    public KickoutSessionControlFilter kickoutSessionControlFilter() {
        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        //用于根据会话ID，获取会话进行踢出操作的；
        kickoutSessionControlFilter.setSessionManager(sessionManager());
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        kickoutSessionControlFilter.setRedisManager(redisManager());
        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；
        kickoutSessionControlFilter.setKickoutAfter(false);
        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        kickoutSessionControlFilter.setMaxSession(ShiroConstant.KICKOUT_MAX_SESSION);
        //被踢出后重定向到的地址；
        kickoutSessionControlFilter.setKickoutUrl("/login?kickout=1");
        return kickoutSessionControlFilter;
    }

    /**  
     * @Description 访问权限拦截器
     * @author linkan 
     * @date 2019/8/21 22:37
     * @return com.xyzq.webapp.shiro.URLPathMatchingFilter  
     */  
    @Bean
    public URLPathMatchingFilter urlPathMatchingFilter() {
        return new URLPathMatchingFilter();
    }

    /**
     * @Description 配置自定义登出拦截器
     * @author linkan
     * @date 2019/8/19 23:57
     * @return com.xyzq.webapp.shiro.ShiroLogoutFilter
     */
    private ShiroLogoutFilter shiroLogoutFilter(){
        ShiroLogoutFilter shiroLogoutFilter = new ShiroLogoutFilter();
        //配置登出后重定向的地址，等出后配置跳转到登录接口
        shiroLogoutFilter.setRedirectUrl("/login");
        shiroLogoutFilter.setRedisManager(redisManager());
        return shiroLogoutFilter;
    }

    /**
     * @Description 注入自定义权限过滤器
     * @author linkan
     * @date 2019/8/30 16:11
     * @return com.xyzq.webapp.shiro.ShiroPermissionsFilter
     */
    @Bean
    public ShiroPermissionsFilter shiroPermissionsFilter(){
        return new ShiroPermissionsFilter();
    }

    /**
     * @Description thymeleaf页面使用shiro标签控制按钮是否显示
     * @author linkan
     * @date 2019/8/19 23:57
     * @return at.pollux.thymeleaf.shiro.dialect.ShiroDialect
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    /**
     * @Description 配置会话ID生成器
     * @author linkan
     * @date 2019/8/19 23:57
     * @return org.apache.shiro.session.mgt.eis.SessionIdGenerator
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * @Description 开启shiro 注解模式,可以在controller中的方法前加上注解,如 @RequiresPermissions("userInfo:add")
     * @author linkan
     * @date 2019/8/19 23:58
     * @param securityManager 安全管理类
     * @return org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * @Description 配置Shiro生命周期处理器
     * @author linkan
     * @date 2019/8/19 23:58
     * @return org.apache.shiro.spring.LifecycleBeanPostProcessor
     */
    @Bean
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        return new DefaultAdvisorAutoProxyCreator();
    }

    /**
     * @Description 配置密码比较器
     * @author linkan
     * @date 2019/8/19 23:58
     * @return com.xyzq.webapp.shiro.RetryLimitHashedCredentialsMatcher
     */
    @Bean("credentialsMatcher")
    public RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher(){
        RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher = 
        		new RetryLimitHashedCredentialsMatcher();
        retryLimitHashedCredentialsMatcher.setRedisManager(redisManager());

        //如果密码加密,可以打开下面配置
        //加密算法的名称
        //retryLimitHashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //配置加密的次数
        //retryLimitHashedCredentialsMatcher.setHashIterations(1024);
        //是否存储为16进制
        //retryLimitHashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);

        return retryLimitHashedCredentialsMatcher;
    }

}
