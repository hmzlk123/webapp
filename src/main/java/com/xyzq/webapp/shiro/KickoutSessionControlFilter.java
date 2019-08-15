package com.xyzq.webapp.shiro;

import com.xyzq.webapp.redis.RedisManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Package: com.xyzq.webapp.shiro
 * Description：登陆并发人数控制过滤器
 * Author: linkan
 * Date: Created in 2019/8/1 14:55
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
public class KickoutSessionControlFilter  extends AccessControlFilter{

	private static final Logger logger = LoggerFactory.getLogger(KickoutSessionControlFilter.class);

    private String kickoutUrl;                  //踢出后到的地址
    private boolean kickoutAfter = false;       //踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
    private int maxSession = 1;                 //同一个帐号最大会话数 默认1
    private SessionManager sessionManager;

    private RedisManager redisManager;

    public static final String DEFAULT_KICKOUT_CACHE_KEY_PREFIX = "webapp:shiro:cache:kickout:";
    private String keyPrefix = DEFAULT_KICKOUT_CACHE_KEY_PREFIX;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    private String getRedisKickoutKey(String username) {
        return this.keyPrefix + username;
    }

    /**
     * 
     * Title: isAccessAllowed   
     * Description:   是否允许访问,返回true表示允许 
     * @param request   请求
     * @param response  响应
     * @param mappedValue   权限map
     * @return boolean
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }


    /**
     * Title: onAccessDenied   
     * Description:   表示访问拒绝时是否自己处理，如果返回true表示自己不处理且继续拦截器链执行，
     *                返回false表示自己已经处理了（比如重定向到另一个页面）。
     * @param request   请求
     * @param response  响应
     * @return boolean
     * @throws Exception 异常
     */
	@Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if(!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }

        Session session = subject.getSession();
        //这里获取的User是实体 因为我在 自定义ShiroRealm中的doGetAuthenticationInfo方法中
        //new SimpleAuthenticationInfo(user, password, getName()); 传的是 User实体 所以这里拿到的也是实体,如果传的是userName 这里拿到的就是userName
        String userName = (String)subject.getPrincipal();
        Serializable sessionId = session.getId();

        // 初始化用户的队列放到缓存里
        Deque<Serializable> deque = (Deque<Serializable>) redisManager.get(getRedisKickoutKey(userName));
        if(deque == null || deque.size()==0) {
            deque = new LinkedList<>();
        }

        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if(!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            deque.push(sessionId);
        }

        //如果队列里的sessionId数超出最大会话数，开始踢人
        while(deque.size() > maxSession) {
            Serializable kickoutSessionId;
            if(kickoutAfter) { //如果踢出后者
                kickoutSessionId = deque.removeFirst();
            } else { //否则踢出前者
                kickoutSessionId = deque.removeLast();
            }
            try {
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if(kickoutSession != null) {
                    //设置会话的kickout属性表示踢出了
                    kickoutSession.setAttribute("kickout", true);
                }
            } catch (Exception e) {//ignore exception
            	logger.error("ERROR", "Error found: ", e);
            }
        }

        redisManager.set(getRedisKickoutKey(userName), deque,1800);

        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickout") != null) {
            //会话被踢出了
            try {
                subject.logout();
            } catch (Exception e) {
            	logger.error("ERROR", "Error found: ", e);
            }
            WebUtils.issueRedirect(request, response, kickoutUrl);
            return false;
        }
        return true;
    }

}