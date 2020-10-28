package com.hbhb.web.config;

import com.hbhb.web.resolver.UserIdArgumentResolver;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import javax.annotation.Resource;

/**
 * @author xiaokang
 * @since 2020-10-28
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private UserIdArgumentResolver userIdArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdArgumentResolver);
    }
}
