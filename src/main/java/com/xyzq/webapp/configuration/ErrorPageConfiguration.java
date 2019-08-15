package com.xyzq.webapp.configuration;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * Package: com.xyzq.webapp.configuration
 * Description： 错误页面配置
 * Author: linkan
 * Date: Created in 2019/8/1 13:52
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
@Configuration
public class ErrorPageConfiguration {


	/**
     * Title: webServerFactoryCustomizer
     * Description: 以编程方式配置嵌入式servlet容器，可以通过注册实现该 WebServerFactoryCustomizer 接口的Spring bean
     * @return WebServerFactoryCustomizer<ConfigurableWebServerFactory>
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return factory -> {
            ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND,"/error-404");
            ErrorPage errorPage403 = new ErrorPage(HttpStatus.FORBIDDEN,"/error-403");
            factory.addErrorPages(errorPage403,errorPage404);
        };
    }
}
