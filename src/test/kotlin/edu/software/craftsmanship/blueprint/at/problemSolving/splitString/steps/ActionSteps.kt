package edu.software.craftsmanship.blueprint.at.problemSolving.splitString.steps

import edu.software.craftsmanship.blueprint.at.common.context.TestContext
import edu.software.craftsmanship.blueprint.domain.problemSolving.strategy.SplitStringStrategy
import io.cucumber.java.en.When
import io.cucumber.java8.En

class ActionSteps: En {

    @When("splitting {word} with an amplitude of {int}")
    fun splitTheString(text: String, amplitude: Int) {
        TestContext.put("splitting.result", SplitStringStrategy().split(text, amplitude))
    }
}
