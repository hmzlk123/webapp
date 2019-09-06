package com.xyzq.webapp.service.system;

import java.util.List;

import com.xyzq.webapp.entity.system.Permission;
import com.xyzq.webapp.entity.system.Role;
import com.xyzq.webapp.entity.system.User;
import com.xyzq.webapp.entity.system.UserLockedRecord;
import org.apache.ibatis.annotations.Param;

/**
 * Package: com.xyzq.webapp.service.system
 * Description： 用户服务接口
 * Author: linkan
 * Date: Created in 2019/8/1 14:55
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
public interface UserService {
	/**
	 * Title: findByNameAndPassword
	 * Description:  根据用户名和密码寻找用户
	 * @param userName 用户名
	 * @param password 密码
	 * @return User 用户信息
	 */
	User findByNameAndPassword(String userName, String password);

	/**
	 * @Description 根据用户编号查找用户
	 * @author linkan
	 * @date 2019/9/6 14:45
	 * @param userId  用户编号
	 * @return com.xyzq.webapp.entity.system.User
	 */
	User findById(String userId);
	
	/**
	 * Title: findByName
	 * Description: 根据用户查找用户
	 * @param userName 用户名
	 * @return User 用户信息
	 */
	User findByName(String userName);
	
	/**
	 * Title: findAll
	 * Description: 根据用户查找用户
	 * @return List<User> 用户信息列表
	 */
	List<User> findAll();
	
	/**
	 * Title: recordLocked
	 * Description:  记录用户锁定
	 * @param userLockedRecord 用户锁定信息
	 */
	void recordLocked(UserLockedRecord userLockedRecord);
	
	/**
	 * Title: findRoleByName
	 * Description:  根据用户名查询角色
	 * @param userName 用户名
	 * @return List<Role> 角色信息列表
	 */
	List<Role> findRoleByName(String userName);

	/**
	 * Title: findPermissionByName
	 * Description: 根据用户名查询权限
	 * @param userName 用户名
	 * @return List<Permission> 角色列表
	 */
	List<Permission> findPermissionByName(String userName);
	
	/**
	 * Title: changeUserEnable
	 * Description:  更改用户禁用状态
	 * @param user 用户信息
	 */
	void changeUserEnable(User user);
}
