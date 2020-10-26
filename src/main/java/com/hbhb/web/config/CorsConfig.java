package com.hbhb.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author xiaokang
 * @since 2020-10-26
 */
@Configuration
public class CorsConfig {

    private static final String ALLOWED_ORIGIN = "*";
    private static final String ALLOWED_METHODS = "GET, PUT, POST, DELETE, OPTIONS";
    private static final String ALLOWED_HEADERS = "x-requested-with, Content-Type, Authorization, credential, X-XSRF-TOKEN";
    private static final long MAX_AGE = 3600;

    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();
        // 开放哪些ip、端口、域名的访问权限，星号表示开放所有域
        config.addAllowedOrigin(ALLOWED_ORIGIN);
        // 是否允许发送Cookie信息
        config.setAllowCredentials(true);
        // 开放哪些Http方法，允许跨域访问
        config.addAllowedMethod(ALLOWED_METHODS);
        // 允许HTTP请求中的携带哪些Header信息
        config.addAllowedHeader(ALLOWED_HEADERS);
//        // 暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
//        config.addExposedHeader("*");
        config.setMaxAge(MAX_AGE);

        // 添加映射路径，/** 表示对所有的路径实行全局跨域访问权限的设置
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        return new CorsFilter(configSource);
    }
}
