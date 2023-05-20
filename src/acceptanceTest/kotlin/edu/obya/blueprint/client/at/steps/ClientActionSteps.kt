package edu.obya.blueprint.client.at.steps

import edu.obya.blueprint.client.at.api.ClientApiClient
import edu.obya.blueprint.client.at.types.TestClientInput
import edu.obya.blueprint.client.at.types.toNullable
import edu.obya.blueprint.client.domain.model.ClientId
import edu.obya.blueprint.common.at.context.TestContext.put
import io.cucumber.java.en.When

class ClientActionSteps(private val api: ClientApiClient) {

    @When("adding the new client")
    fun addClient(attributes: List<TestClientInput>) {
        api.addClient(attributes.first())
            .doOnNext { result ->
                put("response.status", result.status)
                result.content?.let { put("response.body", it) }
            }
            .block()
    }

    @When("looking up existing client {clientId}")
    fun lookupClientById(id: ClientId) {
        api.getClient(id)
            .doOnNext { result ->
                put("response.status", result.status)
                result.content?.let { put("response.body", it) }
            }
            .block()
    }

    @When("looking up existing clients with name {word}")
    fun lookupClientByName(name: String) {
        api.searchClients(name.toNullable() ?: "")
            .doOnNext { result ->
                put("response.status", result.status)
                result.content?.let { put("response.body", it) }
            }
            .block()
    }

    @When("replacing existing client {clientId}")
    fun updateClient(id: ClientId, attributes: List<TestClientInput>) {
        api.updateClient(id, attributes.first())
            .doOnNext {
                put("response.status", it.status)
            }
            .block()
    }

    @When("removing existing client {clientId}")
    fun deleteClient(id: ClientId) {
        api.deleteClient(id)
            .doOnNext {
                put("response.status", it.status)
            }
            .block()
    }

    @When("restoring client {clientId}")
    fun restoreClient(id: ClientId) {
        api.restoreClient(id)
            .doOnNext {
                put("response.status", it.status)
            }
            .block()
    }
}
