package edu.software.craftsmanship.blueprint.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class ExternalConfiguration {

    @Bean
    fun runtimeContext(environment: Environment) = RuntimeContext(environment)
}
