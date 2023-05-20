package edu.obya.blueprint.problemsolving.at.steps.splitString

import edu.obya.blueprint.common.at.context.TestContext.put
import edu.obya.blueprint.problemsolving.domain.SplitStringStrategy
import io.cucumber.java.en.When

class ActionSteps {

    @When("splitting {word} with an amplitude of {int}")
    fun splitTheString(text: String, amplitude: Int) {
        put("splitting.result", SplitStringStrategy().split(text, amplitude))
    }
}
