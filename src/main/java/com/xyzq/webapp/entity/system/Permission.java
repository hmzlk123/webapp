package com.xyzq.webapp.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Package com.xyzq.webapp.entity.system
 * @Description 权限实体
 * @author linkan
 * @date Created in 2019/8/29 22:49
 * @Copyright Copyright (c) 2019
 * @Version 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    private String permissionId;               //UUID
    private String parentId;                   //上级菜单
    private int isChild;                       //是否子节点，0－否，1－是。
    private int type;                          //权限类型 1-url;2-function
    private String url;                        //url
    private String permissionCode;             //权限代码
    private String description;                //权限描述
    private Date createTime;                   //建创时间
    private Date modifyTime;                   //修改时间
}
