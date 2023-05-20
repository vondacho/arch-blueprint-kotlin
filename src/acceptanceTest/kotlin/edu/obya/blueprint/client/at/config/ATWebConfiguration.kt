package edu.obya.blueprint.client.at.config

import mu.KotlinLogging
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.web.reactive.function.client.WebClient

@TestConfiguration
class ATWebConfiguration {
    private val log = KotlinLogging.logger {}

    @Bean
    fun webClient(builder: WebClient.Builder, environment: Environment): WebClient {
        val serverHost = environment.getProperty("server.address")
        val serverPort = environment.getProperty("server.port")
        log.info("Initializing web client with $serverHost:$serverPort")
        return builder
            .baseUrl("http://$serverHost:$serverPort")
            .build()
    }
}
