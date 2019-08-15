package com.xyzq.webapp.dao.system;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.xyzq.webapp.entity.system.Menu;
import com.xyzq.webapp.entity.system.Role;

/**
 * Package: com.xyzq.webapp.dao.system
 * Description： 菜单Dao
 * Author: linkan
 * Date: Created in 2019/8/1 14:55
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
@Mapper
public interface MenuDao {
	/**
	 * Title: findByNameAndPassword
	 * Description:  根据用户名查找菜单
	 * @param userName	用户名
	 * @return List<Menu> 菜单列表
	 */
	List<Menu> findMenuByName(@Param("userName") String userName);
	
	/**
	 * Title: findRoleByUrl
	 * Description:  根据URL寻找角色
	 * @param url	路径
	 * @return List<Role> 角色列表
	 */
	List<Role> findRoleByUrl(@Param("url") String url);
}
