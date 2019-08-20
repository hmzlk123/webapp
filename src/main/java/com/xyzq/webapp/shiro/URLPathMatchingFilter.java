package com.xyzq.webapp.shiro;


import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
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
            // 如果没有登录, 直接返回true 进入登录流程
            return  true;
        }
        return false;
    }
}
