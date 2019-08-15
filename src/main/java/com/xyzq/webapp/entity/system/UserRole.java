package com.xyzq.webapp.entity.system;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Package: com.xyzq.webapp.entity.system
 * Description： 用户角色实体
 * Author: linkan
 * Date: Created in 2019/8/1 13:52
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {
	private String resId;		//编号
	private String userId;		//用户编号
	private String roleId;		//角色编号
	private Date createTime;	//创建时间
	private Date modifyTime;	//修改时间
}
