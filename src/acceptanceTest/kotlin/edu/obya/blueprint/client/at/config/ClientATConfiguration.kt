package edu.obya.blueprint.client.at.config

import edu.obya.blueprint.client.at.api.ClientWebApiClient
import edu.obya.blueprint.client.at.state.DatabaseClientState
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
    fun clientWebApiClient(webClient: WebClient) = ClientWebApiClient(webClient)
}
