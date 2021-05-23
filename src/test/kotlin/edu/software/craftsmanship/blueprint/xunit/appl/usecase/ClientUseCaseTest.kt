package edu.software.craftsmanship.blueprint.xunit.appl.usecase

import edu.software.craftsmanship.blueprint.appl.exception.NotFoundException
import edu.software.craftsmanship.blueprint.appl.model.ClientInput
import edu.software.craftsmanship.blueprint.appl.usecase.ClientUseCase
import edu.software.craftsmanship.blueprint.domain.client.model.Client
import edu.software.craftsmanship.blueprint.domain.client.repository.ClientRepository
import edu.software.craftsmanship.blueprint.xunit.newClientId
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.test.test
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
internal class ClientUseCaseTest {

    @MockK
    lateinit var clientRepository: ClientRepository

    val client = Client(
        id = newClientId(),
        state = Client.State(name = "test", createdAt = LocalDateTime.now())
    )

    @Nested
    inner class Add {
        private val useCase = ClientUseCase(clientRepository)

        @Test
        fun `add saves a new client entity and returns it with an event if input is valid and does not exist`() {
            every { clientRepository.findByName(any(), strict = true) } returns Flux.empty()
            every { clientRepository.save(any<Client.State>()) } returns Mono.just(client)

            useCase.add(ClientInput("test")).test()
                .assertNext { assertThat(it.state.name).isEqualTo("test") }
                .verifyComplete()

            verify(exactly = 1) {
                clientRepository.save(any<Client.State>())
            }
        }

        @Test
        @Disabled("Cannot create an invalid input programmatically")
        fun `add does not save any client and returns an error if input is not valid`() {
            every { clientRepository.findAll() } returns Flux.empty()
            every { clientRepository.findByName(any(), strict = true) } returns Flux.empty()
            every { clientRepository.save(any<Client.State>()) } returns Mono.empty()

            useCase.add(ClientInput("")).test()
                .expectErrorMatches { it is IllegalStateException }
                .verify()

            verify(exactly = 0) {
                clientRepository.save(any<Client.State>())
            }
        }

        @Test
        fun `add does not save any client and returns an error if a client with same input name does exist`() {
            every { clientRepository.findByName(any(), strict = true) } returns Flux.just(mockk())
            every { clientRepository.save(any<Client.State>()) } returns Mono.empty()

            useCase.add(ClientInput("test")).test()
                .expectErrorMatches { it is IllegalStateException }
                .verify()

            verify(exactly = 0) {
                clientRepository.save(any<Client.State>())
            }
        }
    }

    @Nested
    inner class Update {
        private val useCase = ClientUseCase(clientRepository)

        @Test
        fun `update saves the mutations if input is valid, without duplicate, and client does exist`() {
            val updatedClient = client.copy(
                state = client.state.copy(
                    name = "other"
                )
            )
            every { clientRepository.findOne(client.id) } returns Mono.just(client)
            every { clientRepository.findByName(any(), strict = true) } returns Flux.empty()
            every { clientRepository.save(any<Client>()) } returns Mono.just(updatedClient)

            useCase.update(client.id, ClientInput(name = "other")).test()
                .expectNext(1)
                .verifyComplete()

            verify(exactly = 1) {
                clientRepository.save(updatedClient)
            }
        }

        @Test
        @Disabled("Cannot create an invalid input programmatically")
        fun `update does not save any mutation and returns an error if input is not valid`() {
            every { clientRepository.findAll() } returns Flux.empty()
            every { clientRepository.findOne(client.id) } returns Mono.just(client)
            every { clientRepository.findByName(any(), strict = true) } returns Flux.empty()

            useCase.update(client.id, ClientInput("")).test()
                .expectErrorMatches { it is IllegalStateException }
                .verify()

            verify(exactly = 0) {
                clientRepository.save(any<Client>())
            }
        }

        @Test
        fun `update does not save any mutation and returns an error if another client with same input name does exist`() {
            val twin = client.copy(id = newClientId())
            every { clientRepository.findOne(client.id) } returns Mono.just(client)
            every { clientRepository.findByName(any(), strict = true) } returns Flux.just(twin)

            useCase.update(client.id, ClientInput(client.state.name)).test()
                .expectErrorMatches { it is IllegalStateException }
                .verify()

            verify(exactly = 0) {
                clientRepository.save(any<Client>())
            }
        }
    }

    @Nested
    inner class Get {
        private val useCase = ClientUseCase(clientRepository)

        @Test
        fun `get returns a client entity if it does exist`() {
            every { clientRepository.findOne(client.id) } returns Mono.just(client)
            useCase.get(client.id).test()
                .expectNext(client)
                .verifyComplete()
        }

        @Test
        fun `get returns an error if client does not exist`() {
            every { clientRepository.findOne(client.id) } returns Mono.empty()
            useCase.get(client.id).test()
                .expectErrorMatches { it is NotFoundException }
                .verify()
        }
    }

    @Nested
    inner class Delete {
        private val useCase = ClientUseCase(clientRepository)

        @Test
        fun `delete removes the client entity with an event if it does exist`() {
            every { clientRepository.findOne(client.id) } returns Mono.just(client)
            every { clientRepository.delete(any()) } returns Mono.just(client)

            useCase.delete(client.id).test().expectNext(1).verifyComplete()

            verify(exactly = 1) {
                clientRepository.delete(withArg {
                    assertThat(it.id).isEqualTo(client.id)
                })
            }
        }

        @Test
        fun `delete returns an error if client does not exist`() {
            every { clientRepository.findOne(client.id) } returns Mono.empty()

            useCase.delete(client.id).test()
                .expectErrorMatches { it is NotFoundException }
                .verify()

            verify(exactly = 0) {
                clientRepository.delete(any())
            }
        }
    }

    @Nested
    inner class Restore {
        private val useCase = ClientUseCase(clientRepository)

        @Test
        fun `restore returns the client entity with an event if it is not activated`() {
            every { clientRepository.findOne(client.id, activatedOnly = false) } returns Mono.just(client.activate(false))
            every { clientRepository.restore(any()) } returns Mono.just(1)

            useCase.restore(client.id).test().expectNext(client.activate()).verifyComplete()

            verify(exactly = 1) {
                clientRepository.restore(withArg {
                    assertThat(it.id).isEqualTo(client.id)
                })
            }
        }

        @Test
        fun `restore returns an error if client is activated`() {
            every { clientRepository.findOne(client.id, activatedOnly = false) } returns Mono.just(client)

            useCase.restore(client.id).test()
                .expectErrorMatches { it is NotFoundException }
                .verify()

            verify(exactly = 0) {
                clientRepository.restore(any())
            }
        }
    }

}
