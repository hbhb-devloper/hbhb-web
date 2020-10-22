package com.hbhb.web.config;

import com.hbhb.core.constants.AuthConstant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Collections;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
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
        if (!StringUtils.isEmpty(profile) && !"prd".equals(profile) && !"default".equals(profile)) {
            defaultServerUrl += "/" + profile;
        }
        return new OpenAPI()
                .addServersItem(new Server().url(defaultServerUrl))
                .components(new Components().addSecuritySchemes(AuthConstant.JWT_TOKEN_HEADER.value(),
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name(AuthConstant.JWT_TOKEN_HEADER.value())))
                .security(Collections.singletonList(new SecurityRequirement().addList(AuthConstant.JWT_TOKEN_HEADER.value())))
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description(description));
    }
}
