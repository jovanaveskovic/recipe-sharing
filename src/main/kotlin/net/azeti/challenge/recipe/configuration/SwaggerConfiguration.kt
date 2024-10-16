package net.azeti.challenge.recipe.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfiguration {

    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("recipe-sharing")
            .pathsToMatch("/**")
            .build()
    }

    @Bean
    fun customOpenAPI(): OpenAPI {
        val contact = Contact()
        contact.name = "Jovana"
        return OpenAPI()
            .info(
                Info()
                    .title("Recipe sharing challenge")
                    .version("1.0")
                    .description("Recipe sharing challenge")
                    .termsOfService("http://swagger.io/terms/")
                    .license(License().name("Apache 2.0").url("https://springdoc.org"))
                    .contact(contact)
            )
    }
}