package edu.software.craftsmanship.blueprint.at.problemSolving.splitString.steps

import edu.software.craftsmanship.blueprint.at.common.context.TestContext
import io.cucumber.java.en.Then
import io.cucumber.java8.En
import org.assertj.core.api.Assertions.assertThat

class AssertionSteps: En {

    @Then("the result of splitting is {word}")
    fun splittingResultIs(expected: String) {
        val expectedItems = expected.split(",")
        val result: Array<String> = TestContext.at("splitting.result")
        assertThat(result).containsExactlyElementsOf(expectedItems)
    }
}
