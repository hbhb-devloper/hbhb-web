package com.hbhb.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

/**
 * @author xiaokang
 * @since 2020-10-12
 */
@Configuration
public class SwaggerConfig {

    @Value("${springdoc.title}")
    private String title;
    @Value("${springdoc.version}")
    private String version;
    @Value("${springdoc.description}")
    private String description;
    @Value("${springdoc.server-url}")
    private String defaultServerUrl;
    @Value("${spring.profiles.active}")
    private String profile;

    @Bean
    public OpenAPI customOpenApi() {
        if (!StringUtils.isEmpty(profile) && !"prd".equals(profile)) {
            defaultServerUrl += profile;
        }
        return new OpenAPI()
                .addServersItem(new Server().url(defaultServerUrl))
                .components(new Components().addSecuritySchemes("bearer-key", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description(description));
    }
}
