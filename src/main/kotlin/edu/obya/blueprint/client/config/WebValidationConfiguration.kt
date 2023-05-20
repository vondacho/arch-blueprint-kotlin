package edu.obya.blueprint.client.config

import com.atlassian.oai.validator.OpenApiInteractionValidator
import com.atlassian.oai.validator.report.LevelResolver
import com.atlassian.oai.validator.report.ValidationReport
import edu.obya.blueprint.common.config.WebExchangeValidatorWebFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.AntPathMatcher

@ConstructorBinding
@ConfigurationProperties(prefix = "validation.exchange")
data class WebExchangeValidatorPaths(val paths: List<WebExchangeValidatorPath>) {
    fun matches(path: String): WebExchangeValidatorPath? = paths.find { it.match(path) }
}

@EnableConfigurationProperties(WebExchangeValidatorPaths::class)
@Configuration
class WebValidationConfiguration(val properties: WebExchangeValidatorPaths) {

    @Value("\${openapi.spec.url}")
    lateinit var specificationUrl: String

    @Bean
    fun requestValidatorFilter() =
        WebExchangeValidatorWebFilter(
            OpenApiInteractionValidator.createForSpecificationUrl(specificationUrl)
                .withLevelResolver(
                    LevelResolver.create()
                        .withLevel("validation.request.path.missing", ValidationReport.Level.INFO)
                        .withLevel("validation.schema.additionalProperties", ValidationReport.Level.INFO)
                        .withLevel("validation.request.body.schema.additionalProperties", ValidationReport.Level.INFO)
                        .build()
                )
                .build(),
            properties
        )
}

data class WebExchangeValidatorPath(
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
        fun full() = WebExchangeValidatorPath(enableRequest = true, enableResponse = true)
        fun lenient() = WebExchangeValidatorPath(enableRequest = true, enableResponse = true, lenientMode = true)
        fun noRequest() = WebExchangeValidatorPath(enableRequest = false, enableResponse = true)
        fun noResponse() = WebExchangeValidatorPath(enableRequest = true, enableResponse = false)
        fun disabled() = WebExchangeValidatorPath(enableRequest = false, enableResponse = false)
    }
}
