package com.xyzq.webapp.entity.system;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Package: com.xyzq.webapp.entity.system
 * Description： 角色实体
 * Author: linkan
 * Date: Created in 2019/8/1 13:52
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
	private String roleId;		//角色编号
	private String roleName;	//角色名称
	private String description;	//描述
	private String remark;		//备注
	private Date createTime;	//创建时间
	private Date modifyTime;	//修改时间
}
