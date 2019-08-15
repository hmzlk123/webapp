package com.xyzq.webapp.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xyzq.webapp.entity.DataTable;
import com.xyzq.webapp.entity.DataTableParameter;
import com.xyzq.webapp.entity.system.User;
import com.xyzq.webapp.service.system.UserService;
import com.xyzq.webapp.utils.DataTableUtils;


/**
 * Package: com.xyzq.webapp.controller
 * Description： 用户控制层
 * Author: linkan
 * Date: Created in 2019/8/1 14:55
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
@Controller
@RequestMapping("/um")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Title userList
	 * Description 进入用户列表界面
	 * @return String 页面
	 */
	@RequestMapping("")
	public String userList() {
		return "/system/userlist";
	}
	
	/**
	 * Title userList
	 * Description 查询首页滚动图配置信息列表
	 * @param jsonParam 查询参数
	 * @return userlist 用户列表
	 */
    @ResponseBody
    @RequestMapping("/userlist")
    public Object userlist(String jsonParam){
    	DataTableParameter dataTableParam = DataTableUtils.getDataTableParameterByJsonParam(jsonParam);
    	
    	StringBuffer orderBy = new StringBuffer();
    	List<String> iSortColsName = dataTableParam.getiSortColsName();
    	List<String> sSortDirs = dataTableParam.getsSortDirs();
    	for(int i=0; i<iSortColsName.size(); i++){
            String sortColName = iSortColsName.get(i);
            if("userName".equals(sortColName)) {
            	sortColName = "user_name";
            }
            String sortDir = sSortDirs.get(+i);
            if(StringUtils.isEmpty(orderBy))
            	orderBy.append(sortColName).append(" ").append(sortDir);
            else
            	orderBy.append(",").append(sortColName).append(" ").append(sortDir);
        }
    	
    	
    	PageHelper.startPage(dataTableParam.getiDisplayStart()/dataTableParam.getiDisplayLength()+1, dataTableParam.getiDisplayLength(), orderBy.toString());
    	List<User> userList = userService.findAll();
    	
    	PageInfo<User> userPageList = new PageInfo<>(userList);
    	
    	DataTable<User> dt = new DataTable<>();
        int sEcho = dataTableParam.getsEcho()+1;
        dt.setData(userList);
        dt.setDraw(sEcho);
        dt.setRecordsTotal((int)userPageList.getTotal());
        dt.setRecordsFiltered((int)userPageList.getTotal());
        return JSONObject.toJSONString(dt);
    }

	/**
	 * Title changeUserEnable
	 * Description 更改用户禁用状态
	 * @param user 用户信息
	 * @return User 用户信息
	 */
    @ResponseBody
    @RequestMapping("/changeenable")
    public User changeUserEnable(@RequestBody User user){
    	if(user.getEnabled()==0)
    		user.setEnabled(1);
    	else
    		user.setEnabled(0);
    	userService.changeUserEnable(user);
        return user;
    }
    
}
