package edu.software.craftsmanship.blueprint.web.config

import com.fasterxml.jackson.databind.ObjectMapper
import edu.software.craftsmanship.blueprint.web.controller.client.ClientController
import edu.software.craftsmanship.blueprint.web.controller.metrics.MetricsController
import edu.software.craftsmanship.blueprint.web.serializer.ListOutputSerializer
import edu.software.craftsmanship.blueprint.web.validation.WebExchangeValidatorConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.server.WebExceptionHandler
import org.zalando.problem.ProblemModule
import org.zalando.problem.spring.webflux.advice.ProblemExceptionHandler
import org.zalando.problem.spring.webflux.advice.ProblemHandling
import org.zalando.problem.violations.ConstraintViolationProblemModule

/**
 * See https://github.com/zalando/problem-spring-web
 */

@ControllerAdvice
class ExceptionHandling : ProblemHandling

@ComponentScan(basePackageClasses = [
    ClientController::class,
    MetricsController::class,
    ListOutputSerializer::class,
    ExceptionHandling::class
])
@Import(value = [WebExchangeValidatorConfig::class])
@Configuration
class WebConfiguration {

    @Bean
    fun problemModule() = ProblemModule()

    @Bean
    fun constraintViolationProblemModule() = ConstraintViolationProblemModule()

    /**
     *  The handler must have precedence over WebFluxResponseStatusExceptionHandler
     *  and Spring Boot's ErrorWebExceptionHandler
     */
    @Bean
    @Order(-2)
    fun problemExceptionHandler(mapper: ObjectMapper, problemHandling: ProblemHandling): WebExceptionHandler {
        return ProblemExceptionHandler(mapper, problemHandling)
    }
}
