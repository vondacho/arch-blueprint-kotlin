package edu.obya.blueprint.problemsolving.at.steps.mumbling

import edu.obya.blueprint.common.at.context.TestContext.at
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.assertThat

class AssertionSteps {

    @Then("the result of mumbling is {word}")
    fun mumblingResultIs(expected: String) {
        val result: String = at("mumbling.result")
        assertThat(result).isEqualTo(expected)
    }
}
