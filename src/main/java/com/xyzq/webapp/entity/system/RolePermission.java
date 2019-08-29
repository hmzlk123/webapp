package com.xyzq.webapp.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Package com.xyzq.webapp.entity.system
 * @Description 角色权限类
 * @author linkan
 * @date Created in 2019/8/29 22:52
 * @Copyright Copyright (c) 2019
 * @Version 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermission {
    private String resId;              //编号 UUID
    private String roleId;             //角色编号
    private String permissionId;       //权限编号
    private Date createTime;           //建创时间
    private Date modifyTime;           //修改时间

}
