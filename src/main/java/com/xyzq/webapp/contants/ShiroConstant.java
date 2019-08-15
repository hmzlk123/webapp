package com.xyzq.webapp.contants;



/**
 * Package: com.xyzq.webapp.contants
 * Description： shiro常量类
 * Author: linkan
 * Date: Created in 2019/8/1 14:55
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
public class ShiroConstant {
	
	public final static int REDIS_CACHE_EXPIRE = 20000; 			//用户权限信息缓存时间
	public final static String COOKIE_NAME = "sid"; 				//cookie名称
	public final static int COOKIE_MAX_AGE = -1; 					//cookie过期时间，maxAge=-1表示浏览器关闭时失效此Cookie
	public final static int GLOBAL_SESSION_TIMEOUT = 1800000;		//全局会话超时时间(单位毫秒),默认30分钟
	public final static int SESSION_VALIDATION_INTERVAL = 3600000;	//会话验证时长
	public final static int REDIS_SESSION_EXPIRE = 12000;			//session在redis中的保存时间,最好大于session会话超时时间
	public final static int KICKOUT_MAX_SESSION = 1;				//同时最大登录人数
	public final static int RETRY_LIMIT_REDIS_TIMEOUT = 43200;		//尝试登陆记录数在redis中超时时间（单位秒）
	public final static int RETRY_LIMIT_COUNT = 2;					//允许尝试登陆错误次数
	public final static int RETRY_LIMIT_LOCK_TIMEOUT = 24;			//尝试登陆错误账户锁定时间(单位小时)
}
