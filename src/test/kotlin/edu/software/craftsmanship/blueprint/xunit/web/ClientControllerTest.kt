package edu.software.craftsmanship.blueprint.xunit.web

import edu.software.craftsmanship.blueprint.appl.model.ClientInput
import edu.software.craftsmanship.blueprint.appl.usecase.ClientUseCase
import edu.software.craftsmanship.blueprint.domain.client.model.Client
import edu.software.craftsmanship.blueprint.web.controller.client.ClientController
import edu.software.craftsmanship.blueprint.web.model.ClientOutput
import edu.software.craftsmanship.blueprint.web.model.EmptyOutput
import edu.software.craftsmanship.blueprint.web.model.ListOutput
import edu.software.craftsmanship.blueprint.xunit.newClientId
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

    val output = Client(
        id = clientId,
        state = Client.State(
            name = "test",
            age = 21,
            createdAt = LocalDateTime.parse("2020-10-10T12:00:00")
        )
    )

    @Nested
    inner class Add {
        @Test
        fun `successful add returns a representation of new client with status CREATED`() {
            every { clientUseCase.add(input = any()) } returns Mono.just(output)

            StepVerifier.create(controller.add(input))
                .assertNext {
                    assertThat(it.statusCode).isEqualTo(HttpStatus.CREATED)
                    assertThat(it.body).isInstanceOf(ClientOutput::class.java)
                    assertThat(it.body).isEqualTo(output)
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
            every { clientUseCase.update(id = any(), input = any()) } returns Mono.just(1)

            StepVerifier.create(controller.update(clientId, input))
                .assertNext {
                    assertThat(it.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
                    assertThat(it.body).isInstanceOf(EmptyOutput::class.java)
                }

            verify { clientUseCase.update(clientId, eq(input)) }
        }

        @Test
        fun `bad request on update returns a representation of the error with associated status`() {
            every { clientUseCase.update(id = any(), input = any()) } returns Mono.error(IllegalStateException("test"))

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
            every { clientUseCase.get(id = any(), activatedOnly = true) } returns Mono.just(output)

            StepVerifier.create(controller.get(clientId, activatedOnly = true))
                .assertNext {
                    assertThat(it.statusCode).isEqualTo(HttpStatus.OK)
                    assertThat(it.body).isInstanceOf(ClientOutput::class.java)
                }

            verify { clientUseCase.get(clientId, activatedOnly = true) }
        }

        @Test
        fun `bad request on get returns a representation of the error with associated status`() {
            every { clientUseCase.get(id = any(), activatedOnly = true) } returns Mono.error(IllegalArgumentException("test"))

            StepVerifier.create(controller.get(clientId, activatedOnly = true))
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
            every { clientUseCase.delete(id = any()) } returns Mono.just(1)

            StepVerifier.create(controller.delete(clientId))
                .assertNext {
                    assertThat(it.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
                    assertThat(it.body).isInstanceOf(EmptyOutput::class.java)
                }

            verify { clientUseCase.delete(clientId) }
        }

        @Test
        fun `faulty add returns a representation of the error with associated status`() {
            every { clientUseCase.delete(id = any()) } returns Mono.error(IllegalArgumentException("test"))

            StepVerifier.create(controller.delete(clientId))
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
            every { clientUseCase.find(name = any(), activatedOnly = true) } returns Flux.just(output)

            StepVerifier.create(controller.findAll(name = "test", activatedOnly = true))
                .assertNext {
                    assertThat(it.statusCode).isEqualTo(HttpStatus.OK)
                    assertThat(it.body).isInstanceOf(ListOutput::class.java)
                    assertThat(it.body as ListOutput<ClientOutput>).extracting { o -> o.items }.isEqualTo(listOf(output))
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
    inner class Restore {
        @Test
        fun `successful restore returns restored client with status OK`() {
            every { clientUseCase.restore(id = any()) } returns Mono.just(output)

            StepVerifier.create(controller.restore(clientId))
                .assertNext {
                    assertThat(it.statusCode).isEqualTo(HttpStatus.OK)
                    assertThat(it.body).isInstanceOf(ClientOutput::class.java)
                    assertThat(it.body).isEqualTo(output)
                }

            verify { clientUseCase.restore(clientId) }
        }
    }
}
