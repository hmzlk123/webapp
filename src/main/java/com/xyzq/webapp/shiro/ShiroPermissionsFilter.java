package com.xyzq.webapp.shiro;

import com.alibaba.druid.util.StringUtils;
import com.xyzq.webapp.entity.system.Permission;
import com.xyzq.webapp.service.system.PermissionService;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Package com.xyzq.webapp.shiro
 * @author linkan
 * @Description 权限校验过滤器
 * @date Created in 2019/8/27 23:12
 * @Copyright Copyright (c) 2019
 * @Version 0.0.1
 */
public class ShiroPermissionsFilter extends PermissionsAuthorizationFilter {

    private static final Logger logger = LoggerFactory.getLogger(ShiroPermissionsFilter.class);

    @Autowired
    private PermissionService permissionService;

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {

        // 记录日志
        Subject subject = this.getSubject(request, response);

        //获取url
        String url = this.getPathWithinApplication(request);
        //根据url获取permission
        Permission permission = permissionService.findPermissionByUrl(url);
        if(permission == null){
            return true;
        }
        System.out.println(subject.isPermitted(permission.getPermissionCode()));
        return  subject.isPermitted(permission.getPermissionCode());
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        logger.info("----------权限控制-------------");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String header = httpServletRequest.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(header);
        if (isAjax) {//如果是ajax返回指定格式数据
            logger.info("----------AJAX请求拒绝-------------");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            //返回禁止访问json字符串
            httpServletResponse.getWriter().write("{\"success\":false,\"msg\":\"你没有权限\"}");
        } else {//如果是普通请求进行重定向
            logger.info("----------普通请求拒绝-------------");
            httpServletResponse.sendRedirect("error/401");
        }
        return false;
    }
}
