package edu.software.craftsmanship.blueprint.xunit.data

import edu.software.craftsmanship.blueprint.data.config.DataConfiguration
import edu.software.craftsmanship.blueprint.domain.client.model.ClientId
import edu.software.craftsmanship.blueprint.domain.client.repository.ClientRepository
import edu.software.craftsmanship.blueprint.xunit.TestClients
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import reactor.test.StepVerifier

@ActiveProfiles("test")
@SqlGroup(
    value = [
        Sql(
            scripts = ["/data/insert-data.sql"],
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
        ),
        Sql(
            scripts = ["/data/clean-data.sql"],
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
        )
    ]
)
@ContextConfiguration(classes = [DataConfiguration::class, DataSourceAutoConfiguration::class])
@DataR2dbcTest
internal class ClientRepositoryIT {

    @Autowired
    lateinit var repository: ClientRepository

    @Test
    fun `returns the expected persisted client`() {
        StepVerifier.create(repository.findOne(TestClients.client1.id))
            .assertNext { assertThat(it.state).isEqualTo(TestClients.client1.state) }
            .verifyComplete()

        StepVerifier.create(repository.findOne(TestClients.client3.id))
            .verifyComplete()

        StepVerifier.create(repository.findOne(TestClients.client3.id, activatedOnly = false))
            .assertNext { assertThat(it.state).isEqualTo(TestClients.client3.state) }
            .verifyComplete()
    }

    @Test
    fun `returns the expected client list`() {
        StepVerifier.create(repository.findAll().collectList())
            .assertNext { assertThat(it).containsExactlyInAnyOrder(TestClients.client1, TestClients.client2) }
            .verifyComplete()

        StepVerifier.create(repository.findAll(activatedOnly = false).collectList())
            .assertNext {
                assertThat(it).containsExactlyInAnyOrder(
                    TestClients.client1,
                    TestClients.client2,
                    TestClients.client3
                )
            }
            .verifyComplete()

        StepVerifier.create(repository.findByName(name = "test", strict = true))
            .assertNext { assertThat(it.state).isEqualTo(TestClients.client1.state) }
            .verifyComplete()

        StepVerifier.create(repository.findByName(name = "TeSt", strict = true))
            .assertNext { assertThat(it.state).isEqualTo(TestClients.client1.state) }
            .verifyComplete()

        StepVerifier.create(repository.findByName(name = "t", strict = false))
            .assertNext { assertThat(it.state).isEqualTo(TestClients.client1.state) }
            .verifyComplete()

        StepVerifier.create(repository.findByName(name = "T", strict = false))
            .assertNext { assertThat(it.state).isEqualTo(TestClients.client1.state) }
            .verifyComplete()

        StepVerifier.create(repository.findByName(name = "deactivated", activatedOnly = false))
            .assertNext { assertThat(it.state).isEqualTo(TestClients.client3.state) }
            .verifyComplete()
    }

    @Test
    fun `add new client`() {
        var clientId: ClientId? = null

        StepVerifier.create(
            repository.save(
                TestClients.client1.state.copy(
                    name = "new",
                    age = 21,
                    createdAt = null
                )
            )
        )
            .assertNext {
                clientId = it.id
                assertThat(it.state.name).isEqualTo("new")
                assertThat(it.state.age).isEqualTo(TestClients.client1.state.age)
                assertThat(it.state.createdAt).isNotNull()
            }
            .verifyComplete()

        StepVerifier.create(repository.findOne(clientId!!))
            .assertNext { assertThat(it.id).isEqualTo(clientId) }
            .verifyComplete()
    }

    @Test
    fun `modify existing client`() {
        val newState = TestClients.client1.state.copy(age = 31)
        val modifiedClient = TestClients.client1.copy(state = newState)

        StepVerifier.create(repository.save(modifiedClient))
            .expectNext(modifiedClient)
            .verifyComplete()

        StepVerifier.create(repository.findOne(TestClients.client1.id))
            .assertNext {
                assertThat(it).isEqualTo(modifiedClient)
            }
            .verifyComplete()
    }

    @Test
    fun `delete existing client`() {
        StepVerifier.create(repository.delete(TestClients.client1))
            .expectNext(TestClients.client1.copy(state = TestClients.client1.state.copy(activated = false)))
            .verifyComplete()

        StepVerifier.create(repository.findOne(TestClients.client1.id))
            .verifyComplete()
    }
}
