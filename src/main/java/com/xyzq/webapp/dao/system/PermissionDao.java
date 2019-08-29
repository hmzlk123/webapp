package com.xyzq.webapp.dao.system;

import com.xyzq.webapp.entity.system.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Package com.xyzq.webapp.dao.system
 * @Description 权限Dao
 * @author linkan
 * @date Created in 2019/8/29 23:19
 * @Copyright Copyright (c) 2019
 * @Version 0.0.1
 */
@Mapper
public interface PermissionDao {
    /**
     * @Description 根据URL获取权限信息
     * @author linkan
     * @date 2019/8/29 23:26
     * @param url 请求路径
     * @return com.xyzq.webapp.entity.system.Permission
     */
    Permission findPermissionByUrl(@Param("url") String url);
}
