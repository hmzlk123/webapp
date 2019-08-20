package com.xyzq.webapp.contants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public class GlobalProperties {
	private String baseherf;
}
