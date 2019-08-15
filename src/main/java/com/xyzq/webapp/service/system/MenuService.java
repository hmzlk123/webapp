package com.xyzq.webapp.service.system;

import java.util.List;

import com.xyzq.webapp.entity.system.Menu;
import com.xyzq.webapp.entity.system.Role;

/**
 * Package: com.xyzq.webapp.service.system
 * Description： 菜单服务接口
 * Author: linkan
 * Date: Created in 2019/8/1 14:55
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
public interface MenuService {
	
	/**
	 * Title: findMenuByName
	 * Description:  根据用户名查找菜单
	 * @param userName 用户名
	 * @return List<Menu> 菜单信息列表
	 */
	List<Menu> findMenuByName(String userName);
	
	/**
	 * Title: findRoleByUrl
	 * Description:  根据URL寻找角色
	 * @param url url
	 * @return List<Role> 角色信息列表
	 */
	List<Role> findRoleByUrl(String url);
}
