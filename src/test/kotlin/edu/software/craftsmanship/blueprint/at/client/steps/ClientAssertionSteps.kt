package edu.software.craftsmanship.blueprint.at.client.steps

import edu.software.craftsmanship.blueprint.at.client.state.ClientState
import edu.software.craftsmanship.blueprint.at.client.types.TestClient
import edu.software.craftsmanship.blueprint.at.common.context.Id
import edu.software.craftsmanship.blueprint.at.common.context.IdList
import edu.software.craftsmanship.blueprint.at.common.context.TestContext.at
import edu.software.craftsmanship.blueprint.at.common.types.toIdList
import io.cucumber.java.en.Then
import io.cucumber.java8.En
import org.assertj.core.api.Assertions.assertThat

class ClientAssertionSteps(private val state: ClientState) : En {

    @Then("the set of existing clients is unchanged")
    fun existingClientsUnchanged() {
        val existingOnes = state.getState()
        val cachedOnes = at<Map<String, TestClient>>("existing-clients").values
        assertThat(existingOnes).containsExactlyInAnyOrderElementsOf(cachedOnes)
    }

    @Then("the result set of existing clients is {word}")
    fun resultingExistingClientIdentifiersAre(ids: IdList) {
        val expectedOnes = ids.toIdList()
        val returnedOnes = at<List<TestClient>>("response.body")
        assertThat(returnedOnes.map { it.id }).containsExactlyInAnyOrderElementsOf(expectedOnes)
    }

    @Then("the attributes of the returned client are the following")
    fun resultingClientAttributesAre(attributes: List<TestClient>) {
        assertThat(at<TestClient>("response.body")).isEqualTo(attributes.first())
    }

    @Then("the attributes of the existing client {word} are the following")
    fun resultingAttributesOfExistingClientAre(id: Id, attributes: List<TestClient>) {
        val existingOnes = state.getState().associateBy { it.id }
        val existingOne = existingOnes.getValue(id)
        assertThat(existingOne).isEqualTo(attributes.first())
    }

    @Then("the attributes of the existing client {word} are unchanged")
    fun clientAttributesAreUnchanged(id: Id) {
        val existingOnes = state.getState().associateBy { it.id }
        val existingOne = existingOnes.getValue(id)
        val cachedOne = at<Map<String, TestClient>>("existing-clients")[id]
        assertThat(existingOne).isEqualTo(cachedOne)
    }

    @Then("the returned client is added to the set of existing clients")
    fun clientAddedToExistingOnes() {
        val existingOnes = state.getState()
        val returnedOne = at<TestClient>("response.body")
        assertThat(existingOnes).contains(returnedOne)
    }

    @Then("the client {word} is removed from the set of existing clients")
    fun clientRemovedFromExistingOnes(id: Id) {
        val existingOnes = state.getState().map { it.id }
        assertThat(existingOnes).doesNotContain(id)
    }
}
