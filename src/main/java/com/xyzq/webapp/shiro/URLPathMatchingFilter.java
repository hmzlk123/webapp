package com.xyzq.webapp.shiro;


import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import static org.apache.shiro.SecurityUtils.*;

/**
 * @author linkan
 * @Package com.xyzq.webapp.shiro
 * @Description TODO
 * @date Created in 2019/8/20 22:23
 * @Copyright Copyright (c) 2019
 * @Version 0.0.1
 */
public class URLPathMatchingFilter extends PathMatchingFilter {

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        String requestURL = getPathWithinApplication(request);
        Subject subject = getSubject();
        if (!subject.isAuthenticated()){
            //如果没有登录, 直接返回true 进入登录流程
            return true;
        }

        if ("/index".equals(requestURL)){
            return true;
        }else {
            UnauthorizedException ex = new UnauthorizedException("当前用户没有访问路径" + requestURL + "的权限");
            subject.getSession().setAttribute("ex",ex);
            WebUtils.issueRedirect(request, response, "/unauthorized");
            return false;
        }
    }
}
