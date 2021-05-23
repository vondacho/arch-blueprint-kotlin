package edu.software.craftsmanship.blueprint.web.validation

import com.atlassian.oai.validator.OpenApiInteractionValidator
import com.atlassian.oai.validator.report.LevelResolver
import com.atlassian.oai.validator.report.ValidationReport
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.AntPathMatcher

@ConstructorBinding
@ConfigurationProperties(prefix = "validation.exchange")
data class WebExchangeValidatorProperties(val paths: List<WebExchangeValidatorPathConfig>) {
    fun matches(path: String): WebExchangeValidatorPathConfig? = paths.find { it.match(path) }
}

@Configuration
@EnableConfigurationProperties(WebExchangeValidatorProperties::class)
class WebExchangeValidatorConfig(val properties: WebExchangeValidatorProperties) {
    @Bean
    fun requestValidatorFilter() =
        WebExchangeValidatorWebFilter(
            OpenApiInteractionValidator.createForSpecificationUrl("openapi.yaml")
                .withLevelResolver(
                    LevelResolver.create()
                        .withLevel("validation.schema.additionalProperties", ValidationReport.Level.IGNORE)
                        .withLevel("validation.request.body.schema.additionalProperties", ValidationReport.Level.INFO)
                        .build()
                )
                .build(),
            properties
        )
}

data class WebExchangeValidatorPathConfig(
    val pathPattern: String = "/**",
    val enableRequest: Boolean,
    val enableResponse: Boolean,
    val lenientMode: Boolean = false
) {
    private val matcher = AntPathMatcher()

    fun match(path: String): Boolean {
        return matcher.match(pathPattern, path)
    }

    companion object {
        fun full() = WebExchangeValidatorPathConfig(enableRequest = true, enableResponse = true)
        fun lenient() = WebExchangeValidatorPathConfig(enableRequest = true, enableResponse = true, lenientMode = true)
        fun noRequest() = WebExchangeValidatorPathConfig(enableRequest = false, enableResponse = true)
        fun noResponse() = WebExchangeValidatorPathConfig(enableRequest = true, enableResponse = false)
        fun disabled() = WebExchangeValidatorPathConfig(enableRequest = false, enableResponse = false)
    }
}
