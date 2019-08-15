package com.xyzq.webapp.listener;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

/**
 * Package: com.xyzq.webapp.shiro
 * Description： 配置session监听器
 * Author: linkan
 * Date: Created in 2019/8/1 14:55
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
public class ShiroSessionListener implements SessionListener {
	

	//统计在线人数,juc包下线程安全自增
    private final AtomicInteger sessionCount = new AtomicInteger(0);

	/**   
	 * Title: onStart
	 * Description: 会话创建,在线人数+1
	 * @param session 会话
	 */
	@Override
	public void onStart(Session session) {
        sessionCount.incrementAndGet();
	}

	/**   
	 * Title: onStop
	 * Description:会话退出,在线人数-1
	 * @param session 会话
	 */
	@Override
	public void onStop(Session session) {
        sessionCount.decrementAndGet();
	}

	/**   
	 * title: onExpiration
	 * Description: 会话过期,在线人数-1
	 * @param session 会话
	 */
	@Override
	public void onExpiration(Session session) {
        sessionCount.decrementAndGet();
	}
	
	/**
	 * Title: getSessionCount
	 * Description: 获取在线人数使用
	 * @return AtomicInteger
	 */
    public AtomicInteger getSessionCount() {
        return sessionCount;
    }

}
