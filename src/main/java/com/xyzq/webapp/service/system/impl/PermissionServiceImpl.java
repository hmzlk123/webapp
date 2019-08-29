package com.xyzq.webapp.service.system.impl;

import com.xyzq.webapp.dao.system.PermissionDao;
import com.xyzq.webapp.entity.system.Permission;
import com.xyzq.webapp.service.system.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author linkan
 * @Package com.xyzq.webapp.service.system.impl
 * @Description 权限服务实现类
 * @date Created in 2019/8/29 23:30
 * @Copyright Copyright (c) 2019
 * @Version 0.0.1
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Resource
    private PermissionDao permissionDao;

    @Override
    public Permission findPermissionByUrl(String url) {
        return permissionDao.findPermissionByUrl(url);
    }
}
