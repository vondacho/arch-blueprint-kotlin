package edu.obya.blueprint.client.cdc.pact.provider

import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.State
import au.com.dius.pact.provider.junitsupport.loader.PactFolder
import com.ninjasquad.springmockk.MockkBean
import edu.obya.blueprint.client.cdc.TestClient.TEST_CLIENT
import edu.obya.blueprint.client.cdc.TestClient.TEST_CLIENT_ID
import edu.obya.blueprint.client.domain.model.Client
import edu.obya.blueprint.client.domain.model.ClientId
import edu.obya.blueprint.client.domain.service.ClientRepository
import io.mockk.every
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*
import java.util.function.Supplier

@ActiveProfiles("test")
@Provider("clientAPI")
@PactFolder("client/cdc/pact")
@Import(ClientPactContextConfiguration::class)
@AutoConfigureEmbeddedDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientPactProviderTest {

    @LocalServerPort
    var port = 0

    @MockkBean
    lateinit var clientRepository: ClientRepository

    @MockkBean
    lateinit var idSupplier: Supplier<ClientId>

    @BeforeEach
    fun setUp(context: PactVerificationContext) {
        context.target = HttpTestTarget("localhost", port)
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    fun verifyPact(context: PactVerificationContext) {
        context.verifyInteraction()
    }

    @State("with default client")
    fun withDefaultClientState() {
        every { clientRepository.findOne(any(), activatedOnly = true) } returns Mono.just(TEST_CLIENT)
        every { clientRepository.remove(any()) } returns Mono.just(TEST_CLIENT)
    }

    @State("without risk of duplication on addition")
    fun withoutRiskOfDuplicateOnAdditionState() {
        every { clientRepository.findByName(any(), strict = true, activatedOnly = true) } returns Flux.empty()
        every { clientRepository.save(any<Client.State>()) } returns Mono.just(TEST_CLIENT)
    }

    @State("without risk of duplication on mutation")
    fun withoutRiskOfDuplicateOnMutationState() {
        every { clientRepository.findByName(any(), strict = true, activatedOnly = true) } returns Flux.empty()
        every { clientRepository.save(any<Client>()) } returns Mono.just(TEST_CLIENT)
    }

    @State("with default client can be restored")
    fun withDefaultClientRemovalState() {
        every { clientRepository.findOne(any(), activatedOnly = false) } returns Mono.just(TEST_CLIENT.activate(false))
        every { clientRepository.save(any<Client>()) } returns Mono.just(TEST_CLIENT)
    }

    @State("with default client id")
    fun withDefaultClientIdState() {
        every { idSupplier.get() } returns TEST_CLIENT_ID
    }
}
