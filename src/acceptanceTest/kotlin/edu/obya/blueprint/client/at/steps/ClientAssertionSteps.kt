package edu.obya.blueprint.client.at.steps

import edu.obya.blueprint.client.at.state.ClientState
import edu.obya.blueprint.client.at.types.TestClient
import edu.obya.blueprint.client.at.types.toNullable
import edu.obya.blueprint.client.domain.model.ClientId
import edu.obya.blueprint.common.at.context.TestContext.at
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.assertThat

class ClientAssertionSteps(private val state: ClientState) {

    @Then("the set of existing clients is unchanged")
    fun existingClientsUnchanged() {
        val existingOnes = state.getState()
        val cachedOnes = at<Map<String, TestClient>>("existing-clients").values
        assertThat(existingOnes).containsExactlyInAnyOrderElementsOf(cachedOnes)
    }

    @Then("the result set of existing clients contains {word}")
    fun resultingExistingClientIdentifiersAre(ids: String) {
        val expectedOnes = ids.toNullable()?.split(',')?.map { ClientId.fromString(it) } ?: emptyList()
        val returnedOnes = at<List<TestClient>>("response.body")
        assertThat(returnedOnes.map { it.id }).containsExactlyInAnyOrderElementsOf(expectedOnes)
    }

    @Then("the result set of existing clients is empty")
    fun noResultingExistingClientIdentifiers() {
        val returnedOnes = at<List<TestClient>>("response.body")
        assertThat(returnedOnes.map { it.id }).isEmpty()
    }

    @Then("the attributes of the returned client are the following")
    fun resultingClientAttributesAre(attributes: List<TestClient>) {
        assertThat(at<TestClient>("response.body")).isEqualTo(attributes.first())
    }

    @Then("the attributes of the existing client {clientId} are the following")
    fun resultingAttributesOfExistingClientAre(id: ClientId, attributes: List<TestClient>) {
        val existingOnes = state.getState().associateBy { it.id }
        val existingOne = existingOnes.getValue(id)
        assertThat(existingOne).isEqualTo(attributes.first())
    }

    @Then("the attributes of the existing client {clientId} are unchanged")
    fun clientAttributesAreUnchanged(id: ClientId) {
        val existingOnes = state.getState().associateBy { it.id }
        val existingOne = existingOnes.getValue(id)
        val cachedOne = at<Map<ClientId, TestClient>>("existing-clients")[id]
        assertThat(existingOne).isEqualTo(cachedOne)
    }

    @Then("the client {clientId} is removed from the set of existing clients")
    fun clientRemovedFromExistingOnes(id: ClientId) {
        val existingOnes = state.getState().map { it.id }
        assertThat(existingOnes).doesNotContain(id)
    }
}
