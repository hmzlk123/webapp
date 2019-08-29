package com.xyzq.webapp.service.system;

import com.xyzq.webapp.entity.system.Permission;

/**
 * @Package com.xyzq.webapp.service.system
 * @Description 权限服务接口
 * @author linkan
 * @date Created in 2019/8/29 23:29
 * @Copyright Copyright (c) 2019
 * @Version 0.0.1
 */
public interface PermissionService {

    /**
     * @Description 根据URL获取权限信息
     * @author linkan
     * @date 2019/8/29 23:26
     * @param url 请求路径
     * @return com.xyzq.webapp.entity.system.Permission
     */
    Permission findPermissionByUrl(String url);
}
