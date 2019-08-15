package com.xyzq.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.xyzq.webapp.contants.ShiroConstant;
import com.xyzq.webapp.entity.system.Menu;
import com.xyzq.webapp.entity.system.User;
import com.xyzq.webapp.service.system.MenuService;
import com.xyzq.webapp.service.system.UserService;

/**
 * @Package com.xyzq.webapp.controller
 * @Description 首页控制层
 * @author linkan
 * @date Created in 2019/8/1 14:55
 * @Copyright Copyright (c) 2019
 * @version 0.0.1
 */
@Controller
public class IndexController {
	
	private final UserService userService;
	
	private final MenuService menuService;

    public IndexController(UserService userService, MenuService menuService) {
        this.userService = userService;
        this.menuService = menuService;
    }
	private static Logger logger = LoggerFactory.getLogger(IndexController.class);

    /**
	 * @Description 进入登录页面
	 * @return String 页面
	 */
	@RequestMapping("/login")
	public String login() {
		logger.debug("进入登录页面");
		return "login";
	}

    /**
     * @Description 跳转403页面
     * @author linkan
     * @date 2019/8/14 14:35
     * @return java.lang.String
     */
	@RequestMapping("/error-403")
	public String error403() {
		return "error/404";
	}
	
    /**
     * Title enter
     * Description 跳转到登录页面
     */
    @RequestMapping("/")
    public String enter(){
        return "redirect:/login";
    }


    /**
     * @Description  登录页面验证
     * @author linkan
     * @date 2019/8/14 14:36
     * @param kickout 踢出标识
     * @param model  模型
     * @return java.lang.String
     */
    @RequestMapping(value = "login",method = {RequestMethod.GET})
    public String login(@RequestParam(value = "kickout", required = false) String kickout,
    		Model model) {
		logger.debug("进入登录页面");
    	Object principal = SecurityUtils.getSubject().getPrincipal();
   
        // 如果已经登录，则跳转到首页
        if(principal != null){
            return "redirect:/index";
        }
        if("1".equals(kickout)) {
        	model.addAttribute("error", "该用户已经被踢出");
        }
        return "login";
    }
    
    /**
     * @Description 系统首页
     * @author linkan
     * @date 2019/8/14 14:39
     * @param mv  mv
     * @return org.springframework.web.servlet.ModelAndView
     */
	@RequestMapping("/index")
	public ModelAndView index(ModelAndView mv) {
		String userName = (String)SecurityUtils.getSubject().getPrincipal();
		//获取用户信息
		User user = userService.findByName(userName);
		user.setPassword(null);
		
		//获取菜单信息
		List<Menu> menuList = menuService.findMenuByName(userName);
		List<Menu> treeMenuList = new ArrayList<>(); // 树递归
		for (Menu menu : menuList) {
			if ("0".equals(menu.getParentId())) {
				treeMenuList.add(menu);
			}
		}
		
		for (Menu menu : treeMenuList) {
			menu.setChildList(getChild(menu.getMenuId(), menuList));
		}
		
		mv.addObject("userinfo", user);	
		mv.addObject("menulist", treeMenuList);	
		mv.setViewName("index");
		return mv;
	}
	
    /**
     * @Description 认证失败处理，并返回登录页
     * @author linkan
     * @date 2019/8/14 14:39
     * @param request request
     * @param model  model
     * @return java.lang.String
     */
	@RequestMapping(value = "login",method = {RequestMethod.POST})
	public String loginError(HttpServletRequest request, Model model) {
	    String errorName = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
	    if(UnknownAccountException.class.getName().equals(errorName)) {
			model.addAttribute("error", "用户名不存在");
		} else if(IncorrectCredentialsException.class.getName().equals(errorName)) {
			model.addAttribute("error", "用户名/密码错误");
		} else if(ExcessiveAttemptsException.class.getName().equals(errorName)) {
			model.addAttribute("error", "用户密码输入错误达到"+(ShiroConstant.RETRY_LIMIT_COUNT+1)+"次,账户锁定");
		} else if(LockedAccountException.class.getName().equals(errorName)) {
			model.addAttribute("error", "用户已经被锁定");
		} else if(DisabledAccountException.class.getName().equals(errorName)) {
			model.addAttribute("error", "用户已经被禁用");
		} else if(AccountException.class.getName().equals(errorName)) {
			model.addAttribute("error", "用户认证失败");
		}
	    return "login";
	}
	
    /**
     * @Description 递归获得子菜单
     * @author linkan
     * @date 2019/8/14 14:36
     * @param id  父菜单ID
     * @param rootMenu 父菜单数据
     * @return java.util.List<com.xyzq.webapp.entity.system.Menu>
     */
	private List<Menu> getChild(String id,List<Menu> rootMenu){
		// 子菜单
	    List<Menu> childList = new ArrayList<>();
	    for (Menu menu : rootMenu) {
	        // 遍历所有节点，将父菜单id与传过来的id比较
            if (menu.getParentId().equals(id)) {
                childList.add(menu);
            }
	    }

	    // 把子菜单的子菜单再循环一遍
	    for (Menu menu : childList) {
	        menu.setChildList(getChild(menu.getMenuId(), rootMenu));// 递归
	    } 
	    
	    // 判断递归结束
	    if (childList.size() == 0) {
	        return null;
	    }
		return childList;
	}
}
