package com.xyzq.webapp.entity.system;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Package: com.xyzq.webapp.entity.system
 * Description： 用户实体
 * Author: linkan
 * Date: Created in 2019/8/1 13:52
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String userId;			//用户编号
	private String userName;		//登录名
	private String password;		//密码
	private byte[] profile_picture;	//头像
	private String name;			//姓名
	private int sex;				//性别 0-男性 1-女性
	private String address;			//地址
	private String email;			//邮箱
	private String department;		//部门
	private String introduction;	//个人简介
	private int enabled;			//是否可用：0 禁用,1 可用 默认1
	private int locked;				//是否锁定：0 未锁定，1 锁定 默认1
	private Date birthday;			//出生日期
	private Date createTime;		//创建时间
	private Date modifyTime;		//修改时间
}
