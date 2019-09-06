package com.xyzq.webapp.service.system.impl;

import com.xyzq.webapp.dao.system.RoleDao;
import com.xyzq.webapp.entity.system.Role;
import com.xyzq.webapp.service.system.RoleService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Package com.xyzq.webapp.service.system.impl
 * @Description 角色服务实现类
 * @author linkan
 * @date Created in 2019/9/5 14:59
 * @Copyright Copyright (c) 2019
 * @Version 0.0.1
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleDao roleDao;

    @Override
    public List<Role> findRoleByMenuUrl(String url) {
        return roleDao.findRoleByMenuUrl(url);
    }
}
