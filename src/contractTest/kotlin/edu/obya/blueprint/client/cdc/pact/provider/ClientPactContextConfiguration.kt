package edu.obya.blueprint.client.cdc.pact.provider

import org.springframework.boot.actuate.autoconfigure.context.ShutdownEndpointAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.info.InfoEndpointAutoConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import

@EnableAutoConfiguration(exclude = [
    FlywayAutoConfiguration::class,
    HealthEndpointAutoConfiguration::class,
    InfoEndpointAutoConfiguration::class,
    ShutdownEndpointAutoConfiguration::class
])
@Import(PostgresR2dbcDataSourceTestConfiguration::class)
@TestConfiguration
class ClientPactContextConfiguration
