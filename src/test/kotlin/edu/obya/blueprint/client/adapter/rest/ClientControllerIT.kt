package edu.obya.blueprint.client.adapter.rest

import com.ninjasquad.springmockk.MockkBean
import edu.obya.blueprint.client.adapter.r2dbc.ClientR2dbcConfiguration
import edu.obya.blueprint.client.adapter.r2dbc.PostgresR2dbcDataSourceTestConfiguration
import edu.obya.blueprint.client.adapter.rest.TestWebUserTokens.TEST_ADMIN_TOKEN
import edu.obya.blueprint.client.adapter.rest.TestWebUserTokens.TEST_USER_TOKEN
import edu.obya.blueprint.client.application.ClientApplicationConfiguration
import edu.obya.blueprint.client.application.ClientInput
import edu.obya.blueprint.client.config.ExceptionHandling
import edu.obya.blueprint.client.config.WebSecurityConfiguration
import edu.obya.blueprint.client.config.WebValidationConfiguration
import edu.obya.blueprint.client.domain.model.ClientId
import edu.obya.blueprint.client.domain.model.TestClients.client1
import edu.obya.blueprint.client.domain.model.TestClients.client2
import io.mockk.every
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.autoconfigure.context.ShutdownEndpointAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.info.InfoEndpointAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.r2dbc.AutoConfigureDataR2dbc
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.BodyInserters
import java.util.function.Supplier

@ActiveProfiles("test", "r2dbc")
@AutoConfigureDataR2dbc
@AutoConfigureEmbeddedDatabase
@ContextConfiguration(classes = [
    ClientController::class,
    WebSecurityConfiguration::class,
    WebValidationConfiguration::class,
    ExceptionHandling::class,
    ClientR2dbcConfiguration::class,
    ClientApplicationConfiguration::class,
    PostgresR2dbcDataSourceTestConfiguration::class]
)
@WebFluxTest(excludeAutoConfiguration = [
    HealthEndpointAutoConfiguration::class,
    InfoEndpointAutoConfiguration::class,
    ShutdownEndpointAutoConfiguration::class
])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class ClientControllerIT {

    @MockkBean
    lateinit var idSupplier: Supplier<ClientId>

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    @Order(1)
    fun shouldCreateClient() {
        every { idSupplier.get() } returns client1.id

        webTestClient.post()
            .uri("/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, TEST_USER_TOKEN)
            .body(BodyInserters.fromValue(ClientInput(name = client1.state.name, age = client1.state.age)))
            .accept(MediaType.TEXT_PLAIN)
            .exchange()
            .expectStatus().isCreated
            .expectHeader().contentType(MediaType.TEXT_PLAIN)
            .expectBody<String>().isEqualTo(client1.id.toString())

        val output = client1.toOutput()
        webTestClient.get()
            .uri("/clients/{id}", client1.id)
            .header(AUTHORIZATION, TEST_USER_TOKEN)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.name").isEqualTo(output.name)
            .jsonPath("$.age").isEqualTo(output.age!!)
            .jsonPath("$.createdAt").isNotEmpty
            .jsonPath("$.activated").isEqualTo(true)

        webTestClient.get()
            .uri("/clients")
            .header(AUTHORIZATION, TEST_USER_TOKEN)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$[0].name").isEqualTo(output.name)
            .jsonPath("$[0].age").isEqualTo(output.age!!)
            .jsonPath("$[0].createdAt").isNotEmpty
            .jsonPath("$[0].activated").isEqualTo(true)
    }

    @Test
    @Order(2)
    fun shouldModifyClient() {
        val dto = client2.toOutput()
        webTestClient.put()
            .uri("/clients/{id}", client1.id)
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, TEST_USER_TOKEN)
            .body(BodyInserters.fromValue(dto))
            .exchange()
            .expectStatus().isNoContent

        webTestClient.get()
            .uri("/clients/{id}", client1.id)
            .header(AUTHORIZATION, TEST_USER_TOKEN)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.name").isEqualTo(dto.name)
            .jsonPath("$.age").isEqualTo(dto.age!!)
            .jsonPath("$.createdAt").isNotEmpty
            .jsonPath("$.activated").isEqualTo(true)
    }

    @Test
    @Order(3)
    fun shouldDeleteClient() {
        webTestClient.delete()
            .uri("/clients/{id}", client1.id)
            .header(AUTHORIZATION, TEST_ADMIN_TOKEN)
            .exchange()
            .expectStatus().isNoContent

        webTestClient.get()
            .uri("/clients/{id}?activatedOnly=false", client1.id)
            .header(AUTHORIZATION, TEST_USER_TOKEN)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.name").isNotEmpty
            .jsonPath("$.age").isNotEmpty
            .jsonPath("$.createdAt").isNotEmpty
            .jsonPath("$.activated").isEqualTo(false)

        webTestClient.get()
            .uri("/clients?activatedOnly=false")
            .header(AUTHORIZATION, TEST_USER_TOKEN)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(ClientOutput::class.java)
            .hasSize(1)
    }

    @Test
    @Order(4)
    fun shouldRemoveClient() {
        webTestClient.post()
            .uri("/clients/{id}:restore", client1.id)
            .header(AUTHORIZATION, TEST_ADMIN_TOKEN)
            .exchange()
            .expectStatus().isNoContent

        webTestClient.get()
            .uri("/clients/{id}", client1.id)
            .header(AUTHORIZATION, TEST_USER_TOKEN)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.name").isNotEmpty
            .jsonPath("$.age").isNotEmpty
            .jsonPath("$.createdAt").isNotEmpty
            .jsonPath("$.activated").isEqualTo(true)

        webTestClient.get()
            .uri("/clients")
            .header(AUTHORIZATION, TEST_USER_TOKEN)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(ClientOutput::class.java)
            .hasSize(1)
    }
}
