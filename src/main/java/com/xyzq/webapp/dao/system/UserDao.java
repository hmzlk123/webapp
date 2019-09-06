package com.xyzq.webapp.dao.system;

import java.util.List;

import com.xyzq.webapp.entity.system.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.xyzq.webapp.entity.system.Role;
import com.xyzq.webapp.entity.system.User;
import com.xyzq.webapp.entity.system.UserLockedRecord;

/**
 * Package: com.xyzq.webapp.dao.system
 * Description： 用户Dao
 * Author: linkan
 * Date: Created in 2019/8/1 14:55
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
@Mapper
public interface UserDao {
	/**
	 * Title: findByNameAndPassword
	 * Description:  根据用户名和密码查找用户
	 * @param userName	用户名
	 * @param password	密码
	 * @return	User 用户信息
	 */
	User findByNameAndPassword(@Param("userName") String userName, @Param("password") String password);

	/**
	 * @Description 根据用户编号查找用户
	 * @author linkan
	 * @date 2019/9/6 14:45
	 * @param userId  用户编号
	 * @return com.xyzq.webapp.entity.system.User
	 */
	User findById(@Param("userId") String userId);

	/**
	 * Title: findAll
	 * Description:  查找全部用户
	 * @return	List<User> 用户列表
	 */
	List<User> findAll();
	
	/**
	 * Title: findByName
	 * Description:  根据用户查找用户
	 * @param userName 用户名
	 * @return User 用户信息
	 */
	User findByName(@Param("userName") String userName);
	
	/**
	 * Title: saveUserLockedRecord
	 * Description: 记录用户锁定
	 * @param userLockedRecord 用户锁定记录信息
	 */
	void saveUserLockedRecord(UserLockedRecord userLockedRecord);
	
	/**
	 * Title: findRoleByName
	 * Description: 根据用户名查询角色
	 * @param userName 用户名
	 * @return List<Role> 角色列表
	 */
	List<Role> findRoleByName(@Param("userName") String userName);

	/**
	 * Title: findPermissionByName
	 * Description: 根据用户名查询权限
	 * @param userName 用户名
	 * @return List<Permission> 角色列表
	 */
	List<Permission> findPermissionByName(@Param("userName") String userName);
	
	/**
	 * Title: changeUserEnable
	 * Description: 根据用户名查询角色
	 * @param user 用户信息
	 */
	void changeUserEnable(User user);
}
