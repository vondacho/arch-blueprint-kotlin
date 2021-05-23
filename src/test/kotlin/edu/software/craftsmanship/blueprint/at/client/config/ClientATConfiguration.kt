package edu.software.craftsmanship.blueprint.at.client.config

import edu.software.craftsmanship.blueprint.at.client.api.ClientApiClient
import edu.software.craftsmanship.blueprint.at.client.api.ClientWebApiClient
import edu.software.craftsmanship.blueprint.at.client.state.DatabaseClientState
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDateTime
import java.util.*
import java.util.function.Supplier

@TestConfiguration
class ClientATConfiguration {

    @MockBean
    lateinit var idSupplier: Supplier<UUID>

    @MockBean
    lateinit var timestampSupplier: Supplier<LocalDateTime>

    @Bean
    fun clientState(client: DatabaseClient) = DatabaseClientState(client)

    @Bean
    fun clientWebApiClient(webClient: WebClient): ClientApiClient {
        return ClientWebApiClient(webClient)
    }
}
