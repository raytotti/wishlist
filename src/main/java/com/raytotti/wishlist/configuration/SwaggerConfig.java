package com.raytotti.wishlist.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi wishlistsApi() {
        return GroupedOpenApi.builder()
                .group("wishlist")
                .pathsToMatch("/api/v1/wishlists/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI(@Value("${spring-doc.version}") String appVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title("Wishlist API")
                        .description("This microservice is responsible for managing an e-commerce Wishlist.")
                        .version(appVersion)
                        .license(new License().name("Ray Toti Felix de Araujo").url("https://github.com/raytotti/wishlist")));
    }
}
