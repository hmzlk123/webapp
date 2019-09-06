package com.xyzq.webapp.service.system;

import com.xyzq.webapp.entity.system.Role;
import java.util.List;

/**
 * @Package com.xyzq.webapp.service.system
 * @Description 角色服务接口
 * @author linkan
 * @date Created in 2019/8/29 23:29
 * @Copyright Copyright (c) 2019
 * @Version 0.0.1
 */
public interface RoleService {

    /**
     * @Description 根据菜单url查找角色列表
     * @author linkan
     * @date 2019/9/5 14:35
     * @param url 菜单url
     * @return java.util.List<com.xyzq.webapp.entity.system.Role>
     */
    List<Role> findRoleByMenuUrl(String url);
}
