package com.project.manager.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "Project Manager API"))
@Configuration
public class GeneralConfig {
    public static final String PAGINATION_DEFAULT_PAGE_VALUE = "0";
    public static final String PAGINATION_DEFAULT_SIZE_VALUE = "10";

}
