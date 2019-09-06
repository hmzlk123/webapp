package com.xyzq.webapp.dao.system;

import com.xyzq.webapp.entity.system.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Package com.xyzq.webapp.dao.system
 * @Description 角色Dao
 * @author linkan
 * @date Created in 2019/9/5 14:35
 * @Copyright Copyright (c) 2019
 * @Version 0.0.1
 */
@Mapper
public interface RoleDao {

    /**
     * @Description 根据菜单url查找角色列表
     * @author linkan
     * @date 2019/9/5 14:35
     * @param url 菜单url
     * @return java.util.List<com.xyzq.webapp.entity.system.Role>
     */
    List<Role> findRoleByMenuUrl(@Param("url") String url);
}
