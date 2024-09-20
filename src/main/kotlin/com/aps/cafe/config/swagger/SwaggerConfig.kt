package com.aps.cafe.config.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    private val apiKeyStr = "api-key"

    private val securityScheme = SecurityScheme()
        .type(SecurityScheme.Type.APIKEY)
        .`in`(SecurityScheme.In.HEADER)
        .name("token")
    private val securityRequirement = SecurityRequirement()
        .addList(apiKeyStr)

    private val apiInfo = Info()
        .title("APS cafe")
        .version("0.0.1")
        .description("Awesome personal skills cafe")


    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .components(Components())
//        .components(Components().addSecuritySchemes(apiKeyStr,securityScheme))
//        .addSecurityItem(securityRequirement)
        .info(apiInfo)
}