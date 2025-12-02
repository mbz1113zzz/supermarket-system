package org.cityu.supermarket.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3.0 config - Spring Boot 3.x
 * @version 0.0.1
 * @date 2025-11-12
 * @updated 2025/11/14 Upgraded to OpenAPI 3.0
 */
@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Supermarket Membership Management APIs")
                        .description("Supermarket membership management API reference - Spring Boot 3.x + Java 17")
                        .version("3.0")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@example.com")
                                .url("http://localhost:8081/supermarket"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }

}
