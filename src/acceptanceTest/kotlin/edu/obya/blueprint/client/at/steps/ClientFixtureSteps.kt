package edu.obya.blueprint.client.at.steps

import edu.obya.blueprint.client.at.state.ClientState
import edu.obya.blueprint.client.at.types.TestClient
import edu.obya.blueprint.common.at.context.TestContext.put
import io.cucumber.java.en.Given
import java.util.*


class ClientFixtureSteps(private val state: ClientState) {

    @Given("a following set of existing clients")
    fun existingClientsAre(clients: List<Pair<Int, TestClient>>) {
        put("existing-clients", clients.associateBy({ it.second.id }, { it.second }))
        put("existing-clients-id", clients.associateBy({ it.first }, { it.second.id }))
        state.setState(clients.map { it.second })
    }
}
