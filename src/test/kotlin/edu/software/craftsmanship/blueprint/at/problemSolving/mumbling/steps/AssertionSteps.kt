package edu.software.craftsmanship.blueprint.at.problemSolving.mumbling.steps

import edu.software.craftsmanship.blueprint.at.common.context.TestContext
import io.cucumber.java.en.Then
import io.cucumber.java8.En
import org.assertj.core.api.Assertions.assertThat

class AssertionSteps: En {

    @Then("the result of mumbling is {word}")
    fun mumblingResultIs(expected: String) {
        val result: String = TestContext.at("mumbling.result")
        assertThat(result).isEqualTo(expected)
    }
}
