package com.xyzq.webapp.service.system.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xyzq.webapp.dao.system.MenuDao;
import com.xyzq.webapp.entity.system.Menu;
import com.xyzq.webapp.entity.system.Role;
import com.xyzq.webapp.service.system.MenuService;

/**
 * Package: com.xyzq.webapp.service.system.impl
 * Description： 菜单服务接口实现类
 * Author: linkan
 * Date: Created in 2019/8/1 14:55
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
@Service
public class MenuServiceImpl implements MenuService {
	
	@Resource
	private MenuDao menuDao;
	
	@Override
	public List<Menu> findMenuByName(String userName) {
		return menuDao.findMenuByName(userName);
	}

	@Override
	public List<Role> findRoleByUrl(String url) {
		return menuDao.findRoleByUrl(url);
	}

}
