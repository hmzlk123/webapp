package com.xyzq.webapp.controller;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Package com.xyzq.webapp.controller
 * @Description 异常处理
 * @author linkan
 * @date Created in 2019/9/5 17:09
 * @Copyright Copyright (c) 2019
 * @Version 0.0.1
 */
@ControllerAdvice
public class ExceptionHandleController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandleController.class);

    @ExceptionHandler(UnauthorizedException.class)
    public String handleShiroException(Exception ex) {
        logger.debug("捕获没有权限异常");
        return "redirect:/error-403";
    }

    @ExceptionHandler(AuthorizationException.class)
    public String AuthorizationException(Exception ex) {
        logger.debug("捕获没有认证异常");
        return "redirect:/error-401";
    }
}
