package com.xyzq.webapp.shiro;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyzq.webapp.entity.system.Role;
import com.xyzq.webapp.entity.system.User;
import com.xyzq.webapp.service.system.UserService;

/**
 * Package: com.xyzq.webapp.shiro
 * Description：自定义 realm 类
 * Author: linkan
 * Date: Created in 2019/8/1 14:55
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
public class CustomRealm extends AuthorizingRealm{
	private UserService userService;

    @Autowired
    private void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Title: doGetAuthorizationInfo
     * Description: 自定义用户权限认证 
     * @param principals 凭证
     * @return AuthorizationInfo
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//获取用户名
        String userName = (String) SecurityUtils.getSubject().getPrincipal();
        //获取用户角色
        List<Role> roleList = userService.findRoleByName(userName);
        
        //添加角色
        SimpleAuthorizationInfo authorizationInfo =  new SimpleAuthorizationInfo();
        for (Role role : roleList) {
        	authorizationInfo.addRole(role.getRoleName());
		}
        authorizationInfo.addStringPermission("user:view");
        //authorizationInfo.addStringPermission("user:change");
		return authorizationInfo;
	}
	/**
	 * Title: doGetAuthenticationInfo
	 * Description: 自定义用户身份认证
	 * @param authenticationToken 身份标识
	 * @return AuthenticationInfo
	 * @throws AuthenticationException 认证失败异常
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) 
			throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = token.getUsername();
        User user = userService.findByName(userName);
        
        if(user==null)
        	throw new UnknownAccountException();		//用户不存在
        
    	String password = user.getPassword();
    	int enabled = user.getEnabled();
    	int locked = user.getLocked();

    	if(enabled == 0)		
    		throw new DisabledAccountException();		//禁用的账户
    	if(locked == 1)  		
    		throw new LockedAccountException();			//锁定的账户
        
        return new SimpleAuthenticationInfo(userName, password, getName());

	}

	/**
	 * 
	 * Title: clearCachedAuthorizationInfo   
	 * Description:  重写方法,清除当前用户的的 授权缓存
	 * @param principals 凭证
	 */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * Title: clearCachedAuthenticationInfo   
     * Description:  重写方法，清除当前用户的 认证缓存
     * @param principals 凭证
     */
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    /**
     * Title: clearCache
     * Description:  清楚缓存
     * @param principals 凭证
     */
    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    /**
     * Title: clearAllCachedAuthorizationInfo
     * Description: 自定义方法：清除所有 授权缓存
     */
    private void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * Title: clearAllCachedAuthenticationInfo
     * Description:自定义方法：清除所有 认证缓存
     */
    private void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 
     * Title: clearAllCache
     * Description: 自定义方法：清除所有的  认证缓存  和 授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }


}
