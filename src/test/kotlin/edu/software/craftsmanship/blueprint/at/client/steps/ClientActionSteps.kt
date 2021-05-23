package edu.software.craftsmanship.blueprint.at.client.steps

import edu.software.craftsmanship.blueprint.at.client.api.ClientApiClient
import edu.software.craftsmanship.blueprint.at.common.context.Id
import edu.software.craftsmanship.blueprint.at.common.context.TestContext.at
import edu.software.craftsmanship.blueprint.at.common.context.TestContext.put
import edu.software.craftsmanship.blueprint.at.common.types.toNullable
import io.cucumber.java.en.When
import io.cucumber.java8.En

class ClientActionSteps(private val api: ClientApiClient) : En {

    @When("registering the new client")
    fun registerClient() {
        api.addClient(at("client-input"))
            .doOnNext { result ->
                put("response.status", result.status)
                result.content?.let { put("response.body", it) }
            }
            .block()
    }

    @When("looking up existing client {word}")
    fun lookupClientById(id: String) {
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

    @When("updating existing client {word}")
    fun updateClient(id: String) {
        api.updateClient(id, at("client-input"))
            .doOnNext {
                put("response.status", it.status)
            }
            .block()
    }

    @When("deleting existing client {word}")
    fun deleteClient(id: Id) {
        api.deleteClient(id)
            .doOnNext {
                put("response.status", it.status)
            }
            .block()
    }

    @When("restoring client {word}")
    fun restoreClient(id: Id) {
        api.restoreClient(id)
            .doOnNext {
                put("response.status", it.status)
            }
            .block()
    }
}
