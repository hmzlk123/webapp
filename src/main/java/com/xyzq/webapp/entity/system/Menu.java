package com.xyzq.webapp.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

/**
 * Package: com.xyzq.webapp.entity.system
 * Description： 菜单实体
 * Author: linkan
 * Date: Created in 2019/8/1 14:55
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    private String menuId;			//菜单编号
    private String parentId;		//父节点编号
    private String name;			//菜单名称
    private int isChild;			//是否子节点，0－否，1－是。
    private String url;				//菜单链接
    private String target;			//指向类型
    private int relative;			//是否相对路径，0－否，1－是。
    private String icon;			//图标
    private int seq;				//序列
    private int enable;				//是否可用，0－否，1－是。
    private int visible;			//是否可见，0－否，1－是。
    private Date createTime;		//创建时间
    private Date modifyTime;		//修改时间
    private List<Menu> childList;	//子节点菜单
}
