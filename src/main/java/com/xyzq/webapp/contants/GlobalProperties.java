package com.xyzq.webapp.contants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @Package com.xyzq.webapp.contants
 * @Description 公共配置类
 * @author linkan
 * @date Created in 2019/8/1 14:55
 * @Copyright Copyright (c) 2019
 * @version 0.0.1
 */
@Component
@ConfigurationProperties(prefix="com.xyzq.webapp")
@Data
public class GlobalProperties {
	private String basehref;                    //上下文路径
}
