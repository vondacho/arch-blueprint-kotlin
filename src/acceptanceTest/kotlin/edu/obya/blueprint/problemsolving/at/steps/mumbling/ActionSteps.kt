package edu.obya.blueprint.problemsolving.at.steps.mumbling

import edu.obya.blueprint.common.at.context.TestContext
import edu.obya.blueprint.problemsolving.domain.MumblingStrategy
import io.cucumber.java.en.When

class ActionSteps {

    @When("mumbling {word}")
    fun mumble(text: String) {
        TestContext.put("mumbling.result", MumblingStrategy().mumble(text))
    }
}
