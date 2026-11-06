package kr.fasol.obix.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger-UI 설정
 */
@Configuration
public class SwaggerConfig {

    /**
     * Swagger 설정
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot REST API Specifications")
                        .description("Specification")
                        .version("1.0.0")
                );
    }

}