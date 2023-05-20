package edu.obya.blueprint.problemsolving.at.steps.splitString

import edu.obya.blueprint.common.at.context.TestContext.at
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.assertThat

class AssertionSteps {

    @Then("the result of splitting is {word}")
    fun splittingResultIs(expected: String) {
        val expectedItems = expected.split(",")
        val result: Array<String> = at("splitting.result")
        assertThat(result).containsExactlyElementsOf(expectedItems)
    }
}
