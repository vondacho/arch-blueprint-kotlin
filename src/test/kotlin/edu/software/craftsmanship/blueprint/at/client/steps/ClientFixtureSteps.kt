package edu.software.craftsmanship.blueprint.at.client.steps

import edu.software.craftsmanship.blueprint.at.client.state.ClientState
import edu.software.craftsmanship.blueprint.at.client.types.TestClient
import edu.software.craftsmanship.blueprint.at.client.types.TestClientInput
import edu.software.craftsmanship.blueprint.at.common.context.TestContext.put
import io.cucumber.java.en.Given
import io.cucumber.java8.En
import java.util.*


class ClientFixtureSteps(private val state: ClientState) : En {

    @Given("a following set of existing clients")
    fun existingClientsAre(clients: List<Pair<Int, TestClient>>) {
        put("existing-clients", clients.associateBy({ it.second.id }, { it.second }))
        put("existing-clients-id", clients.associateBy({ it.first }, { UUID.fromString(it.second.id) }))
        state.setState(clients.map { it.second })
    }

    @Given("a following set of client attributes")
    fun clientAttributesAre(attributes: List<TestClientInput>) {
        put("client-input", attributes.first())
    }
}
