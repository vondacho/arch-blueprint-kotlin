package edu.obya.blueprint.client.adapter.rest

import edu.obya.blueprint.client.application.ClientInput
import edu.obya.blueprint.client.application.ClientUseCase
import edu.obya.blueprint.client.domain.model.Client
import edu.obya.blueprint.client.domain.model.newClientId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.LocalDateTime

internal class ClientControllerTest {

    val clientUseCase = mockk<ClientUseCase>()

    val controller = ClientController(clientUseCase)

    val input = ClientInput(name = "test", age = 21)

    val clientId = newClientId()

    val client = Client(
        id = clientId,
        state = Client.State(
            name = "test",
            age = 21,
            createdAt = LocalDateTime.parse("2020-10-10T12:00:00")
        )
    )

    val output = client.toOutput()

    @Nested
    inner class Add {
        @Test
        fun `successful add returns a representation of new client with status CREATED`() {
            every { clientUseCase.add(input = any()) } returns Mono.just(client.id)

            StepVerifier.create(controller.add(input))
                .assertNext {
                    assertThat(it.statusCode).isEqualTo(HttpStatus.CREATED)
                    assertThat(it.body).isInstanceOf(ClientOutput::class.java)
                    assertThat(it.body).isEqualTo(output.id)
                }

            verify { clientUseCase.add(input) }
        }

        @Test
        fun `bad request on add returns a representation of the error with associated status`() {
            every { clientUseCase.add(input = any()) } returns Mono.error(IllegalStateException("test"))

            StepVerifier.create(controller.add(input))
                .assertNext {
                    assertThat(it.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
                    assertThat(it.body).isNotNull
                }
        }
    }

    @Nested
    inner class Update {
        @Test
        fun `successful update returns status NO_CONTENT`() {
            every { clientUseCase.replace(id = any(), input = any()) } returns Mono.just(clientId)

            StepVerifier.create(controller.update(clientId, input))
                .assertNext {
                    assertThat(it.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
                }

            verify { clientUseCase.replace(clientId, eq(input)) }
        }

        @Test
        fun `bad request on update returns a representation of the error with associated status`() {
            every { clientUseCase.replace(id = any(), input = any()) } returns Mono.error(IllegalStateException("test"))

            StepVerifier.create(controller.update(clientId, input))
                .assertNext {
                    assertThat(it.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
                    assertThat(it.body).isNotNull
                }
        }
    }

    @Nested
    inner class Get {
        @Test
        fun `successful get returns a representation of existing client with status OK`() {
            every { clientUseCase.get(clientId, activatedOnly = true) } returns Mono.just(client)

            StepVerifier.create(controller.get(clientId, activatedOnly = true))
                .assertNext {
                    assertThat(it.statusCode).isEqualTo(HttpStatus.OK)
                    assertThat(it.body).isInstanceOf(ClientOutput::class.java)
                }

            verify { clientUseCase.get(clientId, activatedOnly = true) }
        }

        @Test
        fun `bad request on get returns a representation of the error with associated status`() {
            every { clientUseCase.get(clientId, activatedOnly = true) } returns Mono.error(IllegalArgumentException("test"))

            StepVerifier.create(controller.get(clientId, activatedOnly = true))
                .assertNext {
                    assertThat(it.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
                    assertThat(it.body).isNotNull
                }
        }
    }

    @Nested
    inner class Search {

        @Test
        fun `successful search returns a representation of existing clients with status OK`() {
            every { clientUseCase.find(name = any(), activatedOnly = true) } returns Flux.just(client)

            StepVerifier.create(controller.findAll(name = "test", activatedOnly = true))
                .assertNext {
                    assertThat(it.statusCode).isEqualTo(HttpStatus.OK)
                    assertThat(it.body).isEqualTo(listOf(output))
                }

            verify { clientUseCase.find(name = "test", activatedOnly = true) }
        }

        @Test
        fun `faulty search returns a representation of the error with associated status`() {
            every { clientUseCase.search(term = any(), activatedOnly = true) } returns Flux.error(IllegalArgumentException("test"))

            StepVerifier.create(controller.search(term = "test", activatedOnly = true))
                .assertNext {
                    assertThat(it.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
                    assertThat(it.body).isNotNull
                }
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `successful delete returns status NO_CONTENT`() {
            every { clientUseCase.remove(clientId) } returns Mono.just(clientId)

            StepVerifier.create(controller.delete(clientId))
                .assertNext {
                    assertThat(it.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
                }

            verify { clientUseCase.remove(clientId) }
        }

        @Test
        fun `faulty add returns a representation of the error with associated status`() {
            every { clientUseCase.remove(clientId) } returns Mono.error(IllegalArgumentException("test"))

            StepVerifier.create(controller.delete(clientId))
                .assertNext {
                    assertThat(it.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
                    assertThat(it.body).isNotNull
                }
        }
    }

    @Nested
    inner class Restore {
        @Test
        fun `successful restore returns restored client with status OK`() {
            every { clientUseCase.restore(clientId) } returns Mono.just(clientId)

            StepVerifier.create(controller.restore(clientId))
                .assertNext {
                    assertThat(it.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
                }

            verify { clientUseCase.restore(clientId) }
        }
    }
}
